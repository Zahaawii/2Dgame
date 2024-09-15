package Main;

import Entity.Player;
import tile.TileManager;

import javax.swing.*;
import java.awt.*;

public class GamePanel extends JPanel implements Runnable {

    // SCREEN SETTINGS
    final int originalTitleSize = 16; // 16 x 16 tile
    final int scale = 3;

    public final int tileSize = originalTitleSize * scale; // 48 x 48 tile
    public final int maxScreenCol = 16;
    public final int maxScreenRow = 12;

    public final int screenWidth = tileSize * maxScreenCol; // 768 pixels
    public final int screenHeight = tileSize * maxScreenRow; // 576 pixels

    // World settings
    public final int maxWorldCol = 50;
    public final int maxWorldRow = 50;
    public final int worldWidth = tileSize * maxWorldCol;
    public final int worldHeight = tileSize * maxWorldRow;

    //FPS
    int FPS = 60;

    TileManager tileM = new TileManager (this);
    KeyHandler keyH = new KeyHandler();
    Thread gameThread;
   public Player player = new Player(this,keyH);


    public GamePanel() {

        this.setPreferredSize(new Dimension(screenWidth, screenHeight));
        this.setBackground(Color.black);
        this.setDoubleBuffered(true);
        this.addKeyListener(keyH);
        this.setFocusable(true);
    }


    public void startGameThread() {
        gameThread = new Thread(this);
        gameThread.start();
    }


    @Override
    public void run() {



         double drawInterval = 100000000/FPS; //0.01666 seconds
         double delta = 0;
         double lastTime = System.nanoTime();
         long currentTime = 0;
         long timer = 0;
         long drawCount = 0;

         while (gameThread != null) {
             currentTime = System.nanoTime();

             delta += (currentTime - lastTime) / drawInterval;
             timer += (currentTime - lastTime);
             lastTime = currentTime;

             if(delta >= 1) {
                 update();
                 repaint();
                 delta--;
                 drawCount++;
             }

             if(timer >= 100000000){
                 System.out.println("FPS: " + drawCount);
                 drawCount = 0;
                 timer = 0;
             }
         }

         //This solution is called a sleep loop. We will not use it, but it is here to demonstrate that there is two options
//        double nextDrawTime = System.nanoTime() + drawInterval;
//
//        while(gameThread != null) {
//
//            update();
//
//            repaint();
//
//            try {
//                double remainingTime = nextDrawTime - System.nanoTime();
//                remainingTime = remainingTime/1000000;
//
//                if(remainingTime < 0) {
//                    remainingTime = 0;
//                }
//                Thread.sleep((long) remainingTime);
//
//                nextDrawTime += drawInterval;
//
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//        }
    }

    public void update() {

        player.update();

    }

    public void paintComponent(Graphics g){

        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D)g;

        tileM.draw(g2);

        player.draw(g2);

        g2.dispose();
    }
}
