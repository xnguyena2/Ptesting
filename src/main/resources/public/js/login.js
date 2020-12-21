$(document).ready(function () {

    $('#btnLogin').click(function (e) {
        e.preventDefault();
        console.log('login click');
        let username = $('#txtUserName').val() + "";
        let password = CryptoJS.MD5($('#txtPassword').val() + "").toString().toUpperCase();
        console.log(password)
        var settings = {
            "url": "http://localhost:5000/auth/signin",
            "method": "POST",
            "timeout": 0,
            "headers": {
                "Content-Type": "application/json"
            },
            "data": JSON.stringify({ "username": username, "password": password }),
        };

        $.ajax(settings).done(function (response) {
            if(response.username === username && response.token){
                console.log('login success');
            }
        });
    });

});