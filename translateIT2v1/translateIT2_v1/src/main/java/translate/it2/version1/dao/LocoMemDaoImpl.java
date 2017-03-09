package translate.it2.version1.dao;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import translate.it2.version1.util.ISO8859Loader;
import translate.it2.version1.model.Transu;

public class LocoMemDaoImpl implements LocoDao{
	private List <Transu> transus = null;
	private boolean isInitialized=false;
	
	public int save(Transu t){
		if (transus == null){
			try {
			    	transus=ISO8859Loader.getTransus();
			    	isInitialized = true;
			    } catch (IOException e) {
			        e.printStackTrace();
			}
		}
		int largestId=transus.get((transus.size()-1)).getId()+1;
		t.setId(largestId);
		transus.add(t);
		return t.getId();
	}
	
	public int update(Transu t){	
		int index = getListPosition(t.getId());
		transus.set(index, t);
		return t.getId();
	}

	public int delete(int id){
		int index = getListPosition(id);
		transus.remove(index);
		return id;
	}
	
	public Transu getTransuById(int id){
		int index = getListPosition(id);
		Transu t=transus.get(index);
		return(t);
	}
	
	private int getListPosition(int id){
		int pos = 0;
		boolean isFound=false;
		Transu t = null;
		while (!isFound){
			t = transus.get(pos);
			if(id == t.getId()){
				isFound=true;
			}
			pos++;
			if (pos >= transus.size() && (!isFound))
			{
				isFound=true;
				pos = 0;
			}
			
		}
		return pos - 1;
	}
	public List<Transu> getTransus(){
		if (!isInitialized){
		    try {
		    	transus=ISO8859Loader.getTransus();
		    	isInitialized = true;
		    } catch (IOException e) {
		        e.printStackTrace();
		    }
		}
		
		return transus;
	}

}

