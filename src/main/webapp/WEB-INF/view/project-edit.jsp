<jsp:include page="template/header.jsp"/>

<%@ page import="ru.stoliarenkoas.tm.webserver.Attributes" %>
<%@ page import="ru.stoliarenkoas.tm.webserver.model.dto.ProjectDTO" %>
<%@ page import="java.text.SimpleDateFormat" %>

<%
    final ProjectDTO project = (ProjectDTO) request.getAttribute(Attributes.PROJECT);
    final SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy");
%>

<div class="container mt-5" style="padding-bottom: 10%">
    <h3>Edit PROJECT:</h3>
    <form action="${pageContext.request.contextPath}/project/edit" method="post">
        <div class="form-group">
            <label for="idInputEditModal">Id</label>
            <input name="<%=Attributes.PROJECT_ID%>" readonly="readonly" class="form-control form-control-sm" type="text" id="idInputEditModal" aria-describedby="idHelp" value="<%=project.getId()%>"/>
        </div>
        <div class="form-row">
            <div class="form-group col">
                <label for="nameInputEditModal">Name</label>
                <input name="<%=Attributes.NAME%>" class="form-control form-control-sm" type="text" required id="nameInputEditModal" aria-describedby="nameHelp" value="<%=project.getName()%>"/>
            </div>
            <div class="form-group col">
                <label for="statusEditModal">Status</label>
                <select name="<%=Attributes.STATUS%>" class="form-control form-control-sm" id="statusEditModal">
                    <option value="PLANNED">Planned</option>
                    <option value="IN_PROGRESS">In progress</option>
                    <option value="COMPLETE">Complete</option>
                </select>
            </div>
        </div>
        <div class="form-group">
            <label for="comment">Description:</label>
            <input name="<%=Attributes.DESCRIPTION%>" class="form-control" rows="3" id="comment" value="<%=project.getDescription()%>"/>
        </div>
        <div class="form-row">
            <div class="form-group col">
                <label for="startDatepicker">Start date</label>
                <input name="<%=Attributes.START_DATE%>" id="startDatepicker" value="<%=format.format(project.getStartDate())%>"/>
                <script>
                    $('#startDatepicker').datepicker({
                        uiLibrary: 'bootstrap4'
                    });
                </script>
            </div>
            <div class="form-group col">
                <label for="endDatepicker">End date</label>
                <input name="<%=Attributes.END_DATE%>" id="endDatepicker" value="<%=format.format(project.getEndDate())%>"/>
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
                <button class="btn btn-success" type="submit">Save</button>
            </div>
        </div>
    </form>
</div>

<jsp:include page="template/footer.jsp"/>