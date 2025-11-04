# 💊 Sistema de Recordatorio de Medicamentos y Citas Médicas — Entrega 2

**Equipo de desarrollo:**
* Hare Atehortua
* Nicolás Gómez
* Juan Pablo Ovalle
* Yurley Avendaño
* Kevin González
* Dayann Urbina
* Jean Vargas

---

## 🧠 Descripción general
Aplicación de consola desarrollada en **Java (Programación Orientada a Objetos)** que permite registrar usuarios, medicamentos y citas médicas, mostrando recordatorios automáticos en consola.

El propósito del sistema es mejorar el cumplimiento de tratamientos y la asistencia a citas médicas, especialmente entre adultos mayores o pacientes con enfermedades crónicas.

Esta versión corresponde al **Prototipo 2 (Entrega 2)** del proyecto, que incluye las historias de usuario implementadas, criterios de aceptación y el **diagrama UML actualizado**.

---

## 🎯 Objetivos del proyecto
- Implementar principios de **POO** (clases, objetos, herencia, encapsulamiento y polimorfismo).
- Permitir el registro de usuarios con sus datos básicos.
- Registrar medicamentos con nombre, dosis y frecuencia.
- Registrar citas médicas con especialidad, médico, fecha y hora.
- Simular recordatorios automáticos mediante mensajes en consola.
- Mostrar avances del segundo prototipo de la aplicación.

---

## 🗂️ Estructura del proyecto

```plaintext
src/
├── aplicacion/
│   └── Main.java
└── modelo/
    ├── Usuario.java
    ├── Medicamento.java
    ├── CitaMedica.java
    ├── Recordatorio.java
    ├── Programador.java
    └── ServicioNotificaciones.java
.gitignore
README.md
Proyecto POO.iml
```
## ⚙️ Requisitos del sistema
- **Java JDK 17 o superior**
- **IntelliJ IDEA** (Community o Ultimate)
- Sistema operativo Windows, Linux o macOS

## 🚀 Cómo ejecutar el proyecto
1.  **Clonar el repositorio:**
    ```bash
    git clone [https://github.com/Jeanvav/Proyecto-POO](https://github.com/Jeanvav/Proyecto-POO)
    ```
2.  **Abrir el proyecto** con IntelliJ IDEA.
3.  **Compilar el código** y ejecutar la clase `aplicacion.Main`.
4.  **Seguir las instrucciones en consola:**
    * Ingresar los datos del usuario.
    * Registrar medicamentos.
    * Registrar una cita médica (opcional).
    * Visualizar los recordatorios simulados.
## 🧪 Ejemplo de uso en consola

```bash
Bienvenido al sistema de recordatorios médicos
Ingrese su nombre: Juan Pérez
Ingrese su correo: juanperez@mail.com
Ingrese su teléfono: 3214567890

¿Cuántos medicamentos desea registrar?: 1
Nombre del medicamento: Losartán
Dosis: 50 mg
Frecuencia (horas): 12

¿Desea registrar una cita médica? (s/n): s
Especialidad: Cardiología
Nombre del médico: Dr. Gómez
Fecha y hora (yyyy-MM-dd HH:mm): 2025-11-06 18:30

✅ Usuario y datos guardados correctamente.
🔔 Recordatorio: Tomar Losartán
🔔 Recordatorio: Cita médica próxima: Cardiología con Dr. Gómez el 06/11/2025 18:30