<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="fr">
<head>
  <title>rplace clicker</title>
  <script src="https://cdn.tailwindcss.com"></script>
  <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.0/css/all.min.css">
</head>

<!-- Style avec tailwind css en utilisant l'IA -->

<body class="bg-gray-100 min-h-screen flex flex-col overflow-x-hidden">

  <%@ include file="nav.jsp" %>

<main class="flex-1 grid grid-cols-1 md:grid-cols-4 min-h-[calc(100vh-64px)]">

    <div class="flex flex-col items-center border-r-4 border-gray-400">
      <!--Block gauche-->
        <h2>Crédits: <span id="nb_click">${sessionScope.nb_Current_Click != null? sessionScope.nb_Current_Click : 0}</span></h2>

        <img id="credit_btn" src="" style="width: 200px;  cursor: pointer;" alt="click me">

    </div>

    <div class="flex flex-col border-r-4 border-gray-400 p-4 pt-8 items-center md:col-span-2">
      <!--Block central-->

    </div>




  <div class="flex flex-col items-center p-4">
    <!--Block droite-->
    <div onclick="openGrid()" 
      class="p-2 bg-white border-2 border-gray-800 shadow-md max-w-[250px] md:max-w-[300px]">
        <canvas id="gridCanvas" 
                width="500" 
                height="500" 
                class="w-full h-auto bg-gray-100 cursor-pointer border border-gray-200"
                style="image-rendering: pixelated;">
        </canvas>
    </div>
