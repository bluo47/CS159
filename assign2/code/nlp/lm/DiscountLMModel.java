package nlp.lm;

/**
 *  CS159 Natural Language Processing @ Pomona College, Spring 2023 Semester
 *  DiscountLMModel.java
 * 
 *  A class describing a discount smoothing LM (language modeling) model.
 *  Extends LMBase.java. 
 * 
 *  @author Ben Luo
 *  @author Harry Berman 
*/
public class DiscountLMModel extends LMBase {

    String filename;
    Double discount;

/**
 *  LambdaLMModel constructor. Takes two arguments, the filename of file to conduct the training on,
 *  and lambda, the chosen lambda value to smooth the data on.
 * 
 *  @param filename
 *  @param lambda
 */    
    public DiscountLMModel(String filename, Double discount) {
        this.filename = filename;
        this.discount = discount;

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

        Double prob = 0.0;

        if (bmap.get(first).containsKey(second)) {
            
            Double num = bmap.get(first).get(second) - discount;
            Double denom = umap.get(first);
            prob = num / denom;
            if (prob < 0.0) {
                System.out.println("num: " + num + " | denom: " + denom + " | totalWords: " + totalWords);
            }
        } else {
            
            if (bmap.get(first).size() > 1) {
                Double rm_num = bmap.get(first).size() * discount;
                Double rm_denom = 0.0;
                for (Double n : bmap.get(first).values()) {
                    rm_denom += n;
                }

                Double rm = rm_num / rm_denom;  
                Double count = 0.0;

                for (String key : bmap.get(first).keySet()) {
                    if (!key.equals("ben_face")) {
                        count += umap.get(key) / totalWords;
                    }
                }

                Double alpha_denom = 1 - count;
                Double alpha = rm / alpha_denom;
                prob = alpha * umap.get(second) / totalWords;
            }           
        }

        return prob;
    };

    public static void main(String[] args) {
        String filename = "data/testing";
        DiscountLMModel lm = new DiscountLMModel(filename, 0.99);
        System.out.println(lm.getPerplexity(filename));
    }
}
