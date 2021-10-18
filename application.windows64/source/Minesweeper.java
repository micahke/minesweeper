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
public void setup() {
  
  frameRate(30);
  game = new Game(10, 10, 40);
}

public void update() {
  
}

public void draw() {
  update();
  background(204);
  game.draw();
}

public void mouseClicked() {
  game.handleMouseInput();
}
public class Board {

  // 10 = covered
  // 11 = bomb
  // 12 = marked
  Cell[][] board;
  int rows;
  int columns;
  int bombs;

  public Board(int rows, int columns, int bombs) {
    board = new Cell[rows][columns];
    this.rows = rows;
    this.columns = columns;
    this.bombs = bombs;
  }

  public void init() {
    float width = 600;
    float height = 650;
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
    int numBombs = this.bombs;
    while (numBombs > 0) {
      int randRow = (int)(Math.random() * board.length);
      int randCol = (int)(Math.random() * board[0].length);
      Cell cell = board[randRow][randCol];
      if (!cell.isBomb) {
        cell.isBomb(true);
        numBombs--;
        System.out.println("added bomb");
      }
    }
    
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
  
  public ArrayList <Cell> getAdjacentEmpties(Cell cell) {
    ArrayList<Cell> adjacentCells = new ArrayList<Cell>();
    for (int i = cell.getRow() - 1; i <= cell.getRow() + 1; i++) {
      for (int j = cell.getCol() - 1; j <= cell.getCol() + 1; j++) {
        try {
          Cell neighborCell = board[i][j];
          if (neighborCell.getNumBombs() == 0 && neighborCell.status != Status.MARKED) {
            if (neighborCell.status == Status.COVERED) {
              adjacentCells.add(neighborCell);
            }
          }
        } catch (ArrayIndexOutOfBoundsException e) {
          // error
        }
      }
    }
    return adjacentCells;
  }
  
  public void uncoverCells(Cell cell) {
    cell.uncover();
    ArrayList<Cell> empties = getAdjacentEmpties(cell);
    if (empties.size() >= 0) {
      return;
    } else {
      for (Cell c : empties) {
        uncoverCells(c);
      }
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
    textAlign(CENTER);
    if (numBombs > 0)
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
    status = Status.MARKED;
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
  
  public Game(int rows, int columns, int bombs) {
    board = new Board(rows, columns, bombs);
    board.init();
  }
  
  public void draw() {
    board.draw();
  }
  
  public Board board() {
    return board;
  }
  
  
  public void handleMouseInput() {
    if (board.getClickedCell() != null) {
      Cell cell = board.getClickedCell();
      if (mouseButton == RIGHT) {
        cell.mark();
      } else if (mouseButton == LEFT) {
        if (cell.isBomb) {
          System.out.println("DEAD");
          cell.uncover();
        } else {
          if (gameStart != true) {
            cell.uncover();
            gameStart = true;
             board.setupBombs();
             board.checkCellNums();
           } else {
             board.uncoverCells(cell);
           }
        }
      }
    }
  }
}
public enum Status {
  COVERED,
  UNCOVERED,
  MARKED
}
  public void settings() {  size(600, 650); }
  static public void main(String[] passedArgs) {
    String[] appletArgs = new String[] { "Minesweeper" };
    if (passedArgs != null) {
      PApplet.main(concat(appletArgs, passedArgs));
    } else {
      PApplet.main(appletArgs);
    }
  }
}
