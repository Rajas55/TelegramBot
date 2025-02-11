package Rajas.com.botRest.BotRest.Service;

import Rajas.com.botRest.BotRest.Entity.Cart;
import Rajas.com.botRest.BotRest.Entity.OrderModel;
import Rajas.com.botRest.BotRest.Entity.ProductModel;
import Rajas.com.botRest.BotRest.Repository.CartRepository;
import Rajas.com.botRest.BotRest.Repository.OrderRepository;
import Rajas.com.botRest.BotRest.Repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

@Component
public class BillService {
    @Autowired
    OrderRepository orderRepository;
    @Autowired
    CartRepository cartRepository;
    @Autowired
    ProductRepository productRepository;

    OrderModel orderModel = new OrderModel();
    public void saveOrder(long userId){
       LinkedList<OrderModel> order = new LinkedList<>();

        LinkedList <Cart> cart =  cartRepository.getCartByUserId(userId);
        LocalDateTime now = LocalDateTime.now();
        for (int i =0; i<cart.size();i++){
            order.add(i,new OrderModel());
            order.get(i).setStatus("Order received");
            order.get(i).setProdId(cart.get(i).getProductId());
            order.get(i).setUserId(userId);
            order.get(i).setDateAndTime(now);
            order.get(i).setQuantity(cart.get(i).getQuantity());

        }
        orderRepository.saveAll(order);

    }
    public String orderHistory(long userID){
        LinkedList<OrderModel> pastOrders = orderRepository.getOrderHistory(userID);
       String orderPast = "Order History (Recent Orders) \n\n";
       for(int i=pastOrders.size()-1;i>pastOrders.size()-4;i--){
           Optional<ProductModel> productName = productRepository.findById(pastOrders.get(i).getProdId());
           orderPast=orderPast.concat(String.valueOf(productName.get().getName()+"\t\t\t"+"Qty:"));
           orderPast=orderPast.concat(String.valueOf(pastOrders.get(i).getQuantity()+"\t\t\t"));
           orderPast=orderPast.concat(String.valueOf("$"+productRepository.findById(pastOrders.get(i).getProdId()).get().getPrice()+"/- \n"));
           orderPast=orderPast.concat(String.valueOf("Order date: "+pastOrders.get(i).getDateAndTime()+"\n"));
           orderPast=orderPast.concat("Status: "+pastOrders.get(i).getStatus()+"\n\n");
       }
       return orderPast;


    }
}
