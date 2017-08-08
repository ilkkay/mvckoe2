package translateit2.persistence.model;

import java.io.Serializable;
import java.util.Locale;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.apache.commons.lang3.builder.ToStringBuilder;

import translateit2.lngfileservice.LanguageFileFormat;
import translateit2.lngfileservice.LanguageFileType;

@Entity(name = "trProject")
@Table(name = "TR_PROJECT")
public class Project implements Serializable {
    private static final long serialVersionUID = 1L;

    @Enumerated(EnumType.STRING)
    private LanguageFileFormat format;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    
    @ManyToOne
    private Info info;;
    
    private String name;

    @ManyToOne
    private Person person;

    private Locale source_locale;

    @Enumerated(EnumType.STRING)
    private LanguageFileType type;

    public LanguageFileFormat getFormat() {
        return format;
    }

    public Long getId() {
        return id;
    }

    public Info getInfo() {
        return info;
    };
    
    public String getName() {
        return name;
    }

    public Person getPerson() {
        return person;
    }

    public Locale getSourceLocale() {
        return source_locale;
    }

    public LanguageFileType getType() {
        return type;
    }

    public void setFormat(LanguageFileFormat format) {
        this.format = format;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setInfo(Info info) {
        this.info = info;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPerson(Person person) {
        this.person = person;
    }

    public void setSourceLocale(Locale sourceLocale) {
        this.source_locale = sourceLocale;
    }

    public void setType(LanguageFileType type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("id", id).append("person", person).append("info", info)
                .append("name", name).append("format", format).append("type", type).append("sourceLocale", source_locale)
                .toString();
    }
}