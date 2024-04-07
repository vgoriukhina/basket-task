package org.example;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.*;

public class BasketSplitterTest {

    private BasketSplitter basketSplitter;

    @BeforeEach
    public void setUp() throws IOException {
        String absolutePathToConfigFile = "Zadanie/config.json";
        basketSplitter = new BasketSplitter(absolutePathToConfigFile);
    }

    @Test
    public void testSplit() {

        List<String> items = Arrays.asList("Cocoa Butter", "Tart - Raisin And Pecan", "Table Cloth 54x72 White", "Flower - Daisies", "Fond - Chocolate", "Cookies - Englishbay Wht");


        Map<String, List<String>> deliveryGroups = basketSplitter.split(items);

        //check empty
        Assertions.assertFalse(deliveryGroups.isEmpty());

        // check items in config
        for (String item : items) {
            boolean found = false;
            for (List<String> group : deliveryGroups.values()) {
                if (group.contains(item)) {
                    found = true;
                    break;
                }
            }
            Assertions.assertTrue(found, "Product " + item + " is not found in any delivery group.");
        }
    }

    @Test
    public void testSplitWithWrongProduct() {
        List<String> items = Arrays.asList("Steak", "Carrot", "AA Battery", "Invalid Product", "Garden Chair");

        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            basketSplitter.split(items);
        });
    }

    @Test
    public void testSplitWithEmptyBasket() {

        List<String> items = Collections.emptyList();

        Map<String, List<String>> deliveryGroups = basketSplitter.split(items);
        Assertions.assertTrue(deliveryGroups.isEmpty());
    }

    @Test
    public void testSplitWithLargeBasket() {

        List<String> items = new ArrayList<>();
        for (int i = 0; i < 101; i++) {
            items.add("Product" + i);
        }


        Assertions.assertThrows(IndexOutOfBoundsException.class, () -> {
            basketSplitter.split(items);
        });
    }

    @Test
    public void testConfigCategoriesCount() throws IOException {
        String absolutePathToConfigFile = "Zadanie/config.json";
        BasketSplitter basketSplitter = new BasketSplitter(absolutePathToConfigFile);

        Map<String, List<String>> products = basketSplitter.getProducts();
        for (List<String> categories : products.values()) {
            Assertions.assertTrue(categories.size() <= 10, "Number of categories in a product exceeds 10");
        }
    }
}
