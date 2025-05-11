# Shopping Cart Microservices

This project implements a shopping cart system using a microservices architecture. The system consists of two main microservices that work together to provide a complete shopping experience.

## Microservices Overview

### Order Service

The Order microservice is responsible for:

- Managing user orders
- Order processing and lifecycle
- Order status tracking
- Handling order-related operations

### Inventory Service

The Inventory microservice handles:

- Product management
- Inventory tracking
- Stock level management
- Product availability status

## Project Structure

```
shoppingcart/
├── order/                    # Order Microservice
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/        # Order service source code
│   │   │   └── resources/   # Configuration files
│   │   └── test/            # Test files
│   ├── build.gradle         # Gradle build configuration
│   └── settings.gradle      # Gradle settings
│
├── inventory/               # Inventory Microservice
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/       # Inventory service source code
│   │   │   └── resources/  # Configuration files
│   │   └── test/           # Test files
│   ├── build.gradle        # Gradle build configuration
│   └── settings.gradle     # Gradle settings
│
├── read-kafka.bat          # Kafka reading utility script
└── start-kafka-docker.bat  # Kafka Docker startup script
```

## Prerequisites

- Java 21
- MySQL 8
- Gradle
- Docker (for Kafka)

## Getting Started

### 1. Start Kafka

First, start the Kafka infrastructure:

```powershell
.\start-kafka-docker.bat
```

### 2. Start the Inventory Service

Navigate to the inventory service directory and run:

```powershell
cd inventory
.\gradlew bootRun
```

### 3. Start the Order Service

Open a new terminal, navigate to the order service directory and run:

```powershell
cd order
.\gradlew bootRun
```

## Monitoring Kafka Messages

To monitor Kafka messages, use:

```powershell
.\read-kafka.bat
```

## Development

### Building the Services

To build either service:

```powershell
.\gradlew build
```

### Running Tests

To run tests for either service:

```powershell
.\gradlew test
```

## Communication

The services communicate through:

1. REST APIs for synchronous communication
2. Kafka events for asynchronous communication

## API Documentation

### Order Service APIs (Port: 8080)

#### Order Management

- `POST /order-service/orders/v1` - Create a new order

  - Request: Order object with order items
  - Response: Order ID and status

- `GET /order-service/orders/v1/{orderId}/lock` - Lock an order for processing

  - Query Parameters:
    - `transactionId` (required): Transaction ID for tracking
  - Response: 200 OK on success

- `GET /order-service/orders/v1/{orderId}/unlock` - Unlock an order and update status
  - Query Parameters:
    - `transactionId` (required): Transaction ID for tracking
    - `status` (required): New status for the order
  - Response: 200 OK on success

#### Order Events (Kafka Topics)

- `order.new` - Published when a new order is created

### Inventory Service APIs (Port: 8081)

#### Order Items Management

- `POST /inventory-service/order-items/v1/{orderId}` - Process order items
  - Request: List of order items
  - Response: 201 Created on success
