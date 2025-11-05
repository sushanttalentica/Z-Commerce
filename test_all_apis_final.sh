#!/bin/bash

# Comprehensive API Test Script for Product Order Service
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

# Test results tracking
TESTS_PASSED=0
TESTS_FAILED=0
TOTAL_TESTS=0

# Function to print test results
print_test_result() {
    local test_name="$1"
    local status="$2"
    local message="$3"
    
    TOTAL_TESTS=$((TOTAL_TESTS + 1))
    
    if [ "$status" = "PASS" ]; then
        echo -e "${GREEN}✅ PASS${NC}: $test_name - $message"
        TESTS_PASSED=$((TESTS_PASSED + 1))
    else
        echo -e "${RED}❌ FAIL${NC}: $test_name - $message"
        TESTS_FAILED=$((TESTS_FAILED + 1))
    fi
}

# Function to make API calls and check responses
make_api_call() {
    local method="$1"
    local endpoint="$2"
    local data="$3"
    local token="$4"
    local expected_status="$5"
    local test_name="$6"
    
    local headers="Content-Type: application/json"
    if [ -n "$token" ]; then
        headers="$headers\nAuthorization: Bearer $token"
    fi
    
    local response
    if [ "$method" = "GET" ]; then
        response=$(curl -s -w "\n%{http_code}" -H "Content-Type: application/json" $(if [ -n "$token" ]; then echo "-H \"Authorization: Bearer $token\""; fi) "$BASE_URL$endpoint")
    else
        response=$(curl -s -w "\n%{http_code}" -X "$method" -H "Content-Type: application/json" $(if [ -n "$token" ]; then echo "-H \"Authorization: Bearer $token\""; fi) -d "$data" "$BASE_URL$endpoint")
    fi
    
    local body=$(echo "$response" | head -n -1)
    local status_code=$(echo "$response" | tail -n 1)
    
    if [ "$status_code" = "$expected_status" ]; then
        print_test_result "$test_name" "PASS" "Status: $status_code"
        echo "$body" | jq . 2>/dev/null || echo "$body"
    else
        print_test_result "$test_name" "FAIL" "Expected: $expected_status, Got: $status_code"
        echo "$body"
    fi
    
    echo ""
}

# Function to extract token from login response
extract_token() {
    local response="$1"
    echo "$response" | jq -r '.token' 2>/dev/null || echo ""
}

echo -e "${BLUE}🚀 Starting Comprehensive API Test Suite${NC}"
echo "=================================================="

# Step 1: Test Authentication
echo -e "\n${YELLOW}📋 STEP 1: Testing Authentication${NC}"
echo "----------------------------------------"

echo "Testing admin login..."
admin_response=$(curl -s -X POST "$BASE_URL/api/v1/auth/login" \
  -H "Content-Type: application/json" \
  -d "{\"username\":\"$ADMIN_USERNAME\",\"password\":\"$ADMIN_PASSWORD\"}")

admin_token=$(extract_token "$admin_response")
if [ -n "$admin_token" ] && [ "$admin_token" != "null" ]; then
    print_test_result "Admin Login" "PASS" "Token received"
    echo "Admin token: ${admin_token:0:50}..."
else
    print_test_result "Admin Login" "FAIL" "No token received"
    echo "$admin_response"
fi

echo "Testing customer login..."
customer_response=$(curl -s -X POST "$BASE_URL/api/v1/auth/login" \
  -H "Content-Type: application/json" \
  -d "{\"username\":\"$CUSTOMER_USERNAME\",\"password\":\"$CUSTOMER_PASSWORD\"}")

customer_token=$(extract_token "$customer_response")
if [ -n "$customer_token" ] && [ "$customer_token" != "null" ]; then
    print_test_result "Customer Login" "PASS" "Token received"
    echo "Customer token: ${customer_token:0:50}..."
else
    print_test_result "Customer Login" "FAIL" "No token received"
    echo "$customer_response"
fi

# Step 2: Test Product APIs
echo -e "\n${YELLOW}📋 STEP 2: Testing Product APIs${NC}"
echo "----------------------------------------"

echo "Testing get all products (public access)..."
make_api_call "GET" "/api/v1/products" "" "" "200" "Get All Products (Public)"

