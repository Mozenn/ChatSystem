package com.chatsystem.model;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;


/*
 * Wrap the content of a file and its name 
 * Used to be serialized to Json and send via socket 
 */
public class FileWrapper {
	
	private byte[] fileContent ; 
	private String fileName ; 
	
	public byte[] getFileContent() {
		return fileContent;
	}

	public void setFileContent(byte[] fileContent) {
		this.fileContent = fileContent;
	}

	public String getFileName() {
		return fileName;
	}

	/*
	 * @throw NullPointerException if fileName is null 
	 */
	public void setFileName(String fileName) {
		
		if(fileName == null)
			throw new NullPointerException() ;
		
		this.fileName = fileName;
	}


	
	public FileWrapper() throws IOException
	{
		fileContent = new byte[16] ; 
		fileName = "null" ; 
	}
	
	/*
	 * @throw NullPointerException if fileName is null 
	 */
	public FileWrapper(String fileName, File file) throws IOException
	{
		if(fileName == null)
			throw new NullPointerException() ;
		
		this.fileContent = Files.readAllBytes(file.toPath()) ; 
		this.fileName = fileName; 
	}
	
	 @Override
	 public String toString()
	 {
		 return fileName ; 
	 }

}
