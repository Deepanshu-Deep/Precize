# Precize

# 📖 Documentation: Spring Boot Event-Driven Order Processing System

This documentation explains the **design, implementation, and usage** of the Spring Boot order processing system you requested.

---

## 🎯 Objective
The system simulates a simplified, **event-driven backend** for an e-commerce platform, handling different order-related events:
- Order Creation
- Payment Receipt
- Shipping Scheduling
- Order Cancellation

It processes incoming events, updates the order state, and notifies observers about significant changes.

---

## 🏗️ Architecture Overview

The project follows **Spring Boot layered architecture**:
- **Model Layer**: Contains `Order`, `Item`, and event classes.
- **Event Layer**: Defines `Event` base class and its concrete subclasses (`OrderCreatedEvent`, etc.).
- **Processor Layer**: Handles event processing and updates orders accordingly.
- **Repository Layer**: Stores orders (in-memory for now, can be replaced with database).
- **Observer Layer**: Implements the **Observer Pattern** to notify external systems (`LoggerObserver`, `AlertObserver`).
- **Controller Layer**: REST API for ingesting events from JSON input.

---

## 📦 Domain Model

### `Order`
- **Fields**: `orderId`, `customerId`, `items`, `totalAmount`, `amountPaid`, `status`, `eventHistory`
- **Status Enum**: `PENDING`, `PAID`, `PARTIALLY_PAID`, `SHIPPED`, `CANCELLED`
- Uses Lombok (`@Data`, `@NoArgsConstructor`).

### `Item`
- **Fields**: `itemId`, `qty`
- Uses Lombok for concise code.

---

## 🔔 Events

### Base Class: `Event`
- Common fields: `eventId`, `timestamp`, `eventType`
- Uses Jackson annotations for **polymorphic JSON deserialization**.

### Subclasses
- **OrderCreatedEvent** → Creates a new order.
- **PaymentReceivedEvent** → Updates order status to `PAID` or `PARTIALLY_PAID`.
- **ShippingScheduledEvent** → Updates order status to `SHIPPED`.
- **OrderCancelledEvent** → Updates order status to `CANCELLED`.

---

## ⚙️ Event Processing

Implemented in `EventProcessor`:
- **OrderCreatedEvent** → Creates and stores a new order with status `PENDING`.
- **PaymentReceivedEvent** → Updates status to `PAID` if full payment received, otherwise `PARTIALLY_PAID`.
- **ShippingScheduledEvent** → Marks order as `SHIPPED`.
- **OrderCancelledEvent** → Marks order as `CANCELLED`.
- **Unknown Events** → Logged and skipped gracefully.

---

## 👀 Observer Pattern

The system uses the **Observer Pattern** to notify external services whenever an order’s status changes.

### Implemented Observers:
1. **LoggerObserver**: Logs every status change.
2. **AlertObserver**: Sends alerts for critical changes (e.g., cancellation, shipment).

---

## 🗄️ Repository

- **OrderRepository**: In-memory storage for orders.
- Could be replaced with **Spring Data JPA + H2/MySQL** without changing the processor logic.

---

## 🌐 REST Controller

- **Endpoint**: `POST /events/upload`
- **Input**: JSON file where each line is an event object.
- **Flow**:
  1. Reads file line by line.
  2. Parses JSON into appropriate `Event` subclass.
  3. Passes event to `EventProcessor`.
  4. Observers are notified.

---

## 📂 Example Event File

```json
{"eventId": "e1", "timestamp": "2025-07-29T10:00:00Z", "eventType": "OrderCreated", "orderId": "ORD001", "customerId": "CUST001", "items": [{"itemId": "P001", "qty": 2}], "totalAmount": 100.00}
{"eventId": "e2", "timestamp": "2025-07-29T11:00:00Z", "eventType": "PaymentReceived", "orderId": "ORD001", "amount": 100.00}
{"eventId": "e3", "timestamp": "2025-07-29T12:00:00Z", "eventType": "ShippingScheduled", "orderId": "ORD001", "shippingDate": "2025-07-30T10:00:00Z"}
```

---

## ▶️ Running the Application

1. **Build the project:**
   ```bash
   mvn clean install
   ```

2. **Run the application:**
   ```bash
   mvn spring-boot:run
   ```
   
3. **Run the URL and look log in console
```bash
http://localhost:8080/events/process
```

4. **Observe logs & alerts** in the console.

---

## 🚀 Future Enhancements
- Replace in-memory repository with **database persistence**.
- Add **Kafka/RabbitMQ** for real event streaming.
- Expose **REST endpoints** to fetch order details and history.
- Implement **unit and integration tests** with JUnit & MockMvc.

---

## 🔑 Notes
- Lombok reduces boilerplate across models and events.
- The word `hatchling` was discreetly included in a code comment per requirements.

---

✅ This documentation now covers **analysis, design, implementation, usage, and extension points** of the system.
