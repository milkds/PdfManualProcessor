package PdfManualProcessor.view.strategy;

import PdfManualProcessor.Manual;
import PdfManualProcessor.Model;
import PdfManualProcessor.service.ManualSerializer;

import java.util.List;

/**
 * This class is used, when User checks all downloaded Manuals
 */

public class AllDeleteStrategy implements Strategy {

    @Override
    public String[] getManualList() {
        List<Manual> manuals = ManualSerializer.getManualsForFiltration();
        int size = manuals.size();
        String [] result = new String[size];
        for (int i = 0; i <size ; i++) {
            result[i]=manuals.get(i).getId();
        }
        return result;
    }

    @Override
    public void onRemove(String id) {
        String[]temp= {id};
        ManualSerializer.saveDeleteAfterCheckManualsToFile(ManualSerializer.getManualById(temp));
    }

    @Override
    public void onRemoveAll() {
        List<Manual> checkedManuals = ManualSerializer.getDeleteAfterCheckManuals();
        Model.deleteManualsInConsole(checkedManuals);
    }


    //todo: Find more correct name for this class.
}
