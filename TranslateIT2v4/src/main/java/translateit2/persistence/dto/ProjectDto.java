package translateit2.persistence.dto;

import java.util.Locale;

import javax.validation.constraints.NotNull;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.hibernate.validator.constraints.NotBlank;

import translateit2.lngfileservice.LanguageFileFormat;
import translateit2.lngfileservice.LanguageFileType;
import translateit2.validator.ProjectConstraint;

@ProjectConstraint
public class ProjectDto {
    /*
     * @NotEmpty The CharSequence, Collection, Map or Array object cannot be
     * null and not empty (size > 0).
     */
    @NotNull
    private LanguageFileFormat format;

    private long id;

    private long infoId;

    @NotBlank // The string is not null and the length is greater than zero
    private String name;

    private long personId;

    @NotNull
    private Locale sourceLocale;

    @NotNull
    private LanguageFileType type;

    /*
     * All relevant fields should be included in the calculation of equals.
     * Derived fields may be ignored. In particular, any field used in
     * generating a hash code must be used in the equals method, and vice versa.
     */
    @Override
    public boolean equals(Object obj) {
        if (obj instanceof ProjectDto == false) {
            return false;
        }
        if (this == obj) {
            return true;
        }
        final ProjectDto otherObject = (ProjectDto) obj;

        return new EqualsBuilder().append(this.id, otherObject.id).append(this.personId, otherObject.personId)
                .append(this.infoId, otherObject.infoId).append(this.name, otherObject.name)
                .append(this.format, otherObject.format).append(this.type, otherObject.type)
                .append(this.sourceLocale, otherObject.sourceLocale).isEquals();
    }

    public LanguageFileFormat getFormat() {
        return format;
    }

    public long getId() {
        return id;
    }

    public long getInfoId() {
        return infoId;
    }

    public String getName() {
        return name;
    }

    public long getPersonId() {
        return personId;
    }

    public Locale getSourceLocale() {
        return sourceLocale;
    }

    public LanguageFileType getType() {
        return type;
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(this.id).append(this.personId).append(this.infoId).append(this.name)
                .append(this.format).append(this.type).append(this.sourceLocale).toHashCode();
    }

    public void setFormat(LanguageFileFormat format) {
        this.format = format;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setInfoId(long infoId) {
        this.infoId = infoId;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPersonId(long personId) {
        this.personId = personId;
    }

    public void setSourceLocale(Locale sourceLocale) {
        this.sourceLocale = sourceLocale;
    }

    public void setType(LanguageFileType type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("id", id).append("personId", personId).append("infoId", infoId)
                .append("name", name).append("format", format).append("type", type).append("sourceLocale", sourceLocale)
                .toString();
    }
}
