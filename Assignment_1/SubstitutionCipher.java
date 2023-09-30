
import static java.util.stream.Collectors.toMap;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Scanner;
import java.util.Set;
import java.util.stream.Collectors;


public class SubstitutionCipher {
    
	String name;
	// Stores the decryption key
	Map<Character,Character> key = new HashMap<>();
	// Stores the language objects
    List<Language> language = new ArrayList<>();
    //Stores the letter frequency of the cipher text
    Map<Character,Float> cipherFreqTable;
    //Holds the cipher text file object
    File filename;
    
	public SubstitutionCipher(){

		Map<Character,Character> key = new HashMap<>();
		key.put('A', 'Z');
		key.put('B', 'I');
		key.put('C', 'C');
		key.put('D', 'N');
		key.put('E', 'B');
		key.put('F', 'O');
		key.put('G', 'K');
		key.put('H', 'M');
		key.put('I', 'H');
		key.put('J', 'Y');
		key.put('K', 'P');
		key.put('L', 'T');
		key.put('M', 'V');
		key.put('N', 'L');
		key.put('O', 'S');
		key.put('P', 'R');
		key.put('Q', 'Q');
		key.put('R', 'J');
		key.put('S', 'D');
		key.put('T', 'G');
		key.put('U', 'W');
		key.put('V', 'U');
		key.put('W', 'E');
		key.put('X', 'F');
		key.put('Y', 'A');
		key.put('Z', 'X');
		this.key = key;
	}


	public SubstitutionCipher(String name, Map<Character,Character>key) {
	 if(notNull(name) && notNull(key))
	 {
		 this.name = name;
		 this.key = key;
	 }
	}

	 /**
	  * Reads the input file of the language and stores relevant frequency table of characters 
	  *
	  * @param  name      the name of the language
	  * @param  filename  the corresponding file of the language 
	  * @return           boolean
	  */
	public boolean originalLanguage(String name, String filename) {
		if(!notNull(name) && !notNull(filename)) {
			return false;
		}
		try {
			File languageFile = new File(filename);
			BufferedReader reader = new BufferedReader(new FileReader(languageFile.getAbsoluteFile()));
			
			//Checks for valid file conditions
			boolean passesCondition = checkPreFileConditions(languageFile);
			if(!passesCondition) {
				System.out.println("Error: Either file has single line or each line characters limit exceed 120 ");
				reader.close();
				return false;
			}
			
		    Map <Character,Float> freqTable = new HashMap<Character,Float>();
			freqTable=generateFrequencyTable(reader);
			
			Language language = new Language(name);
			language.languageFreqTable = freqTable;
			this.language.add(language);
		   
			reader.close();
			
	    } catch(IOException e) {
	      e.printStackTrace();
	      return false;
	    }
		return true;
	}
	/**
	  * Reads the input file of the cipher text and stores relevant frequency table of characters 
	  *
	  * @param  filename  the corresponding file of the cipher text 
	  * @return           boolean
	  */
	public boolean ciphertext( String filename) {
		if(!notNull(filename)) {
			return false;
		}
		try {
			File cipher = new File(filename);
			BufferedReader reader = new BufferedReader(new FileReader(cipher.getAbsoluteFile()));
			
			//Checks for valid file conditions
			boolean passesCondition = checkPreFileConditions(cipher);
			if(!passesCondition) {
				System.out.println("Error: Either file has single line or each line characters limit exceed 120 ");
				reader.close();
				return false;
			}
			
			Map <Character,Float> freqTable = new HashMap<Character,Float>();
			freqTable=generateFrequencyTable(reader);
			reader.close();
			
			if(!freqTable.isEmpty())
				{
				  this.cipherFreqTable = freqTable;
				  this.filename = cipher;
				  return true;
				}
		   return true;
        
	    } catch(IOException e) {
	      e.printStackTrace();
	      return false;
	    }
	}
	
