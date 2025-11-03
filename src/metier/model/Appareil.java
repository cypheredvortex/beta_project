package metier.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import metier.enums.TypeProbleme;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Appareil {
	private long id;
	private String type;
	private String marque;
	private String modele;
	private String numeroSerie;
	private String probleme;
	private TypeProbleme typeProbleme;
	private Reparation reparation;
}
