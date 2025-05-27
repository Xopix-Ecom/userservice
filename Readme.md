# 🧩 Xopix - User Service

The **User Service** is a core component of the **Xopix** eCommerce platform. It handles user authentication, registration, profile management, and secure account handling for both customers and admins.

---

## 📦 Service Overview

| Feature              | Description                                                  |
|----------------------|--------------------------------------------------------------|
| 🧑‍💼 User Registration  | New user onboarding with hashed passwords                   |
| 🔐 Login & JWT Auth  | Secure login with token-based authentication                 |
| 🔄 Token Refresh     | Refresh expired tokens without re-login                      |
| 👤 Profile Management| Get, update, or delete user profiles                         |
| 🛡️ Password Control   | Secure password change endpoint                             |
| 👥 Admin Capabilities | Admin can fetch all users with filters                      |

---

## 🚀 API Endpoints

Base URL: `/api/v1/users`

### 1. **Register User**

- `POST /register`
```json
{
  "firstName": "John",
  "lastName": "Doe",
  "email": "john@example.com",
  "password": "StrongPass123",
  "phone": "9876543210"
}
```

### 2. **Login**

- `POST /login`
```json
{
  "email": "john@example.com",
  "password": "StrongPass123"
}
```

### 3. **Refresh Token**

- `POST /refresh-token`
```json
{
  "refreshToken": "..."
}
```

### 4. **Get User Profile**

- `GET /{userId}`
```text
Auth Required
```


### 5. **Update User Profile**

- `PUT /{userId}`
```text
Auth Required
```

### 6. **Change Password**

- `PUT /{userId}`
```json
{
  "oldPassword": "OldPass123",
  "newPassword": "NewPass456"
}

```

### 7. **Delete User**

- `DELETE /{userId}`
```text
Auth Required
```



