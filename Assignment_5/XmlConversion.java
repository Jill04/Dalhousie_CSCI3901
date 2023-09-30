import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Scanner;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringWriter;
import java.sql.Connection;

public class XmlConversion {
	//Connection class object
	Connection connect;
	
	/*
	 * To connect with the DB
	 */
	private boolean connectToDb() throws ClassNotFoundException, SQLException {
		 try {
			 //Importing jdbc driver
			  Class.forName("com.mysql.cj.jdbc.Driver");
			  
			  //Connecting to database
			  connect = DriverManager.getConnection("jdbc:mysql://db.cs.dal.ca:3306","pujara","B00921464");//Establishing connection
			
			  //Returns true if connection is established successfully
			if(connect != null)return true;
			
			//Prints error message 
			else {
				System.out.println("Error in connecting database");
				return false;
			   }
			} catch (SQLException e) {
			System.out.println("Exception"+e);
			}
		 return false;
	}
	
	/*
	 * Executes queries for all the questions 
	 * 
	 * @param  startDate  start date for the period
	 * @param  endDate    end date of the period
	 * 
	 */
	public Document executeQuery(String startDate,String endDate, String fileName)throws ClassNotFoundException, SQLException, ParserConfigurationException, TransformerException, IOException {
		Document doc = null;
		if(connectToDb()) {
			
			//selecting the database
			PreparedStatement stmt = connect.prepareStatement("use csci3901");
			stmt.execute();
			
			//Query1
			PreparedStatement stmt1  = connect.prepareStatement("Select  customers.customerName as customer_name, concat(customers.addressLine1,customers.addressLine1)as street_address, customers.city,customers.postalCode as postal_code\r\n"
					+ ",customers.country,sum(quantityOrdered * priceEach) as order_value, sum(quantityOrdered * priceEach) - x.total as oustanding_value\r\n"
					+ "from customers \r\n"
					+ "	inner join orders on\r\n"
					+ "		orders.customerNumber = customers.customerNumber\r\n"
					+ "	inner join orderdetails on\r\n"
					+ "		orderdetails.orderNumber = orders.orderNumber\r\n"
					+ "	inner join \r\n"
					+ "    (Select customerNumber, sum(amount) as total from payments group by customerNumber) as x on\r\n"
					+ "    x.customerNumber = customers.customerNumber\r\n"
					+ "where orders.orderDate between '"+startDate+"' and '"+endDate+"'\r\n"
					+ " and customers.customerNumber not in (\r\n"
					+ "Select distinct(customers.customerNumber)\r\n"
					+ "from customers \r\n"
					+ "inner join orders on\r\n"
					+ "	orders.customerNumber = customers.customerNumber\r\n"
					+ "where orderDate < '"+startDate+"') \r\n"
					+ "group by customers.customerNumber;\r\n"
					+ "");
			ResultSet result1 = stmt1.executeQuery();
			
		    stmt = connect.prepareStatement("use csci3901");
			stmt.execute();
			
			//Query 2
			PreparedStatement stmt2  = connect.prepareStatement("Select products.productName, products.productLine, min(orders.orderDate), x.customerName, x.saleValue\r\n"
					+ "from products\r\n"
					+ "	inner join orderdetails on\r\n"
					+ "		orderdetails.productCode = products.productCode\r\n"
					+ "	inner join orders on\r\n"
					+ "		orders.orderNumber = orderdetails.orderNumber\r\n"
					+ "	inner join \r\n"
					+ "    (Select products.productCode, products.productName, customers.customerName, sum(orderdetails.quantityOrdered) as saleValue\r\n"
					+ "		from customers\r\n"
					+ "			inner join orders on\r\n"
					+ "				customers.customerNumber = orders.customerNumber\r\n"
					+ "			inner join orderdetails on\r\n"
					+ "				orderdetails.orderNumber = orders.orderNumber\r\n"
					+ "			inner join products on\r\n"
					+ "				products.productCode = orderdetails.productCode\r\n"
					+ "		where orders.orderDate between '"+startDate+"' and '"+endDate+"'\r\n"
					+ "		and products.productCode not in (\r\n"
					+ "			Select distinct(products.productCode)\r\n"
					+ "			from products \r\n"
					+ "			inner join orderdetails on\r\n"
					+ "				orderdetails.productCode = products.productCode\r\n"
					+ "			inner join orders on\r\n"
					+ "				orders.orderNumber = orderdetails.orderNumber\r\n"
					+ "			where orderDate < '"+startDate+"')\r\n"
					+ "			group by productName, customers.customerName) as x on\r\n"
					+ "	x.productCode = products.productCode\r\n"
					+ "group by products.productName limit 5;");
			
			ResultSet result2 = stmt2.executeQuery();
			
			stmt = connect.prepareStatement("use csci3901");
			stmt.execute();
			
			//Query3
			PreparedStatement stmt3  = connect.prepareStatement("Select offices.city, offices.territory, count(employees.employeeNumber), x.customer_count, x.customer_sales\r\n"
					+ "from offices\r\n"
					+ "	inner join employees on\r\n"
					+ "    employees.officeCode = offices.officeCode\r\n"
					+ "    inner join (\r\n"
					+ "Select customers.city, count(customers.customerNumber) as customer_count, sum(orderdetails.quantityOrdered * orderdetails.priceEach) as customer_sales\r\n"
					+ "from customers\r\n"
					+ "	inner join orders on\r\n"
					+ "		orders.customerNumber = customers.customerNumber\r\n"
					+ "	inner join orderdetails on\r\n"
					+ "		orderdetails.orderNumber = orders.orderNumber\r\n"
					+ "where orders.orderDate between '"+startDate+"' and '"+endDate+"'\r\n"
					+ "and customers.customerNumber not in (\r\n"
					+ "Select distinct(customers.customerNumber)\r\n"
					+ "from customers \r\n"
					+ "inner join orders on\r\n"
					+ "	orders.customerNumber = customers.customerNumber\r\n"
					+ "where orderDate < '"+startDate+"')\r\n"
					+ "group by city) as x on\r\n"
					+ "x.city = offices.city;");
			
			ResultSet result3 = stmt3.executeQuery();
			//resultToHashMap(result, result);
		    doc = dataToXML(result1,result2,result3,startDate,endDate,fileName);
		   
		    //Closing result and queries 
     	    result1.close();
			result2.close();
			result3.close();
			stmt1.close();
			stmt2.close();
			stmt2.close();
     		stmt.close();
		
		}
		return  doc;
	}
	
