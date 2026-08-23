<%@ page contentType="text/html;charset=UTF-8" isErrorPage="true" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%-- page unique pour les erreurs declarees dans web.xml, n'affiche aucun detail technique --%>
<c:set var="code" value="${pageContext.errorData.statusCode}" />
<!DOCTYPE html>
<html lang="fr">
<head>
    <title>Erreur ${code}</title>
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/tailwind.css">
    <link rel="icon" type="image/png" href="${pageContext.request.contextPath}/images/cookie.png">
    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
    <link rel="stylesheet" href="https://fonts.googleapis.com/css2?family=Inter:wght@400;500;600;700&family=Fredoka:wght@500;600;700&display=swap">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.0/css/all.min.css">
</head>

<body class="bg-app text-choco min-h-screen flex flex-col">

    <%@ include file="nav.jsp" %>

    <main class="flex-1 flex items-center justify-center px-4 py-10">
    <div class="card p-8 w-full max-w-md text-center">

        <p class="font-display text-6xl font-bold text-cookie-300 mb-4">${code}</p>

        <h1 class="font-display text-2xl font-bold text-choco mb-2">
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

        <a href="${pageContext.request.contextPath}/" class="btn-primary inline-block">
            Retour à l'accueil
        </a>

    </div>
    </main>

</body>
</html>
