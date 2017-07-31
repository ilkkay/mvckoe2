   <%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>    
   <%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>    
  
<h1>Translation Units List</h1>  
<table border="2" width="70%" cellpadding="2">  
<tr><th>Id</th><th>Source</th><th>Target</th><th>Translate</th><th>Delete</th></tr>  
   <c:forEach var="transu" items="${list}">   
   <tr>  
   <td>${transu.id}</td>  
   <td>${transu.sourceSegm}</td>  
   <td>${transu.targetSegm}</td>  
   <td><a href="edittransu/${transu.id}">Translate</a></td>  
   <td><a href="deleteemp/${transu.id}">Delete</a></td>  
   </tr>  
   </c:forEach>  
   </table>  
   <br/>  
   <!-- 
   <a href="empform">Add New Employee</a>
    -->
   