package com.proyectopoo.dao;

import com.proyectopoo.database.ConexionDB;
import com.proyectopoo.model.Medicamento;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class MedicamentoDAO {

    /**
     * Inserta un nuevo medicamento asociado a un usuario (HU-01).
     */
    public void insertarMedicamento(Medicamento med) {
        String sql = "INSERT INTO medicamentos (usuario_id, nombre, dosis, frecuencia_horas, stock, proxima_toma) VALUES (?, ?, ?, ?, ?, ?)";

        // CORRECCIÓN: Usar getConnection()
        try (Connection conn = ConexionDB.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, med.getUsuarioId());
            pstmt.setString(2, med.getNombre());
            pstmt.setString(3, med.getDosis());
            pstmt.setInt(4, med.getFrecuenciaHoras());
            pstmt.setInt(5, med.getStock());
            // Guardamos LocalDateTime como String en la base de datos
            pstmt.setString(6, med.getProximaToma().toString());
            pstmt.executeUpdate();

        } catch (SQLException e) {
            System.err.println("Error al insertar medicamento: " + e.getMessage());
        }
    }

    /**
     * Obtiene todos los medicamentos activos del usuario para mostrar en el Dashboard (HU-04).
     */
    public List<Medicamento> obtenerMedicamentosActivos(int usuarioId) {
        String sql = "SELECT * FROM medicamentos WHERE usuario_id = ?";
        List<Medicamento> medicamentos = new ArrayList<>();

        // CORRECCIÓN: Usar getConnection()
        try (Connection conn = ConexionDB.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, usuarioId);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                Medicamento med = new Medicamento();
                med.setId(rs.getInt("id"));
                med.setUsuarioId(rs.getInt("usuario_id"));
                med.setNombre(rs.getString("nombre"));
                med.setDosis(rs.getString("dosis"));
                med.setFrecuenciaHoras(rs.getInt("frecuencia_horas"));
                med.setStock(rs.getInt("stock"));
                // Convertimos el String de la DB a LocalDateTime
                med.setProximaToma(LocalDateTime.parse(rs.getString("proxima_toma")));
                medicamentos.add(med);
            }

        } catch (SQLException e) {
            System.err.println("Error al obtener medicamentos: " + e.getMessage());
        }
        return medicamentos;
    }

    /**
     * Registra una toma, reduce el stock en 1 y actualiza la próxima toma (HU-05).
     */
    public boolean registrarToma(int medicamentoId, int frecuenciaHoras) {
        // 1. Calcular la nueva próxima toma, sumando la frecuencia a la hora actual
        LocalDateTime nuevaProximaToma = LocalDateTime.now().plusHours(frecuenciaHoras);

        // 2. SQL: Reducir stock en 1 y actualizar la próxima toma
        String sql = "UPDATE medicamentos SET stock = stock - 1, proxima_toma = ? WHERE id = ? AND stock > 0";

        // CORRECCIÓN: Usar getConnection()
        try (Connection conn = ConexionDB.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, nuevaProximaToma.toString());
            pstmt.setInt(2, medicamentoId);

            int filasAfectadas = pstmt.executeUpdate();

            // Retorna true si se actualizó una fila (es decir, el stock era > 0)
            return filasAfectadas > 0;

        } catch (SQLException e) {
            System.err.println("Error al registrar toma: " + e.getMessage());
            return false;
        }
    }

    /**
     * Verifica si el stock de un medicamento está bajo (HU-06: recordatorio de farmacia).
     * @param umbral Cantidad mínima para considerar 'bajo' (ej. 5 pastillas).
     */
    public boolean verificarStockBajo(int medicamentoId, int umbral) {
        String sql = "SELECT stock FROM medicamentos WHERE id = ?";
        // CORRECCIÓN: Usar getConnection()
        try (Connection conn = ConexionDB.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, medicamentoId);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return rs.getInt("stock") <= umbral;
            }
        } catch (SQLException e) {
            System.err.println("Error al verificar stock: " + e.getMessage());
        }
        return false;
    }

    /**
     * Elimina un medicamento (parte de HU-05: Modificar o Cancelar).
     */
    public void eliminarMedicamento(int medicamentoId) {
        String sql = "DELETE FROM medicamentos WHERE id = ?";

        // CORRECCIÓN: Usar getConnection()
        try (Connection conn = ConexionDB.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, medicamentoId);
            pstmt.executeUpdate();

        } catch (SQLException e) {
            System.err.println("Error al eliminar medicamento: " + e.getMessage());
        }
    }
}