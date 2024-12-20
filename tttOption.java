import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class tttOption extends JPanel {
    public tttOption(JFrame parentFrame) {
        // Set layout untuk welcome screen
        setLayout(new BorderLayout());
        setBackground(new Color(60, 63, 65)); // Warna latar belakang

        // Judul Screen
        JLabel welcomeLabel = new JLabel("Pick Your Board", JLabel.CENTER);
        welcomeLabel.setFont(new Font("Poppins", Font.BOLD, 32));
        welcomeLabel.setForeground(Color.WHITE);
        welcomeLabel.setBorder(BorderFactory.createEmptyBorder(30, 10, 30, 10)); // Padding

        // Panel tombol
        JPanel buttonPanel = new JPanel(new GridLayout(2, 1, 10, 10));
        buttonPanel.setBackground(new Color(60, 63, 65)); // Sama seperti background utama
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 100, 10, 100)); // Padding tombol

        // Tombol untuk 5x5
        JButton txtButton = new JButton("3 X 3");
        txtButton.setFont(new Font("Poppins", Font.PLAIN, 20));
        txtButton.setBackground(new Color(76, 175, 80)); // Warna hijau
        txtButton.setForeground(Color.WHITE);
        txtButton.setFocusPainted(false); // Hilangkan highlight saat klik
        txtButton.addActionListener((ActionEvent e) -> {
            parentFrame.setContentPane(new GameMain("Tic-Tac-Toe 3x3"));
            parentFrame.pack();
            parentFrame.setLocationRelativeTo(null);
            parentFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            parentFrame.validate();
        });

        // Tombol untuk 5x5
        JButton fxfButton = new JButton("5 X 5");
        fxfButton.setFont(new Font("Poppins", Font.PLAIN, 20));
        fxfButton.setBackground(new Color(33, 150, 243)); // Warna biru
        fxfButton.setForeground(Color.WHITE);
        fxfButton.setFocusPainted(false);
        fxfButton.addActionListener((ActionEvent e) -> {
            parentFrame.setContentPane(new GameMain("Tic-Tac-Toe 5x5"));
            parentFrame.pack();
            parentFrame.setLocationRelativeTo(null);
            parentFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            parentFrame.validate();

        });

        // Tambahkan tombol ke panel
        buttonPanel.add(txtButton);
        buttonPanel.add(fxfButton);
        // Tambahkan komponen ke panel utama
        add(welcomeLabel, BorderLayout.NORTH); // Judul di atas
        add(buttonPanel, BorderLayout.CENTER); // Tombol di tengah
    }
}
