package com.example.parks.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Playground {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String type;
    private String openingHours;
    @OneToOne
    @JoinColumn(referencedColumnName = "id", name = "addressId")
    private Address address;
    private String email;
    private String phone;
    private String parkRanger;
    private String surfaceType;
    private String toilets;
    private String disabledToilets;
    private String babyChanging;
    private String drinkingWater;
    private String disabledParking;
    private String seating;

    public Playground(String name, String type, String openingHours, Address address, String email, String phone, String parkRanger, String surfaceType, String toilets, String disabledToilets, String babyChanging, String drinkingWater, String disabledParking, String seating) {
        this.name = name;
        this.type = type;
        this.openingHours = openingHours;
        this.address = address;
        this.email = email;
        this.phone = phone;
        this.parkRanger = parkRanger;
        this.surfaceType = surfaceType;
        this.toilets = toilets;
        this.disabledToilets = disabledToilets;
        this.babyChanging = babyChanging;
        this.drinkingWater = drinkingWater;
        this.disabledParking = disabledParking;
        this.seating = seating;
    }
}
