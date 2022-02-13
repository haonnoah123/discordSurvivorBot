package survivor;

import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.io.IOException;

public class WriteFile {

	
	private String path;
	private boolean appendToFile = false;
	
	public WriteFile(String filePath) {
		path = filePath;
	}
	
	public WriteFile(String filePath, boolean appendValue) {
		path = filePath;
		appendToFile = appendValue;	
	}
	
	public void writeToFile(String textLine) throws IOException{
		FileWriter write = new FileWriter(path, appendToFile);
		PrintWriter printLine = new PrintWriter(write);
		printLine.printf(textLine);
		printLine.close();
	}
	
	public void clearFile() throws IOException{
		PrintWriter writer = new PrintWriter(path);
		writer.print("");
		writer.close();
	}
}
