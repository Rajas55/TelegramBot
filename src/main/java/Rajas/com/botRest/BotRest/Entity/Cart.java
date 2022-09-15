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
@Table(name="cart")
public class Cart {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private int id;

    @Column(name="user_id")
    private long uuid;

    private long productId;

    private int quantity;

//    @ManyToMany
//    @JoinColumn(name = "ProdId")
//    private List<ProductModel> products =new ArrayList<ProductModel>();


}

