/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Clases;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;

public class TechnicianFrame extends JFrame {
    private static List<String> assignedEquipment = new ArrayList<>();
    private static List<String> availableVehicles = new ArrayList<>();
    private static List<String> vehicleRequests = new ArrayList<>();

    public TechnicianFrame() {
        setTitle("Panel de Técnico");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        BackgroundPanel mainPanel = new BackgroundPanel("/Imagenes/fondo.png");
        mainPanel.setLayout(new BorderLayout());

        JTabbedPane tabbedPane = new JTabbedPane();

        // Pestaña de Administración (acceso al panel de Admin)
        AdminFrame adminFrame = new AdminFrame();
        tabbedPane.addTab("Administración", adminFrame.getContentPane());

        // Pestaña de Equipos
        JPanel equipmentPanel = new JPanel(new BorderLayout());
        equipmentPanel.setOpaque(false);
        DefaultListModel<String> equipmentModel = new DefaultListModel<>();
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
            equipmentModel.addElement(item);
        }
        JList<String> equipmentList = new JList<>(equipmentModel);
        equipmentList.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    String selectedEquipment = equipmentList.getSelectedValue();
                    if (selectedEquipment != null) {
                        assignedEquipment.add(selectedEquipment);
                        JOptionPane.showMessageDialog(TechnicianFrame.this, "Equipo solicitado con éxito: " + selectedEquipment + "\nAsignado para contrato.");
                        equipmentModel.removeElement(selectedEquipment);
                    }
                }
            }
        });
        equipmentPanel.add(new JScrollPane(equipmentList), BorderLayout.CENTER);
        tabbedPane.addTab("Equipos", equipmentPanel);

        // Pestaña de Vehículos
        JPanel vehiclesPanel = new JPanel(new BorderLayout());
        vehiclesPanel.setOpaque(false);
        DefaultListModel<String> vehicleModel = new DefaultListModel<>();
        if (availableVehicles.isEmpty()) {
            availableVehicles.add("Vehículo 1: Toyota Hilux, Ruta: Ibarra-Atuntaqui, Estado: Disponible");
            availableVehicles.add("Vehículo 2: Ford Ranger, Ruta: Otavalo-Cotacachi, Estado: Disponible");
            availableVehicles.add("Vehículo 3: Chevrolet LUV, Ruta: Ibarra-Urcuqui, Estado: Ocupado");
        }
        for (String vehicle : availableVehicles) {
            vehicleModel.addElement(vehicle);
        }
        JList<String> vehiclesList = new JList<>(vehicleModel);
        vehiclesPanel.add(new JScrollPane(vehiclesList), BorderLayout.CENTER);
        JButton requestVehicleButton = new JButton("Solicitar Vehículo");
        requestVehicleButton.addActionListener(e -> {
            String selectedVehicle = vehiclesList.getSelectedValue();
            if (selectedVehicle != null && selectedVehicle.contains("Disponible")) {
                vehicleRequests.add(selectedVehicle + ", Solicitado por Técnico el " + new java.util.Date());
                JOptionPane.showMessageDialog(this, "Solicitud enviada al administrador: " + selectedVehicle);
            } else {
                JOptionPane.showMessageDialog(this, "Selecciona un vehículo disponible", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
        vehiclesPanel.add(requestVehicleButton, BorderLayout.SOUTH);
        tabbedPane.addTab("Vehículos", vehiclesPanel);

        mainPanel.add(tabbedPane, BorderLayout.CENTER);

        JButton logoutButton = new JButton("Cerrar Sesión");
        logoutButton.addActionListener(e -> {
            new LoginFrame().setVisible(true);
            dispose();
        });
        mainPanel.add(logoutButton, BorderLayout.SOUTH);

        add(mainPanel);
    }

    public static List<String> getAssignedEquipment() {
        return assignedEquipment;
    }

    public static List<String> getAvailableVehicles() {
        return availableVehicles;
    }

    public static List<String> getVehicleRequests() {
        return vehicleRequests;
    }

    public static void updateVehicleStatus(String vehicle, String newStatus) {
        for (int i = 0; i < availableVehicles.size(); i++) {
            if (availableVehicles.get(i).startsWith(vehicle.split(",")[0])) {
                availableVehicles.set(i, availableVehicles.get(i).replaceAll("Estado: \\w+", "Estado: " + newStatus));
                break;
            }
        }
    }
}