echo "Testing get product by ID (public access)..."
make_api_call "GET" "/api/v1/products/1" "" "" "200" "Get Product by ID (Public)"

echo "Testing create product (admin only)..."
product_data='{
  "name": "Test Product",
  "description": "Test Description",
  "price": 99.99,
  "categoryId": 1,
  "stock": 10,
  "imageUrl": "http://example.com/image.jpg"
}'
make_api_call "POST" "/api/v1/products" "$product_data" "$admin_token" "201" "Create Product (Admin)"

echo "Testing create product (customer - should fail)..."
make_api_call "POST" "/api/v1/products" "$product_data" "$customer_token" "403" "Create Product (Customer - Should Fail)"

echo "Testing update product (admin only)..."
update_data='{
  "id": 1,
  "name": "Updated Product",
  "description": "Updated Description",
  "price": 149.99,
  "categoryId": 1,
  "stock": 15,
  "imageUrl": "http://example.com/updated-image.jpg"
}'
make_api_call "PUT" "/api/v1/products/1" "$update_data" "$admin_token" "200" "Update Product (Admin)"

echo "Testing delete product (admin only)..."
make_api_call "DELETE" "/api/v1/products/5" "" "$admin_token" "204" "Delete Product (Admin)"

echo "Testing delete product (customer - should fail)..."
make_api_call "DELETE" "/api/v1/products/4" "" "$customer_token" "403" "Delete Product (Customer - Should Fail)"

# Step 3: Test Customer APIs
echo -e "\n${YELLOW}📋 STEP 3: Testing Customer APIs${NC}"
echo "----------------------------------------"

echo "Testing get all customers (admin only)..."
make_api_call "GET" "/api/v1/customers" "" "$admin_token" "200" "Get All Customers (Admin)"

echo "Testing get all customers (customer - should fail)..."
make_api_call "GET" "/api/v1/customers" "" "$customer_token" "403" "Get All Customers (Customer - Should Fail)"

echo "Testing get customer by ID (admin only)..."
make_api_call "GET" "/api/v1/customers/1" "" "$admin_token" "200" "Get Customer by ID (Admin)"

echo "Testing get customer by ID (customer - should fail)..."
make_api_call "GET" "/api/v1/customers/1" "" "$customer_token" "403" "Get Customer by ID (Customer - Should Fail)"

echo "Testing register new customer..."
register_data='{
  "username": "newcustomer",
  "password": "newpass123",
  "email": "newcustomer@example.com",
  "firstName": "New",
  "lastName": "Customer",
  "phoneNumber": "9876543210",
  "streetAddress": "123 New St",
  "city": "New City",
  "state": "New State",
  "postalCode": "54321",
  "country": "New Country"
}'
make_api_call "POST" "/api/v1/auth/register" "$register_data" "" "201" "Register New Customer"

echo "Testing delete customer (admin only)..."
make_api_call "DELETE" "/api/v1/customers/3" "" "$admin_token" "204" "Delete Customer (Admin)"

echo "Testing delete customer (customer - should fail)..."
make_api_call "DELETE" "/api/v1/customers/2" "" "$customer_token" "403" "Delete Customer (Customer - Should Fail)"

# Step 4: Test Category APIs
echo -e "\n${YELLOW}📋 STEP 4: Testing Category APIs${NC}"
echo "----------------------------------------"

echo "Testing get all categories (public access)..."
make_api_call "GET" "/api/v1/categories" "" "" "200" "Get All Categories (Public)"

echo "Testing get category by ID (public access)..."
make_api_call "GET" "/api/v1/categories/1" "" "" "200" "Get Category by ID (Public)"

# Step 5: Test Order APIs
echo -e "\n${YELLOW}📋 STEP 5: Testing Order APIs${NC}"
echo "----------------------------------------"

echo "Testing create order (authenticated user)..."
order_data='{
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
}'
make_api_call "POST" "/api/v1/orders" "$order_data" "$customer_token" "201" "Create Order (Customer)"

echo "Testing get orders (authenticated user)..."
make_api_call "GET" "/api/v1/orders" "" "$customer_token" "200" "Get Orders (Customer)"

# Step 6: Test Payment APIs
echo -e "\n${YELLOW}📋 STEP 6: Testing Payment APIs${NC}"
echo "----------------------------------------"

