public class Coord {
	private int x, y;
	int  statusCoord;// 1 - есть корабль, 0 - пустая

	public Coord(int x, int y){
		this.x = x;
		this.y = y;
	}
	
	public Coord(int x, int y, int statusCoord){
		this.x = x;
		this.y = y;
		this.statusCoord = statusCoord;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + x;
		result = prime * result + y;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Coord other = (Coord) obj;
		if (x != other.x)
			return false;
		if (y != other.y)
			return false;
		return true;
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	public int  getStatusCoord() {
		return statusCoord;
	}

	public void setStatusCoord(int statusCoord) {
		this.statusCoord = statusCoord;
	}
	
	public String toString(){
		return x + " " + y;
	}

}
