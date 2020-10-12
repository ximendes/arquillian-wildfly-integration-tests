package com.ximendes.sample;

import com.ximendes.sample.entity.Item;
import com.ximendes.sample.entity.Pedido;
import com.ximendes.sample.service.PedidoService;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.persistence.*;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.ejb.EJB;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

import static org.junit.Assert.*;

@RunWith(Arquillian.class)
@CleanupUsingScript("clean-database.sql")
@UsingDataSet("init-database.xml")
public class PedidoServiceTest {

    @Deployment
    public static Archive<?> createDeploymentPackage() {
        return ShrinkWrap.create(WebArchive.class)
                         .addPackage(Pedido.class.getPackage())
                         .addClass(PedidoService.class)
                         .addAsResource("persistence-h2.xml", "META-INF/persistence.xml");
    }

    @PersistenceContext
    private EntityManager entityManager;

    @EJB
    private PedidoService pedidoService;

    @Test
    public void addItemTest() {
        Item item = Item.builder().id(2002).name("AirPods").build();
        pedidoService.addItem(item, 200);
        List<Item> itens = this.entityManager.createQuery("SELECT item FROM Item item ORDER BY item.id", Item.class)
                                             .getResultList();

        assertNotNull(itens);
        assertFalse(itens.isEmpty());
        assertEquals(6, itens.size());
    }

    @Test
    public void addPedidoTest() {
        Pedido pedido = Pedido.builder().id(300).number("AQY1").build();
        Item item = Item.builder().id(3000).name("AirPods").build();

        pedidoService.addPedido(pedido, item);
        List<Pedido> orders = this.entityManager.createQuery("SELECT pedido FROM Pedido pedido ORDER BY pedido.id", Pedido.class)
                                                .getResultList();

        List<Item> itens = this.entityManager.createQuery("SELECT item FROM Item item ORDER BY item.id", Item.class)
                                             .getResultList();
        assertNotNull(orders);
        assertNotNull(itens);

        assertFalse(orders.isEmpty());
        assertFalse(itens.isEmpty());

        assertEquals(3, orders.size());
        assertEquals(6, itens.size());
    }
}
