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

  void display()  // diplays the hero and any shield layer behind it
  {
    for (float i = shield; i > 0; i--) // displays all layers of shields if they are present
    {
      fill(0, 150 + 20*i, 255);
      noStroke();
      ellipse(xPos, 627, size + 10*i, size + 10*i);
    }
    imageMode(CENTER);
    image(ship, xPos, 627, 1.3*size*240/330, 1.3*size); // displays ship
  }

  void move(boolean _leftPressed, boolean _rightPressed) // moves the player based on what keys are pressed
  {
    if (_leftPressed && !_rightPressed && xPos > 0.3*size/2) // if only left arrow is pressed
    {
      xPos -= maxSpeed;
    } 
    else if (!_leftPressed && _rightPressed && xPos < (width - 0.3*size/2)) // if only right arrow is pressed
    {
      xPos += maxSpeed;
    }
  }
  
  boolean gruntCollision(Grunt enemy) // checks for collision with a grunt type enemy
  {
    if (dist(this.xPos, 627, enemy.xPos, enemy.yPos) < 0.8*(this.size/2 + enemy.size/2))
    {
      return true;
    }
    else
    {
      return false;
    }
  }
  
  boolean chaoticCollision(Chaotic enemy) // checks for collision with a chaotic type enemy
  {
    if (dist(this.xPos, 627, enemy.xPos, enemy.yPos) < 0.8*(this.size/2 + enemy.size/2))
    {
      return true;
    }
    else
    {
      return false;
    }
  }
  
  boolean bulletCollision(Bullet b) // checks for collision with a bullet from a shooter type enemy
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
  
  boolean tokenCollision(Token t) // checks for collision with a token
  {
    if (dist(this.xPos, 627, t.xPos, t.yPos) < 0.8*(this.size/2 + t.size/2))
    {
      return true;
    }
    else
    {
      return false;
    }
  }
  
  void eatGrunt(Grunt g)
  {
    size += 0.03*g.size; // if grunt is caught, player grows relative to size of grunt
    eatEnemySound.play();
  }
  
  void eatChaotic()
  {
    maxSpeed += 0.5; // increaes speed of the player
    eatEnemySound.play();
  }
  
  void eatToken(Token t)
  {
    t.captured = true;
    tokenSound.play();
    if (shield < 5) // increases shield but maxes out at five layers
      shield++;
  }  
  
  void takeDamage()
  {
    lives--;
    damage.play();
  }
  
  void loseShield()
  {
    shield--;
  }
}
