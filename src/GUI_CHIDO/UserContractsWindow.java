package GUI_CHIDO;

import Clases.BackgroundPanel;
import Clases.Cliente;
import Clases.DatabaseConnection;
import java.awt.*;
import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

/**
 * Ventana para que los usuarios vean y gestionen sus contratos activos
 * @author NetNexus Team
 */
public class UserContractsWindow extends JFrame {
    
    private static final Logger logger = Logger.getLogger(UserContractsWindow.class.getName());
    private Cliente currentUser;
    private JPanel contractsPanel;
    private JScrollPane scrollPane;
    
    public UserContractsWindow(Cliente usuario) {
        this.currentUser = usuario;
        initComponents();
        loadUserContracts();
    }
    
    private void initComponents() {
        setTitle("Mis Contratos Activos - NetNexus Ultra");
        setSize(900, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        
        // Panel principal con fondo
        BackgroundPanel mainPanel = new BackgroundPanel("/Imagenes/fondo.png");
        mainPanel.setLayout(new BorderLayout());
        
        // Panel de título
        JPanel titlePanel = new JPanel();
        titlePanel.setOpaque(false);
        titlePanel.setBorder(new EmptyBorder(20, 20, 10, 20));
        
        JLabel titleLabel = new JLabel("Mis Contratos Activos", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 28));
        titleLabel.setForeground(Color.WHITE);
        titlePanel.add(titleLabel);
        
        // Panel de información del usuario
        JPanel userInfoPanel = new JPanel();
        userInfoPanel.setOpaque(false);
        userInfoPanel.setBorder(new EmptyBorder(10, 20, 20, 20));
        
        JLabel userLabel = new JLabel("Usuario: " + currentUser.getNombre() + " " + currentUser.getApellido(), SwingConstants.CENTER);
        userLabel.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        userLabel.setForeground(new Color(200, 200, 200));
        userInfoPanel.add(userLabel);
        
        // Panel superior combinado
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setOpaque(false);
        topPanel.add(titlePanel, BorderLayout.NORTH);
        topPanel.add(userInfoPanel, BorderLayout.CENTER);
        
        // Panel de contratos con scroll
        contractsPanel = new JPanel();
        contractsPanel.setLayout(new BoxLayout(contractsPanel, BoxLayout.Y_AXIS));
        contractsPanel.setOpaque(false);
        contractsPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        
        scrollPane = new JScrollPane(contractsPanel);
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        
        // Panel de botones
        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.setOpaque(false);
        buttonPanel.setBorder(new EmptyBorder(10, 20, 20, 20));
        
        JButton refreshButton = createStyledButton("🔄 Actualizar", new Color(70, 130, 180));
        refreshButton.addActionListener(e -> loadUserContracts());
        
        JButton closeButton = createStyledButton("❌ Cerrar", new Color(220, 53, 69));
        closeButton.addActionListener(e -> dispose());
        
        buttonPanel.add(refreshButton);
        buttonPanel.add(Box.createHorizontalStrut(20));
        buttonPanel.add(closeButton);
        
        // Ensamblar panel principal
        mainPanel.add(topPanel, BorderLayout.NORTH);
        mainPanel.add(scrollPane, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);
        
        setContentPane(mainPanel);
    }
    
