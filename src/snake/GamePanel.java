package snake;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Random;


public class GamePanel extends JPanel implements ActionListener {

    static final int SCREEN_WIDTH = 600;
    static final int SCREEN_HEIGHT = 600;
    static final int UNIT_SIZE = 25;
    static final int GAME_UNITS = (SCREEN_WIDTH * SCREEN_HEIGHT)/UNIT_SIZE;
    static final int DELAY = 125;
    static final int MAX_POS = SCREEN_WIDTH / UNIT_SIZE;
    static final int MAX_POSH = SCREEN_HEIGHT / UNIT_SIZE;
    final int[] x = new int[GAME_UNITS];
    final int[] y = new int[GAME_UNITS];
    int bodyParts = 1;
    int applesEaten;
    int appleX;
    int appleY;
    int beka = 0;
    char direction = 'R';
    boolean running = false;
    Timer timer;
    Random random;
    public final int tonicnierobi = 0;

    GamePanel(){
        random = new Random();
        this.setPreferredSize(new Dimension(SCREEN_WIDTH,SCREEN_HEIGHT));
        this.setBackground(Color.BLACK);
        this.setFocusable(true);
        this.addKeyListener(new MyKeyAdapter());
        startGame();
    }

    public void startGame(){
        newApple();
        running = true;
        timer = new Timer(DELAY,this);
        timer.start();
    }

    public void paintComponent(Graphics g){
        super.paintComponent(g);
        draw(g);
    }

    public void draw(Graphics g) {
        if(running){
            for (int i = 0; i < MAX_POS ; i++) {
                g.drawLine(i * UNIT_SIZE, 0, i * UNIT_SIZE, SCREEN_HEIGHT);
                g.drawLine(0, i * UNIT_SIZE, SCREEN_WIDTH, i * UNIT_SIZE);
            }
            makeApple(g,appleX,appleY);
            drawBody(g);

            drawScore(g);
        }
        else{
            gameOver(g);
        }



    }


    public void drawScore(Graphics g){
        g.setColor(Color.ORANGE);
        g.setFont(new Font("Impact",Font.BOLD,30));
        FontMetrics metrics = getFontMetrics(g.getFont());
        g.drawString("WYNIK: " + applesEaten,(SCREEN_WIDTH - metrics.stringWidth("WYNIK: " + applesEaten)-20),SCREEN_HEIGHT -10);
    }

    public void newApple(){
        appleX = random.nextInt(((int)(MAX_POS))) * UNIT_SIZE;
        appleY = random.nextInt(((int)(MAX_POSH))) * UNIT_SIZE;
    }

    public void makeApple(Graphics g,int xpos,int ypos){
        g.setColor(Color.RED);
        g.fillRoundRect(xpos,ypos,UNIT_SIZE,UNIT_SIZE,15,15);
        g.setColor(Color.GREEN);
        g.fillRoundRect(xpos+12,ypos - 6,4*UNIT_SIZE/20,7*UNIT_SIZE/20,5,5);
    }

    public void move(){
        for(int i = bodyParts; i > 0; i--){
            x[i] = x[i-1];
            y[i] = y[i-1];

        }

        switch (direction){
            case 'U':
                y[0] -= UNIT_SIZE;
                break;
            case 'D':
                y[0] += UNIT_SIZE;
                break;
            case 'R':
                x[0] += UNIT_SIZE;
                break;
            case 'L':
                x[0] -= UNIT_SIZE;

        }
    }

    public void drawBody(Graphics g){
        for(int i = 0; i<bodyParts;i++){
            if(i == 0){
                g.setColor(new Color(0x197519));
                g.fillRect(x[i],y[i],UNIT_SIZE,UNIT_SIZE);
            }
            else{
                g.setColor(Color.GREEN);
                g.fillRect(x[i],y[i],UNIT_SIZE,UNIT_SIZE);
            }
        }
    }

    public void checkApple(){
        if(x[0] == appleX && y[0] == appleY){
            newApple();
            applesEaten++;
            bodyParts++;


        }
    }

    public void checkCollisions(){
        //check for head touch body
        for(int i = bodyParts; i>0;i--){
            if(x[0] == x[i] && y[0] == y[i]){
                running = false;
            }
        }
        //check for head touch border
        if(x[0] < 0 || x[0] > (MAX_POS * UNIT_SIZE) - UNIT_SIZE  || y[0] < 0 || y[0] > (MAX_POSH * UNIT_SIZE) - UNIT_SIZE  ){
            running = false;
        }

        if(running == false){
            timer.stop();
        }
    }

    public void gameOver(Graphics g){
        //GameOver Text
        g.setColor(Color.RED);
        g.setFont(new Font("Impact",Font.BOLD,100));
        FontMetrics metrics = getFontMetrics(g.getFont());
        g.drawString("PRZEGRAŁEŚ",(SCREEN_WIDTH - metrics.stringWidth("PRZEGRAŁEŚ"))/2,SCREEN_HEIGHT / 2);

        g.setColor(Color.ORANGE);
        g.setFont(new Font("Impact",Font.BOLD,100));
        FontMetrics metrics1 = getFontMetrics(g.getFont());
        g.drawString("WYNIK: " + applesEaten,(SCREEN_WIDTH - metrics1.stringWidth("WYNIK: " + applesEaten))/2,g.getFont().getSize());
    }

    public class MyKeyAdapter extends KeyAdapter{
        @Override
        public void keyPressed(KeyEvent e){
            switch (e.getKeyCode()){
                case KeyEvent.VK_LEFT:
                    if(direction != 'R'){
                        direction = 'L';
                    }
                    break;
                case KeyEvent.VK_RIGHT:
                    if(direction != 'L'){
                        direction = 'R';
                    }
                    break;
                case KeyEvent.VK_UP:
                    if(direction != 'D'){
                        direction = 'U';
                    }
                    break;
                case KeyEvent.VK_DOWN:
                    if(direction != 'U'){
                        direction = 'D';
                    }
                    break;
            }
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        if(running){
            move();
            beka++;
            checkApple();
            checkCollisions();
            beka++;
            if(beka % 60==0){
                bodyParts--;
            }
            if(bodyParts==0){
                timer.stop();
                running = false;
            }
        }

        repaint();

    }


}
