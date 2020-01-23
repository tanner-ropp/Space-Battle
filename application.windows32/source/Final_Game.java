import processing.core.*; 
import processing.data.*; 
import processing.event.*; 
import processing.opengl.*; 

import processing.sound.*; 

import java.util.HashMap; 
import java.util.ArrayList; 
import java.io.File; 
import java.io.BufferedReader; 
import java.io.PrintWriter; 
import java.io.InputStream; 
import java.io.OutputStream; 
import java.io.IOException; 

public class Final_Game extends PApplet {

/* CHARACTERS */
Hero player; // player controls this
Grunt[] grunts;
Chaotic[] chaotics;
Shooter[] shooters;
Token token;

/* BACKGROUND ELEMENTS */
Star[] stars;
Galaxy galaxy;

/* MOVEMENT VARS */
boolean leftPressed = false;
boolean rightPressed = false;

/* TIMERS AND COUNTERS */
int typeTimer;
int gruntTimer = millis();
int chaoticTimer = millis();
int shooterTimer = millis();
int scoreTimer = millis();
int waveTime;
int shooterStart;
int chaoticStart;
int gruntCounter = 0;
int chaoticCounter = 0;
int shooterCounter = 0;

/* IMAGES */
PImage heroSkin;
PImage gameOver;
PImage quit;
PImage bg;

/* FONT */
PFont font;

/* GAME-STATE ELEMENTS */
int state, score, finalScore, enemyMaxSpeed, enemyMinSpeed;

/* SOUND */

SoundFile introSound, normalSound, hardSound, buttonSound;

public void setup()
{
  
  imageMode(CENTER);
  
  /************************* LOAD DATA ***************************/
 
 // IMAGES
  bg = loadImage("spacebattle.jpg");
  
  heroSkin = loadImage("Option1.png");
  
  small = loadImage("Enemy1.3.png");
  medium = loadImage("Enemy1.2.png");
  large = loadImage("Enemy1.png");
  
  chaotic = loadImage("Enemy3.png");
  
  shooterIm = loadImage("Enemy2.png");
  
  starIms = new PImage[2];
  starIms[0] = loadImage("Star.png");
  starIms[1] = loadImage("Star2.png");
  
  galaxyIm = loadImage("Galaxy.png");
  
  tokenIm = loadImage("Token.png");
  
  // FONT
  font = createFont("Death Star.otf", 32);
  textFont(font);
  
  //SOUNDS
  damage = new SoundFile(this, "minecraftAlphaDamageSound.mp3");
  introSound = new SoundFile(this, "Sci-fi Pulse Loop.wav");
  normalSound = new SoundFile(this, "Space Cube.wav");
  hardSound = new SoundFile(this, "Arpeggio1 120.wav");
  tokenSound = new SoundFile(this, "Token Sound Effect.mp3");
  eatEnemySound = new SoundFile(this, "Transforming Sound Effect.mp3");
  eatEnemySound.amp(0.2f);
  laserSound = new SoundFile(this, "Laser Gun Sound Effect.mp3");
  buttonSound = new SoundFile(this, "button click.mp3");


  /*******************************GAME SETUP/OBJECT ASSIGNMENTS****************************/
  score = -1; // makes sure the game starts at score = 1 (the timing works out)
  
  player = new Hero(30, 5, 0, heroSkin);
  
  galaxy = new Galaxy(random(0, width), random(0, height), random(80,150));
  
  stars = new Star[20];
  for (int i = 0; i < stars.length; i++) // randomize every star 
  {
    stars[i] = new Star(random(0,width), random(0,height), random(10,30), starIms[(int)random(0,1.99f)]);
  }
  
  shooters = new Shooter[10];
  for (int i = 0; i < shooters.length; i++) // randomize every shooter
  {
    shooters[i] = new Shooter(40,random(30,width-30),0.7f);
  }
  
  token = new Token(random(20, width - 20));
  
  chaotics = new Chaotic[10];
  for (int i = 0; i < chaotics.length; i++) // randomize every chaotic enemy
  {
    chaotics[i] = new Chaotic(30,random(3,5), random(1,2), random(0, width));
  }

  grunts = new Grunt[20];
  for (int i = 0; i < 20; i++) // randomize every grunt
  {
    grunts[i] = new Grunt(random(20, 100), random(20, width-20), random(1, 2));
  }
  
  frameRate(60);
  noStroke();
  
   /*******************************SOUND****************************/
   introSound.loop(); // starts looping the music for the menu
}

public void draw()
{
  if (state == 0) 
  {
    /************************** MAIN MENU ********************************/
    background(bg);

    quit = loadImage("quit.jpg");
    imageMode(CENTER);
    image(quit, width/2, height - 100, 254, 62);

    /* HIGHLIGHT BUTTONS */
    if (onNormal())
    {
      noFill();
      strokeWeight(8);
      stroke(255, 255, 255);
      rectMode(CENTER);
      rect(width/2, height/2 -80, 254, 61);
    } 
    else if (onHard())
    {
      noFill();
      strokeWeight(8);
      stroke(255, 255, 255);
      rectMode(CENTER);
      rect(width/2, height/2 + 20, 254, 61);
    } 
    else if (onQuit())
    {
      noFill();
      strokeWeight(8);
      stroke(255, 255, 255);
      rectMode(CENTER);
      rect(width/2, height-100, 254, 61);
    }
  } 
  else if (state == 1 || state == 2) // GAME MODE
  {
    if (state == 1) // NORMAL VALUES
    {
      waveTime = 2000;
      enemyMaxSpeed = 4;
      enemyMinSpeed = 1;
      chaoticStart = 90000;
      shooterStart = 45000;
    }
    else if (state == 2) // HARD VALUES
    {
      waveTime = 500;
      enemyMaxSpeed = 10;
      enemyMinSpeed = 5;
      chaoticStart = 0;
      shooterStart = 0;
    }
    
    /********************* RESET GAME UPON DEATH*********************/
    if (player.lives == 0)
    {
      introSound = new SoundFile(this, "Sci-fi Pulse Loop.wav");
      introSound.loop(); // starts music of game over screen
      
      /* stops music of previous game state */
      if(state == 1)
      {
        //SOUND
        normalSound.stop(); 
      }
      else
      {
        hardSound.stop(); 
      }
      
      state = 3;
      finalScore = score; // used to display player score upon death
      score = 0;
      typeTimer = millis();
      player = new Hero(30, 5, 0, heroSkin);
      
      token = new Token(random(20, width-20));

      for (int i = 0; i < 20; i++)
      {
        grunts[i] = new Grunt(random(20, 100), random(20, width-20), random(1, 2));
      }
  
      for (int i = 0; i < shooters.length; i++)
      {
        shooters[i] = new Shooter(40,random(30,width-30),0.7f);
      }
   
      for (int i = 0; i < chaotics.length; i++)
      {
        chaotics[i] = new Chaotic(30,random(3,5), random(1,2), random(0, width));
      }
    }
    
    background(0);

    galaxy.display();
    galaxy.move();
    
    for (int i = 0; i < stars.length; i++)
    {
      stars[i].display();
      stars[i].move();
    }
    
    /************************MOVES AND DISPLAYS PLAYER*****************/
    player.move(leftPressed, rightPressed);
    player.display();

    /**************************DEPLOY ENEMIES************************/

    if (millis() - gruntTimer >= waveTime) // GRUNTS
    {
      for (int i = 0; i < (int)random(0, 5); i++)
      {
        gruntTimer = millis();
        grunts[gruntCounter%20].respawn(random(20, player.size*2), random(20, width-20), random(enemyMinSpeed, enemyMaxSpeed));
        gruntCounter++;
      }
    }
    
    
    if (millis() - chaoticTimer >= 12000 && millis() - typeTimer >= chaoticStart) // CHAOTICS
    {
      for (int i = 0; i < (int)random(0, 3); i++)
      {
        chaoticTimer = millis();
        chaotics[chaoticCounter%10].respawn(random(3,5), random(1,2), random(50,width-50));
        chaoticCounter++;
      }
    }
    
    if (millis() - shooterTimer >= 14000  && millis() - typeTimer >= shooterStart) // SHOOTERS
    {
      for (int i = 0; i < (int)random(0,3); i++)
      {
        shooterTimer = millis();
        shooters[shooterCounter%10].respawn(random(50,width-50));
        shooterCounter++;
      }
    }
    
    if (millis() - typeTimer >= shooterStart)
    {
      if (token.captured)
      {
        token.loop(random(20, width-20));
      }
    }

    /************************MOVES ENEMIES AND CHECKS FOR COLLISION/DEATHS*********************/

    if (!token.captured) // TOKEN
    {
      token.move();
      token.display();
      
      if (player.tokenCollision(token))
      {
        player.eatToken(token);
        score += 10;
      }
    }
    
    for (int i = 0; i < grunts.length; i++) // GRUNTS
    {
      if (!grunts[i].isDead)
      {
        grunts[i].move();
        grunts[i].display();

        if (player.gruntCollision(grunts[i]))
        {  
          if (player.size > grunts[i].size)
          {
            player.eatGrunt(grunts[i]); 
            grunts[i].isDead = true;
            score += 10;
          } 
          else if (!grunts[i].hasAttacked) // if grunt is bigger and hasnt already made contact
          {
            player.takeDamage();
            grunts[i].hasAttacked = true; // has made contact
          }
        }
      }
    }
    
    for (int i = 0; i < chaotics.length; i++) // CHAOTICS
    {
      if (!chaotics[i].isDead)
      {
        chaotics[i].move();
        chaotics[i].display();
        
        if (player.chaoticCollision(chaotics[i]))
        {
          if (chaotics[i].yPos < 627 - (0.5f*player.size))
          {
            player.eatChaotic();
            chaotics[i].isDead =true;
            score += 50;
          }
          else if (!chaotics[i].hasAttacked) // if chaotic hits the side of your ship
          {
            player.takeDamage();
            chaotics[i].hasAttacked = true;
          }
        }
      }
    }
    
    for (int i = 0; i < shooters.length; i++) // SHOOTERS
    {
       if (!shooters[i].hasPassed)
       {
        shooters[i].move();
        shooters[i].display();
        shooters[i].shoot();
        
        for (int j= 0; j < shooters[i].weapons.length; j++)
        {
          if (player.bulletCollision(shooters[i].weapons[j]) && !shooters[i].weapons[j].hasHit)
          {
            shooters[i].weapons[j].fired = false;
            shooters[i].weapons[j].hasHit = true;
            if (player.shield == 0)
            {
              player.takeDamage();
            }
            else
            {
              player.loseShield();
            }
          }
        }
      }
    }
    
    /********************************STATS***************************/
    if (millis() - scoreTimer >= 1000) // score increases every second
    {
      scoreTimer = millis();
      score++;
    }
    
    fill(255);
    textSize(20);
    text("Lives: " + player.lives, 410, 30);
    text("Score: " + score, 20, 30);
  }
  else if(state == 3) 
  {
    /************************ GAME OVER SCREEN **********************/
    background(0);

    gameOver = loadImage("gameOver.jpg");
    imageMode(CENTER);
    image(gameOver, 250, 300, 500, 667);
    
    textAlign(CENTER, CENTER);
    textSize(32);
    fill(255,255,0);
    text("Score: " + finalScore ,width/2, height/2 - 20); // displays top score the player reached;
    textAlign(LEFT);
    
    if (onQuit2())
    {
      noFill();
      strokeWeight(8);
      stroke(255, 255, 255);
      rectMode(CENTER);
      rect(345, 565, 160, 50);
    }
    
    else if (onHome())
    {
      noFill();
      strokeWeight(8);
      stroke(255, 255, 255);
      rectMode(CENTER);
      rect(147, 550, 80, 80);
    }
  }
}

public boolean onNormal() // returns true if the mouse is hovering over the "NORMAL" button on the main menu screen
{
    if(mouseX >= (width/2 - 127) && mouseX <= (width/2 + 127) && mouseY >= (height/2 - 110) && mouseY <= (height/2 - 50))
    {
      return true;
    } 
    else 
    {
      return false;
    }
}
  
public boolean onHard() // returns true if the mouse is hovering over the "HARD" button on the main menu
{
    if(mouseX >= (width/2 - 127) && mouseX <= (width/2 +127) && mouseY >= (height/2 - 10) && mouseY <= (height/2 + 50)) 
    {
      return true;
    } 
    else 
    {
      return false;
    }
}
  
public boolean onQuit() // returns true if the mouse is hovering over the "QUIT" button on the main menu
{
    if(mouseX >= (width/2 - 127) && mouseX <= (width/2 + 127) && mouseY >= (height - 130) && mouseY <= (height - 70))
    {
      return true;
    } 
    else
    {
      return false;
    }
}

public boolean onQuit2() // returns true if the mouse is hovering over the "QUIT" button on the game over screen
{
    if(mouseX >= 265 && mouseX <= 425 && mouseY >= 550 && mouseY <= 590)
    {
      return true;
    } 
    else
    {
      return false;
    }
}

public boolean onHome() // returns true if the mouse is hovering over the home/house button on the game over screen
{
  if (mouseX >= 107 && mouseX <= 187 && mouseY >= 510 && mouseY <= 590 )
  {
    return true;
  } 
  else 
  {
    return false;
  }
}
  
  
public void mouseClicked()
{
  /********************* USER INTERFACE FOR MENU***********************/
  if (onNormal() && state == 0)
  {
    // starts the game and its accompanying sounds
    state = 1;
    typeTimer = millis();
    //SOUND
    buttonSound.play();
    introSound.stop(); 
    normalSound.loop();
  }
  else if (onHard() && state == 0) 
  {
    // starts the game and its accompanying sounds
    state = 2;
    typeTimer = millis();
    //SOUND
    buttonSound.play();
    introSound.stop(); 
    hardSound.loop();
  }
  else if (onQuit() && state == 0)
  {
    exit();
  }
  else if (onQuit2() && state == 3)
  {
    exit();
  }
  else if (onHome())
  {
    // starts the mian menu screen and its accompanying sounds
    buttonSound.play();
    state = 0;
  }
}

public void keyPressed()
{
  /****************USER CONTROLS*****************/
  if (keyCode == LEFT) 
  {
    leftPressed = true;
  } else if (keyCode == RIGHT)
  {
    rightPressed = true;
  }
}

public void keyReleased()
{
  /**************USER CONTROLS*********************/
  if (keyCode == RIGHT)
  {
    rightPressed = false;
  } else if (keyCode == LEFT)
  {
    leftPressed = false;
  }
}
/* 
  This class represents the projectiles that will be fired at the player by the shooter class enemies.
  If one hits the player, it will take away one life, or one shield layer if present.  
*/

class Bullet
{
  float speed;
  float xPos;
  float yPos;
  boolean fired; // true if fired by shooter
  boolean hasHit; // true if did not hit player but past the bottom of the window
  
