package com.restaurant;

import com.restaurant.model.RestaurantTable;
import com.restaurant.repository.RestaurantTableRepository;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.boot.CommandLineRunner;

@SpringBootApplication
@EntityScan("com.restaurant.model")
@EnableJpaRepositories("com.restaurant.repository")
public class RestaurantApplication {
    public static void main(String[] args) {
        SpringApplication.run(RestaurantApplication.class, args);
    }

    @Bean
    public CommandLineRunner initializeTables(RestaurantTableRepository tableRepository) {
        return args -> {
            if (tableRepository.count() == 0) {
                // Create sample tables
                for (int i = 1; i <= 10; i++) {
                    RestaurantTable table = new RestaurantTable();
                    table.setTableNumber(i);
                    table.setCapacity(i <= 5 ? 4 : 6); // First 5 tables for 4 people, rest for 6
                    table.setStatus(RestaurantTable.TableStatus.AVAILABLE);
                    table.setIsOccupied(false);
                    tableRepository.save(table);
                }
            }
        };
    }
} 