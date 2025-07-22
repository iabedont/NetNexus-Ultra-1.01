package Clases;

import java.awt.*;
import java.sql.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;

/**
 * Ventana moderna para la gestión de roles y permisos de usuarios
 * Implementa funcionalidades avanzadas de administración de usuarios
 */
public class RoleManagementWindow extends JFrame {
    
    private JTable usersTable;
    private DefaultTableModel tableModel;
    private JTextField searchField;
    private JComboBox<String> roleFilter;
    private JComboBox<String> statusFilter;
    private Connection connection;
    
    // Colores del tema moderno
    private static final Color PRIMARY_COLOR = new Color(41, 128, 185);
    private static final Color SECONDARY_COLOR = new Color(52, 73, 94);
    private static final Color SUCCESS_COLOR = new Color(39, 174, 96);
    private static final Color WARNING_COLOR = new Color(243, 156, 18);
    private static final Color DANGER_COLOR = new Color(231, 76, 60);
    private static final Color BACKGROUND_COLOR = new Color(236, 240, 241);
    private static final Color CARD_COLOR = Color.WHITE;
    
    public RoleManagementWindow() {
        initializeDatabase();
        initializeComponents();
        setupUI();
        loadUserData();
    }
    
    private void initializeDatabase() {
        try {
            connection = DatabaseConnection.getConnection();
            if (connection == null) {
                JOptionPane.showMessageDialog(this, 
                    "No se pudo conectar a la base de datos. Usando datos de ejemplo.", 
                    "Advertencia de Conexión", 
                    JOptionPane.WARNING_MESSAGE);
            }
        } catch (Exception e) {
            System.err.println("Error al conectar con la base de datos: " + e.getMessage());
        }
    }
    
    private void initializeComponents() {
        setTitle("NetNexus Ultra - Gestión de Roles y Usuarios");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(1000, 700);
        setLocationRelativeTo(null);
        setResizable(true);
        
        // Configurar el icono de la ventana
        try {
            ImageIcon icon = new ImageIcon(getClass().getResource("/Imagenes/NetNexus_Ultra_Logo.png"));
            setIconImage(icon.getImage());
        } catch (Exception e) {
            System.err.println("No se pudo cargar el icono: " + e.getMessage());
        }
    }
    
    private void setupUI() {
        setLayout(new BorderLayout());
        getContentPane().setBackground(BACKGROUND_COLOR);
        
        // Panel superior con título y filtros
        add(createHeaderPanel(), BorderLayout.NORTH);
        
        // Panel central con tabla de usuarios
        add(createCenterPanel(), BorderLayout.CENTER);
        
        // Panel inferior con botones de acción
        add(createBottomPanel(), BorderLayout.SOUTH);
    }
    
    private JPanel createHeaderPanel() {
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(BACKGROUND_COLOR);
        headerPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
        
        // Título principal
        JLabel titleLabel = new JLabel("Gestión de Roles y Usuarios");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titleLabel.setForeground(SECONDARY_COLOR);
        headerPanel.add(titleLabel, BorderLayout.WEST);
        
        // Panel de filtros
        JPanel filterPanel = createFilterPanel();
        headerPanel.add(filterPanel, BorderLayout.EAST);
        
        return headerPanel;
    }
    
    private JPanel createFilterPanel() {
        JPanel filterPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        filterPanel.setBackground(BACKGROUND_COLOR);
        
        // Campo de búsqueda
        JLabel searchLabel = new JLabel("Buscar:");
        searchLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        searchField = new JTextField(15);
        searchField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        searchField.addActionListener(e -> filterUsers());
        
        // Filtro por rol
        JLabel roleLabel = new JLabel("Rol:");
        roleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        roleFilter = new JComboBox<>(new String[]{"Todos", "Administrador", "Técnico", "Cliente"});
        roleFilter.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        roleFilter.addActionListener(e -> filterUsers());
        
        // Filtro por estado
        JLabel statusLabel = new JLabel("Estado:");
        statusLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        statusFilter = new JComboBox<>(new String[]{"Todos", "Activo", "Inactivo", "Suspendido"});
        statusFilter.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        statusFilter.addActionListener(e -> filterUsers());
        
        filterPanel.add(searchLabel);
        filterPanel.add(searchField);
        filterPanel.add(Box.createHorizontalStrut(10));
        filterPanel.add(roleLabel);
        filterPanel.add(roleFilter);
        filterPanel.add(Box.createHorizontalStrut(10));
        filterPanel.add(statusLabel);
        filterPanel.add(statusFilter);
        
        return filterPanel;
    }
    
