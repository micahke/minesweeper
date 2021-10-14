public class Board {
  
  // 10 = covered
  // 11 = bomb
  // 12 = marked
  int[][] board;
  int rows;
  int columns;
  int bombs;
  
  public Board(int rows, int columns, int bombs) {
    board = new int[rows][columns];
    this.rows = rows;
    this.columns = columns;
    this.bombs = bombs;
  }
  
  void init() {
    for (int row = 0; row < board.length; row++) {
      for (int col = 0; col < board[row].length; col++) {
        board[row][col] = 10;
      }
    }
  }
  
  void setupBombs() {
    int numBombs = this.bombs;
    boolean isDone = false;
    while (!isDone) {
      for (int row = 0; row < board.length; row++) {
        for (int col = 0; col < board[row].length; col++) {
          if (numBombs > 0) {
            double rand = Math.random();
            double odds = (double)1/100;
            //System.out.println(rand + " / " + odds);
            if (rand < odds) {
              board[row][col] = 11;
              System.out.println("added bomb at " + row + ", " + col);
              numBombs -= 1;
            }
          } else {
            isDone = true;
          }
        }
      }
    }
  }
  
  void draw() {
    float width = 600;
    float height = 650;
    float cellSize = (float)(width / columns);
    float y = (float)(height - (cellSize * rows));
    for (int row = 0; row < board.length; row++) {
      float x = 0;
      for (int col = 0; col < board[row].length; col++) {
        if (board[row][col] == 11) {
          fill(255, 15, 15);
        } else {
          fill(255, 255, 255);
        }
        rect(x, y, cellSize, cellSize);
        if (board[row][col] < 10) {
          fill(0, 0, 0);
          float textX = x + (cellSize / 2);
          float textY = y + (cellSize / 2) + 3;
          textAlign(CENTER);
          text("" + board[row][col], textX, textY);
        }
        x += cellSize;
      }
      y += cellSize;
    }
  }


  void checkCellNums() {
    for (int row = 0; row < board.length; row++) {
      for (int col = 0; col < board[row].length; col++) {
        if (board[row][col] != 11){
          int numBombs = 0;
          for (int i = row - 1; i <= row + 1; i++) {
            for (int j = col - 1; j <= col + 1; j++) {
              try {
                if (board[i][j] == 11) {
                  numBombs++;
                }
              } catch (ArrayIndexOutOfBoundsException e) {
                // error
              }
            }
          }
          if (numBombs > 0)
            board[row][col] = numBombs;
        }
      }
    }
  }
  
  
  
  
  
  
}
