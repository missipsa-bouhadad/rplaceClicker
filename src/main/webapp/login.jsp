<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="fr">
<head>
    <title>Connexion</title>
    <script src="https://cdn.tailwindcss.com"></script>
</head>

<body class="bg-gray-100 min-h-screen flex items-center justify-center">

    <div class="bg-white shadow-lg rounded-xl p-8 w-full max-w-md">

        <h1 class="text-2xl font-bold text-center text-gray-800 mb-6">
            Connexion
        </h1>

        <form method="post" action="login" class="space-y-4">

            <div>
                <label class="block text-sm font-medium text-gray-700">Pseudo</label>
                <input type="text" name="pseudo" required
                       class="mt-1 w-full px-3 py-2 border rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500"/>
            </div>

            <div>
                <label class="block text-sm font-medium text-gray-700">Mot de passe</label>
                <input type="password" name="password" required
                       class="mt-1 w-full px-3 py-2 border rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500"/>
            </div>

            <button type="submit"
                    class="w-full bg-blue-600 text-white py-2 rounded-lg hover:bg-blue-700 transition duration-200">
                Se connecter
            </button>

        </form>

        <c:if test="${not empty error}">
            <div class="mt-4 bg-red-100 text-red-700 px-4 py-2 rounded-lg text-sm">
                ${error}
            </div>
        </c:if>

        <p class="text-sm text-gray-500 text-center mt-6">
            Pas de compte ?
            <a href="register" class="ml-2 text-blue-600 font-semibold hover:underline">S'inscrire</a>
        </p>

    </div>

</body>
</html>