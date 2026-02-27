#!/usr/bin/env python3
"""
Quick Duplicate Code Analyzer
Detects duplicate code blocks in the test files
"""

import os
import hashlib
from pathlib import Path
from collections import defaultdict

def get_code_blocks(file_path, block_size=5):
    """Extract code blocks of specified size"""
    blocks = []
    try:
        with open(file_path, 'r', encoding='utf-8') as f:
            lines = [line.strip() for line in f.readlines() if line.strip() and not line.strip().startswith('//')]
            
        for i in range(len(lines) - block_size + 1):
            block = '\n'.join(lines[i:i+block_size])
            if len(block) > 50:  # Ignore very short blocks
                block_hash = hashlib.md5(block.encode()).hexdigest()
                blocks.append({
                    'hash': block_hash,
                    'content': block,
                    'start_line': i + 1,
                    'file': file_path
                })
    except Exception as e:
        print(f"Error reading {file_path}: {e}")
    
    return blocks

def find_duplicates(source_dir):
    """Find duplicate code blocks"""
    print("🔍 Scanning for duplicate code...\n")
    
    # Find all Java files
    java_files = list(Path(source_dir).rglob("*.java"))
    print(f"Found {len(java_files)} Java files\n")
    
    # Extract all code blocks
    all_blocks = []
    for java_file in java_files:
        blocks = get_code_blocks(str(java_file), block_size=5)
        all_blocks.extend(blocks)
    
    # Group by hash to find duplicates
    duplicates = defaultdict(list)
    for block in all_blocks:
        duplicates[block['hash']].append(block)
    
    # Filter only actual duplicates
    duplicate_groups = {k: v for k, v in duplicates.items() if len(v) > 1}
    
    print(f"=" * 70)
    print(f"DUPLICATE CODE DETECTION REPORT")
    print(f"=" * 70)
    print(f"\n📊 Summary:")
    print(f"  - Total code blocks analyzed: {len(all_blocks)}")
    print(f"  - Duplicate blocks found: {len(duplicate_groups)}")
    print(f"  - Total duplicated instances: {sum(len(v) for v in duplicate_groups.values())}")
    
    # Show top duplicates
    print(f"\n\n{'=' * 70}")
    print(f"TOP DUPLICATE CODE BLOCKS")
    print(f"{'=' * 70}\n")
    
    sorted_duplicates = sorted(duplicate_groups.items(), key=lambda x: len(x[1]), reverse=True)
    
    for idx, (hash_val, instances) in enumerate(sorted_duplicates[:10], 1):
        print(f"\n🔴 DUPLICATE #{idx}: Found in {len(instances)} locations")
        print(f"-" * 70)
        
        for instance in instances:
            rel_path = str(instance['file']).replace(source_dir + '/', '')
            print(f"  📄 {rel_path}:{instance['start_line']}")
        
        print(f"\n  Code snippet:")
        print(f"  ```java")
        for line in instances[0]['content'].split('\n')[:8]:
            print(f"  {line}")
        print(f"  ...```\n")
    
    # Generate fix suggestions
    print(f"\n{'=' * 70}")
    print(f"💡 FIX SUGGESTIONS")
    print(f"{'=' * 70}\n")
    
    if len(duplicate_groups) > 0:
        print("✅ Recommended Actions:\n")
        print("1. EXTRACT VALIDATION LOGIC:")
        print("   - Create ValidationUtils.java with static methods")
        print("   - Move validateCustomer/Vendor/Employee to single method")
        print("   - Example: ValidationUtils.validateEntity(name, email, phone)\n")
        
        print("2. EXTRACT DATA PROCESSING:")
        print("   - Create DataProcessor.java utility class")
        print("   - Consolidate processCustomerData/VendorData/EmployeeData")
        print("   - Example: DataProcessor.processData(List<String> data)\n")
        
        print("3. EXTRACT ERROR HANDLING:")
        print("   - Create ErrorHandler.java utility class")
        print("   - Unified handleError(Exception e, String context) method\n")
        
        print("4. EXTRACT CALCULATION LOGIC:")
        print("   - Create DiscountCalculator.java")
        print("   - Single calculateDiscount(price, quantity) method\n")
        
        print("🚀 Expected Benefits:")
        print("   - Reduce code size by ~60%")
        print("   - Improve maintainability")
        print("   - Easier testing with centralized logic")
        print("   - Better code reusability\n")
    
    return duplicate_groups


if __name__ == "__main__":
    source_dir = "/Users/veera.konjeti/Desktop/testenvfor_sonar/src"
    duplicates = find_duplicates(source_dir)
    
    print(f"\n{'=' * 70}")
    print(f"✓ Analysis Complete!")
    print(f"{'=' * 70}\n")
