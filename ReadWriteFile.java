import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class ReadWriteFile {

	private static final String INPUT_FILE_DIRECTORY = "/Users/vvvv/Desktop/IP_FOLDER";

	private static final String OUTPUT_FILE_PATH = "/Users/vvvv/Desktop/copy/";
	private static final String OUTPUT_FILE_FORMAT = ".txt";
	private static final String HOSTNAME = "hostname";
	private static final String HOSTFILENAME_SEPERATOR = "-";
	private static final int NO_OF_COPIES = 5;
	private static String mHostName;

	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		// Find the list of files in the directory
		getListOfFilesFromDirectory();
		
	}
	
	// API to get the list of files in the directory and check if the file has the sentence.
	private static void getListOfFilesFromDirectory() {
		
		File folder = new File(INPUT_FILE_DIRECTORY);
		File[] listOfFiles = folder.listFiles();
		String path = "";

		    for (int i = 0; i < listOfFiles.length; i++) {
		      if (listOfFiles[i].isFile()) {
		        
		    	// System.out.println("File " + listOfFiles[i].getName());		        
	            // get absolute path
	            path = listOfFiles[i].getAbsolutePath();
	            
	            mHostName = stripExtension (listOfFiles[i].getName());
	            // System.out.print("Absolute Pathname : "+ path + " \n");
	            
	            checkForFiles(path);	            
		      }
		    }
	}
	
    static String stripExtension (String str) {
        // Handle null case specially.

        if (str == null) return null;

        // Get position of last '.'.

        int pos = str.lastIndexOf(".");

        // If there wasn't any '.' just return the string as is.

        if (pos == -1) return str;

        // Otherwise return the string, up to the dot.

        return str.substring(0, pos);
    }
	
	private static void checkForFiles(String fileName) {
		
		boolean fileData = false;

		try {
			// Check if the string is present in the file or not
			fileData = checkIfHostNameExits(fileName);
						
			// If string is present then try to create copies.
			if (fileData) {
				for (int i = 1; i <= NO_OF_COPIES; i++) {
					writeDataToNewFile(fileName, i);
				}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	/*
	 * Read the file line by line and check if the hostname is present in the line.
	 */
	static boolean checkIfHostNameExits(String fileName) throws IOException {

		// System.out.println(" readFile START \n ");
		boolean retVal = false;

		BufferedReader br = new BufferedReader(new FileReader(fileName));
		try {

			String line = null;
			line = br.readLine();

			while (line != null) {

				if (checkforSentence(line)) {

					// System.out.println(" STRING is present in the line \n ");
					retVal = true;
					return retVal;
				}

				line = br.readLine();
			}

			// System.out.println(" readFile END \n ");
			return retVal;

		} finally {
			br.close();
		}
	}

	/*
	 *  Check if the string is present or not.
	 *  If present then return True else false.
	 */
	
	private static boolean checkforSentence(String input) {

		// System.out.println(" checkforString START \n ");

		boolean ret = false;
		boolean hostNameExits = false;

		String[] sentence = input.split(" ");
		int len = sentence.length -1 ;		
			
		for (String word : sentence) {
			if (word.equals(HOSTNAME)) {
				// If hostname is present in the line then make the variable as true.
				hostNameExits = true;
			}

			// If we have the variable then only we check for other words else no need to check other words.
			if (hostNameExits) {
				if (word.equals(mHostName)) {
					ret = true;
					return ret;
				}
			}
		}	

		// System.out.println(" checkforString END \n ");
		return ret;
	}

	/*
	 * This API is to create a new file name
	 * Eg. Bank-YEPG-C3560-A01-1, Bank-YEPG-C3560-A01-2, Bank-YEPG-C3560-A01-3
	 */
	private static String getNewFileName(String fileName, int i) {

		StringBuffer newfileName = new StringBuffer();
		newfileName.append(mHostName);
		newfileName.append(HOSTFILENAME_SEPERATOR);
		newfileName.append(i);

		String newFileName = newfileName.toString();
		System.out.println(" GetNewFileName :  " + newFileName + " \n");

		return newFileName;
	}

	@SuppressWarnings("resource")
	private static void writeDataToNewFile(String orgFileName, int i)
			throws IOException {

		// System.out.println(" writeDataToNewFile START \n ");

		// Get New FileName where we want to write to.
		String newFileName = getNewFileName(orgFileName, i);

		BufferedReader br = new BufferedReader(new FileReader(orgFileName));

		BufferedWriter writer = new BufferedWriter(new FileWriter(
				OUTPUT_FILE_PATH + newFileName + OUTPUT_FILE_FORMAT));

		try {
			StringBuilder sb = new StringBuilder();
			String line = null;
			line = br.readLine();

			while (line != null) {

				// While creating copies make sure we replace the required file name with the new file name
				if (checkforSentence(line)) {

					sb.append(HOSTNAME);
					sb.append(" ");
					sb.append(newFileName);

					line = sb.toString();
				}

				writer.write(line);
				writer.newLine();

				line = br.readLine();
			}

			// System.out.println(" writeDataToNewFile END \n ");

		} finally {
			br.close();
			writer.close();
		}
	}

}
