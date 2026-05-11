<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="fr">
<head>
  <title>Statistiques – rplace clicker</title>
  <script src="https://cdn.tailwindcss.com"></script>
  <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.0/css/all.min.css">
</head>

<body class="bg-gray-100 min-h-screen flex flex-col overflow-x-hidden">

  <%@ include file="nav.jsp" %>

  <main class="flex-1 w-full px-4 py-10">

    <div class="grid grid-cols-1 md:grid-cols-3 gap-6">

      <!-- liste des joueurs -->
      <section>
        <h2 class="text-2xl font-bold text-gray-800 mb-4">Joueurs enregistrés</h2>
        <div class="bg-white rounded-xl shadow p-4">
            <div class="bg-white rounded-lg border border-gray-200">
              <c:forEach var="p" items="${players}">
                <a href=""
                   class="flex justify-between items-center px-4 py-2 hover:bg-gray-50 border-b border-gray-100 last:border-0">
                  <span class="text-gray-800">
                    ${p.pseudo}
                  </span>
                  <span class="text-sm text-gray-400">
                    ${p.country }
                  </span>
                </a>
              </c:forEach>
            </div>
        </div>
      </section>

      <!-- classement par couverture -->
      <section>
        <h2 class="text-2xl font-bold text-gray-800 mb-4">Classement – Couverture</h2>
        <div class="bg-white rounded-xl shadow p-4">
        </div>
      </section>

      <!-- classement par ancienneté -->
      <section>
        <h2 class="text-2xl font-bold text-gray-800 mb-4">Classement – Ancienneté</h2>
        <div class="bg-white rounded-xl shadow p-4">
        </div>
      </section>

    </div>

  </main>

</body>
</html>