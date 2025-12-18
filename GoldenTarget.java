import java.awt.*;
import javax.swing.*;
class GoldenTarget extends Entity {
    private static final Image img = new ImageIcon("goldcandy.png").getImage();

    GoldenTarget(double x, double y, double vy) {
        super(x, y);
        this.vy = vy;
        this.radius = 26;
        this.vx = (Math.random() - 0.5) * 0.6;
    }

    @Override
    void draw(Graphics2D g) { //Dylan wang was here, I know how to use the draw image. 
    // img = goldcandy.png, x-radius = center point subtract radius, moving to the top left which is where the image spawns, 
    //radius*2 = height, radius * 2 = width, obviously I don't know how to use an imageobserver so imageobserver part is set to null. 
    //yes, I had to cast all of it to an integer, because the radius can be a double as it moves across the screen. 
        g.drawImage(img, (int)(x - radius), (int)(y - radius), radius * 2, radius * 2, null);
    }

    @Override
    void onClick(GamePanel panel) {
        panel.addScore(200);
        panel.increaseStreak();
        panel.addLife(1);
        panel.addParticles(Particle.burst((int)x, (int)y, 40, Color.YELLOW));
    }
}
