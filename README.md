# 🚀 AI Resume Analyzer

An AI-powered web application that analyzes resumes against job descriptions, identifies matching and missing skills, and helps candidates improve their resumes for Applicant Tracking Systems (ATS).

## ✨ Features

- **User Authentication:** Register and log in securely using password hashing and JWT-based authentication.
- **Resume Upload:** Upload PDF resumes for text extraction and analysis.
- **Resume–Job Matching:** Compare resume content with job descriptions and generate an estimated match score.
- **Skill Gap Analysis:** Identify matching skills, missing skills, and relevant keywords.
- **Resume Improvement:** Generate suggestions to improve resume bullet points and highlight relevant experience.
- **Interactive Dashboard:** Visualize evaluation results and skill breakdowns.
- **PDF Report Export:** Download resume analysis reports.
- **Analysis History:** View, search, and manage previous resume evaluations.


## 🛠️ Tech Stack

### Frontend
- React 18
- Vite
- React Router
- Tailwind CSS
- Axios
- Recharts
- Lucide Icons
- jsPDF and html2canvas

### Backend
- Java
- Spring Boot 3.2.5
- Spring Security
- Spring Data JPA
- Hibernate
- JWT Authentication
- Apache PDFBox
- Maven

### Database and AI
- MySQL 8.0+
- Google Gemini API (if configured)
- OpenAI API (if configured)

## 🏗️ System Architecture

```text
React + Vite Frontend
        |
        | REST API / JSON / Multipart
        v
Java + Spring Boot Backend
        |
        +------ Spring Security / JWT
        |
        +------ Resume PDF Processing
        |
        +------ Resume Analysis Service
        |              |
        |              v
        |       AI Provider API
        |
        v
     MySQL Database
```

## 📂 Project Structure

```text
AI-Resume-Analyzer/
├── frontend/
│   ├── src/
│   ├── public/
│   ├── package.json
│   └── vite.config.js
│
├── backend/
│   ├── src/
│   ├── pom.xml
│   └── .env.example
│
├── database/
│   ├── schema.sql
│   └── sample-data.sql
│
├── .gitignore
└── README.md
```

The filenames shown above should match the files actually present in the repository.

## 📋 Prerequisites

Install the following tools before running the project:

- Java JDK 17 or 21, compatible with the backend configuration
- Node.js and npm
- MySQL Server 8.0+
- Git
- Google Gemini or OpenAI API key, if the selected AI integration requires it

Check the installations:

```powershell
java -version
node -v
npm -v
```

## ⚙️ Installation and Setup

### 1. Clone the Repository

```bash
git clone https://github.com/YOUR-USERNAME/AI-Resume-Analyzer.git
cd AI-Resume-Analyzer
```

Replace `YOUR-USERNAME` with the actual GitHub username.

### 2. Configure the MySQL Database

Start MySQL and log in:

```bash
mysql -u root -p
```

Create the database:

```sql
CREATE DATABASE resume_analyzer_db;
```

If the repository includes a `database/schema.sql` file, import it using the instructions provided for your operating system. If Hibernate creates the tables automatically, follow the backend's configured database initialization behavior.

### 3. Configure the Backend

Navigate to the backend directory:

```powershell
cd backend
```

Create a local `.env` file from `.env.example` if the application uses that file and has a mechanism to load its variables.

Example configuration:

```ini
DB_HOST=localhost
DB_PORT=3306
DB_NAME=resume_analyzer_db
DB_USERNAME=root
DB_PASSWORD=your_mysql_password
GEMINI_API_KEY=your_gemini_api_key
```

Replace the placeholders with local values. Never commit real passwords, API keys, or signing secrets to GitHub.

**Important:** Spring Boot does not automatically load every `.env` file. Ensure the project is configured to load these variables, or configure the corresponding Spring properties through environment variables or `application.properties`.

Start the backend from the `backend` directory using the Maven Wrapper if it is configured correctly:

```powershell
.\mvnw.cmd spring-boot:run
```

Alternatively, use an installed Maven version:

```powershell
mvn spring-boot:run
```

The backend is expected to run at:

`http://localhost:8080`

API base URL:

`http://localhost:8080/api`

### 4. Configure and Run the Frontend

Open a second terminal:

```powershell
cd frontend
npm install
npm run dev
```

Open the local development URL printed by Vite. By default, it is:

`http://localhost:5173`

Ensure the frontend API proxy or API base URL points to the backend at `http://localhost:8080`.

## 📡 API Endpoints

The following table describes the intended API routes. Confirm that each endpoint is implemented in the backend before relying on this documentation.

| Method | Endpoint | Description |
|---|---|---|
| GET | `/api/health` | Check backend health |
| POST | `/api/auth/register` | Register a user |
| POST | `/api/auth/login` | Authenticate a user |
| GET | `/api/users/me` | Retrieve the current profile |
| PUT | `/api/users/me` | Update the current profile |
| PUT | `/api/users/me/password` | Change the account password |
| POST | `/api/analyses` | Upload a resume and request analysis |
| GET | `/api/analyses` | Retrieve analysis history |
| GET | `/api/analyses/{id}` | Retrieve a specific analysis |
| DELETE | `/api/analyses/{id}` | Delete an analysis |

Protected endpoints require a valid authentication token if JWT security is enabled.

## 🔐 Security Considerations

- Passwords should be stored using a secure password-hashing algorithm.
- Protected APIs should validate authentication and authorization.
- Resume files should be validated for type and size.
- API keys and database credentials must remain outside the public repository.
- Resume files and analysis reports may contain personal information and should be handled securely.

## 🧪 Testing

Run backend tests from the `backend` directory:

```powershell
.\mvnw.cmd test
```

Run frontend checks using the scripts defined in `frontend/package.json`.

Test registration, login, resume uploads, analysis results, and report exports before publishing a working demo.

## 🚀 Future Improvements

- Add more resume formats and extraction options.
- Improve resume–job matching and keyword analysis.
- Expand dashboard analytics.
- Add automated tests for authentication and resume processing.
- Deploy the frontend and backend for public access.

