package com.iud.gestionfuncionarios.ui;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

public class MainFrame extends JFrame {

    public MainFrame() {
        // Configuración básica de la ventana principal
        setTitle("Sistema de Gestión de Recursos Humanos"); // Título de la ventana
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Acción al cerrar
        setSize(900, 600); // Tamaño inicial de la ventana
        setLocationRelativeTo(null); // Centrar en pantalla

        // Creamos una instancia de nuestro formulario y lo añadimos a la ventana
        FuncionarioForm funcionarioPanel = new FuncionarioForm();
        getContentPane().add(funcionarioPanel); // Añade el panel al contenido del JFrame

        // Si quieres que el tamaño se ajuste al contenido (puede ser pequeño si no hay mucho)
        // pack();
    }

    public static void main(String[] args) {
        // Ejecutar la creación de la GUI en el hilo de eventos de Swing
        SwingUtilities.invokeLater(() -> {
            MainFrame frame = new MainFrame();
            frame.setVisible(true); // Mostrar la ventana
        });
    }
}
