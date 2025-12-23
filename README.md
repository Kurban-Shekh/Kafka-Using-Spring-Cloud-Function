# Spring Cloud Function

## 1. What is Spring Cloud Function?

Spring Cloud Function (SCF) is a Spring-based framework that allows you to write business logic as functions (Java Function, Consumer, Supplier) and run them independently of the transport mechanism (HTTP, Kafka, RabbitMQ, AWS Lambda, etc.).

In simple terms:
- You write pure business logic
- Spring Cloud Function handles how the function is triggered

Your code remains the same whether it is triggered by:
- REST API
- Kafka topic
- Message queue
- Serverless platform (AWS Lambda, Azure Functions)

---
## 2. Problems Spring Cloud Function Solves
### Traditional Spring Boot
```java
@RestController
public class OrderController {
    @PostMapping("/orders")
    public Order create(@RequestBody Order order) { ... }
}
```
Problems:
- Business logic is tightly coupled to HTTP
- Hard to reuse logic for Kafka, batch jobs, or serverless

### Spring Cloud Function
```java
Function<Order, Order>
```
problems solved:
- Transport-agnostic
- Same logic reused everywhere

---
## Spring Cloud Function with Kafka

### Without Spring Cloud Function

Without spring cloud function you need to define the following:
- Kafka listeners
- Producer templates
- Serialization logic
- Error handling

### With Spring Cloud Function

With spring cloud function you need to:
- Just define a function
- Binding handles Kafka integration

---
## Kafka with Spring Cloud Function

### Before working with kafka understand the following code:

<strong> Function Types: </strong>

| Interface        | Purpose                        |
| ---------------- | ------------------------------ |
| `Function<T, R>` | Takes input and returns output |
| `Consumer<T>`    | Takes input, no output         |
| `Supplier<T>`    | Produces output, no input      |

<strong> Function Bean Declaration </strong>
```java
@Bean
public Function<String, String> upperCase() {
    return value -> value.toUpperCase();
}
```
Spring manages it as a bean and binds it to input/output automatically.

---
### Kafka publisher using Spring Cloud Function
<strong> kafka publisher code </strong>
```java
@Component
public class OrderPublisher {

    @Bean
    public Supplier<String> orderSupplier() {
        return () -> "Order Created at " + System.currentTimeMillis();
    }
}
```
This function:
- Produces messages
- Publishes them to Kafka topic

<strong> application.yaml file for publisher</strong>
```yaml
spring:
  cloud:
    function:
      definition: orderSupplier
    stream:
      bindings:
        orderSupplier-out-0:
          destination: order-topic
      kafka:
        binder:
          brokers: localhost:9092

```
Explanation
- orderSupplier-out-0 → output binding
- destination → Kafka topic name

---
### Kafka listener using Spring Cloud Function

<strong> Kafka consumer code </strong>
```java
@Component
public class OrderConsumer {

    @Bean
    public Consumer<String> orderConsumer() {
        return message -> {
            System.out.println("Consumed message: " + message);
        };
    }
}
```
This function:
- Automatically listens to Kafka
- No @KafkaListener required

<strong> application.yaml file for listener </strong>
```yaml
spring:
  cloud:
    function:
      definition: orderConsumer
    stream:
      bindings:
        orderConsumer-in-0:
          destination: order-topic
          group: order-group
      kafka:
        binder:
          brokers: localhost:9092
```
---
### Kafka Consumer + Producer (Chained Function)

<strong> Function code </strong>
```java
@Bean
public Function<String, String> processOrder() {
    return order -> "Processed: " + order;
}
```
<strong> application.yaml file for consumer + publisher </strong>

```yaml
spring:
  cloud:
    function:
      definition: processOrder
    stream:
      bindings:
        processOrder-in-0:
          destination: order-topic
          group: order-group
        processOrder-out-0:
          destination: processed-order-topic
      kafka:
        binder:
          brokers: localhost:9092
```
