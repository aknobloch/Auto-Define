import java.io.IOException;
import java.net.URL;
import java.util.Scanner;


public class Dictionary {
	
	private static String definition;
	private static String word;
	
	private Dictionary() {
		// prevents instantiation
	}
	
	public static String define(String inWord) throws IOException {
		
		word = format(inWord);
		definition = findDefinition(word);
		
		return definition;
	}
	
	
	private static String findDefinition(String inWord) throws IOException {
		
		String definition = "";
		URL dictionaryURL = new URL("http://www.yourdictionary.com/" + inWord);
		Scanner scan = new Scanner(dictionaryURL.openStream());
		
		for(int i = 0; i < 30; i++) {
			// Only scan the first 30 lines of HTML doc
			String line = scan.nextLine();
			if(line.contains("definition:")) {
				// chop line to start at proper position
				definition = line.substring(line.indexOf(":") + 2);
				// get rid of "The definition of ____ is... phrase
				if(definition.contains(inWord)) {
					int endOfPhrase = definition.indexOf(inWord) + inWord.length() + 4;
					definition = definition.substring(endOfPhrase);
					// capitalize first letter
					String firstLetter = definition.substring(0,1);
					definition = firstLetter.toUpperCase() + definition.substring(1);
				}
				break;
			}
		}
		
		scan.close();
		
		definition = definition.substring(0, definition.indexOf(".") + 1);
		if(definition.length() < 2) definition = "ERROR: Could not find definition";

		return definition;
	}

	private static String format(String inWord) {
		inWord = inWord.toLowerCase();
		inWord = inWord.replaceAll(" ", "-");
		// remove everything that isn't a letter or -
		inWord = inWord.replaceAll("[^a-zA-Z---]+","");
		
		// remove multiple dashes
		for(int i = 0; i < inWord.length() - 1; i++) {
			if(inWord.charAt(i) == '-' && inWord.charAt(i + 1) == '-') {
				String firstPart = inWord.substring(0, i);
				String secondPart = inWord.substring(i+1);
				inWord = firstPart + secondPart;
				i--;
			}
		}
		
		if(inWord.equals("-")) return "";
		if(inWord.endsWith("-")) inWord = inWord.substring(0,inWord.length() - 2);
		if(inWord.startsWith("-")) inWord = inWord.substring(1);
		
		return inWord;
	}
	
}
