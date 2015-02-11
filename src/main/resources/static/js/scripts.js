/**
 * Created by Mikalai_Dzerachyts on 12/25/2014.
 */

function allowDrop(ev) {
    ev.preventDefault();
}

function allowDropToImage(ev) {
    ev.preventDefault();
}

function getCssProperty(elmId, property){
    var elem = document.getElementById(elmId);
    return window.getComputedStyle(elem,null).getPropertyValue(property);
}

function dropToImage(ev) {
    ev.preventDefault();
    console.log("drop img: "+ev.target.id);
    var data = ev.dataTransfer.getData("text");

    if(ev.target.id === data) {
        return;
    }

    var x = document.querySelectorAll("#"+data);
    x[0].style.top = getCssProperty(ev.target.id, "top");
    x[0].style.left = getCssProperty(ev.target.id, "left");

    clearPawn(ev.target);
}

function drag(ev) {
    console.log("moving img: "+ev.target.id);
    ev.dataTransfer.setData("text", ev.target.id);
}

function drop(ev) {
    ev.preventDefault();
    var data = ev.dataTransfer.getData("text");
    console.log("move img: "+ev.target.id+", data: "+data);
    var cellId = ev.target.id.substring("cell".length) ;
    console.log("cellId: "+cellId);
    var x = document.querySelectorAll("#"+data);
    x[0].style.top = ""+calcTopFromId(cellId)+"px";
    x[0].style.left = ""+calcLeftFromId(cellId)+"px";
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