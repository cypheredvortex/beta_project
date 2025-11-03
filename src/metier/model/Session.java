package metier.model;

import java.sql.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Session {
	private long id;
	private String token;
	private Date dateCreation;
	private Date dateExpiration;
	private Compte compte;
}
