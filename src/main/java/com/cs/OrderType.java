package com.cs;

import java.util.Comparator;

public enum OrderType {

  BUY {
    @Override
    public int comparePrice(Price priceToCompare, Price priceToCompareWith) {
      return HIGH_PRICE.compare(priceToCompare, priceToCompareWith);
    }

  },
  SELL {
    @Override
    public int comparePrice(Price priceToCompare, Price priceToCompareWith) {
      return LOW_PRICE.compare(priceToCompare, priceToCompareWith);
    }

  };

  private static final Comparator<Price> LOW_PRICE = Price::compareTo;
  private static final Comparator<Price> HIGH_PRICE = LOW_PRICE.reversed();


  public abstract int comparePrice(Price priceToCompare, Price priceToCompareWith);


}
