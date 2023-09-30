
import javax.xml.bind.annotation.XmlRootElement;
@XmlRootElement

public class ProductList {

	 private String product_name;
	 private String product_line;
	 private String introduction_date;
	 private String customer_name;
	 private String units_sold;
	 
	 
	public String getProduct_name() {
		return product_name;
	}
	public void setProduct_name(String product_name) {
		this.product_name = product_name;
	}
	public String getProduct_line() {
		return product_line;
	}
	public void setProduct_line(String product_line) {
		this.product_line = product_line;
	}
	public String getIntroduction_date() {
		return introduction_date;
	}
	public void setIntroduction_date(String introduction_date) {
		this.introduction_date = introduction_date;
	}
	public String getCustomer_name() {
		return customer_name;
	}
	public void setCustomer_name(String customer_name) {
		this.customer_name = customer_name;
	}
	public String getUnits_sold() {
		return units_sold;
	}
	public void setUnits_sold(String units_sold) {
		this.units_sold = units_sold;
	}
	
}
