# 🍔 Food Ordering System (Console-Based Java Application)

---

## ✨ Features

### 👨‍💼 Admin
- Manage Users
- Manage Restaurants
- Manage Discounts
- View Orders
- Trace Order

### 👤 Customer
- Register and Login
- View Restaurants
- View Restaurant Menu
- Cart
  - Add / Remove item
  - Place Order
- My Orders
  - View / Cancel / Track

### 🍽️ Restaurant
- Register and Login
- Dashboard
- Manage Orders
  - Update order status
- Manage Menu

### 🚴 Delivery Partner
- Register and Login
- Dashboard
- Assigned Orders
  - Verify OTP
  - Deliver orders
- Today's Deliveries

--- 

## 🏛️ Design Patterns Used

| Pattern | Purpose |
|----------|---------|
| Singleton | Ensures a single application configuration instance throughout the application |
| Factory | Creates the appropriate payment strategy using `PaymentFactory` |
| Strategy | Implements different payment methods (e.g., Cash, UPI) without changing checkout logic |
| Facade | Encapsulates the complete checkout process, including order creation, discount application, payment, and delivery assignment |
| State | Manages order state transitions such as **PLACED → PREPARING → READY → OUT_FOR_DELIVERY → DELIVERED/CANCELLED** |

---

# 👥 User Roles

| Role | Responsibilities |
|------|------------------|
| Admin | Manage users, restaurants and delivery partners |
| Customer | Browse menu, order food and make payment |
| Restaurant | Prepare and process orders |
| Delivery Partner | Deliver customer orders |

---

# 🔄 Order Flow

```
Customer Login
       │
       ▼
Browse Menu
       │
       ▼
Add Items to Cart
       │
       ▼
Checkout
       │
       ▼
Payment
       │
       ▼
Restaurant Accepts Order
       │
       ▼
Delivery Partner Assigned
       │
       ▼
OTP Verification
       │
       ▼
Order Delivered
```

---

# 📚 Core Concepts Covered

- Classes & Objects
- Inheritance
- Polymorphism
- Abstraction
- Encapsulation
- Interfaces
- Exception Handling
- Collections
- File Handling
- Generics
- Enums
- Streams
- Layered Architecture
- Design Patterns
- Solid Principles

---

# 🚀 Getting Started

### Clone the repository

```bash
git clone <repository-url>
```

### Open Project

Open the project in your preferred IDE.

Examples:
- IntelliJ IDEA
- Eclipse
- VS Code

### Run

Execute

```
Main.java
```

---
## 🔑 Default Login Credentials

Use the following credentials to log in with different roles in the application.

| Role | Email | Password |
|------|-------|----------|
| **Admin** | `admin@gmail.com` | `123456` |
| **Restaurant** | `lapinoz@gmail.com` | `123456` |
| **Customer** | `neha@gmail.com` | `123456` |
| **Delivery Partner** | `deliveryboy1@gmail.com` | `123456` |
| **Delivery Partner** | `deliveryboy2@gmail.com` | `123456` |

---

# 📸 Sample Workflow

```
Customer Login

↓

View Menu

↓

Add Pizza x2

↓

View Cart

↓

Checkout

↓

Payment Successful

↓

Restaurant Accepts Order

↓

Delivery Partner Assigned

↓

OTP Verification

↓

Order Delivered

```

---

# 💡 Future Enhancements

- Database Integration (MySQL/PostgreSQL)
- Spring Boot Migration
- REST APIs
- JWT Authentication
- Multiple Restaurant Branches
- Ratings & Reviews
- Coupon System
- Online Payment Gateway
- Email Notifications
- SMS Notifications

---

# ⭐ If you like this project

Give it a ⭐ on GitHub and feel free to contribute or suggest improvements.
