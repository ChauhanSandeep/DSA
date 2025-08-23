#!/bin/bash

# Directory containing Java files
DIR="/Users/sachauha/Idea/tesseract-core/frazsheet"

# Find all Java files and update package declaration
find "$DIR" -name "*.java" -type f -print0 | while IFS= read -r -d $'\0' file; do
    echo "Updating package in: $file"
    
    # Use sed to update package declaration
    # This will replace any package declaration with 'package frazsheet;'
    sed -i '' 's/^package .*;/package frazsheet;/' "$file"
    
    # For files that might not have a package declaration, we'll add one
    if ! grep -q '^package ' "$file"; then
        # Add package declaration at the top of the file
        sed -i '' '1i\
package frazsheet;\
' "$file"
    fi
done

echo "Package updates complete!"
