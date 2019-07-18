<jsp:include page="template/header.jsp"/>

<%@ page import="org.springframework.data.domain.Page" %>
<%@ page import="ru.stoliarenkoas.tm.webserver.Attributes" %>
<%@ page import="ru.stoliarenkoas.tm.webserver.model.dto.ProjectDTO" %>
<%@ page import="ru.stoliarenkoas.tm.webserver.model.dto.TaskDTO" %>
<%@ page import="java.util.Collection" %>

<div class="container-fluid" style="padding-bottom: 10%">
    <div class="container w-100">
        <%--  TABLE  --%>
        <h2 class="mt-3">Task list</h2>
        <table class="table table-bordered table-sm table-striped table-hover">
            <thead class="thead-dark">
            <tr>
                <th scope="col">#</th>
                <th scope="col">Project</th>
                <th scope="col">Name</th>
                <th scope="col" style="width: 66%">Description</th>
                <th scope="col" style="width: 34%">Id</th>
                <th scope="col" style="width: 60px">Start</th>
                <th scope="col" style="width: 60px">End</th>
                <th scope="col" style="width: 40px">Edit</th>
                <th scope="col" style="width: 40px">Delete</th>
            </tr>
            </thead>

            <tbody>
            <% int i = 1;
                Page<TaskDTO> taskPages = (Page<TaskDTO>)request.getAttribute(Attributes.TASK_LIST);
                for (TaskDTO task : taskPages) {%>
            <tr>
                <th scope="row" style="vertical-align: middle"><%=i++%></th>
                <td style="vertical-align: middle"><%=task.getProjectId()%></td>
                <td style="vertical-align: middle"><%=task.getName()%></td>
                <td style="vertical-align: middle"><%=task.getDescription()%></td>
                <td style="vertical-align: middle"><%=task.getId()%></td>
                <td style="vertical-align: middle"><%=task.getStartDate()%></td>
                <td style="vertical-align: middle"><%=task.getEndDate()%></td>
                <td class="text-center" style="vertical-align: middle">
                    <form action="${pageContext.request.contextPath}/task/edit" method="get">
                        <input type="hidden" name="<%=Attributes.TASK_ID%>" value="<%=task.getId()%>">
                        <button type="submit" style="border-color: transparent; background-color: transparent"><i class="fas fa-pencil-alt align-self-center"></i></button>
                    </form>
                </td>
                <td class="text-center" style="vertical-align: middle">
                    <form action="${pageContext.request.contextPath}/task/remove" method="post">
                        <input type="hidden" name="<%=Attributes.TASK_ID%>" value="<%=task.getId()%>">
                        <button type="submit" style="border-color: transparent; background-color: transparent"><i class="far fa-trash-alt  align-self-center"></i></button>
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
                        <li class="page-item <%=(taskPages.getNumber() > 0 ? "" : "disabled")%>">
                            <a class="page-link" href="${pageContext.request.contextPath}/task/list?<%=Attributes.PAGE + "=" + (taskPages.getNumber()-1)%>" aria-label="Previous" style="color: #262627">
                                <span aria-hidden="true">&laquo;</span>
                                <span class="sr-only">Previous</span>
                            </a>
                        </li>
                        <li class="page-item disabled <%=(taskPages.getNumber() > 2 ? "" : "d-none")%>"><a class="page-link" href="#" style="color: #4f4f51">...</a></li>
                        <li class="page-item <%=(taskPages.getNumber() > 1 ? "" : "d-none")%>"><a class="page-link" href="${pageContext.request.contextPath}/task/list?<%=Attributes.PAGE + "=" + (taskPages.getNumber()-2)%>" style="color: #262627"><%=(Integer)request.getAttribute(Attributes.PAGE)-1%></a></li>
                        <li class="page-item <%=(taskPages.getNumber() > 0 ? "" : "d-none")%>"><a class="page-link" href="${pageContext.request.contextPath}/task/list?<%=Attributes.PAGE + "=" + (taskPages.getNumber()-1)%>" style="color: #262627"><%=(Integer)request.getAttribute(Attributes.PAGE)%></a></li>
                        <li class="page-item disabled"><a class="page-link" href="#" style="color: #262627; background-color: lightgray"><%=(Integer)request.getAttribute(Attributes.PAGE)+1%></a></li>
                        <li class="page-item <%=taskPages.getTotalPages()-taskPages.getNumber() > 1 ? "" : "d-none"%>"><a class="page-link" href="${pageContext.request.contextPath}/task/list?<%=Attributes.PAGE + "=" + (taskPages.getNumber()+1)%>" style="color: #262627"><%=(Integer)request.getAttribute(Attributes.PAGE)+2%></a></li>
                        <li class="page-item <%=taskPages.getTotalPages()-taskPages.getNumber() > 2 ? "" : "d-none"%>"><a class="page-link" href="${pageContext.request.contextPath}/task/list?<%=Attributes.PAGE + "=" + (taskPages.getNumber()+2)%>" style="color: #262627"><%=(Integer)request.getAttribute(Attributes.PAGE)+3%></a></li>
                        <li class="page-item disabled <%=taskPages.getTotalPages()-taskPages.getNumber() > 3 ? "" : "d-none"%>"><a class="page-link" href="#" style="color: #4f4f51">...</a></li>
                        <li class="page-item <%=taskPages.getTotalPages()-taskPages.getNumber() > 1 ? "" : "disabled"%>">
                            <a class="page-link" href="${pageContext.request.contextPath}/task/list?<%=Attributes.PAGE + "=" + (taskPages.getNumber()+1)%>" aria-label="Next" style="color: #262627">
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
                <h5 class="modal-title" id="createModalTitle">Create project</h5>
                <button class="close" type="button" data-dismiss="modal" aria-label="Close">
                    <span aria-hidden="true">&times;</span>
                </button>
            </div>
            <div class="modal-body">
                <form action="${pageContext.request.contextPath}/task/create" method="post">
                    <div class="form-group">
                        <label for="projectSelectCreateModal">Project</label>
                        <select name="<%=Attributes.PROJECT_ID%>" onfocus='this.size=3;' onblur='this.size=1;' onchange='this.size=1; this.blur();' class="form-control form-control-sm" id="projectSelectCreateModal">
                            <% for (ProjectDTO project : (Collection<ProjectDTO>)request.getAttribute(Attributes.PROJECT_LIST)) {%>
                            <option value="<%=project.getId()%>"><%=project.getName()%></option>
                            <% } %>
                        </select>
                    </div>
                    <div class="form-group">
                        <label for="nameInputCreateModal">Name</label>
                        <input name="<%=Attributes.NAME%>" class="form-control form-control-sm" type="text" required id="nameInputCreateModal" aria-describedby="nameHelp" placeholder="name"/>
                    </div>
                    <div class="form-group">
                        <label for="comment">Description:</label>
                        <input name="<%=Attributes.DESCRIPTION%>" class="form-control" rows="3" id="comment"/>
                    </div>
                    <div class="form-row">
                        <div class="form-group col">
                            <label for="startDatepicker">Start date</label>
                            <input name="<%=Attributes.START_DATE%>" id="startDatepicker"/>
                            <script>
                                $('#startDatepicker').datepicker({
                                    uiLibrary: 'bootstrap4'
                                });
                            </script>
                        </div>
                        <div class="form-group col">
                            <label for="endDatepicker">End date</label>
                            <input name="<%=Attributes.END_DATE%>" id="endDatepicker"/>
                            <script>
                                $('#endDatepicker').datepicker({
                                    uiLibrary: 'bootstrap4'
                                });
                            </script>
                        </div>
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