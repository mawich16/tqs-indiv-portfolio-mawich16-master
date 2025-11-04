package ua;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.DisplayName;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import java.util.Optional;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.beans.HasPropertyWithValue.hasProperty;
import static org.hamcrest.Matchers.equalTo;

@ExtendWith(MockitoExtension.class)
class ProductFinderTest {

    @InjectMocks
    private ProductFindService productFinder;
    @Mock
    private ISimpleHttpClient httpClient;

    @DisplayName("testFindProductsValidProduct")
    @Test
    void testFindProductsValidProduct() {
        
        String fakeJson = """
        {
        "id": 3,
        "title": "Mens Cotton Jacket",
        "price": 55.99,
        "description": "great jacket",
        "category": "men's clothing",
        "image": "http://example.com"
        }
        """;

        when(httpClient.doHttpGet("https://fakestoreapi.com/products/3"))
            .thenReturn(fakeJson);

        Optional<Product> result = productFinder.findProductDetails(3);

        assertEquals(true, result.isPresent());
        assertEquals(3, result.get().getId());
        assertEquals("Mens Cotton Jacket", result.get().getTitle());
    }

    @DisplayName("testFindProductsNonValidProduct")
    @Test
    void testFindProductsNonValidProduct() {
        
        when(httpClient.doHttpGet("https://fakestoreapi.com/products/300"))
            .thenReturn("{}");

        Optional<Product> result = productFinder.findProductDetails(300);

        assertEquals(true, result.isEmpty());
    }

}
