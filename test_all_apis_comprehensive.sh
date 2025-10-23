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
CUSTOMER_USERNAME="testuser"
CUSTOMER_PASSWORD="testpass123"

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

# Function to make HTTP requests
make_request() {
    local method="$1"
    local url="$2"
    local headers="$3"
    local data="$4"
    local expected_status="$5"
    
    if [ -n "$data" ]; then
        response=$(curl -s -w "\n%{http_code}" -X "$method" "$url" $headers -d "$data")
    else
        response=$(curl -s -w "\n%{http_code}" -X "$method" "$url" $headers)
    fi
    
    http_code=$(echo "$response" | tail -n1)
    body=$(echo "$response" | head -n -1)
    
    if [ "$http_code" = "$expected_status" ]; then
        echo "$body"
        return 0
    else
        echo "Expected status $expected_status, got $http_code" >&2
        echo "$body" >&2
        return 1
    fi
}

# Function to extract token from response
extract_token() {
    echo "$1" | grep -o '"token":"[^"]*"' | cut -d'"' -f4
}

echo -e "${BLUE}🚀 Starting Comprehensive API Testing${NC}"
echo "=================================================="

# Step 1: Test Authentication
echo -e "\n${YELLOW}🔐 Testing Authentication APIs${NC}"
echo "----------------------------------------"

# Test admin login
echo "Testing admin login..."
admin_response=$(make_request "POST" "$BASE_URL/api/v1/auth/login" \
    '-H "Content-Type: application/json"' \
    '{"username":"'$ADMIN_USERNAME'","password":"'$ADMIN_PASSWORD'"}' \
    "200" 2>/dev/null)

if [ $? -eq 0 ]; then
    ADMIN_TOKEN=$(extract_token "$admin_response")
    print_test_result "Admin Login" "PASS" "Admin authenticated successfully"
else
    print_test_result "Admin Login" "FAIL" "Admin authentication failed"
    ADMIN_TOKEN=""
fi

# Test customer login
echo "Testing customer login..."
customer_response=$(make_request "POST" "$BASE_URL/api/v1/auth/login" \
    '-H "Content-Type: application/json"' \
    '{"username":"'$CUSTOMER_USERNAME'","password":"'$CUSTOMER_PASSWORD'"}' \
    "200" 2>/dev/null)

if [ $? -eq 0 ]; then
    CUSTOMER_TOKEN=$(extract_token "$customer_response")
    print_test_result "Customer Login" "PASS" "Customer authenticated successfully"
else
    print_test_result "Customer Login" "FAIL" "Customer authentication failed"
    CUSTOMER_TOKEN=""
fi

# Test unauthorized access
echo "Testing unauthorized access..."
unauthorized_response=$(make_request "GET" "$BASE_URL/api/v1/customers" \
    '-H "Content-Type: application/json"' \
    "" \
    "401" 2>/dev/null)

if [ $? -eq 0 ]; then
    print_test_result "Unauthorized Access" "PASS" "Unauthorized access properly blocked"
else
    print_test_result "Unauthorized Access" "FAIL" "Unauthorized access not blocked"
fi

# Step 2: Test Security and Authorization
echo -e "\n${YELLOW}🔒 Testing Security and Authorization${NC}"
echo "----------------------------------------"

if [ -n "$CUSTOMER_TOKEN" ]; then
    # Test customer trying to access admin-only endpoints
    echo "Testing customer access to admin-only endpoints..."
    
    # Customer trying to get all customers (should fail)
    customer_admin_test=$(make_request "GET" "$BASE_URL/api/v1/customers" \
        '-H "Authorization: Bearer '$CUSTOMER_TOKEN'" -H "Content-Type: application/json"' \
        "" \
        "403" 2>/dev/null)
    
    if [ $? -eq 0 ]; then
        print_test_result "Customer Admin Access Block" "PASS" "Customer properly blocked from admin endpoints"
    else
        print_test_result "Customer Admin Access Block" "FAIL" "Customer can access admin endpoints"
    fi
    
    # Customer trying to create product (should fail)
    customer_product_test=$(make_request "POST" "$BASE_URL/api/v1/products" \
        '-H "Authorization: Bearer '$CUSTOMER_TOKEN'" -H "Content-Type: application/json"' \
        '{"name":"Test Product","description":"Test","price":99.99,"sku":"TEST-001","categoryId":1,"stockQuantity":10,"isActive":true}' \
        "403" 2>/dev/null)
    
    if [ $? -eq 0 ]; then
        print_test_result "Customer Product Creation Block" "PASS" "Customer properly blocked from creating products"
    else
        print_test_result "Customer Product Creation Block" "FAIL" "Customer can create products"
    fi
