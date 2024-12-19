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

    /** Constructor to setup the game UI and initialize components */
    public GameMain(String gameType) {

        board = new Board(gameType); // Initialize the game board
        //board.setupGame(gameType); // Dynamically setup the board


        // Set up the UI
        setLayout(new BorderLayout());
        statusBar = new JLabel("       ");
        statusBar.setFont(FONT_STATUS);
        statusBar.setBackground(COLOR_BG_STATUS);
        statusBar.setOpaque(true);
        statusBar.setPreferredSize(new Dimension(30, 30));
        statusBar.setHorizontalAlignment(JLabel.LEFT);
        statusBar.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 12));
        add(statusBar, BorderLayout.SOUTH);

        super.setPreferredSize(new Dimension(Board.CANVAS_WIDTH, Board.CANVAS_HEIGHT + 30));

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
    private boolean hasWon(Seed player, int row, int col) {
        // Cek horizontal
        boolean horizontalWin = true;
        for (int c = 0; c < Board.COLS; c++) {
            if (board.cells[row][c].content != player) {
                horizontalWin = false;
                break;
            }
        }
        if (horizontalWin) return true;

        // Cek vertikal
        boolean verticalWin = true;
        for (int r = 0; r < Board.ROWS; r++) {
            if (board.cells[r][col].content != player) {
                verticalWin = false;
                break;
            }
        }
        if (verticalWin) return true;

        // Cek diagonal utama (kiri atas ke kanan bawah)
        if (row == col) { // Hanya cek jika baris dan kolom sama
            boolean diagonalWin = true;
            for (int i = 0; i < Board.ROWS; i++) {
                if (board.cells[i][i].content != player) {
                    diagonalWin = false;
                    break;
                }
            }
            if (diagonalWin) return true;
        }

        // Cek diagonal sekunder (kanan atas ke kiri bawah)
        if (row + col == Board.ROWS - 1) { // Hanya cek jika baris + kolom = ukuran papan - 1
            boolean antiDiagonalWin = true;
            for (int i = 0; i < Board.ROWS; i++) {
                if (board.cells[i][Board.COLS - 1 - i].content != player) {
                    antiDiagonalWin = false;
                    break;
                }
            }
            if (antiDiagonalWin) return true;
        }

        return false; // Tidak ada kondisi kemenangan
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
