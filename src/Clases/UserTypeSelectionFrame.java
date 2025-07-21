package Clases;

import java.awt.*;
import javax.swing.*;

public class UserTypeSelectionFrame extends JFrame {
    
    public UserTypeSelectionFrame() {
        setTitle("Tipo de Usuario - NetNexus Ultra");
        setSize(500, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        BackgroundPanel mainPanel = new BackgroundPanel("/Imagenes/fondo.png");
        mainPanel.setLayout(new BorderLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));

        // Título
        JLabel titleLabel = new JLabel("¿Cómo desea ingresar?", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setForeground(Color.WHITE);
        mainPanel.add(titleLabel, BorderLayout.NORTH);

        // Panel de botones
        JPanel buttonPanel = new JPanel(new GridLayout(1, 3, 20, 0));
        buttonPanel.setOpaque(false);
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(50, 20, 50, 20));

        // Botón Usuario
        JButton userButton = new JButton("Usuario");
        userButton.setFont(new Font("Arial", Font.BOLD, 16));
        userButton.setPreferredSize(new Dimension(120, 80));
        userButton.setBackground(new Color(70, 130, 180));
        userButton.setForeground(Color.WHITE);
        userButton.setFocusPainted(false);
        userButton.addActionListener(e -> openLoginFrame("Usuario"));

        // Botón Administrador
        JButton adminButton = new JButton("Administrador");
        adminButton.setFont(new Font("Arial", Font.BOLD, 16));
        adminButton.setPreferredSize(new Dimension(120, 80));
        adminButton.setBackground(new Color(220, 20, 60));
        adminButton.setForeground(Color.WHITE);
        adminButton.setFocusPainted(false);
        adminButton.addActionListener(e -> openLoginFrame("Administrador"));

        // Botón Técnico
        JButton techButton = new JButton("Técnico");
        techButton.setFont(new Font("Arial", Font.BOLD, 16));
        techButton.setPreferredSize(new Dimension(120, 80));
        techButton.setBackground(new Color(34, 139, 34));
        techButton.setForeground(Color.WHITE);
        techButton.setFocusPainted(false);
        techButton.addActionListener(e -> openLoginFrame("Técnico"));

        buttonPanel.add(userButton);
        buttonPanel.add(adminButton);
        buttonPanel.add(techButton);

        mainPanel.add(buttonPanel, BorderLayout.CENTER);

        // Botón Atrás
        JPanel bottomPanel = new JPanel(new FlowLayout());
        bottomPanel.setOpaque(false);
        JButton backButton = new JButton("Atrás");
        backButton.setFont(new Font("Arial", Font.PLAIN, 14));
        backButton.addActionListener(e -> {
            new Bienvenida().setVisible(true);
            dispose();
        });
        bottomPanel.add(backButton);
        mainPanel.add(bottomPanel, BorderLayout.SOUTH);

        add(mainPanel);
    }

    private void openLoginFrame(String userType) {
        new SpecificLoginFrame(userType).setVisible(true);
        dispose();
    }
}
