package Pacman; 

import java.awt.*;  
import java.awt.event.ActionEvent; //keys inputs
import java.awt.event.ActionListener; //keys input all keys
import java.awt.event.KeyAdapter; //codes for each key pressed//specific keys
import java.awt.event.KeyEvent; //codes for each key pressed
import javax.swing.ImageIcon; //image pack similar to html
import javax.swing.JPanel; //opens up jpanel(window of the game);//swing :window based apps
import javax.swing.Timer; //timer for the game apan delay and animations sathi vaparto

public class Model extends JPanel implements ActionListener {
    // action listener is a interface that has a method called action performed
    // jpanel is a class that is used to create a window

    private Dimension d;// dimensions but not for mearsurement
    private final Font smallFont = new Font("Arial", Font.BOLD, 14);
    private boolean inGame = false;     //game is not in progress
    private boolean dying = false;      //pacman is dying
    // we are giving jpanel the measurements for the jpanel so apan size define
    // kartoy ani size ani bhootsarray

    private final int BLOCK_SIZE = 24;  // size of the block
    private final int N_BLOCKS = 15;    //15*24=360
    private final int SCREEN_SIZE = N_BLOCKS * BLOCK_SIZE;// volume
    private final int MAX_GHOSTS = 12;  // maximum number of ghosts
    private final int PACMAN_SPEED = 6; // speed of pacman

    private int N_GHOSTS = 6;
    private int lives, score;
    private int[] dx, dy;// pacman che x and y values (delta x and delta y array)
    private int[] ghost_x, ghost_y, ghost_dx, ghost_dy, ghostSpeed;// bhoot cha positions and speed and dx and dy are
                                                                   // changing values
    // and x and y are opening values

    private Image heart, ghost;// import images from the image pack
    private Image up, down, left, right;// import images from the image pack

    private int pacman_x, pacman_y, pacmand_x, pacmand_y;// default spawning positions of the pacman
    private int req_dx, req_dy;// requested direction(guess)

    // 0=blue, 1=left_border, 2=top+border ,4=right_border, 8=bottom_border,
    // 16=whitedots;
    // used to make maps

    private final short levelData[] = {     // map array
            19, 18, 18, 26, 26, 18, 18, 18, 18, 18, 18, 18, 18, 18, 22,
            17, 16, 20, 0, 0, 17, 16, 16, 16, 16, 16, 16, 16, 16, 20,
            17, 16, 20, 0, 0, 17, 16, 16, 16, 16, 24, 16, 16, 16, 20,
            17, 16, 16, 18, 18, 16, 16, 16, 16, 20, 0, 25, 16, 16, 20,
            17, 16, 24, 24, 24, 24, 24, 24, 16, 20, 0, 0, 25, 16, 20,
            17, 20, 0, 0, 0, 0, 0, 0, 17, 20, 0, 0, 0, 17, 20,
            17, 16, 22, 0, 0, 0, 0, 19, 16, 16, 18, 18, 18, 16, 20,
            17, 16, 28, 0, 0, 0, 0, 25, 16, 16, 16, 16, 16, 16, 20,
            17, 20, 0, 0, 0, 0, 0, 0, 17, 16, 16, 16, 16, 16, 20,
            17, 16, 18, 18, 18, 18, 18, 18, 16, 16, 24, 16, 16, 16, 20,
            17, 16, 16, 16, 16, 16, 16, 16, 16, 28, 0, 25, 16, 16, 20,
            17, 16, 16, 16, 16, 16, 16, 16, 20, 0, 0, 0, 17, 16, 20,
            17, 16, 16, 16, 16, 16, 16, 16, 16, 22, 0, 19, 16, 16, 20,
            17, 16, 16, 16, 16, 16, 16, 16, 16, 20, 0, 17, 16, 16, 20,
            25, 24, 24, 24, 24, 24, 24, 24, 24, 24, 26, 24, 24, 24, 28
    };

    private final int validSpeeds[] = { 1, 2, 3, 4, 6, 8 };// allowed speeds for the pacman
    private final int maxSpeed = 6;// max speed =6

    private int currentSpeed = 3;
    private short[] screenData;// short is datatype and the screendata is for manipulation of the screen data
                               // and is an array
    private Timer timer; // timer for the game apan delay and animations sathi vaparto

