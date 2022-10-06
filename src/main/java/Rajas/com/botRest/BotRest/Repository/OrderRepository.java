package Rajas.com.botRest.BotRest.Repository;

import Rajas.com.botRest.BotRest.Entity.OrderModel;
import Rajas.com.botRest.BotRest.Entity.ProductModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.LinkedList;

public interface OrderRepository  extends JpaRepository<OrderModel,Long> {

    @Query(value = "select * from order_model where user_id =:m", nativeQuery = true)
    public LinkedList <OrderModel> getOrderHistory(@Param("m")long uuid);

    @Query(value = "SELECT * from order_model where date_and_time between :Start_date and :End_date", nativeQuery = true)
    public LinkedList<OrderModel>getHistorySortByMonths(@Param("Start_date") LocalDateTime startDate, @Param("End_date") LocalDateTime EndDate);

    public LinkedList<OrderModel>findByUserId(long userId);

}
