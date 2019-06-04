<%@ taglib prefix="tagfiles" tagdir="WEB-INF/tags" %>
<%@page import="com.match.*"%>

<!DOCTYPE html>
<html lang="en" dir="ltr">
  <head>
    <meta charset="utf-8"/>

    <title>Register to find your match!</title>
  </head>

  <body>
    <tagfiles:header />

    <div class="container heading">
      <h2>Offer an Organ!</h2>
      <form method="post">
        <div class = "row">
            User ID: <br />
            <input type="number" size="18" name="id" required  />
            <br />
            Organ: <br />
            <select name="organ">
              <option value="Heart">Heart</option>
              <option value="Liver">Liver</option>
              <option value="Kidney">Kidney</option>
              <option value="Lung">Lung</option>
              <option value="Pancreas">Pancreas</option>
            </select>
            <br />
        </div>

        <div>
          <input type="submit" value="Submit" />
        </div>

      </form>
    </div>

    <div>
      <%
      	String id = request.getParameter("id");
      	String organ = request.getParameter("organ");
      	
      	int pstatus = -1;
      	int pdoc = -1;
      
      	pstatus = Person.addAvailableOrgan(Organ.fromString(organ), Integer.parseInt(id));
        if (pstatus == 1) {
        	pdoc = Person.getDoctorUpdate(Organ.fromString(organ), Integer.parseInt(id));
        }
        response.sendRedirect("offer?status=" + pstatus + "&doc=" + pdoc);
      
        String status =  Integer.toString(pstatus);
        String doc = Integer.toString(pdoc);
        String message = null;
        if(status == null) {
          message = "";
        }
        else if(status.compareTo("-1") == 0){
          message = "Request failed";
        }
        else if(status.compareTo("1") == 0){
            message = "Request Successful";
        }
        else{
            message = "Request status unknown";
        }

        if(doc == null || doc.compareTo("-1") == 0){
            doc = "";
        }
        else{
            doc = "Your new doctor's id is " + doc;
        }
      %>
      <p><%= message %> <br> <%= doc %></p>
    </div>

  </body>
</html>
