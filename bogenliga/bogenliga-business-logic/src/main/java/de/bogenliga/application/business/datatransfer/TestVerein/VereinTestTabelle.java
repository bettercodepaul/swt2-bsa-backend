package de.bogenliga.application.business.datatransfer.TestVerein;

import java.util.HashSet;
import java.util.Set;
import de.bogenliga.application.business.datatransfer.model.VereinDBO;

/**
 * TODO [AL] class documentation
 *
 * @author Andre Lehnert, eXXcellent solutions consulting & software gmbh
 */
public class VereinTestTabelle {
    private Set<String> tempTable = new HashSet<>();
    private String newName;

    public VereinTestTabelle() {

        this.newName = VereinDBO.getVerein_name();

        processNewName();
    }

    private void processNewName() {
        if (newName != null) {
            if (!tempTable.contains(newName)) {
                tempTable.add(newName);
                System.out.println("Name '" + newName + "' hinzugefügt.");
            } else {
                System.out.println("Name '" + newName + "' bereits vorhanden.");
            }
        }
    }

        public Set<String> getTempTable() {
        return tempTable;
    }

    public static void main(String[] args) {

        VereinTestTabelle vereinTestTabelle = new VereinTestTabelle();

        Set<String> result = vereinTestTabelle.getTempTable();
        System.out.println("TempTable: " + result);
    }
}
