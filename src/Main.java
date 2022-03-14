import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;

import picocli.CommandLine;
import picocli.CommandLine.Option;
import picocli.CommandLine.Parameters;

public class Main implements Runnable {
private String filePathSource = "c:/CODE_JAVA/logging.log";
	
//	@Command(name = "cliyudha", version = "1.0.0", mixinStandardHelpOptions = true, requiredOptionMarker = '*', optionListHeading = "%nPetunjuk Command%n", description = "Ini adalah aplikasi cli sederhana Yudha Permana untuk Telkom Indonesia")
	
	@Parameters(index = "0", description = "File Path Input")
	private String filePathInput;
	
	@Option(names = {"-t"}, description = "Parameter konversi", required = false)
	private Boolean konversi = false;
//	
	@Option(names = {"-o", "--open"}, description = "Parameter Output", required = false)
	private Boolean open = false;
	
	@Parameters(index = "1", description = "Konversi file", defaultValue = "text")
	private String extKonversi;
	
	@Parameters(index = "2", description = "File Path Output", defaultValue = "c:/CODE_JAVA/default.txt")
	private String filePathOutput;
	
	@Option(names = {"-h", "--help"}, usageHelp = true, required = false, description = "Petunjuk Command")
	private Boolean help = false;
	
	public static void main(String[] args){
		new CommandLine(new Main()).execute(args);
	}

	@Override
	public void run() {
		if(filePathInput != null && open == false && konversi == false) {
			try {
				onRead();
			} catch (Exception e) {
				System.out.println(e.getMessage());
			}
		}else if( (filePathInput != null && open == false && konversi) || filePathInput != null && open && konversi){
			if("text".equalsIgnoreCase(extKonversi)){
				try {
					onRead();
				} catch (Exception e) {
					System.out.println(e.getMessage());
				}
			}else if("json".equalsIgnoreCase(extKonversi)) {
				try {
					onCopy();
				} catch (Exception e) {
					System.out.println(e.getMessage());
				}
			}else{
				System.out.println("format ektensi tidak valid");
			}
		}
		else if(filePathInput != null && open && konversi == false){
			if( !("c:/CODE_JAVA/default.txt".equalsIgnoreCase(filePathOutput)) ) {
				try {
					onCopy();
				} catch (Exception e) {
					System.out.println(e.getMessage());
				}					
			}else{
				try {
					onRead();
				} catch (Exception e) {
					System.out.println(e.getMessage());
				}
			}
		}
		else{
			System.out.println("Command belum lengkap");
		}
	}
	
	public void onRead() throws Exception{
		File chiperFile = new File(filePathInput);
		BufferedReader br = new BufferedReader(new FileReader(chiperFile));
		
		File chiperFileOutput = new File("text".equalsIgnoreCase(extKonversi) && open ? filePathOutput : filePathInput+".txt");
		
		BufferedWriter fileOutput = new BufferedWriter(new FileWriter(chiperFileOutput));
		
		String line;
		try {
			System.out.println("\nBerhasil membuat output file .txt, Plaintext :");
		    while ((line = br.readLine()) != null) {
		    	fileOutput.write(line);
		    	fileOutput.newLine();
		        System.out.println(line);	
		    }

		} finally {
		    br.close();
		    fileOutput.close();
		}
	}
	
	public void onCopy() throws Exception{
		File chiperFile = new File(filePathSource);
		BufferedReader br = new BufferedReader(new FileReader(chiperFile));
		File chiperFileOutput = new File("json".equalsIgnoreCase(extKonversi) && open ? filePathOutput : "json".equalsIgnoreCase(extKonversi) ? (filePathInput + ".json") : filePathOutput);
		
		BufferedWriter fileOutput = new BufferedWriter(new FileWriter(chiperFileOutput));
		
		dataObject obj = new dataObject();
		
		try {
		    StringBuilder sb = new StringBuilder();
		    String line = br.readLine();

		    while (line != null) {
		        sb.append(line);
		        sb.append(" ");
		        
		        line = br.readLine();
		    }
		    String everything = sb.toString();
		    
		    ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
		    
			obj.setData(everything);
			String json = ow.writeValueAsString(obj);
			
			//copy
	        fileOutput.write(json);
	        
			System.out.println("Berhasil membuat output file .json");
		} finally {
		    br.close();
			fileOutput.close();
		}
		

	}
	
	public static class dataObject {
		private String data;

		public String getData() {
			return data;
		}

		public void setData(String data) {
			this.data = data;
		}
	}
}
