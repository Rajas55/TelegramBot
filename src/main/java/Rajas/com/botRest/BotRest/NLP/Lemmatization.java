package Rajas.com.botRest.BotRest.NLP;

import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.pipeline.CoreDocument;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;

import java.util.List;

public class Lemmatization {
    public String getLemma(String command) {
        String newLemma = "";
        StanfordCoreNLP stanfordCoreNLP = pipeline.getPipeline();
        CoreDocument coreDocument = new CoreDocument(command);
        stanfordCoreNLP.annotate(coreDocument);
        List<CoreLabel> coreLabelList = coreDocument.tokens();
        for (CoreLabel coreLabel : coreLabelList) {
            String lemma = coreLabel.lemma();
            newLemma = newLemma.concat(lemma)+" ";
        }
        return newLemma;
    }
    }


