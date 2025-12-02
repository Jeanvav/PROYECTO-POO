package com.proyectopoo.dao;

import com.proyectopoo.database.ConexionDB;
import com.proyectopoo.model.RegistroHistorial;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class HistorialDAO {

    /**
     * Registra un nuevo evento en la tabla de historial.
     */
    public boolean registrarEvento(RegistroHistorial registro) {
        String sql = "INSERT INTO historial (usuario_id, tipo_evento, descripcion, fecha_hora) VALUES (?, ?, ?, ?)";

        // CORRECCIÓN: Usar getConnection()
        try (Connection conn = ConexionDB.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, registro.getUsuarioId());
            pstmt.setString(2, registro.getTipoEvento());
            pstmt.setString(3, registro.getDescripcion());
            pstmt.setString(4, registro.getFechaHora().toString()); // Convertir LocalDateTime a String

            pstmt.executeUpdate();
            return true;

        } catch (SQLException e) {
            System.err.println("Error al registrar evento en historial: " + e.getMessage());
            return false;
        }
    }

    /**
     * Obtiene el historial de eventos de un usuario, ordenado por fecha descendente.
     */
    public List<RegistroHistorial> obtenerHistorial(int usuarioId) {
        List<RegistroHistorial> historial = new ArrayList<>();

        // CORRECCIÓN DE LA CONSULTA SQL: Eliminada la duplicación "FROM historial"
        String sql = "SELECT id, usuario_id, tipo_evento, descripcion, fecha_hora FROM historial WHERE usuario_id = ? ORDER BY fecha_hora DESC";

        // CORRECCIÓN: Usar getConnection()
        try (Connection conn = ConexionDB.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, usuarioId);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                RegistroHistorial registro = new RegistroHistorial();
                registro.setId(rs.getInt("id"));
                registro.setUsuarioId(rs.getInt("usuario_id")); // Obtenido del RS para mayor seguridad
                registro.setTipoEvento(rs.getString("tipo_evento"));
                registro.setDescripcion(rs.getString("descripcion"));
                // Convertir String de vuelta a LocalDateTime
                registro.setFechaHora(LocalDateTime.parse(rs.getString("fecha_hora")));
                historial.add(registro);
            }

        } catch (SQLException e) {
            System.err.println("Error al obtener historial: " + e.getMessage());
        }
        return historial;
    }
}