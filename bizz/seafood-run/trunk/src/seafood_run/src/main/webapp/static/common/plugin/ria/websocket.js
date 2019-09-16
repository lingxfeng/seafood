var socket, userId = parseInt(Math.random() * 100 + 1);;
var $ = function() {
    return document.getElementById(arguments[0]);
}
var log = function(msg) {
    $("log").innerHTML ? $("log").innerHTML += "<br />" + msg : $("log").innerHTML = msg;
}
var initWebSocket = function() {
    if (window.WebSocket) {
        //用户先HTTP登陆，登陆成功后返回userId，然后建立websocket信号
        socket = new WebSocket("ws://127.0.0.1:8080/websocket/chat?userId=" + userId);
        socket.onmessage = function(event) {
            log(event.data);
        };
        console.dir(socket);
        socket.onopen = function(event) {
            log("Web Socket opened!");
        };
        socket.onclose = function(event) {
            log("Web Socket closed.");
        };
        socket.onerror = function(event) {
            log("Web Socket error.");
        };
    } else {
        log("Your browser does not support Web Socket.");
    }
}
var send = function() {
    var formObj = arguments[0];
    var data = function() {
        socket.send("{'sendId':" + userId + ",'takeId':'" + formObj.toUserId.value + "','content':'" + formObj.content.value + "','type':1}");
    };
    if (socket.readyState !== 1) {
        socket.close();
        initWebSocket();
        setTimeout(function() {
            data();
        }, 250);
    } else {
        data();
    };
}
window.onload = function() {
    initWebSocket();
    document.getElementById('currentUserId').value = userId;
}
