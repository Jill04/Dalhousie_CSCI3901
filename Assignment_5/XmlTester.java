import java.util.Scanner;

public class XmlTester {

	public static void main(String[] args) {
		 
		
		XmlConversion xml = new XmlConversion();
		Scanner sc = new Scanner(System.in);
		System.out.println("Enter the starting date for the period to summarize");
		String startDate = sc.nextLine();
		
		System.out.println("Enter the ending date for the period to summarize");
		String endDate = sc.nextLine();
		
		System.out.println("Enter the filename");
		String fileName = sc.nextLine();
		
		if(!xml.isValidFormat(startDate,endDate) && xml.notNull(fileName)) {
			System.out.println("Incorrect date. Please enter date in 'YYYY-MM-DD' format or check dates");
		}
		else {
			
			try {
			 xml.executeQuery(startDate,endDate,fileName);
				  
			}
			catch (Exception e) {
				System.out.println("Exception"+e);
				}
		   }
		sc.close();
	 }	
}
