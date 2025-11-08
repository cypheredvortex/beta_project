package presentation.login;

import metier.model.Compte;
import metier.enums.RoleCompte;
import metier.servicesImpl.UtilisateurServiceImpl;
import presentation.reparateur.ReparateurDashboard;
import presentation.proprietaire.view.DashboardProprietaireFrame;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Optional;

public class LoginFrame extends JFrame {

    private JTextField txtLogin;
    private JPasswordField txtPassword;
    private JButton btnLogin;
    private JButton btnExit;
    private UtilisateurServiceImpl utilisateurService;

    public LoginFrame() {
        utilisateurService = new UtilisateurServiceImpl();
        setTitle("🔐 Connexion - Système de Gestion Réparation");
        setSize(500, 380);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);

        setContentPane(new GradientPanel());
        setLayout(new GridBagLayout());

        initComponents();
        setVisible(true);
    }

    private void initComponents() {
        RoundedPanel card = new RoundedPanel(30, new Color(255, 255, 255, 240));
        card.setPreferredSize(new Dimension(400, 300));
        card.setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel lblTitle = new JLabel("🔧 Connexion à votre compte", JLabel.CENTER);
        lblTitle.setFont(new Font("Segoe UI Semibold", Font.BOLD, 20));
        lblTitle.setForeground(new Color(50, 50, 50));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(5, 0, 20, 0);
        card.add(lblTitle, gbc);

        JLabel lblLogin = new JLabel("Nom d'utilisateur");
        lblLogin.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        txtLogin = createStyledTextField("Login");

        gbc.gridwidth = 1;
        gbc.gridx = 0;
        gbc.gridy = 1;
        card.add(lblLogin, gbc);
        gbc.gridx = 1;
        gbc.gridy = 1;
        card.add(txtLogin, gbc);

        JLabel lblPassword = new JLabel("Mot de passe");
        lblPassword.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        txtPassword = createStyledPasswordField("Mot de passe");

        gbc.gridx = 0;
        gbc.gridy = 2;
        card.add(lblPassword, gbc);
        gbc.gridx = 1;
        gbc.gridy = 2;
        card.add(txtPassword, gbc);

        btnLogin = createStyledButton("Se connecter", new Color(66, 133, 244));
        btnExit = createStyledButton("Quitter", new Color(220, 53, 69));

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 5));
        buttonPanel.setOpaque(false);
        buttonPanel.add(btnLogin);
        buttonPanel.add(btnExit);

        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(20, 0, 5, 0);
        card.add(buttonPanel, gbc);

        add(card);

        btnLogin.addActionListener(this::onLoginClicked);
        btnExit.addActionListener(e -> System.exit(0));
    }

    private JTextField createStyledTextField(String placeholder) {
        JTextField field = new JTextField();
        field.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        field.setPreferredSize(new Dimension(200, 35));
        field.setBorder(new CompoundBorder(
                new LineBorder(new Color(200, 200, 200), 1, true),
                new EmptyBorder(5, 10, 5, 10)
        ));
        field.setToolTipText(placeholder);

        field.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                field.setBorder(new LineBorder(new Color(66, 133, 244), 2, true));
            }
            @Override
            public void focusLost(FocusEvent e) {
                field.setBorder(new LineBorder(new Color(200, 200, 200), 1, true));
            }
        });
        return field;
    }

    private JPasswordField createStyledPasswordField(String placeholder) {
        JPasswordField field = new JPasswordField();
        field.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        field.setPreferredSize(new Dimension(200, 35));
        field.setBorder(new CompoundBorder(
                new LineBorder(new Color(200, 200, 200), 1, true),
                new EmptyBorder(5, 10, 5, 10)
        ));
        field.setToolTipText(placeholder);

        field.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                field.setBorder(new LineBorder(new Color(66, 133, 244), 2, true));
            }
            @Override
            public void focusLost(FocusEvent e) {
                field.setBorder(new LineBorder(new Color(200, 200, 200), 1, true));
            }
        });
        return field;
    }

    private JButton createStyledButton(String text, Color bgColor) {
        JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI", Font.BOLD, 14));
        button.setBackground(bgColor);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorder(new EmptyBorder(10, 20, 10, 20));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));

        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setBackground(bgColor.darker());
            }
            @Override
            public void mouseExited(MouseEvent e) {
                button.setBackground(bgColor);
            }
        });
        return button;
    }

    private void onLoginClicked(ActionEvent e) {
        String login = txtLogin.getText().trim();
        String password = new String(txtPassword.getPassword());

        if (login.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Veuillez remplir tous les champs !", "Erreur", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            Optional<Compte> compteOpt = utilisateurService.seConnecter(login, password);
            if (compteOpt.isPresent()) {
                Compte compte = compteOpt.get();

                if (!compte.isActif()) {
                    JOptionPane.showMessageDialog(this,
                            "Ce compte est inactif. Contactez l'administrateur.",
                            "Compte inactif", JOptionPane.WARNING_MESSAGE);
                    return;
                }

                JFrame dashboardFrame;

                RoleCompte roleEnum = compte.getRole();

                if (roleEnum == RoleCompte.reparateur) {
                    dashboardFrame = new ReparateurDashboard(compte);
                } else if (roleEnum == RoleCompte.proprietaire) {
                    dashboardFrame = new DashboardProprietaireFrame(compte);
                } else {
                    JOptionPane.showMessageDialog(this, "Rôle inconnu !", "Erreur", JOptionPane.ERROR_MESSAGE);
                    return;
                }


                dashboardFrame.setVisible(true); // ensure the dashboard is visible
                this.dispose(); // now safely close login
            } else {
                JOptionPane.showMessageDialog(this,
                        "Login ou mot de passe incorrect, ou compte inactif.",
                        "Erreur d'authentification", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Erreur : " + ex.getMessage(),
                    "Exception", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }



    private void openReparateurDashboard(Compte compte) {
        new ReparateurDashboard(compte);
    }

    private void openProprietaireDashboard(Compte compte) {
        new DashboardProprietaireFrame(compte); // Uses your adapted proprietaire dashboard
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(LoginFrame::new);
    }

    static class GradientPanel extends JPanel {
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2d = (Graphics2D) g;
            GradientPaint gp = new GradientPaint(0, 0, new Color(66, 133, 244),
                    getWidth(), getHeight(), new Color(30, 87, 153));
            g2d.setPaint(gp);
            g2d.fillRect(0, 0, getWidth(), getHeight());
        }
    }

    static class RoundedPanel extends JPanel {
        private final int cornerRadius;
        private final Color backgroundColor;

        public RoundedPanel(int radius, Color bgColor) {
            super();
            this.cornerRadius = radius;
            this.backgroundColor = bgColor;
            setOpaque(false);
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Dimension arcs = new Dimension(cornerRadius, cornerRadius);
            int width = getWidth();
            int height = getHeight();
            Graphics2D graphics = (Graphics2D) g;
            graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            graphics.setColor(backgroundColor);
            graphics.fillRoundRect(0, 0, width - 1, height - 1, arcs.width, arcs.height);
            graphics.setColor(new Color(220, 220, 220));
            graphics.drawRoundRect(0, 0, width - 1, height - 1, arcs.width, arcs.height);
        }
    }
}
