import java.util.ArrayList;

public class BlackList {
	
	ArrayList<Coord> blackList = new ArrayList<>();

	public ArrayList<Coord> getBlackList() {
		return blackList;
	}

	public boolean containBusyCoord(int x, int y){
		return blackList.contains(new Coord(x, y));
	}
	
	public void addCoords(int x, int y){
		// border size
		//if( (x < 10 && y < 10) && (x >= 0 && y >= 0)  ){
		if( (x <= 10 && y <= 10) && (x >= 1 && y >= 1)  ){
			blackList.add(new Coord(x, y));
		}
	}
	
	public String toString(){
		return "blackList = " + getBlackList();
	}

}
