# What is this? #
Me playing around with [Spring 3](http://static.springsource.org/spring/docs/3.1.0.M1/spring-framework-reference/html/), [Neo4j's](http://neo4j.org/) graph database and HTML 5 canvas.

# Why? #
I wanted to try out graph databases and Spring 3's REST support to provide a nice web interface to editing a graph structure.

# Disclaimer #
Unlike most of my work there isn't a great deal of tests. The tests included are for experimental discovery purposes. I used maven's [jetty plugin](http://docs.codehaus.org/display/JETTY/Maven+Jetty+Plugin) to drive most of the development.

# Cool stuff #
Spring 3's content negotiation was really simple and means the html for the page is very lightweight. The javascript in the page just requests the same page URL but as 'text/json' and Spring's content negotiation automatically serialises the view as JSON.

HTML5 Canvas. It's suprising simple to build a nice looking dynamic page using the canvas API.

[arbor.js](http://arborjs.org/) "is a graph visualization library", you can even enable "gravity" and allow nodes to fall into place. Try dragging nodes around and let them settle.

Neo4j is a super fast, transactional graph database. The searching facilities are built on top of [lucene](http://lucene.apache.org/java/docs/index.html) which allows you to easily and quickly search and navigate the graph structure.

# Technologies #
*  [Spring 3](http://static.springsource.org/spring/docs/3.1.0.M1/spring-framework-reference/html/)
*  [Neo4j](http://neo4j.org/)
*  [HTML5 Canvas](http://www.whatwg.org/specs/web-apps/current-work/multipage/the-canvas-element.html)
*  [JQuery](http://jquery.com/)
*  [JQuery.ui](http://jqueryui.com/)
*  [arbor.js](http://arborjs.org/)
*  [Freemarker](http://freemarker.sourceforge.net/)

# Some examples #
1. Index, list of graphs ![index list of graphs](https://github.com/oxlade39/graphflow/raw/master/src/main/webapp/META-INF/resources/images/index%20list%20of%20graphs.png)
2. Default generated graph ![default generated graph](https://github.com/oxlade39/graphflow/raw/master/src/main/webapp/META-INF/resources/images/default%20generated%20graph.png)
3. Adding a transition ![adding a transition](https://github.com/oxlade39/graphflow/raw/master/src/main/webapp/META-INF/resources/images/adding%20a%20transition.png)
4. Transition added ![transition added](https://github.com/oxlade39/graphflow/raw/master/src/main/webapp/META-INF/resources/images/transition%20added.png)
5. Nice content negotiation. JSON backing view ![content negotiation](https://github.com/oxlade39/graphflow/raw/master/src/main/webapp/META-INF/resources/images/content%20negotiation.png)