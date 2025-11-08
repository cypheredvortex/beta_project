package presentation.client;

import java.awt.*;
import java.util.List;
import java.util.Optional;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

import dao.impl.AppareilDaoImpl;
import dao.impl.ClientDaoImpl;
import metier.model.Appareil;
import metier.model.Client;
import metier.model.Reparation;
import metier.servicesImpl.ReparationServiceImpl;

public class SuiviClientFrame {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Gestion de Réparation");
            frame.setSize(1000, 650);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setLocationRelativeTo(null);
            frame.setLayout(new BorderLayout());
            frame.getContentPane().setBackground(new Color(245, 245, 245));

            // ---------- Top Input Panel ----------
            JPanel inputPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 20));
            inputPanel.setBackground(new Color(60, 120, 200));
            inputPanel.setBorder(new EmptyBorder(10, 20, 10, 20));

            JLabel label = new JLabel("Code de la réparation:");
            label.setForeground(Color.WHITE);
            label.setFont(new Font("Segoe UI", Font.BOLD, 14));

            JTextField textfield = new JTextField(25);
            textfield.setFont(new Font("Segoe UI", Font.PLAIN, 14));
            textfield.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));

            JButton btn = new JButton("Afficher");
            btn.setFont(new Font("Segoe UI", Font.BOLD, 14));
            btn.setBackground(new Color(255, 140, 0));
            btn.setForeground(Color.WHITE);
            btn.setFocusPainted(false);
            btn.setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 15));

            inputPanel.add(label);
            inputPanel.add(textfield);
            inputPanel.add(btn);
            frame.add(inputPanel, BorderLayout.NORTH);

            // ---------- Scrollable Panel ----------
            JPanel reparationsPanel = new JPanel();
            reparationsPanel.setLayout(new BoxLayout(reparationsPanel, BoxLayout.Y_AXIS));
            reparationsPanel.setBackground(new Color(245, 245, 245));

            JScrollPane scrollPane = new JScrollPane(reparationsPanel);
            scrollPane.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
            scrollPane.getVerticalScrollBar().setUnitIncrement(16); // smoother scrolling
            frame.add(scrollPane, BorderLayout.CENTER);

            frame.setVisible(true);

            // ---------- Services ----------
            ReparationServiceImpl reparationService = new ReparationServiceImpl();
            ClientDaoImpl clientDao = new ClientDaoImpl();
            AppareilDaoImpl appareilDao = new AppareilDaoImpl();

            btn.addActionListener(e -> {
                String code = textfield.getText().trim();
                if (code.isEmpty()) {
                    JOptionPane.showMessageDialog(frame, "Veuillez entrer un code de réparation.");
                    return;
                }

                try {
                    Optional<Reparation> reparOpt = reparationService.trouverParCodeUnique(code);
                    if (reparOpt.isPresent()) {
                        Reparation rep = reparOpt.get();

                        // Fetch Client
                        Client client = null;
                        if (rep.getClient() != null && rep.getClient().getId() > 0) {
                            Optional<Client> clientOpt = clientDao.findById((int) rep.getClient().getId());
                            if (clientOpt.isPresent()) {
                                client = clientOpt.get();
                                rep.setClient(client);
                            }
                        }

                        // Fetch Appareils
                        List<Appareil> appareils = appareilDao.findByReparationId(rep.getId());
                        rep.setAppareils(appareils);

                        // Clear previous content
                        reparationsPanel.removeAll();

                        // ---------- Reparation Card ----------
                        JPanel card = new JPanel();
                        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
                        card.setBackground(Color.WHITE);
                        card.setBorder(BorderFactory.createCompoundBorder(
                                BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
                                new EmptyBorder(15, 15, 15, 15)
                        ));
                        card.setAlignmentX(Component.LEFT_ALIGNMENT);

                        // Title Label
                        JLabel title = new JLabel("Détails de la Réparation");
                        title.setFont(new Font("Segoe UI", Font.BOLD, 18));
                        title.setForeground(new Color(60, 120, 200));
                        title.setAlignmentX(Component.LEFT_ALIGNMENT);
                        card.add(title);
                        card.add(Box.createVerticalStrut(10));

                        // Helper function for adding fields
                        java.util.function.BiConsumer<String, String> addField = (labelText, valueText) -> {
                            JPanel fieldPanel = new JPanel(new BorderLayout());
                            fieldPanel.setBackground(Color.WHITE);

                            JLabel lbl = new JLabel(labelText + ": ");
                            lbl.setFont(new Font("Segoe UI", Font.BOLD, 13));
                            lbl.setForeground(new Color(80, 80, 80));
                            fieldPanel.add(lbl, BorderLayout.WEST);

                            JLabel val = new JLabel(valueText != null ? valueText : "");
                            val.setFont(new Font("Segoe UI", Font.PLAIN, 13));
                            val.setForeground(new Color(50, 50, 50));
                            fieldPanel.add(val, BorderLayout.CENTER);

                            card.add(fieldPanel);
                            card.add(Box.createVerticalStrut(5));
                        };

                        // ---------- Reparation Info ----------
                        addField.accept("ID", String.valueOf(rep.getId()));
                        addField.accept("Code Unique", rep.getCodeUnique());
                        addField.accept("Date Création", String.valueOf(rep.getDateCreation()));
                        addField.accept("Statut", rep.getStatut() != null ? rep.getStatut().name() : "");
                        addField.accept("Description", rep.getDescription());
                        addField.accept("Prix Convenu", String.valueOf(rep.getPrixConvenu()));
                        addField.accept("Prix Total Pièces", String.valueOf(rep.getPrixTotalPieces()));
                        addField.accept("Remarques", rep.getRemarques());
                        addField.accept("Réparateur", rep.getReparateur() != null ? rep.getReparateur().getNom() : "");

                        card.add(Box.createVerticalStrut(10));

                        // ---------- Client Info ----------
                        if (client != null) {
                            JLabel clientTitle = new JLabel("Informations du Client");
                            clientTitle.setFont(new Font("Segoe UI", Font.BOLD, 16));
                            clientTitle.setForeground(new Color(0, 150, 136));
                            clientTitle.setAlignmentX(Component.LEFT_ALIGNMENT);
                            card.add(clientTitle);
                            card.add(Box.createVerticalStrut(5));

                            addField.accept("Client ID", String.valueOf(client.getId()));
                            addField.accept("Nom", client.getNom());
                            addField.accept("Prénom", client.getPrenom());
                            addField.accept("Email", client.getEmail());
                            addField.accept("Téléphone", client.getTelephone());
                            addField.accept("Photo ID", client.getPhoto() != null ? String.valueOf(client.getPhoto().getId()) : "N/A");

                            card.add(Box.createVerticalStrut(10));
                        }

                        // ---------- Appareils Info ----------
                        if (!appareils.isEmpty()) {
                            JLabel appTitle = new JLabel("Appareils Concernés");
                            appTitle.setFont(new Font("Segoe UI", Font.BOLD, 16));
                            appTitle.setForeground(new Color(255, 140, 0));
                            appTitle.setAlignmentX(Component.LEFT_ALIGNMENT);
                            card.add(appTitle);
                            card.add(Box.createVerticalStrut(5));

                            for (int i = 0; i < appareils.size(); i++) {
                                Appareil app = appareils.get(i);
                                JLabel lblApp = new JLabel("Appareil " + (i + 1));
                                lblApp.setFont(new Font("Segoe UI", Font.BOLD, 14));
                                lblApp.setForeground(new Color(120, 120, 120));
                                lblApp.setAlignmentX(Component.LEFT_ALIGNMENT);
                                card.add(lblApp);
                                card.add(Box.createVerticalStrut(3));

                                addField.accept("ID", String.valueOf(app.getId()));
                                addField.accept("Type", app.getType());
                                addField.accept("Marque", app.getMarque());
                                addField.accept("Modèle", app.getModele());
                                addField.accept("Numéro de série", app.getNumeroSerie());
                                addField.accept("Problème", app.getProbleme());
                                addField.accept("Type de problème", app.getTypeProbleme() != null ? app.getTypeProbleme().name() : "");
                                card.add(Box.createVerticalStrut(5));
                            }
                        }

                        reparationsPanel.add(card);
                        reparationsPanel.add(Box.createVerticalStrut(15));
                        reparationsPanel.revalidate();
                        reparationsPanel.repaint();

                    } else {
                        JOptionPane.showMessageDialog(frame, "Réparation non trouvée pour le code: " + code, "Erreur", JOptionPane.ERROR_MESSAGE);
                    }
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(frame, "Erreur lors de la recherche: " + ex.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
                    ex.printStackTrace();
                }
            });
        });
    }
}