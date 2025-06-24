package solutions.skydev.pos.product_service.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import solutions.skydev.pos.product_service.model.entity.Product;
import solutions.skydev.pos.product_service.repository.ProductRepository;

import java.util.List;
import java.util.Optional;

@Service
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;


    @Autowired
    public ProductServiceImpl(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Transactional
    public Product save(Product product) {
        return productRepository.save(product);
    }

    public Product findById(Long id) {
        return productRepository.findById(id).orElse(null);
    }

    public Product update(Product updatedProduct) {
        return productRepository.save(updatedProduct);
    }

    @Transactional
    public Product deleteById(Long id) {
        Product product = productRepository.findById(id).orElse(null);
        productRepository.deleteById(id);
        return product;
    }

    public List<Product> findAll() {
        return productRepository.findAll();
    }
}
