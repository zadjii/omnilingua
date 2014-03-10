
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
  //grid, origin, drops, increment, rollingThreshold, radius,  minHeight,  maxHeight
  brownianGen00(heights, new vec2(-1,-1), 2*size*size, .02, size, 0, 0.5);
  floatDrop00(heights, new vec2(-1,-1), 0.5*size*size, .04, .05, size*.26, 0, 0.9);
  brownianPolygonGen00(heights, new vec2(-1,-1), 10*size, .25, size*.26, 0, 0.5);
  // floatDrop00(heights, new vec2(-1,-1), 0.5*size*size, .04, .05, size*.26, 0, 0.9);
  // floatDrop00(heights, new vec2(-1,-1), 0.5*size*size, .04, .05, size*.26, 0, 0.9);
  smoothFloats(heights, 2);

  // floatDrop00(heights, new vec2(-1,-1), 100*size, .04, .05, size*.26, 0, 0.9);
  // floatDrop00(heights, new vec2(-1,-1), 100*size, .04, .05, size*.26, 0, 0.9);
  // floatDrop00(heights, new vec2(-1,-1), 100*size, .04, .05, size*.26, 0, 0.9);
  // floatDrop00(heights, new vec2(-1,-1), 100*size, .04, .05, size*.26, 0, 0.9);
  // brownianGen00(heights, new vec2(-1,-1), 2*size*size, .01, size*.26, 0, 0.8);

  
  // brownianPolygonGen00(heights, new vec2(size/2, size/2), 10*size, .2, size*.6, 0, 0.5);
  // brownianPolygonGen00(heights, new vec2(size/2, size/2), 10*size, .2, size*.6, 0, 0.5);
  // brownianGen00(heights, new vec2(-1,-1), 2*size*size, .01, size*.6, 0, 0.5);
  // brownianGen00(heights, new vec2(-1,-1), 2*size*size, .01, size*.6, 0, 0.5);
  // floatDrop00(heights, new vec2(-1,-1), 5*size*size, .01, .01, size*.6, 0, 0.9);
  // floatDrop00(heights, new vec2(size/2, size/2), 5*size*size, .01, .02, size, 0, 0.9);
  
  // drawFloatMap(heights, canvas);
  drawMap(heights, null, canvas);

  // floatDrop00(heights, new vec2(-1, -1), 10*size*size, .01, .02, size, 0, 1.0);
  // drawFloatMap(heights, canvas);
  // floatDrop00(heights, new vec2(-1, -1), 10*size*size, .01, .02, size, 0, 1.0);
  // floatDrop00(heights, new vec2(size/2, size/2), 10*size*size, .01, .02, size, 0, 1.0);
  // drawFloatMap(heights, canvas);
  // setTimeout(function(){drawVec3Map(colors, canvas);},2 * 1000);

}




function floatDrop00(grid, origin, drops, increment, rollingThreshold, radius,  minHeight,  maxHeight) {
  var directions = [new vec2(-1,0),new vec2(1,0),new vec2(0,-1),new vec2(0,1),new vec2(-1,1),new vec2(-1,-1),new vec2(1,-1),new vec2(1,1)];
  var size = grid.length;
  if(!gridContains(grid, origin))
    // origin = new vec2(
    //   size/2 + (random() * radius/2) - radius/4,
    //   size/2 + (random() * radius/2) - radius/4
    // );
    //origin = new vec2(random()*size, random()*size);
    origin = new vec2(
      size/8 + (random() * size/2),
      size/8 + (random() * size/2)
    );

  var pos = new vec2(parseInt(origin.x), parseInt(origin.y));
  var height;
  var placed;

  for(var dropped = 0; dropped < drops; dropped++){
    var x = parseInt(pos.x), y = parseInt(pos.y);
    height = (grid[x][y]) - rollingThreshold;
    placed = false;

    DROP:for (var dx = -1; dx <= 1; dx++){
      if(x+dx < 0 || x+dx >= size || placed)continue;

      for (var dy = -1; dy <= 1; dy++) {
        if(dx == 0 && dy == 0)continue;
        if(y+dy < 0 || y+dy >= size)continue;

        if(grid[x+dx][y+dy] <= height){
          if(grid[x+dx][y+dy] < maxHeight && grid[x+dx][y+dy] >= minHeight)
            grid[x+dx][y+dy] += increment;
          placed = true;
          break DROP;
        }
      }
    }//end double for()
    if(!placed && grid[x][y] < maxHeight && grid[x][y] >= minHeight){
      grid[x][y] += increment;
    }
    var oldX = x;
    var oldY = y;

    var step = 2.0;
    var index = randomInt(0, directions.length);
    var newX = x + (directions[index].x)*step;
    var newY = y + (directions[index].y)*step;

  //Okay, rand*4-2 is definitely [-1,1]
  // for(var j=0;j<100;j++){
  //   var x = parseInt(random()*4 - 2);
  //   var y = parseInt(random()*4 - 2);
  //   console.log(x + "," + y + "\n");
  // }

    if(newX < 0) newX = size-1;
    else if(newX >= size) newX = 0;

   // if(newY < 0) newY = 1;
    if(newY < 0) newY = origin.y;
   // else if(newY >= size) newY = size-1;
    else if(newY >= size) newY = origin.y;

    pos = new vec2(newX, newY);

    if(vec2length(pos,origin) > radius){
      pos = origin;
      // pos = new vec2(oldX, oldY);
      // pos = new vec2(
      //   size/4 + (random() * size/2),
      //   size/4 + (random() * size/2)
      // );
    }
  }

}

