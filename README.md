# Smart Academic Infrastructure

A unified, premium-themed academic administration and student self-service infrastructure. This repository consolidates the central executive portal dashboard alongside all subsystems into a single cohesive monorepo.

---

## 🏫 Architecture Overview

The system is split into a modern Vue.js frontend portal that embeds the individual Spring Boot applications inside themed iframes:

1. **Executive Portal Dashboard (`smart-academic-portal`)**: 
   - Built with Vue.js & Vite using custom HSL/CSS variables, dark mode glassmorphism, Outfit typography, and micro-animations.
   - Includes real-time statistics cards, early bird and punctuality leaderboards, active student trackers, and live floor status panels.
2. **Academic Attendance & Payroll System (`Skylink-custom-backend`)**:
   - Spring Boot web app managing academic members, departments, shifts, leave requests, and payroll/payslips.
3. **Result Management System (`Result-Management-System`)**:
   - Spring Boot app managing student transcripts, grades, GPA calculations, and academic results.
4. **Student Watch Monitor (`watch-employee`)**:
   - A background student tracking/security monitor and browser agent module.
5. **SmartAI Integration (`SmartAI`)**:
   - Auxiliary AI modules and configuration files.

---

## ⚙️ Configuration & Databases

- **Database**: PostgreSQL
- **Main DB Name**: `skylink_database` (Credentials configured in application properties files).
- **Theme Injection (`smart-academic-theme.css`)**:
  - Dynamically injected into all backend Thymeleaf layout templates.
  - Automatically hides native app sidebars when loaded inside the Vue iframe (`?embedded=true`), ensuring seamless unified navigation.
  - Overrides default styles to match the portal's emerald green and gold premium aesthetic.

---

## 🎲 Seeding Mock Data

To populate the dashboards with realistic simulation records:
1. Boot the applications.
2. Log in as an **Admin** (`admin` / `admin123`).
3. Click the **🎲 Generate Random Data** button in the purple Demo Tools section of the global portal sidebar.
4. This wipes old records and generates:
   - **4 Departments** (CS, Math, Physics, Business Administration)
   - **20 Academic Members** with realistic names, department mappings, and login credentials.
   - **45 Days of Attendance logs** (skipping weekends, including random absents/lates).
   - **3 Months of PAID payslips** and **approved/pending leave histories**.
   - Password for all generated logins is **`student123`**.

---

## 🚀 Port Allocations & How to Run

### Port Mapping
| Service | Port / URL | Key Credentials |
|---|---|---|
| **Executive Portal** (Vue.js) | `http://localhost:5173` | None (Unified Client) |
| **Attendance Backend** (Java) | `http://localhost:8083` | `admin` / `admin123`<br>`hr` / `hr123`<br>Student: `sarah.mitchell` / `student123` |
| **Result Management** (Java) | `http://localhost:8080` | `admin` / `admin123` |

### Starting the Applications
You can start all applications together using the root startup script:
```bash
./start_all.sh
```

Alternatively, start them individually:
1. **Attendance System**:
   ```bash
   cd Skylink-custom-backend
   ./gradlew bootRun
   ```
2. **Result Management**:
   ```bash
   cd Result-Management-System
   ./gradlew bootRun
   ```
3. **Vue.js Portal**:
   ```bash
   cd smart-academic-portal
   npm install
   npm run dev
   ```
