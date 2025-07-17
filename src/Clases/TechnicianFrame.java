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
    private static List<String> assignedEquipment = new ArrayList<>();
    private static List<String> vehicleRequests = new ArrayList<>();
    private final Map<String, String> equipmentStatus = new HashMap<>(); // Para rastrear el estado de cada equipo
    private final Map<String, Integer> equipmentStock = new HashMap<>(); // Para rastrear el stock de cada equipo

    public TechnicianFrame() {
        setTitle("Panel de Técnico - Net Nexus Ultra");
        setExtendedState(JFrame.MAXIMIZED_BOTH); // Maximize the window
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        // Panel principal con fondo "fondo.png"
        JPanel mainPanel = new BackgroundPanel("/Imagenes/fondo.png");
        if (mainPanel == null) {
            mainPanel = new JPanel(); // Fallback si la imagen no carga
            mainPanel.setBackground(Color.GRAY);
        }
        mainPanel.setLayout(new GridLayout(1, 3)); // 1 row, 3 columns to span the screen
        mainPanel.setBorder(BorderFactory.createEmptyBorder(25, 25, 25, 25));

        // Button for Equipos de Técnicos
        JButton equipmentButton = new JButton();
        ImageIcon equipmentIcon = new ImageIcon(getClass().getResource("/Imagenes/Equipos de Técnicos.png"));
        if (equipmentIcon.getImage() != null) {
            equipmentButton.setIcon(equipmentIcon);
        } else {
            equipmentButton.setText("Equipos");
        }
        equipmentButton.setBorderPainted(false);
        equipmentButton.setContentAreaFilled(false);
        equipmentButton.setFocusPainted(false);
        equipmentButton.addActionListener(e -> {
            new EquipmentWindow().setVisible(true);
        });
        mainPanel.add(equipmentButton);

        // Button for Vehículos
        JButton vehicleButton = new JButton();
        ImageIcon vehicleIcon = new ImageIcon(getClass().getResource("/Imagenes/Vehiculos.png"));
        if (vehicleIcon.getImage() != null) {
            vehicleButton.setIcon(vehicleIcon);
        } else {
            vehicleButton.setText("Vehículos");
        }
        vehicleButton.setBorderPainted(false);
        vehicleButton.setContentAreaFilled(false);
        vehicleButton.setFocusPainted(false);
        vehicleButton.addActionListener(e -> {
            new VehicleWindow().setVisible(true);
        });
        mainPanel.add(vehicleButton);

        // Button for Planes y Servicios (solo para técnicos)
        JButton plansButton = new JButton();
        ImageIcon plansIcon = new ImageIcon(getClass().getResource("/Imagenes/Planes y Servicios.png"));
        if (plansIcon.getImage() != null) {
            plansButton.setIcon(plansIcon);
        } else {
            plansButton.setText("Planes");
        }
        plansButton.setBorderPainted(false);
        plansButton.setContentAreaFilled(false);
        plansButton.setFocusPainted(false);
        plansButton.addActionListener(e -> {
            new PlansWindow().setVisible(true); // Restringido implícitamente a TechnicianFrame
        });
        mainPanel.add(plansButton);

        add(mainPanel);

        // Botón de cerrar sesión con imagen
        JButton logoutButton = new JButton();
        ImageIcon logoutIcon = new ImageIcon(getClass().getResource("/Imagenes/Cerrar_S-removebg-preview (3) (1).png"));
        if (logoutIcon.getImage() != null) {
            Image img = logoutIcon.getImage();
            logoutButton.setIcon(new ImageIcon(img));
            logoutButton.setBorderPainted(false);
            logoutButton.setContentAreaFilled(false);
        } else {
            logoutButton.setText("Cerrar Sesión");
        }
        logoutButton.setFocusPainted(false);
        logoutButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                logoutButton.setForeground(new Color(70, 170, 255));
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                logoutButton.setForeground(Color.WHITE);
            }
        });
        logoutButton.addActionListener(e -> {
            new LoginFrame().setVisible(true);
            dispose();
        });
        add(logoutButton, BorderLayout.SOUTH);
    }

    // Inner class for Equipment window
    private class EquipmentWindow extends JFrame {
        public EquipmentWindow() {
            setTitle("Equipos de Técnicos");
            setSize(600, 400);
            setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            setLocationRelativeTo(null);

            JPanel panel = new JPanel(new BorderLayout());
            panel.setOpaque(false);
            JPanel equipmentListPanel = new JPanel(new GridLayout(0, 1, 10, 10));
            equipmentListPanel.setOpaque(false);

            // Inicializar equipos, estados y stock (5 unidades por defecto)
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
                equipmentStock.put(item, 5); // Stock inicial de 5 unidades por equipo
            }

            // Cargar equipos en el panel
            updateEquipmentPanel(equipmentListPanel, equipment);

            JScrollPane scrollPane = new JScrollPane(equipmentListPanel);
            panel.add(scrollPane, BorderLayout.CENTER);

            // Botón para añadir equipo
            JButton addEquipmentButton = new JButton("Añadir Equipo");
            styleButton(addEquipmentButton);
            addEquipmentButton.addActionListener(e -> {
                String newEquipment = JOptionPane.showInputDialog(this, "Ingrese el nombre del nuevo equipo:");
                if (newEquipment != null && !newEquipment.trim().isEmpty()) {
                    if (!equipmentStatus.containsKey(newEquipment)) {
                        equipmentStatus.put(newEquipment, "Disponible");
                        equipmentStock.put(newEquipment, 5); // Stock inicial de 5 unidades
                        updateEquipmentPanel(equipmentListPanel, equipment);
                        JOptionPane.showMessageDialog(this, "Equipo añadido: " + newEquipment);
                    } else {
                        JOptionPane.showMessageDialog(this, "El equipo ya existe.", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            });

            JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
            buttonPanel.setOpaque(false);
            buttonPanel.add(addEquipmentButton);
            panel.add(buttonPanel, BorderLayout.SOUTH);

            add(panel);
        }

        private void updateEquipmentPanel(JPanel panel, String[] equipment) {
            panel.removeAll();
            for (String item : equipment) {
                if (equipmentStatus.containsKey(item)) {
                    JPanel itemPanel = new JPanel(new BorderLayout(10, 5));
                    itemPanel.setOpaque(false);
                    JLabel label = new JLabel(item + " [Estado: " + equipmentStatus.get(item) + ", Stock: " + equipmentStock.get(item) + "]");
                    label.setForeground(Color.BLACK);
                    label.setFont(new Font("Arial", Font.PLAIN, 14));
                    itemPanel.add(label, BorderLayout.WEST);

                    JButton occupiedButton = new JButton("Ocupado");
                    styleButton(occupiedButton);
                    occupiedButton.addActionListener(e -> {
                        if (equipmentStock.get(item) > 0) {
                            equipmentStatus.put(item, "Ocupado");
                            equipmentStock.put(item, equipmentStock.get(item) - 1);
                            updateEquipmentPanel(panel, equipment);
                            JOptionPane.showMessageDialog(this, item + " marcado como Ocupado. Stock restante: " + equipmentStock.get(item));
                        } else {
                            JOptionPane.showMessageDialog(this, "No hay stock disponible para " + item, "Error", JOptionPane.ERROR_MESSAGE);
                        }
                    });

                    JButton vacantButton = new JButton("Desocupado");
                    styleButton(vacantButton);
                    vacantButton.addActionListener(e -> {
                        equipmentStatus.put(item, "Disponible");
                        equipmentStock.put(item, equipmentStock.get(item) + 1);
                        updateEquipmentPanel(panel, equipment);
                        JOptionPane.showMessageDialog(this, item + " marcado como Desocupado. Stock actualizado: " + equipmentStock.get(item));
                    });

                    JPanel buttonSubPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
                    buttonSubPanel.setOpaque(false);
                    buttonSubPanel.add(occupiedButton);
                    buttonSubPanel.add(vacantButton);
                    itemPanel.add(buttonSubPanel, BorderLayout.CENTER);

                    panel.add(itemPanel);
                }
            }
            panel.revalidate();
            panel.repaint();
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