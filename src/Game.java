import java.awt.Color;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

import javax.swing.JOptionPane;

public class Game{
	ArrayList<MyButton> arrBtnBot = new ArrayList<MyButton>();
	ArrayList<MyButton> arrBtnPlayer = new ArrayList<MyButton>();
	private int id;
	MainWindow main;


	public Game(ArrayList<MyButton> arrBtnPlayer, ArrayList<MyButton> arrBtnBot, MainWindow main) {
		this.arrBtnPlayer = arrBtnPlayer;
		this.arrBtnBot = arrBtnBot;
		this.main = main;
	}


	public void goPlayer(MyButton btn) {
		//btn.setEnabled(false);
		if (btn.getStatus() == 1) {// попадание
			btn.setStatus(2);
			btn.setBackground(Color.black);
			btn.setEnabled(false);
			setDiagonalNeighboursDisable(arrBtnPlayer, btn);
			setNeighboursDisable(arrBtnPlayer, btn);
			ifEnd();
		}
		if (btn.getStatus() == 0) {
			changeNeighbourButtons(btn);
			goBot();
		}
	}

	public void goBot() {
		Collections.shuffle(arrBtnBot);
		
		playBot();
	}

	public void playBot() {
		MyButton botButton = null;

		// проверить, содержатся ли в массиве кнопки со статусов 3 - значит было
		// попадание, надо продолжать		
		for (int i = 0; i < arrBtnBot.size(); i++) {
			if (arrBtnBot.get(i).getStatus() == 3) {
				botButton = arrBtnBot.get(i);
				furtherBotSteps(botButton, arrBtnBot);
			}
		}
		
		Random r = new Random();
		int getNumber = r.nextInt(arrBtnBot.size());
		if (arrBtnBot.get(getNumber).getStatus() == 2) {
			for (int i = 0; i < arrBtnBot.size(); i++) {
				if(arrBtnBot.get(i).getStatus() == 0 || arrBtnBot.get(i).getStatus() == 1){
					botButton = arrBtnBot.get(i);
					break;
				}
			}
		}else{
			botButton = arrBtnBot.get(0);
		}
		
		if(botButton != null){

			if (botButton.getStatus() == 1) {
	            botButton.setBackground(Color.black);
				botButton.setStatus(2);// убита
				botButton.setEnabled(false);
				setDiagonalNeighboursDisable(arrBtnBot, botButton);
				ifEnd();
				// проверка однопалубник
				boolean oneDecker = true;
				ArrayList<MyButton> neighboursOneDeckerShip = getArrNeighbours(arrBtnBot, botButton);
				for (int i = 0; i < neighboursOneDeckerShip.size(); i++) {
					if (neighboursOneDeckerShip.get(i).getStatus() != 0){
						botButton.setStatus(3);// кнопка для запоминания
						oneDecker = false;
						break;
					}
				}
	            if (oneDecker == true) {// однопалубник
					setNeighboursDisable(arrBtnBot, botButton);
					goBot();
				} else {// многопалубник
					furtherBotSteps(botButton, arrBtnBot);
				}
			}
			if (botButton.getStatus() == 0) {
				//botButton.setEnabled(false);
				changeNeighbourButtons(botButton);
				arrBtnBot.remove(botButton);
			}
		}

	}

	public void furtherBotSteps(MyButton btnFuther, ArrayList<MyButton> arr) {

        ArrayList<MyButton> arrToGo = getArrNeighbours(arr, btnFuther);
		Collections.shuffle(arrToGo);
		MyButton b1 = null;

        if (arrToGo.size() > 0) {

        	// если все соседи = 2, то goBot
			boolean bool = true;
			for (int i = 0; i < arrToGo.size(); i++) {
				if (arrToGo.get(i).getStatus() == 2) {
					bool = false;
				} else {
					bool = true;
					break;
				}
			}
			if (bool == false) {// если все соседи = 2, то goBot
				btnFuther.setStatus(2);
				goBot();
				
			} else {// иначе взять кнопку со статусом == 1 == 0
				if (arrToGo.get(0).getStatus() < 2){
					b1 = arrToGo.get(0);
				}
				else {
					if (arrToGo.size() > 1) {
						b1 = arrToGo.get(1);
					} else {
						goBot();
					}
				}
			}

			if (b1 == null) {
				goBot();
			} else {
				if (b1.getStatus() == 1) {
					b1.setStatus(2);
					b1.setBackground(Color.black);
					b1.setEnabled(false);
					setDiagonalNeighboursDisable(arrBtnBot, b1);
					ifEnd();
					furtherBotSteps(b1, arr);
				}
				if (b1.getStatus() == 0) {
					changeNeighbourButtons(b1);
					arr.remove(b1);
				}
				if (b1.getStatus() == 2) {
					for (int i = 0; i < arrBtnBot.size(); i++) {
						if (arrBtnBot.get(i).getStatus() == 3) {
							furtherBotSteps(btnFuther, arr);
						}
					}

				}
			}
		}
	}

