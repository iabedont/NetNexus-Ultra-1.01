/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Clases;

import javax.swing.*;
import java.awt.*;

public class Bienvenida extends JFrame {
    
    public Bienvenida() {
        setTitle("Bienvenida - Net Nexus Ultra");
        setSize(400, 250);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        BackgroundPanel mainPanel = new BackgroundPanel("/Imagenes/fondo.png");
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Welcome message
        JLabel welcomeLabel = new JLabel("Bienvenido a Net Nexus Ultra", SwingConstants.CENTER);
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 20));
        welcomeLabel.setForeground(Color.WHITE); // Ensure text is visible on image
        mainPanel.add(welcomeLabel, BorderLayout.NORTH);

        // Buttons panel
        JPanel buttonPanel = new JPanel(new GridLayout(2, 1, 10, 10));
        buttonPanel.setOpaque(false); // Make button panel transparent
        
        JButton loginButton = new JButton("Iniciar Sesión");
        loginButton.setFont(new Font("Arial", Font.PLAIN, 14));
        loginButton.addActionListener(e -> {
            new LoginFrame().setVisible(true);
            dispose();
        });
        
        JButton registerButton = new JButton("Registrarse");
        registerButton.setFont(new Font("Arial", Font.PLAIN, 14));
        registerButton.addActionListener(e -> {
            new Registro().setVisible(true);
            dispose();
        });
        
        buttonPanel.add(loginButton);
        buttonPanel.add(registerButton);
        
        mainPanel.add(buttonPanel, BorderLayout.CENTER);
        
        add(mainPanel);
    }
}
