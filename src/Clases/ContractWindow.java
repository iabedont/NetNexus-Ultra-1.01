package Clases;

import java.awt.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

/**
 * Ventana para mostrar contratos activos en el panel de administración
 */
public class ContractWindow extends JFrame {
    
    private static final Logger logger = Logger.getLogger(ContractWindow.class.getName());
    
    public ContractWindow() {
        setTitle("Contratos Activos - NetNexus Ultra");
        setSize(900, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        // Panel principal con fondo
        BackgroundPanel mainPanel = new BackgroundPanel("/Imagenes/fondo.png");
        mainPanel.setLayout(new BorderLayout());

        // Panel de título
        JPanel titlePanel = new JPanel();
        titlePanel.setOpaque(false);
        titlePanel.setBorder(new EmptyBorder(20, 0, 10, 0));
        
        JLabel titleLabel = new JLabel("Contratos Activos", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 28));
        titleLabel.setForeground(Color.WHITE);
        titlePanel.add(titleLabel);

        // Panel de contenido con scroll
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setOpaque(false);
        
        JScrollPane scrollPane = new JScrollPane(contentPanel);
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);
        scrollPane.setBorder(null);

        // Cargar contratos activos desde la base de datos
        loadActiveContracts(contentPanel);

        // Botón de actualizar
        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.setOpaque(false);
        
        JButton refreshButton = new JButton("Actualizar");
        refreshButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        refreshButton.setBackground(new Color(0, 123, 255));
        refreshButton.setForeground(Color.WHITE);
        refreshButton.setFocusPainted(false);
        refreshButton.setBorderPainted(false);
        refreshButton.addActionListener(e -> {
            contentPanel.removeAll();
            loadActiveContracts(contentPanel);
            contentPanel.revalidate();
            contentPanel.repaint();
        });
        
        buttonPanel.add(refreshButton);

        mainPanel.add(titlePanel, BorderLayout.NORTH);
        mainPanel.add(scrollPane, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        add(mainPanel);
    }

    private void loadActiveContracts(JPanel panel) {
        try {
            // Obtener contratos activos de la base de datos
            var connection = DatabaseConnection.getConnection();
            var statement = connection.createStatement();
            var resultSet = statement.executeQuery(
                "SELECT c.idContrato, cl.nombre, cl.apellido, c.tiposervicio, c.monto_total, " +
                "c.fecha_inicio, c.fecha_fin FROM contrato c " +
                "JOIN cliente cl ON c.Cliente_idCliente = cl.idCliente " +
                "WHERE c.fecha_fin > CURDATE() " +
                "ORDER BY c.fecha_inicio DESC"
            );

            int contractCount = 0;
            while (resultSet.next()) {
                contractCount++;
                JPanel contractPanel = new JPanel(new BorderLayout());
                contractPanel.setOpaque(false);
                contractPanel.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createEmptyBorder(5, 10, 5, 10),
                    BorderFactory.createLineBorder(Color.WHITE, 2, true)
                ));
                contractPanel.setPreferredSize(new Dimension(800, 80));
                contractPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 80));

                // Información del contrato
                JLabel infoLabel = new JLabel(String.format(
                    "<html><b>Contrato #%d</b> - %s %s<br>" +
                    "Servicio: %s | Monto: $%.2f<br>" +
                    "Vigencia: %s - %s</html>",
                    resultSet.getInt("idContrato"),
                    resultSet.getString("nombre"),
                    resultSet.getString("apellido"),
                    resultSet.getString("tiposervicio"),
                    resultSet.getDouble("monto_total"),
                    resultSet.getString("fecha_inicio"),
                    resultSet.getString("fecha_fin")
                ));
                infoLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
                infoLabel.setForeground(Color.BLACK);
                infoLabel.setOpaque(true);
                infoLabel.setBackground(new Color(255, 255, 255, 200));
                infoLabel.setBorder(new EmptyBorder(10, 15, 10, 15));

                contractPanel.add(infoLabel, BorderLayout.CENTER);
                panel.add(contractPanel);
            }

            // Si no hay contratos activos
            if (contractCount == 0) {
                JLabel noContractsLabel = new JLabel("No hay contratos activos en este momento");
                noContractsLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
                noContractsLabel.setForeground(Color.WHITE);
                noContractsLabel.setHorizontalAlignment(SwingConstants.CENTER);
                panel.add(noContractsLabel);
            }

            resultSet.close();
            statement.close();
            
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error al cargar contratos activos", e);
            JLabel errorLabel = new JLabel("Error al cargar contratos: " + e.getMessage());
            errorLabel.setForeground(Color.RED);
            errorLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
            panel.add(errorLabel);
        }
    }
}
