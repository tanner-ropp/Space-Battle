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
  
  void display()
  {
    // Will display a small yellow "photon" looking pixel bullet at the specificed locaton and of the specified size
    rectMode(CENTER);
    fill(0,255,200);
    noStroke();
    rect(xPos,yPos, 5, 8);
  }
  
  void move()
  {
    // will move the bullet down the window at the specified speed
    yPos += speed;
  }
}
