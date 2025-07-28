package solutions.skydev.pos.product_service.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import solutions.skydev.pos.product_service.model.entity.Product;

import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    boolean existsByName(String name);
    @Query(value = "SELECT * FROM product WHERE id = :id", nativeQuery = true)
    Optional<Product> findByIdIgnoreDeleted(@Param("id") Long id);
}
