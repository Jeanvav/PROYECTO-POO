package com.proyectopoo.ui;

import com.proyectopoo.dao.UsuarioDAO;
import com.proyectopoo.model.Usuario;
import javax.swing.*;
import java.awt.*;

public class VentanaAcceso extends JFrame {

    private final UsuarioDAO usuarioDAO = new UsuarioDAO();

    // Componentes de registro
    private JTextField txtRegNombre;
    private JTextField txtRegEmail;
    private JPasswordField txtRegPassword;
    private JButton btnRegistrar;
    private JCheckBox chkMostrarReg;

    // Componentes de login
    private JTextField txtLogEmail;
    private JPasswordField txtLogPassword;
    private JButton btnLogin;
    private JCheckBox chkMostrarLog;

    // Definiciones de Estilo (Consistencia UI/UX)
    private static final Font LABEL_FONT = new Font("Arial", Font.BOLD, 18);
    private static final Font INPUT_FONT = new Font("Arial", Font.PLAIN, 18);
    private static final Font BUTTON_FONT = new Font("Arial", Font.BOLD, 22);
    private static final Color PRIMARY_BLUE = new Color(0x007AFF);

    public VentanaAcceso() {
        setTitle("Acceso al Sistema de Gestión de Salud");
        setSize(850, 550); // Tamaño más grande para accesibilidad
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Usar un GridLayout simple para dividir la ventana en dos (con más espaciado)
        setLayout(new GridLayout(1, 2, 20, 20));

        ((JComponent) getContentPane()).setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        add(createLoginPanel());
        add(createRegistroPanel());
    }

    // --- Panel de Login ---
    private JPanel createLoginPanel() {
        // Título de panel sin emoji
        JPanel panel = new PanelConTitulo("INICIAR SESIÓN");
        panel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(15, 15, 15, 15); // Espaciado

        // Inicializar y estilizar componentes
        txtLogEmail = new JTextField(20);
        txtLogPassword = new JPasswordField(20);
        // Botón sin emoji
        btnLogin = new JButton("INGRESAR");

        txtLogEmail.setFont(INPUT_FONT);
        txtLogPassword.setFont(INPUT_FONT);

        // Checkbox para mostrar contraseña
        chkMostrarLog = new JCheckBox("Mostrar Contraseña");
        chkMostrarLog.setFont(INPUT_FONT);
        chkMostrarLog.addActionListener(e -> togglePasswordVisibility(txtLogPassword, chkMostrarLog.isSelected()));

        // Estilo del botón LOGIN
        btnLogin.setFont(BUTTON_FONT);
        btnLogin.setPreferredSize(new Dimension(200, 55));
        btnLogin.setBackground(PRIMARY_BLUE);
        btnLogin.setForeground(Color.WHITE);


        // Rellenar panel

        // --- Fila 0: Email ---
        gbc.gridx = 0; gbc.gridy = 0;
        gbc.weightx = 0.0; // Etiqueta no se expande
        gbc.fill = GridBagConstraints.NONE;
        panel.add(createLabel("Email:"), gbc);

        gbc.gridx = 1; gbc.gridy = 0;
        gbc.weightx = 1.0; // Campo de texto se expande (CORRECCIÓN TAMAÑO)
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel.add(txtLogEmail, gbc);

        // --- Fila 1: Contraseña ---
        gbc.gridx = 0; gbc.gridy = 1;
        gbc.weightx = 0.0;
        gbc.fill = GridBagConstraints.NONE;
        panel.add(createLabel("Contraseña:"), gbc);

        gbc.gridx = 1; gbc.gridy = 1;
        gbc.weightx = 1.0; // Campo de texto se expande (CORRECCIÓN TAMAÑO)
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel.add(txtLogPassword, gbc);

        // --- Fila 2: Checkbox ---
        gbc.gridx = 1; gbc.gridy = 2;
        gbc.gridwidth = 1;
        gbc.weightx = 0.0;
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.WEST;
        panel.add(chkMostrarLog, gbc);

        // --- Fila 3: Botón INGRESAR ---
        gbc.gridx = 0; gbc.gridy = 3;
        gbc.gridwidth = 2; // Ocupa ambas columnas
        gbc.weightx = 1.0;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel.add(btnLogin, gbc);

        // Lógica de Login
        btnLogin.addActionListener(e -> iniciarSesion());

        return panel;
    }

