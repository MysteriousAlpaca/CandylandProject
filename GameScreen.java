import java.awt.*;
import javax.swing.*;


class GameScreen extends JPanel {

    GamePanel gamePanel;

    GameScreen() {

        setLayout(new BorderLayout()); //The gamePanel has the same layout as the game itself

        gamePanel = new GamePanel(); //The gameScreen wrapper has to have the original gamePanel code accessible
        
        //Every gamePanel element from the existing game can be directly pulled using gamePanel.xxxxxx
        JPanel top = new JPanel(new FlowLayout(FlowLayout.LEFT));
        top.add(gamePanel.scoreLabel);//Pull the original score and other elements code
        top.add(gamePanel.livesLabel);
        top.add(gamePanel.streakLabel);


        JPanel bottom = new JPanel(new FlowLayout(FlowLayout.RIGHT));

        JButton wrapperpauseBtn = new JButton("Pause"); //the name of the variable does not matter here, I can name it whatever.
        JButton wrapperrestartBtn = new JButton("Restart");//as Long as the functionality is still "gamePanel.methodTitle"

        wrapperpauseBtn.addActionListener(e -> gamePanel.togglePause()); //Lambda expressions that listen for clicking on the buttons
        wrapperrestartBtn.addActionListener(e -> gamePanel.restart());

        bottom.add(wrapperpauseBtn); //actually add the buttons to the GUI
        bottom.add(wrapperrestartBtn);

        add(top, BorderLayout.NORTH); 
        add(gamePanel, BorderLayout.CENTER); //game itself runs in the center of the collective gamescreen in borderlayout code obviously
        add(bottom, BorderLayout.SOUTH);
    }
}