    private JButton createStyledButton(String text, Color baseColor) {
        JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI", Font.BOLD, 14));
        button.setBackground(baseColor);
        button.setForeground(Color.WHITE);
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setPreferredSize(new Dimension(150, 40));
        
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
    
    private void loadUserContracts() {
        contractsPanel.removeAll();
        
        try (Connection conn = DatabaseConnection.getConnection()) {
            String query = """
                SELECT c.id_contrato, c.plan_tipo, c.precio, c.fecha_inicio, c.fecha_fin, c.estado,
                       ts.nombre as tipo_servicio
                FROM contrato c
                LEFT JOIN tipo_servicio ts ON c.tipo_servicio_id = ts.id
                WHERE c.cliente_id = ? AND c.estado = 'Activo'
                ORDER BY c.fecha_inicio DESC
                """;
            
            try (PreparedStatement pstmt = conn.prepareStatement(query)) {
                pstmt.setInt(1, currentUser.getIdCliente());
                
                try (ResultSet rs = pstmt.executeQuery()) {
                    boolean hasContracts = false;
                    
                    while (rs.next()) {
                        hasContracts = true;
                        JPanel contractCard = createContractCard(
                            rs.getInt("id_contrato"),
                            rs.getString("plan_tipo"),
                            rs.getBigDecimal("precio").doubleValue(),
                            rs.getDate("fecha_inicio"),
                            rs.getDate("fecha_fin"),
                            rs.getString("estado"),
                            rs.getString("tipo_servicio")
                        );
                        contractsPanel.add(contractCard);
                        contractsPanel.add(Box.createVerticalStrut(10));
                    }
                    
                    if (!hasContracts) {
                        JLabel noContractsLabel = new JLabel("No tienes contratos activos en este momento", SwingConstants.CENTER);
                        noContractsLabel.setFont(new Font("Segoe UI", Font.ITALIC, 18));
                        noContractsLabel.setForeground(new Color(200, 200, 200));
                        noContractsLabel.setPreferredSize(new Dimension(800, 100));
                        contractsPanel.add(noContractsLabel);
                    }
                }
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error cargando contratos del usuario", e);
            JOptionPane.showMessageDialog(this, 
                "Error al cargar los contratos: " + e.getMessage(), 
                "Error de Base de Datos", 
                JOptionPane.ERROR_MESSAGE);
        }
        
        contractsPanel.revalidate();
        contractsPanel.repaint();
    }
    
    private JPanel createContractCard(int contractId, String planTipo, double precio, 
                                    Date fechaInicio, Date fechaFin, String estado, String tipoServicio) {
        JPanel card = new JPanel(new BorderLayout());
        card.setOpaque(true);
        card.setBackground(new Color(255, 255, 255, 220));
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(70, 130, 180), 2),
            new EmptyBorder(15, 20, 15, 20)
        ));
        card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 120));
        
        // Panel de información principal
        JPanel infoPanel = new JPanel(new GridBagLayout());
        infoPanel.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(2, 0, 2, 20);
        
        // Plan y precio
        gbc.gridx = 0; gbc.gridy = 0;
        JLabel planLabel = new JLabel("Plan: " + (planTipo != null ? planTipo : "N/A"));
        planLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        planLabel.setForeground(new Color(50, 70, 90));
        infoPanel.add(planLabel, gbc);
        
        gbc.gridx = 1;
        JLabel priceLabel = new JLabel("Precio: $" + String.format("%.2f", precio));
        priceLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        priceLabel.setForeground(new Color(40, 140, 40));
        infoPanel.add(priceLabel, gbc);
        
        // Tipo de servicio y estado
        gbc.gridx = 0; gbc.gridy = 1;
        JLabel serviceLabel = new JLabel("Servicio: " + (tipoServicio != null ? tipoServicio : "General"));
        serviceLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        serviceLabel.setForeground(new Color(70, 90, 110));
        infoPanel.add(serviceLabel, gbc);
        
        gbc.gridx = 1;
        JLabel statusLabel = new JLabel("Estado: " + estado);
        statusLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        statusLabel.setForeground(new Color(40, 140, 40));
        infoPanel.add(statusLabel, gbc);
        
        // Fechas
        gbc.gridx = 0; gbc.gridy = 2; gbc.gridwidth = 2;
        String fechasText = String.format("Vigencia: %s - %s", 
            fechaInicio != null ? fechaInicio.toString() : "N/A",
            fechaFin != null ? fechaFin.toString() : "N/A");
        JLabel datesLabel = new JLabel(fechasText);
        datesLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        datesLabel.setForeground(new Color(100, 120, 140));
        infoPanel.add(datesLabel, gbc);
        
        // Panel de botones
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.setOpaque(false);
        
        JButton cancelButton = createStyledButton("🗑️ Cancelar", new Color(220, 53, 69));
        cancelButton.setPreferredSize(new Dimension(120, 35));
        cancelButton.addActionListener(e -> cancelContract(contractId, planTipo));
        
        JButton detailsButton = createStyledButton("📋 Detalles", new Color(40, 167, 69));
        detailsButton.setPreferredSize(new Dimension(120, 35));
        detailsButton.addActionListener(e -> showContractDetails(contractId));
        
        buttonPanel.add(detailsButton);
        buttonPanel.add(Box.createHorizontalStrut(10));
        buttonPanel.add(cancelButton);
        
        card.add(infoPanel, BorderLayout.CENTER);
        card.add(buttonPanel, BorderLayout.EAST);
        
        return card;
    }
    
    private void cancelContract(int contractId, String planTipo) {
        int confirmacion = JOptionPane.showConfirmDialog(this,
            "¿Está seguro de que desea cancelar el contrato '" + planTipo + "'?\n" +
            "Esta acción no se puede deshacer.",
            "Confirmar Cancelación",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.WARNING_MESSAGE);
        
        if (confirmacion == JOptionPane.YES_OPTION) {
            try (Connection conn = DatabaseConnection.getConnection()) {
                String updateQuery = "UPDATE contrato SET estado = 'Cancelado' WHERE id_contrato = ?";
                
                try (PreparedStatement pstmt = conn.prepareStatement(updateQuery)) {
                    pstmt.setInt(1, contractId);
                    int rowsUpdated = pstmt.executeUpdate();
                    
                    if (rowsUpdated > 0) {
                        JOptionPane.showMessageDialog(this,
                            "El contrato ha sido cancelado exitosamente.",
                            "Cancelación Exitosa",
                            JOptionPane.INFORMATION_MESSAGE);
                        loadUserContracts(); // Recargar la lista
                    } else {
                        JOptionPane.showMessageDialog(this,
                            "No se pudo cancelar el contrato. Intente nuevamente.",
                            "Error",
                            JOptionPane.ERROR_MESSAGE);
                    }
                }
            } catch (SQLException e) {
                logger.log(Level.SEVERE, "Error cancelando contrato", e);
                JOptionPane.showMessageDialog(this,
                    "Error al cancelar el contrato: " + e.getMessage(),
                    "Error de Base de Datos",
                    JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    private void showContractDetails(int contractId) {
        try (Connection conn = DatabaseConnection.getConnection()) {
            String query = """
                SELECT c.*, ts.nombre as tipo_servicio, ts.descripcion
                FROM contrato c
                LEFT JOIN tipo_servicio ts ON c.tipo_servicio_id = ts.id
                WHERE c.id_contrato = ?
                """;
            
            try (PreparedStatement pstmt = conn.prepareStatement(query)) {
                pstmt.setInt(1, contractId);
                
                try (ResultSet rs = pstmt.executeQuery()) {
                    if (rs.next()) {
                        StringBuilder details = new StringBuilder();
                        details.append("DETALLES DEL CONTRATO\n");
                        details.append("═══════════════════════\n\n");
                        details.append("ID Contrato: ").append(rs.getInt("id_contrato")).append("\n");
                        details.append("Plan: ").append(rs.getString("plan_tipo")).append("\n");
                        details.append("Precio: $").append(rs.getBigDecimal("precio")).append("\n");
                        details.append("Tipo de Servicio: ").append(rs.getString("tipo_servicio")).append("\n");
                        details.append("Descripción: ").append(rs.getString("descripcion")).append("\n");
                        details.append("Fecha de Inicio: ").append(rs.getDate("fecha_inicio")).append("\n");
                        details.append("Fecha de Fin: ").append(rs.getDate("fecha_fin")).append("\n");
                        details.append("Estado: ").append(rs.getString("estado")).append("\n");
                        
                        JTextArea textArea = new JTextArea(details.toString());
                        textArea.setEditable(false);
                        textArea.setFont(new Font("Segoe UI", Font.PLAIN, 14));
                        textArea.setBackground(new Color(248, 249, 250));
                        textArea.setBorder(new EmptyBorder(10, 10, 10, 10));
                        
                        JScrollPane detailsScroll = new JScrollPane(textArea);
                        detailsScroll.setPreferredSize(new Dimension(400, 300));
                        
                        JOptionPane.showMessageDialog(this,
                            detailsScroll,
                            "Detalles del Contrato",
                            JOptionPane.INFORMATION_MESSAGE);
                    }
                }
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error obteniendo detalles del contrato", e);
            JOptionPane.showMessageDialog(this,
                "Error al obtener los detalles: " + e.getMessage(),
                "Error",
                JOptionPane.ERROR_MESSAGE);
        }
    }
}
