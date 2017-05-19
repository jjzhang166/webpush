var Remind = Remind || {};

/**
 * 构造方法
 * @param socket
 * @param token
 * @constructor
 */
function Remind(socket, token) {
    this.socket = socket;
    this.token = token;
}
//noinspection JSAnnotator
Remind.prototype = {
    socket: null,
    token: null,

    /**
     * 构造方法
     */
    constructor: Remind,

    /**
     * 打开连接
     * @param token
     */
    openSocket: function (token) {
        socket.on('connect', function () {
            output('本地', '连接到服务器');
        });
        socket.on('disconnect', function () {
            output('本地', '关闭连接!');
        });
        var events = ['p2p', 'sysp2p', 'bizp2p', 'group', 'sys'];
        for (var i in events) {
            this.addEvent(events[i]);
        }
    },

    /**
     * 添加事件
     * @param event
     */
    addEvent: function (event) {
        socket.on(event, function (data) {
            output(event, JSON.stringify(data));
        });
    },

    /**
     * 发送消息
     * @param evnet
     * @param text
     */
    sendMessage: function (evnet, text) {
        socket.emit(evnet, {text: text});
    },


    /**
     * 发送API消息
     * @param evnet
     * @param target
     * @param msg
     */
    sendApiMessage: function (evnet, target, msg) {
        var message = {
            from: "admin",
            to: target,
            text: msg
        }

        $.ajax({
            url: '/socket/api/' + evnet + (target == null ? '' : '/' + target),
            type: 'post',
            dataType: 'json',
            contentType: 'application/json',
            data: JSON.stringify(message),
            success: function (r) {
                output('本地', 'send to:' + target + ' message:' + JSON.stringify(message) + '. success.');
            }
        });
    }
}



