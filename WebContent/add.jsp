<%@ taglib prefix="tagfiles" tagdir="/WEB-INF/tags" %>
<%@page import="com.match.Person"%>
<%@page import="java.util.Date" %>
<%@page import="java.text.SimpleDateFormat" %>

<!DOCTYPE html>
<html lang="en" dir="ltr">
  <head>
    <meta charset="utf-8"/>
    
    <title>Register to find your match!</title>
  </head>

  <body>
    <tagfiles:header />

    <div class="container heading">
      <h2>Enter Your Information</h2>
      <form action="" method="post">
        <div class = "row">
            <h3>Personal Information</h3>
            First Name: <br />
            <input type="text" size="18" name="first" required  />
            <br />
            Last Name: <br />
            <input type="text" size="18" name="last" required/>
            <br />
            Birthdate: <br />
            <input type="date" size="18" name="birthdate" />
            <br/>

            Blood type: <br />
            <select name="type">
              <option value="O+">O+</option>
              <option value="O-">O-</option>
              <option value="A+">A+</option>
              <option value="A-">A-</option>
              <option value="B+">B+</option>
              <option value="B-">B-</option>
              <option value="AB+">AB+</option>
              <option value="AB-">AB-</option>
            </select>
            <br/>

        </div>

        <div>
          <input type="submit" value="Submit" />
        </div>

      </form>
    </div>
    <div>
      <% 
      	String first = request.getParameter("first");
      	String last = request.getParameter("last");
      	String birthdate = request.getParameter("birthdate");
      	String blood_type = request.getParameter("type");
     	
     	Date today = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        Date birthDate = new Date();
        try {
        	birthDate = formatter.parse(birthdate);
        } catch (Exception e) {
        	e.printStackTrace();
        }
        int bd = Integer.parseInt(formatter.format(birthDate));
        int day = Integer.parseInt(formatter.format(today));
        int age = (day - bd) / 10000;
        // first name should only contain 12 letters at max
        // last name should only contain 18 letters at max
        // person's age must be 18 years old or older
        if (first.length() > 12 || last.length() > 18 || age < 18) {
            response.sendRedirect("invalidinput");
        }
        else {
        	int id = -1;
            int doc = -1;
            id = Person.addPerson(first, last, birthdate, blood_type);
            if (id != -1) {
            doc = Person.getDoctorFirst(id);
        	}
     
        	response.sendRedirect("add?id=" + id + "&doc=" + doc);
        }
        
        String id =  request.getParameter("id");
        String doc = request.getParameter("doc");
        if(id == null) {
          id = "";
        }
        else {
          id = "Your ID is " + id + " - Remember it!";
        }
        if(doc == null){
            doc = "";
        }
        else{
            doc = "Your doctor's id is " + doc;
        }
      %>
      <p><%= id %> <br> <%= doc %></p>


    </div>

    <div>
      <p>You will be assigned an appropriate doctor for your organ request/offer. </p>
    </div>

  </body>
</html>
