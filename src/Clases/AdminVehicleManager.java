package Clases;

import java.awt.*;
import java.sql.*;
import java.util.Vector;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;

/**
 * Clase para gestión completa de vehículos - Administrador
 * Funcionalidades: CRUD completo, asignaciones, mantenimiento
 */
public class AdminVehicleManager extends JFrame {
    private JTable vehicleTable;
    private DefaultTableModel tableModel;
    private JTextField marcaField, modeloField, anoField, placaField;
    private JComboBox<String> estadoCombo, tipoCombo;
    private JTextArea notasArea;
    private BackgroundPanel backgroundPanel;
    private int selectedVehicleId = -1;
    
    // Estados posibles de vehículos
    private final String[] ESTADOS = {"Disponible", "En uso", "Mantenimiento", "Fuera de servicio"};
    private final String[] TIPOS = {"Camioneta", "Furgoneta", "Automóvil", "Motocicleta", "Camión"};

    public AdminVehicleManager() {
        initializeComponents();
        setupGUI();
        createTableIfNotExists();
        loadVehicles();
        setupEventHandlers();
    }

    private void initializeComponents() {
        setTitle("🚗 Gestión Completa de Vehículos - Administrador");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(1400, 900);
        setLocationRelativeTo(null);

        // Panel de fondo
        backgroundPanel = new BackgroundPanel("/Imagenes/FondoLogin.jpg");
        backgroundPanel.setLayout(new BorderLayout());
        setContentPane(backgroundPanel);

        // Modelo de tabla con más columnas
        String[] columnNames = {"ID", "Marca", "Modelo", "Año", "Placa", "Tipo", "Estado", "Técnico Asignado", "Acciones"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 8; // Solo la columna de acciones
            }
        };
        vehicleTable = new JTable(tableModel);
        
