package Rajas.com.botRest.BotRest.Service;



import java.util.ArrayList;
import java.util.LinkedList;

public class ButtonServiceForProducts {

    String productName ;
    LinkedList<String> buttons  = new LinkedList<>();

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

    public LinkedList<String> getButtons() {
        return buttons;
    }
}
