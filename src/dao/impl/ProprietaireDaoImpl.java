package dao.impl;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import dao.DBConnection;
import dao.interfaces.ProprietaireDao;
import metier.model.Proprietaire;
import metier.model.Reparateur;
import metier.model.Boutique;

public class ProprietaireDaoImpl implements ProprietaireDao {

    @Override
    public Proprietaire save(Proprietaire p) throws Exception {
        Connection conn = DBConnection.getConnection();
        try {
            conn.setAutoCommit(false);

            // Insert into reparateur table first
            String sqlReparateur = "INSERT INTO reparateur " +
            	    "(compte_id, boutique_id, caisse_id, nom, prenom, email, telephone, salairePourcentage) " +
            	    "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
            	PreparedStatement psR = conn.prepareStatement(sqlReparateur, Statement.RETURN_GENERATED_KEYS);
            	psR.setObject(1, p.getCompte() != null ? p.getCompte().getId() : null);
            	psR.setObject(2, p.getBoutique() != null ? p.getBoutique().getId() : null);
            	psR.setObject(3, null); // caisse_id
            	psR.setString(4, p.getNom());
            	psR.setString(5, p.getPrenom());
            	psR.setString(6, p.getEmail());
            	psR.setString(7, p.getTelephone());
            	psR.setDouble(8, p.getSalairePourcentage());
            	psR.executeUpdate();


            ResultSet rs = psR.getGeneratedKeys();
            if (rs.next()) {
                p.setId(rs.getLong(1)); // reparateur id
            }

            // Insert into proprietaire table
            String sqlProprietaire = "INSERT INTO proprietaire (reparateur_id, pourcentageReparation) VALUES (?, ?)";
            PreparedStatement psP = conn.prepareStatement(sqlProprietaire, Statement.RETURN_GENERATED_KEYS);
            psP.setLong(1, p.getId());
            psP.setDouble(2, p.getPourcentageReparation());
            psP.executeUpdate();

            conn.commit();
            return p;
        } catch (Exception e) {
            conn.rollback();
            throw e;
        } finally {
            conn.setAutoCommit(true);
        }
    }

    @Override
    public Optional<Proprietaire> findById(Long id) throws Exception {
        Connection conn = DBConnection.getConnection();
        String sql = "SELECT r.*, p.pourcentageReparation " +
                     "FROM reparateur r JOIN proprietaire p ON r.id = p.reparateur_id " +
                     "WHERE r.id=?";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setLong(1, id);
        ResultSet rs = ps.executeQuery();
        if (rs.next()) {
            Proprietaire p = new Proprietaire();
            p.setId(rs.getLong("id"));
            p.setNom(rs.getString("nom"));
            p.setPrenom(rs.getString("prenom"));
            p.setEmail(rs.getString("email"));
            p.setTelephone(rs.getString("telephone"));
            p.setSalairePourcentage(rs.getDouble("salairePourcentage"));
            p.setPourcentageReparation(rs.getDouble("pourcentageReparation"));

            // Load boutiques owned by this reparateur
            String sqlBoutiques = "SELECT * FROM boutique WHERE proprietaire_id=?";
            PreparedStatement psB = conn.prepareStatement(sqlBoutiques);
            psB.setLong(1, p.getId());
            ResultSet rsB = psB.executeQuery();
            List<Boutique> boutiques = new ArrayList<>();
            while (rsB.next()) {
                Boutique b = new Boutique();
                b.setId(rsB.getLong("id"));
                b.setNom(rsB.getString("nom"));
                b.setAddresse(rsB.getString("addresse"));
                b.setLogoPath(rsB.getString("logoPath"));
                b.setNumPatente(rsB.getString("numPatente"));
                boutiques.add(b);
            }
            p.setBoutiques(boutiques);

            return Optional.of(p);
        }
        return Optional.empty();
    }

