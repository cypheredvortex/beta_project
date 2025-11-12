package presentation.proprietaire.model;

import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;
import java.util.List;
import metier.model.Caisse;

public class CaisseTableModel extends AbstractTableModel {

    private final String[] columns = {"ID", "Solde", "Type", "Réparateur"};
    private List<Caisse> data = new ArrayList<>();

    public void setData(List<Caisse> caisses) {
        this.data = caisses;
        fireTableDataChanged();
    }

    public Caisse getAt(int row) {
        return data.get(row);
    }

    @Override
    public int getRowCount() {
        return data.size();
    }

    @Override
    public int getColumnCount() {
        return columns.length;
    }

    @Override
    public String getColumnName(int col) {
        return columns[col];
    }

    @Override
    public Object getValueAt(int row, int col) {
        Caisse c = data.get(row);
        switch (col) {
            case 0:
                return c.getId();
            case 1:
                return c.getSoldeActuel();
            case 2:
                return c.getType() != null ? c.getType().name() : "";
            case 3:
                if (c.getReparateur() != null) {
                    String nom = c.getReparateur().getNom() != null ? c.getReparateur().getNom() : "";
                    String prenom = c.getReparateur().getPrenom() != null ? c.getReparateur().getPrenom() : "";
                    // Combine both with a space, cleanly
                    return (nom + " " + prenom).trim();
                } else {
                    return "";
                }
            default:
                return "";
        }
    }
}
