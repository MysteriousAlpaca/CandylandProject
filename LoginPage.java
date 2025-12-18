import javax.swing.*;
import java.awt.*;
import java.util.HashMap;

class LoginPanel extends JPanel {

    private JTextField username;
    private JPasswordField password;
    private JButton loginBtn, registerBtn;

    private CardLayout cardLayout;
    private JPanel parentPanel;
    private HashMap<String, String> users; //decided to use HashMap to store username + password combination. 
    private GameScreen gameScreen;

    public LoginPanel() {
        setLayout(new GridLayout(5, 1, 10, 10));
        setBorder(BorderFactory.createEmptyBorder(40, 60, 40, 60));

        JLabel title = new JLabel("Login / Register", SwingConstants.CENTER);
        title.setFont(new Font("ROMAN_BASELINE", Font.BOLD, 22)); //use Unique ROMAN BASELINE font for appearance 

        username = new JTextField();
        password = new JPasswordField();

        registerBtn = new JButton("Register");
        loginBtn = new JButton("Login");

        add(title); //actually add in the functional elements 
        add(username);
        add(password);
        add(registerBtn);
        add(loginBtn);
    }

    public void setCardLayout(CardLayout cardLayout, JPanel parent, HashMap<String, String> users, GameScreen gameScreen)
    {
        this.cardLayout = cardLayout;
        this.parentPanel = parent;
        this.users = users;
        this.gameScreen = gameScreen;

        // Register button logic
        registerBtn.addActionListener(e -> {
            String u = username.getText();
            String p = new String(password.getPassword());
            if (u.isEmpty() || p.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Username/password cannot be empty");
                return;
            }
            if (users.containsKey(u)) {
                JOptionPane.showMessageDialog(this, "Username already exists!");
            } else {
                users.put(u, p);
                JOptionPane.showMessageDialog(this, "Account Created! You can now login to play the game");
            }
        });

        // Login button logic
        loginBtn.addActionListener(e -> {
            String u = username.getText();
            String p = new String(password.getPassword());

            if (users.containsKey(u) && users.get(u).equals(p)) {
                JOptionPane.showMessageDialog(this, "Thank you for logging in!");
                cardLayout.show(parentPanel, "GAME");

                // START the game now after login
                gameScreen.gamePanel.startGame();

                // Make sure the game panel has focus for input
                gameScreen.gamePanel.requestFocusInWindow();
            } else {
                JOptionPane.showMessageDialog(this, "Invalid login");
            }
        });
    }
}