fi

if [ -n "$ADMIN_TOKEN" ]; then
    # Test admin access to admin-only endpoints
    echo "Testing admin access to admin-only endpoints..."
    
    # Admin getting all customers (should succeed)
    admin_customers_test=$(make_request "GET" "$BASE_URL/api/v1/customers" \
        '-H "Authorization: Bearer '$ADMIN_TOKEN'" -H "Content-Type: application/json"' \
        "" \
        "200" 2>/dev/null)
    
    if [ $? -eq 0 ]; then
        print_test_result "Admin Customer Access" "PASS" "Admin can access customer endpoints"
    else
        print_test_result "Admin Customer Access" "FAIL" "Admin cannot access customer endpoints"
    fi
fi

# Step 3: Test Product Management APIs
echo -e "\n${YELLOW}📦 Testing Product Management APIs${NC}"
echo "----------------------------------------"

if [ -n "$ADMIN_TOKEN" ]; then
    # Test product creation
    echo "Testing product creation..."
    product_creation_response=$(make_request "POST" "$BASE_URL/api/v1/products" \
        '-H "Authorization: Bearer '$ADMIN_TOKEN'" -H "Content-Type: application/json"' \
        '{"name":"Test Product","description":"A test product for API testing","price":99.99,"sku":"TEST-001","categoryId":1,"stockQuantity":10,"isActive":true}' \
        "201" 2>/dev/null)
    
    if [ $? -eq 0 ]; then
        PRODUCT_ID=$(echo "$product_creation_response" | grep -o '"id":[0-9]*' | cut -d':' -f2)
        print_test_result "Product Creation" "PASS" "Product created successfully with ID: $PRODUCT_ID"
    else
        print_test_result "Product Creation" "FAIL" "Product creation failed"
        PRODUCT_ID=""
    fi
    
    # Test product update
    if [ -n "$PRODUCT_ID" ]; then
        echo "Testing product update..."
        product_update_response=$(make_request "PUT" "$BASE_URL/api/v1/products/$PRODUCT_ID" \
            '-H "Authorization: Bearer '$ADMIN_TOKEN'" -H "Content-Type: application/json"' \
            '{"name":"Updated Test Product","description":"Updated description","price":149.99,"stockQuantity":25,"isActive":true,"categoryId":1}' \
            "200" 2>/dev/null)
        
        if [ $? -eq 0 ]; then
            print_test_result "Product Update" "PASS" "Product updated successfully"
        else
            print_test_result "Product Update" "FAIL" "Product update failed"
        fi
    fi
    
    # Test product deletion
    if [ -n "$PRODUCT_ID" ]; then
        echo "Testing product deletion..."
        product_deletion_response=$(make_request "DELETE" "$BASE_URL/api/v1/products/$PRODUCT_ID" \
            '-H "Authorization: Bearer '$ADMIN_TOKEN'" -H "Content-Type: application/json"' \
            "" \
            "200" 2>/dev/null)
        
        if [ $? -eq 0 ]; then
            print_test_result "Product Deletion" "PASS" "Product deleted successfully"
        else
            print_test_result "Product Deletion" "FAIL" "Product deletion failed"
        fi
    fi
fi

# Test public product access (no authentication required)
echo "Testing public product access..."
public_products_response=$(make_request "GET" "$BASE_URL/api/v1/products" \
    '-H "Content-Type: application/json"' \
    "" \
    "200" 2>/dev/null)

