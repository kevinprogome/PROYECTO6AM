# Despliegue Gratis: Vercel + Render + Supabase

Esta guia deja el proyecto funcionando con:

- Frontend: Vercel
- Backend: Render (Web Service)
- Base de datos: Supabase (PostgreSQL)

## 1) Base de datos en Supabase

1. Crea un proyecto en Supabase.
2. Ve a `Project Settings > Database`.
3. Copia los datos de conexion:
   - Host
   - Port
   - Database
   - User
   - Password
4. Usa la URL JDBC para Spring con SSL:

```txt
jdbc:postgresql://<HOST>:5432/postgres?sslmode=require
```

Nota: en algunos proyectos conviene usar host/puerto del pooler de Supabase para mayor estabilidad.

## 2) Backend en Render

1. Sube el repo a GitHub.
2. En Render: `New > Web Service`.
3. Conecta el repo y selecciona:
   - Root Directory: `backend`
   - Environment: `Java`
   - Build Command: `./mvnw clean package -DskipTests` (o `mvn clean package -DskipTests` si no usas wrapper)
   - Start Command: `java -jar target/*.jar`
4. En `Environment Variables` configura:

```txt
SPRING_PROFILES_ACTIVE=prod
PROD_DB_URL=jdbc:postgresql://<HOST>:5432/postgres?sslmode=require
PROD_DB_USER=<USER>
PROD_DB_PASSWORD=<PASSWORD>
JWT_SECRET=<SECRET_LARGO>
JWT_EXPIRATION_MINUTES=60
CORS_ALLOWED_ORIGINS=https://<tu-frontend>.vercel.app
FRONTEND_REDIRECT_URI=https://<tu-frontend>.vercel.app/login
GOOGLE_CLIENT_ID=<...>
GOOGLE_CLIENT_SECRET=<...>
PROD_MAIL_HOST=<...>
PROD_MAIL_PORT=587
PROD_MAIL_USER=<...>
PROD_MAIL_PASSWORD=<...>
```

5. Despliega y guarda la URL final, por ejemplo:

```txt
https://greenhouse-backend.onrender.com
```

## 3) Frontend en Vercel

1. En Vercel: `Add New > Project`.
2. Conecta el mismo repo.
3. Configura:
   - Root Directory: `frontend`
   - Framework Preset: `Vite`
   - Build Command: `npm run build`
   - Output Directory: `dist`
4. Variables de entorno:

```txt
VITE_API_BASE_URL=https://greenhouse-backend.onrender.com
VITE_AUTH_URL=https://greenhouse-backend.onrender.com/oauth2/authorization/google
```

5. Deploy.

## 4) Ajustes OAuth (Google)

En Google Cloud Console agrega URIs autorizadas:

- Backend callback:

```txt
https://greenhouse-backend.onrender.com/login/oauth2/code/google
```

- Frontend origin:

```txt
https://<tu-frontend>.vercel.app
```

## 5) Verificacion rapida

1. Backend arriba en Render (sin error de JPA).
2. Frontend carga en Vercel.
3. Login Google redirige correctamente.
4. Requests del frontend responden 200 desde la URL de Render.

## 6) Problemas comunes

- `Unable to determine Dialect without JDBC metadata`:
  - Revisa `PROD_DB_URL`, `PROD_DB_USER`, `PROD_DB_PASSWORD`.
  - Confirma que el perfil activo sea `prod`.
- CORS bloqueado:
  - `CORS_ALLOWED_ORIGINS` debe ser exactamente tu dominio de Vercel.
- Backend lento al primer request:
  - Es normal en Render free por spin down de inactividad.
