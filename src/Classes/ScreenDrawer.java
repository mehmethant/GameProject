package Classes;

import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import java.awt.image.*;
import java.util.LinkedList;
import java.util.TreeSet;
import javax.swing.*;

public final class ScreenDrawer implements  KeyListener {

    public static final Color BLACK      = Color.BLACK;
    public static final Color GREEN      = Color.GREEN;
    public static final Color RED        = Color.RED;
    public static final Color WHITE      = Color.WHITE;
    private static final Color DEFAULT_CLEAR_COLOR = WHITE;
    private static Color penColor;
    private static final int DEFAULT_SIZE = 512;
    private static final int width  = DEFAULT_SIZE;
    private static final int height = DEFAULT_SIZE;

    private static final double DEFAULT_PEN_RADIUS = 0.002;
    private static boolean defer = false;
    private static long nextDraw = -1;
    private static int animationSpeed = -1;

    private static final double BORDER = 0;
    private static final double DEFAULT_XMIN = 0.0;
    private static final double DEFAULT_XMAX = 1.0;
    private static final double DEFAULT_YMIN = 0.0;
    private static final double DEFAULT_YMAX = 1.0;
    private static double xmin, ymin, xmax, ymax;
    private static double xscale, yscale;

    private static final Object mouseLock = new Object();
    private static final Object keyLock = new Object();

    private static Font FONT = new Font("FF", Font.PLAIN, 16);

    private static BufferedImage offscreenImage;
    private static Graphics2D offscreen;
    private static Graphics2D onscreen;

    private static final ScreenDrawer scrDrw = new ScreenDrawer();

    private static JFrame frame;

    private static final LinkedList<Character> keysTyped = new LinkedList<>();

    private static final TreeSet<Integer> keysDown = new TreeSet<>();
    static { init(); }


