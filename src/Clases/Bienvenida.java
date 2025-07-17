/*
 * Click nargs://netbeans/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nargs://netbeans/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Clases;

import javax.swing.*;
import java.awt.*;

public class Bienvenida extends JFrame {
    
    public Bienvenida() {
        setTitle("Bienvenida - Net Nexus Ultra");
        setSize(600, 400); // Aumentamos el tamaño a 600x400 píxeles
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // Centra la ventana

        BackgroundPanel mainPanel = new BackgroundPanel("/Imagenes/fondo.png");
        mainPanel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30)); // Aumentamos los márgenes

        // Welcome message
        JLabel welcomeLabel = new JLabel("Bienvenido a Net Nexus Ultra", SwingConstants.CENTER);
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 28)); // Aumentamos la fuente para el nuevo tamaño
        welcomeLabel.setForeground(Color.WHITE); // Texto visible sobre la imagen
        mainPanel.add(welcomeLabel, BorderLayout.NORTH);

        // Buttons panel
        JPanel buttonPanel = new JPanel(new GridLayout(2, 1, 10, 30)); // Aumentamos el espaciado vertical a 30
        buttonPanel.setOpaque(false); // Panel transparente

        // Botón con imagen para Iniciar Sesión
        JButton loginButton = new JButton();
        ImageIcon loginIcon = new ImageIcon(getClass().getResource("/Imagenes/IS-removebg-preview.png"));
        if (loginIcon.getImage() == null) {
            loginButton.setText("Iniciar Sesión"); // Fallback si la imagen no se carga
        } else {
            loginButton.setIcon(loginIcon);
            loginButton.setBorderPainted(false); // Sin borde
            loginButton.setContentAreaFilled(false); // Fondo transparente
        }
        loginButton.addActionListener(e -> {
            new LoginFrame().setVisible(true);
            dispose();
        });

        // Botón con imagen para Registrarse
        JButton registerButton = new JButton();
        ImageIcon registerIcon = new ImageIcon(getClass().getResource("/Imagenes/Registro-removebg-preview.png"));
        if (registerIcon.getImage() == null) {
            registerButton.setText("Registrarse"); // Fallback si la imagen no se carga
        } else {
            registerButton.setIcon(registerIcon);
            registerButton.setBorderPainted(false); // Sin borde
            registerButton.setContentAreaFilled(false); // Fondo transparente
        }
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