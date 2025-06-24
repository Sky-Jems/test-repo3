package solutions.skydev.pos.product_service.consumer;

import io.github.springwolf.bindings.kafka.annotations.KafkaAsyncOperationBinding;
import io.github.springwolf.core.asyncapi.annotations.AsyncListener;
import io.github.springwolf.core.asyncapi.annotations.AsyncOperation;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import solutions.skydev.pos.common.product_service.dto.request.ProductRequestDto;
import solutions.skydev.pos.common.product_service.dto.response.ProductResponseDto;
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

    @KafkaListener(topics = "create-product-command")
    @AsyncListener(operation = @AsyncOperation(channelName = "create-product-command",
            description = "Create product command"))
    @KafkaAsyncOperationBinding
    @SendTo("product.created")
    public ProductResponseDto createProductCommand(ConsumerRecord<String, ProductRequestDto> record) {
        ProductRequestDto productRequestDto = record.value();
        Product product = this.productMapper.toEntity(productRequestDto);
        Product createdProduct = productService.save(product);
        return this.productMapper.toResponseDto(createdProduct);
    }

    @KafkaListener(topics = "update-product-command")
    @AsyncListener(operation = @AsyncOperation(channelName = "update-product-command",
            description = "Update product command", payloadType = ProductRequestDto.class))
    @KafkaAsyncOperationBinding
    @SendTo("product.updated")
    public ProductResponseDto updateProductCommand(ConsumerRecord<String, ProductRequestDto> record) {
        Product product = this.productMapper.toEntity(record.value());
        Product updatedProduct = productService.update(product);
        return this.productMapper.toResponseDto(updatedProduct);
    }

    @KafkaListener(topics = "delete-product-command")
    @AsyncListener(operation = @AsyncOperation(channelName = "delete-product-command", description = "Delete product command"))
    @KafkaAsyncOperationBinding
    @SendTo("product.deleted")
    @Transactional
    public ProductResponseDto deleteProductCommand(ConsumerRecord<String, ProductRequestDto> record) {
        Product product = this.productMapper.toEntity(record.value());
        Product deletedProduct =  productService.deleteById(product.getId());
        return this.productMapper.toResponseDto(deletedProduct);
    }
}
