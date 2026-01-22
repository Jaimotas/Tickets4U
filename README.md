# Tickets4U

**Tickets4U** es una plataforma para gestión y compra de entradas a eventos, con backend en Spring Boot y frontend Android en Kotlin.

---

## 📋 Descripción

Permite:

- Publicar y gestionar eventos (actuales, destacados e internacionales).  
- Venta de entradas con carrito y pagos.  
- Control de acceso mediante códigos QR.  
- Gestión de usuarios con roles: ADMIN, ORGANIZER, ATTENDEE.  

---

## 🛠️ Tecnologías

**Backend:** Java 17, Spring Boot 4.0.1, H2, JPA/Hibernate, Spring Security, REST, Validation, Google Zxing, Apache Commons CSV, JWT.  

**Frontend:** Kotlin, Arquitectura Android, Corrutinas, Retrofit 2, Material Design, RecyclerView, DrawerLayout, DialogFragment.

---

## 📁 Estructura del Proyecto

### Backend
backend/
├── src/main/java/com/tickets4u/events/
│ ├── BackendApplication.java
│ ├── controllers/
│ ├── models/
│ ├── repositories/
│ └── config/
├── src/main/resources/application.properties
├── pom.xml
└── mvnw

### Frontend
com.grupo5.tickets4u
├── eventos.ui/cart
├── eventos.ui/payment
├── model/
├── MainActivity.kt
├── EventAdapter.kt
└── CrearEventoDialogFragment.kt

---

## 🚀 Instalación y Ejecución

### Backend

cd backend
mvn clean install
mvn spring-boot:run
URL: http://localhost:8080
H2 Console: http://localhost:8080/h2-console
Usuario: sa | Password: (vacío)


Admin por defecto:
Usuario: admin1 | Email: admin@tickets4u.com | Contraseña: admin123 | Rol: ADMIN

Frontend

Abrir en Android Studio.

Configurar URL base en RetrofitClient.kt.

Sincronizar Gradle y reconstruir proyecto.

Ejecutar en emulador o dispositivo.

📚 Endpoints (Backend)

Eventos

GET /api/eventos → Todos los eventos

GET /api/eventos/{id} → Evento por ID

Usuarios

GET /api/usuarios → Todos los usuarios

GET /api/usuarios/{id} → Usuario por ID

📋 Entidades

Evento

{
  "id": Long,
  "idAdmin": Long,
  "nombre": String,
  "descripcion": String,
  "fechaInicio": LocalDateTime,
  "fechaFin": LocalDateTime,
  "ciudad": String,
  "ubicacion": String,
  "direccion": String,
  "aforo": Integer,
  "foto": String,
  "categoria": "ACTUAL | DESTACADO | INTERNACIONAL"
}


Usuario

{
  "id": Long,
  "nombreUsuario": String,
  "email": String,
  "contrasena": String,
  "rol": "ADMIN | ORGANIZER | ATTENDEE"
}

🔒 Seguridad

CORS habilitado para todas las rutas

Spring Security permite /api/** sin autenticación

CSRF deshabilitado para POST, PUT, DELETE

📝 Ejemplo con cURL
curl http://localhost:8080/api/eventos
curl http://localhost:8080/api/usuarios

🐛 Troubleshooting

MySQL no disponible → usar H2 o configurar application.properties.

Puerto 8080 ocupado → cambiar server.port.

📚 Próximas Características

Autenticación JWT

Gestión de pedidos y tickets

Generación de códigos QR

Sistema de descuentos

Notificaciones por email

Check-in con QR

Estadísticas de eventos

👥 Contribuyentes

Desarrollado como parte del proyecto Tickets4U.

📄 Licencia

Código privado.

Si quieres, puedo hacer una **versión todavía más visual y compacta** que funcione como README principal de GitHub, con tablas para endpoints y stack tecnológico, ideal para que se lea rápido. ¿Quieres que haga eso también?