    @Override
    public List<Proprietaire> findAll() throws Exception {
        Connection conn = DBConnection.getConnection();
        String sql = "SELECT r.*, p.pourcentageReparation " +
                     "FROM reparateur r JOIN proprietaire p ON r.id = p.reparateur_id";
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery(sql);
        List<Proprietaire> proprietaires = new ArrayList<>();
        while (rs.next()) {
            Proprietaire p = new Proprietaire();
            p.setId(rs.getLong("id"));
            p.setNom(rs.getString("nom"));
            p.setPrenom(rs.getString("prenom"));
            p.setEmail(rs.getString("email"));
            p.setTelephone(rs.getString("telephone"));
            p.setSalairePourcentage(rs.getDouble("salairePourcentage"));
            p.setPourcentageReparation(rs.getDouble("pourcentageReparation"));
            // optionally load boutiques as in findById
            proprietaires.add(p);
        }
        return proprietaires;
    }

    @Override
    public Proprietaire update(Proprietaire p) throws Exception {
        Connection conn = DBConnection.getConnection();
        try {
            conn.setAutoCommit(false);

            // Update reparateur
            String sqlR = "UPDATE reparateur SET nom=?, prenom=?, email=?, telephone=?, salairePourcentage=? WHERE id=?";
            PreparedStatement psR = conn.prepareStatement(sqlR);
            psR.setString(1, p.getNom());
            psR.setString(2, p.getPrenom());
            psR.setString(3, p.getEmail());
            psR.setString(4, p.getTelephone());
            psR.setDouble(5, p.getSalairePourcentage());
            psR.setLong(6, p.getId());
            psR.executeUpdate();

            // Update proprietaire
            String sqlP = "UPDATE proprietaire SET pourcentageReparation=? WHERE reparateur_id=?";
            PreparedStatement psP = conn.prepareStatement(sqlP);
            psP.setDouble(1, p.getPourcentageReparation());
            psP.setLong(2, p.getId());
            psP.executeUpdate();

            conn.commit();
            return p;
        } catch (Exception e) {
            conn.rollback();
            throw e;
        } finally {
            conn.setAutoCommit(true);
        }
    }

    @Override
    public boolean deleteById(Long id) throws Exception {
        Connection conn = DBConnection.getConnection();
        try {
            conn.setAutoCommit(false);

            // Delete proprietaire first
            PreparedStatement psP = conn.prepareStatement("DELETE FROM proprietaire WHERE reparateur_id=?");
            psP.setLong(1, id);
            psP.executeUpdate();

            // Delete reparateur
            PreparedStatement psR = conn.prepareStatement("DELETE FROM reparateur WHERE id=?");
            psR.setLong(1, id);
            int deleted = psR.executeUpdate();

            conn.commit();
            return deleted > 0;
        } catch (Exception e) {
            conn.rollback();
            throw e;
        } finally {
            conn.setAutoCommit(true);
        }
    }

    @Override
    public Optional<Proprietaire> findByEmail(String email) throws Exception {
        Connection conn = DBConnection.getConnection();
        String sql = "SELECT r.*, p.pourcentageReparation FROM reparateur r JOIN proprietaire p ON r.id=p.reparateur_id WHERE r.email=?";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setString(1, email);
        ResultSet rs = ps.executeQuery();
        if (rs.next()) {
            Proprietaire p = new Proprietaire();
            p.setId(rs.getLong("id"));
            p.setNom(rs.getString("nom"));
            p.setPrenom(rs.getString("prenom"));
            p.setEmail(rs.getString("email"));
            p.setTelephone(rs.getString("telephone"));
            p.setSalairePourcentage(rs.getDouble("salairePourcentage"));
            p.setPourcentageReparation(rs.getDouble("pourcentageReparation"));
            return Optional.of(p);
        }
        return Optional.empty();
    }

    @Override
    public Optional<Proprietaire> findByTelephone(String telephone) throws Exception {
        Connection conn = DBConnection.getConnection();
        String sql = "SELECT r.*, p.pourcentageReparation FROM reparateur r JOIN proprietaire p ON r.id=p.reparateur_id WHERE r.telephone=?";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setString(1, telephone);
        ResultSet rs = ps.executeQuery();
        if (rs.next()) {
            Proprietaire p = new Proprietaire();
            p.setId(rs.getLong("id"));
            p.setNom(rs.getString("nom"));
            p.setPrenom(rs.getString("prenom"));
            p.setEmail(rs.getString("email"));
            p.setTelephone(rs.getString("telephone"));
            p.setSalairePourcentage(rs.getDouble("salairePourcentage"));
            p.setPourcentageReparation(rs.getDouble("pourcentageReparation"));
            return Optional.of(p);
        }
        return Optional.empty();
    }
}
