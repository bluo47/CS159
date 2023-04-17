import edu.stanford.nlp.classify.*;
import edu.stanford.nlp.ling.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

/**
 * A class for playing with name classification approaches
 * 
 * @author drk04747
 * @version 3/3/2011
 *
 */
public class NameClassification {
	private static final String DATA_DIR = "../data/";
	private static final String TRAINING_DATA = DATA_DIR + "names.train";
	private static final String DEV_DATA = DATA_DIR + "names.dev";
	private static final String TEST_DATA = DATA_DIR + "names.test";

	/**
	 * Given a file with name examples of the form:
	 * 
	 * name		male/female
	 * 
	 * read in each example as a NameExample
	 * 
	 * @param filename the filename containing labeled name data
	 * @return an ArrayList of NameExamples
	 */
	private static ArrayList<NameExample> readExamples(String filename){
		Scanner dataScanner = null;

		try {
			dataScanner = new Scanner(new File(filename));
		} catch (FileNotFoundException e) {
			throw new RuntimeException("Problems opening file: " + e.toString());
		}
		
		ArrayList<NameExample> examples = new ArrayList<NameExample>();
		
		while( dataScanner.hasNextLine() ){
			examples.add(new NameExample(dataScanner.nextLine()));
		}
		
		return examples;
	}

	
	/**
	 * Given a List of NameExamples, extract the features from each example
	 * using the makeDataPoint method and create Datum.
	 * 
	 * @param examples a List of NameExamples representing the data set
	 * @return an ArrayList of Datum created from the input examples
	 */
	private static ArrayList<Datum> examplesToData(List<NameExample> examples){
		ArrayList<Datum> data = new ArrayList<Datum>();

		for( NameExample ex: examples ){
			data.add(makeDataPoint(ex));
		}

		return data;
	}

	/**
	 * Get the accuracy of this classifier on the test data
	 * 
	 * @param testData test data to be classified
	 * @param examples original NameExamples associated with the test data
	 * @param classifier the classifier to use
	 * @param printMistakes whether or not to print out the misclassified examples
	 * @return the accuracy of the classifier on the test data
	 */
	private static double accuracy(List<Datum> testData, List<NameExample> examples, LinearClassifier classifier, boolean printMistakes){
		int correct = 0;

		for( int i = 0; i < testData.size(); i++ ){
			String classification = (String)classifier.classOf(testData.get(i));
			
			if( classification.equals(testData.get(i).label()) ){
				correct++;
			}else if( printMistakes ){
				System.out.println("Incorrect: " + examples.get(i));
			}
		}

		return ((double)correct)/testData.size();
	}
	
	/**
	 * Get the log-prob of this classifier on the test data
	 * 
	 * @param testData test data to be examined
	 * @param classifier the classifier to use
	 * @return the log-prob of the classifier on the test data
	 */
	private static double totalLogProb(ArrayList<Datum> testData, LinearClassifier classifier){
		double total = 0.0;

		for( Datum d: testData ){
			// get the log-prob for this model for the correct label
			// ignore this warning
			total += Math.log(classifier.probabilityOf(d).getCount(d.label()));			
		}

		return total;
	}

	// YOU'LL MODIFY THE METHOD BELOW TO ADD MORE FEATURES
	
	/**
	 * Creates a feature-base Datum from the NameExample for use with the classifiers
	 * 
	 * @param example the example to be featurized
	 * @return A Datum representing the input example
	 */
	private static Datum makeDataPoint(NameExample example){
		ArrayList<String> features = new ArrayList<String>();

		String name = example.getName();
		
		// We only need to add the features that occur.  In this case
		// each feature is binary and is represented by adding a String
		// representing that feature to the set of features.
		//
		// To get you started, this will generate up to 26 features per example
		// indicating whether or not the named contained a particular letter.
		for( char c = 'a'; c < 'z'; c++ ){
			if( name.toLowerCase().contains("" + c) ){
				features.add("Contains=" + c);
			}
		}

		if( example.isFemale() ){
			return new BasicDatum(features, "female");
		}else{
			return new BasicDatum(features, "male");
		}
	}

	
	public static void main(String[] args){
		ArrayList<Datum> trainingData = examplesToData(readExamples(TRAINING_DATA));
		ArrayList<NameExample> devExamples = readExamples(DEV_DATA);
		ArrayList<Datum> devData = examplesToData(devExamples);
		
		// DON'T USE THIS UNTIL I TELL YOU TO
		//ArrayList<Datum> testData = examplesToData(readExamples(TEST_DATA));
		
		// MaxEnt
		LinearClassifierFactory maxEntFactory = new LinearClassifierFactory();
		maxEntFactory.useConjugateGradientAscent();
		// Turn on per-iteration convergence updates
		maxEntFactory.setVerbose(true);
		//Small amount of smoothing
		maxEntFactory.setSigma(10.0);
		// Build a classifier
		LinearClassifier maxEntClassifier = (LinearClassifier)maxEntFactory.trainClassifier(trainingData);
		// Check out the learned weights
		maxEntClassifier.dump();
		
		// NB
		NBLinearClassifierFactory nbFactory = new NBLinearClassifierFactory();
		LinearClassifier nbClassifier = (LinearClassifier)nbFactory.trainClassifier(trainingData);
		//nbClassifier.dump();	

		System.out.println("MLR Accuracy: " + accuracy(devData, devExamples, maxEntClassifier, false));
		System.out.println("NB Accuracy: " + accuracy(devData, devExamples, nbClassifier, false));
		System.out.println();
		System.out.println("MLR Log prob: " + totalLogProb(devData, maxEntClassifier));
		System.out.println("NB Log prob: " + totalLogProb(devData, nbClassifier));
	}
}
