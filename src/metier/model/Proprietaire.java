package metier.model;

import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Proprietaire extends Reparateur {
	private long id;
	private List<Boutique> boutiques=new ArrayList<Boutique>();
}
