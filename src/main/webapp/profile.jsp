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
  <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
</head>

<body class="bg-cream text-choco min-h-screen flex items-center justify-center py-10">

<div class="bg-white shadow-lg rounded-xl p-8 w-full max-w-md">

  <%-- Utilisation de l'IA pour le style de la page de modification du profil --%>
  <div class="flex justify-between items-center mb-6">
    <h1 class="font-display text-2xl font-bold text-choco">Mon Profil</h1>
    <a href="${pageContext.request.contextPath}/" class="text-sm text-cookie-700 hover:text-cookie-900 transition">
      <i class="fas fa-arrow-left mr-1"></i> Retour au jeu
    </a>
  </div>

  <form method="post" action="updateProfile" class="space-y-4">
    <h2 class="text-sm font-semibold text-gray-500 uppercase tracking-wider mb-2">Informations personnelles</h2>

    <div>
      <label class="block text-sm font-medium text-gray-700">Âge</label>
      <input type="number" name="age" min="1" max="120" value="${sessionScope.player.age}"
             class="mt-1 w-full px-3 py-2 border rounded-lg focus:outline-none focus:ring-2 focus:ring-cookie-500"/>
    </div>

    <div>
      <label class="block text-sm font-medium text-gray-700">Pays</label>
      <input type="text" name="country" maxlength="20" value="${sessionScope.player.country}"
             class="mt-1 w-full px-3 py-2 border rounded-lg focus:outline-none focus:ring-2 focus:ring-cookie-500"/>
    </div>

    <button type="submit"
            class="w-full bg-cookie-600 text-white py-2 rounded-lg hover:bg-cookie-700 transition duration-200">
      Mettre à jour mes infos
    </button>
  </form>

  <hr class="my-8 border-gray-200">

  <form method="post" action="updatePassword" class="space-y-4">
    <h2 class="text-sm font-semibold text-gray-500 uppercase tracking-wider mb-2">Sécurité</h2>

    <div>
      <label class="block text-sm font-medium text-gray-700">Mot de passe actuel</label>
      <input type="password" name="oldPassword" required
             class="mt-1 w-full px-3 py-2 border rounded-lg focus:outline-none focus:ring-2 focus:ring-red-500"/>
    </div>

    <div>
      <label class="block text-sm font-medium text-gray-700">Nouveau mot de passe</label>
      <input type="password" name="newPassword" required
             class="mt-1 w-full px-3 py-2 border rounded-lg focus:outline-none focus:ring-2 focus:ring-red-500"/>
    </div>

    <button type="submit"
            class="w-full bg-red-500 text-white py-2 rounded-lg hover:bg-red-600 transition duration-200">
      Changer le mot de passe
    </button>
  </form>

  <c:if test="${not empty error}">
    <div class="mt-6 bg-red-100 text-red-700 px-4 py-2 rounded-lg text-sm">
        ${error}
    </div>
  </c:if>

  <c:if test="${not empty success}">
    <div class="mt-6 bg-green-100 text-green-700 px-4 py-2 rounded-lg text-sm">
        ${success}
    </div>
  </c:if>

</div>

</body>
</html>