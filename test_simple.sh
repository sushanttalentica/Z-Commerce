#!/bin/bash

echo "🔍 Testing Z-Commerce Basic Structure"
echo "====================================="

# Test 1: Check if we can at least validate the POM structure
echo ""
echo "1️⃣ Testing Maven POM Validation"
echo "-------------------------------"
cd /Users/sushantpandey/Z-Commerce
if mvn validate -q; then
    echo "✅ Maven POM validation successful"
else
    echo "❌ Maven POM validation failed"
    exit 1
fi

# Test 2: Check if we can compile just the core module with minimal dependencies
echo ""
echo "2️⃣ Testing Core Module Compilation (Minimal)"
echo "--------------------------------------------"
cd /Users/sushantpandey/Z-Commerce/core
if mvn clean compile -q; then
    echo "✅ Core module compilation successful"
else
    echo "❌ Core module compilation failed"
    echo "This indicates there are fundamental issues with the code structure"
fi

# Test 3: Check if we can at least package the modules
echo ""
echo "3️⃣ Testing Module Packaging"
echo "----------------------------"
cd /Users/sushantpandey/Z-Commerce
if mvn package -DskipTests -q; then
    echo "✅ Module packaging successful"
else
    echo "❌ Module packaging failed"
fi

echo ""
echo "🎯 Z-Commerce Structure Test Complete!"
echo "======================================="
echo "Summary:"
echo "- POM validation: $(if mvn validate -q >/dev/null 2>&1; then echo "✅ PASS"; else echo "❌ FAIL"; fi)"
echo "- Core compilation: $(if cd core && mvn clean compile -q >/dev/null 2>&1; then echo "✅ PASS"; else echo "❌ FAIL"; fi)"
echo "- Module packaging: $(if mvn package -DskipTests -q >/dev/null 2>&1; then echo "✅ PASS"; else echo "❌ FAIL"; fi)"
