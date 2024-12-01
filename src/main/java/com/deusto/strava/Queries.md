
# JSON Examples for API Testing

---

## 1. Register
**Endpoint:** `POST /api/auth/user`

**Headers:**  
`Content-Type: application/json`

**Request Body:**
```json
{
  "email": "user@example.com",
  "name": "John Doe",
  "birthDate": "1990-01-01",
  "weight": 75.5,
  "height": 180.0
}
```

---

## 2. Login
**Endpoint:** `POST /api/auth/login`

**Headers:**  
`Content-Type: application/json`

**Request Body:**
```json
{
  "email": "user@example.com"
}
```

---

## 3. Logout
**Endpoint:** `POST /api/auth/logout`

**Headers:**  
`Content-Type: application/json`

**Request Body:**
```json
"9f8b7e2d"
```

---

## 4. Create Training Session
**Endpoint:** `POST /api/sessions`

**Headers:**  
`Content-Type: application/json`  
`token: <user_token>`

**Request Body:**
```json
{
  "sport": "Running",
  "distance": 5.0,
  "startDate": "2024-12-01T09:00:00.000+00:00",
  "duration": 45.0
}
```

**Response Example:**
```json
"Training session created successfully with ID: session_1"
```

---

## 5. Get Training Sessions
**Endpoint:** `GET /api/sessions`

**Headers:**  
`Content-Type: application/json`  
`token: <user_token>`

**Query Parameters (Optional):**
- `startDate`: Start date of the range (e.g., `2024-12-01`)
- `endDate`: End date of the range (e.g., `2024-12-31`)

**Example Request (No Filters):**
```
GET /api/sessions
```

**Example Request (With Filters):**
```
GET /api/sessions?startDate=2024-12-01&endDate=2024-12-31
```

**Response Example:**
```json
[
  {
    "id": "session_1",
    "sport": "Running",
    "distance": 5.0,
    "startDate": "2024-12-01T09:00:00.000+00:00",
    "duration": 60.0
  },
  {
    "id": "session_2",
    "sport": "Cycling",
    "distance": 20.0,
    "startDate": "2024-12-02T10:00:00.000+00:00",
    "duration": 90.0
  }
]
```