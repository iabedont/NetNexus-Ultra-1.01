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

public class AdminFrame extends JFrame {
    private static List<Factura> invoices = new ArrayList<>();

    public AdminFrame() {
        setTitle("Panel de Administrador");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        JTabbedPane tabbedPane = new JTabbedPane();

        // Pestaña de Datos del Usuario
        JPanel userDataPanel = new JPanel(new BorderLayout());
        JTextArea dataArea = new JTextArea(10, 40);
        dataArea.setEditable(false);
        for (String data : UserFrame.getUserData()) {
            dataArea.append(data + "\n");
        }
        userDataPanel.add(new JScrollPane(dataArea), BorderLayout.CENTER);
        tabbedPane.addTab("Datos del Usuario", userDataPanel);

        // Pestaña de Equipos de Técnicos
        JPanel equipmentPanel = new JPanel(new BorderLayout());
        JTextArea equipmentArea = new JTextArea(10, 40);
        equipmentArea.setEditable(false);
        // Materiales típicos de una empresa de fibra óptica
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
            equipmentArea.append(item + "\n");
        }
        equipmentPanel.add(new JScrollPane(equipmentArea), BorderLayout.CENTER);
        tabbedPane.addTab("Equipos de Técnicos", equipmentPanel);

        // Pestaña de Tickets
        JPanel ticketsPanel = new JPanel(new BorderLayout());
        DefaultListModel<String> ticketModel = new DefaultListModel<>();
        for (Ticket ticket : UserFrame.getUserTickets()) {
            ticketModel.addElement("Ticket ID: " + ticket.getIdTicket() + ", Servicio: " + ticket.getDescripcion() + ", Prioridad: " + ticket.getPrioridad() + ", Estado: " + ticket.getEstado());
        }
        JList<String> ticketsList = new JList<>(ticketModel);
        ticketsPanel.add(new JScrollPane(ticketsList), BorderLayout.CENTER);
        JPanel ticketActionsPanel = new JPanel(new GridLayout(3, 2));
        String[] priorities = {"Baja", "Media", "Alta"};
        JComboBox<String> priorityCombo = new JComboBox<>(priorities);
        String[] statuses = {"Pendiente", "En Progreso", "Finalizado"};
        JComboBox<String> statusCombo = new JComboBox<>(statuses);
        ticketActionsPanel.add(new JLabel("Prioridad:"));
        ticketActionsPanel.add(priorityCombo);
        ticketActionsPanel.add(new JLabel("Estado:"));
        ticketActionsPanel.add(statusCombo);
        JButton updateTicketButton = new JButton("Actualizar Ticket");
        updateTicketButton.addActionListener(e -> {
            int selectedIndex = ticketsList.getSelectedIndex();
            if (selectedIndex >= 0) {
                Ticket ticket = UserFrame.getUserTickets().get(selectedIndex);
                ticket.setPrioridad((String) priorityCombo.getSelectedItem());
                ticket.setEstado((String) statusCombo.getSelectedItem());
                ticketModel.set(selectedIndex, "Ticket ID: " + ticket.getIdTicket() + ", Servicio: " + ticket.getDescripcion() + ", Prioridad: " + ticket.getPrioridad() + ", Estado: " + ticket.getEstado());
                if (ticket.getEstado().equals("Finalizado")) {
                    Factura factura = new Factura(invoices.size() + 1, new java.sql.Date(System.currentTimeMillis()), 100.0, "Pendiente", "Efectivo");
                    invoices.add(factura);
                    JOptionPane.showMessageDialog(this, "Factura generada: ID " + factura.getIdFactura() + ", Monto: $" + factura.getMonto());
                }
            }
        });
        ticketActionsPanel.add(updateTicketButton);
        ticketsPanel.add(ticketActionsPanel, BorderLayout.SOUTH);
        tabbedPane.addTab("Tickets", ticketsPanel);

        // Pestaña de Vehículos
        JPanel vehiclesPanel = new JPanel(new BorderLayout());
        DefaultListModel<String> vehicleModel = new DefaultListModel<>();
        for (String vehicle : TechnicianFrame.getAvailableVehicles()) {
            vehicleModel.addElement(vehicle);
        }
        JList<String> vehiclesList = new JList<>(vehicleModel);
        vehiclesPanel.add(new JScrollPane(vehiclesList), BorderLayout.CENTER);

        JPanel vehicleRequestsPanel = new JPanel(new BorderLayout());
        DefaultListModel<String> requestsModel = new DefaultListModel<>();
        for (String request : TechnicianFrame.getVehicleRequests()) {
            requestsModel.addElement(request);
        }
        JList<String> requestsList = new JList<>(requestsModel);
        vehicleRequestsPanel.add(new JScrollPane(requestsList), BorderLayout.CENTER);

        JPanel requestActionsPanel = new JPanel(new GridLayout(1, 2));
        JButton acceptRequestButton = new JButton("Aceptar Solicitud");
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

        JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, new JScrollPane(vehiclesList), vehicleRequestsPanel);
        splitPane.setDividerLocation(150);
        vehiclesPanel.add(splitPane, BorderLayout.CENTER);
        tabbedPane.addTab("Vehículos", vehiclesPanel);

        add(tabbedPane, BorderLayout.CENTER);

        JButton logoutButton = new JButton("Cerrar Sesión");
        logoutButton.addActionListener(e -> {
            new LoginFrame().setVisible(true);
            dispose();
        });
        add(logoutButton, BorderLayout.SOUTH);
    }
}