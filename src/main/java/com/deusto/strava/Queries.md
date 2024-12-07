# API Testing Instructions

## JSON Examples for API Testing

### 1. Register
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
  "height": 180.0,
  "password": "password",
  "service": "Google"
}
```

### 2. Login
**Endpoint:** `POST /api/auth/login`

**Headers:**  
`Content-Type: application/json`

**Request Body:**
```json
{
  "email": "user@example.com",
  "password": "password",
  "service": "Google"
}
```

### 3. Logout
**Endpoint:** `POST /api/auth/logout`

**Headers:**  
`Content-Type: application/json`

**Request Body:**
```json
"9f8b7e2d"
```

### 4. Create Training Session
**Endpoint:** `POST /api/sessions`

**Headers:**  
`Content-Type: application/json`  
`token: <user_token>`

**Request Body:**
```json
{
  "sport": "Running",
  "distance": 5.0,
  "startDate": "2024-12-01T10:00:00.000+00:00",
  "duration": 30.0
}
```

### 5. Get Training Sessions
**Endpoint:** `GET /api/sessions`

**Headers:**  
`Content-Type: application/json`  
`token: <user_token>`

**Query Parameters (Optional):**
- `startDate`: Start date for filtering sessions (e.g., `2024-12-01T00:00:00.000+00:00`)
- `endDate`: End date for filtering sessions (e.g., `2024-12-31T23:59:59.999+00:00`)

### 6. Create Challenge
**Endpoint:** `POST /api/challenges`

**Headers:**  
`Content-Type: application/json`  
`token: <user_token>`

**Request Body:**
```json
{
  "name": "Winter Marathon",
  "sport": "Running",
  "targetDistance": 42.2,
  "targetTime": 360.0,
  "startDate": "2024-12-01T00:00:00.000+00:00",
  "endDate": "2024-12-31T23:59:59.999+00:00"
}
```

### 7. Get All Active Challenges
**Endpoint:** `GET /api/challenges/active`

**Headers:**  
`Content-Type: application/json`

**Response Example:**
```json
[
  {
    "id": "challenge_1",
    "creatorName": "John Doe",
    "name": "Winter Marathon",
    "sport": "Running",
    "targetDistance": 42.2,
    "targetTime": 360.0,
    "startDate": "2024-12-01T00:00:00.000+00:00",
    "endDate": "2024-12-31T23:59:59.999+00:00"
  }
]
```

### 8. Get My Active Challenges
**Endpoint:** `GET /api/challenges/my-active`

**Headers:**  
`Content-Type: application/json`  
`token: <user_token>`

**Response Example:**
```json
[
  {
    "id": "challenge_2",
    "creatorName": "John Doe",
    "name": "Spring Cycling",
    "sport": "Cycling",
    "targetDistance": 100.0,
    "targetTime": 600.0,
    "startDate": "2024-12-01T00:00:00.000+00:00",
    "endDate": "2025-01-15T23:59:59.999+00:00"
  }
]
```

### 9. Accept Challenge
**Endpoint:** `POST /api/challenges/enrollment?challengeId=challenge_n `

**Headers:**  
`Content-Type: application/json`  
`token: <user_token>`

**Response Example:**
```json
"Challenge accepted successfully: Winter Marathon"
```

### 10. Query My Challenges
**Endpoint:** `GET /api/challenges/my`

**Headers:**  
`Content-Type: application/json`  
`token: <user_token>`

**Response Example:**
```json
[
  {
    "id": "challenge_1",
    "creatorName": "John Doe",
    "name": "Winter Marathon",
    "sport": "Running",
    "targetDistance": 42.2,
    "targetTime": 360.0,
    "startDate": "2024-12-01T00:00:00.000+00:00",
    "endDate": "2024-12-31T23:59:59.999+00:00"
  }
]
```

### 10. Query Challenge Progress
**Endpoint:** `GET /api/challenges/progress?challengeId=challenge_n`

**Headers:**  
`Content-Type: application/json`  
`token: <user_token>`

**Response Example:**
```json
{
  "progress": "Progress: 50.00% of distance, 30.00% of time"
}

```