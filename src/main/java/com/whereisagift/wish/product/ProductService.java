package com.whereisagift.wish.product;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProductService {
  private final List<ProductParser> parsers;

  public Product parse(String url) {
    ProductParser parser =
        parsers.stream()
            .filter(p -> p.supports(url))
            .findFirst()
            .orElseThrow(() -> new IllegalArgumentException("No parser available for URL: " + url));

    try {
      return parser.parse(url);
    } catch (Exception e) {
      throw new IllegalArgumentException("Error while parsing product from URL: " + url, e);
    }
  }

  public ProductSource detectSource(@Nullable String url) {
    return parsers.stream()
        .filter(p -> p.supports(url))
        .findFirst()
        .map(ProductParser::getSource)
        .orElse(ProductSource.Manual);
  }
}
