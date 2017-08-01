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

import org.apache.commons.lang3.builder.ToStringBuilder;

@Embeddable
public class Source {

    //@Column(columnDefinition = "TEXT")
    @Column(length=10000)
    private String text;

    @Column(length=10000)
    private String plural;

    private String skeleton_tag;

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
        return skeleton_tag;
    }

    public void setSkeletonTag(String skeletonTag) {
        this.skeleton_tag = skeletonTag;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("text", text).append("plural", plural)
                .append("skeletonTag", skeleton_tag).toString();
    }
}
