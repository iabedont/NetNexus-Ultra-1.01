package Clases;

import java.awt.*;
import java.sql.*;
import java.util.Vector;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;

/**
 * Clase para gestión de vehículos - Técnico
 * Funcionalidades: Ver vehículos asignados, solicitar vehículos, reportar problemas
 */
public class TechnicianVehicleManager extends JFrame {
    private JTable vehicleTable, requestTable;
    private DefaultTableModel vehicleTableModel, requestTableModel;
    private JTextField technicianNameField;
    private JComboBox<String> vehicleTypeCombo, priorityCombo;
    private JTextArea reasonArea, problemArea;
    private BackgroundPanel backgroundPanel;
    private String currentTechnician;
    
    // Tipos de vehículos disponibles
    private final String[] VEHICLE_TYPES = {"Cualquiera", "Camioneta", "Furgoneta", "Automóvil", "Motocicleta", "Camión"};
    private final String[] PRIORITIES = {"Baja", "Media", "Alta", "Urgente"};

    public TechnicianVehicleManager(String technicianName) {
        this.currentTechnician = technicianName != null ? technicianName : "Técnico";
        initializeComponents();
        setupGUI();
        createTablesIfNotExist();
        loadData();
        setupEventHandlers();
    }

    private void initializeComponents() {
        setTitle("🚗 Gestión de Vehículos - " + currentTechnician);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(1300, 800);
        setLocationRelativeTo(null);

        // Panel de fondo
        backgroundPanel = new BackgroundPanel("/Imagenes/FondoLogin.jpg");
        backgroundPanel.setLayout(new BorderLayout());
        setContentPane(backgroundPanel);

        // Modelo de tabla para vehículos asignados
        String[] vehicleColumns = {"ID", "Marca", "Modelo", "Año", "Placa", "Tipo", "Estado", "Fecha Asignación", "Reportar"};
        vehicleTableModel = new DefaultTableModel(vehicleColumns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 8; // Solo la columna de reportar
            }
        };
        vehicleTable = new JTable(vehicleTableModel);

