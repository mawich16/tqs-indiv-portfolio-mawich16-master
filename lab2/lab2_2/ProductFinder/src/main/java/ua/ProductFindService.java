package ua;

import java.util.Optional;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;

class ProductFindService {

    @Getter
    private ISimpleHttpClient httpClient;
    private static final String API_PRODUCTS = "https://fakestoreapi.com/products/";
    private ObjectMapper objectMapper;

    public ProductFindService(ISimpleHttpClient client) {
        this.httpClient = client;
        this.objectMapper = new ObjectMapper();
    }

    public Optional<Product> findProductDetails(Integer id){
        try {
            String url = API_PRODUCTS + id;
            String jsonResponse = httpClient.doHttpGet(url);

            if (jsonResponse == null || jsonResponse.trim().equals("{}") || jsonResponse.isBlank()) {
                return Optional.empty();
            }

            Product product = objectMapper.readValue(jsonResponse, Product.class);
            
            return Optional.ofNullable(product);

        } catch (Exception e) {
            return Optional.empty();
        }
    }

}
