package com.ximendes.sample.entity;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Item implements Serializable {

    @Id
    private Integer id;

    @Column(nullable = false)
    private String name;

    @JoinColumn(nullable = false)
    @ManyToOne
    private Pedido pedido;
}
