/**
 * contain the draw API of the project
 */
;var initTspDrawerLibrary = (function (d3, _, logger) {
	'use strict';

	var width = 600,
	    height = 600,
		optimalCitySize = 5,
	    cities = [],
		edges = [],
		pathDraw = [];
	
	var log = logger.log;
	
	var openRightClicEvent = null;
	
	var selectedCity = null,
		selectedPath = null;
	
	var isLinkingCities = false;

	var svg = d3.select("#tsp")
			.append("div")
			.classed("svg-container", true)
			.append("svg")
			.attr("preserveAspectRatio", "xMinYMin meet")
			.classed("svg-content-responsive", true)
			.attr("viewBox", "0 0 " + width + " " + height)
		;
	
	var view = svg.append("g")
		.attr("class", "view");
	
	var elementsContainerG = view.append("g")
		.attr("width", "100%")
		.attr("height", "100%");
	
	var rect = elementsContainerG.append("rect");
	
	rect.on('click', clickMap)
		.on('contextmenu', clickMap)
		.attr("class", "background");
	
	rect.attr("width", "1000px")
		.attr("height","1000px");
	
	var createCity = function(v){
		return _.extend({
			id: 0,
			x: 0.0,
			y: 0.0,
			name: "Temporary"
		}, v);
	};
	
	var createPath = function(v){
		return _.extend({
			cost: 0,
			rgbaColor: "blue",
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
	
	var lastClic = null;
	
	function clickMap () {
		lastClic = {
			evt: this,
			d3Evt: d3.mouse(this),
			x: d3.mouse(this)[0],
			y: d3.mouse(this)[1]
		};
		$(cmLinkCity).hide();
		$(cmRemoveLink).hide();
	}
	
	/**
	 * Draw all the cities from the cities[] var
	 */
	function drawCities() {
		elementsContainerG.selectAll('circle').remove();
		_.each(cities, function(city){
			var circle = elementsContainerG.append('circle')
				.attr('cx', function () { return city.x;})
				.attr('cy', function () { return city.y; })
				.attr('r', optimalCitySize)
				.attr('class', 'city')
				.attr('data-id', city.id)
				.on('contextmenu', function(){
					selectedCity = {
						city: city,
						circle: city
					};
					$(cmLinkCity).show();
				})
				.call(d3.drag()
					.on('start', function() {
						if(!isLinkingCities){
							circle.style('fill', 'red');
						}
					})
					.on('drag', function() {
						if(!isLinkingCities) {
							city.x = d3.event.x;
							city.y = d3.event.y;
							circle.attr('cx', city.x).attr('cy', city.y);
						}
					})
					.on('end', function() {
						if(!isLinkingCities) {
							circle.style('fill', 'black');
							_.each(onModificationObservers, function (e) {
								e("city updated", city);
							});
						}
					})
				)
		});
	}
	
	var makePoint = function(x, y){
		return ({
			x: x,
			y: y
		});
	};
	
	/**
	 * Draw all the path
	 */
	function drawPaths() {
		var thickness = optimalCitySize * .4; //Some number
		elementsContainerG.selectAll('polygon.connection').remove();
		_.each(edges, function(edge){
			var from = edge['from'];
			var to = edge['to'];
			
			var points = generateRectangleFromTwoPoints(to, from, thickness);
			
			var strPoints = "";
			_.each(points, function(p){
				strPoints += p.x+","+p.y+' ';
			});
			
			pathDraw.push(elementsContainerG.append("polygon")
				.attr("fill", edge["rgbaColor"])
				.attr("points", strPoints)
				.attr("class", "connection")
				.on('contextmenu', function(){
					selectedPath = edge;
					$(cmRemoveLink).show();
				}));
		});
	}
	
	/**
	 * Assuming p1 and p2 have at least x and y
	 * @param p1
	 * @param p2
	 * @param width
	 */
	function generateRectangleFromTwoPoints(p1, p2, width){
		width = typeof width === 'undefined' ? 2 : width;
		var vX = p2.x - p1.x,
			vY = p2.y - p1.y;
		if(vX == 0) {
			vX++;
		}
		if(vY == 0){
			vY++;
		}
		var pX = vY,
			pY = -vX,
			length = Math.sqrt(pX * pX + pY * pY),
			nX = pX / length,
			nY = pY / length;
		return [
			makePoint(p2.x + nX * width / 2, p2.y + nY * width / 2),
			makePoint(p1.x + nX * width / 2, p1.y + nY * width / 2),
			makePoint(p1.x - nX * width / 2, p1.y - nY * width / 2),
			makePoint(p2.x - nX * width / 2, p2.y - nY * width / 2)
		]
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
	
	var createCityFromJava = function(rawData, i){
		return createCity({
			id: i,
			name: rawData['name'],
			x: rawData['vertexInfo']['x'],
			y: rawData['vertexInfo']['y']
		})
	};
	
	// context menu
	
	var cmLinkCity = document.getElementById("cm-linkCity");
	var cmRemoveLink = document.getElementById("cm-removeLink");
	
	$(cmLinkCity).hide();
	$(cmRemoveLink).hide();
	
	$(document).bind("contextmenu", function (event) {
		openRightClicEvent = event;
		event.preventDefault();
		$(".custom-menu").finish().toggle(100).
		css({
			top: event.pageY + "px",
			left: event.pageX + "px"
		});
	});
	
	var bodyListener = function(e){
		if (!$(e.target).parents(".custom-menu").length > 0) {
			$(".custom-menu").hide(100);
		}
	};
	
	$(document).bind("click mousedown", bodyListener);
	
	$(".custom-menu li").click(function(){
		// NOTES: we don't need to update anything here
		// Java will receive the modifications, update on it side
		// And send back a new graph, which will trigger the draw
		switch($(this).attr("data-action")) {
			case "addNewCity":
				var city = createCity({
					x: lastClic.x,
					y: lastClic.y
				});
				_.each(onModificationObservers, function(e){
					e("create city", city);
				});
				break;
			case "linkCityTo":
				isLinkingCities = true;
				var eachC = function(callback){
					_.each($.find("svg circle"), function(c){
						var $circle = $(c);
						var circle = d3.select($circle.get(0));
						var listener = function(){
							_.each(onModificationObservers, function(e){
								e("link city", [selectedCity.city, cities[$circle.attr("data-id")]]);
							});
							circle.on("click", null);
						};
						callback(circle, $circle, listener);
					});
				};
				
				var line = elementsContainerG.insert("line", ":first-child")
					.attr("stroke", "red")
					.attr("stroke-width", (optimalCitySize * .1)+"px")
					.attr("x1", selectedCity.city.x)
					.attr("y1", selectedCity.city.y)
					.attr("x2", selectedCity.city.x)
					.attr("y2", selectedCity.city.y)
					.attr("class", "temporary-connection")
				;
				
				elementsContainerG.on("mousemove", function(){
					line.attr("x2", d3.mouse(this)[0])
						.attr("y2", d3.mouse(this)[1]);
				});
				svg.on("click", function(){
					svg.selectAll('line.temporary-connection').remove();
					eachC(function(dc, $c){
						$c.removeClass("hoverable");
					});
					svg.on("click", null);
					isLinkingCities = false;
				});
				
				eachC(function(dc, $c, listener){
					$c.addClass("hoverable");
					dc.on("click", listener);
				});
				
				break;
			case "removeLink":
				_.each(onModificationObservers, function(e){
					e("link removed", selectedPath);
				});
				break;
		}
		
		$(".custom-menu").hide(100);
	});
	
	// Zoom and pan
	
	var zoom = d3.zoom()
		.scaleExtent([.6, 4])
		.on("zoom", zoomed);
	
	svg.call(zoom);
	
	function zoomed() {
		view.attr("transform", d3.event.transform);
	}
	
	
	function init(newState){
		var json = JSON.parse(newState);
		var rawCities = json['cities'];
		var rawEdges = json['edges'];
		cities = [];
		var maxX = -100000000;
		var maxY = -100000000;
		var minX =  100000000;
		var minY =  100000000;
		_.each(rawCities, function(rawCity, i){
			var nCity = createCityFromJava(rawCity, i);
			cities.push(nCity);
			if(nCity.x > maxX){
				maxX = nCity.x;
			}
			if(nCity.y > maxY){
				maxY = nCity.y;
			}
			if(nCity.x < minX){
				minX = nCity.x;
			}
			if(nCity.y < minY){
				minY = nCity.y;
			}
		});
		edges = [];
		_.each(rawEdges, function(rawEdge){
			edges.push(createPath({
				from: createCityFromJava(rawEdge['from']),
				to: createCityFromJava(rawEdge['to']),
				rgbaColor: rawEdge["rgbaColor"]
			}));
		});
		width = (maxX-minX);
		height = (maxY-minY);
		optimalCitySize = (width+height)/2*.01;
		svg.attr("viewBox", 0 + " " + 0 + " " + width + " " + height);
		elementsContainerG.attr("transform", "translate(" + -minX + "," + -minY + ")");
		//TODO find a solution to prevent pan out : zoom.translateExtent([[0, 0], [0, 0]]);
		drawPaths();
		drawCities();
	}
	
	var newStateReceived = function(newState){
		log("redrawing state");
		var json = JSON.parse(newState);
		_.each(json['edges'], function(path, i){
			var drawEdge = edges[i];
			if(drawEdge['from']['name'] == path['from']['name'] && drawEdge['to']['name'] == path['to']['name']){
				pathDraw[i].attr("fill", path["rgbaColor"])
			}
		});
	};
	
	return {
		init: init,
		newState: newStateReceived,
		onModification: onModification
	}
});