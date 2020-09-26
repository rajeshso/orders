package com.cs;

import static java.util.Comparator.comparing;
import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.reducing;
import static java.util.stream.Collectors.toList;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class LiveOrderBoard {

  public static final int MAX_RECORDS = 10;
  private static final Comparator<Summary> BY_TYPE = comparing(Summary::getOrderType);
  private static final Comparator<Summary> BY_PRICE = (summary, summaryToCompareWith) -> summary
      .getOrderType().comparePrice(summary.getPrice(), summaryToCompareWith.getPrice());
  private List<Order> orders = new ArrayList<>();

  public List<Summary> summary() {

    final Map<CoinType, Map<OrderType, Map<Price, Double>>> aggregation = orders.stream()
        .collect(groupingBy(Order::getCoinType,
            groupingBy(Order::getOrderType, groupingBy(Order::getPrice, reducing(0.0
                , Order::getQuantity
                , Double::sum)))));

    return Util.transformAggregateToSummaryList(aggregation).stream()
        .sorted(BY_TYPE.thenComparing(BY_PRICE)).limit(
            MAX_RECORDS).collect(toList());

  }

  public void register(final String user, CoinType coinType, final Double quantity,
      final Price price, final OrderType orderType) {
    orders.add(new Order(user, coinType, quantity, price, orderType));
  }

  public void cancel(final String user, CoinType coinType, final Double quantity, final Price price,
      final OrderType orderType) {
    orders.remove(new Order(user, coinType, quantity, price, orderType));
  }

}
