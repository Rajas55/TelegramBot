package Rajas.com.botRest.BotRest.NLP;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.pipeline.CoreDocument;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;

import java.util.ArrayList;
import java.util.List;

public class Tokenize {
   public String tokenization (String command){
       StanfordCoreNLP stanfordCoreNLP = pipeline.getPipeline();
       String pascal,pascal1,newCommand="";
       CoreDocument coreDocument = new CoreDocument(command);
       stanfordCoreNLP.annotate(coreDocument);

       List<CoreLabel> coreLabelList = coreDocument.tokens();
       for (CoreLabel coreLabel : coreLabelList){
           pascal = String.valueOf(coreLabel.originalText());
           pascal1 = pascal.substring(0,1).toUpperCase()+pascal.substring(1).toLowerCase();
           newCommand = newCommand.concat(pascal1)+" ";


       }
       return newCommand;

       
   }


}
