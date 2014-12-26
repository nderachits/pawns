/**
 * Created by Mikalai_Dzerachyts on 12/25/2014.
 */

function allowDrop(ev) {
    ev.preventDefault();
}

function drag(ev) {
    ev.dataTransfer.setData("text", ev.target.id);
}

function drop(ev) {
    ev.preventDefault();
    var data = ev.dataTransfer.getData("text");
    var cellId = ev.target.getAttribute('id').substring("cell".length) ;
    var x = document.querySelectorAll("#"+data);
    x[0].style.top = ""+calcTopFromId(cellId)+"px";
    x[0].style.left = ""+calcLeftFromId(cellId)+"px";;
}

function calcTopFromId(id) {
    return Math.floor(id/3)*53;
}

function calcLeftFromId(id) {
    return (id%3)*53;
}

function clearPawn(el) {
    el.style.visibility = "hidden";
}