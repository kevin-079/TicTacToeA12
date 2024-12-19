import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
/**
 * Tic-Tac-Toe: Two-player Graphics version with Simple-OO in one class
 */
public class TTTGraphics extends JFrame {
    private static final long serialVersionUID = 1L; // to prevent serializable warning

    // Define named constants for the game board
    public static final int ROWS = 3;  // ROWS x COLS cells
    public static final int COLS = 3;

    // Define named constants for the drawing graphics
    public static final int CELL_SIZE = 120; // cell width/height (square)
    public static final int BOARD_WIDTH  = CELL_SIZE * COLS; // the drawing canvas
    public static final int BOARD_HEIGHT = CELL_SIZE * ROWS;
    public static final int GRID_WIDTH = 10;                  // Grid-line's width
    public static final int GRID_WIDTH_HALF = GRID_WIDTH / 2;
    public static final int CELL_PADDING = CELL_SIZE / 5;
    public static final int SYMBOL_SIZE = CELL_SIZE - CELL_PADDING * 2; // width/height
    public static final int SYMBOL_STROKE_WIDTH = 8; // pen's stroke width
    public static final Color COLOR_BG = Color.WHITE;  // background
    public static final Color COLOR_BG_STATUS = new Color(216, 216, 216);
    public static final Color COLOR_GRID   = Color.LIGHT_GRAY;  // grid lines
    public static final Color COLOR_CROSS  = new Color(211, 45, 65);  // Red #D32D41
    public static final Color COLOR_NOUGHT = new Color(76, 181, 245); // Blue #4CB5F5
    public static final Font FONT_STATUS = new Font("OCR A Extended", Font.PLAIN, 14);

    private boolean aiEnabled; // Flag for AI mode

    public enum State {
        PLAYING, DRAW, CROSS_WON, NOUGHT_WON
    }
    private State currentState;

    public enum Seed {
        CROSS, NOUGHT, NO_SEED
    }
    private Seed currentPlayer;
    private Seed[][] board;

    private GamePanel gamePanel;
    private JLabel statusBar;

