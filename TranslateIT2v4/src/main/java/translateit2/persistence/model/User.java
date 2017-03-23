package translateit2.persistence.model;

import java.io.Serializable;

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
