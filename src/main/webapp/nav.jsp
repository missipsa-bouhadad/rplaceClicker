<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<nav class="bg-white shadow-md">
    <div class="max-w-7xl mx-auto px-6 py-4 flex items-center justify-between">

        <h1 class="text-xl font-bold text-gray-800">
            r/Place Clicker
        </h1>

        <div class="space-x-3">
            <c:choose>
                <c:when test="${not empty sessionScope.player}">
                    <span class="text-sm font-medium text-gray-700">${sessionScope.player.pseudo}</span>
                    <a href="logout" class="px-4 py-2 text-sm font-medium text-white bg-red-500 rounded-lg hover:bg-red-600 transition">
                        Déconnexion
                    </a>
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