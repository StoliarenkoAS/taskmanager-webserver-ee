<jsp:include page="template/header.jsp"/>

<div class="container-fluid">
    <div class="container">
        <div class="row text-center justify-content-center">
            <div class="col-10" style="margin: 24px"><h1>Taskmanager SPRING</h1></div>
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
                <form action="${pageContext.request.contextPath}/authorization/login" method="post">
                    <div class="form-group">
                        <label for="loginInput">Login</label>
                        <input name="login" class="form-control" type="text" required id="loginInput" aria-describedby="loginHelp" placeholder="login"/>
                        <small id="loginHelp" class="form-text text-muted">Enter your login</small>
                    </div>
                    <div class="form-group">
                        <label for="passwordInput">Password</label>
                        <input name="password" class="form-control" type="password" required id="passwordInput" placeholder="password"/>
                        <small id="passwordHelp" class="form-text text-muted">Enter your password</small>
                    </div>
                    <hr class="separator">
                    <div class="container-fluid">
                        <div class="row justify-content-end">
                            <button class="btn btn-success mr-1" type="submit">Login</button>
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
                <form action="${pageContext.request.contextPath}/authorization/register" method="post">
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

<jsp:include page="template/footer.jsp"/>