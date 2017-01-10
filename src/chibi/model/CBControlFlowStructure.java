package chibi.model;

public class CBControlFlowStructure {
	private CBStatementType statementType;
	private String className;
	private String source;
	public CBControlFlowStructure(CBStatementType statementType, String className, String source){
		this.statementType = statementType;
		this.className = className;
		this.source = source;
	}
	public CBStatementType getStatementType() {
		return statementType;
	}
	public void setStatementType(CBStatementType statementType) {
		this.statementType = statementType;
	}
	public String getClassName() {
		return className;
	}
	public void setClassName(String className) {
		this.className = className;
	}
	public String getSource() {
		return source;
	}
	public void setSource(String source) {
		this.source = source;
	}
	public String toString(){
		return this.statementType.name();
	}

}
