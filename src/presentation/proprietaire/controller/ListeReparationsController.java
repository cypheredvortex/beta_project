package presentation.proprietaire.controller;

import metier.model.Reparation;
import metier.enums.EtatReparation;
import metier.services.IReparationService;
import presentation.proprietaire.model.ReparationTableModel;
import presentation.proprietaire.view.ListeReparationsPanel;

import javax.swing.*;
import java.util.List;

public class ListeReparationsController {

    private final ListeReparationsPanel view;
    private final ReparationTableModel model;
    private final IReparationService service;

    public ListeReparationsController(ListeReparationsPanel view, ReparationTableModel model, IReparationService service) {
        this.view = view;
        this.model = model;
        this.service = service;
        wire();
        load();
    }

    private void wire() {
        view.getBtnRafraichir().addActionListener(e -> load());
        view.getBtnAjouter().addActionListener(e -> ajouter());
        view.getBtnChangerEtat().addActionListener(e -> changeEtat());
        view.getBtnVoirCode().addActionListener(e -> showCode());
    }

    private void load() {
        try {
            List<Reparation> data = service.listerReparations();
            model.setData(data);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(view, "Erreur lors du chargement: " + e.getMessage());
        }
    }

    private void changeEtat() {
        int row = view.getTable().getSelectedRow();
        if (row < 0) { JOptionPane.showMessageDialog(view, "Sélectionnez une réparation."); return; }
        Reparation r = model.getAt(row);
        EtatReparation etat = (EtatReparation) JOptionPane.showInputDialog(
                view,
                "Choisir un nouvel état",
                "Changer état",
                JOptionPane.PLAIN_MESSAGE,
                null,
                EtatReparation.values(),
                r.getStatut()
        );
        if (etat != null) {
            try {
                service.changerStatut(r.getId(), etat);
                load();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(view, "Erreur: " + ex.getMessage());
            }
        }
    }

    private void ajouter() {
        // Using standard JOptionPane input for demo (replace with a real form if needed)
        String description = JOptionPane.showInputDialog(view, "Description de la réparation:");
        if (description == null || description.isEmpty()) return;

        Reparation form = new Reparation();
        form.setDescription(description);

        try {
            Reparation created = service.enregistrerReparation(form);
            load();
            JOptionPane.showMessageDialog(view, "Réparation créée. Code: " + created.getCodeUnique());
        } catch (Exception e) {
            JOptionPane.showMessageDialog(view, "Erreur création: " + e.getMessage());
        }
    }

    private void showCode() {
        int row = view.getTable().getSelectedRow();
        if (row < 0) { JOptionPane.showMessageDialog(view, "Sélectionnez une réparation."); return; }
        Reparation r = model.getAt(row);
        String code = r.getCodeUnique();
        JOptionPane.showMessageDialog(view, "Code unique: " + (code == null ? "(non défini)" : code));
    }
}
