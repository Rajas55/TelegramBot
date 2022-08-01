package Rajas.com.botRest.BotRest.Controller;

import Rajas.com.botRest.BotRest.RecklerBot;
import Rajas.com.botRest.BotRest.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/")
public class UserController {
    @Autowired
    private RecklerBot recklerBot;
    @Autowired
    public  UserRepository userRepository;
    @GetMapping("/test")
    public String test(){
       userRepository.findAll();
        System.out.println("Working");
        return "Working";
    }

}