    public Model() {// class cha constructor banavtoy and the various methods in it

        loadImages();// for loading the images
        initVariables();// for initializing the variables
        addKeyListener(new TAdapter()); // for adding the key listenerin the game   //TAdapter is a class   
        // for adding the key listener and the key adapter is a class that has a method
                                       // called action performed(guess)
        setFocusable(true);// for focusing on a particular value
        initGame();// initialisation of the game
    }

    private void loadImages() {// loading images
        down = new ImageIcon("Pacman/down.gif").getImage();
        up = new ImageIcon("Pacman/up.gif").getImage();
        left = new ImageIcon("Pacman/left.gif").getImage();
        right = new ImageIcon("Pacman/right.gif").getImage();
        ghost = new ImageIcon("Pacman/ghost.gif").getImage();
        heart = new ImageIcon("Pacman/heart.png").getImage();   

    }

    private void initVariables() { // initializing the variables in short datatype
        // max values for the screen data and the screen
        // data is an array 225 in this case 225 possible outcomes for pacman positions
        // on the map
        screenData = new short[N_BLOCKS * N_BLOCKS];
        d = new Dimension(400, 400);
        // 400 is the width and 400 is the height//pixels of dimesion class is used
        // for the dimensions of the game
        ghost_x = new int[MAX_GHOSTS];// giving runtime value for the ghost x
        ghost_dx = new int[MAX_GHOSTS];// giving runtime value for the ghost dx
        ghost_y = new int[MAX_GHOSTS];// giving runtime value for the ghost y
        ghost_dy = new int[MAX_GHOSTS];// giving runtime value for the ghost dy
        ghostSpeed = new int[MAX_GHOSTS];// giving runtime value for the max ghosts
        dx = new int[4];// initialsing the dx and dy values
        dy = new int[4];// initialsing the dx and dy values

        timer = new Timer(40, this);// calling actionlistener in this and the 40 is the delay in the timer
        // ..implements action listenerin this particular class of model class and using
        // this
        timer.start();
    }

    private void playGame(Graphics2D g2d) // function for playing the game graphics2d is the imported from the graphics
                                          // class
    // ..provides control over color management and text layout,geometry etc.
    // g2d is the object of graphics2d class
    {

        if (dying) {        

            death();    // calling the death method

        } else {

            movePacman();   // calling the move pacman method
            drawPacman(g2d);    // calling the draw pacman method
            moveGhosts(g2d);    // calling the moveghosts method
            checkMaze();    // check the maze for the pacman
        }
    }

    private void showIntroScreen(Graphics2D g2d) {// shows intro screen

        String start = "Press SPACE to start";/// main screen

        g2d.setColor(Color.yellow);// yellow color

        g2d.drawString(start, (SCREEN_SIZE) / 4, 150);// middle alignment
    }

    private void drawScore(Graphics2D g) {// we use graphics2d class for editing gpanel..g is different obj name
        g.setFont(smallFont);// setfont

        g.setColor(new Color(5, 181, 79));// green color

        String s = "Score: " + score;// score is being incremented

        g.drawString(s, SCREEN_SIZE / 2 + 96, SCREEN_SIZE + 16);// score is shown here
        // s is printed and 2 axes are used- y pos the x position
        // drawstring is in graphics class of java

        for (int i = 0; i < lives; i++) {   // for loop for the lives

            g.drawImage(heart, i * 28 + 8, SCREEN_SIZE + 1, this);// draws amount of lives or hearts we have currently
        }
    }

    private void checkMaze() {

        int i = 0;
        boolean finished = true;// initialize and declaring the

        while (i < N_BLOCKS * N_BLOCKS && finished) {// smaller than volume and if blocks are finished increase i;

            if ((screenData[i]) != 0) { // if the screen data is not equal to 0
                finished = false;   // if the screen data is not equal to 0 then finished is false
            }

            i++;
        }

        if (finished) {

            score += 50;

            if (N_GHOSTS < MAX_GHOSTS) {// increasing ghosts
                N_GHOSTS++;
            }

            if (currentSpeed < maxSpeed) {// increasing speed of ghosts
                currentSpeed++;
            }

            initLevel();    // initializing the level
        }
    }

