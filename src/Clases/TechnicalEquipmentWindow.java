package Clases;

import java.awt.*;
import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

/**
 * Ventana modernizada para gestión de equipos técnicos
 * @author NetNexus Team
 */
public class TechnicalEquipmentWindow extends JFrame {
    
    private static final Logger logger = Logger.getLogger(TechnicalEquipmentWindow.class.getName());
    private JPanel equipmentPanel;
    private JScrollPane scrollPane;
    
    public TechnicalEquipmentWindow() {
        initComponents();
        loadEquipment();
    }
    
    private void initComponents() {
        setTitle("Gestión de Equipos Técnicos - NetNexus Ultra");
        setSize(1100, 750);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        
        // Panel principal con fondo
        BackgroundPanel mainPanel = new BackgroundPanel("/Imagenes/fondo.png");
        mainPanel.setLayout(new BorderLayout());
        
        // Panel de título
        JPanel titlePanel = new JPanel();
        titlePanel.setOpaque(false);
        titlePanel.setBorder(new EmptyBorder(20, 20, 10, 20));
        
        JLabel titleLabel = new JLabel("🔧 Gestión de Equipos Técnicos", SwingConstants.CENTER);
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
        
        JComboBox<String> statusFilter = new JComboBox<>(new String[]{"Todos", "Libre", "Ocupado", "En Mantenimiento", "Fuera de Servicio"});
        statusFilter.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        statusFilter.setPreferredSize(new Dimension(180, 30));
        statusFilter.addActionListener(e -> loadEquipment((String) statusFilter.getSelectedItem()));
        
        JButton addButton = createStyledButton("➕ Agregar Equipo", new Color(40, 167, 69));
        addButton.addActionListener(e -> addNewEquipment());
        
        JButton refreshButton = createStyledButton("🔄 Actualizar", new Color(70, 130, 180));
        refreshButton.addActionListener(e -> {
            loadEquipment((String) statusFilter.getSelectedItem());
            updateStatsPanel(statsPanel);
        });
        
        filterPanel.add(filterLabel);
        filterPanel.add(Box.createHorizontalStrut(10));
        filterPanel.add(statusFilter);
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
        
        // Panel de equipos con scroll
        equipmentPanel = new JPanel();
        equipmentPanel.setLayout(new BoxLayout(equipmentPanel, BoxLayout.Y_AXIS));
        equipmentPanel.setOpaque(false);
        equipmentPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        
        scrollPane = new JScrollPane(equipmentPanel);
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
            // Contar equipos por estado
            String query = "SELECT estado, COUNT(*) as count FROM equipos GROUP BY estado";
            
            try (PreparedStatement pstmt = conn.prepareStatement(query);
                 ResultSet rs = pstmt.executeQuery()) {
                
                int total = 0;
                while (rs.next()) {
                    String estado = rs.getString("estado");
                    int count = rs.getInt("count");
                    total += count;
                    
                    Color color = getStatusColor(estado);
                    JPanel statCard = createStatCard(estado, count, color);
                    statsPanel.add(statCard);
                    statsPanel.add(Box.createHorizontalStrut(15));
                }
                
                // Card de total
                JPanel totalCard = createStatCard("Total", total, new Color(108, 117, 125));
                statsPanel.add(totalCard);
            }
        } catch (SQLException e) {
            logger.log(Level.WARNING, "Error obteniendo estadísticas de equipos", e);
        }
        
