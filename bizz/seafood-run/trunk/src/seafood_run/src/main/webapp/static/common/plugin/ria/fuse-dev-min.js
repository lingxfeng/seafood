(function() {
    Ext.ns("Disco");
    Disco.ajaxCache = true;//启用ajax缓存功能
    Disco.classMap = new Ext.util.MixedCollection();
    Disco.regClass = function(config) {
        config = config || {};
        Ext.iterate(config, function(k, v) {
            Disco.classMap.add(k, v);
        });
    }
    Disco.loadJs = function(script, async, callback, cache) {
        if (cache)
            Ext.Ajax.disableCaching = false;
        Ext.Ajax.request({
            url: script,
            async: async,//false:同步单线程加载
            success: function(req) {
                eval(req.responseText);
                if (Ext.isFunction(callback)) {
                    callback();
                }
            },
            scope: this
        });
    };

    Disco.loadClass = function(script, cls, callback) {
        Disco.loadJs(script, true, function() {
            callback(eval(cls));
        });
    };

    Disco.requireds = function() {
        var args = Ext.toArray(arguments);
        for (var i = 0; i < args.length; i++) {
            var cls = args[i];
            var isLoadJs = false;
            try {
                if (!eval(cls)) {
                    isLoadJs = true;
                }
            } catch (e) {
                isLoadJs = true;
            }
            if (isLoadJs) {
                var path = Disco.classMap.get(cls);
                this.loadJs(path, false);
            }
        }
    }

    Disco.BASE_URL = "/static/common/plugin/ria/";
    var discoJs = ['core-override-3.2-min.js', 'core-plugin-3.2-min.js', 'core-util-3.2-min.js', 'core-3.2-min.js'];
    for (var i = 0; i < discoJs.length; i++) {
        Disco.loadJs(Disco.BASE_URL + discoJs[i], false, null, Disco.ajaxCache);
    }
}(window));
Ext.onReady(function() {
    var lazyJs = ['ux-all-debug-min.js', 'bug-min.js'];
    for (var i = 0; i < lazyJs.length; i++) {
        Disco.loadJs(Disco.BASE_URL + lazyJs[i], false, null, Disco.ajaxCache);
    }
});