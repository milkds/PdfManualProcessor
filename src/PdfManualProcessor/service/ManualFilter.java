package PdfManualProcessor.service;

import PdfManualProcessor.Manual;
import PdfManualProcessor.service.DictionaryHandler;
import PdfManualProcessor.service.ManualSerializer;

import java.util.ArrayList;
import java.util.List;

public abstract class ManualFilter {

    public static void filterNotOpenManuals (List<Manual> downloadedManuals){} //to be decided
    public static boolean manualContainsWordFromDictionary(String checkString, List<String> dictionary){
        for (String word : dictionary){
            if (checkString.contains(word))return true;
        }
        return false;
    }

    /**
     * This method will be used for filtering manuals both by URLs and by Manual Bodies, however last option will be used by batch (will start testing from 100 pcs)
     * @param manuals List Manuals for Checking
     * @param checkByBody Set true, if need to check by body.
     * @param sureDeleteDictionary Word list for sure delete.
     * @param checkDeleteDictionary Word list for delete after checking.
     */

    public static void deleteFilter(List<Manual> manuals, boolean checkByBody, List<String>sureDeleteDictionary, List<String>checkDeleteDictionary){
        List<Manual> sureDeleteManuals = new ArrayList<>();
        List<Manual> checkDeleteManuals = new ArrayList<>();

        for (Manual manual : manuals){
            String checkString;
            if (checkByBody) {
                 checkString = manual.getBody();
            }
            else checkString=manual.getPdfUrl();
            if (manualContainsWordFromDictionary(checkString,sureDeleteDictionary)) {
                sureDeleteManuals.add(manual);
                break;
            }
            if (manualContainsWordFromDictionary(checkString,checkDeleteDictionary)) {
                checkDeleteManuals.add(manual);
                break;
            }
        }

        ManualSerializer.saveSureDeleteManualsToFile(sureDeleteManuals);
        ManualSerializer.saveCheckDeleteManualsToFile(checkDeleteManuals);
    }

    //// TODO: 30.03.2016 write JavaDocs. Decide how to implement filtering not Open manuals.
}
