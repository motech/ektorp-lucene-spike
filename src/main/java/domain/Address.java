package domain;

import lombok.Data;

@Data
public class Address {
    private String addressLine1;
    private String street;
    private String city;
    private String state;

    public Address(){ }

    public Address(String addressLine1, String street, String city, String state) {
        this.addressLine1 = addressLine1;
        this.street = street;
        this.city = city;
        this.state = state;
    }
}
