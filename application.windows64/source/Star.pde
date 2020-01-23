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
    speed = 0.3;
    skin = im;
  }
  
  void display()
  {
    // displays the star image at thr specified location and at the specified size
    image(skin, xPos, yPos, size, size);
  }
  
  void move()
  {
    // advances the star down the screen at the specified uniform speed
    yPos += speed;
    if (yPos > height + 20)
    {
      loop(random(0, width), random(10, 30)); // imediately recycles the star object without any timer or break
    }
  }
  
  void loop(float _xPos, float _size)
  {
    // resets the star object back at the top of the screen with a new position and size, providing and endless stream of star objects
    xPos = _xPos;
    size = _size;
    yPos = -20;
  }
}
