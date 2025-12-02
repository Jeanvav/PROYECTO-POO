# Sistema de Recordatorio de Medicamentos y Citas Médicas — Entrega Final

**Equipo de desarrollo:**
* Hare Atehortua
* Nicolás Gómez
* Juan Pablo Ovalle
* Yurley Avendaño
* Kevin González
* Jean Vargas

---

##  Descripción general
Aplicación de consola desarrollada en **Java (Programación Orientada a Objetos)** que permite registrar usuarios, medicamentos y citas médicas, mostrando recordatorios automáticos en consola.

El propósito del sistema es mejorar el cumplimiento de tratamientos y la asistencia a citas médicas, especialmente entre adultos mayores o pacientes con enfermedades crónicas.

Esta versión corresponde al **Prototipo 2 (Entrega 2)** del proyecto, que incluye las historias de usuario implementadas, criterios de aceptación y el **diagrama UML actualizado**.

---

##  Objetivos del proyecto
- Implementar principios de **POO** (clases, objetos, herencia, encapsulamiento y polimorfismo).
- Permitir el registro de usuarios con sus datos básicos.
- Registrar medicamentos con nombre, dosis y frecuencia.
- Registrar citas médicas con especialidad, médico, fecha y hora.
- Simular recordatorios automáticos mediante mensajes en consola.
- Mostrar avances del segundo prototipo de la aplicación.

---

## Estructura del proyecto

```plaintext
.
├── src/
│   └── main/
│       └── java/
│           └── com.proyectopoo/
│               ├── app/
│               │   └── Main.java
│               ├── dao/
│               │   ├── CitaDAO.java
│               │   ├── HistorialDAO.java
│               │   ├── MedicamentoDAO.java
│               │   └── UsuarioDAO.java
│               ├── database/
│               │   └── ConexionDB.java
│               ├── model/
│               │   ├── Cita.java
│               │   ├── Medicamento.java
│               │   ├── Recordatorio.java
│               │   ├── RegistroHistorial.java
│               │   └── Usuario.java
│               ├── service/
│               │   └── ServicioNotificacion.java
│               └── ui/
│                   ├── DialogoCita.java
│                   ├── DialogoMedicamento.Sjava
│                   ├── VentanaAcceso.java
│                   └── VentanaPrincipal.java
├── resources/
├── target/
├── .gitignore
├── gestion_salud.db
├── pom.xml
├── PROYECTO_POO.iml
└── README.md
```
##  Requisitos del sistema
- **Java JDK 17 o superior**
- **IntelliJ IDEA** (Community o Ultimate)
- Sistema operativo Windows, Linux o macOS

## ⬇️ 1. Descarga y Uso (Usuarios Finales)

Para utilizar la aplicación sin tener que compilar el código:

### Requisito
* **Java Runtime Environment (JRE):** El usuario debe tener instalado Java 17 o superior para poder ejecutar el archivo `.jar`.

### 📥 Descarga del Ejecutable

1.  Dirígete a la sección de **[Releases/Lanzamientos]** de este repositorio en GitHub.
2.  Descarga el archivo ejecutable más reciente, que será un archivo **`.jar`** (`proyecto-poo.jar`).
3.  Descarga el archivo iniciar.bat
### 🚀 Ejecución

1.  Guarda el archivo `.jar` y el archivo `.bat` en una carpeta de tu PC.
2.  Haz doble clic en el archivo `inicar.bat`.
3.  Si Java está configurado correctamente, la aplicación se iniciará inmediatamente.
4.  La base de datos `gestion_salud.db` se creará automáticamente junto al archivo `.jar` la primera vez que se ejecute.
