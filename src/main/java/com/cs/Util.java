package com.cs;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class Util {

  static public List<Summary> transformAggregateToSummaryList(
      Map<CoinType, Map<OrderType, Map<Price, Double>>> aggregation) {
    List<Summary> result = new ArrayList<>();

    final Iterator<Entry<CoinType, Map<OrderType, Map<Price, Double>>>> coinTypeIterator = aggregation
        .entrySet().iterator();
    while (coinTypeIterator.hasNext()) {
      Entry<CoinType, Map<OrderType, Map<Price, Double>>> coinTypeEntry = coinTypeIterator
          .next();
      CoinType coinType = coinTypeEntry.getKey();

      Iterator<Entry<OrderType, Map<Price, Double>>> orderTypeIterator = coinTypeEntry
          .getValue()
          .entrySet().iterator();
      while (orderTypeIterator.hasNext()) {
        Entry<OrderType, Map<Price, Double>> orderTypeEntry = orderTypeIterator.next();
        OrderType orderType = orderTypeEntry.getKey();
        Map<Price, Double> priceAggregateQuantityMap = orderTypeEntry.getValue();

        priceAggregateQuantityMap.entrySet().stream().forEach(entry -> result
            .add(new Summary(coinType, entry.getValue(), entry.getKey(), orderType)));
      }
    }
    return result;
  }
}
