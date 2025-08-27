# Live Score Task

A **Java Spring Boot microservice** that tracks live sports events by calling external REST APIs every 10s and publishing processed data to **Kafka**.

---

## NOTE
Modify kafka bootstrap url in application.properties file.

## 🚀 Features
- REST API to manage event status (live/not live)
- Scheduled polling of external APIs (10s)
- Kafka integration for message publishing
- Retry & error handling with Spring Retry
- Real-time logging & monitoring

---

## 🛠 Tech Stack
- Java 11, Spring Boot 3.2.0
- Spring Kafka, Spring Retry, Lombok
- Maven 3.6+, Kafka 3.6.0

---

## ⚙️ Prerequisites
- Java 11+
- Maven 3.6+
- Kafka 3.6.0 (Linux/Ubuntu preferred)

---

## 📦 Setup & Installation
```
# Install Java & Maven
sudo apt update && sudo apt install openjdk-17-jdk maven

# Install Kafka
sudo mkdir -p /opt/kafka && cd /opt/kafka
sudo wget https://downloads.apache.org/kafka/3.6.0/kafka_2.13-3.6.0.tgz
sudo tar -xzf kafka_2.13-3.6.0.tgz && ln -s kafka_2.13-3.6.0 kafka
echo 'export KAFKA_HOME=/opt/kafka/kafka' >> ~/.bashrc
echo 'export PATH=$PATH:$KAFKA_HOME/bin' >> ~/.bashrc && source ~/.bashrc

# Start Kafka & Zookeeper
cd /opt/kafka/kafka
bin/zookeeper-server-start.sh config/zookeeper.properties > zookeeper.log 2>&1 &
sleep 10 && bin/kafka-server-start.sh config/server.properties > kafka.log 2>&1 &
bin/kafka-topics.sh --create --topic live-scores --bootstrap-server localhost:9092 --partitions 1 --replication-factor 1

```
## ▶️ Run the Application

`git clone <your-repo-url>`

`cd sports-events-service`

`mvn clean package`

`mvn spring-boot:run`
# or
java -jar target/<JAR_NAME>

## 📡 API Usage
**Update Event Status**

`curl --location 'http://localhost:8080/events/status' \
--header 'Content-Type: application/json' \
--data '{"eventId" : 2347 , "live": 1}'`

**Monitor Kafka Messages**

`/opt/kafka/kafka/bin/kafka-console-consumer.sh \
--topic live-scores --bootstrap-server localhost:9092 --from-beginning
`
## 🧪 Running Tests

**Unit tests**

`mvn test`

`mvn test -Dtest=EventServiceTest`

`mvn test jacoco:report`

**Integration tests**

mvn verify

## 📐 Design Highlights
**Architecture**: Layered, scheduler (10s polling), retry w/ exponential backoff, Kafka producer (observer pattern).

**Concurrency**: Thread-safe collections, async Kafka publishing, no explicit locks.

**Error Handling:** Retries (3 attempts, 1s delay), circuit breaker-like resilience, structured logging.

**API**: RESTful, bean validation, idempotent updates.

**Kafka**: JSON serialization, retry on failures, async delivery.

**Config**: Externalized in application.properties, environment-ready, sensible defaults.

**Performance**: Connection pooling, memory-efficient, horizontally scalable.

## 🤖 AI-Assisted Development

1. Generated project scaffolding, Kafka config.

2. Verified manually with improvements in error handling, JSON serialization, and concurrency.
