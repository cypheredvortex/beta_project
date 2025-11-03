package metier.model;

import java.sql.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Dette {
	private long id;
	private double montant;
	private String donnepar;
	private String recuPar;
	private Date date;
	private boolean reglee;
	private Caisse caisse;
}
