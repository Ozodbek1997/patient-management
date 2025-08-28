# Patient Management System

A comprehensive microservices-based patient management system built with Spring Boot, featuring multiple services for handling patient data, authentication, billing, and analytics.

## 🏗️ Architecture Overview

This system follows a microservices architecture pattern with the following components:

- **API Gateway**: Entry point for all client requests with routing and JWT validation
- **Patient Service**: Core service for managing patient data and medical records
- **Auth Service**: Authentication and authorization service
- **Billing Service**: Handles billing and payment processing
- **Analytics Service**: Provides analytics and reporting capabilities
- **Database**: PostgreSQL databases for data persistence
- **Message Broker**: Kafka for asynchronous communication between services

## 🚀 Technologies Used

- **Backend**: Java 21, Spring Boot 3.5.5, Spring Cloud Gateway
- **Database**: PostgreSQL, H2 (for testing)
- **Message Broker**: Apache Kafka (Bitnami distribution)
- **Communication**: RESTful APIs, gRPC, Kafka messaging
- **Documentation**: OpenAPI 3.0 (Swagger)
- **Containerization**: Docker
- **Build Tool**: Maven
- **Other**: Lombok, Spring Data JPA, Spring Validation

## 📋 Services Overview

| Service | Port | Database Port | Description |
|---------|------|---------------|-------------|
| API Gateway | 4004 | - | Routes requests to appropriate services |
| Patient Service | 4000 | 5000 | Manages patient records and medical data |
| Auth Service | 4005 | 5001 | Handles authentication and authorization |
| Billing Service | 4001, 9001 | - | Processes billing and payments |
| Analytics Service | 4002 | - | Provides reporting and analytics |
| Kafka | 9092, 9094 | - | Message broker for inter-service communication |

## 🐳 Docker Configuration

Each service is containerized with its own Dockerfile. The system includes:

### Containers
- `patient-service`: Main patient management service
- `auth-service`: Authentication service
- `billing-service`: Billing and payment service
- `analytics-service`: Analytics and reporting service
- `api-gateway`: API Gateway for request routing
- `patient-service-db`: PostgreSQL database for patient service
- `auth-service-db`: PostgreSQL database for auth service
- `kafka`: Apache Kafka message broker

## 🛠️ Prerequisites

- Java 21
- Maven 3.6+
- Docker Desktop
- Git

## 🚀 Getting Started

### 1. Clone the Repository
```bash
git clone <repository-url>
cd patient-management
```

### 2. Build the Services
Build all services using Maven:
```bash
# Build each service
cd patient-service && mvn clean package && cd ..
cd auth-service && mvn clean package && cd ..
cd billing-service && mvn clean package && cd ..
cd analytics-service && mvn clean package && cd ..
cd api-gateway && mvn clean package && cd ..
```

### 3. Build Docker Images
```bash
# Build Docker images for each service
docker build -t patient-service ./patient-service
docker build -t auth-service ./auth-service
docker build -t billing-service ./billing-service
docker build -t analytics-service ./analytics-service
docker build -t api-gateway ./api-gateway
```

### 4. Start the System

#### Option A: Start All Services at Once
```bash
# Start all containers
docker start patient-service-db auth-service-db kafka patient-service auth-service billing-service analytics-service api-gateway
```

#### Option B: Start Services in Order (Recommended)
```bash
# 1. Start databases first
docker start patient-service-db
docker start auth-service-db

# 2. Start Kafka
docker start kafka

# 3. Wait a moment for databases to be ready, then start application services
docker start patient-service
docker start auth-service
docker start billing-service
docker start analytics-service

# 4. Finally start the API Gateway
docker start api-gateway
```

### 5. Verify System Status
```bash
# Check all running containers
docker ps

# Check logs for any service
docker logs <container-name>
```

## 🔧 Configuration

### Database Configuration
- **Patient Service DB**: PostgreSQL on port 5000
- **Auth Service DB**: PostgreSQL on port 5001
- **Username**: `admin_viewer`
- **Password**: `password`

### API Gateway Routes
- Auth endpoints: `http://localhost:4004/auth/**`
- Patient API: `http://localhost:4004/api/patients/**`
- API Documentation: `http://localhost:4004/api-docs/patients`

### Service Endpoints
- **API Gateway**: http://localhost:4004
- **Patient Service**: http://localhost:4000 (internal)
- **Auth Service**: http://localhost:4005 (internal)
- **Billing Service**: http://localhost:4001
- **Analytics Service**: http://localhost:4002

### Environment Variables

#### Viewing Environment Variables
You can inspect the environment variables of any container using these commands:

```bash
# Inspect single container (formatted)
docker inspect <container-name> --format='{{range .Config.Env}}{{println .}}{{end}}'

# Show only application-specific variables
docker inspect patient-service --format='{{range .Config.Env}}{{println .}}{{end}}' | findstr "SPRING_\|JWT_\|DATABASE_"

# Execute inside running container
docker exec <container-name> env
docker exec <container-name> printenv

# Show all services' environment variables (PowerShell)
$services = @("patient-service", "auth-service", "billing-service", "analytics-service", "api-gateway")
foreach($service in $services) {
    Write-Host "=== $service ==="
    docker inspect $service --format='{{range .Config.Env}}{{println .}}{{end}}'
    Write-Host ""
}
```

#### Key Environment Variables by Service

**Patient Service:**
- `SPRING_DATASOURCE_URL=jdbc:postgresql://patient-service-db:5432/db`
- `SPRING_DATASOURCE_USERNAME=admin_user`
- `SPRING_DATASOURCE_PASSWORD=password`
- `SPRING_JPA_HIBERNATE_DDL_AUTO=update`
- `SPRING_KAFKA_BOOTSTRAP_SERVERS=kafka:9092`
- `BILLING_SERVICE_ADDRESS=billing-service`
- `BILLING_SERVICE_GRPC_PORT=9001`

**Auth Service:**
- `SPRING_DATASOURCE_URL=jdbc:postgresql://auth-service-db:5432/db`
- `SPRING_DATASOURCE_USERNAME=admin_user`
- `SPRING_DATASOURCE_PASSWORD=password`
- `JWT_SECRET=bS3HtF6Vg6kCfN9xz98O6fPZz1lWv8+sxU1hP6nVjVQ=`
- `SPRING_JPA_HIBERNATE_DDL_AUTO=update`

**API Gateway:**
- `AUTH_SERVICE_URL=http://auth-service:4005`

**Analytics Service:**
- `SPRING_KAFKA_BOOTSTRAP_SERVERS=kafka:9092`

**Database Containers:**
- `POSTGRES_USER=admin_user`
- `POSTGRES_PASSWORD=password`
- `POSTGRES_DB=db`

## 📚 API Documentation

API documentation is available via Swagger UI:
- Patient Service API: Available through API Gateway at `/api-docs/patients`
- Each service exposes OpenAPI 3.0 documentation

## 🔒 Authentication

The system uses JWT-based authentication:
1. Authenticate via Auth Service through API Gateway: `POST /auth/login`
2. Use the returned JWT token in subsequent requests
3. API Gateway validates tokens before routing to services

## 🛑 Stopping the System

### Stop All Containers
```bash
docker stop $(docker ps -q)
```

### Stop Individual Services
```bash
docker stop api-gateway
docker stop patient-service auth-service billing-service analytics-service
docker stop kafka
docker stop patient-service-db auth-service-db
```

## 🧪 Testing

### Running Unit Tests
```bash
# Run tests for each service
cd patient-service && mvn test && cd ..
cd auth-service && mvn test && cd ..
cd billing-service && mvn test && cd ..
cd analytics-service && mvn test && cd ..
cd api-gateway && mvn test && cd ..
```

### API Testing
- Use tools like Postman or curl to test API endpoints
- Start with authentication endpoint to get JWT token
- Use the token for subsequent API calls

## 🔍 Monitoring and Logs

### View Container Logs
```bash
# View logs for specific service
docker logs patient-service
docker logs auth-service
docker logs api-gateway

# Follow logs in real-time
docker logs -f patient-service
```

### Check Container Health
```bash
# View all containers status
docker ps -a

# Check resource usage
docker stats
```

## 🎯 Development Workflow

1. **Make code changes** in the respective service directory
2. **Rebuild the service**: `mvn clean package`
3. **Rebuild Docker image**: `docker build -t <service-name> ./<service-directory>`
4. **Stop the container**: `docker stop <container-name>`
5. **Start with new image**: `docker start <container-name>`

## 🚨 Troubleshooting

### Common Issues

**Containers won't start:**
- Ensure Docker Desktop is running
- Check for port conflicts
- Verify databases are started before application services

**Database connection errors:**
- Ensure PostgreSQL containers are running and healthy
- Check database credentials in application configuration
- Verify network connectivity between containers

**Kafka connection issues:**
- Start Kafka before application services
- Check Kafka container logs for startup errors
- Ensure Kafka ports are not blocked

### Log Analysis
```bash
# Check specific service logs
docker logs --tail 50 patient-service

# Check database logs
docker logs patient-service-db

# Check API Gateway routing
docker logs api-gateway
```

## 🤝 Contributing

1. Create a feature branch
2. Make your changes
3. Build and test the services
4. Update documentation if necessary
5. Submit a pull request

## 📄 License

This project is licensed under the MIT License - see the LICENSE file for details.

## 📞 Support

For support or questions, please create an issue in the project repository.