    private void death() {  // death function

        lives--;        

        if (lives == 0) {   // if lives are 0 then game over
            inGame = false; // if lives are 0 then game is over
        }

        continueLevel();    // continue level until lives are not 0
    }

    private void moveGhosts(Graphics2D g2d) {   // for moving the ghosts    

        int pos;                            // pos is the position of the ghost
        int count;                        // count is the count of the ghost

        for (int i = 0; i < N_GHOSTS; i++) { // for loop for the ghosts
            if (ghost_x[i] % BLOCK_SIZE == 0 && ghost_y[i] % BLOCK_SIZE == 0) { // if the ghost is on the block
                pos = ghost_x[i] / BLOCK_SIZE + N_BLOCKS * (int) (ghost_y[i] / BLOCK_SIZE); // pos is the position of
                                                                                            // the ghost_y

                count = 0;

                if ((screenData[pos] & 1) == 0 && ghost_dx[i] != 1) { // if the ghost is not moving right and the ghost
                                                                      // is not on the block
                    dx[count] = -1;
                    dy[count] = 0;
                    count++;
                }

                if ((screenData[pos] & 2) == 0 && ghost_dy[i] != 1) { // if the ghost is not moving down and the ghost
                                                                      // is not on the block
                    dx[count] = 0;
                    dy[count] = -1;
                    count++;
                }

                if ((screenData[pos] & 4) == 0 && ghost_dx[i] != -1) { // if the ghost is not moving left and the ghost
                                                                       // is not on the block
                    dx[count] = 1;
                    dy[count] = 0;
                    count++;
                }

                if ((screenData[pos] & 8) == 0 && ghost_dy[i] != -1) { // if the ghost is not moving up and the ghost is
                                                                       // not on the block
                    dx[count] = 0;
                    dy[count] = 1;
                    count++;
                }

                if (count == 0) {

                    if ((screenData[pos] & 15) == 15) { // if the ghost is on the block
                        ghost_dx[i] = 0;
                        ghost_dy[i] = 0;
                    } else {
                        ghost_dx[i] = -ghost_dx[i]; // if the ghost is not on the block
                        ghost_dy[i] = -ghost_dy[i]; // if the ghost is not on the block
                    }

                } else {

                    count = (int) (Math.random() * count); // count is the random number

                    if (count > 3) {    // if the count is greater than 3
                        count = 3;  // if the count is greater than 3
                    }

                    ghost_dx[i] = dx[count]; // ghost_dx is the dx and ghost_dy is the dy
                    ghost_dy[i] = dy[count]; // count is the random number
                }

            }

            ghost_x[i] = ghost_x[i] + (ghost_dx[i] * ghostSpeed[i]);
            ghost_y[i] = ghost_y[i] + (ghost_dy[i] * ghostSpeed[i]);
            drawGhost(g2d, ghost_x[i] + 1, ghost_y[i] + 1);

            if (pacman_x > (ghost_x[i] - 12) && pacman_x < (ghost_x[i] + 12) // if pacman is in the ghost range
                    && pacman_y > (ghost_y[i] - 12) && pacman_y < (ghost_y[i] + 12) // if pacman is in the ghost range
                    && inGame) {

                dying = true;
            }
        }
    }

    private void drawGhost(Graphics2D g2d, int x, int y) { // draws the ghost
        g2d.drawImage(ghost, x, y, this); // draws the ghost
        g2d.drawImage(ghost, x, y, this); // draws the ghost
    }

