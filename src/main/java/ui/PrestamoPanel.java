/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package ui;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import model.Cliente;
import model.Libro;
import services.ClienteService;
import services.LibroService;
import services.PrestamoService;

/**
 *
 * @author herma
 */
public class PrestamoPanel extends javax.swing.JPanel {
    
    private PrestamoService prestamoService;
    private ClienteService clienteService;
    private LibroService libroService;
    private List<Libro> librosSeleccionados = new ArrayList<>();
    private DefaultTableModel modeloTablaSeleccionados;

    public PrestamoPanel() {
        initComponents();
        prestamoService = new PrestamoService();
        clienteService = new ClienteService();
        libroService = new LibroService();
        cargarClientes();
        cargarLibrosDisponibles();
        inicializarTablaSeleccionados();
    }
    
    private void cargarClientes() {
        try {
            List<Cliente> clientes = clienteService.listarClientes();
            DefaultComboBoxModel<String> model = new DefaultComboBoxModel<>();
            model.addElement("-- Seleccione un cliente --");
            for (Cliente cliente : clientes) {
                if (cliente.isEstado()) {
                    model.addElement(cliente.getId() + " - " + cliente.getNombreCompleto() + " (" + cliente.getCedula() + ")");
                }
            }
            cmbCliente.setModel(model);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al cargar clientes: " + e.getMessage());
        }
    }
    
    private void cargarLibrosDisponibles() {
        try {
            List<Libro> libros = libroService.listarLibros();
            DefaultComboBoxModel<String> model = new DefaultComboBoxModel<>();
            model.addElement("-- Seleccione un libro --");
            for (Libro libro : libros) {
                if (libro.isEstado() && libro.getCantidad() > 0) {
                    model.addElement(libro.getId() + " - " + libro.getTitulo() + " (Stock: " + libro.getCantidad() + ")");
                }
            }
            cmbLibro.setModel(model);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al cargar libros: " + e.getMessage());
        }
    }
    
    private void inicializarTablaSeleccionados() {
        modeloTablaSeleccionados = new DefaultTableModel();
        modeloTablaSeleccionados.setColumnIdentifiers(new String[]{"ID", "Título", "Autor"});
        tbPrestamos.setModel(modeloTablaSeleccionados);
    }
    
    private int obtenerIdDesdeComboBox(JComboBox<String> comboBox) {
        String selected = (String) comboBox.getSelectedItem();
        if (selected != null && !selected.startsWith("--")) {
            String[] parts = selected.split(" - ");
            return Integer.parseInt(parts[0]);
        }
        return -1;
    }
    
    private Libro obtenerLibroDesdeComboBox() {
        int id = obtenerIdDesdeComboBox(cmbLibro);
        if (id != -1) {
            return libroService.obtenerLibro(id);
        }
        return null;
    }
    
    private void agregarLibroALista() {
        Libro libro = obtenerLibroDesdeComboBox();
        if (libro == null) {
            JOptionPane.showMessageDialog(this, "Seleccione un libro válido");
            return;
        }
        
        for (Libro l : librosSeleccionados) {
            if (l.getId() == libro.getId()) {
                JOptionPane.showMessageDialog(this, "El libro ya está en la lista");
                return;
            }
        }
        
        librosSeleccionados.add(libro);
        modeloTablaSeleccionados.addRow(new Object[]{
            libro.getId(),
            libro.getTitulo(),
            libro.getAutor()
        });
        
        DefaultComboBoxModel<String> model = (DefaultComboBoxModel<String>) cmbLibro.getModel();
        String selected = (String) cmbLibro.getSelectedItem();
        if (selected != null) {
            model.removeElement(selected);
        }
        cmbLibro.setSelectedIndex(0);
    }
    
    private void quitarLibroDeLista() {
        int fila = tbPrestamos.getSelectedRow();
        if (fila >= 0) {
            Libro libro = librosSeleccionados.get(fila);
            librosSeleccionados.remove(fila);
            modeloTablaSeleccionados.removeRow(fila);
            
            // Reagregar al comboBox
            DefaultComboBoxModel<String> model = (DefaultComboBoxModel<String>) cmbLibro.getModel();
            model.addElement(libro.getId() + " - " + libro.getTitulo() + " (Stock: " + libro.getCantidad() + ")");
        } else {
            JOptionPane.showMessageDialog(this, "Seleccione un libro de la lista");
        }
    }
    
