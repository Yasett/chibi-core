package chibi.model;

import chibi.gumtreediff.actions.model.*;

public abstract class CBVisitor {
	
	public abstract void check(Addition addition);
	public abstract void check(Insert insert);
	public abstract void check(Move insert);
	public abstract void check(Delete insert);
	public abstract void check(Update insert);
}
