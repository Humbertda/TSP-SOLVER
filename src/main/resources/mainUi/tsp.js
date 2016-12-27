/**
 * This file is a lot based on this github project
 * @source https://github.com/yanatan16/d3-tsp-demo
 * some adaptation were made to make it look like an API
 */
;var tspDrawer = (function (d3, _) {
	'use strict';

	var width = 600,
	    height = 600,
	    centered,
	    dotscale = 5,
	    cities = [];

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

	svg.append("rect")
		.on('click', clickMap)
		.attr("class", "background")
		.attr("width", width)
		.attr("height", height)
	;
	
	var g = svg.append("g")
	    .attr("id", "states");
	
	function clickMap () {
		cities.push(d3.mouse(this));
		drawCities();
		_.each(onModificationObservers, function(e){
			e(cities);
		});
	}
	
	/**
	 * Reset all the map data
	 */
	function reset () {
		cities = [];
		svg.selectAll('circle').remove();
		svg.selectAll('path.connection').remove();
	}
	
	/**
	 * Draw all the cities from the cities[] var
	 */
	function drawCities() {
		svg.selectAll('circle').data(cities).enter()
			.append('circle')
				.attr('cx', function (d) { return d[0]; })
				.attr('cy', function (d) { return d[1]; })
				.attr('r', dotscale)
				.attr('class', 'city');
	}
	
	/**
	 * Draw all the path
	 * @param ipath
	 */
	function drawPaths(ipath) {
		var paths = _.map(_.zip(ipath.slice(0,ipath.length-1), ipath.slice(1)), function (pair) {
			return [cities[pair[0]], cities[pair[1]]]
		}).slice();

		svg.selectAll('path.connection').remove();
		svg.selectAll('path.connection').data(paths).enter()
			.append('path')
				.attr('d', function(d) {
			    var dx = d[1][0] - d[0][0],
			        dy = d[1][1] - d[0][1],
			        dr = Math.sqrt(dx * dx + dy * dy);
			    return "M" + d[0][0] + "," + d[0][1] + "A" + dr + "," + dr + " 0 0,1 " + d[1][0] + "," + d[1][1];
			  })
				.attr('class', 'connection')
    		.attr("marker-end", "url(#directed-line)");
	}

	function run() {
		var answer = sanTsp(cities, {});
		drawPaths(answer.initial.path);
		setTimeout(function () { drawPaths(answer.final.path); }, 1000);
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
		//TODO:: draw all the cities and path from state sent
	}
	
	return {
		drawState: drawState,
		onModification: onModification
	}
})(d3, _);