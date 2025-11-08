package presentation.proprietaire.model;

import metier.model.Reparation;
import metier.model.Appareil;
import metier.enums.EtatReparation;

import javax.swing.table.AbstractTableModel;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ReparationTableModel extends AbstractTableModel {
    private final String[] columns = {"ID", "Client", "Appareils", "État", "Date création", "Code unique", "Prix total"};
    private final List<Reparation> rows = new ArrayList<>();
    private final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");

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
        Reparation r = rows.get(rowIndex);
        return switch (columnIndex) {
            case 0 -> r.getId();
            case 1 -> r.getClient() == null ? "" : r.getClient().getNom() + " " + r.getClient().getPrenom();
            case 2 -> r.getAppareils() == null ? "" : r.getAppareils().stream()
                        .map(Appareil::getMarque)
                        .collect(Collectors.joining(", "));
            case 3 -> r.getStatut() == null ? "" : r.getStatut();
            case 4 -> r.getDateCreation() == null ? "" : sdf.format(r.getDateCreation());
            case 5 -> r.getCodeUnique();
            case 6 -> r.getPrixConvenu() + r.getPrixTotalPieces();
            default -> "";
        };
    }

    public void setData(List<Reparation> data) {
        rows.clear();
        if (data != null) rows.addAll(data);
        fireTableDataChanged();
    }

    public Reparation getAt(int row) {
        return rows.get(row);
    }
}
