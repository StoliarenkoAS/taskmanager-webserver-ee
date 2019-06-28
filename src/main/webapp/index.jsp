<!doctype html>
<html lang="en">
<head>
    <!-- Required meta tags -->
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
    <!-- Bootstrap CSS -->
    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/css/bootstrap.min.css">

    <title>Taskmanager EE</title>
</head>
<body>
<nav class="navbar navbar-dark navbar-expand-lg bg-dark sticky-top">
    <a href="/" class="navbar-brand"><img src="image/rock-symbol-img.png" alt="logo" width="30"></a>
    <button class="navbar-toggler" type="button" data-toggle="collapse"
            data-target="#navbarSupportedContent" aria-controls="navbarSupportedContent"
            aria-expanded="false" aria-label="Toggle navigation">
        <span class="navbar-toggle-icon"></span>
    </button>
    <div class="collapse navbar-collapse" id="navbarSupportedContent">
        <ul class="navbar-nav mr-auto">
            <li class="nav-item active">
                <a href="/users.jsp" class="nav-link">Users</a>
            </li>
            <li class="nav-item">
                <a href="/projects.jsp" class="nav-link">Projects</a>
            </li>
            <li class="nav-item">
                <a href="/tasks.jsp" class="nav-link">Tasks</a>
            </li>
        </ul>
    </div>
    <div class="btn-group" role="group">
        <button class="btn btn-outline-light mx-1" data-toggle="modal" data-target="#loginModal">Login</button>
        <button class="btn btn-outline-light" data-toggle="modal" data-target="#registerModal">Register</button>
    </div>
</nav>
<nav class="navbar navbar-dark navbar-expand-lg bg-dark fixed-bottom">
    <a href="#" class="navbar-brand"><img src="image/rock-symbol-img.png" alt="logo" width="30"></a>
</nav>

<div class="container-fluid">
    <div class="container">
        <div class="row text-center justify-content-center">
            <div class="col-10" style="margin: 24px"><h1>Taskmanager EE</h1></div>
        </div>
    </div>
</div>

<div class="modal fade" id="loginModal" tabindex="-1" role="dialog" aria-labelledby="loginModal" aria-hidden="true">
    <div class="modal-dialog" role="document">
        <div class="modal-content">
            <div class="modal-header">
                <h5 class="modal-title" id="loginModalTitle">Authorization</h5>
                <button class="close" type="button" data-dismiss="modal" aria-label="Close">
                    <span aria-hidden="true">&times;</span>
                </button>
            </div>
            <div class="modal-body">
                <form>
                    <div class="form-group">
                        <label for="loginInput">Login</label>
                        <input class="form-control" type="text" required id="loginInput" aria-describedby="loginHelp" placeholder="login"/>
                        <small id="loginHelp" class="form-text text-muted">Enter your login</small>
                    </div>
                    <div class="form-group">
                        <label for="passwordInput">Password</label>
                        <input class="form-control" type="password" required id="passwordInput" placeholder="password"/>
                        <small id="passwordHelp" class="form-text text-muted">Enter your password</small>
                    </div>
                    <hr class="separator">
                    <div class="container-fluid">
                        <div class="row justify-content-end">
                            <button class="btn btn-success" type="submit" data-dismiss="modal" style="margin-right: 5px">Login</button>
                            <button class="btn btn-secondary" data-dismiss="modal">Close</button>
                        </div>
                    </div>
                </form>
            </div>
        </div>
    </div>
</div>

<div class="modal fade" id="registerModal" tabindex="-1" role="dialog" aria-labelledby="loginModal" aria-hidden="true">
    <div class="modal-dialog" role="document">
        <div class="modal-content">
            <div class="modal-header">
                <h5 class="modal-title" id="registerModalTitle">Registration</h5>
                <button class="close" type="button" data-dismiss="modal" aria-label="Close">
                    <span aria-hidden="true">&times;</span>
                </button>
            </div>
            <div class="modal-body">
                <form>
                    <div class="form-group">
                        <label for="loginInputRegisterModal">Login</label>
                        <input class="form-control" type="text" required id="loginInputRegisterModal" aria-describedby="loginHelp" placeholder="login"/>
                        <small id="loginHelpRegisterModal" class="form-text text-muted">Enter your login</small>
                    </div>
                    <div class="form-group">
                        <label for="passwordInputRegisterModal">Password</label>
                        <input class="form-control form-control-sm" type="password" required id="passwordInputRegisterModal" placeholder="password"/>
                        <small id="passwordHelpRegisterModal" class="form-text text-muted">Enter your password</small>
                    </div>
                    <div class="form-group">
                        <input class="form-control form-control-sm" type="password" required id="passwordConfirmRegisterModal" placeholder="password"/>
                        <small id="passwordConfirmHelpRegisterModal" class="form-text text-muted">Confirm password</small>
                    </div>
                    <hr class="separator">
                    <div class="container-fluid">
                        <div class="row justify-content-end">
                            <button class="btn btn-success" type="submit" data-dismiss="modal" style="margin-right: 5px">Register</button>
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