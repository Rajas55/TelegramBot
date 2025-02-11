package Rajas.com.botRest.BotRest.Service;

import Rajas.com.botRest.BotRest.Entity.Cart;
import Rajas.com.botRest.BotRest.Entity.CartProductsModel;
import Rajas.com.botRest.BotRest.Entity.ProductModel;
import Rajas.com.botRest.BotRest.NLP.Lemmatization;
import Rajas.com.botRest.BotRest.NLP.Tokenize;
import Rajas.com.botRest.BotRest.Repository.CartProductRepository;
import Rajas.com.botRest.BotRest.Repository.CartRepository;
import Rajas.com.botRest.BotRest.Repository.ProductRepository;


import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Optional;
import java.util.Random;

public class CartService {
    Lemmatization lemmatization = new Lemmatization();
    Tokenize tokenize = new Tokenize();
    ItemService itemService = new ItemService();
    CartProductsModel cartProductsModel = new CartProductsModel();
    Cart cart = new Cart();
    static int id;
    Random random= new Random();




    public LinkedList<String> getcartKeyboardButtons()
    {
        LinkedList<String> keyboardButtons = new LinkedList<>();
        keyboardButtons.add("Delete a product from cart");
        keyboardButtons.add("Delete all from cart");
        keyboardButtons.add("Update Quantity");
        keyboardButtons.add("Back to categories");
        return keyboardButtons;
    }

    public LinkedList<String>  getCartInstruction()
    {
        LinkedList<String> cartButtons = new LinkedList<>();
        cartButtons.add("Checkout");
        return cartButtons;
    }

    public LinkedList<String> getCheckoutButton()
    {
        LinkedList<String> cartButtons = new LinkedList<>();
        cartButtons.add("Checkout");
        return cartButtons;
    }
    public String addProductToCart(String command, ProductRepository productRepository, CartProductRepository cartProductRepository, long userId, CartRepository cartRepository)
    {

        String tokenizedCommand = tokenize.tokenization(command);
        LinkedList<ProductModel>allProductsList = productRepository.allProductsList();
        LinkedList<Cart> cartLinkedList =  cartRepository.getCartByUserId(userId);
        for(int i=0;i<allProductsList.size();i++) {
            if (tokenizedCommand.contains(allProductsList.get(i).getName()))
            {
//                if (cartLinkedList.get(i).getProductId()==allProductsList.get(i).getProdId())
//                {
//                    long cartId = allProductsList.get(i).getProdId();
//                    int quantity = allProductsList.get(i).getQuantity();
//
//                }
                cart.setId(random.nextInt(0,2147483647));
                cart.setUuid(userId);
                cart.setProductId(allProductsList.get(i).getProdId());
                cart.setQuantity(0);


                cartRepository.save(cart);
                cartProductsModel.setCartId(userId);
                cartProductsModel.setProdId(allProductsList.get(i).getProdId());
               id = (int) cartProductRepository.count();

               id++;
                cartProductsModel.setId(id);
                cartProductsModel.setQuantity(1);

                cartProductRepository.save(cartProductsModel);
//                String newCommand = tokenize.tokenization(rootString);
                return  allProductsList.get(i).getName();
            }
            }
        return null;
        }

        public boolean isShowCart(String command)
        {
            String tokenizedCommand = tokenize.tokenization(command);
            if(((tokenizedCommand.contains("Show"))&&(tokenizedCommand.contains("Cart")))||command.equals("/showcart")){
                return true;
            }
            else
            {
                return false;
            }
        }

        public String cartToStringConvertor()
        {

            return null;
        }

        public String displayCart(long uuid,CartRepository cartRepository,ProductRepository productRepository)
        {
            try {
                String productInfo = "";
                LinkedList<Cart> getCartByUserId;
                getCartByUserId = cartRepository.getCartByUserId(uuid);

                LinkedList<Cart> cartByUserWithIndex = new LinkedList<>();

                for(int i=0;i<getCartByUserId.size();i++)
                {
                  cartByUserWithIndex.add(i,getCartByUserId.get(i));
                }
                if (!(getCartByUserId.isEmpty())) {
                    productInfo =productInfo.concat("Shopping Cart\uD83D\uDED2"+"\n"+"\n");

                    for (int i =0 ; i < getCartByUserId.size(); i++) {
                        Optional<ProductModel> product = productRepository.findById(getCartByUserId.get(i).getProductId());
                        productInfo = productInfo.concat(String.valueOf(cartByUserWithIndex.indexOf(getCartByUserId.get(i))+1));
                        productInfo = productInfo.concat(". "+product.get().getName()+"   qty: ");
                        productInfo = productInfo.concat(String.valueOf((cartRepository.getCartByUserId(uuid).get(i).getQuantity())));
                        productInfo =productInfo.concat("    $");
                        productInfo = productInfo.concat(String.valueOf(product.get().getPrice()));
                        productInfo =productInfo.concat("/-"+"\n");

                    }
                    return productInfo;
                }
                else{
                    return "No Items in cart\uD83D\uDC94";

                }
            }catch (Exception e)
            {

                return e.toString();
            }
        }

        public String deleteFromCart(int productId,long uuid,CartRepository cartRepository)
        {
            productId =productId-1;
            LinkedList<Cart> getCartByUserId;
            getCartByUserId = cartRepository.getCartByUserId(uuid);

            LinkedList<Cart> cartByUserWithIndex = new LinkedList<>();

            for(int i=0;i<getCartByUserId.size();i++)
            {
                cartByUserWithIndex.add(i,getCartByUserId.get(i));
            }
            try {
                Cart productToDelete = cartByUserWithIndex.get(productId);
              //  String productName = String.valueOf(productToDelete.getProducts());
                cartRepository.delete(productToDelete);
                return "Product removed from cart";
            }
            catch (Exception e)
            {
                return "unable to delete product ";
            }
        }


        public boolean updateCart(int productNo,int quantity,long uuid,CartRepository cartRepository)
        {
            try {
                LinkedList<Cart> userCart = cartRepository.getCartByUserId(uuid);
                LinkedList<Cart> userCart2 = new LinkedList<>();
                for (int i = 0; i < userCart.size(); i++) {
                    userCart2.add(i, userCart.get(i));
                }
                Cart productToUpdate = userCart2.get(productNo);
                productToUpdate.setQuantity(quantity);
                cartRepository.save(productToUpdate);
                return true;
            }
            catch (Exception e)
            {
                return  false;
            }
        }
       public boolean updateQuantity(int quantity, CartRepository cartRepository,long uuid)
        {
            Cart lastProductId,product;
            int last=0;
            int quantFlag=0;
         try {

             LinkedList<Cart> cart = cartRepository.getCartByUserId(uuid);
             lastProductId=cart.getLast();
             for (int i = 0 ; i<cart.size()-1;i++){
                 if (cart.get(i).getProductId()==lastProductId.getProductId()){
                     product=cart.get(i);
                    int quant =  product.getQuantity();
                    product.setQuantity(quant+quantity);
                    cartRepository.save(product);
                    cartRepository.delete(lastProductId);
                    quantFlag++;
                 }


             }

             last=cart.size();
//            Optional<Cart> lastProduct = cartRepository.getCartByUserId(uuid);
             Cart lastProduct = cart.getLast();




                if (quantFlag==0) {

                    int prodQuantity = lastProduct.getQuantity();
                    lastProduct.setQuantity(quantity);
                    cartRepository.save(lastProduct);
                    return true;
                }




               return true;
         }
         catch (Exception e)
         {
             return  false;
         }
        }


    }
