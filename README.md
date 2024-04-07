# Basket Splitter Library

## Overview

We aimed to extend our online supermarket's product range to include non-food items like electronics and household goods. However, these items often require specialized courier delivery due to their size or sourcing from external suppliers. Consequently, we needed to divide the customer's basket items into delivery groups to optimize the delivery process.

## Task

The task was to create a library that divides the items in the customer's basket into delivery groups. The library should load a configuration file containing the possible delivery methods for all offered products in the store. The goal was to minimize the number of delivery groups while maximizing the size of each group.

## Completed Work

- Implemented the `BasketSplitter` class in Java, which:
  - Loads the product configuration from a JSON file.
  - Divides the items in the customer's basket into delivery groups.
  - Minimizes the number of delivery groups while maximizing the size of each group.
- Ensured the correctness, readability, and test coverage of the implemented code.
- Created unit tests to verify the functionality of the `BasketSplitter` class.

##Instalation
-Clone the repository to your local machine.
-Import the project into your preferred Java IDE.
-Add the necessary dependencies (Jackson ObjectMapper) to your project.
-Initialize absolute path to the configuration file.
-min. Java 17
