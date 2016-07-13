package PdfManualProcessor.service;

import PdfManualProcessor.Manual;

import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * This class serializes and deserialize manuals.
 */
public class ManualSerializer {
    private static final Path RAW_DATA_FILE =Paths.get("PdfManualProcessor\\res\\allManuals.txt");
    private static final Path DOWNLOADED_MANUAL_FILE =Paths.get("PdfManualProcessor\\res\\downloadedManuals.txt");
    private static final Path SURE_DELETE_MANUALS =Paths.get("PdfManualProcessor\\res\\sureDelete.txt");
    private static final Path CHECK_DELETE_MANUALS =Paths.get("PdfManualProcessor\\res\\checkDelete.txt");
    private static final Path NOT_OPEN =Paths.get("PdfManualProcessor\\res\\notOpen.txt");
    private static final Path OPENED_MANUALS =Paths.get("PdfManualProcessor\\res\\processedManuals.txt");

    private static final Path DELETE_AFTER_CHECK =Paths.get("PdfManualProcessor\\res\\deleteAfterCheck.txt");
    private static final Path CHECKED_MANUALS_TO_KEEP = Paths.get("PdfManualProcessor\\res\\checkedManualsToKeep.txt");

    /**
     * Saves manuals to specified file.
     * @param manuals - list of manuals to save.
     * @param filePath - file, to which we save manuals.
     */
    public static void saveManualsToFile(List<Manual> manuals, Path filePath) {
        StringWriter stringWriter = new StringWriter();
        //Transforming manuals to text. Saving necessary parameters (divide them by tabulation).
        //Manuals divided from each other by lineSeparator.
        for (Manual manual : manuals){
            stringWriter.write(manual.getId());
            stringWriter.write("\t");
            stringWriter.write(manual.getPdfUrl());
            stringWriter.write("\t");
            stringWriter.write(manual.getSize()+"");
            stringWriter.write(System.lineSeparator());
        }

        //Writing manual text to specified file.
        try (FileWriter fileWriter = new FileWriter(filePath.toAbsolutePath().toFile(),true)) {
            fileWriter.write(stringWriter.toString());
            stringWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Reads manuals from specified file.
     * @param filePath - file to read manuals from.
     * @return - List of manuals, read from file.
     */
    private static List<Manual> getManualsFromFile (Path filePath){
        List<Manual> result = new ArrayList<>();
        try(BufferedReader fileReader = new BufferedReader(new FileReader(filePath.toAbsolutePath().toFile())) ){
            while (fileReader.ready()){
                String line = fileReader.readLine();
                String[]split = line.split("\t");
                result.add(new Manual(split[0],split[1],Integer.parseInt(split[2])));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * @return all manuals, which are not processed in system.
     */
    public static List<Manual> getAllManualsFromFile(){
        return getManualsFromFile(RAW_DATA_FILE);
    }

    /**
     * @param manuals - Manuals which are filtered as to be deleted for sure.
     */
    public static void saveSureDeleteManualsToFile(List<Manual> manuals) {
        synchronized (SURE_DELETE_MANUALS) {
            saveManualsToFile(manuals, SURE_DELETE_MANUALS);
        }
    }

    /**
     * @param manuals- Manuals which are filtered as to be deleted after visual check.
     */
    public static void saveCheckDeleteManualsToFile(List<Manual> manuals) {
        synchronized (CHECK_DELETE_MANUALS) {
            saveManualsToFile(manuals, CHECK_DELETE_MANUALS);
        }
    }

    /**
     * @param manuals - Manuals which were opened in browser (Should be processed by user.)
     */
    public static void saveOpenedManualsToFile(List<Manual> manuals){
        saveManualsToFile(manuals,OPENED_MANUALS);
    }

    /**
     * @param manuals - Manuals checked visually by user and marked as possibly approvable.
     *                (And so they must be kept for future processing)
     */
    public static void saveKeepManualsToFile(List<Manual> manuals){
        saveManualsToFile(manuals,CHECKED_MANUALS_TO_KEEP);
    }

    /**
     * @param manuals - Manuals checked visually by user and marked as such, that must be
     *                deleted.
     */
    public static void saveDeleteAfterCheckManualsToFile(List<Manual> manuals){
        saveManualsToFile(manuals,DELETE_AFTER_CHECK);
    }

    /**
     * @param manuals - Manuals which had already been downloaded.
     */
    public static void saveDownloadedManualsToFile(List<Manual> manuals){
        synchronized (DOWNLOADED_MANUAL_FILE) {
            saveManualsToFile(manuals, DOWNLOADED_MANUAL_FILE);
        }
    }

    /**
     *  Clears file with all manuals and writes new manuals there.
     * @param manuals - Manuals to be written.
     */
    public static void refreshRawManualFile(List<Manual> manuals){
        //Clearing file.
        try( PrintWriter pw = new PrintWriter(RAW_DATA_FILE.toFile())) {
        } catch (FileNotFoundException ignored) {
        }

        //Saving manuals.
        saveManualsToFile(manuals,RAW_DATA_FILE);
    }

    /**
     * @return List of Manuals for text filtration.
     */
    public static List<Manual> getManualsForFiltration(){
        //Getting all manuals.
        List<Manual>result =getManualsFromFile(RAW_DATA_FILE);

        //Removing those, that are not open.
        result.removeAll(getManualsFromFile(NOT_OPEN));

        //Removing manuals which have been already filtered.
        result.removeAll(getManualsFromFile(SURE_DELETE_MANUALS));
        result.removeAll(getManualsFromFile(CHECK_DELETE_MANUALS));
        result.removeAll(getManualsFromFile(DELETE_AFTER_CHECK));

        //Removing manuals which were already processed by user.
        result.removeAll(getManualsFromFile(OPENED_MANUALS));

        return result;
    }

    /**
     * @return Manuals, filtered as to be deleted for sure.
     */
    public static List<Manual> getManualsForSureDeleteCheck(){
        return getManualsFromFile(SURE_DELETE_MANUALS);
    }

    /**
     * @return Manuals, filtered as to be deleted after visual check by user.
     */
    public static List<Manual> getManualsForPossiblyDeleteCheck(){
        return getManualsFromFile(CHECK_DELETE_MANUALS);
    }

    /**
     * @return Manuals to be downloaded.
     */
    public static List<Manual> getManualsForDownload(){
        //Getting all manuals.
        List<Manual> allManuals = getManualsFromFile(RAW_DATA_FILE);

        //Checking if there any manuals in program (for case if user haven't pressed refresh manuals button.
        if (allManuals.size()==0)return new ArrayList<>();

        //Getting manuals which were already downloaded.
        List<Manual> downloadedManuals = getManualsFromFile(DOWNLOADED_MANUAL_FILE);

        //Checking if there any downloaded manuals at all (to avoid NPE).
        if(downloadedManuals.size()==0) return allManuals;

        //Removing manuals, which were already downloaded from list of all manuals.
        allManuals.removeAll(downloadedManuals);

        return allManuals;
    }

    /**
     * @return Manuals to be opened in browser for processing by user.
     */
    public static List<Manual> getManualsForOpening(){
        //Getting all manuals.
        List<Manual> result = getAllManualsFromFile();

        //Getting already processed manuals.
        List<Manual> processedManuals = getManualsFromFile(OPENED_MANUALS);

        //Removing already processed manuals from list of all manuals.
        result.removeAll(processedManuals);

        return result;
    }

    /**
     * @return Manuals which were opened previously in browser,
     * but not processed by user (or any other purpose for which user
     * will need to open previously opened manuals).
     * Manuals returned in reverse order (means that manuals opened last - will be opened first).
     */
    public static List<Manual> getManualsForReopening(){
        List<Manual> result = getManualsFromFile(OPENED_MANUALS);
        Collections.reverse(result);
        return result;
    }

    /**
     * @return Manuals visually checked by user and marked as possibly approvable.
     */
    public static List<Manual> getKeepManuals(){
        return getManualsFromFile(CHECKED_MANUALS_TO_KEEP);
    }

    /**
     * @return Manuals visually checked by user and marked as to be deleted.
     */
    public static List<Manual> getDeleteAfterCheckManuals(){
        return getManualsFromFile(DELETE_AFTER_CHECK);
    }

    /**
     * Gets manuals by their IDs.
     * @param manualsIds - array of manuals IDs.
     * @return List of manuals, which IDs are present in transferred array.
     */
    public static List<Manual> getManualById(String[] manualsIds){
        //Getting all manuals.
        List<Manual> manuals = getAllManualsFromFile();

        //Checking each manual from list - if its ID equals to ID in array.
        List<Manual> result = new ArrayList<>();
        for (String id : manualsIds){
            for (Manual m : manuals ){
                if (id.equals(m.getId())){
                    result.add(m);
                    break;
                }
            }
        }

        return result;
    }


    // TODO:  implement exception processing. CheckFileWriting for case with interrupted writing.
    // TODO:  check access level.
}
