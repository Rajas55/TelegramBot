package Rajas.com.botRest.BotRest;

import Rajas.com.botRest.BotRest.Controller.UserController;
import Rajas.com.botRest.BotRest.Entity.Cart;
import Rajas.com.botRest.BotRest.Entity.ProductModel;
import Rajas.com.botRest.BotRest.NLP.Tokenize;
import Rajas.com.botRest.BotRest.Repository.*;
import Rajas.com.botRest.BotRest.Service.ButtonServiceForProducts;
import Rajas.com.botRest.BotRest.Service.CartService;
import Rajas.com.botRest.BotRest.Service.ItemService;
import Rajas.com.botRest.BotRest.Service.UserService;
import lombok.SneakyThrows;
import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.methods.invoices.SendInvoice;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardRemove;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import static java.lang.Boolean.TRUE;

//service class/main bot class which extends telegram polling bot(Has all the commands
@Service
public class RecklerBot extends TelegramLongPollingBot {
    int categoryFlag = 0;

    int deleteFlag = 0;
    int cartFlag = 0;
    String mobNo = null;
    SendMessage message = new SendMessage(); //new object for SendMessage predefined by telegram bot api
    Update update2;
    ItemService itemService = new ItemService(); //object for the service class which contains the business logic
    Tokenize tokenize = new Tokenize(); //new object for tokenize class that is nlp/word separation
    //connecting the UserService Class
    @Autowired
    private UserService userService;
    CartService cartService = new CartService();

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

//    CartService cartService = new CartService();

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

    public void sendMessage(String chat) {
        try
        {
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
            message.setChatId(update2.getCallbackQuery().getMessage().getChatId());

            try {
                execute(message);
            } catch (
                    TelegramApiException q) {
                e.printStackTrace();
            }

        }

    }



