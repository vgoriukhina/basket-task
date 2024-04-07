package org.example;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class BasketSplitter {
    private final String absolutePathToConfigFile;

    private final Map<String,List<String>> products;
    public BasketSplitter(String absolutePathToConfigFile) {

        this.absolutePathToConfigFile = absolutePathToConfigFile;
        this.products = readJSON();
    }

    private Map<String,List<String>> readJSON(){
        Map<String,List<String>> readConfig = new HashMap<>();
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode rootNode = objectMapper.readTree(new File(absolutePathToConfigFile));

            Map<String, List<String>> data = new HashMap<>();
            int totalItems = 0;
            int totalDeliveryOptions = 0;

            for (Iterator<Map.Entry<String, JsonNode>> fields = rootNode.fields(); fields.hasNext(); ) {
                Map.Entry<String, JsonNode> entry = fields.next();
                String key = entry.getKey();
                List<String> value = new ArrayList<>();

                for (JsonNode node : entry.getValue()) {
                    value.add(node.asText());
                    totalDeliveryOptions++;
                }
                totalDeliveryOptions=0;

                data.put(key, value);
                totalItems++;
            }

            // check the capacity
            if (totalItems > 1000 || totalDeliveryOptions > 10) {
                throw new IllegalArgumentException("Too many items or delivery options.");
            }

            readConfig.putAll(data);


        } catch (IOException e) {
            e.printStackTrace();
        }

        return readConfig;
    }
    public Map<String, List<String>> split(List<String> items) {
        Map<String, List<String>> deliveryGroups = new HashMap<>();
        Map<String, List<String>> result = new HashMap<>();

        if(items.size()>100){
            throw new IndexOutOfBoundsException("Basket must have max 100 items.");
        }


        for (String item : items) {     //find deliveries of basket's items
            if (!products.containsKey(item)) {
                throw new IllegalArgumentException("Basket contains wrong product.");
            } else {
                for (String delivery : products.get(item)) {
                    if (!deliveryGroups.containsKey(delivery)) {
                        deliveryGroups.put(delivery, new ArrayList<>(Arrays.asList(item)));
                    } else {
                        List<String> productsList = deliveryGroups.get(delivery);
                        productsList.add(item);
                        deliveryGroups.put(delivery, productsList);
                    }
                }
            }
    }
        System.out.println("Basket:");
        for(String item: items){
            System.out.print(item+"; ");
        }
        System.out.println("\n------------------------------");

        System.out.println("Delivery options:");
        for (Map.Entry<String, List<String>> entry : deliveryGroups.entrySet()) {
            System.out.println(entry.getKey() + ": " + entry.getValue());
        }

        while (!deliveryGroups.isEmpty()) {
            String largestDelivery = chooseLargestDelivery(deliveryGroups);
            List<String> largestGroup = deliveryGroups.get(largestDelivery);


            cleanUpDeliveryGroups(deliveryGroups, largestGroup);


            result.put(largestDelivery,deliveryGroups.get(largestDelivery));
            deliveryGroups.remove(largestDelivery);

        }

        System.out.println("------------------------------\nOptimal delivery");
        for (Map.Entry<String, List<String>> entry : result.entrySet()) {
            System.out.println(entry.getKey() + ": " + entry.getValue());
        }
        System.out.println("------------------------------------------------------------");
        return result;
    }

    private String chooseLargestDelivery(Map<String, List<String>> deliveryGroups) {
        String largestDelivery = null;
        int maxItemCount = 0;
        for (Map.Entry<String, List<String>> entry : deliveryGroups.entrySet()) {
            if (entry.getValue().size() > maxItemCount) {
                maxItemCount = entry.getValue().size();
                largestDelivery = entry.getKey();
            }
        }
        return largestDelivery;
    }


    private void cleanUpDeliveryGroups(Map<String, List<String>> deliveryGroups, List<String> largestGroup) {
        for (List<String> group : deliveryGroups.values()) {
            if (group!=largestGroup) {
                group.removeAll(largestGroup);
            }
        }
        deliveryGroups.entrySet().removeIf(entry -> entry.getValue().isEmpty());
    }

    public Map<String, List<String>> getProducts() {
        return products;
    }
}

