<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="fr">
<head>
  <title>Mon Profil</title>
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

  <%-- Utilisation de l'IA pour le style de la page de modification du profil --%>
  <div class="text-center mb-6">
    <h1 class="font-display text-3xl font-bold text-choco">Mon profil</h1>
    <p class="text-sm text-gray-500 mt-1"><c:out value="${sessionScope.player.pseudo}"/></p>
  </div>

  <c:if test="${not empty error}">
    <div class="alert-error mb-4">
      <i class="fas fa-triangle-exclamation"></i>${error}
    </div>
  </c:if>

  <c:if test="${not empty success}">
    <div class="alert-success mb-4">
      <i class="fas fa-circle-check"></i>${success}
    </div>
  </c:if>

  <section class="rounded-xl border border-cookie-100 bg-cookie-50/50 p-5">
    <h2 class="font-display text-lg font-bold text-choco mb-3"><i class="fas fa-id-card mr-2 text-cookie-600"></i>Informations personnelles</h2>

    <form method="post" action="updateProfile" class="space-y-4">
      <div class="grid grid-cols-2 gap-4">
        <div>
          <label for="age" class="label">Âge</label>
          <div class="relative">
            <i class="fas fa-cake-candles input-icon"></i>
            <input id="age" type="number" name="age" min="1" max="120"
                   value="<c:out value='${sessionScope.player.age}'/>" class="input"/>
          </div>
        </div>

        <div>
          <label for="country" class="label">Pays</label>
          <div class="relative">
            <i class="fas fa-earth-europe input-icon"></i>
            <input id="country" type="text" name="country" maxlength="20"
                   value="<c:out value='${sessionScope.player.country}'/>" class="input"/>
          </div>
        </div>
      </div>

      <button type="submit" class="btn-primary">Mettre à jour mes infos</button>
    </form>
  </section>

  <section class="rounded-xl border border-cookie-100 bg-cookie-50/50 p-5 mt-5">
    <h2 class="font-display text-lg font-bold text-choco mb-3"><i class="fas fa-shield-halved mr-2 text-cookie-600"></i>Mot de passe</h2>

    <form method="post" action="updatePassword" class="space-y-4">
      <div>
        <label for="oldPassword" class="label">Mot de passe actuel</label>
        <div class="relative">
          <i class="fas fa-lock input-icon"></i>
          <input id="oldPassword" type="password" name="oldPassword" required autocomplete="current-password" class="input pr-10"/>
          <button type="button" data-toggle-password aria-label="Afficher le mot de passe"
                  class="absolute right-3 top-1/2 -translate-y-1/2 text-gray-400 hover:text-cookie-700">
            <i class="fas fa-eye"></i>
          </button>
        </div>
      </div>

      <div>
        <label for="newPassword" class="label">Nouveau mot de passe</label>
        <div class="relative">
          <i class="fas fa-key input-icon"></i>
          <input id="newPassword" type="password" name="newPassword" required autocomplete="new-password" class="input pr-10"/>
          <button type="button" data-toggle-password aria-label="Afficher le mot de passe"
                  class="absolute right-3 top-1/2 -translate-y-1/2 text-gray-400 hover:text-cookie-700">
            <i class="fas fa-eye"></i>
          </button>
        </div>
      </div>

      <button type="submit" class="btn-primary">Changer le mot de passe</button>
    </form>
  </section>

</div>
</main>

<script src="js/forms.js"></script>
</body>
</html>