    // --- Panel de Registro ---
    private JPanel createRegistroPanel() {
        // Título de panel sin emoji
        JPanel panel = new PanelConTitulo("REGISTRAR CUENTA");
        panel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(15, 15, 15, 15);

        // Inicializar y estilizar componentes
        txtRegNombre = new JTextField(20);
        txtRegEmail = new JTextField(20);
        txtRegPassword = new JPasswordField(20);
        // Botón sin emoji
        btnRegistrar = new JButton("CREAR CUENTA");

        txtRegNombre.setFont(INPUT_FONT);
        txtRegEmail.setFont(INPUT_FONT);
        txtRegPassword.setFont(INPUT_FONT);

        // Checkbox para mostrar contraseña
        chkMostrarReg = new JCheckBox("Mostrar Contraseña");
        chkMostrarReg.setFont(INPUT_FONT);
        chkMostrarReg.addActionListener(e -> togglePasswordVisibility(txtRegPassword, chkMostrarReg.isSelected()));

        // Estilo del botón REGISTRAR
        btnRegistrar.setFont(BUTTON_FONT);
        btnRegistrar.setPreferredSize(new Dimension(200, 55));
        btnRegistrar.setBackground(PRIMARY_BLUE);
        btnRegistrar.setForeground(Color.WHITE);


        // Rellenar panel

        // --- Fila 0: Nombre ---
        gbc.gridx = 0; gbc.gridy = 0;
        gbc.weightx = 0.0;
        gbc.fill = GridBagConstraints.NONE;
        panel.add(createLabel("Nombre:"), gbc);

        gbc.gridx = 1; gbc.gridy = 0;
        gbc.weightx = 1.0; // Campo de texto se expande (CORRECCIÓN TAMAÑO)
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel.add(txtRegNombre, gbc);

        // --- Fila 1: Email ---
        gbc.gridx = 0; gbc.gridy = 1;
        gbc.weightx = 0.0;
        gbc.fill = GridBagConstraints.NONE;
        panel.add(createLabel("Email:"), gbc);

        gbc.gridx = 1; gbc.gridy = 1;
        gbc.weightx = 1.0; // Campo de texto se expande (CORRECCIÓN TAMAÑO)
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel.add(txtRegEmail, gbc);

        // --- Fila 2: Contraseña ---
        gbc.gridx = 0; gbc.gridy = 2;
        gbc.weightx = 0.0;
        gbc.fill = GridBagConstraints.NONE;
        panel.add(createLabel("Contraseña:"), gbc);

        gbc.gridx = 1; gbc.gridy = 2;
        gbc.weightx = 1.0; // Campo de texto se expande (CORRECCIÓN TAMAÑO)
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel.add(txtRegPassword, gbc);

        // --- Fila 3: Checkbox ---
        gbc.gridx = 1; gbc.gridy = 3;
        gbc.gridwidth = 1;
        gbc.weightx = 0.0;
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.WEST;
        panel.add(chkMostrarReg, gbc);

        // --- Fila 4: Botón CREAR CUENTA ---
        gbc.gridx = 0; gbc.gridy = 4;
        gbc.gridwidth = 2;
        gbc.weightx = 1.0;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel.add(btnRegistrar, gbc);

        // Lógica de Registro
        btnRegistrar.addActionListener(e -> registrarUsuario());

        return panel;
    }

    // Método auxiliar para crear etiquetas con el estilo consistente
    private JLabel createLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(LABEL_FONT);
        return label;
    }

    // --- Lógica de Usabilidad (HU-09) ---

    private void togglePasswordVisibility(JPasswordField passwordField, boolean isChecked) {
        if (isChecked) {
            passwordField.setEchoChar((char) 0);
        } else {
            passwordField.setEchoChar('*');
        }
    }

    // --- Lógica de Negocio ---

    private void registrarUsuario() {
        String nombre = txtRegNombre.getText();
        String email = txtRegEmail.getText();
        String password = new String(txtRegPassword.getPassword());

        if (nombre.isEmpty() || email.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Todos los campos son obligatorios.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        Usuario nuevoUsuario = new Usuario(0, nombre, email, password, "SONIDO"); // "SONIDO" es el valor por defecto para modoAlerta

        if (usuarioDAO.registrarUsuario(nuevoUsuario)) {
            JOptionPane.showMessageDialog(this, "Registro exitoso. ¡Inicia sesión!", "Éxito", JOptionPane.INFORMATION_MESSAGE);

            txtRegNombre.setText("");
            txtRegEmail.setText("");
            txtRegPassword.setText("");

            txtLogEmail.setText(email);
            txtLogPassword.requestFocus();

        } else {
            JOptionPane.showMessageDialog(this, "Error al registrar. El email ya está en uso.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void iniciarSesion() {
        String email = txtLogEmail.getText();
        String password = new String(txtLogPassword.getPassword());

        Usuario usuario = usuarioDAO.iniciarSesion(email, password);

        if (usuario != null) {
            JOptionPane.showMessageDialog(this, "Bienvenido, " + usuario.getNombre(), "Inicio Exitoso", JOptionPane.INFORMATION_MESSAGE);

            VentanaPrincipal principal = new VentanaPrincipal(usuario);
            principal.setVisible(true);

            this.dispose();

        } else {
            JOptionPane.showMessageDialog(this, "Credenciales incorrectas.", "Error de Acceso", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Clase interna auxiliar para los paneles con borde y título (Estilo UX)
    class PanelConTitulo extends JPanel {
        PanelConTitulo(String title) {
            setBorder(BorderFactory.createTitledBorder(
                    BorderFactory.createLineBorder(UIManager.getColor("Component.borderColor"), 2),
                    title,
                    0, 0,
                    new Font("Arial", Font.BOLD, 20),
                    UIManager.getColor("Label.foreground")
            ));
        }
    }
}