	/*
	 * Converting jdbc output to xml
	 * 
	 * @param  ResutSet    output of the queries
	 * @param  startDate  start date for the period
	 * @param  endDate    end date of the period
	 */
	private Document dataToXML(ResultSet rs1,ResultSet rs2 ,ResultSet rs3,String startDate,String endDate, String fileName)  throws ParserConfigurationException, SQLException, TransformerException, IOException{
		
		//Document Builder 
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder        = factory.newDocumentBuilder();
		Document doc                   = builder.newDocument();
		DOMSource domSource = null;
		
		
		try {
			
			//Metadata of query 1
		   ResultSetMetaData rsmd1 = rs1.getMetaData();
		   int colCount1          = rsmd1.getColumnCount();
		   
		   //Metadata of query 2
		   ResultSetMetaData rsmd2 = rs2.getMetaData();
		   int colCount2           = rsmd2.getColumnCount();
		   
		   //Metadata of query 3
		   ResultSetMetaData rsmd3 = rs3.getMetaData();
		   int colCount3          = rsmd3.getColumnCount();
		   
		   // Root xml element -- time_period_summary 
		   Element time = doc.createElement("time_period_summary");
		   doc.appendChild(time);
		   
		   //Xml structure for year
		   Element year = doc.createElement("year");
		   time.appendChild(year);
		   Element start_Date = doc.createElement("start_date");
		   start_Date.appendChild(doc.createTextNode(startDate));
		   year.appendChild(start_Date);
		   Element end_Date = doc.createElement("end_date");
		   end_Date.appendChild(doc.createTextNode(endDate));
		   year.appendChild(end_Date);
		   
		   
		   //Xml structure for query 1 -- customer list
		   Element customerlist = doc.createElement("customer_list");
		   time.appendChild(customerlist);
		   
		   while (rs1.next())
		   {
		      Element customer = doc.createElement("customer");
		      customerlist.appendChild(customer);
		      Element address = doc.createElement("address");
		      
		      //looping till the number of columns in the result
			  for (int i = 1; i <= colCount1; i++)
		      {
				  //fetches column name and value of the row
		    	  String columnName = rsmd1.getColumnName(i);
			      Object value      = rs1.getObject(i);
			     
			      //skips if the value is null
			      if(value == null) {
			    	  continue;
			      }
			      
			      //xml internal structure for address
			      else if(i>=2 && i <=4) {   
			    	   Element node = doc.createElement(columnName);
			    	   node.appendChild(doc.createTextNode(value.toString()));
					   customer.appendChild(node);
					       
			    	 }
			      //appends the address
			      else if(i==5) {
			    	    //fetching node and appending to its parent element
			    	    Element node = doc.createElement(columnName);
		    			 node.appendChild(doc.createTextNode(value.toString()));
				         customer.appendChild(node);
				         customer.appendChild(address);      
			       }
			    	 
		    	 else {
				        Element node = doc.createElement(columnName);
				        node.appendChild(doc.createTextNode(value.toString()));
				        customer.appendChild(node); 
				      } 
			      }        
		     }
		   	
		   //Xml structure for query - 2 product list
		   Element productlist = doc.createElement("product_list");
		   time.appendChild(productlist);
		   

      	   while (rs2.next())
		   {
		      Element productSales = doc.createElement("product_sales");
		      productlist.appendChild(productSales);
		      
		      Element customerSales = doc.createElement("customer_sales");
		       
		      //looping till the number of columns in the result
		      for (int i = 1; i <= colCount2; i++)
		      {
		    	  String columnName = rsmd2.getColumnName(i);
			      Object value      = rs2.getObject(i);
			        
			       //xml for internal customer sales element 
		    	 if(i>=4) {
		    		 Element node = doc.createElement(columnName);
			         node.appendChild(doc.createTextNode(value.toString()));
			         customerSales.appendChild(node);
		    	 }
		    	 else{
			         Element node = doc.createElement(columnName);
			         node.appendChild(doc.createTextNode(value.toString()));
			         productSales.appendChild(node); 
		    	 }
		    	 productSales.appendChild(customerSales);
		    		  
		      }
		   }
      	 
      	   //xml structure for query 3--office list
      	 Element officelist = doc.createElement("office_list");
		   time.appendChild(officelist);
		   
    	   while (rs3.next())
		   {
		      Element office = doc.createElement("office");
		      officelist.appendChild(office);
		      
		      Element newCustomer = doc.createElement("new_customer");
		       
		      for (int i = 1; i <= colCount3; i++)
		      {
		    	  String columnName = rsmd3.getColumnName(i);
			      Object value      = rs3.getObject(i);
			         
			     if (value == null) {
			    	 continue;
			     }
			     else {
			    	 if(i>=4) {
			    		 Element node      = doc.createElement(columnName);
				         node.appendChild(doc.createTextNode(value.toString()));
				         newCustomer.appendChild(node); 
			    	 }
			    	 else{
				         Element node      = doc.createElement(columnName);
				         node.appendChild(doc.createTextNode(value.toString()));
				         office.appendChild(node); 
			    	 }
			    	 office.appendChild(newCustomer);
			     }	  
		      }
		   }

    	   domSource = new DOMSource(doc);
		   TransformerFactory tf = TransformerFactory.newInstance();
		   Transformer transformer = tf.newTransformer();

		   transformer.setOutputProperty(OutputKeys.METHOD, "xml");
		   transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
			
		   transformer.setOutputProperty(
		                "{http://xml.apache.org/xslt}indent-amount", "4");
		   transformer.setOutputProperty(OutputKeys.INDENT, "yes");
		   
		  
		   FileWriter writer = new FileWriter(new File(fileName));
		   //StringWriter sw = new StringWriter();
		   StreamResult sr = new StreamResult(writer);
		   transformer.transform(domSource, sr);
						
		   //System.out.println(sw.toString());
			
		} catch (SQLException sqlExp) {

			System.out.println("SQLExcp:" + sqlExp.toString());

		} finally {
			try {

				if (rs1!= null && rs2 != null) {
					rs1.close();
					rs1 = null;
					rs2.close();
					rs2 = null;
				}
				if (connect != null) {
					connect.close();
					connect = null;
				}
			} catch (SQLException expSQL) {
				System.out
						.println("CourtroomDAO::loadCourtList:SQLExcp:CLOSING:"
								+ expSQL.toString());
			}
		}

		 

		return doc;

	}
	 /*
	  * Checks for valid date formats
	  */
	 public boolean isValidFormat(String startDate, String endDate) {
		    String format = "yyyy-MM-dd";
	        Date date1 = null;
	        Date date2 = null;
	        try {
	            SimpleDateFormat sdf = new SimpleDateFormat(format);
	            date1 = sdf.parse(startDate);
	            date2 = sdf.parse(endDate);
	            if (!startDate.equals(sdf.format(date1)) && !endDate.equals(sdf.format(date2))) {
	                date1 = null;
	                date2 = null;
	            }
	            else if(!date1.before(date2)) {
	            	date1 = null;
	                date2 = null;
	            }
	        } catch (ParseException ex) {
	            ex.printStackTrace();
	        }
	        return (date1 != null && date2 != null);
	    }

		/**
		  * Checks whether the object given is null or not
		  * 
		  * @param   obj    object
		  * @return         boolean 
		  */
		public boolean notNull(Object obj) {
			if(obj != null) {
				return true;
			}
			System.out.println(" Error: Empty Input");
			return false;
		}
}
