package metier.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Piece {
	private long id;
	private String nom;
	private double prixUnitaire;
	private String etatRemplacement;
	private Appareil appareil;
}
