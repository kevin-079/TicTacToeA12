import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class tttOption extends JPanel {
    public tttOption(JFrame parentFrame) {
        // Set layout untuk welcome screen
        setLayout(new BorderLayout());
        setBackground(new Color(60, 63, 65)); // Warna latar belakang

        // Judul Screen
        JLabel welcomeLabel = new JLabel("Pick Your Opponent", JLabel.CENTER);
        welcomeLabel.setFont(new Font("Poppins", Font.BOLD, 32));
        welcomeLabel.setForeground(Color.WHITE);
        welcomeLabel.setBorder(BorderFactory.createEmptyBorder(30, 10, 30, 10)); // Padding

        // Panel tombol
        JPanel buttonPanel = new JPanel(new GridLayout(2, 1, 10, 10));
        buttonPanel.setBackground(new Color(60, 63, 65)); // Sama seperti background utama
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 100, 10, 100)); // Padding tombol

        // Tombol untuk PvP
        JButton PvPButton = new JButton("Player VS Player");
        PvPButton.setFont(new Font("Poppins", Font.PLAIN, 20));
        PvPButton.setBackground(new Color(76, 175, 80)); // Warna hijau
        PvPButton.setForeground(Color.WHITE);
        PvPButton.setFocusPainted(false); // Hilangkan highlight saat klik
        PvPButton.addActionListener((ActionEvent e) -> {
            parentFrame.setContentPane(new GameMain("Tic-Tac-Toe"));
            parentFrame.pack();
            parentFrame.setLocationRelativeTo(null);
            parentFrame.validate();
        });

        // Tombol untuk PvC
        JButton PvCButton = new JButton("Player VS Computer");
        PvCButton.setFont(new Font("Poppins", Font.PLAIN, 20));
        PvCButton.setBackground(new Color(33, 150, 243)); // Warna biru
        PvCButton.setForeground(Color.WHITE);
        PvCButton.setFocusPainted(false);
        PvCButton.addActionListener((ActionEvent e) -> {
            parentFrame.setContentPane(new GameMain("Tic-Tac-Toe"));
            parentFrame.pack();
            parentFrame.setLocationRelativeTo(null);
            parentFrame.validate();
        });

        // Tambahkan tombol ke panel
        buttonPanel.add(PvPButton);
        buttonPanel.add(PvCButton);
        // Tambahkan komponen ke panel utama
        add(welcomeLabel, BorderLayout.NORTH); // Judul di atas
        add(buttonPanel, BorderLayout.CENTER); // Tombol di tengah
    }
}
