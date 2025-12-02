package com.proyectopoo.ui;

import com.proyectopoo.app.Main;
import com.proyectopoo.dao.MedicamentoDAO;
import com.proyectopoo.dao.CitaDAO;
import com.proyectopoo.dao.UsuarioDAO;
import com.proyectopoo.dao.HistorialDAO;
import com.proyectopoo.model.Usuario;
import com.proyectopoo.model.Medicamento;
import com.proyectopoo.model.Cita;
import com.proyectopoo.model.RegistroHistorial;
import com.proyectopoo.service.ServicioNotificacion;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;


public class VentanaPrincipal extends JFrame {

    // Capa de datos y usuario
    private Usuario usuarioActual;
    private final MedicamentoDAO medicamentoDAO = new MedicamentoDAO();
    private final CitaDAO citaDAO = new CitaDAO();
    private final UsuarioDAO usuarioDAO = new UsuarioDAO();
    private final HistorialDAO historialDAO = new HistorialDAO();

    // Componentes de la UI
    private DefaultTableModel modeloMedicamentos;
    private JTable tablaMedicamentos;
    private DefaultTableModel modeloCitas;
    private JTable tablaCitas;

    // Formateador de fecha/hora
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");


    public VentanaPrincipal(Usuario usuario) {
        this.usuarioActual = usuarioDAO.obtenerUsuario(usuario.getId());

        setTitle("Gestión de Salud - Usuario: " + usuarioActual.getNombre());
        setSize(1000, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        crearMenuBar();

        iniciarServicioRecordatorios();

        JTabbedPane tabs = new JTabbedPane();
        tabs.setFont(new Font("Arial", Font.BOLD, 18));

        // Pestañas
        tabs.addTab("MIS MEDICAMENTOS", createMedicamentosPanel());
        tabs.addTab("PRÓXIMAS CITAS", createCitasPanel());
        tabs.addTab("PERFIL Y HISTORIAL", createPerfilHistorialPanel());

        add(tabs);

        cargarDatosMedicamentos();
        cargarDatosCitas();
    }

    // --- BARRA DE MENÚ (HU-10: Temas) ---

    private void crearMenuBar() {
        JMenuBar menuBar = new JMenuBar();

        JMenu menuOpciones = new JMenu("Opciones");
        menuOpciones.setFont(new Font("Arial", Font.PLAIN, 16));

        JMenu subMenuTema = new JMenu("Tema de Interfaz");

        JMenuItem itemOscuro = new JMenuItem("Modo Oscuro");
        JMenuItem itemClaro = new JMenuItem("Modo Claro");

        itemOscuro.addActionListener(e -> {
            Main.setTema(true);
            SwingUtilities.updateComponentTreeUI(this);
        });

        itemClaro.addActionListener(e -> {
            Main.setTema(false);
            SwingUtilities.updateComponentTreeUI(this);
        });

        subMenuTema.add(itemOscuro);
        subMenuTema.add(itemClaro);
        menuOpciones.add(subMenuTema);

        menuBar.add(menuOpciones);

        this.setJMenuBar(menuBar);
    }

    // --- PANELES DE PESTAÑAS ---

    private JPanel createMedicamentosPanel() {
        JPanel panel = new JPanel(new BorderLayout(15, 15));
        panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        // Tabla de Medicamentos
        String[] columnas = {"ID", "Nombre", "Dosis", "Frecuencia (h)", "Próxima Toma", "Stock"};
        modeloMedicamentos = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };
        tablaMedicamentos = new JTable(modeloMedicamentos);
        tablaMedicamentos.setRowHeight(35);
        tablaMedicamentos.setFont(new Font("Arial", Font.PLAIN, 18));
        tablaMedicamentos.getColumnModel().getColumn(0).setMaxWidth(50);

        JScrollPane scroll = new JScrollPane(tablaMedicamentos);

        // --- PANEL DE BOTONES (CORRECCIÓN CLAVE DE LAYOUT) ---
        // Usamos GridLayout (1 fila, 3 columnas) para asegurar que todos los botones se vean.
        JPanel botones = new JPanel(new GridLayout(1, 3, 40, 15));
        botones.setBorder(BorderFactory.createEmptyBorder(15, 0, 0, 0));

        // --- BOTÓN 1: NUEVO TRATAMIENTO ---
        JButton btnAgregar = new JButton("NUEVO TRATAMIENTO");
        btnAgregar.setToolTipText("HU-01: Añadir nuevo tratamiento");
        btnAgregar.setFont(new Font("Arial", Font.BOLD, 20));


        // --- BOTÓN 2: REGISTRAR TOMA ---
        JButton btnTomar = new JButton("✓ REGISTRAR TOMA");
        btnTomar.setToolTipText("HU-05: Actualiza el conteo y la próxima toma");
        btnTomar.setFont(new Font("Arial", Font.BOLD, 20));
        btnTomar.setBackground(new Color(0x007AFF));
        btnTomar.setForeground(Color.WHITE);


        // --- BOTÓN 3: CANCELAR TRATAMIENTO (DEBE APARECER) ---
        JButton btnEliminar = new JButton("X CANCELAR TRATAMIENTO");
        btnEliminar.setToolTipText("HU-05: Eliminar medicamento");
        btnEliminar.setFont(new Font("Arial", Font.BOLD, 20));
        btnEliminar.setBackground(new Color(0xFF3B30));
        btnEliminar.setForeground(Color.WHITE);

        // --- Lógica de Acciones ---
        btnAgregar.addActionListener(e -> abrirDialogoMedicamento());
        btnTomar.addActionListener(e -> registrarToma());
        btnEliminar.addActionListener(e -> eliminarMedicamento());

        // Aseguramos que los 3 botones se añadan
        botones.add(btnAgregar);
        botones.add(btnTomar);
        botones.add(btnEliminar);

        panel.add(scroll, BorderLayout.CENTER);
        panel.add(botones, BorderLayout.SOUTH);

        return panel;
    }

