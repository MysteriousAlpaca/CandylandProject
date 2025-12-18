import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.Random;

/* ------------------- Particles (visual) ------------------- */
class Particle {
    private static final Random r = new Random(); // single random instance

    double x, y;
    double vx, vy;
    int life;
    final Color color;
    final double size;

    Particle(double x, double y, double vx, double vy, int life, Color color, double size) {
        this.x = x; this.y = y; this.vx = vx; this.vy = vy; this.life = life; this.color = color; this.size = size;
    }

    void update() {
        x += vx;
        y += vy;
        vy += 0.06; // gravity
        life--;
    }

    void draw(Graphics2D g) {
        if (life <= 0) return;
        float alpha = Math.max(0, life / 40f);
        Color c = new Color(color.getRed(), color.getGreen(), color.getBlue(), Math.min(255, Math.max(0, (int)(alpha*255))));
        g.setColor(c);
        g.fill(new Ellipse2D.Double(x - size/2, y - size/2, size, size));
    }

    static Particle random(int cx, int cy) {
        double angle = r.nextDouble() * Math.PI * 2;
        double speed = r.nextDouble() * 4;
        return new Particle(cx, cy, Math.cos(angle) * speed, Math.sin(angle) * speed - 2, 24 + r.nextInt(18),
                new Color(200 + r.nextInt(55), 60 + r.nextInt(60), 40 + r.nextInt(60)), 4 + r.nextDouble() * 4);
    }

    static List<Particle> burst(int cx, int cy, int count, Color color) {
        List<Particle> list = new CopyOnWriteArrayList<>();
        for (int i = 0; i < count; i++) {
            double angle = r.nextDouble() * Math.PI * 2;
            double speed = r.nextDouble() * 6;
            list.add(new Particle(cx, cy, Math.cos(angle) * speed, Math.sin(angle) * speed - 2,
                    20 + r.nextInt(20), color, 3 + r.nextDouble() * 5));
        }
        return list;
    }
}
