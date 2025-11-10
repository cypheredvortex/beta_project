package presentation.proprietaire.controller;

import metier.model.Client;
import metier.model.Reparateur;
import metier.model.Reparation;
import metier.services.IReparationService;
import presentation.proprietaire.model.ReparationTableModel;
import presentation.proprietaire.view.ListeReparationsPanel;

import javax.swing.*;
import java.awt.*;
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
        view.getBtnVoirCode().addActionListener(e -> showCode());
        view.getBtnVoirAppareils().addActionListener(e -> voirAppareils());
        view.getBtnVoirClient().addActionListener(e -> voirClient());
        view.getBtnVoirReparateur().addActionListener(e -> voirReparateur());
    }

    private void load() {
        try {
            List<Reparation> data = service.listerReparations();
            model.setData(data);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(view, "Erreur lors du chargement: " + e.getMessage());
        }
    }

    private void showCode() {
        int row = view.getTable().getSelectedRow();
        if (row < 0) { JOptionPane.showMessageDialog(view, "Sélectionnez une réparation."); return; }
        Reparation r = model.getAt(row);
        JOptionPane.showMessageDialog(view, "Code unique: " + (r.getCodeUnique() == null ? "(non défini)" : r.getCodeUnique()));
    }

    private void voirAppareils() {
        int row = view.getTable().getSelectedRow();
        if (row < 0) { JOptionPane.showMessageDialog(view, "Sélectionnez une réparation."); return; }
        Reparation r = model.getAt(row);

        if (r.getAppareils() == null || r.getAppareils().isEmpty()) {
            JOptionPane.showMessageDialog(view, "Aucun appareil associé à cette réparation.");
            return;
        }

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < r.getAppareils().size(); i++) {
            var a = r.getAppareils().get(i);
            sb.append("Appareil ").append(i + 1).append(":\n")
              .append("Marque/Modèle: ").append(a.getMarque()).append(" ").append(a.getModele()).append("\n")
              .append("Type: ").append(a.getType()).append("\n")
              .append("N° Série: ").append(a.getNumeroSerie()).append("\n")
              .append("Problème: ").append(a.getProbleme()).append("\n")
              .append("Type Problème: ").append(a.getTypeProbleme()).append("\n")
              .append("--------------------------\n");
        }

        JTextArea textArea = new JTextArea(sb.toString());
        textArea.setEditable(false);
        JScrollPane scroll = new JScrollPane(textArea);
        scroll.setPreferredSize(new Dimension(400, 300));

        JOptionPane.showMessageDialog(view, scroll, "Appareils de la réparation", JOptionPane.INFORMATION_MESSAGE);
    }

    private void voirClient() {
        int row = view.getTable().getSelectedRow();
        if (row < 0) { JOptionPane.showMessageDialog(view, "Sélectionnez une réparation."); return; }
        Client c = model.getAt(row).getClient();
        if (c == null) { JOptionPane.showMessageDialog(view, "Aucun client associé."); return; }

        StringBuilder sb = new StringBuilder();
        sb.append("Nom: ").append(c.getNom()).append("\n")
          .append("Prénom: ").append(c.getPrenom()).append("\n")
          .append("Email: ").append(c.getEmail()).append("\n")
          .append("Téléphone: ").append(c.getTelephone()).append("\n");

        JTextArea textArea = new JTextArea(sb.toString());
        textArea.setEditable(false);
        JScrollPane scroll = new JScrollPane(textArea);
        scroll.setPreferredSize(new Dimension(400, 250));

        JOptionPane.showMessageDialog(view, scroll, "Détails du client", JOptionPane.INFORMATION_MESSAGE);
    }

    private void voirReparateur() {
        int row = view.getTable().getSelectedRow();
        if (row < 0) { JOptionPane.showMessageDialog(view, "Sélectionnez une réparation."); return; }
        Reparateur r = model.getAt(row).getReparateur();
        if (r == null) { JOptionPane.showMessageDialog(view, "Aucun réparateur associé."); return; }

        StringBuilder sb = new StringBuilder();
        sb.append("Nom: ").append(r.getNom()).append("\n")
          .append("Prénom: ").append(r.getPrenom()).append("\n")
          .append("Email: ").append(r.getEmail()).append("\n")
          .append("Téléphone: ").append(r.getTelephone()).append("\n")
          .append("Salaire pourcentage: ").append(r.getSalairePourcentage()).append("\n")
          .append("Boutique: ").append(r.getBoutique() != null ? r.getBoutique().getNom() : "N/A").append("\n");

        JTextArea textArea = new JTextArea(sb.toString());
        textArea.setEditable(false);
        JScrollPane scroll = new JScrollPane(textArea);
        scroll.setPreferredSize(new Dimension(400, 250));

        JOptionPane.showMessageDialog(view, scroll, "Détails du réparateur", JOptionPane.INFORMATION_MESSAGE);
    }
}
