package Clases;

import java.awt.*;
import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

/**
 * Ventana modernizada para gestión de vehículos
 * @author NetNexus Team
 */
public class ModernVehicleWindow extends JFrame {
    
    private static final Logger logger = Logger.getLogger(ModernVehicleWindow.class.getName());
    private JPanel vehiclesPanel;
    private JScrollPane scrollPane;
    
    public ModernVehicleWindow() {
        initComponents();
        loadVehicles();
    }
    
    private void initComponents() {
        setTitle("Gestión de Vehículos - NetNexus Ultra");
        setSize(1200, 800);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        
        // Panel principal con fondo
        BackgroundPanel mainPanel = new BackgroundPanel("/Imagenes/fondo.png");
        mainPanel.setLayout(new BorderLayout());
        
        // Panel de título
        JPanel titlePanel = new JPanel();
        titlePanel.setOpaque(false);
        titlePanel.setBorder(new EmptyBorder(20, 20, 10, 20));
        
        JLabel titleLabel = new JLabel("🚗 Gestión de Flota de Vehículos", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 32));
        titleLabel.setForeground(Color.WHITE);
        titlePanel.add(titleLabel);
        
        // Panel de estadísticas
        JPanel statsPanel = createStatsPanel();
        
        // Panel de filtros y acciones
        JPanel filterPanel = new JPanel(new FlowLayout());
        filterPanel.setOpaque(false);
        filterPanel.setBorder(new EmptyBorder(10, 20, 15, 20));
        
        JLabel filterLabel = new JLabel("Filtrar por estado:");
        filterLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        filterLabel.setForeground(Color.WHITE);
        
        JComboBox<String> statusFilter = new JComboBox<>(new String[]{"Todos", "Disponible", "En Servicio", "Mantenimiento", "Fuera de Servicio"});
        statusFilter.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        statusFilter.setPreferredSize(new Dimension(180, 30));
        statusFilter.addActionListener(e -> loadVehicles((String) statusFilter.getSelectedItem()));
        
        JLabel typeLabel = new JLabel("Tipo:");
        typeLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        typeLabel.setForeground(Color.WHITE);
        
        JComboBox<String> typeFilter = new JComboBox<>(new String[]{"Todos", "Camioneta", "Furgoneta", "Automóvil", "Motocicleta"});
        typeFilter.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        typeFilter.setPreferredSize(new Dimension(150, 30));
        typeFilter.addActionListener(e -> loadVehicles((String) statusFilter.getSelectedItem(), (String) typeFilter.getSelectedItem()));
        
        JButton addButton = createStyledButton("➕ Agregar Vehículo", new Color(40, 167, 69));
        addButton.addActionListener(e -> addNewVehicle());
        
        JButton refreshButton = createStyledButton("🔄 Actualizar", new Color(70, 130, 180));
        refreshButton.addActionListener(e -> {
            loadVehicles((String) statusFilter.getSelectedItem(), (String) typeFilter.getSelectedItem());
            updateStatsPanel(statsPanel);
        });
        
        filterPanel.add(filterLabel);
        filterPanel.add(Box.createHorizontalStrut(10));
        filterPanel.add(statusFilter);
        filterPanel.add(Box.createHorizontalStrut(20));
        filterPanel.add(typeLabel);
        filterPanel.add(Box.createHorizontalStrut(10));
        filterPanel.add(typeFilter);
        filterPanel.add(Box.createHorizontalStrut(20));
        filterPanel.add(addButton);
        filterPanel.add(Box.createHorizontalStrut(10));
        filterPanel.add(refreshButton);
        
        // Panel superior
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setOpaque(false);
        topPanel.add(titlePanel, BorderLayout.NORTH);
        topPanel.add(statsPanel, BorderLayout.CENTER);
        topPanel.add(filterPanel, BorderLayout.SOUTH);
        
        // Panel de vehículos con scroll
        vehiclesPanel = new JPanel();
        vehiclesPanel.setLayout(new BoxLayout(vehiclesPanel, BoxLayout.Y_AXIS));
        vehiclesPanel.setOpaque(false);
        vehiclesPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        
        scrollPane = new JScrollPane(vehiclesPanel);
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        
        // Panel de botones
        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.setOpaque(false);
        buttonPanel.setBorder(new EmptyBorder(10, 20, 20, 20));
        
