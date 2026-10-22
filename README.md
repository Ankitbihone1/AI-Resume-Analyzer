# 🚀 AI Resume Analyzer – AI-Powered Resume Evaluation System

> A complete, production-ready Full Stack web application built with **Java Spring Boot 3**, **React 18 (Vite)**, **MySQL 8**, **Apache PDFBox**, and **AI Large Language Models (Google Gemini 1.5 Flash / OpenAI GPT-4o-mini)**.

Designed specifically for a **Java Full Stack Developer Portfolio** and fresher/junior software engineer interviews.

---

## 🏗️ System Architecture

```text
       +-------------------------------------------------------------+
       |               React 18 + Vite Frontend (SPA)                |
       |  - React Router v6    - Tailwind CSS UI      - Recharts     |
       |  - Axios Interceptors - Lucide Icons         - jsPDF Export |
       +------------------------------+------------------------------+
                                      |  REST APIs / JSON / Multipart
                                      v
       +-------------------------------------------------------------+
       |             Java Spring Boot 3.2.5 Backend                  |
       |  - Spring Security 6 (Stateless JWT Filter)                 |
       |  - Spring Data JPA + Hibernate ORM                          |
       |  - Apache PDFBox (Resume text & structure extraction)       |
       |  - AI Service Engine (Google Gemini 1.5 & OpenAI fallback)  |
       |  - Global Exception Handling & Bean Validation              |
       +-------------------+--------------------+--------------------+
                           |                    |
                           v                    v
         +--------------------+     +------------------------+
         |   MySQL 8.0+ DB    |     |  AI LLM Provider API   |
         |  - users           |     |  - Google Gemini Flash |
         |  - resume_analyses |     |  - OpenAI GPT-4o-mini  |
         +--------------------+     +------------------------+
```

---

## 🌟 Key Application Features

1. **User Authentication & Authorization**:
   - Registration and login with BCrypt password hashing.
   - Stateless JWT tokens (HMAC-SHA256) with Bearer token authentication.
   - User-scoped data isolation (users can only access their own resume reports).

2. **Resume PDF Text Processing**:
   - Apache PDFBox text extraction from uploaded PDF resumes.
   - Scanned PDF detection (flags image-only or unreadable documents).
   - Server-side MIME validation and file size limit enforcement (10 MB).

3. **AI Matching & ATS Scoring**:
   - Compares candidate qualifications directly against job requirements.
   - Estimated match score (0–100) based on algorithmic and semantic evaluation.
   - Matching skills vs. missing skill gaps breakdown.
   - High-impact ATS keyword analysis (keywords found vs. keywords missing).
   - Bullet point enhancer generating quantified, action-oriented draft rewrites.

4. **Interactive Visualizations & Export**:
   - Circular SVG match score gauge.
   - Recharts competency breakdown across core full-stack domains.
   - One-click downloadable PDF evaluation reports using `jsPDF` and `html2canvas`.

5. **Historical Tracking**:
   - Searchable and filterable analysis history scoped to the logged-in candidate.
   - Ability to reopen and delete past reports.

---

## 📡 Complete REST API Documentation

| Method | Endpoint | Access | Description | Request Body / Params |
|:---:|---|:---:|---|---|
| `GET` | `/api/health` | Public | Heartbeat uptime status probe | None |
| `POST` | `/api/auth/register` | Public | Register candidate account | `RegisterRequest` (JSON) |
| `POST` | `/api/auth/login` | Public | Authenticate and obtain JWT | `LoginRequest` (JSON) |
| `GET` | `/api/users/me` | Protected | Get authenticated profile | None (Bearer Token) |
| `PUT` | `/api/users/me` | Protected | Update name and target role | `UpdateProfileRequest` (JSON) |
| `PUT` | `/api/users/me/password` | Protected | Change password | `PasswordChangeRequest` (JSON) |
| `POST` | `/api/analyses` | Protected | Upload PDF & run AI analysis | Multipart (`file`, `targetRole`, `jobDescription`) |
| `GET` | `/api/analyses` | Protected | List user's analysis history | None (Bearer Token) |
| `GET` | `/api/analyses/{id}` | Protected | Get specific evaluation report | None (Bearer Token) |
| `DELETE` | `/api/analyses/{id}` | Protected | Delete evaluation report | None (Bearer Token) |

---

## 🛠️ Prerequisites

Verify the following tools are installed on your Windows machine:
1. **Java Development Kit (JDK) 17 or 21**:
   ```powershell
   java -version
   ```
2. **Apache Maven 3.8+** (or use the provided `mvnw.cmd`):
   ```powershell
   mvn -v
   ```
3. **Node.js 18+ and npm 9+**:
   ```powershell
   node -v
   npm -v
   ```
4. **MySQL Server 8.0+** running on port `3306`:
   ```powershell
   mysql -u root -p
   ```
5. **AI API Key** *(Optional - algorithmic fallback included)*:
   - Google Gemini API key from [Google AI Studio](https://aistudio.google.com/)

---

## ⚙️ Step-by-Step Setup Guide (Windows PowerShell)

### Step 1: Initialize MySQL Database
```powershell
# Execute DDL schema:
Get-Content "d:\AI Resume Anylyzer Project\database\schema.sql" | mysql -u root -p

# (Optional) Seed demo candidate:
Get-Content "d:\AI Resume Anylyzer Project\database\sample-data.sql" | mysql -u root -p
```

### Step 2: Configure Environment Variables
In the `backend/` directory, copy `.env.example` to `.env`:
```powershell
cd "d:\AI Resume Anylyzer Project\backend"
Copy-Item .env.example .env
```
Open `.env` and set your MySQL password and Gemini API key:
```ini
DB_PASSWORD=your_mysql_password
GEMINI_API_KEY=your_gemini_api_key_here
```

### Step 3: Run Backend Service
```powershell
cd "d:\AI Resume Anylyzer Project\backend"

# Run tests:
mvn test

# Start Spring Boot:
mvn spring-boot:run
```
Backend API will be live at `http://localhost:8080/api`.

### Step 4: Run Frontend Development Server
In a second PowerShell window:
```powershell
cd "d:\AI Resume Anylyzer Project\frontend"
npm install
npm run dev
```
Frontend will be available at **`http://localhost:5173`**.

---

## 🧪 Testing Credentials for Interview Demos

* **Demo Email**: `demo.developer@example.com`
* **Demo Password**: `Password123!`
* Or click the **"Demo Sign In (Alex Morgan)"** button on the `/login` page for one-click access!

---

