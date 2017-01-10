package chibi.model;

import java.util.HashMap;
import java.util.Map.Entry;

public class CBWarning implements ICBWarning{
	private String name;
	private String methodName;
	private String className;
	private HashMap<String, Integer> messages;
	
	public CBWarning(String name, HashMap<String, Integer> messages, String className, String methodName){
		this.name = name;
		this.messages = messages;
		this.methodName = methodName;
		this.className = className;
	}
	
	public boolean hasMessage()
	{
		return true;
	}		
	
	public String toString(){
		StringBuilder builder = new StringBuilder();
		builder.append(this.name);
		builder.append(": ");
		builder.append(this.className);
		builder.append(", ");
		builder.append(this.methodName);
		
		for (Entry<String,Integer> cbStatement : this.messages.entrySet()) {
			builder.append("\n");
			builder.append(cbStatement.getKey().toString());
			builder.append(", line: ");
			builder.append(cbStatement.getValue().toString());			
		}
		
		return builder.toString();		
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public HashMap<String, Integer> getMessages() {
		return messages;
	}

	public void setMessages(HashMap<String, Integer> messages) {
		this.messages = messages;
	}
}
