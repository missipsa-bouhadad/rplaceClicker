<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html>
<head>
    <title>Inscription</title>
</head>
<body>

    <h1>Inscription</h1>

    <form method="post" action="register">
        <label>Pseudo</label>
        <input type="text" name="pseudo" required /><br/>

        <label>Mot de passe</label>
        <input type="password" name="password" required /><br/>

        <label>Age</label>
        <input type="number" name="age" /><br/>

        <label>Pays</label>
        <input type="text" name="country" /><br/>

        <button type="submit">S'inscrire</button>
    </form>

    <c:if test="${not empty error}">
        <p style="color:red;">${error}</p>
    </c:if>

    <c:if test="${not empty success}">
        <p style="color:green;">${success}</p>
    </c:if>

</body>
</html>