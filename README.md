# Strava Project 🚴‍♂️🏃‍♀️

Welcome to the **Strava Project**! This repository contains a simplified, distributed version of a sports social network inspired by Strava, designed and implemented as part of our coursework to showcase teamwork and software engineering competencies. Our system will enable users to track activities, set personal goals, participate in challenges, and more.

---

## 📋 Project Objectives

This project is designed to enhance the following competencies:

- **GC**: Teamworking
- **SC1**: Software solution design using suitable patterns.
- **SC2**: Documentation of designs via UML diagrams.
- **SC3**: Implementation of software patterns.
- **SC4**: Development of a distributed Java application with ORM for data management.

## 🚀 Project Description

The **Strava Project** replicates a sports social network with functionalities such as activity tracking, challenge creation, and user engagement. Here’s a breakdown of the project phases and deliverables:

### Project Phases and Milestones
1. **Prototype TW1 (10% final grade)**:
    - Initial UML class and sequence diagrams.
    - Central server setup and API validation.

2. **Prototype TW2 (10% final grade)**:
    - Updated UML diagrams and persistence layer.
    - Communication patterns for Google/Facebook authorization.
    - Functional login services.

3. **Prototype TW3 (24% final grade)**:
    - Finalized UML diagrams.
    - GUI implementation using Java Swing or Thymeleaf web client.
    - Fully functional system with client-server integration.

---

## 🌐 Application Domain

Our application models key features of Strava, a platform for sports tracking and social networking. Users can:
- Register via Google/Facebook.
- Log activities (sessions) manually.
- View and filter completed sessions.
- Set up and join challenges.
- Track progress on ongoing challenges.

## 📦 Project Architecture

The project follows a distributed architecture:
- **Central Server**: Handles all functionality and data storage.
- **Client App**: Available in web and mobile formats, with options to connect performance-monitoring sensors.
- **External Integrations**: Supports Google/Facebook logins, workout data from Garmin/Polar/Suunto, and weather data from providers like Klimat.

## 📑 Basic Functionalities

- **Registration/Login**: OAuth via Google/Facebook for secure access.
- **Activity Logging**: Manual session creation with details such as sport type, distance, and duration.
- **Challenges**: Users can create or join challenges based on activity goals.
- **Progress Tracking**: Track completion status of accepted challenges.

---

## 📐 UML Diagrams

This project is documented using UML class diagrams and sequence diagrams to illustrate the architecture, data flow, and component interaction. Each prototype includes updated UML documentation for review.

---

## 👥 Contributors

- Alexander Jauregui Orue
- Asier Corral Bilbao
- Erik Eguskiza Aranda
- Ibon Erdoiza Mezo

## 🛠️ Setup Instructions

1. **Clone the repository and build it**:
   ```bash
   git clone https://github.com/yourusername/strava-project.git
   cd strava-project
   ./gradlew build
   ```
   
2. **Run the application**:
    ```bash
   ./gradlew run
    ```





   



