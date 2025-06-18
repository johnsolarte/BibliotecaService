# Biblioteca Digital API

Una API REST completa para la gestión de una biblioteca digital desarrollada con Spring Boot, que permite administrar libros, autores, usuarios y préstamos.

## 🚀 Características

- **Gestión de Libros**: CRUD completo con búsqueda y filtros
- **Gestión de Autores**: Administración de información de autores
- **Gestión de Usuarios**: Registro y administración de usuarios
- **Sistema de Préstamos**: Control de préstamos y devoluciones
- **Documentación API**: Swagger UI integrado
- **Validaciones**: Validación de datos con Bean Validation
- **Base de Datos**: H2 en memoria para desarrollo
- **Dockerización**: Listo para contenedores
- **Despliegue**: Configurado para Railway

## 🛠️ Tecnologías Utilizadas

- **Java 21**
- **Spring Boot 3.3.0**
- **Spring Data JPA**
- **Spring Boot Validation**
- **H2 Database** (desarrollo)
- **Oracle Database** (producción)
- **Lombok**
- **SpringDoc OpenAPI** (Swagger)
- **Maven**
- **Docker**

## 📋 Prerrequisitos

- Java 21 o superior
- Maven 3.6 o superior
- Docker (opcional)

## 🔧 Instalación y Configuración

### Clonar el Repositorio

```bash
git clone <repository-url>
cd biblioteca-digital-api
```

### Ejecutar con Maven

```bash
# Compilar el proyecto
mvn clean compile

# Ejecutar tests
mvn test

# Ejecutar la aplicación
mvn spring-boot:run
```

### Ejecutar con Docker

```bash
# Construir la imagen
docker build -t biblioteca-api .

# Ejecutar el contenedor
docker run -p 8080:8080 biblioteca-api
```

### Ejecutar con Docker Compose

```bash
docker-compose up -d
```

## 🌐 Endpoints de la API

La API estará disponible en: `http://localhost:8080/api`

### Autores (`/api/authors`)
- `GET /api/authors` - Listar todos los autores
- `GET /api/authors/{id}` - Obtener autor por ID
- `POST /api/authors` - Crear nuevo autor
- `PUT /api/authors/{id}` - Actualizar autor
- `DELETE /api/authors/{id}` - Eliminar autor

### Libros (`/api/books`)
- `GET /api/books` - Listar libros (con paginación)
- `GET /api/books/{id}` - Obtener libro por ID
- `POST /api/books` - Crear nuevo libro
- `PUT /api/books/{id}` - Actualizar libro
- `DELETE /api/books/{id}` - Eliminar libro
- `GET /api/books/search` - Buscar libros por filtros

### Usuarios (`/api/users`)
- `GET /api/users` - Listar todos los usuarios
- `GET /api/users/{id}` - Obtener usuario por ID
- `POST /api/users` - Crear nuevo usuario
- `PUT /api/users/{id}` - Actualizar usuario
- `DELETE /api/users/{id}` - Eliminar usuario

### Préstamos (`/api/loans`)
- `POST /api/loans` - Crear nuevo préstamo
- `PUT /api/loans/{id}/return` - Marcar préstamo como devuelto
- `GET /api/loans/overdue` - Obtener préstamos vencidos
- `GET /api/loans/users/{userId}` - Obtener préstamos por usuario

## 📚 Documentación de la API

Una vez que la aplicación esté ejecutándose, puedes acceder a la documentación interactiva de la API:

- **Swagger UI**: http://localhost:8080/api/swagger-ui.html
- **API Docs**: http://localhost:8080/api/api-docs

## 🗄️ Base de Datos

### Desarrollo (H2)
- **URL**: `jdbc:h2:mem:bibliotecadb`
- **Usuario**: `sa`
- **Contraseña**: `password`
- **Consola H2**: http://localhost:8080/api/h2-console

### Producción (Oracle)
La aplicación está configurada para usar Oracle Database en producción.

## 📊 Datos de Prueba

La aplicación incluye un `DataLoader` que carga automáticamente datos de prueba:

### Autores
- Gabriel García Márquez
- Isabel Allende
- Mario Vargas Llosa

### Libros
- Cien años de soledad
- El amor en los tiempos del cólera
- La casa de los espíritus
- De amor y de sombra
- La ciudad y los perros
- Conversación en La Catedral

### Usuarios
- Juan Pérez
- María González
- Carlos Rodríguez
- Ana Martínez

## 🏗️ Estructura del Proyecto

```
src/
├── main/
│   ├── java/
│   │   └── com/techsolutions/biblioteca/
│   │       ├── config/          # Configuraciones
│   │       ├── controller/      # Controladores REST
│   │       ├── dto/            # Data Transfer Objects
│   │       ├── entity/         # Entidades JPA
│   │       ├── exception/      # Manejo de excepciones
│   │       ├── repository/     # Repositorios JPA
│   │       └── service/        # Lógica de negocio
│   └── resources/
│       └── application.properties
└── test/
    └── java/                   # Tests unitarios
```

## 🔒 Reglas de Negocio

### Préstamos
- Un usuario puede tener máximo 3 préstamos activos
- Los préstamos vencen después de 30 días
- No se puede eliminar un libro con préstamos activos
- No se puede eliminar un usuario con préstamos activos

### Validaciones
- Emails únicos para usuarios
- ISBN único para libros
- Campos obligatorios validados
- Formatos de email validados

## 🚀 Despliegue

### Railway
El proyecto incluye un archivo `railway.json` para despliegue automático en Railway:

```json
{
  "build": {
    "builder": "DOCKERFILE"
  },
  "deploy": {
    "runtime": "V2",
    "numReplicas": 1
  }
}
```

### Variables de Entorno

Para producción, configura las siguientes variables:

```bash
SPRING_PROFILES_ACTIVE=prod
PORT=8080
SPRING_DATASOURCE_URL=jdbc:oracle:thin:@//host:port/service
SPRING_DATASOURCE_USERNAME=your_username
SPRING_DATASOURCE_PASSWORD=your_password
DDL_AUTO=update
```

## 🧪 Testing

Ejecutar tests:

```bash
# Todos los tests
mvn test

# Tests específicos
mvn test -Dtest=LibroServiceTest
```

El proyecto incluye tests unitarios para los servicios principales.

## 📝 Ejemplos de Uso

### Crear un Autor

```bash
curl -X POST http://localhost:8080/api/authors \
  -H "Content-Type: application/json" \
  -d '{
    "nombre": "Julio",
    "apellido": "Cortázar",
    "fechaNacimiento": "1914-08-26",
    "nacionalidad": "Argentino"
  }'
```

### Crear un Libro

```bash
curl -X POST http://localhost:8080/api/books \
  -H "Content-Type: application/json" \
  -d '{
    "titulo": "Rayuela",
    "isbn": "978-84-376-0494-7",
    "anoPublicacion": 1963,
    "genero": "Novela",
    "autorId": 1
  }'
```

### Crear un Préstamo

```bash
curl -X POST http://localhost:8080/api/loans \
  -H "Content-Type: application/json" \
  -d '{
    "usuarioId": 1,
    "libroId": 1
  }'
```

## 🤝 Contribución

1. Fork el proyecto
2. Crea una rama para tu feature (`git checkout -b feature/AmazingFeature`)
3. Commit tus cambios (`git commit -m 'Add some AmazingFeature'`)
4. Push a la rama (`git push origin feature/AmazingFeature`)
5. Abre un Pull Request
