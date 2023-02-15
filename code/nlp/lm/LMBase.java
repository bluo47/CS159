package nlp.lm;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

/**
 *  CS159 Natural Language Processing @ Pomona College, Spring 2023 Semester
 *  LMBase.java
 * 
 *  A generic class that houses the methods required for various LM models, 
 *  like logProb, getPerplexity, and getBigramProb.
 *  Implements the generic LMModel interface provided by Dr. Dave.
 * 
 *  @author Ben Luo
 *  @author Harry Berman
 * 
 */
public class LMBase implements LMModel{

    public HashMap<String, HashMap<String,Double>> bmap = new HashMap<String, HashMap<String,Double>>();
    public HashMap<String, Double> umap = new HashMap<>();
    public Double totalWords = 0.0;

    /**
	 * Given a sentence, return the log of the probability of the sentence based on the LM.
	 * 
	 * @param sentWords the words in the sentence.  sentWords should NOT contain <s> or </s>.
	 * @return the log probability
	 */
    public double logProb(ArrayList<String> sentWords) {
        // take the log of each bigram probability in the sentence
        Double totalLogProb = 0.0;
        sentWords.add(0, "<s>");
        sentWords.add("</s>");
        for (int i = 0; i < sentWords.size() - 1; i++) {
            String w1 = sentWords.get(i);
            String w2 = sentWords.get(i+1);
            
            totalLogProb += Math.log10(getBigramProb(w1, w2));
        }
        return totalLogProb;
    };

