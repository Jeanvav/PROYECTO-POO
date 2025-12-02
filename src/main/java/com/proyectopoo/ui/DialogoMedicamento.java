package com.proyectopoo.ui;

import com.proyectopoo.dao.MedicamentoDAO;
import com.proyectopoo.model.Medicamento;
import javax.swing.*;
import java.awt.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class DialogoMedicamento extends JDialog {

    private JTextField txtNombre;
    private JTextField txtDosis;
    private JSpinner spinFrecuencia;
    private JTextField txtStock;
    private JTextField txtProximaToma;
    private JButton btnGuardar;

    private final int usuarioId;
    private final VentanaPrincipal padre;
    private final MedicamentoDAO medicamentoDAO = new MedicamentoDAO();

    private final DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    // Definiciones de Estilo
    private static final Font LABEL_FONT = new Font("Arial", Font.BOLD, 18);
    private static final Font INPUT_FONT = new Font("Arial", Font.PLAIN, 18);
    private static final Font BUTTON_FONT = new Font("Arial", Font.BOLD, 22);
    private static final Color PRIMARY_BLUE = new Color(0x007AFF);


    public DialogoMedicamento(VentanaPrincipal parent, int usuarioId) {
        // CORREGIDO: Título del diálogo (sin emoji)
        super(parent, "Registrar Nuevo Tratamiento", true);
        this.usuarioId = usuarioId;
        this.padre = parent;

        setSize(600, 550);
        setLocationRelativeTo(parent);
        setResizable(false);

        initComponents();
    }

    private void initComponents() {
        JPanel mainPanel = new JPanel(new BorderLayout(20, 20));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(25, 30, 25, 30));

        JPanel formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(12, 10, 12, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;

        // Inicialización de campos
        txtNombre = new JTextField(20);
        txtDosis = new JTextField(20);
        txtStock = new JTextField(20);

        // Spinner
        SpinnerNumberModel model = new SpinnerNumberModel(8, 1, 24, 1);
        spinFrecuencia = new JSpinner(model);
        JSpinner.DefaultEditor editor = (JSpinner.DefaultEditor) spinFrecuencia.getEditor();
        editor.getTextField().setFont(INPUT_FONT);

        // Próxima toma inicial
        LocalDateTime manana8am = LocalDateTime.now().plusDays(1).withHour(8).withMinute(0).withSecond(0).withNano(0);
        txtProximaToma = new JTextField(manana8am.format(inputFormatter), 20);

        // Aplicar la fuente grande a los campos de texto
        txtNombre.setFont(INPUT_FONT);
        txtDosis.setFont(INPUT_FONT);
        txtStock.setFont(INPUT_FONT);
        txtProximaToma.setFont(INPUT_FONT);


        // --- Construcción del Formulario ---

        // 1. Nombre
        gbc.gridx = 0; gbc.gridy = 0; formPanel.add(createLabel("Nombre del Medicamento:"), gbc);
        gbc.gridx = 1; gbc.gridy = 0; formPanel.add(txtNombre, gbc);

        // 2. Dosis
        gbc.gridx = 0; gbc.gridy = 1; formPanel.add(createLabel("Dosis (Ej. 500mg, 1 Pastilla):"), gbc);
        gbc.gridx = 1; gbc.gridy = 1; formPanel.add(txtDosis, gbc);

        // 3. Frecuencia
        gbc.gridx = 0; gbc.gridy = 2; formPanel.add(createLabel("Frecuencia (horas):"), gbc);
        gbc.gridx = 1; gbc.gridy = 2; formPanel.add(spinFrecuencia, gbc);

        // 4. Stock
        gbc.gridx = 0; gbc.gridy = 3; formPanel.add(createLabel("Stock Inicial:"), gbc);
        gbc.gridx = 1; gbc.gridy = 3; formPanel.add(txtStock, gbc);

        // 5. Próxima Toma
        gbc.gridx = 0; gbc.gridy = 4; formPanel.add(createLabel("Próxima Toma (YYYY-MM-DD HH:MM):"), gbc);
        gbc.gridx = 1; gbc.gridy = 4; formPanel.add(txtProximaToma, gbc);

        mainPanel.add(formPanel, BorderLayout.CENTER);

        // 2. Botonera (Estilo iOS Primario)
        // CORREGIDO: Botón de guardar (sin emoji)
        btnGuardar = new JButton("GUARDAR Y ACTIVAR");
        btnGuardar.setFont(BUTTON_FONT);
        btnGuardar.setPreferredSize(new Dimension(300, 65));
        btnGuardar.setBackground(PRIMARY_BLUE);
        btnGuardar.setForeground(Color.WHITE);
        btnGuardar.addActionListener(e -> guardarMedicamento());

        JPanel southPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        southPanel.add(btnGuardar);
        mainPanel.add(southPanel, BorderLayout.SOUTH);

        add(mainPanel);
    }

    private JLabel createLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(LABEL_FONT);
        return label;
    }

    private void guardarMedicamento() {
        try {
            String nombre = txtNombre.getText().trim();
            String dosis = txtDosis.getText().trim();

            if (nombre.isEmpty() || dosis.isEmpty()) {
                throw new IllegalArgumentException("El Nombre y la Dosis no pueden estar vacíos.");
            }

            int stock;
            int frecuencia;
            try {
                stock = Integer.parseInt(txtStock.getText().trim());
            } catch (NumberFormatException e) {
                throw new NumberFormatException("El Stock debe ser un número entero válido.");
            }
            frecuencia = (int) spinFrecuencia.getValue();


            if (stock < 0) {
                throw new IllegalArgumentException("El Stock no puede ser negativo.");
            }
            if (frecuencia <= 0) {
                throw new IllegalArgumentException("La Frecuencia debe ser al menos 1 hora.");
            }

            LocalDateTime proximaToma = LocalDateTime.parse(txtProximaToma.getText().trim(), inputFormatter);

            Medicamento nuevoMedicamento = new Medicamento(
                    0,
                    usuarioId,
                    nombre,
                    dosis,
                    frecuencia,
                    stock,
                    proximaToma
            );

            medicamentoDAO.insertarMedicamento(nuevoMedicamento);

            JOptionPane.showMessageDialog(this, "Tratamiento registrado exitosamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
            padre.cargarDatosMedicamentos();
            this.dispose();

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Error de formato numérico: " + e.getMessage(), "Error de formato", JOptionPane.ERROR_MESSAGE);
        } catch (DateTimeParseException e) {
            JOptionPane.showMessageDialog(this, "Formato de fecha/hora incorrecto. Use YYYY-MM-DD HH:MM.", "Error de formato", JOptionPane.ERROR_MESSAGE);
        } catch (IllegalArgumentException e) {
            JOptionPane.showMessageDialog(this, "Error de validación: " + e.getMessage(), "Advertencia", JOptionPane.WARNING_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al guardar el medicamento: " + e.getMessage(), "Error Fatal", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }
}