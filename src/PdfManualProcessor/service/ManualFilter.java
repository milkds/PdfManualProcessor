package PdfManualProcessor.service;

import PdfManualProcessor.Manual;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public abstract class ManualFilter {

    public static void filterNotOpenManuals (List<Manual> downloadedManuals){} //to be decided
    private static boolean manualContainsWordFromDictionary(String checkString, List<String> dictionary){
        for (String word : dictionary){
            if (checkString.contains(word))return true;
        }
        return false;
    }

    /**
     * This method will be used for filtering manuals by URL
     * @param manuals List Manuals for Checking
     * @param sureDeleteDictionary Word list for sure delete.
     * @param checkDeleteDictionary Word list for delete after checking.
     */
    public static void filterManualsByUrl(List<Manual> manuals, List<String>sureDeleteDictionary, List<String>checkDeleteDictionary){
        List<Manual> sureDeleteManuals = new ArrayList<>();
        List<Manual> checkDeleteManuals = new ArrayList<>();

        for (Manual manual : manuals){
            String checkString=manual.getPdfUrl();
            if (manualContainsWordFromDictionary(checkString,sureDeleteDictionary)) {
                sureDeleteManuals.add(manual);
                continue;
            }
            if (manualContainsWordFromDictionary(checkString,checkDeleteDictionary)) {
                checkDeleteManuals.add(manual);
            }
        }

        ManualSerializer.saveSureDeleteManualsToFile(sureDeleteManuals);
        ManualSerializer.saveCheckDeleteManualsToFile(checkDeleteManuals);
    }

    public static void filterManualByBody(Manual m,List<String>sureDeleteDictionary, List<String>checkDeleteDictionary ){
        String s = ManualReader.readStartingPages(m);
        if (s==null) return;

        if (manualContainsWordFromDictionary(s,sureDeleteDictionary)){
           ManualSerializer.saveSureDeleteManualsToFile(Collections.singletonList(m));
           return;
        }
        if (manualContainsWordFromDictionary(s,checkDeleteDictionary)){
            ManualSerializer.saveCheckDeleteManualsToFile(Collections.singletonList(m));
        }
    }


}
