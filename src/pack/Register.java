package pack;

public class Register {
	double val;
	SourceQ qI;
	public void writeValue(double val) {
		this.val = val ; 
		this.qI = SourceQ.ZERO ; 
	}

	public void waitForValue(SourceQ q)
	{
		this.qI = q;
	}
	
}
