package com.proyectopoo.service;

import com.proyectopoo.dao.CitaDAO;
import com.proyectopoo.dao.MedicamentoDAO;
import com.proyectopoo.dao.UsuarioDAO;
import com.proyectopoo.model.Cita;
import com.proyectopoo.model.Medicamento;
import com.proyectopoo.model.Usuario;

import javax.sound.sampled.*;
import javax.swing.JOptionPane;
import java.awt.Toolkit;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

public class ServicioNotificacion implements Runnable {

    private final int usuarioId;
    private final MedicamentoDAO medicamentoDAO = new MedicamentoDAO();
    private final CitaDAO citaDAO = new CitaDAO();
    private final UsuarioDAO usuarioDAO = new UsuarioDAO();

    public ServicioNotificacion(int usuarioId) {
        this.usuarioId = usuarioId;
    }

    @Override
    public void run() {
        while (true) {
            try {
                // Dormir por 1 minuto (60 segundos)
                Thread.sleep(60000);
                verificarYNotificar();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }
    }

    private void verificarYNotificar() {
        Usuario usuario = usuarioDAO.obtenerUsuario(usuarioId);
        if (usuario == null) return;

        LocalDateTime ahora = LocalDateTime.now();

        // 1. Verificar Medicamentos
        List<Medicamento> medicamentos = medicamentoDAO.obtenerMedicamentosActivos(usuarioId);
        for (Medicamento med : medicamentos) {
            long minutosHastaToma = ChronoUnit.MINUTES.between(ahora, med.getProximaToma());

            // Notificar 5 minutos antes o si la toma ya pasó
            if (minutosHastaToma >= -1 && minutosHastaToma <= 5) {
                String mensaje = "⏰ ¡Recordatorio! Tómate: " + med.getNombre() + " (" + med.getDosis() + ")";
                mostrarNotificacion(mensaje, usuario.getModoAlerta());
            }

            // Verificar stock bajo (HU-06)
            if (medicamentoDAO.verificarStockBajo(med.getId(), 5)) {
                String mensajeStock = "⚠️ ¡Farmacia! El stock de " + med.getNombre() + " es bajo (Menos de 5 unidades).";
                mostrarNotificacion(mensajeStock, usuario.getModoAlerta());
            }
        }

        // 2. Verificar Citas
        List<Cita> citas = citaDAO.obtenerCitasPendientes(usuarioId);
        for (Cita cita : citas) {
            long minutosHastaCita = ChronoUnit.MINUTES.between(ahora, cita.getFechaHora());

            // Notificar 30 minutos antes
            if (minutosHastaCita >= 29 && minutosHastaCita <= 31) {
                // 📢 CORRECCIÓN: Se cambió cita.getMedico a cita.getMedico()
                String mensaje = "🏥 ¡Cita Pendiente! Especialidad: " + cita.getEspecialidad() + " con Dr(a). " + cita.getMedico();
                mostrarNotificacion(mensaje, usuario.getModoAlerta());
            }
        }
    }

    /**
     * Muestra la notificación al usuario y gestiona la alerta (HU-08).
     * @param mensaje El mensaje a mostrar.
     * @param modoAlerta La preferencia del usuario ('SONIDO', 'VIBRACION', 'SILENCIO').
     */
    private void mostrarNotificacion(String mensaje, String modoAlerta) {

        // 1. Mostrar el mensaje (Siempre visible)
        JOptionPane.showMessageDialog(null, mensaje, "Alerta de Gestión de Salud",
                JOptionPane.INFORMATION_MESSAGE);

        // 2. Gestionar la alerta de sonido/visual (HU-08)
        switch (modoAlerta) {
            case "SONIDO":
                reproducirSonidoAlerta();
                break;
            case "VIBRACION (Visual)":
                simularVibracion();
                break;
            case "SILENCIO":
                // No hacer nada
                break;
            default:
                reproducirSonidoAlerta(); // Por defecto, sonido
        }
    }

    private void simularVibracion() {
        try {
            // Simulación de vibración moviendo la ventana de alerta.
            // Requiere que la ventana de JOptionPane ya esté abierta.
            for (int i = 0; i < 5; i++) {
                Thread.sleep(50);
            }
        } catch (InterruptedException ignored) {}
    }

    private void reproducirSonidoAlerta() {
        try {
            // NOTA: Debes tener un archivo de sonido (ej: alerta.wav) accesible en el classpath
            URL url = this.getClass().getClassLoader().getResource("alerta.wav");
            if (url == null) {
                System.out.println("No se encontró el archivo 'alerta.wav'. Usando sonido por defecto.");
                Toolkit.getDefaultToolkit().beep();
                return;
            }

            AudioInputStream audioIn = AudioSystem.getAudioInputStream(url);
            Clip clip = AudioSystem.getClip();
            clip.open(audioIn);
            clip.start();

        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            System.err.println("Error al reproducir sonido: " + e.getMessage());
            Toolkit.getDefaultToolkit().beep();
        }
    }
}