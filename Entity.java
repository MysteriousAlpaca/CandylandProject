import java.awt.Graphics2D;
//CHATGPT code, dylan wang only verified bugs
/* ------------------- Entity base class ------------------- */
abstract class Entity {
    double x, y;
    double vx = 0, vy = 0;
    int radius = 20;
    boolean alive = true;

    Entity(double x, double y) {
        this.x = x; this.y = y;
    }

    void update() {
        x += vx;
        y += vy;
    }

    boolean contains(double px, double py) {
        double dx = px - x, dy = py - y;
        return dx * dx + dy * dy <= radius * radius;
    }

    abstract void draw(Graphics2D g);

    // Called when the user clicks the entity
    abstract void onClick(GamePanel panel);
}