    private void limpiarCampos() {
        cmbCliente.setSelectedIndex(0);
        cmbLibro.setSelectedIndex(0);
        dpFechaPrestamo.setDate(new Date());
        dpFechaDevolucion.setDate(new Date(System.currentTimeMillis() + 7 * 24 * 60 * 60 * 1000L));
        librosSeleccionados.clear();
        modeloTablaSeleccionados.setRowCount(0);
        cargarLibrosDisponibles();
    }
    
    private boolean validarCampos() {
        if (obtenerIdDesdeComboBox(cmbCliente) == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione un cliente");
            return false;
        }
        if (librosSeleccionados.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Agregue al menos un libro");
            return false;
        }
        if (dpFechaPrestamo.getDate() == null || dpFechaDevolucion.getDate() == null) {
            JOptionPane.showMessageDialog(this, "Seleccione las fechas");
            return false;
        }
        if (dpFechaDevolucion.getDate().before(dpFechaPrestamo.getDate())) {
            JOptionPane.showMessageDialog(this, "La fecha de devolución no puede ser anterior");
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

        jpPrincipal = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        tbPrestamos = new javax.swing.JTable();
        btnEditar = new javax.swing.JButton();
        btnRegistrar = new javax.swing.JButton();
        btnLimpiar = new javax.swing.JButton();
        lblTitulo1 = new javax.swing.JLabel();
        lblCliente1 = new javax.swing.JLabel();
        lblLibro1 = new javax.swing.JLabel();
        lblFechaPrestamo1 = new javax.swing.JLabel();
        lblFechaDevolucion1 = new javax.swing.JLabel();
        cmbEstado = new javax.swing.JComboBox<>();
        lblEstado1 = new javax.swing.JLabel();
        cmbLibro = new javax.swing.JComboBox<>();
        cmbCliente = new javax.swing.JComboBox<>();
        dpFechaPrestamo = new com.toedter.calendar.JDateChooser();
        dpFechaDevolucion = new com.toedter.calendar.JDateChooser();
        btnAgregar = new javax.swing.JButton();
        btnEliminar = new javax.swing.JButton();

        setPreferredSize(new java.awt.Dimension(757, 456));

        jpPrincipal.setBackground(java.awt.SystemColor.activeCaption);
        jpPrincipal.setPreferredSize(new java.awt.Dimension(757, 456));

        tbPrestamos.setModel(new javax.swing.table.DefaultTableModel(
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
        tbPrestamos.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
        jScrollPane2.setViewportView(tbPrestamos);

        btnEditar.setFont(new java.awt.Font("Microsoft Sans Serif", 1, 12)); // NOI18N
        btnEditar.setText("Editar");
        btnEditar.setPreferredSize(new java.awt.Dimension(65, 23));

        btnRegistrar.setFont(new java.awt.Font("Microsoft Sans Serif", 1, 12)); // NOI18N
        btnRegistrar.setText("Registrar");
        btnRegistrar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRegistrarActionPerformed(evt);
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

        lblTitulo1.setFont(new java.awt.Font("Microsoft Sans Serif", 1, 24)); // NOI18N
        lblTitulo1.setText("Lista de Prestamos");

        lblCliente1.setFont(new java.awt.Font("Microsoft Sans Serif", 1, 12)); // NOI18N
        lblCliente1.setText("Cliente:");

        lblLibro1.setFont(new java.awt.Font("Microsoft Sans Serif", 1, 12)); // NOI18N
        lblLibro1.setText("Libro:");

        lblFechaPrestamo1.setFont(new java.awt.Font("Microsoft Sans Serif", 1, 12)); // NOI18N
        lblFechaPrestamo1.setText("Fecha Prestamo:");

        lblFechaDevolucion1.setFont(new java.awt.Font("Microsoft Sans Serif", 1, 12)); // NOI18N
        lblFechaDevolucion1.setText("Fecha Devolución:");

        cmbEstado.setFont(new java.awt.Font("Microsoft Sans Serif", 0, 12)); // NOI18N
        cmbEstado.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Activo", "No Activo" }));

        lblEstado1.setFont(new java.awt.Font("Microsoft Sans Serif", 1, 12)); // NOI18N
        lblEstado1.setText("Estado");

        cmbLibro.setFont(new java.awt.Font("Microsoft Sans Serif", 0, 12)); // NOI18N
        cmbLibro.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Activo", "No Activo" }));

        cmbCliente.setFont(new java.awt.Font("Microsoft Sans Serif", 0, 12)); // NOI18N
        cmbCliente.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Activo", "No Activo" }));

        btnAgregar.setFont(new java.awt.Font("Microsoft Sans Serif", 1, 12)); // NOI18N
        btnAgregar.setText("Agregar");
        btnAgregar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAgregarActionPerformed(evt);
            }
        });

        btnEliminar.setFont(new java.awt.Font("Microsoft Sans Serif", 1, 12)); // NOI18N
        btnEliminar.setText("Eliminar");
        btnEliminar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEliminarActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jpPrincipalLayout = new javax.swing.GroupLayout(jpPrincipal);
        jpPrincipal.setLayout(jpPrincipalLayout);
        jpPrincipalLayout.setHorizontalGroup(
            jpPrincipalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jpPrincipalLayout.createSequentialGroup()
                .addGap(29, 29, 29)
                .addGroup(jpPrincipalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jpPrincipalLayout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(lblTitulo1)
                        .addGap(277, 277, 277))
                    .addGroup(jpPrincipalLayout.createSequentialGroup()
                        .addGroup(jpPrincipalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lblFechaDevolucion1)
                            .addComponent(lblCliente1)
                            .addComponent(lblLibro1)
                            .addComponent(cmbLibro, javax.swing.GroupLayout.PREFERRED_SIZE, 139, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(dpFechaDevolucion, javax.swing.GroupLayout.PREFERRED_SIZE, 138, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(dpFechaPrestamo, javax.swing.GroupLayout.PREFERRED_SIZE, 139, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(cmbCliente, javax.swing.GroupLayout.PREFERRED_SIZE, 139, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lblFechaPrestamo1)
                            .addComponent(lblEstado1)
                            .addComponent(cmbEstado, javax.swing.GroupLayout.PREFERRED_SIZE, 138, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(24, 24, 24)
                        .addGroup(jpPrincipalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 530, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jpPrincipalLayout.createSequentialGroup()
                                .addComponent(btnAgregar, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(btnEliminar, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(btnRegistrar, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(btnEditar, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(btnLimpiar, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addContainerGap(29, Short.MAX_VALUE))))
        );
        jpPrincipalLayout.setVerticalGroup(
            jpPrincipalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jpPrincipalLayout.createSequentialGroup()
                .addGap(25, 25, 25)
                .addComponent(lblTitulo1)
                .addGap(31, 31, 31)
                .addGroup(jpPrincipalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jpPrincipalLayout.createSequentialGroup()
                        .addComponent(lblCliente1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cmbCliente, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lblLibro1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cmbLibro, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(lblFechaPrestamo1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(dpFechaPrestamo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(12, 12, 12)
                        .addComponent(lblFechaDevolucion1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(dpFechaDevolucion, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(lblEstado1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jpPrincipalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cmbEstado, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnRegistrar)
                    .addComponent(btnEditar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnLimpiar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnAgregar)
                    .addComponent(btnEliminar))
                .addContainerGap(81, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 751, Short.MAX_VALUE)
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(jpPrincipal, javax.swing.GroupLayout.DEFAULT_SIZE, 751, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 435, Short.MAX_VALUE)
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(jpPrincipal, javax.swing.GroupLayout.DEFAULT_SIZE, 435, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void btnLimpiarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLimpiarActionPerformed
        limpiarCampos();
    }//GEN-LAST:event_btnLimpiarActionPerformed

    private void btnRegistrarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRegistrarActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnRegistrarActionPerformed

    private void btnAgregarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAgregarActionPerformed
        agregarLibroALista();
    }//GEN-LAST:event_btnAgregarActionPerformed

    private void btnEliminarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEliminarActionPerformed
        quitarLibroDeLista();
    }//GEN-LAST:event_btnEliminarActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAgregar;
    private javax.swing.JButton btnEditar;
    private javax.swing.JButton btnEliminar;
    private javax.swing.JButton btnLimpiar;
    private javax.swing.JButton btnRegistrar;
    private javax.swing.JComboBox<String> cmbCliente;
    private javax.swing.JComboBox<String> cmbEstado;
    private javax.swing.JComboBox<String> cmbLibro;
    private com.toedter.calendar.JDateChooser dpFechaDevolucion;
    private com.toedter.calendar.JDateChooser dpFechaPrestamo;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JPanel jpPrincipal;
    private javax.swing.JLabel lblCliente1;
    private javax.swing.JLabel lblEstado1;
    private javax.swing.JLabel lblFechaDevolucion1;
    private javax.swing.JLabel lblFechaPrestamo1;
    private javax.swing.JLabel lblLibro1;
    private javax.swing.JLabel lblTitulo1;
    private javax.swing.JTable tbPrestamos;
    // End of variables declaration//GEN-END:variables
}
