package com.endava.endabank.model;

import lombok.*;
import org.hibernate.Hibernate;

import javax.persistence.*;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "identifier_types")
@Getter
@Setter
@ToString
@RequiredArgsConstructor
public class IdentifierType {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column( nullable = false)
    private Integer id;
    @Column(nullable = false, length = 20)
    private String name;
    @OneToMany(mappedBy =  "identifierType" )
    @ToString.Exclude
    private List<User> user;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        IdentifierType that = (IdentifierType) o;
        return id != null && Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
