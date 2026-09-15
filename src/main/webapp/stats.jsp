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

<body class="bg-app text-choco h-screen flex flex-col overflow-hidden">

  <c:set var="activePage" value="stats" />
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
        <section class="mb-6 shrink-0">
          <h2 class="font-display text-xl font-bold text-choco mb-3">Mes statistiques</h2>
          <div class="grid grid-cols-2 md:grid-cols-5 gap-4">
            <div class="card p-4 flex items-center gap-3">
              <span class="w-10 h-10 shrink-0 rounded-full bg-cookie-100 text-cookie-700 flex items-center justify-center"><i class="fas fa-paintbrush"></i></span>
              <div>
                <p class="text-xs font-semibold text-gray-500 uppercase">Pixels placés</p>
                <p class="font-display text-2xl font-bold text-choco">${myStats.totalPlaced}</p>
              </div>
            </div>
            <div class="card p-4 flex items-center gap-3">
              <span class="w-10 h-10 shrink-0 rounded-full bg-cookie-100 text-cookie-700 flex items-center justify-center"><i class="fas fa-border-all"></i></span>
              <div>
                <p class="text-xs font-semibold text-gray-500 uppercase">En place</p>
                <p class="font-display text-2xl font-bold text-choco">${myStats.currentPixels}</p>
              </div>
            </div>
            <div class="card p-4 flex items-center gap-3">
              <span class="w-10 h-10 shrink-0 rounded-full bg-cookie-100 text-cookie-700 flex items-center justify-center"><i class="fas fa-chart-pie"></i></span>
              <div>
                <p class="text-xs font-semibold text-gray-500 uppercase">Couverture</p>
                <p class="font-display text-2xl font-bold text-choco">${myStats.coveragePercentForPlayer}%</p>
              </div>
            </div>
            <div class="card p-4 flex items-center gap-3">
              <span class="w-10 h-10 shrink-0 rounded-full bg-cookie-100 text-cookie-700 flex items-center justify-center"><i class="fas fa-trophy"></i></span>
              <div>
                <p class="text-xs font-semibold text-gray-500 uppercase">Record d'âge</p>
                <p class="font-display text-2xl font-bold text-choco">${myStats.recordFormatted}</p>
              </div>
            </div>
            <div class="card p-4 flex items-center gap-3 col-span-2 md:col-span-1">
              <span class="w-10 h-10 shrink-0 rounded-full bg-cookie-100 text-cookie-700 flex items-center justify-center"><i class="fas fa-hourglass-half"></i></span>
              <div>
                <p class="text-xs font-semibold text-gray-500 uppercase">Plus ancien</p>
                <c:choose>
                  <c:when test="${myStats.oldestPixel != null}">
                    <p class="font-display text-2xl font-bold text-choco flex items-center gap-2">
                      <span style="display:inline-block; width:14px; height:14px; border:1px solid #999; border-radius:3px;" data-color="<c:out value='${myStats.oldestPixel.color}'/>"></span>
                      (${myStats.oldestPixel.id.x}, ${myStats.oldestPixel.id.y})
                    </p>
                  </c:when>
                  <c:otherwise><p class="font-display text-2xl font-bold text-gray-300">–</p></c:otherwise>
                </c:choose>
              </div>
            </div>
          </div>
        </section>
      </c:if>
    </c:if>

    <div class="grid grid-cols-1 md:grid-cols-2 xl:grid-cols-4 gap-6 flex-1 min-h-0">

      <%-- Liste des joueurs --%>
      <section class="card flex flex-col min-h-0 overflow-hidden">
        <div class="px-4 py-3 bg-cookie-50 border-b border-cookie-100 flex items-center gap-2">
          <i class="fas fa-users text-cookie-600"></i>
          <h2 class="font-display text-lg font-bold text-choco">Joueurs enregistrés</h2>
        </div>
        <div class="p-4 overflow-y-auto flex-1 custom-scroll">
          <c:forEach var="ps" items="${playerStats}">
            <div class="flex justify-between items-center py-2 border-b last:border-0 cursor-pointer hover:bg-cookie-50 px-1" onclick="togglePlayer('${ps.player.pseudo}')">
              <span class="text-choco font-medium">${ps.player.pseudo}</span>
              <div class="flex items-center gap-2">
                <span class="text-xs text-gray-400">${ps.player.country}</span>
                <i id="icon-${ps.player.pseudo}" class="fas fa-chevron-down text-gray-400 text-xs"></i>
              </div>
            </div>
            <div id="detail-${ps.player.pseudo}" class="hidden py-3 text-sm text-gray-600 flex-col gap-1 pl-4 border-l-2 border-cookie-200 my-1">
              <span><strong>Age :</strong> ${not empty ps.player.age ? ps.player.age : '–'}</span>
              <span><strong>Pays :</strong> ${not empty ps.player.country ? ps.player.country : '–'}</span>
              <span><strong>Pixels placés :</strong> ${ps.totalPlaced}</span>
              <span><strong>Pixels en place :</strong> ${ps.currentPixels}</span>
              <span><strong>Pixel le plus ancien :</strong>
                <c:choose>
                  <c:when test="${ps.oldestPixel != null}">
                    <span style="display:inline-block; width:12px; height:12px; border:1px solid #999; vertical-align:middle;" data-color="<c:out value='${ps.oldestPixel.color}'/>"></span>
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
      <section class="card flex flex-col min-h-0 overflow-hidden">
        <div class="px-4 py-3 bg-cookie-50 border-b border-cookie-100 flex items-center gap-2">
          <i class="fas fa-coins text-cookie-600"></i>
          <h2 class="font-display text-lg font-bold text-choco">Classement – Crédits</h2>
        </div>
        <div class="p-4 overflow-y-auto flex-1 custom-scroll">
          <c:forEach var="ps" items="${topCredits}" varStatus="loop">
            <div class="flex items-center justify-between py-2 border-b last:border-0 hover:bg-cookie-50 px-1">
              <div class="flex items-center gap-3">
                <%@ include file="WEB-INF/jspf/rank.jspf" %>
                <span class="text-choco font-medium">${ps.player.pseudo}</span>
              </div>
              <div class="flex flex-col items-end">
                <span class="text-sm font-bold text-cookie-600 bg-cookie-50 px-2 py-1 rounded">
                  <fmt:formatNumber value="${ps.player.credits}" maxFractionDigits="0" />
                </span>
                <span class="text-xs font-semibold text-cookie-600">
                  +<fmt:formatNumber value="${ps.cps}" maxFractionDigits="1" /> /s
                </span>
              </div>
            </div>
          </c:forEach>
        </div>
      </section>

      <%-- Classement par couverture, barre proportionnelle a la part de la grille (2500 cases) --%>
      <section class="card flex flex-col min-h-0 overflow-hidden">
        <div class="px-4 py-3 bg-cookie-50 border-b border-cookie-100 flex items-center gap-2">
          <i class="fas fa-border-all text-cookie-600"></i>
          <h2 class="font-display text-lg font-bold text-choco">Classement – Couverture</h2>
        </div>
        <div class="p-4 overflow-y-auto flex-1 custom-scroll">
          <c:forEach var="ps" items="${topPlayers}" varStatus="loop">
            <div class="py-2 border-b last:border-0 hover:bg-cookie-50 px-1">
              <div class="flex items-center justify-between">
                <div class="flex items-center gap-3">
                  <%@ include file="WEB-INF/jspf/rank.jspf" %>
                  <span class="text-choco font-medium">${ps.player.pseudo}</span>
                </div>
                <span class="text-sm font-bold text-cookie-600">${ps.coveragePercentForPlayer}%</span>
              </div>
              <div class="mt-1.5 ml-9 h-2 rounded-full bg-cookie-100 overflow-hidden">
                <div class="h-full rounded-full bg-cookie-500" data-width="${ps.currentPixels / 25}"></div>
              </div>
            </div>
          </c:forEach>
        </div>
      </section>

      <%-- Classement par ancienneté --%>
      <section class="card flex flex-col min-h-0 overflow-hidden">
        <div class="px-4 py-3 bg-cookie-50 border-b border-cookie-100 flex items-center gap-2">
          <i class="fas fa-hourglass-half text-cookie-600"></i>
          <h2 class="font-display text-lg font-bold text-choco">Classement – Ancienneté</h2>
        </div>
        <div class="p-4 overflow-y-auto flex-1 custom-scroll">
          <c:forEach var="ps" items="${topPlayersWithOldestPixel}" varStatus="loop">
            <div class="flex items-center justify-between py-2 border-b last:border-0 hover:bg-cookie-50 px-1">
              <div class="flex items-center gap-3">
                <%@ include file="WEB-INF/jspf/rank.jspf" %>
                <span class="text-choco font-medium">${ps.player.pseudo}</span>
              </div>
              <span class="text-xs text-gray-500 bg-gray-50 px-2 py-1 rounded border">
                <c:choose>
                  <c:when test="${ps.oldestPixel != null}">
                    <span style="display:inline-block; width:10px; height:10px; border:1px solid #999; vertical-align:middle;" data-color="<c:out value='${ps.oldestPixel.color}'/>"></span>
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
