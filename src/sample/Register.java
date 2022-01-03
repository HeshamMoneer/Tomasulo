package sample;

public class Register {
	double val;
	SourceQ qI;

	public Register(){
		this.val = 0;
		this.qI = SourceQ.ZERO;
	}
	public Register(int val){
		this.val = val;
		this.qI = SourceQ.ZERO;
	}

	public void writeValue(double val) {
		this.val = val ; 
		this.qI = SourceQ.ZERO ; 
	}

	public void waitForValue(SourceQ q)
	{
		this.qI = q;
	}

	public String toString(){
		return "("+this.qI + ", "+this.val+")";
	}

}