  Bullet() // default Bullet constructor
  {
    xPos = 0;
    yPos = 0;
    speed = 2;
    fired = false;
    hasHit = false;
  }
  
  public void display()
  {
    // Will display a small yellow "photon" looking pixel bullet at the specificed locaton and of the specified size
    rectMode(CENTER);
    fill(0,255,200);
    noStroke();
    rect(xPos,yPos, 5, 8);
  }
  
  public void move()
  {
    // will move the bullet down the window at the specified speed
    yPos += speed;
  }
}
/* 
  This class is a dynamic enemy type that will zig-zag down the screen.  
  They will be fast and difficult to catch, but if one is caught the player 
  will increase their max speed relative to the speed of the enemy. 
  These enemies cannot damage the player but will only show up rarely later in the game
*/

PImage chaotic;

class Chaotic
{
  float size;
  float xVel;
  float yVel;
  float xPos;
  float yPos;
  boolean isDead; // true if caught by the player
  boolean hasAttacked; // true if hits player
  PImage skin;
  
  Chaotic(float _size, float _xVel, float _yVel, float _xPos) // Chaotic enemy constructor
  {
    size = _size;
    xVel = _xVel;
    yVel = _yVel;
    xPos = _xPos;
    yPos = 100.0f; // starts above the top of the window
    isDead = true; // all enemies start off "dead" and are made "alive" when they are ready to be deployed 
    hasAttacked = false;
    skin = chaotic;
  }
  