	/**
	  * Returns the current decryption key 
	  *
	  * @return           Map<K,V>
	  */
	public Map<Character, Character> getKey(){
		if (this.key == null) {
			System.out.println("Error:No key stored");
			return null;
		}
		return this.key;
	}
	/**
	  * Decodes the cipher text using the key  
	  *
	  * @return           String
	  */
	
	public String decodeText(){
		String plainText = "";
		Map<Character,Character>key = getKey();
		if(!keyIsValid()) {
		 return null;	
		}
		try {
		BufferedReader reader = new BufferedReader(new FileReader(this.filename));
		int ch;
		while((ch=reader.read()) != -1) {
			 char value = (char)ch;
			 if(!Character.isLetter(value) || Character.isWhitespace(value))
		        {
				 plainText = plainText.concat(Character.valueOf(value).toString());
		        }
			 for (Entry<Character, Character> entry : key.entrySet()) {
			        if (Objects.equals(value, entry.getValue())) {
			            char plainChar =  entry.getKey();
			            plainText = plainText.concat(Character.valueOf(plainChar).toString());
			        }
			    }
		     }
		  return plainText;
		}
		catch(IOException e) {
			 System.out.println("An error occurred");
		      e.printStackTrace();
		}
		return plainText;
	}
	/**
	  * Returns the name of the language whose letter frequency matches to the cipher text 
	  *
	  * @return           String
	  */
	public String matchLanguage(){
		 int langObjLength = this.language.size();
	     	
     	 if(langObjLength == 0)return null;
     	 //if only one language file exists then uses that directly
     	 if(langObjLength == 1)return this.language.get(0).name;
		
		 Map<Character,Float>langFreqTable = new HashMap<>();
		 Map<Character,Float>cipherFreqTable = this.cipherFreqTable;
		 Map<String,Float> langDifference = new HashMap<>();
     	 Map<String,ArrayList<Float>> languageArray = new HashMap<>();
     	 
     	 //Calculates the language difference
     	 for(int i =0; i<this.language.size();i++) {
     		langFreqTable = this.language.get(i).languageFreqTable;
			languageArray.put(this.language.get(i).name, calculateDifference(cipherFreqTable,langFreqTable));
		}
     
		Double floatSum;
		//Sums the language difference
		for (Map.Entry<String,ArrayList<Float>> entry : languageArray.entrySet()){
			floatSum=entry.getValue().stream()
		            .mapToDouble(Float::doubleValue)
		            .sum();
			langDifference.put(entry.getKey(),floatSum.floatValue());	
		}
		//Retrieves the language that has minimum difference from all the languages
		String key = Collections.min(langDifference.entrySet(), Map.Entry.comparingByValue()).getKey();
		if(notNull(key))return key;
		return null;
	  }
	
