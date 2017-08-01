package translateit2.persistence.model;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import org.apache.commons.lang3.builder.ToStringBuilder;

@Embeddable
public class Target {
    //@Column(columnDefinition = "TEXT")
    @Column(length=10000)
    private String text;

    @Column(length=10000)
    private String plural;

    private String skeleton_tag;

    private boolean equivalent;

    State state;

    // Alternatives alternatives;

    Comment comment;

    History history;

    Note note;

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

    public boolean isEquivalent() {
        return equivalent;
    }

    public void setEquivalent(boolean equivalent) {
        this.equivalent = equivalent;
    }

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }

    public Comment getComment() {
        return comment;
    }

    public void setComment(Comment comment) {
        this.comment = comment;
    }

    public History getHistory() {
        return history;
    }

    public void setHistory(History history) {
        this.history = history;
    }

    public Note getNote() {
        return note;
    }

    public void setNote(Note note) {
        this.note = note;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("text", text).append("plural", plural).append("state", state)
                .append("equivalent", equivalent).append("skeletonTag", skeleton_tag).append("comment", comment)
                .append("history", history).append("note", note).toString();
    }
}
