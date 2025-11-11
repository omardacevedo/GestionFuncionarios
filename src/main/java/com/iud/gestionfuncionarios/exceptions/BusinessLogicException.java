package com.iud.gestionfuncionarios.exceptions;

// Esta clase representa un error relacionado con las reglas de negocio de tu aplicación.
// Por ejemplo, intentar guardar un funcionario sin nombre.
public class BusinessLogicException extends Exception {

    // Constructor que recibe un mensaje de error
    public BusinessLogicException(String message) {
        super(message);
    }

    // Constructor que recibe un mensaje y la causa original del error
    public BusinessLogicException(String message, Throwable cause) {
        super(message, cause);
    }
}