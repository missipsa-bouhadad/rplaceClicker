<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<nav class="bg-white shadow-md">
    <div class="mx-auto px-6 py-4 flex items-center justify-between">

        <a href="${pageContext.request.contextPath}/" class="text-xl font-bold text-gray-800 hover:text-gray-600 transition">
            r/Place Clicker
        </a>

        <div class="space-x-3">
            <c:choose>
                <c:when test="${not empty sessionScope.player}">

                    <%-- Utilisation de l'IA pour le style du menu déroulant --%>
                    <div class="relative inline-block text-left">
                        <button id="user-menu-btn" class="flex items-center space-x-2 text-gray-700 hover:text-gray-900 focus:outline-none transition">
                            <span class="text-sm font-medium">${sessionScope.player.pseudo}</span>
                            <i class="fas fa-user-circle text-2xl"></i>
                            <i class="fas fa-chevron-down text-xs text-gray-500"></i>
                        </button>

                        <div id="user-dropdown" class="absolute right-0 mt-2 w-48 bg-white rounded-md shadow-lg py-1 z-50 hidden border border-gray-200">
                            <a href="profile" class="block px-4 py-2 text-sm text-gray-700 hover:bg-gray-100 transition">
                                <i class="fas fa-cog mr-2"></i>Modifier mon compte
                            </a>

                            <hr class="border-gray-200">

                            <a href="logout" class="block px-4 py-2 text-sm text-red-600 hover:bg-gray-100 transition">
                                <i class="fas fa-sign-out-alt mr-2"></i>Se déconnecter
                            </a>
                        </div>
                    </div>

                </c:when>
                <c:otherwise>
                    <a href="login" class="px-4 py-2 text-sm font-medium text-white bg-blue-600 rounded-lg hover:bg-blue-700 transition">
                        Connexion
                    </a>
                    <a href="register" class="px-4 py-2 text-sm font-medium text-white bg-blue-600 rounded-lg hover:bg-blue-700 transition">
                        Inscription
                    </a>
                </c:otherwise>
            </c:choose>
        </div>

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
    });
</script>