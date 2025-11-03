package dao.impl;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import dao.DBConnection;
import dao.interfaces.BoutiqueDao;
import metier.model.Boutique;

public class BoutiqueDaoImpl implements BoutiqueDao {

    @Override
    public Boutique save(Boutique boutique) throws Exception {
        String sql = "INSERT INTO boutique (nom, addresse, logo_path, num_patente, proprietaire_id) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, boutique.getNom());
            ps.setString(2, boutique.getAddresse());
            ps.setString(3, boutique.getLogoPath());
            ps.setString(4, boutique.getNumPatente());
            ps.setObject(5, boutique.getProprietaire() != null ? boutique.getProprietaire().getId() : null);

            ps.executeUpdate();

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) boutique.setId(rs.getLong(1));
            }
        }
        return boutique;
    }

    @Override
    public Optional<Boutique> findById(int id) throws Exception {
        String sql = "SELECT * FROM boutique WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return Optional.of(mapBoutique(rs));
            }
        }
        return Optional.empty();
    }

    @Override
    public List<Boutique> findAll() throws Exception {
        List<Boutique> boutiques = new ArrayList<>();
        String sql = "SELECT * FROM boutique";
        try (Connection conn = DBConnection.getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) boutiques.add(mapBoutique(rs));
        }
        return boutiques;
    }

    @Override
    public List<Boutique> findByProprietaireId(long proprietaireId) throws Exception {
        List<Boutique> boutiques = new ArrayList<>();
        String sql = "SELECT * FROM boutique WHERE proprietaire_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setLong(1, proprietaireId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) boutiques.add(mapBoutique(rs));
            }
        }
        return boutiques;
    }

    @Override
    public Boutique update(Boutique boutique) throws Exception {
        String sql = "UPDATE boutique SET nom=?, addresse=?, logo_path=?, num_patente=?, proprietaire_id=? WHERE id=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, boutique.getNom());
            ps.setString(2, boutique.getAddresse());
            ps.setString(3, boutique.getLogoPath());
            ps.setString(4, boutique.getNumPatente());
            ps.setObject(5, boutique.getProprietaire() != null ? boutique.getProprietaire().getId() : null);
            ps.setLong(6, boutique.getId());

            ps.executeUpdate();
        }
        return boutique;
    }

    @Override
    public boolean deleteById(int id) throws Exception {
        String sql = "DELETE FROM boutique WHERE id=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        }
    }

    private Boutique mapBoutique(ResultSet rs) throws SQLException {
        Boutique b = new Boutique();
        b.setId(rs.getLong("id"));
        b.setNom(rs.getString("nom"));
        b.setAddresse(rs.getString("addresse"));
        b.setLogoPath(rs.getString("logo_path"));
        b.setNumPatente(rs.getString("num_patente"));

        long proprietaireId = rs.getLong("proprietaire_id");
        if (!rs.wasNull()) {
            b.setProprietaire(new metier.model.Proprietaire());
            b.getProprietaire().setId(proprietaireId);
        }

        return b;
    }
}
