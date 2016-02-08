//import java.sql.Array;
import java.util.ArrayList;

public class Ship {

	private int statusShip;
	private int sizeShip;
	private ArrayList<Coord> arrElement = new ArrayList<Coord>();
	
	public Ship() {
	}
	
	public Ship(int sizeShip) {
		this.sizeShip = sizeShip;
	}
	
	public Ship(int status, int sizeShip, ArrayList<Coord> arrElement){
		this.statusShip = status;
		this.sizeShip = sizeShip;
		this.arrElement = arrElement;
	}
	
	public ArrayList<Coord> getArrElement() {
		return arrElement;
	}

	public void setArrElement(ArrayList<Coord> arrElement) {
		this.arrElement = arrElement;
	}

	public int getStatus() {
		return statusShip;
	}

	public void setStatus(int status) {
		this.statusShip = status;
	}

	public int getSize() {
		return sizeShip;
	}

	public void setSize(int sizeShip) {
		this.sizeShip = sizeShip;
	}
	public String toString(){
		return "ship" + " "+sizeShip +" " + arrElement;
	}


}
