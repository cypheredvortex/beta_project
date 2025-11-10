package presentation.login;

import javax.swing.*;

import dao.impl.CaisseDaoImpl;
import dao.impl.ProprietaireDaoImpl;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import metier.model.Boutique;
import metier.model.Caisse;
import metier.model.Compte;
import metier.model.Proprietaire;
import metier.servicesImpl.BoutiqueServiceImpl;
import metier.servicesImpl.ReparateurServiceImpl;
import metier.servicesImpl.UtilisateurServiceImpl;

public class ProprietaireSignupFrame extends JFrame {

    private JTextField txtNom, txtPrenom, txtEmail, txtTelephone, txtPourcentage;
    private JTextField txtLogin, txtPassword;
    private JTextField txtBoutiqueNom, txtBoutiqueAdresse, txtBoutiqueNumPatente;
    private JButton btnSignup;

    private final UtilisateurServiceImpl compteService;
    private final ReparateurServiceImpl reparateurService;
    private final BoutiqueServiceImpl boutiqueService;

    public ProprietaireSignupFrame() {
        compteService = new UtilisateurServiceImpl();
        reparateurService = new ReparateurServiceImpl();
        boutiqueService = new BoutiqueServiceImpl();

        setTitle("Inscription Propriétaire");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(500, 600);
        setLocationRelativeTo(null);
        setLayout(new GridLayout(12, 2, 5, 5));

        // Propriétaire fields
        add(new JLabel("Nom:"));
        txtNom = new JTextField();
        add(txtNom);

        add(new JLabel("Prénom:"));
        txtPrenom = new JTextField();
        add(txtPrenom);

        add(new JLabel("Email:"));
        txtEmail = new JTextField();
        add(txtEmail);

        add(new JLabel("Téléphone:"));
        txtTelephone = new JTextField();
        add(txtTelephone);

        add(new JLabel("Pourcentage Réparation:"));
        txtPourcentage = new JTextField();
        add(txtPourcentage);

        add(new JLabel("Login:"));
        txtLogin = new JTextField();
        add(txtLogin);

        add(new JLabel("Mot de passe:"));
        txtPassword = new JTextField();
        add(txtPassword);

        // Boutique fields
        add(new JLabel("Nom Boutique:"));
        txtBoutiqueNom = new JTextField();
        add(txtBoutiqueNom);

        add(new JLabel("Adresse Boutique:"));
        txtBoutiqueAdresse = new JTextField();
        add(txtBoutiqueAdresse);

        add(new JLabel("Numéro Patente:"));
        txtBoutiqueNumPatente = new JTextField();
        add(txtBoutiqueNumPatente);

        btnSignup = new JButton("S'inscrire");
        add(btnSignup);
        add(new JLabel()); // empty placeholder

        btnSignup.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    handleSignup();
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(null, "Erreur: " + ex.getMessage());
                    ex.printStackTrace();
                }
            }
        });
    }

    private void handleSignup() throws Exception {
        // 1️⃣ Create Compte
        Compte compte = new Compte();
        compte.setLogin(txtLogin.getText());
        compte.setMotDePasse(txtPassword.getText());
        compte.setRole(metier.enums.RoleCompte.proprietaire);
        compte = compteService.creerCompte(compte);

        // 2️⃣ Create Proprietaire object
        Proprietaire proprietaire = new Proprietaire();
        proprietaire.setNom(txtNom.getText());
        proprietaire.setPrenom(txtPrenom.getText());
        proprietaire.setEmail(txtEmail.getText());
        proprietaire.setTelephone(txtTelephone.getText());
        proprietaire.setPourcentageReparation(Double.parseDouble(txtPourcentage.getText()));
        proprietaire.setCompte(compte);

        // ✅ Save Proprietaire (inserts Reparateur + Proprietaire)
        ProprietaireDaoImpl proprietaireDao = new ProprietaireDaoImpl();
        proprietaire = proprietaireDao.save(proprietaire); // now p.id is filled

        // 3️⃣ Create Boutique
        Boutique boutique = new Boutique();
        boutique.setNom(txtBoutiqueNom.getText());
        boutique.setAddresse(txtBoutiqueAdresse.getText());
        boutique.setNumPatente(txtBoutiqueNumPatente.getText());
        boutique.setProprietaire(proprietaire);

        // ✅ Save Boutique
        boutiqueService.creerBoutique(boutique);

        // Link boutique in memory
        proprietaire.getBoutiques().add(boutique);

        // 4️⃣ Create Caisse(s) for Proprietaire
        CaisseDaoImpl caisseDao = new CaisseDaoImpl();

        // Personal caisse
        Caisse personalCaisse = new Caisse();
        personalCaisse.setSoldeActuel(0);
        personalCaisse.setType(metier.enums.TypeCaisse.PERSONNELLE);
        personalCaisse.setReparateur(proprietaire); // Proprietaire is a Reparateur
        caisseDao.save(personalCaisse);

        // Global caisse
        Caisse globalCaisse = new Caisse();
        globalCaisse.setSoldeActuel(0);
        globalCaisse.setType(metier.enums.TypeCaisse.GLOBALE);
        globalCaisse.setReparateur(proprietaire); // Proprietaire is a Reparateur
        caisseDao.save(globalCaisse);

        JOptionPane.showMessageDialog(this, "Inscription réussie avec caisses créées !");
    }



    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new ProprietaireSignupFrame().setVisible(true);
        });
    }
}
