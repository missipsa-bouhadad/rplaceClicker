<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%-- activePage ("home" ou "stats") est pose par la page avant l'include, pour souligner le lien courant --%>
<c:set var="navLink" value="px-3 py-2 text-sm font-semibold border-b-2 transition" />
<c:set var="navActive" value="text-cookie-700 border-cookie-600" />
<c:set var="navIdle" value="text-gray-600 border-transparent hover:text-cookie-700 hover:border-cookie-200" />

<nav class="sticky top-0 z-40 bg-white/85 backdrop-blur border-b border-cookie-100 shadow-sm">
    <div class="mx-auto px-6 py-3 flex items-center justify-between gap-4">

        <a href="${pageContext.request.contextPath}/" class="flex items-center gap-2 font-display text-2xl font-bold text-choco hover:text-cookie-700 transition">
            <img src="${pageContext.request.contextPath}/images/cookie.png" alt="" class="w-8 h-8">
            r/Place Clicker
        </a>

        <div class="hidden md:flex items-center gap-2">
            <a href="${pageContext.request.contextPath}/" class="${navLink} ${activePage == 'home' ? navActive : navIdle}">
                <i class="fas fa-cookie-bite mr-1"></i>Jouer
            </a>
            <a href="${pageContext.request.contextPath}/stats" class="${navLink} ${activePage == 'stats' ? navActive : navIdle}">
                <i class="fas fa-chart-simple mr-1"></i>Statistiques
            </a>
        </div>

        <div class="flex items-center gap-3">

        <%-- joueurs en ligne : rempli par gridPreview.js (message "presence"), reste cache sur les pages sans websocket --%>
        <div id="online-badge" class="hidden mr-4 text-xs font-medium text-white bg-choco rounded-full px-3 py-1.5 shadow-lg cursor-default">
            <span class="inline-block w-2 h-2 mr-1 rounded-full bg-green-500 animate-pulse"></span>
            <span id="online-count"></span>
        </div>

        <div class="hidden md:flex items-center gap-3">
            <c:choose>
                <c:when test="${not empty sessionScope.player}">

                    <%-- Utilisation de l'IA pour le style du menu déroulant --%>
                    <div class="relative inline-block text-left">
                        <button id="user-menu-btn" class="flex items-center gap-2 text-choco hover:text-cookie-700 focus:outline-none transition">
                            <span class="text-sm font-semibold">${sessionScope.player.pseudo}</span>
                            <i class="fas fa-user-circle text-2xl"></i>
                            <i class="fas fa-chevron-down text-xs"></i>
                        </button>

                        <div id="user-dropdown" class="absolute right-0 mt-2 w-52 card py-1 z-50 hidden">
                            <a href="${pageContext.request.contextPath}/profile" class="block px-4 py-2 text-sm text-gray-700 hover:bg-cookie-50 transition">
                                <i class="fas fa-cog mr-2"></i>Modifier mon compte
                            </a>

                            <hr class="border-cookie-100">

                            <a href="${pageContext.request.contextPath}/logout" class="block px-4 py-2 text-sm text-red-600 hover:bg-cookie-50 transition">
                                <i class="fas fa-sign-out-alt mr-2"></i>Se déconnecter
                            </a>
                        </div>
                    </div>

                </c:when>
                <c:otherwise>
                    <a href="${pageContext.request.contextPath}/login" class="px-4 py-2 text-sm font-semibold text-cookie-700 rounded-lg hover:bg-cookie-50 transition">
                        Connexion
                    </a>
                    <a href="${pageContext.request.contextPath}/register" class="px-4 py-2 text-sm font-semibold text-white bg-cookie-600 rounded-lg hover:bg-cookie-700 transition">
                        Inscription
                    </a>
                </c:otherwise>
            </c:choose>
        </div>

        <button id="mobile-menu-btn" class="md:hidden text-choco text-2xl" aria-label="Ouvrir le menu">
            <i class="fas fa-bars"></i>
        </button>
        </div>
    </div>

    <%-- menu mobile : memes liens que la barre, empiles --%>
    <div id="mobile-menu" class="hidden md:hidden border-t border-cookie-100 px-6 py-3 flex flex-col gap-1">
        <a href="${pageContext.request.contextPath}/" class="py-2 text-sm font-semibold ${activePage == 'home' ? 'text-cookie-700' : 'text-gray-700'}">
            <i class="fas fa-cookie-bite mr-2"></i>Jouer
        </a>
        <a href="${pageContext.request.contextPath}/stats" class="py-2 text-sm font-semibold ${activePage == 'stats' ? 'text-cookie-700' : 'text-gray-700'}">
            <i class="fas fa-chart-simple mr-2"></i>Statistiques
        </a>
        <hr class="border-cookie-100 my-1">
        <c:choose>
            <c:when test="${not empty sessionScope.player}">
                <a href="${pageContext.request.contextPath}/profile" class="py-2 text-sm text-gray-700">
                    <i class="fas fa-cog mr-2"></i>Modifier mon compte (${sessionScope.player.pseudo})
                </a>
                <a href="${pageContext.request.contextPath}/logout" class="py-2 text-sm text-red-600">
                    <i class="fas fa-sign-out-alt mr-2"></i>Se déconnecter
                </a>
            </c:when>
            <c:otherwise>
                <a href="${pageContext.request.contextPath}/login" class="py-2 text-sm font-semibold text-cookie-700">Connexion</a>
                <a href="${pageContext.request.contextPath}/register" class="py-2 text-sm font-semibold text-cookie-700">Inscription</a>
            </c:otherwise>
        </c:choose>
    </div>
</nav>

<script>
    document.addEventListener('DOMContentLoaded', function() {
        const btn = document.getElementById('user-menu-btn');
        const dropdown = document.getElementById('user-dropdown');

        if (btn && dropdown) {
            btn.addEventListener('click', function(event) {
                event.stopPropagation();
                dropdown.classList.toggle('hidden');
            });

            document.addEventListener('click', function(event) {
                if (!dropdown.contains(event.target)) {
                    dropdown.classList.add('hidden');
                }
            });
        }

        const mobileBtn = document.getElementById('mobile-menu-btn');
        const mobileMenu = document.getElementById('mobile-menu');
        mobileBtn.addEventListener('click', () => mobileMenu.classList.toggle('hidden'));
    });
</script>
