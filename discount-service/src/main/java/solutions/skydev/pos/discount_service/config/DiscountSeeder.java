package solutions.skydev.pos.discount_service.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import solutions.skydev.pos.discount_service.model.entity.Discount;
import solutions.skydev.pos.discount_service.model.enums.DiscountType;
import solutions.skydev.pos.discount_service.service.DiscountService;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

@Configuration
public class DiscountSeeder {

    @Bean
    public CommandLineRunner seedDiscount(DiscountService discountService) {
        return args -> {
            createDiscountIfNotExists(
                    discountService,
                    "50% Percent Discount",
                    BigDecimal.valueOf(50),
                    LocalDateTime.parse("2025-12-31T23:59:59"),
                    LocalDateTime.parse("2030-12-31T23:59:59")
            );

            createDiscountIfNotExists(
                    discountService,
                    "100% Percent Discount",
                    BigDecimal.valueOf(100),
                    LocalDateTime.parse("2025-12-31T23:59:59"),
                    LocalDateTime.parse("2030-12-31T23:59:59")
            );
            createDiscountIfNotExists(
                    discountService,
                    "10% Percent Discount",
                    BigDecimal.valueOf(10),
                    LocalDateTime.parse("2025-12-31T23:59:59"),
                    LocalDateTime.parse("2030-12-31T23:59:59")
            );
            createDiscountIfNotExists(
                    discountService,
                    "20% Percent Discount",
                    BigDecimal.valueOf(20),
                    LocalDateTime.parse("2025-12-31T23:59:59"),
                    LocalDateTime.parse("2030-12-31T23:59:59")
            );
        };
    }

    private void createDiscountIfNotExists(
            DiscountService discountService,
            String name,
            BigDecimal value,
            LocalDateTime startDateTime,
            LocalDateTime endDateTime
    ) {
        Optional<Discount> existing = discountService.findByName(name);
        if (existing.isPresent()) {
            System.out.println("Discount already exists: " + name);
            return;
        }

        Discount discount = new Discount();
        discount.setName(name);
        discount.setType(DiscountType.PERCENTAGE);
        discount.setValue(value);
        discount.setCap(0.0);
        discount.setMaxQty(0);
        discount.setMinQty(0);
        discount.setMinSpend(0.0);
        discount.setDiscountType("PROMOTIONAL");
        discount.setStartDateTime(startDateTime);
        discount.setEndDateTime(endDateTime);

        try {
            discountService.createDiscount(discount);
        } catch (Exception e) {
        }
    }
}
