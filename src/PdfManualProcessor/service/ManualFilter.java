package PdfManualProcessor.service;

import PdfManualProcessor.Manual;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
/**
 * This class manages manual filtration process.
 */
public abstract class ManualFilter {
    public static void filterNotOpenManuals (List<Manual> downloadedManuals){} //to be decided

    /**
     * Checks if manual contains a word from dictionary.
     * @param checkString - Can be manual body, or manual Url - logic remains same for both cases.
     * @param dictionary - list of words, by which manuals are checked for possible delete or other purposes.
     * @return manual contains a word from dictionary
     */
    private static boolean manualContainsWordFromDictionary(String checkString, List<String> dictionary){
        for (String word : dictionary){
            if (checkString.contains(word))return true;
        }
        return false;
    }

    /**
     * Filters and serializes manuals by Url.
     * As method works fast - its no sense of serializing only one manual at once.
     * So we are checking full list and than serializing results.
     * @param manuals List of Manuals for checking.
     * @param sureDeleteDictionary Word list for sure delete.
     * @param checkDeleteDictionary Word list for delete after checking.
     */
    public static void filterManualsByUrl(List<Manual> manuals, List<String>sureDeleteDictionary, List<String>checkDeleteDictionary){
        //Initialising lists for keeping filtered manuals.
        List<Manual> sureDeleteManuals = new ArrayList<>();
        List<Manual> checkDeleteManuals = new ArrayList<>();

        for (Manual manual : manuals){
            //Getting url for check.
            String checkString=manual.getPdfUrl();

            //Checking by url for sure delete. (If true - no need for further check).
            if (manualContainsWordFromDictionary(checkString,sureDeleteDictionary)) {
                sureDeleteManuals.add(manual);
                continue;
            }
            //Checking by url for delete after users visual check.
            if (manualContainsWordFromDictionary(checkString,checkDeleteDictionary)) {
                checkDeleteManuals.add(manual);
            }
        }

        //Serializing manuals.
        ManualSerializer.saveSureDeleteManualsToFile(sureDeleteManuals);
        ManualSerializer.saveCheckDeleteManualsToFile(checkDeleteManuals);
    }

    /**
     * Filters and serializes manual by Url. As method is slow - we serialize each manual, after check.
     * @param m - Manual.
     * @param sureDeleteDictionary - Word list for sure delete.
     * @param checkDeleteDictionary - Word list for delete after checking.
     */
    public static void filterManualByBody(Manual m,List<String>sureDeleteDictionary, List<String>checkDeleteDictionary ){
        //Getting manual body.
        String s = ManualReader.readStartingPages(m);
        if (s==null) return;

        // Checking by body for sure delete. (If true - no need for further check).
        if (manualContainsWordFromDictionary(s,sureDeleteDictionary)){
           ManualSerializer.saveSureDeleteManualsToFile(Collections.singletonList(m));
           return;
        }
        //Checking by url for delete after users visual check.
        if (manualContainsWordFromDictionary(s,checkDeleteDictionary)){
            ManualSerializer.saveCheckDeleteManualsToFile(Collections.singletonList(m));
        }
    }
}
