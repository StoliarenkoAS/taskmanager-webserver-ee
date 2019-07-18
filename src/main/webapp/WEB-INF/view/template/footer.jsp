<%--
  Created by IntelliJ IDEA.
  User: Rabdis
  Date: 16.07.2019
  Time: 12:39
  To change this template use File | Settings | File Templates.
--%>
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

<script src="https://code.jquery.com/jquery-3.3.1.slim.min.js" integrity="sha384-q8i/X+965DzO0rT7abK41JStQIAqVgRVzpbzo5smXKp4YfRvH+8abtTE1Pi6jizo" crossorigin="anonymous"></script>
<script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.14.7/umd/popper.min.js" integrity="sha384-UO2eT0CpHqdSJQ6hJty5KVphtPhzWj9WO1clHTMGa3JDZwrnQq4sF86dIHNDz0W1" crossorigin="anonymous"></script>
<script src="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/js/bootstrap.min.js" integrity="sha384-JjSmVgyd0p3pXB1rRibZUAYoIIy6OrQ6VrjIEaFf/nJGzIxFDsf4x0xIM+B07jRM" crossorigin="anonymous"></script>
</body>
</html>
