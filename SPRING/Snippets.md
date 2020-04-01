# SPRING Code snippets


### request
```
    <%
        String errorMsg = "";
        if(request.getParameter("error") != null && !request.getParameter("error").equals("null")){
            errorMsg = request.getParameter("error");
        }
    %>
    <p style="color: red"><%=errorMsg%></p>
```

### tag lib
```
    <c:if test="${layoutDataVo.getLAYOUT_DATA() != null}">
        currentLayoutData.LAYOUT_DATA = JSON.parse('${layoutDataVo.getLAYOUT_DATA()}');
        currentLayoutData.INX = <c:out value="${layoutDataVo.getINX()}"/>;
    </c:if>
```