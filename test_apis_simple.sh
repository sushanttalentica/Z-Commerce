#!/bin/bash

# Simple API Test Script for Product Order Service
# This script tests all APIs including security, authentication, and authorization

set -e

# Colors for output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

# Configuration
BASE_URL="http://localhost:8080/product-order-service"
ADMIN_USERNAME="admin"
ADMIN_PASSWORD="admin123"
CUSTOMER_USERNAME="customer"
CUSTOMER_PASSWORD="customer123"

echo -e "${BLUE}🚀 Starting Simple API Test Suite${NC}"
echo "=================================================="

# Step 1: Test Authentication
echo -e "\n${YELLOW}📋 STEP 1: Testing Authentication${NC}"
echo "----------------------------------------"

echo "Testing admin login..."
admin_response=$(curl -s -X POST "$BASE_URL/api/v1/auth/login" \
  -H "Content-Type: application/json" \
  -d "{\"username\":\"$ADMIN_USERNAME\",\"password\":\"$ADMIN_PASSWORD\"}")

admin_token=$(echo "$admin_response" | jq -r '.token' 2>/dev/null)
if [ -n "$admin_token" ] && [ "$admin_token" != "null" ]; then
    echo -e "${GREEN}✅ Admin login successful${NC}"
    echo "Admin token: ${admin_token:0:50}..."
else
    echo -e "${RED}❌ Admin login failed${NC}"
    echo "$admin_response"
fi

echo "Testing customer login..."
customer_response=$(curl -s -X POST "$BASE_URL/api/v1/auth/login" \
  -H "Content-Type: application/json" \
  -d "{\"username\":\"$CUSTOMER_USERNAME\",\"password\":\"$CUSTOMER_PASSWORD\"}")

customer_token=$(echo "$customer_response" | jq -r '.token' 2>/dev/null)
if [ -n "$customer_token" ] && [ "$customer_token" != "null" ]; then
    echo -e "${GREEN}✅ Customer login successful${NC}"
    echo "Customer token: ${customer_token:0:50}..."
else
    echo -e "${RED}❌ Customer login failed${NC}"
    echo "$customer_response"
fi

# Step 2: Test Product APIs
echo -e "\n${YELLOW}📋 STEP 2: Testing Product APIs${NC}"
echo "----------------------------------------"

echo "Testing get all products (public access)..."
response=$(curl -s -w "%{http_code}" "$BASE_URL/api/v1/products")
status_code="${response: -3}"
if [ "$status_code" = "200" ]; then
    echo -e "${GREEN}✅ Get all products successful (Status: $status_code)${NC}"
else
    echo -e "${RED}❌ Get all products failed (Status: $status_code)${NC}"
fi

echo "Testing get product by ID (public access)..."
response=$(curl -s -w "%{http_code}" "$BASE_URL/api/v1/products/1")
status_code="${response: -3}"
if [ "$status_code" = "200" ]; then
    echo -e "${GREEN}✅ Get product by ID successful (Status: $status_code)${NC}"
else
    echo -e "${RED}❌ Get product by ID failed (Status: $status_code)${NC}"
fi

if [ -n "$admin_token" ] && [ "$admin_token" != "null" ]; then
    echo "Testing create product (admin only)..."
    response=$(curl -s -w "%{http_code}" -X POST "$BASE_URL/api/v1/products" \
      -H "Content-Type: application/json" \
      -H "Authorization: Bearer $admin_token" \
      -d '{
        "name": "Test Product",
        "description": "Test Description",
        "price": 99.99,
        "categoryId": 1,
        "stock": 10,
        "imageUrl": "http://example.com/image.jpg"
      }')
    status_code="${response: -3}"
    if [ "$status_code" = "201" ]; then
        echo -e "${GREEN}✅ Create product successful (Status: $status_code)${NC}"
    else
        echo -e "${RED}❌ Create product failed (Status: $status_code)${NC}"
    fi
fi

if [ -n "$customer_token" ] && [ "$customer_token" != "null" ]; then
    echo "Testing create product (customer - should fail)..."
    response=$(curl -s -w "%{http_code}" -X POST "$BASE_URL/api/v1/products" \
      -H "Content-Type: application/json" \
      -H "Authorization: Bearer $customer_token" \
      -d '{
        "name": "Test Product",
        "description": "Test Description",
        "price": 99.99,
        "categoryId": 1,
        "stock": 10,
        "imageUrl": "http://example.com/image.jpg"
      }')
    status_code="${response: -3}"
    if [ "$status_code" = "403" ]; then
        echo -e "${GREEN}✅ Create product correctly blocked for customer (Status: $status_code)${NC}"
    else
        echo -e "${RED}❌ Create product should be blocked for customer (Status: $status_code)${NC}"
    fi
fi

# Step 3: Test Customer APIs
echo -e "\n${YELLOW}📋 STEP 3: Testing Customer APIs${NC}"
echo "----------------------------------------"

if [ -n "$admin_token" ] && [ "$admin_token" != "null" ]; then
    echo "Testing get all customers (admin only)..."
    response=$(curl -s -w "%{http_code}" "$BASE_URL/api/v1/customers" \
      -H "Authorization: Bearer $admin_token")
    status_code="${response: -3}"
    if [ "$status_code" = "200" ]; then
        echo -e "${GREEN}✅ Get all customers successful (Status: $status_code)${NC}"
    else
        echo -e "${RED}❌ Get all customers failed (Status: $status_code)${NC}"
    fi
fi