    public  void sendInlineButton(ArrayList<String> buttonName,String productString) {
        if (buttonName.size()==0){
            sendMessage("No products available in this category\uD83D\uDE22");
        }
        else {

            SendMessage message3 = new SendMessage();
            InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
            List<List<InlineKeyboardButton>> rowsInline = new ArrayList<>();
//        List<InlineKeyboardButton> rowInline = new ArrayList<>();
            List<InlineKeyboardButton> rowInline2 = new ArrayList<>();
            InlineKeyboardButton priceBtn = new InlineKeyboardButton();
            InlineKeyboardButton priceBtn2 = new InlineKeyboardButton();
            LinkedList<String> str = new LinkedList<>();

            int buttonDivider = buttonName.size() / 3;
            int buttonMod = buttonName.size() % 3;
            //   buttonDivider+=1;


            int prod = 0;
            int k;
            System.out.println("-------------1");
            for (k = 0; k < buttonDivider; k++) {
                System.out.println("---------2");
                rowsInline.add(k, new ArrayList<InlineKeyboardButton>());
                System.out.println("-------------3");
                try {
                    //    System.out.println
                    for (int i = 0; i < 3; i++) {
                        System.out.println("-------------5");
                        rowsInline.get(k).add(i, new InlineKeyboardButton());


                        for (int m = prod; m < buttonName.size() - buttonMod; m++) {
                            rowsInline.get(k).get(i).setText(buttonName.get(m));
                            rowsInline.get(k).get(i).setCallbackData(buttonName.get(m));


                            prod++;

                            break;
                        }
                    }

                } catch (Exception e) {

                    System.out.println("im in catch " + e);

                }
            }
            if (buttonMod != 0) {
                if (buttonMod == 1) {
                    rowsInline.add(k, new ArrayList<InlineKeyboardButton>());
                    rowsInline.get(k).add(0, new InlineKeyboardButton());
                    rowsInline.get(k).get(0).setText(buttonName.get(buttonName.size() - 1));
                    rowsInline.get(k).get(0).setCallbackData(buttonName.get(buttonName.size() - 1));
                } else if (buttonMod == 2) {

                    rowsInline.add(k, new ArrayList<InlineKeyboardButton>());

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

                System.out.println(message3.getReplyMarkup());
                execute(message3);
            } catch (Exception e) {
                System.out.println(e);
                e.printStackTrace();
            }
        }
    }
    public  void sendInlineButtonForMultipleProducts(ArrayList<String> buttonName,int prodPrice,String prodName,String prodDesc) {
        if (buttonName.size()==0){
            sendMessage("No products available in this category\uD83D\uDE22");
        }
        else {

            SendMessage message3 = new SendMessage();
            InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
            List<List<InlineKeyboardButton>> rowsInline = new ArrayList<>();
//        List<InlineKeyboardButton> rowInline = new ArrayList<>();
            List<InlineKeyboardButton> rowInline2 = new ArrayList<>();
            InlineKeyboardButton priceBtn = new InlineKeyboardButton();
            InlineKeyboardButton priceBtn2 = new InlineKeyboardButton();
            LinkedList<String> str = new LinkedList<>();

            int buttonDivider = buttonName.size() / 3;
            int buttonMod = buttonName.size() % 3;
            //   buttonDivider+=1;


            int prod = 0;
            int k;
            System.out.println("-------------1");
            for (k = 0; k < buttonDivider; k++) {
                System.out.println("---------2");
                rowsInline.add(k, new ArrayList<InlineKeyboardButton>());
                System.out.println("-------------3");
                try {
                    //    System.out.println
                    for (int i = 0; i < 3; i++) {
                        System.out.println("-------------5");
                        rowsInline.get(k).add(i, new InlineKeyboardButton());


                        for (int m = prod; m < buttonName.size() - buttonMod; m++) {
                            rowsInline.get(k).get(i).setText(buttonName.get(m));

                            rowsInline.get(k).get(i).setCallbackData(prodName+" "+buttonName.get(m));


                            prod++;

                            break;
                        }
                    }

                } catch (Exception e) {

                    System.out.println("im in catch " + e);

                }
            }
            if (buttonMod != 0) {
                if (buttonMod == 1) {
                    rowsInline.add(k, new ArrayList<InlineKeyboardButton>());
                    rowsInline.get(k).add(0, new InlineKeyboardButton());
                    rowsInline.get(k).get(0).setText(buttonName.get(buttonName.size() - 1));
                    rowsInline.get(k).get(0).setCallbackData(buttonName.get(buttonName.size() - 1));
                } else if (buttonMod == 2) {

                    rowsInline.add(k, new ArrayList<InlineKeyboardButton>());

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
                    message3.setChatId( update2.getCallbackQuery().getMessage().getChatId());
                }

                System.out.println(message3.getReplyMarkup());
                execute(message3);
            } catch (Exception e) {
                System.out.println(e);
                e.printStackTrace();
            }
        }
    }

    public void requestMobileNumberButton(String text, String buttonName, boolean isOneTimeKeyboard) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setText(text);
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        replyKeyboardMarkup.setResizeKeyboard(true);

        List<KeyboardRow> keyboardRowList = new ArrayList<>();
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
            System.out.println(e + "i am exception");
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

        LinkedList<KeyboardButton> buttonArrayList = new LinkedList<>();

        KeyboardButton keyboardButton1 = new KeyboardButton();
        KeyboardButton keyboardButton2 = new KeyboardButton();
        KeyboardButton keyboardButton3 = new KeyboardButton();
        KeyboardButton keyboardButton4 = new KeyboardButton();
        KeyboardButton keyboardButton5 = new KeyboardButton();
        KeyboardButton keyboardButton6 = new KeyboardButton();
        buttonArrayList.add(keyboardButton1);
        buttonArrayList.add(keyboardButton2);
        buttonArrayList.add(keyboardButton3);
        buttonArrayList.add(keyboardButton4);
        buttonArrayList.add(keyboardButton5);
        buttonArrayList.add(keyboardButton6);


        //  keyboardButton1.setText(String.valueOf(buttonText));
        //  keyboardButtons.add(keyboardButton1);
        for (int i = 0; i < buttonText.size(); i++) {
            keyboardButtons.add(buttonArrayList.get(i));
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
            sendMessage2.setChatId(update2.getCallbackQuery().getMessage().getChatId());
        }

        try {
            execute(sendMessage2);

        } catch (Exception e) {
            System.out.println(e);
        } finally {
            sendMessage2.setReplyMarkup(null);
        }
    }


