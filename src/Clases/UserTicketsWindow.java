package Clases;

import java.awt.*;
import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

/**
 * Ventana para visualizar tickets de usuarios en el panel de administración
 * @author NetNexus Team
 */
public class UserTicketsWindow extends JFrame {
    
    private static final Logger logger = Logger.getLogger(UserTicketsWindow.class.getName());
    private JPanel ticketsPanel;
    private JScrollPane scrollPane;
    
    public UserTicketsWindow() {
        initComponents();
        loadUserTickets();
    }
    
    private void initComponents() {
        setTitle("Tickets de Usuarios - NetNexus Ultra");
        setSize(1000, 700);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        
        // Panel principal con fondo
        BackgroundPanel mainPanel = new BackgroundPanel("/Imagenes/fondo.png");
        mainPanel.setLayout(new BorderLayout());
        
        // Panel de título
        JPanel titlePanel = new JPanel();
        titlePanel.setOpaque(false);
        titlePanel.setBorder(new EmptyBorder(20, 20, 10, 20));
        
        JLabel titleLabel = new JLabel("Gestión de Tickets de Usuarios", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 30));
        titleLabel.setForeground(Color.WHITE);
        titlePanel.add(titleLabel);
        
        // Panel de filtros
        JPanel filterPanel = new JPanel(new FlowLayout());
        filterPanel.setOpaque(false);
        filterPanel.setBorder(new EmptyBorder(10, 20, 15, 20));
        
        JLabel filterLabel = new JLabel("Filtrar por estado:");
        filterLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        filterLabel.setForeground(Color.WHITE);
        
        JComboBox<String> statusFilter = new JComboBox<>(new String[]{"Todos", "Pendiente", "En Proceso", "Resuelto", "Cerrado"});
        statusFilter.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        statusFilter.setPreferredSize(new Dimension(150, 30));
        statusFilter.addActionListener(e -> loadUserTickets((String) statusFilter.getSelectedItem()));
        
        JButton refreshButton = createStyledButton("🔄 Actualizar", new Color(70, 130, 180));
        refreshButton.addActionListener(e -> loadUserTickets((String) statusFilter.getSelectedItem()));
        
        filterPanel.add(filterLabel);
        filterPanel.add(Box.createHorizontalStrut(10));
        filterPanel.add(statusFilter);
        filterPanel.add(Box.createHorizontalStrut(20));
        filterPanel.add(refreshButton);
        
        // Panel superior
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setOpaque(false);
        topPanel.add(titlePanel, BorderLayout.NORTH);
        topPanel.add(filterPanel, BorderLayout.CENTER);
        
        // Panel de tickets con scroll
        ticketsPanel = new JPanel();
        ticketsPanel.setLayout(new BoxLayout(ticketsPanel, BoxLayout.Y_AXIS));
        ticketsPanel.setOpaque(false);
        ticketsPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        
        scrollPane = new JScrollPane(ticketsPanel);
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
    
