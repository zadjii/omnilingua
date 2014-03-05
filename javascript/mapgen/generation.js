function vec3 (x, y, z) {
  this.x = x;
  this.y = y;
  this.z = z;
  this.r = function(){return this.x;}
  this.g = function(){return this.y;}
  this.b = function(){return this.z;}
}
function vec2 (x, y) {
  this.x = x;
  this.y = y;
  this.u = function(){return this.x;}
  this.v = function(){return this.y;}
}
function generateMap (canvas, size, random) {
  // alert(canvas + ", " + size + ", " + random());

  var heights = new Array(size);
  var colors = new Array(size);

  for (var i = 0; i < heights.length; i++) {
    heights[i] = new Array(size);
    colors[i] = new Array(size);
  };

  for (var x = 0; x < heights.length; x++) {
    for (var y = 0; y < heights.length; y++) {
      heights[x][y] = 0.0;
      // heights[x][y] = random();
      colors[x][y] = new vec3(random(), random(), random());
    }
  }
  
  floatDrop00(heights, new vec2(size/2, size/2), 5*size*size, .01, .02, size/2, 0, 0.9);
  floatDrop00(heights, new vec2(size/2, size/2), 5*size*size, .01, .02, size/2, 0, 0.9);
  drawFloatMap(heights, canvas);
  // floatDrop00(heights, new vec2(-1, -1), 10*size*size, .01, .02, size, 0, 1.0);
  // drawFloatMap(heights, canvas);
  // floatDrop00(heights, new vec2(-1, -1), 10*size*size, .01, .02, size, 0, 1.0);
  // floatDrop00(heights, new vec2(size/2, size/2), 10*size*size, .01, .02, size, 0, 1.0);
  // drawFloatMap(heights, canvas);
  // setTimeout(function(){drawVec3Map(colors, canvas);},2 * 1000);

}

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



function floatDrop00(grid, origin, drops, increment, rollingThreshold, radius,  minHeight,  maxHeight) {
  var size = grid.length;
  if(!gridContains(grid, origin))
    origin = new vec2(random() * size, random() * size);
  var pos = new vec2(origin.x, origin.y);
  var height;
  var placed;

  for(var dropped = 0; dropped < drops; dropped++){
    var x = parseInt(pos.x), y = parseInt(pos.y);
//    std::cout<<"\t\t"<<x<<","<<y<<";";
    height = (grid[x][y]) - rollingThreshold;
    placed = false;

    for (var dx = -1; dx <= 1; dx++){
      if(x+dx < 0 || x+dx >= size || placed)continue;

      for (var dy = -1; dy <= 1; dy++) {
        if(dx == 0 && dy == 0)continue;
        if(y+dy < 0 || y+dy >= size)continue;

        if(grid[x+dx][y+dy] <= height){
          if(grid[x+dx][y+dy]<maxHeight && grid[x+dx][y+dy]>=minHeight)
            grid[x+dx][y+dy] += increment;
          placed = true;
          break;
        }
      }
    }//end double for()
    if(!placed && grid[x][y] < maxHeight && grid[x][y] >= minHeight){
      grid[x][y] += increment;
    }
    var oldX = x;
    var oldY = y;

    var newX = x + parseInt(random()*4 - 2);
    var newY = y + parseInt(random()*4 - 2);

    if(newX < 0) newX = size-1;
    else if(newX >= size) newX = 0;

//    if(newY < 0) newY = 1;
    if(newY < 0) newY = origin.y;
//    else if(newY >= size) newY = size-1;
    else if(newY >= size) newY = origin.y;

    pos = new vec2(newX, newY);

    if(vec2length(pos,origin) > radius){
      pos = origin;
    }
  }

}

function vec2length (a,b) {
  return Math.sqrt(
    (a.x-b.x)*(a.x-b.x) + (a.y-b.y)*(a.y-b.y)
  ); 
}

function javaRand(range){
  return range * random();
}
function gridContains(grid, point){
  var size = grid.length;
  if(point.x < 0)return false;
  if(point.y < 0)return false;
  if(point.x >= size)return false;
  if(point.y >= size)return false;
  return true;
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