if [ -n "$customer_token" ] && [ "$customer_token" != "null" ]; then
    echo "Testing get all customers (customer - should fail)..."
    response=$(curl -s -w "%{http_code}" "$BASE_URL/api/v1/customers" \
      -H "Authorization: Bearer $customer_token")
    status_code="${response: -3}"
    if [ "$status_code" = "403" ]; then
        echo -e "${GREEN}✅ Get all customers correctly blocked for customer (Status: $status_code)${NC}"
    else
        echo -e "${RED}❌ Get all customers should be blocked for customer (Status: $status_code)${NC}"
    fi
fi

# Step 4: Test Category APIs
echo -e "\n${YELLOW}📋 STEP 4: Testing Category APIs${NC}"
echo "----------------------------------------"

echo "Testing get all categories (public access)..."
response=$(curl -s -w "%{http_code}" "$BASE_URL/api/v1/categories")
status_code="${response: -3}"
if [ "$status_code" = "200" ]; then
    echo -e "${GREEN}✅ Get all categories successful (Status: $status_code)${NC}"
else
    echo -e "${RED}❌ Get all categories failed (Status: $status_code)${NC}"
fi

# Step 5: Test Order APIs
echo -e "\n${YELLOW}📋 STEP 5: Testing Order APIs${NC}"
echo "----------------------------------------"

if [ -n "$customer_token" ] && [ "$customer_token" != "null" ]; then
    echo "Testing create order (authenticated user)..."
    response=$(curl -s -w "%{http_code}" -X POST "$BASE_URL/api/v1/orders" \
      -H "Content-Type: application/json" \
      -H "Authorization: Bearer $customer_token" \
      -d '{
        "customerId": 2,
        "customerEmail": "customer@example.com",
        "orderItems": [
          {
            "productId": 1,
            "quantity": 2,
            "price": 99.99
          }
        ],
        "shippingAddress": {
          "streetAddress": "123 Test St",
          "city": "Test City",
          "state": "Test State",
          "postalCode": "12345",
          "country": "Test Country"
        }
      }')
    status_code="${response: -3}"
    if [ "$status_code" = "201" ]; then
        echo -e "${GREEN}✅ Create order successful (Status: $status_code)${NC}"
    else
        echo -e "${RED}❌ Create order failed (Status: $status_code)${NC}"
    fi
fi

# Step 6: Test Payment APIs
echo -e "\n${YELLOW}📋 STEP 6: Testing Payment APIs${NC}"
echo "----------------------------------------"

if [ -n "$customer_token" ] && [ "$customer_token" != "null" ]; then
    echo "Testing create payment (authenticated user)..."
    response=$(curl -s -w "%{http_code}" -X POST "$BASE_URL/api/v1/payments" \
      -H "Content-Type: application/json" \
      -H "Authorization: Bearer $customer_token" \
      -d '{
        "orderId": 1,
        "amount": 199.98,
        "paymentMethod": "CREDIT_CARD",
        "cardNumber": "4111111111111111",
        "expiryDate": "12/25",
        "cvv": "123"
      }')
    status_code="${response: -3}"
    if [ "$status_code" = "201" ]; then
        echo -e "${GREEN}✅ Create payment successful (Status: $status_code)${NC}"
    else
        echo -e "${RED}❌ Create payment failed (Status: $status_code)${NC}"
    fi
fi

# Step 7: Test Invoice APIs
echo -e "\n${YELLOW}📋 STEP 7: Testing Invoice APIs${NC}"
echo "----------------------------------------"

if [ -n "$admin_token" ] && [ "$admin_token" != "null" ]; then
    echo "Testing generate invoice (admin only)..."
    response=$(curl -s -w "%{http_code}" -X POST "$BASE_URL/api/v1/invoices" \
      -H "Content-Type: application/json" \
      -H "Authorization: Bearer $admin_token" \
      -d '{
        "orderId": 1,
        "customerId": 2
      }')
    status_code="${response: -3}"
    if [ "$status_code" = "201" ]; then
        echo -e "${GREEN}✅ Generate invoice successful (Status: $status_code)${NC}"
    else
        echo -e "${RED}❌ Generate invoice failed (Status: $status_code)${NC}"
    fi
fi

# Step 8: Test Security - Unauthorized Access
echo -e "\n${YELLOW}📋 STEP 8: Testing Security - Unauthorized Access${NC}"
echo "----------------------------------------"

echo "Testing access without token..."
response=$(curl -s -w "%{http_code}" "$BASE_URL/api/v1/customers")
status_code="${response: -3}"
if [ "$status_code" = "401" ]; then
    echo -e "${GREEN}✅ Access without token correctly blocked (Status: $status_code)${NC}"
else
    echo -e "${RED}❌ Access without token should be blocked (Status: $status_code)${NC}"
fi

# Step 9: Test Swagger UI
echo -e "\n${YELLOW}📋 STEP 9: Testing Swagger UI${NC}"
echo "----------------------------------------"

echo "Testing Swagger UI availability..."
response=$(curl -s -w "%{http_code}" -I "$BASE_URL/swagger-ui.html")
status_code="${response: -3}"
if [ "$status_code" = "200" ]; then
    echo -e "${GREEN}✅ Swagger UI accessible (Status: $status_code)${NC}"
else
    echo -e "${RED}❌ Swagger UI not accessible (Status: $status_code)${NC}"
fi

echo -e "\n${BLUE}🎉 API Testing Complete!${NC}"
echo "=================================================="
echo -e "${BLUE}🔗 Useful URLs:${NC}"
echo "Application: $BASE_URL"
echo "Swagger UI: $BASE_URL/swagger-ui.html"
echo "H2 Console: $BASE_URL/h2-console"
echo "Actuator Health: $BASE_URL/actuator/health"
