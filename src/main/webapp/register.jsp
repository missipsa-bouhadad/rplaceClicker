<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="fr">
<head>
    <title>Inscription</title>
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/tailwind.css">
</head>

<%-- Style basique avec tailwind css en utilisant l'IA --%>

<body class="bg-gray-100 min-h-screen flex items-center justify-center">

    <div class="bg-white shadow-lg rounded-xl p-8 w-full max-w-md">

        <h1 class="text-2xl font-bold text-center text-gray-800 mb-6">
            Inscription
        </h1>

        <form method="post" action="register" class="space-y-4">

            <div>
                <label class="block text-sm font-medium text-gray-700">Pseudo</label>
                <%-- c:out echappe la saisie reaffichee apres une erreur --%>
                <input type="text" name="pseudo" maxlength="20" required value="<c:out value='${param.pseudo}'/>"
                       class="mt-1 w-full px-3 py-2 border rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500"/>
            </div>

            <div>
                <label class="block text-sm font-medium text-gray-700">Mot de passe</label>
                <input type="password" name="password" required
                       class="mt-1 w-full px-3 py-2 border rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500"/>
            </div>

            <div>
                <label class="block text-sm font-medium text-gray-700">Confirmer le mot de passe</label>
                <input type="password" name="confirmPassword" required
                       class="mt-1 w-full px-3 py-2 border rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500"/>
            </div>

            <div>
                <label class="block text-sm font-medium text-gray-700">Age</label>
                <input type="number" name="age" min="1" max="120" value="<c:out value='${param.age}'/>"
                       class="mt-1 w-full px-3 py-2 border rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500"/>
            </div>

            <div>
                <label class="block text-sm font-medium text-gray-700">Pays</label>
                <input type="text" name="country" maxlength="20" value="<c:out value='${param.country}'/>"
                       class="mt-1 w-full px-3 py-2 border rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500"/>
            </div>

            <button type="submit"
                    class="w-full bg-blue-600 text-white py-2 rounded-lg hover:bg-blue-700 transition duration-200">
                S'inscrire
            </button>
        </form>

        <c:if test="${not empty error}">
            <div class="mt-4 bg-red-100 text-red-700 px-4 py-2 rounded-lg text-sm">
                ${error}
            </div>
        </c:if>

    </div>

</body>
</html>