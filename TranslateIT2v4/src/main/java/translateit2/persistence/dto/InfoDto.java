package translateit2.persistence.dto;

import org.apache.commons.lang3.builder.ToStringBuilder;

public class InfoDto {
	private long id;

	private String text;

	
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}
	
	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}
	
    @Override
    public String toString() {
        return new ToStringBuilder(this)
        		.append("id",id)	
        		.append("text",text).toString();
    }
}
