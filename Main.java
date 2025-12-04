import javax.swing.SwingUtilities;

public class Main {
    public static void main(String[] args) {
        // Launch GUI dalam Event Dispatch Thread (standard Swing)
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new WelcomeFrame().setVisible(true);
            }
        });
    }
}
