# Smart Panchayat (GramConnect)

- `frontend/` – static HTML/CSS/JS (login, register, dashboard with complaints)
- `backend/`  – Spring Boot 3 REST API (Java 17), BCrypt passwords, H2 by default, MySQL optional

## Run locally
Backend (needs Java 17+ and Maven):
```
cd backend
.\mvnw spring-boot:run        # http://localhost:8080  (H2 file DB, no setup)
```
Frontend (any static server):
```
cd frontend
npx http-server -p 3000    # open http://localhost:3000/login.html
```
`frontend/config.js` points to `http://localhost:8080` on localhost.

**One command (Windows):** double-click `start.bat`. The backend also serves the frontend, so open http://localhost:8080/login.html

MySQL instead of H2: set `DB_URL=jdbc:mysql://localhost:3306/panchayat DB_USER=root DB_PASSWORD=... DB_DRIVER=com.mysql.cj.jdbc.Driver`.

## API
`POST /api/register {name,email,phone,password}` · `POST /api/login {email,password}` ·
`GET/POST /api/info/{notices|schemes|jobs}` · `POST /api/complaints` · `GET /api/complaints?email=` · `GET /api/complaints/stats?email=` · `PUT /api/complaints/{id}/resolve` · `GET /api/health`

## Deploy
1. **Backend** (Vercel cannot run Spring Boot): deploy `backend/` (has a Dockerfile) on Render / Railway / Fly.io.
   Set env `CORS_ORIGINS=https://<your-app>.vercel.app` and, for persistent data, a MySQL/Postgres `DB_*` config.
2. **Frontend on Vercel**: import the GitHub repo, Framework "Other", output directory `frontend` (vercel.json already sets it).
   Then edit `frontend/config.js`, replace `https://YOUR-BACKEND-URL.onrender.com` with the backend URL and push.

## Known limits
Login is demo-level (no JWT/session tokens); the forgot-password page has no email sending yet.
