#!/bin/bash

# Script to migrate tesseract-core to Gradle project structure
# This moves all Java packages from root to src/main/java

set -e

BASE_DIR="/Users/sachauha/Idea/tesseract-core"
SRC_DIR="$BASE_DIR/src/main/java"

echo "🚀 Starting migration to Gradle structure..."

# List of all directories containing Java files (excluding already created dirs)
JAVA_DIRS=(
    "Array"
    "AmazonOa"
    "Backtracking"
    "BinarySearch"
    "Bitwise"
    "DailyBytes"
    "design"
    "Design"
    "DesignPatterns"
    "DynamicProgramming"
    "Graph"
    "greedy"
    "Hashing"
    "Heap"
    "LinkedIn"
    "LinkedList"
    "Maths"
    "Multithreading"
    "Recursion"
    "slidingwindow"
    "StackQueue"
    "String"
    "Test"
    "Tree"
    "tree"
    "utils"
)

# Move Main.java to src/main/java
if [ -f "$BASE_DIR/Main.java" ]; then
    echo "📁 Moving Main.java to $SRC_DIR/"
    mv "$BASE_DIR/Main.java" "$SRC_DIR/"
fi

# Move each directory to src/main/java
for dir in "${JAVA_DIRS[@]}"; do
    if [ -d "$BASE_DIR/$dir" ] && [ "$dir" != "src" ]; then
        echo "📁 Moving $dir to $SRC_DIR/"
        mv "$BASE_DIR/$dir" "$SRC_DIR/"
    fi
done

echo "✅ Migration completed!"
echo ""
echo "📋 Next steps:"
echo "1. Run 'gradle build' to test the new structure"
echo "2. Run 'gradle runAlgorithm -PmainClass=Main' to test main class"
echo "3. Run 'gradle runAlgorithm -PmainClass=Array.ThreeSum' to test specific algorithm"
