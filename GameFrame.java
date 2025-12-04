import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GameFrame extends JFrame {

    private int BOARD_SIZE = SettingsManager.getBoardSize();
    private JButton[][] buttons = new JButton[BOARD_SIZE][BOARD_SIZE];
    private char[][] board = new char[BOARD_SIZE][BOARD_SIZE];
    private char currentPlayer = 'X';

    private JLabel statusLabel;
    private JButton backButton;
    private JButton resetButton;

    public GameFrame() {
        setTitle("Tic-Tac-Toe");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(500, 550);
        setLocationRelativeTo(null);

        initComponents();
        initBoardData();
    }

    private void initComponents() {
        // Panel utama
        JPanel mainPanel = new JPanel(new BorderLayout());
        add(mainPanel);

        // Atas: status "Player X turn" dll
        statusLabel = new JLabel("Player X's turn", SwingConstants.CENTER);
        statusLabel.setFont(new Font("SansSerif", Font.BOLD, 18));
        mainPanel.add(statusLabel, BorderLayout.NORTH);

        // Tengah: board tic-tac-toe
        JPanel boardPanel = new JPanel(new GridLayout(BOARD_SIZE, BOARD_SIZE));
        mainPanel.add(boardPanel, BorderLayout.CENTER);

        // Create buttons untuk setiap kotak
        for (int row = 0; row < BOARD_SIZE; row++) {
            for (int col = 0; col < BOARD_SIZE; col++) {
                JButton button = new JButton("");
                button.setFont(new Font("SansSerif", Font.BOLD, 40));
                button.setFocusPainted(false);

                final int r = row;
                final int c = col;

                // Event bila button ditekan
                button.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        handleMove(r, c);
                    }
                });

                buttons[row][col] = button;
                boardPanel.add(button);
            }
        }

        // Bawah: butang Back & Reset
        JPanel bottomPanel = new JPanel();
        bottomPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 10));

        backButton = new JButton("Back to Welcome");
        resetButton = new JButton("Reset Board");

        bottomPanel.add(backButton);
        bottomPanel.add(resetButton);

        mainPanel.add(bottomPanel, BorderLayout.SOUTH);

        // Listener butang Back
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                WelcomeFrame welcome = new WelcomeFrame();
                welcome.setVisible(true);
                dispose();
            }
        });

        // Listener butang Reset
        resetButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                resetBoard();
            }
        });
    }

    // Set semua petak kosong pada permulaan
    private void initBoardData() {
        for (int row = 0; row < BOARD_SIZE; row++) {
            for (int col = 0; col < BOARD_SIZE; col++) {
                board[row][col] = ' '; // kosong
            }
        }
    }

    // Handle bila player klik pada (row, col)
    private void handleMove(int row, int col) {
        // Kalau petak dah diisi, jangan buat apa-apa
        if (board[row][col] != ' ') {
            return;
        }

        // Set simbol pada data & button
        board[row][col] = currentPlayer;
        buttons[row][col].setText(String.valueOf(currentPlayer));

        // Check menang?
        if (checkWin(currentPlayer)) {
            JOptionPane.showMessageDialog(this,
                    "Player " + currentPlayer + " wins!",
                    "Game Over",
                    JOptionPane.INFORMATION_MESSAGE);
            resetBoard();
            return;
        }

        // Check seri (full board tanpa menang)
        if (checkDraw()) {
            JOptionPane.showMessageDialog(this,
                    "It's a draw!",
                    "Game Over",
                    JOptionPane.INFORMATION_MESSAGE);
            resetBoard();
            return;
        }

        // Tukar giliran
        currentPlayer = (currentPlayer == 'X') ? 'O' : 'X';
        statusLabel.setText("Player " + currentPlayer + "'s turn");
    }

    // Check sama ada player menang
    private boolean checkWin(char player) {
        // Check baris
        for (int row = 0; row < BOARD_SIZE; row++) {
            boolean rowWin = true;
            for (int col = 0; col < BOARD_SIZE; col++) {
                if (board[row][col] != player) {
                    rowWin = false;
                    break;
                }
            }
            if (rowWin) return true;
        }

        // Check kolum
        for (int col = 0; col < BOARD_SIZE; col++) {
            boolean colWin = true;
            for (int row = 0; row < BOARD_SIZE; row++) {
                if (board[row][col] != player) {
                    colWin = false;
                    break;
                }
            }
            if (colWin) return true;
        }

        // Check diagonal utama
        boolean diag1Win = true;
        for (int i = 0; i < BOARD_SIZE; i++) {
            if (board[i][i] != player) {
                diag1Win = false;
                break;
            }
        }
        if (diag1Win) return true;

        // Check diagonal bertentangan
        boolean diag2Win = true;
        for (int i = 0; i < BOARD_SIZE; i++) {
            if (board[i][BOARD_SIZE - 1 - i] != player) {
                diag2Win = false;
                break;
            }
        }
        if (diag2Win) return true;

        // Kalau semua false → tak menang
        return false;
    }

    // Check seri – semua kotak penuh tanpa pemenang
    private boolean checkDraw() {
        for (int row = 0; row < BOARD_SIZE; row++) {
            for (int col = 0; col < BOARD_SIZE; col++) {
                if (board[row][col] == ' ') {
                    return false; // masih ada ruang kosong
                }
            }
        }
        return true;
    }

    // Reset board (untuk game baru)
    private void resetBoard() {
        initBoardData();
        for (int row = 0; row < BOARD_SIZE; row++) {
            for (int col = 0; col < BOARD_SIZE; col++) {
                buttons[row][col].setText("");
            }
        }
        currentPlayer = 'X';
        statusLabel.setText("Player X's turn");
    }
}
