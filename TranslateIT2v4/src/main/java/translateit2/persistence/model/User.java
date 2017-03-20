package translateit2.persistence.model;

import java.io.Serializable;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.Set;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SecondaryTable;
import javax.persistence.SecondaryTables;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.PrimaryKeyJoinColumn;

/*
@Entity
@Table(name = "USER")
@SecondaryTables({
	  @SecondaryTable(name="USER_COMMENTS", pkJoinColumns={
	      @PrimaryKeyJoinColumn(name="userComment", referencedColumnName="comment"),
	      @PrimaryKeyJoinColumn(name="ID", referencedColumnName="ID")})
	})
*/
public class User implements Serializable{
	/**
	 * Probably not needed ...
	 */
	private static final long serialVersionUID = 1L;

	//@Id
	//@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	
	private String userName;
	// http://www.java2s.com/Tutorial/Java/0355__JPA/SecondaryTableWithManyToOneRelationship.htm
	/*
    @ManyToOne 
    @JoinColumns({
        @JoinColumn(name="COMMENT", referencedColumnName="comment",
                    table="USER_COMMENTS"),
        @JoinColumn(name="CMNT_ID", referencedColumnName="ID",
                    table="USER_COMMENTS")
    })
    */
    private User user;
    
    //@Column (table="comment")
    private String comment;
    
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	
}
