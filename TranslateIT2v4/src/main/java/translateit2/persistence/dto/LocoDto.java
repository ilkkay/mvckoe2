package translateit2.persistence.dto;

import java.util.HashSet;
import java.util.Set;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.NotEmpty;

import translateit2.validator.LocoConstraint;

@LocoConstraint
public class LocoDto {
	private Long id;
	
	@NotEmpty
	private String name;
	
    @Size(
            min = 5,
            max = 35,
            message = "The project name '${validatedValue}' must be between {min} and {max} characters long"
    )
	private String projectName;
	
    //NotBlank
	private String origFilename;
    
    //NotBlank
	private String targetFilename;
	
	@Pattern(regexp="[a-z]{2}_[A-Z]{2}$")
	private String origLocale;
	
	@Pattern(regexp="[a-z]{2}_[A-Z]{2}$")
	private String targetLocale;
	
	public LocoDto() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	// TODO: => transu => transuDto
	// A new problem arises. convertToEntity(locoDto)maps locoID(long) to null
	// when it should be mapped to the current loco object
	private Set<TransuDto> transus = new HashSet<TransuDto>();

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@NotNull
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getProjectName() {
		return projectName;
	}

	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}

	public String getOrigFilename() {
		return origFilename;
	}

	public void setOrigFilename(String origFilename) {
		this.origFilename = origFilename;
	}

	public String getTargetFilename() {
		return targetFilename;
	}

	public void setTargetFilename(String targetFilename) {
		this.targetFilename = targetFilename;
	}

	public Set<TransuDto> getTransus() {
		return transus;
	}

	public void setTransus(Set<TransuDto> transus) {
		this.transus = transus;
	}
	
	public TransuDto getTransuByRowId(long rowId){
		return transus.stream().filter(t->rowId==t.getRowId())
		.findAny()									// If 'findAny' then return found
		.orElse(null);								// If not found, return null
	}

	public String getOrigLocale() {
		return origLocale;
	}

	public void setOrigLocale(String origLocale) {
		this.origLocale = origLocale;
	}

	public String getTargetLocale() {
		return targetLocale;
	}

	public void setTargetLocale(String targetLocale) {
		this.targetLocale = targetLocale;
	}	
}
