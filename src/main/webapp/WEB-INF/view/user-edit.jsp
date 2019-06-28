<!doctype html>
<html lang="en">
<head>
    <!-- Required meta tags -->
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
    <!-- Bootstrap CSS -->
    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/css/bootstrap.min.css">
    <link rel="stylesheet" href="https://use.fontawesome.com/releases/v5.7.0/css/all.css">

    <title>Taskmanager EE</title>
</head>
<body>

<nav class="navbar navbar-dark navbar-expand-lg bg-dark sticky-top">
    <a href="/" class="navbar-brand"><img src="../../image/rock-symbol-img.png" alt="logo" width="30"></a>
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
    <a href="#" class="navbar-brand"><img src="../../image/rock-symbol-img.png" alt="logo" width="30"></a>
</nav>

<div class="container mt-5" style="padding-bottom: 10%">
    <h3>Edit USER:</h3>
    <form>
        <div class="form-group">
            <label for="idInputEditModal">Id</label>
            <input disabled class="form-control form-control-sm" type="text" id="idInputEditModal" aria-describedby="idHelp" placeholder="1111-2222222-33333-4444-555"/>
        </div>
        <div class="form-group">
            <label for="loginInputEditModal">Login</label>
            <input class="form-control form-control-sm" type="text" required id="loginInputEditModal" aria-describedby="loginHelp" placeholder="login"/>
        </div>
        <div class="form-group">
            <label for="passwordInputEditModal">Password</label>
            <input class="form-control form-control-sm" type="text" required id="passwordInputEditModal" placeholder="password"/>
        </div>
        <div class="form-group">
            <label for="roleEditModal">Role</label>
            <select class="form-control form-control-sm" id="roleEditModal">
                <option value="ADMIN">Administrator</option>
                <option value="USER">User</option>
            </select>
        </div>
        <hr class="separator">
        <div class="container-fluid">
            <div class="row justify-content-end">
                <button class="btn btn-success" type="submit" data-dismiss="modal">Save</button>
            </div>
        </div>
    </form>
</div>

<script src="https://code.jquery.com/jquery-3.3.1.slim.min.js" integrity="sha384-q8i/X+965DzO0rT7abK41JStQIAqVgRVzpbzo5smXKp4YfRvH+8abtTE1Pi6jizo" crossorigin="anonymous"></script>
<script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.14.7/umd/popper.min.js" integrity="sha384-UO2eT0CpHqdSJQ6hJty5KVphtPhzWj9WO1clHTMGa3JDZwrnQq4sF86dIHNDz0W1" crossorigin="anonymous"></script>
<script src="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/js/bootstrap.min.js" integrity="sha384-JjSmVgyd0p3pXB1rRibZUAYoIIy6OrQ6VrjIEaFf/nJGzIxFDsf4x0xIM+B07jRM" crossorigin="anonymous"></script>
</body>
</html>