package metier.model;

import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Reparateur {
	private long id;
	private String nom;
	private String prenom;
	private String email;
	private String telephone;
	private double salairePourcentage;
	private List<Reparation> reparation=new ArrayList<Reparation>();
	private Compte compte;
	private Boutique boutique;
	private Caisse caisse;
}
