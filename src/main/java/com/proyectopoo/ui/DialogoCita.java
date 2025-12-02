package com.proyectopoo.ui;

import com.proyectopoo.dao.CitaDAO;
import com.proyectopoo.model.Cita;
import javax.swing.*;
import java.awt.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class DialogoCita extends JDialog {

    // Componentes de entrada
    private JTextField txtEspecialidad;
    private JTextField txtMedico;
    // NUEVO CAMPO: Centro Médico
    private JTextField txtCentroMedico;
    private JTextField txtFechaHora;
    private JButton btnGuardar;

    // Datos de la lógica
    private final int usuarioId;
    private final VentanaPrincipal padre;
    private final CitaDAO citaDAO = new CitaDAO();

    private final DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    // Definiciones de Estilo (Consistencia UI/UX)
    private static final Font LABEL_FONT = new Font("Arial", Font.BOLD, 18);
    private static final Font INPUT_FONT = new Font("Arial", Font.PLAIN, 18);
    private static final Font BUTTON_FONT = new Font("Arial", Font.BOLD, 22);
    private static final Color PRIMARY_BLUE = new Color(0x007AFF);

    public DialogoCita(VentanaPrincipal parent, int usuarioId) {
        super(parent, "Registrar Nueva Cita", true);
        this.usuarioId = usuarioId;
        this.padre = parent;

        setSize(550, 520); // Tamaño ajustado para el nuevo campo
        setLocationRelativeTo(parent);
        setResizable(false);

        initComponents();
    }

    private void initComponents() {
        JPanel mainPanel = new JPanel(new BorderLayout(20, 20));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(25, 30, 25, 30));

        // 1. Panel de Formulario usando GridBagLayout para control de espaciado
        JPanel formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(15, 10, 15, 10); // Espaciado vertical aumentado

        // Inicialización de campos y aplicación de fuente
        txtEspecialidad = new JTextField(20);
        txtMedico = new JTextField(20);
        // Inicialización del nuevo campo
        txtCentroMedico = new JTextField(20);

        txtEspecialidad.setFont(INPUT_FONT);
        txtMedico.setFont(INPUT_FONT);
        // Estilo al nuevo campo
        txtCentroMedico.setFont(INPUT_FONT);

        // Valor inicial para la próxima cita: en una semana
        LocalDateTime proximaSemana = LocalDateTime.now().plusWeeks(1).withSecond(0).withNano(0);
        txtFechaHora = new JTextField(proximaSemana.format(inputFormatter), 20);
        txtFechaHora.setFont(INPUT_FONT);

        // --- Construcción del Formulario ---
        // Se establece weightx = 1.0 para la columna 1 (gridx=1) para garantizar la expansión (CORRECCIÓN TAMAÑO)

        // 1. Especialidad (Fila 0)
        gbc.gridx = 0; gbc.gridy = 0; gbc.weightx = 0.0; gbc.fill = GridBagConstraints.NONE; formPanel.add(createLabel("Especialidad:"), gbc);
        gbc.gridx = 1; gbc.gridy = 0; gbc.weightx = 1.0; gbc.fill = GridBagConstraints.HORIZONTAL; formPanel.add(txtEspecialidad, gbc);

        // 2. Médico (Fila 1)
        gbc.gridx = 0; gbc.gridy = 1; gbc.weightx = 0.0; gbc.fill = GridBagConstraints.NONE; formPanel.add(createLabel("Médico (Opcional):"), gbc);
        gbc.gridx = 1; gbc.gridy = 1; gbc.weightx = 1.0; gbc.fill = GridBagConstraints.HORIZONTAL; formPanel.add(txtMedico, gbc);

        // 3. CENTRO MÉDICO (Fila 2)
        gbc.gridx = 0; gbc.gridy = 2; gbc.weightx = 0.0; gbc.fill = GridBagConstraints.NONE; formPanel.add(createLabel("Centro Médico:"), gbc);
        gbc.gridx = 1; gbc.gridy = 2; gbc.weightx = 1.0; gbc.fill = GridBagConstraints.HORIZONTAL; formPanel.add(txtCentroMedico, gbc);

        // 4. Fecha/Hora (Fila 3)
        gbc.gridx = 0; gbc.gridy = 3; gbc.weightx = 0.0; gbc.fill = GridBagConstraints.NONE; formPanel.add(createLabel("Fecha/Hora (YYYY-MM-DD HH:MM):"), gbc);
        gbc.gridx = 1; gbc.gridy = 3; gbc.weightx = 1.0; gbc.fill = GridBagConstraints.HORIZONTAL; formPanel.add(txtFechaHora, gbc);

        mainPanel.add(formPanel, BorderLayout.CENTER);

        // 2. Botonera
        btnGuardar = new JButton("GUARDAR CITA");
        btnGuardar.setFont(BUTTON_FONT);
        btnGuardar.setPreferredSize(new Dimension(300, 65)); // Botón grande
        btnGuardar.setBackground(PRIMARY_BLUE);
        btnGuardar.setForeground(Color.WHITE);
        btnGuardar.addActionListener(e -> guardarCita());

        JPanel southPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        southPanel.add(btnGuardar);
        mainPanel.add(southPanel, BorderLayout.SOUTH);

        add(mainPanel);
    }

    // Método auxiliar para crear etiquetas con el estilo consistente
    private JLabel createLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(LABEL_FONT);
        return label;
    }

    private void guardarCita() {
        try {
            String especialidad = txtEspecialidad.getText().trim();
            String medico = txtMedico.getText().trim(); // Puede ser vacío
            // Obtener valor del nuevo campo
            String centroMedico = txtCentroMedico.getText().trim();

            if (especialidad.isEmpty() || centroMedico.isEmpty()) {
                throw new IllegalArgumentException("La Especialidad y el Centro Médico son obligatorios.");
            }

            String fechaHoraStr = txtFechaHora.getText().trim();
            if (fechaHoraStr.isEmpty()) {
                throw new IllegalArgumentException("La Fecha/Hora es obligatoria.");
            }

            // Conversión de String a LocalDateTime
            LocalDateTime fechaHora = LocalDateTime.parse(fechaHoraStr, inputFormatter);

            if (fechaHora.isBefore(LocalDateTime.now())) {
                throw new IllegalArgumentException("La fecha de la cita debe ser futura.");
            }

            // 1. Crear el objeto Cita (ASUME que el constructor de Cita.java ha sido actualizado)
            Cita nuevaCita = new Cita(0, usuarioId, especialidad, medico, centroMedico, fechaHora);

            // 2. Insertar en la DB (ASUME que CitaDAO.java ha sido actualizado)
            citaDAO.insertarCita(nuevaCita);

            // 3. Notificar y actualizar UI
            JOptionPane.showMessageDialog(this, "Cita registrada exitosamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
            padre.cargarDatosCitas();
            this.dispose();

        } catch (DateTimeParseException e) {
            JOptionPane.showMessageDialog(this, "Formato de fecha/hora incorrecto. Use YYYY-MM-DD HH:MM.", "Error de formato", JOptionPane.ERROR_MESSAGE);
        } catch (IllegalArgumentException e) {
            JOptionPane.showMessageDialog(this, "Error de validación: " + e.getMessage(), "Advertencia", JOptionPane.WARNING_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al guardar la cita.", "Error Fatal", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }
}