package PdfManualProcessor.service;

import PdfManualProcessor.Manual;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringWriter;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 * This class serialize and deserialize manuals.
 */
public class ManualSerializer {
    private static final Path RAW_DATA_FILE =Paths.get("src\\PdfManualProcessor\\res\\allManuals.txt");
    private static final Path SURE_DELETE_MANUALS =Paths.get("src\\PdfManualProcessor\\res\\sureDelete.txt");
    private static final Path CHECK_DELETE_MANUALS =Paths.get("src\\PdfManualProcessor\\res\\checkDelete.txt");
    private static final Path NOT_OPEN =Paths.get("src\\PdfManualProcessor\\res\\checkDelete.txt");


    public static void saveManualsToFile(List<Manual> manuals, Path filePath) {
        StringWriter stringWriter = new StringWriter();
        for (Manual manual : manuals){
            stringWriter.write(manual.getId());
            stringWriter.write("\t");
            stringWriter.write(manual.getPdfUrl());
            stringWriter.write(System.lineSeparator());
        }

        try (FileWriter fileWriter = new FileWriter(filePath.toAbsolutePath().toFile())) {
            fileWriter.write(stringWriter.toString());
            stringWriter.close();
        } catch (IOException e) {
           e.printStackTrace();
        }
    }
    public static List<Manual> readManualsFromFile (Path filePath){
        return null;
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


    // TODO:  decide and implement methods
}
