
jQuery(document).ready(function ($) {
    $('#logged-form').submit(function (event) {
        event.preventDefault();

        var userName = $('#fld-username').val();
        var password = $('#fld-password').val();
        var confirmationPassword = $('#fld-confirm-password').val();

        if (!userName || !password || password !== confirmationPassword){
            $('#login-error+br').display = 'block';
            $('#login-error').display = 'block';
            $('#login-error').text('Login or password are incorrect. Please try again.');
            return;
        }

        var data = 'username=' + userName + '&password=' + password + '&remember-me=yes';
        $.ajax({
            data: data,
            timeout: 1000,
            type: 'POST',
            url: '/server/login'
        }).done(function(data, textStatus, jqXHR) {
            var preLoginInfo = JSON.parse($.cookie('restsecurity.pre.login.request'))
            $.cookie('restsecurity.username', $('#fld-username').val());
            window.location = preLoginInfo.url;
        }).fail(function(jqXHR, textStatus, errorThrown) {
            $('#login-error+br').display = 'block';
            $('#login-error').display = 'block';
            $('.login-error').text('Login or password are incorrect. Please try again.');
        });
    });
});