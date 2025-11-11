package com.iud.gestionfuncionarios.ui;

import com.iud.gestionfuncionarios.exceptions.BusinessLogicException;
import com.iud.gestionfuncionarios.model.Funcionario;
import com.iud.gestionfuncionarios.service.FuncionarioService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Vector;

public class FuncionarioForm extends JPanel {

    private FuncionarioService funcionarioService = new FuncionarioService();
    private JTable tableFuncionarios;
    private DefaultTableModel tableModel;
    private JTextField txtId;
    private JTextField txtTipoIdentificacion;
    private JTextField txtNumeroIdentificacion;
    private JTextField txtNombres;
    private JTextField txtApellidos;
    private JTextField txtEstadoCivil;
    private JTextField txtSexo;
    private JTextField txtDireccion;
    private JTextField txtTelefono;
    private JTextField txtFechaNacimiento; // Usar JFormattedTextField o DatePicker para mejor manejo

    private JButton btnNuevo;
    private JButton btnGuardar;
    private JButton btnActualizar;
    private JButton btnEliminar;
    private JButton btnLimpiar;
    private JButton btnBuscar;
    private JTextField txtBuscarId; // Campo para buscar por ID

    private int selectedFuncionarioId = -1; // Para saber qué funcionario se está editando/eliminando

