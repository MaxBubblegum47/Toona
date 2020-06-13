
import java.io.*;
import java.lang.reflect.Array;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

/**
 * Split divides in parts files. It's Zip's and Crypt's father.
 * 
 * @author Lorenzo
 */
public class Split {
	private ArrayList<String> list = new ArrayList<String>();

	/**
	 * This class divides an input file according to a given part number or cut
	 * 
	 * @param file_string File's name
	 * @param part        File's part
	 * @param cut         File's cut
	 * @param CutSize 	  Cut's Dimension Byte, Kilo Byte, Mega Byte,...	 
	 * @param CutType	  Custom Cut or Part Cut selector 	
	 * @return list of file's parts
	 * @throws IOException Signals that an I/O exception of some sort has occurred.
	 *                     This class is the general class of exceptions produced by
	 *                     failed or interrupted I/O operations.
	 */
	public ArrayList<String> splitFile(String file_string, int part, int cut, String CutSize, Boolean CutType)
			throws IOException {
		list.clear();
		File f = new File(file_string);
		int partCounter = 1;
		int dim = (int) f.length();
		int sizeOfFiles = 0;
		if (CutType) {
			sizeOfFiles = dim / part;
		} else {
			if (sizeOfFiles > dim) {
				cut = Integer.parseInt(ErrorCutDialog());
			}
			if (CutSize == "B") {
				sizeOfFiles = cut;
			} else if (CutSize == "KB") {
				sizeOfFiles = cut * 1024;
			} else if (CutSize == "MB") {
				sizeOfFiles = cut * 1048576;
			} else if (CutSize == "GB") {
				sizeOfFiles = cut * 1073741824;
			}
		}

		byte[] buffer = new byte[sizeOfFiles];

		/*
		 * for odd's number of bytes it creates an array of sizeOfFile + 1 dimension in
		 * order to get the right number of part selected by the user
		 */
		if ((sizeOfFiles) / 2 == 0) {
			buffer = new byte[sizeOfFiles];
		} else {
			buffer = new byte[sizeOfFiles + 1];
		}

		String fileName = f.getName();

		// try-with-resources to ensure closing stream
		try (FileInputStream fis = new FileInputStream(f); BufferedInputStream bis = new BufferedInputStream(fis)) {

			int bytesAmount = 0;
			while ((bytesAmount = bis.read(buffer)) > 0) {
				// write each chunk of data into separate file with different number in name
				String filePartName = String.format("%s", fileName + "part" + partCounter);
				partCounter++;

				File newFile = new File(f.getParent(), filePartName);
				list.add(newFile.getAbsolutePath());
				try (FileOutputStream out = new FileOutputStream(newFile)) {
					out.write(buffer, 0, bytesAmount);
				}
			}
		}

		TalkingTerminal(list);
		return list;
	}// ends of splitFile

	/**
	 * ErrorCutDialog works if the user chooses a cut bigger than file's dimension
	 * 
	 * @return return string that is the new cut values if the old one is bigger
	 *         than file's dimension
	 */
	public String ErrorCutDialog() {
		String name = null;
		JFrame Dialog;
		Dialog = new JFrame();
		return name = JOptionPane.showInputDialog(Dialog, "Appropriate Cut");
	}

	public void TalkingTerminal(ArrayList<String> list) {
		System.out.println("The return's list of element, ready for crypt or zip: " + list.toString());
	}
}// ends of Split Class
