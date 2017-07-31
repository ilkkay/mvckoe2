package translate.it2.version1.dao;

import java.util.List;
import translate.it2.version1.model.Transu;

public interface LocoDao {
	public int save(Transu t);
	public int update(Transu t);
	public Transu getTransuById(int id);
	public int delete(int id);
	public List<Transu> getTransus();
}