    private static void init() {
        if (frame != null) frame.setVisible(false);
        frame = new JFrame();
        offscreenImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        BufferedImage onscreenImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        offscreen = offscreenImage.createGraphics();
        onscreen  = onscreenImage.createGraphics();
        setXscale();
        setYscale();
        offscreen.setColor(DEFAULT_CLEAR_COLOR);
        offscreen.fillRect(0, 0, width, height);
        setPenRadius();
        setFont();
        clear();

        RenderingHints hints = new RenderingHints(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        hints.put(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        offscreen.addRenderingHints(hints);

        ImageIcon icon = new ImageIcon(onscreenImage);
        JLabel draw = new JLabel(icon);

        frame.setContentPane(draw);
        frame.addKeyListener(scrDrw);
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setTitle("Donkey KONG");
        frame.pack();
        frame.requestFocusInWindow();
        frame.setVisible(true);
    }



    public static void setXscale() {
        setXscale(DEFAULT_XMIN, DEFAULT_XMAX);
    }

    public static void setYscale() { setYscale(DEFAULT_YMIN, DEFAULT_YMAX); }

    public static void setXscale(double min, double max) {
        double size = max - min;
        synchronized (mouseLock) {
            xmin = min - BORDER * size;
            xmax = max + BORDER * size;
            setTransform();
        }
    }

    public static void setYscale(double min, double max) {
        double size = max - min;
        synchronized (mouseLock) {
            ymin = min - BORDER * size;
            ymax = max + BORDER * size;
            setTransform();
        }
    }

    private static void setTransform() {
        xscale = width  / (xmax - xmin);
        yscale = height / (ymax - ymin);
    }

    private static double  scaleX(double x) { return xscale * (x - xmin); }
    private static double  scaleY(double y) { return yscale * (ymax - y); }
    private static double factorX(double w) { return w * width  / Math.abs(xmax - xmin);  }
    private static double factorY(double h) { return h * height / Math.abs(ymax - ymin);  }

    public static void clear() {
        clear(DEFAULT_CLEAR_COLOR);
    }

    public static void clear(Color color) {
        offscreen.setColor(color);
        filledRectangle(0.5 * (xmax + xmin), 0.5 * (ymax + ymin), 0.5 * (xmax - xmin), 0.5 * (ymax - ymin));
        offscreen.setColor(penColor);
        draw();
    }

    public static void setPenRadius() { setPenRadius(DEFAULT_PEN_RADIUS); }

    public static void setPenRadius(double r) {
        if (r < 0) throw new IllegalArgumentException();
        float scaledPenRadius = (float) (r * DEFAULT_SIZE);
        BasicStroke stroke = new BasicStroke(scaledPenRadius, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND);
        offscreen.setStroke(stroke);
    }


    public static void setPenColor(Color color) {
        penColor = color;
        offscreen.setColor(penColor);
    }

    public static void setFont() {
        setFont(FONT);
    }

    public static void setFont(Font f) {
        FONT = f;
        offscreen.setFont(f);
    }

    public static void setFontSize(double pointSize) {
        setFont(FONT.deriveFont((float) pointSize));
    }



    private static void pixel(double x, double y) {
        offscreen.fillRect((int) Math.round(scaleX(x)), (int) Math.round(scaleY(y)), 1, 1);
    }



    public static void filledRectangle(double x, double y, double halfWidth, double halfHeight) {
        rectangle(x, y, halfWidth, halfHeight);
    }

    private static void rectangle(double x, double y, double halfWidth, double halfHeight) {
        if (halfWidth  < 0) throw new IllegalArgumentException();
        if (halfHeight < 0) throw new IllegalArgumentException();

        double xs = scaleX(x);
        double ys = scaleY(y);
        double ws = factorX(2 * halfWidth);
        double hs = factorY(2 * halfHeight);

        if (ws <= 1 && hs <= 1) pixel(x, y);
        else offscreen.fill(new Rectangle2D.Double(xs - ws / 2, ys - hs / 2, ws, hs));
        draw();
    }

    private static Image getImage(String filename) {
        ImageIcon icon = new ImageIcon(filename);
        if (icon.getImageLoadStatus() != MediaTracker.COMPLETE) {
            icon = new ImageIcon(filename);
        }
        return icon.getImage();
    }

    public static void picture(double x, double y, String s, double w, double h) {
        picture(x, y, s, w, h, 0);
    }

    public static void picture(double x, double y, String s, double w, double h, double degrees) {
        Image image = getImage(s);
        int iw = image.getWidth(null);
        int ih = image.getHeight(null);
        if (iw <= 0 || ih <= 0) throw new IllegalArgumentException();

        AffineTransform t = offscreen.getTransform();

        double xs = xscale * (x - xmin);
        double ys = height - yscale * (y - ymin);

        if (degrees != 0)
            offscreen.rotate(-Math.toRadians(degrees), xs, ys);
                //yuvarlama

        if (w == 0 && h == 0){
            offscreen.drawImage(image, (int) Math.round(xs - 0.5 * iw), (int) Math.round(ys - 0.5 * ih), null);
        }
        else {
            if (w == 0)
                w = (iw * h) / ih;
            if (h == 0)
                h = (ih * w) / iw;
            offscreen.drawImage(image, (int) Math.round(xs - 0.5 * w), (int) Math.round(ys - 0.5 * h),
                                (int) Math.round(w), (int) Math.round(h), null);

        }
        if (degrees != 0) offscreen.setTransform(t);
        draw();
    }



    public static void text(double x, double y, String s) {
        double dw;
        dw=-0.5;
        FontMetrics metrics = offscreen.getFontMetrics();
        int w = metrics.stringWidth(s);
        int h = metrics.getDescent();
        double xs = scaleX(x);
        double ys = scaleY(y);
        offscreen.drawString(s, (float) (xs + dw * w), (float) (ys + h));
        draw();
    }

    public static void show(int t) {
        long millis = System.currentTimeMillis();
        if (millis < nextDraw) {
            try {
                Thread.sleep(nextDraw - millis); }
            catch (InterruptedException e)
            {
                e.printStackTrace();
            }
            millis = nextDraw;
        }
        defer = false;
        draw();
        defer = true;

        nextDraw = millis + t;
    }

    public static void show() {
        defer = false;
        draw();
    }
    private static void draw() {
        if (defer) return;
        onscreen.drawImage(offscreenImage, 0, 0, null);
        frame.repaint();
    }

    public static void disableAnimation() {
        animationSpeed = -1;
        show();
    }

    public static void FPS(double frameRate) {
        if (frameRate < 0)
            throw new IllegalArgumentException();
        animationSpeed = frameRate == 0 ? 0 : (int) Math.round(1000.0 / frameRate);
        show(0);
    }

    public static void advance() {
        if (animationSpeed < 0)
            throw new RuntimeException();

        show(animationSpeed);
    }

    public static boolean hasNextKeyTyped() {
        synchronized (keyLock) {
            return !keysTyped.isEmpty();
        }
    }

    public static char nextKeyTyped() {
        synchronized (keyLock) {
            return keysTyped.removeLast();
        }
    }

    public void keyTyped(KeyEvent e) {
        synchronized (keyLock) {
            keysTyped.addFirst(e.getKeyChar());
        }
    }

    public void keyPressed(KeyEvent e) {
        synchronized (keyLock) {
            keysDown.add(e.getKeyCode());
        }
    }

    public void keyReleased(KeyEvent e) {
        synchronized (keyLock) {
            keysDown.remove(e.getKeyCode());
        }
    }
}
