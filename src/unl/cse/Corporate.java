package unl.cse;

public class Corporate extends Customers{

	public Corporate(String customerId, String custType, String customerName,
			Address address, String email) {
		super(customerId, custType, customerName, address, email);
	}
	
	public Double getBulkItemDiscount(Double subTotal, int numbUnits){
		double discount = 0.0;
		subTotal *= numbUnits;
		
		if(numbUnits >= 11 && numbUnits <= 49){
			discount += subTotal * .1;
		}
		else if(numbUnits >= 50 && numbUnits <= 99){
			discount += subTotal * .15;
		}
		else if(numbUnits >= 100){
			discount += subTotal * .2;
		}
		
		
		return discount;
	}
	
	public Double getOrderDiscount(Double subTotal, int numbOrders){
		double discount = 0.0;
		if(subTotal >= 1000.00 && subTotal <= 4999.99){
			discount += subTotal * .1;
		}
		else if(subTotal >= 5000.00){
			discount += subTotal * .2;
		}
		return discount;
	}
	
	public Double getFee(int numbOrders){
		if(numbOrders > 0){
			return 100.00;
		}
		else{
			return 0.0;
		}
	}
	
	public Double getTaxes(Double subTotal){
		return subTotal * .03;
	}

}
