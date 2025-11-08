package presentation.proprietaire.model;

import javax.swing.table.AbstractTableModel;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import metier.model.Transaction;

public class CaisseTableModel extends AbstractTableModel {
    private final String[] columns = {"ID", "Date", "Type", "Montant", "Description", "Contrepartie"};
    private final List<Transaction> rows = new ArrayList<>();
    private final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

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
        Transaction t = rows.get(rowIndex);
        return switch (columnIndex) {
            case 0 -> t.getId();
            case 1 -> t.getDate() == null ? "" : sdf.format(t.getDate());
            case 2 -> t.getType() == null ? "" : t.getType().name();
            case 3 -> t.getMontant();
            case 4 -> t.getDescription();
            case 5 -> t.getContrepartie();
            default -> "";
        };
    }

    public void setData(List<Transaction> data) {
        rows.clear();
        if (data != null) rows.addAll(data);
        fireTableDataChanged();
    }

    public Transaction getAt(int row) {
        return rows.get(row);
    }
}
