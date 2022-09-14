package Rajas.com.botRest.BotRest.Entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.time.LocalDateTime;
import java.util.Date;

@Getter
@Setter
@Entity
public class OrderModel {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private  long orderId;

    private long userId;
    private long prodId;
    private int quantity;
    private String status;
    private LocalDateTime dateAndTime;



}
