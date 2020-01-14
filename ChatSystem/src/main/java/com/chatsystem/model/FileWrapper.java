package com.chatsystem.model;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class FileWrapper {
	
	public byte[] getFileContent() {
		return fileContent;
	}

	public void setFileContent(byte[] fileContent) {
		this.fileContent = fileContent;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	private byte[] fileContent ; 
	private String fileName ; 
	
	public FileWrapper() throws IOException
	{
		fileContent = new byte[16] ; 
		fileName = "null" ; 
	}
	
	public FileWrapper(String fileName, File file) throws IOException
	{
		this.fileContent = Files.readAllBytes(file.toPath()) ; 
		this.fileName = fileName; 
	}
	
	 @Override
	 public String toString()
	 {
		 return fileName ; 
	 }

}
