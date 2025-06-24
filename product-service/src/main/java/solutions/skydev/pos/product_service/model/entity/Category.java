package solutions.skydev.pos.product_service.model.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@ToString
@NoArgsConstructor
@Entity
@SQLDelete(sql = "UPDATE category SET deleted = true WHERE id=?")
@SQLRestriction("deleted=false")
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;

    @ManyToMany(mappedBy = "categories", fetch = FetchType.LAZY)
    @ToString.Exclude
    private Set<Product> products = new HashSet<>();

    @Column(columnDefinition = "BOOLEAN DEFAULT false")
    private Boolean deleted = Boolean.FALSE;
}
