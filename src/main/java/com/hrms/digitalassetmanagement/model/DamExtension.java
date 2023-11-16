package com.hrms.digitalassetmanagement.model;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DamExtension {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "dam_extension_id")
    Integer id;

    @Column(name = "name", length = 10)
    String name;

    @Column(name = "icon_uri")
    String iconUri;
}
