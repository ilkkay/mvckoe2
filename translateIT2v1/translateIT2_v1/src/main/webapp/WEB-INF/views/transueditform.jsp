<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>    
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>    
  
        <h1>Edit Translation Unit</h1>  
       <form:form method="POST" action="/one/editsave">    
        <table >    
        <tr>  
        <td></td>    
         <td><form:hidden  path="id" /></td>  
         </tr>   
         <tr>    
          <td>Source : </td>  
          <td><form:input path="sourceSegm" size="75" /></td> 
         </tr>       
         
         <tr>    
          <td>Target :</td>    
          <td><form:input path="targetSegm" size="75" /></td>  
         </tr>   
           
         <tr>    
          <td> </td>    
          <td><input type="submit" value="Edit Save" /></td>    
         </tr>    
        </table>    
       </form:form>