/**
 * Created by Mikalai_Dzerachyts on 12/25/2014.
 */

window.onload = function () {
    //alert("It's loaded with images!")
    loadBoard();
    connectWebSocket();
};

function connectWebSocket() {
    var ws = new WebSocket("ws://"+location.host+"/websocket");

    ws.onopen = function() {
        console.log("Opened!");
    };

    ws.onmessage = function (evt) {
        var obj = JSON.parse(evt.data);
        console.log("cells: " + obj.cells);
        clearAllPawns();
        placePawns(obj.cells);
    };

    ws.onclose = function() {
        console.log("Closed!");
    };

    ws.onerror = function(err) {
        console.log("Error: " + err);
    };
}

function newgame() {
    var xmlhttp = new XMLHttpRequest();   // new HttpRequest instance
    xmlhttp.open("POST", "/newgame");
    xmlhttp.setRequestHeader("Content-Type", "application/json;charset=UTF-8");
    xmlhttp.send(JSON.stringify({}));
    location.reload();
}

function loadBoard() {
    var xmlhttp = new XMLHttpRequest();
    xmlhttp.onreadystatechange = function() {
            if (xmlhttp.readyState==4 && xmlhttp.status==200) {
                var obj = JSON.parse(xmlhttp.responseText);
                console.log("cells: " + obj.cells);
                placePawns(obj.cells);
            }
        };
    xmlhttp.open('GET', '/board', true);
    xmlhttp.send();
}

function placePawns(cells) {
    var blackIds = ["pawn0", "pawn1", "pawn2"];
    var whiteIds = ["pawn3", "pawn4", "pawn5"];
    for(var i=0; i<9; i++) {
        var cellId = "cell"+i;
        if(cells[i] === "black") {
            var pawnId = blackIds.pop();
            placePawn(pawnId, cellId);
        } else if( cells[i] == "white") {
            var pawnId = whiteIds.pop();
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
    xmlhttp.open("POST", "/move");
    xmlhttp.setRequestHeader("Content-Type", "application/json;charset=UTF-8");
    xmlhttp.send(JSON.stringify({from:cellIndFrom, to:cellIndTo}));
}

function cellIndByPawn(pawn) {
    var top = getCssProperty(pawn, "top");
    var left = getCssProperty(pawn, "left");
    var topInt = top.substr(0, top.length-"px".length);
    var leftInt = left.substr(0, left.length-"px".length);
    var id = Math.floor(topInt/53)*3 + Math.floor(leftInt/53);
    return id;
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
    console.log("moving "+pawnToMove.id+" to "+targetPawn.id);
    pawnToMove.style.top = getCssProperty(targetPawn, "top");
    pawnToMove.style.left = getCssProperty(targetPawn, "left");
}

function dropToImage(ev) {
    ev.preventDefault();
    console.log("drop img: "+ev.target.id);
    var data = ev.dataTransfer.getData("text");

    if(ev.target.id === data) {
        return;
    }

    moveToPawn(document.getElementById(data), ev.target);
    clearPawn(ev.target);
}

function drag(ev) {
    console.log("moving img: "+ev.target.id);
    ev.dataTransfer.setData("text", ev.target.id);
}

function moveToCell(pawn, cellId) {
    console.log("cellId: "+cellId);
    var cellNum = cellId.substring("cell".length) ;
    pawn.style.top = "" + calcTopFromId(cellNum) + "px";
    pawn.style.left = "" + calcLeftFromId(cellNum) + "px";
}

function drop(ev) {
    ev.preventDefault();
    var data = ev.dataTransfer.getData("text");
    console.log("move img: "+ev.target.id+", data: "+data);
    var pawn = document.getElementById(data);

    moveListener(cellIndByPawn(pawn), ev.target.id.substring("cell".length));
    moveToCell(pawn, ev.target.id);
}

function calcTopFromId(id) {
    return Math.floor(id/3)*53;
}

function calcLeftFromId(id) {
    return (id%3)*53;
}

function clearPawn(el) {
    console.log("clear img: "+el.id);
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
    console.log("click: "+ el.id);
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
    console.log("cell click "+cell.id);
    var selectedPawn = getSelectedPawn();
    if(selectedPawn) {
        moveListener(cellIndByPawn(selectedPawn), cell.id.substring("cell".length));
        moveToCell(selectedPawn, cell.id);
        selectedPawn.classList.remove('pawnselected');
    }
}