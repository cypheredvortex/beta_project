package metier.model;

import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Client {
	private long id;
	private String nom;
	private String prenom;
	private String telephone;
	private String email;
	private List<Reparation> reparations=new ArrayList<Reparation>();
	private Photo photo;
}
