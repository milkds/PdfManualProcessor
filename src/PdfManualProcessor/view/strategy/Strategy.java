package PdfManualProcessor.view.strategy;

/**
 * This interface is used for setting up LongActionView behaviour.
 */

public interface Strategy {
    /**
     * @return Array of Manuals' IDs for JList.
     */
    String[] getManualList();

    /**
     * Removes Manual from JList.
     * @param id - Manual's ID
     */
    void onRemove(String id);

    /**
     * Removes selected manuals from system.
     */
    void onRemoveAll();

    //todo: Think about implementation onRemove() method in one place.
}
