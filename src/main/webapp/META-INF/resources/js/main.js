//
//  main.js
//
//  A project template for using arbor.js
//

(function($){

  var Renderer = function(canvas){
    var canvas = $(canvas).get(0)
    var ctx = canvas.getContext("2d");
    var particleSystem

    var that = {
      init:function(system){
        //
        // the particle system will call the init function once, right before the
        // first frame is to be drawn. it's a good place to set up the canvas and
        // to pass the canvas size to the particle system
        //
        // save a reference to the particle system for use in the .redraw() loop
        particleSystem = system

        // inform the system of the screen dimensions so it can map coords for us.
        // if the canvas is ever resized, screenSize should be called again with
        // the new dimensions
        particleSystem.screenSize(canvas.width, canvas.height)
        particleSystem.screenPadding(80) // leave an extra 80px of whitespace per side

        // set up some event handlers to allow for node-dragging
        that.initMouseHandling()
      },

      redraw:function(){
        //
        // redraw will be called repeatedly during the run whenever the node positions
        // change. the new positions for the nodes can be accessed by looking at the
        // .p attribute of a given node. however the p.x & p.y values are in the coordinates
        // of the particle system rather than the screen. you can either map them to
        // the screen yourself, or use the convenience iterators .eachNode (and .eachEdge)
        // which allow you to step through the actual node objects but also pass an
        // x,y point in the screen's coordinate system
        //
        ctx.fillStyle = "white"
        ctx.clearRect(0,0, canvas.width, canvas.height)

        particleSystem.eachEdge(function(edge, pt1, pt2){
          // edge: {source:Node, target:Node, length:#, data:{}}
          // pt1:  {x:#, y:#}  source position in screen coords
          // pt2:  {x:#, y:#}  target position in screen coords

          edge.source.data = {label: edge.data.to};
          edge.target.data = {label: edge.data.from};

          // draw a line from pt1 to pt2
          ctx.strokeStyle = "rgba(0,0,0, .333)"
          ctx.lineWidth = 1
          ctx.beginPath()
          ctx.moveTo(pt1.x, pt1.y)
          ctx.lineTo(pt2.x, pt2.y)
          ctx.stroke()
        })

        particleSystem.eachNode(function(node, pt){
          // node: {mass:#, p:{x,y}, name:"", data:{}}
          // pt:   {x:#, y:#}  node position in screen coords

          // draw a rectangle centered at pt
          var w = 25;
          ctx.fillStyle = (node.data.label == that.currentPositionName) ? "orange" : "blue"
          ctx.beginPath();
          ctx.arc(pt.x, pt.y, w, 0, Math.PI*2, true);
          ctx.closePath();
          ctx.fill();
          ctx.textAlign = 'center';
          ctx.fillStyle = 'black';
          ctx.fillText(node.data.label, pt.x, pt.y, 2*w);
        })
      },

      initMouseHandling:function(){
        // no-nonsense drag and drop (thanks springy.js)
        var dragged = null;

        // set up a handler object that will initially listen for mousedowns then
        // for moves and mouseups while dragging
        var handler = {
          clicked:function(e){

            var pos = $(canvas).offset();
            _mouseP = arbor.Point(e.pageX-pos.left, e.pageY-pos.top)
            dragged = particleSystem.nearest(_mouseP);

            if(e.shiftKey) {
                var addStep = $('#addStep');
                if (dragged && dragged.node !== null){
                    var fromInput = $('input[name="from"]', addStep);
                    fromInput.val(dragged.node.data.label);
                }
                addStep.dialog({ modal: true });;
                $('input[name="from"]', addStep).attr('readonly','disabled');
                $('input[name="to"]', addStep).focus();
            } else {
                if (dragged && dragged.node !== null){
                  // while we're dragging, don't let physics move the node
                  dragged.node.fixed = true
                }

                $(canvas).bind('mousemove', handler.dragged)
                $(window).bind('mouseup', handler.dropped)
            }
            return false
          },
          dragged:function(e){
            var pos = $(canvas).offset();
            var s = arbor.Point(e.pageX-pos.left, e.pageY-pos.top)

            if (dragged && dragged.node !== null){
              var p = particleSystem.fromScreen(s)
              dragged.node.p = p
            }

            return false
          },

          dropped:function(e){
            if (dragged===null || dragged.node===undefined) return
            if (dragged.node !== null) dragged.node.fixed = false
            dragged.node.tempMass = 1000
            dragged = null
            $(canvas).unbind('mousemove', handler.dragged)
            $(window).unbind('mouseup', handler.dropped)
            _mouseP = null
            return false
          }
        }

        // start listening
        $(canvas).mousedown(handler.clicked);

      },

      currentPositionName: ''

    }
    return that
  }

  $(document).ready(function(){
    var sys = arbor.ParticleSystem(1000, 600, 0.5) // create the system with sensible repulsion/stiffness/friction
    sys.parameters({gravity:true}) // use center-gravity to make the graph settle nicely (ymmv)
    sys.renderer = Renderer("#viewport") // our newly created renderer will have its .init() method called shortly by sys...

    var jsonUrl = window.location + '';

    $.getJSON(jsonUrl, function(data) {
      sys.renderer.currentPositionName = data.graph.currentPosition.name;
      $.each(data.transitions, function(index, transition) {
        sys.addEdge(transition.to, transition.from, {
            to: transition.to,
            from: transition.from
        });
      });

    });

    $('#addStep > form').submit(function(e) {
        e.preventDefault();
        var form = $(this)
        $.post(form.attr("action"), form.serialize(), function(response) {
            var toName = form.find("input[name=to]").val();
            var fromName = form.find("input[name=from]").val();
            sys.addEdge(toName, fromName, {to: toName, from: fromName});
        }, "json");
        $('#addStep').dialog('close');
    });
  })

})(this.jQuery)