package app.models;

import app.models.relations.Product_Warehouse;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonIncludeProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;

import java.util.HashSet;
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
    private int maxStorage;
    private int minStorage;
    private int currentStorage;

    @OneToMany(mappedBy = "warehouse", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnoreProperties({"warehouse"})
    private final Set<Team> teams = new HashSet<>();

    @OneToMany(mappedBy = "warehouse", cascade = CascadeType.ALL)
    @JsonIgnoreProperties({"warehouse"})
    private final Set<Product_Warehouse> products = new HashSet<>();

    public Warehouse(){

    }

    public Warehouse(int id, String name, String city, String address, String postalCode, int maxStorage, int minStorage,int currentStorage) {
        this.id = id;
        this.name = name;
        this.city = city;
        this.address = address;
        this.postalCode = postalCode;
        this.maxStorage = maxStorage;
        this.minStorage = minStorage;
        this.currentStorage = currentStorage;
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
        int maxStorage = 100;
        int minStorage = maxStorage / 5;
        int currentStorage = 35;

        return new Warehouse(id, name, city, address, postalCode, maxStorage, minStorage, currentStorage );

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
    public int getMaxStorage() {
        return maxStorage;
    }

    public void setMaxStorage(int maxStorage) {
        this.maxStorage = maxStorage;
    }

    public int getMinStorage() {
        return minStorage;
    }

    public void setMinStorage(int minStorage) {
        this.minStorage = minStorage;
    }

    public int getCurrentStorage() {
        return currentStorage;
    }

    public void setCurrentStorage(int currentStorage) {
        this.currentStorage = currentStorage;
    }


    public Set<Product_Warehouse> getProducts() {
        return products;
    }

    public void addProduct(Product_Warehouse product) {
        Product_Warehouse existingProductInWarehouse = this.products.stream().filter(productWarehouse -> productWarehouse.getProduct().equals(product.getProduct())).findAny().orElse(null);
        if (existingProductInWarehouse != null) {
            existingProductInWarehouse.setAmount(existingProductInWarehouse.getAmount() + product.getAmount());
        } else {
            this.products.add(product);
        }
    }

    public void removeProduct(Product_Warehouse product) {
        this.products.remove(product);
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
