package chibi.model;

public class CBMethod {
	private String methodName;
	private String className;
	private int argumentSize;
	private String source;
	private int startLineNumber;
	private String signature;
	private String description;
	private String classFullPath;
	private CBClass cbClass;
	
	public CBMethod(String methodName, String className, int argumentSize,
			String source, int lineNumberBody, String signature, CBClass cbClass) {
		this.methodName = methodName;
		this.className = className;
		this.argumentSize = argumentSize;
		this.source = source;
		this.startLineNumber = lineNumberBody;
		this.signature = signature;
		this.description = "";
		this.cbClass = cbClass;
	}
	
	public String getMethodName() {
		return methodName;
	}
	public void setMethodName(String methodName) {
		this.methodName = methodName;
	}
	public String getClassName() {
		return className;
	}
	public void setClassName(String className) {
		this.className = className;
	}
	public int getArgumentSize() {
		return argumentSize;
	}
	public void setArgumentSize(int argumentSize) {
		this.argumentSize = argumentSize;
	}
	public String getSource() {
		return source;
	}
	public void setSource(String source) {
		this.source = source;
	}
	
	public String toString(){
		return this.methodName;
	}

	public int getStartLineNumber() {
		return startLineNumber;
	}

	public void setStartLineNumber(int lineNumberBody) {
		this.startLineNumber = lineNumberBody;
	}

	public String getSignature() {
		return signature;
	}

	public void setSignature(String signature) {
		this.signature = signature;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getClassFullPath() {
		return classFullPath;
	}

	public void setClassFullPath(String classFullPath) {
		this.classFullPath = classFullPath;
	}

	public CBClass getCbClass() {
		return cbClass;
	}

	public void setCbClass(CBClass cbClass) {
		this.cbClass = cbClass;
	}
}
