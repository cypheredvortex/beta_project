package presentation.proprietaire.view;

import metier.model.Compte;
import metier.services.IBoutiqueService;
import metier.services.IReparateurService;
import metier.services.IReparationService;
import metier.servicesImpl.BoutiqueServiceImpl;
import metier.servicesImpl.ReparateurServiceImpl;
import metier.servicesImpl.ReparationServiceImpl;
import presentation.proprietaire.controller.CaisseGlobaleController;
import presentation.proprietaire.controller.GestionBoutiqueController;
import presentation.proprietaire.controller.GestionReparateursController;
import presentation.proprietaire.controller.ListeReparationsController;
import presentation.proprietaire.model.*;
import javax.swing.*;
import java.awt.*;

public class DashboardProprietaireFrame extends JFrame {

    private final CardLayout card = new CardLayout();
    private final JPanel root = new JPanel(card);

    public DashboardProprietaireFrame(Compte compte) {
        super("Dashboard Propriétaire - " + compte.getLogin());
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1150, 700);
        setLocationRelativeTo(null);

        // =====================================================
        // 1️⃣ SERVICES
        // =====================================================
        IReparateurService reparateurService = new ReparateurServiceImpl();
        IBoutiqueService boutiqueService = new BoutiqueServiceImpl();
        IReparationService reparationService = new ReparationServiceImpl();

        // =====================================================
        // 2️⃣ RÉPARATEURS
        // =====================================================
        ReparateurTableModel reparateurTableModel = new ReparateurTableModel();
        GestionReparateursPanel gestionReparateurs = new GestionReparateursPanel(reparateurTableModel);
        new GestionReparateursController(gestionReparateurs, reparateurTableModel, reparateurService);

        // =====================================================
        // 3️⃣ BOUTIQUE
        // =====================================================
        BoutiqueTableModel boutiqueTableModel = new BoutiqueTableModel();
        GestionBoutiquePanel gestionBoutique = new GestionBoutiquePanel(boutiqueTableModel);
        new GestionBoutiqueController(gestionBoutique, boutiqueTableModel, boutiqueService);

        // =====================================================
        // 4️⃣ CAISSE GLOBALE
        // =====================================================
        CaisseTableModel caisseTableModel = new CaisseTableModel();
        CaisseGlobalePanel caisseGlobalePanel = new CaisseGlobalePanel(caisseTableModel);
        new CaisseGlobaleController(caisseGlobalePanel, caisseTableModel);

        // =====================================================
        // 5️⃣ RÉPARATIONS
        // =====================================================
        ReparationTableModel reparationTableModel = new ReparationTableModel();
        ListeReparationsPanel listeReparations = new ListeReparationsPanel(reparationTableModel);
        new ListeReparationsController(listeReparations, reparationTableModel, reparationService); // 🔥 Contrôleur connecté

        // =====================================================
        // 🧭 MENU LATÉRAL
        // =====================================================
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

        // =====================================================
        // 🧩 PANELS PRINCIPAUX
        // =====================================================
        root.add(gestionReparateurs, "reparateurs");
        root.add(gestionBoutique, "boutique");
        root.add(caisseGlobalePanel, "caisse");
        root.add(listeReparations, "reparations");

        // =====================================================
        // ⚙️ LAYOUT GLOBAL
        // =====================================================
        getContentPane().setLayout(new BorderLayout());
        JScrollPane left = new JScrollPane(menu);
        left.setPreferredSize(new Dimension(200, 0));
        getContentPane().add(left, BorderLayout.WEST);
        getContentPane().add(root, BorderLayout.CENTER);

        // =====================================================
        // 🎬 AFFICHAGE PAR DÉFAUT
        // =====================================================
        card.show(root, "reparateurs");
        setVisible(true);
    }
}