function brownianGen00(grid, origin, iterations, increment, radius, minHeight, maxHeight){
  var directions = [
    new vec2(-1,0),
    new vec2(1,0),
    new vec2(0,-1),
    new vec2(0,1)
  ];
  var size = grid.length;
  if(!gridContains(grid, origin))
    // origin = new vec2(
    //   size/2 + (random() * radius/2) - radius/4,
    //   size/2 + (random() * radius/2) - radius/4
    // );
    //origin = new vec2(random()*size, random()*size);
    origin = new vec2(
      size/4 + (random() * size/2),
      size/4 + (random() * size/2)
    );

  var pos = new vec2(parseInt(origin.x), parseInt(origin.y));

  for(var dropped = 0; dropped < iterations; dropped++){
    var x = parseInt(pos.x), y = parseInt(pos.y);
    
    if(grid[x][y] >= minHeight && grid[x][y] < maxHeight)
      grid[x][y] += increment;

    // var index = parseInt(random() * directions.length);
    var index = randomInt(0, directions.length);

    var step = 1.0;
    var oldX = x;
    var oldY = y;
    if(directions[index] == undefined){
      console.log(index);
      console.log(directions);
      console.log(random);
    }
    var newX = x + (directions[index].x)*step;
    var newY = y + (directions[index].y)*step;

    if(newX < 0) newX = size-1;
    else if(newX >= size) newX = 0;

    if(newY < 0) newY = origin.y;
    else if(newY >= size) newY = origin.y;

    pos = new vec2(newX, newY);

    if(vec2length(pos,origin) > radius){
      pos = origin;
      // pos = new vec2(oldX, oldY);
      // pos = new vec2(
      //   size/4 + (random() * size/2),
      //   size/4 + (random() * size/2)
      // );
    }
  }
}

function brownianPolygonGen00(grid, origin, iterations, increment, radius, minHeight, maxHeight){
  var directions = [new vec2(-1,0),new vec2(1,0),new vec2(0,-1),new vec2(0,1)];
  var poly = new Polygon();
  var size = grid.length;
  if(!gridContains(grid, origin))
    // origin = new vec2(
    //   size/2 + (random() * radius/2) - radius/4,
    //   size/2 + (random() * radius/2) - radius/4
    // );
    //origin = new vec2(random()*size, random()*size);
    origin = new vec2(
      size/4 + (random() * size/2),
      size/4 + (random() * size/2)
    );

  var pos = new vec2(parseInt(origin.x), parseInt(origin.y));

  for(var dropped = 0; dropped < iterations; dropped++){
    var x = parseInt(pos.x), y = parseInt(pos.y);
    
    if(grid[x][y] >= minHeight && grid[x][y] < maxHeight){
      //grid[x][y] += increment;
      poly.add(new vec2(x, y));
    }

    var index = randomInt(0, directions.length);
    
    var step = 2.0;
    var oldX = x;
    var oldY = y;
    var newX = x + (directions[index].x)*step;
    var newY = y + (directions[index].y)*step;

    // if(newX < 0) newX = size-1;
    // else if(newX >= size) newX = 0;
    if(newX < 0) newX = oldX;
    else if(newX >= size) newX = oldX;

    // if(newY < 0) newY = origin.y;
    // else if(newY >= size) newY = origin.y;
    if(newY < 0) newY = oldY;
    else if(newY >= size) newY = oldY

    pos = new vec2(newX, newY);

    if(vec2length(pos,origin) > radius){
      // pos = origin;
      pos = new vec2(oldX, oldY);
      // pos = new vec2(
      //   size/4 + (random() * size/2),
      //   size/4 + (random() * size/2)
      // );
    }
  }//end of points generation
  
  // poly = new Polygon();
  // poly.add(new vec2(20, 20));
  // poly.add(new vec2(40, 20));
  // poly.add(new vec2(40, 40));
  // poly.add(new vec2(20, 40));


  // console.log(poly);

  for(var x = poly.minX; x < poly.maxX; x++){
    for(var y = poly.minY; y < poly.maxY; y++){
      if(poly.contains(new vec2(x, y))){
        // console.log("Contained: " + x + "," + y + "\n");
        grid[x][y] += increment;
      }
    }
  }

}

function smoothFloats (grid, radius) {
  if(radius <= 0)return;
  for(var x = radius; x < grid.length-radius; x++){
    for(var y = radius; y < grid.length-radius; y++){
      var sum = 0;
      var indicies = 0;
      for(var dx = -radius; dx <= radius; dx++){
        for(var dy = -radius; dy <= radius; dy++){
          // if(dx == 0 && dy == 0)continue;
          sum += grid[x+dx][y+dy];
          indicies++;
        }
      }
      sum /= indicies;
      grid[x][y] = sum;
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



