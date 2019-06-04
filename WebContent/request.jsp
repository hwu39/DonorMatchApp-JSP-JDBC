<%@ taglib prefix="tagfiles" tagdir="WEB-INF/tags" %>
<%@page import="com.match.Person"%>

<!DOCTYPE html>
<html lang="en" dir="ltr">
  <head>
    <meta charset="utf-8"/>

    <title>Register to find your match!</title>
  </head>

  <body>
    <tagfiles:header />

    <div class="container heading">
      <h2>Request an Organ!</h2>
      <form action="request.do" method="post">
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
            Needs by: <br />
            <input type="date" size="18" name="by" />
            <br />
            <!-- ADD YOUR INPUT FIELD FOR THE FIELD YOU ADDED TO THE PERSON DATABASE RIGHT HERE-->

        </div>

        <div>
          <input type="submit" value="Submit" />
        </div>

      </form>
    </div>

    <div>
      <%
        String status =  request.getParameter("status");
        String doc = request.getParameter("doc");
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
            doc = "Your doctor's id is " + doc;
        }
      %>
      <p><%= message %> <br> <%= doc %></p>
    </div>

  </body>
</html>
