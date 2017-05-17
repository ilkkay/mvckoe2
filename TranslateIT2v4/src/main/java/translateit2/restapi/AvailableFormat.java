package translateit2.restapi;

import translateit2.lngfileservice.LngFileFormat;

public class AvailableFormat {
	private LngFileFormat type;

	private long id;
	
	public LngFileFormat getType() {
		return type;
	}

	public void setType(LngFileFormat type) {
		this.type = type;
	}

	public AvailableFormat(LngFileFormat type) {
		super();
		this.type = type;
	}	
	
}
