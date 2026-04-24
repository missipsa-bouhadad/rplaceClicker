<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="fr">
<head>
  <title>rplace clicker</title>
  <script src="https://cdn.tailwindcss.com"></script>
</head>

<!-- Style avec tailwind css en utilisant l'IA -->

<body class="bg-gray-100 min-h-screen flex flex-col overflow-x-hidden">

  <%@ include file="nav.jsp" %>

  <main class="flex-1 grid grid-cols-1 md:grid-cols-3 min-h-[calc(100vh-64px)]">

    <div class="hidden md:block border-r-4 border-gray-400"></div>

    <div class="flex flex-col border-r-4 border-gray-400 p-4 pt-8 items-center">
      <button onclick="document.getElementById('grid-overlay').classList.remove('hidden')"
              class="w-[160px] h-[120px] md:h-[10%] bg-white border border-gray-300 rounded-xl shadow-sm
                     hover:shadow-md hover:bg-gray-50 transition-all duration-200 cursor-pointer
                     flex flex-col items-center justify-center gap-1">
        <span class="text-lg font-bold text-gray-800">Ouvrir la grille</span>
      </button>
    </div>

    <div class="hidden md:block"></div>

  </main>

  <div id="grid-overlay" class="hidden fixed inset-0 bg-black/70 flex items-center justify-center z-50">
    <div class="relative bg-white shadow-2xl p-4 w-[95vw] h-[95vh] flex flex-col">

      <button onclick="document.getElementById('grid-overlay').classList.add('hidden')"
              class="absolute top-3 right-4 text-gray-400 hover:text-gray-800 text-2xl bg-transparent border-none cursor-pointer transition-colors">
        &times;
      </button>

      <div class="flex-1 overflow-auto md:overflow-hidden flex items-center justify-center">
        <canvas id="grid-canvas"
                width="1000" height="1000"
                style="image-rendering: pixelated; width: calc(95vh - 2rem); height: calc(95vh - 2rem);"
                class="border border-gray-200 block">
        </canvas>
      </div>

    </div>
  </div>

  <script src="js/grid.js"></script>

</body>
</html>