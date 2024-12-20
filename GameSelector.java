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
                // Dialog untuk memilih tingkat kesulitan
                String[] difficulties = {"Easy", "Hard"};
                int difficultyResponse = JOptionPane.showOptionDialog(
                        parentFrame,
                        "Pilih tingkat kesulitan untuk AI:",
                        "Tingkat Kesulitan",
                        JOptionPane.DEFAULT_OPTION,
                        JOptionPane.QUESTION_MESSAGE,
                        null,
                        difficulties,
                        "Easy"
                );

                String selectedDifficulty = (difficultyResponse == 0) ? "Easy" : "Hard";
                new TTTGraphics(true, selectedDifficulty); // Menyesuaikan konstruktor baru
                parentFrame.dispose();
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

    public GameSelector(JFrame parentFrame, State i, int game) {

        SoundEffect.EXPLODE.play();

        // Set layout untuk end screen
        setLayout(new BorderLayout());
        setBackground(new Color(60, 63, 65)); // Warna latar belakang



        String message = "";
        if(i == State.DRAW) {
            message = "It's a Draw";
        } else if(i == State.NOUGHT_WON) {
            message = " Player O is The Winner";
        } else if(i == State.CROSS_WON) {
            message = " Player X is The Winner";
        }

        // gameType memory
        String gameType = "";
        if (game == 2) {
            gameType = "Tic-Tac-Toe 3x3";
        } else if (game == 3) {
            gameType = "Tic-Tac-Toe 5x5";
        } else if (game == 4) {
            gameType = "Connect Four";
        }

        JLabel messageLabel = new JLabel(message, JLabel.CENTER);
        messageLabel.setFont(new Font("Poppins", Font.BOLD, 32));
        messageLabel.setForeground(Color.WHITE);
        messageLabel.setBorder(BorderFactory.createEmptyBorder(30, 10, 30, 10));

        // Judul Screen
        JLabel choiceLabel = new JLabel("What's Next?", JLabel.CENTER);
        choiceLabel.setFont(new Font("Poppins", Font.BOLD, 32));
        choiceLabel.setForeground(Color.WHITE);
        choiceLabel.setBorder(BorderFactory.createEmptyBorder(30, 10, 30, 10)); // Padding

        // Panel tombol
        JPanel buttonPanel = new JPanel(new GridLayout(4, 1, 10, 10));
        buttonPanel.setBackground(new Color(60, 63, 65)); // Sama seperti background utama
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 100, 10, 100)); // Padding tombol

        // Tombol untuk PvP
        JButton newGamesButton = new JButton("Change Game");
        newGamesButton.setFont(new Font("Poppins", Font.PLAIN, 20));
        newGamesButton.setBackground(new Color(76, 175, 80)); // Warna hijau
        newGamesButton.setForeground(Color.WHITE);
        newGamesButton.setFocusPainted(false); // Hilangkan highlight saat klik
        newGamesButton.addActionListener((ActionEvent e) -> {
            parentFrame.setContentPane(new GameSelector(parentFrame));
            parentFrame.pack();
            parentFrame.setLocationRelativeTo(null);
            parentFrame.validate();
        });

        // Tombol untuk PvC
        JButton replayButton = new JButton("Play Again");
        replayButton.setFont(new Font("Poppins", Font.PLAIN, 20));
        replayButton.setBackground(new Color(33, 150, 243)); // Warna biru
        replayButton.setForeground(Color.WHITE);
        replayButton.setFocusPainted(false);
        replayButton.addActionListener((ActionEvent e) -> {
            if(game == 1) {
                new TTTGraphics(true, "Hard"); // Contoh default level Hard
                parentFrame.dispose();
            } else if (game == 2) {
                parentFrame.setContentPane(new GameMain("Tic-Tac-Toe 3x3"));
            } else if (game == 3) {
                parentFrame.setContentPane(new GameMain("Tic-Tac-Toe 5x5"));
            } else if (game == 4) {
                parentFrame.setContentPane(new GameMain("Connect Four"));
            }

            parentFrame.pack();
            parentFrame.setLocationRelativeTo(null);
            parentFrame.validate();
        });

        JButton exitButton = new JButton("Exit");
        exitButton.setFont(new Font("Poppins", Font.PLAIN, 20));
        exitButton.setBackground(new Color(33, 150, 243)); // Warna biru
        exitButton.setForeground(Color.WHITE);
        exitButton.setFocusPainted(false);
        exitButton.addActionListener((ActionEvent e) -> {
            parentFrame.pack();
            parentFrame.dispose();
            parentFrame.validate();

            // Safety Net
            Window[] windows = Window.getWindows();
            for (Window window : windows) {
                // Check if the window is an instance of JFrame
                if (window instanceof JFrame) {
                    // Dispose of the JFrame
                    window.dispose();
                    break; // Exit after disposing the first JFrame found
                }
            }
        });

        // Tambahkan tombol ke panel
        buttonPanel.add(newGamesButton);
        buttonPanel.add(replayButton);
        buttonPanel.add(exitButton);
        // Tambahkan komponen ke panel utama
        add(choiceLabel, BorderLayout.CENTER); // Judul di atas
        add(buttonPanel, BorderLayout.SOUTH); // Tombol di tengah
    }
}