    @SneakyThrows
    @Override
    public void onUpdateReceived(Update update) { //overriding the telegram bot api method which takes and update that is a new message.
        update2 = update;

        sendMessage2 = null;
//        sendMessage2.setReplyMarkup(null);

        //   System.out.println(update.getMessage().getText()); //to get the text sent by user in the console.
         //to get the first name of user in console.
        String command = "";
        try {
            if (update.getMessage().hasContact()) {
                String contact = String.valueOf(update2.getMessage().getContact().getPhoneNumber());
                long chatId = update2.getMessage().getContact().getUserId();
                String name = update2.getMessage().getContact().getFirstName();
                boolean isContactDetailsSaved = false;
                boolean isUserAlreadyRegistered = false;
                isUserAlreadyRegistered = userService.checkNoInDb(contact);
                if (isUserAlreadyRegistered) {
                    sendMessage("Welcome back " + name);
                    // sendMessage(itemService.getCategories(categoryRepository));
                    String categoryString = itemService.getCategories(categoryRepository);
                    LinkedList<String> categoriesList = itemService.getCategoryList(categoryRepository);
                    sendButtons(categoryString, categoriesList, false, true);
                } else {
                    isContactDetailsSaved = userService.saveUser(chatId, contact, name);
                    if (isContactDetailsSaved) {
                        sendMessage(contact + " Registered Successfully");
                        //   sendMessage(itemService.getCategories(categoryRepository));
                        String categoryString = itemService.getCategories(categoryRepository);
                        LinkedList<String> categoriesList = itemService.getCategoryList(categoryRepository);
                        sendButtons(categoryString, categoriesList, false, true);


                    } else {
                        sendMessage("Server down please try again later");
                    }
                }

            } else {
                command = update.getMessage().getText();//storing the new message set by user in a string
                if (itemService.recogniseCategory(command)) {
                    categoryFlag = 1;
                    String categoryString = itemService.getCategories(categoryRepository);
                    LinkedList<String> categoriesList = itemService.getCategoryList(categoryRepository);
                    sendButtons(categoryString, categoriesList, false, true);


                }


            }
        }catch (Exception e){
            try {
                command = update.getMessage().getText();
                System.out.println(e);
            }catch (Exception f){
               command =  update.getCallbackQuery().getData();

            }

        }
        if ((cartFlag == 1) && (userService.isDigit(command))) {
            long uuid = update.getMessage().getChatId();
            int quantity = itemService.stringToIntConverter(command);
            cartService.updateQuantity(quantity, cartRepository, uuid);
            cartFlag = 0;
          //  sendMessage("Product added to cart\uD83D\uDED2");
            ArrayList<String> keyButtons = new ArrayList<>();
            keyButtons.add("Show Cart");
            keyButtons.add("Show Categories");
            sendInlineButton(keyButtons,"Product added to cart\uD83D\uDED2");
        }
        else if (cartService.isShowCart(command)) {
            System.out.println("innnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnn");
            String cart = null;
            ArrayList<String> cartButtons = new ArrayList<>();
            cartButtons.add("Checkout");
            //   sendButtons();
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
                LinkedList<String> keyboardButtons = new LinkedList<>();
                keyboardButtons.add("Delete a product from cart");
                keyboardButtons.add("Update Quantity");
                keyboardButtons.add("Back to categories");
                sendButtons(cartInstruction, keyboardButtons, false, true);
            }



        }
        else if (deleteFlag>0 && userService.isDigit(command)) {

            deleteFlag=0;
            sendMessage(cartService.deleteFromCart(Integer.parseInt(command),update2.getMessage().getChatId(),cartRepository));
       //     sendMessage(cartService.displayCart(update.getMessage().getChatId(),cartRepository,productRepository));
            String cart=null;
            ArrayList<String> cartButtons = new ArrayList<>();
            cartButtons.add("Checkout");
            //   sendButtons();
            try
            {
                cart = cartService.displayCart(update.getMessage().getChatId(), cartRepository, productRepository);
            }catch (Exception g){
                cart=cartService.displayCart(update.getCallbackQuery().getMessage().getChatId(),cartRepository,productRepository);
            }
            sendInlineButton(cartButtons,cart);
            String cartInstruction = "Use keyboard shortcuts to modify cart";
            LinkedList<String> keyboardButtons = new LinkedList<>();
            keyboardButtons.add("Delete a product from cart");
            keyboardButtons.add("Update Quantity");
            keyboardButtons.add("Back to categories");
            sendButtons(cartInstruction,keyboardButtons,false,true);

            //  sendMessage(cartRepository.getCartByUserId(update.getMessage().getChatId()));
        }
        else if (itemService.recogniseAddToCart(command)) {
            long userId;
            try {

                 userId = update.getMessage().getChatId();
            }catch (Exception d){
                userId = update.getCallbackQuery().getMessage().getChatId();
            }
            String productsAddedInCartResult = cartService.addProductToCart(command, productRepository, cartProductRepository, userId, cartRepository);
            if (productsAddedInCartResult != null) {
                LinkedList<String> quantity = new LinkedList<>();
                quantity.add("1");
                quantity.add("2");
                quantity.add("3");
                quantity.add("4");
                quantity.add("5");


                sendButtons("Please specify quantity",quantity,false,true);
             //   sendMessage("Please specify quantity");

                cartFlag = 1;

//            sendMessage(productsAddedInCartResult);
//            sendMessage("Added to cart");
            } else {
                sendMessage("Unable to add product into cart");
                LinkedList<String> categoriesList = itemService.getCategoryList(categoryRepository);
                sendButtons(itemService.getCategories(categoryRepository), categoriesList, false, true);

            }


        }
        else if (command.equals("/start")) { //command.equals function to check if user entered command has a defined word.
            sendMessage("Welcome " + update.getMessage().getFrom().getFirstName());//sending welcome and user's first name.
            sendMessage("Type /help for help");
//            sendMessage(userService.registerUser()); // register user function in user service to send message "Enter you number".
            String text = "Please enter your number" + "\n" + "\n" + "You can edit/change your number just by typing a new one.";
            requestMobileNumberButton(text, "Send Contact Information", true);
            System.out.println("me ithe aahe");


            //Passing the command and list of categories to the recogniseCategoryByName function in itemService
        } else if (itemService.recogniseCategoryByName(command, categoryRepository.findAll()) != null) {
            //saving the category name returned by recogniseCategoryByName in a string
            String categoryReturned = itemService.recogniseCategoryByName(command, categoryRepository.findAll());
            int catId = itemService.getCategoryIdByCategory(categoryReturned, categoryRepository);//passing the above string and the category repo to the function getCategoryIdByCategory

            String msg = "Products from "+categoryReturned;
            ArrayList<String> productListByCategory = itemService.getProductsListByCategory(productRepository, catId);

            LinkedList<String> buttons = new LinkedList<>();
            buttons.add("Back to categories");
            buttons.add("Show Cart");
            sendInlineButton(productListByCategory,msg);
            sendButtons("Quick actions ↴",buttons,false,true);


        } else if (itemService.recogniseCategory(command)&& command.equals("Show Categories")) { //recognise the word category from user's message and if true returning all the categories
//            categoryFlag = 1;
            sendMessage(itemService.getCategories(categoryRepository));
         //   sendMessage("innnnnnnnn");
        } else if (command.equals("/help")) { //help command to help user

            sendMessage("This is a retail bot to help you shop at storefront businesses online." + "\n" + "\n"
                    + "You can view all product categories the business offers as well as the products." + "\n" + "\n"
                    + "You can try these commands" + "\n" + "-> Show Categories" + "\n" + "\n"
                    + "You can also try Hinglish commands like" + "\n" + "-> Fashion dikhao");
        }


