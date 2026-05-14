/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package ui;

import java.awt.Component;
import java.awt.Image;
import java.util.List;
import javax.swing.DefaultCellEditor;
import javax.swing.ImageIcon;
import javax.swing.JCheckBox;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import javax.swing.JButton;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;
import model.Cliente;
import services.ClienteService;


/**
 *
 * @author herma
 */
public class ClientePanel extends javax.swing.JPanel {
    
    // Servicio para acceder a la lógica de negocio
    private ClienteService clienteService;
    
    // Variables para control de selección
    private int selectedId = -1;
    
    /**
     * Creates new form ClientePanel
     */
    public ClientePanel() {
        initComponents();
        // Inicializar el servicio
        clienteService = new ClienteService();
        
        // Cargar datos en la tabla
        listarClientes();
    }
    
    private void listarClientes() {
        DefaultTableModel model = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 7; // Solo columna de acción
            }
        };

        model.setColumnIdentifiers(new String[]{"ID", "Nombre", "Cédula", "Teléfono", "Email", "Dirección", "Estado", "Acción"});
        tbClientes.setModel(model);

        try {
            List<Cliente> clientes = clienteService.listarClientes();
            for (Cliente cliente : clientes) {
                model.addRow(new Object[]{
                    cliente.getId(),
                    cliente.getNombreCompleto(),
                    cliente.getCedula(),
                    cliente.getTelefono(),
                    cliente.getCorreoElectronico(),
                    cliente.getDireccion(),
                    cliente.isEstado() ? "Activo" : "Inactivo",
                    "Cargar"
                });
            }

            // Configurar botón en la tabla
            tbClientes.getColumn("Acción").setCellRenderer(new ButtonRenderer());
            tbClientes.getColumn("Acción").setCellEditor(new ButtonEditor(new JCheckBox()));
            tbClientes.getColumn("Estado").setCellRenderer(new EstadoCellRenderer());
            tbClientes.getColumnModel().getColumn(0).setPreferredWidth(40);   // ID
            tbClientes.getColumnModel().getColumn(1).setPreferredWidth(200);  // Nombre
            tbClientes.getColumnModel().getColumn(2).setPreferredWidth(100);  // Cédula
            tbClientes.getColumnModel().getColumn(3).setPreferredWidth(100);  // Teléfono
            tbClientes.getColumnModel().getColumn(4).setPreferredWidth(180);  // Email
            tbClientes.getColumnModel().getColumn(5).setPreferredWidth(250);  // Dirección
            tbClientes.getColumnModel().getColumn(6).setPreferredWidth(80);   // Estado
            tbClientes.getColumnModel().getColumn(7).setPreferredWidth(50);   // Acción
            tbClientes.getColumn("Acción").setMinWidth(40);
            tbClientes.getColumn("Acción").setMaxWidth(60);

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al cargar clientes: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void limpiarCampos() {
        txtNombreCompleto.setText("");
        txtCedula.setText("");
        txtTelefono.setText("");
        txtCorreoElectronico.setText("");
        tpDireccion.setText("");
        cmbEstado.setSelectedIndex(0);
        selectedId = -1;
        txtNombreCompleto.requestFocus();
        tbClientes.clearSelection();
    }

    private void cargarClienteDesdeFila(int row) {
        if (row != -1) {
            selectedId = (int) tbClientes.getValueAt(row, 0);
            txtNombreCompleto.setText((String) tbClientes.getValueAt(row, 1));
            txtCedula.setText((String) tbClientes.getValueAt(row, 2));
            txtTelefono.setText((String) tbClientes.getValueAt(row, 3));
            txtCorreoElectronico.setText((String) tbClientes.getValueAt(row, 4));
            tpDireccion.setText((String) tbClientes.getValueAt(row, 5));
            String estado = (String) tbClientes.getValueAt(row, 6);
            cmbEstado.setSelectedIndex(estado.equals("Activo") ? 0 : 1);

            // Resaltar la fila seleccionada en la tabla
            tbClientes.setRowSelectionInterval(row, row);
        }
    }
    
    private Cliente obtenerClienteDesdeCampos() {
        Cliente cliente = new Cliente();
        if (selectedId > 0) {
            cliente.setId(selectedId);
        }
        cliente.setNombreCompleto(txtNombreCompleto.getText().trim());
        cliente.setCedula(txtCedula.getText().trim());
        cliente.setTelefono(txtTelefono.getText().trim());
        cliente.setCorreoElectronico(txtCorreoElectronico.getText().trim());
        cliente.setDireccion(tpDireccion.getText().trim());
        cliente.setEstado(cmbEstado.getSelectedIndex() == 0);
        return cliente;
    }

    private boolean validarCampos() {
        if (txtNombreCompleto.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "El nombre completo es obligatorio", "Validación", JOptionPane.WARNING_MESSAGE);
            txtNombreCompleto.requestFocus();
            return false;
        }
        if (txtCedula.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "La cédula es obligatoria", "Validación", JOptionPane.WARNING_MESSAGE);
            txtCedula.requestFocus();
            return false;
        }
        String cedula = txtCedula.getText().trim();
        if (cedula.length() < 7 || cedula.length() > 10) {
            JOptionPane.showMessageDialog(this, "La cédula debe tener entre 7 y 10 dígitos", "Validación", JOptionPane.WARNING_MESSAGE);
            txtCedula.requestFocus();
            return false;
        }
        String email = txtCorreoElectronico.getText().trim();
        if (!email.isEmpty() && !email.contains("@")) {
            JOptionPane.showMessageDialog(this, "El correo electrónico no es válido", "Validación", JOptionPane.WARNING_MESSAGE);
            txtCorreoElectronico.requestFocus();
            return false;
        }
        return true;
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tbClientes = new javax.swing.JTable();
        btnEditar = new javax.swing.JButton();
        btnRegistrar = new javax.swing.JButton();
        btnEliminar = new javax.swing.JButton();
        btnLimpiar = new javax.swing.JButton();
        lblTitulo = new javax.swing.JLabel();
        txtCedula = new javax.swing.JTextField();
        txtTelefono = new javax.swing.JTextField();
        txtCorreoElectronico = new javax.swing.JTextField();
        txtNombreCompleto = new javax.swing.JTextField();
        lblNombreCompleto = new javax.swing.JLabel();
        lblCedula = new javax.swing.JLabel();
        lblTelefono = new javax.swing.JLabel();
        lblTelefono1 = new javax.swing.JLabel();
        cmbEstado = new javax.swing.JComboBox<>();
        lblEstado = new javax.swing.JLabel();
        lblDireccion = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        tpDireccion = new javax.swing.JTextPane();

        setPreferredSize(new java.awt.Dimension(757, 456));

        jPanel1.setBackground(java.awt.SystemColor.activeCaption);
        jPanel1.setPreferredSize(new java.awt.Dimension(757, 456));

        tbClientes.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        tbClientes.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
        jScrollPane1.setViewportView(tbClientes);

        btnEditar.setFont(new java.awt.Font("Microsoft Sans Serif", 1, 12)); // NOI18N
        btnEditar.setText("Editar");
        btnEditar.setPreferredSize(new java.awt.Dimension(65, 23));
        btnEditar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEditarActionPerformed(evt);
            }
        });

        btnRegistrar.setFont(new java.awt.Font("Microsoft Sans Serif", 1, 12)); // NOI18N
        btnRegistrar.setText("Registrar");
        btnRegistrar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRegistrarActionPerformed(evt);
            }
        });

        btnEliminar.setFont(new java.awt.Font("Microsoft Sans Serif", 1, 12)); // NOI18N
        btnEliminar.setText("Eliminar");
        btnEliminar.setPreferredSize(new java.awt.Dimension(65, 23));
        btnEliminar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEliminarActionPerformed(evt);
            }
        });

        btnLimpiar.setFont(new java.awt.Font("Microsoft Sans Serif", 1, 12)); // NOI18N
        btnLimpiar.setText("Limpiar");
        btnLimpiar.setPreferredSize(new java.awt.Dimension(65, 23));
        btnLimpiar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLimpiarActionPerformed(evt);
            }
        });

        lblTitulo.setFont(new java.awt.Font("Microsoft Sans Serif", 1, 24)); // NOI18N
        lblTitulo.setText("Lista de Clientes");

        txtCedula.setFont(new java.awt.Font("Microsoft Sans Serif", 0, 12)); // NOI18N

        txtTelefono.setFont(new java.awt.Font("Microsoft Sans Serif", 0, 12)); // NOI18N

        txtCorreoElectronico.setFont(new java.awt.Font("Microsoft Sans Serif", 0, 12)); // NOI18N

        txtNombreCompleto.setFont(new java.awt.Font("Microsoft Sans Serif", 0, 12)); // NOI18N

        lblNombreCompleto.setFont(new java.awt.Font("Microsoft Sans Serif", 1, 12)); // NOI18N
        lblNombreCompleto.setText("Nombre completo:");

        lblCedula.setFont(new java.awt.Font("Microsoft Sans Serif", 1, 12)); // NOI18N
        lblCedula.setText("Cedula:");

        lblTelefono.setFont(new java.awt.Font("Microsoft Sans Serif", 1, 12)); // NOI18N
        lblTelefono.setText("Telefono:");

        lblTelefono1.setFont(new java.awt.Font("Microsoft Sans Serif", 1, 12)); // NOI18N
        lblTelefono1.setText("Correo electrónico:");

        cmbEstado.setFont(new java.awt.Font("Microsoft Sans Serif", 0, 12)); // NOI18N
        cmbEstado.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Activo", "No Activo" }));

        lblEstado.setFont(new java.awt.Font("Microsoft Sans Serif", 1, 12)); // NOI18N
        lblEstado.setText("Estado");

        lblDireccion.setFont(new java.awt.Font("Microsoft Sans Serif", 1, 12)); // NOI18N
        lblDireccion.setText("Dirección:");

        jScrollPane2.setViewportView(tpDireccion);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(29, 29, 29)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblEstado)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(txtNombreCompleto, javax.swing.GroupLayout.DEFAULT_SIZE, 138, Short.MAX_VALUE)
                        .addComponent(txtCedula)
                        .addComponent(txtTelefono, javax.swing.GroupLayout.DEFAULT_SIZE, 138, Short.MAX_VALUE)
                        .addComponent(txtCorreoElectronico, javax.swing.GroupLayout.DEFAULT_SIZE, 138, Short.MAX_VALUE)
                        .addComponent(lblCedula)
                        .addComponent(lblTelefono)
                        .addComponent(lblTelefono1)
                        .addComponent(lblNombreCompleto)
                        .addComponent(lblDireccion)
                        .addComponent(jScrollPane2))
                    .addComponent(cmbEstado, javax.swing.GroupLayout.PREFERRED_SIZE, 139, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 26, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(btnRegistrar, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(58, 58, 58)
                        .addComponent(btnEditar, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnLimpiar, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(49, 49, 49)
                        .addComponent(btnEliminar, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 530, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(33, 33, 33))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(lblTitulo)
                .addGap(277, 277, 277))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(25, 25, 25)
                .addComponent(lblTitulo)
                .addGap(26, 26, 26)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(lblNombreCompleto)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtNombreCompleto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 7, Short.MAX_VALUE)
                        .addComponent(lblCedula)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtCedula, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lblTelefono)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtTelefono, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lblTelefono1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtCorreoElectronico, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lblDireccion)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(lblEstado)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cmbEstado, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnRegistrar)
                    .addComponent(btnEditar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnLimpiar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnEliminar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(35, 35, 35))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, 439, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents

    private void btnRegistrarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRegistrarActionPerformed
        if (selectedId != -1) {
            JOptionPane.showMessageDialog(this, "Para modificar use el botón 'Editar'.\nO limpie los campos para registrar uno nuevo.", 
                    "Información", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        if (!validarCampos()) return;

        try {
            Cliente cliente = obtenerClienteDesdeCampos();
            int id = clienteService.registrarCliente(cliente);

            if (id > 0) {
                JOptionPane.showMessageDialog(this, "Cliente registrado exitosamente", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                limpiarCampos();
                listarClientes();
            } else {
                JOptionPane.showMessageDialog(this, "Error al registrar el cliente", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (IllegalArgumentException e) {
            JOptionPane.showMessageDialog(this, e.getMessage(), "Error de validación", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_btnRegistrarActionPerformed

    private void btnEditarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEditarActionPerformed
        if (selectedId == -1) {
            JOptionPane.showMessageDialog(this, "Primero cargue un cliente desde la tabla", "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (!validarCampos()) return;

        try {
            Cliente cliente = obtenerClienteDesdeCampos();
            if (clienteService.editarCliente(cliente)) {
                JOptionPane.showMessageDialog(this, "Cliente actualizado exitosamente", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                limpiarCampos();
                listarClientes();
            } else {
                JOptionPane.showMessageDialog(this, "Error al actualizar el cliente", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (IllegalArgumentException e) {
            JOptionPane.showMessageDialog(this, e.getMessage(), "Error de validación", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_btnEditarActionPerformed

    private void btnLimpiarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLimpiarActionPerformed
       limpiarCampos();
    }//GEN-LAST:event_btnLimpiarActionPerformed

    private void btnEliminarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEliminarActionPerformed
        if (selectedId == -1) {
            JOptionPane.showMessageDialog(this, "Primero cargue un cliente desde la tabla", "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String nombre = txtNombreCompleto.getText().trim();

        int confirm = JOptionPane.showConfirmDialog(this,
                "¿Está seguro de eliminar el cliente '" + nombre + "'?",
                "Confirmar eliminación",
                JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            try {
                if (clienteService.eliminarCliente(selectedId)) {
                    JOptionPane.showMessageDialog(this, "Cliente eliminado exitosamente", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                    limpiarCampos();
                    listarClientes();
                } else {
                    JOptionPane.showMessageDialog(this, "Error al eliminar el cliente", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }//GEN-LAST:event_btnEliminarActionPerformed
    
    class ButtonRenderer extends JButton implements TableCellRenderer {
        private ImageIcon iconoCargar;
        private static final int ICON_SIZE = 16; 

        public ButtonRenderer() {
            setOpaque(true);
            setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
            setBorder(javax.swing.BorderFactory.createEmptyBorder(2, 2, 2, 2));
            cargarIcono();
        }

        private void cargarIcono() {
            try {
                ImageIcon iconoOriginal = new ImageIcon(getClass().getResource("/img/visto.png"));
                Image img = iconoOriginal.getImage().getScaledInstance(ICON_SIZE, ICON_SIZE, java.awt.Image.SCALE_SMOOTH);
                iconoCargar = new ImageIcon(img);
                setIcon(iconoCargar);
                setText("");
            } catch (Exception e) {
                setText("");
                setFont(new java.awt.Font("Segoe UI Emoji", java.awt.Font.PLAIN, 12));
            }
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                boolean isSelected, boolean hasFocus, int row, int column) {
            return this;
        }
    }
    
    // Clase para manejar clics en el botón
    class ButtonEditor extends DefaultCellEditor {
        protected JButton button;
        private int selectedRow;
        private static final int ICON_SIZE = 16; 
        private ImageIcon iconoCargar;

        public ButtonEditor(JCheckBox checkBox) {
            super(checkBox);
            button = new JButton();
            button.setOpaque(true);
            button.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
            button.setBorder(javax.swing.BorderFactory.createEmptyBorder(2, 2, 2, 2));

            try {
                ImageIcon iconoOriginal = new ImageIcon(getClass().getResource("/img/visto.png"));
                Image img = iconoOriginal.getImage().getScaledInstance(ICON_SIZE, ICON_SIZE, java.awt.Image.SCALE_SMOOTH);
                iconoCargar = new ImageIcon(img);
                button.setIcon(iconoCargar);
                button.setText("");
            } catch (Exception e) {
                button.setText("");
                button.setFont(new java.awt.Font("Segoe UI Emoji", java.awt.Font.PLAIN, 12));
            }

            button.addActionListener(e -> {
                fireEditingStopped();
                cargarClienteDesdeFila(selectedRow);
            });
        }

        @Override
        public Component getTableCellEditorComponent(JTable table, Object value,
                boolean isSelected, int row, int column) {
            selectedRow = row;
            return button;
        }

        @Override
        public Object getCellEditorValue() {
            return "Cargar";
        }
    }
    
    // Clase para renderizar la celda de estado con colores
    class EstadoCellRenderer extends javax.swing.table.DefaultTableCellRenderer {
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
            boolean isSelected, boolean hasFocus, int row, int column) {

            Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

            if (value != null) {
                String estado = value.toString();
                if (estado.equals("Activo")) {
                    c.setBackground(new java.awt.Color(144, 238, 144)); // Verde claro
                    c.setForeground(new java.awt.Color(0, 100, 0));     // Verde oscuro
                } else if (estado.equals("Inactivo")) {
                    c.setBackground(new java.awt.Color(255, 182, 193)); // Rojo claro
                    c.setForeground(new java.awt.Color(139, 0, 0));     // Rojo oscuro
                } else {
                    c.setBackground(java.awt.Color.WHITE);
                    c.setForeground(java.awt.Color.BLACK);
                }
            }

            if (isSelected) {
                c.setBackground(table.getSelectionBackground());
                c.setForeground(table.getSelectionForeground());
            }

            return c;
        }
    }
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnEditar;
    private javax.swing.JButton btnEliminar;
    private javax.swing.JButton btnLimpiar;
    private javax.swing.JButton btnRegistrar;
    private javax.swing.JComboBox<String> cmbEstado;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JLabel lblCedula;
    private javax.swing.JLabel lblDireccion;
    private javax.swing.JLabel lblEstado;
    private javax.swing.JLabel lblNombreCompleto;
    private javax.swing.JLabel lblTelefono;
    private javax.swing.JLabel lblTelefono1;
    private javax.swing.JLabel lblTitulo;
    private javax.swing.JTable tbClientes;
    private javax.swing.JTextPane tpDireccion;
    private javax.swing.JTextField txtCedula;
    private javax.swing.JTextField txtCorreoElectronico;
    private javax.swing.JTextField txtNombreCompleto;
    private javax.swing.JTextField txtTelefono;
    // End of variables declaration//GEN-END:variables
}
