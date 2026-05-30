/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package ui;

import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import model.Prestamo;
import services.PrestamoService;

/**
 *
 * @author herma
 */
public class DashboardPanel extends javax.swing.JPanel {

    private PrestamoService prestamoService;
    private DefaultTableModel modeloTabla;
    
    /**
     * Creates new form DashboarPanel
     */
    public DashboardPanel() {
        initComponents();
        prestamoService = new PrestamoService();
        configurarTabla();
        cargarPrestamosActivos();
    }
    
    private void configurarTabla() {
        // Configurar modelo de tabla con columnas
        modeloTabla = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 4; // Solo columna de acción
            }
        };
        modeloTabla.setColumnIdentifiers(new String[]{"ID", "Cliente", "Fecha Préstamo", "Fecha Devolución", "Acción"});
        tbPrestamos.setModel(modeloTabla);
        
        // Ajustar anchos de columnas
        tbPrestamos.getColumnModel().getColumn(0).setPreferredWidth(50);
        tbPrestamos.getColumnModel().getColumn(1).setPreferredWidth(200);
        tbPrestamos.getColumnModel().getColumn(2).setPreferredWidth(120);
        tbPrestamos.getColumnModel().getColumn(3).setPreferredWidth(120);
        tbPrestamos.getColumnModel().getColumn(4).setPreferredWidth(80);
        tbPrestamos.setRowHeight(30);
        
        // Configurar el botón en la tabla
        tbPrestamos.getColumn("Acción").setCellRenderer(new ButtonRenderer());
        tbPrestamos.getColumn("Acción").setCellEditor(new ButtonEditor(new javax.swing.JCheckBox()));
    }
    
    private void cargarPrestamosActivos() {
        modeloTabla.setRowCount(0);
        
        try {
            java.util.List<Prestamo> prestamos = prestamoService.listarPrestamos();
            for (Prestamo prestamo : prestamos) {
                if (prestamo.getEstado().equals("ACTIVO")) {
                    modeloTabla.addRow(new Object[]{
                        prestamo.getId(),
                        prestamo.getNombreCliente(),
                        prestamo.getFechaPrestamo(),
                        prestamo.getFechaDevolucionEsperada(),
                        "Editar"
                    });
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al cargar préstamos: " + e.getMessage());
        }
    }
    
    private void editarPrestamo(int row) {
        int idPrestamo = (int) tbPrestamos.getValueAt(row, 0);
        
        try {
            Prestamo prestamo = prestamoService.obtenerPrestamo(idPrestamo);
            EditarPrestamoDialog dialog = new EditarPrestamoDialog(
                (java.awt.Frame) javax.swing.SwingUtilities.getWindowAncestor(this), 
                true, 
                prestamo);
            dialog.setVisible(true);
            
            if (dialog.isActualizado()) {
                cargarPrestamosActivos();
                JOptionPane.showMessageDialog(this, "Fecha de devolución actualizada");
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al editar préstamo: " + e.getMessage());
        }
    }
    
    // Clase para renderizar el botón
    class ButtonRenderer extends javax.swing.JButton implements javax.swing.table.TableCellRenderer {
        public ButtonRenderer() {
            setOpaque(true);
        }
        
        @Override
        public java.awt.Component getTableCellRendererComponent(javax.swing.JTable table, Object value,
                boolean isSelected, boolean hasFocus, int row, int column) {
            setText((value == null) ? "Editar" : value.toString());
            return this;
        }
    }
    
    // Clase para manejar clics en el botón
    class ButtonEditor extends javax.swing.DefaultCellEditor {
        protected javax.swing.JButton button;
        private String label;
        private int selectedRow;
        
        public ButtonEditor(javax.swing.JCheckBox checkBox) {
            super(checkBox);
            button = new javax.swing.JButton();
            button.setOpaque(true);
            button.addActionListener(e -> {
                fireEditingStopped();
                editarPrestamo(selectedRow);
            });
        }
        
        @Override
        public java.awt.Component getTableCellEditorComponent(javax.swing.JTable table, Object value,
                boolean isSelected, int row, int column) {
            label = (value == null) ? "Editar" : value.toString();
            button.setText(label);
            selectedRow = row;
            return button;
        }
        
        @Override
        public Object getCellEditorValue() {
            return label;
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPasswordField1 = new javax.swing.JPasswordField();
        jpPrincipal = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        tbPrestamos = new javax.swing.JTable();
        lblTitulo = new javax.swing.JLabel();

        jPasswordField1.setText("jPasswordField1");

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

        lblTitulo.setFont(new java.awt.Font("Microsoft Sans Serif", 1, 24)); // NOI18N
        lblTitulo.setText("Bienvenido");

        javax.swing.GroupLayout jpPrincipalLayout = new javax.swing.GroupLayout(jpPrincipal);
        jpPrincipal.setLayout(jpPrincipalLayout);
        jpPrincipalLayout.setHorizontalGroup(
            jpPrincipalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jpPrincipalLayout.createSequentialGroup()
                .addGap(0, 46, Short.MAX_VALUE)
                .addGroup(jpPrincipalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jpPrincipalLayout.createSequentialGroup()
                        .addComponent(lblTitulo)
                        .addGap(277, 277, 277))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jpPrincipalLayout.createSequentialGroup()
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 674, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(37, 37, 37))))
        );
        jpPrincipalLayout.setVerticalGroup(
            jpPrincipalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jpPrincipalLayout.createSequentialGroup()
                .addGap(25, 25, 25)
                .addComponent(lblTitulo)
                .addGap(59, 59, 59)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 250, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(88, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jpPrincipal, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jpPrincipal, javax.swing.GroupLayout.DEFAULT_SIZE, 450, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPasswordField jPasswordField1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JPanel jpPrincipal;
    private javax.swing.JLabel lblTitulo;
    private javax.swing.JTable tbPrestamos;
    // End of variables declaration//GEN-END:variables
}
