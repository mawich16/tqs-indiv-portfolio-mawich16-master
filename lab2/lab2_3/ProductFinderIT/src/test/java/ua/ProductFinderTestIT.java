package ua;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Optional;

class ProductFinderTestIT {

    private ISimpleHttpClient httpClient = new BasicHttpClient();
    private ProductFindService productFinder = new ProductFindService(httpClient);

    @DisplayName("testFindProductsValidProduct")
    @Test
    void testFindProductsValidProduct() {

        Optional<Product> result = productFinder.findProductDetails(3);

        assertTrue(result.isPresent(), "Product should be found");
        assertEquals(3, result.get().getId());
        assertEquals("Mens Cotton Jacket", result.get().getTitle());
    }

    @DisplayName("testFindProductsNonValidProduct")
    @Test
    void testFindProductsNonValidProduct() {

        Optional<Product> result = productFinder.findProductDetails(300);

        assertTrue(result.isEmpty(), "Product should not be found for invalid ID");
    }

}
