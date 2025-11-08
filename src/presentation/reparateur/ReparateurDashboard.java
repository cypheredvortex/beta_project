package presentation.reparateur;

import metier.enums.EtatReparation;
import metier.model.Compte;
import metier.model.Reparation;
import metier.model.Caisse;
import metier.servicesImpl.ReparationServiceImpl;
import metier.servicesImpl.CaisseServiceImpl;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.util.List;
import java.util.Optional;

public class ReparateurDashboard extends JFrame {

    private final Compte reparateurCompte;
    private final ReparationServiceImpl reparationService;
    private final CaisseServiceImpl caisseService;

    private JTable tblReparations;
    private DefaultTableModel reparationsTableModel;

    private JLabel lblTotalRecette;

    public ReparateurDashboard(Compte compte) {
        this.reparateurCompte = compte;
        this.reparationService = new ReparationServiceImpl();
        this.caisseService = new CaisseServiceImpl();

        setTitle("Réparateur Dashboard - " + reparateurCompte.getLogin());
        setSize(1000, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Gradient background panel
        GradientPanel background = new GradientPanel();
        background.setLayout(new BorderLayout());
        setContentPane(background);

        initComponents();
        setVisible(true);
    }

    private void initComponents() {
        // Top Panel - Dashboard Title
        JPanel topPanel = new JPanel();
        topPanel.setOpaque(false);
        topPanel.setPreferredSize(new Dimension(0, 60));
        topPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 20, 15));

        JLabel lblTitle = new JLabel("🔧 Tableau de bord Réparateur");
        lblTitle.setFont(new Font("Segoe UI Semibold", Font.BOLD, 24));
        lblTitle.setForeground(Color.WHITE);
        topPanel.add(lblTitle);

        // Total recette
        lblTotalRecette = new JLabel("Recette Totale: 0.0 MAD");
        lblTotalRecette.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblTotalRecette.setForeground(Color.WHITE);
        topPanel.add(lblTotalRecette);

        add(topPanel, BorderLayout.NORTH);

        // Center Panel - Main actions and tables
        JPanel centerPanel = new JPanel();
        centerPanel.setOpaque(false);
        centerPanel.setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;

        // Reparations Panel
        JPanel reparationsPanel = new RoundedPanel(20, new Color(255, 255, 255, 230));
        reparationsPanel.setLayout(new BorderLayout());
        reparationsPanel.setPreferredSize(new Dimension(600, 400));

        JLabel lblReparations = new JLabel("Réparations en cours / passées");
        lblReparations.setFont(new Font("Segoe UI Semibold", Font.BOLD, 16));
        lblReparations.setBorder(new EmptyBorder(10, 10, 10, 10));
        reparationsPanel.add(lblReparations, BorderLayout.NORTH);

        // Table
        reparationsTableModel = new DefaultTableModel(new Object[]{
                "ID", "Code", "Client", "Appareils", "Statut", "Prix Convenu", "Total Pièces", "Remarques"
        }, 0);
        tblReparations = new JTable(reparationsTableModel);
        tblReparations.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane scrollPane = new JScrollPane(tblReparations);
        reparationsPanel.add(scrollPane, BorderLayout.CENTER);

        // Buttons panel
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        btnPanel.setOpaque(false);

        JButton btnCreer = createStyledButton("Créer", new Color(66, 133, 244));
        JButton btnModifier = createStyledButton("Modifier", new Color(255, 193, 7));
        JButton btnSupprimer = createStyledButton("Supprimer", new Color(220, 53, 69));
        JButton btnChangerStatut = createStyledButton("Changer Statut", new Color(40, 167, 69));
        JButton btnRefresh = createStyledButton("Rafraîchir", new Color(108, 117, 125));

        btnPanel.add(btnCreer);
        btnPanel.add(btnModifier);
        btnPanel.add(btnSupprimer);
        btnPanel.add(btnChangerStatut);
        btnPanel.add(btnRefresh);

        reparationsPanel.add(btnPanel, BorderLayout.SOUTH);

        gbc.gridx = 0;
        gbc.gridy = 0;
        centerPanel.add(reparationsPanel, gbc);

        // Caisse Panel
        JPanel caissePanel = new RoundedPanel(20, new Color(255, 255, 255, 230));
        caissePanel.setLayout(new BorderLayout());
        caissePanel.setPreferredSize(new Dimension(300, 400));

        JLabel lblCaisse = new JLabel("Caisse personnelle");
        lblCaisse.setFont(new Font("Segoe UI Semibold", Font.BOLD, 16));
        lblCaisse.setBorder(new EmptyBorder(10, 10, 10, 10));
        caissePanel.add(lblCaisse, BorderLayout.NORTH);

