package com.whereisagift.wish.product.steam;


import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.whereisagift.wish.price.Price;
import com.whereisagift.wish.product.Product;
import com.whereisagift.wish.product.ProductParser;
import com.whereisagift.wish.product.ProductSource;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.math.BigDecimal;
import java.net.URI;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
@RequiredArgsConstructor
public class SteamParser implements ProductParser {
    private static final String BASE_API_URL = "https://store.steampowered.com/api/appdetails";
    private static final Pattern APP_ID_PATTERN = Pattern.compile("/app/(\\d+)(?:/|$)");
    private static final String DEFAULT_CURRENCY = "RUB";
    private static final String LOCALE_PARAM = "cc";

    private final RestTemplate restTemplate = new RestTemplateBuilder()
            .defaultHeader(HttpHeaders.USER_AGENT, "Mozilla/5.0")
            .build();
    private final ObjectMapper objectMapper;

    @Override
    public boolean supports(String url) {
        return APP_ID_PATTERN.matcher(url).find();
    }

    @Override
    public Product parse(String url) {
        String appId = extractAppId(url)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Cannot extract Steam app ID from URL: " + url));

        JsonNode data = fetchAppDetails(appId);
        JsonNode info = data.path("data");

        String name = info.path("name").asText();
        String description = info.path("short_description").asText(null);
        String imageUrl = info.path("header_image").asText(null);

        Optional<Price> optionalPrice = parsePrice(info);

        Product.ProductBuilder builder = Product.builder()
                .name(name)
                .type(ProductSource.Steam)
                .link(url)
                .img(imageUrl)
                .description(description);

        optionalPrice.ifPresent(builder::price);

        return builder.build();
    }

    private Optional<String> extractAppId(String url) {
        Matcher matcher = APP_ID_PATTERN.matcher(url);
        return matcher.find() ? Optional.of(matcher.group(1)) : Optional.empty();
    }

    private JsonNode fetchAppDetails(String appId) {
        URI uri = UriComponentsBuilder.fromUriString(BASE_API_URL)
                .queryParam("appids", appId)
                .queryParam(LOCALE_PARAM, "ru")
                .build()
                .toUri();

        String response = restTemplate.getForObject(uri, String.class);
        try {
            JsonNode root = objectMapper.readTree(response);
            JsonNode appNode = root.path(appId);
            if (!appNode.path("success").asBoolean(false)) {
                throw new IllegalStateException(
                        "Steam API returned success=false for appId=" + appId);
            }
            return appNode;
        } catch (Exception ex) {
            throw new IllegalArgumentException(
                    "Failed to fetch or parse Steam API response for appId=" + appId, ex);
        }
    }

    private Optional<Price> parsePrice(JsonNode info) {
        JsonNode priceNode = info.path("price_overview");
        if (priceNode.isMissingNode()) {
            return Optional.empty(); // no price available
        }

        BigDecimal finalPrice = BigDecimal.valueOf(
                priceNode.path("final").asLong(), 2
        );
        String currency = priceNode.path("currency").asText(DEFAULT_CURRENCY);

        return Optional.of(new Price(currency, finalPrice));
    }
}