package solutions.skydev.pos.product_service.consumer;

import kafka.Kafka;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.kafka.test.EmbeddedKafkaBroker;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.kafka.test.utils.ContainerTestUtils;
import org.springframework.kafka.test.utils.KafkaTestUtils;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.event.annotation.BeforeTestClass;
import solutions.skydev.pos.common.product_service.dto.request.ProductRequestDto;
import solutions.skydev.pos.common.product_service.dto.response.ProductResponseDto;
import solutions.skydev.pos.product_service.model.entity.Category;
import solutions.skydev.pos.product_service.model.entity.Product;
import solutions.skydev.pos.product_service.repository.CategoryRepository;
import solutions.skydev.pos.product_service.repository.ProductRepository;
import solutions.skydev.pos.product_service.service.ProductService;

import java.math.BigDecimal;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

@SpringBootTest
@ActiveProfiles("test")
@DirtiesContext
@TestPropertySource(locations = "classpath:application-test.properties")
@EmbeddedKafka(partitions = 1, topics = {"create-product-command", "product.created", "update-product-command", "product.updated", "delete-product-command", "product.deleted"})
public class ProductConsumerIntegrationTest {

    @Autowired
    ProductRepository productRepository;

    @Autowired
    CategoryRepository categoryRepository;

    @Autowired
    KafkaTemplate<String, Object> kafkaTemplate;

    @Autowired
    EmbeddedKafkaBroker embeddedKafkaBroker;

    @Test
    void contextLoads() {
        // Verify that the application context loads successfully
        System.out.println("Application context loaded successfully");
        System.out.println("Embedded Kafka broker is running at: " + embeddedKafkaBroker.getBrokersAsString());
    }

    @BeforeAll
    static void setup() {
        System.out.println("Embedded Kafka broker is starting up at"  );

    }

    @Test
    void testProductIsCreated() throws Exception {
        Map<String, Object> consumerProps = KafkaTestUtils.consumerProps("testT", "false", embeddedKafkaBroker);
        consumerProps.put(JsonDeserializer.USE_TYPE_INFO_HEADERS, true);
        consumerProps.put(JsonDeserializer.TRUSTED_PACKAGES, "*");
        consumerProps.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);

        DefaultKafkaConsumerFactory<String, ProductResponseDto> cf = new DefaultKafkaConsumerFactory<>(consumerProps);
        Consumer<String, ProductResponseDto> consumer = cf.createConsumer();
        
        // wait for 4 sec
//        TimeUnit.SECONDS.sleep(4);
//        embeddedKafkaBroker.

        embeddedKafkaBroker.consumeFromAllEmbeddedTopics(consumer);

        Category category = new Category();
        category.setName("Test Category");

        Category newCategory = categoryRepository.save(category);

        List<Long> categoryIds = new ArrayList<>();
        categoryIds.add(newCategory.getId());

        // Create test data
        ProductRequestDto productRequestDto = ProductRequestDto.builder()
                .name("Test Product")
                .description("Test Description")
                .price(10.99)
                .categoryIds(categoryIds)
                .build();


        kafkaTemplate.send("create-product-command", productRequestDto);
        kafkaTemplate.flush();
        // create consumer to listen to the "product.created" topic

        ConsumerRecord<String, ProductResponseDto> record = KafkaTestUtils.getSingleRecord(consumer, "product.created", Duration.ofSeconds(5));
        // Assert that the product was created successfully
        ProductResponseDto productResponseDto = record.value();
        assert productResponseDto != null;

