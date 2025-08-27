A Java Spring Boot microservice that monitors live sports events by periodically calling external REST APIs and publishing processed data to Kafka.

Features
REST API for managing event status (live/not live)

Scheduled external API calls every 10 seconds for active events

Kafka integration for message publishing

Comprehensive error handling with retry mechanisms

Real-time logging and monitoring

Technology Stack
Java 17 with Spring Boot 3.2.0

Spring Kafka for message brokering

Spring Retry for resilient external calls

Lombok for reduced boilerplate code

Maven for dependency management

Kafka 3.6.0 for message queueing

Prerequisites
Java 17 or later

Maven 3.6+

Kafka 3.6.0

Ubuntu/Linux environment

Setup & Installation
1. Install Java and Maven
bash
sudo apt update
sudo apt install openjdk-17-jdk maven
2. Install and Configure Kafka
bash
# Create installation directory
sudo mkdir -p /opt/kafka
cd /opt/kafka

# Download and extract Kafka
sudo wget https://downloads.apache.org/kafka/3.6.0/kafka_2.13-3.6.0.tgz
sudo tar -xzf kafka_2.13-3.6.0.tgz
sudo ln -s kafka_2.13-3.6.0 kafka

# Set ownership and permissions
sudo chown -R $USER:$USER /opt/kafka/

# Add to PATH
echo 'export KAFKA_HOME=/opt/kafka/kafka' >> ~/.bashrc
echo 'export PATH=$PATH:$KAFKA_HOME/bin' >> ~/.bashrc
source ~/.bashrc

# Create logs directory
mkdir -p /opt/kafka/kafka/logs
3. Start Kafka Services
bash
# Start Zookeeper (terminal 1)
cd /opt/kafka/kafka
bin/zookeeper-server-start.sh config/zookeeper.properties > zookeeper.log 2>&1 &

# Wait 10 seconds, then start Kafka (terminal 2)
sleep 10
bin/kafka-server-start.sh config/server.properties > kafka.log 2>&1 &

# Create required topic
bin/kafka-topics.sh --create --topic live-scores --bootstrap-server localhost:9092 --partitions 1 --replication-factor 1
4. Build and Run the Application
bash
# Clone the repository
git clone <your-repo-url>
cd sports-events-service

# Build the project
mvn clean package

# Run the application
mvn spring-boot:run

# Or run the compiled jar
java -jar target/sports-events-service-1.0.0.jar
The application will start on http://localhost:8080

API Usage
Update Event Status
bash
curl -X POST http://localhost:8080/events/status \
  -H "Content-Type: application/json" \
  -d '{"eventId": "1234", "live": true}'
Monitor Kafka Messages
bash
/opt/kafka/kafka/bin/kafka-console-consumer.sh \
  --topic live-scores \
  --bootstrap-server localhost:9092 \
  --from-beginning
Running Tests
Unit Tests
bash
# Run all unit tests
mvn test

# Run specific test class
mvn test -Dtest=EventServiceTest

# Run with coverage report
mvn test jacoco:report
Integration Tests
bash
# Run all tests including integration tests
mvn verify

# Run tests with detailed output
mvn test -Dmaven.test.failure.ignore=false
Test Structure
Unit Tests: Located in src/test/java/ with *Test.java suffix

Integration Tests: Located in src/test/java/ with *IntegrationTest.java suffix

Test Coverage: JaCoCo integration for coverage reports in target/site/jacoco/

Design Decisions
1. Architecture & Patterns
Layered Architecture: Clear separation between controller, service, and data layers

Scheduler Pattern: Fixed-rate polling for live events every 10 seconds

Retry Pattern: Exponential backoff for external API failures

Observer Pattern: Kafka producers observing event changes

2. Concurrency Management
Thread-Safe Collections: CopyOnWriteArraySet for live events tracking

Non-Blocking Operations: Async Kafka message publishing

Synchronized Access: No explicit locking needed for read-heavy operations

3. Error Handling Strategy
Retry Mechanisms: Configurable retries for external API calls (3 attempts with 1s delay)

Circuit Breaker: Implicit circuit breaking through retry limits

Comprehensive Logging: Structured logging for debugging and monitoring

4. API Design
RESTful Principles: Clean, predictable API endpoints

Validation: Bean Validation with custom error messages

Idempotency: Safe for duplicate status update requests

5. Kafka Integration
JSON Serialization: Custom serialization for complex objects

Error Recovery: Retry logic for message publishing failures

Async Processing: Non-blocking message delivery with callbacks

6. Configuration Management
Externalized Configuration: All settings in application.properties

Environment Ready: Easy configuration for different environments

Sensible Defaults: Pre-configured for local development

Performance Considerations
Memory Efficiency: Optimized for read-heavy workload patterns

Connection Management: REST template connection pooling

Batch Potential: Architecture supports future batch processing

Scalability: Stateless design allows horizontal scaling

AI-Assisted Development
Generated Components & Verification
Project Scaffolding

Generated: Initial Spring Boot project structure

Verified: Manual review of dependencies and package organization

Improved: Added specific Kafka, retry, and validation dependencies

Kafka Configuration

Generated: Basic producer factory setup

Verified: Tested with local Kafka instance and message serialization

Improved: Enhanced with JSON serialization, error handling, and custom configuration

Retry Mechanism

Generated: Spring Retry annotation structure

Verified: Tested retry behavior with simulated API failures

Improved: Added custom backoff strategies and exception handling

Scheduled Task Implementation

Generated: Basic @Scheduled method framework

Verified: Validated timing accuracy and concurrency behavior

Improved: Enhanced with thread safety and comprehensive error handling

Verification Process
Code Review: Every AI-generated component underwent manual code review

Unit Testing: Comprehensive test coverage for all functional components

Integration Testing: End-to-end testing with real Kafka infrastructure

Performance Testing: Validation under concurrent load conditions

Enhancements Beyond AI Generation
Production-Ready Error Handling: Beyond basic try-catch to include retry policies and circuit breaking

Comprehensive Logging: Added structured logging for operational monitoring

Configuration Management: Externalized all parameters for environment flexibility

Testing Infrastructure: Built complete test suite beyond initial snippets

Monitoring & Operations
Logging
Application logs: logs/application.log

Kafka logs: /opt/kafka/kafka/logs/server.log

Access logs: Built-in Spring Boot access logging

