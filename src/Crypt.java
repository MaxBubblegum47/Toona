import java.awt.List;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;

import java.security.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;

import javax.crypto.*;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.PBEParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileSystemView;

import java.util.Base64;

/**
 * Crypting Class
 * @author Lorenzo
 */
public class Crypt extends Split{	

	/**
	 * This method split and cipher all file's parts
	 * @param nomefile File's name
	 * @param part File's part
	 * @param cut File's cut
	 * @param password File's password for cipher
	 * @param CutSize bytes, kilobytes, megabytes,...
	 * @param CutType same size, custom size
	 * @throws IOException Signals that an I/O exception of some sort has occurred. This class is the general class of exceptions produced by failed or interrupted I/O operations.
	 * @throws CryptoException Crypt Exception
	 */
	public void cryptFiles(String nomefile, int part, int cut, String password, String CutSize,Boolean CutType) throws IOException, CryptoException{
		ArrayList<?> list = super.splitFile(nomefile, part, cut, CutSize,CutType);
		String[] filePath = (String[]) list.toArray(new String[list.size()]);
        for (int i = 0; i < list.size(); i++) {
       	        try {
	            encryptFile( filePath[i], password );
	   	        } catch (IOException e) {
	            e.printStackTrace();
	        } catch (GeneralSecurityException e) {   
	            e.printStackTrace();
	        }
	    }
        
        TalkingTerminal(filePath);
	}

	    
	    /**
	     * Arbitrarily selected 8-byte salt sequence:
	     */
	    public final byte[] salt = {
	        (byte) 0x43, (byte) 0x76, (byte) 0x95, (byte) 0xc7,
	        (byte) 0x5b, (byte) 0xd7, (byte) 0x45, (byte) 0x17 
	    };

	    /**
	     * @param pass File's password
	     * @param decryptMode crypt mode (cipher/decipher)
	     * @return Cipher Object
	     * @throws GeneralSecurityException General Security Exception
	     */
	    public Cipher makeCipher(String pass, Boolean decryptMode) throws GeneralSecurityException{

	        //Use a KeyFactory to derive the corresponding key from the passphrase:
	        PBEKeySpec keySpec = new PBEKeySpec(pass.toCharArray());
	        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("PBEWithMD5AndDES");
	        SecretKey key = keyFactory.generateSecret(keySpec);

	        //Create parameters from the salt and an arbitrary number of iterations:
	        PBEParameterSpec pbeParamSpec = new PBEParameterSpec(salt, 42);

	        /*Dump the key to a file for testing: */
	        //Crypt.keyToFile(key);

	        //Set up the cipher:
	        Cipher cipher = Cipher.getInstance("PBEWithMD5AndDES");

	        //Set the cipher mode to decryption or encryption:
	        if(decryptMode){
	            cipher.init(Cipher.ENCRYPT_MODE, key, pbeParamSpec);
	        } else {
	            cipher.init(Cipher.DECRYPT_MODE, key, pbeParamSpec);
	        }

	        return cipher;
	    }


	    /**
	     * Encrypts one file to a second file using a key derived from a passphrase:
	     * @param fileName File's name
	     * @param pass File's password
	     * @throws IOException Signals that an I/O exception of some sort has occurred. This class is the general class of exceptions produced by failed or interrupted I/O operations.
	     * @throws GeneralSecurityException General Security Exception
	     */
	    public void encryptFile(String fileName, String pass)
	                                throws IOException, GeneralSecurityException{
	        byte[] decData;
	        byte[] encData;
	        File inFile = new File(fileName);
	        //Generate the cipher using pass:
	        Cipher cipher = makeCipher(pass, true);

	        //Read in the file:
	        FileInputStream inStream = new FileInputStream(inFile);

	        int blockSize = 8;
	        //Figure out how many bytes are padded
	        int paddedCount = blockSize - ((int)inFile.length()  % blockSize );

	        //Figure out full size including padding
	        int padded = (int)inFile.length() + paddedCount;

	        decData = new byte[padded];


	        inStream.read(decData);

	        inStream.close();

	        //Write out padding bytes as per PKCS5 algorithm
	        for( int i = (int)inFile.length(); i < padded; ++i ) {
	            decData[i] = (byte)paddedCount;
	        }

	        //Encrypt the file data:
	        encData = cipher.doFinal(decData);


	        //Write the encrypted data to a new file:
	        FileOutputStream outStream = new FileOutputStream(new File(fileName + ".encrypted"));
	        inFile.delete();
	        outStream.write(encData);
	        outStream.close();
	    }


	    
	    /**
	     * Decipher method
	     * @param fileName File's name
	     * @param pass File's password
	     * @return givemebackmyfile, my file deciphered
	     * @throws GeneralSecurityException GeneralSecurityException
	     * @throws IOException IOException
	     */
	    public File decryptFile(String fileName, String pass)
	                            throws GeneralSecurityException, IOException{
	        byte[] encData;
	        byte[] decData;
	        File inFile = new File(fileName);

	        //Generate the cipher using pass:
	        Cipher cipher = makeCipher(pass, false);

	        //Read in the file:
	        FileInputStream inStream = new FileInputStream(inFile);
	        encData = new byte[(int)inFile.length()];
	        inStream.read(encData);
	        inStream.close();
	        //Decrypt the file data:
	        decData = cipher.doFinal(encData);

	        //Figure out how much padding to remove
	        int padCount = (int)decData[decData.length - 1];

	        //Naive check, will fail if plaintext file actually contained
	        //this at the end
	        //For robust check, check that padCount bytes at the end have same value
	        if( padCount >= 1 && padCount <= 8 ) {
	            decData = Arrays.copyOfRange( decData , 0, decData.length - padCount);
	        }
	        
	        File givemebackmyfile = new File(fileName + ".decrypted.txt");
	        
	        //Write the decrypted data to a new file:
	        FileOutputStream target = new FileOutputStream(givemebackmyfile);
	        target.write(decData);
	        target.close();
	        return givemebackmyfile;
	    }

	    
	    /**
	     * Testing Method to see if it was possible to save-up all password used
	     * in a text file
	     * @param key password written by the user
	     */
	    public void keyToFile(SecretKey key){
	        try {
	            File keyFile = new File("C:\\keyfile.txt");
	            FileWriter keyStream = new FileWriter(keyFile);
	            String encodedKey = "\n" + "Encoded version of key:  " + key.getEncoded().toString();
	            keyStream.write(key.toString());
	            keyStream.write(encodedKey);
	            keyStream.close();
	        } catch (IOException e) {
	            System.err.println("Failure writing key to file");
	            e.printStackTrace();
	        }

	    }
	    
	    public void TalkingTerminal(String[] list) {
	    	System.out.println("List of file that got Crypted: " + Arrays.toString(list));
	    }
}


