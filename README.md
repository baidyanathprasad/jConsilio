# Java Project

A Java-based application demonstrating professional coding practices and design patterns.

## 📋 Quick Start

### Prerequisites
- Java JDK 16 or higher
- Git (optional)

### Installation & Setup

```bash
# Navigate to project directory
cd project-directory

# Compile all Java files
javac -d out $(find src -name '*.java')

# Verify compilation
ls -la out/
```

### Running the Project

```bash
# Run the main demo/application
java -cp out package.name.ClassName

# Example:
# java -cp out parkinglot.ParkingLotDemo
```

## 📁 Project Structure

```
project-root/
├── src/
│   └── package/
│       ├── models/          # Data models
│       ├── service/         # Business logic
│       ├── enums/           # Type definitions
│       ├── exceptions/      # Custom exceptions
│       └── utils/           # Utility classes
├── out/                     # Compiled classes (auto-generated)
└── README.md               # This file
```

## ✨ Features

- Clean code architecture
- Thread-safe operations
- Custom exception handling
- Professional design patterns
- Immutable models

## 🏗️ Key Design Patterns

- Singleton Pattern
- Service Layer Pattern
- Facade Pattern
- Delegation Pattern
- Immutability Pattern

## 🛠️ Configuration

Edit configuration in relevant service or model classes as needed.

## 🧪 Testing

```bash
# Run individual class tests
javac -cp out test/YourTest.java
java -cp out test.YourTest
```

## 📝 Code Example

```java
// Basic usage template
public class Example {
    public static void main(String[] args) {
        try {
            // Your code here
            System.out.println("✓ Success");
        } catch (Exception e) {
            System.out.println("✗ Error: " + e.getMessage());
        }
    }
}
```

## 🔧 Troubleshooting

| Issue                      | Solution                |
|----------------------------|-------------------------|
| `javac: command not found` | Install Java JDK        |
| `class not found`          | Run compilation command |
| `NoClassDefFoundError`     | Recompile all files     |

## 📚 Directory Reference

- `src/models/` - Data objects and entities
- `src/service/` - Business logic and operations
- `src/enums/` - Enumeration types
- `src/exceptions/` - Custom exception classes
- `src/utils/` - Utility and helper classes
- `out/` - Compiled bytecode (generated)

## ✅ Quick Checklist

- [ ] Java JDK 16+ installed
- [ ] Project structure verified
- [ ] `javac` compilation successful
- [ ] Application runs without errors

## 📖 Notes

- All classes use proper encapsulation
- Exception handling is built-in
- Thread-safe where applicable
- Code follows Java conventions

---

**Status**: Ready to use ✅
