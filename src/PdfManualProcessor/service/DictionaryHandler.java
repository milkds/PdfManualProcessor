package PdfManualProcessor.service;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 * This class reads and returns dictionaries from files.
 */

public class DictionaryHandler {
    
    private static final Path SURE_DELETE_BY_URL = Paths.get("src\\PdfManualProcessor\\res\\sureDeleteUrlDict.txt");
    private static final Path CHECK_DELETE_BY_URL = Paths.get("src\\PdfManualProcessor\\res\\checkDeleteUrlDict.txt");
    private static final Path SURE_DELETE_BY_BODY = Paths.get("src\\PdfManualProcessor\\res\\sureDeleteBodyDict.txt");
    private static final Path CHECK_DELETE_BY_BODY = Paths.get("src\\PdfManualProcessor\\res\\checkDeleteBodyDict.txt");
    
    private static List<String> getDictionary(Path dictionaryFilePath){
        List<String> result = new ArrayList<>();
        try(BufferedReader fileReader = new BufferedReader(new FileReader(dictionaryFilePath.toAbsolutePath().toFile())) ){
            while (fileReader.ready()){
                String line = fileReader.readLine();
                result.add(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;

    }
    
    public static List<String>getSureDeleteByUrlDictionary(){
        return getDictionary(SURE_DELETE_BY_URL);
    }
    public static List<String>getCheckDeleteByUrlDictionary(){
        return getDictionary(CHECK_DELETE_BY_URL);
    }
    public static List<String>getSureDeleteByBodyDictionary(){
        return getDictionary(SURE_DELETE_BY_BODY);
    }
    public static List<String>getCheckDeleteByBodyDictionary(){
        return getDictionary(CHECK_DELETE_BY_BODY);
    }
    
    
    //// TODO: 30.03.2016 write Paths, implement getDictionary 
}
