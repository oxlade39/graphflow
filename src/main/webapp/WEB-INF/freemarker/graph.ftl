<#import "spring.ftl" as spring />
<html>
<#include "/common/head.ftl" >
<body>

    <h1>Hello World ${id}</h1>

    <canvas id="viewport" width="800" height="600"></canvas>

    <div id="addStep" style="display:none">
        <form action="<@spring.url "/flow/${id}/transition"/>">
            <span>Enter transition</span>
            <div>
                <label for="from">From:</label>
                <input type="text" name="from"/>
            </div>
            <div>
                <label for="to">To:</label>
                <input type="text" name="to"/>
            </div>
            <div>
                <input type="submit" value="Create"/>
            </div>
        </form>
    </div>

    <script src="<@spring.url "/resources/js/jquery-1.4.4.min.js"/>"></script>
    <script src="<@spring.url "/resources/js/jquery-ui-1.8.10.custom.min.js"/>"></script>
    <script src="<@spring.url "/resources/js/arbor.js"/>"></script>
    <script src="<@spring.url "/resources/js/arbor-tween.js"/>"></script>
    <script src="<@spring.url "/resources/js/main.js"/>"></script>
</body>

</html>