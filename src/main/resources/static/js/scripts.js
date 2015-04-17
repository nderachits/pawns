/**
 * Created by Mikalai_Dzerachyts on 12/25/2014.
 */

window.onload = function () {
    window.gameId=document.getElementById("gameId").textContent;
    //alert("It's loaded with images! gameId: "+window.gameId);
    connectWebSocket();

    document.addEventListener("visibilitychange", updateVisibilityState);
};

function updateVisibilityState(event) {
    console.log("Event: " + event.type+", hidden:"+document["hidden"] );
    if (!document["hidden"]) {
        console.log("updateOnlineStatus reconnect");
        if(window["ws"] ) {
            window.ws.onclose = function(){};
            window.ws.close();
        }
        connectWebSocket();
    }
}

function connectWebSocket() {

    window.ws = new WebSocket("ws://"+location.host+"/websocket?gameId="+window.gameId);

    window.ws.onopen = function() {
        console.log("Opened!");
    };

    window.ws.onmessage = function (evt) {
        var obj = JSON.parse(evt.data);
        console.log("cells from websocket: " + obj.cells);
        clearAllPawns();
        placePawns(obj.cells);
    };

    window.ws.onclose = function() {
        console.log("Closed!");
        if (typeof document["hidden"] === "undefined" || !document["hidden"]) {
            setTimeout(function () {
                // Connection has closed so try to reconnect every 10 seconds.
                console.log("try reconnect");
                connectWebSocket();
            }, 10 * 1000);
        }
    };

    window.ws.onerror = function(err) {
        console.log("Error: " + err);
    };
}

function newgame() { //depricated, not used
    var xmlhttp = new XMLHttpRequest();   // new HttpRequest instance
    xmlhttp.open("POST", "/newgame");
    xmlhttp.setRequestHeader("Content-Type", "application/json;charset=UTF-8");
    xmlhttp.send(JSON.stringify({}));
}

function loadBoard() {
    var xmlhttp = new XMLHttpRequest();
    xmlhttp.onreadystatechange = function() {
            if (xmlhttp.readyState==4 && xmlhttp.status==200) {
                var obj = JSON.parse(xmlhttp.responseText);
                console.log("cells from rest: " + obj.cells);
                placePawns(obj.cells);
            }
        };
    xmlhttp.open('GET', "/board/"+window.gameId, true);
    xmlhttp.send();
}

function placePawns(cells) {
    var blackIds = ["pawn0", "pawn1", "pawn2"];
    var whiteIds = ["pawn3", "pawn4", "pawn5"];
    for(var i=0; i<9; i++) {
        var cellId = "cell"+i;
        if(cells[i] === "black") {
            var pawnId = blackIds.shift();
            placePawn(pawnId, cellId);
        } else if( cells[i] == "white") {
            var pawnId = whiteIds.shift();
            placePawn(pawnId, cellId);
        }
    }
}

function placePawn(pawnId, cellId) {
    var pawn = document.getElementById(pawnId);
    moveToCell(pawn, cellId);
    pawn.style.display = "block";
}

function clearAllPawns() {
    for (var i = 0; i < 6; i++) {
        var pawnId = "pawn"+i;
        clearPawn(document.getElementById(pawnId));
    }
}

function moveListener(cellIndFrom, cellIndTo) {
    console.log("cellIndFrom: "+cellIndFrom+", cellIndTo: "+cellIndTo);
    //ajaxcall
    var xmlhttp = new XMLHttpRequest();   // new HttpRequest instance
    xmlhttp.open("POST", "/move/"+window.gameId);
    xmlhttp.setRequestHeader("Content-Type", "application/json;charset=UTF-8");
    xmlhttp.send(JSON.stringify({from:cellIndFrom, to:cellIndTo}));
    xmlhttp.onreadystatechange = function() {
        if (xmlhttp.readyState==4 && xmlhttp.status!==200) {
            //alert("Server error!");
            loadBoard();
        }
    };
}

function cellIndByPawn(pawn) {
    var cellClass = pawn.className.match(/\bposcell\d+\b/);
    var cellInd = cellClass[0].substring("poscell".length);
    console.log("cellIndByPawn pawn: "+pawn+", return: "+cellInd);
    return cellInd;
}

function allowDrop(ev) {
    ev.preventDefault();
}

function allowDropToImage(ev) {
    ev.preventDefault();
}

function getCssProperty(elem, property){
    return window.getComputedStyle(elem,null).getPropertyValue(property);
}

function moveToPawn(pawnToMove, targetPawn) {
    moveListener(cellIndByPawn(pawnToMove), cellIndByPawn(targetPawn));
    console.log("moveToPawn from  pawn "+pawnToMove.id+" to pawn "+targetPawn.id);
    var cellClass = targetPawn.className.match(/\bposcell\d+\b/);
    pawnToMove.className = pawnToMove.className.replace(/\bposcell\d+\b/,'');
    pawnToMove.classList.add(cellClass);
}

function dropToImage(ev) {
    ev.preventDefault();
    var data = ev.dataTransfer.getData("text");

    if(ev.target.id === data) {
        return;
    }
    console.log("drop pawn "+ev.target.id + " to pawn "+data);

    moveToPawn(document.getElementById(data), ev.target);
    clearPawn(ev.target);
}

function drag(ev) {
    console.log("drag pawn: "+ev.target.id);
    ev.dataTransfer.setData("text", ev.target.id);

    var selected = getSelectedPawn();
    if(selected) {
        pawnClick(selected);
    }
}

function moveToCell(pawn, cellId) {
    console.log("moveToCell: pawn "+pawn.id+" to "+cellId);
    var cellNum = cellId.substring("cell".length) ;

    pawn.className = pawn.className.replace(/\bposcell\d+\b/,'');
    pawn.classList.add("pos" + cellId);
    console.log("pawn "+pawn.id+" classList: "+pawn.classList);
}

function drop(ev) {
    ev.preventDefault();
    var data = ev.dataTransfer.getData("text");
    console.log("drop: pawn "+data+" to cell "+ev.target.id);
    var pawn = document.getElementById(data);

    moveListener(cellIndByPawn(pawn), ev.target.id.substring("cell".length));
    moveToCell(pawn, ev.target.id);
}

function clearPawn(el) {
    console.log("clear: pawn "+el.id);
    el.style.display = "none";
}

function getSelectedPawn() {
    for (var i = 0; i < 6; i++) {
        var pawn = document.getElementById("pawn" + i);
        if (pawn.classList.contains("pawnselected")) {
            return pawn;
        }
    }
}
function pawnClick(el) {
    console.log("click: at pawn "+ el.id);
    var selectedPawn = getSelectedPawn();
    console.log("selectedPawn: "+selectedPawn);
    if(selectedPawn) {
        if (selectedPawn.id !== el.id) {
            moveToPawn(selectedPawn, el);
            clearPawn(el);
        }
        selectedPawn.classList.remove('pawnselected');
    } else {
        el.classList.add('pawnselected');
    }
}

function moveClick(cell) {
    console.log("moveClick: at cell "+cell.id);
    var selectedPawn = getSelectedPawn();
    if(selectedPawn) {
        moveListener(cellIndByPawn(selectedPawn), cell.id.substring("cell".length));
        moveToCell(selectedPawn, cell.id);
        selectedPawn.classList.remove('pawnselected');
    }
}