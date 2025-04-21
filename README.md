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

### Option 1: Local Development
- Java 17 or higher
- Maven 3.6 or higher
- MariaDB 10.6 or higher
- Git (optional, for version control)

### Option 2: Docker Development
- Docker
- Docker Compose

## Quick Start with Docker

1. Clone the repository:
```bash
git clone <repository-url>
cd restaurant-management-system
```

2. Build and start the containers:
```bash
docker-compose up --build
```

3. Access the application:
- Web interface: http://localhost:8080
- Database: localhost:3306

4. Stop the containers:
```bash
docker-compose down
```

## Setting up Development Environment on Linux

### Installing Docker and Docker Compose

1. Install Docker:
```bash
sudo apt update
sudo apt install docker.io
sudo systemctl start docker
sudo systemctl enable docker
```

2. Install Docker Compose:
```bash
sudo curl -L "https://github.com/docker/compose/releases/download/v2.20.0/docker-compose-$(uname -s)-$(uname -m)" -o /usr/local/bin/docker-compose
sudo chmod +x /usr/local/bin/docker-compose
```

3. Verify installations:
```bash
docker --version
docker-compose --version
```

4. Add your user to the docker group:
```bash
sudo usermod -aG docker $USER
```

### Local Development Setup (Alternative to Docker)

### Installing Java 17

1. Update package list:
```bash
sudo apt update
```

2. Install Java 17:
```bash
sudo apt install openjdk-17-jdk
```

3. Verify installation:
```bash
java -version
```

4. Set JAVA_HOME (add to ~/.bashrc or ~/.zshrc):
```bash
echo 'export JAVA_HOME=/usr/lib/jvm/java-17-openjdk-amd64' >> ~/.bashrc
echo 'export PATH=$JAVA_HOME/bin:$PATH' >> ~/.bashrc
source ~/.bashrc
```

### Installing Maven

1. Download Maven (replace version number with latest stable):
```bash
wget https://dlcdn.apache.org/maven/maven-3/3.9.6/binaries/apache-maven-3.9.6-bin.tar.gz
```

2. Extract Maven:
```bash
sudo tar -xvf apache-maven-3.9.6-bin.tar.gz -C /opt
```

3. Create symbolic link:
```bash
sudo ln -s /opt/apache-maven-3.9.6 /opt/maven
```

4. Set Maven environment variables (add to ~/.bashrc or ~/.zshrc):
```bash
echo 'export M2_HOME=/opt/maven' >> ~/.bashrc
echo 'export MAVEN_HOME=/opt/maven' >> ~/.bashrc
echo 'export PATH=${M2_HOME}/bin:${PATH}' >> ~/.bashrc
source ~/.bashrc
```

5. Verify installation:
```bash
mvn -version
```

### Installing MariaDB

1. Install MariaDB:
```bash
sudo apt install mariadb-server
```

2. Start MariaDB service:
```bash
sudo systemctl start mariadb
sudo systemctl enable mariadb
```

3. Secure MariaDB installation:
```bash
sudo mysql_secure_installation
```

4. Create database and user:
```sql
sudo mysql -u root -p
CREATE DATABASE restaurant_db;
CREATE USER 'restaurant_user'@'localhost' IDENTIFIED BY 'your_password';
GRANT ALL PRIVILEGES ON restaurant_db.* TO 'restaurant_user'@'localhost';
FLUSH PRIVILEGES;
EXIT;
```

## Installation

1. Clone the repository:
```bash
git clone <repository-url>
cd restaurant-management-system
```

2. Update database configuration in `src/main/resources/application.properties`:
```properties
spring.datasource.url=jdbc:mariadb://localhost:3306/restaurant_db?createDatabaseIfNotExist=true
spring.datasource.username=restaurant_user
spring.datasource.password=your_password
```

3. Build the project:
```bash
mvn clean install
```

4. Run the application:
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

### Running with Docker

1. Build and start the application:
```bash
docker-compose up --build
```

2. View logs:
```bash
docker-compose logs -f
```

3. Stop the application:
```bash
docker-compose down
```

4. Rebuild and restart:
```bash
docker-compose up --build --force-recreate
```

### Database Management

1. Access MariaDB container:
```bash
docker-compose exec db mysql -u restaurant_user -p
```

2. Backup database:
```bash
docker-compose exec db mysqldump -u restaurant_user -p restaurant_db > backup.sql
```

3. Restore database:
```bash
docker-compose exec -T db mysql -u restaurant_user -p restaurant_db < backup.sql
```

### Adding New Features
1. Create new entity classes in `