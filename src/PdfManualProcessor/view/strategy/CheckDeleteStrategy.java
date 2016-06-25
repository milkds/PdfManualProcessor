package PdfManualProcessor.view.strategy;

import PdfManualProcessor.Manual;
import PdfManualProcessor.Model;
import PdfManualProcessor.service.ManualSerializer;

import java.util.List;

public class CheckDeleteStrategy implements Strategy {
    @Override
    public String[] getManualList() {
        List<Manual> manuals = ManualSerializer.getManualsForPossiblyDeleteCheck();
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
        ManualSerializer.saveKeepManualsToFile(ManualSerializer.getManualById(temp));
    }

    @Override
    public void onRemoveAll() {
        //getting manuals, which we marked as they possibly can be approved
        List<Manual> checkedManuals = ManualSerializer.getKeepManuals();

        //getting manuals which we've previously filtered as to be deleted for sure
        List<Manual> allManuals = ManualSerializer.getManualById(getManualList());

        //remove from delete list, manuals which can be possibly approved
        allManuals.removeAll(checkedManuals);

        //delete all manuals left from server
        Model.deleteManualsInConsole(allManuals);
    }
}
