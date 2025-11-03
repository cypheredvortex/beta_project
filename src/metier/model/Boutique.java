package metier.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Boutique {
	private long id;
	private String nom;
	private String addresse;
	private String logoPath;
	private String numPatente;
	private Proprietaire proprietaire;
}
