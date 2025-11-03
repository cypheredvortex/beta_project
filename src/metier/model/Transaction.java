package metier.model;

import java.sql.Date;
import java.sql.Timestamp;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import metier.enums.TypeTransaction;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Transaction {
	private long id;
	private Timestamp date;
	private double montant;
	private TypeTransaction type;
	private String description;
	private String contrepartie;
	private Caisse caisse;
}
