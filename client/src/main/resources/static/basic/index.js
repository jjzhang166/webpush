var token = null;
var socket = null;
var remind = null;

$(function () {
    $("#loginUser").click(function () {
        var userId = $("#uid").val();
        login(userId, $("#userName").val(), [], []);

        if (!token) {
            alert("获取Token失败，请联系管理员");
            return;
        }
        socket = io.connect('http://localhost:7777?token=' + token);

        if (!socket) {
            alert("连接服务器失败，请联系管理员");
            return;
        }
        remind = new Remind(socket, token);
        tokenLogin(remind, token);
    });

    $(" #refresh").click(function () {
        if (socket && token) {
            tokenRefresh(socket, token);
        }
    });

    $("#close").click(function () {
        if (socket) {
            socket.disconnect();
        }
    });

    $("#send").click(function () {
        if (remind) {
            remind.sendApiMessage('p2p', $("#touid").val(), $('#msg').val());
        }
    });
});

/**
 * 登录
 */
var login = function (uid, name, roles, groups) {
    var data = {
        uid: uid,
        name: name,
        roles: roles,
        groups: groups
    };

    $.ajax({
        url: '/socket/api/token',
        type: 'post',
        contentType: 'application/json',
        dataType: "json",
        data: JSON.stringify(data),
        async: false,
        success: function (result) {
            console.log("result: " + JSON.stringify(result));
            debugger;
            if (result.returnTag == "0") {
                var tmpToken = result.data.token;
                token = tmpToken;
                output("本地", "获取令牌成功!<br>用户id:" + uid + "<br>令牌:" + tmpToken + ".<br>会话剩余时间：" + result.data.lastExpires + '秒.');
                $('#loginPanel').hide();
                $('#msgPanel').show();
            } else {
                alert(result.error);
            }
        }
    });
}

/**
 * 输出
 * @param type
 * @param message
 */
var output = function (type, message) {
    var element = "<div><span style='font-weight: bold;'>" + type + ":</span>" + message + "</div>";
    $('#console').prepend(element);
}

/**
 * 获取Token
 * @param t
 */
var tokenLogin = function (remind, token) {
    remind.openSocket(token);
    $('#loginPanel').hide();
    $('#msgPanel').show();
}

/**
 * 刷新Token
 * @param socket
 * @param token
 */
var tokenRefresh = function (socket, token) {
    socket.emit("/socket/api/refresh", {token: token});
}