	/**
	  * Determines whether key is valid or not.
	  *
	  * @return           boolean
	  */
	public boolean keyIsValid() {
		if (this.key.size() ==0) {
			System.out.println("Error:No key stored");
			return false;
		}
		Set<Character> cipherSet = this.cipherFreqTable.keySet();
	    Iterator<Character> cipherIterator = cipherSet.iterator();
        
	    //Validates that for each cipher text letter has a mapping with the key
		 while(cipherIterator.hasNext()) {
			 if(!this.key.containsValue(cipherIterator.next()))return false;
		 }
		return true;
	}
	/**
	  * Generates the key by using letter frequency table.
	  *
	  * @param   language  name of the language in which decryption is possible
	  * @return            boolean
	  */
	public boolean guessKeyFromFrequency(String language) {
		if(!notNull(language) && languageExists(language)) {
			return false;
		}
		
		Map<Character,Float> langFreqTable = returnlangFreTable(language);
		if(langFreqTable == null)return false;
		
		Set<Character> cipherSet = this.cipherFreqTable.keySet();
		Set <Character>languageSet = langFreqTable.keySet();
		 
	    Iterator<Character> cipherIterator = cipherSet.iterator();
	    Iterator<Character> languageIterator = languageSet.iterator();
	   
	    if(this.key != null)this.key.clear();
	   
	    for(int i = 0 ; i<this.cipherFreqTable.size();i++) {
	    	Character cipherTextChar = cipherIterator.next();
	    	Character plainTextChar;
	    	
	    	//Used when cipher text letters are left for mapping
	    	if(this.cipherFreqTable.size() !=langFreqTable.size() && i == langFreqTable.size())
	    	{
	    		Iterator<Character> it = missingKey().iterator();
	            //Searches for the next available letter for mapping
	            while (it.hasNext()) {
	            	Character ch = it.next();
	            	if(!langFreqTable.containsKey(ch)) {
	            		setDecodeLetter(ch,cipherTextChar);
	            	}	
	            }
	    	}
	    	else {
	    		plainTextChar= languageIterator.next();
	    		setDecodeLetter(plainTextChar,cipherTextChar);
	    	}
	    }
	   //Sorts key in alphabetical order
	   this.key = sortAlphabetically(this.key);
	  
	   return true;
	    
	}
	/**
	  * Maps plain text character with cipher text character
	  *
	  * @return           boolean
	  */
	public boolean setDecodeLetter( Character plaintextChar, Character ciphertextChar ){
	
		if(notNull(plaintextChar) && notNull(ciphertextChar) && isEncodeable(plaintextChar) && isEncodeable(ciphertextChar)  )
		{
			if(Character.isLowerCase(plaintextChar))Character.toUpperCase(plaintextChar);
			if(Character.isLowerCase(ciphertextChar))Character.toUpperCase(ciphertextChar);
		    if(this.key.size() != 0) {
		    	if(!this.key.containsValue(ciphertextChar)) {
				     this.key.put(plaintextChar, ciphertextChar);
	               }
			    else {
			    	 for (Map.Entry<Character,Character> entry : (this.key.entrySet())) {
			            if(ciphertextChar.equals(entry.getValue())){
			                Character key = entry.getKey();
			                this.key.remove(key);
			                this.key.put(plaintextChar,ciphertextChar);
			                }
			    	  } 
	        }
			return true;
		}
	    else {
			this.key.put(plaintextChar, ciphertextChar);
			return true;
			}
		}	    
		return  false;
	}
	
	/********** Helper Functions **********/
	
	/**
	  *Checks for valid file
	  *
	  * @param   file     file to be validated
	  * @return           boolean
	  */
	private boolean checkPreFileConditions(File file) {
		if(!file.exists())
		{
			System.out.println("Error: File doesnot exists");
			return false;
		}
		if(!file.canRead()) {
				System.out.println("Error:Cannot read file");
				return false;
		}
		if(file.length() < 120) {
			System.out.println("Error: File of length "+file.length()+" characters is not valid");
			return false;
		}
			 
		 List<String> list = new ArrayList<>();
		 try {
			  BufferedReader reader = new BufferedReader(new FileReader(file.getAbsoluteFile()));
				list = reader.lines().collect(Collectors.toList());
				
				if(list.size()==1)return false;
				for(int i = 0; i< list.size();i++) {
					if(list.get(i).length() > 120)
						return false;
				 }
			    }catch(Exception e) {
					  System.out.println(e);
			    }
			return true;	  
	}
	
	/**
	  * Generates the frequency table for specified file
	  * 
	  * @param   reader  Buffered file reader object
	  * @return           boolean
	  */
	private Map<Character,Float> generateFrequencyTable(BufferedReader reader){
		Map<Character,Integer> charCount = new HashMap<Character,Integer>();
	    int ch;
	    int total = 0;
	        
	    try {
	        while((ch=reader.read()) != -1) {
	                if((int)ch < 65 || (int)ch >=123)continue;
	        		  char key = (char)ch;
	        		  if(Character.isLowerCase(key))Character.toUpperCase(key);
	        		  if(charCount.containsKey(key)) {
	        		    int value = charCount.get(key);
	        		    charCount.put(key, value + 1);
	        		  } else {
	        			  charCount.put(key,1);
	        		  }
	        		  total++;
	        		}
	         reader.close();
	      
	        Map <Character,Float> freqTable = new HashMap<Character,Float>();
	        for (Map.Entry<Character,Integer> entry : (charCount.entrySet())) {
	        	 float value = ((entry.getValue()).floatValue()/total)*100;
	        	freqTable.put(entry.getKey(),value);
	        }  
	        freqTable = sortMap(freqTable);
	        return freqTable;
	     } 
	    catch(IOException e) {
			  System.out.println(e);
		      e.printStackTrace();
	      }
	     return null;    
	}
	
