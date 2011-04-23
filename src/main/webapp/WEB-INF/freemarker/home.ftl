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
            <li>
                <form action="<@spring.url "/flow"/>" method="POST">
                    <input type="submit" value="Create New Flow"></input>
                </form>
            </li>
        <ul>
    </div>
    <#include "/common/githubRibbon.ftl" >
</body>
</html>