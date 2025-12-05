import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GameFrame extends JFrame {

    private int BOARD_SIZE;
    private JButton[][] buttons;
    private char[][] board;

    private char currentPlayer = 'X';
    private boolean isSingleplayer;
    private boolean gameOver = false;

    // UI
    private JLabel titleLabel;          // "You won!", "Draw!", "You lost!", etc.
    private JLabel timeLabel;           // ⏱ time
    private JLabel spotsLabel;          // ▢ spots taken
    private JLabel humanWinLabel;       // 👤 human / Player X wins
    private JLabel botWinLabel;         // 🤖 bot / Player O wins

    private JButton backButton;
    private JButton playAgainButton;

    // Counters
    private int humanWinCount = 0;      // Single: human, Multi: Player X
    private int botWinCount = 0;        // Single: bot,   Multi: Player O
    private int drawCount = 0;
    private int spotsTaken = 0;

    // Timer
    private int elapsedSeconds = 0;
    private Timer gameTimer;

    public GameFrame() {
        // Ambil setting
        this.isSingleplayer = SettingsManager.getGameMode().equals("Singleplayer");
        this.BOARD_SIZE = SettingsManager.getBoardSize();

        this.buttons = new JButton[BOARD_SIZE][BOARD_SIZE];
        this.board   = new char[BOARD_SIZE][BOARD_SIZE];

        setTitle("Tic-Tac-Toe (" + (isSingleplayer ? "Singleplayer" : "Multiplayer") + ")");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(600, 650);
        setLocationRelativeTo(null);

        initComponents();
        initTimer();
        startNewGame();
    }

    // ===================== UI SETUP =====================

    private void initComponents() {
        JPanel mainPanel = new JPanel(new BorderLayout());
        add(mainPanel);

        // ==== TOP: Title + icon row ====
        JPanel topPanel = new JPanel(new BorderLayout());

        titleLabel = new JLabel("", SwingConstants.CENTER);
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 22));
        topPanel.add(titleLabel, BorderLayout.NORTH);

        JPanel infoPanel = new JPanel(new GridLayout(1, 4, 10, 0));

        timeLabel      = new JLabel("", SwingConstants.CENTER);
        spotsLabel     = new JLabel("", SwingConstants.CENTER);
        humanWinLabel  = new JLabel("", SwingConstants.CENTER);
        botWinLabel    = new JLabel("", SwingConstants.CENTER);

        infoPanel.add(timeLabel);
        infoPanel.add(spotsLabel);
        infoPanel.add(humanWinLabel);
        infoPanel.add(botWinLabel);

        topPanel.add(infoPanel, BorderLayout.SOUTH);

        mainPanel.add(topPanel, BorderLayout.NORTH);

        // ==== CENTER: Board ====
        JPanel boardPanel = new JPanel(new GridLayout(BOARD_SIZE, BOARD_SIZE));
        mainPanel.add(boardPanel, BorderLayout.CENTER);

        for (int r = 0; r < BOARD_SIZE; r++) {
            for (int c = 0; c < BOARD_SIZE; c++) {
                JButton btn = new JButton("");
                btn.setFont(new Font("SansSerif", Font.BOLD, 40));
                btn.setFocusPainted(false);
                btn.setBackground(Color.WHITE);

                final int row = r;
                final int col = c;

                btn.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        handleHumanMove(row, col);
                    }
                });

                buttons[r][c] = btn;
                boardPanel.add(btn);
            }
        }

        // ==== BOTTOM: Back + Play again/Try again ====
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));

        backButton = new JButton("Back");
        playAgainButton = new JButton("Play again");

        bottomPanel.add(backButton);
        bottomPanel.add(playAgainButton);

        mainPanel.add(bottomPanel, BorderLayout.SOUTH);

        backButton.addActionListener(e -> {
            WelcomeFrame welcome = new WelcomeFrame();
            welcome.setVisible(true);
            dispose();
        });

        playAgainButton.addActionListener(e -> startNewGame());
    }

    private void initTimer() {
        gameTimer = new Timer(1000, e -> {
            elapsedSeconds++;
            updateInfoLabels();
        });
    }

    // ===================== GAME FLOW =====================

    private void startNewGame() {
        // reset data
        for (int r = 0; r < BOARD_SIZE; r++) {
            for (int c = 0; c < BOARD_SIZE; c++) {
                board[r][c] = ' ';
                buttons[r][c].setText("");
                buttons[r][c].setBackground(Color.WHITE);
                buttons[r][c].setEnabled(true);
            }
        }
        spotsTaken = 0;
        elapsedSeconds = 0;
        gameOver = false;
        currentPlayer = 'X';

        // title semasa main
        if (isSingleplayer) {
            titleLabel.setText("Your turn");
        } else {
            titleLabel.setText("Player X's turn");
        }

        playAgainButton.setText("Play again");
        updateInfoLabels();

        gameTimer.restart();
    }

    private void endGame() {
        gameOver = true;
        gameTimer.stop();

        // disable button supaya tak boleh klik
        for (int r = 0; r < BOARD_SIZE; r++) {
            for (int c = 0; c < BOARD_SIZE; c++) {
                buttons[r][c].setEnabled(false);
            }
        }
    }

    private void handleHumanMove(int row, int col) {
        if (gameOver) return;
        if (board[row][col] != ' ') return;

        // Singleplayer: human hanya X
        if (isSingleplayer && currentPlayer == 'O') return;

        makeMove(row, col, currentPlayer);

        if (checkWin(currentPlayer)) {
            handleWin(currentPlayer);
            return;
        }
        if (checkDraw()) {
            handleDraw();
            return;
        }

        if (isSingleplayer) {
            // Bot turn
            currentPlayer = 'O';
            titleLabel.setText("Bot's turn");
            botMove();
        } else {
            // Multiplayer
            currentPlayer = (currentPlayer == 'X') ? 'O' : 'X';
            titleLabel.setText("Player " + currentPlayer + "'s turn");
        }
    }

    private void botMove() {
        if (gameOver) return;

        List<int[]> emptyCells = new ArrayList<>();
        for (int r = 0; r < BOARD_SIZE; r++) {
            for (int c = 0; c < BOARD_SIZE; c++) {
                if (board[r][c] == ' ') {
                    emptyCells.add(new int[]{r, c});
                }
            }
        }
        if (emptyCells.isEmpty()) return;

        Random rand = new Random();
        int[] move = emptyCells.get(rand.nextInt(emptyCells.size()));
        int row = move[0];
        int col = move[1];

        makeMove(row, col, 'O');

        if (checkWin('O')) {
            handleWin('O');
            return;
        }
        if (checkDraw()) {
            handleDraw();
            return;
        }

        // balik turn human
        currentPlayer = 'X';
        titleLabel.setText("Your turn");
    }

    private void makeMove(int row, int col, char player) {
        board[row][col] = player;
        buttons[row][col].setText(String.valueOf(player));
        spotsTaken++;
        updateInfoLabels();
    }

    // ===================== RESULT HANDLING =====================

    private void handleWin(char winner) {
        endGame();

        if (isSingleplayer) {
            if (winner == 'X') {
                humanWinCount++;
                titleLabel.setText("You won!");
                playAgainButton.setText("Play again");
            } else {
                botWinCount++;
                titleLabel.setText("You lost!");
                playAgainButton.setText("Try again");
            }
        } else {
            if (winner == 'X') {
                humanWinCount++;     // guna icon 👤 sebagai Player X
                titleLabel.setText("Player X won!");
            } else {
                botWinCount++;       // guna icon 🤖 sebagai Player O
                titleLabel.setText("Player O won!");
            }
            playAgainButton.setText("Play again");
        }

        highlightWinningLine(winner);
        updateInfoLabels();
    }

    private void handleDraw() {
        endGame();

        drawCount++;
        titleLabel.setText("Draw!");
        playAgainButton.setText("Play again");
        updateInfoLabels();
    }

    // Highlight line menang (simple)
    private void highlightWinningLine(char player) {
        // Row
        for (int r = 0; r < BOARD_SIZE; r++) {
            boolean rowWin = true;
            for (int c = 0; c < BOARD_SIZE; c++) {
                if (board[r][c] != player) {
                    rowWin = false;
                    break;
                }
            }
            if (rowWin) {
                for (int c = 0; c < BOARD_SIZE; c++) {
                    buttons[r][c].setBackground(new Color(0, 120, 140));
                    buttons[r][c].setForeground(Color.WHITE);
                }
                return;
            }
        }
        // Col
        for (int c = 0; c < BOARD_SIZE; c++) {
            boolean colWin = true;
            for (int r = 0; r < BOARD_SIZE; r++) {
                if (board[r][c] != player) {
                    colWin = false;
                    break;
                }
            }
            if (colWin) {
                for (int r = 0; r < BOARD_SIZE; r++) {
                    buttons[r][c].setBackground(new Color(0, 120, 140));
                    buttons[r][c].setForeground(Color.WHITE);
                }
                return;
            }
        }
        // Diagonal utama
        boolean diag1Win = true;
        for (int i = 0; i < BOARD_SIZE; i++) {
            if (board[i][i] != player) {
                diag1Win = false;
                break;
            }
        }
        if (diag1Win) {
            for (int i = 0; i < BOARD_SIZE; i++) {
                buttons[i][i].setBackground(new Color(0, 120, 140));
                buttons[i][i].setForeground(Color.WHITE);
            }
            return;
        }
        // Diagonal kedua
        boolean diag2Win = true;
        for (int i = 0; i < BOARD_SIZE; i++) {
            if (board[i][BOARD_SIZE - 1 - i] != player) {
                diag2Win = false;
                break;
            }
        }
        if (diag2Win) {
            for (int i = 0; i < BOARD_SIZE; i++) {
                buttons[i][BOARD_SIZE - 1 - i].setBackground(new Color(0, 120, 140));
                buttons[i][BOARD_SIZE - 1 - i].setForeground(Color.WHITE);
            }
        }
    }

    // ===================== CHECKS =====================

    private boolean checkWin(char player) {
        // Row
        for (int r = 0; r < BOARD_SIZE; r++) {
            boolean win = true;
            for (int c = 0; c < BOARD_SIZE; c++) {
                if (board[r][c] != player) {
                    win = false;
                    break;
                }
            }
            if (win) return true;
        }

        // Col
        for (int c = 0; c < BOARD_SIZE; c++) {
            boolean win = true;
            for (int r = 0; r < BOARD_SIZE; r++) {
                if (board[r][c] != player) {
                    win = false;
                    break;
                }
            }
            if (win) return true;
        }

        // Diagonal utama
        boolean winDiag1 = true;
        for (int i = 0; i < BOARD_SIZE; i++) {
            if (board[i][i] != player) {
                winDiag1 = false;
                break;
            }
        }
        if (winDiag1) return true;

        // Diagonal kedua
        boolean winDiag2 = true;
        for (int i = 0; i < BOARD_SIZE; i++) {
            if (board[i][BOARD_SIZE - 1 - i] != player) {
                winDiag2 = false;
                break;
            }
        }
        return winDiag2;
    }

    private boolean checkDraw() {
        for (int r = 0; r < BOARD_SIZE; r++) {
            for (int c = 0; c < BOARD_SIZE; c++) {
                if (board[r][c] == ' ') {
                    return false;
                }
            }
        }
        return true;
    }

    // ===================== INFO LABELS =====================

    private void updateInfoLabels() {
        // guna emoji / unicode sebagai icon ringkas
        timeLabel.setText("⏱ " + elapsedSeconds);
        spotsLabel.setText("▢ " + spotsTaken);

        if (isSingleplayer) {
            humanWinLabel.setText("👤 " + humanWinCount);
            botWinLabel.setText("🤖 " + botWinCount);
        } else {
            humanWinLabel.setText("👤 " + humanWinCount); // Player X
            botWinLabel.setText("🤖 " + botWinCount);     // Player O
        }
    }
}
