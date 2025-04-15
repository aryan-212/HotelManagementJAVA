# Restaurant Management System

A comprehensive web-based restaurant management system built with Spring Boot and Thymeleaf. The system helps restaurant staff manage tables, orders, menu items, inventory, and reservations efficiently.

## Features

- **Dashboard**: View key metrics and recent activities
- **Table Management**: Track table status and capacity
- **Order Processing**: Create and manage customer orders
- **Menu Management**: Maintain menu items and categories
- **Inventory Tracking**: Monitor stock levels and manage inventory
- **Reservation System**: Handle table reservations and availability

## Prerequisites

- Java 17 or higher
- Maven 3.6 or higher
- MySQL 8.0 or higher
- Git (optional, for cloning the repository)

## Installation

1. Clone the repository (if using Git):
   ```bash
   git clone <repository-url>
   cd restaurant-management-system
   ```

2. Configure the database:
   - Create a new MySQL database named `restaurant_db`
   - Update the database configuration in `src/main/resources/application.properties`:
     ```properties
     spring.datasource.url=jdbc:mysql://localhost:3306/restaurant_db
     spring.datasource.username=your_username
     spring.datasource.password=your_password
     ```

3. Configure environment variables:
   - Create a `.env` file in the root directory of the project
   - Add the following environment variables:
     ```properties
     # Database Configuration
     DB_URL=jdbc:mysql://localhost:3306/restaurant_db
     DB_USERNAME=your_username
     DB_PASSWORD=your_password

     # Server Configuration
     SERVER_PORT=8080
     SPRING_PROFILES_ACTIVE=dev

     # JWT Configuration (if using authentication)
     JWT_SECRET=your_jwt_secret_key
     JWT_EXPIRATION=86400000

     # Email Configuration (if using email notifications)
     SMTP_HOST=smtp.gmail.com
     SMTP_PORT=587
     SMTP_USERNAME=your_email@gmail.com
     SMTP_PASSWORD=your_app_specific_password
     ```
   - Note: The `.env` file is ignored by git for security reasons. Make sure to keep your sensitive information secure and never commit this file to version control.

4. Build the project:
   ```bash
   mvn clean install
   ```

## Running the Application

1. Start the application:
   ```bash
   mvn spring-boot:run
   ```

2. Access the application:
   - Open your web browser
   - Navigate to `http://localhost:8080`
   - The dashboard will be displayed as the home page

## Project Structure

```
src/
├── main/
│   ├── java/
│   │   └── com/
│   │       └── restaurant/
│   │           ├── controller/    # REST controllers
│   │           ├── model/         # Entity classes
│   │           ├── repository/    # Data access layer
│   │           ├── service/       # Business logic
│   │           └── RestaurantApplication.java
│   └── resources/
│       ├── static/               # Static resources (CSS, JS)
│       ├── templates/            # Thymeleaf templates
│       └── application.properties # Application configuration
```

## Usage

1. **Dashboard**
   - View pending orders, available tables, low stock items, and today's reservations
   - Access quick actions for common tasks

2. **Tables**
   - View all tables and their status
   - Add new tables
   - Update table status and capacity

3. **Orders**
   - Create new orders
   - Track order status
   - View order history

4. **Menu**
   - Manage menu items
   - Update prices and availability
   - Categorize items

5. **Inventory**
   - Track stock levels
   - Add new inventory items
   - Set low stock alerts

6. **Reservations**
   - Create new reservations
   - Check table availability
   - Manage reservation status

## Troubleshooting

1. **Port Already in Use**
   - If port 8080 is already in use, you can either:
     - Kill the existing process: `pkill java`
     - Change the port in `application.properties`: `server.port=8081`

2. **Database Connection Issues**
   - Verify MySQL is running
   - Check database credentials in `application.properties`
   - Ensure the database exists

3. **Build Failures**
   - Clean and rebuild: `mvn clean install`
   - Check Java version: `java -version`
   - Verify Maven installation: `mvn -version`

## Contributing

1. Fork the repository
2. Create a feature branch
3. Commit your changes
4. Push to the branch
5. Create a Pull Request

## License

This project is licensed under the MIT License - see the LICENSE file for details. 