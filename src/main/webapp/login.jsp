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
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.0/css/all.min.css">
</head>

<body class="bg-app text-choco min-h-screen flex flex-col">

    <%@ include file="nav.jsp" %>

    <main class="flex-1 flex items-center justify-center px-4 py-10">
        <div class="card p-8 w-full max-w-md">

            <h1 class="font-display text-3xl font-bold text-choco text-center mb-6">Connexion</h1>

            <%-- apres la redirection de RegisterServlet --%>
            <c:if test="${param.registered != null}">
                <div class="alert-success mb-4">
                    <i class="fas fa-circle-check"></i>Compte créé avec succès, vous pouvez vous connecter.
                </div>
            </c:if>

            <c:if test="${not empty error}">
                <div class="alert-error mb-4">
                    <i class="fas fa-triangle-exclamation"></i>${error}
                </div>
            </c:if>

            <form method="post" action="login" class="space-y-4">

                <div>
                    <label for="pseudo" class="label">Pseudo</label>
                    <div class="relative">
                        <i class="fas fa-user input-icon"></i>
                        <input id="pseudo" type="text" name="pseudo" required autocomplete="username"
                               value="<c:out value='${param.pseudo}'/>" class="input"/>
                    </div>
                </div>

                <div>
                    <label for="password" class="label">Mot de passe</label>
                    <div class="relative">
                        <i class="fas fa-lock input-icon"></i>
                        <input id="password" type="password" name="password" required autocomplete="current-password" class="input pr-10"/>
                        <button type="button" data-toggle-password aria-label="Afficher le mot de passe"
                                class="absolute right-3 top-1/2 -translate-y-1/2 text-gray-400 hover:text-cookie-700">
                            <i class="fas fa-eye"></i>
                        </button>
                    </div>
                </div>

                <button type="submit" class="btn-primary">Se connecter</button>

            </form>

            <p class="text-sm text-gray-500 text-center mt-6">
                Pas de compte ?
                <a href="register" class="ml-1 text-cookie-700 font-semibold hover:underline">S'inscrire</a>
            </p>

        </div>
    </main>

    <script src="js/forms.js"></script>
</body>
</html>
