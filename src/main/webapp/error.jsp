<%@ page contentType="text/html;charset=UTF-8" isErrorPage="true" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%-- page unique pour les erreurs declarees dans web.xml, n'affiche aucun detail technique --%>
<c:set var="code" value="${pageContext.errorData.statusCode}" />
<!DOCTYPE html>
<html lang="fr">
<head>
    <title>Erreur ${code}</title>
    <script src="https://cdn.tailwindcss.com"></script>
</head>

<body class="bg-gray-100 min-h-screen flex items-center justify-center">

    <div class="bg-white shadow-lg rounded-xl p-8 w-full max-w-md text-center">

        <p class="text-6xl font-black text-gray-300 mb-4">${code}</p>

        <h1 class="text-2xl font-bold text-gray-800 mb-2">
            <c:choose>
                <c:when test="${code == 404}">Page introuvable</c:when>
                <c:when test="${code == 405}">Action non autorisée</c:when>
                <c:otherwise>Une erreur est survenue</c:otherwise>
            </c:choose>
        </h1>

        <p class="text-sm text-gray-500 mb-6">
            <c:choose>
                <c:when test="${code == 404}">La page demandée n'existe pas ou a été déplacée.</c:when>
                <c:when test="${code == 405}">Cette action n'est pas possible depuis cette page.</c:when>
                <c:otherwise>Réessayez dans quelques instants.</c:otherwise>
            </c:choose>
        </p>

        <a href="${pageContext.request.contextPath}/"
           class="inline-block w-full bg-blue-600 text-white py-2 rounded-lg hover:bg-blue-700 transition duration-200">
            Retour à l'accueil
        </a>

    </div>

</body>
</html>
