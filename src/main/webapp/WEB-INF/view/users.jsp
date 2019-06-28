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
            <tr>
                <th scope="row" style="vertical-align: middle">1</th>
                <td style="vertical-align: middle">Johnatannnnnn</td>
                <td style="vertical-align: middle">131655-3213454646-231166-131565-312</td>
                <td class="text-center" style="vertical-align: middle"><a href="/user-edit"><i class="fas fa-pencil-alt align-self-center"></i></a></td>
                <td class="text-center" style="vertical-align: middle"><a href="#"><i class="far fa-trash-alt"></i></a></td>
            </tr>
            <tr>
                <th scope="row">2</th>
                <td>Johnatan</td>
                <td>131655-3213454646-231166-131565-312</td>
                <td class="text-center" style="vertical-align: middle"><a href="/user-edit"><i class="fas fa-pencil-alt align-self-center"></i></a></td>
                <td class="text-center" style="vertical-align: middle"><a href="#"><i class="far fa-trash-alt"></i></a></td>
            </tr>
            <tr>
                <th scope="row">3</th>
                <td>Marquis</td>
                <td>131655-3213454646-231166-131565-312</td>
                <td class="text-center" style="vertical-align: middle"><a href="/user-edit"><i class="fas fa-pencil-alt align-self-center"></i></a></td>
                <td class="text-center" style="vertical-align: middle"><a href="#"><i class="far fa-trash-alt"></i></a></td>
            </tr>
            <tr>
                <th scope="row">4</th>
                <td>SantaClaus</td>
                <td>131655-3213454646-231166-131565-312</td>
                <td class="text-center" style="vertical-align: middle"><a href="/user-edit"><i class="fas fa-pencil-alt align-self-center"></i></a></td>
                <td class="text-center" style="vertical-align: middle"><a href="#"><i class="far fa-trash-alt"></i></a></td>
            </tr>
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
                <h5 class="modal-title" id="createModalTitle">Create user</h5>
                <button class="close" type="button" data-dismiss="modal" aria-label="Close">
                    <span aria-hidden="true">&times;</span>
                </button>
            </div>
            <div class="modal-body">
                <form>
                    <div class="form-group">
                        <label for="loginInputCreateModal">Login</label>
                        <input class="form-control form-control-sm" type="text" required id="loginInputCreateModal" aria-describedby="loginHelp" placeholder="login"/>
                        <small id="loginHelpRegisterModal" class="form-text text-muted">Enter your login</small>
                    </div>
                    <div class="form-group">
                        <label for="passwordInputCreateModal">Password</label>
                        <input class="form-control form-control-sm" type="password" required id="passwordInputCreateModal" placeholder="password"/>
                        <small id="passwordHelpRegisterModal" class="form-text text-muted">Enter your password</small>
                    </div>
                    <div class="form-group">
                        <input class="form-control form-control-sm" type="password" required id="passwordConfirmCreateModal" placeholder="password"/>
                        <small id="passwordConfirmHelpRegisterModal" class="form-text text-muted">Confirm password</small>
                    </div>
                    <div class="form-group">
                        <label for="roleCreateModal">Role</label>
                        <select class="form-control form-control-sm" id="roleCreateModal">
                            <option value="ADMIN">Administrator</option>
                            <option value="USER">User</option>
                        </select>
                    </div>
                    <hr class="separator">
                    <div class="container-fluid">
                        <div class="row justify-content-end">
                            <button class="btn btn-success mr-1" type="submit" data-dismiss="modal">Create</button>
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