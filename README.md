# Restaurant Management System

A comprehensive restaurant management system built with Spring Boot and Thymeleaf that helps manage tables, orders, menu items, inventory, and reservations.

## Features

- **Dashboard**: Overview of recent orders, available tables, low stock items, and today's reservations
- **Table Management**: Track table status (available/occupied) and capacity
- **Order Management**: Create and manage orders with multiple items
- **Menu Management**: Add, edit, and remove menu items
- **Inventory Management**: Track stock levels and low stock items
- **Reservation System**: Manage table reservations

## Prerequisites

- Java 17 or higher
- Maven 3.6 or higher
- MariaDB 10.6 or higher
- Git (optional, for version control)

## Installation

1. Clone the repository:
```bash
git clone <repository-url>
cd restaurant-management-system
```

2. Create a MariaDB database:
```sql
CREATE DATABASE restaurant_db;
```

3. Update database configuration in `src/main/resources/application.properties`:
```properties
spring.datasource.url=jdbc:mariadb://localhost:3306/restaurant_db?createDatabaseIfNotExist=true
spring.datasource.username=your_username
spring.datasource.password=your_password
```

4. Build the project:
```bash
mvn clean install
```

5. Run the application:
```bash
mvn spring-boot:run
```

The application will be available at `http://localhost:8080`

## Project Structure

```
src/
├── main/
│   ├── java/
│   │   └── com/restaurant/
│   │       ├── controller/    # Web controllers
│   │       ├── model/         # Entity classes
│   │       ├── repository/    # JPA repositories
│   │       ├── service/       # Business logic
│   │       └── RestaurantApplication.java
│   └── resources/
│       ├── static/           # Static resources (CSS, JS)
│       └── templates/        # Thymeleaf templates
```

## Key Components

### Models
- `RestaurantTable`: Represents dining tables with status and capacity
- `Order`: Manages customer orders with items and status
- `MenuItem`: Defines menu items with prices and descriptions
- `OrderItem`: Links orders with menu items and quantities
- `Reservation`: Handles table reservations

### Controllers
- `DashboardController`: Handles dashboard view and statistics
- `OrderController`: Manages order creation and updates
- `TableController`: Handles table management
- `MenuController`: Manages menu items
- `ReservationController`: Handles reservations

### Services
- `OrderService`: Business logic for order management
- `TableService`: Table status and availability management
- `MenuService`: Menu item operations
- `ReservationService`: Reservation management

## Usage Guide

### Creating an Order
1. Navigate to Orders > New Order
2. Select menu items by clicking on them
3. Enter customer details
4. Select a table
5. Click "Place Order"

### Managing Tables
1. Go to Tables
2. View table status (available/occupied)
3. Update table status as needed

### Managing Menu
1. Navigate to Menu
2. Add new items with name, description, and price
3. Edit or remove existing items

### Managing Inventory
1. Go to Inventory
2. View current stock levels
3. Update stock quantities
4. Monitor low stock items

### Managing Reservations
1. Navigate to Reservations
2. Create new reservations
3. View upcoming reservations
4. Update or cancel reservations

## Development

### Adding New Features
1. Create new entity classes in `model` package
2. Add repository interfaces in `repository` package
3. Implement service layer in `service` package
4. Create controllers in `controller` package
5. Add Thymeleaf templates in `templates` directory

### Testing
Run tests with:
```bash
mvn test
```

## Contributing
1. Fork the repository
2. Create a feature branch
3. Commit your changes
4. Push to the branch
5. Create a Pull Request

## License
This project is licensed under the MIT License - see the LICENSE file for details.

## Support
For support, please open an issue in the repository or contact the maintainers. 