  public void display()
  {
    // This will display the image for the chaotic enemey at its specified size and location
    image(skin, xPos, yPos, size*1.2f, size);
  }
  
  public void move()
  {
    // This will advance the location of the chaotic enemy based on its velocities, IF it is not dead
    // This method will also change the xVelocity of the enemy based on its location so it moves in a zig-zag path
    float r = random(0,1);
    if (yPos > height + 50)
    {
      isDead = true;
    }
    else if (xPos < size/2 + 20 || xPos > width - (size/2) - 20) // bounces off screen boundaries
    {
      xVel *= -1;
    }
    else if (r < 0.01f) // randomly switches direction
    {
      xVel *= -1;
    }
    
    yPos += yVel;
    xPos += xVel;
  }
  
  public void respawn(float _xVel, float _yVel, float _xPos)
  {
    // This will reset the enemy object at the top of the window, giving it a new x-position and velocity, essentially recycling the enemy and "reviving" it
    isDead = false;
    xVel = _xVel;
    yVel = _yVel;
    yPos = -100;
    xPos = _xPos;
    hasAttacked = false;
  }
}
/*
  This class is used to create a moveable galaxy in the background of the game to simulate space travel.
  The player cannot interact with this class objects.  It will show up much less often then stars.
*/

PImage galaxyIm;

class Galaxy
{
  float speed;
  float xPos;
  float yPos;
  float size;
  PImage skin;
  
