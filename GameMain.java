import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

/**
 * GameMain: A generic game panel supporting both Tic-Tac-Toe and Connect Four.
 * Implements Connect Four-specific logic such as token drop and 4-in-a-line win condition.
 */
public class GameMain extends JPanel {
    private static final long serialVersionUID = 1L;

    // Define named constants for the UI
    public static final String TITLE = "Game Selector";
    public static final Color COLOR_BG = Color.WHITE;
    public static final Color COLOR_BG_STATUS = new Color(216, 216, 216);
    public static final Font FONT_STATUS = new Font("OCR A Extended", Font.PLAIN, 14);

    // Define game objects
    private Board board;         // the game board
    private State currentState;  // the current state of the game
    private Seed currentPlayer;  // the current player
    private JLabel statusBar;    // for displaying status message
    private int gameTypeWin;     // for saving the win condition gametype

    /** Constructor to setup the game UI and initialize components */
    public GameMain(String gameType) {

        board = new Board(gameType); // Initialize the game board
        //board.setupGame(gameType); // Dynamically setup the board

        if(gameType.equals("Tic-Tac-Toe")) {
            this.gameTypeWin = 3;
        } else if(gameType.equals("Connect Four")) {
            this.gameTypeWin = 4;
        }


        // Set up the UI
        setLayout(new BorderLayout());
        statusBar = new JLabel("       ");
        statusBar.setFont(FONT_STATUS);
        statusBar.setBackground(COLOR_BG_STATUS);
        statusBar.setOpaque(true);
        statusBar.setPreferredSize(new Dimension(30, 30));
        statusBar.setHorizontalAlignment(JLabel.LEFT);
        statusBar.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 12));


        super.setLayout(new BorderLayout());
        super.add(statusBar, BorderLayout.SOUTH);
        super.setPreferredSize(new Dimension(Board.CANVAS_WIDTH, Board.CANVAS_HEIGHT + 30));
        super.setBorder(BorderFactory.createLineBorder(COLOR_BG_STATUS, 2, false));

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                handleMouseClick(e.getX(), e.getY());
            }
        });

        newGame();
    }

    /** Initialize a new game */
    public void newGame() {
        board.newGame();
        currentPlayer = Seed.CROSS; // CROSS plays first
        currentState = State.PLAYING;
        updateStatusBar();
    }
    private State updateGame(Seed player, int row, int col) {
        // Periksa apakah pemain saat ini menang
        if (hasWon(player, row, col)) {
            return (player == Seed.CROSS) ? State.CROSS_WON : State.NOUGHT_WON;
        }

        // Periksa apakah papan penuh (DRAW)
        for (int r = 0; r < Board.ROWS; r++) {
            for (int c = 0; c < Board.COLS; c++) {
                if (board.cells[r][c].content == Seed.NO_SEED) {
                    return State.PLAYING; // Masih ada sel kosong, permainan berlanjut
                }
            }
        }

        return State.DRAW; // Tidak ada sel kosong dan tidak ada pemenang, game seri
    }

    /** Handle mouse click event */
    private void handleMouseClick(int mouseX, int mouseY) {
        int row = mouseY / Cell.SIZE; // Hitung baris
        int col = mouseX / Cell.SIZE; // Hitung kolom

        // Debugging untuk memastikan perhitungan benar
        System.out.println("MouseX: " + mouseX + ", MouseY: " + mouseY);
        System.out.println("Klik di Baris: " + row + ", Kolom: " + col);

        if (currentState == State.PLAYING) {
            // Validasi apakah klik berada dalam grid
            if (row >= 0 && row < Board.ROWS && col >= 0 && col < Board.COLS) {
                // Periksa apakah sel kosong
                if (board.cells[row][col].content == Seed.NO_SEED) {
                    // Update simbol di sel
                    board.cells[row][col].content = currentPlayer;
                    // Perbarui state permainan
                    currentState = updateGame(currentPlayer, row, col);
                    // Ganti pemain
                    currentPlayer = (currentPlayer == Seed.CROSS) ? Seed.NOUGHT : Seed.CROSS;
                    // Perbarui GUI
                    repaint();
                    return; // Hentikan setelah simbol ditempatkan
                }
            }
        } else { // Jika game selesai
            newGame(); // Mulai game baru
        }
        SwingUtilities.invokeLater(() -> repaint()); // Redraw the board
        updateStatusBar();
    }


    /** Update the status bar message */
    private void updateStatusBar() {
        if (currentState == State.PLAYING) {
            statusBar.setForeground(Color.BLACK);
            statusBar.setText((currentPlayer == Seed.CROSS) ? "X's Turn" : "O's Turn");
        } else if (currentState == State.DRAW) {
            statusBar.setForeground(Color.RED);
            statusBar.setText("It's a Draw! Click to restart.");
        } else if (currentState == State.CROSS_WON) {
            statusBar.setForeground(Color.RED);
            statusBar.setText("X Won! Click to restart.");
        } else if (currentState == State.NOUGHT_WON) {
            statusBar.setForeground(Color.RED);
            statusBar.setText("O Won! Click to restart.");
        }
    }
    private boolean hasWon(Seed player, int rowSelected, int colSelected) {
        // Cek horizontal
        boolean win = false;
        int count = 0;
        for (int col = 0; col < Board.COLS; col++) {
            if (board.cells[rowSelected][col].content == player) {
                count++;
                if (count == gameTypeWin) return true; // 3 atau 4-in-a-row ditemukan
            } else {
                count = 0;
            }
        }

        // Vertical check
        count = 0;
        for (int row = 0; row < Board.ROWS; row++) {
            if (board.cells[row][colSelected].content == player) {
                count++;
                if (count == gameTypeWin) return true; // 3 atau 4-in-a-column ditemukan
            } else {
                count = 0;
            }
        }

        // Diagonal (top-left to bottom-right)
        count = 0;
        for (int i = -3; i <= 3; i++) {
            int r = rowSelected + i, c = colSelected + i;
            if (r >= 0 && r < Board.ROWS && c >= 0 && c < Board.COLS && board.cells[r][c].content == player) {
                count++;
                if (count == gameTypeWin) return true;
            } else {
                count = 0;
            }
        }

        // Anti-diagonal (top-right to bottom-left)
        count = 0;
        for (int i = -3; i <= 3; i++) {
            int r = rowSelected + i, c = colSelected - i;
            if (r >= 0 && r < Board.ROWS && c >= 0 && c < Board.COLS && board.cells[r][c].content == player) {
                count++;
                if (count == gameTypeWin) return true;
            } else {
                count = 0;
            }
        }

        return false; // Tidak ada kondisi kemenangan ditemukan
    }


    /** Custom painting codes on this JPanel */
    @Override
    public void paintComponent(Graphics g) {  // Callback via repaint()
        super.paintComponent(g);
        setBackground(COLOR_BG); // set its background color

        board.paint(g);  // ask the game board to paint itself

        // Print status-bar message
        if (currentState == State.PLAYING) {
            statusBar.setForeground(Color.BLACK);
            statusBar.setText((currentPlayer == Seed.CROSS) ? "X's Turn" : "O's Turn");
        } else if (currentState == State.DRAW) {
            statusBar.setForeground(Color.RED);
            statusBar.setText("It's a Draw! Click to play again.");
        } else if (currentState == State.CROSS_WON) {
            statusBar.setForeground(Color.RED);
            statusBar.setText("'X' Won! Click to play again.");
        } else if (currentState == State.NOUGHT_WON) {
            statusBar.setForeground(Color.RED);
            statusBar.setText("'O' Won! Click to play again.");
        }
    }


    /** The entry main method */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame(TITLE);
            frame.setContentPane(new GameSelector(frame)); // GameSelector directs to GameMain
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.pack();
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }
}
