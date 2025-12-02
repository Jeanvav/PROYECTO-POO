package com.proyectopoo.app;

import com.proyectopoo.database.ConexionDB;
import com.proyectopoo.ui.VentanaAcceso;

import com.formdev.flatlaf.FlatDarkLaf;
import com.formdev.flatlaf.FlatLightLaf; // Import necesario para el tema claro

import javax.swing.SwingUtilities;
import javax.swing.UIManager;

public class Main {

    private static boolean temaOscuro = false;

    public static void main(String[] args) {

        // 1. Inicialización de la base de datos (CRÍTICO)
        ConexionDB.inicializarTablas();

        // 2. Establecer el tema visual inicial
        setTema(temaOscuro);

        // 3. Iniciar la interfaz
        SwingUtilities.invokeLater(() -> {
            new VentanaAcceso().setVisible(true);
        });
    }

    /**
     * Establece el Look and Feel de FlatLaf (tema visual).
     */
    public static void setTema(boolean oscuro) {
        try {
            temaOscuro = oscuro;
            if (oscuro) {
                UIManager.setLookAndFeel(new FlatDarkLaf());
            } else {
                // CORREGIDO: Usar FlatLightLaf
                UIManager.setLookAndFeel(new FlatLightLaf());
            }
            // NO se llama a updateComponentTreeUI aquí, se hace desde VentanaPrincipal
        } catch (Exception ex) {
            System.err.println("Fallo al inicializar Look and Feel: " + ex);
        }
    }

    public static boolean isTemaOscuro() {
        return temaOscuro;
    }
}