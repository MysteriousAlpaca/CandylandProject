import java.awt.*;

class Turret {

    int x;
    int y;

    double angle = 0;
    int recoil = 0;
    long lastShotTime = 0;

    // Constructor
    Turret(int panelWidth, int panelHeight) {
        x = panelWidth / 2;
        y = panelHeight ; // FIXED variable name
    }

    // Update turret direction based on mouse position
    void updateAim(Point mouse) {
        if (mouse == null){
          return;  
        } else {
            double dx = mouse.x - x;
            double dy = mouse.y - y;
            angle = Math.atan2(dy, dx); // Calculates the angle upward that the mouse is at
        }
    }

    // Fake firing (visual only)
    void shoot() {
        long now = System.currentTimeMillis();

        if (now - lastShotTime > 150) { //if Enough time in Milliseconds has passed since the last shot was fired
            recoil = 10; //Recoil is added
            lastShotTime = now;
        }
    }

    // Draw turret
    void draw(Graphics2D g) {

        g.setRenderingHint(
            RenderingHints.KEY_ANTIALIASING,
            RenderingHints.VALUE_ANTIALIAS_ON //Change the rendering preference for algorithms, choosing antialias and then 
            //turning antialias on. 
        );
        
        //after shooting, slowly reset the recoil. 
        if (recoil > 0) recoil--;

        // Turret base
        g.setColor(Color.PINK);
        g.fillRect(x - 20, y - 10, 40, 20);

        // Turret barrel
        int barrelLength = 40;
        
        int bx = (int)(x + Math.cos(angle) * (barrelLength - recoil));
        int by = (int)(y + Math.sin(angle) * (barrelLength - recoil));

        g.setStroke(new BasicStroke(6));
        g.setColor(Color.RED);
        g.drawLine(x, y, bx, by);
    }
}
