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
