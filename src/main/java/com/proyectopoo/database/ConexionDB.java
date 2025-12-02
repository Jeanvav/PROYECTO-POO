package com.proyectopoo.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class ConexionDB {

    // 📢 CONFIGURACIÓN DE CONEXIÓN A SQLite (LOCAL)
    private static final String DRIVER = "org.sqlite.JDBC";
    private static final String URL = "jdbc:sqlite:gestion_salud.db";

    public static Connection conectar() {
        Connection conn = null;
        try {
            Class.forName(DRIVER);
            conn = DriverManager.getConnection(URL);
        } catch (SQLException e) {
            System.err.println("❌ ERROR: No se pudo conectar a la base de datos local SQLite.");
            System.err.println("Detalles del error: " + e.getMessage());
        } catch (ClassNotFoundException e) {
            System.err.println("❌ ERROR: No se encontró el driver de SQLite. Verifique su pom.xml y Maven.");
        }
        return conn;
    }

    public static void inicializarTablas() {
        try (Connection conn = conectar()) {
            if (conn != null) {
                try (Statement stmt = conn.createStatement()) {

                    // Tabla usuarios
                    String sqlUsuarios = "CREATE TABLE IF NOT EXISTS usuarios (" +
                            "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                            "nombre TEXT NOT NULL," +
                            "email TEXT NOT NULL UNIQUE," +
                            "password TEXT NOT NULL," +
                            "modo_alerta TEXT DEFAULT 'SONIDO'" +
                            ")";
                    stmt.execute(sqlUsuarios);

                    // Tabla medicamentos
                    String sqlMedicamentos = "CREATE TABLE IF NOT EXISTS medicamentos (" +
                            "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                            "usuario_id INTEGER NOT NULL," +
                            "nombre TEXT NOT NULL," +
                            "dosis TEXT," +
                            "frecuencia_horas INTEGER," +
                            "stock INTEGER," +
                            "proxima_toma DATETIME," +
                            "FOREIGN KEY (usuario_id) REFERENCES usuarios(id)" +
                            ")";
                    stmt.execute(sqlMedicamentos);

                    // Tabla citas (CORREGIDA con centro_medico)
                    String sqlCitas = "CREATE TABLE IF NOT EXISTS citas (" +
                            "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                            "usuario_id INTEGER NOT NULL," +
                            "especialidad TEXT NOT NULL," +
                            "medico TEXT," +
                            "centro_medico TEXT," +
                            "fecha_hora DATETIME NOT NULL," +
                            "FOREIGN KEY (usuario_id) REFERENCES usuarios(id)" +
                            ")";
                    stmt.execute(sqlCitas);

                    // Tabla historial
                    String sqlHistorial = "CREATE TABLE IF NOT EXISTS historial (" +
                            "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                            "usuario_id INTEGER NOT NULL," +
                            "tipo_evento TEXT NOT NULL," +
                            "descripcion TEXT NOT NULL," +
                            "fecha_hora DATETIME NOT NULL," +
                            "FOREIGN KEY (usuario_id) REFERENCES usuarios(id)" +
                            ")";
                    stmt.execute(sqlHistorial);

                    System.out.println("✅ Tablas SQLite inicializadas correctamente.");

                } catch (SQLException e) {
                    System.err.println("❌ ERROR: Fallo al crear tablas en SQLite: " + e.getMessage());
                }
            }
        } catch (SQLException e) {
            // Manejo de error de conexión
        }
    }
}