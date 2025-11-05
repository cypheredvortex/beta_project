package metier.services;

import java.util.List;
import java.util.Optional;

import metier.model.Boutique;

public interface IBoutiqueService {
    Boutique creerBoutique(Boutique boutique) throws Exception;
    Boutique modifierBoutique(Boutique boutique) throws Exception;
    boolean supprimerBoutique(int id) throws Exception;
    Optional<Boutique> trouverParId(int id) throws Exception;
    List<Boutique> listerBoutiques() throws Exception;
    List<Boutique> listerParProprietaire(long proprietaireId) throws Exception;
}
