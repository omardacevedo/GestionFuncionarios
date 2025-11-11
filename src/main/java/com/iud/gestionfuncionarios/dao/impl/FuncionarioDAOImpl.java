package com.iud.gestionfuncionarios.dao.impl;

import com.iud.gestionfuncionarios.dao.FuncionarioDAO;
import com.iud.gestionfuncionarios.exceptions.DAOException;
import com.iud.gestionfuncionarios.model.Funcionario;
import com.iud.gestionfuncionarios.util.DatabaseUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

// Esta es la IMPLEMENTACIÓN CONCRETA del DAO para Funcionario.
// Aquí escribimos el código SQL para interactuar con la tabla 'funcionarios'.
public class FuncionarioDAOImpl implements FuncionarioDAO {

    // Sentencias SQL que usaremos
    private static final String INSERT_SQL = "INSERT INTO funcionarios (tipo_identificacion, numero_identificacion, nombres, apellidos, estado_civil, sexo, direccion, telefono, fecha_nacimiento) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
    private static final String SELECT_BY_ID_SQL = "SELECT id_funcionario, tipo_identificacion, numero_identificacion, nombres, apellidos, estado_civil, sexo, direccion, telefono, fecha_nacimiento FROM funcionarios WHERE id_funcionario = ?";
    private static final String UPDATE_SQL = "UPDATE funcionarios SET tipo_identificacion = ?, numero_identificacion = ?, nombres = ?, apellidos = ?, estado_civil = ?, sexo = ?, direccion = ?, telefono = ?, fecha_nacimiento = ? WHERE id_funcionario = ?";
    private static final String DELETE_SQL = "DELETE FROM funcionarios WHERE id_funcionario = ?";
    private static final String SELECT_ALL_SQL = "SELECT id_funcionario, tipo_identificacion, numero_identificacion, nombres, apellidos, estado_civil, sexo, direccion, telefono, fecha_nacimiento FROM funcionarios";
    private static final String FIND_BY_NUMERO_IDENTIFICACION_SQL = "SELECT id_funcionario, tipo_identificacion, numero_identificacion, nombres, apellidos, estado_civil, sexo, direccion, telefono, fecha_nacimiento FROM funcionarios WHERE numero_identificacion LIKE ?";

