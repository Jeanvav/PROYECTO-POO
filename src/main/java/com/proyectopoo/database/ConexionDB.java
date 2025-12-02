package com.proyectopoo.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class ConexionDB {

    // URL de conexión: busca o crea la base de datos en el mismo directorio que se ejecuta el JAR.
    private static final String URL = "jdbc:sqlite:gestion_salud.db";

    /**
     * Establece y retorna una conexión a la base de datos.
     * Si la DB es nueva, también crea todas las tablas necesarias.
     *
     * @return Objeto Connection si la conexión es exitosa, o null si falla.
     */
    public static Connection getConnection() {
        Connection conn = null;
        try {
            // 1. Establecer la conexión
            conn = DriverManager.getConnection(URL);

            // 2. Crear las tablas si no existen.
            if (conn != null) {
                // LLAMADA CORRECTA DENTRO DE LA CLASE
                inicializarTablas(conn);
            }

        } catch (SQLException e) {
            // Muestra la excepción CRÍTICA en la terminal del JAR
            System.err.println("❌ ERROR de Conexión o Inicialización de DB.");
            e.printStackTrace();
        }
        return conn;
    }

    /**
     * Método auxiliar para crear las tablas necesarias si no existen.
     * Es PUBLIC para que Main pueda forzar su ejecución si es necesario, aunque se llama primariamente desde getConnection.
     * * @param conn La conexión activa.
     */
    public static void inicializarTablas(Connection conn) { // <--- CAMBIADO A PUBLIC
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

            // Tabla citas
            String sqlCitas = "CREATE TABLE IF NOT EXISTS citas (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "usuario_id INTEGER NOT NULL," +
                    "especialidad TEXT NOT NULL," +
                    "medico TEXT,"
                    + "centro_medico TEXT,"
                    + "fecha_hora DATETIME NOT NULL,"
                    + "FOREIGN KEY (usuario_id) REFERENCES usuarios(id)"
                    + ")";
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

        } catch (SQLException e) {
            System.err.println("❌ ERROR: Fallo al crear tablas en SQLite.");
            e.printStackTrace();
        }
    }
}