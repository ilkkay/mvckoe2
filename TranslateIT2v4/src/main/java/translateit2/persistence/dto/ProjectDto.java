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
    private long id;

    private long personId;

    private long infoId;

    @NotBlank // The string is not null and the length is greater than zero
    private String name;

    /*
     * @NotEmpty The CharSequence, Collection, Map or Array object cannot be
     * null and not empty (size > 0).
     */
    @NotNull
    private LanguageFileFormat format;

    @NotNull
    private LanguageFileType type;

    @NotNull
    private Locale sourceLocale;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getPersonId() {
        return personId;
    }

    public void setPersonId(long personId) {
        this.personId = personId;
    }

    public long getInfoId() {
        return infoId;
    }

    public void setInfoId(long infoId) {
        this.infoId = infoId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LanguageFileFormat getFormat() {
        return format;
    }

    public void setFormat(LanguageFileFormat format) {
        this.format = format;
    }

    public LanguageFileType getType() {
        return type;
    }

    public void setType(LanguageFileType type) {
        this.type = type;
    }

    public Locale getSourceLocale() {
        return sourceLocale;
    }

    public void setSourceLocale(Locale sourceLocale) {
        this.sourceLocale = sourceLocale;
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(this.id).append(this.personId).append(this.infoId).append(this.name)
                .append(this.format).append(this.type).append(this.sourceLocale).toHashCode();
    }

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

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("id", id).append("personId", personId).append("infoId", infoId)
                .append("name", name).append("format", format).append("type", type).append("sourceLocale", sourceLocale)
                .toString();
    }
}