    /**
     * Método para crear un nuevo registro en la tabla 'funcionarios'.
     */
    @Override
    public void create(Funcionario funcionario) throws DAOException {
        // Usamos try-with-resources para asegurarnos de que los recursos (Connection, PreparedStatement) se cierren automáticamente.
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(INSERT_SQL, Statement.RETURN_GENERATED_KEYS)) { // RETURN_GENERATED_KEYS para obtener el ID autogenerado

            // Asignamos los valores del objeto Funcionario a los parámetros del PreparedStatement (?)
            pstmt.setString(1, funcionario.getTipoIdentificacion());
            pstmt.setString(2, funcionario.getNumeroIdentificacion());
            pstmt.setString(3, funcionario.getNombres());
            pstmt.setString(4, funcionario.getApellidos());
            pstmt.setString(5, funcionario.getEstadoCivil());
            pstmt.setString(6, funcionario.getSexo());
            pstmt.setString(7, funcionario.getDireccion());
            pstmt.setString(8, funcionario.getTelefono());
            // Convertimos java.util.Date a java.sql.Date
            pstmt.setDate(9, new java.sql.Date(funcionario.getFechaNacimiento().getTime()));

            int affectedRows = pstmt.executeUpdate(); // Ejecutamos la inserción

            if (affectedRows == 0) {
                // Si no se afectó ninguna fila, algo salió mal.
                throw new DAOException("La creación del funcionario falló, no se afectaron filas.");
            }

            // Obtenemos el ID que la base de datos generó automáticamente
            try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    funcionario.setIdFuncionario(generatedKeys.getInt(1)); // Asignamos el ID al objeto Funcionario
                } else {
                    throw new DAOException("La creación del funcionario falló y no se pudo obtener el ID.");
                }
            }
            System.out.println("Funcionario creado con ID: " + funcionario.getIdFuncionario());

        } catch (SQLException e) {
            // Capturamos cualquier error de SQL
            System.err.println("Error SQL al crear funcionario: " + e.getMessage());
            // Lanzamos nuestra excepción personalizada, incluyendo la causa original
            throw new DAOException("Error al crear el funcionario en la base de datos.", e);
        }
    }

    /**
     * Método para leer (obtener) un funcionario por su ID.
     */
    @Override
    public Funcionario read(int id) throws DAOException {
        Funcionario funcionario = null; // Inicializamos en null
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(SELECT_BY_ID_SQL)) {

            pstmt.setInt(1, id); // Asignamos el ID a la consulta
            try (ResultSet rs = pstmt.executeQuery()) { // Ejecutamos la consulta SELECT
                if (rs.next()) { // Si se encontró una fila...
                    funcionario = mapResultSetToFuncionario(rs); // Mapeamos la fila a un objeto Funcionario
                }
            }
        } catch (SQLException e) {
            System.err.println("Error SQL al leer funcionario por ID " + id + ": " + e.getMessage());
            throw new DAOException("Error al leer el funcionario de la base de datos.", e);
        }
        return funcionario; // Devolvemos el objeto Funcionario o null si no se encontró
    }

    /**
     * Método para actualizar un registro existente en la tabla 'funcionarios'.
     */
    @Override
    public void update(Funcionario funcionario) throws DAOException {
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(UPDATE_SQL)) {

            // Asignamos los nuevos valores
            pstmt.setString(1, funcionario.getTipoIdentificacion());
            pstmt.setString(2, funcionario.getNumeroIdentificacion());
            pstmt.setString(3, funcionario.getNombres());
            pstmt.setString(4, funcionario.getApellidos());
            pstmt.setString(5, funcionario.getEstadoCivil());
            pstmt.setString(6, funcionario.getSexo());
            pstmt.setString(7, funcionario.getDireccion());
            pstmt.setString(8, funcionario.getTelefono());
            pstmt.setDate(9, new java.sql.Date(funcionario.getFechaNacimiento().getTime()));
            pstmt.setInt(10, funcionario.getIdFuncionario()); // El ID en la cláusula WHERE

            int affectedRows = pstmt.executeUpdate(); // Ejecutamos la actualización
            if (affectedRows == 0) {
                // Si no se afectó ninguna fila, puede que el ID no exista.
                System.err.println("Advertencia: No se afectaron filas al actualizar funcionario con ID: " + funcionario.getIdFuncionario() + ". El ID podría no existir.");
                // Podrías lanzar una excepción aquí si quieres ser más estricto:
                // throw new DAOException("No se encontró el funcionario con ID " + funcionario.getIdFuncionario() + " para actualizar.");
            } else {
                System.out.println("Funcionario con ID " + funcionario.getIdFuncionario() + " actualizado.");
            }
        } catch (SQLException e) {
            System.err.println("Error SQL al actualizar funcionario ID " + funcionario.getIdFuncionario() + ": " + e.getMessage());
            throw new DAOException("Error al actualizar el funcionario en la base de datos.", e);
        }
    }

    /**
     * Método para eliminar un registro de la tabla 'funcionarios' por su ID.
     */
    @Override
    public void delete(int id) throws DAOException {
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(DELETE_SQL)) {

            pstmt.setInt(1, id); // Asignamos el ID a la consulta
            int affectedRows = pstmt.executeUpdate(); // Ejecutamos la eliminación
            if (affectedRows == 0) {
                System.err.println("Advertencia: No se afectaron filas al eliminar funcionario con ID: " + id + ". El ID podría no existir.");
                // throw new DAOException("No se encontró el funcionario con ID " + id + " para eliminar.");
            } else {
                System.out.println("Funcionario con ID " + id + " eliminado.");
            }
        } catch (SQLException e) {
            System.err.println("Error SQL al eliminar funcionario ID " + id + ": " + e.getMessage());
            throw new DAOException("Error al eliminar el funcionario de la base de datos.", e);
        }
    }

    /**
     * Método para obtener todos los registros de la tabla 'funcionarios'.
     */
    @Override
    public List<Funcionario> getAll() throws DAOException {
        List<Funcionario> funcionarios = new ArrayList<>(); // Creamos una lista para guardar los resultados
        try (Connection conn = DatabaseUtil.getConnection();
             Statement stmt = conn.createStatement(); // Usamos Statement porque no hay parámetros
             ResultSet rs = stmt.executeQuery(SELECT_ALL_SQL)) { // Ejecutamos la consulta

            while (rs.next()) { // Recorremos cada fila del resultado
                funcionarios.add(mapResultSetToFuncionario(rs)); // Mapeamos y añadimos a la lista
            }
        } catch (SQLException e) {
            System.err.println("Error SQL al obtener todos los funcionarios: " + e.getMessage());
            throw new DAOException("Error al obtener la lista de funcionarios de la base de datos.", e);
        }
        return funcionarios; // Devolvemos la lista completa
    }

    /**
     * Método para buscar funcionarios por número de identificación.
     */
    @Override
    public List<Funcionario> findByNumeroIdentificacion(String numeroIdentificacion) throws DAOException {
        List<Funcionario> funcionarios = new ArrayList<>();
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(FIND_BY_NUMERO_IDENTIFICACION_SQL)) {

            // Usamos LIKE con '%' para permitir búsquedas parciales (ej. buscar "1000" y que encuentre "100000001")
            pstmt.setString(1, "%" + numeroIdentificacion + "%");
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    funcionarios.add(mapResultSetToFuncionario(rs));
                }
            }
        } catch (SQLException e) {
            System.err.println("Error SQL al buscar funcionarios por número de identificación '" + numeroIdentificacion + "': " + e.getMessage());
            throw new DAOException("Error al buscar funcionarios por número de identificación.", e);
        }
        return funcionarios;
    }

    /**
     * Método auxiliar (privado) para convertir una fila de ResultSet en un objeto Funcionario.
     * Esto evita repetir el mismo código en varios métodos.
     * @param rs El ResultSet de la consulta.
     * @return Un objeto Funcionario.
     * @throws SQLException Si ocurre un error al leer del ResultSet.
     */
    private Funcionario mapResultSetToFuncionario(ResultSet rs) throws SQLException {
        Funcionario funcionario = new Funcionario();
        // Obtenemos los datos de cada columna y los asignamos a los atributos del objeto Funcionario
        funcionario.setIdFuncionario(rs.getInt("id_funcionario"));
        funcionario.setTipoIdentificacion(rs.getString("tipo_identificacion"));
        funcionario.setNumeroIdentificacion(rs.getString("numero_identificacion"));
        funcionario.setNombres(rs.getString("nombres"));
        funcionario.setApellidos(rs.getString("apellidos"));
        funcionario.setEstadoCivil(rs.getString("estado_civil"));
        funcionario.setSexo(rs.getString("sexo"));
        funcionario.setDireccion(rs.getString("direccion"));
        funcionario.setTelefono(rs.getString("telefono"));
        funcionario.setFechaNacimiento(rs.getDate("fecha_nacimiento")); // getDate() devuelve java.sql.Date, que es compatible con java.util.Date

        // Nota: Aquí NO cargamos las relaciones (GrupoFamiliar, InfoAcademica)
        // porque este DAO es solo para la tabla 'funcionarios'.
        // Si quisiéramos cargar las relaciones, necesitaríamos llamar a otros DAOs aquí,
        // o hacerlo en la capa de Servicio.
        return funcionario;
    }
}
