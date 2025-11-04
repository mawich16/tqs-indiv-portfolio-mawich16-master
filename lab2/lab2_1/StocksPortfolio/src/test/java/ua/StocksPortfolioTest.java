package ua;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.beans.HasPropertyWithValue.hasProperty;
import static org.hamcrest.Matchers.equalTo;

@ExtendWith(MockitoExtension.class)
class StocksPortfolioTest {

    @InjectMocks
    private StocksPortfolio stocksPortfolio;
    @Mock
    private IStockmarketService service;

    @Test
    void getTotalValueTest() {
        stocksPortfolio.addStock(new Stock("AMZ", 5));
        stocksPortfolio.addStock(new Stock("EBAY", 5));

        when(service.lookUpPrice("AMZ")).thenReturn(10.0);
        when(service.lookUpPrice("EBAY")).thenReturn(10.0);

        assertEquals(100.0, stocksPortfolio.totalValue());
        verify(service, times(1)).lookUpPrice("AMZ");
        verify(service, times(1)).lookUpPrice("EBAY");
    }

    @Test
    void getTotalValueWithExtraExpectationsTest() {
        stocksPortfolio.addStock(new Stock("AMZ", 5));
        stocksPortfolio.addStock(new Stock("EBAY", 5));
        
        when(service.lookUpPrice("AMZ")).thenReturn(10.0);
        when(service.lookUpPrice("EBAY")).thenReturn(10.0);

        // when(service.lookUpPrice("AAPL")).thenReturn(150.0);
        // when(service.lookUpPrice("GOOG")).thenReturn(2800.0);
        // when(service.lookUpPrice("TSLA")).thenReturn(700.0);

        assertEquals(100.0, stocksPortfolio.totalValue()); 
        verify(service, times(1)).lookUpPrice("AMZ");
        verify(service, times(1)).lookUpPrice("EBAY");
    }

    @Test
    void checkStockHasAllPropertiesTest() {
        Stock s = new Stock("AMZ", 5);
        assertThat(s, hasProperty("label", equalTo("AMZ")));
        assertThat(s, hasProperty("quantity", equalTo(5)));
    }

    @Test
    void mostValuableStocksReturnsTop3() {
        stocksPortfolio.addStock(new Stock("AMZ", 5));   
        stocksPortfolio.addStock(new Stock("EBAY", 10)); 
        stocksPortfolio.addStock(new Stock("AAPL", 2)); 
        stocksPortfolio.addStock(new Stock("GOOG", 1));  
        stocksPortfolio.addStock(new Stock("TSLA", 3));  

        when(service.lookUpPrice("AMZ")).thenReturn(10.0);
        when(service.lookUpPrice("EBAY")).thenReturn(7.0);
        when(service.lookUpPrice("AAPL")).thenReturn(100.0);
        when(service.lookUpPrice("GOOG")).thenReturn(150.0);
        when(service.lookUpPrice("TSLA")).thenReturn(40.0);

        var result = stocksPortfolio.mostValuableStocks(3);

        assertEquals(3, result.size());
        assertEquals("AAPL", result.get(0).getLabel()); 
        assertEquals("GOOG", result.get(1).getLabel()); 
        assertEquals("TSLA", result.get(2).getLabel()); 

        verify(service, atLeastOnce()).lookUpPrice(anyString());
    }

    @Test
    void mostValuableStocksWhenTopNExceedsPortfolioSize() {
        stocksPortfolio.addStock(new Stock("AMZ", 2));
        stocksPortfolio.addStock(new Stock("EBAY", 1)); 

        when(service.lookUpPrice("AMZ")).thenReturn(50.0);
        when(service.lookUpPrice("EBAY")).thenReturn(20.0);

        var result = stocksPortfolio.mostValuableStocks(5);

        assertEquals(2, result.size());
        assertEquals("AMZ", result.get(0).getLabel());
        assertEquals("EBAY", result.get(1).getLabel());
    }

    @Test
    void mostValuableStocksWithEmptyPortfolioReturnsEmptyList() {
        var result = stocksPortfolio.mostValuableStocks(3);
        assertEquals(0, result.size());
    }
}
