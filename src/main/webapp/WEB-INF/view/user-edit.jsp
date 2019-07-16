<jsp:include page="template/header.jsp"/>

<%@ page import="ru.stoliarenkoas.tm.webserver.entity.User" %>
<%@ page import="ru.stoliarenkoas.tm.webserver.Attributes" %>

<%
    final User user = (User) request.getAttribute(Attributes.USER);
%>

<div class="container mt-5" style="padding-bottom: 10%">
    <h3>Edit USER:</h3>
    <form action="${pageContext.request.contextPath}/user/edit" method="post">
        <div class="form-group">
            <label for="idInputEditModal">Id</label>
            <input name="<%=Attributes.USER_ID%>" readonly="readonly" class="form-control form-control-sm" type="text" id="idInputEditModal" aria-describedby="idHelp" value="<%=user.getId()%>"/>
        </div>
        <div class="form-group">
            <label for="loginInputEditModal">Login</label>
            <input name="<%=Attributes.LOGIN%>" class="form-control form-control-sm" type="text" required id="loginInputEditModal" aria-describedby="loginHelp" value="<%=user.getLogin()%>"/>
        </div>
        <div class="form-group">
            <label for="passwordInputEditModal">Password</label>
            <input name="<%=Attributes.PASSWORD%>" class="form-control form-control-sm" type="text" required id="passwordInputEditModal" placeholder="password"/>
        </div>
        <div class="form-group">
            <label for="roleEditModal">Role</label>
            <select name="<%=Attributes.ROLE%>" class="form-control form-control-sm" id="roleEditModal">
                <option value="ADMIN">Administrator</option>
                <option value="USER">User</option>
            </select>
        </div>
        <hr class="separator">
        <div class="container-fluid">
            <div class="row justify-content-end">
                <button class="btn btn-success" type="submit">Save</button>
            </div>
        </div>
    </form>
</div>

<jsp:include page="template/footer.jsp"/>