package ua;

import java.util.ArrayList;
import java.util.List;
import lombok.Getter;

class StocksPortfolio {

    @Getter
    private IStockmarketService stockmarket;
    private List<Stock> stocks = new ArrayList<>();

    public StocksPortfolio(IStockmarketService sm) {
        this.stockmarket = sm;
    }

    public void addStock(Stock s){
        this.stocks.add(s);
    }

    public double totalValue() {
        double total = 0.0;
        for (Stock stock : this.stocks) {
            total += this.stockmarket.lookUpPrice(stock.getLabel()) * stock.getQuantity();
        }
        return total;
    }

    public List<Stock> mostValuableStocks(int topN) {
        return this.stocks.stream()
                .map(stock -> new Object[] {
                    stock,
                    stockmarket.lookUpPrice(stock.getLabel()) * stock.getQuantity()
                })
                .sorted((a, b) -> Double.compare((double) b[1], (double) a[1]))
                .limit(topN)
                .map(a -> (Stock) a[0])
                .toList();
    }
    
}
