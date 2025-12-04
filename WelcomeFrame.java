import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class WelcomeFrame extends JFrame {

    private JButton playButton;
    private JButton settingsButton;

    public WelcomeFrame() {
        setTitle("Tic-Tac-Toe - Welcome");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 300);
        setLocationRelativeTo(null); // center screen

        initComponents();
    }

    private void initComponents() {
        // Panel utama
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());

        // Title di atas
        JLabel titleLabel = new JLabel("Welcome to Tic-Tac-Toe", SwingConstants.CENTER);
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 20));

        // Panel untuk button
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(2, 1, 10, 10));

        playButton = new JButton("Play");
        settingsButton = new JButton("Settings");

        buttonPanel.add(playButton);
        buttonPanel.add(settingsButton);

        mainPanel.add(titleLabel, BorderLayout.NORTH);
        mainPanel.add(buttonPanel, BorderLayout.CENTER);

        add(mainPanel);

        // Event handling
        setupListeners();
    }

    private void setupListeners() {
        // Bila user tekan Play
        playButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Buka GameFrame
                GameFrame gameFrame = new GameFrame();
                gameFrame.setVisible(true);

                // Tutup current window (optional)
                dispose();
            }
        });

        // Bila user tekan Settings
        settingsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                SettingsFrame settingsFrame = new SettingsFrame();
                settingsFrame.setVisible(true);
                dispose();
            }
        });
    }
}
