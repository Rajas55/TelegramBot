package Rajas.com.botRest.BotRest;

import Rajas.com.botRest.BotRest.Entity.Cart;
import Rajas.com.botRest.BotRest.Entity.OrderModel;
import Rajas.com.botRest.BotRest.Entity.ProductModel;
import Rajas.com.botRest.BotRest.Repository.*;
import Rajas.com.botRest.BotRest.Service.*;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.methods.AnswerPreCheckoutQuery;
import org.telegram.telegrambots.meta.api.methods.invoices.SendInvoice;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.payments.SuccessfulPayment;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

import java.time.LocalDateTime;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

//service class/main bot class which extends telegram polling bot(Has all the commands
@Service
public class RecklerBot extends TelegramLongPollingBot {
     private   int updateFlag=0;
     private boolean isSuccessfulPayment=false;
     private int deleteFlag = 0;
     private int cartFlag = 0;
     private int yr =0;
    private final Map<Long, CommandData> lastCommandMap = new HashMap<>();
    private final Set<Long> processedMessages = new HashSet<>();

    private int cartProductNo=0;
    SendMessage message = new SendMessage(); //new object for SendMessage predefined by telegram bot api
    SuccessfulPayment successfulPayment = new SuccessfulPayment();

    Message m = new Message();
    Update update2;
    ItemService itemService = new ItemService(); //object for the service class which contains the business logic
    //connecting the UserService Class
    @Autowired
    private UserService userService;
    CartService cartService = new CartService();
    @Autowired
     BillService billService;
    int buyNowFlag =0;
    @Autowired
    OrderHistoryService orderHistoryService;



    @SneakyThrows
        // sneaky throw concept allows us to throw any checked exception without defining it explicitly in the method signature.

        //creating a new object for the telegram bot and registering it in telegram bot api
    RecklerBot() {
        TelegramBotsApi telegramBotsApi = new TelegramBotsApi(DefaultBotSession.class);
        telegramBotsApi.registerBot(this);
    }


    @Autowired
    private ProductRepository productRepository; //connecting productRepo which extends JpaRepo to use the jpa features.
    @Autowired
    private UserRepository userRepository; //connecting userRepo which extends JpaRepo to use the jpa features.
    @Autowired
    private CategoryRepository categoryRepository; //connecting categoryRepo which extends JpaRepo to use the jpa features.
    @Autowired
    private CartProductRepository cartProductRepository;
    @Autowired
    private CartRepository cartRepository;
    @Autowired
    private OrderRepository orderRepository;



    @Override
    public String getBotUsername() {
        return "RetailBot";
    } //overriding the method defined by telegram bot api.

    @Override
    public String getBotToken() {
        return "5596116515:AAELPd4BQLotLwgy6ClnC6fYW2kEklE3NzU";
    }//overriding the method defined by telegram bot api.

    @Override
    public void onRegister() {
        super.onRegister();
    } //overriding the method defined by telegram bot api.
    public static class CommandData {
        public String command;
        public long timestamp;
        public CommandData(String command, long timestamp) {
            this.command = command;
            this.timestamp = timestamp;
        }
    }
    public void sendMessage(String chat) {
        try
        {
//            message.setParseMode("HTML");
            message.setText(chat);
            message.setChatId(update2.getMessage().getChatId());

            try {
                execute(message);
            } catch (
                    TelegramApiException e) {
                e.printStackTrace();
            }
        }catch (Exception e ){
            message.setText(chat);
            try {
                message.setChatId(update2.getCallbackQuery().getMessage().getChatId());
            }
            catch (Exception z)
            {

//              sendMessage(successfulPayment.getProviderPaymentChargeId());

            }

            try {
                execute(message);
            } catch (
                    TelegramApiException q) {
                e.printStackTrace();
            }

        }

    }



