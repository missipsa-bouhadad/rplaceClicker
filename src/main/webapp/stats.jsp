<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<fmt:setLocale value="fr_FR" />
<!DOCTYPE html>
<html lang="fr">
<head>
  <title>Statistiques – rplace clicker</title>
  <meta name="viewport" content="width=device-width, initial-scale=1">
  <link rel="stylesheet" href="${pageContext.request.contextPath}/css/tailwind.css">
  <link rel="icon" type="image/png" href="${pageContext.request.contextPath}/images/cookie.png">
  <link rel="preconnect" href="https://fonts.googleapis.com">
  <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
  <link rel="stylesheet" href="https://fonts.googleapis.com/css2?family=Inter:wght@400;500;600;700&family=Fredoka:wght@500;600;700&display=swap">
  <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.0/css/all.min.css">
  <style>
    .custom-scroll::-webkit-scrollbar { width: 6px; }
    .custom-scroll::-webkit-scrollbar-track { background: #f1f1f1; }
    .custom-scroll::-webkit-scrollbar-thumb { background: #d1d5db; border-radius: 10px; }
    .custom-scroll::-webkit-scrollbar-thumb:hover { background: #9ca3af; }
  </style>
</head>

<body class="bg-cream text-choco h-screen flex flex-col overflow-hidden">

  <%@ include file="nav.jsp" %>

  <main class="flex-1 w-full px-4 py-6 flex flex-col min-h-0">

    <%-- ajout stat perso sans ia puis modifiaction du style avec ia avec retouche --%>


    <c:if test="${not empty sessionScope.player}">
      <c:set var="myStats" value="${null}" />
      <c:forEach var="ps" items="${playerStats}">
        <c:if test="${ps.player.pseudo == sessionScope.player.pseudo}">
          <c:set var="myStats" value="${ps}" />
        </c:if>
      </c:forEach>

      <c:if test="${not empty myStats}">
        <div class="mx-auto w-full bg-white rounded-lg shadow-md mb-6 shrink-0">
          <h2 class="font-display text-xl font-bold text-choco p-3 text-center border-b">Mes statistiques</h2>
          <div class="bg-gray-50 flex rounded-b-lg overflow-hidden">
            <div class="flex-1 p-4 text-center border-r border-gray-200">
              <h3 class="text-xs font-semibold text-gray-500 uppercase mb-1">Pixels placés</h3>
              <p class="text-2xl font-bold text-blue-600">${myStats.totalPlaced}</p>
            </div>
            <div class="flex-1 p-4 text-center border-r border-gray-200">
              <h3 class="text-xs font-semibold text-gray-500 uppercase mb-1">En place</h3>
              <p class="text-2xl font-bold text-green-600">${myStats.currentPixels}</p>
            </div>
            <div class="flex-1 p-4 text-center border-r border-gray-200">
              <h3 class="text-xs font-semibold text-gray-500 uppercase mb-1">Recouvrement</h3>
              <p class="text-2xl font-bold text-purple-600">${myStats.coveragePercentForPlayer}%</p>
            </div>
            <div class="flex-1 p-4 text-center border-r border-gray-200">
              <h3 class="text-xs font-semibold text-gray-500 uppercase mb-1">Record d'âge</h3>
              <p class="text-2xl font-bold text-orange-600">${myStats.recordFormatted}</p>
            </div>
            <div class="flex-1 p-4 text-center">
              <h3 class="text-xs font-semibold text-gray-500 uppercase mb-1">Plus ancien</h3>
              <div class="flex items-center justify-center gap-2">
                <c:choose>
                  <c:when test="${myStats.oldestPixel != null}">
                    <span style="display:inline-block; width:12px; height:12px; background:${myStats.oldestPixel.color}; border:1px solid #999;"></span>
                    <span class="text-lg font-bold text-gray-700">(${myStats.oldestPixel.id.x}, ${myStats.oldestPixel.id.y})</span>
                  </c:when>
                  <c:otherwise><span class="text-gray-400">-</span></c:otherwise>
                </c:choose>
              </div>
            </div>
          </div>
        </div>
      </c:if>
    </c:if>

    <div class="grid grid-cols-1 md:grid-cols-2 xl:grid-cols-4 gap-6 flex-1 min-h-0">

      <%-- Liste des joueurs --%>
      <section class="flex flex-col min-h-0">
        <h2 class="font-display text-xl font-bold text-choco mb-3">Joueurs enregistrés</h2>
        <div class="bg-white rounded-xl shadow p-4 overflow-y-auto flex-1 border border-gray-100 custom-scroll">
          <c:forEach var="ps" items="${playerStats}">
            <div class="flex justify-between items-center py-2 border-b last:border-0 cursor-pointer hover:bg-gray-50 px-1" onclick="togglePlayer('${ps.player.pseudo}')">
              <span class="text-gray-800 font-medium">${ps.player.pseudo}</span>
              <div class="flex items-center gap-2">
                <span class="text-xs text-gray-400">${ps.player.country}</span>
                <i id="icon-${ps.player.pseudo}" class="fas fa-chevron-down text-gray-400 text-xs"></i>
              </div>
            </div>
            <div id="detail-${ps.player.pseudo}" class="hidden py-3 text-sm text-gray-600 flex flex-col gap-1 pl-4 border-l border-gray-200 my-1">
              <span><strong>Age :</strong> ${not empty ps.player.age ? ps.player.age : '–'}</span>
              <span><strong>Pays :</strong> ${not empty ps.player.country ? ps.player.country : '–'}</span>
              <span><strong>Pixels placés :</strong> ${ps.totalPlaced}</span>
              <span><strong>Pixels en place :</strong> ${ps.currentPixels}</span>
              <span><strong>Pixel le plus ancien :</strong>
                <c:choose>
                  <c:when test="${ps.oldestPixel != null}">
                    <span style="display:inline-block; width:12px; height:12px; background:${ps.oldestPixel.color}; border:1px solid #999; vertical-align:middle;"></span>
                    (${ps.oldestPixel.id.x}, ${ps.oldestPixel.id.y})
                  </c:when>
                  <c:otherwise>-</c:otherwise>
                </c:choose>
              </span>
              <span><strong>Pixel le plus agé :</strong> ${ps.recordFormatted}</span>
            </div>
          </c:forEach>
        </div>
      </section>

      <%-- Classement par credits, avec le cps de chaque joueur --%>
      <section class="flex flex-col min-h-0">
        <h2 class="font-display text-xl font-bold text-choco mb-3">Classement – Crédits</h2>
        <div class="bg-white rounded-xl shadow p-4 overflow-y-auto flex-1 border border-gray-100 custom-scroll">
          <c:forEach var="ps" items="${topCredits}" varStatus="loop">
            <div class="flex items-center justify-between py-2 border-b last:border-0 hover:bg-gray-50 px-1">
              <div class="flex items-center gap-3">
                <span class="font-bold w-6 text-center text-gray-400 text-sm">${loop.index + 1}</span>
                <span class="text-gray-800 font-medium">${ps.player.pseudo}</span>
              </div>
              <div class="flex flex-col items-end">
                <span class="text-sm font-bold text-yellow-600 bg-yellow-50 px-2 py-1 rounded">
                  <fmt:formatNumber value="${ps.player.credits}" maxFractionDigits="0" />
                </span>
                <span class="text-xs font-semibold text-green-600">
                  +<fmt:formatNumber value="${ps.cps}" maxFractionDigits="1" /> /s
                </span>
              </div>
            </div>
          </c:forEach>
        </div>
      </section>

      <%-- Classement par couverture --%>
      <section class="flex flex-col min-h-0">
        <h2 class="font-display text-xl font-bold text-choco mb-3">Classement – Couverture</h2>
        <div class="bg-white rounded-xl shadow p-4 overflow-y-auto flex-1 border border-gray-100 custom-scroll">
          <c:forEach var="ps" items="${topPlayers}" varStatus="loop">
            <div class="flex items-center justify-between py-2 border-b last:border-0 hover:bg-gray-50 px-1">
              <div class="flex items-center gap-3">
                <span class="font-bold w-6 text-center text-gray-400 text-sm">${loop.index + 1}</span>
                <span class="text-gray-800 font-medium">${ps.player.pseudo}</span>
              </div>
              <span class="text-sm font-bold text-purple-600 bg-purple-50 px-2 py-1 rounded">
                ${ps.coveragePercentForPlayer}%
              </span>
            </div>
          </c:forEach>
        </div>
      </section>

      <%-- Classement par ancienneté --%>
      <section class="flex flex-col min-h-0">
        <h2 class="font-display text-xl font-bold text-choco mb-3">Classement – Ancienneté</h2>
        <div class="bg-white rounded-xl shadow p-4 overflow-y-auto flex-1 border border-gray-100 custom-scroll">
          <c:forEach var="ps" items="${topPlayersWithOldestPixel}" varStatus="loop">
            <div class="flex items-center justify-between py-2 border-b last:border-0 hover:bg-gray-50 px-1">
              <div class="flex items-center gap-3">
                <span class="font-bold w-6 text-center text-gray-400 text-sm">${loop.index + 1}</span>
                <span class="text-gray-800 font-medium">${ps.player.pseudo}</span>
              </div>
              <span class="text-xs text-gray-500 bg-gray-50 px-2 py-1 rounded border">
                <c:choose>
                  <c:when test="${ps.oldestPixel != null}">
                    <span style="display:inline-block; width:10px; height:10px; background:${ps.oldestPixel.color}; border:1px solid #999; vertical-align:middle;"></span>
                    (${ps.oldestPixel.id.x}, ${ps.oldestPixel.id.y})
                  </c:when>
                  <c:otherwise>Aucun pixel</c:otherwise>
                </c:choose>
              </span>
            </div>
          </c:forEach>
        </div>
      </section>

    </div>

  </main>

  <script src="js/stats.js"></script>

</body>
</html>
