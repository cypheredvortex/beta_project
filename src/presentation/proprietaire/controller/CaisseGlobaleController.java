package presentation.proprietaire.controller;

import javax.swing.*;
import java.sql.Timestamp;
import java.util.List;

import metier.enums.TypeTransaction;
import metier.enums.TypeCaisse;
import metier.model.Caisse;
import metier.model.Transaction;
import metier.servicesImpl.CaisseServiceImpl;
import metier.servicesImpl.TransactionServiceImpl;
import presentation.proprietaire.model.CaisseTableModel;
import presentation.proprietaire.view.CaisseGlobalePanel;

public class CaisseGlobaleController {
    private final CaisseGlobalePanel view;
    private final CaisseTableModel model;
    private final CaisseServiceImpl caisseService;
    private final TransactionServiceImpl transactionService;
    private Caisse caisseGlobale;

    public CaisseGlobaleController(CaisseGlobalePanel view, CaisseTableModel model) {
        this.view = view;
        this.model = model;
        this.caisseService = new CaisseServiceImpl();
        this.transactionService = new TransactionServiceImpl();
        wire();
        load();
    }

    private void wire() {
        view.getBtnRafraichir().addActionListener(e -> load());
        view.getBtnAjouterEntree().addActionListener(e -> addTransaction());
        view.getBtnSupprimer().addActionListener(e -> remove());
    }

    // ======================================================
    // 🔹 Chargement de la caisse globale et des transactions
    // ======================================================
    private void load() {
        try {
            List<Caisse> allCaisses = caisseService.listerParType(TypeCaisse.GLOBALE);
            caisseGlobale = allCaisses.isEmpty() ? null : allCaisses.get(0);

            if (caisseGlobale == null) {
                JOptionPane.showMessageDialog(view, "Aucune caisse globale trouvée !");
                return;
            }

            List<Transaction> transactions = transactionService.listerParCaisse(caisseGlobale.getId());
            model.setData(transactions);

            view.getLblSolde().setText(String.format("Solde: %.2f", caisseGlobale.getSoldeActuel()));
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(view, "Erreur lors du chargement des données : " + ex.getMessage());
        }
    }

    // ======================================================
    // 🔹 Ajout d’une transaction (avec choix du type)
    // ======================================================
    private void addTransaction() {
        try {
            JTextField tfDesc = new JTextField();
            JTextField tfContrepartie = new JTextField();
            JSpinner spMontant = new JSpinner(new SpinnerNumberModel(0.0, 0.0, 1_000_000.0, 1.0));
            JComboBox<TypeTransaction> cbType = new JComboBox<>(TypeTransaction.values());

            JPanel p = new JPanel();
            p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
            p.add(new JLabel("Type de transaction :"));
            p.add(cbType);
            p.add(new JLabel("Description :"));
            p.add(tfDesc);
            p.add(new JLabel("Contrepartie :"));
            p.add(tfContrepartie);
            p.add(new JLabel("Montant :"));
            p.add(spMontant);

            int res = JOptionPane.showConfirmDialog(view, p, "Nouvelle transaction", JOptionPane.OK_CANCEL_OPTION);
            if (res == JOptionPane.OK_OPTION && caisseGlobale != null) {
                double montant = ((Number) spMontant.getValue()).doubleValue();
                TypeTransaction type = (TypeTransaction) cbType.getSelectedItem();

                if (type == null) {
                    JOptionPane.showMessageDialog(view, "Veuillez sélectionner un type !");
                    return;
                }

                // Créer la transaction
                Transaction t = new Transaction();
                t.setDate(new Timestamp(System.currentTimeMillis()));
                t.setMontant(montant);
                t.setType(type);
                t.setDescription(tfDesc.getText().trim());
                t.setContrepartie(tfContrepartie.getText().trim());
                t.setCaisse(caisseGlobale);

                transactionService.enregistrerTransaction(t);

                // Ajuster le solde selon le type
                double newSolde = caisseGlobale.getSoldeActuel() + calculerImpactSolde(type, montant);
                caisseGlobale.setSoldeActuel(newSolde);
                caisseService.modifierOperation(caisseGlobale);

                load();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(view, "Erreur: " + ex.getMessage());
        }
    }

    // ======================================================
    // 🔹 Suppression d’une transaction
    // ======================================================
    private void remove() {
        int row = view.getTable().getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(view, "Sélectionnez une transaction !");
            return;
        }

        Transaction tx = model.getAt(row);
        int res = JOptionPane.showConfirmDialog(view, "Supprimer cette transaction ?", "Confirmation", JOptionPane.YES_NO_OPTION);
        if (res == JOptionPane.YES_OPTION) {
            try {
                transactionService.supprimerTransaction(tx.getId());

                double newSolde = caisseGlobale.getSoldeActuel() - calculerImpactSolde(tx.getType(), tx.getMontant());
                caisseGlobale.setSoldeActuel(newSolde);
                caisseService.modifierOperation(caisseGlobale);

                load();
            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(view, "Erreur: " + ex.getMessage());
            }
        }
    }

    // ======================================================
    // 🔹 Calcul de l’impact sur le solde selon le type
    // ======================================================
    private double calculerImpactSolde(TypeTransaction type, double montant) {
        switch (type) {
            case VENTE:
            case AVANCE:
            case REMBOURSEMENT:
                return montant;  // augmente le solde
            case DEPENSE:
                return -montant; // diminue le solde
            default:
                return 0;
        }
    }
}
