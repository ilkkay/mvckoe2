package translateit2.persistence.dto;

import org.apache.commons.lang3.builder.ToStringBuilder;

public class PersonDto {
	private long id;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	private String fullName;

	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}
	
    @Override
    public String toString() {
        return new ToStringBuilder(this)
        		.append("id",id)	
        		.append("fullName",fullName).toString();
    }
}
