import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class SettingsFrame extends JFrame {

    private JComboBox<String> gamemodeCombo;
    private JComboBox<Integer> boardSizeCombo;

    private JButton saveButton;
    private JButton backButton;

    public SettingsFrame() {
        setTitle("Settings");
        setSize(400, 300);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        initComponents();
    }

    private void initComponents() {
        JPanel panel = new JPanel(new GridLayout(5, 1, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));

        // Gamemode dropdown
        panel.add(new JLabel("Game Mode:"));
        gamemodeCombo = new JComboBox<>(new String[]{"Singleplayer", "Multiplayer"});
        gamemodeCombo.setSelectedItem(SettingsManager.getGameMode());
        panel.add(gamemodeCombo);

        // Board size dropdown
        panel.add(new JLabel("Board Size (NxN):"));
        boardSizeCombo = new JComboBox<>(new Integer[]{3, 4, 5, 6});
        boardSizeCombo.setSelectedItem(SettingsManager.getBoardSize());
        panel.add(boardSizeCombo);

        // Save button
        saveButton = new JButton("Save Settings");
        backButton = new JButton("Back");

        JPanel bottomPanel = new JPanel();
        bottomPanel.add(saveButton);
        bottomPanel.add(backButton);

        add(panel, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);

        setupListeners();
    }

    private void setupListeners() {

        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                String selectedMode = (String) gamemodeCombo.getSelectedItem();
                int selectedSize = (int) boardSizeCombo.getSelectedItem();

                SettingsManager.setGameMode(selectedMode);
                SettingsManager.setBoardSize(selectedSize);

                JOptionPane.showMessageDialog(SettingsFrame.this,
                        "Settings saved!\nMode: " + selectedMode + "\nBoard Size: " + selectedSize + "x" + selectedSize);

            }
        });

        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                WelcomeFrame welcome = new WelcomeFrame();
                welcome.setVisible(true);
                dispose();
            }
        });
    }
}
