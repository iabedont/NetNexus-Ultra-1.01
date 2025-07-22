package Clases;

import java.awt.*;
import java.sql.*;
import java.util.Vector;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class UserRoleManagementFrame extends JFrame {
    private JTable usersTable;
    private DefaultTableModel tableModel;
    private JComboBox<String> roleComboBox;
    private JButton updateRoleButton;
    private JButton refreshButton;

    public UserRoleManagementFrame() {
        setTitle("Gestión de Roles de Usuario - Administrador");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        initComponents();
        loadUsers();
    }

    private void initComponents() {
        setLayout(new BorderLayout());

        // Panel superior con título
        JPanel topPanel = new JPanel();
        topPanel.setBackground(new Color(220, 20, 60));
        JLabel titleLabel = new JLabel("Gestión de Roles de Usuario");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        topPanel.add(titleLabel);
        add(topPanel, BorderLayout.NORTH);

        // Panel central con tabla
        String[] columnNames = {"ID Cliente", "Nombre", "Apellido", "Email", "Teléfono", "Rol Actual"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Hacer la tabla no editable
            }
        };
        usersTable = new JTable(tableModel);
        usersTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        usersTable.setRowHeight(25);
        usersTable.getTableHeader().setBackground(new Color(70, 130, 180));
        usersTable.getTableHeader().setForeground(Color.WHITE);

        JScrollPane scrollPane = new JScrollPane(usersTable);
        add(scrollPane, BorderLayout.CENTER);

        // Panel inferior con controles
        JPanel bottomPanel = new JPanel(new FlowLayout());
        bottomPanel.setBackground(Color.LIGHT_GRAY);

        JLabel roleLabel = new JLabel("Nuevo Rol:");
        roleComboBox = new JComboBox<>(new String[]{"Usuario", "Administrador", "Técnico"});
        roleComboBox.setPreferredSize(new Dimension(120, 30));

        updateRoleButton = new JButton("Actualizar Rol");
        updateRoleButton.setBackground(new Color(34, 139, 34));
        updateRoleButton.setForeground(Color.WHITE);
        updateRoleButton.setFocusPainted(false);
        updateRoleButton.addActionListener(e -> updateUserRole());

        refreshButton = new JButton("Actualizar Lista");
        refreshButton.setBackground(new Color(70, 130, 180));
        refreshButton.setForeground(Color.WHITE);
        refreshButton.setFocusPainted(false);
        refreshButton.addActionListener(e -> loadUsers());

        JButton closeButton = new JButton("Cerrar");
        closeButton.addActionListener(e -> dispose());

        bottomPanel.add(roleLabel);
        bottomPanel.add(roleComboBox);
        bottomPanel.add(updateRoleButton);
        bottomPanel.add(refreshButton);
        bottomPanel.add(closeButton);

        add(bottomPanel, BorderLayout.SOUTH);
    }

    private void loadUsers() {
        // Limpiar tabla
        tableModel.setRowCount(0);

        try (Connection conn = DatabaseConnection.getConnection()) {
            String sql = "SELECT idCliente, nombre, apellido, email, telefono, tipo FROM cliente ORDER BY tipo, nombre";
            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Vector<String> row = new Vector<>();
                row.add(String.valueOf(rs.getInt("idCliente")));
                row.add(rs.getString("nombre"));
                row.add(rs.getString("apellido"));
                row.add(rs.getString("email"));
                row.add(rs.getString("telefono"));
                row.add(rs.getString("tipo"));
                tableModel.addRow(row);
            }

            JOptionPane.showMessageDialog(this, 
                "Lista de usuarios actualizada. Total: " + tableModel.getRowCount() + " usuarios", 
                "Información", 
                JOptionPane.INFORMATION_MESSAGE);

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, 
                "Error al cargar usuarios: " + e.getMessage(), 
                "Error", 
                JOptionPane.ERROR_MESSAGE);
        }
    }

    private void updateUserRole() {
        int selectedRow = usersTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, 
                "Por favor seleccione un usuario de la tabla", 
                "Error", 
                JOptionPane.WARNING_MESSAGE);
            return;
        }

        String idCliente = (String) tableModel.getValueAt(selectedRow, 0);
        String currentRole = (String) tableModel.getValueAt(selectedRow, 5);
        String newRole = (String) roleComboBox.getSelectedItem();
        String userName = (String) tableModel.getValueAt(selectedRow, 1) + " " + (String) tableModel.getValueAt(selectedRow, 2);

        if (currentRole.equals(newRole)) {
            JOptionPane.showMessageDialog(this, 
                "El usuario ya tiene el rol seleccionado", 
                "Información", 
                JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this, 
            "¿Está seguro de cambiar el rol de " + userName + " de '" + currentRole + "' a '" + newRole + "'?", 
            "Confirmar Cambio de Rol", 
            JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            try (Connection conn = DatabaseConnection.getConnection()) {
                String sql = "UPDATE cliente SET tipo = ? WHERE idCliente = ?";
                PreparedStatement stmt = conn.prepareStatement(sql);
                stmt.setString(1, newRole);
                stmt.setString(2, idCliente);
                
                int rowsAffected = stmt.executeUpdate();
                
                if (rowsAffected > 0) {
                    JOptionPane.showMessageDialog(this, 
                        "Rol actualizado exitosamente.\n" + userName + " ahora es '" + newRole + "'", 
                        "Éxito", 
                        JOptionPane.INFORMATION_MESSAGE);
                    
                    // Actualizar la tabla
                    tableModel.setValueAt(newRole, selectedRow, 5);
                } else {
                    JOptionPane.showMessageDialog(this, 
                        "No se pudo actualizar el rol", 
                        "Error", 
                        JOptionPane.ERROR_MESSAGE);
                }

            } catch (SQLException e) {
                JOptionPane.showMessageDialog(this, 
                    "Error al actualizar rol: " + e.getMessage(), 
                    "Error", 
                    JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}
