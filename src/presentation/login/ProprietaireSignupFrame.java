package presentation.login;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.ActionEvent;

import dao.impl.CaisseDaoImpl;
import dao.impl.ProprietaireDaoImpl;
import metier.model.*;
import metier.servicesImpl.*;

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

        setTitle("Inscription du Propriétaire");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(600, 750);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        getContentPane().setBackground(new Color(245, 247, 250));

        // Title panel
        JLabel lblTitle = new JLabel("Créer un compte propriétaire", SwingConstants.CENTER);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 22));
        lblTitle.setForeground(new Color(40, 60, 100));
        lblTitle.setBorder(new EmptyBorder(20, 10, 20, 10));
        add(lblTitle, BorderLayout.NORTH);

        // Main form panel
        JPanel formPanel = new JPanel();
        formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));
        formPanel.setBackground(Color.WHITE);
        formPanel.setBorder(new CompoundBorder(
                new EmptyBorder(20, 30, 20, 30),
                new LineBorder(new Color(230, 230, 230), 1, true)
        ));

        // Section: Propriétaire
        formPanel.add(sectionTitle("Informations du Propriétaire"));
        formPanel.add(formField("Nom", txtNom = new JTextField()));
        formPanel.add(formField("Prénom", txtPrenom = new JTextField()));
        formPanel.add(formField("Email", txtEmail = new JTextField()));
        formPanel.add(formField("Téléphone", txtTelephone = new JTextField()));
        formPanel.add(formField("Pourcentage Réparation", txtPourcentage = new JTextField()));
        formPanel.add(Box.createVerticalStrut(10));

        // Section: Compte
        formPanel.add(sectionTitle("Informations du Compte"));
        formPanel.add(formField("Login", txtLogin = new JTextField()));
        formPanel.add(formField("Mot de passe", txtPassword = new JPasswordField()));
        formPanel.add(Box.createVerticalStrut(10));

        // Section: Boutique
        formPanel.add(sectionTitle("Informations de la Boutique"));
        formPanel.add(formField("Nom Boutique", txtBoutiqueNom = new JTextField()));
        formPanel.add(formField("Adresse Boutique", txtBoutiqueAdresse = new JTextField()));
        formPanel.add(formField("Numéro Patente", txtBoutiqueNumPatente = new JTextField()));
        formPanel.add(Box.createVerticalStrut(20));

        // Signup button
        btnSignup = new JButton("S'inscrire");
        stylePrimaryButton(btnSignup);
        btnSignup.setAlignmentX(Component.CENTER_ALIGNMENT);
        formPanel.add(btnSignup);

        // Wrap in a scroll pane for smaller screens
        JScrollPane scrollPane = new JScrollPane(formPanel);
        scrollPane.setBorder(null);
        add(scrollPane, BorderLayout.CENTER);

        // Event
        btnSignup.addActionListener(this::handleSignupAction);
    }

    private void handleSignupAction(ActionEvent e) {
        try {
            handleSignup();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Erreur: " + ex.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }

    private void handleSignup() throws Exception {
        // 1️⃣ Create Compte
        Compte compte = new Compte();
        compte.setLogin(txtLogin.getText());
        compte.setMotDePasse(txtPassword.getText());
        compte.setRole(metier.enums.RoleCompte.proprietaire);
        compte = compteService.creerCompte(compte);

        // 2️⃣ Create Proprietaire
        Proprietaire proprietaire = new Proprietaire();
        proprietaire.setNom(txtNom.getText());
        proprietaire.setPrenom(txtPrenom.getText());
        proprietaire.setEmail(txtEmail.getText());
        proprietaire.setTelephone(txtTelephone.getText());
        proprietaire.setPourcentageReparation(Double.parseDouble(txtPourcentage.getText()));
        proprietaire.setCompte(compte);

        ProprietaireDaoImpl proprietaireDao = new ProprietaireDaoImpl();
        proprietaire = proprietaireDao.save(proprietaire);

        // 3️⃣ Create Boutique
        Boutique boutique = new Boutique();
        boutique.setNom(txtBoutiqueNom.getText());
        boutique.setAddresse(txtBoutiqueAdresse.getText());
        boutique.setNumPatente(txtBoutiqueNumPatente.getText());
        boutique.setProprietaire(proprietaire);
        boutiqueService.creerBoutique(boutique);
        proprietaire.getBoutiques().add(boutique);

        // 4️⃣ Create Caisse(s)
        CaisseDaoImpl caisseDao = new CaisseDaoImpl();

        Caisse personalCaisse = new Caisse();
        personalCaisse.setSoldeActuel(0);
        personalCaisse.setType(metier.enums.TypeCaisse.PERSONNELLE);
        personalCaisse.setReparateur(proprietaire);
        caisseDao.save(personalCaisse);

        Caisse globalCaisse = new Caisse();
        globalCaisse.setSoldeActuel(0);
        globalCaisse.setType(metier.enums.TypeCaisse.GLOBALE);
        globalCaisse.setReparateur(proprietaire);
        caisseDao.save(globalCaisse);

        JOptionPane.showMessageDialog(this,
                "Inscription réussie !\nLes caisses personnelle et globale ont été créées.",
                "Succès", JOptionPane.INFORMATION_MESSAGE);
    }

    // -------------------- UI HELPERS -------------------- //

    private JPanel formField(String labelText, JTextField field) {
        JLabel label = new JLabel(labelText);
        label.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        label.setForeground(new Color(70, 70, 70));

        field.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        field.setBorder(new CompoundBorder(
                new LineBorder(new Color(200, 200, 200), 1, true),
                new EmptyBorder(8, 10, 8, 10)
        ));
        field.setMaximumSize(new Dimension(Integer.MAX_VALUE, 36));

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(Color.WHITE);
        panel.add(label);
        panel.add(Box.createVerticalStrut(4));
        panel.add(field);
        panel.add(Box.createVerticalStrut(10));

        return panel;
    }

    private JLabel sectionTitle(String text) {
        JLabel lbl = new JLabel(text);
        lbl.setFont(new Font("Segoe UI Semibold", Font.PLAIN, 16));
        lbl.setForeground(new Color(30, 60, 120));
        lbl.setBorder(new EmptyBorder(10, 0, 5, 0));
        return lbl;
    }

    private void stylePrimaryButton(JButton button) {
        button.setBackground(new Color(60, 90, 200));
        button.setForeground(Color.WHITE);
        button.setFont(new Font("Segoe UI Semibold", Font.PLAIN, 16));
        button.setFocusPainted(false);
        button.setBorder(new EmptyBorder(10, 20, 10, 20));
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        button.setAlignmentX(Component.CENTER_ALIGNMENT);

        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(45, 75, 180));
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(60, 90, 200));
            }
        });
    }

    // -------------------- MAIN -------------------- //

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new ProprietaireSignupFrame().setVisible(true));
    }
}