    private void movePacman() {

        int pos;// deifining the position of the pacman
        short ch;// defining the checkMaze

        if (pacman_x % BLOCK_SIZE == 0 && pacman_y % BLOCK_SIZE == 0) { // if the pacman is on the blocks
            pos = pacman_x / BLOCK_SIZE + N_BLOCKS * (int) (pacman_y / BLOCK_SIZE); // pos is the position of the
                                                                                    // pacman_y
            ch = screenData[pos]; // ch is the checkMaze

            if ((ch & 16) != 0) {
                screenData[pos] = (short) (ch & 15); // if the pacman is on the block
                score++; // incrementing the score
            }

            if (req_dx != 0 || req_dy != 0) {
                if (!((req_dx == -1 && req_dy == 0 && (ch & 1) != 0) // if the pacman is not moving left and the pacman
                                                                     // is not on the block
                        || (req_dx == 1 && req_dy == 0 && (ch & 4) != 0) // if the pacman is not moving right and the
                                                                         // pacman is not on the block
                        || (req_dx == 0 && req_dy == -1 && (ch & 2) != 0) // if the pacman is not moving down and the
                                                                          // pacman is not on the block
                        || (req_dx == 0 && req_dy == 1 && (ch & 8) != 0))) { // if the pacman is not moving up and the
                                                                             // pacman is not on the block
                    pacmand_x = req_dx;
                    pacmand_y = req_dy;
                }
            }

            // Check for standstill
            if ((pacmand_x == -1 && pacmand_y == 0 && (ch & 1) != 0) // if the pacman is not moving left and the pacman
                                                                     // is not on the block
                    || (pacmand_x == 1 && pacmand_y == 0 && (ch & 4) != 0) // if the pacman is not moving right and the
                                                                           // pacman is not on the block
                    || (pacmand_x == 0 && pacmand_y == -1 && (ch & 2) != 0) // if the pacman is not moving down and the
                                                                            // pacman is not on the block
                    || (pacmand_x == 0 && pacmand_y == 1 && (ch & 8) != 0)) { // if the pacman is not moving up and the
                                                                              // pacman is not on the block
                pacmand_x = 0; // pacmand_x is 0
                pacmand_y = 0; // pacmand_y is 0
            }
        }
        pacman_x = pacman_x + PACMAN_SPEED * pacmand_x; // pacman_x is the pacman_x plus the pacman speed
        pacman_y = pacman_y + PACMAN_SPEED * pacmand_y; // pacman_y is the pacman_y plus the pacman speed
    }

    private void drawPacman(Graphics2D g2d) {

        if (req_dx == -1) {
            g2d.drawImage(left, pacman_x + 1, pacman_y + 1, this); // draws the pacman
        } else if (req_dx == 1) {
            g2d.drawImage(right, pacman_x + 1, pacman_y + 1, this); // draws the pacman
        } else if (req_dy == -1) {
            g2d.drawImage(up, pacman_x + 1, pacman_y + 1, this); // draws the pacman
        } else {
            g2d.drawImage(down, pacman_x + 1, pacman_y + 1, this); // draws the pacman
        }
    }

    private void drawMaze(Graphics2D g2d) { // draws the maze

        short i = 0; // defining the i
        int x, y; // defining the x and y

        for (y = 0; y < SCREEN_SIZE; y += BLOCK_SIZE) { // for the y positions
            for (x = 0; x < SCREEN_SIZE; x += BLOCK_SIZE) { // for the x positions

                g2d.setColor(new Color(56, 72, 255)); // sets the color to blue
                g2d.setStroke(new BasicStroke(5)); // sets the stroke to 5 points

                if ((levelData[i] == 0)) {
                    g2d.fillRect(x, y, BLOCK_SIZE, BLOCK_SIZE); // fills the rectangle
                }

                if ((screenData[i] & 1) != 0) { // if the screenData is not 0
                    g2d.drawLine(x, y, x, y + BLOCK_SIZE - 1); // draws the line
                }

                if ((screenData[i] & 2) != 0) { // if the screenData is not 0
                    g2d.drawLine(x, y, x + BLOCK_SIZE - 1, y); // draws the line
                }

                if ((screenData[i] & 4) != 0) { // if the screenData is not 0
                    g2d.drawLine(x + BLOCK_SIZE - 1, y, x + BLOCK_SIZE - 1,
                            y + BLOCK_SIZE - 1);    // draws the line
                }

                if ((screenData[i] & 8) != 0) { // if the screenData is not 0
                    g2d.drawLine(x, y + BLOCK_SIZE - 1, x + BLOCK_SIZE - 1,
                            y + BLOCK_SIZE - 1);    // draws the line
                }

                if ((screenData[i] & 16) != 0) { // if the screenData is not 0
                    g2d.setColor(new Color(255, 255, 255)); // sets the color to white
                    g2d.fillOval(x + 10, y + 10, 6, 6); // fills the oval
                }

                i++;
            }
        }
    }

