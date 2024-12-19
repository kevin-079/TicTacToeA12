import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class GameSelector extends JPanel {
    public GameSelector(JFrame parentFrame) {
        setLayout(new GridLayout(3, 1, 10, 10)); //Label + 2 Tombol

        JLabel welcomeLabel = new JLabel("Welcome! Pilih Gamenya: ", JLabel.CENTER);
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 24));

        JButton tttButton = new JButton("Play Tic-Tac-Toe");
        JButton cfButton = new JButton("Play Connect Four");

        tttButton.addActionListener((ActionEvent e) -> {
            parentFrame.setContentPane(new GameMain("Tic-Tac-Toe"));
            parentFrame.pack();
            parentFrame.validate();
        });

        cfButton.addActionListener((ActionEvent e) -> {
            parentFrame.setContentPane(new GameMain("Connect Four"));
            parentFrame.pack();
            parentFrame.validate();
        });

        add(welcomeLabel);
        add(tttButton);
        add(cfButton);
    }
}
