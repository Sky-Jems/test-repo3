package solutions.skydev.pos.product_service.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import solutions.skydev.pos.product_service.producer.ProductProducer;
import solutions.skydev.pos.product_service.model.entity.Product;
import solutions.skydev.pos.product_service.repository.ProductRepository;

import java.util.List;
import java.util.Optional;

@Service
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final ProductProducer productProducer;


    @Autowired
    public ProductServiceImpl(ProductRepository productRepository, ProductProducer productProducer) {
        this.productRepository = productRepository;
        this.productProducer = productProducer;
    }

    @Override
    @Transactional
    public Product save(Product product) {
        Product createdProduct = productRepository.save(product);
        this.productProducer.sendProductCreated(createdProduct);

        return createdProduct;
    }

    public Product findById(Long id) {
        return productRepository.findById(id).orElse(null);
    }

    public Product update(Long id, Product updatedProduct) {
        Product existingProduct = productRepository.findById(id).orElse(null);
        if (existingProduct == null) {
            // TODO: handle the case where the product doesn't exist.
            return null;
        }
        productRepository.save(updatedProduct);
        this.productProducer.sendProductUpdated(updatedProduct);
        return updatedProduct;
    }

    public void deleteById(Long id) {
        // Assuming the product exists, todo: handle the case where it doesn't
        Optional<Product> product = productRepository.findById(id);
        this.productProducer.sendProductDeleted(product);
        productRepository.deleteById(id);
    }

    public List<Product> findAll() {
        return productRepository.findAll();
    }
}
