package com.iud.gestionfuncionarios.service;

import com.iud.gestionfuncionarios.dao.FuncionarioDAO;
import com.iud.gestionfuncionarios.dao.impl.FuncionarioDAOImpl;
// Importamos los DAOs de las relaciones si los vamos a usar aquí
// import com.iud.gestionfuncionarios.dao.GrupoFamiliarDAO;
// import com.iud.gestionfuncionarios.dao.impl.GrupoFamiliarDAOImpl;
// import com.iud.gestionfuncionarios.dao.InformacionAcademicaDAO;
// import com.iud.gestionfuncionarios.dao.impl.InformacionAcademicaDAOImpl;

import com.iud.gestionfuncionarios.exceptions.BusinessLogicException;
import com.iud.gestionfuncionarios.exceptions.DAOException;
import com.iud.gestionfuncionarios.model.Funcionario;

import java.util.List;

// Esta capa maneja la lógica de negocio.
// Aquí se aplican reglas, se validan datos y se coordinan las operaciones de los DAOs.
public class FuncionarioService {

    // Creamos una instancia del DAO. En aplicaciones más grandes, esto se haría con Inyección de Dependencias.
    private FuncionarioDAO funcionarioDAO = new FuncionarioDAOImpl();
    // private GrupoFamiliarDAO grupoFamiliarDAO = new GrupoFamiliarDAOImpl();
    // private InformacionAcademicaDAO infoAcademicaDAO = new InformacionAcademicaDAOImpl();

    /**
     * Crea un nuevo funcionario, aplicando validaciones de negocio.
     */
    public void crearFuncionario(Funcionario funcionario) throws BusinessLogicException {
        try {
            // --- Validaciones de Negocio ---
            if (funcionario.getNumeroIdentificacion() == null || funcionario.getNumeroIdentificacion().trim().isEmpty()) {
                throw new BusinessLogicException("El número de identificación es obligatorio.");
            }
            if (funcionario.getNombres() == null || funcionario.getNombres().trim().isEmpty()) {
                throw new BusinessLogicException("Los nombres son obligatorios.");
            }
            if (funcionario.getApellidos() == null || funcionario.getApellidos().trim().isEmpty()) {
                throw new BusinessLogicException("Los apellidos son obligatorios.");
            }
            // Podrías añadir más validaciones aquí (ej. formato de fecha, longitud de campos, etc.)

            // Opcional: Verificar si ya existe un funcionario con ese número de identificación
            // (Aunque la base de datos ya tiene una restricción UNIQUE, es bueno validarlo antes)
            List<Funcionario> existentes = funcionarioDAO.findByNumeroIdentificacion(funcionario.getNumeroIdentificacion());
            if (!existentes.isEmpty()) {
                // Si encontramos alguno y no es el mismo funcionario que estamos intentando actualizar (si fuera el caso)
                // En creación, siempre será un error si ya existe.
                throw new BusinessLogicException("Ya existe un funcionario con el número de identificación: " + funcionario.getNumeroIdentificacion());
            }

            // Si todas las validaciones pasan, llamamos al DAO para crear el funcionario
            funcionarioDAO.create(funcionario);
            System.out.println("Funcionario creado exitosamente en el servicio.");

            // Si tuvieras que guardar también GrupoFamiliar o InfoAcademica aquí, lo harías ahora.
            // Ejemplo:
            // if (funcionario.getGrupoFamiliar() != null) {
            //     for (GrupoFamiliar miembro : funcionario.getGrupoFamiliar()) {
            //         miembro.setIdFuncionario(funcionario.getIdFuncionario()); // Asegurarse de que la FK esté correcta
            //         grupoFamiliarDAO.create(miembro);
            //     }
            // }

        } catch (DAOException e) {
            // Si el DAO lanza un error, lo envolvemos en una BusinessLogicException
            System.err.println("Error DAO al intentar crear funcionario: " + e.getMessage());
            throw new BusinessLogicException("Error interno al intentar crear el funcionario.", e);
        }
    }

    /**
     * Obtiene un funcionario por su ID, incluyendo sus datos relacionados si es necesario.
     */
    public Funcionario obtenerFuncionarioPorId(int id) throws BusinessLogicException {
        try {
            Funcionario f = funcionarioDAO.read(id);
            if (f == null) {
                // Si el DAO no encontró nada, lanzamos una excepción de negocio.
                throw new BusinessLogicException("Funcionario con ID " + id + " no encontrado.");
            }

            // --- Cargar datos relacionados (Opcional, pero útil) ---
            // Si quieres que el objeto Funcionario devuelto tenga también su grupo familiar y estudios:
            // List<GrupoFamiliar> familia = grupoFamiliarDAO.findByFuncionarioId(id);
            // f.setGrupoFamiliar(familia);
            // List<InformacionAcademica> estudios = infoAcademicaDAO.findByFuncionarioId(id);
            // f.setInformacionAcademica(estudios);

            return f;
        } catch (DAOException e) {
            System.err.println("Error DAO al obtener funcionario por ID " + id + ": " + e.getMessage());
            throw new BusinessLogicException("Error al obtener el funcionario con ID " + id, e);
        }
    }

