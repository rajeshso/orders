package com.cs;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@AllArgsConstructor
@Getter
@ToString
@EqualsAndHashCode
public class Order {

  private final String userId;
  private final CoinType coinType;
  private final Double quantity;
  private final Price price;
  private final OrderType orderType;

}