    private JPanel createCitasPanel() {
        JPanel panel = new JPanel(new BorderLayout(15, 15));
        panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        // Columnas para Citas (con Centro Médico)
        String[] columnas = {"ID", "Especialidad", "Médico", "Centro Médico", "Fecha y Hora"};

        modeloCitas = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };
        tablaCitas = new JTable(modeloCitas);
        tablaCitas.setRowHeight(35);
        tablaCitas.setFont(new Font("Arial", Font.PLAIN, 18));
        tablaCitas.getColumnModel().getColumn(0).setMaxWidth(50);

        JScrollPane scroll = new JScrollPane(tablaCitas);

        // Panel de botones para Citas
        JPanel botones = new JPanel(new FlowLayout(FlowLayout.CENTER, 40, 15));

        // --- BOTÓN 1: REGISTRAR CITA ---
        JButton btnAgregarCita = new JButton("REGISTRAR CITA");
        btnAgregarCita.setToolTipText("HU-03: Añadir nueva cita médica");
        btnAgregarCita.setFont(new Font("Arial", Font.BOLD, 20));
        btnAgregarCita.setPreferredSize(new Dimension(320, 70));
        btnAgregarCita.setBackground(new Color(0x007AFF));
        btnAgregarCita.setForeground(Color.WHITE);

        // --- BOTÓN 2: CANCELAR CITA ---
        JButton btnEliminarCita = new JButton("X CANCELAR CITA");
        btnEliminarCita.setToolTipText("HU-05: Eliminar cita médica");
        btnEliminarCita.setFont(new Font("Arial", Font.BOLD, 20));
        btnEliminarCita.setPreferredSize(new Dimension(320, 70));
        btnEliminarCita.setBackground(new Color(0xFF3B30));
        btnEliminarCita.setForeground(Color.WHITE);

        // Lógica de Acciones
        btnAgregarCita.addActionListener(e -> abrirDialogoCita());
        btnEliminarCita.addActionListener(e -> eliminarCita());

        botones.add(btnAgregarCita);
        botones.add(btnEliminarCita);
        panel.add(scroll, BorderLayout.CENTER);
        panel.add(botones, BorderLayout.SOUTH);

