package PdfManualProcessor.service;

import java.nio.file.Path;
import java.util.List;

/**
 * This class reads and returns dictionaries from files.
 */

public class DictionaryHandler {
    
    private static final Path sureDeleteByUrl = null;
    private static final Path checkDeleteByUrl = null;
    private static final Path sureDeleteByBody = null;
    private static final Path checkDeleteByBody = null;
    
    private static List<String> getDictionary(Path dictionaryFilePath){
        return null;
    }
    
    public static List<String>getSureDeleteByUrlDictionary(){
        return getDictionary(sureDeleteByUrl);
    }
    public static List<String>getCheckDeleteByUrlDictionary(){
        return getDictionary(checkDeleteByUrl);
    }
    public static List<String>getSureDeleteByBodyDictionary(){
        return getDictionary(sureDeleteByBody);
    }
    public static List<String>getCheckDeleteByBodyDictionary(){
        return getDictionary(checkDeleteByBody);
    }
    
    
    //// TODO: 30.03.2016 write Paths, implement getDictionary 
}