	public void ifEnd() {

		/*if(arrBtnBot.size() == 0){
            result(-1);
			return;
		}
		if(arrBtnPlayer.size() == 0){
			result(1);
            return;
		}*/

		int countBot = 0;
		if (arrBtnBot.size() > 0) {
            for (MyButton b1 : arrBtnBot) {
                if (b1.getStatus() == 1) {
                    countBot++;
                    break;
                }
            }
		}
		if (countBot == 0){
            result(-1);
			//return;
		}

		int countPlayer = 0;
		if (arrBtnPlayer.size() > 0) {
            for (MyButton b1 : arrBtnPlayer) {
                if (b1.getStatus() == 1) {
                    countPlayer++;
                    break;
                }
            }
		}
		if (countPlayer == 0){
            result(1);
           // return;
        }
	}

    public void result(int result){
        if (result == 1) {
            JOptionPane.showMessageDialog(null, "Player Win!", "", JOptionPane.INFORMATION_MESSAGE);
            main.newGameAction(1);
            
        } else {
            JOptionPane.showMessageDialog(null, "Player lose!", "", JOptionPane.INFORMATION_MESSAGE);
            main.newGameAction(-1);
        }
    }

	public void insertResult(int id1, int id0, int result){
		Connection cn;
		Statement st;
		String insert =   "insert into games ("
				+ "user_id_1, user_id_2, result) "
				+ "values(" + id1 + ", " + id0 + ", " + result+ ")";
		try {
			cn = (Connection) DriverManager.getConnection(
					"jdbc:mysql://localhost", "root", "root");
			st = cn.createStatement();
			st.executeUpdate("use BD");
			st.executeUpdate(insert);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public MyButton getNeighboursDiagonal(MyButton btn, int k, int dx) {

		int x = btn.getMyX();
		int y = btn.getMyY();
		switch (k) {
			case 1:
				return (new MyButton(x + dx, y + dx, 0));
			case 2:
				return (new MyButton(x + dx, y - dx, 0));
			case 3:
				return (new MyButton(x - dx, y + dx, 0));
			case 4:
				return (new MyButton(x - dx, y - dx, 0));
		}
		return null;
	}

	public MyButton getNeighbours(MyButton btn, int k, int dx) {

		int x = btn.getMyX();
		int y = btn.getMyY();
		switch (k) {
			case 1:
				return (new MyButton(x + dx, y, 0));
			case 2:
				return (new MyButton(x - dx, y, 0));
			case 3:
				return (new MyButton(x, y + dx, 0));
			case 4:
				return (new MyButton(x, y - dx, 0));
		}
		return null;
	}

	public void setDiagonalNeighboursDisable(ArrayList<MyButton> arr, MyButton b) {
		MyButton btnChange = new MyButton();
		for (int i = 1; i <= 4; i++) {
			btnChange = getNeighboursDiagonal(b, i, 1);
			if (arr.contains(btnChange)) {
				for (int j = 0; j < arr.size(); j++) {
					if (arr.get(j).getMyX() == btnChange.getMyX()
							&& arr.get(j).getMyY() == btnChange.getMyY()) {
						btnChange = arr.get(j);
					}
				}
				btnChange.setBackground(Color.gray);
				btnChange.setEnabled(false);
				arr.remove(btnChange);
			}
		}
	}

	public void setNeighboursDisable(ArrayList<MyButton> arr, MyButton btnActive) {

		ArrayList<MyButton> arr2Neighbours = getArrNeighbours(arr, btnActive);
		// однопалубники
		boolean bool = true;
		for (int j3 = 0; j3 < arr2Neighbours.size(); j3++) {
			if (arr2Neighbours.get(j3).getStatus() != 0) {
				bool = false;
				break;
			}
		}
		if (bool == true) {
			changeNeighbourButtons(arr2Neighbours);
			//System.out.println("arr2Neighbours " + arr2Neighbours);
			for (int i = 0; i < arr.size(); i++) {//??????????????????????????
				for (int j = 0; j < arr2Neighbours.size(); j++) {
					arr.remove(arr2Neighbours.get(j));
				}
			}
			
		} else {// end однопалубники
			myltiDecker(arr, btnActive);
		}
	}

	public void myltiDecker(ArrayList<MyButton> arr, MyButton btnIn) {
		ArrayList<MyButton> arrNeighbours = getArrNeighbours(arr, btnIn);
		int count0 = 0, count1 = 0, count2 = 0, count4 = 0;

		for (int i = 0; i < arrNeighbours.size(); i++) {
			if (arrNeighbours.get(i).getStatus() == 0) {
				count0++;
			}
			if (arrNeighbours.get(i).getStatus() == 1) {
				count1++;
			}
			if (arrNeighbours.get(i).getStatus() == 2) {
				count2++;
			}
			if (arrNeighbours.get(i).getStatus() == 4) {
				count4++;
			}
		}

		//  begin
		if ((count0 == 0 || count0 == 1) && count2 == 1 && count1 == 0 && count4 == 0) {
			btnIn.setStatus(4);
			for (int i = 0; i < arrNeighbours.size(); i++) {
				if (arrNeighbours.get(i).getStatus() == 0) {
					// запоминаем пустую координату
					arrNeighbours.get(i).setStatus(5);
					break;
				}
			}

			for (int i = 0; i < arrNeighbours.size(); i++) {
				if (arrNeighbours.get(i).getStatus() == 2) {
					btnIn = arrNeighbours.get(i);
					myltiDecker(arr, btnIn);
					break;
				}
			}
		}

		// middle
		if (count4 == 1 && count2 == 1 && count0 == 0 && count1 == 0) {
			btnIn.setStatus(4);
			for (int i = 0; i < arrNeighbours.size(); i++) {
				if (arrNeighbours.get(i).getStatus() == 2) {
					btnIn = arrNeighbours.get(i);
					myltiDecker(arr, btnIn);
					break;
				}
			}
		}

		// end
		if (count4 == 1 && (count0 == 1 || count0 == 0) && count1 == 0 && count2 == 0) {
			ArrayList<MyButton> arrToChange = new ArrayList<MyButton>();
			btnIn.setStatus(4);
			for (int i = 0; i < arrNeighbours.size(); i++) {
				if (arrNeighbours.get(i).getStatus() == 0) {
					// запоминаем пустую координату
					arrNeighbours.get(i).setStatus(5);
					break;
				}
			}

			//если последняя часть корабля, то среди всего массива ищем 2 кнопки со статусом5
			for (int i = 0; i < arr.size(); i++) {
				if (arr.get(i).getStatus() == 5) {
					changeNeighbourButtons(arr.get(i));
					arr.remove(arr.get(i));
				}
			}
			return;
		}

		if (count4 == 1 && count1 == 1 && count2 == 0 && count0 == 0) {
			// значит корабль еще не убит
			retStatus(arr);
			return;
		}
	}

	public void retStatus(ArrayList<MyButton> arr){
		for (int i = 0; i < arr.size(); i++) {
			if (arr.get(i).getStatus() == 5) {
				arr.get(i).setStatus(0);
			}
			if (arr.get(i).getStatus() == 4) {
				arr.get(i).setStatus(2);
			}
		}
	}

	public ArrayList<MyButton> getArrNeighbours(ArrayList<MyButton> arr, MyButton b) {
		ArrayList<MyButton> arr2Neighbours = new ArrayList<MyButton>();
		for (int j = 1; j <= 4; j++) {
			for (int j2 = 0; j2 < arr.size(); j2++) {
				if (getNeighbours(b, j, 1).equals(arr.get(j2))) {
					arr2Neighbours.add(arr.get(j2));
				}
			}
		}
		return arr2Neighbours;
	}

	public void changeNeighbourButtons(ArrayList<MyButton> arrChange) {
		for (int i = 0; i < arrChange.size(); i++) {
			arrChange.get(i).setBackground(Color.gray);
			arrChange.get(i).setEnabled(false);
		}
	}
	
	public void changeNeighbourButtons(MyButton btnChange) {
		btnChange.setEnabled(false);
		btnChange.setBackground(Color.gray);
	}
}
