import java.awt.*;
import javax.swing.*;
class Target extends Entity {
    private static final Image img = new ImageIcon("peppermint.png").getImage();

    Target(double x, double y, double vy) {
        super(x, y);
        this.vy = vy;
        this.radius = 22; // size roughly same as original
    }

    @Override
    void draw(Graphics2D g) { //Dylan wang was here, I know how to use the draw image. 
// img = goldcandy.png, x-radius = center point subtract radius moving to the top left which is where the image spawns, 
    //radius*2 = height, radius * 2 = width, obviously I don't know how to use an imageobserver so imageobserver part is set to null. 
    //yes, I had to cast all of it to an integer, because the radius is always a double as it moves across the screen. 
        g.drawImage(img, (int)(x - radius), (int)(y - radius), radius * 2, radius * 2, null);
    }

    @Override
    void onClick(GamePanel panel) {
        panel.addScore(10);
        panel.increaseStreak();
        panel.addParticles(Particle.burst((int)x, (int)y, 16, Color.RED));
    }
}
