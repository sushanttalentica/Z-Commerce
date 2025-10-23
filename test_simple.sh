#!/bin/bash

echo "Testing basic application functionality..."

# Test if application is running
echo "Testing health endpoint..."
curl -s -w "Status: %{http_code}\n" http://localhost:8080/product-order-service/api/v1/auth/health

echo ""
echo "Testing products endpoint..."
curl -s -w "Status: %{http_code}\n" http://localhost:8080/product-order-service/api/v1/products

echo ""
echo "Testing categories endpoint..."
curl -s -w "Status: %{http_code}\n" http://localhost:8080/product-order-service/api/v1/categories

echo ""
echo "Testing registration..."
curl -s -X POST http://localhost:8080/product-order-service/api/v1/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "username": "testuser2",
    "email": "testuser2@example.com",
    "password": "testpass123",
    "firstName": "Test",
    "lastName": "User"
  }' | head -c 200

echo ""
echo "Testing login..."
curl -s -X POST http://localhost:8080/product-order-service/api/v1/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "username": "admin",
    "password": "admin123"
  }' | head -c 200

echo ""
echo "Simple test completed."
