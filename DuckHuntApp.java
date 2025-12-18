import javax.swing.*;
import java.awt.*;
import java.util.HashMap;

public class DuckHuntApp extends JFrame {

    CardLayout cardLayout = new CardLayout();
    JPanel mainPanel = new JPanel(cardLayout);
    HashMap<String, String> users = new HashMap<>();

    public DuckHuntApp() {
        // Create login panel and game screen
        LoginPanel loginPanel = new LoginPanel();
        GameScreen gameScreen = new GameScreen();

        // Add panels to the CardLayout container
        mainPanel.add(loginPanel, "LOGIN");
        mainPanel.add(gameScreen, "GAME");

        // Pass references to loginPanel so it can switch cards and interact with the game obviously
        loginPanel.setCardLayout(cardLayout, mainPanel, users, gameScreen);

        // Show the login panel first
        cardLayout.show(mainPanel, "LOGIN");

        // Add mainPanel to JFrame
        add(mainPanel);
        setTitle("Duck Hunt");
        setSize(960, 640);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(DuckHuntApp::new);
    }
}
