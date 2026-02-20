# 🔍 SonarQube Test Code - Expected Issues

This document lists all the intentional code issues that SonarQube should detect.

## 📁 Test Files Created

### 1. **BadCodeExample.java**
Security vulnerabilities and code quality issues

### 2. **DuplicateCode.java**
Duplicate code blocks and methods

### 3. **SecurityIssues.java**
Multiple security vulnerabilities

### 4. **MoreCodeSmells.java**
Various code smells and anti-patterns

---

## 🔴 Expected Security Issues (Critical)

### **Hardcoded Passwords** (Multiple instances)
- `PASSWORD = "admin123"`
- `DB_PASSWORD = "MySecretPassword"`
- `DATABASE_PASSWORD = "SuperSecret123!"`
- `ADMIN_PASSWORD = "admin"`
- `ROOT_PASSWORD = "root123"`
- `userPassword = "password123"`
- `password = "password123"` (in connection string)

### **Hardcoded API Keys & Tokens**
- `API_KEY = "sk_live_1234567890abcdef"`
- `AWS_SECRET_KEY = "AKIAIOSFODNN7EXAMPLE"`
- `STRIPE_API_KEY = "sk_test_1234567890abcdefghijklmno"`
- `JWT_SECRET = "myJWTSecretKey123456789"`

### **SQL Injection Vulnerability**
```java
String query = "SELECT * FROM users WHERE username = '" + userInput + "'";
```

### **Command Injection**
```java
Runtime.getRuntime().exec("ping " + userInput);
```

### **Weak Cryptography**
- Using MD5 for password hashing (deprecated and insecure)
- Predictable random number generation using `java.util.Random`

### **Path Traversal Vulnerability**
```java
new File("/app/data/" + fileName);  // No validation
```

### **Insecure Deserialization**
```java
ObjectInputStream.readObject();  // Without validation
```

---

## 🟠 Expected Code Smells (Major)

### **Code Duplication**
- 3 identical methods: `processUserData()`, `processCustomerData()`, `processEmployeeData()`
- 3 identical address formatting methods
- Multiple duplicate code blocks

### **Cognitive Complexity Too High**
- `complexMethod()` - deeply nested if statements (5 levels)
- `complexMethod()` - nested loops (4 levels deep)
- `calculateDiscount()` - complex conditional logic

### **Method Too Long**
- `complexMethod()` with excessive nesting and loops

### **Too Many Parameters**
- `tooManyParameters()` - 8 parameters
- `calculateDiscount()` - 5 parameters

### **Empty Catch Block**
```java
catch (Exception e) {
    // Swallowing exception
}
```

### **Dead Code**
- `unusedVariable` - never used
- `neverCalledMethod()` - never called

### **Magic Numbers**
```java
return price * 100;  // What is 100?
```

### **Switch Without Default**
```java
switch (code) {
    case 1: ...
    case 2: ...
    // Missing default
}
```

---

## 🟡 Expected Code Smells (Minor)

### **String Concatenation in Loop**
```java
for (String item : items) {
    result = result + item + ", ";  // Should use StringBuilder
}
```

### **Multiple Return Statements**
- `getStatus()` method has 6 return statements

### **Identical Branches**
```java
if (condition) {
    System.out.println("Same action");
} else {
    System.out.println("Same action");  // Identical!
}
```

### **Boolean Method Naming**
- `active()` should be `isActive()`

### **Method Naming Convention**
- `DoSomething()` should be `doSomething()`

### **Commented Out Code**
```java
// System.out.println("Old implementation");
// for (int i = 0; i < 10; i++) { ... }
```

### **Public Mutable Static Field**
```java
public static List<String> publicList = new ArrayList<>();
```

---

## 📊 Expected SonarQube Results

### **Overall Metrics:**
- **Bugs:** 3-5 issues
- **Vulnerabilities:** 10-15 issues (HIGH severity)
- **Code Smells:** 30-40 issues
- **Security Hotspots:** 5-10 hotspots
- **Duplications:** 15-20% duplicate code
- **Coverage:** 0% (no tests)

### **Quality Gate Status:**
- ❌ **FAILED** (expected due to critical security issues)

### **Top Issues by Type:**
1. 🔴 **Critical:** Hardcoded credentials (10+ instances)
2. 🔴 **Critical:** SQL/Command injection vulnerabilities
3. 🟠 **Major:** Code duplication (3 sets of duplicates)
4. 🟠 **Major:** Cognitive complexity too high
5. 🟡 **Minor:** Various code smells

---

## 🎯 How to View Results

### **1. GitHub Actions:**
https://github.com/KONJETIVEERASHANKARUDU/testenvfor_sonar/actions

### **2. SonarCloud Dashboard:**
https://sonarcloud.io/project/overview?id=KONJETIVEERASHANKARUDU_testenvfor_sonar

### **3. View Specific Issue Types:**
- **Security:** Security tab in SonarCloud
- **Reliability:** Issues → Bugs
- **Maintainability:** Issues → Code Smells
- **Duplications:** Measures → Duplications

---

## ✅ Verification Checklist

After SonarQube analysis completes, verify:

- [ ] At least 10 hardcoded credential issues detected
- [ ] SQL injection vulnerability flagged
- [ ] Command injection vulnerability flagged
- [ ] Code duplication percentage > 15%
- [ ] Cognitive complexity warnings present
- [ ] Empty catch block detected
- [ ] Dead code identified
- [ ] Quality Gate status is FAILED
- [ ] Security rating is low (D or E)
- [ ] Maintainability rating reflects code smells

---

## 📝 Notes

These are **intentional** code issues created specifically to test SonarQube's detection capabilities. This code should **NEVER** be used in production!

**Purpose:** Verify that SonarQube is properly configured and accurately detecting security vulnerabilities, bugs, and code quality issues.
