package Clases;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.HashMap;
import java.util.Map;

public class TechnicianFrame extends JFrame {
    private static final List<String> assignedEquipment = new ArrayList<>();
    private static final List<String> vehicleRequests = new ArrayList<>();
    private final Map<String, String> equipmentStatus = new HashMap<>(); // Para rastrear el estado de cada equipo
    private final Map<String, Integer> equipmentStock = new HashMap<>(); // Para rastrear el stock de cada equipo

    public TechnicianFrame() {
        setTitle("Panel de Técnico - NetNexus Ultra");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        // Panel principal con fondo personalizado
        JPanel mainPanel = new BackgroundPanel("/Imagenes/fondo.png");
        if (mainPanel == null) {
            mainPanel = new JPanel();
            mainPanel.setBackground(new Color(45, 45, 45));
        }
        mainPanel.setLayout(new BorderLayout());

        // Panel superior con título
        JPanel headerPanel = createHeaderPanel();
        mainPanel.add(headerPanel, BorderLayout.NORTH);

        // Panel central con botones principales
        JPanel centerPanel = createCenterPanel();
        mainPanel.add(centerPanel, BorderLayout.CENTER);

        // Panel inferior con logout
        JPanel footerPanel = createFooterPanel();
        mainPanel.add(footerPanel, BorderLayout.SOUTH);

        add(mainPanel);
    }

