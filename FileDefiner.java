import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;


public class FileDefiner implements Runnable {
	
	private static ArrayList<String> allWords = new ArrayList<String>();
	private static HashMap<String,String> words = new HashMap<String,String>();
	private static File docFile = null;
	
	public FileDefiner(File inFile) {
		docFile = inFile;
	}
	
	@Override
	public void run() {
		Scanner scan = null;
		try {
			scan = new Scanner(docFile);
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		}
		
		String word;
		while(scan.hasNextLine()) {
			word = scan.nextLine();
			// while current line is just white space, skip to next line
			while(!word.matches(".*\\w.*")) {
				if(scan.hasNextLine()) word = scan.nextLine();
				else break;
			}
			
			String definition = "";
			try {
				definition = Dictionary.define(word);
			} catch(FileNotFoundException e) {
				definition = "ERROR: Could not find definition";
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			allWords.add(word);
			words.put(word, definition);
		}
		
		scan.close();
		
		try {
			writeDefinitions();
		} catch(IOException e) {
			e.printStackTrace();
			System.out.println("Trouble writing to file.");
		}
	}
	
	
	private static void writeDefinitions() throws IOException {
		
		// first, erase current file
		PrintWriter out = new PrintWriter(docFile);
		out.write("");
		
		out.close();
		
		// now write new terms + definitions
		out = new PrintWriter(new FileWriter(docFile, true));
		
		for(int i = 0; i < allWords.size(); i++) {
			String word = allWords.get(i);
			out.write(word + " - " + words.get(word) + "\n");
		}
		
		out.close();
		
	}
	
}
