package Java.RenderingTests.PictureTest;

import java.awt.Color;
// import java.awt.event.ActionEvent;
// import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JPanel;
// import javax.swing.Timer;

public class picTest {
    static int sizeX = 800;
    static int sizeY = 800;
    static int pointCount = 5000;
    static int speed = 2;
    static int frameCount = 100;
    static boolean debug = false;

    static double[] time;
    static double[] time2;

    static int currentIndex = 0;
    static BufferedImage image;

    static String imagePath = "./NoFilePath.png";

    static NeighborGrid grid;

    public static void main(String[] args) throws InterruptedException {
        if (args.length > 0) {
            imagePath = args[0];
        } else {
            System.out.println("No image path provided, using default path");
        }

        double[] directions = new double[pointCount];
        Point[] points = new Point[pointCount];
        grid = new NeighborGrid(sizeX, sizeY, 50, 0.3);

        JFrame frame = new JFrame("Test");
        frame.setSize(sizeX, sizeY+20);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);

        time = new double[frameCount];
        time2 = new double[frameCount];
        
        try {
            File imageFile = new File(imagePath);
            image = ImageIO.read(imageFile);
            BufferedImage scaledImage = new BufferedImage(sizeX, sizeY, BufferedImage.TYPE_INT_ARGB);
            java.awt.Graphics2D g2d = scaledImage.createGraphics();
            g2d.drawImage(image.getScaledInstance(sizeX, sizeY, java.awt.Image.SCALE_SMOOTH), 0, 0, null);
            g2d.dispose();
            image = scaledImage;
        } catch (Exception e) {
            System.out.println(imagePath);
            e.printStackTrace();
            System.exit(1);
        }

        for (int i = 0; i < pointCount; i++) {
            points[i] = new Point((int) (Math.random() * sizeX), (int) (Math.random() * sizeY), new Color((int)(Math.random()*255)), i);
            points[i].color = new Color(image.getRGB(points[i].x, points[i].y));
            directions[i] = Math.random() * 2 * Math.PI;
            grid.addPoint(points[i]);
        }

        double startTime = System.currentTimeMillis();

        JPanel[] panels = new JPanel[frameCount];
        for (int i = 0; i < frameCount; i++){
            BufferedImage img = render(points, image, i);
            panels[i] = new JPanel() {
                @Override
                protected void paintComponent(java.awt.Graphics g) {
                    super.paintComponent(g);
                    g.drawImage(img, 0, 0, null);
                }
            };
            img.flush();
            
            
            for(int p = 0; p < pointCount; p++){
                // points[p].x += Math.cos(directions[p]) * speed;
                // points[p].y += Math.sin(directions[p]) * speed;

                // if(points[p].x > sizeX || points[p].x < 0){
                //     directions[p] = Math.PI - directions[p];
                // }
                // if(points[p].y > sizeY || points[p].y < 0){
                //     directions[p] = -directions[p];
                // }
                grid.movePoint(p, points[p].x + (int) (Math.cos(directions[p]) * speed), points[p].y + (int) (Math.sin(directions[p]) * speed));
                if(points[p].x >= sizeX || points[p].x <= 0){
                    directions[p] = Math.PI - directions[p];
                }
                
                if(points[p].y >= sizeY || points[p].y <= 0){
                    directions[p] = -directions[p];
                }
                
                try {

                    if(i < 50){points[p].color = new Color(image.getRGB(points[p].x, points[p].y));}
                } catch (Exception e) {
                    points[p].color = Color.BLACK;
                }

                
            }
            // System.out.println("Frame " + i + " rendered");
            progressBar(i, frameCount);
        }
        

        double endTime = System.currentTimeMillis();
        
        System.out.println("\n" + frameCount + " Frames rendered");
        
        for (int i = 0; i < frameCount; i++) {
            frame.getContentPane().removeAll();
            frame.getContentPane().add(panels[i]);
            frame.revalidate();
            frame.repaint();
            Thread.sleep(50);
        }
        
