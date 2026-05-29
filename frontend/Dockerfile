# ── Stage 1: Build React app ────────────────────────
FROM node:20-alpine AS builder
WORKDIR /app
COPY package.json .
RUN npm install --silent
COPY . .
RUN npm run build

# ── Stage 2: Serve with Nginx ────────────────────────
FROM nginx:alpine
COPY --from=builder /app/build /usr/share/nginx/html

# Nginx config to support React Router
RUN echo 'server { \
  listen 8086; \
  root /usr/share/nginx/html; \
  index index.html; \
  location / { try_files $uri $uri/ /index.html; } \
  location /api { proxy_pass http://backend-service:8085; } \
}' > /etc/nginx/conf.d/default.conf

EXPOSE 8086
CMD ["nginx", "-g", "daemon off;"]
