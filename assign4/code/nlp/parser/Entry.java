package nlp.parser;
import java.util.*;

public class Entry {
//	store constituents
//	each constituent has a parsetree
//	store weights
	
	
	
//	HashMap<String, Parseforest> constituents;
//	only store the highest probability constituent
	
	private HashMap<String, ParseTree> constituents = new HashMap<>();
	
	
	public Entry(HashMap<String, ParseTree> constituents) {
		this.constituents = constituents;
	}
	
	public HashMap<String, ParseTree> getConstituents() {
		return constituents;
	}
}