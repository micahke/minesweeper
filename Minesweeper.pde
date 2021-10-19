Game game;
void setup() {
  size(600, 650);
  frameRate(30);
  game = new Game(16, 30, 99);
}

void update() {
  
}

void draw() {
  update();
  background(204);
  game.draw();
}

void mouseClicked() {
  game.handleMouseInput();
}
