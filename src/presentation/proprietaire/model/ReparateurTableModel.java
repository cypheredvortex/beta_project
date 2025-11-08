package presentation.proprietaire.model;

import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;
import java.util.List;
import metier.model.Reparateur;

public class ReparateurTableModel extends AbstractTableModel {
    private final String[] columns = {"ID", "Nom", "Prénom", "Téléphone", "Email", "Salaire %", "Boutique", "Login"};
    private final List<Reparateur> rows = new ArrayList<>();

    @Override
    public int getRowCount() { return rows.size(); }

    @Override
    public int getColumnCount() { return columns.length; }

    @Override
    public String getColumnName(int column) { return columns[column]; }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Reparateur r = rows.get(rowIndex);
        return switch (columnIndex) {
            case 0 -> r.getId();
            case 1 -> r.getNom();
            case 2 -> r.getPrenom();
            case 3 -> r.getTelephone();
            case 4 -> r.getEmail();
            case 5 -> r.getSalairePourcentage();
            case 6 -> r.getBoutique() != null ? r.getBoutique().getNom() : "(aucune)";
            case 7 -> r.getCompte() != null ? r.getCompte().getLogin() : "(non défini)";
            default -> "";
        };
    }

    public void setData(List<Reparateur> data) {
        rows.clear();
        rows.addAll(data);
        fireTableDataChanged();
    }

    public Reparateur getAt(int row) { return rows.get(row); }
}
