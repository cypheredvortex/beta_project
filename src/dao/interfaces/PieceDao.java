package dao.interfaces;

import java.util.List;
import java.util.Optional;

import metier.model.Piece;

public interface PieceDao {
    Piece save(Piece piece) throws Exception;
    Optional<Piece> findById(Long id) throws Exception;
    List<Piece> findByAppareilId(Long appareilId) throws Exception;
    List<Piece> findAll() throws Exception;
    Piece update(Piece piece) throws Exception;
    boolean deleteById(Long id) throws Exception;
    List<Piece> findByNom(String nom) throws Exception;
    List<Piece> findByEtatRemplacement(String etatRemplacement) throws Exception;
}