if [ $? -eq 0 ]; then
    print_test_result "Public Product Access" "PASS" "Public product access working"
else
    print_test_result "Public Product Access" "FAIL" "Public product access failed"
fi

# Step 4: Test Order Management APIs
echo -e "\n${YELLOW}📋 Testing Order Management APIs${NC}"
echo "----------------------------------------"

if [ -n "$CUSTOMER_TOKEN" ]; then
    # Test order creation
    echo "Testing order creation..."
    order_creation_response=$(make_request "POST" "$BASE_URL/api/v1/orders" \
        '-H "Authorization: Bearer '$CUSTOMER_TOKEN'" -H "Content-Type: application/json"' \
        '{"customerId":3,"customerEmail":"test@example.com","shippingAddress":"123 Test St, Test City, Test State, 12345, Test Country","orderItems":[{"productId":5,"quantity":2}]}' \
        "201" 2>/dev/null)
    
    if [ $? -eq 0 ]; then
        ORDER_ID=$(echo "$order_creation_response" | grep -o '"id":[0-9]*' | cut -d':' -f2)
        print_test_result "Order Creation" "PASS" "Order created successfully with ID: $ORDER_ID"
    else
        print_test_result "Order Creation" "FAIL" "Order creation failed"
        ORDER_ID=""
    fi
    
    # Test order retrieval
    if [ -n "$ORDER_ID" ]; then
        echo "Testing order retrieval..."
        order_retrieval_response=$(make_request "GET" "$BASE_URL/api/v1/orders/$ORDER_ID" \
            '-H "Authorization: Bearer '$CUSTOMER_TOKEN'" -H "Content-Type: application/json"' \
            "" \
            "200" 2>/dev/null)
        
        if [ $? -eq 0 ]; then
            print_test_result "Order Retrieval" "PASS" "Order retrieved successfully"
        else
            print_test_result "Order Retrieval" "FAIL" "Order retrieval failed"
        fi
    fi
fi

# Step 5: Test Payment Management APIs
echo -e "\n${YELLOW}💳 Testing Payment Management APIs${NC}"
echo "----------------------------------------"

if [ -n "$CUSTOMER_TOKEN" ] && [ -n "$ORDER_ID" ]; then
    # Test payment processing
    echo "Testing payment processing..."
    payment_response=$(make_request "POST" "$BASE_URL/api/v1/payments" \
        '-H "Authorization: Bearer '$CUSTOMER_TOKEN'" -H "Content-Type: application/json"' \
        '{"orderId":'$ORDER_ID',"customerId":3,"amount":299.98,"paymentMethod":"CREDIT_CARD","transactionId":"TXN-001","cardNumber":"4111111111111111","cardHolderName":"Test User","expiryDate":"12/25","cvv":"123"}' \
        "200" 2>/dev/null)
    
    if [ $? -eq 0 ]; then
        PAYMENT_ID=$(echo "$payment_response" | grep -o '"id":[0-9]*' | cut -d':' -f2)
        print_test_result "Payment Processing" "PASS" "Payment processed successfully with ID: $PAYMENT_ID"
    else
        print_test_result "Payment Processing" "FAIL" "Payment processing failed"
        PAYMENT_ID=""
    fi
fi

# Step 6: Test Invoice Management APIs
echo -e "\n${YELLOW}📄 Testing Invoice Management APIs${NC}"
echo "----------------------------------------"

