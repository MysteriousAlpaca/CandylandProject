import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.List;
import java.util.Random;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.CopyOnWriteArrayList;

class GamePanel extends JPanel {
    private final ScheduledExecutorService spawner = Executors.newScheduledThreadPool(1);
    private final Random rand = new Random();

    final CopyOnWriteArrayList<Entity> entities = new CopyOnWriteArrayList<>();
    private final CopyOnWriteArrayList<Particle> particles = new CopyOnWriteArrayList<>();

    volatile boolean running = false; // start paused
    final AtomicInteger score = new AtomicInteger(0);
    final AtomicInteger lives = new AtomicInteger(5);
    final AtomicInteger streak = new AtomicInteger(0);

    final JLabel scoreLabel = new JLabel();
    final JLabel livesLabel = new JLabel();
    final JLabel streakLabel = new JLabel();

    private final Timer gameLoop;
    private volatile int spawnIntervalMillis = 900;
    private volatile boolean gameOver = false;
    private boolean initialized = false;
    private Turret turret;
    private Point mousePos = null;

    private final Image background;

    GamePanel() {
        setFocusable(true);
        turret = new Turret(960, 640); // temporary size
        setDoubleBuffered(true);

        // Load background
        background = new ImageIcon("candyland.jpg").getImage();

        updateLabels();
        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                turret.x = getWidth() / 2;
                turret.y = getHeight() - 40;
                }
            
        });
        addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                mousePos = e.getPoint();
                turret.updateAim(mousePos);
                
            }
            
        });


        // Mouse listener
        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                // Only handle clicks if the game is running
                if (!running || gameOver) return;
                turret.shoot();
                handleClick(e.getPoint());
            }
        });

        // Key listener
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (!running || gameOver) return;
                if (e.getKeyCode() == KeyEvent.VK_P) togglePause();
                if (e.getKeyCode() == KeyEvent.VK_R) restart();
            }
        });

        // Game loop
        gameLoop = new Timer(16, ev -> {
            if (!running) return;
            updateGame();
            repaint();
        });
    }

    // Start game (call after login, which activates the timer, game over screen, and starts spawning enemies.)
    public void startGame() {
        if (!initialized) {
            running = true;
            gameOver = false;
            initialized = true;
            gameLoop.start();
            scheduleSpawner();
            requestFocusInWindow(); // ensure keyboard focus
        }
    }

    private void updateLabels() {
        scoreLabel.setText("Score: " + score.get());
        livesLabel.setText("Lives: " + lives.get());
        streakLabel.setText("Streak: " + streak.get());
    }

    private void scheduleSpawner() {
        spawner.schedule(new Runnable() {
            @Override
            public void run() {
                if (!running || gameOver) {
                    spawner.schedule(this, 500, TimeUnit.MILLISECONDS);
                    return;
                }
                spawnRandomEntity();
                int jitter = rand.nextInt(spawnIntervalMillis / 3 + 1);
                int delay = Math.max(100, spawnIntervalMillis + jitter - spawnIntervalMillis / 6);
                spawner.schedule(this, delay, TimeUnit.MILLISECONDS);
            }
        }, 500, TimeUnit.MILLISECONDS);

        Executors.newSingleThreadScheduledExecutor().scheduleAtFixedRate(() -> {
            if (!running || gameOver) return;
            if (spawnIntervalMillis > 300) spawnIntervalMillis -= 20;
        }, 10, 10, TimeUnit.SECONDS);
    }

    private void spawnRandomEntity() {
        int w = getWidth(), h = getHeight();
        if (w <= 0 || h <= 0) return;

        double typeRoll = rand.nextDouble();
        Entity e;
        if (typeRoll < 0.08) e = new GoldenTarget(randX(w), h + 40, -1.6 - rand.nextDouble() * 1.2);
        else if (typeRoll < 0.18) e = new Bomb(randX(w), h + 30, -1.8 - rand.nextDouble() * 1.6);
        else e = new Target(randX(w), h + 30, -1.2 - rand.nextDouble() * 1.1);

        e.vx = (rand.nextDouble() - 0.5) * 1.6;
        entities.add(e);
    }

    private int randX(int w) {
        int pad = 60;
        return pad + rand.nextInt(Math.max(10, w - pad * 2));
    }

    private void updateGame() {
        int w = getWidth(), h = getHeight();

        for (Entity e : entities) {
            e.update();
            if (e.x < 20) { e.x = 20; e.vx = Math.abs(e.vx); }
            if (e.x > w - 20) { e.x = w - 20; e.vx = -Math.abs(e.vx); }

            if (e.y + e.radius < -50) {
                entities.remove(e);
                if (e instanceof Target || e instanceof GoldenTarget) {
                    int newLives = lives.decrementAndGet();
                    streak.set(0);
                    updateLabels();
                    createSmallExplosion((int)e.x, (int)e.y, 8);
                    if (newLives <= 0) endGame();
                }
            }
        }

        for (Particle p : particles) {
            p.update();
            if (p.life <= 0) particles.remove(p);
        }
    }

    private void createSmallExplosion(int cx, int cy, int count) {
        for (int i = 0; i < count; i++) particles.add(Particle.random(cx, cy));
    }

    private void endGame() {
        gameOver = true;
        running = false;
        SwingUtilities.invokeLater(() -> {
            JOptionPane.showMessageDialog(this,
                    "GAME OVER\nScore: " + score.get(),
                    "Game Over",
                    JOptionPane.INFORMATION_MESSAGE);
        });
    }

    void togglePause() { running = !running; }

    void restart() {
        running = true;
        gameOver = false;
        entities.clear();
        particles.clear();
        score.set(0);
        lives.set(5);
        streak.set(0);
        spawnIntervalMillis = 900;
        updateLabels();
    }

    private void handleClick(Point p) {
        Entity clicked = null;
        for (Entity e : entities) {
            if (e.contains(p.x, p.y)) {
                if (clicked == null || e.y < clicked.y) clicked = e;
            }
        }
        if (clicked != null) {
            clicked.onClick(this);
            entities.remove(clicked);
            updateLabels();
        } else {
            streak.set(0);
            updateLabels();
            createSmallExplosion(p.x, p.y, 6);
        }
    }

    void addParticles(List<Particle> list) { particles.addAll(list); }
    void addScore(int pts) {
        score.addAndGet(pts);
        updateLabels();
    }
    void addLife(int delta) { lives.addAndGet(delta); updateLabels(); }
    void increaseStreak() {
        int s = streak.incrementAndGet();
        updateLabels();
        if (s % 10 == 0) { addScore(100*(s/10)); addLife(1); }
    }
    void breakStreak() { streak.set(0); updateLabels(); }

    @Override
    protected void paintComponent(Graphics gOrig) {
        super.paintComponent(gOrig);
        Graphics2D g = (Graphics2D) gOrig.create();
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        g.drawImage(background, 0, 0, getWidth(), getHeight(), null);

        entities.stream().sorted((a,b)->Double.compare(a.y,b.y)).forEach(e->e.draw(g));
        for (Particle p : particles) p.draw(g);
        turret.draw(g);

        if (!running && !gameOver) {
            g.setFont(g.getFont().deriveFont(28f));
            drawCenteredString(g, "PAUSED (Press P to resume)", getWidth(), getHeight());
        }
        if (gameOver) {
            g.setFont(g.getFont().deriveFont(44f));
            drawCenteredString(g, "GAME OVER", getWidth(), getHeight());
        }

        g.dispose();
    }

    private void drawCenteredString(Graphics2D g, String text, int width, int height) {
        FontMetrics metrics = g.getFontMetrics(g.getFont());
        int x = (width - metrics.stringWidth(text))/2;
        int y = (height - metrics.getHeight())/2 + metrics.getAscent();
        g.setColor(new Color(0,0,0,160));
        g.fillRoundRect(x-12, y-metrics.getAscent()-6, metrics.stringWidth(text)+24, metrics.getHeight()+12,12,12);
        g.setColor(Color.white);
        g.drawString(text, x, y);
    }
}
