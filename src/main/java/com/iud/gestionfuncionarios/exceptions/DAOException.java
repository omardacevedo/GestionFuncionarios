package com.iud.gestionfuncionarios.exceptions;

// Esta clase representa un error que ocurre al intentar acceder o manipular la base de datos.
public class DAOException extends Exception {

    // Constructor que recibe un mensaje de error
    public DAOException(String message) {
        super(message); // Llama al constructor de la clase padre (Exception)
    }

    // Constructor que recibe un mensaje y la causa original del error (otra excepción)
    public DAOException(String message, Throwable cause) {
        super(message, cause); // Llama al constructor de la clase padre
    }
}
