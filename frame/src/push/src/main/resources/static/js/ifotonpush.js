var SocketClient = {
    /**
     * 初始化连接socket服务器
     * @param {} args 连接成功后客户端向服务端传送的参数
     * @param {} url 提供一个socket server连接地址
     */
    init: function(args, url) {
        if (!$.isPlainObject(args)) {
            alert("第一个参数必须为一个JSON对象")
            return;
        }
        var url = url || 'http://localhost:8600/fotonpush';
        var socket = io.connect(url);
        socket.on('connect', function() {
            socket.emit('logic', args);//当客户端与服务端连接成功后,发送业务数据给服务端
        });
        socket.on('user message', this.message);

        socket.on('reconnect', function() {
            this.message('202', '重新连接到服务器。');
        });

        socket.on('reconnecting', function() {
            this.message('302', '与服务器的连接已中断，正在尝试重新连接到服务器。');
        });

        socket.on('error', function(e) {
            this.message('500', e ? e : '发生未知错误。');
        });
        return this;
    },
    /**
     * 消息回调
     * @param {} code
     * @param {} data
     */
    message: function(code, data) {
        $(document.body).append($('<p>').append(data));
    }
}
/**-------------------------------start使用例子-----------------------------------**/
//josn参数
var args = {
    "userId": "1",
    "type": ["1", "2"]
}
SocketClient.message = function(code, data) {
    //推送回来的数据
    console.log(data)
}
SocketClient.init(args);
/**-------------------------------end使用例子-----------------------------------**/
