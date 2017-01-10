package chibi.model;

import java.util.List;

public class CBClass {
	private List<String> lstImports;
	
	public CBClass(List<String> lstImports){
		this.lstImports = lstImports;
	}

	public List<String> getLstImports() {
		return lstImports;
	}

	public void setLstImports(List<String> lstImports) {
		this.lstImports = lstImports;
	}
}
