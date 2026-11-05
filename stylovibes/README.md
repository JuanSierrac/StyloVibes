# StyloVibes

Plataforma educativa y gratuita de asesoría de imagen (cuerpo, visagismo, colorimetría, estilos).
Java 21 · Spring Boot 3.3 · Spring Security · almacenamiento en memoria · Thymeleaf · Bootstrap 5.

## Requisitos
- JDK 21, Maven 3.9+ (o el Maven integrado de VS Code / IntelliJ)

## Ejecutar
1. Ejecuta `mvn spring-boot:run` (en Windows, `mvnw.cmd spring-boot:run`; también puedes usar F5 en VS Code con *Extension Pack for Java* + *Spring Boot Extension Pack*).
2. Abre http://localhost:8080

## Acceso admin inicial
- Correo: `admin@stylovibes.com`
- Clave: `Admin123*` (cámbiala con `ADMIN_EMAIL` / `ADMIN_PASSWORD` antes del primer arranque)

## Notas
- Los repositorios guardan datos solo en memoria. El contenido de ejemplo se carga con los seeders al arrancar; los cambios realizados durante el uso se pierden al detener o reiniciar la aplicación.
- Los seeders cargan roles, admin, 9 tipologías corporales, 6 rostros, 4 estaciones, 6 estilos, blog y recomendaciones (`stylovibes.seed.enabled=true`).
- Recuperar contraseña: sin SMTP configurado (`MAIL_USER`, `MAIL_PASSWORD`) el enlace se imprime en la consola.
- Lombok: VS Code lo soporta con la extensión Language Support for Java; en IntelliJ activa *Annotation Processing*.
- Contenido en listas: los campos de lista usan `;` como separador; colores como `Nombre:#HEX;Nombre:#HEX`.
