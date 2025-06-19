package solutions.skydev.pos.product_service.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import solutions.skydev.pos.product_service.model.entity.Category;

public interface CategoryRepository extends JpaRepository<Category, Long> { }
