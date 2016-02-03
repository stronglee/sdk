package com.example;

import de.greenrobot.daogenerator.DaoGenerator;
import de.greenrobot.daogenerator.Entity;
import de.greenrobot.daogenerator.Property;
import de.greenrobot.daogenerator.Schema;
import de.greenrobot.daogenerator.ToMany;

public class GeneratorGreenDao {

    public static void main(String[] args) throws Exception {
        Schema schema = new Schema(1, "greendao");

        addNote(schema);
        addCustomerOrder(schema);
        addUser(schema);
        addInfo(schema);
        new DaoGenerator().generateAll(schema, args[0]);
    }

    private static void addNote(Schema schema) {
        Entity note = schema.addEntity("Note");
        // 实现序列化
        note.implementsSerializable();
        note.addIdProperty().autoincrement();
        note.addStringProperty("text").notNull();
        note.addStringProperty("comment");
        note.addDateProperty("date");
    }

    private static void addCustomerOrder(Schema schema) {
        Entity customer = schema.addEntity("Customer");
        customer.addIdProperty();
        customer.addStringProperty("name").notNull();

        Entity order = schema.addEntity("Order");
        order.setTableName("ORDERS");
        order.addIdProperty();
        Property orderDate = order.addDateProperty("date").getProperty();
        Property customerId = order.addLongProperty("customerId").notNull().getProperty();
        order.addToOne(customer, customerId);
        ToMany customerToOrders = customer.addToMany(order, customerId);
        customerToOrders.setName("orders");
        customerToOrders.orderAsc(orderDate);
    }

    private static void addUser(Schema schema) {
        Entity userBean = schema.addEntity("Users");
        userBean.setTableName("Users");
        userBean.addIdProperty();
        userBean.addStringProperty("sex");
        userBean.addStringProperty("phone");
        userBean.addStringProperty("age");
        userBean.addStringProperty("name");
    }

    private static void addInfo(Schema schema) {
        Entity typeEntity = schema.addEntity("infoType");
        typeEntity.implementsSerializable();
        typeEntity.addIdProperty();
        typeEntity.addStringProperty("infoName");

        Entity infoEntity = schema.addEntity("infos");
        infoEntity.implementsSerializable();
        infoEntity.addIdProperty();
        infoEntity.addStringProperty("infoTitle");
        infoEntity.addStringProperty("infoAuthor");
        infoEntity.addStringProperty("infoContent");
        Property typeId = infoEntity.addLongProperty("typeId").getProperty();
        infoEntity.addToOne(typeEntity, typeId);

        ToMany addToMany = typeEntity.addToMany(infoEntity, typeId);
        addToMany.setName("infoes");
    }
}
