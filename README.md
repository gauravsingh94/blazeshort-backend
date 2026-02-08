🔗 BlazeShort Backend

A production-ready URL shortener backend built with Spring Boot, PostgreSQL, and Redis, featuring:
	•	🔐 JWT-based authentication
	•	🔗 URL shortening & redirection
	•	🚦 Redis-backed rate limiting
	•	📊 URL analytics (clicks, IP, User-Agent, daily stats)
	•	🧠 Redis caching for fast redirects
	•	🐳 Docker & Docker Compose support

⸻

🧠 Architecture Overview

Client
  ↓
Spring Boot API
  ├── PostgreSQL (persistent data)
  │     ├── Users
  │     ├── Short URLs
  │     └── URL Analytics
  │
  └── Redis (in-memory)
        ├── Rate limiting counters
        ├── Redirect cache
        └── Health checks


⸻

✨ Features

✅ Core
	•	Create short URLs
	•	Redirect using short code
	•	Enable / disable URLs
	•	URL expiry handling

🚦 Rate Limiting (Redis)
	•	Per-IP redirect rate limiting
	•	Prevents abuse & bot traffic
	•	Redis-based atomic counters

📊 Analytics
	•	Total click count
	•	Unique IP count
	•	Clicks per day
	•	Top IP addresses
	•	Top User Agents
	•	Recent click logs

⚡ Performance
	•	Redis cache for redirects
	•	DB fallback if cache misses
	•	Automatic cache refresh

⸻

🧰 Tech Stack

Layer	Technology
Backend	Spring Boot 4
Auth	Spring Security + JWT
Database	PostgreSQL
Cache / Rate Limit	Redis
ORM	Spring Data JPA
Build	Maven
Container	Docker, Docker Compose


⸻

📁 Project Structure

blazeshort-backend
│
├── src/main/java/com/blazeshort/demo
│   ├── config
│   │   ├── SecurityConfig.java
│   │   ├── RedisConfig.java
│   │   └── RateLimitConfig.java
│   │
│   ├── controller
│   │   ├── AuthController.java
│   │   ├── UrlController.java
│   │   └── DashboardController.java
│   │
│   ├── service
│   │   ├── ShortUrlService.java
│   │   ├── RateLimitService.java
│   │   ├── AnalyticsService.java
│   │   └── DashboardService.java
│   │
│   ├── repository
│   │   ├── ShortUrlRepository.java
│   │   ├── UserRepository.java
│   │   └── UrlAnalyticsRepository.java
│   │
│   ├── model
│   │   ├── entity
│   │   ├── dto
│   │   └── enums
│   │
│   └── security
│       └── JwtAuthenticationFilter.java
│
├── src/main/resources
│   └── application.properties
│
├── Dockerfile
├── docker-compose.yml
├── .dockerignore
└── README.md


⸻

🚦 Rate Limiting – How It Works

🎯 Goal

Limit how many redirects a single IP can perform in a time window.

🔧 Implementation
	•	Redis stores counters per IP
	•	Atomic increment using INCR
	•	TTL set for window duration

🧠 Flow

Request → RateLimitService
        → Redis INCR key
        → If count > limit → 429 Too Many Requests

📌 Example Key

redirect_rate:192.168.1.10

🧩 Code (simplified)

public boolean isAllowed(String key, int limit, int windowSec) {
    Long count = redisTemplate.opsForValue().increment(key);
    if (count == 1) {
        redisTemplate.expire(key, Duration.ofSeconds(windowSec));
    }
    return count <= limit;
}

📍 Applied At

@GetMapping("/{code}")
public void redirect(...) {
    if (!rateLimitService.isAllowed(...)) {
        throw new RateLimitExceededException();
    }
    response.sendRedirect(originalUrl);
}


⸻

📊 Analytics – How It Works

🔍 What We Track
	•	IP address
	•	User-Agent
	•	Click timestamp
	•	Associated short URL

🧠 When Data Is Stored

Every successful redirect

📦 Entity

UrlAnalytics {
  ipAddress
  userAgent
  createdAt
  shortUrl
}

🔄 Flow

Redirect Request
 → Validate URL
 → Save analytics record
 → Redirect user


⸻

⚡ Redis Cache for Redirects

Why?
	•	Avoid DB hit on every redirect
	•	Improve latency

Flow

GET /{code}
 → Redis GET short_url:{code}
   → HIT → redirect
   → MISS → DB → Redis SET → redirect


⸻

🐳 Setup with Docker (Recommended)

1️⃣ Prerequisites
	•	Docker
	•	Docker Compose (v2)

2️⃣ Run Everything

docker compose up --build

3️⃣ Services

Service	Port
Backend	8080
PostgreSQL	5432
Redis	6379

4️⃣ docker-compose.yml (overview)

services:
  app:
    build: .
    ports:
      - "8080:8080"
    depends_on:
      - postgres
      - redis

  postgres:
    image: postgres:15
    ports:
      - "5432:5432"

  redis:
    image: redis:7
    ports:
      - "6379:6379"


⸻

⚙️ Setup WITHOUT Docker

1️⃣ Install Dependencies
	•	Java 17
	•	PostgreSQL
	•	Redis

2️⃣ Create Database

CREATE DATABASE blazeshort;

3️⃣ Start Redis

redis-server

4️⃣ application.properties

spring.datasource.url=jdbc:postgresql://localhost:5432/blazeshort
spring.datasource.username=myuser
spring.datasource.password=mypassword

spring.data.redis.host=localhost
spring.data.redis.port=6379

5️⃣ Run App

mvn spring-boot:run


⸻

🧪 Testing Rate Limiting

for i in {1..20}; do
  curl -I http://localhost:8080/abc123
done

Expected:

HTTP 429 Too Many Requests


⸻

❌ Error Handling

Scenario	Response
URL expired	410 Gone
URL disabled	403 Forbidden
Not found	404
Rate limit	429

Handled via @ControllerAdvice.

⸻

🚀 Future Improvements
	•	Geo-location analytics
	•	Kafka for async analytics
	•	Admin dashboard
	•	Prometheus metrics
	•	Redis cluster support

⸻

👨‍💻 Author

Gaurav Singh
Spring Boot • Redis • System Design

⸻
