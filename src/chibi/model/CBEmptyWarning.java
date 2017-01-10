package chibi.model;

public class CBEmptyWarning implements ICBWarning{
	
	public boolean hasMessage()
	{
		return false;
	}
	
	public String toString(){
		return "";
	}
}