    /**
	 * Given a text file, calculate the perplexity of the text file, that is the negative average per word log
	 * probability
	 * 
	 * @param filename a text file.  The file will contain sentences WITHOUT <s> or </s>.
	 * @return the perplexity of the text in file based on the LM
	 */
    public double getPerplexity(String filename) {
        // for each sentence, calculate log prob and add to total
        Double exp = 0.0;
        Double n = 0.0;
        BufferedReader br = null;

        try {
            FileReader fr = new FileReader(filename);
            br = new BufferedReader(fr);
            String sentence;
            ArrayList<String> strList = new ArrayList<>();
            HashMap<String,Double> pmap = new HashMap<>();
            pmap.put("<UNK>", 0.0);

            while ((sentence = br.readLine()) != null) {
                // we need to turn sentence into an arraylist sentWords
                // split string by no space
                String[] strSplit = sentence.split("\\s+");
                for (int i = 0; i < strSplit.length; i++) {
                    if (!pmap.containsKey(strSplit[i])) {
                        pmap.put(strSplit[i], 0.0);
                        strSplit[i] = "<UNK>";
                    }
                }
                // Now convert string into ArrayList
                strList = new ArrayList<String>(Arrays.asList(strSplit));
                exp += logProb(strList);
                n += strList.size() + 1.0;
            }
            // close the BufferedReader
            br.close();
            exp = exp * (-1/n);    
        } 
        // catch block to return exception if the no file with name filename exists
        catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        } 
        return Math.pow(10, exp);
    };

    /**
	 * Returns p(second | first)
	 * 
	 * @param first
	 * @param second
	 * @return the probability of the second word given the first word (as a probability)
	 */
    public double getBigramProb(String first, String second) {
        return -1;
    };

    /**
     *  Counts the occurrences of all unigrams that occur in the file named filename.
     *  Stores it in a HashMap<String, Double> called umap.
     * 
     *  @param filename
     */
    public void countUnigrams(String filename) {

        BufferedReader br;

        try {

            FileReader fr = new FileReader(filename);
            br = new BufferedReader(fr);
            umap.put("<UNK>", 0.0);
            umap.put("<s>", 0.0);
            umap.put("</s>", 0.0);
            String sentence;
            
            while ((sentence = br.readLine()) != null) {
                
                sentence = "<s> " + sentence + " </s>";
                String[] words = sentence.split("\\s+", 0);
                
                for(int i = 0; i < words.length; i++) {
                    totalWords++; 
                    String w1 = words[i];                    
                    // if in hash
                        // if so, increase count by 1
                    if (umap.containsKey(w1))
                        umap.put(w1, umap.get(w1) + 1.0);
                    // else
                        // add to hashmap with count 0
                        // add one to <UNK>
                    else {
                        if (w1.equals("<s>") || w1.equals("</s>"))
                            umap.put(w1, umap.get(w1) + 1.0);
                        else {
                            umap.put(w1, 0.0);
                            umap.put("<UNK>", umap.get("<UNK>") + 1.0);
                        }    
                    }
                }
            }

            br.close();

        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
        // for (String s : umap.keySet()) {
        //     System.out.println("word: " + s + " with count: " + umap.get(s));
        // }
    }

    /**
     *  Counts the occurrences of all bigrams that occur in the file named filename.
     *  Stores it in a HashMap<String, HashMap<String, Double>> called bmap.
     * 
     *  @param filename
     * 
     */
    public void countBigrams(String filename) {
        BufferedReader br;
        try {
            FileReader fr = new FileReader(filename);
            br = new BufferedReader(fr);
            String sentence;
            
            HashMap<String,Double> m = new HashMap<>();
            m.put("<UNK>", 0.0);
            bmap.put("<UNK>", m);

            while ((sentence = br.readLine()) != null) {
              sentence = "<s> " + sentence + " </s>";
              String[] words = sentence.split("\\s+", 0);
              
              for(int i = 0; i < words.length-1; i++) {
                String w1 = words[i];
                String w2 = words[i+1];
               
                // check if hashmap contains any matching first words
                if (bmap.containsKey(w1)) {
                    // check if temp contains the second word
                    if (bmap.get(w1).containsKey(w2)) {
                        // if bigram does exist, then increase count by 1
                        Double count = bmap.get(w1).get(w2) + 1;
                        bmap.get(w1).replace(w2, count);
                    } else {
                        // check if w2 is in map
                        if (bmap.containsKey(w2) && !w2.equals("</s>")) {
                            // if the map doesn't contain the second word
                            // bigram does not exist yet, so add
                            HashMap<String,Double> temp = bmap.get(w1);
                            temp.put(w2, 1.0);
                            bmap.put(w1, temp);

                        } else if (w2.equals("</s>")) {
                            HashMap<String,Double> temp = bmap.get(w1);

                            if (temp.get(w2) == null) {
                                temp.put(w2, 1.0);
                                bmap.replace(w1, temp);

                            } else {
                                temp.put(w2, temp.get(w2) + 1);
                                bmap.replace(w1, temp);
                            }
                            
                        } else {
                            HashMap<String,Double> temp = bmap.get(w1);
                            temp.put("<UNK>", 1.0);
                            bmap.put("<UNK>", temp);
                        }                       
                    }
                } else {
                    // if the map doesn't contain the first word
                    // bigram does not exist yet, so add
                    HashMap<String,Double> temp_real = new HashMap<String,Double>();
                    temp_real.put("ben_face", 0.0);
                    bmap.put(w1, temp_real);

                    if (!w1.equals("<s>")) {
                        if (bmap.containsKey(w2)) {
                            HashMap<String,Double> temp1 = bmap.get("<UNK>");
                            if (temp1.get(w2) == null) {
                                temp1.put(w2, 1.0);
                            } else {
                                Double count = temp1.get(w2) + 1;
                                temp1.replace(w2, count);
                            }

                            bmap.put("<UNK>", temp1); 
                        } else {

                            HashMap<String,Double> temp2 = new HashMap<String,Double>();
                            temp2.put("<UNK>", 0.0);
                            bmap.put(w1, temp2);

                            HashMap<String,Double> temp1 = bmap.get("<UNK>");
                            Double count = temp1.get("<UNK>") + 1;
                            temp1.replace("<UNK>", count);
                            bmap.replace("<UNK>", temp1);
                        }        
                    } else {
                        if (bmap.containsKey(w2)) {
                            HashMap<String,Double> temp = new HashMap<String,Double>();
                            temp.put(w2, 1.0);
                            bmap.put(w1, temp);
                        } else {
                            // add s unknown and increment that count
                            HashMap<String,Double> temp = bmap.get("<s>");
                            
                            if (temp.get("<UNK>") == null)
                                temp.put("<UNK>", 1.0);
                            else
                                temp.replace("<UNK>", temp.get("<UNK>") + 1);

                            bmap.replace("<s>", temp);
                        }    
                    } 
                }
              }             
            }

            for (String wone : bmap.keySet()) {
                if (wone.equals("<UNK>")) {
                    System.out.println("-----");
                    System.out.print("w1: " + wone + "\n");

                    for (String wtwo : bmap.get(wone).keySet()) {
                        if (wtwo.equals("<UNK>")) 
                            System.out.println(" w2: " + wtwo + " c: " + bmap.get(wone).get(wtwo) + ", ");
                    }
                }
            }
            br.close();

          } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
          }
    }
}
