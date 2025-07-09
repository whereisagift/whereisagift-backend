package com.whereisagift.wish.product;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final List<ProductParser> parsers;

    public Product parse(String url) {
        ProductParser parser = parsers.stream()
                .filter(p -> p.supports(url))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("No parser available for URL: " + url));

        try {
            return parser.parse(url);
        } catch (Exception e) {
            throw new IllegalArgumentException("Error while parsing product from URL: " + url, e);
        }
    }

    public ProductSource detectSource(String url) {
        return parsers.stream()
                .filter(p -> p.supports(url))
                .findFirst()
                .map(ProductParser::getSource)
                .orElse(ProductSource.Manual);
    }
}
