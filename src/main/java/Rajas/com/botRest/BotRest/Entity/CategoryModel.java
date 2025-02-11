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

    @OneToMany(mappedBy = "categoryModel")
    private List<ProductModel> products =new ArrayList<ProductModel>();

    }
