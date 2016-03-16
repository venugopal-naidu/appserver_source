<%--
  Created by IntelliJ IDEA.
  User: roopesh
  Date: 11/03/16
  Time: 6:05 AM
--%>

<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <title>Email Verification </title>
</head>

<body>
    To complete registration go through the verification link ${verificationLink} and enter the following OPT.

     <b>OTP: ${registration.verificationCode} </b>
</body>
</html>