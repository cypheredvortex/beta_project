package dao.impl;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import dao.DBConnection;
import dao.interfaces.PieceDao;
import metier.model.Piece;

public class PieceDaoImpl implements PieceDao {

    @Override
    public Piece save(Piece piece) throws Exception {
        String sql = "INSERT INTO piece (nom, prix_unitaire, etat_remplacement, appareil_id) VALUES (?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, piece.getNom());
            ps.setDouble(2, piece.getPrixUnitaire());
            ps.setString(3, piece.getEtatRemplacement());
            ps.setObject(4, piece.getAppareil() != null ? piece.getAppareil().getId() : null);
            ps.executeUpdate();
            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) piece.setId(keys.getLong(1));
            }
        }
        return piece;
    }

    @Override
    public Piece update(Piece piece) throws Exception {
        String sql = "UPDATE piece SET nom=?, prix_unitaire=?, etat_remplacement=?, appareil_id=? WHERE id=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, piece.getNom());
            ps.setDouble(2, piece.getPrixUnitaire());
            ps.setString(3, piece.getEtatRemplacement());
            ps.setObject(4, piece.getAppareil() != null ? piece.getAppareil().getId() : null);
            ps.setLong(5, piece.getId());
            ps.executeUpdate();
        }
        return piece;
    }

    @Override
    public boolean deleteById(Long id) throws Exception {
        String sql = "DELETE FROM piece WHERE id=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, id);
            return ps.executeUpdate() > 0;
        }
    }

    @Override
    public Optional<Piece> findById(Long id) throws Exception {
        String sql = "SELECT * FROM piece WHERE id=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return Optional.of(mapPiece(rs));
            }
        }
        return Optional.empty();
    }

    @Override
    public List<Piece> findAll() throws Exception {
        List<Piece> list = new ArrayList<>();
        String sql = "SELECT * FROM piece";
        try (Connection conn = DBConnection.getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) list.add(mapPiece(rs));
        }
        return list;
    }

    @Override
    public List<Piece> findByAppareilId(Long appareilId) throws Exception {
        List<Piece> list = new ArrayList<>();
        String sql = "SELECT * FROM piece WHERE appareil_id=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, appareilId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) list.add(mapPiece(rs));
            }
        }
        return list;
    }

    @Override
    public List<Piece> findByNom(String nom) throws Exception {
        List<Piece> list = new ArrayList<>();
        String sql = "SELECT * FROM piece WHERE nom=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, nom);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) list.add(mapPiece(rs));
            }
        }
        return list;
    }

    @Override
    public List<Piece> findByEtatRemplacement(String etatRemplacement) throws Exception {
        List<Piece> list = new ArrayList<>();
        String sql = "SELECT * FROM piece WHERE etat_remplacement=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, etatRemplacement);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) list.add(mapPiece(rs));
            }
        }
        return list;
    }

    private Piece mapPiece(ResultSet rs) throws SQLException {
        Piece p = new Piece();
        p.setId(rs.getLong("id"));
        p.setNom(rs.getString("nom"));
        p.setPrixUnitaire(rs.getDouble("prix_unitaire"));
        p.setEtatRemplacement(rs.getString("etat_remplacement"));

        long appareilId = rs.getLong("appareil_id");
        if (!rs.wasNull()) {
            metier.model.Appareil appareil = new metier.model.Appareil();
            appareil.setId(appareilId);
            p.setAppareil(appareil);
        }

        return p;
    }
}
