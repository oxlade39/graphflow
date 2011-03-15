<#import "spring.ftl" as spring />
<html>
<#include "/common/head.ftl" >
<body>
    <h1>Welcome to Graphflow</h1>
    <div id="graphList">
        <span>Graphs:</span>
        <ul>
        <#list graphs as graph>
            <li><a href="<@spring.url "/flow/${graph}"/>">${graph}</a></li>
        </#list>
        <ul>
    </div>
</body>
</html>