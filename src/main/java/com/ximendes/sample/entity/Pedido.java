package com.ximendes.sample.entity;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Collection;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Pedido implements Serializable {

    @Id
    private Integer id;

    @Column(nullable = false)
    private String number;

    @OneToMany(mappedBy = "pedido")
    private Collection<Item> items;
}
