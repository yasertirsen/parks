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
    private boolean toilets;
    private boolean disabledToilets;
    private boolean babyChanging;
    private boolean drinkingWater;
    private boolean disabledParking;
    private boolean seating;
}
