/*
 * Click nargs://netbeans/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nargs://netbeans/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Clases;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AdminFrame extends JFrame {
    private Map<String, String> equipmentStatus = new HashMap<>(); // Para rastrear el estado de cada equipo
    private Map<String, String> ticketStatus = new HashMap<>(); // Para rastrear el estado de cada ticket

    public AdminFrame() {
        setTitle("Panel de Administrador - Net Nexus Ultra");
        setExtendedState(JFrame.MAXIMIZED_BOTH); // Maximize the window
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        // Panel principal con fondo "fondo.png"
        JPanel mainPanel = new BackgroundPanel("/Imagenes/fondo.png");
        mainPanel.setLayout(new GridLayout(1, 4)); // 1 row, 4 columns to span the screen
        mainPanel.setBorder(BorderFactory.createEmptyBorder(25, 25, 25, 25));

        // Button for Datos del Usuario
        JButton userButton = new JButton();
        userButton.setIcon(new ImageIcon(getClass().getResource("/Imagenes/Datos del usuario.png")));
        userButton.setBorderPainted(false);
        userButton.setContentAreaFilled(false);
        userButton.setFocusPainted(false);
        userButton.addActionListener(e -> {
            new UserDataWindow().setVisible(true);
        });
        mainPanel.add(userButton);

        // Button for Equipos de Técnicos
        JButton equipmentButton = new JButton();
        equipmentButton.setIcon(new ImageIcon(getClass().getResource("/Imagenes/Equipos de Técnicos.png")));
        equipmentButton.setBorderPainted(false);
        equipmentButton.setContentAreaFilled(false);
        equipmentButton.setFocusPainted(false);
        equipmentButton.addActionListener(e -> {
            new EquipmentWindow().setVisible(true);
        });
        mainPanel.add(equipmentButton);

        // Button for Tickets
        JButton ticketButton = new JButton();
        ticketButton.setIcon(new ImageIcon(getClass().getResource("/Imagenes/Tickets.png")));
        ticketButton.setBorderPainted(false);
        ticketButton.setContentAreaFilled(false);
        ticketButton.setFocusPainted(false);
        ticketButton.addActionListener(e -> {
            new TicketWindow().setVisible(true);
        });
        mainPanel.add(ticketButton);

        // Button for Vehículos
        JButton vehicleButton = new JButton();
        vehicleButton.setIcon(new ImageIcon(getClass().getResource("/Imagenes/Vehiculos.png")));
        vehicleButton.setBorderPainted(false);
        vehicleButton.setContentAreaFilled(false);
        vehicleButton.setFocusPainted(false);
        vehicleButton.addActionListener(e -> {
            new VehicleWindow().setVisible(true);
        });
        mainPanel.add(vehicleButton);

        add(mainPanel);

        // Botón de cerrar sesión con imagen, tamaño ajustado al ícono
        JButton logoutButton = new JButton();
        ImageIcon logoutIcon = new ImageIcon(getClass().getResource("/Imagenes/Cerrar_S-removebg-preview (3) (1).png"));
        if (logoutIcon.getImage() != null) {
            Image img = logoutIcon.getImage(); // Use original size of the icon
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

    // Inner class for User Data window
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
            for (String data : UserFrame.getUserData()) {
                dataArea.append(data + "\n");
            }
            panel.add(new JScrollPane(dataArea), BorderLayout.CENTER);

            ImageIcon profileIcon = new ImageIcon(getClass().getResource("/Imagenes/Perfil_U.png"));
            if (profileIcon.getImage() != null) {
                Image img = profileIcon.getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH);
                JLabel profileLabel = new JLabel(new ImageIcon(img));
                profileLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
                panel.add(profileLabel, BorderLayout.NORTH);
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
            add(new JScrollPane(panel));
        }
    }

    // Inner class for Ticket window
    private class TicketWindow extends JFrame {
        public TicketWindow() {
            setTitle("Tickets");
            setSize(600, 400);
            setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            setLocationRelativeTo(null);

            JPanel panel = new JPanel(new GridLayout(0, 1, 10, 10));
            panel.setOpaque(false);
            List<Ticket> tickets = UserFrame.getUserTickets();
            for (Ticket ticket : tickets) {
                String ticketKey = "Ticket ID: " + ticket.getIdTicket();
                ticketStatus.put(ticketKey, "Disponible"); // Estado inicial
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
            add(new JScrollPane(panel));
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