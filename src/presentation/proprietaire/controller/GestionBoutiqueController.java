package presentation.proprietaire.controller;

import presentation.proprietaire.model.BoutiqueTableModel;
import presentation.proprietaire.view.GestionBoutiquePanel;
import metier.model.Boutique;
import metier.model.Proprietaire;
import metier.services.IBoutiqueService;

import javax.swing.*;
import java.util.List;

public class GestionBoutiqueController {

    private final GestionBoutiquePanel view;
    private final BoutiqueTableModel model;
    private final IBoutiqueService service;

    public GestionBoutiqueController(GestionBoutiquePanel view, BoutiqueTableModel model, IBoutiqueService service) {
        this.view = view;
        this.model = model;
        this.service = service;
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
            if (row < 0) {
                JOptionPane.showMessageDialog(view, "Sélectionnez une boutique.");
                return;
            }
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
            if (row < 0) {
                JOptionPane.showMessageDialog(view, "Sélectionnez une boutique.");
                return;
            }
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
    }

    private void load() {
        try {
            List<Boutique> data = service.listerBoutiques();
            model.setData(data);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(view, "Erreur lors du chargement : " + ex.getMessage());
        }
    }

    // Simple dialog for editing/creating Boutique
    private Boutique editBoutiqueDialog(Boutique b) {
        JTextField tfNom = new JTextField(b != null ? b.getNom() : "");
        JTextField tfAdresse = new JTextField(b != null ? b.getAddresse() : "");
        JTextField tfNumPatente = new JTextField(b != null ? b.getNumPatente() : "");
        JTextField tfProprio = new JTextField(b != null && b.getProprietaire() != null ? b.getProprietaire().getNom() : "");

        Object[] message = {
            "Nom:", tfNom,
            "Adresse:", tfAdresse,
            "Num Patente:", tfNumPatente,
            "Propriétaire:", tfProprio
        };

        int option = JOptionPane.showConfirmDialog(view, message, b == null ? "Ajouter Boutique" : "Modifier Boutique", JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION) {
            if (b == null) b = new Boutique();
            b.setNom(tfNom.getText());
            b.setAddresse(tfAdresse.getText());
            b.setNumPatente(tfNumPatente.getText());

            Proprietaire p = new Proprietaire();
            p.setNom(tfProprio.getText());
            b.setProprietaire(p);

            return b;
        }
        return null;
    }
}
