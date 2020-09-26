package com.cs;

import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@EqualsAndHashCode
@ToString
@Getter
@AllArgsConstructor
public class Summary  {

  private final CoinType coinType;
  private final Double quantity;
  private final Price price;
  private final OrderType orderType;

}
