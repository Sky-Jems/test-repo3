package solutions.skydev.pos.product_service.consumer;

import io.github.springwolf.bindings.kafka.annotations.KafkaAsyncOperationBinding;
import io.github.springwolf.core.asyncapi.annotations.AsyncListener;
import io.github.springwolf.core.asyncapi.annotations.AsyncOperation;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Component;
import solutions.skydev.pos.product_service.model.dto.request.CategoryProductRequestDto;
import solutions.skydev.pos.product_service.model.dto.request.CategoryRequestDto;
import solutions.skydev.pos.product_service.model.dto.response.CategoryResponseDto;
import solutions.skydev.pos.product_service.model.entity.Category;
import solutions.skydev.pos.product_service.model.entity.Product;
import solutions.skydev.pos.product_service.model.mapper.CategoryMapper;
import solutions.skydev.pos.product_service.model.mapper.ProductMapper;
import solutions.skydev.pos.product_service.service.CategoryService;

@Component
public class CategoryConsumer {
    
    private final CategoryService categoryService;
    private final CategoryMapper categoryMapper;
    private final ProductMapper productMapper;
    
    @Autowired
    public CategoryConsumer(CategoryService categoryService, CategoryMapper categoryMapper, ProductMapper productMapper) {
        this.categoryService = categoryService;
        this.categoryMapper = categoryMapper;
        this.productMapper = productMapper;
    }

    @KafkaListener(topics = "create-category-command", properties = {
            "spring.json.value.default.type=solutions.skydev.pos.product_service.model.dto.request.CategoryRequestDto"
    })
    @AsyncListener(operation = @AsyncOperation(
            channelName = "create-category-command",
            description = "Create category command",
            payloadType = Category.class
    ))
    @KafkaAsyncOperationBinding
    @SendTo("category.created")
    public CategoryResponseDto createCategoryCommand(ConsumerRecord<String, CategoryRequestDto> record) {
        CategoryRequestDto categoryRequestDto = record.value();
        Category category = this.categoryMapper.toEntity(categoryRequestDto);
        categoryService.addCategory(category);
        return this.categoryMapper.toResponseDto(category);
    }

    @KafkaListener(topics = "update-category-command", properties = {
            "spring.json.value.default.type=solutions.skydev.pos.product_service.model.dto.request.CategoryRequestDto"
    })
    @AsyncListener(operation = @AsyncOperation(
            channelName = "update-category-command",
            description = "Update category command",
            payloadType = Category.class
    ))
    @KafkaAsyncOperationBinding
    @SendTo("category.updated")
    public CategoryResponseDto updateCategoryCommand(ConsumerRecord<String, CategoryRequestDto> record) {
        Long categoryId = Long.valueOf(record.key());
        CategoryRequestDto categoryRequestDto = record.value();
        Category category = this.categoryMapper.toEntity(categoryRequestDto);
        categoryService.updateCategory(categoryId, category);
        return this.categoryMapper.toResponseDto(category);
    }

    @KafkaListener(topics = "delete-category-command")
    @AsyncListener(operation = @AsyncOperation(
            channelName = "delete-category-command",
            description = "Delete category command",
            payloadType = Category.class
    ))
    @KafkaAsyncOperationBinding
    @SendTo("category.deleted")
    public String deleteCategoryCommand(ConsumerRecord<String, String> record) {
        Long categoryId = Long.valueOf(record.key());
        categoryService.deleteCategory(categoryId);
        return "Category with ID " + categoryId + " deleted successfully";
    }

    @KafkaListener(topics = "create-category-product-command", properties = {
            "spring.json.value.default.type=solutions.skydev.pos.product_service.model.dto.request.CategoryProductRequestDto"
    })
    @AsyncListener(operation = @AsyncOperation(
            channelName = "create-category-product-command",
            description = "Create category product command",
            payloadType = Category.class
    ))
    @KafkaAsyncOperationBinding
    public void createCategoryProductCommand(ConsumerRecord<String, CategoryProductRequestDto> record) {
        CategoryProductRequestDto categoryProductRequestDto = record.value();
        Category category = this.categoryMapper.toEntity(categoryProductRequestDto);
        Product product = this.productMapper.toEntity(categoryProductRequestDto);
        categoryService.addProduct(category, product);
        // submit event change state using correlation id
    }
}
