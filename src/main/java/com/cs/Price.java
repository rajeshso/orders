package com.cs;

import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@AllArgsConstructor
@EqualsAndHashCode
@ToString
public class Price implements Comparable<Price> {

  private final BigDecimal amount;

  static Price price(final BigDecimal amount) {
    return new Price(amount);
  }

  @Override
  public int compareTo(Price o) {
    return amount.compareTo(o.amount);
  }
}
