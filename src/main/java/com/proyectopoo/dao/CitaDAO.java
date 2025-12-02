package com.proyectopoo.dao;

import com.proyectopoo.database.ConexionDB;
import com.proyectopoo.model.Cita;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class CitaDAO {

    /**
     * Registra una nueva cita médica. Incluye centro_medico.
     */
    public void insertarCita(Cita cita) {
        // SQL con 5 placeholders para los campos: usuario_id, especialidad, medico, centro_medico, fecha_hora
        String sql = "INSERT INTO citas (usuario_id, especialidad, medico, centro_medico, fecha_hora) VALUES (?, ?, ?, ?, ?)";

        // CORRECCIÓN: Usar getConnection()
        try (Connection conn = ConexionDB.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, cita.getUsuarioId());
            pstmt.setString(2, cita.getEspecialidad());
            pstmt.setString(3, cita.getMedico());
            pstmt.setString(4, cita.getCentroMedico());
            pstmt.setString(5, cita.getFechaHora().toString());
            pstmt.executeUpdate();

        } catch (SQLException e) {
            System.err.println("Error al insertar cita: " + e.getMessage());
        }
    }

    /**
     * Obtiene las citas pendientes. Recupera centro_medico.
     */
    public List<Cita> obtenerCitasPendientes(int usuarioId) {
        String sql = "SELECT * FROM citas WHERE usuario_id = ? AND fecha_hora > ? ORDER BY fecha_hora ASC";
        List<Cita> citas = new ArrayList<>();

        // CORRECCIÓN: Usar getConnection()
        try (Connection conn = ConexionDB.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, usuarioId);
            pstmt.setString(2, LocalDateTime.now().toString());
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                Cita cita = new Cita();
                cita.setId(rs.getInt("id"));
                cita.setUsuarioId(rs.getInt("usuario_id"));
                cita.setEspecialidad(rs.getString("especialidad"));
                cita.setMedico(rs.getString("medico"));
                cita.setCentroMedico(rs.getString("centro_medico"));
                cita.setFechaHora(LocalDateTime.parse(rs.getString("fecha_hora")));
                citas.add(cita);
            }

        } catch (SQLException e) {
            System.err.println("Error al obtener citas: " + e.getMessage());
        }
        return citas;
    }

    /**
     * Elimina una cita.
     */
    public void eliminarCita(int citaId) {
        String sql = "DELETE FROM citas WHERE id = ?";

        // CORRECCIÓN: Usar getConnection()
        try (Connection conn = ConexionDB.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, citaId);
            pstmt.executeUpdate();

        } catch (SQLException e) {
            System.err.println("Error al eliminar cita: " + e.getMessage());
        }
    }
}