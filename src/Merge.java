
import java.io.*;
import java.nio.channels.FileChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.GeneralSecurityException;
import java.util.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.filechooser.FileSystemView;
import javax.xml.parsers.DocumentBuilder;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileSystemView;

/**
 * Merging Class
 * 
 * @author Lorenzo
 */
public class Merge {
	private final int BUFFER_SIZE = 4096;

	/**
	 * Merge Method called from testing.java that performs Merge Button in gui
	 * merging all selected files
	 * 
	 * @param mergiato Merged's file name
	 * @throws FileNotFoundException    Signals that an attempt to open the file
	 *                                  denoted by a specified pathname has failed.
	 * @throws IOException              Signals that an I/O exception of some sort
	 *                                  has occurred. This class is the general
	 *                                  class of exceptions produced by failed or
	 *                                  interrupted I/O operations.
	 * @throws GeneralSecurityException The GeneralSecurityException class is a
	 *                                  generic security exception class that
	 *                                  provides type safety for all the
	 *                                  security-related exception classes that
	 *                                  extend from it.
	 */
	public void Merge(String mergiato) throws FileNotFoundException, IOException, GeneralSecurityException {
		Path path = Paths.get(mergiato);
		Path filename = path.getFileName();

		Path completedir = path.getParent();
		String dir = completedir.toString();
		__rebuild(filename, dir);

		TalkingTerminal(mergiato, null);// 1
	}

	/**
	 * The method is used to search all the part that concerns a particular file
	 * choosen by the user
	 * 
	 * @param p           first file's complete path
	 * @param completeDir first's file directory location
	 * @throws IOException              Signals that an I/O exception of some sort
	 *                                  has occurred. This class is the general
	 *                                  class of exceptions produced by failed or
	 *                                  interrupted I/O operations.
	 * @throws FileNotFoundException    Signals that an attempt to open the file
	 *                                  denoted by a specified pathname has failed.
	 * @throws GeneralSecurityException The GeneralSecurityException class is a
	 *                                  generic security exception class that
	 *                                  provides type safety for all the
	 *                                  security-related exception classes that
	 *                                  extend from it.
	 */
	protected void __rebuild(Path p, String completeDir)
			throws IOException, FileNotFoundException, GeneralSecurityException {
		Boolean zip = false;
		Boolean crypt = false;
		ArrayList<File> list = new ArrayList<File>();
		boolean finished = false;
		int partnumber = 1;
		String pp = p.toString();
		String ff = null;

		Path rebuiltFileName = p;

		if (p.toString().contains(".zip")) {
			zip = true;
			System.out.println("Chosen file is zipped"); // 2
			ff = p.toString().substring(0, pp.lastIndexOf(".zip"));
			ff = p.toString().substring(0, pp.lastIndexOf("1"));
		} else if (p.toString().contains(".encrypted")) {
			crypt = true;
			System.out.println("Chosen file is crypted"); // 3
			ff = p.toString().substring(0, pp.lastIndexOf(".encrypted"));
			ff = p.toString().substring(0, pp.lastIndexOf("1"));
		} else {
			ff = p.toString().substring(0, pp.lastIndexOf("1"));
		}

		try (RandomAccessFile rebuiltFile = new RandomAccessFile(rebuiltFileName.toFile(), "rw")) {
			do {
				Path partFileName;
				if (zip) {
					partFileName = Paths.get(completeDir + File.separator + ff + partnumber + ".zip");
				} else if (crypt) {
					partFileName = Paths.get(completeDir + File.separator + ff + partnumber + ".encrypted");
				} else {
					partFileName = Paths.get(completeDir + File.separator + ff + partnumber);
				}

				if (!(Files.exists(partFileName))) {
					finished = true;
				} else {
					list.add(partFileName.toFile());
					partnumber++;
				}
			} while (!finished);
		}

		if (zip && !crypt) {
			List<File> uzip = new ArrayList<File>();
			File destDir = null;
			for (int i = 0; i < list.size(); i++) {
				String fileZip = list.get(i).toString();
				destDir = new File(list.get(i).getParent());
				byte[] buffer = new byte[1024];
				ZipInputStream zis = new ZipInputStream(new FileInputStream(fileZip));
				ZipEntry zipEntry = zis.getNextEntry();
				while (zipEntry != null) {
					File newFile = newFile(destDir, zipEntry);
					FileOutputStream fos = new FileOutputStream(newFile);
					int len;
					while ((len = zis.read(buffer)) > 0) {
						fos.write(buffer, 0, len);
					}
					fos.close();
					zipEntry = zis.getNextEntry();
					uzip.add(newFile);
				}

				zis.closeEntry();
				zis.close();
				File temp = new File(fileZip);
				temp.delete();
			}
			File into = new File(completeDir + File.separator + ff.substring(0, ff.lastIndexOf('p')));
			TalkingTerminal(null, list); // 4
			mergeFiles(uzip, into);
			
		} else if (crypt && !zip) {
			List<File> ucrypt = new ArrayList<File>();
			String pw = InputDialog();
			for (int i = 0; i < list.size(); i++) {
				Crypt Crypt = new Crypt();
				ucrypt.add(Crypt.decryptFile(list.get(i).toString(), pw));
			}
			File into = new File(completeDir + File.separator + ff.substring(0, ff.lastIndexOf('p')));
			TalkingTerminal(null, list); // 5
			Bidone(list);
			mergeFiles(ucrypt, into);

		} else {
			File into = new File(completeDir + File.separator + ff.substring(0, ff.lastIndexOf('p')));
			System.out.println("Compelte Dir: " + completeDir);
			TalkingTerminal(null, list); // 6
			mergeFiles(list, into);
		}
	}

