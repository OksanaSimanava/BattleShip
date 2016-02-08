//import java.awt.Button;
import javax.swing.JButton;
import java.awt.*;

public class MyButton extends JButton {

	private int x;
	private int y;
	private int status;

	public MyButton() {
       // super();
	}

	public MyButton(int x, int y, int status) {//throws HeadlessException {
		//super();
		this.x = x;
		this.y = y;
		this.status = status;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
    }
	
	public int getMyX() {
		return x;
	}

//	public void setX(int x) {
//		this.x = x;
//	}

	public int getMyY() {
		return y;
	}

//	public void setY(int y) {
//		this.y = y;
//	}

	@Override
	public String toString() {
		return this.x + " " + this.y + " status=" + status;
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
		MyButton other = (MyButton) obj;
		if (x != other.x || y != other.y)
			return false;
		return true;
	}

}
