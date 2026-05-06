# Proyecto: GreenHouse Manager
# Archivo: Dockerfile
# Descripcion: Imagen Docker para frontend Vite.
# Autor: Equipo GreenHouse Manager
# Fecha: 2026-05-06
# Version: 1.0.0

FROM node:20-alpine
WORKDIR /app
COPY package.json package-lock.json ./
RUN npm ci
COPY . .
EXPOSE 5173
CMD ["npm", "run", "dev", "--", "--host", "0.0.0.0"]