        JButton closeButton = createStyledButton("❌ Cerrar", new Color(220, 53, 69));
        closeButton.addActionListener(e -> dispose());
        
        buttonPanel.add(closeButton);
        
        // Ensamblar panel principal
        mainPanel.add(topPanel, BorderLayout.NORTH);
        mainPanel.add(scrollPane, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);
        
        setContentPane(mainPanel);
    }
    
    private JPanel createStatsPanel() {
        JPanel statsPanel = new JPanel(new FlowLayout());
        statsPanel.setOpaque(false);
        statsPanel.setBorder(new EmptyBorder(10, 20, 10, 20));
        
        updateStatsPanel(statsPanel);
        
        return statsPanel;
    }
    
    private void updateStatsPanel(JPanel statsPanel) {
        statsPanel.removeAll();
        
        try (Connection conn = DatabaseConnection.getConnection()) {
            // Crear tabla si no existe
            createVehicleTableIfNotExists(conn);
            
            // Contar vehículos por estado
            String query = "SELECT estado, COUNT(*) as count FROM vehiculos GROUP BY estado";
            
            try (PreparedStatement pstmt = conn.prepareStatement(query);
                 ResultSet rs = pstmt.executeQuery()) {
                
                int total = 0;
                while (rs.next()) {
                    String estado = rs.getString("estado");
                    int count = rs.getInt("count");
                    total += count;
                    
                    Color color = getVehicleStatusColor(estado);
                    JPanel statCard = createStatCard(estado, count, color);
                    statsPanel.add(statCard);
                    statsPanel.add(Box.createHorizontalStrut(15));
                }
                
                // Card de total
                JPanel totalCard = createStatCard("Total", total, new Color(108, 117, 125));
                statsPanel.add(totalCard);
                
                if (total == 0) {
                    // Agregar vehículos de ejemplo si no hay datos
                    insertSampleVehicles(conn);
                    updateStatsPanel(statsPanel);
                    return;
                }
            }
        } catch (SQLException e) {
            logger.log(Level.WARNING, "Error obteniendo estadísticas de vehículos", e);
        }
        
        statsPanel.revalidate();
        statsPanel.repaint();
    }
    
    private void createVehicleTableIfNotExists(Connection conn) throws SQLException {
        String createTableQuery = """
            CREATE TABLE IF NOT EXISTS vehiculos (
                id INT AUTO_INCREMENT PRIMARY KEY,
                marca VARCHAR(50) NOT NULL,
                modelo VARCHAR(50) NOT NULL,
                año INT,
                placa VARCHAR(20) UNIQUE,
                tipo VARCHAR(30),
                estado VARCHAR(30) DEFAULT 'Disponible',
                kilometraje INT DEFAULT 0,
                ubicacion VARCHAR(100),
                conductor_asignado VARCHAR(100),
                fecha_mantenimiento DATE,
                notas TEXT,
                fecha_registro TIMESTAMP DEFAULT CURRENT_TIMESTAMP
            )
            """;
        
        try (PreparedStatement pstmt = conn.prepareStatement(createTableQuery)) {
            pstmt.execute();
        }
    }
    
    private void insertSampleVehicles(Connection conn) throws SQLException {
        String insertQuery = """
            INSERT IGNORE INTO vehiculos (marca, modelo, año, placa, tipo, estado, kilometraje, ubicacion, conductor_asignado, notas)
            VALUES 
            ('Toyota', 'Hilux', 2022, 'ABC-1234', 'Camioneta', 'Disponible', 15000, 'Sede Central', null, 'Vehículo para servicios técnicos'),
            ('Ford', 'Transit', 2021, 'DEF-5678', 'Furgoneta', 'En Servicio', 25000, 'Zona Norte', 'Juan Pérez', 'Equipada con herramientas'),
            ('Chevrolet', 'Spark', 2020, 'GHI-9012', 'Automóvil', 'Mantenimiento', 45000, 'Taller', null, 'Mantenimiento preventivo'),
            ('Honda', 'CB600F', 2023, 'JKL-3456', 'Motocicleta', 'Disponible', 8000, 'Sede Central', null, 'Para servicios rápidos'),
            ('Nissan', 'NV200', 2019, 'MNO-7890', 'Furgoneta', 'Fuera de Servicio', 80000, 'Taller', null, 'Requiere reparación mayor')
            """;
        
        try (PreparedStatement pstmt = conn.prepareStatement(insertQuery)) {
            pstmt.execute();
        }
    }
    
    private JPanel createStatCard(String label, int count, Color color) {
        JPanel card = new JPanel(new BorderLayout());
        card.setOpaque(true);
        card.setBackground(new Color(255, 255, 255, 220));
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(color, 2),
            new EmptyBorder(10, 15, 10, 15)
        ));
        card.setPreferredSize(new Dimension(140, 70));
        
        JLabel countLabel = new JLabel(String.valueOf(count), SwingConstants.CENTER);
        countLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        countLabel.setForeground(color);
        
        JLabel labelLabel = new JLabel(label, SwingConstants.CENTER);
        labelLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        labelLabel.setForeground(new Color(70, 90, 110));
        
        card.add(countLabel, BorderLayout.CENTER);
        card.add(labelLabel, BorderLayout.SOUTH);
        
        return card;
    }
    
    private JButton createStyledButton(String text, Color baseColor) {
        JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI", Font.BOLD, 14));
        button.setBackground(baseColor);
        button.setForeground(Color.WHITE);
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setPreferredSize(new Dimension(170, 35));
        
        // Efectos hover
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(baseColor.brighter());
            }
            
            @Override
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(baseColor);
            }
        });
        
        return button;
    }
    
    private void loadVehicles() {
        loadVehicles("Todos", "Todos");
    }
    
    private void loadVehicles(String statusFilter) {
        loadVehicles(statusFilter, "Todos");
    }
    
    private void loadVehicles(String statusFilter, String typeFilter) {
        vehiclesPanel.removeAll();
        
        try (Connection conn = DatabaseConnection.getConnection()) {
            createVehicleTableIfNotExists(conn);
            
            StringBuilder queryBuilder = new StringBuilder("""
                SELECT id, marca, modelo, año, placa, tipo, estado, kilometraje, 
                       ubicacion, conductor_asignado, fecha_mantenimiento, notas
                FROM vehiculos
                WHERE 1=1
                """);
            
            if (!"Todos".equals(statusFilter)) {
                queryBuilder.append(" AND estado = ?");
            }
            if (!"Todos".equals(typeFilter)) {
                queryBuilder.append(" AND tipo = ?");
            }
            queryBuilder.append(" ORDER BY estado, marca, modelo");
            
            try (PreparedStatement pstmt = conn.prepareStatement(queryBuilder.toString())) {
                int paramIndex = 1;
                if (!"Todos".equals(statusFilter)) {
                    pstmt.setString(paramIndex++, statusFilter);
                }
                if (!"Todos".equals(typeFilter)) {
                    pstmt.setString(paramIndex, typeFilter);
                }
                
                try (ResultSet rs = pstmt.executeQuery()) {
                    boolean hasVehicles = false;
                    
                    while (rs.next()) {
                        hasVehicles = true;
                        JPanel vehicleCard = createVehicleCard(
                            rs.getInt("id"),
                            rs.getString("marca"),
                            rs.getString("modelo"),
                            rs.getInt("año"),
                            rs.getString("placa"),
                            rs.getString("tipo"),
                            rs.getString("estado"),
                            rs.getInt("kilometraje"),
                            rs.getString("ubicacion"),
                            rs.getString("conductor_asignado"),
                            rs.getDate("fecha_mantenimiento"),
                            rs.getString("notas")
                        );
                        vehiclesPanel.add(vehicleCard);
                        vehiclesPanel.add(Box.createVerticalStrut(10));
                    }
                    
                    if (!hasVehicles) {
                        JLabel noVehiclesLabel = new JLabel("No hay vehículos para mostrar", SwingConstants.CENTER);
                        noVehiclesLabel.setFont(new Font("Segoe UI", Font.ITALIC, 18));
                        noVehiclesLabel.setForeground(new Color(200, 200, 200));
                        noVehiclesLabel.setPreferredSize(new Dimension(1100, 100));
                        vehiclesPanel.add(noVehiclesLabel);
                    }
                }
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error cargando vehículos", e);
            JOptionPane.showMessageDialog(this, 
                "Error al cargar los vehículos: " + e.getMessage(), 
                "Error de Base de Datos", 
                JOptionPane.ERROR_MESSAGE);
        }
        
        vehiclesPanel.revalidate();
        vehiclesPanel.repaint();
    }
    
    private JPanel createVehicleCard(int id, String marca, String modelo, int año, 
                                   String placa, String tipo, String estado, int kilometraje,
                                   String ubicacion, String conductorAsignado, Date fechaMantenimiento,
                                   String notas) {
        JPanel card = new JPanel(new BorderLayout());
        card.setOpaque(true);
        card.setBackground(new Color(255, 255, 255, 245));
        
        Color statusColor = getVehicleStatusColor(estado);
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(statusColor, 3),
            new EmptyBorder(15, 20, 15, 20)
        ));
        card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 160));
        
        // Panel de información principal
        JPanel infoPanel = new JPanel(new GridBagLayout());
        infoPanel.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(3, 0, 3, 25);
        
        // Icono del vehículo y nombre
        gbc.gridx = 0; gbc.gridy = 0;
        String vehicleIcon = getVehicleIcon(tipo);
        JLabel nameLabel = new JLabel(vehicleIcon + " " + marca + " " + modelo + " (" + año + ")");
        nameLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        nameLabel.setForeground(new Color(50, 70, 90));
        infoPanel.add(nameLabel, gbc);
        
        gbc.gridx = 1;
        JLabel plateLabel = new JLabel("Placa: " + (placa != null ? placa : "N/A"));
        plateLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        plateLabel.setForeground(new Color(70, 90, 110));
        infoPanel.add(plateLabel, gbc);
        
        gbc.gridx = 2;
        JLabel statusLabel = new JLabel("● " + estado);
        statusLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        statusLabel.setForeground(statusColor);
        infoPanel.add(statusLabel, gbc);
        
        // Tipo y kilometraje
        gbc.gridx = 0; gbc.gridy = 1;
        JLabel typeLabel = new JLabel("Tipo: " + (tipo != null ? tipo : "N/A"));
        typeLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        typeLabel.setForeground(new Color(70, 90, 110));
        infoPanel.add(typeLabel, gbc);
        
        gbc.gridx = 1;
        JLabel mileageLabel = new JLabel("📊 " + String.format("%,d km", kilometraje));
        mileageLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        mileageLabel.setForeground(new Color(70, 90, 110));
        infoPanel.add(mileageLabel, gbc);
        
        gbc.gridx = 2;
        String maintenanceText = fechaMantenimiento != null ? 
            "🔧 Mant: " + fechaMantenimiento.toString() : "🔧 Sin programar";
        JLabel maintenanceLabel = new JLabel(maintenanceText);
        maintenanceLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        maintenanceLabel.setForeground(new Color(100, 120, 140));
        infoPanel.add(maintenanceLabel, gbc);
        
        // Ubicación y conductor
        gbc.gridx = 0; gbc.gridy = 2;
        JLabel locationLabel = new JLabel("📍 " + (ubicacion != null ? ubicacion : "Sin ubicación"));
        locationLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        locationLabel.setForeground(new Color(70, 90, 110));
        infoPanel.add(locationLabel, gbc);
        
        gbc.gridx = 1; gbc.gridwidth = 2;
        String conductorText = conductorAsignado != null ? 
            "👤 Conductor: " + conductorAsignado : "👤 Sin asignar";
        JLabel conductorLabel = new JLabel(conductorText);
        conductorLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        conductorLabel.setForeground(new Color(70, 90, 110));
        infoPanel.add(conductorLabel, gbc);
        
        // Notas (si existen)
        if (notas != null && !notas.trim().isEmpty()) {
            gbc.gridx = 0; gbc.gridy = 3; gbc.gridwidth = 3;
            String notasTruncadas = notas.length() > 60 ? notas.substring(0, 60) + "..." : notas;
            JLabel notesLabel = new JLabel("📝 " + notasTruncadas);
            notesLabel.setFont(new Font("Segoe UI", Font.ITALIC, 12));
            notesLabel.setForeground(new Color(120, 140, 160));
            infoPanel.add(notesLabel, gbc);
        }
        
        // Panel de botones
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.setOpaque(false);
        
        JButton detailsButton = createStyledButton("👁️ Detalles", new Color(40, 167, 69));
        detailsButton.setPreferredSize(new Dimension(110, 30));
        detailsButton.addActionListener(e -> showVehicleDetails(id));
        
        JButton editButton = createStyledButton("✏️ Editar", new Color(255, 193, 7));
        editButton.setPreferredSize(new Dimension(100, 30));
        editButton.addActionListener(e -> editVehicle(id));
        
        JButton statusButton = createStyledButton("🔄 Estado", new Color(70, 130, 180));
        statusButton.setPreferredSize(new Dimension(100, 30));
        statusButton.addActionListener(e -> changeVehicleStatus(id, estado));
        
        JButton deleteButton = createStyledButton("🗑️ Eliminar", new Color(220, 53, 69));
        deleteButton.setPreferredSize(new Dimension(110, 30));
        deleteButton.addActionListener(e -> deleteVehicle(id, marca + " " + modelo));
        
        buttonPanel.add(detailsButton);
        buttonPanel.add(Box.createHorizontalStrut(5));
        buttonPanel.add(editButton);
        buttonPanel.add(Box.createHorizontalStrut(5));
        buttonPanel.add(statusButton);
        buttonPanel.add(Box.createHorizontalStrut(5));
        buttonPanel.add(deleteButton);
        
        card.add(infoPanel, BorderLayout.CENTER);
        card.add(buttonPanel, BorderLayout.EAST);
        
        return card;
    }
    
    private String getVehicleIcon(String tipo) {
        return switch (tipo != null ? tipo : "") {
            case "Camioneta" -> "🚙";
            case "Furgoneta" -> "🚐";
            case "Automóvil" -> "🚗";
            case "Motocicleta" -> "🏍️";
            default -> "🚗";
        };
    }
    
    private Color getVehicleStatusColor(String estado) {
        return switch (estado != null ? estado : "") {
            case "Disponible" -> new Color(40, 167, 69);
            case "En Servicio" -> new Color(70, 130, 180);
            case "Mantenimiento" -> new Color(255, 193, 7);
            case "Fuera de Servicio" -> new Color(220, 53, 69);
            default -> new Color(108, 117, 125);
        };
    }
    
    private void addNewVehicle() {
        JDialog dialog = new JDialog(this, "Agregar Nuevo Vehículo", true);
        dialog.setSize(450, 600);
        dialog.setLocationRelativeTo(this);
        
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(new EmptyBorder(20, 20, 20, 20));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;
        
        // Campos del formulario
        JTextField marcaField = new JTextField(20);
        JTextField modeloField = new JTextField(20);
        JSpinner añoSpinner = new JSpinner(new SpinnerNumberModel(2024, 1980, 2030, 1));
        JTextField placaField = new JTextField(20);
        JComboBox<String> tipoCombo = new JComboBox<>(new String[]{"Camioneta", "Furgoneta", "Automóvil", "Motocicleta"});
        JComboBox<String> estadoCombo = new JComboBox<>(new String[]{"Disponible", "En Servicio", "Mantenimiento", "Fuera de Servicio"});
        JSpinner kilometrajeSpinner = new JSpinner(new SpinnerNumberModel(0, 0, 999999, 1000));
        JTextField ubicacionField = new JTextField(20);
        JTextField conductorField = new JTextField(20);
        JTextArea notasArea = new JTextArea(3, 20);
        notasArea.setLineWrap(true);
        notasArea.setWrapStyleWord(true);
        
        // Agregar campos al panel
        int row = 0;
        
        gbc.gridx = 0; gbc.gridy = row;
        panel.add(new JLabel("Marca:*"), gbc);
        gbc.gridx = 1;
        panel.add(marcaField, gbc);
        
        gbc.gridx = 0; gbc.gridy = ++row;
        panel.add(new JLabel("Modelo:*"), gbc);
        gbc.gridx = 1;
        panel.add(modeloField, gbc);
        
        gbc.gridx = 0; gbc.gridy = ++row;
        panel.add(new JLabel("Año:"), gbc);
        gbc.gridx = 1;
        panel.add(añoSpinner, gbc);
        
        gbc.gridx = 0; gbc.gridy = ++row;
        panel.add(new JLabel("Placa:"), gbc);
        gbc.gridx = 1;
        panel.add(placaField, gbc);
        
        gbc.gridx = 0; gbc.gridy = ++row;
        panel.add(new JLabel("Tipo:"), gbc);
        gbc.gridx = 1;
        panel.add(tipoCombo, gbc);
        
        gbc.gridx = 0; gbc.gridy = ++row;
        panel.add(new JLabel("Estado:"), gbc);
        gbc.gridx = 1;
        panel.add(estadoCombo, gbc);
        
        gbc.gridx = 0; gbc.gridy = ++row;
        panel.add(new JLabel("Kilometraje:"), gbc);
        gbc.gridx = 1;
        panel.add(kilometrajeSpinner, gbc);
        
        gbc.gridx = 0; gbc.gridy = ++row;
        panel.add(new JLabel("Ubicación:"), gbc);
        gbc.gridx = 1;
        panel.add(ubicacionField, gbc);
        
        gbc.gridx = 0; gbc.gridy = ++row;
        panel.add(new JLabel("Conductor:"), gbc);
        gbc.gridx = 1;
        panel.add(conductorField, gbc);
        
        gbc.gridx = 0; gbc.gridy = ++row;
        panel.add(new JLabel("Notas:"), gbc);
        gbc.gridx = 1;
        panel.add(new JScrollPane(notasArea), gbc);
        
        // Botones
        JPanel buttonPanel = new JPanel(new FlowLayout());
        JButton saveButton = new JButton("Guardar");
        JButton cancelButton = new JButton("Cancelar");
        
        saveButton.addActionListener(e -> {
            if (marcaField.getText().trim().isEmpty() || modeloField.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(dialog, "La marca y el modelo son obligatorios");
                return;
            }
            
            try (Connection conn = DatabaseConnection.getConnection()) {
                String query = """
                    INSERT INTO vehiculos (marca, modelo, año, placa, tipo, estado, 
                                         kilometraje, ubicacion, conductor_asignado, notas)
                    VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
                    """;
                
                try (PreparedStatement pstmt = conn.prepareStatement(query)) {
                    pstmt.setString(1, marcaField.getText().trim());
                    pstmt.setString(2, modeloField.getText().trim());
                    pstmt.setInt(3, (Integer) añoSpinner.getValue());
                    pstmt.setString(4, placaField.getText().trim().isEmpty() ? null : placaField.getText().trim());
                    pstmt.setString(5, (String) tipoCombo.getSelectedItem());
                    pstmt.setString(6, (String) estadoCombo.getSelectedItem());
                    pstmt.setInt(7, (Integer) kilometrajeSpinner.getValue());
                    pstmt.setString(8, ubicacionField.getText().trim().isEmpty() ? null : ubicacionField.getText().trim());
                    pstmt.setString(9, conductorField.getText().trim().isEmpty() ? null : conductorField.getText().trim());
                    pstmt.setString(10, notasArea.getText().trim().isEmpty() ? null : notasArea.getText().trim());
                    
                    pstmt.executeUpdate();
                    
                    JOptionPane.showMessageDialog(dialog, "Vehículo agregado exitosamente");
                    dialog.dispose();
                    loadVehicles();
                }
            } catch (SQLException ex) {
                logger.log(Level.SEVERE, "Error agregando vehículo", ex);
                JOptionPane.showMessageDialog(dialog, "Error al agregar vehículo: " + ex.getMessage());
            }
        });
        
        cancelButton.addActionListener(e -> dialog.dispose());
        
        buttonPanel.add(saveButton);
        buttonPanel.add(cancelButton);
        
        gbc.gridx = 0; gbc.gridy = ++row; gbc.gridwidth = 2;
        panel.add(buttonPanel, gbc);
        
        dialog.add(panel);
        dialog.setVisible(true);
    }
    
    private void showVehicleDetails(int vehicleId) {
        try (Connection conn = DatabaseConnection.getConnection()) {
            String query = "SELECT * FROM vehiculos WHERE id = ?";
            
            try (PreparedStatement pstmt = conn.prepareStatement(query)) {
                pstmt.setInt(1, vehicleId);
                
                try (ResultSet rs = pstmt.executeQuery()) {
                    if (rs.next()) {
                        StringBuilder details = new StringBuilder();
                        details.append("DETALLES DEL VEHÍCULO\n");
                        details.append("══════════════════════\n\n");
                        details.append("ID: ").append(rs.getInt("id")).append("\n");
                        details.append("Marca: ").append(rs.getString("marca")).append("\n");
                        details.append("Modelo: ").append(rs.getString("modelo")).append("\n");
                        details.append("Año: ").append(rs.getInt("año")).append("\n");
                        details.append("Placa: ").append(rs.getString("placa")).append("\n");
                        details.append("Tipo: ").append(rs.getString("tipo")).append("\n");
                        details.append("Estado: ").append(rs.getString("estado")).append("\n");
                        details.append("Kilometraje: ").append(String.format("%,d km", rs.getInt("kilometraje"))).append("\n");
                        details.append("Ubicación: ").append(rs.getString("ubicacion")).append("\n");
                        details.append("Conductor Asignado: ").append(rs.getString("conductor_asignado")).append("\n");
                        details.append("Fecha Mantenimiento: ").append(rs.getDate("fecha_mantenimiento")).append("\n");
                        details.append("Fecha Registro: ").append(rs.getTimestamp("fecha_registro")).append("\n\n");
                        
                        if (rs.getString("notas") != null) {
                            details.append("NOTAS:\n");
                            details.append("──────\n");
                            details.append(rs.getString("notas"));
                        }
                        
                        JTextArea textArea = new JTextArea(details.toString());
                        textArea.setEditable(false);
                        textArea.setFont(new Font("Segoe UI", Font.PLAIN, 14));
                        textArea.setBackground(new Color(248, 249, 250));
                        textArea.setBorder(new EmptyBorder(10, 10, 10, 10));
                        
                        JScrollPane detailsScroll = new JScrollPane(textArea);
                        detailsScroll.setPreferredSize(new Dimension(500, 400));
                        
                        JOptionPane.showMessageDialog(this,
                            detailsScroll,
                            "Detalles del Vehículo",
                            JOptionPane.INFORMATION_MESSAGE);
                    }
                }
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error obteniendo detalles del vehículo", e);
            JOptionPane.showMessageDialog(this,
                "Error al obtener los detalles: " + e.getMessage(),
                "Error",
                JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void editVehicle(int vehicleId) {
        JOptionPane.showMessageDialog(this, "Función de edición en desarrollo", "Info", JOptionPane.INFORMATION_MESSAGE);
    }
    
    private void changeVehicleStatus(int vehicleId, String currentStatus) {
        String[] estados = {"Disponible", "En Servicio", "Mantenimiento", "Fuera de Servicio"};
        String nuevoEstado = (String) JOptionPane.showInputDialog(this,
            "Seleccione el nuevo estado para el vehículo:",
            "Cambiar Estado",
            JOptionPane.QUESTION_MESSAGE,
            null,
            estados,
            currentStatus);
        
        if (nuevoEstado != null && !nuevoEstado.equals(currentStatus)) {
            try (Connection conn = DatabaseConnection.getConnection()) {
                String updateQuery = "UPDATE vehiculos SET estado = ? WHERE id = ?";
                
                try (PreparedStatement pstmt = conn.prepareStatement(updateQuery)) {
                    pstmt.setString(1, nuevoEstado);
                    pstmt.setInt(2, vehicleId);
                    
                    int rowsUpdated = pstmt.executeUpdate();
                    if (rowsUpdated > 0) {
                        JOptionPane.showMessageDialog(this,
                            "Estado del vehículo actualizado exitosamente.",
                            "Actualización Exitosa",
                            JOptionPane.INFORMATION_MESSAGE);
                        loadVehicles();
                    }
                }
            } catch (SQLException e) {
                logger.log(Level.SEVERE, "Error actualizando estado del vehículo", e);
                JOptionPane.showMessageDialog(this,
                    "Error al actualizar el estado: " + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    private void deleteVehicle(int vehicleId, String vehicleName) {
        int confirmacion = JOptionPane.showConfirmDialog(this,
            "¿Está seguro de que desea eliminar el vehículo '" + vehicleName + "'?\n" +
            "Esta acción no se puede deshacer.",
            "Confirmar Eliminación",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.WARNING_MESSAGE);
        
        if (confirmacion == JOptionPane.YES_OPTION) {
            try (Connection conn = DatabaseConnection.getConnection()) {
                String deleteQuery = "DELETE FROM vehiculos WHERE id = ?";
                
                try (PreparedStatement pstmt = conn.prepareStatement(deleteQuery)) {
                    pstmt.setInt(1, vehicleId);
                    
                    int rowsDeleted = pstmt.executeUpdate();
                    if (rowsDeleted > 0) {
                        JOptionPane.showMessageDialog(this,
                            "Vehículo eliminado exitosamente.",
                            "Eliminación Exitosa",
                            JOptionPane.INFORMATION_MESSAGE);
                        loadVehicles();
                    }
                }
            } catch (SQLException e) {
                logger.log(Level.SEVERE, "Error eliminando vehículo", e);
                JOptionPane.showMessageDialog(this,
                    "Error al eliminar el vehículo: " + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}
