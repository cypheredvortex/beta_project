package metier.model;

import java.sql.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import metier.enums.SourcePhoto;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Photo {
	private long id;
	private String chemin;
	private Date datePrise;
	private SourcePhoto source;
	private Reparation reparation;
	private Client client;
}
