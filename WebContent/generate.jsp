<%@ taglib prefix="tagfiles" tagdir="/WEB-INF/tags" %>
<%@page import="com.match.Person"%>
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
      	<div>
        <h2>Enter Your ID to see your matches</h2>
        <form method="get">
         Enter your id to see your matches: 
          <br />
          <input type="number" name="id" />
          <input type = "submit" value = "Generate Match!" />

        </form>
        
        <%
        	String id = request.getParameter("id");
        	//request.setAttribute("id", id);
        	Person[] people = Person.getOrganMatches(id);
        	pageContext.setAttribute("matches", people);
        %>
        For the patient ID: ${id }
        <br/>
        The matches are:
        <hr>
        <table border="1">
        	<tr>
				<th>First Name</th>
				<th>Last Name</th>
			</tr>
            <tr>
            <c:forEach var="person" items="${matches }">
            	<td>${person.firstName}</td>
            	<td>${person.lastName}</td>
            </c:forEach>
            </tr>
          
        </table>
    	</div>
	</div>
    <div>
      <p>MatchMaker</p>
    </div>
  </body>
</html>
