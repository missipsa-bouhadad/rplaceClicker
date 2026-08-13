<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="fr">
<head>
    <title>Connexion</title>
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/tailwind.css">
    <link rel="icon" type="image/png" href="${pageContext.request.contextPath}/images/cookie.png">
    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
    <link rel="stylesheet" href="https://fonts.googleapis.com/css2?family=Inter:wght@400;500;600;700&family=Fredoka:wght@500;600;700&display=swap">
</head>

<body class="bg-app text-choco min-h-screen flex items-center justify-center">

    <div class="card p-8 w-full max-w-md">

        <h1 class="font-display text-2xl font-bold text-center text-choco mb-6">
            Connexion
        </h1>

        <form method="post" action="login" class="space-y-4">

            <div>
                <label class="block text-sm font-medium text-gray-700">Pseudo</label>
                <input type="text" name="pseudo" required value="<c:out value='${param.pseudo}'/>"
                       class="mt-1 w-full px-3 py-2 border rounded-lg focus:outline-none focus:ring-2 focus:ring-cookie-500"/>
            </div>

            <div>
                <label class="block text-sm font-medium text-gray-700">Mot de passe</label>
                <input type="password" name="password" required
                       class="mt-1 w-full px-3 py-2 border rounded-lg focus:outline-none focus:ring-2 focus:ring-cookie-500"/>
            </div>

            <button type="submit"
                    class="w-full bg-cookie-600 text-white py-2 rounded-lg hover:bg-cookie-700 transition duration-200">
                Se connecter
            </button>

        </form>

        <%-- apres la redirection de RegisterServlet --%>
        <c:if test="${param.registered != null}">
            <div class="mt-4 bg-green-100 text-green-700 px-4 py-2 rounded-lg text-sm">
                Compte créé avec succès, vous pouvez vous connecter.
            </div>
        </c:if>

        <c:if test="${not empty error}">
            <div class="mt-4 bg-red-100 text-red-700 px-4 py-2 rounded-lg text-sm">
                ${error}
            </div>
        </c:if>

        <p class="text-sm text-gray-500 text-center mt-6">
            Pas de compte ?
            <a href="register" class="ml-2 text-cookie-700 font-semibold hover:underline">S'inscrire</a>
        </p>

    </div>

</body>
</html>