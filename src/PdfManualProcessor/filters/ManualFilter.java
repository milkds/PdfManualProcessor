package PdfManualProcessor.filters;

import PdfManualProcessor.Manual;

import java.util.List;

public abstract class ManualFilter {

    public static void filterNotOpenManuals (List<Manual> downloadedManuals){}
    public static void filterManualByBody(String manualBody, List<String> dictionary){}
    public static void filterManualsByUrl(List<Manual> manualsForCheckByUrl, List<String> dictionary){}

}
