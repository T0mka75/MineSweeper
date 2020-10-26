import javax.swing.*;
import javax.swing.JPanel;
import javax.swing.JFrame;
import java.awt.Graphics;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import sweeper.Box;
import sweeper.Coord;
import sweeper.Game;
import sweeper.Ranges;

public class MineSweeper extends JFrame implements ActionListener {
  private Game game;
  private JPanel panel;
  private JLabel label,label1;
  private JButton button;
  private JRadioButton radio1, radio2, radio3;

  private int cols = 10;
  private int rows = 10;
  private int bombs = 20;
  private final int image_size = 50;

  public static void main(String[] args) {
    new MineSweeper();
  }

  public MineSweeper() {
    initMeniu();
  }

  public void initMeniu() {
    // ScreenSize
    Dimension screensize = Toolkit.getDefaultToolkit().getScreenSize();
    double value = screensize.getWidth();
    int ScreenWidth = (int) value;
    double ScreenHeight = screensize.getHeight();

    // RadioButton
    radio1 = new JRadioButton("Easy");
    radio2 = new JRadioButton("Medium");
    radio3 = new JRadioButton("Expert");
    ButtonGroup group = new ButtonGroup();
    group.add(radio1);
    group.add(radio2);
    group.add(radio3);
    radio1.setBounds(10, 100, 100, 50);
    radio2.setBounds(110, 100, 100, 50);
    radio3.setBounds(210, 100, 100, 50);
    radio1.addActionListener(this::actionPerformed);
    radio2.addActionListener(this::actionPerformed);
    radio3.addActionListener(this::actionPerformed);
    radio1.setSelected(true);

    // Button
    button = new JButton("Start");
    button.setBounds(70, 10, 150, 80);
    button.setFocusable(false);
    button.addActionListener(this::actionPerformed);

    // Meniu
    JFrame meniu = new JFrame();
    meniu.setTitle("Meniu");
    meniu.setLayout(null);
    meniu.setVisible(true);
    meniu.setLocation(ScreenWidth / 2 - 150, 50);
    meniu.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    meniu.setSize(300, 180);
    meniu.setResizable(false);
    meniu.add(radio1);
    meniu.add(radio2);
    meniu.add(radio3);
    meniu.add(button);
  }

  public void actionPerformed(ActionEvent e) {
    if (radio1.isSelected()) {
      cols = 10;
      rows = 10;
      bombs = 15;
    }
    if (radio2.isSelected()) {
      cols = 15;
      rows = 15;
      bombs = 40;
    }
    if (radio3.isSelected()) {
      cols = 30;
      rows = 19;
      bombs = 99;
    }
    if (e.getSource() == button) {
      game = new Game(cols, rows, bombs);
      game.start();
      setImages();
      initLabel();
      initPanel();
      initFrame();
    }
  }

  private void initLabel() {
    label = new JLabel("Welcome");
    add(label, BorderLayout.SOUTH);
  }

  public void initPanel() {
    panel =
        new JPanel() {
          @Override
          protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            for (Coord coord : Ranges.getAllCoords()) {
              g.drawImage(
                  (Image) game.getBox(coord).image,
                  coord.x * image_size,
                  coord.y * image_size,
                  this);
            }
          }
        };

    panel.addMouseListener(
        new MouseAdapter() {
          @Override
          public void mousePressed(MouseEvent e) {
            int x = e.getX() / image_size;
            int y = e.getY() / image_size;
            Coord coord = new Coord(x, y);
            if (e.getButton() == MouseEvent.BUTTON1) game.pressLeftButton(coord);
            if (e.getButton() == MouseEvent.BUTTON2) game.start();
            if (e.getButton() == MouseEvent.BUTTON3) game.pressRightButton(coord);
            label.setText(getMessage());
            panel.repaint();
          }
        });

    panel.setPreferredSize(
        new Dimension(Ranges.getSize().x * image_size, Ranges.getSize().y * image_size));
    add(panel);
  }

  private String getMessage() {
    switch (game.getState()) {
      case PLAYED:
        return "Do your best!";
      case BOMBED:
        return "You lost!";
      case WINNER:
        return "Congratulations, you won!!!";
      default:
        return "";
    }
  }
  private void initFrame() {
    setTitle("Mine Sweeper");
    setVisible(true);
    setResizable(false);
    setIconImage(getImage("icon"));
    pack();
    setLocationRelativeTo(null);

  }

  private void setImages() {
    for (Box box : Box.values()) box.image = getImage(box.name().toLowerCase());
  }

  private Image getImage(String name) {
    String filename = "img/" + name + ".png";
    ImageIcon icon = new ImageIcon(getClass().getResource(filename));
    return icon.getImage();
  }
}