    public FuncionarioForm() {
        setLayout(new BorderLayout(10, 10)); // Layout principal: BorderLayout
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10)); // Borde alrededor del panel

        // --- Panel Izquierdo: Formulario y Botones ---
        JPanel leftPanel = new JPanel(new BorderLayout(10, 10));
        leftPanel.setPreferredSize(new Dimension(450, 500)); // Tamaño preferido para el panel izquierdo

        // Panel del Formulario (Grid para organizar campos)
        JPanel formPanel = new JPanel(new GridLayout(0, 2, 5, 5)); // 0 filas, 2 columnas, con espaciado
        formPanel.setBorder(BorderFactory.createTitledBorder("Datos del Funcionario")); // Título para el panel

        // Campos del formulario
        formPanel.add(new JLabel("ID:"));
        txtId = new JTextField();
        txtId.setEditable(false); // El ID no se edita directamente
        formPanel.add(txtId);

        formPanel.add(new JLabel("Tipo Identificación:"));
        txtTipoIdentificacion = new JTextField();
        formPanel.add(txtTipoIdentificacion);

        formPanel.add(new JLabel("Número Identificación:"));
        txtNumeroIdentificacion = new JTextField();
        formPanel.add(txtNumeroIdentificacion);

        formPanel.add(new JLabel("Nombres:"));
        txtNombres = new JTextField();
        formPanel.add(txtNombres);

        formPanel.add(new JLabel("Apellidos:"));
        txtApellidos = new JTextField();
        formPanel.add(txtApellidos);

        formPanel.add(new JLabel("Estado Civil:"));
        txtEstadoCivil = new JTextField();
        formPanel.add(txtEstadoCivil);

        formPanel.add(new JLabel("Sexo:"));
        txtSexo = new JTextField();
        formPanel.add(txtSexo);

        formPanel.add(new JLabel("Dirección:"));
        txtDireccion = new JTextField();
        formPanel.add(txtDireccion);

        formPanel.add(new JLabel("Teléfono:"));
        txtTelefono = new JTextField();
        formPanel.add(txtTelefono);

        formPanel.add(new JLabel("Fecha Nacimiento (YYYY-MM-DD):"));
        txtFechaNacimiento = new JTextField();
        formPanel.add(txtFechaNacimiento);

        // Panel de Botones del Formulario (FlowLayout para agruparlos)
        JPanel formButtonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        btnNuevo = new JButton("Nuevo");
        btnGuardar = new JButton("Guardar");
        btnActualizar = new JButton("Actualizar");
        btnEliminar = new JButton("Eliminar");
        btnLimpiar = new JButton("Limpiar Campos");

        formButtonPanel.add(btnNuevo);
        formButtonPanel.add(btnGuardar);
        formButtonPanel.add(btnActualizar);
        formButtonPanel.add(btnEliminar);
        formButtonPanel.add(btnLimpiar);

        // Panel de Búsqueda (FlowLayout)
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        searchPanel.add(new JLabel("Buscar por ID:"));
        txtBuscarId = new JTextField(10); // Campo para ingresar el ID a buscar
        btnBuscar = new JButton("Buscar");
        searchPanel.add(txtBuscarId);
        searchPanel.add(btnBuscar);

        // Añadimos los paneles al panel izquierdo
        leftPanel.add(formPanel, BorderLayout.NORTH); // Formulario arriba
        leftPanel.add(formButtonPanel, BorderLayout.CENTER); // Botones en el centro
        leftPanel.add(searchPanel, BorderLayout.SOUTH); // Búsqueda abajo

        // --- Panel Derecho: Tabla de Funcionarios ---
        JPanel rightPanel = new JPanel(new BorderLayout());
        rightPanel.setBorder(BorderFactory.createTitledBorder("Listado de Funcionarios"));

        // Configuración del modelo de la tabla
        String[] columnNames = {"ID", "Tipo ID", "Número ID", "Nombres", "Apellidos", "Teléfono"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Hace que las celdas no sean editables directamente
            }
        };
        tableFuncionarios = new JTable(tableModel);
        tableFuncionarios.setSelectionMode(ListSelectionModel.SINGLE_SELECTION); // Solo se puede seleccionar una fila a la vez

        // Listener para detectar clics en la tabla y cargar datos en el formulario
        tableFuncionarios.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent me) {
                if (me.getClickCount() == 1 && tableFuncionarios.getSelectedRow() != -1) { // Clic simple y fila seleccionada
                    JTable target = (JTable) me.getSource();
                    int row = target.getSelectedRow();
                    selectedFuncionarioId = (int) tableModel.getValueAt(row, 0); // Obtenemos el ID de la primera columna
                    cargarDatosFuncionario(selectedFuncionarioId); // Cargamos los datos en el formulario
                    // Habilitamos botones de actualizar/eliminar y deshabilitamos guardar
                    btnActualizar.setEnabled(true);
                    btnEliminar.setEnabled(true);
                    btnGuardar.setEnabled(false);
                    btnNuevo.setEnabled(false); // Deshabilitamos Nuevo al seleccionar para editar
                }
            }
        });

        JScrollPane scrollPane = new JScrollPane(tableFuncionarios); // Añadimos scroll a la tabla
        rightPanel.add(scrollPane, BorderLayout.CENTER); // La tabla ocupa el centro del panel derecho

        // --- Ensamblaje Final ---
        add(leftPanel, BorderLayout.WEST); // Panel izquierdo (formulario) a la izquierda
        add(rightPanel, BorderLayout.CENTER); // Panel derecho (tabla) al centro

        // Configurar listeners de botones
        setupButtonListeners();

        // Cargar datos iniciales en la tabla al iniciar la aplicación
        cargarTablaFuncionarios();
        // Configurar el estado inicial de los botones
        habilitarDeshabilitarBotones(true); // Estado inicial: Nuevo y Limpiar habilitados
        btnActualizar.setEnabled(false); // Deshabilitar Actualizar/Eliminar al inicio
        btnEliminar.setEnabled(false);
        btnGuardar.setEnabled(false); // Deshabilitar Guardar al inicio
    }

    /**
     * Configura los listeners (eventos) para cada botón.
     */
    private void setupButtonListeners() {
        btnNuevo.addActionListener(e -> nuevoFuncionario());
        btnGuardar.addActionListener(e -> guardarFuncionario());
        btnActualizar.addActionListener(e -> actualizarFuncionario());
        btnEliminar.addActionListener(e -> eliminarFuncionario());
        btnLimpiar.addActionListener(e -> limpiarFormulario());
        btnBuscar.addActionListener(e -> buscarFuncionarioPorId()); // Cambiado el nombre para mayor claridad
    }

    /**
     * Carga todos los funcionarios en la tabla.
     */
    private void cargarTablaFuncionarios() {
        limpiarTabla(); // Primero vaciamos la tabla
        try {
            List<Funcionario> funcionarios = funcionarioService.obtenerTodosLosFuncionarios();
            for (Funcionario f : funcionarios) {
                // Creamos una fila con los datos básicos para la tabla
                Vector<Object> row = new Vector<>();
                row.add(f.getIdFuncionario());
                row.add(f.getTipoIdentificacion());
                row.add(f.getNumeroIdentificacion());
                row.add(f.getNombres());
                row.add(f.getApellidos());
                row.add(f.getTelefono());
                tableModel.addRow(row); // Añadimos la fila al modelo de la tabla
            }
        } catch (BusinessLogicException e) {
            mostrarMensajeError("Error al cargar la lista de funcionarios: " + e.getMessage());
        }
    }

    /**
     * Vacía todas las filas de la tabla.
     */
    private void limpiarTabla() {
        while (tableModel.getRowCount() > 0) {
            tableModel.removeRow(0);
        }
    }

    /**
     * Carga los datos de un funcionario específico en los campos del formulario.
     * @param id El ID del funcionario a cargar.
     */
    private void cargarDatosFuncionario(int id) {
        try {
            Funcionario f = funcionarioService.obtenerFuncionarioPorId(id);
            if (f != null) {
                // Llenamos los campos del formulario con los datos del funcionario
                txtId.setText(String.valueOf(f.getIdFuncionario()));
                txtTipoIdentificacion.setText(f.getTipoIdentificacion());
                txtNumeroIdentificacion.setText(f.getNumeroIdentificacion());
                txtNombres.setText(f.getNombres());
                txtApellidos.setText(f.getApellidos());
                txtEstadoCivil.setText(f.getEstadoCivil());
                txtSexo.setText(f.getSexo());
                txtDireccion.setText(f.getDireccion());
                txtTelefono.setText(f.getTelefono());
                // Formateamos la fecha para mostrarla
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                txtFechaNacimiento.setText(f.getFechaNacimiento() != null ? sdf.format(f.getFechaNacimiento()) : "");

                // Actualizamos el ID seleccionado
                selectedFuncionarioId = id;
                // Habilitamos botones de edición/eliminación
                btnActualizar.setEnabled(true);
                btnEliminar.setEnabled(true);
                btnNuevo.setEnabled(false); // Deshabilitamos Nuevo al estar en modo edición
            }
        } catch (BusinessLogicException e) {
            mostrarMensajeError("Error al cargar datos del funcionario: " + e.getMessage());
            limpiarFormulario(); // Limpiamos el formulario si hay error
        }
    }

    /**
     * Limpia todos los campos del formulario y resetea el estado.
     */
    private void limpiarFormulario() {
        txtId.setText("");
        txtTipoIdentificacion.setText("");
        txtNumeroIdentificacion.setText("");
        txtNombres.setText("");
        txtApellidos.setText("");
        txtEstadoCivil.setText("");
        txtSexo.setText("");
        txtDireccion.setText("");
        txtTelefono.setText("");
        txtFechaNacimiento.setText("");
        txtBuscarId.setText(""); // Limpiamos también el campo de búsqueda
        selectedFuncionarioId = -1; // Reseteamos el ID seleccionado
        tableFuncionarios.clearSelection(); // Deseleccionamos cualquier fila en la tabla

        // Restauramos el estado inicial de los botones
        habilitarDeshabilitarBotones(true);
        btnActualizar.setEnabled(false);
        btnEliminar.setEnabled(false);
        btnGuardar.setEnabled(false); // Guardar se habilita solo al presionar Nuevo
    }

    /**
     * Prepara el formulario para ingresar un nuevo funcionario.
     */
    private void nuevoFuncionario() {
        limpiarFormulario(); // Limpiamos campos existentes
        // Habilitamos campos para entrada y el botón Guardar
        btnGuardar.setEnabled(true);
        btnNuevo.setEnabled(false); // Deshabilitamos Nuevo mientras se está creando
        btnActualizar.setEnabled(false);
        btnEliminar.setEnabled(false);
        txtTipoIdentificacion.requestFocus(); // Ponemos el foco en el primer campo editable
    }

    /**
     * Recopila los datos del formulario y llama al servicio para guardar un nuevo funcionario.
     */
    private void guardarFuncionario() {
        Funcionario f = new Funcionario();
        try {
            // Validaciones básicas antes de crear el objeto
            if (txtNombres.getText().trim().isEmpty() || txtApellidos.getText().trim().isEmpty() || txtNumeroIdentificacion.getText().trim().isEmpty()) {
                mostrarMensajeAdvertencia("Los campos Nombres, Apellidos y Número de Identificación son obligatorios.");
                return; // Salimos si faltan datos obligatorios
            }

            // Llenamos el objeto Funcionario con los datos del formulario
            f.setTipoIdentificacion(txtTipoIdentificacion.getText());
            f.setNumeroIdentificacion(txtNumeroIdentificacion.getText());
            f.setNombres(txtNombres.getText());
            f.setApellidos(txtApellidos.getText());
            f.setEstadoCivil(txtEstadoCivil.getText());
            f.setSexo(txtSexo.getText());
            f.setDireccion(txtDireccion.getText());
            f.setTelefono(txtTelefono.getText());

            // Manejo de fecha (simplificado)
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            try {
                if (!txtFechaNacimiento.getText().trim().isEmpty()) {
                    f.setFechaNacimiento(sdf.parse(txtFechaNacimiento.getText()));
                } else {
                    f.setFechaNacimiento(null); // Permitir fecha nula si está vacía
                }
            } catch (ParseException e) {
                mostrarMensajeAdvertencia("Formato de fecha inválido. Use YYYY-MM-DD.");
                return; // Salimos si la fecha es inválida
            }

            // Llamamos al servicio para crear el funcionario
            funcionarioService.crearFuncionario(f);
            mostrarMensajeExito("Funcionario guardado exitosamente.");

            // Actualizamos la tabla y limpiamos el formulario
            cargarTablaFuncionarios();
            limpiarFormulario();

        } catch (BusinessLogicException e) {
            // Mostramos cualquier error de lógica de negocio que ocurra
            mostrarMensajeError("Error al guardar funcionario: " + e.getMessage());
        }
    }

    /**
     * Recopila los datos del formulario (ya cargados) y llama al servicio para actualizar el funcionario.
     */
    private void actualizarFuncionario() {
        if (selectedFuncionarioId == -1) {
            mostrarMensajeAdvertencia("Por favor, seleccione un funcionario de la tabla para actualizar.");
            return;
        }

        Funcionario f = new Funcionario();
        try {
            // Asignamos el ID del funcionario que estamos editando
            f.setIdFuncionario(selectedFuncionarioId);

            // Llenamos el objeto Funcionario con los datos del formulario
            f.setTipoIdentificacion(txtTipoIdentificacion.getText());
            f.setNumeroIdentificacion(txtNumeroIdentificacion.getText());
            f.setNombres(txtNombres.getText());
            f.setApellidos(txtApellidos.getText());
            f.setEstadoCivil(txtEstadoCivil.getText());
            f.setSexo(txtSexo.getText());
            f.setDireccion(txtDireccion.getText());
            f.setTelefono(txtTelefono.getText());

            // Manejo de fecha
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            try {
                if (!txtFechaNacimiento.getText().trim().isEmpty()) {
                    f.setFechaNacimiento(sdf.parse(txtFechaNacimiento.getText()));
                } else {
                    f.setFechaNacimiento(null);
                }
            } catch (ParseException e) {
                mostrarMensajeAdvertencia("Formato de fecha inválido. Use YYYY-MM-DD.");
                return;
            }

            // Llamamos al servicio para actualizar
            funcionarioService.actualizarFuncionario(f);
            mostrarMensajeExito("Funcionario actualizado exitosamente.");

            // Actualizamos la tabla y limpiamos el formulario
            cargarTablaFuncionarios();
            limpiarFormulario();

        } catch (BusinessLogicException e) {
            mostrarMensajeError("Error al actualizar funcionario: " + e.getMessage());
        }
    }

    /**
     * Solicita confirmación y llama al servicio para eliminar el funcionario seleccionado.
     */
    private void eliminarFuncionario() {
        if (selectedFuncionarioId == -1) {
            mostrarMensajeAdvertencia("Por favor, seleccione un funcionario de la tabla para eliminar.");
            return;
        }

        // Pedimos confirmación al usuario
        int confirm = JOptionPane.showConfirmDialog(this,
                "¿Está seguro de que desea eliminar al funcionario seleccionado? Esta acción no se puede deshacer.",
                "Confirmar Eliminación",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE); // Icono de advertencia

        if (confirm == JOptionPane.YES_OPTION) { // Si el usuario confirma
            try {
                // Llamamos al servicio para eliminar
                funcionarioService.eliminarFuncionario(selectedFuncionarioId);
                mostrarMensajeExito("Funcionario eliminado exitosamente.");

                // Actualizamos la tabla y limpiamos el formulario
                cargarTablaFuncionarios();
                limpiarFormulario();
            } catch (BusinessLogicException e) {
                mostrarMensajeError("Error al eliminar funcionario: " + e.getMessage());
            }
        }
    }

    /**
     * Busca un funcionario por su ID usando el campo txtBuscarId y muestra el resultado.
     */
    private void buscarFuncionarioPorId() {
        String idText = txtBuscarId.getText().trim(); // Obtenemos el texto del campo de búsqueda

        if (idText.isEmpty()) {
            // Si el campo está vacío, recargamos la tabla completa
            cargarTablaFuncionarios();
            return;
        }

        try {
            int id = Integer.parseInt(idText); // Convertimos el texto a un número entero
            Funcionario f = funcionarioService.obtenerFuncionarioPorId(id); // Buscamos el funcionario

            if (f != null) {
                // Si se encontró, limpiamos la tabla y mostramos solo el resultado
                limpiarTabla();
                Vector<Object> row = new Vector<>();
                row.add(f.getIdFuncionario());
                row.add(f.getTipoIdentificacion());
                row.add(f.getNumeroIdentificacion());
                row.add(f.getNombres());
                row.add(f.getApellidos());
                row.add(f.getTelefono());
                tableModel.addRow(row);

                // Seleccionamos la fila en la tabla y cargamos los datos en el formulario
                tableFuncionarios.setRowSelectionInterval(0, 0); // Selecciona la primera (y única) fila
                cargarDatosFuncionario(id); // Cargamos los detalles en el formulario
                selectedFuncionarioId = id; // Actualizamos el ID seleccionado
            } else {
                // Si no se encontró, mostramos un mensaje y limpiamos la tabla y el formulario
                mostrarMensajeAdvertencia("No se encontró ningún funcionario con el ID especificado.");
                limpiarTabla();
                limpiarFormulario();
            }
        } catch (NumberFormatException ex) {
            // Si el texto ingresado no es un número válido
            mostrarMensajeAdvertencia("Por favor, ingrese un ID numérico válido.");
        } catch (BusinessLogicException e) {
            // Si ocurre un error al buscar (ej. problema de base de datos)
            mostrarMensajeError("Error al buscar funcionario: " + e.getMessage());
        }
    }

    /**
     * Habilita o deshabilita los botones según el estado de la operación.
     * @param esEstadoInicial true si estamos en el estado inicial (limpio), false si estamos editando/creando.
     */
    private void habilitarDeshabilitarBotones(boolean esEstadoInicial) {
        // En estado inicial (después de limpiar o al iniciar): Nuevo y Limpiar habilitados.
        // Al seleccionar un registro: Actualizar, Eliminar habilitados.
        // Al presionar Nuevo: Guardar habilitado.

        btnNuevo.setEnabled(esEstadoInicial); // Habilitado solo cuando no estamos creando/editando
        btnLimpiar.setEnabled(true); // Siempre habilitado
        btnGuardar.setEnabled(!esEstadoInicial); // Habilitado solo cuando se presiona Nuevo
        btnActualizar.setEnabled(!esEstadoInicial); // Habilitado cuando se selecciona un registro
        btnEliminar.setEnabled(!esEstadoInicial); // Habilitado cuando se selecciona un registro
    }

    // --- Métodos de Ayuda para Mostrar Mensajes ---
    private void mostrarMensajeError(String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje, "Error", JOptionPane.ERROR_MESSAGE);
    }

    private void mostrarMensajeAdvertencia(String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje, "Advertencia", JOptionPane.WARNING_MESSAGE);
    }

    private void mostrarMensajeExito(String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje, "Éxito", JOptionPane.INFORMATION_MESSAGE);
    }

    // --- Método main para ejecutar la aplicación ---
    // Este método crea la ventana principal (JFrame) y añade nuestro FuncionarioForm.
    public static void main(String[] args) {
        // SwingUtilities.invokeLater asegura que la creación de la UI se haga en el hilo de eventos de Swing.
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Gestión de Funcionarios"); // Título de la ventana
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Acción al cerrar la ventana (terminar la aplicación)
            frame.getContentPane().add(new FuncionarioForm()); // Añadimos nuestro panel al contenido del frame
            frame.pack(); // Ajusta el tamaño del frame al contenido
            frame.setLocationRelativeTo(null); // Centra la ventana en la pantalla
            frame.setVisible(true); // Hace la ventana visible
        });
    }
}
