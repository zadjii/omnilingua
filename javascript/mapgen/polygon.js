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
function PolyNode (point) {
  this.point = point;
  this.x = point.x;
  this.y = point.y;
  // this.prev = null;
  // this.next = null;
  this.data = null;
}
function Polygon () {
  this.nodes = [];

//the "bounds" of the polygon
  this.minX = -1;
  this.maxX = -1;
  this.minY = -1;
  this.maxY = -1;

  this.insert = function (node, index) {
    if(index > nodes.length){
      this.nodes.push(node);
    }
    else
      this.nodes.splice(index, 0, node);
  };//end of insert
  this.add = function (point) {
    var node = new PolyNode(point);
    this.nodes.push(node);
    this.updateBounds(node);
  };//end of add
  this.updateBounds = function(node){
    if(this.maxX == -1)this.maxX = node.point.x;
    if(this.maxY == -1)this.maxY = node.point.y;
    if(this.minX == -1)this.minX = node.point.x;
    if(this.minY == -1)this.minY = node.point.y;

    if(node.point.x > this.maxX)this.maxX = node.point.x;
    if(node.point.y > this.maxY)this.maxY = node.point.y;

    if(node.point.x < this.minX)this.minX = node.point.x;
    if(node.point.y < this.minY)this.minY = node.point.y;
  }


  this.contains = function(point) {
    var nodes = this.nodes;
    var i = 0, j = nodes.length-1;
    var oddNodes = 0;
    var x = point.x;
    var y = point.y;
    // console.log("looking at:" + x + "," + y);
    // var oddNodesString = "";
    for (i=0; i<nodes.length; i++) {
      if ((nodes[i].y < y && nodes[j].y >= y
        ||   nodes[j].y < y && nodes[i].y >= y)
        &&  (nodes[i].x <=x || nodes[j].x <= x)) {

        oddNodes ^= (nodes[i].x+(y-nodes[i].y)/(nodes[j].y-nodes[i].y)*(nodes[j].x-nodes[i].x) < x);
        // oddNodesString += (oddNodes + ",");
        // console.log(oddNodes+",");
      }
      j=i;
    }
    // console.log(oddNodesString);
    // console.log(oddNodes+":" + nodes.length);
    return oddNodes;
  }

}