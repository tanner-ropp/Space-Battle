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
    speed = 0.3;
    skin = galaxyIm;
  }
  
  void display()
  {
    // displays the galaxy image at thr specified location and at the specified size
    image(skin, xPos, yPos, size, size);
  }
  
  void move()
  {
    // advances the galaxy down the screen at the specified uniform speed
    yPos += speed;
    if (yPos > height + 100)
    {
      loop(random(0, width), random(80, 150)); // galaxy is recycled immediately without a pause from a timer
    }
  }
  
  void loop(float _xPos, float _size)
  {
    // resets the galaxy object back above the top of the window with a new position and size, providing and endless stream of galaxy objects over time
    xPos = _xPos;
    size = _size;
    yPos = -300;
  }
}
