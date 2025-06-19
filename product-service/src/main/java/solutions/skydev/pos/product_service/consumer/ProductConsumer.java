package solutions.skydev.pos.product_service.consumer;

import io.github.springwolf.bindings.kafka.annotations.KafkaAsyncOperationBinding;
import io.github.springwolf.core.asyncapi.annotations.AsyncListener;
import io.github.springwolf.core.asyncapi.annotations.AsyncOperation;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Component;
import solutions.skydev.pos.product_service.model.dto.request.ProductRequestDto;
import solutions.skydev.pos.product_service.model.dto.response.ProductResponseDto;
import solutions.skydev.pos.product_service.model.entity.Product;
import solutions.skydev.pos.product_service.model.mapper.ProductMapper;
import solutions.skydev.pos.product_service.service.ProductService;

@Component
public class ProductConsumer {

    private final ProductService productService;
    private final ProductMapper productMapper;

    @Autowired
    public ProductConsumer(ProductService productService, ProductMapper productMapper) {
        this.productService = productService;
        this.productMapper = productMapper;
    }

    @KafkaListener(topics = "create-product-command", properties = {
            "spring.json.value.default.type=solutions.skydev.pos.product_service.model.dto.request.ProductRequestDto"
    })
    @AsyncListener(operation = @AsyncOperation(channelName = "create-product-command",
            description = "Create product command", payloadType = ProductRequestDto.class))
    @KafkaAsyncOperationBinding
    @SendTo("product.created")
    public ProductResponseDto createProductCommand(ConsumerRecord<String, ProductRequestDto> record) {
        ProductRequestDto productRequestDto = record.value();
        Product product = this.productMapper.toEntity(productRequestDto);
        productService.save(product);
        return this.productMapper.toResponseDto(product);
    }

    @KafkaListener(topics = "update-product-command", properties = {
            "spring.json.value.default.type=solutions.skydev.pos.product_service.model.dto.request.ProductRequestDto"
    })
    @AsyncListener(operation = @AsyncOperation(channelName = "update-product-command",
            description = "Update product command", payloadType = ProductRequestDto.class))
    @KafkaAsyncOperationBinding
    @SendTo("product.updated")
    public ProductResponseDto updateProductCommand(ConsumerRecord<String, ProductRequestDto> record) {
        Long productId = Long.valueOf(record.key());
        ProductRequestDto productRequestDto = record.value();
        Product product = this.productMapper.toEntity(productRequestDto);
        Product updatedProduct = productService.update(productId, product);
        return this.productMapper.toResponseDto(updatedProduct);
    }

    @KafkaListener(topics = "delete-product-command")
    @AsyncListener(operation = @AsyncOperation(channelName = "delete-product-command", description = "Delete product command"))
    @KafkaAsyncOperationBinding
    @SendTo("product.deleted")
    public String deleteProductCommand(ConsumerRecord<String, String> record) {
        String id = record.key();
        Long productId = null;
        try {
            productId = Long.valueOf(id);
        } catch (Exception e) {
            // submit event to product.deleted.error topic
            return "Error: " + e.getMessage();
        }
        productService.deleteById(productId);
        return "Product with ID " + id + " deleted successfully";
    }
}
