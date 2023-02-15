package nlp.lm;

/**
 *  CS159 Natural Language Processing @ Pomona College, Spring 2023 Semester
 *  LambdaLMModel.java
 * 
 *  A class describing a lambda smoothing LM (language modeling) model.
 *  Extends LMBase.java. 
 * 
 *  @author Ben Luo
 *  @author Harry Berman 
*/
public class LambdaLMModel extends LMBase {

    String filename;
    double lambda;
    
/**
 *  LambdaLMModel constructor. Takes two arguments, the filename of file to conduct the training on,
 *  and lambda, the chosen lambda value to smooth the data on.
 * 
 *  @param filename
 *  @param lambda
 */    
    public LambdaLMModel(String filename, double lambda) {
        this.filename = filename;
        this.lambda = lambda;

        countUnigrams(filename);
        countBigrams(filename);
        
    }

/**
 *  Returns the probability of P(second | first) given a lambda smoothing LM model.
 * 
 *  @param first
 *  @param second
 *  @return the probability of P(second | first) given a lambda smoothing LM model.
 */ 
    public double getBigramProb(String first, String second) {
        Double count = 0.0;
        Double prob = 0.0;

        if (bmap.get(first).containsKey(second)) {
            count = bmap.get(first).get(second);
            prob = (count + lambda)/(umap.get(first) + lambda * umap.size());
        } else
            prob = lambda/(umap.get(first) + lambda * umap.size());


        return prob;
    };
   
    public static void main(String[] args) {
        String filename = "data/development";
        LambdaLMModel lm = new LambdaLMModel(filename, 0.1);
        System.out.println(lm.getPerplexity(filename));
    }
}
