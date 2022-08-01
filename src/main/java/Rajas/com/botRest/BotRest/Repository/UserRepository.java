package Rajas.com.botRest.BotRest.Repository;

import Rajas.com.botRest.BotRest.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface UserRepository extends JpaRepository<User,Long> {

    @Query(value = "select phone_no from user_details where phone_no =:m",nativeQuery = true)
    String findByNumber(@Param("m")String mobileNo);
}
