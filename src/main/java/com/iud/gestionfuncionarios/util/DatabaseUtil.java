package com.iud.gestionfuncionarios.util;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class DatabaseUtil {

    private static Connection connection = null;
    private static Properties properties = new Properties();

    // Bloque estático: se ejecuta una sola vez cuando la clase se carga por primera vez.
    static {
        try (InputStream input = DatabaseUtil.class.getClassLoader().getResourceAsStream("config.properties")) {
            if (input == null) {
                // Si el archivo no se encuentra, lanzamos un error grave.
                System.err.println("¡ERROR CRÍTICO! No se pudo encontrar el archivo config.properties.");
                throw new RuntimeException("Error al cargar la configuración de la base de datos. Asegúrate de que config.properties esté en src/main/resources.");
            }
            // Cargamos las propiedades del archivo config.properties
            properties.load(input);
            // Cargamos el driver de MySQL. Esto es necesario para que Java sepa cómo hablar con MySQL.
            Class.forName(properties.getProperty("db.driver"));
            System.out.println("Driver de MySQL cargado correctamente.");
        } catch (IOException ex) {
            System.err.println("Error de I/O al leer config.properties: " + ex.getMessage());
            throw new RuntimeException("Error al cargar la configuración de la base de datos.", ex);
        } catch (ClassNotFoundException ex) {
            System.err.println("Error: Driver de MySQL no encontrado. Asegúrate de que la dependencia mysql-connector-java esté en tu pom.xml.");
            throw new RuntimeException("Error al cargar el driver de la base de datos.", ex);
        }
    }

    /**
     * Obtiene una conexión a la base de datos. Si ya existe una conexión abierta, la reutiliza.
     * @return Un objeto Connection.
     * @throws SQLException Si ocurre un error al intentar conectar.
     */
    public static Connection getConnection() throws SQLException {
        // Verificamos si la conexión está nula o cerrada
        if (connection == null || connection.isClosed()) {
            try {
                // Intentamos establecer una nueva conexión usando los datos del properties
                connection = DriverManager.getConnection(
                        properties.getProperty("db.url"),
                        properties.getProperty("db.user"),
                        properties.getProperty("db.password"));
                System.out.println("Conexión a la base de datos establecida.");
            } catch (SQLException e) {
                System.err.println("Error al conectar a la base de datos: " + e.getMessage());
                // Lanzamos la excepción para que sea manejada por quien llamó a este método
                throw e;
            }
        }
        return connection;
    }

    /**
     * Cierra la conexión a la base de datos si está abierta.
     */
    public static void closeConnection() {
        if (connection != null) {
            try {
                connection.close();
                connection = null; // Reseteamos la variable para que se cree una nueva si se necesita
                System.out.println("Conexión a la base de datos cerrada.");
            } catch (SQLException e) {
                System.err.println("Error al cerrar la conexión a la base de datos: " + e.getMessage());
            }
        }
    }
}