        return panel;
    }

    private JPanel createPerfilHistorialPanel() {
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JTabbedPane subTabs = new JTabbedPane();
        subTabs.setFont(new Font("Arial", Font.BOLD, 16));

        subTabs.addTab("MI PERFIL (HU-10, HU-08)", createPerfilPanel());
        subTabs.addTab("HISTORIAL DE EVENTOS (HU-07)", createHistorialPanel());

        mainPanel.add(subTabs, BorderLayout.CENTER);
        return mainPanel;
    }

    private JPanel createPerfilPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(15, 15, 15, 15);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        Font labelFont = new Font("Arial", Font.BOLD, 18);
        Font inputFont = new Font("Arial", Font.PLAIN, 18);

        JTextField txtNombre = new JTextField(usuarioActual.getNombre(), 25);
        JTextField txtEmail = new JTextField(usuarioActual.getEmail(), 25);
        JPasswordField txtPassword = new JPasswordField(usuarioActual.getPassword(), 25);
        txtNombre.setFont(inputFont);
        txtEmail.setFont(inputFont);
        txtPassword.setFont(inputFont);

        JLabel lblAlerta = new JLabel("Modo de Alerta (HU-08):");
        lblAlerta.setFont(labelFont);
        String[] modos = {"SONIDO", "VIBRACION (Visual)", "SILENCIO"};
        JComboBox<String> cmbAlerta = new JComboBox<>(modos);
        cmbAlerta.setSelectedItem(usuarioActual.getModoAlerta());
        cmbAlerta.setFont(inputFont);

        JButton btnGuardar = new JButton("GUARDAR CAMBIOS");

        btnGuardar.setFont(new Font("Arial", Font.BOLD, 20));
        btnGuardar.setPreferredSize(new Dimension(280, 55));
        btnGuardar.setBackground(new Color(0x007AFF));
        btnGuardar.setForeground(Color.WHITE);

        gbc.gridx = 0; gbc.gridy = 0; panel.add(createLabel("Nombre:", labelFont), gbc);
        gbc.gridx = 1; gbc.gridy = 0; panel.add(txtNombre, gbc);

        gbc.gridx = 0; gbc.gridy = 1; panel.add(createLabel("Email:", labelFont), gbc);
        gbc.gridx = 1; gbc.gridy = 1; panel.add(txtEmail, gbc);

        gbc.gridx = 0; gbc.gridy = 2; panel.add(createLabel("Contraseña:", labelFont), gbc);
        gbc.gridx = 1; gbc.gridy = 2; panel.add(txtPassword, gbc);

        gbc.gridx = 0; gbc.gridy = 3; panel.add(lblAlerta, gbc);
        gbc.gridx = 1; gbc.gridy = 3; panel.add(cmbAlerta, gbc);

        gbc.gridx = 1; gbc.gridy = 4; gbc.gridwidth = 1; panel.add(btnGuardar, gbc);

        btnGuardar.addActionListener(e -> {
            usuarioActual.setNombre(txtNombre.getText());
            usuarioActual.setEmail(txtEmail.getText());
            usuarioActual.setPassword(new String(txtPassword.getPassword()));
            usuarioActual.setModoAlerta((String) cmbAlerta.getSelectedItem());

            if (usuarioDAO.actualizarUsuario(usuarioActual)) {
                setTitle("Gestión de Salud - Usuario: " + usuarioActual.getNombre());
                JOptionPane.showMessageDialog(this, "Datos y preferencias actualizados correctamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "Error al actualizar. Verifique que el email no esté duplicado.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        return panel;
    }

    private JLabel createLabel(String text, Font font) {
        JLabel label = new JLabel(text);
        label.setFont(font);
        return label;
    }

    private JPanel createHistorialPanel() {
        JPanel panel = new JPanel(new BorderLayout());

        String[] columnas = {"Tipo Evento", "Descripción", "Fecha y Hora"};
        DefaultTableModel modeloHistorial = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };
        JTable tablaHistorial = new JTable(modeloHistorial);
        tablaHistorial.setRowHeight(35);
        tablaHistorial.setFont(new Font("Arial", Font.PLAIN, 18));

        List<RegistroHistorial> historial = historialDAO.obtenerHistorial(usuarioActual.getId());

        for (RegistroHistorial reg : historial) {
            modeloHistorial.addRow(new Object[]{
                    reg.getTipoEvento(),
                    reg.getDescripcion(),
                    reg.getFechaHora().format(formatter)
            });
        }

        JLabel lblTitulo = new JLabel("Registro de Tomas, Cancelaciones y Alertas de Farmacia.", SwingConstants.CENTER);
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 18));

        panel.add(new JScrollPane(tablaHistorial), BorderLayout.CENTER);
        panel.add(lblTitulo, BorderLayout.NORTH);
        return panel;
    }

    // --- LÓGICA DE CARGA DE DATOS Y ACCIÓN ---

    public void cargarDatosMedicamentos() {
        modeloMedicamentos.setRowCount(0);
        List<Medicamento> medicamentos = medicamentoDAO.obtenerMedicamentosActivos(usuarioActual.getId());

        for (Medicamento med : medicamentos) {
            modeloMedicamentos.addRow(new Object[]{
                    med.getId(),
                    med.getNombre(),
                    med.getDosis(),
                    med.getFrecuenciaHoras(),
                    med.getProximaToma().format(formatter),
                    med.getStock()
            });
        }
    }

    public void cargarDatosCitas() {
        modeloCitas.setRowCount(0);
        List<Cita> citas = citaDAO.obtenerCitasPendientes(usuarioActual.getId());

        for (Cita cita : citas) {
            modeloCitas.addRow(new Object[]{
                    cita.getId(),
                    cita.getEspecialidad(),
                    cita.getMedico(),
                    cita.getCentroMedico(),
                    cita.getFechaHora().format(formatter)
            });
        }
    }

    private void iniciarServicioRecordatorios() {
        Thread hiloVerificacion = new Thread(new ServicioNotificacion(usuarioActual.getId()));
        hiloVerificacion.setDaemon(true);
        hiloVerificacion.start();
    }

    private void abrirDialogoMedicamento() {
        new DialogoMedicamento(this, usuarioActual.getId()).setVisible(true);
    }

    private void abrirDialogoCita() {
        // Asumiendo que el DialogoCita.java ya fue modificado para incluir el campo centroMedico
        new DialogoCita(this, usuarioActual.getId()).setVisible(true);
    }

    private void registrarToma() {
        int filaSeleccionada = tablaMedicamentos.getSelectedRow();
        if (filaSeleccionada == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione un medicamento de la lista.", "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            int medicamentoId = (int) modeloMedicamentos.getValueAt(filaSeleccionada, 0);
            String nombre = (String) modeloMedicamentos.getValueAt(filaSeleccionada, 1);
            int frecuenciaHoras = (int) modeloMedicamentos.getValueAt(filaSeleccionada, 3);

            boolean exito = medicamentoDAO.registrarToma(medicamentoId, frecuenciaHoras);

            if (exito) {
                JOptionPane.showMessageDialog(this, "Toma de '" + nombre + "' registrada. Próxima dosis calculada.", "Registro Exitoso", JOptionPane.INFORMATION_MESSAGE);
                cargarDatosMedicamentos();

                historialDAO.registrarEvento(new RegistroHistorial(
                        0, usuarioActual.getId(), "TOMA_MEDICAMENTO",
                        "Toma registrada: " + nombre + " (" + frecuenciaHoras + "h)",
                        LocalDateTime.now()
                ));

                if (medicamentoDAO.verificarStockBajo(medicamentoId, 5)) {
                    JOptionPane.showMessageDialog(this, "¡ATENCIÓN! El stock de '" + nombre + "' es bajo. Recuerde recoger en la farmacia.", "Recordatorio de Farmacia", JOptionPane.WARNING_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(this, "ERROR: Stock agotado para '" + nombre + "'.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al registrar la toma: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void eliminarMedicamento() {
        int filaSeleccionada = tablaMedicamentos.getSelectedRow();
        if (filaSeleccionada == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione un medicamento para eliminar.", "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int medicamentoId = (int) modeloMedicamentos.getValueAt(filaSeleccionada, 0);
        String nombre = (String) modeloMedicamentos.getValueAt(filaSeleccionada, 1);

        int confirm = JOptionPane.showConfirmDialog(this,
                "¿Está seguro de CANCELAR el tratamiento de '" + nombre + "'?",
                "Confirmar Eliminación", JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            medicamentoDAO.eliminarMedicamento(medicamentoId);

            historialDAO.registrarEvento(new RegistroHistorial(
                    0, usuarioActual.getId(), "TRATAMIENTO_CANCELADO",
                    "Se canceló el tratamiento: " + nombre,
                    LocalDateTime.now()
            ));

            cargarDatosMedicamentos();
            JOptionPane.showMessageDialog(this, "Tratamiento cancelado.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void eliminarCita() {
        int filaSeleccionada = tablaCitas.getSelectedRow();
        if (filaSeleccionada == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione una cita para cancelar.", "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int citaId = (int) modeloCitas.getValueAt(filaSeleccionada, 0);
        String especialidad = (String) modeloCitas.getValueAt(filaSeleccionada, 1);

        int confirm = JOptionPane.showConfirmDialog(this,
                "¿Está seguro de CANCELAR la cita de '" + especialidad + "'?",
                "Confirmar Cancelación", JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            citaDAO.eliminarCita(citaId);

            historialDAO.registrarEvento(new RegistroHistorial(
                    0, usuarioActual.getId(), "CITA_CANCELADA",
                    "Se canceló la cita de: " + especialidad,
                    LocalDateTime.now()
            ));

            cargarDatosCitas();
            JOptionPane.showMessageDialog(this, "Cita cancelada.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
        }
    }
}