        // Verify the product details
        Product product = productRepository.findById(productResponseDto.getId())
                .orElseThrow(() -> new Exception("Product not found"));
        assert product.getName().equals(productResponseDto.getName());
    }

    @Test
    void testProductIsUpdated() throws Exception {
        Map<String, Object> consumerProps = KafkaTestUtils.consumerProps("testT", "false", embeddedKafkaBroker);
        consumerProps.put(JsonDeserializer.USE_TYPE_INFO_HEADERS, true);
        consumerProps.put(JsonDeserializer.TRUSTED_PACKAGES, "*");
        consumerProps.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);

        DefaultKafkaConsumerFactory<String, ProductResponseDto> cf = new DefaultKafkaConsumerFactory<>(consumerProps);
        Consumer<String, ProductResponseDto> consumer = cf.createConsumer();

        embeddedKafkaBroker.consumeFromAllEmbeddedTopics(consumer);

        // Create a category for the product
        Category category = new Category();
        category.setName("Test Category");

        Category newCategory = categoryRepository.save(category);

        // First create a product
        Product product = new Product();
        product.setName("Original Product");
        product.setDescription("Original Description");
        product.setPrice(BigDecimal.valueOf(9.99));
        product.setCategories(Collections.singleton(newCategory));

        Product savedProduct = productRepository.save(product);

        List<Long> categoryIds = new ArrayList<>();
        categoryIds.add(newCategory.getId());

        // Create update data
        ProductRequestDto productRequestDto = ProductRequestDto.builder()
                .id(savedProduct.getId())
                .name("Updated Product")
                .description("Updated Description")
                .price(19.99)
                .categoryIds(categoryIds)
                .build();

        kafkaTemplate.send("update-product-command", productRequestDto);
        kafkaTemplate.flush();

        ConsumerRecord<String, ProductResponseDto> record = KafkaTestUtils.getSingleRecord(consumer, "product.updated", Duration.ofSeconds(5));
        // Assert that the product was created successfully
        ProductResponseDto productResponseDto = record.value();
        assert productResponseDto != null;

       // Verify the product details
        Product updatedProduct = productRepository.findById(savedProduct.getId())
                .orElseThrow(() -> new Exception("Product not found"));
        assert updatedProduct.getName().equals("Updated Product");
        assert updatedProduct.getDescription().equals("Updated Description");
        assert updatedProduct.getPrice().equals(BigDecimal.valueOf(19.99));
    }

    @Test
    void testProductIsDeleted() throws Exception {
        Map<String, Object> consumerProps = KafkaTestUtils.consumerProps("testT", "false", embeddedKafkaBroker);
        consumerProps.put(JsonDeserializer.USE_TYPE_INFO_HEADERS, true);
        consumerProps.put(JsonDeserializer.TRUSTED_PACKAGES, "*");
        consumerProps.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);

        DefaultKafkaConsumerFactory<String, ProductResponseDto> cf = new DefaultKafkaConsumerFactory<>(consumerProps);
        Consumer<String, ProductResponseDto> consumer = cf.createConsumer();

        embeddedKafkaBroker.consumeFromAllEmbeddedTopics(consumer);
        
        // Check consumer .assignments before proceeding
        

        // Create a category for the product
        Category category = new Category();
        category.setName("Test Category");

        Category newCategory = categoryRepository.save(category);

        // First create a product
        Product product = new Product();
        product.setName("Product To Delete");
        product.setDescription("This product will be deleted");
        product.setPrice(BigDecimal.valueOf(15.99));
        product.setCategories(Collections.singleton(newCategory));

        Product savedProduct = productRepository.save(product);

        ProductRequestDto productRequestDto = ProductRequestDto.builder()
                .id(savedProduct.getId())
                .build();
        
        kafkaTemplate.send("delete-product-command", productRequestDto);
        kafkaTemplate.flush();

        ConsumerRecord<String, ProductResponseDto> record = KafkaTestUtils.getSingleRecord(consumer, "product.deleted", Duration.ofSeconds(5));
        // Assert that the product was deleted successfully
        ProductResponseDto productResponseDto = record.value();
        assert productResponseDto != null;

        // Verify the product is no longer in the database
        Optional<Product> productInDb = productRepository.findById(savedProduct.getId());

        // Assert that the product was deleted
        assert !productInDb.isPresent() : "Product should have been deleted";
    }
}
