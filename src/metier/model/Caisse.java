package metier.model;

import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import metier.enums.TypeCaisse;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Caisse {
	private long id;
	private double soldeActuel;
	private TypeCaisse type;
	private Reparateur reparateur;
	private List<Dette> dettes=new ArrayList<Dette>();
	private List<Transaction> transactions=new ArrayList<Transaction>();
}