  Galaxy(float _xPos, float _yPos, float _size) // Galaxy object constructor
  {
    xPos = _xPos;
    yPos = _yPos;
    size = _size;
    speed = 0.3f;
    skin = galaxyIm;
  }
  
  public void display()
  {
    // displays the galaxy image at thr specified location and at the specified size
    image(skin, xPos, yPos, size, size);
  }
  
  public void move()
  {
    // advances the galaxy down the screen at the specified uniform speed
    yPos += speed;
    if (yPos > height + 100)
    {
      loop(random(0, width), random(80, 150)); // galaxy is recycled immediately without a pause from a timer
    }
  }
  
  public void loop(float _xPos, float _size)
  {
    // resets the galaxy object back above the top of the window with a new position and size, providing and endless stream of galaxy objects over time
    xPos = _xPos;
    size = _size;
    yPos = -300;
  }
}
/* 
  This class is a basic enemy type that will move straight down the screen.  
  They will be of different speeds and sizes.  Grunts larger than the player will
  take a life if hit, and if one less than or equal to the size of the player is caught,
  the player will increase their size relative to the size of the enemy. 
  These enemies will be the first to show up in normal mode.
*/

PImage small;
PImage medium;
PImage large;

class Grunt
{
  float size;
  float speed; // velocity moving down the screen
  float xPos;
  float yPos;
  boolean isDead; // true if caught by the player
  boolean hasAttacked; // true when an enemy larger than the player hits the player
  PImage skin;
 
