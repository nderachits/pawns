/**
 * Created by Mikalai_Dzerachyts on 12/25/2014.
 */

window.onload = function () {
    //alert("It's loaded with images!")
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
    var x = document.getElementById(data);
    moveToCell(x, ev.target.id);
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
        moveToCell(selectedPawn, cell.id);
        selectedPawn.classList.remove('pawnselected');
    }
}