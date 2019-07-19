<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="ru.stoliarenkoas.tm.webserver.Attributes" %>
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

    <title>Taskmanager SPRING</title>
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
                <a href="${pageContext.request.contextPath}/user/list" class="nav-link">Users</a>
            </li>
            <li class="nav-item">
                <a href="${pageContext.request.contextPath}/project/list" class="nav-link">Projects</a>
            </li>
            <li class="nav-item">
                <a href="${pageContext.request.contextPath}/task/list" class="nav-link">Tasks</a>
            </li>
        </ul>
    </div>
    <% boolean loggedIn = request.getSession().getAttribute(Attributes.USER_ID) != null; %>
    <div class="btn-group <%=!loggedIn?"":"d-none"%>" role="group">
        <button class="btn btn-outline-light mx-1" data-toggle="modal" data-target="#loginModal">Login</button>
        <button class="btn btn-outline-light" data-toggle="modal" data-target="#registerModal">Register</button>
    </div>
    <form class="m-0 p-0 <%=loggedIn?"":"d-none"%>" action="${pageContext.request.contextPath}/authorization/logout" method="post">
        <button class="btn btn-outline-light" type="submit">Logout</button>
    </form>
</nav>
<nav class="navbar navbar-dark navbar-expand-lg bg-dark fixed-bottom">
    <a href="#" class="navbar-brand"><img src="https://i.imgur.com/cVPgHhm.png" alt="logo" width="30"></a>
</nav>
