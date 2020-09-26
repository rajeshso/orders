# Interview Solution

The program is written in Java 11. The program uses gradle 6, JUnit5, an annotation processor called lombok and AssertJ
If you are using intelliJ, please enable annotation processors. The ide will remind to enable. Please accept the prompt.

Assumptions:

The Live Order Board has one more grouping called coin type.
The instructions mentioned multiple coin types, but the example contained only Ethereum.
As it is not possible to combine an order of Ethereum with Litecoin, even if the quantity and price are same, I differentiated the orders.
The summary now specifies the coin type in addition to the rest of the requirements.

- 35 3.6 of Ethereum for £13. 6 // order a + order d
- 441. 8 of Ethereum for £13.9 // order c
- 50.5 of Ethereum for £ 14 // order b
- 20.8 of Litecoin for £10.9 // order e
- 5.5 of Bitcoin for £ 5 // order f

The Util uses an external iterator. I could not use a stream to make the complex transformation. 
So, I extracted it away from the rest of the code to articulate the instructions better.

Some features such as concurrency, UI Observers are not implemented to keep the code focused on the central theme.
It is possible to extend new features.



# Interview exercise

You are working for Crypto Inc. and we would like you to create a program to show the top 10 BUY or
SELL orders in the Crypto Inc. marketplace.

The Crypto Inc. 'Live Order Board' should support these features:

1) Placing an order. An order can be either a BUY or a SELL and should capture

- user id
- coin type (Litecoin, Ethereum.. etc)
- order quantity (how many coins)
- price per coin (e.g.: £ 125 )

2) Cancel a registered order - this will remove the order from 'Live Order Board'

3) Get summary information of live orders (see explanation below)
Imagine we have received the following orders:

- a) SELL: 350.1 Ethereum @ £13.6 [user1]
- b) SELL: 50. 5 for £ 14 [user2]
- c) SELL: 441. 8 for £13.9 [user3]
- d) SELL: 3. 5 for £13.6 [user4]

Our ‘Live Order Board’ should provide us the following summary information:

- 35 3.6 for £13. 6 // order a + order d
- 441. 8 for £13.9 // order c
- 50.5 for £ 14 // order b

The first thing to note here is that orders for the same price should be merged together (even when they
are from different users). In this case it can be seen that order a) and d) were for the same price (£13.6)
and this is why only their sum ( 353. 6 ) is displayed (for £13.6) and not the individual orders (350.1 and
3.5).The last thing to note is that for SELL orders the orders with lowest prices are displayed first.
Opposite is true for the BUY orders.

Please provide the implementation of the live order board which will be packaged and shipped as a library
to be used by the UI team. No database or UI/WEB is needed for this assignment (we're absolutely fine
with in memory solution). The only important thing is that you just write it according to your normal
standards.

NOTE: if during your implementation you'll find that something could be designed in multiple different
ways, just implement the one which seems most reasonable to you and if you could provide a short (once
sentence) reasoning why you choose this way and not another one, it would be great.


