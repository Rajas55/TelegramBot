package Rajas.com.botRest.BotRest.Repository;

import Rajas.com.botRest.BotRest.Entity.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.ArrayList;
import java.util.LinkedList;

public interface CartRepository extends JpaRepository<Cart,Integer> {

    @Query(value = "select * from cart where user_id=:m",nativeQuery = true)
    LinkedList<Cart> getCartByUserId(@Param("m")long uuId);

}
