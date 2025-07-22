package GUI_CHIDO;

import java.awt.*;
import javax.swing.*;

public class LoadingScreen extends JDialog {

    public LoadingScreen(Frame owner, boolean modal) {
        super(owner, modal);
        initComponents();
    }

    private void initComponents() {
        setUndecorated(true); // Sin bordes ni barra de título
        setResizable(false);
        setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE); // No permitir cerrar con la X

        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(0, 0, 0, 150)); // Fondo semi-transparente oscuro
        
        JLabel loadingLabel = new JLabel("Verificando tarjeta...", SwingConstants.CENTER);
        loadingLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        loadingLabel.setForeground(Color.WHITE);
        panel.add(loadingLabel, BorderLayout.CENTER);

        // Opcional: Añadir un GIF de carga o un JProgressBar
        // ImageIcon loadingGif = new ImageIcon(getClass().getResource("/Imagenes/loading.gif"));
        // JLabel gifLabel = new JLabel(loadingGif);
        // panel.add(gifLabel, BorderLayout.NORTH);

        add(panel);
        setSize(300, 150); // Tamaño del diálogo
        setLocationRelativeTo(getOwner()); // Centrar respecto a la ventana principal
    }
}
