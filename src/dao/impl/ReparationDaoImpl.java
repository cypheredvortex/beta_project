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
        String sql = "INSERT INTO reparation (code_unique, date_creation, statut, description, prix_convenu, prix_total_pieces, remarques, client_id, client_phone, reparateur_id) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
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
            ps.setString(9, r.getClient() != null ? r.getClient().getTelephone() : null);
            ps.setObject(10, r.getReparateur() != null ? r.getReparateur().getId() : null);
            ps.executeUpdate();
            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) r.setId(keys.getLong(1));
            }
        }
        return r;
    }

    @Override
    public Reparation update(Reparation r) throws Exception {
        String sql = "UPDATE reparation SET code_unique=?, date_creation=?, statut=?, description=?, prix_convenu=?, prix_total_pieces=?, remarques=?, client_id=?, client_phone=?, reparateur_id=? WHERE id=?";
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
            ps.setString(9, r.getClient() != null ? r.getClient().getTelephone() : null);
            ps.setObject(10, r.getReparateur() != null ? r.getReparateur().getId() : null);
            ps.setLong(11, r.getId());
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
        String sql = "SELECT * FROM reparation WHERE code_unique=?";
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
    public List<Reparation> findByClientPhone(String phone) throws Exception {
        List<Reparation> list = new ArrayList<>();
        String sql = "SELECT * FROM reparation WHERE client_phone=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, phone);
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
        String sql = "SELECT * FROM reparation WHERE date_creation=?";
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
        r.setCodeUnique(rs.getString("code_unique"));
        Timestamp ts = rs.getTimestamp("date_creation");
        if (ts != null) r.setDateCreation(new Date(ts.getTime()));
        String statut = rs.getString("statut");
        if (statut != null) r.setStatut(EtatReparation.valueOf(statut));
        r.setDescription(rs.getString("description"));
        r.setPrixConvenu(rs.getDouble("prix_convenu"));
        r.setPrixTotalPieces(rs.getDouble("prix_total_pieces"));
        r.setRemarques(rs.getString("remarques"));

        long clientId = rs.getLong("client_id");
        if (!rs.wasNull()) {
            r.setClient(new Client());
            r.getClient().setId(clientId);
            r.getClient().setTelephone(rs.getString("client_phone"));
        }

        long reparateurId = rs.getLong("reparateur_id");
        if (!rs.wasNull()) r.setReparateur(new Reparateur(reparateurId, statut, statut, statut, statut, reparateurId, null, null, null, null));

        return r;
    }
}
