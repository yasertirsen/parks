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
@AllArgsConstructor
@NoArgsConstructor
public class Park {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String openingHours;
    @OneToOne
    @JoinColumn(referencedColumnName = "id", name = "addressId")
    private Address address;
    private String email;
    private String phone;
    @OneToMany
    @JoinColumn(referencedColumnName = "id", name = "facilityId")
    private List<Facility> facilities;

}
