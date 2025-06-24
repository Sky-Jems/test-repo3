package solutions.skydev.pos.product_service.service;
import solutions.skydev.pos.product_service.model.entity.Product;

import java.util.List;


public interface ProductService {
    Product save(Product product);
    Product findById(Long id);
    Product deleteById(Long id);
    Product update(Product product);
    List<Product> findAll();
}
