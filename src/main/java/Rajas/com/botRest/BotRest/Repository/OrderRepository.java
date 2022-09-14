package Rajas.com.botRest.BotRest.Repository;

import Rajas.com.botRest.BotRest.Entity.OrderModel;
import Rajas.com.botRest.BotRest.Entity.ProductModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.LinkedList;

public interface OrderRepository  extends JpaRepository<OrderModel,Long> {

    @Query(value = "select * from order_model where user_id =:m", nativeQuery = true)
    public LinkedList <OrderModel> getOrderHistory(@Param("m")long uuid);
}
