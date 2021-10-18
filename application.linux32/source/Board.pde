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

  void init() {
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

  void setupBombs() {
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

  void draw() {
    for (int row = 0; row < board.length; row++) {
      for (int col = 0; col < board[row].length; col++) {
        Cell cell = board[row][col];
        cell.draw();
      }
    }
  }

  void checkCellNums() {
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
