<%@ page import="ru.stoliarenkoas.tm.webserver.entity.Task" %>
<%@ page import="ru.stoliarenkoas.tm.webserver.Attributes" %>
<%@ page import="java.text.SimpleDateFormat" %>
<!doctype html>
<html lang="en">
<head>
    <!-- Required meta tags -->
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
    <!-- Bootstrap CSS -->
    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/css/bootstrap.min.css">
    <link rel="stylesheet" href="https://use.fontawesome.com/releases/v5.7.0/css/all.css">
    <%--  Datepicker  --%>
    <script src="https://code.jquery.com/jquery-3.3.1.min.js"></script>
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0/css/bootstrap.min.css" integrity="sha384-Gn5384xqQ1aoWXA+058RXPxPg6fy4IWvTNh0E263XmFcJlSAwiGgFAW/dAiS6JXm" crossorigin="anonymous">
    <script src="https://unpkg.com/gijgo@1.9.13/js/gijgo.min.js" type="text/javascript"></script>
    <link href="https://unpkg.com/gijgo@1.9.13/css/gijgo.min.css" rel="stylesheet" type="text/css" />

    <title>Taskmanager EE</title>
</head>
<body>

<nav class="navbar navbar-dark navbar-expand-lg bg-dark sticky-top">
    <a href="/" class="navbar-brand"><img src="https://i.imgur.com/cVPgHhm.png" alt="logo" width="30"></a>
    <button class="navbar-toggler" type="button" data-toggle="collapse"
            data-target="#navbarSupportedContent" aria-controls="navbarSupportedContent"
            aria-expanded="false" aria-label="Toggle navigation">
        <span class="navbar-toggle-icon"></span>
    </button>
    <div class="collapse navbar-collapse" id="navbarSupportedContent">
        <ul class="navbar-nav mr-auto">
            <li class="nav-item active">
                <a href="/users" class="nav-link">Users</a>
            </li>
            <li class="nav-item">
                <a href="/projects" class="nav-link">Projects</a>
            </li>
            <li class="nav-item">
                <a href="/tasks" class="nav-link">Tasks</a>
            </li>
        </ul>
    </div>

    <button class="btn btn-outline-light">Logout</button>
</nav>
<nav class="navbar navbar-dark navbar-expand-lg bg-dark fixed-bottom">
    <a href="#" class="navbar-brand"><img src="https://i.imgur.com/cVPgHhm.png" alt="logo" width="30"></a>
</nav>

<%
    final Task task = (Task) request.getAttribute(Attributes.TASK);
    final SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy");
%>

<div class="container mt-5" style="padding-bottom: 10%">
    <h3>Edit TASK:</h3>
    <form action="task-edit" method="post">
        <div class="form-row">
            <div class="form-group col">
                <label for="idInputEditModal">Id</label>
                <input name="<%=Attributes.TASK_ID%>" readonly="readonly" class="form-control form-control-sm" type="text" id="idInputEditModal" aria-describedby="idHelp" value="<%=task.getId()%>"/>
            </div>
            <div class="form-group col">
                <label for="projectIdInputEditModal">Project id</label>
                <input name="<%=Attributes.PROJECT_ID%>" readonly="readonly" class="form-control form-control-sm" type="text" id="projectIdInputEditModal" aria-describedby="projectIdHelp" value="<%=task.getProjectId()%>"/>
            </div>
        </div>
        <div class="form-row">
            <div class="form-group col">
                <label for="nameInputCreateModal">Name</label>
                <input name="<%=Attributes.NAME%>" class="form-control form-control-sm" type="text" required id="nameInputCreateModal" aria-describedby="nameHelp" value="<%=task.getName()%>"/>
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
            <input name="<%=Attributes.DESCRIPTION%>" class="form-control" rows="3" id="comment" value="<%=task.getDescription()%>"/>
        </div>
        <div class="form-row">
            <div class="form-group col">
                <label for="startDatepicker">Start date</label>
                <input name="<%=Attributes.START_DATE%>" id="startDatepicker" value="<%=format.format(task.getStartDate())%>"/>
                <script>
                    $('#startDatepicker').datepicker({
                        uiLibrary: 'bootstrap4'
                    });
                </script>
            </div>
            <div class="form-group col">
                <label for="endDatepicker">End date</label>
                <input name="<%=Attributes.END_DATE%>" id="endDatepicker" value="<%=format.format(task.getEndDate())%>"/>
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


<script src="https://code.jquery.com/jquery-3.3.1.slim.min.js" integrity="sha384-q8i/X+965DzO0rT7abK41JStQIAqVgRVzpbzo5smXKp4YfRvH+8abtTE1Pi6jizo" crossorigin="anonymous"></script>
<script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.14.7/umd/popper.min.js" integrity="sha384-UO2eT0CpHqdSJQ6hJty5KVphtPhzWj9WO1clHTMGa3JDZwrnQq4sF86dIHNDz0W1" crossorigin="anonymous"></script>
<script src="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/js/bootstrap.min.js" integrity="sha384-JjSmVgyd0p3pXB1rRibZUAYoIIy6OrQ6VrjIEaFf/nJGzIxFDsf4x0xIM+B07jRM" crossorigin="anonymous"></script>
</body>
</html>