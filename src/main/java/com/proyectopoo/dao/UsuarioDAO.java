package com.proyectopoo.dao;

import com.proyectopoo.database.ConexionDB;
import com.proyectopoo.model.Usuario;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UsuarioDAO {

    public boolean registrarUsuario(Usuario usuario) {
        // SQL actualizado con modo_alerta
        String sql = "INSERT INTO usuarios (nombre, email, password, modo_alerta) VALUES (?, ?, ?, ?)";

        // CORRECCIÓN: Usar getConnection()
        try (Connection conn = ConexionDB.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, usuario.getNombre());
            pstmt.setString(2, usuario.getEmail());
            pstmt.setString(3, usuario.getPassword());
            pstmt.setString(4, "SONIDO"); // Valor por defecto
            pstmt.executeUpdate();
            return true;

        } catch (SQLException e) {
            System.err.println("Error al registrar usuario: " + e.getMessage());
            return false;
        }
    }

    public Usuario iniciarSesion(String email, String password) {
        Usuario usuario = null;
        String sql = "SELECT id, nombre, email, password, modo_alerta FROM usuarios WHERE email = ? AND password = ?";

        // CORRECCIÓN: Usar getConnection()
        try (Connection conn = ConexionDB.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, email);
            pstmt.setString(2, password);

            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                usuario = new Usuario();
                usuario.setId(rs.getInt("id"));
                usuario.setNombre(rs.getString("nombre"));
                usuario.setEmail(rs.getString("email"));
                usuario.setPassword(rs.getString("password"));
                // Campo actualizado (HU-08)
                usuario.setModoAlerta(rs.getString("modo_alerta"));
            }

        } catch (SQLException e) {
            System.err.println("Error en iniciar sesion: " + e.getMessage());
        }
        return usuario;
    }

    public Usuario obtenerUsuario(int id) {
        Usuario usuario = null;
        // SQL actualizado para seleccionar modo_alerta
        String sql = "SELECT id, nombre, email, password, modo_alerta FROM usuarios WHERE id = ?";

        // CORRECCIÓN: Usar getConnection()
        try (Connection conn = ConexionDB.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                usuario = new Usuario();
                usuario.setId(rs.getInt("id"));
                usuario.setNombre(rs.getString("nombre"));
                usuario.setEmail(rs.getString("email"));
                usuario.setPassword(rs.getString("password"));
                // Campo actualizado (HU-08)
                usuario.setModoAlerta(rs.getString("modo_alerta"));
            }

        } catch (SQLException e) {
            System.err.println("Error al obtener usuario: " + e.getMessage());
        }
        return usuario;
    }

    public boolean actualizarUsuario(Usuario usuario) {
        // SQL actualizado para incluir modo_alerta
        String sql = "UPDATE usuarios SET nombre = ?, email = ?, password = ?, modo_alerta = ? WHERE id = ?";

        // CORRECCIÓN: Usar getConnection()
        try (Connection conn = ConexionDB.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, usuario.getNombre());
            pstmt.setString(2, usuario.getEmail());
            pstmt.setString(3, usuario.getPassword());
            // Campo actualizado (HU-08)
            pstmt.setString(4, usuario.getModoAlerta());
            pstmt.setInt(5, usuario.getId());

            return pstmt.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("Error al actualizar usuario: " + e.getMessage());
            return false;
        }
    }
}