package translateit2.persistence.dto;

import java.time.LocalDate;
import java.util.Locale;

import javax.validation.constraints.NotNull;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.hibernate.validator.constraints.NotEmpty;

import translateit2.persistence.model.Priority;
import translateit2.persistence.model.Status;
import translateit2.validator.WorkConstraint;

@WorkConstraint
public class WorkDto {
    private Long id;

    private long projectId;

    private long groupId;
    
    private long fileinfoId;

    @NotNull
    private Locale locale;

    @NotEmpty
    private String version;

    private String originalFile;

    private String skeletonFile;

    private String backupFile;

    private Status status;

    @NotNull
    private Priority priority;

    private double progress;

    private LocalDate started;

    private LocalDate finished;

    @NotNull
    private LocalDate deadLine;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public long getProjectId() {
        return projectId;
    }

    public void setProjectId(long projectId) {
        this.projectId = projectId;
    }

    public long getGroupId() {
        return groupId;
    }

    public void setGroupId(long groupId) {
        this.groupId = groupId;
    }

    public long getFileinfoId() {
        return fileinfoId;
    }

    public void setFileInfoId(long fileinfoId) {
        this.fileinfoId = fileinfoId;
    }
    
    public Locale getLocale() {
        return locale;
    }

    public void setLocale(Locale locale) {
        this.locale = locale;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getOriginalFile() {
        return originalFile;
    }

    public void setOriginalFile(String originalFile) {
        this.originalFile = originalFile;
    }

    public String getSkeletonFile() {
        return skeletonFile;
    }

    public void setSkeletonFile(String skeletonFile) {
        this.skeletonFile = skeletonFile;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Priority getPriority() {
        return priority;
    }

    public void setPriority(Priority priority) {
        this.priority = priority;
    }

    public double getProgress() {
        return progress;
    }

    public void setProgress(double progress) {
        this.progress = progress;
    }

    public LocalDate getStarted() {
        return started;
    }

    public void setStarted(LocalDate started) {
        this.started = started;
    }

    public LocalDate getFinished() {
        return finished;
    }

    public void setFinished(LocalDate finished) {
        this.finished = finished;
    }

    public LocalDate getDeadLine() {
        return deadLine;
    }

    public void setDeadLine(LocalDate deadLine) {
        this.deadLine = deadLine;
    }

    public String getBackupFile() {
        return backupFile;
    }

    public void setBackupFile(String backupFile) {
        this.backupFile = backupFile;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("id", id).append("groupId", groupId).append("projectId", projectId)
                .append("locale", locale).append("version", version).append("originalFile", originalFile)
                .append("skeletonFile", skeletonFile).append("backupFile", backupFile).append("status", status)
                .append("progress", progress).append("started", started).append("finished", finished).toString();
    }

}
