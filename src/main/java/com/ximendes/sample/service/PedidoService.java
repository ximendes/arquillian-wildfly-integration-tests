package com.ximendes.sample.service;

import com.ximendes.sample.entity.Item;
import com.ximendes.sample.entity.Pedido;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.ArrayList;
import java.util.Collection;

@Stateless
@LocalBean
public class PedidoService {

    @PersistenceContext
    private EntityManager entityManager;

    public void addItem(Item item, Integer pedidoId) {
        final Pedido pedido = entityManager.find(Pedido.class, pedidoId);
        pedido.getItems().add(item);
        item.setPedido(pedido);
        entityManager.persist(item);
    }

    public void addPedido(Pedido pedido, Item item) {
        Collection<Item> itens = new ArrayList<>();
        pedido.setItems(itens);
        itens.add(item);
        item.setPedido(pedido);
        entityManager.persist(pedido);
        entityManager.persist(item);
    }
}
