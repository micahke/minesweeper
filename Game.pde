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
  
  void draw() {
    int numFlags = game.checkFlags();
    if (status == 0) {
      text("Bombs left: " + (bombs - numFlags), 300, 30);
    } else if (status == 1) {
      fill(255, 0, 0);
      text("BOMB! GAME OVER!!!", 300, 30);
    } else {
      fill(0, 255, 0);
      text("GAME WON!!", 300, 30);
    }
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
