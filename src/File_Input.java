
/**
 * This class represents an input file
 * 
 * @author Lorenzo
 * 
 */
public class File_Input {
	private String Job;
	private String File_Name;
	private Boolean Zip;
	private Boolean Crypt;
	private int Part;
	private int Cut;
	private String CutSize;
	private Boolean CutType;

	/**
	 * @param FN      File's Name
	 * @param J       Job that concerns the file
	 * @param Z       Boolean, true if the file need to be zip
	 * @param C       Boolean, true if the file need to be crypt
	 * @param PT      Part counter
	 * @param CT      Cut value
	 * @param CutSize size of cut (bytes, kilobytes, megabytes, ...)
	 * @param CutType type of cut (same size for every cut or custom size)
	 */
	public File_Input(String FN, String J, Boolean Z, Boolean C, int PT, int CT, String CutSize, Boolean CutType) {
		this.Job = J;
		this.File_Name = FN;
		this.Zip = Z;
		this.Crypt = C;
		this.Part = PT;
		this.Cut = CT;
		this.CutSize = CutSize;
		this.CutType = CutType;
		System.out.println(FN + " is getting into the Queue");
	}

	public String getFileName() {
		return File_Name;
	}

	public String JobName() {
		return Job;
	}

	public boolean FileZip() {
		if (Zip == true)
			return true;
		else
			return false;
	}

	public boolean FileCrypt() {
		if (Crypt == true)
			return true;
		else
			return false;
	}

	public Integer FilePart() {
		return Part;
	}

	public Boolean CutType() {
		return CutType;
	}

	public Integer FileCut() {
		return Cut;
	}

	public String CutSize() {
		return CutSize;
	}
}
