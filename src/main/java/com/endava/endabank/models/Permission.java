package com.endava.endabank.models;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "permissions")
public class Permission {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(length = 40, nullable = false)
    private String name;
    @ManyToMany(cascade = CascadeType.ALL, mappedBy = "permissions")
    private List<Role> roles;

}
