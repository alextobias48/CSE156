package unl.cse;

public class Academic extends Customers{

	public Academic(String customerId, String custType, String customerName,
			Address address, String email) {
		super(customerId, custType, customerName, address, email);
		
	}
	
	public Double getBulkItemDiscount(Double subTotal, int numbUnits){
		double discount = 0.0;
		subTotal *= numbUnits;
		
		if(10 <= numbUnits && numbUnits <= 19){
			discount += subTotal *.1;}
		else if(numbUnits >= 20){
			discount += subTotal * .2;
		}
		return discount;
	}
	
	public Double getOrderDiscount(Double subTotal, int numbOrders){
		if(numbOrders > 0){
		return subTotal * .05;}
		else{
			return 0.0;
		}
		 
	}
	
	public Double getFee(int numbOrders){
		return 0.0;
	}
	
	public Double getTaxes(Double subTotal){
		return 0.0;
	}

}
