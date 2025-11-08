package presentation.proprietaire.view;

import metier.model.Compte;
import presentation.proprietaire.controller.CaisseGlobaleController;
import presentation.proprietaire.model.BoutiqueTableModel;
import presentation.proprietaire.model.CaisseTableModel;
import presentation.proprietaire.model.ReparateurTableModel;
import presentation.proprietaire.model.ReparationTableModel;

import javax.swing.*;
import java.awt.*;

public class DashboardProprietaireFrame extends JFrame {
    private final CardLayout card = new CardLayout();
    private final JPanel root = new JPanel(card);

    public DashboardProprietaireFrame(Compte compte) {
        super("Dashboard Propriétaire - " + compte.getLogin());
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1100, 700);
        setLocationRelativeTo(null);

        // === PANELS ===

        // 1️⃣ Réparateurs
        ReparateurTableModel reparateurTableModel = new ReparateurTableModel();
        GestionReparateursPanel gestionReparateurs = new GestionReparateursPanel(reparateurTableModel);
        // TODO: attach controller if needed

        // 2️⃣ Boutique
        BoutiqueTableModel boutiqueTableModel = new BoutiqueTableModel();
        GestionBoutiquePanel gestionBoutique = new GestionBoutiquePanel(boutiqueTableModel);
        // TODO: attach controller if needed

        // 3️⃣ Caisse
        CaisseTableModel caisseTableModel = new CaisseTableModel();
        CaisseGlobalePanel caisseGlobalePanel = new CaisseGlobalePanel(caisseTableModel);
        // Attach controller
        new CaisseGlobaleController(caisseGlobalePanel, caisseTableModel);

        // 4️⃣ Réparations
        ReparationTableModel reparationTableModel = new ReparationTableModel();
        ListeReparationsPanel listeReparations = new ListeReparationsPanel(reparationTableModel);
        // TODO: attach controller if needed

        // === MENU ===
        String[] options = {"Réparateurs", "Boutique", "Caisse", "Réparations"};
        JList<String> menu = new JList<>(options);
        menu.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        menu.setSelectedIndex(0);

        menu.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                switch (menu.getSelectedIndex()) {
                    case 0 -> card.show(root, "reparateurs");
                    case 1 -> card.show(root, "boutique");
                    case 2 -> card.show(root, "caisse");
                    case 3 -> card.show(root, "reparations");
                }
            }
        });

        // === ADD PANELS TO ROOT ===
        root.add(gestionReparateurs, "reparateurs");
        root.add(gestionBoutique, "boutique");
        root.add(caisseGlobalePanel, "caisse");
        root.add(listeReparations, "reparations");

        // === LAYOUT ===
        getContentPane().setLayout(new BorderLayout());
        JScrollPane left = new JScrollPane(menu);
        left.setPreferredSize(new Dimension(200, 0));
        getContentPane().add(left, BorderLayout.WEST);
        getContentPane().add(root, BorderLayout.CENTER);

        // Show default
        card.show(root, "reparateurs");

        setVisible(true);
    }
}
