package translateit2.persistence.model;

import java.io.Serializable;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity(name = "trUnit")
@Table(name = "TR_UNIT")
public class Unit implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    //@Column(columnDefinition = "TEXT")
    @Column(length=10000)
    private String segment_key;

    @ManyToOne
    private Work work;

    @Embedded // @Column(columnDefinition="TEXT")
    @AttributeOverrides({
            @AttributeOverride(name = "text", column = @Column(name = "source_text", length=10000)),
            @AttributeOverride(name = "plural", column = @Column(name = "source_plural", length=10000)),
            @AttributeOverride(name = "skeleton_tag", column = @Column(name = "source_skeleton_tag")) })
    private Source source;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "text", column = @Column(name = "target_text", length=10000)),
            @AttributeOverride(name = "plural", column = @Column(name = "target_plural", length=10000)),
            @AttributeOverride(name = "skeleton_tag", column = @Column(name = "target_skeleton_tag")) })
    private Target target;

    private int serial_number;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSegmentKey() {
        return segment_key;
    }

    public void setSegmentKey(String segmentKey) {
        this.segment_key = segmentKey;
    }

    public Source getSource() {
        return source;
    }

    public void setSource(Source source) {
        this.source = source;
    }

    public Target getTarget() {
        return target;
    }

    public void setTarget(Target target) {
        this.target = target;
    }

    public int getSerialNumber() {
        return serial_number;
    }

    public void setSerialNumber(int serialNumber) {
        this.serial_number = serialNumber;
    }

    public Work getWork() {
        return work;
    }

    public void setWork(Work work) {
        this.work = work;
    }

}
