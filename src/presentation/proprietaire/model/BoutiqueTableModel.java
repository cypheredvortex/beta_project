package presentation.proprietaire.model;

import metier.model.Boutique;

import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;
import java.util.List;

public class BoutiqueTableModel extends AbstractTableModel {

    private final String[] columns = {"ID", "Nom", "Adresse", "Num Patente", "Propriétaire"};
    private final List<Boutique> rows = new ArrayList<>();

    @Override
    public int getRowCount() {
        return rows.size();
    }

    @Override
    public int getColumnCount() {
        return columns.length;
    }

    @Override
    public String getColumnName(int column) {
        return columns[column];
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Boutique b = rows.get(rowIndex);
        return switch (columnIndex) {
            case 0 -> b.getId();
            case 1 -> b.getNom();
            case 2 -> b.getAddresse();
            case 3 -> b.getNumPatente();
            case 4 -> b.getProprietaire() != null ? b.getProprietaire().getNom() : "—";
            default -> "";
        };
    }

    public void setData(List<Boutique> data) {
        rows.clear();
        rows.addAll(data);
        fireTableDataChanged();
    }

    public Boutique getAt(int row) {
        return rows.get(row);
    }
}
