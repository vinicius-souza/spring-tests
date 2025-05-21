package com.viniciussouza.tests.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.hibernate.proxy.HibernateProxy;

@Entity
@Table(name = "planets")
@Getter
@Setter
@ToString
@RequiredArgsConstructor
public class Planet {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String terrain;
    private String climate;

    public Planet(String name, String terrain, String climate) {
        this.name = name;
        this.terrain = terrain;
        this.climate = climate;
    }

    @Override
    public final boolean equals(Object obj) {
        return EqualsBuilder.reflectionEquals( obj, this);
    }

    @Override
    public final int hashCode() {
        return this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass().hashCode() : getClass().hashCode();
    }
}