    private JButton createStyledButton(String text, Color baseColor) {
        JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI", Font.BOLD, 14));
        button.setBackground(baseColor);
        button.setForeground(Color.WHITE);
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setPreferredSize(new Dimension(130, 35));
        
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
    
    private void loadUserTickets() {
        loadUserTickets("Todos");
    }
    
    private void loadUserTickets(String statusFilter) {
        ticketsPanel.removeAll();
        
        try (Connection conn = DatabaseConnection.getConnection()) {
            StringBuilder queryBuilder = new StringBuilder("""
                SELECT t.idTicket, t.Cliente_idCliente, t.descripcion, t.estado,
                       t.fecha_creacion, t.fecha_cierre,
                       c.nombre, c.apellido, c.email, c.telefono
                FROM ticket t
                LEFT JOIN cliente c ON t.Cliente_idCliente = c.idCliente
                """);
            
            if (!"Todos".equals(statusFilter)) {
                queryBuilder.append(" WHERE t.estado = ?");
            }
            queryBuilder.append(" ORDER BY t.fecha_creacion DESC");
            
            try (PreparedStatement pstmt = conn.prepareStatement(queryBuilder.toString())) {
                if (!"Todos".equals(statusFilter)) {
                    pstmt.setString(1, statusFilter);
                }
                
                try (ResultSet rs = pstmt.executeQuery()) {
                    boolean hasTickets = false;
                    
                    while (rs.next()) {
                        hasTickets = true;
                        JPanel ticketCard = createTicketCard(
                            rs.getInt("idTicket"),
                            rs.getInt("Cliente_idCliente"),
                            rs.getString("nombre"),
                            rs.getString("apellido"),
                            rs.getString("email"),
                            rs.getString("telefono"),
                            "Ticket #" + rs.getInt("idTicket"), // Asunto generado
                            rs.getString("descripcion"),
                            rs.getString("estado"),
                            "Normal", // Prioridad por defecto
                            rs.getTimestamp("fecha_creacion"),
                            rs.getTimestamp("fecha_cierre")
                        );
                        ticketsPanel.add(ticketCard);
                        ticketsPanel.add(Box.createVerticalStrut(10));
                    }
                    
                    if (!hasTickets) {
                        JLabel noTicketsLabel = new JLabel("No hay tickets para mostrar", SwingConstants.CENTER);
                        noTicketsLabel.setFont(new Font("Segoe UI", Font.ITALIC, 18));
                        noTicketsLabel.setForeground(new Color(200, 200, 200));
                        noTicketsLabel.setPreferredSize(new Dimension(900, 100));
                        ticketsPanel.add(noTicketsLabel);
                    }
                }
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error cargando tickets de usuarios", e);
            JOptionPane.showMessageDialog(this, 
                "Error al cargar los tickets: " + e.getMessage(), 
                "Error de Base de Datos", 
                JOptionPane.ERROR_MESSAGE);
        }
        
        ticketsPanel.revalidate();
        ticketsPanel.repaint();
    }
    
    private JPanel createTicketCard(int ticketId, int clienteId, String nombre, String apellido, 
                                  String email, String telefono, String asunto, String descripcion,
                                  String estado, String prioridad, Timestamp fechaCreacion, 
                                  Timestamp fechaActualizacion) {
        JPanel card = new JPanel(new BorderLayout());
        card.setOpaque(true);
        card.setBackground(new Color(255, 255, 255, 230));
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(getPriorityColor(prioridad), 3),
            new EmptyBorder(15, 20, 15, 20)
        ));
        card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 180));
        
        // Panel de información principal
        JPanel infoPanel = new JPanel(new GridBagLayout());
        infoPanel.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(3, 0, 3, 15);
        
        // Ticket ID y estado
        gbc.gridx = 0; gbc.gridy = 0;
        JLabel idLabel = new JLabel("Ticket #" + ticketId);
        idLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        idLabel.setForeground(new Color(50, 70, 90));
        infoPanel.add(idLabel, gbc);
        
        gbc.gridx = 1;
        JLabel statusLabel = new JLabel("Estado: " + estado);
        statusLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        statusLabel.setForeground(getStatusColor(estado));
        infoPanel.add(statusLabel, gbc);
        
        gbc.gridx = 2;
        JLabel priorityLabel = new JLabel("Prioridad: " + prioridad);
        priorityLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        priorityLabel.setForeground(getPriorityColor(prioridad));
        infoPanel.add(priorityLabel, gbc);
        
        // Cliente
        gbc.gridx = 0; gbc.gridy = 1; gbc.gridwidth = 2;
        String clienteInfo = String.format("Cliente: %s %s (ID: %d)", 
            nombre != null ? nombre : "N/A", 
            apellido != null ? apellido : "N/A", 
            clienteId);
        JLabel clienteLabel = new JLabel(clienteInfo);
        clienteLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        clienteLabel.setForeground(new Color(70, 90, 110));
        infoPanel.add(clienteLabel, gbc);
        
        gbc.gridx = 2; gbc.gridwidth = 1;
        JLabel emailLabel = new JLabel("📧 " + (email != null ? email : "N/A"));
        emailLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        emailLabel.setForeground(new Color(100, 120, 140));
        infoPanel.add(emailLabel, gbc);
        
        // Asunto
        gbc.gridx = 0; gbc.gridy = 2; gbc.gridwidth = 3;
        JLabel asuntoLabel = new JLabel("Asunto: " + (asunto != null ? asunto : "Sin asunto"));
        asuntoLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        asuntoLabel.setForeground(new Color(50, 70, 90));
        infoPanel.add(asuntoLabel, gbc);
        
        // Descripción (truncada)
        gbc.gridy = 3;
        String descripcionTruncada = descripcion != null && descripcion.length() > 80 
            ? descripcion.substring(0, 80) + "..." 
            : descripcion;
        JLabel descripcionLabel = new JLabel("Descripción: " + (descripcionTruncada != null ? descripcionTruncada : "Sin descripción"));
        descripcionLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        descripcionLabel.setForeground(new Color(100, 120, 140));
        infoPanel.add(descripcionLabel, gbc);
        
        // Fechas
        gbc.gridy = 4;
        String fechasText = String.format("Creado: %s | Actualizado: %s", 
            fechaCreacion != null ? fechaCreacion.toString().substring(0, 19) : "N/A",
            fechaActualizacion != null ? fechaActualizacion.toString().substring(0, 19) : "N/A");
        JLabel fechasLabel = new JLabel(fechasText);
        fechasLabel.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        fechasLabel.setForeground(new Color(120, 140, 160));
        infoPanel.add(fechasLabel, gbc);
        
        // Panel de botones
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.setOpaque(false);
        
        JButton viewButton = createStyledButton("👁️ Ver", new Color(40, 167, 69));
        viewButton.setPreferredSize(new Dimension(90, 30));
        viewButton.addActionListener(e -> viewTicketDetails(ticketId));
        
        JButton updateButton = createStyledButton("✏️ Estado", new Color(255, 193, 7));
        updateButton.setPreferredSize(new Dimension(100, 30));
        updateButton.addActionListener(e -> updateTicketStatus(ticketId, estado));
        
        JButton deleteButton = createStyledButton("🗑️ Eliminar", new Color(220, 53, 69));
        deleteButton.setPreferredSize(new Dimension(110, 30));
        deleteButton.addActionListener(e -> deleteTicket(ticketId));
        
        buttonPanel.add(viewButton);
        buttonPanel.add(Box.createHorizontalStrut(5));
        buttonPanel.add(updateButton);
        buttonPanel.add(Box.createHorizontalStrut(5));
        buttonPanel.add(deleteButton);
        
        card.add(infoPanel, BorderLayout.CENTER);
        card.add(buttonPanel, BorderLayout.EAST);
        
        return card;
    }
    
    private Color getStatusColor(String estado) {
        return switch (estado != null ? estado : "") {
            case "Pendiente" -> new Color(255, 193, 7);
            case "En Proceso" -> new Color(70, 130, 180);
            case "Resuelto" -> new Color(40, 167, 69);
            case "Cerrado" -> new Color(108, 117, 125);
            default -> new Color(100, 120, 140);
        };
    }
    
    private Color getPriorityColor(String prioridad) {
        return switch (prioridad != null ? prioridad : "") {
            case "Alta" -> new Color(220, 53, 69);
            case "Media" -> new Color(255, 193, 7);
            case "Baja" -> new Color(40, 167, 69);
            default -> new Color(108, 117, 125);
        };
    }
    
    private void viewTicketDetails(int ticketId) {
        try (Connection conn = DatabaseConnection.getConnection()) {
            String query = """
                SELECT t.*, c.nombre, c.apellido, c.email, c.telefono
                FROM ticket t
                LEFT JOIN cliente c ON t.Cliente_idCliente = c.idCliente
                WHERE t.idTicket = ?
                """;
            
            try (PreparedStatement pstmt = conn.prepareStatement(query)) {
                pstmt.setInt(1, ticketId);
                
                try (ResultSet rs = pstmt.executeQuery()) {
                    if (rs.next()) {
                        StringBuilder details = new StringBuilder();
                        details.append("DETALLES DEL TICKET\n");
                        details.append("══════════════════════\n\n");
                        details.append("ID: ").append(rs.getInt("idTicket")).append("\n");
                        details.append("Cliente: ").append(rs.getString("nombre")).append(" ").append(rs.getString("apellido")).append("\n");
                        details.append("Email: ").append(rs.getString("email")).append("\n");
                        details.append("Teléfono: ").append(rs.getString("telefono")).append("\n");
                        details.append("Estado: ").append(rs.getString("estado")).append("\n");
                        details.append("Fecha Creación: ").append(rs.getTimestamp("fecha_creacion")).append("\n");
                        details.append("Fecha Cierre: ").append(rs.getTimestamp("fecha_cierre")).append("\n\n");
                        details.append("DESCRIPCIÓN:\n");
                        details.append("─────────────\n");
                        details.append(rs.getString("descripcion"));
                        
                        JTextArea textArea = new JTextArea(details.toString());
                        textArea.setEditable(false);
                        textArea.setFont(new Font("Segoe UI", Font.PLAIN, 14));
                        textArea.setBackground(new Color(248, 249, 250));
                        textArea.setBorder(new EmptyBorder(10, 10, 10, 10));
                        
                        JScrollPane detailsScroll = new JScrollPane(textArea);
                        detailsScroll.setPreferredSize(new Dimension(500, 400));
                        
                        JOptionPane.showMessageDialog(this,
                            detailsScroll,
                            "Detalles del Ticket #" + ticketId,
                            JOptionPane.INFORMATION_MESSAGE);
                    }
                }
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error obteniendo detalles del ticket", e);
            JOptionPane.showMessageDialog(this,
                "Error al obtener los detalles: " + e.getMessage(),
                "Error",
                JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void updateTicketStatus(int ticketId, String currentStatus) {
        String[] estados = {"Pendiente", "En Proceso", "Resuelto", "Cerrado"};
        String nuevoEstado = (String) JOptionPane.showInputDialog(this,
            "Seleccione el nuevo estado para el ticket:",
            "Actualizar Estado",
            JOptionPane.QUESTION_MESSAGE,
            null,
            estados,
            currentStatus);
        
        if (nuevoEstado != null && !nuevoEstado.equals(currentStatus)) {
            try (Connection conn = DatabaseConnection.getConnection()) {
                String updateQuery = "UPDATE ticket SET estado = ?, fecha_actualizacion = CURRENT_TIMESTAMP WHERE id = ?";
                
                try (PreparedStatement pstmt = conn.prepareStatement(updateQuery)) {
                    pstmt.setString(1, nuevoEstado);
                    pstmt.setInt(2, ticketId);
                    
                    int rowsUpdated = pstmt.executeUpdate();
                    if (rowsUpdated > 0) {
                        JOptionPane.showMessageDialog(this,
                            "Estado del ticket actualizado exitosamente.",
                            "Actualización Exitosa",
                            JOptionPane.INFORMATION_MESSAGE);
                        loadUserTickets();
                    }
                }
            } catch (SQLException e) {
                logger.log(Level.SEVERE, "Error actualizando estado del ticket", e);
                JOptionPane.showMessageDialog(this,
                    "Error al actualizar el estado: " + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    private void deleteTicket(int ticketId) {
        int confirmacion = JOptionPane.showConfirmDialog(this,
            "¿Está seguro de que desea eliminar el ticket #" + ticketId + "?\n" +
            "Esta acción no se puede deshacer.",
            "Confirmar Eliminación",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.WARNING_MESSAGE);
        
        if (confirmacion == JOptionPane.YES_OPTION) {
            try (Connection conn = DatabaseConnection.getConnection()) {
                String deleteQuery = "DELETE FROM ticket WHERE id = ?";
                
                try (PreparedStatement pstmt = conn.prepareStatement(deleteQuery)) {
                    pstmt.setInt(1, ticketId);
                    
                    int rowsDeleted = pstmt.executeUpdate();
                    if (rowsDeleted > 0) {
                        JOptionPane.showMessageDialog(this,
                            "Ticket eliminado exitosamente.",
                            "Eliminación Exitosa",
                            JOptionPane.INFORMATION_MESSAGE);
                        loadUserTickets();
                    }
                }
            } catch (SQLException e) {
                logger.log(Level.SEVERE, "Error eliminando ticket", e);
                JOptionPane.showMessageDialog(this,
                    "Error al eliminar el ticket: " + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}