  Grunt(float _size, float _xPos, float _speed) // Grunt enemy constructor
  {
    size = _size;
    speed = _speed;
    xPos = _xPos;
    yPos = -200.0f;
    isDead = true; // new enemies start off dead and are made alive when they are ready to be deployed/displayed
    hasAttacked = false;
  }
  
  public void display() // displays the enmey at the correct location and with the correct size
  {  
    imageMode(CENTER);
    
    /* the size of the enemy determines its color */
    if (this.size < 75)
    {
      image(small,xPos,yPos,size,size);
    }
    else if (this.size < 150)
    {
      image(medium,xPos,yPos,size,size);
    }
    else
    {
      image(large,xPos,yPos,size,size);
    }
  }  
  
  public void move() // moves the enemy down the screen at the specified speed, marks it as dead if it passes the bottom of the screen
  {
    if (!(this.isDead)) // if enemy isn't dead
    {
      yPos += speed; // moves down the window
    }
    if(yPos > height + 200) // if enemy has passed the bottom of the screen
    {
      isDead = true;
    }
  }
  
  public void respawn(float _size, float _xPos, float _speed) // This function recycles dead enemies so they can be used again, appearing at a new random location with a random speed and size later on
  { 
    isDead = false; //"revives" the enemy
    hasAttacked = false;
    xPos = _xPos; // new x-position
    yPos = -200.0f; // brings enemy back to the top of the window
    size = _size; // new size
    speed = _speed;
  }
}
/* 
  This the class for the main player.  The hero starts with a fixed size, speed, and no shield and 3 lives at a central position.
  They can be moved side to side by the user, and can modify their size, speed, shield, and lose lives by interacting with the 
  other object in the game.  The player will be considered dead if they have no lives.
*/

SoundFile damage, tokenSound, eatEnemySound; 

class Hero
{
  float size;
  float maxSpeed;
  float shield;
  float xPos;
  PImage ship;
  int lives;

