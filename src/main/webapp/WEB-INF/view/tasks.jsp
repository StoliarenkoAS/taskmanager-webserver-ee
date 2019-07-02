<%@ page import="ru.stoliarenkoas.tm.webserver.entity.Task" %>
<%@ page import="ru.stoliarenkoas.tm.webserver.Attributes" %>
<%@ page import="java.util.Collection" %>
<%@ page import="ru.stoliarenkoas.tm.webserver.entity.Project" %>
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
                for (Task task : (Collection<Task>)request.getAttribute(Attributes.TASK_LIST)) {%>
            <tr>
                <th scope="row" style="vertical-align: middle"><%=i++%></th>
                <td style="vertical-align: center"><%=task.getProjectId()%></td>
                <td style="vertical-align: middle"><%=task.getName()%></td>
                <td style="vertical-align: middle"><%=task.getDescription()%></td>
                <td style="vertical-align: middle"><%=task.getId()%></td>
                <td style="vertical-align: middle"><%=task.getStartDate()%></td>
                <td style="vertical-align: middle"><%=task.getEndDate()%></td>
                <td class="text-center" style="vertical-align: middle"><a href="task-edit?<%=Attributes.TASK_ID%>=<%=task.getId()%>"><i class="fas fa-pencil-alt align-self-center"></i></a></td>
                <td class="text-center" style="vertical-align: middle"><a href="task-remove?<%=Attributes.TASK_ID%>=<%=task.getId()%>"><i class="far fa-trash-alt"></i></a></td>
            </tr>
            <% } %>
            </tbody>

        </table>
        <%--    PAGINATION    --%>
        <nav aria-label="Page navigation">
            <div class="row">
                <div class="col">
                    <ul class="pagination">
                        <li class="page-item">
                            <a class="page-link" href="#" aria-label="Previous" style="color: #262627">
                                <span aria-hidden="true">&laquo;</span>
                                <span class="sr-only">Previous</span>
                            </a>
                        </li>
                        <li class="page-item"><a class="page-link" href="#" style="color: #262627">1</a></li>
                        <li class="page-item"><a class="page-link" href="#" style="color: #262627">2</a></li>
                        <li class="page-item"><a class="page-link" href="#" style="color: #262627">3</a></li>
                        <li class="page-item">
                            <a class="page-link" href="#" aria-label="Next" style="color: #262627">
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
                <form action="task-create" method="post">
                    <div class="form-group">
                        <label for="projectSelectCreateModal">Project</label>
                        <select name="<%=Attributes.PROJECT_ID%>" onfocus='this.size=3;' onblur='this.size=1;' onchange='this.size=1; this.blur();' class="form-control form-control-sm" id="projectSelectCreateModal">
                            <% for (Project project : (Collection<Project>)request.getAttribute(Attributes.PROJECT_LIST)) {%>
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
                        <input name="<%=Attributes.DESCRIPTION%>" class="form-control" rows="3" id="comment"></input>
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

<script src="https://code.jquery.com/jquery-3.3.1.slim.min.js" integrity="sha384-q8i/X+965DzO0rT7abK41JStQIAqVgRVzpbzo5smXKp4YfRvH+8abtTE1Pi6jizo" crossorigin="anonymous"></script>
<script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.14.7/umd/popper.min.js" integrity="sha384-UO2eT0CpHqdSJQ6hJty5KVphtPhzWj9WO1clHTMGa3JDZwrnQq4sF86dIHNDz0W1" crossorigin="anonymous"></script>
<script src="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/js/bootstrap.min.js" integrity="sha384-JjSmVgyd0p3pXB1rRibZUAYoIIy6OrQ6VrjIEaFf/nJGzIxFDsf4x0xIM+B07jRM" crossorigin="anonymous"></script>
</body>
</html>