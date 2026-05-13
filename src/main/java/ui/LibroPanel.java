/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package ui;

import java.awt.Component;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import javax.swing.DefaultCellEditor;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.SpinnerNumberModel;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import model.Libro;
import services.LibroService;

/**
 *
 * @author herma
 */
public class LibroPanel extends javax.swing.JPanel {

    // Servicio para acceder a la lógica de negocio
    private LibroService libroService;
    
    // Variables para control de selección
    private int selectedId = -1;
    
    /**
     * Creates new form LibroPanel
     */
    public LibroPanel() {
        initComponents();
        // Inicializar el servicio
        libroService = new LibroService();

        // Configurar el spinner para la cantidad
        jSpinner1.setModel(new SpinnerNumberModel(0, 0, 999, 1));

        // Cargar datos en la tabla
        listarLibros();
    }
    
    private void listarLibros() {
        DefaultTableModel model = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                // Solo la columna de acción (última columna) es editable
                return column == 7;
            }
        };

        model.setColumnIdentifiers(new String[]{"ID", "Título", "Autor", "Año", "Editorial", "Cantidad", "Estado", "Acción"});
        tbLibros.setModel(model);

        try {
            List<Libro> libros = libroService.listarLibros();
            for (Libro libro : libros) {
                model.addRow(new Object[]{
                    libro.getId(),
                    libro.getTitulo(),
                    libro.getAutor(),
                    libro.getAnioPublicacion(),
                    libro.getEditorial(),
                    libro.getCantidad(),
                    libro.isEstado() ? "Activo" : "Inactivo",
                    "Cargar"  // Texto del botón
                });
            }

            // Configurar el renderizador y editor para la columna de botones
            tbLibros.getColumn("Acción").setCellRenderer(new ButtonRenderer());
            tbLibros.getColumn("Acción").setCellEditor(new ButtonEditor(new JCheckBox()));
            tbLibros.getColumn("Estado").setCellRenderer(new EstadoCellRenderer());
            tbLibros.getColumnModel().getColumn(0).setPreferredWidth(40);   // ID
            tbLibros.getColumnModel().getColumn(1).setPreferredWidth(200);  // Nombre
            tbLibros.getColumnModel().getColumn(2).setPreferredWidth(100);  // Autor
            tbLibros.getColumnModel().getColumn(3).setPreferredWidth(40);  // Año publicación
            tbLibros.getColumnModel().getColumn(4).setPreferredWidth(180);  // Editorial
            tbLibros.getColumnModel().getColumn(5).setPreferredWidth(80);   // Estado
            tbLibros.getColumnModel().getColumn(6).setPreferredWidth(50);   // Acción
            tbLibros.getColumn("Acción").setMinWidth(40);
            tbLibros.getColumn("Acción").setMaxWidth(60);

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al cargar libros: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void limpiarCampos() {
       txtNombre.setText("");
       txtAutor.setText("");
       txtAnioPublicacion.setText("");
       txtEditorial.setText("");
       jSpinner1.setValue(0);
       cmbEstado.setSelectedIndex(0);
       selectedId = -1;
       txtNombre.requestFocus();
   }
    
    private boolean validarCampos() {
        if (txtNombre.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "El título del libro es obligatorio", "Validación", JOptionPane.WARNING_MESSAGE);
            txtNombre.requestFocus();
            return false;
        }

        if (txtAutor.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "El autor del libro es obligatorio", "Validación", JOptionPane.WARNING_MESSAGE);
            txtAutor.requestFocus();
            return false;
        }

        // Validar año (si no está vacío, debe ser número)
        String anioStr = txtAnioPublicacion.getText().trim();
        if (!anioStr.isEmpty()) {
            try {
                int anio = Integer.parseInt(anioStr);
                if (anio < 0 || anio > 2026) {
                    JOptionPane.showMessageDialog(this, "El año debe ser entre 0 y 2026", "Validación", JOptionPane.WARNING_MESSAGE);
                    txtAnioPublicacion.requestFocus();
                    return false;
                }
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "El año debe ser un número válido", "Validación", JOptionPane.WARNING_MESSAGE);
                txtAnioPublicacion.requestFocus();
                return false;
            }
        }

        return true;
    }
    
    private void cargarLibroDesdeFila(int row) {
        if (row != -1) {
            selectedId = (int) tbLibros.getValueAt(row, 0);
            txtNombre.setText((String) tbLibros.getValueAt(row, 1));
            txtAutor.setText((String) tbLibros.getValueAt(row, 2));

            int anio = (int) tbLibros.getValueAt(row, 3);
            txtAnioPublicacion.setText(anio > 0 ? String.valueOf(anio) : "");

            txtEditorial.setText((String) tbLibros.getValueAt(row, 4));
            jSpinner1.setValue((int) tbLibros.getValueAt(row, 5));
            String estado = (String) tbLibros.getValueAt(row, 6);
            cmbEstado.setSelectedIndex(estado.equals("Activo") ? 0 : 1);

            // Resaltar la fila seleccionada en la tabla
            tbLibros.setRowSelectionInterval(row, row);
        }
    }
    
    private Libro obtenerLibroDesdeCampos() {
        Libro libro = new Libro();
        if (selectedId > 0) {
            libro.setId(selectedId);
        }
        libro.setTitulo(txtNombre.getText().trim());
        libro.setAutor(txtAutor.getText().trim());
        
        String anioStr = txtAnioPublicacion.getText().trim();
        try {
            libro.setAnioPublicacion(anioStr.isEmpty() ? 0 : Integer.parseInt(anioStr));
        } catch (NumberFormatException e) {
            libro.setAnioPublicacion(0);
        }
        
        libro.setEditorial(txtEditorial.getText().trim());
        libro.setCantidad((Integer) jSpinner1.getValue());
        libro.setEstado(cmbEstado.getSelectedIndex() == 0);
        return libro;
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
        jPanel2 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tbLibros = new javax.swing.JTable();
        btnEditar = new javax.swing.JButton();
        btnRegistrar = new javax.swing.JButton();
        btnEliminar = new javax.swing.JButton();
        btnLimpiar = new javax.swing.JButton();
        lblTitulo = new javax.swing.JLabel();
        txtAutor = new javax.swing.JTextField();
        txtAnioPublicacion = new javax.swing.JTextField();
        txtEditorial = new javax.swing.JTextField();
        txtNombre = new javax.swing.JTextField();
        lblNombre = new javax.swing.JLabel();
        lblAutor = new javax.swing.JLabel();
        lblTelefono = new javax.swing.JLabel();
        lblTelefono1 = new javax.swing.JLabel();
        cmbEstado = new javax.swing.JComboBox<>();
        lblEstado = new javax.swing.JLabel();
        lblCantidad = new javax.swing.JLabel();
        jSpinner1 = new javax.swing.JSpinner();

        setPreferredSize(new java.awt.Dimension(757, 456));

        jPanel2.setBackground(java.awt.SystemColor.activeCaption);
        jPanel2.setPreferredSize(new java.awt.Dimension(757, 456));

        tbLibros.setModel(new javax.swing.table.DefaultTableModel(
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
        tbLibros.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
        jScrollPane1.setViewportView(tbLibros);

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
        lblTitulo.setText("Lista de Libros");

        txtAutor.setFont(new java.awt.Font("Microsoft Sans Serif", 0, 12)); // NOI18N

        txtAnioPublicacion.setFont(new java.awt.Font("Microsoft Sans Serif", 0, 12)); // NOI18N

        txtEditorial.setFont(new java.awt.Font("Microsoft Sans Serif", 0, 12)); // NOI18N

        txtNombre.setFont(new java.awt.Font("Microsoft Sans Serif", 0, 12)); // NOI18N

        lblNombre.setFont(new java.awt.Font("Microsoft Sans Serif", 1, 12)); // NOI18N
        lblNombre.setText("Nombre:");

        lblAutor.setFont(new java.awt.Font("Microsoft Sans Serif", 1, 12)); // NOI18N
        lblAutor.setText("Autor:");

        lblTelefono.setFont(new java.awt.Font("Microsoft Sans Serif", 1, 12)); // NOI18N
        lblTelefono.setText("Año publicación");

        lblTelefono1.setFont(new java.awt.Font("Microsoft Sans Serif", 1, 12)); // NOI18N
        lblTelefono1.setText("Editorial:");

        cmbEstado.setFont(new java.awt.Font("Microsoft Sans Serif", 0, 12)); // NOI18N
        cmbEstado.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Activo", "No Activo" }));

        lblEstado.setFont(new java.awt.Font("Microsoft Sans Serif", 1, 12)); // NOI18N
        lblEstado.setText("Estado");

        lblCantidad.setFont(new java.awt.Font("Microsoft Sans Serif", 1, 12)); // NOI18N
        lblCantidad.setText("Cantidad:");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(lblTitulo)
                .addGap(277, 277, 277))
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(29, 29, 29)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(cmbEstado, javax.swing.GroupLayout.PREFERRED_SIZE, 139, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addComponent(jSpinner1, javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(txtNombre, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 138, Short.MAX_VALUE)
                        .addComponent(txtAutor, javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(txtAnioPublicacion, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 138, Short.MAX_VALUE)
                        .addComponent(txtEditorial, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 138, Short.MAX_VALUE)
                        .addComponent(lblAutor, javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(lblTelefono, javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(lblTelefono1, javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(lblNombre, javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(lblCantidad, javax.swing.GroupLayout.Alignment.LEADING))
                    .addComponent(lblEstado))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 26, Short.MAX_VALUE)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                        .addComponent(btnRegistrar, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(57, 57, 57)
                        .addComponent(btnEditar, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(62, 62, 62)
                        .addComponent(btnLimpiar, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(51, 51, 51)
                        .addComponent(btnEliminar, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 530, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(33, 33, 33))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(25, 25, 25)
                .addComponent(lblTitulo)
                .addGap(37, 37, 37)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(lblNombre)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtNombre, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lblAutor)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtAutor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lblTelefono)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtAnioPublicacion, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lblTelefono1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtEditorial, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lblCantidad)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jSpinner1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(12, 12, 12)
                        .addComponent(lblEstado)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cmbEstado, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 250, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 31, Short.MAX_VALUE)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(btnEliminar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnLimpiar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnEditar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnRegistrar))
                        .addGap(39, 39, 39))))
        );

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, 433, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
    }// </editor-fold>//GEN-END:initComponents

    private void btnEliminarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEliminarActionPerformed
        if (selectedId == -1) {
            JOptionPane.showMessageDialog(this, "Primero cargue un libro desde la tabla", "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String titulo = txtNombre.getText().trim();

        int confirm = JOptionPane.showConfirmDialog(this,
            "¿Está seguro de eliminar el libro '" + titulo + "'?",
            "Confirmar eliminación",
            JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            try {
                if (libroService.eliminarLibro(selectedId)) {
                    JOptionPane.showMessageDialog(this, "Libro eliminado exitosamente", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                    limpiarCampos();
                    listarLibros();
                } else {
                    JOptionPane.showMessageDialog(this, "Error al eliminar el libro", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }//GEN-LAST:event_btnEliminarActionPerformed

    private void btnRegistrarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRegistrarActionPerformed
        if (selectedId != -1) {
            JOptionPane.showMessageDialog(this, "Para modificar un libro use el botón 'Editar'.\nO limpie los campos para registrar uno nuevo.", "Información", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        if (!validarCampos()) return;

        try {
            Libro libro = obtenerLibroDesdeCampos();
            int id = libroService.registrarLibro(libro);

            if (id > 0) {
                JOptionPane.showMessageDialog(this, "Libro registrado exitosamente", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                limpiarCampos();
                listarLibros();
            } else {
                JOptionPane.showMessageDialog(this, "Error al registrar el libro", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (IllegalArgumentException e) {
            JOptionPane.showMessageDialog(this, e.getMessage(), "Error de validación", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_btnRegistrarActionPerformed

    private void btnEditarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEditarActionPerformed
         if (selectedId == -1) {
            JOptionPane.showMessageDialog(this, "Primero cargue un libro desde la tabla", "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (!validarCampos()) return;

        try {
            Libro libro = obtenerLibroDesdeCampos();

            if (libroService.editarLibro(libro)) {
                JOptionPane.showMessageDialog(this, "Libro actualizado exitosamente", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                limpiarCampos();
                listarLibros();
            } else {
                JOptionPane.showMessageDialog(this, "Error al actualizar el libro", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (IllegalArgumentException e) {
            JOptionPane.showMessageDialog(this, e.getMessage(), "Error de validación", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_btnEditarActionPerformed

    private void btnLimpiarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLimpiarActionPerformed
        limpiarCampos();
    }//GEN-LAST:event_btnLimpiarActionPerformed

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
                cargarLibroDesdeFila(selectedRow);
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
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JSpinner jSpinner1;
    private javax.swing.JLabel lblAutor;
    private javax.swing.JLabel lblCantidad;
    private javax.swing.JLabel lblEstado;
    private javax.swing.JLabel lblNombre;
    private javax.swing.JLabel lblTelefono;
    private javax.swing.JLabel lblTelefono1;
    private javax.swing.JLabel lblTitulo;
    private javax.swing.JTable tbLibros;
    private javax.swing.JTextField txtAnioPublicacion;
    private javax.swing.JTextField txtAutor;
    private javax.swing.JTextField txtEditorial;
    private javax.swing.JTextField txtNombre;
    // End of variables declaration//GEN-END:variables
}