    private JPanel createHeaderPanel() {
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setOpaque(false);
        headerPanel.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));

        // Título principal
        JLabel titleLabel = new JLabel("Panel de Técnico - NetNexus Ultra", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 28));
        titleLabel.setForeground(Color.WHITE);
        headerPanel.add(titleLabel, BorderLayout.CENTER);

        // Información de bienvenida
        JLabel welcomeLabel = new JLabel("Gestión de Equipos y Vehículos Técnicos", SwingConstants.CENTER);
        welcomeLabel.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        welcomeLabel.setForeground(new Color(220, 220, 220));
        headerPanel.add(welcomeLabel, BorderLayout.SOUTH);

        return headerPanel;
    }

    private JPanel createCenterPanel() {
        JPanel centerPanel = new JPanel(new GridBagLayout());
        centerPanel.setOpaque(false);
        centerPanel.setBorder(BorderFactory.createEmptyBorder(30, 50, 30, 50));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(20, 20, 20, 20);
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;

        // Panel de Equipos
        JPanel equipmentPanel = createModernCardPanel(
            "Equipos de Técnicos",
            "Gestionar inventario y asignaciones",
            "/Imagenes/Equipos de Técnicos.png",
            new Color(76, 175, 80),
            new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent evt) {
                    new EquipmentWindow().setVisible(true);
                }
            }
        );
        gbc.gridx = 0;
        gbc.gridy = 0;
        centerPanel.add(equipmentPanel, gbc);

        // Panel de Vehículos
        JPanel vehiclePanel = createModernCardPanel(
            "Vehículos",
            "Control de vehículos y solicitudes",
            "/Imagenes/Vehiculos.png",
            new Color(33, 150, 243),
            new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent evt) {
                    new VehicleWindow().setVisible(true);
                }
            }
        );
        gbc.gridx = 1;
        gbc.gridy = 0;
        centerPanel.add(vehiclePanel, gbc);

        // Panel de Planes y Servicios
        JPanel plansPanel = createModernCardPanel(
            "Planes y Servicios",
            "Consultar servicios disponibles",
            "/Imagenes/Planes y Servicios.png",
            new Color(156, 39, 176),
            new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent evt) {
                    new PlansWindow().setVisible(true);
                }
            }
        );
        gbc.gridx = 2;
        gbc.gridy = 0;
        centerPanel.add(plansPanel, gbc);

        return centerPanel;
    }

    private JPanel createModernCardPanel(String title, String description, String iconPath, Color accentColor, ActionListener action) {
        JPanel card = new JPanel();
        card.setLayout(new BorderLayout());
        card.setBackground(new Color(60, 60, 60, 180));
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(accentColor, 2),
            BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));

        // Icono
        JLabel iconLabel = new JLabel();
        try {
            ImageIcon icon = new ImageIcon(getClass().getResource(iconPath));
            if (icon.getImage() != null) {
                Image img = icon.getImage().getScaledInstance(80, 80, Image.SCALE_SMOOTH);
                iconLabel.setIcon(new ImageIcon(img));
            }
        } catch (Exception ex) {
            iconLabel.setText("📱");
            iconLabel.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 48));
        }
        iconLabel.setHorizontalAlignment(SwingConstants.CENTER);
        card.add(iconLabel, BorderLayout.NORTH);

        // Texto
        JPanel textPanel = new JPanel(new BorderLayout());
        textPanel.setOpaque(false);
        
        JLabel titleLabel = new JLabel(title, SwingConstants.CENTER);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(15, 10, 5, 10));
        
        JLabel descLabel = new JLabel("<html><center>" + description + "</center></html>", SwingConstants.CENTER);
        descLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        descLabel.setForeground(new Color(200, 200, 200));
        descLabel.setBorder(BorderFactory.createEmptyBorder(0, 10, 15, 10));
        
        textPanel.add(titleLabel, BorderLayout.NORTH);
        textPanel.add(descLabel, BorderLayout.CENTER);
        card.add(textPanel, BorderLayout.CENTER);

        // Efectos hover y clic
        card.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                card.setBackground(new Color(80, 80, 80, 200));
                card.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(accentColor.brighter(), 3),
                    BorderFactory.createEmptyBorder(19, 19, 19, 19)
                ));
                card.setCursor(new Cursor(Cursor.HAND_CURSOR));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                card.setBackground(new Color(60, 60, 60, 180));
                card.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(accentColor, 2),
                    BorderFactory.createEmptyBorder(20, 20, 20, 20)
                ));
                card.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
            }

            @Override
            public void mouseClicked(MouseEvent e) {
                action.actionPerformed(new ActionEvent(card, ActionEvent.ACTION_PERFORMED, ""));
            }
        });

        return card;
    }

    private JPanel createFooterPanel() {
        JPanel footerPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        footerPanel.setOpaque(false);
        footerPanel.setBorder(BorderFactory.createEmptyBorder(20, 30, 30, 30));

        // Botón de cerrar sesión moderno
        JButton logoutButton = createModernButton("Cerrar Sesión", new Color(244, 67, 54));
        logoutButton.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(
                this,
                "¿Está seguro de que desea cerrar sesión?",
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

    private JButton createModernButton(String text, Color backgroundColor) {
        JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI", Font.BOLD, 14));
        button.setForeground(Color.WHITE);
        button.setBackground(backgroundColor);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(12, 24, 12, 24));
        
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setBackground(backgroundColor.brighter());
            }
            
            @Override
            public void mouseExited(MouseEvent e) {
                button.setBackground(backgroundColor);
            }
        });
        
        return button;
    }

    private JButton createStyledButton(String text, Color backgroundColor) {
        JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI", Font.BOLD, 12));
        button.setForeground(Color.WHITE);
        button.setBackground(backgroundColor);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(8, 16, 8, 16));
        
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setBackground(backgroundColor.brighter());
            }
            
            @Override
            public void mouseExited(MouseEvent e) {
                button.setBackground(backgroundColor);
            }
        });
        
        return button;
    }

    // Inner class for Equipment window  
    private class EquipmentWindow extends JFrame {
        public EquipmentWindow() {
            setTitle("Gestión de Equipos - NetNexus Ultra");
            setSize(900, 650);
            setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            setLocationRelativeTo(null);

            // Panel principal con fondo moderno
            JPanel mainPanel = new JPanel(new BorderLayout());
            mainPanel.setBackground(new Color(45, 45, 45));

            // Header
            JPanel headerPanel = new JPanel(new BorderLayout());
            headerPanel.setBackground(new Color(33, 150, 243));
            headerPanel.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));

            JLabel titleLabel = new JLabel("Equipos de Técnicos", SwingConstants.CENTER);
            titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
            titleLabel.setForeground(Color.WHITE);

            JLabel subtitleLabel = new JLabel("Gestión de inventario y asignaciones", SwingConstants.CENTER);
            subtitleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
            subtitleLabel.setForeground(new Color(220, 220, 220));

            headerPanel.add(titleLabel, BorderLayout.CENTER);
            headerPanel.add(subtitleLabel, BorderLayout.SOUTH);

            // Panel de contenido
            JPanel contentPanel = new JPanel(new BorderLayout());
            contentPanel.setBackground(new Color(50, 50, 50));
            contentPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

            // Lista de equipos con scroll
            JPanel equipmentListPanel = new JPanel();
            equipmentListPanel.setLayout(new BoxLayout(equipmentListPanel, BoxLayout.Y_AXIS));
            equipmentListPanel.setBackground(new Color(50, 50, 50));

            // Inicializar equipos, estados y stock
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
                equipmentStatus.put(item, "Disponible");
                equipmentStock.put(item, 5);
            }

            updateEquipmentPanel(equipmentListPanel, equipment);

            JScrollPane scrollPane = new JScrollPane(equipmentListPanel);
            scrollPane.setBackground(new Color(50, 50, 50));
            scrollPane.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(33, 150, 243), 1),
                "Inventario de Equipos",
                0, 0,
                new Font("Segoe UI", Font.BOLD, 14),
                Color.WHITE
            ));
            contentPanel.add(scrollPane, BorderLayout.CENTER);

            // Panel de botones
            JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
            buttonPanel.setBackground(new Color(50, 50, 50));

            JButton addEquipmentButton = createStyledButton("Añadir Equipo", new Color(76, 175, 80));
            addEquipmentButton.addActionListener(e -> {
                String newEquipment = JOptionPane.showInputDialog(this, "Nombre del nuevo equipo:");
                if (newEquipment != null && !newEquipment.trim().isEmpty()) {
                    if (!equipmentStatus.containsKey(newEquipment)) {
                        equipmentStatus.put(newEquipment, "Disponible");
                        equipmentStock.put(newEquipment, 5);
                        equipmentListPanel.removeAll();
                        
                        String[] updatedEquipment = new String[equipment.length + 1];
                        System.arraycopy(equipment, 0, updatedEquipment, 0, equipment.length);
                        updatedEquipment[equipment.length] = newEquipment;
                        
                        updateEquipmentPanel(equipmentListPanel, updatedEquipment);
                        revalidate();
                        repaint();
                        JOptionPane.showMessageDialog(this, "Equipo añadido exitosamente: " + newEquipment);
                    } else {
                        JOptionPane.showMessageDialog(this, "El equipo ya existe.", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            });

            buttonPanel.add(addEquipmentButton);
            contentPanel.add(buttonPanel, BorderLayout.SOUTH);

            mainPanel.add(headerPanel, BorderLayout.NORTH);
            mainPanel.add(contentPanel, BorderLayout.CENTER);
            add(mainPanel);
        }

        private void updateEquipmentPanel(JPanel panel, String[] equipment) {
            panel.removeAll();
            
            for (String item : equipment) {
                if (equipmentStatus.containsKey(item)) {
                    JPanel equipmentCard = createEquipmentCard(item);
                    panel.add(equipmentCard);
                    panel.add(Box.createRigidArea(new Dimension(0, 10)));
                }
            }
            
            panel.revalidate();
            panel.repaint();
        }

        private JPanel createEquipmentCard(String equipmentName) {
            JPanel card = new JPanel(new BorderLayout());
            card.setBackground(new Color(70, 70, 70));
            card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(100, 100, 100), 1),
                BorderFactory.createEmptyBorder(15, 20, 15, 20)
            ));
            card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 80));

            // Info del equipo
            JPanel infoPanel = new JPanel(new GridLayout(2, 1));
            infoPanel.setOpaque(false);

            JLabel nameLabel = new JLabel(equipmentName);
            nameLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
            nameLabel.setForeground(Color.WHITE);

            String status = equipmentStatus.get(equipmentName);
            int stock = equipmentStock.get(equipmentName);
            JLabel statusLabel = new JLabel("Estado: " + status + " | Stock: " + stock + " unidades");
            statusLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
            statusLabel.setForeground(new Color(200, 200, 200));

            infoPanel.add(nameLabel);
            infoPanel.add(statusLabel);

            // Botones de acción
            JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
            buttonPanel.setOpaque(false);

            JButton occupiedButton = createStyledButton("Ocupar", new Color(255, 152, 0));
            occupiedButton.addActionListener(e -> {
                if (stock > 0) {
                    equipmentStatus.put(equipmentName, "Ocupado");
                    equipmentStock.put(equipmentName, stock - 1);
                    updateEquipmentPanel((JPanel) card.getParent(), 
                        equipmentStatus.keySet().toArray(new String[0]));
                    JOptionPane.showMessageDialog(this, equipmentName + " marcado como ocupado. Stock restante: " + (stock - 1));
                } else {
                    JOptionPane.showMessageDialog(this, "No hay stock disponible para " + equipmentName, "Error", JOptionPane.ERROR_MESSAGE);
                }
            });

            JButton vacantButton = createStyledButton("Liberar", new Color(76, 175, 80));
            vacantButton.addActionListener(e -> {
                equipmentStatus.put(equipmentName, "Disponible");
                equipmentStock.put(equipmentName, stock + 1);
                updateEquipmentPanel((JPanel) card.getParent(), 
                    equipmentStatus.keySet().toArray(new String[0]));
                JOptionPane.showMessageDialog(this, equipmentName + " liberado. Stock actualizado: " + (stock + 1));
            });

            buttonPanel.add(occupiedButton);
            buttonPanel.add(vacantButton);
            
            card.add(infoPanel, BorderLayout.CENTER);
            card.add(buttonPanel, BorderLayout.EAST);

            return card;
        }
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
            loadVehicles(vehicleModel);
            JList<String> vehiclesList = new JList<>(vehicleModel);
            vehiclesList.setForeground(Color.WHITE);
            vehiclesList.setBackground(new Color(20, 20, 30));
            vehiclesList.setFont(new Font("Arial", Font.PLAIN, 14));
            panel.add(new JScrollPane(vehiclesList), BorderLayout.CENTER);

            // Botón para añadir vehículo
            JButton addVehicleButton = new JButton("Añadir Vehículo");
            addVehicleButton.addActionListener(e -> {
                JTextField idField = new JTextField(10);
                JTextField modelField = new JTextField(10);
                JTextField plateField = new JTextField(10);
                JTextField colorField = new JTextField(10);
                JTextField routeField = new JTextField(10);

                JPanel inputPanel = new JPanel(new GridLayout(5, 2, 5, 5));
                inputPanel.add(new JLabel("ID Vehículo:"));
                inputPanel.add(idField);
                inputPanel.add(new JLabel("Modelo:"));
                inputPanel.add(modelField);
                inputPanel.add(new JLabel("Placa:"));
                inputPanel.add(plateField);
                inputPanel.add(new JLabel("Color:"));
                inputPanel.add(colorField);
                inputPanel.add(new JLabel("Ruta:"));
                inputPanel.add(routeField);

                int result = JOptionPane.showConfirmDialog(this, inputPanel, "Añadir Nuevo Vehículo", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
                if (result == JOptionPane.OK_OPTION) {
                    int id;
                    try {
                        id = Integer.parseInt(idField.getText().trim());
                    } catch (NumberFormatException ex) {
                        JOptionPane.showMessageDialog(this, "El ID debe ser un número válido.", "Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                    String model = modelField.getText().trim();
                    String plate = plateField.getText().trim().toUpperCase();
                    String color = colorField.getText().trim();
                    String route = routeField.getText().trim();

                    if (!model.isEmpty() && !plate.isEmpty() && !color.isEmpty() && !route.isEmpty()) {
                        if (isPlateUnique(plate)) {
                            String estado = "disponible";
                            if (addVehicleToDatabase(id, model, plate, color, route, estado)) {
                                vehicleModel.clear();
                                loadVehicles(vehicleModel);
                                JOptionPane.showMessageDialog(this, "Vehículo añadido: Vehículo " + id + ": " + model + ", Placa: " + plate + ", Color: " + color + ", Ruta: " + route + ", Estado: " + estado);
                            } else {
                                JOptionPane.showMessageDialog(this, "Error al añadir el vehículo. El ID " + id + " puede estar duplicado o no es válido.", "Error", JOptionPane.ERROR_MESSAGE);
                            }
                        } else {
                            JOptionPane.showMessageDialog(this, "La placa " + plate + " ya está registrada.", "Error", JOptionPane.ERROR_MESSAGE);
                        }
                    } else {
                        JOptionPane.showMessageDialog(this, "Todos los campos son obligatorios.", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            });

            // Botón para solicitar vehículo
            JButton requestVehicleButton = new JButton("Solicitar Vehículo");
            requestVehicleButton.addActionListener(e -> {
                String selectedVehicle = vehiclesList.getSelectedValue();
                if (selectedVehicle != null && selectedVehicle.contains("Estado: disponible")) {
                    String id = selectedVehicle.split(":")[0].replace("Vehículo ", "").trim();
                    vehicleRequests.add(selectedVehicle + ", Solicitado por Técnico el " + new Date());
                    updateVehicleStatusInDatabase(id, "en uso");
                    vehicleModel.clear();
                    loadVehicles(vehicleModel);
                    JOptionPane.showMessageDialog(this, "Solicitud enviada al administrador: " + selectedVehicle);
                } else {
                    JOptionPane.showMessageDialog(this, "Selecciona un vehículo disponible", "Error", JOptionPane.ERROR_MESSAGE);
                }
            });

            JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
            buttonPanel.setOpaque(false);
            buttonPanel.add(addVehicleButton);
            buttonPanel.add(requestVehicleButton);
            panel.add(buttonPanel, BorderLayout.SOUTH);

            add(panel);
        }

        private void loadVehicles(DefaultListModel<String> model) {
            try (Connection conn = DatabaseConnection.getConnection()) {
                String sql = "SELECT idVehiculos, modelo, placa, color, ruta, estado FROM vehiculos";
                PreparedStatement stmt = conn.prepareStatement(sql);
                ResultSet rs = stmt.executeQuery();
                while (rs.next()) {
                    int id = rs.getInt("idVehiculos");
                    String modelo = rs.getString("modelo");
                    String placa = rs.getString("placa");
                    String color = rs.getString("color");
                    String ruta = rs.getString("ruta");
                    String estado = rs.getString("estado");
                    model.addElement(String.format("Vehículo %d: %s, Placa: %s, Color: %s, Ruta: %s, Estado: %s", id, modelo, placa, color, ruta, estado));
                }
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(this, "Error al cargar vehículos: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }

        private boolean isPlateUnique(String plate) {
            try (Connection conn = DatabaseConnection.getConnection()) {
                String sql = "SELECT COUNT(*) FROM vehiculos WHERE placa = ?";
                PreparedStatement stmt = conn.prepareStatement(sql);
                stmt.setString(1, plate);
                ResultSet rs = stmt.executeQuery();
                if (rs.next()) {
                    return rs.getInt(1) == 0;
                }
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(this, "Error al verificar placa: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
            return false;
        }

        private boolean addVehicleToDatabase(int id, String model, String plate, String color, String route, String estado) {
            try (Connection conn = DatabaseConnection.getConnection()) {
                String sql = "INSERT INTO vehiculos (idVehiculos, modelo, placa, color, ruta, estado) VALUES (?, ?, ?, ?, ?, ?)";
                PreparedStatement stmt = conn.prepareStatement(sql);
                stmt.setInt(1, id);
                stmt.setString(2, model);
                stmt.setString(3, plate);
                stmt.setString(4, color);
                stmt.setString(5, route);
                stmt.setString(6, estado);
                int rowsAffected = stmt.executeUpdate();
                return rowsAffected > 0;
            } catch (SQLException e) {
                return false;
            }
        }

        private void updateVehicleStatusInDatabase(String id, String newStatus) {
            try (Connection conn = DatabaseConnection.getConnection()) {
                String sql = "UPDATE vehiculos SET estado = ? WHERE idVehiculos = ?";
                PreparedStatement stmt = conn.prepareStatement(sql);
                stmt.setString(1, newStatus);
                stmt.setInt(2, Integer.parseInt(id));
                stmt.executeUpdate();
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(this, "Error al actualizar estado: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    // Inner class for Plans window (solo para técnicos)
    private class PlansWindow extends JFrame {
        public PlansWindow() {
            setTitle("Planes y Servicios - Solo para Técnicos");
            setSize(600, 400);
            setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            setLocationRelativeTo(null);

            JPanel panel = new JPanel(new BorderLayout());
            panel.setOpaque(false);
            DefaultListModel<String> clientPlansModel = new DefaultListModel<>();
            loadClientPlans(clientPlansModel);
            JList<String> clientPlansList = new JList<>(clientPlansModel);
            panel.add(new JScrollPane(clientPlansList), BorderLayout.CENTER);

            add(panel);
        }

        private void loadClientPlans(DefaultListModel<String> model) {
            try (Connection conn = DatabaseConnection.getConnection()) {
                // Consulta para mostrar solo técnicos con idCliente, nombre, apellido y estado
                String sql = "SELECT c.idCliente, c.nombre, c.apellido, " +
                             "CASE WHEN s.idServicios IS NOT NULL THEN 'Ocupado' ELSE 'Desocupado' END AS estado " +
                             "FROM cliente c " +
                             "LEFT JOIN servicios s ON c.idCliente = s.Tecnicos_idTecnicos " +
                             "WHERE c.tipo = 'Técnico'";
                PreparedStatement stmt = conn.prepareStatement(sql);
                ResultSet rs = stmt.executeQuery();
                while (rs.next()) {
                    String clientInfo = String.format("ID: %d, Nombre: %s %s, Estado: %s",
                            rs.getInt("idCliente"),
                            rs.getString("nombre"),
                            rs.getString("apellido"),
                            rs.getString("estado"));
                    model.addElement(clientInfo);
                }
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(this, "Error al cargar planes: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    /**
     * Retrieves a list of available vehicles from the database.
     * This method is static so it can be called directly from other classes like AdminFrame.
     * @return A List of Strings, each representing an available vehicle with its details.
     */
    public static List<String> getAvailableVehicles() {
        List<String> availableVehicles = new ArrayList<>();
        try (Connection conn = DatabaseConnection.getConnection()) {
            String sql = "SELECT idVehiculos, modelo, placa, color, ruta FROM vehiculos WHERE estado = 'disponible'";
            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                int id = rs.getInt("idVehiculos");
                String modelo = rs.getString("modelo");
                String placa = rs.getString("placa");
                String color = rs.getString("color");
                String ruta = rs.getString("ruta");
                availableVehicles.add(String.format("Vehículo %d: %s, Placa: %s, Color: %s, Ruta: %s", id, modelo, placa, color, ruta));
            }
        } catch (SQLException e) {
            // Log the error but don't show a JOptionPane here, as this is a static utility method.
            // The calling method (e.g., in AdminFrame) should handle the error display.
            System.err.println("Error al obtener vehículos disponibles: " + e.getMessage());
        }
        return availableVehicles;
    }


    public static List<String> getAssignedEquipment() {
        return assignedEquipment;
    }

    public static List<String> getVehicleRequests() {
        return vehicleRequests;
    }

    public static void updateVehicleStatus(String vehicle, String newStatus) {
        // Este método ya no se usa directamente, pero se mantiene por compatibilidad
    }
}
