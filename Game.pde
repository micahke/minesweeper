public class Game {
  
  Board board;
  
  public Game(int rows, int columns, int bombs) {
    board = new Board(rows, columns, bombs);
    board.init();
    board.setupBombs();
    board.checkCellNums();
  }
  
  void draw() {
    board.draw();
  }
  
  public Board board() {
    return board;
  }
}
