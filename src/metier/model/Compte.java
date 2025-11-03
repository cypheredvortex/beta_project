package metier.model;

import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import metier.enums.RoleCompte;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Compte {
	private long id;
	private String login;
	private String motDePasse;
	private RoleCompte role;
	private boolean actif;
	private Reparateur reparateur;
	private List<Session> sessions=new ArrayList<Session>();
}