	/**
	  * Sorts the map in descending order
	  * 
	  * @param   Map<k,v>  Map that needs to be sorted
	  * @return            Map<k,v> 
	  */
	private Map<Character,Float> sortMap(Map<Character,Float> map){
		map = map
        .entrySet()
        .stream()
        .sorted(Collections.reverseOrder(Map.Entry.comparingByValue()))
        .collect(
            toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e2,
                LinkedHashMap::new));
		return map;
	}
	
	/**
	  * Checks whether specified language exists or not
	  * 
	  * @param   name   language name
	  * @return         boolean 
	  */
	private boolean languageExists(String name) {
		for(int i =0; i<this.language.size();i++) {
			if(name == this.language.get(i).name)return true;	
		}
		return false;
	}
	
	/**
	  * Returns the letter frequency table of specified language
	  * 
	  * @param   name   language name
	  * @return         Map<K,V> 
	  */
	private Map<Character,Float> returnlangFreTable(String name){
		if(!notNull(name))return null;
		for(int i =0; i<this.language.size();i++) {
			if(name == this.language.get(i).name) {
				return  this.language.get(i).languageFreqTable;
			}	
		}
		return null;
	}
	
	/**
	  * Checks whether the object given is null or not
	  * 
	  * @param   obj    object
	  * @return         boolean 
	  */
	private boolean notNull(Object obj) {
		if(obj != null) {
			return true;
		}
		System.out.println(" Error: Empty Input");
		return false;
	}

	/**
	  * Checks whether the character is encodeable  or not
	  * 
	  * @param   charc    Character
	  * @return           boolean 
	  */
	private boolean isEncodeable(Character charc) {
		if(Character.isLetter(charc)) {
			return true;
		}
		System.out.println(" Error: Character is not encodeable");
		return false;
	}

	/**
	  * Sorts the map alphabetically
	  * 
	  * @param   map    the map to be sorted
	  * @return         Map<K,V> 
	  */
	private Map<Character,Character> sortAlphabetically(Map<Character,Character> map){
		map.entrySet()
		  .stream()
		  .sorted(Map.Entry.<Character, Character>comparingByKey());
           return map;	
         }

	/**
	  * Calculates the difference between cipher text letter frequency and language letter frequency
	  * 
	  * @param   cipher    Cipher text frequency table
	  * @param   language  Language frequency table
	  * @return            ArrayList<Obj> 
	  */
	private ArrayList<Float> calculateDifference(Map<Character,Float> cipher, Map<Character,Float> language){
		ArrayList<Float> difference = new ArrayList<>();
		ArrayList<Float> temp = new ArrayList<>();
		ArrayList<Float> temp1 = new ArrayList<>();
		int limit;
		for (Entry<Character, Float> entry : cipher.entrySet()) {
           temp.add(entry.getValue());
	        }
		for (Entry<Character, Float> entry : language.entrySet()) {
	         temp1.add(entry.getValue());
	        }
		if(temp.size()>temp1.size())limit = temp1.size();
		else limit= temp.size();
		for(int i =0 ;i<limit;i++) {
			difference.add(temp.get(i) - temp1.get(i) );
		}
		return difference;
	}

	/**
	  * Provides plain text character of mapping
	  * 
	  * @return         ArrayLists<Obj> 
	  */
	private ArrayList<Character> missingKey(){
		ArrayList<Character> keyBackUp = new ArrayList<Character>();
		Character c;

	    for(c = 'A'; c <= 'Z'; ++c) {
			keyBackUp.add(c);
		}
	    return keyBackUp;
	}
}

