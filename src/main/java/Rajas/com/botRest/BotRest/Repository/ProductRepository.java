package Rajas.com.botRest.BotRest.Repository;

import Rajas.com.botRest.BotRest.Entity.ProductDisplay;
import Rajas.com.botRest.BotRest.Entity.ProductModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.ArrayList;

public interface ProductRepository extends JpaRepository<ProductModel,Long> {
    @Query(value = "select * from products where cat_id =:m",nativeQuery = true)
    public ArrayList<ProductModel> getProductsByCategoryId( @Param("m")int category);

    //@Query(value = "select * from Test.products where name like ",nativeQuery = true)
    public ArrayList<ProductModel>findByNameContaining(String oneValue);

//    public ArrayList<ProductModel> findByName(String name);
    @Query(value = "select * from products",nativeQuery = true)
    public ArrayList<ProductModel>allProductsList();

    public ArrayList<ProductModel>findByNameEquals(String prodName);

}
