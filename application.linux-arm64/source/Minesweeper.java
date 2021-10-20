import processing.core.*; 
import processing.data.*; 
import processing.event.*; 
import processing.opengl.*; 

import java.util.HashMap; 
import java.util.ArrayList; 
import java.io.File; 
import java.io.BufferedReader; 
import java.io.PrintWriter; 
import java.io.InputStream; 
import java.io.OutputStream; 
import java.io.IOException; 

public class Minesweeper extends PApplet {

Game game;
int bombs = 400;
int status = 0;

public void setup() {
  
  frameRate(30);
}


public void draw() {
  background(204);
  if (status == 0) {
    drawIntro();
  } else {
    game.draw();
  }
}

public void drawIntro() {
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

public void mousePressed() {
  //System.out.println("clicked");
  if (status > 0)
    game.handleMouseInput();
}

public void keyReleased() {
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
public class Board {

  // 10 = covered
  // 11 = bomb
  // 12 = marked
  Cell[][] board;
  int rows;
  int columns;
  int bombs;
  int startX;
  int startY;

  public Board(int rows, int columns, int bombs) {
    board = new Cell[rows][columns];
    this.rows = rows;
    this.columns = columns;
    this.bombs = bombs;
  }

  public void init() {
    float width = 700;
    float height = 750;
    float cellSize = (float)(width / columns);
    float y = (float)(height - (cellSize * rows));
    for (int row = 0; row < board.length; row++) {
      float x = 0;
      for (int col = 0; col < board[row].length; col++) {
        board[row][col] = new Cell(x, y, cellSize, row, col);
        x += cellSize;
      }
      y += cellSize;
    }
  }

  public void setupBombs() {
    System.out.println("Placing bombs");
    int numBombs = this.bombs;
    while (numBombs > 0) {
      int randRow = (int)(Math.random() * board.length);
      int randCol = (int)(Math.random() * board[0].length);
      //System.out.println("Starting at " + startX + ", " + startY);
      Cell cell = board[randRow][randCol];
      if (!cell.isReserved() && !cell.isBomb) {
        cell.isBomb(true);
        numBombs--;
      }
    }
    System.out.println("Finished placing bombs");
  }

  public void draw() {
    for (int row = 0; row < board.length; row++) {
      for (int col = 0; col < board[row].length; col++) {
        Cell cell = board[row][col];
        cell.draw();
      }
    }
  }

  public void checkCellNums() {
    for (int row = 0; row < board.length; row++) {
      for (int col = 0; col < board[row].length; col++) {
        Cell cell = board[row][col];
        if (!cell.isBomb) {
          int numBombs = 0;
          for (int i = row - 1; i <= row + 1; i++) {
            for (int j = col - 1; j <= col + 1; j++) {
              try {
                Cell neighborCell = board[i][j];
                if (neighborCell.isBomb) {
                  numBombs++;
                }
              } catch (ArrayIndexOutOfBoundsException e) {
                // error
              }
            }
          }
          if (numBombs > 0)
            cell.setNumBombs(numBombs);
        }
      }
    }
  }
  
  public void setStart(Cell cell) {
    this.startX = cell.getRow();
    this.startY = cell.getCol();
    cell.uncover();
    setReservedCells(cell);
    
  }
  
  public void setReservedCells(Cell cell) {
    System.out.println("reserving cells");
    for (int i = cell.getRow() - 1; i <= cell.getRow() + 1; i++) {
      for (int j = cell.getCol() - 1; j <= cell.getCol() + 1; j++) {
        try {
          board[i][j].setReserved(true);
        } catch (ArrayIndexOutOfBoundsException e) {
          // if checking a cell off the board
        }
      }
    }
  }
  
  public ArrayList <Cell> getAdjacentEmpties(Cell cell) {
    ArrayList<Cell> adjacentCells = new ArrayList<Cell>();
    for (int i = cell.getRow() - 1; i <= cell.getRow() + 1; i++) {
      for (int j = cell.getCol() - 1; j <= cell.getCol() + 1; j++) {
        try {
          Cell neighborCell = board[i][j];
          if ((neighborCell.numBombs >= 0 && !neighborCell.isBomb) && neighborCell.status == Status.COVERED) {
            adjacentCells.add(neighborCell);
          }
        } catch (ArrayIndexOutOfBoundsException e) {
          // error
        }
      }
    }
    return adjacentCells;
  }
  
  public void uncoverCells(Cell cell) {
    ArrayList<Cell> cells = getAdjacentEmpties(cell);
    cell.uncover();
    if (cell.numBombs > 0) {
      return;
    }
    for (Cell c : cells) {
      uncoverCells(c);
    }
  }
  
  public Cell getClickedCell() {
    for (int row = 0; row < board.length; row++) {
      for (int col = 0; col < board[row].length; col++) {
        Cell cell = board[row][col];
        if (mouseX > cell.getX() && mouseX < cell.getX() + cell.getSize()) {
          if (mouseY > cell.getY() && mouseY < cell.getY() + cell.getSize()) {
            return cell;
          }
        }
      }
    }
    return null;
  }
  
  public Cell[][] getBoard() {
    return board;
  }
  
}
public class Cell {
 
  Status status;
  float x;
  float y;
  float cellSize;
  int row;
  int col;
  int numBombs;
  boolean isBomb;
  boolean reserved = false;
  
  float circleSize;
  
  
  public Cell(float x, float y, float cellSize, int row, int col) {
    this.x = x;
    this.y = y;
    this.cellSize = cellSize;
    this.status = Status.COVERED;
    this.row = row;
    this.col = col;
    this.isBomb = false;
    this.circleSize = cellSize / 2;
  }
  
  public void setNumBombs(int numBombs) {
    this.numBombs = numBombs;
  }
  
  public void setReserved(boolean reserved) {
    this.reserved = reserved;
  }
  
  public boolean isReserved() {
    return this.reserved;
  }
  
  public void draw() {
    // RECTANGLE
    // SET COLORS
    fill(255, 255, 255);
    if (status == Status.UNCOVERED)
      fill(128, 128, 128);
      
    // DRAW RECT
    rect(x, y,cellSize, cellSize);
    
    // DRAW OBJECTS ON TOP
    if (status == Status.UNCOVERED) { 
      if (isBomb) {
        fill(0, 0, 0);
        float cX = x + (circleSize);
        float cY = y + (circleSize);
        ellipse(cX, cY, circleSize, circleSize);
        //ellipse(x, y, 10, 10);
      }
    }
    if (status == Status.MARKED) {
      fill(255, 0, 0);
      rect(x + (cellSize / 4), y + (cellSize / 4), cellSize / 2, cellSize / 2);
    }
    
    // NUMBER LABEL
    fill(0, 0, 0);
    float textX = x + (cellSize / 2);
    float textY = y + (cellSize / 2) + 3;
    textSize(cellSize / 2);
    textAlign(CENTER);
    if (numBombs > 0 && status == Status.UNCOVERED)
      text("" + numBombs, textX, textY);
  }
  
  public void isBomb(boolean isBomb) {
    this.isBomb = isBomb;
  }
  
  public float getX(){
    return this.x;
  }
  
  public int getNumBombs() {
    return this.numBombs;
  }
  
  public float getY(){
    return this.y;
  }
  
  public float getSize(){
    return this.cellSize;
  }
  
  public void mark() {
    if (status == Status.MARKED) {
      status = Status.COVERED;
    } else {
      
      status = Status.MARKED;
    }
  }
  
  public void setStatus(Status status) {
    this.status = status;
  }
  
  public void uncover() {
    this.status = Status.UNCOVERED;
  }
  
  public int getRow() {
    return row;
  }
    
  
  public int getCol() {
    return col;
  }
  
}
public class Game {
  
  Board board;
  boolean gameStart = false;
  int bombs;
  int status = 0;
  
  public Game(int rows, int columns, int bombs) {
    board = new Board(rows, columns, bombs);
    this.bombs = bombs;
    board.init();
  }
  
  public void draw() {
    int numFlags = game.checkFlags();
    textSize(20);
    if (status == 0) {
      text("Bombs left: " + (bombs - numFlags), 325, 30);
    } else if (status == 1) {
      fill(255, 0, 0);
      text("BOMB! GAME OVER!!!", 325, 30);
    } else {
      fill(0, 255, 0);
      text("GAME WON!!", 325, 30);
    }
    textAlign(CENTER);
    text("developed by Micah Elias", 350, 775);
    if (bombs - numFlags == 0) {
      if (checkWin()) {
        status = 2;
      }
    }
    board.draw();
  }
  
  public boolean checkWin() {
    Cell[][] board = this.board.getBoard();
    for (int row = 0; row < board.length; row++) {
      for (int col = 0; col < board[0].length; col++) {
        if (board[row][col].status == Status.MARKED) {
          if (!board[row][col].isBomb) {
            return false;
          }
        }
      }
    }
    return true;
  }
  
  public Board board() {
    return board;
  }
  
  
  public void handleMouseInput() {
    if (status == 0) {
      Cell cell = board.getClickedCell();
      if (cell != null) {
        if (mouseButton == RIGHT) {
          cell.mark();
        } else if (mouseButton == LEFT) {
          cell.uncover();
          redraw();
          if (!gameStart) {
            System.out.println("Starting game");
            gameStart = true;
            board.setStart(cell);
            board.setupBombs();
            board.checkCellNums();
            board.uncoverCells(cell);
          } else if (cell.numBombs == 0 && !cell.isBomb) {
            board.uncoverCells(cell);
          }
          if (cell.isBomb) {
            status = 1;
          }
        }
      }
    }
  }
  
  public int checkFlags() {
    int total = 0;
    Cell[][] board = this.board.getBoard();
    for (int row = 0; row < board.length; row++) {
      for (int col = 0; col < board[0].length; col++) {
        if (board[row][col].status == Status.MARKED) {
          total++;
        }
      }
    }
    return total;
  }
}
public enum Status {
  COVERED,
  UNCOVERED,
  MARKED
}
  public void settings() {  size(700, 750); }
  static public void main(String[] passedArgs) {
    String[] appletArgs = new String[] { "Minesweeper" };
    if (passedArgs != null) {
      PApplet.main(concat(appletArgs, passedArgs));
    } else {
      PApplet.main(appletArgs);
    }
  }
}
