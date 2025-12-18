import javax.swing.*;
import java.awt.*;


class GameFrame extends JFrame {
    GamePanel gamePanel;

    GameFrame(String title) {
        super(title);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        
        gamePanel = new GamePanel();
        add(gamePanel, BorderLayout.CENTER);

        // Top Panel for labels
        JPanel top = new JPanel(new FlowLayout(FlowLayout.LEFT));
        top.add(gamePanel.scoreLabel);
        top.add(gamePanel.livesLabel);
        top.add(gamePanel.streakLabel);
        add(top, BorderLayout.NORTH);

        // Bottom Panel for controls
        JPanel bottom = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton pauseButton = new JButton("Pause");
        pauseButton.addActionListener(e -> gamePanel.togglePause());
        bottom.add(pauseButton);

        JButton restart = new JButton("Restart");
        restart.addActionListener(e -> gamePanel.restart());
        bottom.add(restart);

        add(bottom, BorderLayout.SOUTH);

        setSize(960, 640);
        setLocationRelativeTo(null);
    }
}
