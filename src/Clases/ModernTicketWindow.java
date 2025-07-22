package Clases;

import java.awt.*;
import java.sql.*;
import java.util.Vector;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;

public class ModernTicketWindow extends JFrame {
    private JTable contractTable;
    private DefaultTableModel tableModel;
    private JTextArea ticketDetailsArea;
    private JComboBox<String> technicianComboBox;
    private JComboBox<String> priorityComboBox;
    private JTextField ticketSubjectField;
    private JButton sendTicketButton;
    private BackgroundPanel backgroundPanel;

    public ModernTicketWindow() {
        initializeComponents();
        setupGUI();
        loadContracts();
        setupEventHandlers();
    }

    private void initializeComponents() {
        setTitle("Gestión Moderna de Contratos y Tickets");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(1400, 900);
        setLocationRelativeTo(null);

        // Panel de fondo con imagen
        backgroundPanel = new BackgroundPanel("/Imagenes/FondoLogin.jpg");
        backgroundPanel.setLayout(new BorderLayout());
        setContentPane(backgroundPanel);

        // Modelo de tabla para contratos
        String[] columnNames = {"ID", "Cliente", "Descripción", "Fecha Inicio", "Fecha Fin", "Estado", "Total", "Acciones"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 7; // Solo la columna de acciones es editable
            }
        };
        contractTable = new JTable(tableModel);
    }

    private void setupGUI() {
        // Panel principal con diseño moderno
        JPanel mainPanel = new JPanel(new BorderLayout(20, 20));
        mainPanel.setOpaque(false);
        mainPanel.setBorder(new EmptyBorder(30, 30, 30, 30));

        // Panel superior con título
        JPanel headerPanel = createHeaderPanel();
        mainPanel.add(headerPanel, BorderLayout.NORTH);

        // Panel central dividido
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        splitPane.setOpaque(false);
        splitPane.setDividerLocation(800);
        splitPane.setResizeWeight(0.6);

        // Panel izquierdo - Lista de contratos
        JPanel contractPanel = createContractPanel();
        splitPane.setLeftComponent(contractPanel);

        // Panel derecho - Creación de tickets
        JPanel ticketPanel = createTicketPanel();
        splitPane.setRightComponent(ticketPanel);

        mainPanel.add(splitPane, BorderLayout.CENTER);

        // Panel inferior con botones de acción
        JPanel footerPanel = createFooterPanel();
        mainPanel.add(footerPanel, BorderLayout.SOUTH);

        backgroundPanel.add(mainPanel);
    }

    private JPanel createHeaderPanel() {
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setOpaque(false);

        JLabel titleLabel = new JLabel("Gestión de Contratos y Tickets", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 28));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setBorder(new EmptyBorder(10, 0, 20, 0));

        JPanel titleWrapper = new JPanel(new FlowLayout());
        titleWrapper.setOpaque(false);
        titleWrapper.add(titleLabel);

        headerPanel.add(titleWrapper, BorderLayout.CENTER);
        return headerPanel;
    }

    private JPanel createContractPanel() {
        JPanel contractPanel = new JPanel(new BorderLayout(10, 10));
        contractPanel.setOpaque(false);
        contractPanel.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(Color.WHITE, 2),
            "Contratos Activos",
            0, 0,
            new Font("Segoe UI", Font.BOLD, 16),
            Color.WHITE
        ));

        // Configurar tabla con diseño moderno
        setupModernTable();
        JScrollPane scrollPane = new JScrollPane(contractTable);
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);
        scrollPane.setBorder(BorderFactory.createLineBorder(Color.WHITE));
        scrollPane.setPreferredSize(new Dimension(750, 500));

        // Panel de filtros
        JPanel filterPanel = createFilterPanel();
        contractPanel.add(filterPanel, BorderLayout.NORTH);
        contractPanel.add(scrollPane, BorderLayout.CENTER);

        return contractPanel;
    }

    private JPanel createFilterPanel() {
        JPanel filterPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        filterPanel.setOpaque(false);

        JLabel filterLabel = new JLabel("Filtrar por estado:");
        filterLabel.setForeground(Color.WHITE);
        filterLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));

        JComboBox<String> statusFilter = new JComboBox<>(new String[]{"Todos", "Activo", "Pendiente", "Completado", "Cancelado"});
        statusFilter.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        statusFilter.addActionListener(e -> filterContractsByStatus((String) statusFilter.getSelectedItem()));

        JButton refreshButton = new JButton("🔄 Actualizar");
        refreshButton.setFont(new Font("Segoe UI", Font.BOLD, 12));
        refreshButton.setBackground(new Color(230, 126, 34)); // Naranja
        refreshButton.setForeground(Color.WHITE);
        refreshButton.setBorder(new EmptyBorder(8, 15, 8, 15));
        refreshButton.setFocusPainted(false);
        refreshButton.addActionListener(e -> loadContracts());

        filterPanel.add(filterLabel);
        filterPanel.add(statusFilter);
        filterPanel.add(Box.createHorizontalStrut(20));
        filterPanel.add(refreshButton);

        return filterPanel;
    }

    private void setupModernTable() {
        contractTable.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        contractTable.setRowHeight(35);
        contractTable.setSelectionBackground(new Color(230, 126, 34, 150)); // Naranja con transparencia
        contractTable.setSelectionForeground(Color.WHITE);
        contractTable.setGridColor(new Color(200, 200, 200));
        contractTable.setShowGrid(true);
        contractTable.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));
        contractTable.getTableHeader().setBackground(new Color(230, 126, 34)); // Naranja
        contractTable.getTableHeader().setForeground(Color.WHITE);

        // Configurar ancho de columnas
        TableColumn[] columns = new TableColumn[tableModel.getColumnCount()];
        int[] widths = {60, 150, 200, 120, 120, 100, 100, 120};
        
        for (int i = 0; i < columns.length; i++) {
            columns[i] = contractTable.getColumnModel().getColumn(i);
            columns[i].setPreferredWidth(widths[i]);
        }

        // Renderer personalizado para la columna de acciones
        contractTable.getColumn("Acciones").setCellRenderer(new ActionButtonRenderer());
        contractTable.getColumn("Acciones").setCellEditor(new ActionButtonEditor());
    }

    private JPanel createTicketPanel() {
        JPanel ticketPanel = new JPanel(new BorderLayout(10, 10));
        ticketPanel.setOpaque(false);
        ticketPanel.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(Color.WHITE, 2),
            "Crear Ticket de Soporte",
            0, 0,
            new Font("Segoe UI", Font.BOLD, 16),
            Color.WHITE
        ));

        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.anchor = GridBagConstraints.WEST;

        // Campo de asunto
        gbc.gridx = 0; gbc.gridy = 0;
        JLabel subjectLabel = new JLabel("Asunto del Ticket:");
        subjectLabel.setForeground(Color.WHITE);
        subjectLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        formPanel.add(subjectLabel, gbc);

        gbc.gridx = 1; gbc.gridy = 0; gbc.fill = GridBagConstraints.HORIZONTAL; gbc.weightx = 1.0;
        ticketSubjectField = new JTextField(20);
        ticketSubjectField.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        formPanel.add(ticketSubjectField, gbc);

        // Selección de técnico
        gbc.gridx = 0; gbc.gridy = 1; gbc.fill = GridBagConstraints.NONE; gbc.weightx = 0;
        JLabel techLabel = new JLabel("Asignar a Técnico:");
        techLabel.setForeground(Color.WHITE);
        techLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        formPanel.add(techLabel, gbc);

        gbc.gridx = 1; gbc.gridy = 1; gbc.fill = GridBagConstraints.HORIZONTAL; gbc.weightx = 1.0;
        technicianComboBox = new JComboBox<>();
        technicianComboBox.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        loadTechnicians();
        formPanel.add(technicianComboBox, gbc);

        // Prioridad
        gbc.gridx = 0; gbc.gridy = 2; gbc.fill = GridBagConstraints.NONE; gbc.weightx = 0;
        JLabel priorityLabel = new JLabel("Prioridad:");
        priorityLabel.setForeground(Color.WHITE);
        priorityLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        formPanel.add(priorityLabel, gbc);

        gbc.gridx = 1; gbc.gridy = 2; gbc.fill = GridBagConstraints.HORIZONTAL; gbc.weightx = 1.0;
        priorityComboBox = new JComboBox<>(new String[]{"Baja", "Media", "Alta", "Crítica"});
        priorityComboBox.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        formPanel.add(priorityComboBox, gbc);

        // Área de descripción
        gbc.gridx = 0; gbc.gridy = 3; gbc.fill = GridBagConstraints.NONE; gbc.weightx = 0;
        JLabel descLabel = new JLabel("Descripción del Problema:");
        descLabel.setForeground(Color.WHITE);
        descLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        formPanel.add(descLabel, gbc);

        gbc.gridx = 0; gbc.gridy = 4; gbc.gridwidth = 2; gbc.fill = GridBagConstraints.BOTH; gbc.weightx = 1.0; gbc.weighty = 1.0;
        ticketDetailsArea = new JTextArea(15, 30);
        ticketDetailsArea.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        ticketDetailsArea.setLineWrap(true);
        ticketDetailsArea.setWrapStyleWord(true);
        ticketDetailsArea.setBorder(new EmptyBorder(10, 10, 10, 10));
        JScrollPane textScrollPane = new JScrollPane(ticketDetailsArea);
        textScrollPane.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        formPanel.add(textScrollPane, gbc);

        ticketPanel.add(formPanel, BorderLayout.CENTER);
        return ticketPanel;
    }

    private JPanel createFooterPanel() {
        JPanel footerPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        footerPanel.setOpaque(false);

        sendTicketButton = new JButton("📨 Enviar Ticket");
        sendTicketButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        sendTicketButton.setBackground(new Color(230, 126, 34)); // Naranja
        sendTicketButton.setForeground(Color.WHITE);
        sendTicketButton.setBorder(new EmptyBorder(12, 25, 12, 25));
        sendTicketButton.setFocusPainted(false);
        sendTicketButton.setCursor(new Cursor(Cursor.HAND_CURSOR));

        JButton closeButton = new JButton("🚪 Cerrar");
        closeButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        closeButton.setBackground(new Color(231, 76, 60));
        closeButton.setForeground(Color.WHITE);
        closeButton.setBorder(new EmptyBorder(12, 25, 12, 25));
        closeButton.setFocusPainted(false);
        closeButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        closeButton.addActionListener(e -> dispose());

        footerPanel.add(sendTicketButton);
        footerPanel.add(Box.createHorizontalStrut(10));
        footerPanel.add(closeButton);

        return footerPanel;
    }

    private void loadContracts() {
        tableModel.setRowCount(0);
        String sql = "SELECT id, cliente_id, descripcion, fecha_inicio, fecha_fin, estado, total FROM contrato ORDER BY fecha_inicio DESC";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            
            while (rs.next()) {
                Vector<Object> row = new Vector<>();
                row.add(rs.getInt("id"));
                
                // Obtener nombre del cliente
                String clienteName = getClientName(rs.getInt("cliente_id"));
                row.add(clienteName);
                
                row.add(rs.getString("descripcion"));
                row.add(rs.getDate("fecha_inicio"));
                row.add(rs.getDate("fecha_fin"));
                row.add(rs.getString("estado"));
                row.add(String.format("$%.2f", rs.getDouble("total")));
                row.add("Crear Ticket");
                
                tableModel.addRow(row);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, 
                "Error al cargar contratos: " + e.getMessage(), 
                "Error de Base de Datos", 
                JOptionPane.ERROR_MESSAGE);
        }
    }

    private String getClientName(int clienteId) {
        String sql = "SELECT nombre FROM cliente WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, clienteId);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                return rs.getString("nombre");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return "Cliente #" + clienteId;
    }

    private void loadTechnicians() {
        technicianComboBox.removeAllItems();
        technicianComboBox.addItem("Seleccionar técnico...");
        
        String sql = "SELECT id, nombre FROM cliente WHERE rol = 'Tecnico'";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            
            while (rs.next()) {
                String technician = rs.getString("nombre") + " (ID: " + rs.getInt("id") + ")";
                technicianComboBox.addItem(technician);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void filterContractsByStatus(String status) {
        if ("Todos".equals(status)) {
            loadContracts();
            return;
        }
        
        tableModel.setRowCount(0);
        String sql = "SELECT id, cliente_id, descripcion, fecha_inicio, fecha_fin, estado, total FROM contrato WHERE estado = ? ORDER BY fecha_inicio DESC";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, status);
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                Vector<Object> row = new Vector<>();
                row.add(rs.getInt("id"));
                
                String clienteName = getClientName(rs.getInt("cliente_id"));
                row.add(clienteName);
                
                row.add(rs.getString("descripcion"));
                row.add(rs.getDate("fecha_inicio"));
                row.add(rs.getDate("fecha_fin"));
                row.add(rs.getString("estado"));
                row.add(String.format("$%.2f", rs.getDouble("total")));
                row.add("Crear Ticket");
                
                tableModel.addRow(row);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, 
                "Error al filtrar contratos: " + e.getMessage(), 
                "Error de Base de Datos", 
                JOptionPane.ERROR_MESSAGE);
        }
    }

    private void setupEventHandlers() {
        sendTicketButton.addActionListener(e -> createTicket());
        
        contractTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int selectedRow = contractTable.getSelectedRow();
                if (selectedRow != -1) {
                    populateTicketFromContract(selectedRow);
                }
            }
        });
    }

    private void populateTicketFromContract(int row) {
        String contractId = tableModel.getValueAt(row, 0).toString();
        String clientName = tableModel.getValueAt(row, 1).toString();
        String description = tableModel.getValueAt(row, 2).toString();
        
        ticketSubjectField.setText("Soporte para Contrato #" + contractId + " - " + clientName);
        ticketDetailsArea.setText("Descripción del contrato: " + description + "\n\nDescribir el problema o solicitud de soporte:");
    }

    private void createTicket() {
        if (contractTable.getSelectedRow() == -1) {
            JOptionPane.showMessageDialog(this, 
                "Por favor seleccione un contrato primero.", 
                "Selección Requerida", 
                JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (ticketSubjectField.getText().trim().isEmpty() || 
            ticketDetailsArea.getText().trim().isEmpty() ||
            technicianComboBox.getSelectedIndex() == 0) {
            
            JOptionPane.showMessageDialog(this, 
                "Por favor complete todos los campos del ticket.", 
                "Campos Requeridos", 
                JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Aquí implementarías la lógica para guardar el ticket en la base de datos
        JOptionPane.showMessageDialog(this, 
            "Ticket creado y enviado exitosamente!\n" +
            "Asunto: " + ticketSubjectField.getText() + "\n" +
            "Técnico: " + technicianComboBox.getSelectedItem() + "\n" +
            "Prioridad: " + priorityComboBox.getSelectedItem(), 
            "Ticket Enviado", 
            JOptionPane.INFORMATION_MESSAGE);
        
        // Limpiar campos
        ticketSubjectField.setText("");
        ticketDetailsArea.setText("");
        technicianComboBox.setSelectedIndex(0);
        priorityComboBox.setSelectedIndex(0);
    }

    // Renderer para botones en la tabla
    class ActionButtonRenderer extends JButton implements TableCellRenderer {
        public ActionButtonRenderer() {
            setOpaque(true);
            setFont(new Font("Segoe UI", Font.BOLD, 10));
            setBackground(new Color(52, 152, 219));
            setForeground(Color.WHITE);
            setBorder(new EmptyBorder(5, 10, 5, 10));
        }

        public Component getTableCellRendererComponent(JTable table, Object value,
                boolean isSelected, boolean hasFocus, int row, int column) {
            setText((value == null) ? "Crear Ticket" : value.toString());
            return this;
        }
    }

    // Editor para botones en la tabla
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
            button.setBackground(new Color(230, 126, 34)); // Naranja
            button.setForeground(Color.WHITE);
            button.setBorder(new EmptyBorder(5, 10, 5, 10));
            button.addActionListener(e -> fireEditingStopped());
        }

        public Component getTableCellEditorComponent(JTable table, Object value,
                boolean isSelected, int row, int column) {
            label = (value == null) ? "Crear Ticket" : value.toString();
            button.setText(label);
            isPushed = true;
            selectedRow = row;
            return button;
        }

        public Object getCellEditorValue() {
            if (isPushed) {
                contractTable.setRowSelectionInterval(selectedRow, selectedRow);
                populateTicketFromContract(selectedRow);
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
