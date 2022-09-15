package Rajas.com.botRest.BotRest.Repository;

import Rajas.com.botRest.BotRest.Entity.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.LinkedList;

public interface CartRepository extends JpaRepository<Cart,Integer> {

    @Query(value = "select * from cart where user_id=:m",nativeQuery = true)
    LinkedList<Cart> getCartByUserId(@Param("m")long uuId);

//
//    boolean deleteByUuid(long uuid);

    @Modifying
    @Transactional
    @Query(value = "delete from cart where user_id=:m",nativeQuery = true)
     void deleteByUuid(@Param("m") long uuid);

    LinkedList<Cart> findByUuid(long chatUserId);

//    @Query(value = "SET foreign_key_checks =:m",nativeQuery = true)
//    void setForeignKeyChecks(@Param("m") int fkCheck);

}
