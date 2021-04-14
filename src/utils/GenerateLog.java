package utils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;



public class GenerateLog {
	public static void writeFileTxt(Double kpi, File file, Integer nnt) {
        try {
            FileOutputStream is = new FileOutputStream(file);
            OutputStreamWriter osw = new OutputStreamWriter(is);
            Writer w = new BufferedWriter(osw);
            w.write(LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss"))+"\n");
            w.write("KPI actual\n");
            w.write(kpi+" %\n");
            w.write("Numero de transacciones no integras: "+nnt+"\n");
            w.write("+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++\n\n");
            w.close();
        } catch (IOException | SecurityException | IllegalArgumentException e) {
           System.out.println("error inesperado");
           e.printStackTrace();
        }

        
    }
	
	public static File createFichero() {
        String currentPath = Paths.get("").toAbsolutePath().normalize().toString();
        String downloadFolder = "/logIntegrity";
        String downloadPath = currentPath + downloadFolder;
        File newFolder = new File(downloadPath);
        boolean dirCreated = newFolder.mkdir();

        String fileName ="logs.txt";

        File res = new File(downloadPath + "/" + fileName);
        return res;
	}
}
