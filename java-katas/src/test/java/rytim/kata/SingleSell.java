package rytim.kata;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class SingleSell {

    public static int maxProfit(int...prices) {
        if(prices == null || prices.length == 0) {
            return -1;
        }

        int lowestPriceSeen = prices[0];
        int maxProfitSeen   = 0;

        for(int i=0; i < prices.length; i++) {
            int price = prices[i];
            if (price < lowestPriceSeen) {
                lowestPriceSeen = price;
            }
            if (price - lowestPriceSeen > maxProfitSeen) {
                maxProfitSeen = price - lowestPriceSeen;
            }
        }
        return maxProfitSeen;
    }

    public static class Tests {
        @Test
        public void empty() {
            assertEquals(-1, maxProfit());
            assertEquals(-1, maxProfit(null));
        }
        @Test
        public void one() {
            assertEquals(5-1, maxProfit(
                    1, 2, 3, 4, 5
            ));
        }
        @Test
        public void bear() {
            assertEquals(0, maxProfit(
                    5, 4, 3, 2, 1
            ));
        }
        @Test
        public void dividends() {
            assertEquals(3, maxProfit(
                    200, 201, 202, 203,
                    100, 101, 102, 103,
                    0,   1,   2,   3
            ));
        }
        @Test
        public void two() {
            assertEquals(5-2, maxProfit(
                    100, 2, 3, 4, 5, 1
            ));
        }
        @Test
        public void three() {
            assertEquals(33-2, maxProfit(
                    100,
                    20, 30, 40, 50,
                    2, 10, 20, 30, 33
            ));
        }
    }
}
