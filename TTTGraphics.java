/**
 * ES234317-Algorithm and Data Structures
 * Semester Ganjil, 2024/2025
 * Group Capstone Project
 * Group #12
 * 1 - 5026231008 - Batara Haryo Yudanto
 * 2 - 5026231079 - Kevin Nathanael
 * 3 - 5026231089 - Yusuf Acala Sadurjaya Sri Krisna
 */

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.Random;

public class TTTGraphics extends JFrame {
    private static final long serialVersionUID = 1L; // to prevent serializable warning

    // Define named constants for the game board
    public static final int ROWS = 3;  // ROWS x COLS cells
    public static final int COLS = 3;
    public static final int CELL_SIZE = 120; // cell width/height (square)
    public static final int BOARD_WIDTH = CELL_SIZE * COLS; // the drawing canvas
    public static final int BOARD_HEIGHT = CELL_SIZE * ROWS;
    public static final int GRID_WIDTH = 10;                  // Grid-line's width
    public static final int GRID_WIDTH_HALF = GRID_WIDTH / 2;
    public static final int CELL_PADDING = CELL_SIZE / 5;
    public static final int SYMBOL_SIZE = CELL_SIZE - CELL_PADDING * 2; // width/height
    public static final int SYMBOL_STROKE_WIDTH = 8; // pen's stroke width

    private boolean aiEnabled; // Flag for AI mode
    private String aiLevel; // "Easy" or "Hard"
    private State currentState;


    public enum Seed {
        CROSS, NOUGHT, NO_SEED
    }

    private Seed currentPlayer;
    private Seed[][] board;

    private GamePanel gamePanel;
    private JLabel statusBar;

    // Variabel untuk animasi
    private int animatedRow = -1;
    private int animatedCol = -1;
    private int animationStep = 0;
    private final int MAX_ANIMATION_STEPS = 10; // Jumlah langkah animasi
    private final int ANIMATION_DELAY = 30;    // Delay antar langkah animasi (ms)
    // Variabel untuk animasi garis kemenangan
    private boolean hasWinningLine = false;
    private int winningRowStart = -1, winningColStart = -1;
    private int winningRowEnd = -1, winningColEnd = -1;
    private int lineAnimationStep = 0;
    private final int MAX_LINE_ANIMATION_STEPS = 20;
    private final int LINE_ANIMATION_DELAY = 30;

