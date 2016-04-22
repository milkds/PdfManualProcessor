package PdfManualProcessor.service;

import PdfManualProcessor.Manual;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 * This class serialize and deserialize manuals.
 */
public class ManualSerializer {
    private static final Path RAW_DATA_FILE =Paths.get("src\\PdfManualProcessor\\res\\allManuals.txt");
    private static final Path DOWNLOADED_MANUAL_FILE =Paths.get("src\\PdfManualProcessor\\res\\downloadedManuals.txt");
    private static final Path SURE_DELETE_MANUALS =Paths.get("src\\PdfManualProcessor\\res\\sureDelete.txt");
    private static final Path CHECK_DELETE_MANUALS =Paths.get("src\\PdfManualProcessor\\res\\checkDelete.txt");
    private static final Path NOT_OPEN =Paths.get("src\\PdfManualProcessor\\res\\checkDelete.txt");


    public static void saveManualsToFile(List<Manual> manuals, Path filePath) {
        StringWriter stringWriter = new StringWriter();
        for (Manual manual : manuals){
            stringWriter.write(manual.getId());
            stringWriter.write("\t");
            stringWriter.write(manual.getPdfUrl());
            stringWriter.write("\t");
            stringWriter.write(manual.getSize()+"");
            stringWriter.write(System.lineSeparator());
        }

        try (FileWriter fileWriter = new FileWriter(filePath.toAbsolutePath().toFile(),true)) {
            fileWriter.write(stringWriter.toString());
            stringWriter.close();
        } catch (IOException e) {
           e.printStackTrace();
        }
    }
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


    public static void saveRawManualsToFile(List<Manual> manuals) {
        saveManualsToFile(manuals,RAW_DATA_FILE);
    }
    public static void saveSureDeleteManualsToFile(List<Manual> manuals) {
        saveManualsToFile(manuals,SURE_DELETE_MANUALS);
    }
    public static void saveCheckDeleteManualsToFile(List<Manual> manuals) {
        saveManualsToFile(manuals,CHECK_DELETE_MANUALS);
    }

    public static List<Manual> getManualsForFiltration(){
        List<Manual>result =getManualsFromFile(RAW_DATA_FILE);
        result.removeAll(getManualsFromFile(NOT_OPEN));
        result.removeAll(getManualsFromFile(SURE_DELETE_MANUALS));
        result.removeAll(getManualsFromFile(CHECK_DELETE_MANUALS));

        return result;
    }
    public static List<Manual> getManualsForDownload(){
        List<Manual> allManuals = getManualsFromFile(RAW_DATA_FILE);
        if (allManuals.size()==0)return new ArrayList<>();
        List<Manual> downloadedManuals = getManualsFromFile(DOWNLOADED_MANUAL_FILE);
        if(downloadedManuals.size()==0) return allManuals;
        allManuals.removeAll(downloadedManuals);

        return allManuals;
    }
    public static Path getRawDataFile() {
        return RAW_DATA_FILE;
    }
    public static Path getDownloadedManualFile() {
        return DOWNLOADED_MANUAL_FILE;
    }
    /**
     * to be deleted after class is complete.
     */
    public static void main(String[] args) {
        List<Manual> manuals = new ArrayList<>();
        manuals.add(new Manual("work","pdf"));
        manuals.add(new Manual("work2","pdf2"));
        manuals.add(new Manual("work3","pdf3"));
        saveRawManualsToFile(manuals);
    }


    // TODO:  implement exception processing. Write JavaDocs. CheckFileWriting for case with interrupted writing.
}
