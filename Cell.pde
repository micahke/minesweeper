public class Cell {
 
  Status status;
  float x;
  float y;
  float cellSize;
  int numBombs;
  
  
  
  public Cell(float x, float y, float cellSize) {
    this.x = x;
    this.y = y;
    this.status = Status.EMPTY_COVERED;
  }
  
  public void setNumber(int numBombs) {
    this.numBombs = numBombs;
  }
  
  public void setStatus(Status status) {
    this.status = status;
  }
  
  public void draw() {
    rect(x, y,cellSize, cellSize);
  }
  
  
}
