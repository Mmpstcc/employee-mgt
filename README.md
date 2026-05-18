# Employee Management System
### React + Spring Boot + MongoDB — CI/CD with Git, Jenkins, Docker, Kubernetes

---

## Project Structure

```
employee-mgmt/
├── backend/
│   ├── src/main/java/com/example/employee/
│   │   ├── EmployeeApplication.java     ← Spring Boot entry point
│   │   ├── controller/
│   │   │   └── EmployeeController.java  ← REST API endpoints
│   │   ├── model/
│   │   │   └── Employee.java            ← MongoDB document model
│   │   ├── repository/
│   │   │   └── EmployeeRepository.java  ← MongoDB queries
│   │   └── service/
│   │       └── EmployeeService.java     ← Business logic
│   ├── src/main/resources/
│   │   └── application.properties      ← MongoDB config
│   ├── Dockerfile
│   └── pom.xml
│
├── frontend/
│   ├── src/
│   │   ├── App.js                       ← Routing
│   │   ├── services/
│   │   │   └── employeeService.js       ← Axios API calls
│   │   ├── components/
│   │   │   ├── Sidebar.jsx
│   │   │   ├── EmployeeForm.jsx         ← Add/Edit modal
│   │   │   └── Toast.jsx
│   │   ├── pages/
│   │   │   ├── Dashboard.jsx            ← Stats dashboard
│   │   │   └── Employees.jsx            ← CRUD table
│   │   └── assets/App.css
│   ├── public/index.html
│   ├── Dockerfile
│   └── package.json
│
├── k8s/
│   ├── mongodb.yaml                     ← MongoDB StatefulSet
│   ├── backend.yaml                     ← Spring Boot Deployment
│   └── frontend.yaml                    ← React + Nginx Deployment
│
├── docker-compose.yml                   ← Local development
├── Jenkinsfile                          ← CI/CD pipeline
└── README.md
```

---

## REST API Endpoints

| Method | URL | Description |
|--------|-----|-------------|
| GET    | /api/employees | Get all employees |
| GET    | /api/employees?search=John | Search by name |
| GET    | /api/employees?department=HR | Filter by dept |
| GET    | /api/employees/{id} | Get one employee |
| GET    | /api/employees/stats | Dashboard stats |
| POST   | /api/employees | Create employee |
| PUT    | /api/employees/{id} | Update employee |
| DELETE | /api/employees/{id} | Delete employee |

---

## Option A — Run locally with Docker Compose (Easiest)

```bash
# 1. Clone and enter project
git clone https://github.com/YOUR_USERNAME/employee-mgmt.git
cd employee-mgmt

# 2. Start everything (MongoDB + Backend + Frontend)
docker-compose up --build

# 3. Open in browser
# Frontend:  http://localhost:3000
# Backend:   http://localhost:8080/api/employees
# MongoDB:   mongodb://localhost:27017/employeedb
```

---

## Option B — Run without Docker (Development)

```bash
# Terminal 1: Start MongoDB
mongod

# Terminal 2: Start Backend
cd backend
mvn spring-boot:run

# Terminal 3: Start Frontend
cd frontend
npm install
npm start
# Opens http://localhost:3000
```

---

## Option C — CI/CD with Jenkins + Minikube

### Step 1: Start Minikube
```bash
minikube start
eval $(minikube docker-env)
```

### Step 2: Push code to GitHub
```bash
git init && git add . && git commit -m "Initial commit"
git remote add origin https://github.com/YOUR/REPO.git
git push -u origin main
```

### Step 3: Configure Jenkins
1. Open http://localhost:8080 (Jenkins)
2. Install plugins: Git, Docker Pipeline, NodeJS, Kubernetes CLI
3. Add credentials:
   - `dockerhub-creds` → Docker Hub username + password
4. Replace `your-dockerhub-username` in:
   - `Jenkinsfile`
   - `k8s/backend.yaml`
   - `k8s/frontend.yaml`
5. Create Pipeline job → point to your GitHub repo

### Step 4: Run Pipeline & Access
```bash
# After Jenkins deploys successfully:
minikube service frontend-service --url
# Open the printed URL in your browser
```

---

## Useful kubectl commands

```bash
kubectl get all                                        # See everything
kubectl get pods                                       # Check pod status
kubectl logs -l app=emp-backend                        # Backend logs
kubectl logs -l app=emp-frontend                       # Frontend logs
kubectl scale deployment emp-backend --replicas=3      # Scale up
kubectl rollout undo deployment/emp-backend            # Rollback
kubectl exec -it mongodb-0 -- mongosh employeedb       # MongoDB shell
```
