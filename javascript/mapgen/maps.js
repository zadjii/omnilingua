/*
Map Generator - Javascript Version
Generates a map based on the size of the current canvas.
Allows the user to specify the seed and size of the map.

There isn't really a mechanism currently to directly save it
*/

var MAX_SIZE = 1024;
// var MAPS_DIRECTORY = __dirname + "/maps/";
// var FAVS_DIRECTORY = __dirname + "/maps/fav/";

var size;
var seed = 0;



/*
Random number generation, straight from http://stackoverflow.com/a/19301306
*/
var m_w = 123456789;
var m_z = 987654321;
var mask = 0xffffffff;
var RAND_MAX = 4294967296;
// Takes any integer
function seed_random(i) {
    m_w = i;
    m_z = 987654321;
}
// Returns number between 0 (inclusive) and 1.0 (exclusive),
// just like Math.random().
function random(){
    m_z = (36969 * (m_z & 65535) + (m_z >> 16)) & mask;
    m_w = (18000 * (m_w & 65535) + (m_w >> 16)) & mask;
    var result = ((m_z << 16) + m_w) & mask;
    result /= 4294967296;
    return result + 0.5;
}
/**
  Resizes the canvas and the map.
  Caps to MAX_SIZE
*/
function resize () {
  // alert("pressed resize");
  var canvas;
  var newSize;
  canvas = document.getElementById("canvas");
  if(canvas == null) return;
  newSize = parseInt( document.getElementById("size_box").value);
  if(isNaN(newSize)){
    document.getElementById("size_box").value = size;
    return;
  }

  if(newSize > MAX_SIZE)newSize = MAX_SIZE;
  if(newSize < 1)newSize = 1;
  canvas.width = newSize;
  canvas.height = newSize;
  size = newSize;
  document.getElementById("size_box").value = size;
  initializeCanvas();
}

function onPageLoad(){
  initializeCanvas();
  updateSeed();
  seed_random(seed);
}

function generate () {
  updateSeed();
  generateMap(canvas, size, random);

}
function getNewSeed(){
  document.getElementById("seed_box").value = (RAND_MAX * random());
  updateSeed();
}
function updateSeed () {
  var newSeed;
  newSeed = parseInt( document.getElementById("seed_box").value );
  //alert(typeof newSeed);
  if(!isNaN(newSeed)){
    seed = newSeed;
    //alert("true " + seed);
  }
  else{
    document.getElementById("seed_box").value = (seed);  
    //alert("false " + seed);
  }
  document.getElementById("seed_text").innerHTML = ("Current Seed: " + seed);
  seed_random(seed);
}
function onKeyDown(event){}
function onKeyUp(event){}
function onMouseMove(event){}
function onMousePress(event){}


function initializeCanvas(){
  var canvas, ctx;
  canvas = document.getElementById("canvas");
  if (canvas.getContext)
    ctx = canvas.getContext("2d");
  if (ctx == null || ctx == undefined) return;

  size = canvas.width;

  ctx.fillStyle = "rgb(0,0,0)";
  ctx.fillRect(0, 0, size, size);
  //alert("finished drawing");
}








