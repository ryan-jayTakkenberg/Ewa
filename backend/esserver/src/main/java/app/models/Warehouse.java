package app.models;

import java.time.LocalDate;

public class Warehouse {

    private int id = 2000;
    private String name;
    private City city;
    private String address;
    private PostalCode postalCode;

    public Warehouse(int id, String name, City city, String address, PostalCode postalCode) {
        this.id = id;
        this.name = name;
        this.city = city;
        this.address = address;
        this.postalCode = postalCode;
    }

    public Warehouse(String name){
        this.name = name;
    }

    public static Warehouse createSampleOffer(int id){
        String name = "Warehouse " + id;
        City city = City.values()[(int)(Math.random() * City.values().length)];
        String address = "Street" + id;
        PostalCode postalCode = PostalCode.values()[(int)(Math.random()* PostalCode.values().length)];

        return new Warehouse(id, name, city, address, postalCode);

    }

    public enum City {
        AMSTERDAM("Amsterdam"),
        DEN_HELDER("Den Helder"),
        ZAANDAM("Zaandam"),
        PURMEREND("Purmerend"),
        UTRECHT("Utrecht");
        private final String city;
        City(String city) {
            this.city = city;
        }
    }

    public enum PostalCode {
        AMSTERDAM("1887HG"),
        DEN_HELDER("1003AB"),
        ZAANDAM("1023JK"),
        PURMEREND("1908JK"),
        UTRECHT("2300QW");
        private final String postalCode;
        PostalCode(String postalCode) {
            this.postalCode = postalCode;
        }
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public City getCity() {
        return city;
    }

    public String getAddress() {
        return address;
    }

    public PostalCode getPostalCode() {
        return postalCode;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setCity(City city) {
        this.city = city;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setPostalCode(PostalCode postalCode) {
        this.postalCode = postalCode;
    }
}
