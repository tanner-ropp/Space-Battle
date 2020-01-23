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
    yPos = 100.0; // starts above the top of the window
    isDead = true; // all enemies start off "dead" and are made "alive" when they are ready to be deployed 
    hasAttacked = false;
    skin = chaotic;
  }
  
  void display()
  {
    // This will display the image for the chaotic enemey at its specified size and location
    image(skin, xPos, yPos, size*1.2, size);
  }
  
  void move()
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
    else if (r < 0.01) // randomly switches direction
    {
      xVel *= -1;
    }
    
    yPos += yVel;
    xPos += xVel;
  }
  
  void respawn(float _xVel, float _yVel, float _xPos)
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
