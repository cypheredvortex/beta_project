package presentation.proprietaire.controller;

import javax.swing.*;
import java.awt.GridLayout;
import java.util.List;

import dao.impl.CompteDaoImpl;
import dao.interfaces.CompteDao;
import metier.enums.RoleCompte;
import metier.model.Reparateur;
import metier.model.Compte;
import metier.model.Boutique;
import metier.services.IReparateurService;
import metier.services.IBoutiqueService;
import metier.servicesImpl.BoutiqueServiceImpl;
import presentation.proprietaire.model.ReparateurTableModel;
import presentation.proprietaire.view.GestionReparateursPanel;

public class GestionReparateursController {

    private final GestionReparateursPanel view;
    private final ReparateurTableModel model;
    private final IReparateurService reparateurService;
    private final IBoutiqueService boutiqueService;
    private final CompteDao compteDao;

    private List<Boutique> boutiques;

    public GestionReparateursController(GestionReparateursPanel view,
                                        ReparateurTableModel model,
                                        IReparateurService reparateurService) {
        this.view = view;
        this.model = model;
        this.reparateurService = reparateurService;
        this.boutiqueService = new BoutiqueServiceImpl();
        this.compteDao = new CompteDaoImpl();

        loadBoutiques();
        wireEvents();
        loadData();
    }

    private void loadBoutiques() {
        try {
            boutiques = boutiqueService.listerBoutiques();
        } catch (Exception ex) {
            ex.printStackTrace();
            boutiques = List.of();
        }
    }

    private void wireEvents() {
        view.getBtnRafraichir().addActionListener(e -> loadData());
        view.getBtnAjouter().addActionListener(e -> ajouterReparateur());
        view.getBtnModifier().addActionListener(e -> modifierReparateur());
        view.getBtnSupprimer().addActionListener(e -> supprimerReparateur());
        view.getBtnEnvoyerIdentifiants().addActionListener(e -> envoyerIdentifiants());
        view.getBtnRechercher().addActionListener(e -> rechercherReparateur());
    }

    // ------------------ CRUD ------------------

    private void ajouterReparateur() {
        Reparateur r = editReparateurDialog(null);
        if (r == null) return;

        try {
            // Ensure Compte exists
            if (r.getCompte() == null) r.setCompte(new Compte());
            Compte c = r.getCompte();

            if (c.getLogin() == null || c.getLogin().isBlank()) c.setLogin(generateLogin(r.getNom()));
            if (c.getMotDePasse() == null || c.getMotDePasse().isBlank()) c.setMotDePasse(generateRandomPassword(8));
            c.setRole(RoleCompte.reparateur);
            c.setActif(true);

            // 1️⃣ Save Compte first (without reparateur_id)
            c = compteDao.save(c);

            // 2️⃣ Assign Compte to Reparateur
            r.setCompte(c);

            // 3️⃣ Save Reparateur
            reparateurService.enregistrerReparateur(r);

            // 4️⃣ Update Compte with reparateur_id to satisfy FK
            c.setReparateur(r);
            compteDao.update(c);

            loadData();
            showCredentials(r);

        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(view, "Erreur lors de l'ajout : " + ex.getMessage());
        }
    }

    private void modifierReparateur() {
        int row = view.getTable().getSelectedRow();
        if (row < 0) { JOptionPane.showMessageDialog(view, "Sélectionnez un réparateur."); return; }

        Reparateur selected = model.getAt(row);
        Reparateur edited = editReparateurDialog(selected);
        if (edited == null) return;

        try {
            // Update Reparateur first
            reparateurService.modifierReparateur(edited);

            // Ensure Compte exists
            if (edited.getCompte() == null) edited.setCompte(new Compte());
            Compte c = edited.getCompte();
            c.setRole(RoleCompte.reparateur);
            c.setActif(true);

            // Ensure login/password are set
            if (c.getLogin() == null || c.getLogin().isBlank()) {
                c.setLogin(generateLogin(edited.getNom()));
            }
            if (c.getMotDePasse() == null || c.getMotDePasse().isBlank()) {
                c.setMotDePasse(generateRandomPassword(8));
            }

            if (c.getId() > 0) {
                // Update existing Compte
                compteDao.update(c);
            } else {
                // Save new Compte and link it
                c = compteDao.save(c);
                edited.setCompte(c);
                reparateurService.modifierReparateur(edited);
                c.setReparateur(edited);
                compteDao.update(c);
            }

            loadData();
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(view, "Erreur lors de la modification : " + ex.getMessage());
        }
    }


    private void supprimerReparateur() {
        int row = view.getTable().getSelectedRow();
        if (row < 0) { JOptionPane.showMessageDialog(view, "Sélectionnez un réparateur."); return; }

        Reparateur selected = model.getAt(row);
        int confirm = JOptionPane.showConfirmDialog(view, "Supprimer ce réparateur ?");
        if (confirm != JOptionPane.YES_OPTION) return;

        try {
            // Delete Reparateur first
            reparateurService.supprimerReparateur(selected.getId());

            // Delete Compte (if exists)
            if (selected.getCompte() != null && selected.getCompte().getId() > 0)
                compteDao.deleteById(selected.getCompte().getId());

            loadData();
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(view, "Erreur lors de la suppression : " + ex.getMessage());
        }
    }

