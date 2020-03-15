package sumup;
import java.io.Serializable;

public class AbstractMessage implements Serializable{
	
	static final long serialVersionUID = 1L;
	
	private int sequence;
	
	public int getSequence() {
		return sequence;
	}
	
	
	public void setSequence(int sequence) {
		this.sequence = sequence;
	}

}
