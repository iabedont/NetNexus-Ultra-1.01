package Clases;

import java.awt.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class AdminFrame extends JFrame {
    private final Map<String, String> equipmentStatus = new HashMap<>(); // Para rastrear el estado de cada equipo
    private final Map<String, String> ticketStatus = new HashMap<>(); // Para rastrear el estado de cada ticket

    public AdminFrame() {
        setTitle("Panel de Administrador - NetNexus Ultra");
        setExtendedState(JFrame.MAXIMIZED_BOTH); // Maximize the window
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        // Panel principal con fondo "fondo.png" y diseño mejorado
        JPanel mainPanel = new BackgroundPanel("/Imagenes/fondo.png");
        mainPanel.setLayout(new BorderLayout());
        
        // Panel de título
        JPanel titlePanel = new JPanel();
        titlePanel.setOpaque(false);
        titlePanel.setBorder(new EmptyBorder(30, 0, 20, 0));
        
        JLabel titleLabel = new JLabel("Panel de Administración", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 36));
        titleLabel.setForeground(Color.WHITE);
        titlePanel.add(titleLabel);
        
        // Panel de botones con diseño mejorado
        JPanel buttonPanel = new JPanel(new GridBagLayout());
        buttonPanel.setOpaque(false);
        buttonPanel.setBorder(new EmptyBorder(20, 50, 50, 50));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(20, 20, 20, 20);
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;

        // Botón para Equipos de Técnicos con diseño mejorado
        JButton equipmentButton = createStyledButton("/Imagenes/Equipos de Técnicos.png");
        equipmentButton.addActionListener(e -> new TechnicalEquipmentWindow().setVisible(true));
        gbc.gridx = 0; gbc.gridy = 0;
        buttonPanel.add(equipmentButton, gbc);

        // Botón para Tickets con diseño mejorado
        JButton ticketButton = createStyledButton("/Imagenes/Tickets.png");
        ticketButton.addActionListener(e -> new UserTicketsWindow().setVisible(true));
        gbc.gridx = 1; gbc.gridy = 0;
        buttonPanel.add(ticketButton, gbc);

        // Botón para Vehículos con diseño mejorado
        JButton vehicleButton = createStyledButton("/Imagenes/Vehiculos.png");
        vehicleButton.addActionListener(e -> new ModernVehicleWindow().setVisible(true));
        gbc.gridx = 0; gbc.gridy = 1;
        buttonPanel.add(vehicleButton, gbc);

        // Botón para Contratos Activos con diseño mejorado
        JButton contractButton = createStyledButton("/Imagenes/Contratos Activos.png");
        contractButton.addActionListener(e -> new ContractWindow().setVisible(true));
        gbc.gridx = 1; gbc.gridy = 1;
        buttonPanel.add(contractButton, gbc);
        
        // Agregar componentes al panel principal
        mainPanel.add(titlePanel, BorderLayout.NORTH);
        mainPanel.add(buttonPanel, BorderLayout.CENTER);
        
        add(mainPanel);
    }
    
    /**
     * Crea un botón con estilo mejorado para el panel de administración
     */
    private JButton createStyledButton(String iconPath) {
        JButton button = new JButton();
        
        try {
            ImageIcon icon = new ImageIcon(getClass().getResource(iconPath));
            if (icon.getIconWidth() > 0) {
                // Escalar la imagen para que se vea mejor
                Image img = icon.getImage().getScaledInstance(200, 150, Image.SCALE_SMOOTH);
                button.setIcon(new ImageIcon(img));
            }
        } catch (Exception e) {
            // Si no se puede cargar la imagen, usar texto
            button.setText("Opción Admin");
            button.setFont(new Font("Segoe UI", Font.BOLD, 16));
            button.setForeground(Color.WHITE);
        }
        
        // Estilo del botón
        button.setBorderPainted(false);
        button.setContentAreaFilled(false);
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        // Efectos hover
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setOpaque(true);
                button.setBackground(new Color(255, 255, 255, 30));
            }
            
            @Override
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setOpaque(false);
            }
        });
        
        button.setPreferredSize(new Dimension(220, 170));
        
        return button;
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
            setTitle("Equipos de Técnicos");
            setSize(600, 400);
            setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            setLocationRelativeTo(null);

            JPanel panel = new JPanel(new GridLayout(0, 1, 10, 10));
            panel.setOpaque(false);
            String[] equipment = {
                "Cables de fibra óptica",
                "Conectores SC/APC",
                "Empalmadoras de fusión",
                "Cortadoras de precisión",
                "Medidores de potencia óptica",
                "OTDR (Reflectómetro Óptico)",
                "Cajas de empalme",
                "Herramientas de pelado y corte",
                "Kits de limpieza de conectores",
                "Tubos de protección de empalme"
            };
            for (String item : equipment) {
                equipmentStatus.put(item, "Disponible"); // Estado inicial
                JPanel itemPanel = new JPanel(new BorderLayout(10, 5));
                itemPanel.setOpaque(false);
                JLabel label = new JLabel(item + " [" + equipmentStatus.get(item) + "]");
                label.setForeground(Color.BLACK);
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
            add(new JScrollPane(panel));
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
    // Inner class for Vehicle window
    private class VehicleWindow extends JFrame {
        public VehicleWindow() {
            setTitle("Vehículos");
            setSize(600, 400);
            setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            setLocationRelativeTo(null);

            JPanel panel = new JPanel(new BorderLayout(10, 10));
            panel.setOpaque(false);
            DefaultListModel<String> vehicleModel = new DefaultListModel<>();
            for (String vehicle : TechnicianFrame.getAvailableVehicles()) {
                vehicleModel.addElement(vehicle);
            }
            JList<String> vehiclesList = new JList<>(vehicleModel);
            vehiclesList.setForeground(Color.WHITE);
            vehiclesList.setBackground(new Color(20, 20, 30));
            vehiclesList.setFont(new Font("Arial", Font.PLAIN, 14));
            panel.add(new JScrollPane(vehiclesList), BorderLayout.CENTER);

            JPanel vehicleRequestsPanel = new JPanel(new BorderLayout(10, 10));
            vehicleRequestsPanel.setOpaque(false);
            DefaultListModel<String> requestsModel = new DefaultListModel<>();
            for (String request : TechnicianFrame.getVehicleRequests()) {
                requestsModel.addElement(request);
            }
            JList<String> requestsList = new JList<>(requestsModel);
            requestsList.setForeground(Color.WHITE);
            requestsList.setBackground(new Color(20, 20, 30));
            requestsList.setFont(new Font("Arial", Font.PLAIN, 14));
            vehicleRequestsPanel.add(new JScrollPane(requestsList), BorderLayout.CENTER);

            JPanel requestActionsPanel = new JPanel(new GridLayout(1, 2, 10, 10));
            requestActionsPanel.setOpaque(false);
            JButton acceptRequestButton = new JButton("Aceptar Solicitud");
            styleButton(acceptRequestButton);
            acceptRequestButton.addActionListener(e -> {
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
                    JOptionPane.showMessageDialog(this, "Solicitud aceptada: " + vehicleName);
                }
            });
            JButton rejectRequestButton = new JButton("Rechazar Solicitud");
            styleButton(rejectRequestButton);
            rejectRequestButton.addActionListener(e -> {
                int selectedIndex = requestsList.getSelectedIndex();
                if (selectedIndex >= 0) {
                    requestsModel.remove(selectedIndex);
                    JOptionPane.showMessageDialog(this, "Solicitud rechazada");
                }
            });
            requestActionsPanel.add(acceptRequestButton);
            requestActionsPanel.add(rejectRequestButton);
            vehicleRequestsPanel.add(requestActionsPanel, BorderLayout.SOUTH);

            JPanel statusPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
            statusPanel.setOpaque(false);
            JButton occupiedButton = new JButton("Ocupado");
            styleButton(occupiedButton);
            occupiedButton.addActionListener(e -> {
                int selectedIndex = vehiclesList.getSelectedIndex();
                if (selectedIndex >= 0) {
                    String vehicleName = vehicleModel.getElementAt(selectedIndex);
                    TechnicianFrame.updateVehicleStatus(vehicleName, "Ocupado");
                    vehicleModel.clear();
                    for (String vehicle : TechnicianFrame.getAvailableVehicles()) {
                        vehicleModel.addElement(vehicle);
                    }
                    JOptionPane.showMessageDialog(this, "Vehículo " + vehicleName + " marcado como Ocupado");
                }
            });
            JButton vacantButton = new JButton("Desocupado");
            styleButton(vacantButton);
            vacantButton.addActionListener(e -> {
                int selectedIndex = vehiclesList.getSelectedIndex();
                if (selectedIndex >= 0) {
                    String vehicleName = vehicleModel.getElementAt(selectedIndex);
                    TechnicianFrame.updateVehicleStatus(vehicleName, "Disponible");
                    vehicleModel.clear();
                    for (String vehicle : TechnicianFrame.getAvailableVehicles()) {
                        vehicleModel.addElement(vehicle);
                    }
                    JOptionPane.showMessageDialog(this, "Vehículo " + vehicleName + " marcado como Desocupado");
                }
            });
            statusPanel.add(occupiedButton);
            statusPanel.add(vacantButton);
            vehicleRequestsPanel.add(statusPanel, BorderLayout.NORTH);

            JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, new JScrollPane(vehiclesList), vehicleRequestsPanel);
            splitPane.setDividerLocation(200);
            splitPane.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
            panel.add(splitPane, BorderLayout.CENTER);

            add(panel);
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