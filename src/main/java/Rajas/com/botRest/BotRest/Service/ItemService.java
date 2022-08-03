package Rajas.com.botRest.BotRest.Service;

import Rajas.com.botRest.BotRest.Entity.CategoryModel;
import Rajas.com.botRest.BotRest.Entity.ProductModel;
import Rajas.com.botRest.BotRest.NLP.Lemmatization;
import Rajas.com.botRest.BotRest.NLP.Tokenize;
import Rajas.com.botRest.BotRest.RecklerBot;
import Rajas.com.botRest.BotRest.Repository.CategoryRepository;
import Rajas.com.botRest.BotRest.Repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ItemService {


    Lemmatization lemmatization = new Lemmatization();
    Tokenize tokenize = new Tokenize();
    private CategoryRepository categoryRepository;

    public String getCategories(CategoryRepository categoryRepository) {
        String categoryList = "All Categories" + "\n" + "\n";

        List<CategoryModel> categories = categoryRepository.findAll();
        for (CategoryModel category : categories) {
            categoryList = categoryList.concat(category.getCatId() + ". ");
            categoryList = categoryList.concat(category.getCategory() + "\n");


        }
        return categoryList;
    }

    public int getCategoryIdByCategory(String category, CategoryRepository categoryRepository) {
        return categoryRepository.getCategoryId(category);
    }

    public String getProducts(ProductRepository productRepository, int catId) {
        ArrayList<ProductModel> products = productRepository.getProductsByCategoryId(catId);
        String productDetails = "";
        for (ProductModel productModel : products) {
            productDetails = productDetails.concat("-> ");
            productDetails = productDetails.concat(productModel.getName() + "   " + "₹" + productModel.getPrice() + "/-" + "\n");

        }
        return productDetails;

    }


    public ArrayList<String> getProductsListByCategory(ProductRepository productRepository, int catId) {
        ArrayList<ProductModel> products = productRepository.getProductsByCategoryId(catId);
        ArrayList<String> productNameList = new ArrayList<>();
        for (ProductModel productModel : products) {
            productNameList.add(productModel.getName());
        }
        return productNameList;

    }




    public ArrayList<String> getCategoryList(CategoryRepository categoryRepository)
    {
        ArrayList<String> categoryList =new ArrayList<>();
        List<CategoryModel> categories = categoryRepository.findAll();
        int i=0;
        for(CategoryModel categoryModel: categories)
        {
            categoryList.add(categories.get(i).getCategory());
            i++;
        }
        return  categoryList;
    }

    public String recogniseCategoryByName(String command, List<CategoryModel> categories) {
        String rootString = lemmatization.getLemma(command);
        String newCommand = tokenize.tokenization(rootString);
        for (int i = 0; i < categories.size(); i++) {
            if (newCommand.contains(categories.get(i).getCategory())) {
                return categories.get(i).getCategory();
            }
        }
        return null;
    }

    public boolean recogniseCategory(String command) {
        String rootString = lemmatization.getLemma(command);
        String newCommand = tokenize.tokenization(rootString);
        if (newCommand.contains("Category")) {
            return true;
        } else {
            return false;
        }

    }
// returns only one product which is matching with user command
    public ArrayList<ProductModel> productModels(String command, ProductRepository productRepository) {

        int i = 0;
        ArrayList<ProductModel> productByName = null;
        String rootString = lemmatization.getLemma(command);
//        String newCommand = tokenize.tokenization(rootString);

        String[] meaningfulWords = rootString.split(" ");
        String[] arr = new String[meaningfulWords.length];

        for (i = 0; i < meaningfulWords.length; i++) {
            if (meaningfulWords[i].length() > 2) {
                arr[i] = meaningfulWords[i];
            }
        }
//
        for (i = 0; i < arr.length; i++) {
            if (productRepository.findByNameContaining(arr[i]).size() != 0) {
                System.out.println("Inside for loop");
                productByName = productRepository.findByNameContaining(arr[i]);
                break;
//
            }
        }
//
        System.out.println(productByName);
        return productByName;
    }

    public String stringConverterForProductList(ArrayList<ProductModel> productModels) {
        int i = 0;
        String products="";
        if(productModels!=null) {
            for (ProductModel productModel : productModels) {
                products = products.concat("->");
                products = products.concat(productModels.get(i).getName() + "\t" + "₹");
                products = products.concat(String.valueOf(productModels.get(i).getPrice()) + "\n");
                products = products.concat("Desc: " + productModels.get(i).getDescription() + "\n" + "\n");
                i++;
            }
            System.out.println(products);
        }
        return products;
    }

    public int stringToIntConverter(String command) {
        int number = 0;
        try {
            number = Integer.parseInt(command);
        } catch (Exception e) {
            System.out.println(e);
        }
        return number;
    }

    public String getProductsByCategoryId(ProductRepository productRepository, int id) {
        String products = getProducts(productRepository, id);
        return products;
    }

                
                


    public boolean recogniseAddToCart(String command) {
        String rootString = lemmatization.getLemma(command);
        String newCommand = tokenize.tokenization(rootString);
//        System.out.println("Recognise--------->"+newCommand);
        if ((newCommand.contains("Put")) || (newCommand.contains("Insert")) || (newCommand.contains("Add"))&&(newCommand.contains("Cart"))) {
            return true;
        } else {
            return false;
        }

    }
public int flag=0;
    public String productByButton(String command,ProductRepository productRepository){
        ArrayList<ProductModel>productModels= productRepository.findByNameEquals(command);
        String productName ="";
        if(productModels.isEmpty())
        {

        }
        else {
            productName = productModels.get(0).getName();
        }
        if (productName.length()>2)
        {
            flag=1;
            System.out.println("In if of product by button");
            return productName;
        }
        else {
            flag=0;
            System.out.println("In else of product by button");
            ButtonServiceForProducts buttonService = new ButtonServiceForProducts();
            ArrayList<ButtonServiceForProducts> buttonServiceForProducts = new ArrayList<>();
            ArrayList<ProductModel> productModelArrayList = productModels(command,productRepository);
//            for (int i = 0 ; i< productModelArrayList.size();i++){
//              buttonServiceForProducts.add(i, new ButtonServiceForProducts());
//              buttonServiceForProducts.get(i).setProductName(productModelArrayList.get(i).getName());
////              setProductName(productModelArrayList.get(i).getName()));
//
//
//            }

           return stringConverterForProductList(productModelArrayList);


        }



    }
    public String formattedString(String prodName,String prodDescription,int prodPrice){
        String formatString = "-> "+prodName +"\n"+"₹ "+ prodPrice +"\n"+prodDescription;
        return formatString;
    }
}