        // Modelo de tabla para solicitudes
        String[] requestColumns = {"ID", "Tipo Vehículo", "Prioridad", "Motivo", "Estado", "Fecha Solicitud", "Cancelar"};
        requestTableModel = new DefaultTableModel(requestColumns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 6; // Solo la columna de cancelar
            }
        };
        requestTable = new JTable(requestTableModel);
        
        // Inicializar campos
        technicianNameField = new JTextField(currentTechnician);
        technicianNameField.setEditable(false);
        vehicleTypeCombo = new JComboBox<>(VEHICLE_TYPES);
        priorityCombo = new JComboBox<>(PRIORITIES);
        reasonArea = new JTextArea(3, 30);
        problemArea = new JTextArea(3, 30);
    }

    private void setupGUI() {
        JPanel mainPanel = new JPanel(new BorderLayout(20, 20));
        mainPanel.setOpaque(false);
        mainPanel.setBorder(new EmptyBorder(30, 30, 30, 30));

        // Header
        JPanel headerPanel = createHeaderPanel();
        mainPanel.add(headerPanel, BorderLayout.NORTH);

        // Content con pestañas
        JTabbedPane tabbedPane = createTabbedPane();
        mainPanel.add(tabbedPane, BorderLayout.CENTER);

        // Footer
        JPanel footerPanel = createFooterPanel();
        mainPanel.add(footerPanel, BorderLayout.SOUTH);

        backgroundPanel.add(mainPanel);
    }

    private JPanel createHeaderPanel() {
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setOpaque(false);

        JLabel titleLabel = new JLabel("🚗 Sistema de Vehículos - Técnico", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 28));
        titleLabel.setForeground(Color.WHITE);

        JLabel welcomeLabel = new JLabel("Bienvenido, " + currentTechnician, SwingConstants.CENTER);
        welcomeLabel.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        welcomeLabel.setForeground(new Color(255, 255, 255, 180));

        JPanel titlePanel = new JPanel();
        titlePanel.setOpaque(false);
        titlePanel.setLayout(new BoxLayout(titlePanel, BoxLayout.Y_AXIS));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        welcomeLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        titlePanel.add(titleLabel);
        titlePanel.add(Box.createVerticalStrut(10));
        titlePanel.add(welcomeLabel);

        headerPanel.add(titlePanel, BorderLayout.CENTER);
        return headerPanel;
    }

    private JTabbedPane createTabbedPane() {
        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.setFont(new Font("Segoe UI", Font.BOLD, 14));
        tabbedPane.setBackground(new Color(155, 89, 182, 100));
        tabbedPane.setForeground(Color.WHITE);

        // Pestaña 1: Vehículos Asignados
        JPanel assignedPanel = createAssignedVehiclesPanel();
        tabbedPane.addTab("🚙 Mis Vehículos", assignedPanel);

        // Pestaña 2: Solicitar Vehículo
        JPanel requestPanel = createVehicleRequestPanel();
        tabbedPane.addTab("📝 Solicitar Vehículo", requestPanel);

        // Pestaña 3: Mis Solicitudes
        JPanel myRequestsPanel = createMyRequestsPanel();
        tabbedPane.addTab("📋 Mis Solicitudes", myRequestsPanel);

        return tabbedPane;
    }

    private JPanel createAssignedVehiclesPanel() {
        JPanel panel = new JPanel(new BorderLayout(15, 15));
        panel.setOpaque(false);
        panel.setBorder(new EmptyBorder(20, 20, 20, 20));

        // Título
        JLabel titleLabel = new JLabel("🚙 Vehículos Asignados a Mi Nombre", SwingConstants.LEFT);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        titleLabel.setForeground(Color.WHITE);

        // Configurar tabla de vehículos
        setupVehicleTable();
        JScrollPane vehicleScrollPane = new JScrollPane(vehicleTable);
        vehicleScrollPane.setOpaque(false);
        vehicleScrollPane.getViewport().setOpaque(false);
        vehicleScrollPane.setBorder(BorderFactory.createLineBorder(Color.WHITE));
        vehicleScrollPane.setPreferredSize(new Dimension(800, 300));

        // Panel de acciones
        JPanel actionsPanel = createVehicleActionsPanel();

        panel.add(titleLabel, BorderLayout.NORTH);
        panel.add(vehicleScrollPane, BorderLayout.CENTER);
        panel.add(actionsPanel, BorderLayout.SOUTH);

        return panel;
    }

    private JPanel createVehicleActionsPanel() {
        JPanel actionsPanel = new JPanel(new BorderLayout(10, 10));
        actionsPanel.setOpaque(false);
        actionsPanel.setBorder(new EmptyBorder(15, 0, 0, 0));

        // Área para reportar problemas
        JPanel problemPanel = new JPanel(new BorderLayout(10, 10));
        problemPanel.setOpaque(false);
        problemPanel.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(Color.WHITE),
            "🔧 Reportar Problema del Vehículo",
            0, 0,
            new Font("Segoe UI", Font.BOLD, 14),
            Color.WHITE
        ));

        JLabel problemLabel = new JLabel("Descripción del problema:");
        problemLabel.setForeground(Color.WHITE);
        problemLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));

        problemArea.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        problemArea.setLineWrap(true);
        problemArea.setWrapStyleWord(true);
        problemArea.setBorder(new EmptyBorder(8, 8, 8, 8));
        JScrollPane problemScroll = new JScrollPane(problemArea);
        problemScroll.setPreferredSize(new Dimension(400, 80));

        JButton reportButton = createStyledButton("📢 Reportar Problema", new Color(231, 76, 60), 160, 35);
        reportButton.addActionListener(e -> reportProblem());

        JButton refreshButton = createStyledButton("🔄 Actualizar Lista", new Color(155, 89, 182), 140, 35);
        refreshButton.addActionListener(e -> loadAssignedVehicles());

        JPanel problemInputPanel = new JPanel(new BorderLayout(10, 5));
        problemInputPanel.setOpaque(false);
        problemInputPanel.add(problemLabel, BorderLayout.NORTH);
        problemInputPanel.add(problemScroll, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.setOpaque(false);
        buttonPanel.add(refreshButton);
        buttonPanel.add(reportButton);

        problemPanel.add(problemInputPanel, BorderLayout.CENTER);
        problemPanel.add(buttonPanel, BorderLayout.SOUTH);

        actionsPanel.add(problemPanel, BorderLayout.CENTER);

        return actionsPanel;
    }

    private JPanel createVehicleRequestPanel() {
        JPanel panel = new JPanel(new BorderLayout(15, 15));
        panel.setOpaque(false);
        panel.setBorder(new EmptyBorder(30, 50, 30, 50));

        // Título
        JLabel titleLabel = new JLabel("📝 Solicitar Asignación de Vehículo", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 22));
        titleLabel.setForeground(Color.WHITE);

        // Formulario
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(15, 15, 15, 15);
        gbc.anchor = GridBagConstraints.WEST;

        // Técnico (solo lectura)
        addFormField(formPanel, "👤 Técnico:", technicianNameField, gbc, 0);

        // Tipo de vehículo
        addFormField(formPanel, "🚗 Tipo de Vehículo:", vehicleTypeCombo, gbc, 1);

        // Prioridad
        addFormField(formPanel, "⚡ Prioridad:", priorityCombo, gbc, 2);

        // Motivo
        gbc.gridx = 0; gbc.gridy = 3;
        JLabel reasonLabel = new JLabel("📋 Motivo de la solicitud:");
        reasonLabel.setForeground(Color.WHITE);
        reasonLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        formPanel.add(reasonLabel, gbc);

        gbc.gridx = 0; gbc.gridy = 4; gbc.gridwidth = 2; gbc.fill = GridBagConstraints.BOTH; gbc.weightx = 1.0; gbc.weighty = 1.0;
        reasonArea.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        reasonArea.setLineWrap(true);
        reasonArea.setWrapStyleWord(true);
        reasonArea.setBorder(new EmptyBorder(10, 10, 10, 10));
        JScrollPane reasonScroll = new JScrollPane(reasonArea);
        reasonScroll.setPreferredSize(new Dimension(400, 120));
        reasonScroll.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        formPanel.add(reasonScroll, gbc);

        // Botones
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 20));
        buttonPanel.setOpaque(false);

        JButton submitButton = createStyledButton("📤 Enviar Solicitud", new Color(155, 89, 182), 160, 45);
        JButton clearButton = createStyledButton("🧹 Limpiar", new Color(149, 165, 166), 120, 45);

        submitButton.addActionListener(e -> submitVehicleRequest());
        clearButton.addActionListener(e -> clearRequestForm());

        buttonPanel.add(submitButton);
        buttonPanel.add(clearButton);

        panel.add(titleLabel, BorderLayout.NORTH);
        panel.add(formPanel, BorderLayout.CENTER);
        panel.add(buttonPanel, BorderLayout.SOUTH);

        return panel;
    }

    private JPanel createMyRequestsPanel() {
        JPanel panel = new JPanel(new BorderLayout(15, 15));
        panel.setOpaque(false);
        panel.setBorder(new EmptyBorder(20, 20, 20, 20));

        // Título
        JLabel titleLabel = new JLabel("📋 Mis Solicitudes de Vehículos", SwingConstants.LEFT);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        titleLabel.setForeground(Color.WHITE);

        // Configurar tabla de solicitudes
        setupRequestTable();
        JScrollPane requestScrollPane = new JScrollPane(requestTable);
        requestScrollPane.setOpaque(false);
        requestScrollPane.getViewport().setOpaque(false);
        requestScrollPane.setBorder(BorderFactory.createLineBorder(Color.WHITE));

        // Panel de acciones
        JPanel actionsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        actionsPanel.setOpaque(false);
        actionsPanel.setBorder(new EmptyBorder(15, 0, 0, 0));

        JButton refreshRequestsButton = createStyledButton("🔄 Actualizar Solicitudes", new Color(155, 89, 182), 180, 35);
        refreshRequestsButton.addActionListener(e -> loadMyRequests());

        actionsPanel.add(refreshRequestsButton);

        panel.add(titleLabel, BorderLayout.NORTH);
        panel.add(requestScrollPane, BorderLayout.CENTER);
        panel.add(actionsPanel, BorderLayout.SOUTH);

        return panel;
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
            field.setPreferredSize(new Dimension(250, 35));
            ((JTextField) field).setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.GRAY),
                new EmptyBorder(8, 12, 8, 12)
            ));
        } else if (field instanceof JComboBox) {
            field.setPreferredSize(new Dimension(250, 35));
        }
        parent.add(field, gbc);
    }

    private void setupVehicleTable() {
        vehicleTable.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        vehicleTable.setRowHeight(35);
        vehicleTable.setSelectionBackground(new Color(155, 89, 182, 150));
        vehicleTable.setSelectionForeground(Color.WHITE);
        vehicleTable.setGridColor(new Color(200, 200, 200));
        vehicleTable.setShowGrid(true);
        vehicleTable.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));
        vehicleTable.getTableHeader().setBackground(new Color(155, 89, 182));
        vehicleTable.getTableHeader().setForeground(Color.WHITE);
        vehicleTable.getTableHeader().setPreferredSize(new Dimension(0, 35));

        // Configurar ancho de columnas
        int[] widths = {50, 80, 100, 60, 100, 100, 120, 130, 80};
        for (int i = 0; i < widths.length && i < vehicleTable.getColumnCount(); i++) {
            vehicleTable.getColumnModel().getColumn(i).setPreferredWidth(widths[i]);
        }

        // Renderer para botón de reportar
        vehicleTable.getColumn("Reportar").setCellRenderer(new ReportButtonRenderer());
        vehicleTable.getColumn("Reportar").setCellEditor(new ReportButtonEditor());
    }

    private void setupRequestTable() {
        requestTable.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        requestTable.setRowHeight(35);
        requestTable.setSelectionBackground(new Color(155, 89, 182, 150));
        requestTable.setSelectionForeground(Color.WHITE);
        requestTable.setGridColor(new Color(200, 200, 200));
        requestTable.setShowGrid(true);
        requestTable.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));
        requestTable.getTableHeader().setBackground(new Color(155, 89, 182));
        requestTable.getTableHeader().setForeground(Color.WHITE);
        requestTable.getTableHeader().setPreferredSize(new Dimension(0, 35));

        // Configurar ancho de columnas
        int[] widths = {50, 120, 80, 200, 100, 120, 80};
        for (int i = 0; i < widths.length && i < requestTable.getColumnCount(); i++) {
            requestTable.getColumnModel().getColumn(i).setPreferredWidth(widths[i]);
        }

        // Renderer para estado
        requestTable.getColumn("Estado").setCellRenderer(new RequestStatusRenderer());
        
        // Renderer para botón de cancelar
        requestTable.getColumn("Cancelar").setCellRenderer(new CancelButtonRenderer());
        requestTable.getColumn("Cancelar").setCellEditor(new CancelButtonEditor());
    }

    private JPanel createFooterPanel() {
        JPanel footerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        footerPanel.setOpaque(false);

        JButton closeButton = createStyledButton("🚪 Cerrar", new Color(108, 117, 125), 120, 35);
        closeButton.addActionListener(e -> dispose());

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
    private void createTablesIfNotExist() {
        // Tabla de solicitudes de vehículos
        String requestTableSql = """
            CREATE TABLE IF NOT EXISTS vehicle_requests (
                id INT AUTO_INCREMENT PRIMARY KEY,
                technician_name VARCHAR(100) NOT NULL,
                vehicle_type VARCHAR(50) NOT NULL,
                priority VARCHAR(20) NOT NULL,
                reason TEXT NOT NULL,
                status VARCHAR(30) DEFAULT 'Pendiente',
                request_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                response_date TIMESTAMP NULL,
                admin_response TEXT NULL
            )
        """;
        
        // Tabla de reportes de problemas
        String problemTableSql = """
            CREATE TABLE IF NOT EXISTS vehicle_problems (
                id INT AUTO_INCREMENT PRIMARY KEY,
                vehicle_id INT NOT NULL,
                technician_name VARCHAR(100) NOT NULL,
                problem_description TEXT NOT NULL,
                status VARCHAR(30) DEFAULT 'Reportado',
                report_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                resolution_date TIMESTAMP NULL,
                resolution_notes TEXT NULL,
                FOREIGN KEY (vehicle_id) REFERENCES vehiculos_flota(id)
            )
        """;
        
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement()) {
            stmt.execute(requestTableSql);
            stmt.execute(problemTableSql);
        } catch (SQLException e) {
            showErrorMessage("Error al crear tablas: " + e.getMessage());
        }
    }

    private void loadData() {
        loadAssignedVehicles();
        loadMyRequests();
    }

    private void loadAssignedVehicles() {
        vehicleTableModel.setRowCount(0);
        String sql = "SELECT id, marca, modelo, ano, placa, tipo, estado, fecha_registro FROM vehiculos_flota WHERE tecnico_asignado = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, currentTechnician);
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
                row.add(rs.getTimestamp("fecha_registro"));
                row.add("Reportar");
                vehicleTableModel.addRow(row);
            }
        } catch (SQLException e) {
            showErrorMessage("Error al cargar vehículos asignados: " + e.getMessage());
        }
    }

    private void loadMyRequests() {
        requestTableModel.setRowCount(0);
        String sql = "SELECT id, vehicle_type, priority, reason, status, request_date FROM vehicle_requests WHERE technician_name = ? ORDER BY request_date DESC";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, currentTechnician);
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                Vector<Object> row = new Vector<>();
                row.add(rs.getInt("id"));
                row.add(rs.getString("vehicle_type"));
                row.add(rs.getString("priority"));
                row.add(rs.getString("reason"));
                row.add(rs.getString("status"));
                row.add(rs.getTimestamp("request_date"));
                row.add("Cancelar");
                requestTableModel.addRow(row);
            }
        } catch (SQLException e) {
            showErrorMessage("Error al cargar solicitudes: " + e.getMessage());
        }
    }

    private void submitVehicleRequest() {
        if (!validateRequestForm()) return;
        
        String sql = "INSERT INTO vehicle_requests (technician_name, vehicle_type, priority, reason) VALUES (?, ?, ?, ?)";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, currentTechnician);
            pstmt.setString(2, (String) vehicleTypeCombo.getSelectedItem());
            pstmt.setString(3, (String) priorityCombo.getSelectedItem());
            pstmt.setString(4, reasonArea.getText().trim());
            
            int result = pstmt.executeUpdate();
            if (result > 0) {
                showSuccessMessage("✅ Solicitud enviada exitosamente!");
                clearRequestForm();
                loadMyRequests();
            }
        } catch (SQLException e) {
            showErrorMessage("Error al enviar solicitud: " + e.getMessage());
        }
    }

    private void reportProblem() {
        int selectedRow = vehicleTable.getSelectedRow();
        if (selectedRow == -1) {
            showWarningMessage("Por favor seleccione un vehículo primero.");
            return;
        }
        
        String problemDescription = problemArea.getText().trim();
        if (problemDescription.isEmpty()) {
            showWarningMessage("Por favor describa el problema.");
            return;
        }
        
        int vehicleId = (Integer) vehicleTableModel.getValueAt(selectedRow, 0);
        
        String sql = "INSERT INTO vehicle_problems (vehicle_id, technician_name, problem_description) VALUES (?, ?, ?)";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, vehicleId);
            pstmt.setString(2, currentTechnician);
            pstmt.setString(3, problemDescription);
            
            int result = pstmt.executeUpdate();
            if (result > 0) {
                showSuccessMessage("✅ Problema reportado exitosamente!");
                problemArea.setText("");
                
                // Actualizar estado del vehículo
                updateVehicleStatus(vehicleId, "Mantenimiento");
                loadAssignedVehicles();
            }
        } catch (SQLException e) {
            showErrorMessage("Error al reportar problema: " + e.getMessage());
        }
    }

    private void updateVehicleStatus(int vehicleId, String newStatus) {
        String sql = "UPDATE vehiculos_flota SET estado = ? WHERE id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, newStatus);
            pstmt.setInt(2, vehicleId);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void cancelRequest(int requestId) {
        int confirm = JOptionPane.showConfirmDialog(
            this,
            "¿Está seguro que desea cancelar esta solicitud?",
            "Confirmar Cancelación",
            JOptionPane.YES_NO_OPTION
        );
        
        if (confirm == JOptionPane.YES_OPTION) {
            String sql = "UPDATE vehicle_requests SET status = 'Cancelada' WHERE id = ?";
            
            try (Connection conn = DatabaseConnection.getConnection();
                 PreparedStatement pstmt = conn.prepareStatement(sql)) {
                
                pstmt.setInt(1, requestId);
                
                int result = pstmt.executeUpdate();
                if (result > 0) {
                    showSuccessMessage("✅ Solicitud cancelada exitosamente!");
                    loadMyRequests();
                }
            } catch (SQLException e) {
                showErrorMessage("Error al cancelar solicitud: " + e.getMessage());
            }
        }
    }

    private void setupEventHandlers() {
        // Selección de vehículo para reportar problemas
        vehicleTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int selectedRow = vehicleTable.getSelectedRow();
                if (selectedRow != -1) {
                    String vehicleInfo = String.format("Vehículo seleccionado: %s %s - %s",
                        vehicleTableModel.getValueAt(selectedRow, 1),
                        vehicleTableModel.getValueAt(selectedRow, 2),
                        vehicleTableModel.getValueAt(selectedRow, 4));
                    // Podrías mostrar info del vehículo seleccionado aquí
                }
            }
        });
    }

    private boolean validateRequestForm() {
        if (reasonArea.getText().trim().isEmpty()) {
            showWarningMessage("Por favor ingrese el motivo de la solicitud.");
            return false;
        }
        
        if (reasonArea.getText().trim().length() < 10) {
            showWarningMessage("El motivo debe tener al menos 10 caracteres.");
            return false;
        }
        
        return true;
    }

    private void clearRequestForm() {
        vehicleTypeCombo.setSelectedIndex(0);
        priorityCombo.setSelectedIndex(0);
        reasonArea.setText("");
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

    // Renderers y editores
    class ReportButtonRenderer extends JButton implements TableCellRenderer {
        public ReportButtonRenderer() {
            setOpaque(true);
            setFont(new Font("Segoe UI", Font.BOLD, 10));
            setBackground(new Color(231, 76, 60));
            setForeground(Color.WHITE);
            setBorder(new EmptyBorder(5, 10, 5, 10));
        }

        public Component getTableCellRendererComponent(JTable table, Object value,
                boolean isSelected, boolean hasFocus, int row, int column) {
            setText("🔧 Reportar");
            return this;
        }
    }

    class ReportButtonEditor extends DefaultCellEditor {
        protected JButton button;
        private boolean isPushed;
        private int selectedRow;

        public ReportButtonEditor() {
            super(new JCheckBox());
            button = new JButton();
            button.setOpaque(true);
            button.setFont(new Font("Segoe UI", Font.BOLD, 10));
            button.setBackground(new Color(231, 76, 60));
            button.setForeground(Color.WHITE);
            button.setBorder(new EmptyBorder(5, 10, 5, 10));
            button.addActionListener(e -> fireEditingStopped());
        }

        public Component getTableCellEditorComponent(JTable table, Object value,
                boolean isSelected, int row, int column) {
            button.setText("🔧 Reportar");
            isPushed = true;
            selectedRow = row;
            return button;
        }

        public Object getCellEditorValue() {
            if (isPushed) {
                vehicleTable.setRowSelectionInterval(selectedRow, selectedRow);
            }
            isPushed = false;
            return "🔧 Reportar";
        }

        public boolean stopCellEditing() {
            isPushed = false;
            return super.stopCellEditing();
        }
    }

    class RequestStatusRenderer extends JLabel implements TableCellRenderer {
        public RequestStatusRenderer() {
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
                    case "pendiente":
                        setBackground(new Color(243, 156, 18));
                        break;
                    case "aprobada":
                        setBackground(new Color(46, 204, 113));
                        break;
                    case "rechazada":
                        setBackground(new Color(231, 76, 60));
                        break;
                    case "cancelada":
                        setBackground(new Color(149, 165, 166));
                        break;
                    default:
                        setBackground(new Color(52, 152, 219));
                        break;
                }
            }
            return this;
        }
    }

    class CancelButtonRenderer extends JButton implements TableCellRenderer {
        public CancelButtonRenderer() {
            setOpaque(true);
            setFont(new Font("Segoe UI", Font.BOLD, 10));
            setBackground(new Color(149, 165, 166));
            setForeground(Color.WHITE);
            setBorder(new EmptyBorder(5, 10, 5, 10));
        }

        public Component getTableCellRendererComponent(JTable table, Object value,
                boolean isSelected, boolean hasFocus, int row, int column) {
            
            String status = table.getValueAt(row, 4).toString(); // Columna de estado
            
            if ("Pendiente".equals(status)) {
                setText("❌ Cancelar");
                setBackground(new Color(231, 76, 60));
                setEnabled(true);
            } else {
                setText("--");
                setBackground(new Color(149, 165, 166));
                setEnabled(false);
            }
            
            return this;
        }
    }

    class CancelButtonEditor extends DefaultCellEditor {
        protected JButton button;
        private boolean isPushed;
        private int selectedRow;

        public CancelButtonEditor() {
            super(new JCheckBox());
            button = new JButton();
            button.setOpaque(true);
            button.setFont(new Font("Segoe UI", Font.BOLD, 10));
            button.addActionListener(e -> fireEditingStopped());
        }

        public Component getTableCellEditorComponent(JTable table, Object value,
                boolean isSelected, int row, int column) {
            
            String status = table.getValueAt(row, 4).toString(); // Columna de estado
            
            if ("Pendiente".equals(status)) {
                button.setText("❌ Cancelar");
                button.setBackground(new Color(231, 76, 60));
                button.setForeground(Color.WHITE);
                button.setEnabled(true);
                isPushed = true;
            } else {
                button.setText("--");
                button.setBackground(new Color(149, 165, 166));
                button.setForeground(Color.WHITE);
                button.setEnabled(false);
                isPushed = false;
            }
            
            selectedRow = row;
            return button;
        }

        public Object getCellEditorValue() {
            if (isPushed) {
                int requestId = (Integer) requestTable.getValueAt(selectedRow, 0);
                cancelRequest(requestId);
            }
            isPushed = false;
            return button.getText();
        }

        public boolean stopCellEditing() {
            isPushed = false;
            return super.stopCellEditing();
        }
    }
}
