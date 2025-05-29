package com.viniciussouza.tests.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.hibernate.proxy.HibernateProxy;

@Entity
@Table(name = "planets")
@Getter
@Setter
public class Planet {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotEmpty
    @Column(nullable = false, unique = true)
    private String name;

    @NotEmpty
    @Column(nullable = false)
    private String terrain;

    @NotEmpty
    @Column(nullable = false)
    private String climate;

    public Planet() {
    }

    public Planet(String climate, String terrain) {
        this.climate = climate;
        this.terrain = terrain;
    }

    public Planet(String name, String climate, String terrain) {
        this.name = name;
        this.climate = climate;
        this.terrain = terrain;
    }

    public Planet(Long id, String name, String climate, String terrain) {
        this.id = id;
        this.name = name;
        this.climate = climate;
        this.terrain = terrain;
    }

    @Override
    public final boolean equals(Object obj) {
        return EqualsBuilder.reflectionEquals( obj, this);
    }

    @Override
    public final int hashCode() {
        return this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass().hashCode() : getClass().hashCode();
    }

    @Override
    public String toString() {
        return "Planet{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", terrain='" + terrain + '\'' +
                ", climate='" + climate + '\'' +
                '}';
    }
}
