package com.iud.gestionfuncionarios.dao.impl;

import com.iud.gestionfuncionarios.dao.InformacionAcademicaDAO;
import com.iud.gestionfuncionarios.exceptions.DAOException;
import com.iud.gestionfuncionarios.model.InformacionAcademica;
import com.iud.gestionfuncionarios.util.DatabaseUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class InformacionAcademicaDAOImpl implements InformacionAcademicaDAO {

    private static final String INSERT_SQL = "INSERT INTO informacion_academica (id_funcionario, universidad, nivel_estudio, titulo_estudio) VALUES (?, ?, ?, ?)";
    private static final String SELECT_BY_ID_SQL = "SELECT id_estudio, id_funcionario, universidad, nivel_estudio, titulo_estudio FROM informacion_academica WHERE id_estudio = ?";
    private static final String UPDATE_SQL = "UPDATE informacion_academica SET id_funcionario = ?, universidad = ?, nivel_estudio = ?, titulo_estudio = ? WHERE id_estudio = ?";
    private static final String DELETE_SQL = "DELETE FROM informacion_academica WHERE id_estudio = ?";
    private static final String SELECT_ALL_SQL = "SELECT id_estudio, id_funcionario, universidad, nivel_estudio, titulo_estudio FROM informacion_academica";
    private static final String FIND_BY_FUNCIONARIO_ID_SQL = "SELECT id_estudio, id_funcionario, universidad, nivel_estudio, titulo_estudio FROM informacion_academica WHERE id_funcionario = ?";

    @Override
    public void create(InformacionAcademica infoAcademica) throws DAOException {
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(INSERT_SQL, Statement.RETURN_GENERATED_KEYS)) {

            pstmt.setInt(1, infoAcademica.getIdFuncionario());
            pstmt.setString(2, infoAcademica.getUniversidad());
            pstmt.setString(3, infoAcademica.getNivelEstudio());
            pstmt.setString(4, infoAcademica.getTituloEstudio());

            int affectedRows = pstmt.executeUpdate();
            if (affectedRows == 0) {
                throw new DAOException("La creación de la información académica falló, no se afectaron filas.");
            }

            try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    infoAcademica.setIdEstudio(generatedKeys.getInt(1));
                } else {
                    throw new DAOException("La creación de la información académica falló y no se pudo obtener el ID.");
                }
            }
             System.out.println("Información académica creada con ID: " + infoAcademica.getIdEstudio());

        } catch (SQLException e) {
            System.err.println("Error SQL al crear información académica: " + e.getMessage());
            throw new DAOException("Error al crear la información académica en la base de datos.", e);
        }
    }

    @Override
    public InformacionAcademica read(int id) throws DAOException {
        InformacionAcademica info = null;
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(SELECT_BY_ID_SQL)) {

            pstmt.setInt(1, id);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    info = mapResultSetToInformacionAcademica(rs);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error SQL al leer información académica por ID " + id + ": " + e.getMessage());
            throw new DAOException("Error al leer la información académica de la base de datos.", e);
        }
        return info;
    }

    @Override
    public void update(InformacionAcademica infoAcademica) throws DAOException {
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(UPDATE_SQL)) {

            pstmt.setInt(1, infoAcademica.getIdFuncionario());
            pstmt.setString(2, infoAcademica.getUniversidad());
            pstmt.setString(3, infoAcademica.getNivelEstudio());
            pstmt.setString(4, infoAcademica.getTituloEstudio());
            pstmt.setInt(5, infoAcademica.getIdEstudio()); // WHERE clause

            int affectedRows = pstmt.executeUpdate();
            if (affectedRows == 0) {
                System.err.println("Advertencia: No se afectaron filas al actualizar información académica ID: " + infoAcademica.getIdEstudio());
            } else {
                 System.out.println("Información académica con ID " + infoAcademica.getIdEstudio() + " actualizada.");
            }
        } catch (SQLException e) {
            System.err.println("Error SQL al actualizar información académica ID " + infoAcademica.getIdEstudio() + ": " + e.getMessage());
            throw new DAOException("Error al actualizar la información académica en la base de datos.", e);
        }
    }

    @Override
    public void delete(int id) throws DAOException {
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(DELETE_SQL)) {

            pstmt.setInt(1, id);
            int affectedRows = pstmt.executeUpdate();
            if (affectedRows == 0) {
                System.err.println("Advertencia: No se afectaron filas al eliminar información académica ID: " + id);
            } else {
                 System.out.println("Información académica con ID " + id + " eliminada.");
            }
        } catch (SQLException e) {
            System.err.println("Error SQL al eliminar información académica ID " + id + ": " + e.getMessage());
            throw new DAOException("Error al eliminar la información académica de la base de datos.", e);
        }
    }

    @Override
    public List<InformacionAcademica> getAll() throws DAOException {
        List<InformacionAcademica> estudios = new ArrayList<>();
        try (Connection conn = DatabaseUtil.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(SELECT_ALL_SQL)) {

            while (rs.next()) {
                estudios.add(mapResultSetToInformacionAcademica(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error SQL al obtener toda la información académica: " + e.getMessage());
            throw new DAOException("Error al obtener la lista de información académica de la base de datos.", e);
        }
        return estudios;
    }

    @Override
    public List<InformacionAcademica> findByFuncionarioId(int funcionarioId) throws DAOException {
        List<InformacionAcademica> estudios = new ArrayList<>();
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(FIND_BY_FUNCIONARIO_ID_SQL)) {

            pstmt.setInt(1, funcionarioId);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    estudios.add(mapResultSetToInformacionAcademica(rs));
                }
            }
        } catch (SQLException e) {
            System.err.println("Error SQL al buscar información académica para funcionario ID " + funcionarioId + ": " + e.getMessage());
            throw new DAOException("Error al buscar información académica por ID de funcionario.", e);
        }
        return estudios;
    }

    private InformacionAcademica mapResultSetToInformacionAcademica(ResultSet rs) throws SQLException {
        InformacionAcademica info = new InformacionAcademica();
        info.setIdEstudio(rs.getInt("id_estudio"));
        info.setIdFuncionario(rs.getInt("id_funcionario"));
        info.setUniversidad(rs.getString("universidad"));
        info.setNivelEstudio(rs.getString("nivel_estudio"));
        info.setTituloEstudio(rs.getString("titulo_estudio"));
        return info;
    }
}