    public TTTGraphics(boolean aiEnabled, String aiLevel) {
        this.aiEnabled = aiEnabled;
        this.aiLevel = aiLevel;
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
                    if (row >= 0 && row < ROWS && col >= 0 && col < COLS && board[row][col] == Seed.NO_SEED) {
                        currentState = stepGame(currentPlayer, row, col);
                        if (!hasWinningLine) {
                            animatedRow = row;
                            animatedCol = col;
                            animationStep = 0;
                            startBounceAnimation();
                        }
                        currentPlayer = (currentPlayer == Seed.CROSS) ? Seed.NOUGHT : Seed.CROSS;

                        if (aiEnabled && currentPlayer == Seed.NOUGHT && currentState == State.PLAYING) {
                            int[] aiMove = (aiLevel.equals("Easy")) ? randomMove() : minimaxMove();
                            currentState = stepGame(Seed.NOUGHT, aiMove[0], aiMove[1]);
                            currentPlayer = Seed.CROSS;
                        }
                    }
                } else {

                    Window[] windows = Window.getWindows();
                    for (Window window : windows) {
                        // Check if the window is an instance of JFrame
                        if (window instanceof JFrame) {
                            // Dispose of the JFrame
                            window.dispose();
                            break; // Exit after disposing the first JFrame found
                        }
                    }

                    SwingUtilities.invokeLater(() -> {
                        JFrame frame = new JFrame();
                        frame.setContentPane(new GameSelector(frame, currentState, 1)); // GameSelector directs to GameMain
                        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                        frame.pack();
                        frame.setLocationRelativeTo(null);
                        frame.setVisible(true);
                    });

                }
                repaint();
            }
        });

        statusBar = new JLabel("       ");
        statusBar.setFont(new Font("OCR A Extended", Font.PLAIN, 14));
        statusBar.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 12));
        statusBar.setOpaque(true);
        statusBar.setBackground(new Color(216, 216, 216));

        Container cp = getContentPane();
        cp.setLayout(new BorderLayout());
        cp.add(gamePanel, BorderLayout.CENTER);
        cp.add(statusBar, BorderLayout.PAGE_END);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        pack();
        setLocationRelativeTo(null);
        setTitle("Tic Tac Toe - " + aiLevel + " Mode");
        setVisible(true);

        newGame();
    }

    private void initGame() {
        board = new Seed[ROWS][COLS];
    }

    private void newGame() {
        for (int row = 0; row < ROWS; ++row) {
            for (int col = 0; col < COLS; ++col) {
                board[row][col] = Seed.NO_SEED;
            }
        }
        currentPlayer = Seed.CROSS;
        currentState = State.PLAYING;
    }

    private State stepGame(Seed player, int selectedRow, int selectedCol) {
        board[selectedRow][selectedCol] = player;

        if (currentState == State.PLAYING) {
            SoundEffect.klik.play();
        } else {
            SoundEffect.EXPLODE.play();
        }

        if (board[selectedRow][0] == player && board[selectedRow][1] == player && board[selectedRow][2] == player) {
            startWinningLineAnimation(selectedRow, 0, selectedRow, 2);
            return (player == Seed.CROSS) ? State.CROSS_WON : State.NOUGHT_WON;
        }
        if (board[0][selectedCol] == player && board[1][selectedCol] == player && board[2][selectedCol] == player) {
            startWinningLineAnimation(0, selectedCol, 2, selectedCol);
            return (player == Seed.CROSS) ? State.CROSS_WON : State.NOUGHT_WON;
        }
        if (selectedRow == selectedCol && board[0][0] == player && board[1][1] == player && board[2][2] == player) {
            startWinningLineAnimation(0, 0, 2, 2);
            return (player == Seed.CROSS) ? State.CROSS_WON : State.NOUGHT_WON;
        }
        if (selectedRow + selectedCol == 2 && board[0][2] == player && board[1][1] == player && board[2][0] == player) {
            startWinningLineAnimation(0, 2, 2, 0);
            return (player == Seed.CROSS) ? State.CROSS_WON : State.NOUGHT_WON;
        }

        for (int row = 0; row < ROWS; ++row) {
            for (int col = 0; col < COLS; ++col) {
                if (board[row][col] == Seed.NO_SEED) {
                    return State.PLAYING;
                }
            }
        }

        return State.DRAW;
    }

    private boolean isDraw() {
        for (int row = 0; row < ROWS; row++) {
            for (int col = 0; col < COLS; col++) {
                if (board[row][col] == Seed.NO_SEED) return false;
            }
        }
        return true;
    }

    private int[] randomMove() {
        Random random = new Random();
        int row, col;
        do {
            row = random.nextInt(ROWS);
            col = random.nextInt(COLS);
        } while (board[row][col] != Seed.NO_SEED);
        return new int[]{row, col};
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

    private void restartGame() {
        Window[] windows = Window.getWindows();
        for (Window window : windows) {
            if (window instanceof JFrame) {
                window.dispose();
                break;
            }
        }
        SwingUtilities.invokeLater(() -> new TTTGraphics(aiEnabled, aiLevel));
    }

    private void startBounceAnimation() {
        Timer bounceTimer = new Timer(ANIMATION_DELAY, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                animationStep++;
                if (animationStep >= MAX_ANIMATION_STEPS) {
                    ((Timer) e.getSource()).stop();
                }
                repaint();
            }
        });
        bounceTimer.start();
    }

    private void startWinningLineAnimation(int startRow, int startCol, int endRow, int endCol) {
        hasWinningLine = true;
        winningRowStart = startRow;
        winningColStart = startCol;
        winningRowEnd = endRow;
        winningColEnd = endCol;
        lineAnimationStep = 0;

        Timer lineAnimationTimer = new Timer(LINE_ANIMATION_DELAY, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                lineAnimationStep++;
                if (lineAnimationStep >= MAX_LINE_ANIMATION_STEPS) {
                    ((Timer) e.getSource()).stop();
                }
                repaint();
            }
        });
        lineAnimationTimer.start();
    }

    class GamePanel extends JPanel {
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            setBackground(new Color(20, 20, 20));

            Graphics2D g2d = (Graphics2D) g;
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            g2d.setColor(new Color(150, 50, 250));
            g2d.setStroke(new BasicStroke(4));
            for (int row = 1; row < ROWS; row++) {
                g2d.drawLine(0, row * CELL_SIZE, BOARD_WIDTH, row * CELL_SIZE);
            }
            for (int col = 1; col < COLS; col++) {
                g2d.drawLine(col * CELL_SIZE, 0, col * CELL_SIZE, BOARD_HEIGHT);
            }

            for (int row = 0; row < ROWS; row++) {
                for (int col = 0; col < COLS; col++) {
                    int x1 = col * CELL_SIZE + CELL_PADDING;
                    int y1 = row * CELL_SIZE + CELL_PADDING;
                    int size = SYMBOL_SIZE;

                    if (row == animatedRow && col == animatedCol && animationStep < MAX_ANIMATION_STEPS) {
                        double scaleFactor = 1.0 + 0.1 * Math.sin((double) animationStep / MAX_ANIMATION_STEPS * Math.PI);
                        size = (int) (SYMBOL_SIZE * scaleFactor);
                        x1 = col * CELL_SIZE + (CELL_SIZE - size) / 2;
                        y1 = row * CELL_SIZE + (CELL_SIZE - size) / 2;
                    }

                    if (board[row][col] == Seed.CROSS) {
                        drawNeonCross(g2d, x1, y1, size);
                    } else if (board[row][col] == Seed.NOUGHT) {
                        drawNeonCircle(g2d, x1, y1, size);
                    }
                }
            }

            if (hasWinningLine) {
                g2d.setColor(new Color(50, 255, 50));
                g2d.setStroke(new BasicStroke(10, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));

                int xStart = winningColStart * CELL_SIZE + CELL_SIZE / 2;
                int yStart = winningRowStart * CELL_SIZE + CELL_SIZE / 2;
                int xEnd = winningColEnd * CELL_SIZE + CELL_SIZE / 2;
                int yEnd = winningRowEnd * CELL_SIZE + CELL_SIZE / 2;

                int xCurrent = xStart + (xEnd - xStart) * lineAnimationStep / MAX_LINE_ANIMATION_STEPS;
                int yCurrent = yStart + (yEnd - yStart) * lineAnimationStep / MAX_LINE_ANIMATION_STEPS;

                g2d.drawLine(xStart, yStart, xCurrent, yCurrent);
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



        private void drawNeonCross(Graphics2D g2d, int x, int y, int size) {
            for (int glow = 5; glow > 0; glow--) {
                g2d.setColor(new Color(255, 20, 147, 50 + glow * 20));
                g2d.setStroke(new BasicStroke(SYMBOL_STROKE_WIDTH + glow * 2, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
                g2d.drawLine(x, y, x + size, y + size);
                g2d.drawLine(x + size, y, x, y + size);
            }
            g2d.setColor(new Color(255, 20, 147));
            g2d.setStroke(new BasicStroke(SYMBOL_STROKE_WIDTH, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
            g2d.drawLine(x, y, x + size, y + size);
            g2d.drawLine(x + size, y, x, y + size);
        }

        private void drawNeonCircle(Graphics2D g2d, int x, int y, int size) {
            for (int glow = 5; glow > 0; glow--) {
                g2d.setColor(new Color(0, 255, 255, 50 + glow * 20));
                g2d.setStroke(new BasicStroke(SYMBOL_STROKE_WIDTH + glow * 2, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
                g2d.drawOval(x, y, size, size);
            }
            g2d.setColor(new Color(0, 255, 255));
            g2d.setStroke(new BasicStroke(SYMBOL_STROKE_WIDTH, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
            g2d.drawOval(x, y, size, size);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            String[] options = {"Easy", "Hard"};
            int choice = JOptionPane.showOptionDialog(null, "Select AI Level", "Tic Tac Toe",
                    JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, options, options[0]);
            String level = (choice == 0) ? "Easy" : "Hard";
            new TTTGraphics(true, level);
        });
    }
}
