package com.barclays.ospworkflow;

public class FileDetails {

	String file;
	String docType;
	private String fileName;
	
	public String getfile() {
		return file;
	}
	public void setfile(String base64File) {
		this.file = base64File;
	}
	public String getdocType() {
		return docType;
	}
	public void setdocType(String documentType) {
		docType = documentType;
	}


	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
}