        double avg = 0;
        for(int i = 0; i < time2.length; i++){
            System.out.println("Frame " + i + " took " + time2[i] + "ms");
            avg += time2[i];
        }
        
        System.out.println("--------------------\n");
        System.out.println("Average time: " + avg / time2.length + "ms");
        System.out.println("Time: " + (endTime - startTime) + "ms");

        // grid.printGrid();


        // frame.add(panels[0]);
        // frame.revalidate();
        // Timer timer = new Timer(50, new ActionListener() {
        //     @Override
        //     public void actionPerformed(ActionEvent e) {
        //         currentIndex++;
        //         if (currentIndex < 100) {
        //             System.out.println("Frame " + currentIndex + " shown");
        //             frame.getContentPane().removeAll();
        //             frame.getContentPane().add(panels[currentIndex]);
        //             frame.revalidate();
        //             frame.repaint();
        //         } else {
        //             ((Timer) e.getSource()).stop(); // Stop the timer when all panels are shown
        //         }
        //     }
        // });
        // timer.start();
    }
    
    public static BufferedImage render(Point[] points, BufferedImage image, int fn) {
        // for (int i = 0; i < points.length; i++) {
        //     try {
        //         points[i].color = new Color(
        //                 image.getRGB(points[i].x, points[i].y));
        //     } catch (Exception e) { }
        // }

        // BufferedImage img = new BufferedImage(sizeX, sizeY, BufferedImage.TYPE_INT_ARGB);
        BufferedImage img = new BufferedImage(sizeX, sizeY, BufferedImage.TYPE_INT_ARGB);

        double startTime2 = System.currentTimeMillis();

        for (int y = 0; y < img.getHeight(); y++) {
            for (int x = 0; x < img.getWidth(); x++) {
                img.setRGB(x, y, findNearestPoint(x, y, points, fn).getRGB());
            }
        }

        double endTime2 = System.currentTimeMillis();
        time2[fn] = endTime2 - startTime2;

        if(debug){
            for (int p = 0; p < points.length; p++) {
                try {
                    img.setRGB(points[p].x, points[p].y, Color.WHITE.getRGB());
                } catch (Exception e) {}
            }

        // Draw grid lines
            java.awt.Graphics2D g2d = img.createGraphics();
            g2d.setColor(Color.GRAY);
            int gridSize = 100; // Adjust the grid size as needed
            for (int i = 0; i < sizeX; i += gridSize) {
                g2d.drawLine(i, 0, i, sizeY);
            }
            for (int j = 0; j < sizeY; j += gridSize) {
                g2d.drawLine(0, j, sizeX, j);
            }
            // Draw numbers on the grid
            g2d.setColor(Color.RED);
            int l = 0;
            for (int j = 0; j < sizeY; j += gridSize) {
                for (int i = 0; i < sizeX; i += gridSize) {
                    g2d.drawString(""+l, i + 5, j + 15);
                    l++;
                }
            }
            
            g2d.dispose();
        }
                
        return img;
    }

    static Color findNearestPoint(int x, int y, Point[] points, int fn) {
        // // double epsilon = 50;
        // int nearestPointIndex = 0;
        // double nearestDistance = Math.hypot(x - points[0].x, y - points[0].y);
        // for (int i = 1; i < points.length; i++) {
        //     // if (nearestDistance < epsilon) {
        //     //     break;
        //     // }
        //     double distance = Math.hypot(x - points[i].x, y - points[i].y);
        //     if (distance < nearestDistance) {
        //         nearestDistance = distance;
        //         nearestPointIndex = i;
        //     }
        // }
        // return points[nearestPointIndex].color;

        return grid.getNearestPoint(x, y).color;
    }

    static void progressBar(int current, int total) {
        int width = 50;
        float percent = (float) current / total;
        int num = (int) (percent * width);
        System.out.print("\r[");
        for (int i = 0; i < num; i++) {
            System.out.print("=");
        }
        for (int i = num; i < width; i++) {
            System.out.print(" ");
        }
        System.out.print("] " + (int) (percent * 100) + "%");
    }
}
