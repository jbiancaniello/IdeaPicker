public class Restaurant {
	private String name;
    private String cuisine;
    private String address;
    private int desire;

    public Restaurant(String name, String cuisine, String address, int desire) {
        this.name = name;
        this.cuisine = cuisine;
        this.address = address;
        this.desire = desire;
    }

    public String toString() {
        String str = "";
        str += "Name: " + name + "\n";
        str += "Cuisine: " + cuisine + "\n";
        str += "Address: " + address + "\n";
        str += "How bad do you want to try this restaurant: " + desire + "\n";
        return str;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCuisine() {
        return cuisine;
    }

    public void setCuisine(String cuisine) {
        this.cuisine = cuisine;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getDesire() {
        return desire;
    }

    public void setDesire(int desire) {
        this.desire = desire;
    }
}