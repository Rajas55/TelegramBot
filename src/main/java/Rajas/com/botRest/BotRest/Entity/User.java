package Rajas.com.botRest.BotRest.Entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name="user_details")
public class User {
    @Id
    @Column(name = "user_id",nullable = false)
    private long uuid;


    @Column(name = "Name",nullable = false)
    private String name;

    @Column(name = "Address")
    private String address;

    @Column(name = "PhoneNo",nullable = false)
    private String mob;





}
