package presentation.proprietaire.view;

import presentation.proprietaire.model.BoutiqueTableModel;

import javax.swing.*;
import java.awt.*;

public class GestionBoutiquePanel extends JPanel {

    private final JTable table;
    private final JButton btnAjouter;
    private final JButton btnModifier;
    private final JButton btnSupprimer;
    private final JButton btnRafraichir;

    public GestionBoutiquePanel(BoutiqueTableModel model) {
        setLayout(new BorderLayout());

        table = new JTable(model);
        table.setFillsViewportHeight(true); // better style
        JScrollPane scroll = new JScrollPane(table);

        btnRafraichir = new JButton("Rafraîchir");
        btnAjouter = new JButton("Ajouter");
        btnModifier = new JButton("Modifier");
        btnSupprimer = new JButton("Supprimer");

        JToolBar tb = new JToolBar();
        tb.add(btnRafraichir);
        tb.addSeparator();
        tb.add(btnAjouter);
        tb.add(btnModifier);
        tb.add(btnSupprimer);

        JPanel panel = new JPanel(new BorderLayout());
        panel.add(tb, BorderLayout.NORTH);
        panel.add(scroll, BorderLayout.CENTER);

        add(panel, BorderLayout.CENTER);
    }

    public JTable getTable() { return table; }
    public JButton getBtnAjouter() { return btnAjouter; }
    public JButton getBtnModifier() { return btnModifier; }
    public JButton getBtnSupprimer() { return btnSupprimer; }
    public JButton getBtnRafraichir() { return btnRafraichir; }
}
