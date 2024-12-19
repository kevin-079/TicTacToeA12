import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class GameSelector extends JPanel {
    public GameSelector(JFrame parentFrame) {
        // Set layout untuk welcome screen
        setLayout(new BorderLayout());
        setBackground(new Color(60, 63, 65)); // Warna latar belakang

        // Judul Welcome Screen
        JLabel welcomeLabel = new JLabel("Welcome to the Game!", JLabel.CENTER);
        welcomeLabel.setFont(new Font("Poppins", Font.BOLD, 32));
        welcomeLabel.setForeground(Color.WHITE);
        welcomeLabel.setBorder(BorderFactory.createEmptyBorder(30, 10, 30, 10)); // Padding

        // Panel tombol
        JPanel buttonPanel = new JPanel(new GridLayout(2, 1, 10, 10));
        buttonPanel.setBackground(new Color(60, 63, 65)); // Sama seperti background utama
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 100, 10, 100)); // Padding tombol

        // Tombol untuk Tic-Tac-Toe
        JButton tttButton = new JButton("Play Tic-Tac-Toe");
        tttButton.setFont(new Font("Poppins", Font.PLAIN, 20));
        tttButton.setBackground(new Color(76, 175, 80)); // Warna hijau
        tttButton.setForeground(Color.WHITE);
        tttButton.setFocusPainted(false); // Hilangkan highlight saat klik
        tttButton.addActionListener((ActionEvent e) -> {
            // Dialog untuk memilih mode permainan
            int response = JOptionPane.showOptionDialog(
                    parentFrame,
                    "Pilih mode permainan untuk Tic-Tac-Toe:",
                    "Mode Permainan",
                    JOptionPane.DEFAULT_OPTION,
                    JOptionPane.QUESTION_MESSAGE,
                    null,
                    new Object[] {"1P (vs AI)", "2P (vs Player)"},
                    "1P (vs AI)"
            );

            if (response == 0) { // Mode 1P (vs AI)
                parentFrame.setContentPane(new TTTGraphics(true)); // true untuk AI
                parentFrame.pack();
                parentFrame.setLocationRelativeTo(null);
                parentFrame.validate();
            } else if (response == 1) { // Mode 2P (vs Player)
                parentFrame.setContentPane(new tttOption(parentFrame)); // Masuk ke opsi 2P
                parentFrame.pack();
                parentFrame.setLocationRelativeTo(null);
                parentFrame.validate();
            }
        });

        // Tombol untuk Connect Four
        JButton cfButton = new JButton("Play Connect Four");
        cfButton.setFont(new Font("Poppins", Font.PLAIN, 20));
        cfButton.setBackground(new Color(33, 150, 243)); // Warna biru
        cfButton.setForeground(Color.WHITE);
        cfButton.setFocusPainted(false);
        cfButton.addActionListener((ActionEvent e) -> {
            parentFrame.setContentPane(new GameMain("Connect Four"));
            parentFrame.pack();
            parentFrame.setLocationRelativeTo(null);
            parentFrame.validate();
        });

        // Tambahkan tombol ke panel
        buttonPanel.add(tttButton);
        buttonPanel.add(cfButton);
        // Tambahkan komponen ke panel utama
        add(welcomeLabel, BorderLayout.NORTH); // Judul di atas
        add(buttonPanel, BorderLayout.CENTER); // Tombol di tengah
    }
}