</div>
    




  </main>

  <div id="grid-overlay" class="hidden fixed inset-0 bg-black/70 flex items-center justify-center z-50">
    <div class="relative bg-white shadow-2xl p-4 w-[95vw] h-[95vh] flex flex-col">
      <button onclick="document.getElementById('grid-overlay').classList.add('hidden')"
              class="absolute top-3 right-4 text-gray-400 hover:text-gray-800 text-2xl bg-transparent border-none cursor-pointer transition-colors">
              &times;
      </button>

      <c:if test="${not empty sessionScope.player}">
          <button id="palette-btn"
                  onclick="handlePalette()"
                  class="absolute -right-5 top-1/2 -translate-y-1/2 bg-white border border-gray-300 rounded-full w-10 h-10 flex items-center justify-center shadow-md hover:bg-gray-50 transition-all z-30">
            <i id="palette-icon" class="fas fa-chevron-left text-gray-600"></i>
          </button>

          <!-- Drawer palette -->
          <div id="palette-drawer"
               class="hidden absolute right-0 top-0 h-full w-48 bg-white border-l border-gray-200 rounded-r-xl p-3 flex-col gap-2 z-20">

            <p class="text-xs font-semibold text-gray-500 mb-1">Couleur sélectionnée</p>
            <div id="selected-color-rectangle" class="w-full h-8 rounded" style="background:#FF0000;"></div>

            <p class="text-xs font-semibold text-gray-500 mb-1">Palette</p>
            <div class="grid grid-cols-4 gap-1">
              <div class="selected-color w-7 h-7 rounded cursor-pointer" style="background:#FF0000;" data-color="#FF0000"></div>
              <div class="selected-color w-7 h-7 rounded cursor-pointer" style="background:#FF4500;" data-color="#FF4500"></div>
              <div class="selected-color w-7 h-7 rounded cursor-pointer" style="background:#FF6600;" data-color="#FF6600"></div>
              <div class="selected-color w-7 h-7 rounded cursor-pointer" style="background:#FF8C00;" data-color="#FF8C00"></div>
              <div class="selected-color w-7 h-7 rounded cursor-pointer" style="background:#FFA500;" data-color="#FFA500"></div>
              <div class="selected-color w-7 h-7 rounded cursor-pointer" style="background:#FFD700;" data-color="#FFD700"></div>
              <div class="selected-color w-7 h-7 rounded cursor-pointer" style="background:#FFFF00;" data-color="#FFFF00"></div>
              <div class="selected-color w-7 h-7 rounded cursor-pointer" style="background:#ADFF2F;" data-color="#ADFF2F"></div>
              <div class="selected-color w-7 h-7 rounded cursor-pointer" style="background:#00FF00;" data-color="#00FF00"></div>
              <div class="selected-color w-7 h-7 rounded cursor-pointer" style="background:#00FA9A;" data-color="#00FA9A"></div>
              <div class="selected-color w-7 h-7 rounded cursor-pointer" style="background:#00FFFF;" data-color="#00FFFF"></div>
              <div class="selected-color w-7 h-7 rounded cursor-pointer" style="background:#00BFFF;" data-color="#00BFFF"></div>
              <div class="selected-color w-7 h-7 rounded cursor-pointer" style="background:#0000FF;" data-color="#0000FF"></div>
              <div class="selected-color w-7 h-7 rounded cursor-pointer" style="background:#8A2BE2;" data-color="#8A2BE2"></div>
              <div class="selected-color w-7 h-7 rounded cursor-pointer" style="background:#FF00FF;" data-color="#FF00FF"></div>
              <div class="selected-color w-7 h-7 rounded cursor-pointer" style="background:#FF1493;" data-color="#FF1493"></div>
              <div class="selected-color w-7 h-7 rounded cursor-pointer" style="background:#FF69B4;" data-color="#FF69B4"></div>
              <div class="selected-color w-7 h-7 rounded cursor-pointer" style="background:#FFC0CB;" data-color="#FFC0CB"></div>
              <div class="selected-color w-7 h-7 rounded cursor-pointer" style="background:#FFFFFF;" data-color="#FFFFFF"></div>
              <div class="selected-color w-7 h-7 rounded cursor-pointer" style="background:#D3D3D3;" data-color="#D3D3D3"></div>
              <div class="selected-color w-7 h-7 rounded cursor-pointer" style="background:#A9A9A9;" data-color="#A9A9A9"></div>
              <div class="selected-color w-7 h-7 rounded cursor-pointer" style="background:#696969;" data-color="#696969"></div>
              <div class="selected-color w-7 h-7 rounded cursor-pointer" style="background:#000000;" data-color="#000000"></div>
              <div class="selected-color w-7 h-7 rounded cursor-pointer" style="background:#8B4513;" data-color="#8B4513"></div>
              <div class="selected-color w-7 h-7 rounded cursor-pointer" style="background:#A0522D;" data-color="#A0522D"></div>
              <div class="selected-color w-7 h-7 rounded cursor-pointer" style="background:#D2691E;" data-color="#D2691E"></div>
              <div class="selected-color w-7 h-7 rounded cursor-pointer" style="background:#F4A460;" data-color="#F4A460"></div>
              <div class="selected-color w-7 h-7 rounded cursor-pointer" style="background:#DEB887;" data-color="#DEB887"></div>
              <div class="selected-color w-7 h-7 rounded cursor-pointer" style="background:#FFDEAD;" data-color="#FFDEAD"></div>
              <div class="selected-color w-7 h-7 rounded cursor-pointer" style="background:#FFFACD;" data-color="#FFFACD"></div>
              <div class="selected-color w-7 h-7 rounded cursor-pointer" style="background:#E6E6FA;" data-color="#E6E6FA"></div>
              <div class="selected-color w-7 h-7 rounded cursor-pointer" style="background:#B0C4DE;" data-color="#B0C4DE"></div>
            </div>
          </div>
      </c:if>

      <div class="flex-1 overflow-auto md:overflow-hidden flex items-center justify-center">
        
        <canvas id="grid-canvas"
                width="1000" height="1000"
                style="image-rendering: pixelated; width: calc(95vh - 2rem); height: calc(95vh - 2rem);"
                class="border border-gray-200 block">
        </canvas>
      </div>

    </div>
  </div>

  <script>
    const isConnected = ${not empty sessionScope.player ? "true" : "false"};
    const contextPath = "${pageContext.request.contextPath}";
  </script>

  
  <script src="js/grid.js"></script>
  <script src="js/clicker.js"></script>
  <script src="js/autosave.js"></script>
  <script src="js/gridPreview.js"></script>



</body>
</html>