package com.iud.gestionfuncionarios.dao.impl;

import com.iud.gestionfuncionarios.dao.GrupoFamiliarDAO;
import com.iud.gestionfuncionarios.exceptions.DAOException;
import com.iud.gestionfuncionarios.model.GrupoFamiliar;
import com.iud.gestionfuncionarios.util.DatabaseUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class GrupoFamiliarDAOImpl implements GrupoFamiliarDAO {

    private static final String INSERT_SQL = "INSERT INTO grupo_familiar (id_funcionario, nombre_miembro, apellido_miembro, parentesco) VALUES (?, ?, ?, ?, ?)";
    private static final String SELECT_BY_ID_SQL = "SELECT id_miembro_familiar, id_funcionario, nombre_miembro, apellido_miembro, parentesco, FROM grupo_familiar WHERE id_miembro_familiar = ?";
    private static final String UPDATE_SQL = "UPDATE grupo_familiar SET id_funcionario = ?, nombre_miembro = ?, apellido_miembro = ?, parentesco = ?,  WHERE id_miembro_familiar = ?";
    private static final String DELETE_SQL = "DELETE FROM grupo_familiar WHERE id_miembro_familiar = ?";
    private static final String SELECT_ALL_SQL = "SELECT id_miembro_familiar, id_funcionario, nombre_miembro, apellido_miembro, parentesco, FROM grupo_familiar";
    private static final String FIND_BY_FUNCIONARIO_ID_SQL = "SELECT id_miembro_familiar, id_funcionario, nombre_miembro, apellido_miembro, parentesco,  FROM grupo_familiar WHERE id_funcionario = ?";

    @Override
    public void create(GrupoFamiliar grupoFamiliar) throws DAOException {
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(INSERT_SQL, Statement.RETURN_GENERATED_KEYS)) {

            pstmt.setInt(1, grupoFamiliar.getIdFuncionario());
            pstmt.setString(2, grupoFamiliar.getNombreMiembro());
            pstmt.setString(3, grupoFamiliar.getApellidoMiembro());
            pstmt.setString(4, grupoFamiliar.getParentesco());

            int affectedRows = pstmt.executeUpdate();
            if (affectedRows == 0) {
                throw new DAOException("La creación del miembro familiar falló, no se afectaron filas.");
            }

            try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    grupoFamiliar.setIdMiembroFamiliar(generatedKeys.getInt(1));
                } else {
                    throw new DAOException("La creación del miembro familiar falló y no se pudo obtener el ID.");
                }
            }
            System.out.println("Miembro familiar creado con ID: " + grupoFamiliar.getIdMiembroFamiliar());

        } catch (SQLException e) {
            System.err.println("Error SQL al crear miembro familiar: " + e.getMessage());
            throw new DAOException("Error al crear el miembro familiar en la base de datos.", e);
        }
    }

    @Override
    public GrupoFamiliar read(int id) throws DAOException {
        GrupoFamiliar miembro = null;
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(SELECT_BY_ID_SQL)) {

            pstmt.setInt(1, id);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    miembro = mapResultSetToGrupoFamiliar(rs);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error SQL al leer miembro familiar por ID " + id + ": " + e.getMessage());
            throw new DAOException("Error al leer el miembro familiar de la base de datos.", e);
        }
        return miembro;
    }

    @Override
    public void update(GrupoFamiliar grupoFamiliar) throws DAOException {
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(UPDATE_SQL)) {

            pstmt.setInt(1, grupoFamiliar.getIdFuncionario());
            pstmt.setString(2, grupoFamiliar.getNombreMiembro());
            pstmt.setString(3, grupoFamiliar.getApellidoMiembro());
            pstmt.setString(4, grupoFamiliar.getParentesco());
            pstmt.setInt(6, grupoFamiliar.getIdMiembroFamiliar()); // WHERE clause

            int affectedRows = pstmt.executeUpdate();
            if (affectedRows == 0) {
                System.err.println("Advertencia: No se afectaron filas al actualizar miembro familiar ID: " + grupoFamiliar.getIdMiembroFamiliar());
            } else {
                 System.out.println("Miembro familiar con ID " + grupoFamiliar.getIdMiembroFamiliar() + " actualizado.");
            }
        } catch (SQLException e) {
            System.err.println("Error SQL al actualizar miembro familiar ID " + grupoFamiliar.getIdMiembroFamiliar() + ": " + e.getMessage());
            throw new DAOException("Error al actualizar el miembro familiar en la base de datos.", e);
        }
    }

    @Override
    public void delete(int id) throws DAOException {
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(DELETE_SQL)) {

            pstmt.setInt(1, id);
            int affectedRows = pstmt.executeUpdate();
            if (affectedRows == 0) {
                System.err.println("Advertencia: No se afectaron filas al eliminar miembro familiar ID: " + id);
            } else {
                 System.out.println("Miembro familiar con ID " + id + " eliminado.");
            }
        } catch (SQLException e) {
            System.err.println("Error SQL al eliminar miembro familiar ID " + id + ": " + e.getMessage());
            throw new DAOException("Error al eliminar el miembro familiar de la base de datos.", e);
        }
    }

    @Override
    public List<GrupoFamiliar> getAll() throws DAOException {
        List<GrupoFamiliar> miembros = new ArrayList<>();
        try (Connection conn = DatabaseUtil.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(SELECT_ALL_SQL)) {

            while (rs.next()) {
                miembros.add(mapResultSetToGrupoFamiliar(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error SQL al obtener todos los miembros familiares: " + e.getMessage());
            throw new DAOException("Error al obtener la lista de miembros familiares de la base de datos.", e);
        }
        return miembros;
    }

    @Override
    public List<GrupoFamiliar> findByFuncionarioId(int funcionarioId) throws DAOException {
        List<GrupoFamiliar> miembros = new ArrayList<>();
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(FIND_BY_FUNCIONARIO_ID_SQL)) {

            pstmt.setInt(1, funcionarioId);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    miembros.add(mapResultSetToGrupoFamiliar(rs));
                }
            }
        } catch (SQLException e) {
            System.err.println("Error SQL al buscar miembros familiares para funcionario ID " + funcionarioId + ": " + e.getMessage());
            throw new DAOException("Error al buscar miembros familiares por ID de funcionario.", e);
        }
        return miembros;
    }

    private GrupoFamiliar mapResultSetToGrupoFamiliar(ResultSet rs) throws SQLException {
        GrupoFamiliar miembro = new GrupoFamiliar();
        miembro.setIdMiembroFamiliar(rs.getInt("id_miembro_familiar"));
        miembro.setIdFuncionario(rs.getInt("id_funcionario"));
        miembro.setNombreMiembro(rs.getString("nombre_miembro"));
        miembro.setApellidoMiembro(rs.getString("apellido_miembro"));
        miembro.setParentesco(rs.getString("parentesco"));
        return miembro;
    }
}
