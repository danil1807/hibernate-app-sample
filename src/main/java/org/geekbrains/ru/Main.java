package org.geekbrains.ru;

/**
 * 1. Создайте сущность Product (Long id, String title, int price)
 * и таблицу в базе данных для хранения объектов этой сущности;
 * 2. Создайте класс ProductDao и реализуйте в нем логику выполнения
 * CRUD-операций над сущностью Product
 * (Product findById(Long id), List<Product> findAll(), void deleteById(Long id),
 * Product saveOrUpdate(Product product));
 * 3. * Вшить ProductDao в веб-проект, и показывать товары, лежащие в базе данных.
 * Помните что в таком случае SessionFactory или обертку над ней надо будет делать в виде Spring бина.
 */

import org.flywaydb.core.Flyway;
import org.geekbrains.ru.domain.Product;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import java.util.List;

public class Main {

    public static SessionFactory factory;

    private static void init() {
        factory = new Configuration()
                .configure("config/hibernate.cfg.xml")
                .buildSessionFactory();
    }

    private static void shutdown() {
        factory.close();
    }

    public static void main(String[] args) {
//        Flyway flyway = Flyway.configure()
//                .dataSource("jdbc:postgresql://localhost:5432/simple-app", "postgres", "postgrespass").load();
//        flyway.migrate();
        try {
            init();
            getCollectionSimpleItems();
        } finally {
            shutdown();
        }
    }

    // ACID
    public static void createSimpleItem() {
        try (Session session = factory.getCurrentSession()) {
            session.beginTransaction();
            Product item = new Product("Gift 1", 200);
            System.out.println(item);
            session.save(item);
            System.out.println(item);
            session.getTransaction().commit();
            System.out.println(item);
        }
    }

    public static void getSimpleItem() {
        try (Session session = factory.getCurrentSession()) {
            session.beginTransaction();

            Product item = session.get(Product.class, 1L);
            System.out.println(item);

            session.getTransaction().commit();
        }
    }

    public static void updateSimpleItem() {
        try (Session session = factory.getCurrentSession()) {
            session.beginTransaction();

            Product item = session.get(Product.class, 1L);
            item.setPrice(200);

            session.getTransaction().commit();
        }
    }

    public static void deleteSimpleItem() {
        try (Session session = factory.getCurrentSession()) {
            session.beginTransaction();

            Product item = session.get(Product.class, 3L);
            session.delete(item);

            session.getTransaction().commit();
        }
    }

    public static void getCollectionSimpleItems() {
        try (Session session = factory.getCurrentSession()) {
            session.beginTransaction();

            List<Product> resultList = session.createQuery("select s from Product s where s.price <= 100", Product.class)
                    .getResultList();

            System.out.println(resultList);
            session.getTransaction().commit();
        }
    }

}
