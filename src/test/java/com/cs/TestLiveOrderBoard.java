package com.cs;

import static com.cs.CoinType.BITCOIN;
import static com.cs.CoinType.ETHEREUM;
import static com.cs.CoinType.LITECOIN;
import static com.cs.OrderType.BUY;
import static com.cs.OrderType.SELL;
import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class TestLiveOrderBoard {

  private static final Price A_PRICE = Price.price(BigDecimal.valueOf(304));
  private static final Price LOW_PRICE = Price.price(BigDecimal.valueOf(50));
  private static final Price HIGH_PRICE = Price.price(BigDecimal.valueOf(150));
  private static final Price LOW_BUY_PRICE = Price.price(BigDecimal.valueOf(100));
  private static final Price HIGH_BUY_PRICE = Price.price(BigDecimal.valueOf(200));
  private static final Price HIGH_SELL_PRICE = Price.price(BigDecimal.valueOf(400));
  private static final Price LOW_SELL_PRICE = Price.price(BigDecimal.valueOf(300));
  private static final String ANY_USER = "user1";
  private static final Double ANY_QUANTITY = 5.0;
  private static final CoinType ANY_COIN = CoinType.BITCOIN;
  private static final Price ANY_PRICE = Price.price(BigDecimal.valueOf(303));

  private LiveOrderBoard orderBoard;
  private List<Summary> orderSummary;

  @BeforeEach
  public void createSUT() {
    orderBoard = new LiveOrderBoard();
  }

  @Test
  public void
  boardEmpty_WhenNoOrders() {
    assertThat(orderBoard.summary()).isEmpty();
  }

  @Test
  public void
  boardContainsOneOrder_WhenSingleBuyOrderRegistered() {
    registerOrderWith(ANY_PRICE, BUY);

    orderSummary = orderBoard.summary();

    assertThat(orderSummary.size()).isOne();
    assertOrderSummaryContains(ANY_QUANTITY, ANY_PRICE, BUY);
  }

  @Test
  public void
  boardContainsTwoOrders_WhenTwoDifferentOrdersAreRegistered() {
    registerOrderWith(ANY_QUANTITY, ANY_PRICE, BUY);
    registerOrderWith(ANY_QUANTITY, A_PRICE, SELL);
    orderSummary = orderBoard.summary();
    assertThat(orderSummary.size()).isEqualTo(2);
    assertOrderSummary(0, ANY_QUANTITY, ANY_PRICE);
    assertOrderSummary(1, ANY_QUANTITY, A_PRICE);
    assertOrderSummaryContains(ANY_QUANTITY, ANY_PRICE, BUY);
    assertOrderSummaryContains(ANY_QUANTITY, A_PRICE, SELL);
  }

  @Test
  public void
  boardContainsNoOrders_WhenOrderIsRegisteredAndThenCancelled() {
    registerOrderWith(ANY_PRICE, BUY);
    orderBoard.cancel(ANY_USER, ANY_COIN, ANY_QUANTITY, ANY_PRICE, BUY);
    assertThat(orderBoard.summary()).isEmpty();
  }

  @Test
  public void
  boardContainsOneOrder_WhenTwoDifferentOrdersRegisteredAndThenOneCancelled() {
    registerOrderWith(ANY_PRICE, BUY);
    registerOrderWith(A_PRICE, SELL);
    orderSummary = orderBoard.summary();
    assertThat(orderSummary.size()).isEqualTo(2);

    orderBoard.cancel(ANY_USER, ANY_COIN, ANY_QUANTITY, A_PRICE, SELL);

    orderSummary = orderBoard.summary();

    assertThat(orderSummary.size()).isOne();
    assertOrderSummaryContains(ANY_QUANTITY, ANY_PRICE, BUY);
  }

  @Test
  public void
  boardContainsNoOrder_WhenTwoDifferentOrdersAreRegisteredAndThenBothCancelled() {
    registerOrderWith(ANY_QUANTITY, ANY_PRICE, SELL);
    registerOrderWith(ANY_QUANTITY, A_PRICE, SELL);
    orderSummary = orderBoard.summary();
    assertThat(orderSummary.size()).isEqualTo(2);
    orderBoard.cancel(ANY_USER, ANY_COIN, ANY_QUANTITY, ANY_PRICE, SELL);
    orderBoard.cancel(ANY_USER, ANY_COIN, ANY_QUANTITY, A_PRICE, SELL);

    assertThat(orderBoard.summary().size()).isZero();
  }

  @Test
  public void
  boardShowsLowestPriceOrderFirst_WhenTwoSellOrdersAreRegistered() {
    registerOrderWith(HIGH_PRICE, SELL);
    registerOrderWith(LOW_PRICE, SELL);

    orderSummary = orderBoard.summary();

    assertOrderSummary(0, ANY_QUANTITY, LOW_PRICE);
    assertOrderSummary(1, ANY_QUANTITY, HIGH_PRICE);
  }

  @Test
  public void
  boardShowsHighestPriceOrderFirst_WhenTwoBuyOrdersAreRegistered() {
    registerOrderWith(LOW_PRICE, BUY);
    registerOrderWith(HIGH_PRICE, BUY);

    orderSummary = orderBoard.summary();

    assertOrderSummary(0, ANY_QUANTITY, HIGH_PRICE);
    assertOrderSummary(1, ANY_QUANTITY, LOW_PRICE);
  }

  @Test
  public void
  boardShowsOrdersInSequence_WhenBuyAndSellOrdersWithDifferentPriceAreRegistered() {
    registerOrderWith(LOW_BUY_PRICE, BUY);
    registerOrderWith(HIGH_BUY_PRICE, BUY);
    registerOrderWith(HIGH_SELL_PRICE, SELL);
    registerOrderWith(LOW_SELL_PRICE, SELL);

    orderSummary = orderBoard.summary();

    assertThat(orderBoard.summary().size()).isEqualTo(4);
    assertThat(positionInBoardOfOrderWith(ANY_COIN, ANY_QUANTITY, HIGH_BUY_PRICE, BUY))
        .isLessThan(positionInBoardOfOrderWith(ANY_COIN, ANY_QUANTITY, LOW_BUY_PRICE, BUY));
    assertThat(positionInBoardOfOrderWith(ANY_COIN, ANY_QUANTITY, LOW_SELL_PRICE, SELL))
        .isLessThan(positionInBoardOfOrderWith(ANY_COIN, ANY_QUANTITY, HIGH_SELL_PRICE, SELL));
  }

  @Test
  public void
  boardShowsOrdersInSequence_WhenBuyAndSellOrdersXWithDifferentPriceAreRegistered() {
    registerOrderWith(LOW_BUY_PRICE, BUY);
    registerOrderWith(HIGH_SELL_PRICE, SELL);
    registerOrderWith(HIGH_BUY_PRICE, BUY);
    registerOrderWith(LOW_SELL_PRICE, SELL);

    orderSummary = orderBoard.summary();

    assertThat(orderBoard.summary().size()).isEqualTo(4);
    assertThat(positionInBoardOfOrderWith(ANY_COIN, ANY_QUANTITY, HIGH_BUY_PRICE, BUY))
        .isLessThan(positionInBoardOfOrderWith(ANY_COIN, ANY_QUANTITY, LOW_BUY_PRICE, BUY));
    assertThat(positionInBoardOfOrderWith(ANY_COIN, ANY_QUANTITY, LOW_SELL_PRICE, SELL))
        .isLessThan(positionInBoardOfOrderWith(ANY_COIN, ANY_QUANTITY, HIGH_SELL_PRICE, SELL));
  }

  @Test
  public void
  boardShowsAggregatedQuantity_WhenTwoBuyOrdersWithSamePriceAreRegistered() {
    registerOrderWith(LOW_PRICE, BUY);
    registerOrderWith(LOW_PRICE, BUY);

    orderSummary = orderBoard.summary();

    assertThat(orderBoard.summary().size()).isOne();
    assertOrderSummaryContains(ANY_QUANTITY + ANY_QUANTITY, LOW_PRICE, BUY);
  }

  @Test
  public void
  boardShowsAggregatedQuantity_WhenMultipleBuyOrdersWithSomeAtSamePriceAreRegistered() {
    registerOrderWith(5.0, LOW_PRICE, BUY);
    registerOrderWith(3.5, LOW_PRICE, BUY);
    registerOrderWith(10.0, HIGH_PRICE, BUY);

    orderSummary = orderBoard.summary();

    assertThat(orderSummary.size()).isEqualTo(2);
    assertOrderSummaryContains(10.0, HIGH_PRICE, BUY);
    assertOrderSummaryContains(8.5, LOW_PRICE, BUY);
  }

  @Test
  public void
  boardShowsAggregatedQuantity_WhenMultipleSellOrdersWithSomeAtSamePriceAreRegistered() {
    registerOrderWith(4.0, HIGH_PRICE, SELL);
    registerOrderWith(3.5, HIGH_PRICE, SELL);
    registerOrderWith(7.0, LOW_PRICE, SELL);

    orderSummary = orderBoard.summary();

    assertThat(orderSummary.size()).isEqualTo(2);
    assertOrderSummaryContains(7.0, LOW_PRICE, SELL);
    assertOrderSummaryContains(7.5, HIGH_PRICE, SELL);
  }

  @Test
  public void
  boardShowsAggregatedQuantity_WhenMultipleDifferentTypesOfOrderAreRegistered() {
    registerOrderWith(4.0, HIGH_PRICE, SELL);
    registerOrderWith(3.5, HIGH_PRICE, SELL);
    registerOrderWith(5.0, LOW_PRICE, BUY);
    registerOrderWith(3.5, LOW_PRICE, BUY);

    orderSummary = orderBoard.summary();

    assertThat(orderSummary.size()).isEqualTo(2);
    assertOrderSummaryContains(7.5, HIGH_PRICE, SELL);
    assertOrderSummaryContains(8.5, LOW_PRICE, BUY);
  }

  @Test
  public void
  boardShowsAggregatedQuantity_WhenMultipleDifferentTypesOfOrderForDifferentCoinsForTheSamePriceAreRegistered() {
    registerOrderWith(LITECOIN, 4.0, HIGH_PRICE, BUY);
    registerOrderWith(ETHEREUM, 3.5, HIGH_PRICE, BUY);

    orderSummary = orderBoard.summary();

    assertThat(orderSummary.size()).isEqualTo(2);
    assertOrderSummaryContains(LITECOIN, 4.0, HIGH_PRICE, BUY);
    assertOrderSummaryContains(ETHEREUM, 3.5, HIGH_PRICE, BUY);
  }

  @Test
  public void
  boardShowsAggregatedQuantity_WhenMultipleDifferentTypesOfOrderForDifferentCoinsForTheDifferentPriceAreRegistered() {
    registerOrderWith(LITECOIN, 4.0, HIGH_BUY_PRICE, BUY);
    registerOrderWith(LITECOIN, 5.0, HIGH_BUY_PRICE, BUY);
    registerOrderWith(LITECOIN, 3.0, LOW_BUY_PRICE, BUY);
    registerOrderWith(BITCOIN, 4.0, HIGH_PRICE, BUY);
    registerOrderWith(BITCOIN, 5.0, HIGH_PRICE, BUY);
    registerOrderWith(BITCOIN, 3.0, LOW_BUY_PRICE, BUY);
    registerOrderWith(ETHEREUM, 3.5, HIGH_PRICE, BUY);
    registerOrderWith(ETHEREUM, 3.5, LOW_SELL_PRICE, SELL);
    registerOrderWith(LITECOIN, 7.5, HIGH_SELL_PRICE, SELL);

    orderSummary = orderBoard.summary();

    assertThat(orderSummary.size()).isEqualTo(7);
    assertThat(positionInBoardOfOrderWith(LITECOIN, 9.0, HIGH_BUY_PRICE, BUY))
        .isLessThan(positionInBoardOfOrderWith(ETHEREUM, 3.5, HIGH_PRICE, BUY))
        .isLessThan(positionInBoardOfOrderWith(BITCOIN, 9.0, HIGH_PRICE, BUY))
        .isLessThan(positionInBoardOfOrderWith(LITECOIN, 3.0, LOW_BUY_PRICE, BUY))
        .isLessThan(positionInBoardOfOrderWith(BITCOIN, 3.0, LOW_BUY_PRICE, BUY))
        .isLessThan(positionInBoardOfOrderWith(ETHEREUM, 3.5, LOW_SELL_PRICE, SELL))
        .isLessThan(positionInBoardOfOrderWith(LITECOIN, 7.5, HIGH_SELL_PRICE, SELL));
  }

  private int positionInBoardOfOrderWith(CoinType coinType, Double quantity, Price price,
      OrderType orderType) {
    return orderSummary.indexOf(new Summary(coinType, quantity, price, orderType));
  }

  private void assertOrderSummaryContains(CoinType coinType, final Double quantity,
      final Price price, final OrderType orderType) {
    assertThat(orderSummary).contains(new Summary(coinType, quantity, price, orderType));
  }

  private void assertOrderSummaryContains(final Double quantity, final Price price,
      final OrderType orderType) {
    assertThat(orderSummary).contains(new Summary(ANY_COIN, quantity, price, orderType));
  }

  private void assertOrderSummary(final int position, final Double quantity, final Price price) {
    assertThat(orderSummary.get(position).getQuantity()).isEqualTo(quantity);
    assertThat(orderSummary.get(position).getPrice()).isEqualTo(price);
  }

  private void registerOrderWith(Price price, OrderType orderType) {
    orderBoard.register(ANY_USER, ANY_COIN, ANY_QUANTITY, price, orderType);
  }

  private void registerOrderWith(Double quantity, Price price, OrderType orderType) {
    orderBoard.register(ANY_USER, ANY_COIN, quantity, price, orderType);
  }

  private void registerOrderWith(CoinType coinType, Double quantity, Price price,
      OrderType orderType) {
    orderBoard.register(ANY_USER, coinType, quantity, price, orderType);
  }

}
