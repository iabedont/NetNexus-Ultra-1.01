/*
 * Click nargs://netbeans/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nargs://netbeans/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Clases;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

public class AdminFrame extends JFrame {
    private final Map<String, String> equipmentStatus = new HashMap<>(); // Para rastrear el estado de cada equipo
    private final Map<String, String> ticketStatus = new HashMap<>(); // Para rastrear el estado de cada ticket

    public AdminFrame() {
        setTitle("Panel de Administrador - NetNexus Ultra");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        // Panel principal moderno con gradiente
        JPanel mainPanel = new BackgroundPanel("/Imagenes/fondo.png");
        mainPanel.setLayout(new BorderLayout());
        
        // Panel superior con título
        JPanel headerPanel = createHeaderPanel();
        mainPanel.add(headerPanel, BorderLayout.NORTH);
        
        // Panel central con botones principales
        JPanel centerPanel = createModernButtonPanel();
        mainPanel.add(centerPanel, BorderLayout.CENTER);
        
        // Panel inferior con botón de cerrar sesión
        JPanel footerPanel = createFooterPanel();
        mainPanel.add(footerPanel, BorderLayout.SOUTH);

        
        add(mainPanel);
    }
    
    private JPanel createHeaderPanel() {
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setOpaque(false);
        headerPanel.setBorder(new EmptyBorder(20, 30, 20, 30));
        
        JLabel titleLabel = new JLabel("Panel de Administración", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 36));
        titleLabel.setForeground(Color.WHITE);
        
        // Sombra para el título
        JPanel titlePanel = new JPanel(new BorderLayout());
        titlePanel.setOpaque(false);
        titlePanel.add(titleLabel, BorderLayout.CENTER);
        
        headerPanel.add(titlePanel, BorderLayout.CENTER);
        return headerPanel;
    }
    
    private JPanel createModernButtonPanel() {
        JPanel buttonPanel = new JPanel(new GridBagLayout());
        buttonPanel.setOpaque(false);
        buttonPanel.setBorder(new EmptyBorder(30, 50, 30, 50));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(20, 20, 20, 20);
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;

        // Botón para Equipos de Técnicos
        JButton equipmentButton = createModernButton("Equipos Técnicos", "/Imagenes/Equipos de Técnicos.png", new Color(52, 152, 219));
        equipmentButton.addActionListener(e -> new EquipmentWindow().setVisible(true));
        gbc.gridx = 0; gbc.gridy = 0;
        buttonPanel.add(equipmentButton, gbc);

        // Botón para Tickets mejorado
        JButton ticketButton = createModernButton("Gestión de Contratos", "/Imagenes/Tickets.png", new Color(155, 89, 182));
        ticketButton.addActionListener(e -> new ModernTicketWindow().setVisible(true));
        gbc.gridx = 1; gbc.gridy = 0;
        buttonPanel.add(ticketButton, gbc);

        // Botón para Vehículos
        JButton vehicleButton = createModernButton("Flota de Vehículos", "/Imagenes/Vehiculos.png", new Color(46, 204, 113));
        vehicleButton.addActionListener(e -> new AdminVehicleManager().setVisible(true));
        gbc.gridx = 0; gbc.gridy = 1;
        buttonPanel.add(vehicleButton, gbc);

        // Botón para Gestión de Roles
        JButton roleButton = createModernButton("Gestión de Roles", "/Imagenes/Gestion.png", new Color(231, 76, 60));
        roleButton.addActionListener(e -> new UserRoleManagementFrame().setVisible(true));
        gbc.gridx = 1; gbc.gridy = 1;
        buttonPanel.add(roleButton, gbc);
        
        return buttonPanel;
    }
    
    private JButton createModernButton(String text, String iconPath, Color baseColor) {
        JButton button = new JButton(text);
        button.setPreferredSize(new Dimension(280, 180));
        button.setFont(new Font("Segoe UI", Font.BOLD, 16));
        button.setForeground(Color.WHITE);
        button.setBackground(baseColor);
        button.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createRaisedBevelBorder(),
            new EmptyBorder(15, 15, 15, 15)
        ));
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        // Cargar icono si existe
        try {
            java.net.URL iconURL = getClass().getResource(iconPath);
            if (iconURL != null) {
                ImageIcon icon = new ImageIcon(iconURL);
                Image img = icon.getImage().getScaledInstance(48, 48, Image.SCALE_SMOOTH);
                button.setIcon(new ImageIcon(img));
                button.setVerticalTextPosition(SwingConstants.BOTTOM);
                button.setHorizontalTextPosition(SwingConstants.CENTER);
                button.setIconTextGap(10);
            }
        } catch (Exception e) {
            // Usar solo texto si no se puede cargar la imagen
        }
        
        // Efectos hover modernos
        Color hoverColor = baseColor.brighter();
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(hoverColor);
                button.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createRaisedBevelBorder(),
                    BorderFactory.createLineBorder(Color.WHITE, 2)
                ));
            }
            
            @Override
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(baseColor);
                button.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createRaisedBevelBorder(),
                    new EmptyBorder(15, 15, 15, 15)
                ));
            }
        });
        
        return button;
    }
    
    private JPanel createFooterPanel() {
        JPanel footerPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        footerPanel.setOpaque(false);
        footerPanel.setBorder(new EmptyBorder(10, 30, 20, 30));
        
        JButton logoutButton = new JButton("Cerrar Sesión");
        logoutButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        logoutButton.setForeground(Color.WHITE);
        logoutButton.setBackground(new Color(220, 53, 69));
        logoutButton.setBorder(new EmptyBorder(10, 20, 10, 20));
        logoutButton.setFocusPainted(false);
        logoutButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        logoutButton.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                logoutButton.setBackground(new Color(200, 35, 51));
            }
            
            @Override
            public void mouseExited(java.awt.event.MouseEvent evt) {
                logoutButton.setBackground(new Color(220, 53, 69));
            }
        });
        
        logoutButton.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(
                this,
                "¿Está seguro que desea cerrar sesión?",
                "Confirmar Cierre de Sesión",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE
            );
            if (confirm == JOptionPane.YES_OPTION) {
                new Bienvenida().setVisible(true);
                dispose();
            }
        });
        
        footerPanel.add(logoutButton);
        return footerPanel;
    }

    private class UserDataWindow extends JFrame {
        public UserDataWindow() {
            setTitle("Datos del Usuario");
            setSize(600, 400);
            setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            setLocationRelativeTo(null);

            JPanel panel = new JPanel(new BorderLayout(10, 10));
            panel.setOpaque(false);
            JTextArea dataArea = new JTextArea(10, 40);
            dataArea.setEditable(false);
            dataArea.setForeground(Color.WHITE);
            dataArea.setBackground(new Color(20, 20, 30));
            dataArea.setFont(new Font("Arial", Font.PLAIN, 14));

            // Depuración de datos del usuario
            List<String> userData = null;
            try {
                Class<?> userFrameClass = Class.forName("Clases.UserFrame"); // Verifica si la clase existe
                java.lang.reflect.Method method = userFrameClass.getMethod("getUserData");
                userData = (List<String>) method.invoke(null); // Invoca el método estático
                if (userData != null) {
                    for (String data : userData) {
                        dataArea.append(data != null ? data + "\n" : "Dato no disponible\n");
                    }
                } else {
                    dataArea.append("Error: UserFrame.getUserData() devolvió null.\n");
                    System.err.println("UserFrame.getUserData() devolvió null a las " + new java.util.Date());
                }
            } catch (ClassNotFoundException e) {
                dataArea.append("Error: Clase UserFrame no encontrada.\n");
                System.err.println("Clase UserFrame no encontrada: " + e.getMessage());
            } catch (NoSuchMethodException e) {
                dataArea.append("Error: Método getUserData no encontrado en UserFrame.\n");
                System.err.println("Método getUserData no encontrado: " + e.getMessage());
            } catch (Exception e) {
                dataArea.append("Error al cargar datos del usuario: " + e.getMessage() + "\n");
                System.err.println("Excepción al cargar UserFrame.getUserData(): " + e.getMessage());
            }
            panel.add(new JScrollPane(dataArea), BorderLayout.CENTER);

            // Depuración y manejo de la imagen de perfil
            ImageIcon profileIcon = null;
            try {
                java.net.URL imageUrl = getClass().getResource("/Imagenes/Perfil_U.png");
                if (imageUrl == null) {
                    throw new Exception("Recurso /Imagenes/Perfil_U.png no encontrado en el classpath");
                }
                profileIcon = new ImageIcon(imageUrl);
                if (profileIcon.getImage() == null) {
                    throw new Exception("Imagen no válida en /Imagenes/Perfil_U.png");
                }
                Image img = profileIcon.getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH);
                JLabel profileLabel = new JLabel(new ImageIcon(img));
                profileLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
                panel.add(profileLabel, BorderLayout.NORTH);
            } catch (Exception e) {
                JLabel errorLabel = new JLabel("Imagen de perfil no disponible: " + e.getMessage());
                errorLabel.setForeground(Color.RED);
                errorLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
                panel.add(errorLabel, BorderLayout.NORTH);
                System.err.println("Error al cargar la imagen de perfil a las " + new java.util.Date() + ": " + e.getMessage());
            }

            add(panel);
        }
    }
    // Inner class for Equipment window
    private class EquipmentWindow extends JFrame {
        public EquipmentWindow() {
            setTitle("Gestión de Equipos - NetNexus Ultra");
            setSize(1056, 768);
            setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            setLocationRelativeTo(null);
            
            // Panel principal con fondo azul gradiente
            JPanel mainPanel = new JPanel() {
                @Override
                protected void paintComponent(Graphics g) {
                    super.paintComponent(g);
                    Graphics2D g2d = (Graphics2D) g.create();
                    g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
                    
                    // Gradiente azul
                    GradientPaint gradient = new GradientPaint(
                        0, 0, new Color(52, 152, 219), 
                        0, getHeight(), new Color(41, 128, 185)
                    );
                    g2d.setPaint(gradient);
                    g2d.fillRect(0, 0, getWidth(), getHeight());
                    g2d.dispose();
                }
            };
            mainPanel.setLayout(new BorderLayout());
            
            // Panel de encabezado
            JPanel headerPanel = new JPanel();
            headerPanel.setOpaque(false);
            headerPanel.setBorder(new EmptyBorder(30, 0, 20, 0));
            
            JLabel titleLabel = new JLabel("Equipos de Técnicos", SwingConstants.CENTER);
            titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 36));
            titleLabel.setForeground(Color.WHITE);
            
            JLabel subtitleLabel = new JLabel("Gestión de inventario y asignaciones", SwingConstants.CENTER);
            subtitleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 16));
            subtitleLabel.setForeground(new Color(255, 255, 255, 180));
            
            headerPanel.setLayout(new BoxLayout(headerPanel, BoxLayout.Y_AXIS));
            titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            subtitleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            headerPanel.add(titleLabel);
            headerPanel.add(Box.createVerticalStrut(10));
            headerPanel.add(subtitleLabel);
            
            // Panel de contenido con inventario
            JPanel contentPanel = new JPanel();
            contentPanel.setOpaque(false);
            contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
            contentPanel.setBorder(new EmptyBorder(0, 40, 20, 40));
            
            // Título del inventario
            JPanel inventoryHeaderPanel = new JPanel(new BorderLayout());
            inventoryHeaderPanel.setOpaque(false);
            inventoryHeaderPanel.setBorder(new EmptyBorder(0, 20, 15, 20));
            
            JLabel inventoryLabel = new JLabel("📦 Inventario de Equipos");
            inventoryLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
            inventoryLabel.setForeground(Color.WHITE);
            inventoryHeaderPanel.add(inventoryLabel, BorderLayout.WEST);
            
            contentPanel.add(inventoryHeaderPanel);
            
            // Panel scrolleable para equipos
            JPanel equipmentListPanel = new JPanel();
            equipmentListPanel.setLayout(new BoxLayout(equipmentListPanel, BoxLayout.Y_AXIS));
            equipmentListPanel.setOpaque(false);
            
            String[] equipment = {
                "Conectores SC/APC",
                "Empalmadoras de fusión", 
                "Cortadoras de precisión",
                "Medidores de potencia óptica",
                "OTDR (Reflectómetro Óptico)"
            };
            
            for (String item : equipment) {
                equipmentStatus.put(item, "Disponible");
                
                // Panel para cada equipo con diseño moderno
                JPanel equipmentPanel = new JPanel(new BorderLayout());
                equipmentPanel.setOpaque(true);
                equipmentPanel.setBackground(new Color(44, 44, 54, 200)); // Fondo semi-transparente
                equipmentPanel.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(new Color(255, 255, 255, 50), 1),
                    new EmptyBorder(20, 25, 20, 25)
                ));
                
                // Información del equipo
                JPanel infoPanel = new JPanel(new BorderLayout());
                infoPanel.setOpaque(false);
                
                JLabel equipmentName = new JLabel(item);
                equipmentName.setFont(new Font("Segoe UI", Font.BOLD, 16));
                equipmentName.setForeground(Color.WHITE);
                
                String statusText = "Estado: " + equipmentStatus.get(item) + " | Stock: 5 unidades";
                JLabel statusLabel = new JLabel(statusText);
                statusLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
                statusLabel.setForeground(new Color(200, 200, 200));
                
                infoPanel.add(equipmentName, BorderLayout.NORTH);
                infoPanel.add(statusLabel, BorderLayout.SOUTH);
                
                // Panel de botones
                JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
                buttonPanel.setOpaque(false);
                
                JButton occupyButton = createEquipmentButton("Ocupar", new Color(255, 193, 7), new Color(255, 171, 0));
                JButton freeButton = createEquipmentButton("Liberar", new Color(40, 167, 69), new Color(34, 139, 58));
                
                occupyButton.addActionListener(e -> {
                    equipmentStatus.put(item, "Ocupado");
                    updateEquipmentDisplay();
                    JOptionPane.showMessageDialog(this, 
                        "✅ " + item + " marcado como Ocupado", 
                        "Estado Actualizado", 
                        JOptionPane.INFORMATION_MESSAGE);
                });
                
                freeButton.addActionListener(e -> {
                    equipmentStatus.put(item, "Disponible");
                    updateEquipmentDisplay();
                    JOptionPane.showMessageDialog(this, 
                        "✅ " + item + " marcado como Disponible", 
                        "Estado Actualizado", 
                        JOptionPane.INFORMATION_MESSAGE);
                });
                
                buttonPanel.add(occupyButton);
                buttonPanel.add(freeButton);
                
                equipmentPanel.add(infoPanel, BorderLayout.CENTER);
                equipmentPanel.add(buttonPanel, BorderLayout.EAST);
                
                equipmentListPanel.add(equipmentPanel);
                equipmentListPanel.add(Box.createVerticalStrut(15));
            }
            
            // ScrollPane para la lista de equipos
            JScrollPane scrollPane = new JScrollPane(equipmentListPanel);
            scrollPane.setOpaque(false);
            scrollPane.getViewport().setOpaque(false);
            scrollPane.setBorder(BorderFactory.createEmptyBorder());
            scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
            scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
            
            contentPanel.add(scrollPane);
            
            // Botón de agregar equipo
            JPanel addButtonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
            addButtonPanel.setOpaque(false);
            addButtonPanel.setBorder(new EmptyBorder(20, 0, 0, 0));
            
            JButton addEquipmentButton = createEquipmentButton("Añadir Equipo", new Color(40, 167, 69), new Color(34, 139, 58));
            addEquipmentButton.setPreferredSize(new Dimension(200, 40));
            addEquipmentButton.addActionListener(e -> {
                JOptionPane.showMessageDialog(this, 
                    "Funcionalidad de agregar equipo disponible próximamente", 
                    "En Desarrollo", 
                    JOptionPane.INFORMATION_MESSAGE);
            });
            
            addButtonPanel.add(addEquipmentButton);
            contentPanel.add(addButtonPanel);
            
            mainPanel.add(headerPanel, BorderLayout.NORTH);
            mainPanel.add(contentPanel, BorderLayout.CENTER);
            
            add(mainPanel);
        }
        
        private JButton createEquipmentButton(String text, Color bgColor, Color hoverColor) {
            JButton button = new JButton(text);
            button.setFont(new Font("Segoe UI", Font.BOLD, 12));
            button.setForeground(Color.WHITE);
            button.setBackground(bgColor);
            button.setBorder(BorderFactory.createEmptyBorder(8, 16, 8, 16));
            button.setFocusPainted(false);
            button.setCursor(new Cursor(Cursor.HAND_CURSOR));
            
            button.addMouseListener(new java.awt.event.MouseAdapter() {
                @Override
                public void mouseEntered(java.awt.event.MouseEvent evt) {
                    button.setBackground(hoverColor);
                }
                
                @Override
                public void mouseExited(java.awt.event.MouseEvent evt) {
                    button.setBackground(bgColor);
                }
            });
            
            return button;
        }
        
        private void updateEquipmentDisplay() {
            // Refrescar la ventana
            SwingUtilities.invokeLater(() -> {
                dispose();
                new EquipmentWindow().setVisible(true);
            });
        }
    }
private class TicketWindow extends JFrame {
    // Mapa para almacenar el estado de los tickets
    private Map<String, String> ticketStatus = new HashMap<>();

    public TicketWindow() {
        setTitle("Tickets");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel(new GridLayout(0, 1, 10, 10));
        panel.setOpaque(false);

        // Obtener y verificar tickets con depuración detallada
        List<Ticket> tickets = null;
        try {
            Class<?> userFrameClass = Class.forName("Clases.UserFrame");
            System.out.println("Clase UserFrame encontrada.");
            java.lang.reflect.Method method = userFrameClass.getMethod("getUserTickets");
            System.out.println("Método getUserTickets encontrado.");
            tickets = (List<Ticket>) method.invoke(null);
            System.out.println("Invocación de getUserTickets exitosa. Tickets: " + (tickets != null ? tickets.size() : "null"));
            if (tickets == null) {
                throw new Exception("UserFrame.getUserTickets() devolvió null");
            }
        } catch (ClassNotFoundException e) {
            JOptionPane.showMessageDialog(this, "Error: Clase UserFrame no encontrada: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            System.err.println("Clase UserFrame no encontrada a las " + new java.util.Date() + ": " + e.getMessage());
            e.printStackTrace();
            return;
        } catch (NoSuchMethodException e) {
            JOptionPane.showMessageDialog(this, "Error: Método getUserTickets no encontrado en UserFrame: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            System.err.println("Método getUserTickets no encontrado a las " + new java.util.Date() + ": " + e.getMessage());
            e.printStackTrace();
            return;
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al cargar tickets: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            System.err.println("Excepción al cargar tickets a las " + new java.util.Date() + ": " + e.getMessage());
            e.printStackTrace();
            return;
        }

        // Construir panel con tickets
        for (Ticket ticket : tickets) {
            if (ticket == null) {
                System.err.println("Ticket nulo encontrado en la lista a las " + new java.util.Date());
                continue;
            }
            int idTicket = ticket.getIdTicket() != 0 ? ticket.getIdTicket() : -1;
            String ticketKey = "Ticket ID: " + idTicket;
            ticketStatus.put(ticketKey, "Disponible");

            JPanel ticketPanel = new JPanel(new BorderLayout(10, 5));
            ticketPanel.setOpaque(false);
            String description = ticket.getDescripcion() != null ? ticket.getDescripcion() : "Sin descripción";
            JLabel label = new JLabel(ticketKey + ", Servicio: " + description + " [" + ticketStatus.get(ticketKey) + "]");
            label.setForeground(Color.CYAN);
            label.setFont(new Font("Arial", Font.PLAIN, 14));
            ticketPanel.add(label, BorderLayout.WEST);

            // Variables finales para la lambda
            final String finalTicketKey = ticketKey; // Hacer el ticketKey final para la lambda
            final JPanel finalPanel = panel; // Capturar el panel como final
            final List<Ticket> finalTickets = tickets; // Capturar los tickets como final

            JButton occupiedButton = new JButton("Ocupado");
            styleButton(occupiedButton);
            occupiedButton.addActionListener(e -> {
                ticketStatus.put(finalTicketKey, "Ocupado");
                updateTicketsPanel(finalPanel, finalTickets);
                JOptionPane.showMessageDialog(TicketWindow.this, finalTicketKey + " marcado como Ocupado");
            });

            JButton vacantButton = new JButton("Desocupado");
            styleButton(vacantButton);
            vacantButton.addActionListener(e -> {
                ticketStatus.put(finalTicketKey, "Disponible");
                updateTicketsPanel(finalPanel, finalTickets);
                JOptionPane.showMessageDialog(TicketWindow.this, finalTicketKey + " marcado como Desocupado");
            });

            JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
            buttonPanel.setOpaque(false);
            buttonPanel.add(occupiedButton);
            buttonPanel.add(vacantButton);
            ticketPanel.add(buttonPanel, BorderLayout.CENTER);

            panel.add(ticketPanel);
        }
        add(new JScrollPane(panel));
    }

    // Método para estilizar botones
    private void styleButton(JButton button) {
        button.setBackground(new Color(0, 120, 215));
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
    }

    // Método para actualizar el panel de tickets
    private void updateTicketsPanel(JPanel panel, List<Ticket> tickets) {
        panel.removeAll();
        for (Ticket ticket : tickets) {
            if (ticket == null) continue;
            int idTicket = ticket.getIdTicket() != 0 ? ticket.getIdTicket() : -1;
            String ticketKey = "Ticket ID: " + idTicket;
            JPanel ticketPanel = new JPanel(new BorderLayout(10, 5));
            ticketPanel.setOpaque(false);
            String description = ticket.getDescripcion() != null ? ticket.getDescripcion() : "Sin descripción";
            JLabel label = new JLabel(ticketKey + ", Servicio: " + description + " [" + ticketStatus.get(ticketKey) + "]");
            label.setForeground(Color.CYAN);
            label.setFont(new Font("Arial", Font.PLAIN, 14));
            ticketPanel.add(label, BorderLayout.WEST);

            final String finalTicketKey = ticketKey; // Captura final para la lambda
            JButton occupiedButton = new JButton("Ocupado");
            styleButton(occupiedButton);
            occupiedButton.addActionListener(e -> {
                ticketStatus.put(finalTicketKey, "Ocupado");
                updateTicketsPanel(panel, tickets);
                JOptionPane.showMessageDialog(TicketWindow.this, finalTicketKey + " marcado como Ocupado");
            });

            JButton vacantButton = new JButton("Desocupado");
            styleButton(vacantButton);
            vacantButton.addActionListener(e -> {
                ticketStatus.put(finalTicketKey, "Disponible");
                updateTicketsPanel(panel, tickets);
                JOptionPane.showMessageDialog(TicketWindow.this, finalTicketKey + " marcado como Desocupado");
            });

            JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
            buttonPanel.setOpaque(false);
            buttonPanel.add(occupiedButton);
            buttonPanel.add(vacantButton);
            ticketPanel.add(buttonPanel, BorderLayout.CENTER);

            panel.add(ticketPanel);
        }
        panel.revalidate();
        panel.repaint();
    }
}

// Clase Ticket mínima para pruebas
class Ticket {
    private int idTicket;
    private String descripcion;

    public Ticket(int idTicket, String descripcion) {
        this.idTicket = idTicket;
        this.descripcion = descripcion;
    }

    public int getIdTicket() { return idTicket; }
    public String getDescripcion() { return descripcion; }
}
    // Inner class for Vehicle window - Igual que la del técnico
    private class VehicleWindow extends JFrame {
        public VehicleWindow() {
            setTitle("Gestión de Vehículos - NetNexus Ultra");
            setSize(1056, 768);
            setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            setLocationRelativeTo(null);
            
            // Panel principal con fondo púrpura gradiente
            JPanel mainPanel = new JPanel() {
                @Override
                protected void paintComponent(Graphics g) {
                    super.paintComponent(g);
                    Graphics2D g2d = (Graphics2D) g.create();
                    g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
                    
                    // Gradiente púrpura
                    GradientPaint gradient = new GradientPaint(
                        0, 0, new Color(155, 89, 182), 
                        0, getHeight(), new Color(142, 68, 173)
                    );
                    g2d.setPaint(gradient);
                    g2d.fillRect(0, 0, getWidth(), getHeight());
                    g2d.dispose();
                }
            };
            mainPanel.setLayout(new BorderLayout());
            
            // Panel de encabezado
            JPanel headerPanel = new JPanel();
            headerPanel.setOpaque(false);
            headerPanel.setBorder(new EmptyBorder(30, 0, 20, 0));
            
            JLabel titleLabel = new JLabel("Gestión de Vehículos", SwingConstants.CENTER);
            titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 36));
            titleLabel.setForeground(Color.WHITE);
            
            JLabel subtitleLabel = new JLabel("Administración de flota y solicitudes", SwingConstants.CENTER);
            subtitleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 16));
            subtitleLabel.setForeground(new Color(255, 255, 255, 180));
            
            headerPanel.setLayout(new BoxLayout(headerPanel, BoxLayout.Y_AXIS));
            titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            subtitleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            headerPanel.add(titleLabel);
            headerPanel.add(Box.createVerticalStrut(10));
            headerPanel.add(subtitleLabel);
            
            // Panel de contenido principal
            JPanel contentPanel = new JPanel(new BorderLayout(20, 20));
            contentPanel.setOpaque(false);
            contentPanel.setBorder(new EmptyBorder(0, 40, 20, 40));
            
            // Panel izquierdo - Lista de vehículos disponibles
            JPanel vehicleListPanel = new JPanel(new BorderLayout());
            vehicleListPanel.setOpaque(true);
            vehicleListPanel.setBackground(new Color(44, 44, 54, 200));
            vehicleListPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(255, 255, 255, 50), 1),
                new EmptyBorder(20, 20, 20, 20)
            ));
            
            JLabel vehicleListTitle = new JLabel("🚗 Vehículos Disponibles");
            vehicleListTitle.setFont(new Font("Segoe UI", Font.BOLD, 18));
            vehicleListTitle.setForeground(Color.WHITE);
            vehicleListTitle.setBorder(new EmptyBorder(0, 0, 15, 0));
            
            DefaultListModel<String> vehicleModel = new DefaultListModel<>();
            for (String vehicle : TechnicianFrame.getAvailableVehicles()) {
                vehicleModel.addElement(vehicle);
            }
            JList<String> vehiclesList = new JList<>(vehicleModel);
            vehiclesList.setFont(new Font("Segoe UI", Font.PLAIN, 14));
            vehiclesList.setBackground(new Color(60, 60, 70));
            vehiclesList.setForeground(Color.WHITE);
            vehiclesList.setSelectionBackground(new Color(155, 89, 182));
            vehiclesList.setSelectionForeground(Color.WHITE);
            
            JScrollPane vehicleScrollPane = new JScrollPane(vehiclesList);
            vehicleScrollPane.setBorder(BorderFactory.createEmptyBorder());
            vehicleScrollPane.setOpaque(false);
            vehicleScrollPane.getViewport().setOpaque(false);
            
            // Botones de control de estado
            JPanel vehicleControlPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
            vehicleControlPanel.setOpaque(false);
            
            JButton occupiedButton = createVehicleButton("🔒 Marcar Ocupado", new Color(231, 76, 60), new Color(220, 53, 69));
            JButton availableButton = createVehicleButton("✅ Marcar Disponible", new Color(40, 167, 69), new Color(34, 139, 58));
            
            occupiedButton.addActionListener(ev -> {
                int selectedIndex = vehiclesList.getSelectedIndex();
                if (selectedIndex >= 0) {
                    String vehicleName = vehicleModel.getElementAt(selectedIndex);
                    TechnicianFrame.updateVehicleStatus(vehicleName, "Ocupado");
                    vehicleModel.clear();
                    for (String vehicle : TechnicianFrame.getAvailableVehicles()) {
                        vehicleModel.addElement(vehicle);
                    }
                    JOptionPane.showMessageDialog(this, "✅ Vehículo " + vehicleName + " marcado como Ocupado");
                }
            });
            
            availableButton.addActionListener(ev -> {
                int selectedIndex = vehiclesList.getSelectedIndex();
                if (selectedIndex >= 0) {
                    String vehicleName = vehicleModel.getElementAt(selectedIndex);
                    TechnicianFrame.updateVehicleStatus(vehicleName, "Disponible");
                    vehicleModel.clear();
                    for (String vehicle : TechnicianFrame.getAvailableVehicles()) {
                        vehicleModel.addElement(vehicle);
                    }
                    JOptionPane.showMessageDialog(this, "✅ Vehículo " + vehicleName + " marcado como Disponible");
                }
            });
            
            vehicleControlPanel.add(occupiedButton);
            vehicleControlPanel.add(availableButton);
            
            vehicleListPanel.add(vehicleListTitle, BorderLayout.NORTH);
            vehicleListPanel.add(vehicleScrollPane, BorderLayout.CENTER);
            vehicleListPanel.add(vehicleControlPanel, BorderLayout.SOUTH);
            
            // Panel derecho - Solicitudes de vehículos
            JPanel requestsPanel = new JPanel(new BorderLayout());
            requestsPanel.setOpaque(true);
            requestsPanel.setBackground(new Color(44, 44, 54, 200));
            requestsPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(255, 255, 255, 50), 1),
                new EmptyBorder(20, 20, 20, 20)
            ));
            
            JLabel requestsTitle = new JLabel("📋 Solicitudes Pendientes");
            requestsTitle.setFont(new Font("Segoe UI", Font.BOLD, 18));
            requestsTitle.setForeground(Color.WHITE);
            requestsTitle.setBorder(new EmptyBorder(0, 0, 15, 0));
            
            DefaultListModel<String> requestsModel = new DefaultListModel<>();
            for (String request : TechnicianFrame.getVehicleRequests()) {
                requestsModel.addElement(request);
            }
            JList<String> requestsList = new JList<>(requestsModel);
            requestsList.setFont(new Font("Segoe UI", Font.PLAIN, 14));
            requestsList.setBackground(new Color(60, 60, 70));
            requestsList.setForeground(Color.WHITE);
            requestsList.setSelectionBackground(new Color(155, 89, 182));
            requestsList.setSelectionForeground(Color.WHITE);
            
            JScrollPane requestsScrollPane = new JScrollPane(requestsList);
            requestsScrollPane.setBorder(BorderFactory.createEmptyBorder());
            requestsScrollPane.setOpaque(false);
            requestsScrollPane.getViewport().setOpaque(false);
            
            // Botones de gestión de solicitudes
            JPanel requestControlPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
            requestControlPanel.setOpaque(false);
            
            JButton acceptButton = createVehicleButton("✅ Aceptar Solicitud", new Color(40, 167, 69), new Color(34, 139, 58));
            JButton rejectButton = createVehicleButton("❌ Rechazar Solicitud", new Color(231, 76, 60), new Color(220, 53, 69));
            
            acceptButton.addActionListener(ev -> {
                int selectedIndex = requestsList.getSelectedIndex();
                if (selectedIndex >= 0) {
                    String request = requestsModel.getElementAt(selectedIndex);
                    String vehicleName = request.split(",")[0];
                    TechnicianFrame.updateVehicleStatus(vehicleName, "Ocupado");
                    vehicleModel.clear();
                    for (String vehicle : TechnicianFrame.getAvailableVehicles()) {
                        vehicleModel.addElement(vehicle);
                    }
                    requestsModel.remove(selectedIndex);
                    JOptionPane.showMessageDialog(this, "✅ Solicitud aceptada: " + vehicleName);
                }
            });
            
            rejectButton.addActionListener(ev -> {
                int selectedIndex = requestsList.getSelectedIndex();
                if (selectedIndex >= 0) {
                    requestsModel.remove(selectedIndex);
                    JOptionPane.showMessageDialog(this, "❌ Solicitud rechazada");
                }
            });
            
            requestControlPanel.add(acceptButton);
            requestControlPanel.add(rejectButton);
            
            requestsPanel.add(requestsTitle, BorderLayout.NORTH);
            requestsPanel.add(requestsScrollPane, BorderLayout.CENTER);
            requestsPanel.add(requestControlPanel, BorderLayout.SOUTH);
            
            // Agregar paneles al contenido principal
            JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, vehicleListPanel, requestsPanel);
            splitPane.setDividerLocation(500);
            splitPane.setResizeWeight(0.5);
            splitPane.setOpaque(false);
            splitPane.setBorder(BorderFactory.createEmptyBorder());
            
            contentPanel.add(splitPane, BorderLayout.CENTER);
            
            mainPanel.add(headerPanel, BorderLayout.NORTH);
            mainPanel.add(contentPanel, BorderLayout.CENTER);
            
            add(mainPanel);
        }
        
        private JButton createVehicleButton(String text, Color bgColor, Color hoverColor) {
            JButton button = new JButton(text);
            button.setFont(new Font("Segoe UI", Font.BOLD, 12));
            button.setForeground(Color.WHITE);
            button.setBackground(bgColor);
            button.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
            button.setFocusPainted(false);
            button.setCursor(new Cursor(Cursor.HAND_CURSOR));
            
            button.addMouseListener(new java.awt.event.MouseAdapter() {
                @Override
                public void mouseEntered(java.awt.event.MouseEvent evt) {
                    button.setBackground(hoverColor);
                }
                
                @Override
                public void mouseExited(java.awt.event.MouseEvent evt) {
                    button.setBackground(bgColor);
                }
            });
            
            return button;
        }
    }

    private void styleButton(JButton button) {
        button.setFont(new Font("Arial", Font.BOLD, 14));
        button.setForeground(Color.WHITE);
        button.setBackground(new Color(50, 150, 255));
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(70, 170, 255), 2),
            BorderFactory.createEmptyBorder(5, 15, 5, 15)
        ));
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(70, 170, 255));
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(50, 150, 255));
            }
        });
    }

    private void updateEquipmentPanel(JPanel panel, String[] equipment) {
        panel.removeAll();
        for (String item : equipment) {
            JPanel itemPanel = new JPanel(new BorderLayout(10, 5));
            itemPanel.setOpaque(false);
            JLabel label = new JLabel(item + " [" + equipmentStatus.get(item) + "]");
            label.setForeground(Color.CYAN);
            label.setFont(new Font("Arial", Font.PLAIN, 14));
            itemPanel.add(label, BorderLayout.WEST);

            JButton occupiedButton = new JButton("Ocupado");
            styleButton(occupiedButton);
            occupiedButton.addActionListener(e -> {
                equipmentStatus.put(item, "Ocupado");
                updateEquipmentPanel(panel, equipment);
                JOptionPane.showMessageDialog(this, item + " marcado como Ocupado");
            });

            JButton vacantButton = new JButton("Desocupado");
            styleButton(vacantButton);
            vacantButton.addActionListener(e -> {
                equipmentStatus.put(item, "Disponible");
                updateEquipmentPanel(panel, equipment);
                JOptionPane.showMessageDialog(this, item + " marcado como Desocupado");
            });

            JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
            buttonPanel.setOpaque(false);
            buttonPanel.add(occupiedButton);
            buttonPanel.add(vacantButton);
            itemPanel.add(buttonPanel, BorderLayout.CENTER);

            panel.add(itemPanel);
        }
        panel.revalidate();
        panel.repaint();
    }

    private void updateTicketsPanel(JPanel panel, List<Ticket> tickets) {
        panel.removeAll();
        for (Ticket ticket : tickets) {
            String ticketKey = "Ticket ID: " + ticket.getIdTicket();
            JPanel ticketPanel = new JPanel(new BorderLayout(10, 5));
            ticketPanel.setOpaque(false);
            JLabel label = new JLabel(ticketKey + ", Servicio: " + ticket.getDescripcion() + " [" + ticketStatus.get(ticketKey) + "]");
            label.setForeground(Color.CYAN);
            label.setFont(new Font("Arial", Font.PLAIN, 14));
            ticketPanel.add(label, BorderLayout.WEST);

            JButton occupiedButton = new JButton("Ocupado");
            styleButton(occupiedButton);
            occupiedButton.addActionListener(e -> {
                ticketStatus.put(ticketKey, "Ocupado");
                updateTicketsPanel(panel, tickets);
                JOptionPane.showMessageDialog(this, ticketKey + " marcado como Ocupado");
            });

            JButton vacantButton = new JButton("Desocupado");
            styleButton(vacantButton);
            vacantButton.addActionListener(e -> {
                ticketStatus.put(ticketKey, "Disponible");
                updateTicketsPanel(panel, tickets);
                JOptionPane.showMessageDialog(this, ticketKey + " marcado como Desocupado");
            });

            JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
            buttonPanel.setOpaque(false);
            buttonPanel.add(occupiedButton);
            buttonPanel.add(vacantButton);
            ticketPanel.add(buttonPanel, BorderLayout.CENTER);

            panel.add(ticketPanel);
        }
        panel.revalidate();
        panel.repaint();
    }
}