        JTextArea txtCaisse = new JTextArea();
        txtCaisse.setEditable(false);
        txtCaisse.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        JScrollPane scrollCaisse = new JScrollPane(txtCaisse);
        caissePanel.add(scrollCaisse, BorderLayout.CENTER);

        JButton btnRefreshCaisse = createStyledButton("Rafraîchir Caisse", new Color(66, 133, 244));
        JPanel caisseBtnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        caisseBtnPanel.setOpaque(false);
        caisseBtnPanel.add(btnRefreshCaisse);
        caissePanel.add(caisseBtnPanel, BorderLayout.SOUTH);

        gbc.gridx = 1;
        gbc.gridy = 0;
        centerPanel.add(caissePanel, gbc);

        add(centerPanel, BorderLayout.CENTER);

        // --- Action Listeners ---
        btnRefresh.addActionListener(e -> loadReparations());
        btnRefreshCaisse.addActionListener(e -> loadCaisse(txtCaisse));

        btnCreer.addActionListener(e -> openReparationForm(null));
        btnModifier.addActionListener(e -> {
            int selectedRow = tblReparations.getSelectedRow();
            if (selectedRow >= 0) {
                Long repId = (Long) tblReparations.getValueAt(selectedRow, 0);
                openReparationForm(repId);
            } else {
                JOptionPane.showMessageDialog(this, "Veuillez sélectionner une réparation à modifier !");
            }
        });
        btnSupprimer.addActionListener(e -> {
            int selectedRow = tblReparations.getSelectedRow();
            if (selectedRow >= 0) {
                Long repId = (Long) tblReparations.getValueAt(selectedRow, 0);
                try {
                    if (reparationService.supprimerReparation(repId)) {
                        JOptionPane.showMessageDialog(this, "Réparation supprimée avec succès !");
                        loadReparations();
                    }
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, "Erreur: " + ex.getMessage());
                    ex.printStackTrace();
                }
            }
        });
        btnChangerStatut.addActionListener(e -> {
            int selectedRow = tblReparations.getSelectedRow();
            if (selectedRow >= 0) {
                Long repId = (Long) tblReparations.getValueAt(selectedRow, 0);
                EtatReparation newStatut = (EtatReparation) JOptionPane.showInputDialog(
                        this,
                        "Sélectionnez le nouveau statut:",
                        "Changer statut",
                        JOptionPane.PLAIN_MESSAGE,
                        null,
                        EtatReparation.values(),
                        EtatReparation.EN_COURS
                );
                if (newStatut != null) {
                    try {
                        reparationService.changerStatut(repId, newStatut);
                        loadReparations();
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(this, "Erreur: " + ex.getMessage());
                        ex.printStackTrace();
                    }
                }
            }
        });

        // Initial load
        loadReparations();
        loadCaisse(txtCaisse);
    }

    private void loadReparations() {
        try {
            reparationsTableModel.setRowCount(0);
            List<Reparation> reparations = reparationService.listerParReparateur(reparateurCompte.getReparateur().getId());
            for (Reparation r : reparations) {
                String appareils = r.getAppareils().isEmpty() ? "" : r.getAppareils().toString();
                reparationsTableModel.addRow(new Object[]{
                        r.getId(), r.getCodeUnique(),
                        r.getClient().getNom(),
                        appareils,
                        r.getStatut(),
                        r.getPrixConvenu(),
                        r.getPrixTotalPieces(),
                        r.getRemarques()
                });
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Erreur: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    private void loadCaisse(JTextArea txtCaisse) {
        try {
            Optional<Caisse> caisseOpt = caisseService.trouverParReparateur(reparateurCompte.getReparateur().getId());
            if (caisseOpt.isPresent()) {
                Caisse c = caisseOpt.get();
                txtCaisse.setText("Solde actuel: " + c.getSoldeActuel() + " MAD\n");
                txtCaisse.append("Type: " + c.getType() + "\n");
                txtCaisse.append("Dettes: " + c.getDettes().size() + "\n");
                txtCaisse.append("Transactions: " + c.getTransactions().size() + "\n");
                lblTotalRecette.setText("Recette Totale: " + caisseService.calculerRecetteTotale() + " MAD");
            } else {
                txtCaisse.setText("Aucune donnée disponible.");
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Erreur: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    private JButton createStyledButton(String text, Color bgColor) {
        JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI", Font.BOLD, 14));
        button.setBackground(bgColor);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorder(new EmptyBorder(8, 15, 8, 15));
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

    private void openReparationForm(Long reparationId) {
        // TODO: Open a new dialog to create/modify reparations
        JOptionPane.showMessageDialog(this,
                reparationId == null ? "Créer nouvelle réparation (à implémenter)" :
                        "Modifier réparation ID=" + reparationId + " (à implémenter)");
    }

    // --- Gradient background ---
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

    // --- Rounded panel for cards ---
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
