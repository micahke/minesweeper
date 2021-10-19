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
    Cell cell = board.getClickedCell();
    if (cell != null) {
      if (mouseButton == RIGHT) {
        cell.mark();
      } else if (mouseButton == LEFT) {
        if (!gameStart) {
          System.out.println("Starting game");
          gameStart = true;
          board.setStart(cell);
          board.setupBombs();
          board.checkCellNums();
          board.uncoverCells(cell);
        } else if (cell.numBombs == 0) {
          board.uncoverCells(cell);
        }
        cell.uncover();
      }
    }
  }
}
