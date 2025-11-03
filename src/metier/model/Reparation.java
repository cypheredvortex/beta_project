package metier.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import metier.enums.EtatReparation;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Reparation {
	private long id;
	private String codeUnique;
	private Date dateCreation;
	private EtatReparation statut;
	private String description;
	private double prixConvenu;
	private double prixTotalPieces;
	private String remarques;
	private Reparateur reparateur;
	private Client client;
	private List<Appareil> appareils=new ArrayList<Appareil>();
}
