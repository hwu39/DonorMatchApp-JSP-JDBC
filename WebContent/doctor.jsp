<%@ taglib prefix="tagfiles" tagdir="/WEB-INF/tags" %>
<%@page import="com.match.Person"%>

<!DOCTYPE html>
<html lang="en" dir="ltr">

  <head>
    <meta charset="utf-8"/>

    <title>Find doctor's name</title>
  </head>

  <body>
    <tagfiles:header />

    <div>
      <h2>Find your doctor's name!</h2>
      <form class="" action="search" method="get">
        <input type="text" name="id" />
        <input type="submit" value="Submit" />
      </form>
    </div>
    <%
    	String id = request.getParameter("id");
    	int pid = Integer.parseInt(id);
    	if (pid < 1) {
    		response.sendRedirect("Invalid input");
    	}
    	else {
    		String doctorName = "";
    		doctorName = Person.getDoctorName(pid);
    		response.sendRedirect("find?id=" + id + "&doc=" + doctorName);
    		pageContext.setAttribute("doctor", doctorName);
    	}
    %>
	<div>
    <h2>Doctor For This Patient:</h2>
    <h2>${doctor }</h2>
  	</div>

  </body>
</html>
