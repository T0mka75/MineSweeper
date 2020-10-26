package sweeper;

public class Game {
  private Bomb bomb;
  private Flag flag;

  private GameState state;

  public GameState getState() {
    return state;
  }

  public Game(int cols, int rows, int bombs) {
    Ranges.setSize(new Coord(cols, rows));
    bomb = new Bomb(bombs);
    flag = new Flag();
  }

  public void start() {
    bomb.start();
    flag.start();
    state = GameState.PLAYED;
  }

  public Box getBox(Coord coord) {
    if (flag.get(coord) == Box.OPENED) return bomb.get(coord);
    else return flag.get(coord);
  }

  private void checkWinner() {
    if (state == GameState.PLAYED)
      if (flag.getCountOfClosedBoxes() == bomb.getTotalBombs()) {
        state = GameState.WINNER;
      }
  }

  public void pressLeftButton(Coord coord) {
    if (gameOver()) return;
    openBox(coord);
    checkWinner();
  }

  public void pressRightButton(Coord coord) {
    if (gameOver()) return;
    flag.toggleFlaggedToBox(coord);
  }

  private void openBox(Coord coord) {
    switch (flag.get(coord)) {
      case OPENED:
        setOpenedToClosedBoxesAroundNumber(coord);
        return;
      case FLAGGED:
        return;
      case CLOSED:
        switch (bomb.get(coord)) {
          case ZERO:
            openBoxesAround(coord);//Jeigu paspaudzia i tuscia langeli
            return;
          case BOMB://i bomba
            openBombs(coord);
            return;
          default://i skaiciu
            flag.setOpenedToBox(coord);
            return;
        }
    }
  }

  private void openBoxesAround(Coord coord) {
    flag.setOpenedToBox(coord);
    for (Coord around : Ranges.getCoordsAround(coord)) openBox(around);
  }

  private void openBombs(Coord bombed) {
    state = GameState.BOMBED;
    flag.setBombedToBox(bombed);//Pazymim bomba raudona
    for (Coord coord : Ranges.getAllCoords())
      if (bomb.get(coord) == Box.BOMB) flag.setOpenedToClosedBombBox(coord);//Parodau visas bombas
      else flag.setNobombToFlaggedSafeBox(coord);//Parodau klaidingas veliaveles
  }

  private boolean gameOver() {
    if (state == GameState.PLAYED) return false;
    start();
    return true;
  }
//Kad leistu atidaryt lengelius paspaudzius ant skaiciu, jeigu yra reik.sk. veliaveliu
  void setOpenedToClosedBoxesAroundNumber(Coord coord) {
    if (bomb.get(coord) != Box.BOMB)
      if (flag.getCountOfFlaggedBoxesAround(coord) == bomb.get(coord).getNumber())
        for (Coord around : Ranges.getCoordsAround(coord))
          if (flag.get(around) == Box.CLOSED) openBox(around);
  }
}
