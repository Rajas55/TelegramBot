package Rajas.com.botRest.BotRest.Entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table
public class CategoryModel {
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        @Column(name = "CatId",nullable = false,unique = true)
        private int catId;

        @Column(name = "Category",nullable = false)
        private String category;

//    @OneToMany(cascade = CascadeType.ALL)
//    @JoinColumn(name = "productCategories")
//    private Products products;
//@OneToOne(mappedBy = "productCategories")
//private Products products;

    @OneToMany(mappedBy = "categoryModel")
    private List<ProductModel> products =new ArrayList<ProductModel>();

    }
