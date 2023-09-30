import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class A1 {

	public static void main(String[] args) {
		  //Input variables
		  Map<Character,Character> key = new HashMap<>();
		  Scanner sc = new Scanner(System.in);
		  String language ="";
		  String name = "";
		  Character plainChar;
		  Character cipherChar;
		  String cipherFile = "";
		  int  noOfFiles;
		  SubstitutionCipher cipher  = new SubstitutionCipher();
	      System.out.println("Choose decryption method");
	      System.out.println("1. Manually 2. Automatic");

		     // To fetch user mode of decryption
	      int type = sc.nextInt();
		  switch (type) {
		  case 1:
			  System.out.println("Enter the ciphertext file");
			  cipherFile = sc.next();
			  cipher.ciphertext(cipherFile);
			  System.out.print("Name:");
			  name = sc.next();
			  
			  //Reads the key character mapping from the user
			  for(int i = 0; i< 26; i++) {
				    System.out.print("PlainText:");
				    plainChar = sc.next().charAt(0);
				    System.out.println("CipherText:");
				    cipherChar = sc.next().charAt(0);
				  
				    cipher.setDecodeLetter(plainChar, cipherChar);
				    }
			  key = cipher.getKey();
			  cipher = new SubstitutionCipher(name,key);
			  cipher.ciphertext(cipherFile);
			  if(cipher.keyIsValid()) {
					 System.out.println("Decoded message:"+cipher.decodeText());
			  }
		    break;
		  case 2:
			  System.out.println("Enter the ciphertext file");
			  cipherFile = sc.next();
			   
			  System.out.println("Enter number of language files");
			  noOfFiles = sc.nextInt();
			  System.out.println(noOfFiles);
			  System.out.println("Enter the language name and file ");
			  
			  //Reads the language files from the user
			  for(int i =0; i< noOfFiles;i++) {
				    System.out.print("Name:");
				    name = sc.next();
				    System.out.println("File:");
				    language = sc.next();
						  cipher.originalLanguage(name,language);
					  }
					cipher.ciphertext(cipherFile);
					String keyLang = cipher.matchLanguage();	
					if(keyLang != null)
						{
						 cipher.guessKeyFromFrequency(keyLang);
						 if(cipher.keyIsValid()) {
         						 System.out.println("Decoded message:"+cipher.decodeText());
						 }
						}
					break;
		    default:
		         System.out.println("Input incorrect");
		}
		  
	}

}
