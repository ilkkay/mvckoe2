package translateit2.persistence.model;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Locale;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;

@Entity(name = "trWork")
@Table(name = "TR_WORK")
public class Work implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    private Project project;

    @ManyToOne
    private TranslatorGroup group;

    @OneToOne
    private FileInfo fileinfo;
    
    private Locale locale;

    private String version;

    private String original_file;

    private String backup_file;

    private String skeleton_file;

    @Enumerated(EnumType.STRING)
    private Status status;

    @Enumerated(EnumType.STRING)
    private Priority priority;

    private double progress;

    @DateTimeFormat(iso = ISO.DATE)
    private LocalDate started;

    @DateTimeFormat(iso = ISO.DATE)
    private LocalDate finished;

    @DateTimeFormat(iso = ISO.DATE)
    private LocalDate deadLine;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
    }

    public TranslatorGroup getGroup() {
        return group;
    }

    public void setGroup(TranslatorGroup group) {
        this.group = group;
    }

    public FileInfo getFileinfo() {
        return fileinfo;
    }

    public void setFileinfo(FileInfo fileinfo) {
        this.fileinfo = fileinfo;
    }

    public String getOriginalFile() {
        return original_file;
    }

    public void setOriginalFile(String originalFile) {
        this.original_file = originalFile;
    }

    public String getSkeletonFile() {
        return skeleton_file;
    }

    public void setSkeletonFile(String skeletonFile) {
        this.skeleton_file = skeletonFile;
    }

    public String getBackupFile() {
        return backup_file;
    }

    public void setBackupFile(String backupFile) {
        this.backup_file = backupFile;
    }
}
