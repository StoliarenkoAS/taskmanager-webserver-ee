<jsp:include page="template/header.jsp"/>

<%@ page import="org.springframework.data.domain.Page" %>
<%@ page import="ru.stoliarenkoas.tm.webserver.Attributes" %>
<%@ page import="ru.stoliarenkoas.tm.webserver.model.dto.UserDTO" %>

<div class="container-fluid" style="padding-bottom: 10%">
    <div class="container w-100">
        <%--  TABLE  --%>
        <h2 class="mt-3">User list</h2>
        <table class="table table-bordered table-sm table-striped table-hover">
            <thead class="thead-dark">
            <tr>
                <th scope="col">#</th>
                <th scope="col">Username</th>
                <th scope="col" style="width: 100%">Id</th>
                <th scope="col">Edit</th>
                <th scope="col">Delete</th>
            </tr>
            </thead>
            <tbody>
            <% int i = 1;
                Page<UserDTO> userPages = (Page<UserDTO>) request.getAttribute(Attributes.USER_LIST);
                for (UserDTO user : userPages) {%>
            <tr>
                <th scope="row" style="vertical-align: middle"><%=i++%></th>
                <td style="vertical-align: middle"><%=user.getLogin()%></td>
                <td style="vertical-align: middle"><%=user.getId()%></td>
                <td class="text-center" style="vertical-align: middle">
                    <form action="${pageContext.request.contextPath}/user/edit" method="get">
                        <input type="hidden" name="<%=Attributes.USER_ID%>" value="<%=user.getId()%>">
                        <button type="submit" style="border-color: transparent; background-color: transparent"><i class="fas fa-pencil-alt align-self-center"></i></button>
                    </form>
                </td>
                <td class="text-center" style="vertical-align: middle">
                    <form action="${pageContext.request.contextPath}/user/remove" method="post">
                        <input type="hidden" name="<%=Attributes.USER_ID%>" value="<%=user.getId()%>">
                        <button type="submit" style="border-color: transparent; background-color: transparent"><i class="far fa-trash-alt"></i></button>
                    </form>
                </td>
            </tr>
            <% } %>
            </tbody>
        </table>
        <%--    PAGINATION    --%>
        <nav aria-label="Page navigation">
            <div class="row">
            <div class="col">
            <ul class="pagination">
                <li class="page-item <%=(userPages.getNumber() > 0 ? "" : "disabled")%>">
                    <a class="page-link" href="${pageContext.request.contextPath}/user/list?<%=Attributes.PAGE + "=" + (userPages.getNumber()-1)%>" aria-label="Previous" style="color: #262627">
                        <span aria-hidden="true">&laquo;</span>
                        <span class="sr-only">Previous</span>
                    </a>
                </li>
                <li class="page-item disabled <%=(userPages.getNumber() > 2 ? "" : "d-none")%>"><a class="page-link" href="#" style="color: #4f4f51">...</a></li>
                <li class="page-item <%=(userPages.getNumber() > 1 ? "" : "d-none")%>"><a class="page-link" href="${pageContext.request.contextPath}/user/list?<%=Attributes.PAGE + "=" + (userPages.getNumber()-2)%>" style="color: #262627"><%=(Integer)request.getAttribute(Attributes.PAGE)-1%></a></li>
                <li class="page-item <%=(userPages.getNumber() > 0 ? "" : "d-none")%>"><a class="page-link" href="${pageContext.request.contextPath}/user/list?<%=Attributes.PAGE + "=" + (userPages.getNumber()-1)%>" style="color: #262627"><%=(Integer)request.getAttribute(Attributes.PAGE)%></a></li>
                <li class="page-item disabled"><a class="page-link" href="#" style="color: #262627; background-color: lightgray"><%=(Integer)request.getAttribute(Attributes.PAGE)+1%></a></li>
                <li class="page-item <%=userPages.getTotalPages()-userPages.getNumber() > 1 ? "" : "d-none"%>"><a class="page-link" href="${pageContext.request.contextPath}/user/list?<%=Attributes.PAGE + "=" + (userPages.getNumber()+1)%>" style="color: #262627"><%=(Integer)request.getAttribute(Attributes.PAGE)+2%></a></li>
                <li class="page-item <%=userPages.getTotalPages()-userPages.getNumber() > 2 ? "" : "d-none"%>"><a class="page-link" href="${pageContext.request.contextPath}/user/list?<%=Attributes.PAGE + "=" + (userPages.getNumber()+2)%>" style="color: #262627"><%=(Integer)request.getAttribute(Attributes.PAGE)+3%></a></li>
                <li class="page-item disabled <%=userPages.getTotalPages()-userPages.getNumber() > 3 ? "" : "d-none"%>"><a class="page-link" href="#" style="color: #4f4f51">...</a></li>
                <li class="page-item <%=userPages.getTotalPages()-userPages.getNumber() > 1 ? "" : "disabled"%>">
                    <a class="page-link" href="${pageContext.request.contextPath}/user/list?<%=Attributes.PAGE + "=" + (userPages.getNumber()+1)%>" aria-label="Next" style="color: #262627">
                        <span aria-hidden="true">&raquo;</span>
                        <span class="sr-only">Next</span>
                    </a>
                </li>
            </ul>
            </div>
                <button class="btn btn-outline-dark mb-3 mr-3" data-toggle="modal" data-target="#createModal">Create</button>
            </div>
        </nav>
    </div>
</div>

<div class="modal fade" id="createModal" tabindex="-1" role="dialog" aria-labelledby="createUserModal" aria-hidden="true">
    <div class="modal-dialog" role="document">
        <div class="modal-content">
            <div class="modal-header">
                <h5 class="modal-title" id="createModalTitle">Create user</h5>
                <button class="close" type="button" data-dismiss="modal" aria-label="Close">
                    <span aria-hidden="true">&times;</span>
                </button>
            </div>
            <div class="modal-body">
                <form action="${pageContext.request.contextPath}/user/create" method="post">
                    <div class="form-group">
                        <label for="loginInputCreateModal">Login</label>
                        <input name="<%=Attributes.LOGIN%>" class="form-control form-control-sm" type="text" required id="loginInputCreateModal" aria-describedby="loginHelp" placeholder="login"/>
                        <small id="loginHelpRegisterModal" class="form-text text-muted">Enter your login</small>
                    </div>
                    <div class="form-group">
                        <label for="passwordInputCreateModal">Password</label>
                        <input name="<%=Attributes.PASSWORD%>" class="form-control form-control-sm" type="password" required id="passwordInputCreateModal" placeholder="password"/>
                        <small id="passwordHelpRegisterModal" class="form-text text-muted">Enter your password</small>
                    </div>
                    <div class="form-group">
                        <input name="passwordConfirmation" class="form-control form-control-sm" type="password" required id="passwordConfirmCreateModal" placeholder="password"/>
                        <small id="passwordConfirmHelpRegisterModal" class="form-text text-muted">Confirm password</small>
                    </div>
                    <div class="form-group">
                        <label for="roleCreateModal">Role</label>
                        <select name="<%=Attributes.ROLE%>" class="form-control form-control-sm" id="roleCreateModal">
                            <option value="ADMIN">Administrator</option>
                            <option value="USER">User</option>
                        </select>
                    </div>
                    <hr class="separator">
                    <div class="container-fluid">
                        <div class="row justify-content-end">
                            <button class="btn btn-success mr-1" type="submit">Create</button>
                            <button class="btn btn-secondary" data-dismiss="modal">Close</button>
                        </div>
                    </div>
                </form>
            </div>
        </div>
    </div>
</div>

<jsp:include page="template/footer.jsp"/>