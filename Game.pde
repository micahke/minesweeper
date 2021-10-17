public class Game {
  
  Board board;
  boolean gameStart = false;
  
  public Game(int rows, int columns, int bombs) {
    board = new Board(rows, columns, bombs);
    board.init();
  }
  
  void draw() {
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
