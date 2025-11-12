package presentation.proprietaire.controller;

import metier.model.Client;
import metier.model.Reparateur;
import metier.model.Reparation;
import metier.services.IReparationService;
import presentation.proprietaire.model.ReparationTableModel;
import presentation.proprietaire.view.ListeReparationsPanel;

import javax.swing.*;
import java.awt.*;
import java.io.File;
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
        if (row < 0) { 
            JOptionPane.showMessageDialog(view, "Sélectionnez une réparation."); 
            return; 
        }

        Client c = model.getAt(row).getClient();
        if (c == null) { 
            JOptionPane.showMessageDialog(view, "Aucun client associé."); 
            return; 
        }

        // --- Debug info ---
        System.out.println("Client object: " + c);
        System.out.println("Client photo object: " + c.getPhoto());
        if (c.getPhoto() != null) {
            System.out.println("Client photo path: " + c.getPhoto().getChemin());
        }

        // --- Text panel ---
        JTextArea textArea = new JTextArea(
            "Nom: " + c.getNom() + "\n" +
            "Prénom: " + c.getPrenom() + "\n" +
            "Email: " + c.getEmail() + "\n" +
            "Téléphone: " + c.getTelephone()
        );
        textArea.setEditable(false);
        textArea.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        textArea.setBackground(Color.WHITE);
        textArea.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel textPanel = new JPanel(new BorderLayout());
        textPanel.setBackground(Color.WHITE);
        textPanel.add(textArea, BorderLayout.CENTER);

        // --- Photo panel ---
        JPanel photoPanel = new JPanel(new BorderLayout());
        photoPanel.setBackground(Color.WHITE);

        JLabel photoLabel;

        if (c.getPhoto() != null && c.getPhoto().getChemin() != null && !c.getPhoto().getChemin().isEmpty()) {
            File f = new File(c.getPhoto().getChemin());
            if(f.exists()) {
                ImageIcon icon = new ImageIcon(f.getAbsolutePath());
                Image scaled = icon.getImage().getScaledInstance(160, 160, Image.SCALE_SMOOTH);
                photoLabel = new JLabel(new ImageIcon(scaled));
                photoLabel.setBorder(BorderFactory.createTitledBorder("Photo du client"));
            } else {
                photoLabel = new JLabel("Photo introuvable");
                photoLabel.setHorizontalAlignment(SwingConstants.CENTER);
                photoLabel.setBorder(BorderFactory.createTitledBorder("Photo du client"));
            }
        } else {
            photoLabel = new JLabel("Pas de photo");
            photoLabel.setHorizontalAlignment(SwingConstants.CENTER);
            photoLabel.setBorder(BorderFactory.createTitledBorder("Photo du client"));
        }

        photoLabel.setPreferredSize(new Dimension(160,160));
        photoPanel.add(photoLabel, BorderLayout.CENTER);

        // --- Main panel (horizontal layout) ---
        JPanel mainPanel = new JPanel();
        mainPanel.setBackground(Color.WHITE);
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.X_AXIS));
        mainPanel.add(textPanel);
        mainPanel.add(Box.createRigidArea(new Dimension(15,0)));
        mainPanel.add(photoPanel);
        mainPanel.setPreferredSize(new Dimension(500, 200));

        JOptionPane.showMessageDialog(view, mainPanel, "Détails du client", JOptionPane.INFORMATION_MESSAGE);
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
