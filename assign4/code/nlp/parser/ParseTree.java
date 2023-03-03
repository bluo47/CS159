package nlp.parser;

import java.util.ArrayList;

//I feel like this should be a parsetree class, not parseforest
public class ParseTree {
	private String label;  // tree head
	private ArrayList<ParseTree> children = new ArrayList<ParseTree>();
	double weight = 0.0;
	
	
//	weight
//	label (head)
//	ArraList<ParseTree> (left and right leaf)
	
	public ParseTree(String label, ArrayList<ParseTree> children, double weight) {
		this.label = label;
		this.children = children;
		this.weight = weight;
	}
	
	/**
	 * Get the weight associated with this tree
	 * 
	 * @return the weight associated with this tree
	 */
	public double getWeight(){
		return weight;
	}
	
}