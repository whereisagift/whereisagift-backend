package com.whereisagift.wish.product.wildberries;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.whereisagift.wish.price.Price;
import com.whereisagift.wish.product.Product;
import com.whereisagift.wish.product.ProductParser;
import com.whereisagift.wish.product.ProductSource;
import io.sentry.Sentry;
import java.math.BigDecimal;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
@RequiredArgsConstructor
public class WildberriesParser implements ProductParser {
  private static final Pattern ARTICLE_ID_PATTERN = Pattern.compile("/catalog/(\\d+)/");
  private static final String API_URL = "https://card.wb.ru/cards/detail?dest=-1257786&nm=%s";
  private static final int[] BASKET_THRESHOLDS = {
    143, 281, 429, 718, 1002, 1049, 1113, 1168, 1309, 1601,
    1651, 1918, 2045, 2189, 2405, 2621, 2830, 3046, 3267, 3485,
    3697, 3916, 4132, 4344, 4435
  };

  private final RestTemplate restTemplate =
      new RestTemplateBuilder().defaultHeader(HttpHeaders.USER_AGENT, "Mozilla/5.0").build();
  private final ObjectMapper objectMapper;

  @Override
  public boolean supports(String url) {
    return url.contains("wildberries.ru/catalog/");
  }

  @Override
  public ProductSource getSource() {
    return ProductSource.Wildberries;
  }

  @Override
  public Product parse(String url) {
    String articleId =
        extractArticleId(url)
            .orElseThrow(
                () -> new IllegalArgumentException("Cannot extract article id from URL: " + url));

    JsonNode prodNode = fetchProductNode(articleId, url);
    int id = prodNode.path("id").asInt();

    String name = prodNode.path("name").asText();
    String brand = prodNode.path("brand").asText();
    String desc = prodNode.path("description").asText(null);

    double finalPrice =
        calculateFinalPrice(prodNode.path("priceU").asInt(), prodNode.path("salePriceU").asInt());

    String imgUrl = safeResolveImageUrl(id);

    return Product.builder()
        .name(String.format("%s (%s)", name, brand))
        .type(getSource())
        .link(url)
        .img(imgUrl)
        .description(desc)
        .price(new Price("RUB", BigDecimal.valueOf(finalPrice)))
        .build();
  }

  private Optional<String> extractArticleId(String url) {
    Matcher matcher = ARTICLE_ID_PATTERN.matcher(url);
    return matcher.find() ? Optional.of(matcher.group(1)) : Optional.empty();
  }

  private JsonNode fetchProductNode(String articleId, String sourceUrl) {
    String apiUrl = String.format(API_URL, articleId);
    try {
      String body = restTemplate.getForObject(apiUrl, String.class);
      JsonNode root = objectMapper.readTree(body);
      return root.path("data").path("products").get(0);
    } catch (Exception e) {
      Sentry.captureException(e);
      throw new IllegalArgumentException("Invalid JSON from WB API for URL: " + sourceUrl, e);
    }
  }

  private double calculateFinalPrice(int priceU, int saleU) {
    int amount = saleU > 0 ? saleU : priceU;
    return amount / 100.0;
  }

  private String safeResolveImageUrl(int id) {
    try {
      return resolveImageUrl(id);
    } catch (Exception e) {
      Sentry.captureException(e);
      return null;
    }
  }

  private String resolveImageUrl(int id) {
    int vol = id / 100_000;
    int part = id / 1_000;
    int basket = resolveBasket(vol);

    return String.format(
        "https://basket-%02d.wbbasket.ru/vol%d/part%d/%d/images/big/1.webp", basket, vol, part, id);
  }

  private int resolveBasket(int vol) {
    for (int i = 0; i < BASKET_THRESHOLDS.length; i++) {
      if (vol <= BASKET_THRESHOLDS[i]) {
        return i + 1;
      }
    }
    throw new IllegalStateException("Cannot determine basket for volume=" + vol);
  }
}