    public TTTGraphics(boolean aiEnabled) {
        this.aiEnabled = aiEnabled;
        initGame();

        gamePanel = new GamePanel();
        gamePanel.setPreferredSize(new Dimension(BOARD_WIDTH, BOARD_HEIGHT));

        gamePanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int mouseX = e.getX();
                int mouseY = e.getY();
                int row = mouseY / CELL_SIZE;
                int col = mouseX / CELL_SIZE;

                if (currentState == State.PLAYING) {
                    if (row >= 0 && row < ROWS && col >= 0
                            && col < COLS && board[row][col] == Seed.NO_SEED) {
                        currentState = stepGame(currentPlayer, row, col);
                        currentPlayer = (currentPlayer == Seed.CROSS) ? Seed.NOUGHT : Seed.CROSS;

                        if (aiEnabled && currentPlayer == Seed.NOUGHT && currentState == State.PLAYING) {
                            int[] aiMove = minimaxMove();
                            currentState = stepGame(Seed.NOUGHT, aiMove[0], aiMove[1]);
                            currentPlayer = Seed.CROSS;
                        }
                    }
                } else {
                    newGame();
                }
                repaint();
            }
        });

        statusBar = new JLabel("       ");
        statusBar.setFont(FONT_STATUS);
        statusBar.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 12));
        statusBar.setOpaque(true);
        statusBar.setBackground(COLOR_BG_STATUS);

        Container cp = getContentPane();
        cp.setLayout(new BorderLayout());
        cp.add(gamePanel, BorderLayout.CENTER);
        cp.add(statusBar, BorderLayout.PAGE_END);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        pack();
        setTitle("Tic Tac Toe");
        setVisible(true);

        newGame();
    }

    public void initGame() {
        board = new Seed[ROWS][COLS];
    }

    public void newGame() {
        for (int row = 0; row < ROWS; ++row) {
            for (int col = 0; col < COLS; ++col) {
                board[row][col] = Seed.NO_SEED;
            }
        }
        currentPlayer = Seed.CROSS;
        currentState  = State.PLAYING;
    }

    public State stepGame(Seed player, int selectedRow, int selectedCol) {
        board[selectedRow][selectedCol] = player;

        if (board[selectedRow][0] == player  // 3-in-the-row
                && board[selectedRow][1] == player
                && board[selectedRow][2] == player
                || board[0][selectedCol] == player // 3-in-the-column
                && board[1][selectedCol] == player
                && board[2][selectedCol] == player
                || selectedRow == selectedCol  // 3-in-the-diagonal
                && board[0][0] == player
                && board[1][1] == player
                && board[2][2] == player
                || selectedRow + selectedCol == 2 // 3-in-the-opposite-diagonal
                && board[0][2] == player
                && board[1][1] == player
                && board[2][0] == player) {
            return (player == Seed.CROSS) ? State.CROSS_WON : State.NOUGHT_WON;
        } else {
            for (int row = 0; row < ROWS; ++row) {
                for (int col = 0; col < COLS; ++col) {
                    if (board[row][col] == Seed.NO_SEED) {
                        return State.PLAYING;
                    }
                }
            }
            return State.DRAW;
        }
    }

    private int[] minimaxMove() {
        int bestScore = Integer.MIN_VALUE;
        int[] bestMove = new int[2];

        for (int row = 0; row < ROWS; row++) {
            for (int col = 0; col < COLS; col++) {
                if (board[row][col] == Seed.NO_SEED) {
                    board[row][col] = Seed.NOUGHT;
                    int score = minimax(0, false);
                    board[row][col] = Seed.NO_SEED;
                    if (score > bestScore) {
                        bestScore = score;
                        bestMove[0] = row;
                        bestMove[1] = col;
                    }
                }
            }
        }
        return bestMove;
    }

    private int minimax(int depth, boolean isMaximizing) {
        if (checkWin(Seed.NOUGHT)) return 10 - depth;
        if (checkWin(Seed.CROSS)) return depth - 10;
        if (isDraw()) return 0;

        if (isMaximizing) {
            int bestScore = Integer.MIN_VALUE;
            for (int row = 0; row < ROWS; row++) {
                for (int col = 0; col < COLS; col++) {
                    if (board[row][col] == Seed.NO_SEED) {
                        board[row][col] = Seed.NOUGHT;
                        bestScore = Math.max(bestScore, minimax(depth + 1, false));
                        board[row][col] = Seed.NO_SEED;
                    }
                }
            }
            return bestScore;
        } else {
            int bestScore = Integer.MAX_VALUE;
            for (int row = 0; row < ROWS; row++) {
                for (int col = 0; col < COLS; col++) {
                    if (board[row][col] == Seed.NO_SEED) {
                        board[row][col] = Seed.CROSS;
                        bestScore = Math.min(bestScore, minimax(depth + 1, true));
                        board[row][col] = Seed.NO_SEED;
                    }
                }
            }
            return bestScore;
        }
    }

    private boolean checkWin(Seed player) {
        for (int i = 0; i < ROWS; i++) {
            if (board[i][0] == player && board[i][1] == player && board[i][2] == player) return true;
            if (board[0][i] == player && board[1][i] == player && board[2][i] == player) return true;
        }
        if (board[0][0] == player && board[1][1] == player && board[2][2] == player) return true;
        if (board[0][2] == player && board[1][1] == player && board[2][0] == player) return true;
        return false;
    }

    private boolean isDraw() {
        for (int row = 0; row < ROWS; row++) {
            for (int col = 0; col < COLS; col++) {
                if (board[row][col] == Seed.NO_SEED) return false;
            }
        }
        return true;
    }

    class GamePanel extends JPanel {
        private static final long serialVersionUID = 1L;

        @Override
        public void paintComponent(Graphics g) {
            super.paintComponent(g);
            setBackground(COLOR_BG);

            g.setColor(COLOR_GRID);
            for (int row = 1; row < ROWS; ++row) {
                g.fillRoundRect(0, CELL_SIZE * row - GRID_WIDTH_HALF,
                        BOARD_WIDTH-1, GRID_WIDTH, GRID_WIDTH, GRID_WIDTH);
            }
            for (int col = 1; col < COLS; ++col) {
                g.fillRoundRect(CELL_SIZE * col - GRID_WIDTH_HALF, 0,
                        GRID_WIDTH, BOARD_HEIGHT-1, GRID_WIDTH, GRID_WIDTH);
            }

            Graphics2D g2d = (Graphics2D)g;
            g2d.setStroke(new BasicStroke(SYMBOL_STROKE_WIDTH,
                    BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
            for (int row = 0; row < ROWS; ++row) {
                for (int col = 0; col < COLS; ++col) {
                    int x1 = col * CELL_SIZE + CELL_PADDING;
                    int y1 = row * CELL_SIZE + CELL_PADDING;
                    if (board[row][col] == Seed.CROSS) {
                        g2d.setColor(COLOR_CROSS);
                        int x2 = (col + 1) * CELL_SIZE - CELL_PADDING;
                        int y2 = (row + 1) * CELL_SIZE - CELL_PADDING;
                        g2d.drawLine(x1, y1, x2, y2);
                        g2d.drawLine(x2, y1, x1, y2);
                    } else if (board[row][col] == Seed.NOUGHT) {
                        g2d.setColor(COLOR_NOUGHT);
                        g2d.drawOval(x1, y1, SYMBOL_SIZE, SYMBOL_SIZE);
                    }
                }
            }

            if (currentState == State.PLAYING) {
                statusBar.setForeground(Color.BLACK);
                statusBar.setText((currentPlayer == Seed.CROSS) ? "X's Turn" : "O's Turn");
            } else if (currentState == State.DRAW) {
                statusBar.setForeground(Color.RED);
                statusBar.setText("It's a Draw! Click to play again");
            } else if (currentState == State.CROSS_WON) {
                statusBar.setForeground(Color.RED);
                statusBar.setText("'X' Won! Click to play again");
            } else if (currentState == State.NOUGHT_WON) {
                statusBar.setForeground(Color.RED);
                statusBar.setText("'O' Won! Click to play again");
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new TTTGraphics(false));
    }
}
