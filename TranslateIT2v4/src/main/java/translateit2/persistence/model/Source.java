package translateit2.persistence.model;
import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Embeddable
public class Source {

    @Column(columnDefinition="TEXT")
	private String text;
	
	@Column(columnDefinition="TEXT")
	private String plural;
	
	private String skeletonTag;

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public String getPlural() {
		return plural;
	}

	public void setPlural(String plural) {
		this.plural = plural;
	}

	public String getSkeletonTag() {
		return skeletonTag;
	}

	public void setSkeletonTag(String skeletonTag) {
		this.skeletonTag = skeletonTag;
	}

	
	
}