    /**
     * Actualiza un funcionario existente, aplicando validaciones.
     */
    public void actualizarFuncionario(Funcionario funcionario) throws BusinessLogicException {
        try {
            // --- Validaciones de Negocio para Actualización ---
            if (funcionario.getIdFuncionario() <= 0) {
                throw new BusinessLogicException("El ID del funcionario es inválido para actualizar.");
            }
            // Revalidar campos obligatorios si es necesario
            if (funcionario.getNumeroIdentificacion() == null || funcionario.getNumeroIdentificacion().trim().isEmpty() ||
                funcionario.getNombres() == null || funcionario.getNombres().trim().isEmpty() ||
                funcionario.getApellidos() == null || funcionario.getApellidos().trim().isEmpty()) {
                throw new BusinessLogicException("Los campos Número de Identificación, Nombres y Apellidos no pueden estar vacíos.");
            }

            // Opcional: Verificar si el número de identificación ha cambiado y si el nuevo ya existe para otro funcionario.
            // Esto es más complejo y a menudo se deja a la restricción UNIQUE de la base de datos.

            // Llamamos al DAO para actualizar
            funcionarioDAO.update(funcionario);
            System.out.println("Funcionario con ID " + funcionario.getIdFuncionario() + " actualizado en el servicio.");

            // Aquí también podrías manejar la actualización de las relaciones (GrupoFamiliar, InfoAcademica)
            // Esto implicaría eliminar las antiguas y crear/actualizar las nuevas, o usar métodos de update específicos.

        } catch (DAOException e) {
            System.err.println("Error DAO al actualizar funcionario ID " + funcionario.getIdFuncionario() + ": " + e.getMessage());
            throw new BusinessLogicException("Error al actualizar el funcionario.", e);
        }
    }

    /**
     * Elimina un funcionario por su ID.
     */
    public void eliminarFuncionario(int id) throws BusinessLogicException {
        try {
            // --- Lógica de Negocio para Eliminación ---
            // Podrías querer verificar si el funcionario tiene dependencias críticas que impidan su eliminación,
            // o si debe eliminarse primero su grupo familiar e información académica.
            // Si las FKs en la base de datos tienen ON DELETE CASCADE, la BD se encargará de eliminar las dependencias.
            // Si no, deberías eliminarlas explícitamente aquí antes de eliminar el funcionario.
            // Ejemplo (si no usas CASCADE):
            // grupoFamiliarDAO.deleteByFuncionarioId(id); // Necesitarías añadir este método al DAO
            // infoAcademicaDAO.deleteByFuncionarioId(id); // Necesitarías añadir este método al DAO

            funcionarioDAO.delete(id);
            System.out.println("Funcionario con ID " + id + " eliminado en el servicio.");

        } catch (DAOException e) {
            System.err.println("Error DAO al eliminar funcionario ID " + id + ": " + e.getMessage());
            throw new BusinessLogicException("Error al eliminar el funcionario.", e);
        }
    }

    /**
     * Obtiene una lista de todos los funcionarios.
     */
    public List<Funcionario> obtenerTodosLosFuncionarios() throws BusinessLogicException {
        try {
            return funcionarioDAO.getAll();
        } catch (DAOException e) {
            System.err.println("Error DAO al obtener todos los funcionarios: " + e.getMessage());
            throw new BusinessLogicException("Error al obtener la lista de funcionarios.", e);
        }
    }

    /**
     * Busca funcionarios por número de identificación.
     */
    public List<Funcionario> buscarFuncionariosPorIdentificacion(String numeroIdentificacion) throws BusinessLogicException {
        try {
            if (numeroIdentificacion == null || numeroIdentificacion.trim().isEmpty()) {
                // Si la búsqueda está vacía, podrías devolver todos o lanzar un error.
                // Devolver todos es más amigable para la UI si el campo de búsqueda está vacío.
                return obtenerTodosLosFuncionarios();
                // O podrías lanzar: throw new BusinessLogicException("El número de identificación para buscar no puede estar vacío.");
            }
            return funcionarioDAO.findByNumeroIdentificacion(numeroIdentificacion);
        } catch (DAOException e) {
            System.err.println("Error DAO al buscar funcionarios por identificación '" + numeroIdentificacion + "': " + e.getMessage());
            throw new BusinessLogicException("Error al buscar funcionarios por número de identificación.", e);
        }
    }
}
