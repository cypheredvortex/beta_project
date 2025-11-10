package presentation.proprietaire.controller;


import presentation.proprietaire.model.BoutiqueTableModel;

import presentation.proprietaire.view.GestionBoutiquePanel;
import metier.model.Boutique;
import metier.model.Proprietaire;
import metier.model.Reparateur;
import metier.services.IBoutiqueService;
import dao.interfaces.ReparateurDao;
import dao.impl.ReparateurDaoImpl;
import dao.impl.ProprietaireDaoImpl;

import javax.swing.*;
import java.util.List;

public class GestionBoutiqueController {

    private final GestionBoutiquePanel view;
    private final BoutiqueTableModel model;
    private final IBoutiqueService service;
    private final ReparateurDao reparateurDao;
    private final ProprietaireDaoImpl proprietaireDao;

    public GestionBoutiqueController(GestionBoutiquePanel view,
                                     BoutiqueTableModel model,
                                     IBoutiqueService service) {
        this.view = view;
        this.model = model;
        this.service = service;
        this.reparateurDao = new ReparateurDaoImpl();
        this.proprietaireDao = new ProprietaireDaoImpl();
        wire();
        load();
    }

    private void wire() {
        view.getBtnRafraichir().addActionListener(e -> load());

        view.getBtnAjouter().addActionListener(e -> {
            Boutique b = editBoutiqueDialog(null);
            if (b != null) {
                try {
                    service.creerBoutique(b);
                    load();
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(view, ex.getMessage());
                }
            }
        });

        view.getBtnModifier().addActionListener(e -> {
            int row = view.getTable().getSelectedRow();
            if (row < 0) { JOptionPane.showMessageDialog(view, "Sélectionnez une boutique."); return; }
            Boutique sel = model.getAt(row);
            Boutique edited = editBoutiqueDialog(sel);
            if (edited != null) {
                try {
                    service.modifierBoutique(edited);
                    load();
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(view, ex.getMessage());
                }
            }
        });

        view.getBtnSupprimer().addActionListener(e -> {
            int row = view.getTable().getSelectedRow();
            if (row < 0) { JOptionPane.showMessageDialog(view, "Sélectionnez une boutique."); return; }
            Boutique sel = model.getAt(row);
            if (JOptionPane.showConfirmDialog(view, "Supprimer cette boutique ?") == JOptionPane.YES_OPTION) {
                try {
                    service.supprimerBoutique((int) sel.getId());
                    load();
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(view, ex.getMessage());
                }
            }
        });

        view.getBtnVoirReparateurs().addActionListener(e -> {
            int row = view.getTable().getSelectedRow();
            if (row < 0) { JOptionPane.showMessageDialog(view, "Sélectionnez une boutique."); return; }
            Boutique sel = model.getAt(row);

            try {
                List<Reparateur> reparateurs = reparateurDao.findAll().stream()
                        .filter(r -> r.getBoutique() != null && r.getBoutique().getId() == sel.getId())
                        .toList();

                String msg = reparateurs.isEmpty() ? "Aucun réparateur." :
                        reparateurs.stream()
                                   .map(r -> r.getNom() + " " + r.getPrenom())
                                   .reduce("", (a,b) -> a + "\n" + b);

                JOptionPane.showMessageDialog(view, msg);

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(view, "Erreur: " + ex.getMessage());
            }
        });
    }

    private void load() {
        try {
            List<Boutique> data = service.listerBoutiques();
            model.setData(data);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(view, "Erreur lors du chargement : " + ex.getMessage());
        }
    }

    // Ajout/modification boutique avec choix du propriétaire
    private Boutique editBoutiqueDialog(Boutique b) {
        JTextField tfNom = new JTextField(b != null ? b.getNom() : "");
        JTextField tfAdresse = new JTextField(b != null ? b.getAddresse() : "");
        JTextField tfLogo = new JTextField(b != null ? b.getLogoPath() : "");
        JTextField tfNumPatente = new JTextField(b != null ? b.getNumPatente() : "");

        // Charger tous les propriétaires pour selection
        List<Proprietaire> proprietaires;
        try {
            proprietaires = proprietaireDao.findAll();
        } catch (Exception e) {
            proprietaires = List.of();
        }
        String[] propNames = proprietaires.stream()
                .map(p -> p.getNom() + " " + p.getPrenom())
                .toArray(String[]::new);
        JComboBox<String> cbProprio = new JComboBox<>(propNames);
        if (b != null && b.getProprietaire() != null) {
            for (int i = 0; i < proprietaires.size(); i++) {
                if (proprietaires.get(i).getId() == b.getProprietaire().getId()) {
                    cbProprio.setSelectedIndex(i);
                    break;
                }
            }
        }

        Object[] message = {
            "Nom:", tfNom,
            "Adresse:", tfAdresse,
            "Logo Path:", tfLogo,
            "Num Patente:", tfNumPatente,
            "Propriétaire:", cbProprio
        };

        int option = JOptionPane.showConfirmDialog(view, message,
                b == null ? "Ajouter Boutique" : "Modifier Boutique",
                JOptionPane.OK_CANCEL_OPTION);

        if (option == JOptionPane.OK_OPTION) {
            if (b == null) b = new Boutique();
            b.setNom(tfNom.getText());
            b.setAddresse(tfAdresse.getText());
            b.setLogoPath(tfLogo.getText());
            b.setNumPatente(tfNumPatente.getText());
            if (!proprietaires.isEmpty()) b.setProprietaire(proprietaires.get(cbProprio.getSelectedIndex()));
            return b;
        }
        return null;
    }
}
