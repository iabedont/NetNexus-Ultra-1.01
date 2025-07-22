package Clases;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.util.Vector;

public class ModernVehicleWindow extends JFrame {
    private JTable vehicleTable;
    private DefaultTableModel tableModel;
    private JTextField marcaField, modeloField, anoField, placaField, estadoField;
    private JButton addButton, updateButton, deleteButton, clearButton;
    private BackgroundPanel backgroundPanel;
    private int selectedVehicleId = -1;

    public ModernVehicleWindow() {
        initializeComponents();
        setupModernGUI();
        loadVehicles();
        setupEventHandlers();
    }

    private void initializeComponents() {
        setTitle("Gestión Moderna de Flota de Vehículos");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(1300, 800);
        setLocationRelativeTo(null);

        // Panel de fondo con imagen
        backgroundPanel = new BackgroundPanel("/Imagenes/FondoLogin.jpg");
        backgroundPanel.setLayout(new BorderLayout());
        setContentPane(backgroundPanel);

        // Modelo de tabla
        String[] columnNames = {"ID", "Marca", "Modelo", "Año", "Placa", "Estado"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        vehicleTable = new JTable(tableModel);
        
        // Inicializar campos de texto
        marcaField = new JTextField(20);
        modeloField = new JTextField(20);
        anoField = new JTextField(20);
        placaField = new JTextField(20);
        estadoField = new JTextField(20);
    }

    private void setupModernGUI() {
        // Panel principal
        JPanel mainPanel = new JPanel(new BorderLayout(20, 20));
        mainPanel.setOpaque(false);
        mainPanel.setBorder(new EmptyBorder(30, 30, 30, 30));

        // Panel superior con título
        JPanel headerPanel = createHeaderPanel();
        mainPanel.add(headerPanel, BorderLayout.NORTH);

        // Panel central dividido
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        splitPane.setOpaque(false);
        splitPane.setDividerLocation(750);
        splitPane.setResizeWeight(0.6);

        // Panel izquierdo - Tabla de vehículos
        JPanel tablePanel = createTablePanel();
        splitPane.setLeftComponent(tablePanel);

        // Panel derecho - Formulario
        JPanel formPanel = createFormPanel();
        splitPane.setRightComponent(formPanel);

        mainPanel.add(splitPane, BorderLayout.CENTER);

        // Panel inferior con estadísticas
        JPanel footerPanel = createFooterPanel();
        mainPanel.add(footerPanel, BorderLayout.SOUTH);

        backgroundPanel.add(mainPanel);
    }

    private JPanel createHeaderPanel() {
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setOpaque(false);

        JLabel titleLabel = new JLabel("🚗 Gestión de Flota de Vehículos", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 32));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setBorder(new EmptyBorder(10, 0, 20, 0));

        // Panel de estadísticas rápidas
        JPanel statsPanel = new JPanel(new FlowLayout());
        statsPanel.setOpaque(false);

        JLabel totalVehiclesLabel = new JLabel("Total: " + tableModel.getRowCount());
        totalVehiclesLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        totalVehiclesLabel.setForeground(new Color(46, 204, 113));
        totalVehiclesLabel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(46, 204, 113), 2),
            new EmptyBorder(5, 15, 5, 15)
        ));

        statsPanel.add(totalVehiclesLabel);

        headerPanel.add(titleLabel, BorderLayout.CENTER);
        headerPanel.add(statsPanel, BorderLayout.SOUTH);
        return headerPanel;
    }

    private JPanel createTablePanel() {
        JPanel tablePanel = new JPanel(new BorderLayout(10, 10));
        tablePanel.setOpaque(false);
        tablePanel.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(Color.WHITE, 2),
            "Lista de Vehículos",
            0, 0,
            new Font("Segoe UI", Font.BOLD, 16),
            Color.WHITE
        ));

        // Configurar tabla moderna
        setupModernTable();
        JScrollPane scrollPane = new JScrollPane(vehicleTable);
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);
        scrollPane.setBorder(BorderFactory.createLineBorder(Color.WHITE));
        scrollPane.setPreferredSize(new Dimension(700, 500));

        // Panel de búsqueda y filtros
        JPanel searchPanel = createSearchPanel();
        tablePanel.add(searchPanel, BorderLayout.NORTH);
        tablePanel.add(scrollPane, BorderLayout.CENTER);

        return tablePanel;
    }

    private JPanel createSearchPanel() {
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        searchPanel.setOpaque(false);

        JLabel searchLabel = new JLabel("🔍 Buscar:");
        searchLabel.setForeground(Color.WHITE);
        searchLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));

        JTextField searchField = new JTextField(20);
        searchField.setFont(new Font("Segoe UI", Font.PLAIN, 12));

        JComboBox<String> filterCombo = new JComboBox<>(new String[]{"Todos", "Disponible", "En uso", "Mantenimiento", "Fuera de servicio"});
        filterCombo.setFont(new Font("Segoe UI", Font.PLAIN, 12));

        JButton refreshButton = createStyledButton("🔄 Actualizar", new Color(52, 152, 219), 100, 35);
        refreshButton.addActionListener(e -> loadVehicles());

        searchPanel.add(searchLabel);
        searchPanel.add(searchField);
        searchPanel.add(Box.createHorizontalStrut(10));
        searchPanel.add(new JLabel("Estado:") {{ setForeground(Color.WHITE); setFont(new Font("Segoe UI", Font.BOLD, 12)); }});
        searchPanel.add(filterCombo);
        searchPanel.add(Box.createHorizontalStrut(10));
        searchPanel.add(refreshButton);

        // Funcionalidad de búsqueda en tiempo real
        searchField.addCaretListener(e -> filterVehicles(searchField.getText(), (String) filterCombo.getSelectedItem()));
        filterCombo.addActionListener(e -> filterVehicles(searchField.getText(), (String) filterCombo.getSelectedItem()));

        return searchPanel;
    }

    private void setupModernTable() {
        vehicleTable.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        vehicleTable.setRowHeight(40);
        vehicleTable.setSelectionBackground(new Color(52, 152, 219, 150));
        vehicleTable.setSelectionForeground(Color.WHITE);
        vehicleTable.setGridColor(new Color(200, 200, 200));
        vehicleTable.setShowGrid(true);
        vehicleTable.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));
        vehicleTable.getTableHeader().setBackground(new Color(52, 152, 219));
        vehicleTable.getTableHeader().setForeground(Color.WHITE);
        vehicleTable.getTableHeader().setPreferredSize(new Dimension(0, 35));

        // Configurar ancho de columnas
        int[] widths = {60, 120, 120, 80, 120, 120};
        for (int i = 0; i < widths.length; i++) {
            vehicleTable.getColumnModel().getColumn(i).setPreferredWidth(widths[i]);
        }

        // Renderer personalizado para el estado
        vehicleTable.getColumn("Estado").setCellRenderer(new StatusCellRenderer());
    }

    private JPanel createFormPanel() {
        JPanel formPanel = new JPanel(new BorderLayout());
        formPanel.setOpaque(false);
        formPanel.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(Color.WHITE, 2),
            "Datos del Vehículo",
            0, 0,
            new Font("Segoe UI", Font.BOLD, 16),
            Color.WHITE
        ));

        // Panel de campos
        JPanel fieldsPanel = new JPanel(new GridBagLayout());
        fieldsPanel.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(15, 15, 15, 15);
        gbc.anchor = GridBagConstraints.WEST;

        // Campos del formulario
        addFormField(fieldsPanel, "🏷️ Marca:", marcaField, gbc, 0);
        addFormField(fieldsPanel, "🚙 Modelo:", modeloField, gbc, 1);
        addFormField(fieldsPanel, "📅 Año:", anoField, gbc, 2);
        addFormField(fieldsPanel, "🔢 Placa:", placaField, gbc, 3);
        addFormField(fieldsPanel, "📊 Estado:", estadoField, gbc, 4);

        // Panel de botones
        JPanel buttonPanel = createButtonPanel();

        formPanel.add(fieldsPanel, BorderLayout.CENTER);
        formPanel.add(buttonPanel, BorderLayout.SOUTH);

        return formPanel;
    }

    private void addFormField(JPanel parent, String labelText, JTextField field, GridBagConstraints gbc, int row) {
        gbc.gridx = 0; gbc.gridy = row;
        JLabel label = new JLabel(labelText);
        label.setForeground(Color.WHITE);
        label.setFont(new Font("Segoe UI", Font.BOLD, 14));
        parent.add(label, gbc);

        gbc.gridx = 1; gbc.gridy = row; gbc.fill = GridBagConstraints.HORIZONTAL; gbc.weightx = 1.0;
        field.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        field.setPreferredSize(new Dimension(200, 35));
        field.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Color.GRAY),
            new EmptyBorder(8, 12, 8, 12)
        ));
        parent.add(field, gbc);
        
        gbc.fill = GridBagConstraints.NONE; gbc.weightx = 0;
    }

    private JPanel createButtonPanel() {
        JPanel buttonPanel = new JPanel(new GridLayout(2, 2, 15, 15));
        buttonPanel.setOpaque(false);
        buttonPanel.setBorder(new EmptyBorder(20, 15, 15, 15));

        // Crear botones con estilos modernos
        addButton = createStyledButton("➕ Agregar", new Color(46, 204, 113), 140, 45);
        updateButton = createStyledButton("✏️ Actualizar", new Color(52, 152, 219), 140, 45);
        deleteButton = createStyledButton("🗑️ Eliminar", new Color(231, 76, 60), 140, 45);
        clearButton = createStyledButton("🧹 Limpiar", new Color(149, 165, 166), 140, 45);

        buttonPanel.add(addButton);
        buttonPanel.add(updateButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(clearButton);

        return buttonPanel;
    }

    private JButton createStyledButton(String text, Color color, int width, int height) {
        JButton button = new JButton(text);
        button.setPreferredSize(new Dimension(width, height));
        button.setFont(new Font("Segoe UI", Font.BOLD, 12));
        button.setForeground(Color.WHITE);
        button.setBackground(color);
        button.setBorder(new EmptyBorder(10, 15, 10, 15));
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // Efectos hover
        Color hoverColor = color.brighter();
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(hoverColor);
            }
            
            @Override
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(color);
            }
        });

        return button;
    }

    private JPanel createFooterPanel() {
        JPanel footerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        footerPanel.setOpaque(false);

        JButton closeButton = createStyledButton("🚪 Cerrar Ventana", new Color(108, 117, 125), 150, 40);
        closeButton.addActionListener(e -> dispose());

        footerPanel.add(closeButton);
        return footerPanel;
    }

    private void setupEventHandlers() {
        addButton.addActionListener(e -> addVehicle());
        updateButton.addActionListener(e -> updateVehicle());
        deleteButton.addActionListener(e -> deleteVehicle());
        clearButton.addActionListener(e -> clearFields());

        vehicleTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int selectedRow = vehicleTable.getSelectedRow();
                if (selectedRow != -1) {
                    populateFields(selectedRow);
                }
            }
        });
    }

    private void loadVehicles() {
        tableModel.setRowCount(0);
        String sql = "SELECT id, marca, modelo, ano, placa, estado FROM vehiculos ORDER BY marca, modelo";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            
            while (rs.next()) {
                Vector<Object> row = new Vector<>();
                row.add(rs.getInt("id"));
                row.add(rs.getString("marca"));
                row.add(rs.getString("modelo"));
                row.add(rs.getInt("ano"));
                row.add(rs.getString("placa"));
                row.add(rs.getString("estado"));
                tableModel.addRow(row);
            }
            updateVehicleCount();
        } catch (SQLException e) {
            showErrorMessage("Error al cargar vehículos: " + e.getMessage());
        }
    }

    private void filterVehicles(String searchText, String statusFilter) {
        tableModel.setRowCount(0);
        StringBuilder sql = new StringBuilder("SELECT id, marca, modelo, ano, placa, estado FROM vehiculos WHERE 1=1");
        
        if (!searchText.trim().isEmpty()) {
            sql.append(" AND (marca LIKE ? OR modelo LIKE ? OR placa LIKE ?)");
        }
        
        if (!"Todos".equals(statusFilter)) {
            sql.append(" AND estado = ?");
        }
        
        sql.append(" ORDER BY marca, modelo");
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql.toString())) {
            
            int paramIndex = 1;
            if (!searchText.trim().isEmpty()) {
                String searchPattern = "%" + searchText + "%";
                pstmt.setString(paramIndex++, searchPattern);
                pstmt.setString(paramIndex++, searchPattern);
                pstmt.setString(paramIndex++, searchPattern);
            }
            
            if (!"Todos".equals(statusFilter)) {
                pstmt.setString(paramIndex, statusFilter);
            }
            
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                Vector<Object> row = new Vector<>();
                row.add(rs.getInt("id"));
                row.add(rs.getString("marca"));
                row.add(rs.getString("modelo"));
                row.add(rs.getInt("ano"));
                row.add(rs.getString("placa"));
                row.add(rs.getString("estado"));
                tableModel.addRow(row);
            }
            updateVehicleCount();
        } catch (SQLException e) {
            showErrorMessage("Error al filtrar vehículos: " + e.getMessage());
        }
    }

    private void addVehicle() {
        if (!validateFields()) return;
        
        String sql = "INSERT INTO vehiculos (marca, modelo, ano, placa, estado) VALUES (?, ?, ?, ?, ?)";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, marcaField.getText().trim());
            pstmt.setString(2, modeloField.getText().trim());
            pstmt.setInt(3, Integer.parseInt(anoField.getText().trim()));
            pstmt.setString(4, placaField.getText().trim().toUpperCase());
            pstmt.setString(5, estadoField.getText().trim());
            
            int result = pstmt.executeUpdate();
            if (result > 0) {
                showSuccessMessage("Vehículo agregado exitosamente!");
                loadVehicles();
                clearFields();
            }
        } catch (SQLException e) {
            showErrorMessage("Error al agregar vehículo: " + e.getMessage());
        } catch (NumberFormatException e) {
            showErrorMessage("El año debe ser un número válido.");
        }
    }

    private void updateVehicle() {
        if (selectedVehicleId == -1) {
            showWarningMessage("Por favor seleccione un vehículo para actualizar.");
            return;
        }
        
        if (!validateFields()) return;
        
        String sql = "UPDATE vehiculos SET marca = ?, modelo = ?, ano = ?, placa = ?, estado = ? WHERE id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, marcaField.getText().trim());
            pstmt.setString(2, modeloField.getText().trim());
            pstmt.setInt(3, Integer.parseInt(anoField.getText().trim()));
            pstmt.setString(4, placaField.getText().trim().toUpperCase());
            pstmt.setString(5, estadoField.getText().trim());
            pstmt.setInt(6, selectedVehicleId);
            
            int result = pstmt.executeUpdate();
            if (result > 0) {
                showSuccessMessage("Vehículo actualizado exitosamente!");
                loadVehicles();
                clearFields();
            }
        } catch (SQLException e) {
            showErrorMessage("Error al actualizar vehículo: " + e.getMessage());
        } catch (NumberFormatException e) {
            showErrorMessage("El año debe ser un número válido.");
        }
    }

    private void deleteVehicle() {
        if (selectedVehicleId == -1) {
            showWarningMessage("Por favor seleccione un vehículo para eliminar.");
            return;
        }
        
        int confirm = JOptionPane.showConfirmDialog(
            this,
            "¿Está seguro que desea eliminar este vehículo?\nEsta acción no se puede deshacer.",
            "Confirmar Eliminación",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.WARNING_MESSAGE
        );
        
        if (confirm == JOptionPane.YES_OPTION) {
            String sql = "DELETE FROM vehiculos WHERE id = ?";
            
            try (Connection conn = DatabaseConnection.getConnection();
                 PreparedStatement pstmt = conn.prepareStatement(sql)) {
                
                pstmt.setInt(1, selectedVehicleId);
                
                int result = pstmt.executeUpdate();
                if (result > 0) {
                    showSuccessMessage("Vehículo eliminado exitosamente!");
                    loadVehicles();
                    clearFields();
                }
            } catch (SQLException e) {
                showErrorMessage("Error al eliminar vehículo: " + e.getMessage());
            }
        }
    }

    private void populateFields(int row) {
        selectedVehicleId = (Integer) tableModel.getValueAt(row, 0);
        marcaField.setText(tableModel.getValueAt(row, 1).toString());
        modeloField.setText(tableModel.getValueAt(row, 2).toString());
        anoField.setText(tableModel.getValueAt(row, 3).toString());
        placaField.setText(tableModel.getValueAt(row, 4).toString());
        estadoField.setText(tableModel.getValueAt(row, 5).toString());
    }

    private void clearFields() {
        selectedVehicleId = -1;
        marcaField.setText("");
        modeloField.setText("");
        anoField.setText("");
        placaField.setText("");
        estadoField.setText("");
        vehicleTable.clearSelection();
    }

    private boolean validateFields() {
        if (marcaField.getText().trim().isEmpty() ||
            modeloField.getText().trim().isEmpty() ||
            anoField.getText().trim().isEmpty() ||
            placaField.getText().trim().isEmpty() ||
            estadoField.getText().trim().isEmpty()) {
            
            showWarningMessage("Por favor complete todos los campos.");
            return false;
        }
        
        try {
            int year = Integer.parseInt(anoField.getText().trim());
            if (year < 1900 || year > 2030) {
                showWarningMessage("El año debe estar entre 1900 y 2030.");
                return false;
            }
        } catch (NumberFormatException e) {
            showWarningMessage("El año debe ser un número válido.");
            return false;
        }
        
        return true;
    }

    private void updateVehicleCount() {
        // Actualizar contador en el header si existe
        SwingUtilities.invokeLater(() -> {
            Component[] components = ((JPanel) backgroundPanel.getComponent(0))
                .getComponent(0) instanceof JPanel ? 
                ((JPanel) ((JPanel) backgroundPanel.getComponent(0)).getComponent(0)).getComponents() : new Component[0];
            
            for (Component comp : components) {
                if (comp instanceof JPanel) {
                    JPanel panel = (JPanel) comp;
                    for (Component subComp : panel.getComponents()) {
                        if (subComp instanceof JLabel && ((JLabel) subComp).getText().startsWith("Total:")) {
                            ((JLabel) subComp).setText("Total: " + tableModel.getRowCount());
                            break;
                        }
                    }
                }
            }
        });
    }

    // Métodos de utilidad para mensajes
    private void showSuccessMessage(String message) {
        JOptionPane.showMessageDialog(this, message, "Éxito", JOptionPane.INFORMATION_MESSAGE);
    }

    private void showErrorMessage(String message) {
        JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
    }

    private void showWarningMessage(String message) {
        JOptionPane.showMessageDialog(this, message, "Advertencia", JOptionPane.WARNING_MESSAGE);
    }

    // Renderer personalizado para el estado
    class StatusCellRenderer extends JLabel implements TableCellRenderer {
        public StatusCellRenderer() {
            setOpaque(true);
            setHorizontalAlignment(CENTER);
            setFont(new Font("Segoe UI", Font.BOLD, 11));
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, 
                boolean isSelected, boolean hasFocus, int row, int column) {
            
            String status = value.toString();
            setText(status);
            
            if (isSelected) {
                setBackground(table.getSelectionBackground());
                setForeground(table.getSelectionForeground());
            } else {
                setForeground(Color.WHITE);
                switch (status.toLowerCase()) {
                    case "disponible":
                        setBackground(new Color(46, 204, 113));
                        break;
                    case "en uso":
                        setBackground(new Color(52, 152, 219));
                        break;
                    case "mantenimiento":
                        setBackground(new Color(243, 156, 18));
                        break;
                    case "fuera de servicio":
                        setBackground(new Color(231, 76, 60));
                        break;
                    default:
                        setBackground(new Color(149, 165, 166));
                        break;
                }
            }
            return this;
        }
    }
}