    private void initGame() {   // initializes the game

        lives = 3;  // lives is 3
        score = 0;  // sets the score to 0
        initLevel();    // initializes the level
        N_GHOSTS = 6;   // number of ghosts
        currentSpeed = 2;   // sets the current speed to 3
    }

    private void initLevel() {  // initializes the level

        int i;
        for (i = 0; i < N_BLOCKS * N_BLOCKS; i++) {// initializing the level and using a for loop for total volume of
                                                   // the frame and

            screenData[i] = levelData[i]; // setting the level data to the screen data and using the screen and level
                                          // data to draw maze
            // screendata changes because of eating
            // level data does not change because the frame is the same
        }

        continueLevel();
    }

    private void continueLevel() {  // continues the levelData

        int dx = 1;
        int random;

        for (int i = 0; i < N_GHOSTS; i++) {    // for the ghost_y positions

            ghost_y[i] = 4 * BLOCK_SIZE; // start position
            ghost_x[i] = 4 * BLOCK_SIZE;
            ghost_dy[i] = 0;
            ghost_dx[i] = dx;
            dx = -dx;
            random = (int) (Math.random() * (currentSpeed + 1));    // random number between 0 and currentSpeed     

            if (random > currentSpeed) {    // if the random number is greater than the currentSpeed
                random = currentSpeed;  // set the random number to the currentSpeed
            }

            ghostSpeed[i] = validSpeeds[random];    // setting the ghost speed to the valid speeds
        }

        pacman_x = 7 * BLOCK_SIZE; // start position
        pacman_y = 11 * BLOCK_SIZE;
        pacmand_x = 0; // reset direction move
        pacmand_y = 0;
        req_dx = 0; // reset direction controls
        req_dy = 0;
        dying = false;
    }

    // This method is needed to draw something on JPanel other than drawing the
    // background color.
    // This method already exists in a JPanel class so that we need to use the super
    // declaration to
    // add something to this method and takes Graphics objects as parameters. The
    // super.paintComponent()
    // which represents the normal the paintComponent() method of the JPanel which
    // can only handle the
    // background of the panel must be called in the first line.
    public void paintComponent(Graphics g) {    // paintComponent methods   
        super.paintComponent(g);    

        Graphics2D g2d = (Graphics2D) g;    // Graphics2D object

        g2d.setColor(Color.black);
        g2d.fillRect(0, 0, d.width, d.height);  // fills the rectangle

        drawMaze(g2d);  // draws the maze
        drawScore(g2d); // draws the Score

        if (inGame) {   // if inGame is true
            playGame(g2d);  // playGame method
        } else {
            showIntroScreen(g2d);       // showIntroScreen method
        }

        Toolkit.getDefaultToolkit().sync(); // sync the screendata
        g2d.dispose();  // dispose the graphics object  
    }

    // controls
    class TAdapter extends KeyAdapter { // TAdapter class

        @Override
        public void keyPressed(KeyEvent e) {    // keyPressed method

            int key = e.getKeyCode();// when key is pressed, it will be stored in key variable

            if (inGame) {
                if (key == KeyEvent.VK_LEFT) {// if left is pressed, pacman will move left
                    req_dx = -1;
                    req_dy = 0;
                } else if (key == KeyEvent.VK_RIGHT) {  // if right is pressed, pacman will move right
                    req_dx = 1;
                    req_dy = 0;
                } else if (key == KeyEvent.VK_UP) { // if up is pressed, pacman will move up
                    req_dx = 0;
                    req_dy = -1;
                } else if (key == KeyEvent.VK_DOWN) {   // if down is pressed, pacman will move down
                    req_dx = 0; 
                    req_dy = 1;
                } else if (key == KeyEvent.VK_ESCAPE && timer.isRunning()) {// timer gets stopped if escape is pressed
                                                                            // and exited the game
                    inGame = false; // inGame is false
                }
            } else {    // if inGame is false
                if (key == KeyEvent.VK_SPACE) {// press space to start game
                    inGame = true;  // inGame is true
                    initGame(); // initializes the game
                }
            }
        }
    }

    @Override   // override the keyReleased method
    public void actionPerformed(ActionEvent e) {    // actionPerformed method
        repaint();  // repaint the screen
    }

}