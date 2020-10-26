package sweeper;

public class Flag {
  private Matrix flagMap;
  private int countOfClosedBoxes;

  void start() {
    flagMap = new Matrix(Box.CLOSED);
    countOfClosedBoxes = Ranges.getSize().x * Ranges.getSize().y;
  }

  Box get(Coord coord) {
    return flagMap.get(coord);
  }

  void setOpenedToBox(Coord coord) {
    flagMap.set(coord, Box.OPENED);
    countOfClosedBoxes--;
  }

  private void setFlaggedToBox(Coord coord) {
    flagMap.set(coord, Box.FLAGGED);
  }

  void toggleFlaggedToBox(Coord coord) {
    switch (flagMap.get(coord)) {
      case FLAGGED:
        setCLosedToBox(coord);
        break;
      case CLOSED:
        setFlaggedToBox(coord);
        break;
    }
  }

  void setCLosedToBox(Coord coord) {
    flagMap.set(coord, Box.CLOSED);
  }

  void setBombedToBox(Coord coord) {
    flagMap.set(coord, Box.BOMBED);
  }

  void setNobombToFlaggedSafeBox(Coord coord) {
    if (flagMap.get(coord) == Box.FLAGGED) flagMap.set(coord, Box.NOBOMB);//Nera bombos
  }

  void setOpenedToClosedBombBox(Coord coord) {
    if (flagMap.get(coord) == Box.CLOSED) flagMap.set(coord, Box.OPENED);
  }

  int getCountOfClosedBoxes() {
    return countOfClosedBoxes;
  }

  int getCountOfFlaggedBoxesAround(Coord coord) {
    int count = 0;
    for (Coord around : Ranges.getCoordsAround(coord))
      if (flagMap.get(around) == Box.FLAGGED)
        count++;
    return count;
  }
}
