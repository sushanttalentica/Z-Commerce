# Docker Setup for Z-Commerce Platform

This guide explains how to run the Z-Commerce platform using Docker, ensuring it works on any system without needing to install Java, Maven, Kafka, Redis, or MySQL locally.

## Prerequisites

- Docker Desktop installed ([Download here](https://www.docker.com/products/docker-desktop/))
- Docker Compose (included with Docker Desktop)
- (Optional) AWS credentials for S3 invoice storage

## Quick Start Options

### Option 1: Development Mode (Simplest - No Dependencies)

**Best for:** Quick testing, API development, no Kafka/Redis needed

```bash
docker-compose -f docker-compose.dev.yml up --build
```

**What's included:**
- ✅ Z-Commerce application with H2 in-memory database
- ✅ All APIs available at `http://localhost:8080/zcommerce`
- ❌ No Kafka (events won't be published)
- ❌ No Redis (no caching)

**Access:**
- Application: http://localhost:8080/zcommerce
- Health Check: http://localhost:8080/zcommerce/actuator/health
- H2 Console: http://localhost:8080/h2-console (JDBC URL: `jdbc:h2:mem:testdb`)

---

### Option 2: Local Development Mode (Recommended)

**Best for:** Full-stack development with all services

```bash
docker-compose -f docker-compose.local.yml up --build
```

**What's included:**
- ✅ Z-Commerce application with H2 in-memory database
- ✅ Kafka + Zookeeper (event streaming)
- ✅ Redis (caching)
- ✅ Kafka UI for monitoring topics and messages

**Access:**
- Application: http://localhost:8080/zcommerce
- Health Check: http://localhost:8080/zcommerce/actuator/health
- Kafka UI: http://localhost:8090
- Redis: localhost:6380 (external port)

**Features:**
- Remote debugging enabled on port 5005 (for IntelliJ IDEA)
- All event streaming works (order events, payment events, etc.)
- Caching enabled for better performance
- Kafka UI for visual inspection of topics and messages

---

### Option 3: Production-like Mode (Full Stack)

**Best for:** Production simulation, integration testing

```bash
docker-compose up --build
```

**What's included:**
- ✅ Z-Commerce application
- ✅ MySQL database (persistent storage)
- ✅ Kafka + Zookeeper (event streaming)
- ✅ Redis (caching)
- ✅ Kafka UI (topic monitoring)

**Access:**
- Application: http://localhost:8080/zcommerce
- Health Check: http://localhost:8080/zcommerce/actuator/health
- MySQL: localhost:3306 (database: `zcommerce_db`, user: `root`, password: `password`)
- Kafka UI: http://localhost:8090
- Redis: localhost:6379

---

## Environment Variables

Create a `.env` file in the project root with your AWS credentials (optional):

```env
# AWS S3 for Invoice Storage (optional)
AWS_ACCESS_KEY_ID=your-access-key-id
AWS_SECRET_ACCESS_KEY=your-secret-access-key
AWS_REGION=us-east-1
AWS_S3_BUCKET_NAME=zcommerce-invoices

# JWT Secret (optional, default provided)
JWT_SECRET=your-super-secret-jwt-key-change-this-in-production-min-256-bits
```

If you don't provide AWS credentials, the invoice generation will still work, but uploads to S3 will fail gracefully.

---

## Testing the Application

Once the containers are running, test the APIs:

```bash
# Run the comprehensive test suite
./test_zcommerce_apis.sh
```

Or test manually:

```bash
# Health check
curl http://localhost:8080/zcommerce/actuator/health

# Register a user
curl -X POST http://localhost:8080/zcommerce/api/v1/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "username": "testuser",
    "password": "password123",
    "email": "test@example.com",
    "firstName": "Test",
    "lastName": "User"
  }'

# Login
curl -X POST http://localhost:8080/zcommerce/api/v1/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "username": "testuser",
    "password": "password123"
  }'
```

---

## Stopping the Services

```bash
# Stop all containers
docker-compose -f docker-compose.local.yml down

# Stop and remove volumes (clean slate)
docker-compose -f docker-compose.local.yml down -v
```

---

## Troubleshooting

### Port Conflicts

If you get port conflicts:

```bash
# Check what's using the port
lsof -i :8080

# Kill the process
kill -9 <PID>

# Or change the port in docker-compose file
ports:
  - "8081:8080"  # Use 8081 externally instead of 8080
```

### Application Won't Start

1. Check logs:
```bash
docker logs zcommerce-platform-local -f
```

2. Rebuild from scratch:
```bash
docker-compose -f docker-compose.local.yml down -v
docker-compose -f docker-compose.local.yml build --no-cache
docker-compose -f docker-compose.local.yml up
```

### Kafka Connection Issues

If Kafka health checks fail:
- Wait 60 seconds for Kafka to fully initialize
- Check Kafka logs: `docker logs zcommerce-kafka`
- Restart Kafka: `docker-compose restart kafka`

---

## Remote Debugging (IntelliJ IDEA)

When using `docker-compose.local.yml`, remote debugging is enabled:

1. In IntelliJ, go to **Run > Edit Configurations**
2. Click **+** and select **Remote JVM Debug**
3. Set **Host**: `localhost`, **Port**: `5005`
4. Click **OK** and start debugging

---

## Docker Compose File Comparison

| Feature | dev.yml | local.yml | yml |
|---------|---------|-----------|-----|
| Database | H2 (in-memory) | H2 (in-memory) | MySQL (persistent) |
| Kafka | ❌ | ✅ | ✅ |
| Redis | ❌ | ✅ | ✅ |
| Kafka UI | ❌ | ✅ | ✅ |
| Remote Debug | ❌ | ✅ | ❌ |
| Best for | Quick API tests | Full dev | Prod simulation |

---

## Multi-Module Build Structure

The Dockerfile uses a multi-stage build:

1. **Build Stage**: Uses Maven to compile all modules (core, api, application, payment, invoice, etc.)
2. **Runtime Stage**: Uses lightweight JRE image with just the application JAR

This ensures:
- ✅ No need for Java/Maven on your machine
- ✅ Consistent build environment
- ✅ Smaller final image size
- ✅ Build cache optimization

---

## Production Deployment

For production deployment:

1. Update environment variables in `docker-compose.yml`
2. Use external MySQL/Redis/Kafka (not containerized)
3. Set proper resource limits
4. Use secrets management (not `.env` file)
5. Enable SSL/TLS
6. Configure proper logging and monitoring

---

## Support

For issues or questions:
- Check logs: `docker logs <container-name>`
- View all containers: `docker ps -a`
- Check network: `docker network inspect <network-name>`

