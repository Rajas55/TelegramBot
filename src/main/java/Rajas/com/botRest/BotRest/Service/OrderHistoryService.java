package Rajas.com.botRest.BotRest.Service;

import Rajas.com.botRest.BotRest.Entity.OrderModel;
import Rajas.com.botRest.BotRest.Entity.ProductModel;
import Rajas.com.botRest.BotRest.Repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.Optional;

@Service
public class OrderHistoryService {
    @Autowired
    private ProductRepository productRepository;

    public LinkedList<String> buttonsForMonths(){
        LinkedList<String> monthButtons = new LinkedList<>();
        monthButtons.add("Jan-Apr");
        monthButtons.add("May-Aug");
        monthButtons.add("Sept-Dec");



        return monthButtons;
    }
    public String convertLinkedListToString(LinkedList<OrderModel>pastOrders){
        String orderPast="";
        System.out.println(pastOrders.size());
        try {
            for (int i = 0; i < pastOrders.size(); i++) {
                Optional<ProductModel> productName = productRepository.findById(pastOrders.get(i).getProdId());
                orderPast = orderPast.concat(String.valueOf(productName.get().getName() + "\t\t\t" + "Qty:"));
                orderPast = orderPast.concat(String.valueOf(pastOrders.get(i).getQuantity() + "\t\t\t"));
                orderPast = orderPast.concat(String.valueOf("₹" + productRepository.findById(pastOrders.get(i).getProdId()).get().getPrice() + "/- \n"));
                orderPast = orderPast.concat(String.valueOf("Order date: " + pastOrders.get(i).getDateAndTime() + "\n"));
                orderPast = orderPast.concat("Status: " + pastOrders.get(i).getStatus() + "\n\n");
            }
        }catch (Exception w){
            System.out.println(w+"for error");
        }
        return orderPast;
    }

}