	/**
	 * Auxiliary function to Unzip file
	 * 
	 * @param destinationDir Destination Directory for the unzipped file
	 * @param zipEntry       Zip file Entry
	 * @return destination File
	 * @throws IOException Signals that an I/O exception of some sort has occurred.
	 *                     This class is the general class of exceptions produced by
	 *                     failed or interrupted I/O operations.
	 */
	public File newFile(File destinationDir, ZipEntry zipEntry) throws IOException {
		File destFile = new File(destinationDir, zipEntry.getName());

		String destDirPath = destinationDir.getCanonicalPath();
		String destFilePath = destFile.getCanonicalPath();

		if (!destFilePath.startsWith(destDirPath + File.separator)) {
			throw new IOException("Entry is outside of the target dir: " + zipEntry.getName());
		}

		return destFile;

	}

	/**
	 * Merge all files from the given list
	 * 
	 * @param files List of Files to merge
	 * @param into  Output File
	 * @throws IOException Signals that an I/O exception of some sort has occurred.
	 *                     This class is the general class of exceptions produced by
	 *                     failed or interrupted I/O operations.
	 */
	public void mergeFiles(List<File> files, File into) throws IOException {
		try (FileOutputStream fos = new FileOutputStream(into);
				BufferedOutputStream mergingStream = new BufferedOutputStream(fos)) {
			for (File f : files) {
				Boolean gg = f.isFile();
				Path temp = f.toPath();
				System.out.println("Path used during mergingstream " + temp + " " + gg);
				Files.copy(temp, mergingStream);
				System.out.println("Delete this trash: " + f.toString());
			}
			Bidone(files);
		}
	}

	/**
	 * Garbage Files collector
	 * 
	 * @param files all redundant files to delete
	 * @throws IOException Signals that an I/O exception of some sort has occurred.
	 *                     This class is the general class of exceptions produced by
	 *                     failed or interrupted I/O operations.
	 */
	public void Bidone(List<File> files) throws IOException {
		for (int i = 0; i < files.size(); i++) {
			System.out.println(files.get(i));
			files.get(i).delete();
		}
	}

	/**
	 * Take from user's input the password to decipher files
	 * 
	 * @return return the password for decipher file
	 */
	@SuppressWarnings("unused")
	public String InputDialog() {
		JPanel panel = new JPanel();
		JLabel label = new JLabel("Enter a password:");
		JPasswordField pass = new JPasswordField(10);
		panel.add(label);
		panel.add(pass);
		String[] options = new String[]{"OK", "Cancel"};
		int option = JOptionPane.showOptionDialog(null, panel, "Decryption",
		                         JOptionPane.NO_OPTION, JOptionPane.PLAIN_MESSAGE,
		                         null, options, options[1]);
		String password = null;
		
		if(option == 0) // pressing OK button
		{
		    char[] Char_password = pass.getPassword();
		    password = new String (Char_password);
		    if (password == null) {
		    	password = "";
		    }
		    System.out.println("La tua password è: " + password);
		};
		return password;
	}

	public void TalkingTerminal(String merged, List lista) {
		if (merged != null) {
			System.out.println("Chosen File to Merge: " + merged);
		}
		if (lista != null) {
			System.out.println("List of file to merge" + lista);
		}
	}

}