  Hero(float _size, float _maxSpeed, float _shield, PImage _ship) // Hero object constructor
  {
    size = _size;
    maxSpeed = _maxSpeed;
    shield = _shield;
    xPos = 300;
    lives = 3; // starts with three lives
    ship = _ship;
  }

  public void display()  // diplays the hero and any shield layer behind it
  {
    for (float i = shield; i > 0; i--) // displays all layers of shields if they are present
    {
      fill(0, 150 + 20*i, 255);
      noStroke();
      ellipse(xPos, 627, size + 10*i, size + 10*i);
    }
    imageMode(CENTER);
    image(ship, xPos, 627, 1.3f*size*240/330, 1.3f*size); // displays ship
  }

  public void move(boolean _leftPressed, boolean _rightPressed) // moves the player based on what keys are pressed
  {
    if (_leftPressed && !_rightPressed && xPos > 0.3f*size/2) // if only left arrow is pressed
    {
      xPos -= maxSpeed;
    } 
    else if (!_leftPressed && _rightPressed && xPos < (width - 0.3f*size/2)) // if only right arrow is pressed
    {
      xPos += maxSpeed;
    }
  }
  
  public boolean gruntCollision(Grunt enemy) // checks for collision with a grunt type enemy
  {
    if (dist(this.xPos, 627, enemy.xPos, enemy.yPos) < 0.8f*(this.size/2 + enemy.size/2))
    {
      return true;
    }
    else
    {
      return false;
    }
  }
  
  public boolean chaoticCollision(Chaotic enemy) // checks for collision with a chaotic type enemy
  {
    if (dist(this.xPos, 627, enemy.xPos, enemy.yPos) < 0.8f*(this.size/2 + enemy.size/2))
    {
      return true;
    }
    else
    {
      return false;
    }
  }
  
  public boolean bulletCollision(Bullet b) // checks for collision with a bullet from a shooter type enemy
  {
    if (dist(this.xPos, 627, b.xPos, b.yPos) < (this.size/2))
    {
      return true;
    }
    else
    {
      return false;
    }
  }
  
  public boolean tokenCollision(Token t) // checks for collision with a token
  {
    if (dist(this.xPos, 627, t.xPos, t.yPos) < 0.8f*(this.size/2 + t.size/2))
    {
      return true;
    }
    else
    {
      return false;
    }
  }
  
  public void eatGrunt(Grunt g)
  {
    size += 0.03f*g.size; // if grunt is caught, player grows relative to size of grunt
    eatEnemySound.play();
  }
  
  public void eatChaotic()
  {
    maxSpeed += 0.5f; // increaes speed of the player
    eatEnemySound.play();
  }
  
  public void eatToken(Token t)
  {
    t.captured = true;
    tokenSound.play();
    if (shield < 5) // increases shield but maxes out at five layers
      shield++;
  }  
  
  public void takeDamage()
  {
    lives--;
    damage.play();
  }
  
  public void loseShield()
  {
    shield--;
  }
}
/*
  This class represents the enemy type that moves straight down the screen, firing bullets that can take a life from the player.
  It will fire in bursts of three bullets.  These enemies cannot be caught.
*/

PImage shooterIm;

SoundFile laserSound;

class Shooter
{
  float size;
  float speed;
  float xPos;
  float yPos;
  int fireTime;
  int fireCount;
  boolean hasPassed; // true if passed the bottom of the window
  Bullet[] weapons; // stores bullets to fire
  PImage skin;
  
