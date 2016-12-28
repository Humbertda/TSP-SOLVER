/**
 * This file is a lot based on this github project
 * @source https://github.com/yanatan16/d3-tsp-demo
 * some adaptation were made to make it look like an API
 */
;var tspDrawer = (function (d3, _) {
	'use strict';

	var width = 600,
	    height = 600,
		optimalCitySize = 5,
	    cities = [],
		edges = [];

	var path = d3.geo.path();

	var svg = d3.select("#tsp")
			.append("div")
			.classed("svg-container", true)
			.append("svg")
			.attr("preserveAspectRatio", "xMinYMin meet")
			.attr("viewBox", "0 0 " + width + " " + height)
			.classed("svg-content-responsive", true)
		;

	// Arrows
	svg.append("svg:defs")
	  .append("svg:marker")
	    .attr("id", "directed-line")
	    .attr("viewBox", "0 -5 10 10")
	    .attr("refX", 15)
	    .attr("refY", -1.5)
	    .attr("markerWidth", 6)
	    .attr("markerHeight", 6)
	    .attr("orient", "auto")
	  .append("svg:path")
	    .attr("d", "M0,-5L10,0L0,5");

	var rect = svg.append("rect");
	
	rect.on('click', clickMap)
		.attr("class", "background")
		.attr("width", width)
		.attr("height", height);
	
	var createCity = function(v){
		return _.extend({
			x: 0.0,
			y: 0.0
		}, v);
	};
	
	var createPath = function(v){
		return _.extend({
			cost: 0,
			from: {
				vertexInfo: {
					x: 0.0,
					y: 0.0
				}
			},
			to: {
				vertexInfo: {
					x: 0.0,
					y: 0.0
				}
			}
		}, v);
	};
	
	function clickMap () {
		drawCities();
		var created = createCity({
			x: d3.mouse(this)[0],
			y: d3.mouse(this)[1]
		});
		_.each(onModificationObservers, function(e){
			e(created);
		});
	}
	
	/**
	 * Draw all the cities from the cities[] var
	 */
	function drawCities() {
		svg.selectAll('circle').data(cities).enter()
			.append('circle')
				.attr('cx', function (d) { return d.x; })
				.attr('cy', function (d) { return d.y; })
				.attr('r', optimalCitySize)
				.attr('class', 'city');
	}
	
	/**
	 * Draw all the path
	 */
	function drawPaths() {
		svg.selectAll('path.connection').remove();
		svg.selectAll('path.connection').data(edges).enter()
			.append('path')
			.attr('d', function(d) {
				var from = d['from']['vertexInfo'];
				var to = d['to']['vertexInfo'];
				var cost = d['cost'];
				var dx = to.x - from.x,
					dy = to.y - from.y,
					dr = Math.sqrt(dx * dx + dy * dy);
				return "M" + from.x + "," + from.y + "A" + dr + "," + dr + " 0 0,1 " + to.x + "," + to.y;
			})
			.attr("stroke-width", function(d) {
				return (optimalCitySize*.3)+"px";
			})
			.attr('class', 'connection')
			//.attr("marker-end", "url(#directed-line)"); // This add an arrow to the end
	}
	
	var onModificationObservers = [];
	function onModification(fn){
		if(typeof fn !== "function"){
			fn = function(r){
				alert("Wrong parameters type for onModification");
			}
		}
		onModificationObservers.push(fn);
	}
	
	function drawState(newState){
		var json = JSON.parse(newState);
		var rawCities = json['cities'];
		var rawEdges = json['edges'];
		cities = [];
		var maxX = -100000000;
		var maxY = -100000000;
		for(var i = 0; i < rawCities.length; i++){
			var nfo = rawCities[i].vertexInfo;
			cities.push(createCity({
				x: nfo.x,
				y: nfo.y
			}));
			if(nfo.x > maxX){
				maxX = nfo.x;
			}
			if(nfo.y > maxY){
				maxY = nfo.y;
			}
		}
		edges = [];
		for(i = 0 ; i < rawEdges.length; i++){
			edges.push(createPath(rawEdges[i]));
		}
		width = maxX;
		height = maxY;
		optimalCitySize = (width+height)/2*.01;
		svg.attr("viewBox", 0 + " " + 0 + " " + maxX + " " + maxY);
		rect.attr("width", width)
			.attr("height", height);
		drawCities();
		drawPaths();
	}
	
	return {
		drawState: drawState,
		onModification: onModification
	}
})(d3, _);