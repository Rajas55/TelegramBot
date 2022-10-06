package Rajas.com.botRest.BotRest.Service;

import Rajas.com.botRest.BotRest.Entity.Cart;
import Rajas.com.botRest.BotRest.Entity.ProductModel;
import Rajas.com.botRest.BotRest.Repository.ProductRepository;
import org.telegram.telegrambots.meta.api.methods.invoices.SendInvoice;
import org.telegram.telegrambots.meta.api.objects.payments.Invoice;
import org.telegram.telegrambots.meta.api.objects.payments.LabeledPrice;
import org.telegram.telegrambots.meta.exceptions.TelegramApiRequestException;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

public class CheckoutService  {
    private long userId;
    private String companyName;
    private String providerToken="1877036958:TEST:1942230c8b50294c0d51920bda254d712d654afd";
//    private String providerToken = "5322214758:TEST:022f116a-0e7e-454b-87a8-8039462791f2";
    private String payload;
    private String description;
    private String currency ="INR";
    public CheckoutService()
    {

    }
   public CheckoutService(long userId,String companyName,String payload,String description)
    {
        this.userId = userId;
        this.companyName=companyName;
        this.payload = payload;
        this.description = description;
    }
    public SendInvoice invoiceGenerator(LinkedList<Cart> cart, ProductRepository productRepository)
    {
        ArrayList<LabeledPrice> labeledPrices= new ArrayList<>();
        for(int i=0;i<cart.size();i++) {
          long prodId =   cart.get(i).getProductId();
         Optional<ProductModel> productModel= productRepository.findById(prodId);
            labeledPrices.add(i,new LabeledPrice());
            labeledPrices.get(i).setLabel(productModel.get().getName());
            labeledPrices.get(i).setAmount((productModel.get().getPrice()*100)*cart.get(i).getQuantity());
        }


        Invoice invoice =new Invoice();

       SendInvoice sendInvoice= new SendInvoice();
        sendInvoice.setChatId(userId);
        sendInvoice.setProviderToken(providerToken);
        sendInvoice.setTitle(companyName);
        sendInvoice.setPayload(payload);
        sendInvoice.setDescription(description);
        sendInvoice.setCurrency(currency);
        sendInvoice.setPrices(labeledPrices);
        sendInvoice.setNeedShippingAddress(true);
        sendInvoice.setPhotoHeight(200);
        sendInvoice.setPhotoWidth(200);
        sendInvoice.setPhotoUrl("https://static.vecteezy.com/system/resources/previews/004/999/463/original/shopping-cart-icon-illustration-free-vector.jpg");



        return sendInvoice;
    }

}
