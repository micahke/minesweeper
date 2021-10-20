Game game;
int bombs = 400;
int status = 0;

void setup() {
  size(700, 750);
  frameRate(30);
}


void draw() {
  background(204);
  if (status == 0) {
    drawIntro();
  } else {
    game.draw();
  }
}

void drawIntro() {
    textSize(40);
    fill(0, 0, 0);
    text("Welcome to Minesweeper!", 55, 200);
    textSize(18);
    text("Select a grid size below: ", 55, 230);
    text("[0] : DEFAULT MODE", 55, 260);
    text("[1] : 10 x 10", 55, 290);
    text("[2] : 20 x 20", 55, 320);
    text("[3] : 30 x 30", 55, 350);
    text("[4] : 40 x 40", 55, 380);
    text("[5] : 50 x 50", 55, 410);
}

void mousePressed() {
  //System.out.println("clicked");
  if (status > 0)
    game.handleMouseInput();
}

void keyReleased() {
  if (status == 0) {
    if (key == '1') {
      game = new Game(10, 10, 20);
      status = 1;
    } else if (key == '2') {
      game = new Game(20, 20, 80);
      status = 1;
    } else if (key == '3') {
      game = new Game(30, 30, 150);
      status = 1;
    } else if (key == '4') {
      game = new Game(40, 40, 300);
      status = 1;
    } else if (key == '5') {
      game = new Game(50, 50, 500);
      status = 1;
    } else if (key == '0') {
      game = new Game(16, 16, 40);
      status = 1;
    }
  }
}
