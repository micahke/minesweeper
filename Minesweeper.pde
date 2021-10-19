Game game;
int bombs = 40;

void setup() {
  size(600, 650);
  frameRate(30);
  game = new Game(20, 20, bombs);
}


void draw() {
  background(204);
  game.draw();
}

void mousePressed() {
  //System.out.println("clicked");
  game.handleMouseInput();
}
