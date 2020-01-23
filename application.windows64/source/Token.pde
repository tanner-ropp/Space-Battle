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
  
  void display()
  {
    // displays the token image at the specified location and at the specified size
    image(skin, xPos, yPos, size, size);
  }
  
  void move()
  {
    // advances the token down the screen at the specified uniform speed
    // sets hasPasses = true; if the star passes the bottom of the window
    yPos += speed;
    if (yPos > height + 50)
    {
      captured = true;
    }
  }
  
  void loop(float _xPos)
  {
    // resets the token object back at the top of the screen with a new position, providing and endless stream of token objects
    yPos = -2000;
    xPos = _xPos;
    captured = false;
  }
}
