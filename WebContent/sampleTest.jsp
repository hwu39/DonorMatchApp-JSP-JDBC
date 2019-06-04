<%@page import="com.match.Person"%>
<%@ taglib prefix="tagfiles" tagdir="/WEB-INF/tags" %>

<!DOCTYPE html>
<html lang="en" dir="ltr">
  <head>
    <meta charset="utf-8"/>

    <title>Sample Test</title>
  </head>

<body>

Let's have a sample test: <%= Person.sampleTest() %>

</body>


</html>