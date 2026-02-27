# 🚀 Java Code Migration Guide (Optional)

## ⚠️ IMPORTANT: This is OPTIONAL!

**You do NOT need to migrate your code from Java 17 to Java 21 for SonarQube 2026.1 to work!**

This guide is ONLY for teams who want to:
- Modernize their codebase to use Java 21 features
- Improve code readability with modern syntax
- Gain performance improvements (5-10%)
- Take advantage of latest language features

---

## 📋 Table of Contents

1. [Should You Migrate?](#should-you-migrate)
2. [OpenRewrite - Automated Migration](#openrewrite---automated-migration)
3. [What Will Change](#what-will-change)
4. [Step-by-Step Guide](#step-by-step-guide)
5. [Before & After Examples](#before--after-examples)
6. [Testing Strategy](#testing-strategy)
7. [Rollback Plan](#rollback-plan)

---

## 🤔 Should You Migrate?

### ✅ Migrate If:
- You want to use modern Java features (records, pattern matching, etc.)
- Your team wants cleaner, more readable code
- You're starting a new development phase
- You have time for thorough testing
- You want performance improvements

### ❌ Don't Migrate If:
- You're in a rush to upgrade SonarQube
- You're close to a production release
- Your team is unfamiliar with Java 21 features
- You have strict change freeze policies
- You just want SonarQube to work (it already does!)

---

## ⚡ OpenRewrite - Automated Migration

**OpenRewrite** is an automated code refactoring tool that can migrate your entire codebase from Java 17 to Java 21 in minutes.

### Why OpenRewrite?

| Manual Migration | OpenRewrite (Automated) |
|-----------------|-------------------------|
| Days to weeks of work | 5-10 minutes |
| Human errors | Consistent, tested |
| Hard to review | Git diff shows all changes |
| Tedious | One command |

### What OpenRewrite Does:
- ✅ Upgrades language constructs automatically
- ✅ Replaces old patterns with modern equivalents
- ✅ Maintains code functionality
- ✅ Creates reviewable git changes
- ✅ Safe and reversible

---

## 📝 What Will Change

### Java 21 Features OpenRewrite Applies:

#### 1. **Switch Expressions** (Java 14+)
Old switch statements → Modern switch expressions

#### 2. **Text Blocks** (Java 15+)
Multi-line strings → Clean text blocks

#### 3. **Pattern Matching for instanceof** (Java 16+)
instanceof + cast → Pattern matching

#### 4. **Records** (Java 16+)
POJOs → Immutable records (manual review needed)

#### 5. **Sealed Classes** (Java 17+)
Inheritance → Sealed hierarchies (manual)

#### 6. **Enhanced Null Handling**
Null checks → Cleaner patterns

---

## 🛠️ Step-by-Step Guide

### Step 1: Backup Your Code

```bash
# Create a branch for migration
git checkout -b feature/java-21-migration
git push -u origin feature/java-21-migration

# Tag current state (for easy rollback)
git tag pre-java-21-migration
git push origin pre-java-21-migration
```

---

### Step 2: Add OpenRewrite to Your Project

Update your `pom.xml`:

```xml
<project>
    <!-- ... existing configuration ... -->
    
    <properties>
        <!-- Update Java version -->
        <maven.compiler.source>21</maven.compiler.source>
        <maven.compiler.target>21</maven.compiler.target>
        <java.version>21</java.version>
    </properties>
    
    <build>
        <plugins>
            <!-- Add OpenRewrite Maven Plugin -->
            <plugin>
                <groupId>org.openrewrite.maven</groupId>
                <artifactId>rewrite-maven-plugin</artifactId>
                <version>5.42.0</version>
                <configuration>
                    <activeRecipes>
                        <recipe>org.openrewrite.java.migrate.UpgradeToJava21</recipe>
                    </activeRecipes>
                </configuration>
                <dependencies>
                    <dependency>
                        <groupId>org.openrewrite.recipe</groupId>
                        <artifactId>rewrite-migrate-java</artifactId>
                        <version>2.26.0</version>
                    </dependency>
                </dependencies>
            </plugin>
        </plugins>
    </build>
</project>
```

---

### Step 3: Discover Available Recipes

```bash
# See what OpenRewrite will do
mvn rewrite:discover
```

This shows all recipes that will be applied to your code.

---

### Step 4: Run a Dry-Run

```bash
# Preview changes without modifying files
mvn rewrite:dryRun

# Review the rewrite.patch file
cat target/rewrite.patch
```

This generates a patch file showing all proposed changes without actually changing your code.

---

### Step 5: Apply the Migration

```bash
# Apply all changes
mvn rewrite:run

# Review the changes
git diff
```

---

### Step 6: Build and Test

```bash
# Compile the project
mvn clean compile

# Run all tests
mvn test

# Run integration tests
mvn verify

# Check code coverage
mvn test jacoco:report
```

---

### Step 7: Review Changes

```bash
# Review file-by-file
git diff --name-only | while read file; do
    echo "Reviewing: $file"
    git diff "$file" | less
done

# Or use your IDE's diff viewer
```

---

### Step 8: Commit and Push

```bash
# Stage all changes
git add .

# Commit with descriptive message
git commit -m "Migrate to Java 21 using OpenRewrite

- Applied UpgradeToJava21 recipe
- Modernized switch statements to expressions
- Applied pattern matching for instanceof
- Updated text blocks where applicable
- All tests passing ✅"

# Push to remote
git push origin feature/java-21-migration
```

---

## 📊 Before & After Examples

### Example 1: Switch Expressions

**Before (Java 17):**
```java
public String getDayType(DayOfWeek day) {
    String type;
    switch (day) {
        case MONDAY:
        case TUESDAY:
        case WEDNESDAY:
        case THURSDAY:
        case FRIDAY:
            type = "Weekday";
            break;
        case SATURDAY:
        case SUNDAY:
            type = "Weekend";
            break;
        default:
            type = "Unknown";
            break;
    }
    return type;
}
```

**After (Java 21):**
```java
public String getDayType(DayOfWeek day) {
    return switch (day) {
        case MONDAY, TUESDAY, WEDNESDAY, THURSDAY, FRIDAY -> "Weekday";
        case SATURDAY, SUNDAY -> "Weekend";
        default -> "Unknown";
    };
}
```

**Impact:** 75% less code, more readable! ✅

---

### Example 2: Pattern Matching for instanceof

**Before (Java 17):**
```java
public double calculateArea(Shape shape) {
    if (shape instanceof Circle) {
        Circle circle = (Circle) shape;
        return Math.PI * circle.getRadius() * circle.getRadius();
    } else if (shape instanceof Rectangle) {
        Rectangle rectangle = (Rectangle) shape;
        return rectangle.getWidth() * rectangle.getHeight();
    } else if (shape instanceof Triangle) {
        Triangle triangle = (Triangle) shape;
        return 0.5 * triangle.getBase() * triangle.getHeight();
    }
    return 0;
}
```

**After (Java 21):**
```java
public double calculateArea(Shape shape) {
    if (shape instanceof Circle circle) {
        return Math.PI * circle.getRadius() * circle.getRadius();
    } else if (shape instanceof Rectangle rectangle) {
        return rectangle.getWidth() * rectangle.getHeight();
    } else if (shape instanceof Triangle triangle) {
        return 0.5 * triangle.getBase() * triangle.getHeight();
    }
    return 0;
}
```

**Even better with switch patterns (Java 21):**
```java
public double calculateArea(Shape shape) {
    return switch (shape) {
        case Circle circle -> Math.PI * circle.getRadius() * circle.getRadius();
        case Rectangle rectangle -> rectangle.getWidth() * rectangle.getHeight();
        case Triangle triangle -> 0.5 * triangle.getBase() * triangle.getHeight();
        default -> 0;
    };
}
```

**Impact:** Cleaner, safer, no manual casting! ✅

---

### Example 3: Text Blocks

**Before (Java 17):**
```java
public String getJsonTemplate() {
    return "{\n" +
           "  \"name\": \"John Doe\",\n" +
           "  \"age\": 30,\n" +
           "  \"address\": {\n" +
           "    \"street\": \"123 Main St\",\n" +
           "    \"city\": \"Springfield\"\n" +
           "  }\n" +
           "}";
}
```

**After (Java 21):**
```java
public String getJsonTemplate() {
    return """
        {
          "name": "John Doe",
          "age": 30,
          "address": {
            "street": "123 Main St",
            "city": "Springfield"
          }
        }
        """;
}
```

**Impact:** Much more readable, no escape characters! ✅

---

### Example 4: Records (Manual Review Recommended)

**Before (Java 17):**
```java
public class User {
    private final Long id;
    private final String name;
    private final String email;
    
    public User(Long id, String name, String email) {
        this.id = id;
        this.name = name;
        this.email = email;
    }
    
    public Long getId() { return id; }
    public String getName() { return name; }
    public String getEmail() { return email; }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof User)) return false;
        User user = (User) o;
        return Objects.equals(id, user.id) &&
               Objects.equals(name, user.name) &&
               Objects.equals(email, user.email);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(id, name, email);
    }
    
    @Override
    public String toString() {
        return "User{id=" + id + ", name='" + name + "', email='" + email + "'}";
    }
}
```

**After (Java 21):**
```java
public record User(Long id, String name, String email) {
    // Auto-generated:
    // - Constructor
    // - Getters (id(), name(), email())
    // - equals()
    // - hashCode()
    // - toString()
    
    // Add custom validation if needed
    public User {
        if (email == null || !email.contains("@")) {
            throw new IllegalArgumentException("Invalid email");
        }
    }
}
```

**Impact:** 90% less boilerplate! ✅

---

### Example 5: Null Safety

**Before (Java 17):**
```java
public String getUserCity(User user) {
    if (user != null) {
        Address address = user.getAddress();
        if (address != null) {
            return address.getCity();
        }
    }
    return "Unknown";
}
```

**After (Java 21):**
```java
public String getUserCity(User user) {
    return Optional.ofNullable(user)
        .map(User::getAddress)
        .map(Address::getCity)
        .orElse("Unknown");
}
```

**Impact:** Cleaner null handling! ✅

---

## 🧪 Testing Strategy

### 1. Unit Tests (Critical!)

```bash
# Run all unit tests
mvn test

# Check specific test class
mvn test -Dtest=UserServiceTest

# Run with coverage
mvn test jacoco:report
open target/site/jacoco/index.html
```

**Expected:** All tests should pass ✅

---

### 2. Integration Tests

```bash
# Run integration tests
mvn verify

# Or run specific integration test
mvn verify -Dit.test=UserServiceIT
```

---

### 3. Manual Testing Checklist

- [ ] Application starts successfully
- [ ] Core features work as expected
- [ ] Authentication/Authorization works
- [ ] Database operations function correctly
- [ ] API endpoints respond properly
- [ ] No new errors in logs
- [ ] Performance is same or better

---

### 4. Performance Testing

```bash
# Before migration - baseline
mvn clean install
java -jar target/your-app.jar &
./run_performance_tests.sh > before.txt

# After migration
mvn clean install
java -jar target/your-app.jar &
./run_performance_tests.sh > after.txt

# Compare
diff before.txt after.txt
```

---

## 🔄 Rollback Plan

### If Something Goes Wrong:

#### Option 1: Revert Git Branch

```bash
# Discard all changes
git checkout main
git branch -D feature/java-21-migration

# Start over if needed
git checkout -b feature/java-21-migration
```

---

#### Option 2: Revert to Tag

```bash
# Go back to pre-migration state
git reset --hard pre-java-21-migration

# Force push (if already pushed)
git push origin feature/java-21-migration --force
```

---

#### Option 3: Revert Specific Files

```bash
# Revert individual files
git checkout HEAD~1 -- src/main/java/com/example/User.java

# Commit the revert
git commit -m "Revert User.java from Java 21 migration"
```

---

## 📈 Migration Checklist

Use this checklist to track your progress:

- [ ] **Preparation**
  - [ ] Create backup branch
  - [ ] Create rollback tag
  - [ ] Document current state
  - [ ] Get team approval

- [ ] **Setup**
  - [ ] Update `pom.xml` Java version
  - [ ] Add OpenRewrite plugin
  - [ ] Run `mvn rewrite:discover`

- [ ] **Execution**
  - [ ] Run `mvn rewrite:dryRun`
  - [ ] Review patch file
  - [ ] Run `mvn rewrite:run`
  - [ ] Compile: `mvn clean compile`

- [ ] **Testing**
  - [ ] Run unit tests: `mvn test`
  - [ ] Run integration tests: `mvn verify`
  - [ ] Manual testing
  - [ ] Performance testing
  - [ ] Code review

- [ ] **Deployment**
  - [ ] Update CI/CD pipeline
  - [ ] Deploy to staging
  - [ ] Smoke tests in staging
  - [ ] Deploy to production
  - [ ] Monitor for issues

- [ ] **Documentation**
  - [ ] Update README.md
  - [ ] Update developer guides
  - [ ] Notify team of changes
  - [ ] Document Java 21 features used

---

## 📚 Additional Resources

### Official Documentation
- [Java 21 Release Notes](https://openjdk.org/projects/jdk/21/)
- [OpenRewrite Documentation](https://docs.openrewrite.org/)
- [Java Language Specification](https://docs.oracle.com/javase/specs/jls/se21/html/index.html)

### Java 21 Features Deep Dive
- [Pattern Matching](https://openjdk.org/jeps/441)
- [Record Patterns](https://openjdk.org/jeps/440)
- [Virtual Threads](https://openjdk.org/jeps/444)
- [Sequenced Collections](https://openjdk.org/jeps/431)
- [String Templates](https://openjdk.org/jeps/430) (Preview)

### Performance
- [Java 21 Performance Improvements](https://www.oracle.com/java/technologies/javase/21-relnotes.html)

---

## ❓ FAQ

### Q1: How long does migration take?
**A:** OpenRewrite runs in 5-10 minutes. Testing and review: 1-2 hours.

### Q2: Will it break my code?
**A:** OpenRewrite is safe and maintains functionality. Always test thoroughly!

### Q3: Can I migrate part of the codebase?
**A:** Yes! Use OpenRewrite on specific modules or packages.

### Q4: What if I don't like the changes?
**A:** Git revert makes rollback easy. That's why we create a branch!

### Q5: Do I need to upgrade dependencies?
**A:** Most Java 21-compatible libraries work fine. Check major frameworks.

### Q6: Is this required for SonarQube?
**A:** NO! This is completely optional. SonarQube works with Java 17 code!

---

## ✅ Success Criteria

Your migration is successful when:

1. ✅ All unit tests pass
2. ✅ All integration tests pass
3. ✅ Application starts without errors
4. ✅ Core features work correctly
5. ✅ Performance is same or better
6. ✅ Code review approved
7. ✅ Team understands new syntax
8. ✅ Documentation updated

---

## 🎯 Summary

| Aspect | Details |
|--------|---------|
| **Required?** | NO - Completely optional! |
| **Tool** | OpenRewrite (automated) |
| **Time** | 5-10 minutes (automation) + 1-2 hours (testing) |
| **Risk** | Low (easily reversible with Git) |
| **Benefits** | Modern syntax, better performance, cleaner code |
| **When to do** | When you have time for testing and team buy-in |
| **When to skip** | If you're in a rush or close to release |

---

## 🚀 Ready to Start?

```bash
# Quick start commands:
git checkout -b feature/java-21-migration
git tag pre-java-21-migration

# Add OpenRewrite to pom.xml (see Step 2 above)

mvn rewrite:dryRun      # Preview
mvn rewrite:run         # Apply
mvn clean test          # Test
git diff                # Review
git commit -am "Migrate to Java 21"
```

---

*Remember: This migration is OPTIONAL! Your Java 17 code works perfectly with SonarQube 2026.1!*

*Last Updated: February 23, 2026*
