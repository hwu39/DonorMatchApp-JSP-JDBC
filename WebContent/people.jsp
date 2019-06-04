<%@ taglib prefix="tagfiles" tagdir="/WEB-INF/tags" %>
<%@page import="com.match.*"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!DOCTYPE html>
<html lang="en" dir="ltr">
  <head>
    <meta charset="utf-8"/>

    <title>People List</title>
  </head>
  <body>
    <tagfiles:header />
    
    <div>
      	<form action="search" method="get">
        	Search for Person: 
        	<input type="text" name="query" />
        	<input type="submit" value="Submit" />
      	</form>
	<% 
		String query = request.getParameter("query");
		Person[] people = Person.getPersonSearch(query);
	
		pageContext.setAttribute("peopleList", people); 
	%>
      	<h2>People List</h2>
      	<table border="1">
        	<tr>
				<th>First Name</th>
				<th>Last Name</th>
			</tr>
            <tr>
            <c:forEach var="person" items="${peopleList }">
            	<td>${person.firstName}</td>
            	<td>${person.lastName}</td>
            </c:forEach>
            </tr>
          
        </table>
    </div>

  </body>
</html>