        // Inicializar campos
        marcaField = new JTextField(20);
        modeloField = new JTextField(20);
        anoField = new JTextField(20);
        placaField = new JTextField(20);
        estadoCombo = new JComboBox<>(ESTADOS);
        tipoCombo = new JComboBox<>(TIPOS);
        notasArea = new JTextArea(3, 20);
    }

    private void setupGUI() {
        JPanel mainPanel = new JPanel(new BorderLayout(20, 20));
        mainPanel.setOpaque(false);
        mainPanel.setBorder(new EmptyBorder(30, 30, 30, 30));

        // Header
        JPanel headerPanel = createHeaderPanel();
        mainPanel.add(headerPanel, BorderLayout.NORTH);

        // Content - Dividido en tabla y formulario
        JSplitPane contentSplit = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        contentSplit.setOpaque(false);
        contentSplit.setDividerLocation(900);
        contentSplit.setResizeWeight(0.65);

        // Panel izquierdo - Tabla y controles
        JPanel tablePanel = createTablePanel();
        contentSplit.setLeftComponent(tablePanel);

        // Panel derecho - Formulario
        JPanel formPanel = createFormPanel();
        contentSplit.setRightComponent(formPanel);

        mainPanel.add(contentSplit, BorderLayout.CENTER);

        // Footer con estadísticas
        JPanel footerPanel = createFooterPanel();
        mainPanel.add(footerPanel, BorderLayout.SOUTH);

        backgroundPanel.add(mainPanel);
    }

    private JPanel createHeaderPanel() {
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setOpaque(false);

        JLabel titleLabel = new JLabel("🚗 Sistema de Gestión Vehicular", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 32));
        titleLabel.setForeground(Color.WHITE);

        JLabel subtitleLabel = new JLabel("Administración completa de flota empresarial", SwingConstants.CENTER);
        subtitleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        subtitleLabel.setForeground(new Color(255, 255, 255, 180));

        JPanel titlePanel = new JPanel();
        titlePanel.setOpaque(false);
        titlePanel.setLayout(new BoxLayout(titlePanel, BoxLayout.Y_AXIS));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        subtitleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        titlePanel.add(titleLabel);
        titlePanel.add(Box.createVerticalStrut(10));
        titlePanel.add(subtitleLabel);

        headerPanel.add(titlePanel, BorderLayout.CENTER);
        return headerPanel;
    }

    private JPanel createTablePanel() {
        JPanel tablePanel = new JPanel(new BorderLayout(10, 10));
        tablePanel.setOpaque(false);
        tablePanel.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(Color.WHITE, 2),
            "Lista de Vehículos de la Flota",
            0, 0,
            new Font("Segoe UI", Font.BOLD, 16),
            Color.WHITE
        ));

        // Configurar tabla
        setupTable();
        JScrollPane scrollPane = new JScrollPane(vehicleTable);
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);
        scrollPane.setBorder(BorderFactory.createLineBorder(Color.WHITE));

        // Panel de filtros y búsqueda
        JPanel filterPanel = createFilterPanel();
        tablePanel.add(filterPanel, BorderLayout.NORTH);
        tablePanel.add(scrollPane, BorderLayout.CENTER);

        // Panel de acciones rápidas
        JPanel quickActionsPanel = createQuickActionsPanel();
        tablePanel.add(quickActionsPanel, BorderLayout.SOUTH);

        return tablePanel;
    }

    private JPanel createFilterPanel() {
        JPanel filterPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        filterPanel.setOpaque(false);

        JLabel searchLabel = new JLabel("🔍 Buscar:");
        searchLabel.setForeground(Color.WHITE);
        searchLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));

        JTextField searchField = new JTextField(15);
        searchField.setFont(new Font("Segoe UI", Font.PLAIN, 12));

        JLabel filterLabel = new JLabel("Estado:");
        filterLabel.setForeground(Color.WHITE);
        filterLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));

        JComboBox<String> statusFilter = new JComboBox<>();
        statusFilter.addItem("Todos");
        for (String estado : ESTADOS) {
            statusFilter.addItem(estado);
        }

        JButton refreshButton = createStyledButton("🔄 Actualizar", new Color(155, 89, 182), 120, 35);
        refreshButton.addActionListener(e -> loadVehicles());

        // Funcionalidad de búsqueda en tiempo real
        searchField.addCaretListener(e -> filterVehicles(searchField.getText(), (String) statusFilter.getSelectedItem()));
        statusFilter.addActionListener(e -> filterVehicles(searchField.getText(), (String) statusFilter.getSelectedItem()));

        filterPanel.add(searchLabel);
        filterPanel.add(searchField);
        filterPanel.add(Box.createHorizontalStrut(15));
        filterPanel.add(filterLabel);
        filterPanel.add(statusFilter);
        filterPanel.add(Box.createHorizontalStrut(15));
        filterPanel.add(refreshButton);

        return filterPanel;
    }

    private JPanel createQuickActionsPanel() {
        JPanel actionsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        actionsPanel.setOpaque(false);
        actionsPanel.setBorder(new EmptyBorder(10, 0, 0, 0));

        JButton assignButton = createStyledButton("👤 Asignar Técnico", new Color(52, 152, 219), 150, 40);
        JButton maintenanceButton = createStyledButton("🔧 Programar Mantenimiento", new Color(243, 156, 18), 180, 40);
        JButton reportButton = createStyledButton("📊 Generar Reporte", new Color(46, 204, 113), 150, 40);

        assignButton.addActionListener(e -> assignTechnician());
        maintenanceButton.addActionListener(e -> scheduleMaintenance());
        reportButton.addActionListener(e -> generateReport());

        actionsPanel.add(assignButton);
        actionsPanel.add(Box.createHorizontalStrut(10));
        actionsPanel.add(maintenanceButton);
        actionsPanel.add(Box.createHorizontalStrut(10));
        actionsPanel.add(reportButton);

        return actionsPanel;
    }

    private void setupTable() {
        vehicleTable.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        vehicleTable.setRowHeight(40);
        vehicleTable.setSelectionBackground(new Color(155, 89, 182, 150));
        vehicleTable.setSelectionForeground(Color.WHITE);
        vehicleTable.setGridColor(new Color(200, 200, 200));
        vehicleTable.setShowGrid(true);
        vehicleTable.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));
        vehicleTable.getTableHeader().setBackground(new Color(155, 89, 182));
        vehicleTable.getTableHeader().setForeground(Color.WHITE);
        vehicleTable.getTableHeader().setPreferredSize(new Dimension(0, 35));

        // Configurar ancho de columnas
        int[] widths = {50, 100, 120, 60, 100, 100, 120, 150, 100};
        for (int i = 0; i < widths.length && i < vehicleTable.getColumnCount(); i++) {
            vehicleTable.getColumnModel().getColumn(i).setPreferredWidth(widths[i]);
        }

        // Renderer para estado
        vehicleTable.getColumn("Estado").setCellRenderer(new StatusCellRenderer());
        
        // Renderer para acciones
        vehicleTable.getColumn("Acciones").setCellRenderer(new ActionButtonRenderer());
        vehicleTable.getColumn("Acciones").setCellEditor(new ActionButtonEditor());
    }

    private JPanel createFormPanel() {
        JPanel formPanel = new JPanel(new BorderLayout());
        formPanel.setOpaque(false);
        formPanel.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(Color.WHITE, 2),
            "Gestión de Vehículo",
            0, 0,
            new Font("Segoe UI", Font.BOLD, 16),
            Color.WHITE
        ));

        // Panel de campos
        JPanel fieldsPanel = new JPanel(new GridBagLayout());
        fieldsPanel.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.anchor = GridBagConstraints.WEST;

        // Campos del formulario
        addFormField(fieldsPanel, "🏷️ Marca:", marcaField, gbc, 0);
        addFormField(fieldsPanel, "🚙 Modelo:", modeloField, gbc, 1);
        addFormField(fieldsPanel, "📅 Año:", anoField, gbc, 2);
        addFormField(fieldsPanel, "🔢 Placa:", placaField, gbc, 3);
        addFormField(fieldsPanel, "🚗 Tipo:", tipoCombo, gbc, 4);
        addFormField(fieldsPanel, "📊 Estado:", estadoCombo, gbc, 5);

        // Área de notas
        gbc.gridx = 0; gbc.gridy = 6;
        JLabel notasLabel = new JLabel("📝 Notas:");
        notasLabel.setForeground(Color.WHITE);
        notasLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        fieldsPanel.add(notasLabel, gbc);

        gbc.gridx = 0; gbc.gridy = 7; gbc.gridwidth = 2; gbc.fill = GridBagConstraints.BOTH; gbc.weightx = 1.0; gbc.weighty = 1.0;
        notasArea.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        notasArea.setLineWrap(true);
        notasArea.setWrapStyleWord(true);
        notasArea.setBorder(new EmptyBorder(8, 8, 8, 8));
        JScrollPane notasScroll = new JScrollPane(notasArea);
        notasScroll.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        fieldsPanel.add(notasScroll, gbc);

        // Panel de botones
        JPanel buttonPanel = createButtonPanel();

        formPanel.add(fieldsPanel, BorderLayout.CENTER);
        formPanel.add(buttonPanel, BorderLayout.SOUTH);

        return formPanel;
    }

    private void addFormField(JPanel parent, String labelText, JComponent field, GridBagConstraints gbc, int row) {
        gbc.gridx = 0; gbc.gridy = row; gbc.gridwidth = 1; gbc.fill = GridBagConstraints.NONE; gbc.weightx = 0; gbc.weighty = 0;
        JLabel label = new JLabel(labelText);
        label.setForeground(Color.WHITE);
        label.setFont(new Font("Segoe UI", Font.BOLD, 14));
        parent.add(label, gbc);

        gbc.gridx = 1; gbc.gridy = row; gbc.fill = GridBagConstraints.HORIZONTAL; gbc.weightx = 1.0;
        field.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        if (field instanceof JTextField) {
            field.setPreferredSize(new Dimension(200, 35));
            ((JTextField) field).setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.GRAY),
                new EmptyBorder(8, 12, 8, 12)
            ));
        } else if (field instanceof JComboBox) {
            field.setPreferredSize(new Dimension(200, 35));
        }
        parent.add(field, gbc);
    }

    private JPanel createButtonPanel() {
        JPanel buttonPanel = new JPanel(new GridLayout(2, 2, 10, 10));
        buttonPanel.setOpaque(false);
        buttonPanel.setBorder(new EmptyBorder(20, 10, 10, 10));

        JButton addButton = createStyledButton("➕ Agregar", new Color(155, 89, 182), 120, 40);
        JButton updateButton = createStyledButton("✏️ Actualizar", new Color(142, 68, 173), 120, 40);
        JButton deleteButton = createStyledButton("🗑️ Eliminar", new Color(231, 76, 60), 120, 40);
        JButton clearButton = createStyledButton("🧹 Limpiar", new Color(149, 165, 166), 120, 40);

        addButton.addActionListener(e -> addVehicle());
        updateButton.addActionListener(e -> updateVehicle());
        deleteButton.addActionListener(e -> deleteVehicle());
        clearButton.addActionListener(e -> clearFields());

        buttonPanel.add(addButton);
        buttonPanel.add(updateButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(clearButton);

        return buttonPanel;
    }

    private JPanel createFooterPanel() {
        JPanel footerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        footerPanel.setOpaque(false);

        // Estadísticas rápidas
        JLabel statsLabel = new JLabel();
        statsLabel.setForeground(Color.WHITE);
        statsLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        updateStatsLabel(statsLabel);

        JButton closeButton = createStyledButton("🚪 Cerrar", new Color(108, 117, 125), 120, 35);
        closeButton.addActionListener(e -> dispose());

        footerPanel.add(statsLabel);
        footerPanel.add(Box.createHorizontalStrut(30));
        footerPanel.add(closeButton);

        return footerPanel;
    }

    private JButton createStyledButton(String text, Color color, int width, int height) {
        JButton button = new JButton(text);
        button.setPreferredSize(new Dimension(width, height));
        button.setFont(new Font("Segoe UI", Font.BOLD, 12));
        button.setForeground(Color.WHITE);
        button.setBackground(color);
        button.setBorder(new EmptyBorder(8, 15, 8, 15));
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));

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

    // Métodos de base de datos
    private void createTableIfNotExists() {
        String sql = """
            CREATE TABLE IF NOT EXISTS vehiculos_flota (
                id INT AUTO_INCREMENT PRIMARY KEY,
                marca VARCHAR(50) NOT NULL,
                modelo VARCHAR(50) NOT NULL,
                ano INT NOT NULL,
                placa VARCHAR(20) UNIQUE NOT NULL,
                tipo VARCHAR(30) NOT NULL,
                estado VARCHAR(30) DEFAULT 'Disponible',
                tecnico_asignado VARCHAR(100),
                notas TEXT,
                fecha_registro TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                fecha_mantenimiento DATE,
                kilometraje INT DEFAULT 0
            )
        """;
        
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
        } catch (SQLException e) {
            showErrorMessage("Error al crear tabla de vehículos: " + e.getMessage());
        }
    }

    private void loadVehicles() {
        tableModel.setRowCount(0);
        String sql = "SELECT id, marca, modelo, ano, placa, tipo, estado, tecnico_asignado FROM vehiculos_flota ORDER BY marca, modelo";
        
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
                row.add(rs.getString("tipo"));
                row.add(rs.getString("estado"));
                row.add(rs.getString("tecnico_asignado") != null ? rs.getString("tecnico_asignado") : "Sin asignar");
                row.add("Gestionar");
                tableModel.addRow(row);
            }
        } catch (SQLException e) {
            showErrorMessage("Error al cargar vehículos: " + e.getMessage());
        }
    }

    private void filterVehicles(String searchText, String statusFilter) {
        tableModel.setRowCount(0);
        StringBuilder sql = new StringBuilder("SELECT id, marca, modelo, ano, placa, tipo, estado, tecnico_asignado FROM vehiculos_flota WHERE 1=1");
        
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
                row.add(rs.getString("tipo"));
                row.add(rs.getString("estado"));
                row.add(rs.getString("tecnico_asignado") != null ? rs.getString("tecnico_asignado") : "Sin asignar");
                row.add("Gestionar");
                tableModel.addRow(row);
            }
        } catch (SQLException e) {
            showErrorMessage("Error al filtrar vehículos: " + e.getMessage());
        }
    }

    private void addVehicle() {
        if (!validateFields()) return;
        
        String sql = "INSERT INTO vehiculos_flota (marca, modelo, ano, placa, tipo, estado, notas) VALUES (?, ?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, marcaField.getText().trim());
            pstmt.setString(2, modeloField.getText().trim());
            pstmt.setInt(3, Integer.parseInt(anoField.getText().trim()));
            pstmt.setString(4, placaField.getText().trim().toUpperCase());
            pstmt.setString(5, (String) tipoCombo.getSelectedItem());
            pstmt.setString(6, (String) estadoCombo.getSelectedItem());
            pstmt.setString(7, notasArea.getText().trim());
            
            int result = pstmt.executeUpdate();
            if (result > 0) {
                showSuccessMessage("✅ Vehículo agregado exitosamente!");
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
        
        String sql = "UPDATE vehiculos_flota SET marca = ?, modelo = ?, ano = ?, placa = ?, tipo = ?, estado = ?, notas = ? WHERE id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, marcaField.getText().trim());
            pstmt.setString(2, modeloField.getText().trim());
            pstmt.setInt(3, Integer.parseInt(anoField.getText().trim()));
            pstmt.setString(4, placaField.getText().trim().toUpperCase());
            pstmt.setString(5, (String) tipoCombo.getSelectedItem());
            pstmt.setString(6, (String) estadoCombo.getSelectedItem());
            pstmt.setString(7, notasArea.getText().trim());
            pstmt.setInt(8, selectedVehicleId);
            
            int result = pstmt.executeUpdate();
            if (result > 0) {
                showSuccessMessage("✅ Vehículo actualizado exitosamente!");
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
            String sql = "DELETE FROM vehiculos_flota WHERE id = ?";
            
            try (Connection conn = DatabaseConnection.getConnection();
                 PreparedStatement pstmt = conn.prepareStatement(sql)) {
                
                pstmt.setInt(1, selectedVehicleId);
                
                int result = pstmt.executeUpdate();
                if (result > 0) {
                    showSuccessMessage("✅ Vehículo eliminado exitosamente!");
                    loadVehicles();
                    clearFields();
                }
            } catch (SQLException e) {
                showErrorMessage("Error al eliminar vehículo: " + e.getMessage());
            }
        }
    }

    private void setupEventHandlers() {
        vehicleTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int selectedRow = vehicleTable.getSelectedRow();
                if (selectedRow != -1) {
                    populateFields(selectedRow);
                }
            }
        });
    }

    private void populateFields(int row) {
        selectedVehicleId = (Integer) tableModel.getValueAt(row, 0);
        marcaField.setText(tableModel.getValueAt(row, 1).toString());
        modeloField.setText(tableModel.getValueAt(row, 2).toString());
        anoField.setText(tableModel.getValueAt(row, 3).toString());
        placaField.setText(tableModel.getValueAt(row, 4).toString());
        tipoCombo.setSelectedItem(tableModel.getValueAt(row, 5).toString());
        estadoCombo.setSelectedItem(tableModel.getValueAt(row, 6).toString());
        
        // Cargar notas si existen
        loadVehicleNotes(selectedVehicleId);
    }

    private void loadVehicleNotes(int vehicleId) {
        String sql = "SELECT notas FROM vehiculos_flota WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, vehicleId);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                String notas = rs.getString("notas");
                notasArea.setText(notas != null ? notas : "");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void clearFields() {
        selectedVehicleId = -1;
        marcaField.setText("");
        modeloField.setText("");
        anoField.setText("");
        placaField.setText("");
        tipoCombo.setSelectedIndex(0);
        estadoCombo.setSelectedIndex(0);
        notasArea.setText("");
        vehicleTable.clearSelection();
    }

    private boolean validateFields() {
        if (marcaField.getText().trim().isEmpty() ||
            modeloField.getText().trim().isEmpty() ||
            anoField.getText().trim().isEmpty() ||
            placaField.getText().trim().isEmpty()) {
            
            showWarningMessage("Por favor complete todos los campos obligatorios.");
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

    // Métodos de acciones especiales
    private void assignTechnician() {
        int selectedRow = vehicleTable.getSelectedRow();
        if (selectedRow == -1) {
            showWarningMessage("Por favor seleccione un vehículo primero.");
            return;
        }
        
        String technician = JOptionPane.showInputDialog(this, 
            "Ingrese el nombre del técnico a asignar:", 
            "Asignar Técnico", 
            JOptionPane.QUESTION_MESSAGE);
        
        if (technician != null && !technician.trim().isEmpty()) {
            int vehicleId = (Integer) tableModel.getValueAt(selectedRow, 0);
            updateTechnicianAssignment(vehicleId, technician.trim());
        }
    }

    private void updateTechnicianAssignment(int vehicleId, String technician) {
        String sql = "UPDATE vehiculos_flota SET tecnico_asignado = ?, estado = 'En uso' WHERE id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, technician);
            pstmt.setInt(2, vehicleId);
            
            int result = pstmt.executeUpdate();
            if (result > 0) {
                showSuccessMessage("✅ Técnico asignado exitosamente!");
                loadVehicles();
            }
        } catch (SQLException e) {
            showErrorMessage("Error al asignar técnico: " + e.getMessage());
        }
    }

    private void scheduleMaintenance() {
        int selectedRow = vehicleTable.getSelectedRow();
        if (selectedRow == -1) {
            showWarningMessage("Por favor seleccione un vehículo primero.");
            return;
        }
        
        int vehicleId = (Integer) tableModel.getValueAt(selectedRow, 0);
        String sql = "UPDATE vehiculos_flota SET estado = 'Mantenimiento', fecha_mantenimiento = CURDATE() WHERE id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, vehicleId);
            
            int result = pstmt.executeUpdate();
            if (result > 0) {
                showSuccessMessage("✅ Vehículo programado para mantenimiento!");
                loadVehicles();
            }
        } catch (SQLException e) {
            showErrorMessage("Error al programar mantenimiento: " + e.getMessage());
        }
    }

    private void generateReport() {
        StringBuilder report = new StringBuilder();
        report.append("=== REPORTE DE FLOTA VEHICULAR ===\n\n");
        
        // Contar por estados
        try (Connection conn = DatabaseConnection.getConnection()) {
            String sql = "SELECT estado, COUNT(*) as cantidad FROM vehiculos_flota GROUP BY estado";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            ResultSet rs = pstmt.executeQuery();
            
            report.append("RESUMEN POR ESTADOS:\n");
            while (rs.next()) {
                report.append("- ").append(rs.getString("estado")).append(": ")
                      .append(rs.getInt("cantidad")).append(" vehículos\n");
            }
            
            report.append("\nTOTAL DE VEHÍCULOS: ").append(tableModel.getRowCount());
            
            JTextArea reportArea = new JTextArea(15, 40);
            reportArea.setText(report.toString());
            reportArea.setEditable(false);
            reportArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
            
            JScrollPane scrollPane = new JScrollPane(reportArea);
            JOptionPane.showMessageDialog(this, scrollPane, "Reporte de Flota", JOptionPane.INFORMATION_MESSAGE);
            
        } catch (SQLException e) {
            showErrorMessage("Error al generar reporte: " + e.getMessage());
        }
    }

    private void updateStatsLabel(JLabel statsLabel) {
        statsLabel.setText("📊 Total de vehículos: " + tableModel.getRowCount());
    }

    // Métodos de utilidad
    private void showSuccessMessage(String message) {
        JOptionPane.showMessageDialog(this, message, "Éxito", JOptionPane.INFORMATION_MESSAGE);
    }

    private void showErrorMessage(String message) {
        JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
    }

    private void showWarningMessage(String message) {
        JOptionPane.showMessageDialog(this, message, "Advertencia", JOptionPane.WARNING_MESSAGE);
    }

    // Renderer para estado
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

    // Renderer para botones de acción
    class ActionButtonRenderer extends JButton implements TableCellRenderer {
        public ActionButtonRenderer() {
            setOpaque(true);
            setFont(new Font("Segoe UI", Font.BOLD, 10));
            setBackground(new Color(155, 89, 182));
            setForeground(Color.WHITE);
            setBorder(new EmptyBorder(5, 10, 5, 10));
        }

        public Component getTableCellRendererComponent(JTable table, Object value,
                boolean isSelected, boolean hasFocus, int row, int column) {
            setText(value == null ? "Gestionar" : value.toString());
            return this;
        }
    }

    // Editor para botones de acción
    class ActionButtonEditor extends DefaultCellEditor {
        protected JButton button;
        private String label;
        private boolean isPushed;
        private int selectedRow;

        public ActionButtonEditor() {
            super(new JCheckBox());
            button = new JButton();
            button.setOpaque(true);
            button.setFont(new Font("Segoe UI", Font.BOLD, 10));
            button.setBackground(new Color(155, 89, 182));
            button.setForeground(Color.WHITE);
            button.setBorder(new EmptyBorder(5, 10, 5, 10));
            button.addActionListener(e -> fireEditingStopped());
        }

        public Component getTableCellEditorComponent(JTable table, Object value,
                boolean isSelected, int row, int column) {
            label = value == null ? "Gestionar" : value.toString();
            button.setText(label);
            isPushed = true;
            selectedRow = row;
            return button;
        }

        public Object getCellEditorValue() {
            if (isPushed) {
                vehicleTable.setRowSelectionInterval(selectedRow, selectedRow);
                populateFields(selectedRow);
            }
            isPushed = false;
            return label;
        }

        public boolean stopCellEditing() {
            isPushed = false;
            return super.stopCellEditing();
        }
    }
}
