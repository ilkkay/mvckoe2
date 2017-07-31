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

    @Column(columnDefinition = "TEXT")
    private String segmentKey;

    @ManyToOne
    private Work work;

    @Embedded // @Column(columnDefinition="TEXT")
    @AttributeOverrides({
            @AttributeOverride(name = "text", column = @Column(name = "sourceText", columnDefinition = "TEXT")),
            @AttributeOverride(name = "plural", column = @Column(name = "sourcePlural", columnDefinition = "TEXT")),
            @AttributeOverride(name = "skeletonTag", column = @Column(name = "sourceSkeletonTag")) })
    private Source source;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "text", column = @Column(name = "targetText", columnDefinition = "TEXT")),
            @AttributeOverride(name = "plural", column = @Column(name = "targetPlural", columnDefinition = "TEXT")),
            @AttributeOverride(name = "skeletonTag", column = @Column(name = "targetSkeletonTag")) })
    private Target target;

    private int serialNumber;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSegmentKey() {
        return segmentKey;
    }

    public void setSegmentKey(String segmentKey) {
        this.segmentKey = segmentKey;
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
        return serialNumber;
    }

    public void setSerialNumber(int serialNumber) {
        this.serialNumber = serialNumber;
    }

    public Work getWork() {
        return work;
    }

    public void setWork(Work work) {
        this.work = work;
    }

}
