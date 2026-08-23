<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="fr">
<head>
    <title>Inscription</title>
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/tailwind.css">
    <link rel="icon" type="image/png" href="${pageContext.request.contextPath}/images/cookie.png">
    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
    <link rel="stylesheet" href="https://fonts.googleapis.com/css2?family=Inter:wght@400;500;600;700&family=Fredoka:wght@500;600;700&display=swap">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.0/css/all.min.css">
</head>

<%-- Style basique avec tailwind css en utilisant l'IA --%>

<body class="bg-app text-choco min-h-screen flex flex-col">

    <%@ include file="nav.jsp" %>

    <main class="flex-1 flex items-center justify-center px-4 py-10">
        <div class="card p-8 w-full max-w-md">

            <h1 class="font-display text-3xl font-bold text-choco text-center mb-6">Inscription</h1>

            <c:if test="${not empty error}">
                <div class="alert-error mb-4">
                    <i class="fas fa-triangle-exclamation"></i>${error}
                </div>
            </c:if>

            <form method="post" action="register" class="space-y-4">

                <div>
                    <label for="pseudo" class="label">Pseudo</label>
                    <div class="relative">
                        <i class="fas fa-user input-icon"></i>
                        <%-- c:out echappe la saisie reaffichee apres une erreur --%>
                        <input id="pseudo" type="text" name="pseudo" maxlength="20" required autocomplete="username"
                               value="<c:out value='${param.pseudo}'/>" class="input"/>
                    </div>
                </div>

                <div>
                    <label for="password" class="label">Mot de passe</label>
                    <div class="relative">
                        <i class="fas fa-lock input-icon"></i>
                        <input id="password" type="password" name="password" required autocomplete="new-password" class="input pr-10"/>
                        <button type="button" data-toggle-password aria-label="Afficher le mot de passe"
                                class="absolute right-3 top-1/2 -translate-y-1/2 text-gray-400 hover:text-cookie-700">
                            <i class="fas fa-eye"></i>
                        </button>
                    </div>
                </div>

                <div>
                    <label for="confirmPassword" class="label">Confirmer le mot de passe</label>
                    <div class="relative">
                        <i class="fas fa-lock input-icon"></i>
                        <input id="confirmPassword" type="password" name="confirmPassword" required autocomplete="new-password" class="input pr-10"/>
                        <button type="button" data-toggle-password aria-label="Afficher le mot de passe"
                                class="absolute right-3 top-1/2 -translate-y-1/2 text-gray-400 hover:text-cookie-700">
                            <i class="fas fa-eye"></i>
                        </button>
                    </div>
                </div>

                <div class="grid grid-cols-2 gap-4">
                    <div>
                        <label for="age" class="label">Âge <span class="font-normal text-gray-400">(facultatif)</span></label>
                        <div class="relative">
                            <i class="fas fa-cake-candles input-icon"></i>
                            <input id="age" type="number" name="age" min="1" max="120"
                                   value="<c:out value='${param.age}'/>" class="input"/>
                        </div>
                    </div>

                    <div>
                        <label for="country" class="label">Pays <span class="font-normal text-gray-400">(facultatif)</span></label>
                        <div class="relative">
                            <i class="fas fa-earth-europe input-icon"></i>
                            <input id="country" type="text" name="country" maxlength="20"
                                   value="<c:out value='${param.country}'/>" class="input"/>
                        </div>
                    </div>
                </div>

                <button type="submit" class="btn-primary">S'inscrire</button>
            </form>

            <p class="text-sm text-gray-500 text-center mt-6">
                Déjà un compte ?
                <a href="login" class="ml-1 text-cookie-700 font-semibold hover:underline">Se connecter</a>
            </p>

        </div>
    </main>

    <script src="js/forms.js"></script>
</body>
</html>
