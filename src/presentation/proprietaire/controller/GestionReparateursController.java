package presentation.proprietaire.controller;

import javax.swing.*;
import java.util.List;
import metier.model.Reparateur;
import metier.model.Compte;
import metier.services.IReparateurService;
import presentation.proprietaire.model.ReparateurTableModel;
import presentation.proprietaire.view.GestionReparateursPanel;

public class GestionReparateursController {
    private final GestionReparateursPanel view;
    private final ReparateurTableModel model;
    private final IReparateurService service;

    public GestionReparateursController(GestionReparateursPanel view, ReparateurTableModel model, IReparateurService service) {
        this.view = view;
        this.model = model;
        this.service = service;
        wireEvents();
        loadData();
    }

    private void wireEvents() {
        view.getBtnRafraichir().addActionListener(e -> loadData());

        view.getBtnAjouter().addActionListener(e -> {
            Reparateur r = editReparateurDialog(null);
            if (r != null) {
                if (r.getCompte() == null) r.setCompte(new Compte());
                if (r.getCompte().getLogin() == null || r.getCompte().getLogin().isBlank()) {
                    r.getCompte().setLogin(generateLogin(r.getNom()));
                }
                if (r.getCompte().getMotDePasse() == null || r.getCompte().getMotDePasse().isBlank()) {
                    r.getCompte().setMotDePasse(generateRandomPassword(8));
                }
                try { service.enregistrerReparateur(r); } catch (Exception ex) { ex.printStackTrace(); }
                loadData();
                showCredentials(r);
            }
        });

        view.getBtnModifier().addActionListener(e -> {
            int row = view.getTable().getSelectedRow();
            if (row < 0) { JOptionPane.showMessageDialog(view, "Sélectionnez un réparateur."); return; }
            Reparateur selected = model.getAt(row);
            Reparateur edited = editReparateurDialog(selected);
            if (edited != null) {
                try { service.modifierReparateur(edited); } catch (Exception ex) { ex.printStackTrace(); }
                loadData();
            }
        });

        view.getBtnSupprimer().addActionListener(e -> {
            int row = view.getTable().getSelectedRow();
            if (row < 0) { JOptionPane.showMessageDialog(view, "Sélectionnez un réparateur."); return; }
            Reparateur selected = model.getAt(row);
            int confirm = JOptionPane.showConfirmDialog(view, "Supprimer ce réparateur ?");
            if (confirm == JOptionPane.YES_OPTION) {
                try { service.supprimerReparateur(selected.getId()); } catch (Exception ex) { ex.printStackTrace(); }
                loadData();
            }
        });

        view.getBtnEnvoyerIdentifiants().addActionListener(e -> {
            int row = view.getTable().getSelectedRow();
            if (row < 0) { JOptionPane.showMessageDialog(view, "Sélectionnez un réparateur."); return; }
            Reparateur selected = model.getAt(row);
            if (selected.getCompte() == null) selected.setCompte(new Compte());
            if (selected.getCompte().getLogin() == null || selected.getCompte().getLogin().isBlank()) {
                selected.getCompte().setLogin(generateLogin(selected.getNom()));
                selected.getCompte().setMotDePasse(generateRandomPassword(8));
                try { service.modifierReparateur(selected); } catch (Exception ex) { ex.printStackTrace(); }
                loadData();
            }
            showCredentials(selected);
        });
    }

    private void showCredentials(Reparateur r) {
        String login = r.getCompte() != null ? r.getCompte().getLogin() : "(non défini)";
        String password = r.getCompte() != null ? r.getCompte().getMotDePasse() : "(non défini)";
        JOptionPane.showMessageDialog(view,
                "Identifiants pour " + r.getNom() + ":\nLogin: " + login + "\nMot de passe: " + password,
                "Identifiants",
                JOptionPane.INFORMATION_MESSAGE);
    }

    private String generateLogin(String nom) {
        if (nom == null || nom.isBlank()) return "reparateur" + System.currentTimeMillis();
        String base = nom.toLowerCase().replaceAll("[^a-z0-9]", "");
        return base.isEmpty() ? "reparateur" + System.currentTimeMillis() : base + System.currentTimeMillis() % 10000;
    }

    private String generateRandomPassword(int length) {
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            int idx = (int) (Math.random() * chars.length());
            sb.append(chars.charAt(idx));
        }
        return sb.toString();
    }

    private Reparateur editReparateurDialog(Reparateur r) {
        // Simple placeholder dialog using JOptionPane for demonstration
        String nom = JOptionPane.showInputDialog(view, "Nom:", r != null ? r.getNom() : "");
        if (nom == null || nom.isBlank()) return null; // Cancelled
        String prenom = JOptionPane.showInputDialog(view, "Prénom:", r != null ? r.getPrenom() : "");
        String email = JOptionPane.showInputDialog(view, "Email:", r != null ? r.getEmail() : "");
        String telephone = JOptionPane.showInputDialog(view, "Téléphone:", r != null ? r.getTelephone() : "");
        double salaire = 0;
        try { salaire = Double.parseDouble(JOptionPane.showInputDialog(view, "Salaire pourcentage:", r != null ? r.getSalairePourcentage() : 0)); } catch (Exception ignored) {}

        Reparateur reparateur = r != null ? r : new Reparateur();
        reparateur.setNom(nom);
        reparateur.setPrenom(prenom);
        reparateur.setEmail(email);
        reparateur.setTelephone(telephone);
        reparateur.setSalairePourcentage(salaire);

        return reparateur;
    }

    private void loadData() {
        try {
            List<Reparateur> data = service.listerReparateurs();
            model.setData(data);
        } catch (Exception ex) { ex.printStackTrace(); }
    }
}
