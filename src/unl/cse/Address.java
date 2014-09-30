package unl.cse;

public class Address{
	
String street;
String city;
String state;
String zip;
String country;

//address constructor using getters and setters
public Address(String street, String city, String state, String zip,
		String country) {
	this.setStreet(street);
	this.setCity(city);
	this.setState(state);
	this.setZip(zip);
	this.setCountry(country);
}
public String getStreet() {
	return this.street;
}
public void setStreet(String street) {
	this.street = street;
}
public String getCity() {
	return this.city;
}
public void setCity(String city) {
	this.city = city;
}
public String getState() {
	return this.state;
}
public void setState(String state) {
	this.state = state;
}
public String getZip() {
	return this.zip;
}
public void setZip(String zip) {
	this.zip = zip;
}
public String getCountry() {
	return this.country;
}
public void setCountry(String country) {
	this.country = country;
}

}
