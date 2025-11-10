package dao.impl;

import java.sql.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import dao.DBConnection;
import dao.interfaces.ReparationDao;
import metier.enums.EtatReparation;
import metier.model.Client;
import metier.model.Reparateur;
import metier.model.Reparation;

public class ReparationDaoImpl implements ReparationDao {

    @Override
    public Reparation save(Reparation r) throws Exception {
        String sql = "INSERT INTO reparation (codeUnique, dateCreation, statut, description, prixConvenu, prixTotalPieces, remarques, client_id, reparateur_id) VALUES ( ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, r.getCodeUnique());
            ps.setTimestamp(2, new Timestamp(r.getDateCreation().getTime()));
            ps.setString(3, r.getStatut().name());
            ps.setString(4, r.getDescription());
            ps.setDouble(5, r.getPrixConvenu());
            ps.setDouble(6, r.getPrixTotalPieces());
            ps.setString(7, r.getRemarques());
            ps.setObject(8, r.getClient() != null ? r.getClient().getId() : null);
            ps.setObject(9, r.getReparateur() != null ? r.getReparateur().getId() : null);
            ps.executeUpdate();
            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) r.setId(keys.getLong(1));
            }
        }
        return r;
    }

    @Override
    public Reparation update(Reparation r) throws Exception {
        String sql = "UPDATE reparation SET codeUnique=?, dateCreation=?, statut=?, description=?, prixConvenu=?, prixTotalPieces=?, remarques=?, client_id=?, reparateur_id=? WHERE id=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, r.getCodeUnique());
            ps.setTimestamp(2, new Timestamp(r.getDateCreation().getTime()));
            ps.setString(3, r.getStatut().name());
            ps.setString(4, r.getDescription());
            ps.setDouble(5, r.getPrixConvenu());
            ps.setDouble(6, r.getPrixTotalPieces());
            ps.setString(7, r.getRemarques());
            ps.setObject(8, r.getClient() != null ? r.getClient().getId() : null);
            ps.setObject(9, r.getReparateur() != null ? r.getReparateur().getId() : null);
            ps.setLong(10, r.getId());
            ps.executeUpdate();
        }
        return r;
    }

    @Override
    public boolean deleteById(Long id) throws Exception {
        String sql = "DELETE FROM reparation WHERE id=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, id);
            return ps.executeUpdate() > 0;
        }
    }

    @Override
    public Optional<Reparation> findById(Long id) throws Exception {
        String sql = "SELECT * FROM reparation WHERE id=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return Optional.of(mapReparation(rs));
            }
        }
        return Optional.empty();
    }

    @Override
    public Optional<Reparation> findByCodeUnique(String codeUnique) throws Exception {
        String sql = "SELECT * FROM reparation WHERE codeUnique=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, codeUnique);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return Optional.of(mapReparation(rs));
            }
        }
        return Optional.empty();
    }

    @Override
    public List<Reparation> findAll() throws Exception {
        List<Reparation> list = new ArrayList<>();
        String sql = "SELECT * FROM reparation";
        try (Connection conn = DBConnection.getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) list.add(mapReparation(rs));
        }
        return list;
    }

    @Override
    public List<Reparation> findByReparateurId(Long reparateurId) throws Exception {
        List<Reparation> list = new ArrayList<>();
        String sql = "SELECT * FROM reparation WHERE reparateur_id=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, reparateurId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) list.add(mapReparation(rs));
            }
        }
        return list;
    }



    @Override
    public List<Reparation> findByStatut(EtatReparation statut) throws Exception {
        List<Reparation> list = new ArrayList<>();
        String sql = "SELECT * FROM reparation WHERE statut=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, statut.name());
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) list.add(mapReparation(rs));
            }
        }
        return list;
    }

    @Override
    public List<Reparation> findByDateCreation(Date date) throws Exception {
        List<Reparation> list = new ArrayList<>();
        String sql = "SELECT * FROM reparation WHERE dateCreation=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setDate(1, new java.sql.Date(date.getTime()));
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) list.add(mapReparation(rs));
            }
        }
        return list;
    }

    private Reparation mapReparation(ResultSet rs) throws SQLException {
        Reparation r = new Reparation();
        r.setId(rs.getLong("id"));
        r.setCodeUnique(rs.getString("codeUnique"));
        Timestamp ts = rs.getTimestamp("dateCreation");
        if (ts != null) r.setDateCreation(new Date(ts.getTime()));
        String statut = rs.getString("statut");
        if (statut != null) r.setStatut(EtatReparation.valueOf(statut));
        r.setDescription(rs.getString("description"));
        r.setPrixConvenu(rs.getDouble("prixConvenu"));
        r.setPrixTotalPieces(rs.getDouble("prixTotalPieces"));
        r.setRemarques(rs.getString("remarques"));

        // 🔹 Récupérer le client complet
        long clientId = rs.getLong("client_id");
        if (!rs.wasNull()) {
            try {
                r.setClient(new dao.impl.ClientDaoImpl().findById((int) clientId).orElse(null));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        // 🔹 Récupérer le réparateur complet
        long reparateurId = rs.getLong("reparateur_id");
        if (!rs.wasNull()) {
            try {
                Optional<Reparateur> repOpt = new metier.servicesImpl.ReparateurServiceImpl().trouverParId(reparateurId);
                repOpt.ifPresent(r::setReparateur); // affecte le réparateur complet avec nom + prénom
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        // 🔹 Récupérer la liste des appareils
        try {
            r.setAppareils(new dao.impl.AppareilDaoImpl().findByReparationId(r.getId()));
        } catch (Exception e) {
            e.printStackTrace();
        }

        return r;
    }

}
