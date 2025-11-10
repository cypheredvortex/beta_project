package presentation.proprietaire.controller;

import javax.swing.*;
import java.util.List;
import metier.model.Caisse;
import metier.model.Dette;
import metier.model.Transaction;
import metier.servicesImpl.CaisseServiceImpl;
import metier.servicesImpl.TransactionServiceImpl;
import metier.servicesImpl.ReparateurServiceImpl;
import dao.impl.DetteDaoImpl;
import presentation.proprietaire.model.CaisseTableModel;
import presentation.proprietaire.view.CaisseGlobalePanel;

public class CaisseGlobaleController {
    private final CaisseGlobalePanel view;
    private final CaisseTableModel model;
    private final CaisseServiceImpl caisseService;
    private final TransactionServiceImpl transactionService;
    private final DetteDaoImpl detteDao;
    private final ReparateurServiceImpl reparateurService;

    public CaisseGlobaleController(CaisseGlobalePanel view, CaisseTableModel model) {
        this.view = view;
        this.model = model;
        this.caisseService = new CaisseServiceImpl();
        this.transactionService = new TransactionServiceImpl();
        this.detteDao = new DetteDaoImpl();
        this.reparateurService = new ReparateurServiceImpl();
        wire();
        load();
    }

    private void wire() {
        view.getBtnRafraichir().addActionListener(e -> load());
        view.getBtnSupprimer().addActionListener(e -> remove());
        view.getBtnVoirDettes().addActionListener(e -> showDettes());
        view.getBtnVoirTransactions().addActionListener(e -> showTransactions());
    }

    private void load() {
        try {
            List<Caisse> caisses = caisseService.listerOperations(); // Load all caisses

            // Load reparateur for each caisse if not null
            for (Caisse c : caisses) {
                if (c.getReparateur() != null) {
                    c.setReparateur(reparateurService.trouverParId(c.getReparateur().getId()).orElse(null));
                }
            }

            model.setData(caisses); // Set data to table model

        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(view, "Erreur lors du chargement : " + ex.getMessage());
        }
    }



    private void remove() {
        int row = view.getTable().getSelectedRow();
        if (row < 0) { JOptionPane.showMessageDialog(view, "Sélectionnez une caisse !"); return; }

        Caisse caisse = model.getAt(row);
        int res = JOptionPane.showConfirmDialog(view, "Supprimer cette caisse ?", "Confirmation", JOptionPane.YES_NO_OPTION);
        if (res == JOptionPane.YES_OPTION) {
            try {
                caisseService.supprimerOperation(caisse.getId());
                load();
            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(view, "Erreur: " + ex.getMessage());
            }
        }
    }

    private void showDettes() {
        int row = view.getTable().getSelectedRow();
        if (row < 0) { JOptionPane.showMessageDialog(view, "Sélectionnez une caisse !"); return; }
        Caisse caisse = model.getAt(row);

        try {
            List<Dette> dettes = detteDao.findByCaisseId(caisse.getId());
            JTextArea area = new JTextArea();
            area.setEditable(false);
            for (Dette d : dettes) {
                area.append(String.format("ID: %d | Montant: %.2f | Donné par: %s | Reçu par: %s | Date: %s | Réglée: %s%n",
                        d.getId(), d.getMontant(), d.getDonnepar(), d.getRecuPar(), d.getDate(), d.isReglee()));
            }
            JScrollPane scroll = new JScrollPane(area);
            scroll.setPreferredSize(new java.awt.Dimension(600, 400));
            JOptionPane.showMessageDialog(view, scroll, "Dettes de la caisse", JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(view, "Erreur: " + ex.getMessage());
        }
    }

    private void showTransactions() {
        int row = view.getTable().getSelectedRow();
        if (row < 0) { JOptionPane.showMessageDialog(view, "Sélectionnez une caisse !"); return; }
        Caisse caisse = model.getAt(row);

        try {
            List<Transaction> transactions = transactionService.listerParCaisse(caisse.getId());
            JTextArea area = new JTextArea();
            area.setEditable(false);
            for (Transaction t : transactions) {
                area.append(String.format("ID: %d | Type: %s | Montant: %.2f | Description: %s | Contrepartie: %s | Date: %s%n",
                        t.getId(), t.getType(), t.getMontant(), t.getDescription(), t.getContrepartie(), t.getDate()));
            }
            JScrollPane scroll = new JScrollPane(area);
            scroll.setPreferredSize(new java.awt.Dimension(600, 400));
            JOptionPane.showMessageDialog(view, scroll, "Transactions de la caisse", JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(view, "Erreur: " + ex.getMessage());
        }
    }
}