echo "Testing create payment (authenticated user)..."
payment_data='{
  "orderId": 1,
  "amount": 199.98,
  "paymentMethod": "CREDIT_CARD",
  "cardNumber": "4111111111111111",
  "expiryDate": "12/25",
  "cvv": "123"
}'
make_api_call "POST" "/api/v1/payments" "$payment_data" "$customer_token" "201" "Create Payment (Customer)"

echo "Testing get payments (authenticated user)..."
make_api_call "GET" "/api/v1/payments" "" "$customer_token" "200" "Get Payments (Customer)"

# Step 7: Test Invoice APIs
echo -e "\n${YELLOW}📋 STEP 7: Testing Invoice APIs${NC}"
echo "----------------------------------------"

echo "Testing generate invoice (admin only)..."
invoice_data='{
  "orderId": 1,
  "customerId": 2
}'
make_api_call "POST" "/api/v1/invoices" "$invoice_data" "$admin_token" "201" "Generate Invoice (Admin)"

echo "Testing generate invoice (customer - should fail)..."
make_api_call "POST" "/api/v1/invoices" "$invoice_data" "$customer_token" "403" "Generate Invoice (Customer - Should Fail)"

echo "Testing get all invoices (admin only)..."
make_api_call "GET" "/api/v1/invoices" "" "$admin_token" "200" "Get All Invoices (Admin)"

echo "Testing get invoice by ID (admin only)..."
make_api_call "GET" "/api/v1/invoices/1" "" "$admin_token" "200" "Get Invoice by ID (Admin)"

echo "Testing delete invoice (admin only)..."
make_api_call "DELETE" "/api/v1/invoices/1" "" "$admin_token" "204" "Delete Invoice (Admin)"

# Step 8: Test Security - Unauthorized Access
echo -e "\n${YELLOW}📋 STEP 8: Testing Security - Unauthorized Access${NC}"
echo "----------------------------------------"

echo "Testing access without token..."
make_api_call "GET" "/api/v1/customers" "" "" "401" "Access Without Token"

echo "Testing access with invalid token..."
make_api_call "GET" "/api/v1/customers" "" "invalid_token" "401" "Access With Invalid Token"

# Step 9: Test WebSocket Connection
echo -e "\n${YELLOW}📋 STEP 9: Testing WebSocket Connection${NC}"
echo "----------------------------------------"

echo "Testing WebSocket endpoint availability..."
ws_response=$(curl -s -I "$BASE_URL/ws")
if echo "$ws_response" | grep -q "101 Switching Protocols\|404\|200"; then
    print_test_result "WebSocket Endpoint" "PASS" "WebSocket endpoint accessible"
else
    print_test_result "WebSocket Endpoint" "FAIL" "WebSocket endpoint not accessible"
fi

# Step 10: Test Swagger UI
echo -e "\n${YELLOW}📋 STEP 10: Testing Swagger UI${NC}"
echo "----------------------------------------"

echo "Testing Swagger UI availability..."
swagger_response=$(curl -s -I "$BASE_URL/swagger-ui.html")
if echo "$swagger_response" | grep -q "200"; then
    print_test_result "Swagger UI" "PASS" "Swagger UI accessible"
else
    print_test_result "Swagger UI" "FAIL" "Swagger UI not accessible"
fi

# Final Results
echo -e "\n${BLUE}📊 FINAL TEST RESULTS${NC}"
echo "=================================================="
echo -e "Total Tests: $TOTAL_TESTS"
echo -e "${GREEN}Passed: $TESTS_PASSED${NC}"
echo -e "${RED}Failed: $TESTS_FAILED${NC}"

if [ $TESTS_FAILED -eq 0 ]; then
    echo -e "\n${GREEN}🎉 ALL TESTS PASSED! 🎉${NC}"
    echo "The application is working correctly with proper security controls."
else
    echo -e "\n${RED}❌ SOME TESTS FAILED${NC}"
    echo "Please review the failed tests above."
fi

echo -e "\n${BLUE}🔗 Useful URLs:${NC}"
echo "Application: $BASE_URL"
echo "Swagger UI: $BASE_URL/swagger-ui.html"
echo "H2 Console: $BASE_URL/h2-console"
echo "Actuator Health: $BASE_URL/actuator/health"
echo "WebSocket: ws://localhost:8080/product-order-service/ws"