    private void envoyerIdentifiants() {
        int row = view.getTable().getSelectedRow();
        if (row < 0) { JOptionPane.showMessageDialog(view, "Sélectionnez un réparateur."); return; }

        Reparateur selected = model.getAt(row);

        try {
            if (selected.getCompte() == null) selected.setCompte(new Compte());
            Compte c = selected.getCompte();

            if (c.getLogin() == null || c.getLogin().isBlank()) {
                c.setLogin(generateLogin(selected.getNom()));
                c.setMotDePasse(generateRandomPassword(8));
            }

            c.setRole(RoleCompte.reparateur);
            c.setActif(true);

            if (c.getId() > 0) compteDao.update(c);
            else {
                c = compteDao.save(c);
                selected.setCompte(c);
                reparateurService.modifierReparateur(selected);
                c.setReparateur(selected);
                compteDao.update(c);
            }

            loadData();
            showCredentials(selected);
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(view, "Erreur lors de l'envoi des identifiants : " + ex.getMessage());
        }
    }

    private void rechercherReparateur() {
        String query = view.getTxtRecherche().getText().trim().toLowerCase();
        if (query.isEmpty()) {
            loadData();
            return;
        }

        try {
            List<Reparateur> all = reparateurService.listerReparateurs();
            List<Reparateur> result = new java.util.ArrayList<>();

            for (Reparateur r : all) {
                String nom = r.getNom() != null ? r.getNom().toLowerCase() : "";
                String prenom = r.getPrenom() != null ? r.getPrenom().toLowerCase() : "";
                String fullName = (nom + " " + prenom).trim();

                boolean match = false;

                // Search by full name, partial name, email, or phone
                if (fullName.contains(query) || prenom.contains(query) || nom.contains(query)) {
                    match = true;
                } else if (r.getEmail() != null && r.getEmail().toLowerCase().contains(query)) {
                    match = true;
                } else if (r.getTelephone() != null && r.getTelephone().toLowerCase().contains(query)) {
                    match = true;
                }

                if (match) result.add(r);
            }

            // 🔹 Ensure full boutique details
            for (Reparateur r : result) {
                if (r.getBoutique() != null && r.getBoutique().getId() > 0) {
                    r.setBoutique(
                        boutiqueService.trouverParId((int) r.getBoutique().getId())
                                       .orElse(r.getBoutique())
                    );
                }
            }

            model.setData(result);

            if (result.isEmpty()) {
                JOptionPane.showMessageDialog(view, "Aucun réparateur trouvé pour : " + query);
            }

        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(view, "Erreur lors de la recherche : " + ex.getMessage());
        }
    }



    // ------------------ Helpers ------------------

    private void loadData() {
        try {
            List<Reparateur> reparateurs = reparateurService.listerReparateurs();

            // 🔹 Ensure each reparateur has the full boutique object
            for (Reparateur r : reparateurs) {
                if (r.getBoutique() != null && r.getBoutique().getId() > 0) {
                    r.setBoutique(
                        boutiqueService.trouverParId((int) r.getBoutique().getId())
                                       .orElse(r.getBoutique())
                    );
                }
            }

            model.setData(reparateurs);
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(view, "Erreur lors du chargement des réparateurs : " + ex.getMessage());
        }
    }


    private Reparateur editReparateurDialog(Reparateur r) {
        JPanel panel = new JPanel(new GridLayout(0, 1, 5, 5));

        JTextField txtNom = new JTextField(r != null ? r.getNom() : "");
        JTextField txtPrenom = new JTextField(r != null ? r.getPrenom() : "");
        JTextField txtEmail = new JTextField(r != null ? r.getEmail() : "");
        JTextField txtTel = new JTextField(r != null ? r.getTelephone() : "");
        JTextField txtSalaire = new JTextField(String.valueOf(r != null ? r.getSalairePourcentage() : 0));

        JComboBox<Boutique> cbBoutique = new JComboBox<>(boutiques.toArray(new Boutique[0]));
        if (r != null && r.getBoutique() != null) cbBoutique.setSelectedItem(r.getBoutique());

        panel.add(new JLabel("Nom:")); panel.add(txtNom);
        panel.add(new JLabel("Prénom:")); panel.add(txtPrenom);
        panel.add(new JLabel("Email:")); panel.add(txtEmail);
        panel.add(new JLabel("Téléphone:")); panel.add(txtTel);
        panel.add(new JLabel("Salaire %:")); panel.add(txtSalaire);
        panel.add(new JLabel("Boutique:")); panel.add(cbBoutique);

        int result = JOptionPane.showConfirmDialog(view, panel,
                r == null ? "Ajouter réparateur" : "Modifier réparateur",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (result != JOptionPane.OK_OPTION) return null;

        Reparateur reparateur = r != null ? r : new Reparateur();
        reparateur.setNom(txtNom.getText().trim());
        reparateur.setPrenom(txtPrenom.getText().trim());
        reparateur.setEmail(txtEmail.getText().trim());
        reparateur.setTelephone(txtTel.getText().trim());
        try { reparateur.setSalairePourcentage(Double.parseDouble(txtSalaire.getText().trim())); } catch (Exception ignored) {}
        reparateur.setBoutique((Boutique) cbBoutique.getSelectedItem());

        return reparateur;
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
        for (int i = 0; i < length; i++) sb.append(chars.charAt((int)(Math.random() * chars.length())));
        return sb.toString();
    }
}
