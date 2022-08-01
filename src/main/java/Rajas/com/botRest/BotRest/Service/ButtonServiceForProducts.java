package Rajas.com.botRest.BotRest.Service;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.util.ArrayList;

public class ButtonServiceForProducts {

    String productName ;
    ArrayList<String> buttons  = new ArrayList<>();

    public ButtonServiceForProducts(){
        buttons.add("Add to cart");
        buttons.add("Buy now");
        buttons.add("Show Cart");
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public ArrayList<String> getButtons() {
        return buttons;
    }
}