  Shooter(float _size, float _xPos, float _speed) // Shooter object constructor
  {
    weapons = new Bullet[10];
    size = _size;
    xPos = _xPos;
    yPos = -50;
    speed = _speed;
    hasPassed = true;
    skin = shooterIm;
    fireTime = millis(); // used to make bullets fire with space in between
    fireCount = 0;
    
    for (int i = 0; i < weapons.length; i++)
    {
      weapons[i] = new Bullet(); // assign bullet objects
    }
  }
  
  public void display() // displays the imageFile for the shooter at the specified location and at the specified size, as well as all the bullets currently fired from the shooter
  {
    image(shooterIm, xPos, yPos, size, size);
    for (int i = 0; i < weapons.length; i++)
    {
      if (weapons[i].fired && !weapons[i].hasHit)
      {
        weapons[i].display();
      }
    }
  }
  
  public void move() // moves the object down through the window at a specified speed, as well as any fired bullets
  {
    yPos += speed;
    for (int i = 0; i < weapons.length; i++)
    {
      if (weapons[i].fired)
      {
        weapons[i].move();
      }
    }
  }
  
  public void shoot() // causes the bullets stored in "weapons" to be fired from the ship at a specified time interval
  {
    if ((millis() - fireTime) > 1750 && yPos >= 0 && yPos <= width + 30)
    {
      weapons[fireCount%weapons.length].xPos = this.xPos;
      weapons[fireCount%weapons.length].yPos = this.yPos + 15;
      weapons[fireCount%weapons.length].fired = true;
      fireCount++;
      fireTime = millis();
      laserSound.play();
    }
  }
  
  public void respawn(float _xPos) // recyles the object after it passes the bottom of the window, allowing it to be deployed as a "new" shooter later
  {
    xPos = _xPos;
    yPos = -50;
    hasPassed = false;
  }
}
/*
  This class is used to create moeable stars in the background of the game to simulate space travel.
  The player cannot interact with these objects.
*/

PImage[] starIms;

class Star
{
  float speed;
  float xPos;
  float yPos;
  float size;
  PImage skin;
  
  Star(float _xPos, float _yPos, float _size, PImage im) // Star object constructor
  {
    xPos = _xPos;
    yPos = _yPos;
    size = _size;
    speed = 0.3f;
    skin = im;
  }
  
  public void display()
  {
    // displays the star image at thr specified location and at the specified size
    image(skin, xPos, yPos, size, size);
  }
  
  public void move()
  {
    // advances the star down the screen at the specified uniform speed
    yPos += speed;
    if (yPos > height + 20)
    {
      loop(random(0, width), random(10, 30)); // imediately recycles the star object without any timer or break
    }
  }
  
  public void loop(float _xPos, float _size)
  {
    // resets the star object back at the top of the screen with a new position and size, providing and endless stream of star objects
    xPos = _xPos;
    size = _size;
    yPos = -20;
  }
}
/*
  This class will be used as a token that, when caught by the player, will give them a 
  layer of shield that can be used to block bullets fired from shooter enemies.
*/

PImage tokenIm;

class Token
{
  float speed;
  float xPos;
  float yPos;
  float size;
  boolean hasPassed;
  boolean captured; // true if the player catches the token
  PImage skin;
  
  Token(float _xPos) // Token object constructor
  {
    xPos = _xPos;
    yPos = -200;
    size = 30;
    speed = 1;
    hasPassed = false;
    captured = true;
    skin = tokenIm;
  }
  
  public void display()
  {
    // displays the token image at the specified location and at the specified size
    image(skin, xPos, yPos, size, size);
  }
  
  public void move()
  {
    // advances the token down the screen at the specified uniform speed
    // sets hasPasses = true; if the star passes the bottom of the window
    yPos += speed;
    if (yPos > height + 50)
    {
      captured = true;
    }
  }
  
  public void loop(float _xPos)
  {
    // resets the token object back at the top of the screen with a new position, providing and endless stream of token objects
    yPos = -2000;
    xPos = _xPos;
    captured = false;
  }
}
  public void settings() {  size(500, 667); }
  static public void main(String[] passedArgs) {
    String[] appletArgs = new String[] { "--present", "--window-color=#666666", "--stop-color=#cccccc", "Final_Game" };
    if (passedArgs != null) {
      PApplet.main(concat(appletArgs, passedArgs));
    } else {
      PApplet.main(appletArgs);
    }
  }
}
