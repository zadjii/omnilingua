
var seaLevel = 0.21;
var sandLevel = seaLevel+.02;

var water = "rgb(100, 125, 255)";
var sand = "rgb(255, 255, 125)";
var grass = "rgb(100, 255, 125)";


function drawMap (floatgrid,vec3grid, canvas) {
  reallySuperSimpleColorDraw(floatgrid, canvas);
  // drawFloatMap(floatgrid, canvas);
}
/**
  Draws the data in floatgrid as a greyscale heightmap on the canvas
*/
function drawFloatMap (floatgrid, canvas) {
  var ctx;
  if (canvas.getContext)
    ctx = canvas.getContext("2d");
  if (ctx == null || ctx == undefined) return;

  for (var x = 0; x < floatgrid.length; x++) {
    for (var y = 0; y < floatgrid.length; y++) {
      //console.log( rgb(floatgrid[x][y]));
      ctx.fillStyle = floatrgb(floatgrid[x][y]);
      ctx.fillRect(x, y, 1, 1);
    }
  }
}
/**
  Draws the data in vec3grid as a rgb heightmap on the canvas
*/
function drawVec3Map (vec3grid, canvas) {
  var ctx;
  if (canvas.getContext)
    ctx = canvas.getContext("2d");
  if (ctx == null || ctx == undefined) return;

  for (var x = 0; x < vec3grid.length; x++) {
    for (var y = 0; y < vec3grid.length; y++) {
      //console.log( rgb(floatgrid[x][y]));
      ctx.fillStyle = vec3rgb(vec3grid[x][y]);
      ctx.fillRect(x, y, 1, 1);
    }
  }
}


function reallySuperSimpleColorDraw (floatgrid, canvas) {
  // console.log("top of reallySuperSimpleColorDraw");
  var ctx;
  if (canvas.getContext)
    ctx = canvas.getContext("2d");
  if (ctx == null || ctx == undefined) return;
  // var maxHeight = 0;
  for (var x = 0; x < floatgrid.length; x++) {
    for (var y = 0; y < floatgrid.length; y++) {

      //Determine a color based of the height (and eventually other factors)
      var height = floatgrid[x][y];
      // maxHeight = height>maxHeight? maxHeight : height;
      if(height < seaLevel) ctx.fillStyle = water;
      else if(height < sandLevel) ctx.fillStyle = sand;
      else ctx.fillStyle = grass;

      ctx.fillRect(x, y, 1, 1);
    }
    // console.log("ANYTHING");
  }
  // alert(maxHeight);
  // console.log(maxHeight);
}



function floatrgb (value) {
  if(value > 1.0)
    return "rgb("+parseInt(value)+","+parseInt(value)+","+parseInt(value)+")";
  else
    return "rgb("+parseInt(value*256)+","+parseInt(value*256)+","+parseInt(value*256)+")";
}
function vec3rgb (value) {
  if(value.r() > 1.0 || value.g() > 1.0 || value.b() > 1.0)
    return "rgb("+parseInt(value.r())+","+parseInt(value.g())+","+parseInt(value.b())+")";
  else
    return "rgb("+parseInt(value.r()*256)+","+parseInt(value.g() * 256)+","+parseInt(value.b() * 256)+")";
}
