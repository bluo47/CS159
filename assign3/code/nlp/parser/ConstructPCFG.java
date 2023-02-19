package nlp.parser;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.ArrayList;

public class ConstructPCFG {
    static HashMap<String, HashMap<ArrayList<String>, Integer>> outerMap = new HashMap<>();

    static void parseTheTree(ParseTree pt) {
        ArrayList<String> rhs = new ArrayList<String>();
        HashMap<ArrayList<String>, Integer> innerMap;
        if (outerMap.get(pt.getLabel()) == null) {
            innerMap = new HashMap<>();
        } else {
            innerMap = outerMap.get(pt.getLabel());
        }
        
        for(ParseTree child: pt.getChildren()){
            rhs.add(child.getLabel());
        }
        if (innerMap.containsKey(rhs)) innerMap.put(rhs, innerMap.get(rhs) + 1);
        else innerMap.put(rhs, 1);
               
        outerMap.put(pt.getLabel(), innerMap);
        
        for(ParseTree child: pt.getChildren()){
            if (!child.isTerminal()) {
                parseTheTree(child);
            }
        }       
    }

    public static void main(String[] args) {
      
            BufferedReader objReader = null;
            try {
            String strCurrentLine;
            objReader = new BufferedReader(new FileReader("example\\example.parsed"));

            while ((strCurrentLine = objReader.readLine()) != null) {
                ParseTree pt = new ParseTree(strCurrentLine);
                //System.out.println(pt.getChild(1));   
                parseTheTree(pt);
            }
            } catch (IOException e) {

            e.printStackTrace();
            } finally {
            try {
            if (objReader != null)
                objReader.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            for (String key : outerMap.keySet()) {
                System.out.println("Key: " + key + " | Value: " + outerMap.get(key));
            }
        }
    }	
}
