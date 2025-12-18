import java.awt.*;
import javax.swing.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

class Bomb extends Entity {

    private Image img;

    Bomb(double x, double y, double vy) {
        super(x, y);
        this.vy = vy;
        this.radius = 20;


        img = new ImageIcon("evilcandy.png").getImage();
    }

    @Override
    void draw(Graphics2D g) {
//Dylan wang was here, I know how to use the draw image. 
    // img = goldcandy.png, x-radius = center point subtract radius, moving to the top left which is where the image spawns, 
    //radius*2 = height, radius * 2 = width, obviously I don't know how to use an imageobserver so imageobserver part is set to null. 
    //yes, I had to cast all of it to an integer, because the radius can be a double as it moves across the screen. 
        g.drawImage(img, (int)(x - radius), (int)(y - radius), radius * 2, radius * 2, null);
    }

    @Override
    void onClick(GamePanel panel) {
        panel.addLife(-1);
        panel.breakStreak();
        panel.addScore(-25);


        int cx = (int)x, cy = (int)y;
        panel.addParticles(Particle.burst(cx, cy, 60, new Color(255, 120, 20)));

        // remove other entities within blast radius
        double blastRadius = 120;
        CopyOnWriteArrayList<Entity> entities = panel.entities;

        List<Entity> toRemove = new ArrayList<>();
        for (Entity e : entities) {
            if (e != this) {
                double dx = e.x - x, dy = e.y - y;
                if (dx*dx + dy*dy <= blastRadius * blastRadius) {
                    toRemove.add(e);
                    panel.addScore((e instanceof GoldenTarget) ? 150 : (e instanceof Target ? 5 : 0));
                }
            }
        }
        entities.removeAll(toRemove);
    }
}
