package app.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;

import java.util.Set;

@Entity
public class Warehouse {

    @Id
    @GeneratedValue
    private int id;
    private String name;
    private String city;
    private String address;
    private String postalCode;

    @OneToMany(mappedBy = "warehouse", cascade = CascadeType.ALL)
    @JsonIgnoreProperties("warehouse")
    private Set<Team> teams;

    @OneToMany(mappedBy = "warehouse", cascade = CascadeType.REMOVE)
    @JsonIgnoreProperties({"warehouse"})
    private Set<Product> products;

    public Warehouse(int id, String name, String city, String address, String postalCode) {
        this.id = id;
        this.name = name;
        this.city = city;
        this.address = address;
        this.postalCode = postalCode;
    }

    public Warehouse(){

    }

    public static Warehouse createSampleOffer(int id){
        String name = "Warehouse " + id;
        String city = String.valueOf(City.values()[(int)(Math.random() * City.values().length)]);
        String address = "Street" + id;
        int number1 = (int) Math.floor((Math.random() * 9 + 1));
        int number2 = (int) Math.floor((Math.random() * 9 + 1));
        int number3 = (int) Math.floor((Math.random() * 9 + 1));
        int number4 = (int) Math.floor((Math.random() * 9 + 1));
        String postalCode = number1+ "" + number2 + "" + number3 + "" + number4 + " AB";

        return new Warehouse(id, name, city, address, postalCode);

    }

    public enum City {
        Amsterdam("Amsterdam"),
        Den_Helder("Den Helder"),
        Zaandam("Zaandam"),
        Utrecht("Utrecht");
        private final String city;
        City(String city) {
            this.city = city;
        }
    }


    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getCity() {
        return city;
    }

    public String getAddress() {
        return address;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public Set<Product> getProducts() {
        return products;
    }

    public void addProduct(Product product) {
        this.products.add(product);
    }
    public Set<Team> getTeams() {
        return teams;
    }

    public void addTeam(Team team) {
        this.teams.add(team);
        team.setWarehouse(this);
    }

    public void removeTeam(Team team) {
        this.teams.remove(team);
        team.setWarehouse(null);
    }
}
