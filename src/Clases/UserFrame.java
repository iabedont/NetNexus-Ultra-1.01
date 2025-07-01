/*
 * Click nfs://netbeans/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nfs://netbeans/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Clases;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserFrame extends JFrame {
    private static List<String> userData = new ArrayList<>();
    private static List<Contrato> userContracts = new ArrayList<>();
    private static List<Ticket> userTickets = new ArrayList<>();

    public UserFrame() {
        setTitle("Panel de Usuario");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        BackgroundPanel mainPanel = new BackgroundPanel("/Imagenes/fondo.png");
        mainPanel.setLayout(new BorderLayout());

        JTabbedPane tabbedPane = new JTabbedPane();

        // Pestaña de Contratos
        JPanel contractsPanel = new JPanel(new BorderLayout());
        contractsPanel.setOpaque(false);
        DefaultListModel<String> contractModel = new DefaultListModel<>();
        if (userContracts.isEmpty()) {
            userContracts.add(new Contrato(1, 1, Date.valueOf("2025-01-01"), Date.valueOf("2025-12-31"), 1200.0));
        }
        for (Contrato contrato : userContracts) {
            contractModel.addElement("Contrato ID: " + contrato.getIdContrato() + ", Inicio: " + contrato.getFechaInicio() + ", Fin: " + contrato.getFechaFin() + ", Monto: $" + contrato.getMontoTotal());
        }
        JList<String> contractsList = new JList<>(contractModel);
        contractsPanel.add(new JScrollPane(contractsList), BorderLayout.CENTER);
        JPanel contractActionsPanel = new JPanel(new GridLayout(1, 2));
        contractActionsPanel.setOpaque(false);
        JButton cancelContractButton = new JButton("Cancelar Contrato");
        cancelContractButton.addActionListener(e -> {
            int selectedIndex = contractsList.getSelectedIndex();
            if (selectedIndex >= 0) {
                userContracts.remove(selectedIndex);
                contractModel.remove(selectedIndex);
                JOptionPane.showMessageDialog(this, "Contrato cancelado con éxito");
            } else {
                JOptionPane.showMessageDialog(this, "Por favor selecciona un contrato", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
        JButton addContractButton = new JButton("Agregar Contrato");
        addContractButton.addActionListener(e -> showContractOptions(contractModel));
        contractActionsPanel.add(cancelContractButton);
        contractActionsPanel.add(addContractButton);
        contractsPanel.add(contractActionsPanel, BorderLayout.SOUTH);
        tabbedPane.addTab("Contratos", contractsPanel);

        // Pestaña de Servicios
        JPanel servicesPanel = new JPanel(new BorderLayout());
        servicesPanel.setOpaque(false);
        String[] serviceTypes = {"Mantenimiento de Router", "Cambio de Router", "Otros"};
        JList<String> servicesList = new JList<>(serviceTypes);
        servicesList.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (servicesList.getSelectedValue().equals("Otros")) {
                    JTextArea otherServiceArea = new JTextArea(5, 20);
                    int result = JOptionPane.showConfirmDialog(null, new JScrollPane(otherServiceArea), "Detalla el Servicio Deseado", JOptionPane.OK_CANCEL_OPTION);
                    if (result == JOptionPane.OK_OPTION) {
                        String detailedService = otherServiceArea.getText();
                        if (!detailedService.trim().isEmpty()) {
                            Ticket ticket = new Ticket(userTickets.size() + 1, 1, new Date(System.currentTimeMillis()), "Otros: " + detailedService, "Media", "Pendiente");
                            userTickets.add(ticket);
                            userData.add("Usuario solicitó servicio: Otros - " + detailedService);
                            JOptionPane.showMessageDialog(UserFrame.this, "Servicio solicitado. Ticket generado: " + ticket.getIdTicket());
                        }
                    }
                }
            }
        });
        servicesPanel.add(new JScrollPane(servicesList), BorderLayout.CENTER);
        JButton requestServiceButton = new JButton("Solicitar Servicio");
        requestServiceButton.addActionListener(e -> {
            String selectedService = servicesList.getSelectedValue();
            if (selectedService != null && !selectedService.equals("Otros")) {
                Ticket ticket = new Ticket(userTickets.size() + 1, 1, new Date(System.currentTimeMillis()), selectedService, "Media", "Pendiente");
                userTickets.add(ticket);
                userData.add("Usuario solicitó servicio: " + selectedService);
                JOptionPane.showMessageDialog(this, "Servicio solicitado. Ticket generado: " + ticket.getIdTicket());
            } else if (!selectedService.equals("Otros")) {
                JOptionPane.showMessageDialog(this, "Por favor selecciona un servicio", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
        servicesPanel.add(requestServiceButton, BorderLayout.SOUTH);
        tabbedPane.addTab("Servicios", servicesPanel);

        // Pestaña de Información del Usuario
        JPanel infoPanel = new JPanel(new BorderLayout());
        infoPanel.setOpaque(false);
        JTextArea infoArea = new JTextArea(10, 40);
        infoArea.setEditable(false);
        Cliente usuario = new Cliente(1, "Juan", "Pérez", "0991234567", "juan.perez@example.com");
        infoArea.append("ID Cliente: " + usuario.getIdCliente() + "\n");
        infoArea.append("Nombre: " + usuario.getNombre() + "\n");
        infoArea.append("Apellido: " + usuario.getApellido() + "\n");
        infoArea.append("Teléfono: " + usuario.getTelefono() + "\n");
        infoArea.append("Email: " + usuario.getEmail() + "\n");
        infoArea.append("Ubicación: Ibarra, Imbabura\n");
        infoPanel.add(new JScrollPane(infoArea), BorderLayout.CENTER);
        tabbedPane.addTab("Información del Usuario", infoPanel);

        mainPanel.add(tabbedPane, BorderLayout.CENTER);

        JButton logoutButton = new JButton("Cerrar Sesión");
        logoutButton.addActionListener(e -> {
            new LoginFrame().setVisible(true);
            dispose();
        });
        mainPanel.add(logoutButton, BorderLayout.SOUTH);

        add(mainPanel);
    }

    private void showContractOptions(DefaultListModel<String> contractModel) {
        String[] contractOptions = {
            "Contrato de 1 año - $1,000",
            "Contrato de 5 años - $10,000",
            "Contrato de 15 años - $15,000"
        };
        String selectedOption = (String) JOptionPane.showInputDialog(
            this,
            "Selecciona un contrato para agregar:",
            "Agregar Contrato",
            JOptionPane.PLAIN_MESSAGE,
            null,
            contractOptions,
            contractOptions[0]
        );

        if (selectedOption != null) {
            int id = userContracts.size() + 1;
            java.sql.Date fechaInicio = new java.sql.Date(System.currentTimeMillis());
            java.sql.Date fechaFin;
            double monto;

            switch (selectedOption) {
                case "Contrato de 1 año - $1,000":
                    fechaFin = new java.sql.Date(fechaInicio.getTime() + (365L * 24 * 60 * 60 * 1000));
                    monto = 1000.0;
                    break;
                case "Contrato de 5 años - $10,000":
                    fechaFin = new java.sql.Date(fechaInicio.getTime() + (5L * 365 * 24 * 60 * 60 * 1000));
                    monto = 10000.0;
                    break;
                case "Contrato de 15 años - $15,000":
                    fechaFin = new java.sql.Date(fechaInicio.getTime() + (15L * 365 * 24 * 60 * 60 * 1000));
                    monto = 15000.0;
                    break;
                default:
                    return;
            }

            Contrato newContract = new Contrato(id, 1, fechaInicio, fechaFin, monto);
            userContracts.add(newContract);
            contractModel.addElement("Contrato ID: " + newContract.getIdContrato() + ", Inicio: " + newContract.getFechaInicio() + ", Fin: " + newContract.getFechaFin() + ", Monto: $" + newContract.getMontoTotal());
            JOptionPane.showMessageDialog(this, "Contrato agregado con éxito");
        }
    }

    public static List<String> getUserData() {
        return userData;
    }

    public static List<Ticket> getUserTickets() {
        return userTickets;
    }
}