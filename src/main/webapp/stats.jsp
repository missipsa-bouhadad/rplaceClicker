<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="fr">
<head>
  <title>Statistiques – rplace clicker</title>
  <script src="https://cdn.tailwindcss.com"></script>
  <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.0/css/all.min.css">
</head>

<!-- Utilisation régulière de l ia pour le style -->
<body class="bg-gray-100 min-h-screen flex flex-col overflow-x-hidden">

  <%@ include file="nav.jsp" %>

  <main class="flex-1 w-full px-4 py-10">

    <div class="grid grid-cols-1 md:grid-cols-3 gap-6">

      <!-- liste des joueurs -->
      <section>
        <h2 class="text-2xl font-bold text-gray-800 mb-4">Joueurs enregistrés</h2>
        <div class="bg-white rounded-xl shadow p-4">
          <c:forEach var="ps" items="${playerStats}">
            <div class="flex justify-between items-center py-2 cursor-pointer"
                   onclick="togglePlayer('${ps.player.pseudo}')">
                <span class="text-gray-800">${ps.player.pseudo}</span>
                <div class="flex items-center gap-2">
                  <span class="text-sm text-gray-400">${ps.player.country}</span>
                  <i id="icon-${ps.player.pseudo}" class="fas fa-chevron-down text-gray-400 text-xs"></i>
                </div>
              </div>
              <div id="detail-${ps.player.pseudo}" class="hidden py-2 text-sm text-gray-500 flex flex-col gap-1 pl-2">
              <span>Age : ${not empty ps.player.age ? ps.player.age : '–'}</span>
              <span>Pays : ${not empty ps.player.country ? ps.player.country : '–'}</span>
              <span>Pixels placés : ${ps.totalPlaced}</span>
              <span>Pixels en place : ${ps.currentPixels}</span>
              <span>Pixel le plus ancien :
                <c:choose>
                  <c:when test="${ps.oldestPixel != null}">
                    <span style="display:inline-block; width:14px; height:14px; background:${ps.oldestPixel.color}; border:1px solid #999; vertical-align:middle;"></span>
                    (${ps.oldestPixel.id.x}, ${ps.oldestPixel.id.y})
                  </c:when>
                  <c:otherwise>-</c:otherwise>
                </c:choose>
              </span>
              <span>Record d age :
                  <c:choose>
                      <c:when test="${ps.record != null && ps.record.age > 0}">
                          <span style="display:inline-block; width:14px; height:14px; background:${ps.record.color}; border:1px solid #999; vertical-align:middle;"></span>
                          (${ps.record.x}, ${ps.record.y}) - ${ps.recordFormatted}
                      </c:when>
                      <c:otherwise>-</c:otherwise>
                  </c:choose>
              </span>
            </div>
          </c:forEach>
        </div>
      </section>

      <!-- classement par couverture -->
      <section>
        <h2 class="text-2xl font-bold text-gray-800 mb-4">Classement – Couverture</h2>
        <div class="bg-white rounded-xl shadow p-4">
          <c:forEach var="ps" items="${topPlayers}" varStatus="loop">
            <div class="flex items-center justify-between py-2 border-b last:border-0">
              <div class="flex items-center gap-3">
                <span class="font-bold w-6 text-center text-gray-500">${loop.index +1}</span>
                <span class="text-gray-800 font-medium">${ps.player.pseudo}</span>
              </div>
              <span class="text-sm text-gray-500">
                ${ps.coveragePercentForPlayer}%
              </span>
            </div>
          </c:forEach>
        </div>
      </section>

      <!-- classement par ancienneté -->
      <section>
        <h2 class="text-2xl font-bold text-gray-800 mb-4">Classement – Ancienneté</h2>
        <div class="bg-white rounded-xl shadow p-4">
          <c:forEach var="ps" items="${topPlayersWithOldestPixel}" varStatus="loop">
            <div class="flex items-center justify-between py-2 border-b last:border-0">
              <div class="flex items-center gap-3">
                <span class="font-bold w-6 text-center text-gray-500">${loop.index + 1}</span>
                <span class="text-gray-800 font-medium">${ps.player.pseudo}</span>
              </div>
              <span class="text-sm text-gray-500">
                <c:choose>
                  <c:when test="${ps.oldestPixel != null}">
                    <span style="display:inline-block; width:14px; height:14px; background:${ps.oldestPixel.color}; border:1px solid #999; vertical-align:middle;"></span>
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