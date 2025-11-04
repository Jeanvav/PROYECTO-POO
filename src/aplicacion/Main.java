package aplicacion;

import modelo.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        ServicioNotificaciones servicio = new ServicioNotificaciones();
        Programador programador = new Programador();

        System.out.println("=== Bienvenido al Sistema de Recordatorio de Medicamentos y Citas Médicas ===");

        // 1️⃣ Registro de usuario
        System.out.print("\nIngrese su nombre: ");
        String nombre = sc.nextLine();
        System.out.print("Ingrese su edad: ");
        int edad = sc.nextInt();
        sc.nextLine(); // limpiar buffer
        System.out.print("Ingrese su correo electrónico: ");
        String correo = sc.nextLine();
        System.out.print("Ingrese su número de teléfono: ");
        String telefono = sc.nextLine();

        Usuario usuario = new Usuario(nombre, edad, correo, telefono);
        System.out.println(usuario);

        // 2️⃣ Registro de medicamentos
        List<Medicamento> medicamentos = new ArrayList<>();
        System.out.print("\n¿Cuántos medicamentos desea registrar?: ");
        int n = sc.nextInt();
        sc.nextLine();

        for (int i = 0; i < n; i++) {
            System.out.println("\n--- Medicamento " + (i + 1) + " ---");
            System.out.print("Nombre: ");
            String nom = sc.nextLine();
            System.out.print("Dosis: ");
            String dosis = sc.nextLine();
            System.out.print("Frecuencia: ");
            String frecuencia = sc.nextLine();
            medicamentos.add(new Medicamento(nom, dosis, frecuencia));
        }

        // 3️⃣ Registro de una cita médica
        System.out.print("\n¿Desea registrar una cita médica? (s/n): ");
        char op = sc.next().toLowerCase().charAt(0);
        sc.nextLine();

        CitaMedica cita = null;
        if (op == 's') {
            System.out.print("Especialidad: ");
            String esp = sc.nextLine();
            System.out.print("Nombre del médico: ");
            String med = sc.nextLine();
            cita = new CitaMedica(esp, med, LocalDateTime.now().plusSeconds(10));
        }

        // 4️⃣ Mostrar resumen
        System.out.println("\n--- RESUMEN DE REGISTROS ---");
        medicamentos.forEach(System.out::println);
        if (cita != null) System.out.println(cita);

        // 5️⃣ Simular recordatorios automáticos
        for (Medicamento m : medicamentos) {
            Recordatorio r = new Recordatorio("Tomar " + m.getNombre(), LocalDateTime.now().plusSeconds(5));
            programador.programarRecordatorio(r, servicio, 5);
        }

        if (cita != null) {
            Recordatorio rCita = new Recordatorio("Cita médica próxima: " + cita.toString(), LocalDateTime.now().plusSeconds(10));
            programador.programarRecordatorio(rCita, servicio, 10);
        }

        System.out.println("\nLos recordatorios se mostrarán automáticamente...");
    }
}
