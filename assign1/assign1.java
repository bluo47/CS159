package assign1;
import java.util.regex.*;


class myClass {
    // answer for 1a
    static boolean isValidSSN(String s) {
        
        String ssn = s;
        Pattern pattern = Pattern.compile("\\b\\d{3}(-?)\\d{2}\\1\\d{4}\\b");
        Matcher matcher = pattern.matcher(ssn);

        boolean found = false;
        while(matcher.find()) {
            found = true;
        }
        return found;        
    }

    // answer for 1b
    static boolean properValidSSN(String s) {
        
        String ssn = s;
        Pattern pattern = Pattern.compile("\\b([0-6]\\d\\d|7[0-6]\\d|77[0-2])(-)\\d{2}\\2\\d{4}\\b");
        Matcher matcher = pattern.matcher(ssn);

        boolean found = false;
        while(matcher.find()) {
            found = true;   
        }
        return found; 
    }

    // answer for question 4
    static String pigLatin(String s) {
  
        Pattern p = Pattern.compile("([^AEIOUaeiou]*)(.*)");
        Matcher m = p.matcher(s);
        if (m.find())
            return m.group(2) + m.group(1) + "ay";
        else 
            return "No match found.";
    }

    static void paraToSen(  ) {
        

    }

    public static void main(String[] args) {
        //q1 test cases
        // String test1 = "772-44-1953";
        // String test2 = "772441953";
        // System.out.println(properValidSSN(test1));
        // System.out.println(properValidSSN(test2));

        // System.out.println(pigLatin("benSUX"));

    }
}