if [ -n "$ADMIN_TOKEN" ] && [ -n "$ORDER_ID" ]; then
    # Test invoice generation
    echo "Testing invoice generation..."
    invoice_generation_response=$(make_request "POST" "$BASE_URL/api/v1/invoices/order/$ORDER_ID" \
        '-H "Authorization: Bearer '$ADMIN_TOKEN'" -H "Content-Type: application/json"' \
        "" \
        "201" 2>/dev/null)
    
    if [ $? -eq 0 ]; then
        print_test_result "Invoice Generation" "PASS" "Invoice generated successfully"
    else
        print_test_result "Invoice Generation" "FAIL" "Invoice generation failed"
    fi
    
    # Test invoice existence check
    echo "Testing invoice existence check..."
    invoice_exists_response=$(make_request "GET" "$BASE_URL/api/v1/invoices/order/$ORDER_ID/exists" \
        '-H "Authorization: Bearer '$ADMIN_TOKEN'" -H "Content-Type: application/json"' \
        "" \
        "200" 2>/dev/null)
    
    if [ $? -eq 0 ]; then
        print_test_result "Invoice Existence Check" "PASS" "Invoice existence check working"
    else
        print_test_result "Invoice Existence Check" "FAIL" "Invoice existence check failed"
    fi
    
    # Test invoice URL retrieval
    echo "Testing invoice URL retrieval..."
    invoice_url_response=$(make_request "GET" "$BASE_URL/api/v1/invoices/order/$ORDER_ID" \
        '-H "Authorization: Bearer '$ADMIN_TOKEN'" -H "Content-Type: application/json"' \
        "" \
        "200" 2>/dev/null)
    
    if [ $? -eq 0 ]; then
        print_test_result "Invoice URL Retrieval" "PASS" "Invoice URL retrieved successfully"
    else
        print_test_result "Invoice URL Retrieval" "FAIL" "Invoice URL retrieval failed"
    fi
    
    # Test invoice deletion
    echo "Testing invoice deletion..."
    invoice_deletion_response=$(make_request "DELETE" "$BASE_URL/api/v1/invoices/order/$ORDER_ID" \
        '-H "Authorization: Bearer '$ADMIN_TOKEN'" -H "Content-Type: application/json"' \
        "" \
        "200" 2>/dev/null)
    
    if [ $? -eq 0 ]; then
        print_test_result "Invoice Deletion" "PASS" "Invoice deleted successfully"
    else
        print_test_result "Invoice Deletion" "FAIL" "Invoice deletion failed"
    fi
fi

# Step 7: Test Customer Management APIs
echo -e "\ KH
Testing Customer Management APIs${NC}"
echo "----------------------------------------"

if [ -n "$ADMIN_TOKEN" ]; then
    # Test customer creation
    echo "Testing customer creation..."
    customer_creation_response=$(make_request "POST" "$BASE_URL/api/v1/auth/register" \
        '-H "Content-Type: application/json"' \
        '{"username":"testuser2","password":"testpass123","email":"testuser2@example.com","firstName":"Test","lastName":"User2","phoneNumber":"9876543210","streetAddress":"123 Test St","city":"Test City","state":"Test State","postalCode":"12345","country":"Test Country"}' \
        "201" 2>/dev/null)
    
    if [ $? -eq 0 ]; then
        print_test_result "Customer Creation" "PASS" "Customer created successfully"
    else
        print_test_result "Customer Creation" "FAIL" "Customer creation failed"
    fi
fi

# Step 8: Test Category Management APIs
echo -e "\n${YELLOW}📂 Testing Category Management APIs${NC}"
echo "----------------------------------------"

# Test category retrieval (public access)
echo "Testing category retrieval..."
category_response=$(make_request "GET" "$BASE_URL/api/v1/categories" \
    '-H "Content-Type: application/json"' \
    "" \
    "200" 2>/dev/null)

if [ $? -eq 0 ]; then
    print_test_result "Category Retrieval" "PASS" "Categories retrieved successfully"
else
    print_test_result "Category Retrieval" "FAIL" "Category retrieval failed"
fi

# Final Results
echo -e "\n${BLUE}📊 TEST RESULTS SUMMARY${NC}"
echo "=================================================="
echo -e "Total Tests: $TOTAL_TESTS"
echo -e "${GREEN}Passed: $TESTS_PASSED${NC}"
echo -e "${RED}Failed: $TESTS_FAILED${NC}"

if [ $TESTS_FAILED -eq 0 ]; then
    echo -e "\n${GREEN}🎉 ALL TESTS PASSED! The application is working correctly.${NC}"
    exit 0
else
    echo -e "\n${RED}⚠️  Some tests failed. Please review the issues above.${NC}"
    exit 1
fi
