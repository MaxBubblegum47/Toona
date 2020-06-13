
import java.io.*;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.zip.*;

/**
 * Zip class splits and zips all file's parts; Split's son
 * @author Lorenzo
 * 
 */
public class Zip extends Split{
	
	 /**
	 * Method to split and zip file
	 * @param file_string File's name
	 * @param part File's part
	 * @param cut FIle's cut
	 * @param CutSize bytes, kilobytes, megabytes,...
	 * @param CutType same size, custom size
	 * @throws IOException Signals that an I/O exception of some sort has occurred. This class is the general class of exceptions produced by failed or interrupted I/O operations.
	 */
	public void zipFiles(String file_string, int part, int cut, String CutSize, Boolean CutType) throws IOException {
		 	ArrayList<?> list = super.splitFile(file_string, part, cut, CutSize, CutType);
		 	String[] filePath = (String[]) list.toArray(new String[list.size()]);
	        for (int i = 0; i < list.size(); i++) {
		 	try {
	            File file = new File(filePath[i]);
	            String zipFileName = file.getAbsolutePath().concat(".zip");
	 
	            FileOutputStream fos = new FileOutputStream(zipFileName);
	            ZipOutputStream zos = new ZipOutputStream(fos);
	 
	            zos.putNextEntry(new ZipEntry(file.getName()));
	 
	            byte[] bytes = Files.readAllBytes(Paths.get(filePath[i]));
	            zos.write(bytes, 0, bytes.length);
	            zos.closeEntry();
	            zos.close();
	            file.delete();
	            
	            TalkingTerminal(file);

	 
	        } catch (FileNotFoundException ex) {
	            System.err.println("A file does not exist: " + ex);
	        } catch (IOException ex) {
	            System.err.println("I/O error: " + ex);
	        }
	    }
	 }
	
public void TalkingTerminal(File filename) {
	System.out.println("List of file that got zipped: " + filename.toString());
}


}//end of Class