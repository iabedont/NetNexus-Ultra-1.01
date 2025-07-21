package GUI_CHIDO;

import Clases.Cliente;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Versión temporal simplificada de User_1 para resolver problema de login
 * @author ASUS
 */
public class User_1_Temp extends JFrame {
    
    private Cliente currentUser;
    
    public User_1_Temp(Cliente cliente) {
        this.currentUser = cliente;
        initComponents();
    }
    
    private void initComponents() {
        setTitle("Panel de Usuario - " + currentUser.getNombre());
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        
        JPanel mainPanel = new JPanel(new BorderLayout());
        
        // Panel superior con información del usuario
        JPanel topPanel = new JPanel(new FlowLayout());
        topPanel.setBackground(new Color(70, 130, 180));
        
        JLabel welcomeLabel = new JLabel("Bienvenido: " + currentUser.getNombre() + " " + currentUser.getApellido());
        welcomeLabel.setForeground(Color.WHITE);
        welcomeLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        topPanel.add(welcomeLabel);
        
        // Panel central con opciones
        JPanel centerPanel = new JPanel(new GridLayout(3, 2, 20, 20));
        centerPanel.setBorder(BorderFactory.createEmptyBorder(50, 50, 50, 50));
        
        JButton serviciosBtn = new JButton("Servicios");
        serviciosBtn.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        serviciosBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(User_1_Temp.this, 
                    "Función de Servicios en desarrollo.\nSe abrirá cuando User_1 completo esté listo.", 
                    "Servicios", 
                    JOptionPane.INFORMATION_MESSAGE);
            }
        });
        
        JButton facturacionBtn = new JButton("Facturación");
        facturacionBtn.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        facturacionBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Abrir ventana de facturación con el constructor correcto
                GUI_CHIDO.Facturación facturacionFrame = new GUI_CHIDO.Facturación("Plan Estándar", 45.99, currentUser.getIdCliente());
                facturacionFrame.setParentFrame(User_1_Temp.this);
                facturacionFrame.setVisible(true);
            }
        });
        
        JButton contratosBtn = new JButton("Contratos Activos");
        contratosBtn.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        contratosBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(User_1_Temp.this, 
                    "Función de Contratos en desarrollo.\nSe abrirá cuando User_1 completo esté listo.", 
                    "Contratos Activos", 
                    JOptionPane.INFORMATION_MESSAGE);
            }
        });
        
        JButton perfilBtn = new JButton("Mi Perfil");
        perfilBtn.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        perfilBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String info = "Información del Usuario:\n\n" +
                            "ID: " + currentUser.getIdCliente() + "\n" +
                            "Nombre: " + currentUser.getNombre() + "\n" +
                            "Apellido: " + currentUser.getApellido() + "\n" +
                            "Teléfono: " + currentUser.getTelefono() + "\n" +
                            "Email: " + currentUser.getEmail();
                JOptionPane.showMessageDialog(User_1_Temp.this, info, "Mi Perfil", JOptionPane.INFORMATION_MESSAGE);
            }
        });
        
        JButton internetBtn = new JButton("Internet");
        internetBtn.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        internetBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(User_1_Temp.this, 
                    "Función de Internet en desarrollo.\nSe abrirá cuando User_1 completo esté listo.", 
                    "Internet", 
                    JOptionPane.INFORMATION_MESSAGE);
            }
        });
        
        JButton logoutBtn = new JButton("Cerrar Sesión");
        logoutBtn.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        logoutBtn.setBackground(new Color(220, 20, 60));
        logoutBtn.setForeground(Color.WHITE);
        logoutBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int response = JOptionPane.showConfirmDialog(User_1_Temp.this, 
                    "¿Está seguro que desea cerrar sesión?", 
                    "Confirmar", 
                    JOptionPane.YES_NO_OPTION);
                if (response == JOptionPane.YES_OPTION) {
                    dispose();
                    // Cerrar aplicación por ahora - LoginFrame será compilado después
                    System.exit(0);
                }
            }
        });
        
        centerPanel.add(serviciosBtn);
        centerPanel.add(facturacionBtn);
        centerPanel.add(contratosBtn);
        centerPanel.add(perfilBtn);
        centerPanel.add(internetBtn);
        centerPanel.add(logoutBtn);
        
        mainPanel.add(topPanel, BorderLayout.NORTH);
        mainPanel.add(centerPanel, BorderLayout.CENTER);
        
        add(mainPanel);
    }
    
    // Constructor alternativo para compatibilidad
    public User_1_Temp() {
        this(new Cliente(0, "Usuario", "Temporal", "000-000-0000", "temp@email.com", "temp"));
    }
}