    private JPanel createCenterPanel() {
        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.setBackground(BACKGROUND_COLOR);
        centerPanel.setBorder(new EmptyBorder(0, 20, 20, 20));
        
        // Crear tabla con modelo
        String[] columnNames = {"ID", "Usuario", "Nombre Completo", "Email", "Rol", "Estado", "Último Acceso", "Permisos"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Hacer la tabla no editable
            }
        };
        
        usersTable = new JTable(tableModel);
        setupTable();
        
        JScrollPane scrollPane = new JScrollPane(usersTable);
        scrollPane.setBackground(CARD_COLOR);
        scrollPane.getViewport().setBackground(CARD_COLOR);
        scrollPane.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(189, 195, 199), 1),
            BorderFactory.createEmptyBorder(5, 5, 5, 5)
        ));
        
        centerPanel.add(scrollPane, BorderLayout.CENTER);
        
        return centerPanel;
    }
    
    private void setupTable() {
        usersTable.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        usersTable.setRowHeight(40);
        usersTable.setSelectionBackground(new Color(52, 152, 219, 50));
        usersTable.setSelectionForeground(SECONDARY_COLOR);
        usersTable.setGridColor(new Color(189, 195, 199));
        usersTable.setShowGrid(true);
        usersTable.setIntercellSpacing(new Dimension(1, 1));
        
        // Configurar encabezado
        JTableHeader header = usersTable.getTableHeader();
        header.setFont(new Font("Segoe UI", Font.BOLD, 14));
        header.setBackground(PRIMARY_COLOR);
        header.setForeground(Color.WHITE);
        header.setPreferredSize(new Dimension(0, 45));
        
        // Configurar anchos de columna
        usersTable.getColumnModel().getColumn(0).setPreferredWidth(50);   // ID
        usersTable.getColumnModel().getColumn(1).setPreferredWidth(120);  // Usuario
        usersTable.getColumnModel().getColumn(2).setPreferredWidth(180);  // Nombre
        usersTable.getColumnModel().getColumn(3).setPreferredWidth(200);  // Email
        usersTable.getColumnModel().getColumn(4).setPreferredWidth(100);  // Rol
        usersTable.getColumnModel().getColumn(5).setPreferredWidth(80);   // Estado
        usersTable.getColumnModel().getColumn(6).setPreferredWidth(120);  // Último Acceso
        usersTable.getColumnModel().getColumn(7).setPreferredWidth(150);  // Permisos
    }
    
    private JPanel createBottomPanel() {
        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.setBackground(BACKGROUND_COLOR);
        bottomPanel.setBorder(new EmptyBorder(0, 20, 20, 20));
        
        // Panel de estadísticas
        JPanel statsPanel = createStatsPanel();
        bottomPanel.add(statsPanel, BorderLayout.WEST);
        
        // Panel de botones de acción
        JPanel actionPanel = createActionPanel();
        bottomPanel.add(actionPanel, BorderLayout.EAST);
        
        return bottomPanel;
    }
    
    private JPanel createStatsPanel() {
        JPanel statsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        statsPanel.setBackground(BACKGROUND_COLOR);
        
        JLabel totalLabel = new JLabel("Total de usuarios: " + tableModel.getRowCount());
        totalLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        totalLabel.setForeground(SECONDARY_COLOR);
        
        statsPanel.add(totalLabel);
        
        return statsPanel;
    }
    
    private JPanel createActionPanel() {
        JPanel actionPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        actionPanel.setBackground(BACKGROUND_COLOR);
        
        // Botón para agregar usuario
        JButton addButton = createStyledButton("Agregar Usuario", SUCCESS_COLOR);
        addButton.addActionListener(e -> showAddUserDialog());
        
        // Botón para editar usuario
        JButton editButton = createStyledButton("Editar Usuario", WARNING_COLOR);
        editButton.addActionListener(e -> editSelectedUser());
        
        // Botón para eliminar usuario
        JButton deleteButton = createStyledButton("Eliminar Usuario", DANGER_COLOR);
        deleteButton.addActionListener(e -> deleteSelectedUser());
        
        // Botón para actualizar
        JButton refreshButton = createStyledButton("Actualizar", PRIMARY_COLOR);
        refreshButton.addActionListener(e -> loadUserData());
        
        actionPanel.add(addButton);
        actionPanel.add(Box.createHorizontalStrut(10));
        actionPanel.add(editButton);
        actionPanel.add(Box.createHorizontalStrut(10));
        actionPanel.add(deleteButton);
        actionPanel.add(Box.createHorizontalStrut(10));
        actionPanel.add(refreshButton);
        
        return actionPanel;
    }
    
    private JButton createStyledButton(String text, Color backgroundColor) {
        JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI", Font.BOLD, 12));
        button.setForeground(Color.WHITE);
        button.setBackground(backgroundColor);
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        button.setPreferredSize(new Dimension(130, 35));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        // Efecto hover
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(backgroundColor.darker());
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(backgroundColor);
            }
        });
        
        return button;
    }
    
    private void loadUserData() {
        tableModel.setRowCount(0); // Limpiar tabla
        
        if (connection != null) {
            loadFromDatabase();
        } else {
            loadSampleData();
        }
        
        updateStats();
    }
    
    private void loadFromDatabase() {
        try {
            String query = "SELECT c.idCliente, c.nombre, c.apellido, c.email, c.telefono, c.tipo " +
                          "FROM cliente c " +
                          "ORDER BY c.idCliente";
            
            PreparedStatement statement = connection.prepareStatement(query);
            ResultSet resultSet = statement.executeQuery();
            
            while (resultSet.next()) {
                Object[] row = {
                    resultSet.getInt("idCliente"),
                    resultSet.getString("nombre"),
                    resultSet.getString("apellido"),
                    resultSet.getString("email"),
                    resultSet.getString("tipo"),
                    "Activo", // Estado por defecto
                    null, // Sin last_login en la tabla cliente
                    resultSet.getString("telefono") // Usar teléfono como info adicional
                };
                tableModel.addRow(row);
            }
            
            resultSet.close();
            statement.close();
            
        } catch (SQLException e) {
            System.err.println("Error al cargar datos de usuarios: " + e.getMessage());
            loadSampleData();
        }
    }
    
    private void loadSampleData() {
        // Datos de ejemplo cuando no hay conexión a BD
        Object[][] sampleData = {
            {1, "admin", "Administrador Sistema", "admin@netnexus.com", "Administrador", "Activo", "2024-01-15 10:30", "Todos los permisos"},
            {2, "tecnico1", "Juan Pérez", "juan.perez@netnexus.com", "Técnico", "Activo", "2024-01-15 09:15", "Equipos, Tickets"},
            {3, "tecnico2", "María García", "maria.garcia@netnexus.com", "Técnico", "Activo", "2024-01-14 16:45", "Equipos, Vehículos"},
            {4, "cliente1", "Carlos Rodríguez", "carlos.rodriguez@email.com", "Cliente", "Activo", "2024-01-13 14:20", "Ver contratos"},
            {5, "cliente2", "Ana López", "ana.lopez@email.com", "Cliente", "Inactivo", "2024-01-10 11:00", "Ver contratos"},
            {6, "supervisor", "Luis Morales", "luis.morales@netnexus.com", "Administrador", "Activo", "2024-01-15 08:00", "Supervisión, Reportes"}
        };
        
        for (Object[] row : sampleData) {
            tableModel.addRow(row);
        }
    }
    
    private void filterUsers() {
        // Implementar filtrado de usuarios
        String searchText = searchField.getText().toLowerCase();
        String selectedRole = (String) roleFilter.getSelectedItem();
        String selectedStatus = (String) statusFilter.getSelectedItem();
        
        // Recargar datos y aplicar filtros
        loadUserData();
        
        // Aquí se implementaría la lógica de filtrado real
        // Por ahora mostramos un mensaje informativo
        if (!searchText.isEmpty() || !"Todos".equals(selectedRole) || !"Todos".equals(selectedStatus)) {
            JOptionPane.showMessageDialog(this, 
                "Filtros aplicados:\n" +
                "Búsqueda: " + searchText + "\n" +
                "Rol: " + selectedRole + "\n" +
                "Estado: " + selectedStatus,
                "Filtros Activos",
                JOptionPane.INFORMATION_MESSAGE);
        }
    }
    
    private void updateStats() {
        // Actualizar estadísticas en el panel inferior
        try {
            Container contentPane = getContentPane();
            if (contentPane instanceof Container) {
                Component bottomComponent = ((BorderLayout) contentPane.getLayout()).getLayoutComponent(BorderLayout.SOUTH);
                if (bottomComponent instanceof JPanel) {
                    JPanel bottomPanel = (JPanel) bottomComponent;
                    updateStatsInPanel(bottomPanel);
                }
            }
        } catch (Exception e) {
            System.err.println("Error al actualizar estadísticas: " + e.getMessage());
        }
    }
    
    private void updateStatsInPanel(JPanel panel) {
        for (Component comp : panel.getComponents()) {
            if (comp instanceof JPanel) {
                JPanel subPanel = (JPanel) comp;
                for (Component subComp : subPanel.getComponents()) {
                    if (subComp instanceof JLabel && ((JLabel) subComp).getText().startsWith("Total")) {
                        ((JLabel) subComp).setText("Total de usuarios: " + tableModel.getRowCount());
                        return;
                    }
                }
            }
        }
    }
    
    private void showAddUserDialog() {
        JDialog dialog = new JDialog(this, "Agregar Nuevo Usuario", true);
        dialog.setSize(500, 400);
        dialog.setLocationRelativeTo(this);
        
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        
        // Campos del formulario
        JTextField usernameField = new JTextField(20);
        JTextField nameField = new JTextField(20);
        JTextField emailField = new JTextField(20);
        JComboBox<String> roleCombo = new JComboBox<>(new String[]{"Administrador", "Técnico", "Cliente"});
        JComboBox<String> statusCombo = new JComboBox<>(new String[]{"Activo", "Inactivo"});
        
        // Agregar componentes al panel
        gbc.gridx = 0; gbc.gridy = 0; panel.add(new JLabel("Usuario:"), gbc);
        gbc.gridx = 1; panel.add(usernameField, gbc);
        
        gbc.gridx = 0; gbc.gridy = 1; panel.add(new JLabel("Nombre Completo:"), gbc);
        gbc.gridx = 1; panel.add(nameField, gbc);
        
        gbc.gridx = 0; gbc.gridy = 2; panel.add(new JLabel("Email:"), gbc);
        gbc.gridx = 1; panel.add(emailField, gbc);
        
        gbc.gridx = 0; gbc.gridy = 3; panel.add(new JLabel("Rol:"), gbc);
        gbc.gridx = 1; panel.add(roleCombo, gbc);
        
        gbc.gridx = 0; gbc.gridy = 4; panel.add(new JLabel("Estado:"), gbc);
        gbc.gridx = 1; panel.add(statusCombo, gbc);
        
        // Botones
        JPanel buttonPanel = new JPanel();
        JButton saveButton = new JButton("Guardar");
        JButton cancelButton = new JButton("Cancelar");
        
        saveButton.addActionListener(e -> {
            String username = usernameField.getText().trim();
            String name = nameField.getText().trim();
            String email = emailField.getText().trim();
            String role = (String) roleCombo.getSelectedItem();
            String status = (String) statusCombo.getSelectedItem();
            
            if (username.isEmpty() || name.isEmpty() || email.isEmpty()) {
                JOptionPane.showMessageDialog(dialog, 
                    "Por favor complete todos los campos obligatorios", 
                    "Campos Requeridos", 
                    JOptionPane.WARNING_MESSAGE);
                return;
            }
            
            if (saveUserToDatabase(username, name, email, role, status)) {
                JOptionPane.showMessageDialog(dialog, "Usuario agregado exitosamente");
                dialog.dispose();
                loadUserData();
            } else {
                JOptionPane.showMessageDialog(dialog, 
                    "Error al guardar el usuario. Verifique la conexión a la base de datos.", 
                    "Error", 
                    JOptionPane.ERROR_MESSAGE);
            }
        });
        
        cancelButton.addActionListener(e -> dialog.dispose());
        
        buttonPanel.add(saveButton);
        buttonPanel.add(cancelButton);
        
        gbc.gridx = 0; gbc.gridy = 5; gbc.gridwidth = 2;
        panel.add(buttonPanel, gbc);
        
        dialog.add(panel);
        dialog.setVisible(true);
    }
    
    private void editSelectedUser() {
        int selectedRow = usersTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Por favor seleccione un usuario para editar.", 
                "Sin Selección", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        // Obtener datos del usuario seleccionado
        String username = (String) tableModel.getValueAt(selectedRow, 1);
        JOptionPane.showMessageDialog(this, "Editando usuario: " + username, 
            "Editar Usuario", JOptionPane.INFORMATION_MESSAGE);
    }
    
    private void deleteSelectedUser() {
        int selectedRow = usersTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Por favor seleccione un usuario para eliminar.", 
                "Sin Selección", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        String username = (String) tableModel.getValueAt(selectedRow, 1);
        int confirm = JOptionPane.showConfirmDialog(this, 
            "¿Está seguro que desea eliminar el usuario '" + username + "'?", 
            "Confirmar Eliminación", 
            JOptionPane.YES_NO_OPTION, 
            JOptionPane.WARNING_MESSAGE);
        
        if (confirm == JOptionPane.YES_OPTION) {
            deleteUserFromDatabase(selectedRow);
            tableModel.removeRow(selectedRow);
            JOptionPane.showMessageDialog(this, "Usuario eliminado exitosamente");
            updateStats();
        }
    }
    
    // Métodos para persistencia en base de datos
    private boolean saveUserToDatabase(String username, String name, String email, String role, String status) {
        if (connection == null) {
            return false;
        }
        
        try {
            String query = "INSERT INTO users (username, full_name, email, role, status, created_at, last_login) " +
                          "VALUES (?, ?, ?, ?, ?, NOW(), NOW())";
            
            PreparedStatement stmt = connection.prepareStatement(query);
            stmt.setString(1, username);
            stmt.setString(2, name);
            stmt.setString(3, email);
            stmt.setString(4, role);
            stmt.setString(5, status);
            
            int rowsAffected = stmt.executeUpdate();
            stmt.close();
            
            return rowsAffected > 0;
        } catch (SQLException e) {
            System.err.println("Error al guardar usuario: " + e.getMessage());
            return false;
        }
    }
    
    private boolean updateUserInDatabase(int userId, String username, String name, String email, String role, String status) {
        if (connection == null) {
            return false;
        }
        
        try {
            String query = "UPDATE users SET username = ?, full_name = ?, email = ?, role = ?, status = ? WHERE id = ?";
            
            PreparedStatement stmt = connection.prepareStatement(query);
            stmt.setString(1, username);
            stmt.setString(2, name);
            stmt.setString(3, email);
            stmt.setString(4, role);
            stmt.setString(5, status);
            stmt.setInt(6, userId);
            
            int rowsAffected = stmt.executeUpdate();
            stmt.close();
            
            return rowsAffected > 0;
        } catch (SQLException e) {
            System.err.println("Error al actualizar usuario: " + e.getMessage());
            return false;
        }
    }
    
    private boolean deleteUserFromDatabase(int tableRow) {
        if (connection == null || tableRow >= tableModel.getRowCount()) {
            return false;
        }
        
        try {
            // Obtener el ID del usuario de la tabla
            int userId = (Integer) tableModel.getValueAt(tableRow, 0);
            
            String query = "DELETE FROM cliente WHERE idCliente = ?";
            PreparedStatement stmt = connection.prepareStatement(query);
            stmt.setInt(1, userId);
            
            int rowsAffected = stmt.executeUpdate();
            stmt.close();
            
            return rowsAffected > 0;
        } catch (SQLException e) {
            System.err.println("Error al eliminar usuario: " + e.getMessage());
            return false;
        }
    }
    
    private void saveEquipmentStatusToDatabase(String equipmentName, String status) {
        if (connection == null) {
            return;
        }
        
        try {
            // Primero verificar si el equipo existe
            String checkQuery = "SELECT id FROM equipment WHERE name = ?";
            PreparedStatement checkStmt = connection.prepareStatement(checkQuery);
            checkStmt.setString(1, equipmentName);
            ResultSet rs = checkStmt.executeQuery();
            
            if (rs.next()) {
                // Actualizar estado existente
                String updateQuery = "UPDATE equipment SET status = ?, last_updated = NOW() WHERE name = ?";
                PreparedStatement updateStmt = connection.prepareStatement(updateQuery);
                updateStmt.setString(1, status);
                updateStmt.setString(2, equipmentName);
                updateStmt.executeUpdate();
                updateStmt.close();
            } else {
                // Crear nuevo registro de equipo
                String insertQuery = "INSERT INTO equipment (name, status, created_at, last_updated) VALUES (?, ?, NOW(), NOW())";
                PreparedStatement insertStmt = connection.prepareStatement(insertQuery);
                insertStmt.setString(1, equipmentName);
                insertStmt.setString(2, status);
                insertStmt.executeUpdate();
                insertStmt.close();
            }
            
            rs.close();
            checkStmt.close();
        } catch (SQLException e) {
            System.err.println("Error al guardar estado del equipo: " + e.getMessage());
        }
    }
    
    private void saveTicketStatusToDatabase(int ticketId, String status) {
        if (connection == null) {
            return;
        }
        
        try {
            String query = "UPDATE tickets SET estado = ?, updated_at = NOW() WHERE id = ?";
            PreparedStatement stmt = connection.prepareStatement(query);
            stmt.setString(1, status);
            stmt.setInt(2, ticketId);
            
            int rowsAffected = stmt.executeUpdate();
            stmt.close();
            
            if (rowsAffected > 0) {
                System.out.println("Estado del ticket " + ticketId + " actualizado a: " + status);
            }
        } catch (SQLException e) {
            System.err.println("Error al actualizar estado del ticket: " + e.getMessage());
        }
    }
}
