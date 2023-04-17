
/**
 * A class representing a name example.  A name example consists of a first name
 * and whether or not that first name is male or female.
 * 
 * @author drk04747
 * @version 3/3/2011
 *
 */
public class NameExample {
	private String name;
	private boolean isMale;
	
	/**
	 * Create a new name example
	 * 
	 * @param name the name
	 * @param isMale whether the example is male (false for female)
	 */
	public NameExample(String name, boolean isMale){
		this.name = name;
		this.isMale = isMale;
	}
	
	/**
	 * Create a new name example from a name label string
	 * 
	 * @param nameExample tab separated String with the first part the name and second part "male" or "female"
	 */
	public NameExample(String nameExample){
		String parts[] = nameExample.split("\t");
		
		if( parts.length != 2 ){
			throw new RuntimeException("Bad name example: " + nameExample);
		}
		
		name = parts[0];
		
		if( parts[1].equals("male") ){
			isMale = true;
		}else if( parts[1].equals("female") ){
			isMale = false;
		}else{
			throw new RuntimeException("Bad name label: " + nameExample);
		}
	}
	
	/**
	 * @return the name associated with this example
	 */
	public String getName() {
		return name;
	}

	/**
	 * @return whether or not this name example is a male name
	 */
	public boolean isMale() {
		return isMale;
	}

	/**
	 * @return whether or not this name example is a female name
	 */
	public boolean isFemale(){
		return !isMale;
	}
	
	public String toString(){
		if( isMale ){
			return name + "\tmale";
		}else{
			return name + "\tfemale";
		}
	}
}
