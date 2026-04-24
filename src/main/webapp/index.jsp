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

  <main class="flex-1 flex items-center justify-center py-8">
      <canvas id="grid-canvas"
         width="1000" height="1000"
         style="image-rendering: pixelated; width: min(1000px, 90vw); height: min(1000px, 90vw);"
         class="border border-gray-300 shadow-lg">
      </canvas>
  </main>

  <script src="js/grid.js"></script>

</body>
</html>