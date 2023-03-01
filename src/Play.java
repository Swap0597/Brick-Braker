import javax.swing.JPanel;
import javax.swing.Timer;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.Graphics;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
public class Play extends JPanel implements KeyListener, ActionListener {
    private boolean play = false;      // initially the game is stopped
    private int score = 0;              // to store score
    private int totalbricks = 21;       // will be used in counting score
    private Timer Timer;
    private int delay = 8;              // increasing it slows down the ball and reducing increses it
    private int playerX = 300;          //310
    private int ballposX = 120;
    private int ballposY = 300;
    private int ballXdir = -1;       // -1
    private int ballYdir = -2;       // -2
    private MapGenerator map;

    public Play() {
        map = new MapGenerator(3, 7);
        addKeyListener(this);
        setFocusable(true);
        Timer = new Timer(delay, this);
        Timer.start();
    }

    public void paint(Graphics g) {
        // background
        g.setColor(Color.black);
        g.fillRect(0, 0, 700, 600);

        map.draw((Graphics2D) g);

        // boundaries
        g.setColor(Color.green);
        g.fillRect(0, 0, 3, 600);
        g.fillRect(0, 0, 700, 3);
        g.fillRect(683, 0, 3, 600);
        g.fillRect(0, 560, 700, 3);

        // score card
        g.setColor(Color.white);
        g.setFont(new Font("serif", Font.BOLD, 25));
        g.drawString(" " + score, 590, 30);

        // slider
        g.setColor(Color.yellow);
        g.fillRect(playerX, 552, 100, 8);

        //ball
        g.setColor(Color.GREEN);
        g.fillOval(ballposX, ballposY, 20, 20);

        // below slider
        if (ballposY > 560) {
            play = false;

            g.setColor(Color.white);
            g.fillRect(180, 270, 350, 80);

            g.setColor(Color.red);
            g.setFont(new Font("serif", Font.BOLD, 30));
            g.drawString("    Game Over Score: " + score, 190, 300);

            g.setFont(new Font("serif", Font.BOLD, 30));
            g.drawString("   Press Enter to Restart", 190, 340);
        }
        if(totalbricks == 0){
            play = false;

            g.setColor(Color.white);
            g.fillRect(180, 270, 350, 80);

            g.setColor(Color.red);
            g.setFont(new Font("serif",Font.BOLD,30));
            g.drawString("    YOU WON!!!: "+score,190,300);

            g.setFont(new Font("serif", Font.BOLD, 30));
            g.drawString("   Press Enter to Restart", 190, 340);
        }
        g.dispose();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Timer.start();

        if (play) {

            // ball intersecting with slider
            if (new Rectangle(ballposX, ballposY, 20, 20).intersects(new Rectangle(playerX, 552, 100, 8))) {
                ballYdir = -ballYdir;
            }

            A:
            for (int i = 0; i < map.map.length; i++) {
                for (int j = 0; j < map.map[0].length; j++) {
                    if (map.map[i][j] > 0) {
                        int brickX = j * map.bricksWidth + 80;
                        int brickY = i * map.bricksHeight + 50;
                        int bricksWidth = map.bricksWidth;
                        int bricksHeight = map.bricksHeight;

                        Rectangle rect = new Rectangle(brickX, brickY, bricksWidth, bricksHeight);
                        Rectangle ballrect = new Rectangle(ballposX, ballposY, 20, 20);
                        Rectangle brickrect = rect;

                        if (ballrect.intersects(brickrect)) {
                            map.setBricksValue(0, i, j);
                            totalbricks--;
                            score += 5;
                            if (ballposX + 19 <= brickrect.x || ballposX + 1 >= brickrect.x + bricksWidth) {
                                ballXdir = -ballXdir;

                            } else {
                                ballYdir = -ballYdir;
                            }
                            break A;
                        }
                    }
                }
            }

            ballposX += ballXdir;
            ballposY += ballYdir;
            if (ballposX < 0) {             // when ball hits left wall
                ballXdir = -ballXdir;
            }
            if (ballposY < 0) {             // when ball hits top wall
                ballYdir = -ballYdir;
            }
            if (ballposX > 670) {           // when ball hits right wall
                ballXdir = -ballXdir;
            }
        }
        repaint();          //calls paint() method ASAP
     }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
            if (playerX >= 590) {                       // prevents movement of slider from limits in right
                playerX = 590;
            } else {
                moveRight();
            }
        }
        if (e.getKeyCode() == KeyEvent.VK_LEFT) {
            if (playerX <= 0) {
                playerX = 0;
            } else {
                moveLeft();
            }
        }

        if (e.getKeyCode() == KeyEvent.VK_ENTER) {
            if (!play) {
                ballposX = 120;
                ballposY = 350;
                ballXdir = -1;
                ballYdir = -2;
                score = 0;
                playerX = 310;
                totalbricks = 21;
                map = new MapGenerator(3, 7);

                repaint();
            }
        }
    }

    public void moveRight ()
    {
        play = true;                // game starts
        playerX += 10;
    }
    public void moveLeft ()
    {
        play = true;                // game start
        playerX -= 10;
    }
}
