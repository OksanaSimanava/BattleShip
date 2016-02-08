import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collections;

import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

public class Field extends JPanel {

    /**
     *
     */
    private static final long serialVersionUID = 1L;
    ArrayList<MyButton> player1Boxes;
    ArrayList<MyButton> player2Boxes;
    JPanel player1Field;
    JPanel player2Field;
    MainWindow main;

    public Field(MainWindow main) {
        this.main = main;
    }

    public int sizeShip(int size) {
        int numb = 0;
        switch (size) {
            case 10:
                numb = 4;
                break;
            case 9:
            case 8:
                numb = 3;
                break;
            case 7:
            case 6:
            case 5:
                numb = 2;
                break;
            case 4:
            case 3:
            case 2:
            case 1:
                numb = 1;
                break;
        }

        return numb;
    }

    public void createField() {
        player1Boxes = new ArrayList<>();
        player2Boxes = new ArrayList<>();

        this.setBorder(new EmptyBorder(5, 5, 5, 5));
        this.setLayout(new GridLayout(1,2));//,0,5));
        player1Field = new JPanel();
        player1Field.setLayout(new GridLayout(10,10));

        player2Field = new JPanel();
        player2Field.setLayout(new GridLayout(10,10));

        buildShips(player1Field, player1Boxes, Color.orange, false);
        buildShips(player2Field, player2Boxes, null, true);

        this.add(player1Field);
        this.add(player2Field);

        this.revalidate();
        this.setVisible(true);
    }

    private void buildShips (JPanel playerField, ArrayList<MyButton> playerBoxes, Color playerColor, boolean live) {
        ArrayList<Ship> playerShips;
      	do{
    		playerShips = generateShips();
    	}while(playerShips.size() != 10);
    	
        ArrayList<Coord> playerShipsCoords = new ArrayList<>();
        final int fieldSize = 10;
        Ship playerShip = null;
        for (int i = 0; i < playerShips.size(); i++) {
        	playerShip = playerShips.get(i);
           // if (playerShip == null) {
           //     System.err.println("Ship wasn't generated");
           //     continue;
           // }
            for (int z = 0; z < playerShip.getSize(); z++) {
                int xShip = playerShip.getArrElement().get(z).getX();
                int yShip = playerShip.getArrElement().get(z).getY();
                playerShipsCoords.add(new Coord(xShip, yShip));
            }
        }

        for (int x = 1; x <= fieldSize; x++) {
            for (int y = 1; y <= fieldSize; y++) {
                MyButton box = new MyButton(x, y, 0 );
                if (playerShipsCoords.contains(new Coord(x, y))) {
                    box.setBackground(playerColor);
                    box.setStatus(1);
                }
                if (live){
                	box.addActionListener(new ActionListener() {
              			public void actionPerformed(ActionEvent arg0) {
            				goGame(box);
            			}
            		}); 
                }
                
                playerBoxes.add(box);
                playerField.add(box);
            }
        }
    }

    public void goGame(MyButton btn) {
        Game game = new Game(player2Boxes, player1Boxes, main);
        game.goPlayer(btn);
    }

    public ArrayList<Ship> generateShips() {// ship placement

        ArrayList<Ship> arr4ship = new ArrayList<>();// 4 ships in 4 directions
        ArrayList<Coord> arrElement;// each ship elements
        ArrayList<Ship> arrFinal = new ArrayList<>();
        BlackList bL = new BlackList();
        int size0;
        Ship ship;
        int amountShips = 10;
        
        for (int z = amountShips; z >= 1; z--) {
            size0 = sizeShip(z);
            int x0;
            int y0;
            do {
                x0 = (int) (Math.random() * 10 + 1);
                y0 = (int) (Math.random() * 10 + 1);
            } while (bL.containBusyCoord(x0, y0));

            int x = x0;
            int y = y0;
            arrElement = new ArrayList<>();
            for (int i = 0; i < size0; i++) {
                arrElement.add(new Coord(x, y, 1));
                x = x + 1;
            }
            ship = new Ship(1, size0, arrElement);
            arr4ship.add(ship);

            x = x0;
            y = y0;
            arrElement = new ArrayList<>();
            for (int i = 0; i < size0; i++) {
                arrElement.add(new Coord(x, y, 1));
                x = x - 1;
            }
            ship = new Ship(1, size0, arrElement);
            arr4ship.add(ship);

            x = x0;
            y = y0;
            arrElement = new ArrayList<>();
            for (int i = 0; i < size0; i++) {
                arrElement.add(new Coord(x, y, 1));
                y = y + 1;
            }
            ship = new Ship(1, size0, arrElement);
            arr4ship.add(ship);

            x = x0;
            y = y0;
            arrElement = new ArrayList<>();
            for (int i = 0; i < size0; i++) {
                arrElement.add(new Coord(x, y, 1));
                y = y - 1;
            }
            ship = new Ship(1, size0, arrElement);
            arr4ship.add(ship);
           // Ship shipAdd = chooseDirection(arr4ship, bL);
            //if(shipAdd != null){
            arrFinal.add(chooseDirection(arr4ship, bL));
            //}//else{
            //	System.out.println("1111");
            //	generateShips();
           // }
            arr4ship.clear();
        }
        return arrFinal;
    }

    public Ship chooseDirection(ArrayList<Ship> arr4, BlackList bL) {
        ArrayList<Ship> arrShipTrue = new ArrayList<>();
        Ship shipRet = null;
        boolean b = true;
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < arr4.get(i).getArrElement().size(); j++) {
                int x = arr4.get(i).getArrElement().get(j).getX();
                int y = arr4.get(i).getArrElement().get(j).getY();
                b = x <= 10 && x >= 1 && y <= 10 && y >= 1
                        && !bL.containBusyCoord(x, y);
            }

            if (b) {
                arrShipTrue.add(arr4.get(i));
            }
        }
       
        if (arrShipTrue.size() != 0) {
            Collections.shuffle(arrShipTrue);
            shipRet = arrShipTrue.get(0);
            for (int j = 0; j < shipRet.getSize(); j++) {
                black(bL, shipRet.getArrElement().get(j).getX(), shipRet
                        .getArrElement().get(j).getY());
            }
        } //else {
        //	System.out.println("1111");
         //   generateShips();
        //}
        return shipRet;
    }

    public void black(BlackList bL, int x, int y) {
        bL.addCoords(x, y);
        bL.addCoords(x + 1, y);
        bL.addCoords(x - 1, y);
        bL.addCoords(x, y + 1);
        bL.addCoords(x, y - 1);
        bL.addCoords(x + 1, y + 1);
        bL.addCoords(x + 1, y - 1);
        bL.addCoords(x - 1, y + 1);
        bL.addCoords(x - 1, y - 1);
    }

}