        else if(command.equals("Delete a product from cart"))
        {

            sendMessage("Please specify the number of product you want to delete↴");
            sendMessage(cartService.displayCart(update.getMessage().getChatId(),cartRepository,productRepository));
            deleteFlag++;

        }  else if ((productRepository.findByNameEquals(command)!=null))
        {
            ButtonServiceForProducts buttonService = new ButtonServiceForProducts();
//           ArrayList<ButtonServiceForProducts> buttonServiceForProducts = new ArrayList<>();
            System.out.println("In product by buttons-------------");
            String productByButton =  itemService.productByButton(command,productRepository);
            ArrayList<ProductModel> productModels = productRepository.findByNameEquals(productByButton);
            System.out.println(productByButton+" product by button");
            if (itemService.flag==1) {

                buttonService.setProductName(productByButton);
                sendInlineButtonForMultipleProducts(buttonService.getButtons(),productModels.get(0).getPrice(),
                        productModels.get(0).getName(),
                        productModels.get(0).getDescription());
            }
            else if (itemService.flag==0){
                ArrayList<ProductModel> productModelArrayList = itemService.productModels(command,productRepository);
                if(productModelArrayList!=null) {
                    for (int i = 0; i < productModelArrayList.size(); i++) {
                        String productInfo = productModelArrayList.get(i).getDescription();
                        productInfo = productInfo.concat(String.valueOf(productModelArrayList.get(i).getPrice()));
//                    sendMessage(productInfo);
                        sendInlineButtonForMultipleProducts(buttonService.getButtons(), productModelArrayList.get(i).getPrice(),
                                productModelArrayList.get(i).getName(),
                                productModelArrayList.get(i).getDescription());

                    }
                }
            }

//            productRepository.findByNameContaining()
//            buttonServiceForProducts.add(buttonService.setProductName()).

//            sendMessage(productByButton);
        }
        else if (itemService.productModels(command, productRepository) != null) {
            //passing the text to productModels function to directly return any product if it matches with the user's text
            ArrayList<ProductModel> productByName = itemService.productModels(command, productRepository);//saving  the matched products in an arrayList
            String productsByName = itemService.stringConverterForProductList(productByName);//passing the arrayList to stringConverterForProductList to covert array to string
            sendMessage(productsByName);//returning the string i.e. the matched products list

        }

        else if (command.equals("/hey")) {
//          sendInlineButton();
        } else if (!(command.equals("")))//if the command does not match with any if returning can't recognise command message.
        {

            System.out.println("----------------->" + tokenize.tokenization(command));
            sendMessage("I don't recognize this command\uD83D\uDE13 yet, but I am still working on it\uD83D\uDEE0");
        }


//


    }

    @Override
    public void onUpdatesReceived(List<Update> updates) {
        super.onUpdatesReceived(updates);
    }
}