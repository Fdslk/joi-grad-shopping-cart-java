package com.thoughtworks.codepairing.model;

import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

public class ShoppingCart {
    private List<Product> products;
    private Customer customer;

    public ShoppingCart(Customer customer, List<Product> products) {
        this.customer = customer;
        this.products = products;
    }

    private static HashMap<String, Integer> apply(Product x) {
        HashMap<String, Integer> stringIntegerHashMap = new HashMap<>();
        if (stringIntegerHashMap.containsKey(x.getName())) {
            stringIntegerHashMap.put(x.getName(), stringIntegerHashMap.get(x.getName()) + 1);
        } else {
            stringIntegerHashMap.put(x.getName(), 1);
        }
        return stringIntegerHashMap;
    }

    private static Integer apply(HashMap<String, Integer> x) {
        int totalPrice = 0;
        for (int itemNum : x.values()) {
            totalPrice += (itemNum + itemNum / 2) * 100;
        }
        return totalPrice;
    }

    public void addProduct(Product product) {
        products.add(product);
    }

    public Order checkout() {
        double totalPrice = 0;

        int loyaltyPointsEarned = 0;
        for (Product product : products) {
            double discount = 0;
            if (product.getProductCode().startsWith("DIS_10")) {
                discount = (product.getPrice() * 0.1);
                loyaltyPointsEarned += (product.getPrice() / 10);
            } else if (product.getProductCode().startsWith("DIS_15")) {
                discount = (product.getPrice() * 0.15);
                loyaltyPointsEarned += (product.getPrice() / 15);
            } else if (product.getProductCode().startsWith("DIS_20")) {
                discount = (product.getPrice() * 0.2);
                loyaltyPointsEarned += (product.getPrice() / 20);
            } else {
                loyaltyPointsEarned += (product.getPrice() / 5);
            }

            totalPrice += product.getPrice() - discount;
        }

        return new Order(totalPrice, loyaltyPointsEarned);
    }

    public Order checkForBULK_BUY_2_GET_1() {
        Integer bulk_buy_2_get_1 = products.stream()
                .filter(p -> p.getProductCode().equals("BULK_BUY_2_GET_1"))
                .map(ShoppingCart::apply)
                .map(
                        ShoppingCart::apply
                )
                .reduce(0, (l, r)-> l + r);

        return new Order(bulk_buy_2_get_1, bulk_buy_2_get_1/5);
    }

    @Override
    public String toString() {
        return "Customer: " + customer.getName() + "\n" + "Bought:  \n" + products.stream().map(p -> "- " + p.getName() + ", " + p.getPrice()).collect(Collectors.joining("\n"));
    }
}
