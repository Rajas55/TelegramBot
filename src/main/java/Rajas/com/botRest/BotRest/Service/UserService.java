package Rajas.com.botRest.BotRest.Service;

import Rajas.com.botRest.BotRest.Entity.User;
import Rajas.com.botRest.BotRest.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
 public class UserService {

    @Autowired
    private UserRepository userRepository;
    public String registerUser(){
        return "Please enter your number"+ "\n"+"\n"+"You can edit/change your number just by typing a new one.";
    }
    public boolean isMobileNumber(String str)
    {

        Pattern ptrn = Pattern.compile("(0/60)?[7-9][0-9]{9}");
//the matcher() method creates a matcher that will match the given input against this pattern
        Matcher match = ptrn.matcher(str);
//returns a boolean value
        if ((match.find() && match.group().equals(str))){
            return true;
        }
        else {
            return false;
        }
//        return (match.find() && match.group().equals(str));
    }

    public boolean isDigit(String command){
        try {
            long mobNo =  Long.parseLong(command);

            return true;

        }catch (Exception e){
            return false;
        }
    }
    public boolean checkNoInDb(String mobno){
        if ( userRepository.findByNumber(mobno)!=null){
            return true;
        }
        else {

            return false;

        }


    }
    public boolean saveUser(long userId,String mobileNo,String name){
        User user = new User();
        user.setUuid(userId);
        user.setMob(mobileNo);
        user.setName(name);
       try {
           userRepository.save(user);
           return true;
       }catch (Exception e)
       {
           return false;
       }


    }

}