    public  void sendInlineButton(LinkedList<String> buttonName,String productString) {
        if (buttonName.size()==0){
            sendMessage("No products available in this category\uD83D\uDE22");
        }
        else {

            SendMessage message3 = new SendMessage();
            InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
            List<List<InlineKeyboardButton>> rowsInline = new LinkedList<>();



            int buttonDivider = buttonName.size() / 3;
            int buttonMod = buttonName.size() % 3;
            //   buttonDivider+=1;
            

            int prod = 0;
            int k;
            for (k = 0; k < buttonDivider; k++) {
                rowsInline.add(k, new LinkedList<InlineKeyboardButton>());
                try {
                    for (int i = 0; i < 3; i++) {
                        rowsInline.get(k).add(i, new InlineKeyboardButton());


                        for (int m = prod; m < buttonName.size() - buttonMod; m++) {
                            rowsInline.get(k).get(i).setText(buttonName.get(m));
                            rowsInline.get(k).get(i).setCallbackData(buttonName.get(m));


                            prod++;

                            break;
                        }
                    }

                } catch (Exception e) {
                    System.out.println(e);


                }
            }
            if (buttonMod != 0) {
                if (buttonMod == 1) {
                    rowsInline.add(k, new LinkedList<InlineKeyboardButton>());
                    rowsInline.get(k).add(0, new InlineKeyboardButton());
                    rowsInline.get(k).get(0).setText(buttonName.get(buttonName.size() - 1));
                    rowsInline.get(k).get(0).setCallbackData(buttonName.get(buttonName.size() - 1));
                } else if (buttonMod == 2) {

                    rowsInline.add(k, new LinkedList<InlineKeyboardButton>());

                    rowsInline.get(k).add(0, new InlineKeyboardButton());
                    rowsInline.get(k).get(0).setText(buttonName.get(buttonName.size() - 1));
                    rowsInline.get(k).get(0).setCallbackData(buttonName.get(buttonName.size() - 1));
                    rowsInline.get(k).add(1, new InlineKeyboardButton());
                    rowsInline.get(k).get(1).setText(buttonName.get(buttonName.size() - 2));
                    rowsInline.get(k).get(1).setCallbackData(buttonName.get(buttonName.size() - 2));


                }
            }
            inlineKeyboardMarkup.setKeyboard(rowsInline);


            try {
                message3.setText(productString);
                message3.setReplyMarkup(inlineKeyboardMarkup);
                try {
                    message3.setChatId(update2.getMessage().getChatId());
                }catch (Exception x){
                   message3.setChatId( update2.getCallbackQuery().getMessage().getChatId());
                }

                execute(message3);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    public  void sendInlineButtonForMultipleProducts(LinkedList<String> buttonName,int prodPrice,String prodName,String prodDesc) {
        if (buttonName.size()==0){
            sendMessage("No products available in this category\uD83D\uDE22");
        }
        else {

            SendMessage message3 = new SendMessage();
            InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
            List<List<InlineKeyboardButton>> rowsInline = new LinkedList<>();

            List<InlineKeyboardButton> rowInline2 = new LinkedList<>();
            InlineKeyboardButton priceBtn = new InlineKeyboardButton();
            InlineKeyboardButton priceBtn2 = new InlineKeyboardButton();
            LinkedList<String> str = new LinkedList<>();

            int buttonDivider = buttonName.size() / 3;
            int buttonMod = buttonName.size() % 3;



            int prod = 0;
            int k;
            for (k = 0; k < buttonDivider; k++) {
                rowsInline.add(k, new LinkedList<InlineKeyboardButton>());
                try {
                    for (int i = 0; i < 3; i++) {
                        rowsInline.get(k).add(i, new InlineKeyboardButton());


                        for (int m = prod; m < buttonName.size() - buttonMod; m++) {
                            rowsInline.get(k).get(i).setText(buttonName.get(m));

                            rowsInline.get(k).get(i).setCallbackData(prodName+" "+buttonName.get(m));


                            prod++;

                            break;
                        }
                    }

                } catch (Exception e) {


                }
            }
            if (buttonMod != 0) {
                if (buttonMod == 1) {
                    rowsInline.add(k, new LinkedList<InlineKeyboardButton>());
                    rowsInline.get(k).add(0, new InlineKeyboardButton());
                    rowsInline.get(k).get(0).setText(buttonName.get(buttonName.size() - 1));
                    rowsInline.get(k).get(0).setCallbackData(buttonName.get(buttonName.size() - 1));
                } else if (buttonMod == 2) {

                    rowsInline.add(k, new LinkedList<InlineKeyboardButton>());

                    rowsInline.get(k).add(0, new InlineKeyboardButton());
                    rowsInline.get(k).get(0).setText(buttonName.get(buttonName.size() - 1));
                    rowsInline.get(k).get(0).setCallbackData(buttonName.get(buttonName.size() - 1));
                    rowsInline.get(k).add(1, new InlineKeyboardButton());
                    rowsInline.get(k).get(1).setText(buttonName.get(buttonName.size() - 2));
                    rowsInline.get(k).get(1).setCallbackData(buttonName.get(buttonName.size() - 2));


                }
            }
            inlineKeyboardMarkup.setKeyboard(rowsInline);


            try {

                message3.setText(itemService.formattedString(prodName,prodDesc,prodPrice));
                message3.setReplyMarkup(inlineKeyboardMarkup);
                try {
                    message3.setChatId(update2.getMessage().getChatId());
                }catch (Exception x){
                    try {
                        message3.setChatId(update2.getCallbackQuery().getMessage().getChatId());
                    }catch (Exception q){
                        message3.setChatId(update2.getPreCheckoutQuery().getFrom().getId());
                    }
                }

                execute(message3);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void requestMobileNumberButton(String text, String buttonName, boolean isOneTimeKeyboard) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setText(text);
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        replyKeyboardMarkup.setResizeKeyboard(true);

        List<KeyboardRow> keyboardRowList = new LinkedList<>();
        KeyboardRow keyboardRow1 = new KeyboardRow();

        KeyboardButton keyboardButton1 = new KeyboardButton();
        keyboardButton1.setText(buttonName);
        keyboardButton1.setRequestContact(true);
        keyboardRow1.add(keyboardButton1);
        keyboardRowList.add(keyboardRow1);
        sendMessage.setReplyMarkup(replyKeyboardMarkup);
        replyKeyboardMarkup.setKeyboard(keyboardRowList);
        replyKeyboardMarkup.setOneTimeKeyboard(isOneTimeKeyboard);
        sendMessage.setChatId(update2.getMessage().getChatId());
        try {
            execute(sendMessage);
        } catch (Exception e) {
        }
    }


    SendMessage sendMessage2 = new SendMessage();

    public void sendButtons(String text, LinkedList<String> buttonText, boolean isContact, boolean isOneTimeKeyboard) {

        sendMessage2 = new SendMessage();
        sendMessage2.setText(text);
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        replyKeyboardMarkup.setResizeKeyboard(true);

        KeyboardRow keyboardRow1 = new KeyboardRow();
        KeyboardRow keyboardRow2 = new KeyboardRow();

        List<KeyboardRow> keyboardRowList = new LinkedList<>();

        LinkedList<KeyboardButton> keyboardButtons = new LinkedList<>();

        LinkedList<KeyboardButton> buttonLinkedList = new LinkedList<>();

        KeyboardButton keyboardButton1 = new KeyboardButton();
        KeyboardButton keyboardButton2 = new KeyboardButton();
        KeyboardButton keyboardButton3 = new KeyboardButton();
        KeyboardButton keyboardButton4 = new KeyboardButton();
        KeyboardButton keyboardButton5 = new KeyboardButton();
        KeyboardButton keyboardButton6 = new KeyboardButton();
        buttonLinkedList.add(keyboardButton1);
        buttonLinkedList.add(keyboardButton2);
        buttonLinkedList.add(keyboardButton3);
        buttonLinkedList.add(keyboardButton4);
        buttonLinkedList.add(keyboardButton5);
        buttonLinkedList.add(keyboardButton6);



        for (int i = 0; i < buttonText.size(); i++) {
            keyboardButtons.add(buttonLinkedList.get(i));
            keyboardButtons.get(i).setText(buttonText.get(i));
            keyboardButtons.get(i).setRequestContact(isContact);
            if (i % 3 != 0)
                keyboardRow1.add(keyboardButtons.get(i));
            if (i % 3 == 0) {
                keyboardRow2.add(keyboardButtons.get(i));
            }
        }



        keyboardRowList.add(keyboardRow1);
        keyboardRowList.add(keyboardRow2);


        replyKeyboardMarkup.setKeyboard(keyboardRowList);
        replyKeyboardMarkup.setOneTimeKeyboard(isOneTimeKeyboard);


        sendMessage2.setReplyMarkup(replyKeyboardMarkup);
        try {
            sendMessage2.setChatId(update2.getMessage().getChatId());
        }
        catch (Exception e)
        {
            try {
                sendMessage2.setChatId(update2.getCallbackQuery().getMessage().getChatId());
            }
            catch (Exception exception)
            {
                sendMessage2.setChatId(update2.getPreCheckoutQuery().getFrom().getId());
            }
        }

        try {
            execute(sendMessage2);

        } catch (Exception e) {
        } finally {
            sendMessage2.setReplyMarkup(null);
        }
    }


    @SneakyThrows
    @Override
    public void onUpdateReceived(Update update) { //overriding the telegram bot api method which takes and update that is a new message.
        update2 = update;


     AnswerPreCheckoutQuery answerPreCheckoutQuery =new AnswerPreCheckoutQuery();


        Logger logger = Logger.getLogger(RecklerBot.class.getSimpleName());
        logger.log(Level.INFO,"Firstname received");



        sendMessage2 = null;

        String command = "";


        try {
            if (update.getMessage().hasContact()) {
                System.out.println("1");
                String contact = String.valueOf(update2.getMessage().getContact().getPhoneNumber());
                long chatId = update2.getMessage().getContact().getUserId();
                String name = update2.getMessage().getContact().getFirstName();
                boolean isContactDetailsSaved = false;
                boolean isUserAlreadyRegistered = false;
                isUserAlreadyRegistered = userService.checkNoInDb(contact);
                if (isUserAlreadyRegistered) {
                    sendMessage("Welcome back " + name);
                    String categoryString = itemService.getCategories(categoryRepository);
                    LinkedList<String> categoriesList = itemService.getCategoryList(categoryRepository);
                    sendButtons(categoryString, categoriesList, false, true);
                } else {
                    isContactDetailsSaved = userService.saveUser(chatId, contact, name);
                    if (isContactDetailsSaved) {
                        sendMessage(contact + " Registered Successfully");
                        String categoryString = itemService.getCategories(categoryRepository);
                        LinkedList<String> categoriesList = itemService.getCategoryList(categoryRepository);
                        sendButtons(categoryString, categoriesList, false, true);


                    } else {
                        sendMessage("Server down please try again later");
                    }
                }


            }
            else {
                System.out.println("2");
                command = update.getMessage().getText();//storing the new message set by user in a string
            }
        } catch (Exception e) {
            try {
                System.out.println("3");
                command = update.getMessage().getText();
            } catch (Exception f) {
                try {
                    System.out.println("4");


                    command = update.getCallbackQuery().getData();
                } catch (Exception a) {
                    try {
                        System.out.println("5");
                        isSuccessfulPayment = m.hasSuccessfulPayment();
                        command = successfulPayment.getInvoicePayload();
                        int fun =successfulPayment.getTotalAmount();

                    }
                    catch(Exception x)
                    {



                    }

                }

            }

        }
        long chatUserId;

        if(update.hasPreCheckoutQuery()) {
            String boughtProductName = "";
            System.out.println("6");
            chatUserId = update.getPreCheckoutQuery().getFrom().getId();
            message.setChatId(chatUserId);
            message.setText("Your order will be delivered soon");
            LinkedList<String> button = new LinkedList<>();
            button.add("Back To Categories");
            button.add("View Order History");
            sendButtons("Order Placed Successfully✅", button, false, true);
            billService.saveOrder(chatUserId);
           LinkedList<OrderModel>orderHistoryList = orderRepository.findByUserId(chatUserId);
            long boughtProductId=(productRepository.findById(orderHistoryList.getLast().getProdId())).get().getProdId();
            Optional<ProductModel> productId = productRepository.findById(boughtProductId);
            boughtProductName=  productId.get().getName();
            long suggestedProdId = productId.get().getSuggestedProductId();
            LinkedList<ProductModel> productModels = productRepository.findProductByProductId(suggestedProdId);
            ButtonServiceForProducts buttonServiceForProducts = new ButtonServiceForProducts();
            Thread.sleep(2000);













//            long productModelsId = (productRepository.findById(orderHistoryList.getLast().getProdId())).get().getSuggestedProductId();
//            Optional<ProductModel> suggestedProduct = productRepository.findById(productModelsId);









            try{
                if (cartRepository.findByUuid(chatUserId)!=null) {
                    System.out.println(buyNowFlag+"Buy now flag");

                    if(buyNowFlag>0)
                    {
                      LinkedList<Cart>cartLinkedList= cartRepository.getCartByUserId(chatUserId);
                      cartRepository.delete(cartLinkedList.getLast());
                      buyNowFlag=0;
                    }
                    else {
                        cartRepository.deleteByUuid(chatUserId);
                        buyNowFlag=0;
                    }
                }
            }catch (Exception tushar){
                sendMessage(tushar.toString());
            }

            try {

                answerPreCheckoutQuery.setPreCheckoutQueryId(update.getPreCheckoutQuery().getId());
                answerPreCheckoutQuery.setOk(true);
                execute(answerPreCheckoutQuery);




            } catch (Exception w) {
                System.out.println(w);
            }

            try {
                execute(message);
                sendInlineButtonForMultipleProducts(buttonServiceForProducts.getButtons(),productModels.get(0).getPrice(),
                        productModels.get(0).getName(),
                        productModels.get(0).getDescription()+" \n\n"
                                +productModels.get(0).getPhotoPath());
                sendButtons("Would You like to buy "+ productModels.get(0).getName() +"\n (frequently bought together with "+boughtProductName+")",button,false,true);

            }catch (Exception q){
                System.out.println(q);
            }


        }

        else if(command==null) {
            System.out.println("7");

        }
      else if ((cartFlag == 1) && (userService.isDigit(command))) {
            System.out.println("8");
            long uuid = update.getMessage().getChatId();
            int quantity = itemService.stringToIntConverter(command);
            cartService.updateQuantity(quantity, cartRepository, uuid);
            cartFlag = 0;
          //  sendMessage("Product added to cart\uD83D\uDED2");
            LinkedList<String> keyButtons = new LinkedList<>();
            keyButtons.add("Show Cart");
            keyButtons.add("Show Categories");
            sendInlineButton(keyButtons,"Product added to cart\uD83D\uDED2");


        }
       else if((command != null)&& command.contains("Buy now") && command.length() >7)
       {
           System.out.println("9");

           String productName = command.replace("Buy now","").trim();
           LinkedList<ProductModel> product = productRepository.findByNameEquals(productName);


           long userId = update.getCallbackQuery().getMessage().getChatId();
           String companyName = "RetailBot";
           String payload = "this is payload";
           String description = "Shopping cart";

           LinkedList<Cart> cart = new LinkedList<>();
           cart.add(0,new Cart());
           cart.get(0).setId(0);
           cart.get(0).setUuid(userId);
        //   cart.get(0).setProductId(product.);
           cart.get(0).setProductId(product.get(0).getProdId());
           cart.get(0).setQuantity(1);
           cartRepository.save(cart.get(0));
           buyNowFlag=1;
       //    cartRepository.save(cart.get(0));
      //     LinkedList<Cart> cart = cartRepository.getCartByUserId(userId);
           CheckoutService checkoutService = new CheckoutService(userId, companyName, payload, description);
           SendInvoice sendInvoice = checkoutService.invoiceGenerator(cart, productRepository);



           try {
               execute(sendInvoice);
           } catch (Exception e) {
               System.out.println(e);
           }

       }else if(command.equals("View Order History")||command.equals("/orderhistory")){
          String orderList =  billService.orderHistory(update2.getMessage().getChatId());
            LinkedList<String> button = new LinkedList<>();
            button.add("Back To Categories");
           if (orderList.equals("Order History \n")){


               sendButtons("No previous orders", button, false, true);


           }
           else {
               System.out.println("10");
               LinkedList <String> filter = new LinkedList();
               filter.add("2020");
               filter.add("2021");
               filter.add("2022");



               sendButtons(billService.orderHistory(update2.getMessage().getChatId()), button, false, true);
               sendInlineButton(filter,"Past Orders - select year ↴");
           }


        }
        else if(command.equals("Delete all from cart"))
        {
            System.out.println("11");
            cartRepository.deleteByUuid(update.getMessage().getChatId());
            sendMessage("All products deleted from cart");
            String cartMessage ="No Items in cart\uD83D\uDC94";
            LinkedList<String> button = new LinkedList<>();
            button.add("Back To Categories");
            sendButtons(cartMessage, button, false, true);
        }
        else if (cartService.isShowCart(command)) {
            System.out.println("12");
            String cart=null;
            LinkedList<String> cartButtons = cartService.getCheckoutButton();
            try {
                cart = cartService.displayCart(update.getMessage().getChatId(), cartRepository, productRepository);
            } catch (Exception g) {
                cart = cartService.displayCart(update.getCallbackQuery().getMessage().getChatId(), cartRepository, productRepository);
            }
            if (cart.equals("No Items in cart\uD83D\uDC94")) {
                LinkedList<String> button = new LinkedList<>();
                button.add("Back To Categories");
                sendButtons(cart, button, false, true);
            } else {
                sendInlineButton(cartButtons, cart);
                String cartInstruction = "Use keyboard shortcuts to modify cart";
                LinkedList<String> keyboardButtons = cartService.getcartKeyboardButtons();
                sendButtons(cartInstruction, keyboardButtons, false, true);
            }



        } else if (command.equals("Checkout")) {
            System.out.println("13");
            long userId = update.getCallbackQuery().getMessage().getChatId();
            String companyName = "RetailBot";
            String payload = "this is payload";
            String description = "Shopping cart";
            LinkedList<Cart> cart = cartRepository.getCartByUserId(userId);
            CheckoutService checkoutService = new CheckoutService(userId, companyName, payload, description);
            SendInvoice sendInvoice = checkoutService.invoiceGenerator(cart, productRepository);

            successfulPayment.setInvoicePayload(sendInvoice.getPayload());
           successfulPayment.getProviderPaymentChargeId();


            try {
                execute(sendInvoice);
            } catch (Exception e) {
            }
        }


        else if (deleteFlag>0 && userService.isDigit(command)) {
            System.out.println("14");

            deleteFlag=0;
            sendMessage(cartService.deleteFromCart(Integer.parseInt(command),update2.getMessage().getChatId(),cartRepository));
            String cart=null;
            LinkedList<String> cartButtons = cartService.getCheckoutButton();
            try
            {
                cart = cartService.displayCart(update.getMessage().getChatId(), cartRepository, productRepository);
            }catch (Exception g){
                cart=cartService.displayCart(update.getCallbackQuery().getMessage().getChatId(),cartRepository,productRepository);
            }
            sendInlineButton(cartButtons,cart);
            String cartInstruction = "Use keyboard shortcuts to modify cart";
            LinkedList<String> keyboardButtons = cartService.getcartKeyboardButtons();
            sendButtons(cartInstruction,keyboardButtons,false,true);

            //  sendMessage(cartRepository.getCartByUserId(update.getMessage().getChatId()));
        }
        else if (itemService.recogniseAddToCart(command)) {
            System.out.println("15");
            long userId;
            try {

                 userId = update.getMessage().getChatId();
            }catch (Exception d){
                userId = update.getCallbackQuery().getMessage().getChatId();
            }
            String productsAddedInCartResult = cartService.addProductToCart(command, productRepository, cartProductRepository, userId, cartRepository);
            if (productsAddedInCartResult != null) {
                System.out.println("16");
                LinkedList<String> quantity = new LinkedList<>();
               quantity.add("4");
               quantity.add("1");
               quantity.add("2");
               quantity.add("5");
               quantity.add("3");


                sendButtons("Please specify quantity",quantity,false,true);
             //   sendMessage("Please specify quantity");

                cartFlag = 1;

//
            } else {
                System.out.println("17");
                sendMessage("Unable to add product into cart");
                LinkedList<String> categoriesList = itemService.getCategoryList(categoryRepository);
                sendButtons(itemService.getCategories(categoryRepository), categoriesList, false, true);

            }

        }
        // --- Process the /start command only once ---
        if ("/start".equals(command)) {
            // Only process /start if it comes as a message update.
            if (!update.hasMessage()) {
                return;
            }
            long chatId = update.getMessage().getChatId();
            long messageId = update.getMessage().getMessageId();
            long now = System.currentTimeMillis();

            // Check if we've processed a /start from this chat very recently (e.g., within 3 seconds)
            CommandData last = lastCommandMap.get(chatId);
            if (last != null && last.command.equals(command) && (now - last.timestamp < 8000)) {
                System.out.println("Duplicate /start detected within 3 seconds for chat: " + chatId);
                return;
            }
            // Record this command for this chat
            lastCommandMap.put(chatId, new CommandData(command, now));

            // Also check for duplicate message ID if needed
            if (processedMessages.contains(messageId)) {
                System.out.println("Ignoring duplicate /start message: " + messageId);
                return;
            }
            processedMessages.add(messageId);

            System.out.println("Handling /start command - only once");
            sendMessage("Welcome " + update.getMessage().getFrom().getFirstName());
            sendMessage("Type /help for help");
            requestMobileNumberButton("Please register using mobile number", "☎️ Click here to register", true);
            return;
        }
        else if (itemService.recogniseCategoryByName(command, categoryRepository.findAll()) != null) {
            System.out.println("19");
            //saving the category name returned by recogniseCategoryByName in a string
            String categoryReturned = itemService.recogniseCategoryByName(command, categoryRepository.findAll());
            int catId = itemService.getCategoryIdByCategory(categoryReturned, categoryRepository);//passing the above string and the category repo to the function getCategoryIdByCategory

            String msg = "Products from "+categoryReturned;
            LinkedList<String> productListByCategory = itemService.getProductsListByCategory(productRepository, catId);

            LinkedList<String> buttons = new LinkedList<>();
            buttons.add("Back to categories");
            buttons.add("Show Cart");
            sendInlineButton(productListByCategory,msg);
            sendButtons("Quick actions ↴",buttons,false,true);


        } else if (itemService.recogniseCategory(command) || command.equals("Show Categories")) {
            System.out.println("20");//recognise the word category from user's message and if true returning all the categories
//

            LinkedList<String> categoriesList = itemService.getCategoryList(categoryRepository);
            sendButtons(itemService.getCategories(categoryRepository), categoriesList, false, true);

        } else if (command.equals("/help")) {
            System.out.println("21");//help command to help user

            sendMessage("This is a retail bot to help you shop at storefront businesses online." + "\n" + "\n"
                    + "You can view all product categories the business offers as well as the products." + "\n" + "\n"
                    + "You can try these commands" + "\n" + "-> Show Categories" + "\n" + "\n");
        }


        else if(command.equals("Delete a product from cart"))
        {
            System.out.println("22");

            sendMessage("Please specify the number of product you want to delete↴");
            sendMessage(cartService.displayCart(update.getMessage().getChatId(),cartRepository,productRepository));
            deleteFlag++;

        }

        else if(command.equals("Update Quantity")){
            System.out.println("23");
           sendMessage(cartService.displayCart(update.getMessage().getChatId(),cartRepository,productRepository));

           sendMessage("Enter the number of the product to update its Qty");

            updateFlag =1;
       }
   else if (updateFlag==1) {
            System.out.println("24");
           try {
               cartProductNo = Integer.parseInt(command);
                int cartSize = cartRepository.getCartByUserId(update.getMessage().getChatId()).size();
                if (cartProductNo>cartSize){
                    sendMessage("Enter a valid number");
                }
                else {
                    sendMessage("Enter a new quantity for the product");
                    updateFlag=2;


                }

           }
           catch (Exception e)
           {

               sendMessage("Enter a valid number");

               System.out.println(e);
           }



       }

       else if(updateFlag==2&&userService.isDigit(command))
       {
           System.out.println("25");
           int quanity=1;

           cartProductNo = cartProductNo-1;
           try {


                quanity = Integer.parseInt(command);
           }catch (Exception e) {
               sendMessage("Enter a valid number");
               System.out.println(e);
           }


           long chatId = update.getMessage().getChatId();
         boolean result =   cartService.updateCart(cartProductNo,quanity,chatId,cartRepository);
           updateFlag=0;

           if(result)
           {
               System.out.println("26");
               LinkedList<String> cartButtons = cartService.getCheckoutButton();
               sendMessage("Quantity updated");
               sendInlineButton(cartButtons,cartService.displayCart(update.getMessage().getChatId(),cartRepository,productRepository));
               String cartInstruction = "Use keyboard shortcuts to modify cart";
               LinkedList<String> keyboardButtons = cartService.getcartKeyboardButtons();
               sendButtons(cartInstruction, keyboardButtons, false, true);


           }
           else
           {
               sendMessage("unable to update");
           }
       }

        else if ((productRepository.findByNameEquals(command).size()>0)) {
            System.out.println("27");
            ButtonServiceForProducts buttonService = new ButtonServiceForProducts();
            String productByButton = itemService.productByButton(command, productRepository);
            LinkedList<ProductModel> productModels = productRepository.findByNameEquals(productByButton);
            if (itemService.flag == 1) {

                buttonService.setProductName(productByButton);
                sendInlineButtonForMultipleProducts(buttonService.getButtons(), productModels.get(0).getPrice(),
                        productModels.get(0).getName(),
                        productModels.get(0).getDescription() + " \n\n" + productModels.getFirst().getPhotoPath());
                itemService.flag=0;
            }
        }
           else if (itemService.flag==0){
            ButtonServiceForProducts buttonService = new ButtonServiceForProducts();

            LinkedList<ProductModel> productModelLinkedList = itemService.productModels(command,productRepository);
               if(productModelLinkedList!=null) {
                   for (int i = 0; i < productModelLinkedList.size(); i++) {
                       String productInfo = productModelLinkedList.get(i).getDescription();
                       productInfo = productInfo.concat(String.valueOf(productModelLinkedList.get(i).getPrice()));
                       sendInlineButtonForMultipleProducts(buttonService.getButtons(), productModelLinkedList.get(i).getPrice(),
                               productModelLinkedList.get(i).getName(),
                               productModelLinkedList.get(i).getDescription()+" \n\n"+productModelLinkedList.get(i).getPhotoPath());
     //                  itemService.flag=1;
                   }
               }
           }






        else if (!(command.equals("")))//if the command does not match with any if returning can't recognise command message.
        {

          try {
              System.out.println("12345");
              yr = Integer.parseInt(command);
              System.out.println("67890");
              sendInlineButton(orderHistoryService.buttonsForMonths(),"orders "+yr + " - select period↴");

          }catch (Exception a) {
              System.out.println("asdfg");

              String months = command;
              System.out.println("xcvb");
//              "2021-09-27T00:00:01"
              String stDate="";
              String eDate ="";
              if(months.equals("Jan-Apr")||months.equals("May-Aug")||months.equals("Sept-Dec")){


                  switch (months)
                  {
                      case "Jan-Apr":
                          System.out.println("Jan Apr");
                         stDate=String.valueOf(yr)+"-01-01T00:00:01";
                          eDate=String.valueOf(yr)+"-04-30T23:59:59";
                          break;
                      case "May-Aug":
                          System.out.println("May Aug");
                           stDate=String.valueOf(yr)+"-05-01T00:00:01";
                          eDate=String.valueOf(yr)+"-08-31T23:59:59";
                          break;
                      case "Sept-Dec":
                          System.out.println("Sept Dec");
                           stDate=String.valueOf(yr)+"-09-01T00:00:01";
                          eDate=String.valueOf(yr)+"-12-31T23:59:59";
                          break;

                      default:
                          sendMessage("default "+"I don't recognize this command\uD83D\uDE13 yet, but I am still working on it\uD83D\uDEE0");


                  }
                  LocalDateTime startDate = LocalDateTime.parse(stDate);
                  LocalDateTime endDate = LocalDateTime.parse(eDate);
                  LinkedList<OrderModel>orderModels = orderRepository.getHistorySortByMonths(startDate,endDate);
                  if (orderModels.size()<1){
                      sendMessage("No orders");
                  }
                  else {
                      sendMessage(orderHistoryService.convertLinkedListToString(orderModels));

                  }


              }
          }
          try {
//              String months = command;
//              if (yr>2019&&yr<2025){
//
//
//                  sendMessage(orderHistoryService.convertLinkedListToString(orderRepository.getHistorySortByMonths(startDate,endDate)));
//
//
//              }
          }catch (Exception q){

//              sendMessage(1+"I don't recognize this command\uD83D\uDE13 yet, but I am still working on it\uD83D\uDEE0"+q.getMessage());

          }


        }
        else if (!(update.hasPreCheckoutQuery()||update.getMessage().hasContact()))
        {
            sendMessage(2+"I don't recognize this command\uD83D\uDE13 yet, but I am still working on it\uD83D\uDEE0");

        }



//


    }

    @Override
    public void onUpdatesReceived(List<Update> updates) {
        
        super.onUpdatesReceived(updates);
    }
}