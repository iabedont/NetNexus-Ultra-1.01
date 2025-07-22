package GUI_CHIDO;

public class TeleFija extends javax.swing.JFrame {
    
    private javax.swing.JFrame parentFrame;
    private int currentClienteId; // Añadir esta variable

    public TeleFija() {
        initComponents();
        this.setLocationRelativeTo(null); // Centrar la ventana
    }

    public TeleFija(javax.swing.JFrame parentFrame, int clienteId) { // Nuevo constructor
        this(); // Llama al constructor sin argumentos para inicializar componentes
        this.parentFrame = parentFrame;
        this.currentClienteId = clienteId; // Asigna el ID del cliente
    }

    private void initComponents() {
        
        // Inicializar todos los componentes
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jButton4 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Telefonía Fija - NetNexus Ultra");
        setResizable(false);
        
        // Configurar layout
        setLayout(null);
        
        // Configurar labels
        jLabel1.setText("TELEFONÍA FIJA");
        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 24));
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        add(jLabel1);
        jLabel1.setBounds(50, 20, 400, 40);
        
        jLabel2.setText("Plan Básico Fijo");
        jLabel2.setFont(new java.awt.Font("Segoe UI", 1, 16));
        add(jLabel2);
        jLabel2.setBounds(50, 80, 150, 25);
        
        jLabel3.setText("$15.00/mes");
        add(jLabel3);
        jLabel3.setBounds(50, 110, 100, 20);
        
        jLabel4.setText("Plan Premium Fijo");
        jLabel4.setFont(new java.awt.Font("Segoe UI", 1, 16));
        add(jLabel4);
        jLabel4.setBounds(250, 80, 150, 25);
        
        jLabel5.setText("$25.00/mes");
        add(jLabel5);
        jLabel5.setBounds(250, 110, 100, 20);
        
        // Configurar botones
        jButton1.setText("Contratar Plan Básico");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        add(jButton1);
        jButton1.setBounds(50, 200, 150, 30);
        
        jButton2.setText("Regresar");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });
        add(jButton2);
        jButton2.setBounds(50, 300, 100, 30);
        
        jButton3.setText("Contratar Plan Premium");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });
        add(jButton3);
        jButton3.setBounds(250, 200, 150, 30);
        
        jButton4.setText("Ver Detalles");
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });
        add(jButton4);
        jButton4.setBounds(200, 300, 100, 30);
        
        // Configurar ventana
        setSize(500, 400);
        setLocationRelativeTo(null);
    }

    // Asegúrate de tener un método para regresar si tu UI lo requiere
    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {
        this.setVisible(false);
        if (parentFrame != null) {
            parentFrame.setVisible(true);
        }
    }

    // Método para contratar plan básico
    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {
        Facturación facturacionFrame = new Facturación("Plan Básico Fijo", 15.00, currentClienteId);
        facturacionFrame.setParentFrame(this);
        facturacionFrame.setVisible(true);
        this.setVisible(false);
    }
    
    // Método para contratar plan premium
    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {
        Facturación facturacionFrame = new Facturación("Plan Premium Fijo", 25.00, currentClienteId);
        facturacionFrame.setParentFrame(this);
        facturacionFrame.setVisible(true);
        this.setVisible(false);
    }
    
    // Método para ver detalles
    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {
        javax.swing.JOptionPane.showMessageDialog(this, 
            "PLANES DE TELEFONÍA FIJA:\n\n" +
            "Plan Básico ($15.00/mes):\n" +
            "- Llamadas locales ilimitadas\n" +
            "- 100 minutos nacionales\n" +
            "- Buzón de voz\n\n" +
            "Plan Premium ($25.00/mes):\n" +
            "- Llamadas locales y nacionales ilimitadas\n" +
            "- 50 minutos internacionales\n" +
            "- Buzón de voz premium\n" +
            "- Identificador de llamadas", 
            "Detalles de Planes", 
            javax.swing.JOptionPane.INFORMATION_MESSAGE);
    }

    public static void main(String args[]) {
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ReflectiveOperationException | javax.swing.UnsupportedLookAndFeelException ex) {
            System.err.println("Error setting look and feel: " + ex.getMessage());
        }

        java.awt.EventQueue.invokeLater(() -> new TeleFija().setVisible(true));
    }

    // Declaración de variables (asegúrate de que coincida con tu .form)
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
}
