package Rajas.com.botRest.BotRest.Entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@EnableJpaRepositories
@Table(name="products")
public class ProductModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ProdId",nullable = false)
    private long prodId;

    @ManyToOne
    @JoinColumn(name = "CatId")
    private CategoryModel categoryModel;
    //
    @ManyToOne
    @JoinColumn(name = "hsnCode")
    private HSNCode hsnCode;

    @Column(name = "Name")
    private String name;

    @Column(name = "Quantity")
    private int quantity;

    @Column(name = "description")
    private String description;

    @Column(name = "Price")
    private int price;

    @Column(name = "photo_path")
    private String photoPath;

     @Column(name = "suggestion")
    private Long suggestedProductId;


}

