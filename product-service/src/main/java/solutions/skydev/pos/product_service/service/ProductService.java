package solutions.skydev.pos.product_service.service;
import solutions.skydev.pos.product_service.model.entity.Product;

import java.util.List;

public interface ProductService {
    @SuppressWarnings("UnusedReturnValue")
    Product save(Product product);
    Product findById(Long id);
    void deleteById(Long id);
    Product update(Long id, Product product);
    List<Product> findAll();
}