        statsPanel.revalidate();
        statsPanel.repaint();
    }
    
    private JPanel createStatCard(String label, int count, Color color) {
        JPanel card = new JPanel(new BorderLayout());
        card.setOpaque(true);
        card.setBackground(new Color(255, 255, 255, 200));
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(color, 2),
            new EmptyBorder(8, 15, 8, 15)
        ));
        card.setPreferredSize(new Dimension(120, 60));
        
        JLabel countLabel = new JLabel(String.valueOf(count), SwingConstants.CENTER);
        countLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
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
        button.setPreferredSize(new Dimension(160, 35));
        
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
    
    private void loadEquipment() {
        loadEquipment("Todos");
    }
    
    private void loadEquipment(String statusFilter) {
        equipmentPanel.removeAll();
        
        try (Connection conn = DatabaseConnection.getConnection()) {
            StringBuilder queryBuilder = new StringBuilder("""
                SELECT e.idEquipos, e.nombre, e.Servicios_idServicios,
                       'Disponible' as estado, 'Técnico' as tipo, 
                       'N/A' as modelo, 'N/A' as numero_serie,
                       CURRENT_DATE as fecha_adquisicion, 'Oficina Central' as ubicacion, 
                       'Sin asignar' as responsable
                FROM equipos e
                """);
            
            queryBuilder.append(" ORDER BY e.nombre");
            
            try (PreparedStatement pstmt = conn.prepareStatement(queryBuilder.toString())) {
                
                try (ResultSet rs = pstmt.executeQuery()) {
                    boolean hasEquipment = false;
                    
                    while (rs.next()) {
                        hasEquipment = true;
                        JPanel equipmentCard = createEquipmentCard(
                            rs.getInt("idEquipos"),
                            rs.getString("nombre"),
                            rs.getString("tipo"),
                            rs.getString("modelo"),
                            rs.getString("numero_serie"),
                            rs.getString("estado"),
                            rs.getDate("fecha_adquisicion"),
                            rs.getString("ubicacion"),
                            rs.getString("responsable"),
                            "Sin asignar", // tecnico_nombre
                            "" // tecnico_apellido
                        );
                        equipmentPanel.add(equipmentCard);
                        equipmentPanel.add(Box.createVerticalStrut(10));
                    }
                    
                    if (!hasEquipment) {
                        JLabel noEquipmentLabel = new JLabel("No hay equipos para mostrar", SwingConstants.CENTER);
                        noEquipmentLabel.setFont(new Font("Segoe UI", Font.ITALIC, 18));
                        noEquipmentLabel.setForeground(new Color(200, 200, 200));
                        noEquipmentLabel.setPreferredSize(new Dimension(1000, 100));
                        equipmentPanel.add(noEquipmentLabel);
                    }
                }
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error cargando equipos", e);
            JOptionPane.showMessageDialog(this, 
                "Error al cargar los equipos: " + e.getMessage(), 
                "Error de Base de Datos", 
                JOptionPane.ERROR_MESSAGE);
        }
        
        equipmentPanel.revalidate();
        equipmentPanel.repaint();
    }
    
    private JPanel createEquipmentCard(int id, String nombre, String tipo, String modelo, 
                                     String numeroSerie, String estado, Date fechaAdquisicion,
                                     String ubicacion, String responsable, String tecnicoNombre, 
                                     String tecnicoApellido) {
        JPanel card = new JPanel(new BorderLayout());
        card.setOpaque(true);
        card.setBackground(new Color(255, 255, 255, 240));
        
        Color statusColor = getStatusColor(estado);
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(statusColor, 3),
            new EmptyBorder(15, 20, 15, 20)
        ));
        card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 150));
        
        // Panel de información principal
        JPanel infoPanel = new JPanel(new GridBagLayout());
        infoPanel.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(3, 0, 3, 20);
        
        // Nombre y tipo
        gbc.gridx = 0; gbc.gridy = 0;
        JLabel nameLabel = new JLabel("🔧 " + (nombre != null ? nombre : "Sin nombre"));
        nameLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        nameLabel.setForeground(new Color(50, 70, 90));
        infoPanel.add(nameLabel, gbc);
        
        gbc.gridx = 1;
        JLabel typeLabel = new JLabel("Tipo: " + (tipo != null ? tipo : "N/A"));
        typeLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        typeLabel.setForeground(new Color(70, 90, 110));
        infoPanel.add(typeLabel, gbc);
        
        gbc.gridx = 2;
        JLabel statusLabel = new JLabel("● " + estado);
        statusLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        statusLabel.setForeground(statusColor);
        infoPanel.add(statusLabel, gbc);
        
        // Modelo y serie
        gbc.gridx = 0; gbc.gridy = 1;
        JLabel modelLabel = new JLabel("Modelo: " + (modelo != null ? modelo : "N/A"));
        modelLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        modelLabel.setForeground(new Color(70, 90, 110));
        infoPanel.add(modelLabel, gbc);
        
        gbc.gridx = 1;
        JLabel serialLabel = new JLabel("S/N: " + (numeroSerie != null ? numeroSerie : "N/A"));
        serialLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        serialLabel.setForeground(new Color(70, 90, 110));
        infoPanel.add(serialLabel, gbc);
        
        gbc.gridx = 2;
        JLabel dateLabel = new JLabel("Adquirido: " + (fechaAdquisicion != null ? fechaAdquisicion.toString() : "N/A"));
        dateLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        dateLabel.setForeground(new Color(100, 120, 140));
        infoPanel.add(dateLabel, gbc);
        
        // Ubicación y responsable
        gbc.gridx = 0; gbc.gridy = 2;
        JLabel locationLabel = new JLabel("📍 " + (ubicacion != null ? ubicacion : "Sin ubicación"));
        locationLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        locationLabel.setForeground(new Color(70, 90, 110));
        infoPanel.add(locationLabel, gbc);
        
        gbc.gridx = 1; gbc.gridwidth = 2;
        String tecnicoInfo = "";
        if (tecnicoNombre != null && tecnicoApellido != null) {
            tecnicoInfo = "👤 Técnico: " + tecnicoNombre + " " + tecnicoApellido;
        } else if (responsable != null) {
            tecnicoInfo = "👤 Responsable: " + responsable;
        } else {
            tecnicoInfo = "👤 Sin asignar";
        }
        JLabel tecnicoLabel = new JLabel(tecnicoInfo);
        tecnicoLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        tecnicoLabel.setForeground(new Color(70, 90, 110));
        infoPanel.add(tecnicoLabel, gbc);
        
        // Panel de botones
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.setOpaque(false);
        
        JButton editButton = createStyledButton("✏️ Editar", new Color(255, 193, 7));
        editButton.setPreferredSize(new Dimension(100, 30));
        editButton.addActionListener(e -> editEquipment(id));
        
        JButton statusButton = createStyledButton("🔄 Estado", new Color(70, 130, 180));
        statusButton.setPreferredSize(new Dimension(100, 30));
        statusButton.addActionListener(e -> changeEquipmentStatus(id, estado));
        
        JButton deleteButton = createStyledButton("🗑️ Eliminar", new Color(220, 53, 69));
        deleteButton.setPreferredSize(new Dimension(110, 30));
        deleteButton.addActionListener(e -> deleteEquipment(id, nombre));
        
        buttonPanel.add(editButton);
        buttonPanel.add(Box.createHorizontalStrut(5));
        buttonPanel.add(statusButton);
        buttonPanel.add(Box.createHorizontalStrut(5));
        buttonPanel.add(deleteButton);
        
        card.add(infoPanel, BorderLayout.CENTER);
        card.add(buttonPanel, BorderLayout.EAST);
        
        return card;
    }
    
    private Color getStatusColor(String estado) {
        return switch (estado != null ? estado : "") {
            case "Libre" -> new Color(40, 167, 69);
            case "Ocupado" -> new Color(220, 53, 69);
            case "En Mantenimiento" -> new Color(255, 193, 7);
            case "Fuera de Servicio" -> new Color(108, 117, 125);
            default -> new Color(70, 130, 180);
        };
    }
    
    private void addNewEquipment() {
        JDialog dialog = new JDialog(this, "Agregar Nuevo Equipo", true);
        dialog.setSize(400, 500);
        dialog.setLocationRelativeTo(this);
        
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(new EmptyBorder(20, 20, 20, 20));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;
        
        // Campos del formulario
        JTextField nameField = new JTextField(20);
        JTextField typeField = new JTextField(20);
        JTextField modelField = new JTextField(20);
        JTextField serialField = new JTextField(20);
        JTextField locationField = new JTextField(20);
        JTextField responsibleField = new JTextField(20);
        JComboBox<String> statusCombo = new JComboBox<>(new String[]{"Libre", "Ocupado", "En Mantenimiento", "Fuera de Servicio"});
        
        // Agregar campos al panel
        gbc.gridx = 0; gbc.gridy = 0;
        panel.add(new JLabel("Nombre:"), gbc);
        gbc.gridx = 1;
        panel.add(nameField, gbc);
        
        gbc.gridx = 0; gbc.gridy = 1;
        panel.add(new JLabel("Tipo:"), gbc);
        gbc.gridx = 1;
        panel.add(typeField, gbc);
        
        gbc.gridx = 0; gbc.gridy = 2;
        panel.add(new JLabel("Modelo:"), gbc);
        gbc.gridx = 1;
        panel.add(modelField, gbc);
        
        gbc.gridx = 0; gbc.gridy = 3;
        panel.add(new JLabel("Número de Serie:"), gbc);
        gbc.gridx = 1;
        panel.add(serialField, gbc);
        
        gbc.gridx = 0; gbc.gridy = 4;
        panel.add(new JLabel("Ubicación:"), gbc);
        gbc.gridx = 1;
        panel.add(locationField, gbc);
        
        gbc.gridx = 0; gbc.gridy = 5;
        panel.add(new JLabel("Responsable:"), gbc);
        gbc.gridx = 1;
        panel.add(responsibleField, gbc);
        
        gbc.gridx = 0; gbc.gridy = 6;
        panel.add(new JLabel("Estado:"), gbc);
        gbc.gridx = 1;
        panel.add(statusCombo, gbc);
        
        // Botones
        JPanel buttonPanel = new JPanel(new FlowLayout());
        JButton saveButton = new JButton("Guardar");
        JButton cancelButton = new JButton("Cancelar");
        
        saveButton.addActionListener(e -> {
            if (nameField.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(dialog, "El nombre es obligatorio");
                return;
            }
            
            try (Connection conn = DatabaseConnection.getConnection()) {
                String query = """
                    INSERT INTO equipos (nombre, tipo, modelo, numero_serie, estado, 
                                       fecha_adquisicion, ubicacion, responsable)
                    VALUES (?, ?, ?, ?, ?, CURDATE(), ?, ?)
                    """;
                
                try (PreparedStatement pstmt = conn.prepareStatement(query)) {
                    pstmt.setString(1, nameField.getText().trim());
                    pstmt.setString(2, typeField.getText().trim());
                    pstmt.setString(3, modelField.getText().trim());
                    pstmt.setString(4, serialField.getText().trim());
                    pstmt.setString(5, (String) statusCombo.getSelectedItem());
                    pstmt.setString(6, locationField.getText().trim());
                    pstmt.setString(7, responsibleField.getText().trim());
                    
                    pstmt.executeUpdate();
                    
                    JOptionPane.showMessageDialog(dialog, "Equipo agregado exitosamente");
                    dialog.dispose();
                    loadEquipment();
                }
            } catch (SQLException ex) {
                logger.log(Level.SEVERE, "Error agregando equipo", ex);
                JOptionPane.showMessageDialog(dialog, "Error al agregar equipo: " + ex.getMessage());
            }
        });
        
        cancelButton.addActionListener(e -> dialog.dispose());
        
        buttonPanel.add(saveButton);
        buttonPanel.add(cancelButton);
        
        gbc.gridx = 0; gbc.gridy = 7; gbc.gridwidth = 2;
        panel.add(buttonPanel, gbc);
        
        dialog.add(panel);
        dialog.setVisible(true);
    }
    
    private void editEquipment(int equipmentId) {
        // Implementación similar a addNewEquipment pero para editar
        JOptionPane.showMessageDialog(this, "Función de edición en desarrollo", "Info", JOptionPane.INFORMATION_MESSAGE);
    }
    
    private void changeEquipmentStatus(int equipmentId, String currentStatus) {
        String[] estados = {"Libre", "Ocupado", "En Mantenimiento", "Fuera de Servicio"};
        String nuevoEstado = (String) JOptionPane.showInputDialog(this,
            "Seleccione el nuevo estado para el equipo:",
            "Cambiar Estado",
            JOptionPane.QUESTION_MESSAGE,
            null,
            estados,
            currentStatus);
        
        if (nuevoEstado != null && !nuevoEstado.equals(currentStatus)) {
            try (Connection conn = DatabaseConnection.getConnection()) {
                String updateQuery = "UPDATE equipos SET estado = ? WHERE id = ?";
                
                try (PreparedStatement pstmt = conn.prepareStatement(updateQuery)) {
                    pstmt.setString(1, nuevoEstado);
                    pstmt.setInt(2, equipmentId);
                    
                    int rowsUpdated = pstmt.executeUpdate();
                    if (rowsUpdated > 0) {
                        JOptionPane.showMessageDialog(this,
                            "Estado del equipo actualizado exitosamente.",
                            "Actualización Exitosa",
                            JOptionPane.INFORMATION_MESSAGE);
                        loadEquipment();
                    }
                }
            } catch (SQLException e) {
                logger.log(Level.SEVERE, "Error actualizando estado del equipo", e);
                JOptionPane.showMessageDialog(this,
                    "Error al actualizar el estado: " + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    private void deleteEquipment(int equipmentId, String equipmentName) {
        int confirmacion = JOptionPane.showConfirmDialog(this,
            "¿Está seguro de que desea eliminar el equipo '" + equipmentName + "'?\n" +
            "Esta acción no se puede deshacer.",
            "Confirmar Eliminación",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.WARNING_MESSAGE);
        
        if (confirmacion == JOptionPane.YES_OPTION) {
            try (Connection conn = DatabaseConnection.getConnection()) {
                String deleteQuery = "DELETE FROM equipos WHERE id = ?";
                
                try (PreparedStatement pstmt = conn.prepareStatement(deleteQuery)) {
                    pstmt.setInt(1, equipmentId);
                    
                    int rowsDeleted = pstmt.executeUpdate();
                    if (rowsDeleted > 0) {
                        JOptionPane.showMessageDialog(this,
                            "Equipo eliminado exitosamente.",
                            "Eliminación Exitosa",
                            JOptionPane.INFORMATION_MESSAGE);
                        loadEquipment();
                    }
                }
            } catch (SQLException e) {
                logger.log(Level.SEVERE, "Error eliminando equipo", e);
                JOptionPane.showMessageDialog(this,
                    "Error al eliminar el equipo: " + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}
