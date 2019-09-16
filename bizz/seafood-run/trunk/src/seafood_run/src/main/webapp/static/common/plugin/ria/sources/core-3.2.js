/**
 * @class Disco_RIA
 * @singleton
 */
Disco_RIA = {
    /**
     * Disco RIA Framework的版本号
     * 
     * @type String
     */
    version: "2.0",
    /**
     * RIA平台的名称
     * 
     * @type String
     */
    title: "Disco RIA Framework",
    /**
     * 是否开启开发模式,适用于开发阶段,发布可能会影响性能,作用修改js文件，不需要刷新整个页面
     * @type Boolean
     */
    devMode: false,
    /**
     * EasyFuse RIA平台是否开启权限检查。如果开启了权限检查，在使用CrudPanel或者CrudListPanel的时候，会向后台请求权限的判断
     * 
     * @type Boolean
     */
    permissionCheck: true,
    /**
     * 如果开启了权限检查，这个参数用来指定请求权限判断的后台地址
     * @type String
     */
    permissionCheckAction: "/manage.java?cmd=checkButtons",
    /**
     * 指定RIA平台使用的EXTJS的代码版本。可选值目前有'ext-2.2'和'ext-3.2'
     * 
     * @type String
     */
    extVersion: 'ext-3.2',
    /**
     * <p>
     * 指定RIA平台延迟加载js的请求地址。如果是使用后台加载的话，该参数指定后台返回JS的地址，如果是前台加载js，则可以直接指定为js的地址
     * 例如：如果项目所有的js都放在/demo-script下 那么这里就指定为script:'/demo-script',
     * </p>
     * 
     * @type String
     */
    script: '/extApp.java?cmd=loadScript&script=',
    /**
     * RIA平台集成了个性化定制（包括皮肤，前台portal等）的前后台集成，如果要使用该个性化功能，该参数指定了个性化请求和保存地址
     * 
     * @type String
     */
    PersonalityUrl: "manage.java",
    /**
     * 系统自己的css样式目录，比如说头部的图片,背景颜色等等
     * 
     * @type String
     */
    sysSkinPath: null,
    /**
     * RIA平台针对CRUD集成到CrudPanel和CrudListPanel中和后台请求关联的URL样式。 for
     * struts2的动态方法调用，使用：return baseUrl+"!"+cmd+".action" 也可以定制自己的请求样式。参考Demo
     * @type String
     */
    getCfg: function(cfg) {
        return Ext.getObjVal(window.Global.Config, cfg);
    },
    setCfg: function(key, value) {
        var cfg = Disco_RIA.getCfg();
        if (cfg) {
            cfg[key] = value;
        }
        return this;
    },
    /**
     * 格式化系统中出现的url。<br/>
     * 该方法默认使用baseUrl?cmd=cmd来匹配，如果使用struts等，可以自己覆盖该方法，实现自定义的匹配模式
     * 
     * @param {String}
     *            baseUrl 基础url
     * @param {String}
     *            cmd 命令名称
     */
    formatUrl: function(baseUrl, cmd) {
        return Ext.urlAppend(baseUrl, Ext.urlEncode({
            cmd: cmd
        }));
    },
    ajaxErrorHandler: function(result, defaultMsg) {
        Disco.Ext.Msg.error("操作失败，失败原因：<br/>" + (result.error || (result.errors ? (result.errors.msg || result.errors.msg) : null) || defaultMsg || "错误未知"));
    }
};
Ext.Ajax.timeout=90000;//90秒
Ext.form.Field.prototype.msgTarget = 'side';
Ext.BLANK_IMAGE_URL = String.format('static/common/plugin/extjs/{0}/resources/images/default/s.gif', Disco_RIA.extVersion);
Ext.chart.Chart.CHART_URL = String.format('static/common/plugin/extjs/{0}/resources/charts.swf', Disco_RIA.extVersion);
Ext.QuickTips.init();

/**
 * disco框架,基本包
 */
Ext.namespace("Disco.Ext", "Disco.Ext.Msg");

Disco.Ext = Disco.Ext;
Disco.Ext.Msg = Disco.Ext.Msg;
/**
 * @class Disco.Ext.Msg 对Ext.Msg的包装，加入了一些缺省的设置。
 */
Ext.apply(Disco.Ext.Msg, {
    /**
     * <p>
     * 对Ext.Msg.alert的包装，加入了一些缺省设置
     * <li>title默认为'操作提示'</li>
     * <li>type默认为Ext.Msg.INFO</li>
     * <li>宽度默认为240</li>
     * </p>
     * 
     * @param {Object/String}
     *            msg 提示的消息,如果是Object，则认为是自定义的设置对象
     * @param {String}
     *            title 提示窗口的标题
     * @param {String}
     *            fn callback方法
     * @param {Object}
     *            scope callback方法执行的域
     * @param {Object}
     *            type 提示的图片，为以下可选值： Ext.MessageBox.INFO
     *            Ext.MessageBox.WARNING Ext.MessageBox.QUESTION
     *            Ext.MessageBox.ERROR
     * 
     */
    alert: function(msg, title, fn, scope, type) {
        if (Ext.isFunction(title)) {
            type = scope;
            scope = fn;
            fn = title;
            title = null;
        }
        var obj = {
            title: title || '操作提示',
            msg: msg,
            scope: scope || window,
            fn: fn,
            width: 220,
            icon: type || Ext.Msg.INFO,
            buttons: Ext.Msg.OK
        };
        if (typeof msg == 'object')
            Ext.apply(obj, msg);
        Ext.Msg.show(obj);
    },
    /**
     * <p>
     * 扩展的一个专门用于RIA平台中错误提示的方法，扩展自Disco.Ext.Msg.alert方法
     * <li>type默认为Ext.Msg.ERROR</li>
     * </p>
     * 
     * @param {Object/String}
     *            msg 提示的消息,如果是Object，则认为是自定义的设置对象
     * @param {String}
     *            title 提示窗口的标题
     * @param {String}
     *            fn callback方法
     * @param {Object}
     *            scope callback方法执行的域
     */
    error: function(msg, title, fn, scope) {
        var expargs = Disco.Ext.Msg.error.length;
        var args = Array.prototype.slice.call(arguments, 0, expargs);
        if (expargs != args.length) {
            if (args.length < expargs) {
                var i = args.length;
                for (i; i < expargs; i++) {
                    args.push(null);
                }
                args.push(Ext.Msg.ERROR);
            }
        } else {
            args.push(Ext.Msg.ERROR);
        }
        Disco.Ext.Msg.alert.apply(this, args);
    }
});

/**
 * @class Disco.Ext.Window
 * 
 * 显示单例模式的Window,可将自己的面板放入到Disco.Ext.Window中。
 * 
 * <pre>
 * <code>
 * //示例：
 * //创建需要在窗口中显示的panel
 * var myshowpanel = new MyShowPanel();
 * //调用工厂方法show来显示窗口
 * Disco.Ext.Window.show({
 * 			single : false,
 * 			width : 400,
 * 			height : 160,
 * 			scope : this,
 * 			title : &quot;确认信息&quot;,
 * 			buttons : ['yes', 'no'],
 * 			items : myshowpanel,
 * 			handler : function(btn, win, fp) {
 * 				if (btn == 'yes') {
 * 					//my customer handler
 * 				} else {
 * 					win.hide();
 * 				}
 * 			}
 * 		});
 * </code>
 * </pre>
 * 
 * @singleton
 */
Disco.Ext.Window = function() {
    var components = new Ext.util.MixedCollection();
    components.on('add', function(length, o, key) {
        if (length > 50) {
            components.removeAt(0);
        }
    }, this)
    var all = new Ext.util.MixedCollection();
    var buttonText = {
        ok: "确定",
        cancel: "重置",
        yes: "确定",
        no: "取消"
    };
    var buttonHandler = function(button, btn) {
        var win = button.ownerCt.ownerCt;
        Ext.callback(win.handler, win.scope || win, [btn, win, win.getComponent(0)]);
        if (win.autoHide)
            win.hide();
    }
    return {
        defaultConfig: {
            maxWin: 5,
            width: 500,
            height: 300,
            modal: true,
            layout: 'fit',
            single: true,
            constrain: true,
            resizable: false,
            buttonAlign: 'center',
            closeAction: 'hide'
        },
        components: components,
        autoDestroyWin: function(win) {
            if (this.all.getCount() > this.defaultConfig.maxWin) {
                win.destroy();
                this.autoRemoveProperty.call(win);
                this.all.remove(win);
            }
        },
        autoRemoveProperty: function() {
            delete this.handler;
            delete this.scope;
            delete this.autoHide;
        },
        destroyComp: function() {
            var args = Array.prototype.slice.call(arguments, 0);
            Ext.each(args, function(c) {
                if (Ext.type(c.destroy) == 'function') {
                    c.destroy();
                }
                components.remove(c);
            }, this)
        },

        /**
         * 调用该方法来显示一个窗口。
         * 
         * @param {Object}
         *            config 配置显示窗口的对象，包括以下配置项：
         *            <ul>
         *            <li>single {Boolean} :
         *            设置是否是单例显示，如果是true，在关闭窗口后会删除窗口中的panel。如果要多次使用该窗口显示同一个panel，single要设置为true。</li>
         *            <li>width {Integer} : 设置窗口的宽度</li>
         *            <li>height {Integer} : 设置窗口高度</li>
         *            <li>scope {Object} : 方法中按钮响应方法的执行域</li>
         *            <li>title {String} : 窗口的显示名称</li>
         *            <li>buttons {Array} :
         *            自定义窗口下显示的按钮，可选值有：'ok','cancel','yes','no'</li>
         *            <li>items {Object} ： 窗口内要显示的面板panel</li>
         *            <li>handler {Function} : 窗口下按钮的响应事件,其中传入的参数有：
         *            <ul>
         *            <li>btn :点击的btn代码</li>
         *            <li>win :当前window</li>
         *            <li>fp ：窗口中的panel</li>
         *            </ul>
         *            </li>
         *            <li>layout {String} : 窗口的布局，默认为fit</li>
         *            <li>buttonAlign {String} : 窗口下按钮的对齐方式，默认为'center'</li>
         *            <li>closeAction {String} : 窗口关闭事件，默认为'hide'</li>
         *            </ul>
         */
        show: function(obj) {
            var config = Ext.apply({}, obj, this.defaultConfig);
            var win = this.getFreeWin(config);
            win.on('beforehide', this.autoDestroyWin, this, {
                single: true
            });

            win.on('hide', function(win) {
                if (config.single) {
                    win.remove(0, true);
                    components.remove(win.getComponent(0));
                    this.autoRemoveProperty.call(win);
                } else {
                    var cmp = win.remove(0, false);
                    cmp.hide();
                    cmp.getEl().appendTo(Ext.getBody());
                }
            }, this, {
                single: true
            });

            win.show();
            win.setTitle(config.title);
            win.setWidth(config.width);
            if (config.height == 'auto') {
                win.autoHeight = true;
                win.syncSize.defer(1000, win);
            } else {
                win.autoHeight = false;
                win.setHeight(config.height);
            }
            win.center();
        },
        buildComponent: function(cmp, compId) {
            if (components.get(compId)) {
                return components.get(compId);
            } else if (Ext.type(cmp) == 'object') {
                var component = null;
                if (cmp.constructor.prototype.ctype == 'Ext.Component') {
                    component = cmp;
                } else if (cmp.constructor == Object) {
                    component = Ext.ComponentMgr.create(cmp, 'panel');
                } else {
                    component = cmp;
                }
                if (compId) {
                    components.add(compId, component);
                }
                return component;
            }
        },
        updateButtons: function(win, buttons) {
            if (Ext.isArray(buttons) && buttons.length) {
                Ext.each(win.buttons, function(btn) {
                    if (buttons.indexOf(btn.id) >= 0) {
                        btn.show();
                    } else {
                        btn.hide();
                    }
                }, win);
            } else {
                Ext.each(win.buttons, function(btn) {
                    btn.hide();
                }, win);
            }
        },
        getFreeWin: function(config) {
            var win = null;
            all.each(function(o) {
                if (o.hidden) {
                    win = o;
                    return false;
                }
            }, this);
            var cmp = config.items;
            var buttons = config.buttons;
            var handler = config.handler;
            var scope = config.scope;
            var autoHide = config.autoHide;
            Ext.del(config, 'buttons', 'items', 'handler', 'autoHide', 'scope');
            cmp = this.buildComponent(cmp, config.compId);
            var win = win || this.getWin(Ext.apply(config, {
                buttons: this.getButtons.call(this)
            }));

            win.autoHide = autoHide;
            win.handler = handler;
            win.scope = scope;

            this.updateButtons(win, buttons);
            if (win.items && win.items.getCount() > 0)
                win.remove(0, false);
            if (cmp.hidden)
                cmp.show();
            win.add(cmp);
            return win;
        },
        all: all,
        getWin: function(config) {
            var win = new Ext.Window(Ext.apply(config, {
                manager: this.group
            }));
            all.add(win);
            return win;
        },
        YESNO: ['yes', 'no'],
        YESNOCANCEL: ['yes', 'cancel', 'no'],
        YES: ['yes'],
        getButtons: (function() {
            return [{
                text: buttonText['yes'],
                id: 'yes',
                handler: buttonHandler.createDelegate(this, ['yes'], 1),
                scope: this
            }, {
                text: buttonText['cancel'],
                id: 'cancel',
                handler: buttonHandler.createDelegate(this, ['cancel'], 1),
                scope: this
            }, {
                text: buttonText['no'],
                id: 'no',
                handler: buttonHandler.createDelegate(this, ['no'], 1),
                scope: this
            }]
        })
    }
}();

/**
 * @class Ext.form.LabelField
 * @extends Ext.form.Field
 * 
 * 用于在FORM表单用div显示数据,类似于Ext的Label。但在使用方法上还是有一些区别。
 * 
 * <pre>
 * <code>
 *  //例子：
 *  {xtype:&quot;labelfield&quot;,value:&quot;标签名称&quot; /&gt;
 * </pre>
 * </code>
 * 
 */
Ext.form.LabelField = Ext.extend(Ext.form.Field, {
    // private
    defaultAutoCreate: {
        tag: 'div'
    },
    /**
     * @cfg {String} fieldClass 默认的字css
     */
    fieldClass: "x-form-item-label",
    // 对齐
    style: "padding-top:3px",
    onRender: function(ct, position) {
        Ext.form.LabelField.superclass.onRender.call(this, ct, position);
        this.el.dom.name = this.initialConfig.name;
        this.el.dom.value = '';
    },
    /**
     * 对value的显示进行渲染
     * @cfg {function} renderer
     */
    /**
     * 设置LabelField的显示值 如果有renderer,则会先调用renderer,然后在显示
     * @param {Object} v
     */
    setValue: function(v) {
        Ext.form.LabelField.superclass.setValue.call(this, v);
        var t = v;
        if (this.renderer)
            t = this.renderer(v);
        if (typeof t == window.undefined)
            t = '';
        if (this.el)
            this.el.update(t);
    },
    /**
     * @cfg function markInvalid 覆盖父类Ext.emptyFn
     */
    markInvalid: Ext.emptyFn,
    /**
     * @cfg function clearInvalid 覆盖父类Ext.emptyFn
     */
    clearInvalid: Ext.emptyFn
});
Ext.reg('labelfield', Ext.form.LabelField);

/**
 * @class Ext.form.LabelFieldReadonly
 * @extends Ext.form.LabelField
 * 
 * 只读的LabelField,Ext.form.LabelField中的getValue,是可以通过getValue来获取设置的值，而LabelFieldReadonly无法获得去值的!
 */
Ext.form.LabelFieldReadonly = Ext.extend(Ext.form.LabelField, {
    getValue: Ext.emptyFn
});
Ext.reg('labelfieldreadonly', Ext.form.LabelField);

/**
 * @class Ext.form.SearchField
 * @extends Ext.form.TwinTriggerField
 * 
 * 三态的通用查询控件
 * 
 * <pre>
 * <code>
 * Ext.onReady(function() {
 * 			var field = new Ext.form.SearchField();
 * 			new Ext.Viewport({
 * 						layout : 'border',
 * 						items : [{
 * 									xtype : &quot;panel&quot;,
 * 									region : &quot;center&quot;,
 * 									items : [field]
 * 								}]
 * 					});
 * 			field.on(&quot;search&quot;, function(c, v, e) {
 * 						alert(&quot;do my search:&quot; + v)
 * 					}, this);
 * 			field.on(&quot;clear&quot;, function(c, v, e) {
 * 						alert(&quot;do my clear:&quot; + v)
 * 					}, this);
 * 		});
 * </code>
 * </pre>
 */
Ext.form.SearchField = Ext.extend(Ext.form.TwinTriggerField, {
    // private
    onSearch: function() {
        this.hasSearch = true;
    },
    // private
    onClear: function() {
        if (this.hasSearch) {
            this.hasSearch = false;
            this.el.dom.value = '';
            this.triggers[0].hide();
            this.focus();
        }
    },
    initComponent: function() {
        Ext.form.SearchField.superclass.initComponent.call(this);
        this.addEvents(
        /**
         * @event clear 当点击查询图标时触发，在该事件中自定义查询的方法。
         * @param {Ext.form.SearchField}
         *            三态查询控件自己
         * @param {String}
         *            value 在查询控件中输入的查询条件
         * @param {Ext.EventObject}
         *            e 事件对象
         */
        'search',
        /**
         * @event clear 当点击清空查询条件图标时触发，在该事件中自定义清除查询的方法。
         * @param {Ext.form.SearchField}
         *            三态查询控件自己
         * @param {String}
         *            value 在查询控件中清除的查询条件
         * @param {Ext.EventObject}
         *            e 事件对象
         */
        'clear');
        this.on('specialkey', function(f, e) {
            if (e.getKey() == e.ENTER && !(e.shiftKey || e.ctrlKey || e.altKey)) {
                this.onSearch(this, this.getValue(), e);
            }
        }, this);
        this.on({
            scope: this,
            clear: this.onClear,
            search: this.onSearch
        });
    },
    validationEvent: false,
    validateOnBlur: false,
    hideTrigger1: true,
    trigger1Class: 'x-form-clear-trigger',
    trigger2Class: 'x-form-search-trigger',
    width: 180,
    hasSearch: false,
    paramName: 'query',
    onTrigger1Click: function(e) {
        this.fireEvent("clear", this, this.getValue(), e);
    },
    onTrigger2Click: function(e) {
        this.fireEvent("search", this, this.getValue(), e);
    }
});

/**
 * 搜索框<br/> 该搜索框可以绑定在一个Store上，并通过search和clear方法，来控制store的刷新和查询等。<br/>
 * 
 * @class SearchField
 * @extends Ext.form.TwinTriggerField
 * @xtype searchfield
 */
Disco.Ext.SearchField = Ext.extend(Ext.form.SearchField, {
    /**
     * @cfg {String} paramName 搜索框在查询时，把需要搜索的数据绑定到store的查询名称
     */
    /**
     * @cfg {Ext.data.Store} store 搜索框绑定的store对象。
     */
    validationEvent: false,
    validateOnBlur: false,
    /**
     * @cfg {String} emptyText 搜索框默认显示
     */
    emptyText: '内容关键字......',
    onClear: function() {
        if (this.hasSearch) {
            Disco.Ext.SearchField.superclass.onClear.call(this);
            var s = this.store;
            delete s.baseParams[this.paramName];
            s.load();
        }
    },
    onSearch: function() {
        var v = this.getRawValue();
        if (v.length < 1)
            return this.onTrigger1Click();
        // this.store.removeAll();
        this.store.baseParams = {};
        this.store.baseParams[this.paramName] = v;
        var o = {
            start: 0,
            searchType: 'simple'
        };
        this.store.reload({
            params: o,
            callback: function(rs) {
                if (!rs || rs.length < 1) {
                    Disco.Ext.Msg.alert("没有找到符合条件的记录！")
                }
            }
        });
        this.hasSearch = true;
        this.triggers[0].show();
        this.focus();
    },
    initComponent: function() {
        if (!this.store.baseParams) {
            this.store.baseParams = {};
        }
        Disco.Ext.SearchField.superclass.initComponent.call(this);
    }
});
Ext.reg("searchfield", Disco.Ext.SearchField);

/**
 * CkEditor编辑器Ext集成
 * @class Disco.Ext.CkEditor
 * @extends Ext.form.TextArea
 */
Disco.Ext.CkEditor = Ext.extend(Ext.form.TextArea, {
    editorCfg: {},
    onResize: function(t, w, h) {
        var ed;
        if (ed = this.getEditor()) {
            ed.resize('99%', h - 5);
        }
    },
    editDataReadyHanlder: function() {
        this.fireEvent('editdataready', this, this.getEditor(), this.getEditor().getData());
    },
    setValue: function(v, callback) {
        Disco.Ext.CkEditor.superclass.setValue.call(this, v);
        var ed;
        if (ed = this.getEditor()) {
            var fn = this.editDataReadyHanlder;
            if (Ext.isFunction(callback)) {
                callback.createSequence(fn, this);
            } else {
                callback = fn.createDelegate(this);
            }
            ed.setData.defer(1, ed, [v, callback]);
        }
    },
    getValue: function() {
        var ed;
        if (ed = this.getEditor()) {
            return ed.getData();
        } else {
            return Disco.Ext.CkEditor.superclass.getValue.call(this);
        }
    },
    getEditor: function() {
        if (this.el) {
            var id = this.el.id, ed = null;
            if (window.CKEDITOR && CKEDITOR.instances && (ed = CKEDITOR.instances[id]))
                return ed;
        }
    },
    createEditor: function() {
        var id = this.el.id;
        var size = this.el.getSize();
        var h = Math.max(size.height, this.height);
        if (this.getEditor()) {
            CKEDITOR.remove(this.getEditor());
        }
        var editor = CKEDITOR.replace(id, Ext.apply({
            height: h
        }, this.editorCfg));
        var em = this;
        editor.on('instanceReady', function() {
            em.fireEvent.call(em, 'instanceReady');
            editor.resize('99%', h - 5);
        });
    },
    beforeDestroy: function() {
        Disco.Ext.CkEditor.superclass.beforeDestroy.call(this);
        if (this.getEditor()) {
            CKEDITOR.remove(this.getEditor());
        }
    },
    afterRender: function() {
        this.addEvents('editdataready', 'instanceReady');
        Disco.Ext.CkEditor.superclass.afterRender.call(this);
        this.on('resize', this.onResize, this);
        this.createEditor.defer(1, this);
    }
});
Ext.reg('ckeditor', Disco.Ext.CkEditor);
/**
 * @class Disco.Ext.FormWindow
 * @extends Ext.Window
 * 
 * 扩展的包含表单的窗口组件。<br/> 主要的扩展是在窗口关闭的时候，去检查表单是否已经做了修改，如果修改了，可以提示是否保存表单内容。<br/>
 * 要使用该组件，必须保证该窗口内的第一个组件就是form表单。及window.getComponent(0)就是formPanel.<br/>
 * 
 */
Disco.Ext.FormWindow = Ext.extend(Ext.Window, {
    /**
     * @cfg {Disco.Ext.CrudPanel} service [option]
     *      如果该FormWindow是CRUDpanel的一部分，则需要把该CrudPanel传入，在调用保存方法的时候，使用CrudFunction的save方法。
     */
    /**
     * @cfg {Function} saveFunction [option]
     *      可以配置一个保存方法，在调用保存方法的时候，使用该放来来处理保存表单的动作。
     *      <ul>
     *      传入参数
     *      <li>FormPanel 保存的表单</li>
     *      </ul>
     */
    /**
     * @cfg {Boolean} confirmSave 在关闭的时候是否需要提示保存表单内容。默认为false。
     */
    confirmSave: false,
    checkFormDirty: function() {
        var fp = this.getComponent(0);
        if (fp && fp.form)
            return fp.form.isDirty();
        else
            return false;
    },
    hide: function(animateTarget, cb, scope) {
        if (this.hidden || this.fireEvent("beforehide", this) === false)
            return;
        if (this.confirmSave && this.checkFormDirty()) {
            Ext.MessageBox.show({
                title: '是否要保存录入的数据?',
                msg: '您所编辑的表单中含有末保存的数据,是否要保存修改后的内容?',
                buttons: Ext.Msg.YESNOCANCEL,
                fn: function(btn) {
                    if (btn == "no") {
                        Disco.Ext.FormWindow.superclass.hide.call(this, animateTarget, cb, scope);
                    } else if (btn == "yes") {
                        if (this.crudService) {
                            this.crudService.save(Disco.Ext.FormWindow.superclass.hide.createDelegate(this, [animateTarget, cb, scope]), this.autoClose);
                        } else if (Ext.isFunction(this.saveFunction)) {
                            this.saveFunction(this.getComponent(0));
                        }
                    } else if (btn == "cancel") {
                    }
                },
                icon: Ext.MessageBox.QUESTION,
                scope: this
            });
        } else {
            Disco.Ext.FormWindow.superclass.hide.call(this, animateTarget, cb, scope);
        }
    }
});

/**
 * @class Disco.Ext.CascadeForm
 * @extends Ext.FormPanel
 * 
 * 主从表单，主要用于Form+EditorGridPanel[+]的情况。<br/>
 * 比如销售出库单就是一个典型的主从表单。包括出库单的相关信息表单和出库商品明细可编辑列表。<br/>
 * 
 * <pre>
 * <code>
 * var formPanel = new Disco.Ext.CascadeForm({
 * 	//支持回车键导航
 * 	enterNavigationKey : true,
 * 	//组件导航顺序
 * 	navigationSequences : ['client', 'deliveryTime', this.editGrid.id],
 * 	//创建工具栏
 * 	tbar : this.createFormToolBar(),
 * 	//创建北部表单（即主表单信息）
 * 	buildNorthForm : function() {
 * 		var Uitl = Disco.Ext.Util;
 * 		return {
 * 			height : 70,
 * 			frame : true,
 * 			border : false,
 * 			layout : 'form',
 * 			style : 'padding:2px;',
 * 			bodyStyle : 'padding:5px;',
 * 			defaults : {
 * 				anchor : '-20'
 * 			},
 * 			items : [{
 * 						xtype : &quot;hidden&quot;,
 * 						name : &quot;id&quot;
 * 					}, Uitl.buildColumnForm(4, {
 * 								fieldLabel : '编号',
 * 								name : 'sn',
 * 								allowBlank : false,
 * 								readOnly : true
 * 							}, Ext.apply({}, {
 * 										allowBlank : false,
 * 										choiceValue : function(o) {
 * 											var fp = formPanel.form;
 * 											if (!fp
 * 													.findField(&quot;consigneeAddress&quot;)
 * 													.getValue()) {
 * 												fp
 * 														.findField(&quot;consigneeAddress&quot;)
 * 														.setValue(o.address
 * 																|| &quot;&quot;);
 * 											}
 * 											if (!fp.findField(&quot;consigneePhone&quot;)
 * 													.getValue()) {
 * 												fp.findField(&quot;consigneePhone&quot;)
 * 														.setValue(o.tel || &quot;&quot;);
 * 											}
 * 										}.createDelegate(this)
 * 									}, ConfigConst.CRM.client), {
 * 								xtype : &quot;datefield&quot;,
 * 								fieldLabel : '下单时间',
 * 								name : 'buyTime',
 * 								format : &quot;Y-m-d&quot;,
 * 								allowBlank : false,
 * 								value : new Date()
 * 							}, {
 * 								xtype : &quot;datefield&quot;,
 * 								fieldLabel : '交货时间',
 * 								name : 'deliveryTime',
 * 								format : &quot;Y-m-d&quot;,
 * 								allowBlank : false
 * 							}), Uitl.buildColumnForm(3, {
 * 								panelCfg : {
 * 									columnWidth : .25
 * 								},
 * 								fieldLabel : '联系电话',
 * 								name : 'consigneePhone',
 * 								allowBlank : false
 * 							}, {
 * 								panelCfg : {
 * 									columnWidth : .25
 * 								},
 * 								fieldLabel : '联系地址',
 * 								name : 'consigneeAddress',
 * 								allowBlank : false
 * 							}, {
 * 								panelCfg : {
 * 									columnWidth : .5
 * 								},
 * 								fieldLabel : '备注',
 * 								name : 'remark',
 * 								allowBlank : false
 * 							})]
 * 		}
 * 	},
 * 	//创建南部表单，即主表单附属信息
 * 	buildSouthForm : function() {
 * 		return {
 * 			height : 45,
 * 			border : false,
 * 			frame : true,
 * 			region : &quot;south&quot;,
 * 			style : 'padding:1px;',
 * 			items : [Disco.Ext.Util.twoColumnPanelBuild(3, Ext.apply({}, {
 * 								fieldLabel : '业务员'
 * 							}, ConfigConst.CRM.seller), {
 * 						xtype : &quot;labelfield&quot;,
 * 						fieldLabel : '制单',
 * 						name : &quot;inputUser&quot;,
 * 						value : {
 * 							trueName : &quot;$!{session.Disco_SECURITY_USER.trueName}&quot;
 * 						},
 * 						renderer : objectRender
 * 					}, {
 * 						xtype : &quot;labelfield&quot;,
 * 						fieldLabel : '审核',
 * 						name : &quot;auditor&quot;,
 * 						renderer : objectRender
 * 					})]
 * 		}
 * 	},
 * 	//创建从表单，即可编辑表格
 * 	buildContentForm : function() {
 * 		return editGrid;
 * 	}
 * });
 * </code>
 * </pre>
 */
Disco.Ext.CascadeForm = Ext.extend(Ext.FormPanel, {
    /**
     * @cfg {String} layout Form表单内的布局
     */
    layout: 'border',
    /**
     * @cfg {Array} formElements 如果是在border布局下，指定需要哪些region组件。<br/>
     *      在这里设置了region，才会去调用对应的buildXXXForm方法。
     */
    formElements: ['west', 'north', 'east', 'south'],
    /**
     * 构建西部表单面板
     */
    buildWestForm: function() {
    },
    /**
     * 构建北部表单面板
     */
    buildNorthForm: function() {
    },
    /**
     * 构建东部表单面板
     */
    buildEastForm: function() {
    },
    /**
     * 构建南部表单面板
     */
    buildSouthForm: function() {
    },
    /**
     * 构建中心表单面板
     */
    buildContentForm: function() {
    },
    /**
     * 获取面板中可以编辑的组件
     * 
     * @return {Array} cmps 得到表单中可编辑组件的数组。
     */
    getEditCmps: function() {
        return this.editCmps = this.editCmps || [];
    },

    /**
     * 添加可以编辑的组件<br/> 在主从表单中，可能存在一个主表单+多个可编辑列表的情况。<br/>
     * 如果是存在多个可编辑列表的情况，可以使用该方法把所有可编辑列表添加进去。<br/>
     * 只有添加为可编辑组件的组件，在主表单提交的时候，才会从这些可编辑列表中取得表单值。<br/>
     * 
     * @param {Component/Array[]
     *            Component} cmp 可编辑组件实例。
     */
    addEditCmps: function(cmp) {
        Ext.each(arguments, function(cmp) {
            if (Ext.isArray(cmp)) {
                this.addEditCmps(cmp);
                return true;
            }
            if (!this.containsEditCmp(cmp)) {
                this.getEditCmps().push(cmp);
            }
        }, this);
    },
    // private
    containsEditCmp: function(cmp) {
        return this.getEditCmps().indexOf(cmp) >= 0;
    },
    /**
     * 获取可编辑组件的值<br/>
     * EditorGridPanel，EditorTreeGridPanel都已经实现了getRowsValues方法。<br/>
     * 如果是要实现自己的可编辑组件，要在表单提交的时候能够得到其中的值，也需要实现一个getRowsValues()的方法。<br/>
     * 
     * @return {Object} v 得到的所有可编辑组件的值的对象。
     */
    getEditorCmpValues: function() {
        var values = {};
        Ext.each(this.getEditCmps(), function(cmp) {
            var rvs = cmp.getRowsValues();
            if (rvs) {
                Ext.apply(values, rvs);
            }
        }, this);
        return values;
    },
    /**
     * 获取整个表单的数据，包括表单中所有可编辑组件的值。
     * 
     * @return {Object} o 得到的主表单的值对象。
     */
    getValues: function() {
        var values = this.getForm().getValues();
        var cmpValues = this.getEditorCmpValues();
        Ext.apply(values, cmpValues);
        return values;
    },
    /**
     * 设置表单数据
     * 
     * @param {Object}
     *            values
     */
    setValues: function(values) {
        this.getForm().setValues(values);
    },

    // private
    onBeforeSubmit: function() {
        var values = this.getEditorCmpValues();
        this.getForm().baseParams = values;
    },
    /**
     * 提交表单数据
     * 
     * @param {Object}
     *            options
     */
    submit: function(options) {
        if (this.onBeforeSubmit() !== false) {
            this.getForm().submit(options);
        }
    },
    // private
    beforeDestroy: function() {
        this.editCmps = null;
        Disco.Ext.CascadeForm.superclass.beforeDestroy.call(this);
    },
    /**
     * @cfg {Function} buildContentForm 创建可编辑组件的方法。
     */
    /**
     * 创建Form主体部分
     * 
     * 可以在该方法中创建所有主从表单中所需要的可编辑组件。
     * 
     * @return {Component} cf 创建的可编辑的组件。
     */
    createContent: function() {
        var cf = this.buildContentForm();
        this.addEditCmps(cf);
        return cf;
    },
    initComponent: function() {
        this.items = [{
            layout: 'fit',
            region: 'center',
            border: false,
            items: this.createContent()
        }]
        for (var i = 0; i < this.formElements.length; i++) {
            var en = this.formElements[i], cmp;
            var eName = en.substring(0, 1).toUpperCase() + en.substring(1);
            if (cmp = this[String.format('build{0}Form', eName)]()) {
                cmp.region = en;
                this.items.push(cmp);
            }
        }
        Disco.Ext.CascadeForm.superclass.initComponent.call(this);
    }
});
/**
 * @class Disco.Ext.TreeCascadePanel
 * @extends Ext.Panel
 * 
 * 左右关联的一种组件包装。该组件在CRUDPanel的基础上，在主列表页面中，左边加入了一个树面板。<br/>
 * 在点击左边树中节点后，会把选中的树节点传入到右边列表中，并完成相应动作。<br/>
 * 
 * 该组件常用在呈树状结构的业务组件上，比如部门、产品分类、角色等。<br/>
 * 该组件也不仅限制于部门这种对象，也可用在任何树面板+任何面板，且点击树节点有联动效果的组件组件上。
 * 
 * <pre>
 * <code>
 * DepartmentPanel = Ext.extendX(Disco.Ext.TreeCascadePanel,
 * 		function(superclass) {
 * 			return {
 * 				queryParam : 'parentId',
 * 				treeCfg : {
 * 					title : &quot;部门树&quot;,
 * 					rootText : '所有部门',
 * 					rootIconCls : 'treeroot-icon',
 * 					loader : Global.departmentLoader
 * 				},
 * 				onTreeClick : function(node) {
 * 					var id = (node.id != 'root' ? node.id : &quot;&quot;);
 * 					this.list.parent = {
 * 						id : id,
 * 						title : node.text
 * 					};
 * 					this.list.store.baseParams.parent = id;
 * 					superclass.onTreeClick.apply(this, arguments);
 * 				},
 * 				getPanel : function() {
 * 					if (!this.panel) {
 * 						this.panel = new DepartmentListPanel();
 * 						this.panel.tree = this.tree;
 * 						this.list = this.panel.grid;
 * 					}
 * 					return this.panel;
 * 				}
 * 			}
 * 		});
 * </code>
 * </pre>
 */
Disco.Ext.TreeCascadePanel = Ext.extend(Ext.Panel, {
    /**
     * @cfg {Object} treeCfg 定义列表左边树的结构
     */
    treeCfg: {},
    /**
     * @cfg {String} layout 定义主页面布局。默认为hbox
     */
    layout: 'hbox',
    /**
     * @cfg {Boolean} border 定义主页面边框。默认为false
     */
    border: false,
    /**
     * @cfg {Object} layoutConfig 定义布局属性。默认为align:'stretch'
     */
    layoutConfig: {
        align: 'stretch'
    },
    /**
     * @cfg {String} queryParam 定义当点击树节点后，传入到list中作为查询条件的参数的传入名称。默认为'type'<br/>
     *      例如，如果点击某一个treeNode，该节点id为123，相当于在列表store.baseParams中加入了{type:123}的查询参数。
     */
    queryParam: 'type',
    /**
     * @cfg {Object} defaultstreeCfg 默认的树配置参数
     */
    defaultstreeCfg: {
        width: 200,
        dataUrl: '',
        rootText: 'root',
        rootId: 'root',
        loader: null,
        rootVisible: true
    },
    /**
     * 得到主面板，即左边列表面板，常用CrudPanel。
     * 
     * @return {Object} panel 左边面板对象
     */
    getPanel: function() {
        return this.panel;
    },
    /**
     * 得到树面板
     * 
     * @return {Object} tree 树面板对象
     */
    getTree: function() {
        return this.tree;
    },
    /**
     * 得到列表对象。如果主面板单纯的是一个GridPanel，则直接返回该面板，如果主面板是一个EasJF.Ext.CrudPanel，则返回其中的grid。如果主面板不是gridPanel，但是其grid属性对应了一个grid，则返回该grid。
     * 
     * @return {Object} grid 返回主面板中能访问到的grid对象。用于传入节点参数
     */
    getGrid: function() {
        if (this.panel instanceof Ext.grid.GridPanel)
            return this.panel;
        else if (this.panel instanceof Disco.Ext.CrudPanel) {
            return this.panel.grid;
        } else if (this.panel && this.panel.grid) {
            return this.panel.grid;
        }
    },
    /**
     * 创建树面板对象。
     * 
     * @return {Object} 返回创建好的树面板对象。
     */
    createTree: function() {
        var cfg = Ext.apply({}, this.treeCfg, this.defaultstreeCfg);
        var rootId = cfg.rootId;
        var rootText = cfg.rootText;
        var dataUrl = cfg.dataUrl;
        var treeLoader = cfg.loader;
        var iconCls = cfg.rootIconCls;
        var expanded = cfg.expanded;

        delete cfg['rootId'], delete cfg['rootText'], delete cfg['dataUrl'], delete cfg['loader'], cfg['rootIconCls'];

        var tree = new Ext.tree.TreePanel(Ext.apply({
            tools: [{
                id: "refresh",
                handler: function() {
                    this.tree.root.reload();
                },
                scope: this
            }],
            style: 'padding-right:1px;padding-top:1px;',
            root: new Ext.tree.AsyncTreeNode({
                expanded: expanded,
                id: rootId,
                iconCls: iconCls,
                text: rootText
            }),
            loader: treeLoader || new Ext.tree.TreeLoader({
                dataUrl: dataUrl
            })
        }, cfg));
        tree.on({
            scope: this,
            click: this.onTreeClick
        });
        return tree;
    },
    /**
     * 当点击了树节点后，重新加载主面板中列表数据
     * 
     * @param {Object}
     *            id 传入节点id
     * @param {Object}
     *            node 选中的节点数据。
     */
    reloadGrid: function(id, node) {
        var g = this.getGrid();
        if (g) {
            g.getStore().baseParams[this.queryParam] = (id == 'root') ? null : id;
            g.getStore().load();
        }
    },
    /**
     * 当点击了树节点后的响应事件<br/> 默认为调用reloadGrid方法来刷新主面板中列表的数据。<br/>
     * 子类可以通过重写该方法来提供自己的响应事件。
     * 
     * @param {Object}
     *            node 点击的节点对象
     * @param {Ext.EventObject}
     *            eventObject 点击事件。
     */
    onTreeClick: function(node, eventObject) {
        this.reloadGrid(node.id, node);
    },
    // private
    beforeDestroy: function() {
        Ext.destroy(this.panel, this.tree);
        delete this.panel;
        delete this.tree;
        Disco.Ext.TreeCascadePanel.superclass.beforeDestroy.call(this);
    },
    initComponent: function() {
        this.tree = this.createTree();
        this.items = [this.tree, Ext.apply(this.getPanel(), {
            flex: 1
        })];
        Disco.Ext.TreeCascadePanel.superclass.initComponent.call(this);
    }
});

/**
 * @class Disco.Ext.CrudFunction
 * 
 * 基础的添删改查业务支持类。<br/> 该类支持CrudPanel和CrudListPanel后端业务实现。
 */
Disco.Ext.CrudFunction = {
    /**
     * @cfg {Boolean} exportData 业务是否支持导出Excel功能（是否显示导出excel按钮）。默认不显示。
     */
    exportData: false,
    /**
     * @cfg {Boolean} importData 业务是否支持导入excel数据功能（是否显示导入excel按钮）。默认不显示。
     */
    importData: false,
    /**
     * @cfg {Boolean} printData 业务是否支持列表打印功能（是否显示打印列表按钮）。默认不显示。
     */
    printData: false,
    /**
     * @cfg {Boolean} clearData 业务是否支持清除上次查询功能（是否显示清除查询按钮）。默认不显示。
     */
    clearData: false,
    /**
     * @cfg {Boolean} allowSearch 业务是否支持查询功能（是否显示查询按钮）。默认支持。
     */
    allowSearch: true,
    /**
     * @cfg {Boolean} showMenu 业务是否支持在列表页中支持右键功能菜单。默认支持。
     */
    showMenu: true,
    /**
     * 
     * @cfg {Boolean} showTbar 业务是否支持显示数据Grid顶部工具栏。默认支持。
     */
    showTbar: true,
    /**
     * @cfg {Boolean} showAdd 业务是否支持添加功能（是否显示添加按钮）。默认支持。
     */
    showAdd: true,
    /**
     * @cfg {Boolean} showAdd 业务是否支持编辑功能（是否显示编辑按钮）。默认支持。
     */
    showEdit: true,
    /**
     * @cfg {Boolean} showRemove 业务是否支持删除功能（是否显示删除按钮）。默认支持。
     */
    showRemove: true,
    /**
     * @cfg {Boolean} showView 业务是否支持查看明细功能（是否显示查看按钮）。默认支持。
     */
    showView: true,
    /**
     * @cfg {Boolean} showRefresh 业务是否支持页面列表刷新功能（是否显示刷新按钮）。默认支持。
     */
    showRefresh: true,
    /**
     * @cfg {Boolean} showSearchField 业务是否支持内容查询功能（是否显示内容查询组件）。默认支持。
     */
    showSearchField: true,
    /**
     * 
     * @cfg {Object} defaultsActions 配置Crud对应的方法 
     */
    defaultsActions: {
        create: 'save',
        list: 'list',
        view: 'view',
        update: 'update',
        remove: 'remove'
    },
    /**
     * @cfg {Boolean} batchRemoveMode 业务是否支持批量删除模式。默认支持。
     */
    batchRemoveMode: false,
    /**
     * @cfg {Boolean} autoLoadGridData 是否在列表加载后自动加载表格数据。默认支持。
     */
    autoLoadGridData: true,
    /**
     * @cfg {Boolean} columnLock 是否支持表格列锁定。默认不支持。
     */
    columnLock: false,
    /**
     * @cfg {Boolean} summaryGrid 是否开启列表统计功能。默认不支持。
     */
    summaryGrid: false,
    /**
     * @cfg {Boolean} dirtyFormCheck 是否自动检查编辑表单中的数据项已经修改。默认支持。
     */
    dirtyFormCheck: true,
    /**
     * @cfg {Integer} operatorButtonStyle 添删改查页面功能按钮的显示样式。有以下可选项：
     *      <ul>
     *      <li>1:为图文混合</li>
     *      <li>2:为只显示文字</li>
     *      <li>3:为只显示图标</li>
     *      </ul>
     */
    operatorButtonStyle: 1,
    /**
     * @cfg {Boolean} customizeQueryObject 业务是否支持自定义查询，默认不支持。
     */
    customizeQueryObject: false,
    /**
     * @cfg {String} queryObjectName 如果开启了支持自定义条件查询，设置自定义查询对象的名称。
     */
    queryObjectName: null,
    /**
     * @cfg {Integer} winWidth 默认的【添加/修改/查看】窗口的宽度
     */
    winWidth: 500,
    /**
     * @cfg {Integer} winHeight 默认的【添加/修改/查看】窗口的高度
     */
    winHeight: 400,
    /**
     * @cfg {Integer} winHeight 默认的【添加/修改/查看】窗口的名称
     */
    winTitle: "数据管理",
    /**
     * @cfg {Integer} pageSize 业务列表中默认的分页页数。
     */
    pageSize: 10,
    /**
     * @cfg {Boolean} pagingToolbar 在业务列表中是否显示分页工具栏。
     */
    pagingToolbar: true,
    /**
     * @cfg {Boolean} defaultCmd
     *      是否使用默认的list命令。如果是true，列表的命令是this.baseUrl+'cmd=list'，如果是false，列表直接使用baseUrl。
     */
    defaultCmd: true,
    /**
     * @cfg {Function} viewSave 用来处理视图查看时，保存按钮的回调函数。
     */
    viewSave: Ext.emptyFn,
    initComponent: Ext.emptyFn,
    /**
     * @cfg {linkRender} linkRender
     *      默认的linkRender，方便在grid中直接使用renderer:this.linkRender。
     */
    linkRender: Disco.Ext.Util.linkRenderer,
    /**
     * @cfg {imgRender} imgRender
     *      默认的imgRender，方便在grid中直接使用renderer:this.imgRender。
     */
    imgRender: Disco.Ext.Util.imgRender,
    /**
     * @cfg {booleanRender} booleanRender
     *      默认的booleanRender，方便在grid中直接使用renderer:this.booleanRender。
     */
    booleanRender: Disco.Ext.Util.booleanRender,
    /**
     * @cfg {dateRender} dateRender
     *      默认的dateRender，方便在grid中直接使用renderer:this.dateRender。
     */
    dateRender: Disco.Ext.Util.dateRender,
    /**
     * @cfg {objectRender} objectRender
     *      默认的objectRender，方便在grid中直接使用renderer:this.objectRender。
     */
    objectRender: Disco.Ext.Util.objectRender,
    /**
     * @cfg {typesRender} typesRender
     *      默认的typesRender，方便在grid中直接使用renderer:this.typesRender。
     */
    typesRender: Disco.Ext.Util.typesRender,
    /**
     * @cfg {readOnlyRender} readOnlyRender
     *      默认的readOnlyRender，方便在grid中直接使用renderer:this.readOnlyRender。
     */
    readOnlyRender: Disco.Ext.Util.readOnlyRender,
    /**
     * @cfg {operaterRender} operaterRender
     *      默认的operaterRender，方便在grid中直接使用renderer:this.operaterRender。
     */
    operaterRender: Disco.Ext.Util.operaterRender,
    /**
     * 创建远程Combox,参见Disco.Ext.Util.buildRemoteCombox
     */
    buildRemoteCombox: Disco.Ext.Util.buildRemoteCombox,
    /**
     * @cfg {Boolean} singleWindowMode 是否使用单例窗口模式。<br/>
     *      在CrudPanel或者CrudListPanel中，点击【添加/修改/查看】按钮时弹出的窗口使用的都是GlobalWindow。<br/>
     *      如果在一个添加窗口中要使用Disco.Ext.Util.addObject/Disco.Ext.Util.editObject/Disco.Ext.Util.viewObject来打开<br/>
     *      其他的模块的【添加/修改/查看】窗口，就会使用对应模块的panel来覆盖GlobalWindow的内容。<br/>
     *      所以，如果在两个互相调用的模块，必须设置某一方的singleWindowMode为true。
     */
    singleWindowMode: false,
    /**
     * @type {String}  gridSelModel grid的选择模式(checkbox/row)<br/>
     * checkbox使用Ext.grid.CheckBoxSelectionModel<br/>
     * row使用Ext.grid.RowSelectionModel<br/>
     */
    gridSelModel: 'row',
    /**
     * @type Boolean gridRowNumberer grid是否是否显示序号列<br/>
     */
    gridRowNumberer: false,
    booleans: [["是", true], ["否", false]],
    storage: null,
    // private
    crud_operators: [{
        name: "btn_add",
        text: "添加(<u>A</u>)",
        iconCls: 'add',
        method: "create",
        cmd: "save",
        noneSelectRow: true,
        hidden: true
    }, {
        itemId: "btn_edit",
        name: "btn_edit",
        text: "编辑(<u>E</u>)",
        iconCls: "edit",
        disabled: true,
        method: "edit",
        cmd: "update",
        hidden: true
    }, {
        name: "btn_view",
        text: "查看(<u>V</u>)",
        iconCls: "view",
        method: "view",
        disabled: true,
        hidden: true
    }, {
        name: "btn_remove",
        text: "删除(<u>D</u>)",
        iconCls: "delete",
        disabled: false,
        method: "removeData",
        cmd: "remove",
        hidden: true
    }, {
        name: "btn_refresh",
        itemId: "btn_refresh",
        text: "刷新",
        iconCls: "refresh",
        method: "refresh",
        noneSelectRow: true
    }, {
        name: "btn_advancedSearch",
        text: "高级查询(<u>S</u>)",
        iconCls: "srsearch",
        method: "advancedSearch",
        cmd: "list",
        hidden: true,
        noneSelectRow: true,
        clientOperator: true
    }, {
        name: "btn_clearSearch",
        text: "显示全部",
        cls: "x-btn-text-icon",
        iconCls: "search",
        noneSelectRow: true,
        method: "clearSearch",
        hidden: true
    }, {
        name: "btn_print",
        text: "打印(<u>P</u>)",
        iconCls: "print-icon",
        disabled: true,
        method: "printRecord",
        hidden: true
    }, {
        name: "btn_export",
        text: "导出Excel(<u>O</u>)",
        iconCls: 'export-icon',
        method: "exportExcel",
        noneSelectRow: true,
        hidden: true
    }, {
        name: "btn_import",
        text: "导入数据(<u>I</u>)",
        iconCls: 'import-icon',
        method: "importExcel",
        noneSelectRow: true,
        hidden: true
    }, '->', {
        type: "searchfield",
        name: "searchField",
        width: 100,
        noneSelectRow: true,
        paramName: 'searchKey',
        clientOperator: true
    }],
    // private
    objectAutoRender: function(v) {
        if (v && v.id) {
            for (var d in v) {
                if (d != "id" && v[d])
                    return v[d];
            }
            return v.id;
        } else
            return v;
    },
    // private
    search: function() {
        Ext.apply(this.store.baseParams, {
            searchKey: this.searchField ? this.searchField.getValue() : ""
        });
        if (this.store.lastOptions && this.store.lastOptions.params) {
            this.store.lastOptions.params.start = 0;
            this.store.lastOptions.params.pageSize = this.store.baseParams.pageSize || this.pageSize;
        }
        this.refresh(false);
    },
    formatUrl: function(cmd) {
        return Disco_RIA.formatUrl(this.baseUrl, cmd);
    },
    importExcel: function() {
        if (!Disco.Ext.ImportPanel) {
            Disco.Ext.ImportPanel = new Ext.form.FormPanel({
                id: "crudExportPanel",
                fileUpload: true,
                items: [{
                    xtype: "fieldset",
                    title: "选择数据文件",
                    autoHeight: true,
                    items: {
                        xtype: "textfield",
                        hideLabel: true,
                        inputType: "file",
                        name: "file",
                        anchor: "100%"
                    }
                }, {
                    xtype: "fieldset",
                    title: "导入说明",
                    html: this.importExplain || "",
                    height: 120
                }]
            });
        }

        var win = this.createGlobalWin("CrudExportWindow", 500, 310, "导入数据", Disco.Ext.ImportPanel, function() {
            var form = Disco.Ext.ImportPanel.form;
            if (form.findField("file").getValue().length < 2) {
                Ext.Msg.alert("提示", "你没有选择要导入的文件！");
                return;
            }
            Disco.Ext.ImportPanel.form.submit({
                url: this.baseUrl,
                params: {
                    cmd: "import"
                },
                waitMsg: "请稍候，正在导入数据",
                success: function() {
                    Ext.Msg.alert("提示", "数据导入成功!", function() {
                        form.findField("file").reset();
                        win.hide();
                        this.store.reload();
                    }, this)
                },
                failure: function(form, action) {
                    Disco_RIA.ajaxErrorHandler(action.result, "数据导入出错，请检测所选择的文件格式是否正确?");
                },
                scope: this
            })
        });
    },
    getExportForm: Disco.Ext.Util.getExportForm,
    /**
     * 导出数据为Excel格式
     */
    exportExcel: function() {
        var params = this.store.baseParams;
        Ext.apply(params, {
            searchKey: this.searchField ? this.searchField.getValue() : ""
        });
        /*
         * var s = Ext.urlEncode(params); window.open(this.baseUrl +
         * '?cmd=export&' + s);
         */
        var exportForm = this.getExportForm();
        exportForm.form.submit({
            url: this.baseUrl,
            params: Ext.apply({
                cmd: "export"
            }, this.store.baseParams)
        });
    },
    printList: function(cmd) {
        return function() {
            var params = Ext.apply(this.store.baseParams, {
                cmd: (cmd ? cmd : "printList")
            });
            var s = Ext.urlEncode(params);
            window.open(this.baseUrl + "?" + s);
        }
    },
    printRecord: function() {
        var record = this.grid.getSelectionModel().getSelected();
        if (!record) {
            this.alert("请先选择要操作的数据！", "提示信息");
            return false;
        }
        window.open(this.baseUrl + "?cmd=print&id=" + record.get("id"));
    },
    clearSearch: function() {
        this.store.baseParams = {};
        this.store.removeAll();
        this.store.load({
            params: {
                start: 0,
                pageSize: this.pageSize
            }
        });
        // this.refresh();
    },
    /**
     * 重新加载列表中的数据。<br/> 可以重写该方法，在refresh方法调用之前向store添加查询条件。<br/>
     * 如果有自定义的列表上按钮，在refresh方法后，还需要调用disableOperaterItem方法来禁用或启用按钮。<br/>
     */
    refresh: function(reload) {
        this.store.removeAll();
        var callback = function(rs) {
            if (rs && rs.length < 1) {
                this.alert("没有符合条件的数据！", "提示信息");
                Disco.Ext.Util.autoCloseMsg.defer(2000);
            }
        };

        var cfg = {
            callback: callback,
            scope: this
        };
        this.store[reload !== false ? "reload" : "load"](cfg);
        this.disableOperaterItem("btn_remove", "btn_edit", "btn_view", "btn_print");
        this.focus();
    },
    // private
    initWin: function(width, height, title, callback, autoClose, resizable, maximizable) {
        this.winWidth = width;
        this.winHeight = height;
        this.winTitle = title;
        var winName = autoClose ? "CrudEditNewWindow" : "CrudEditWindow";
        if (this.singleWindowMode)
            winName = winName + this.id;
        var win = this.createGlobalWin(winName, width, height, title, this.fp, null, "fp", [{
            itemId: "btnSave",
            text: "保存(<u>K</u>)",
            handler: function() {
                Disco.Ext[winName].crudService.save(callback, autoClose);
            },
            iconCls: 'save',
            scope: this
        }, {
            itemId: "btnReset",
            text: "重置(<u>R</u>)",
            iconCls: 'clean',
            handler: function() {
                Disco.Ext[winName].crudService.reset()
            },
            scope: this
        }, {
            itemId: "btnClose",
            text: "取消(<u>X</u>)",
            iconCls: 'delete',
            handler: function() {
                Disco.Ext[winName].crudService.closeWin(autoClose)
            },
            scope: this
        }], autoClose, resizable ? true : false, maximizable ? true : false);
        win.confirmSave = this.dirtyFormCheck;
        return win;
    },
    // private
    getViewWin: function(autoClose) {
        var viewWin = Ext.isFunction(this.viewWin) ? this.viewWin() : this.viewWin;
        var width = viewWin.width;
        var height = viewWin.height;
        var title = viewWin.title;
        return this.createGlobalWin("CrudViewWindow", width, height, title, this.viewPanel, function() {
            var w = Disco.Ext.CrudViewWindow;
            if (w.crudService)
                w.crudService.viewSave(this.viewPanel);
            w.hide();
        }, "viewPanel", null, autoClose);
    },
    /**
     * 封装的可以对某一条业务数据完成一组方法的方法。<br/> 包装的方法流程：<br/>
     * <ul>
     * <li>1，必须选中一条业务数据</li>
     * <li>2，打开一个窗口，把业务数据传入窗口的表单中</li>
     * <li>3，提交窗口后关闭窗口，并刷新列表</li>
     * </ul>
     * 
     * <pre>
     * <code>
     * 	//需求：在产品列表中选中某一个产品，点击【添加完整备注】按钮，弹出窗口，包含CkEditor用于添加大量的备注信息。
     * 	//设计思路：在企业级产品应用中，处于后台数据库性能设计和前台页面render性能设计，大文本属性都会独立于业务对象，在前台也往往采用分离的独立窗口添加。
     * 	//实现，首先在toolbar中加入按钮
     * 	{xtype:'button',text:'添加完整备注',scope:this,handler:this.addBigInfo},
     * 	//addBigInfo方法:
     * 	addBigInfo:function(){
     * 	Disco.Ext.Util.doSomeWork(500,400,
     * 			'添加大备注',
     * 			'bigIntroAddPanel',
     * 			'createBigIntroAddPanel',
     * 			'addBigIntro',
     * 			'GlobalBigIntroAddWin');
     * 	}
     * 	//创建大备注panel方法：
     * 	createBigIntroAddPanel:function(){
     * 	var fp=//...;
     * 	return fp;
     * 	}
     * </code>
     * </pre>
     * 
     * @param {Integer}
     *            width 弹出窗口的宽度
     * @param {Integer}
     *            height 弹出窗口的高度
     * @param {String}
     *            title 弹出窗口的标题
     * @param {String}
     *            panel
     *            在本CrudFunction实例中缓存的弹出窗体中表单的名称。CrudFunction使用this[panel]来查找相同的panel实例。
     * @param {String}
     *            createPanel
     *            在this作用域中定义的用来创建弹出窗口中表单的方法名称。所以在示例中，如果第一次调用该方法，就会使用createBigIntroAddPanel方法来创建表单。
     * @param {String}
     *            cmd 弹出窗口中表单提交到后台的cmd命令名称。
     * @param {String}
     *            workWinName 全局缓存的窗口名称
     * @param {String}
     *            url 弹出窗口中表单提交到后台的url路径。默认使用this.baseUrl。
     * @parma {Boolean} autoClose 在完成窗口中表单提交后，是否自动关闭弹出窗口。
     * 
     */
    doSomeWork: function(width, height, title, panel, createPanel, cmd, workWinName, url, autoClose) {
        var record = this.grid.getSelectionModel().getSelected();
        if (!record) {
            this.alert("请先选择要操作的数据！", "提示信息");
            return false;
        }
        if (!this[panel])
            this[panel] = this[createPanel]();
        var win = this.getWorkWin(width, height, title, this[panel], function() {
            if (!this[panel].form.isValid())
                return false;
            this[panel].form.submit({
                url: url ? url : this.baseUrl,
                waitMsg: "正在执行操作，请稍候",
                params: {
                    cmd: cmd
                },
                success: function() {
                    win.hide();
                    this.refresh()
                },
                failure: function(form, action) {
                    var msg = "";
                    if (action.failureType == Ext.form.Action.SERVER_INVALID) {
                        for (var p in action.result.errors) {
                            msg += action.result.errors[p] + "&nbsp;";
                        }
                    } else
                        msg = "数据录入不合法或不完整！";
                    Disco.Ext.Msg.alert(msg, "保存失败!");
                },
                scope: this
            });
        }, panel, workWinName, autoClose);
        return win;

    },
    /**
     * 根据参数，创建一个工作窗口。<br/> 该窗口使用缓存机制。<br/> 使用示例请参考doSomeWork方法的实现方式。
     * 
     * @param {Integer}
     *            width 创建窗口的宽度
     * @param {Integer}
     *            height 创建窗口的高度
     * @param {String}
     *            title 创建窗口的标题
     * @param {Object}
     *            workPanel 在创建的窗口中使用的panel实例。
     * @param {Function}
     *            save 点击窗口上【保存】按钮，调用的回调方法。
     * @param {String}
     *            pname 在this域中保存的该panel示例缓存名称。
     * @param {String}
     *            workWinName 全局缓存的窗口名称
     * @parma {Boolean} autoClose 在完成窗口中表单提交后，是否自动关闭弹出窗口。
     */
    getWorkWin: function(width, height, title, workPanel, save, pname, winName, autoClose) {
        var record = this.grid.getSelectionModel().getSelected();
        if (!record) {
            this.alert("请先选择要操作的数据！", "提示信息");
            return false;
        }
        var id = record.get("id");
        var win = this.createGlobalWin(winName ? winName : "CrudWorkWindow", width, height, title, workPanel, save, pname, null, autoClose);
        // 显示数据
        for (var n in record.data) {
            var c = win.getComponent(0).findSomeThing(n);
            // if(!c &&
            // win.getComponent(0).getXType()=="form")c=win.getComponent(0).form.findField(n);
            if (c) {
                var v = record.get(n);
                if (c.isFormField) {
                    c.setValue(v);
                    c.clearDirty();
                } else {
                    if (c.renderer)
                        v = c.renderer(v);
                    if (c.setText)
                        c.setText(v);
                    else if (c.getXType && c.getXType() == "panel")
                        c.body.update(v);
                }
            }
        }
        return win;
    },
    /**
     * 简单的alert方法封装。
     * 
     * @param {String}
     *            msg 提示的内容。
     * @param {String}
     *            title 提示的标题。可选，默认为'提示'。
     */
    alert: function(msg, title) {
        Disco.Ext.Msg.alert(msg, title || "提示", function() {
            this.focus();
        }, this)
    },
    /**
     * 简单的confirm方法封装。
     * 
     * @param {String}
     *            title 确认的标题
     * @param {String}
     *            msg 确认的内容
     * @param {Function}
     *            callback 点击确认提示窗口中yes按钮后的回调方法。
     */
    confirm: function(title, msg, callback) {
        Ext.Msg.confirm(title, msg, function(btn) {
            if (btn == "yes") {
                callback();
            } else {
                this.focus();
            }
        }, this);
    },
    // private
    focusFirstField: function(fp, win) {
        fp = fp || this.fp;
        win = win || this.win;
        fp = win.findBy(function(cmp) {
            if (cmp instanceof Ext.FormPanel) {
                return true;
            }
        }, this);
        win.currentFocus = false;
        if (fp && fp[0].form.items) {
            fp[0].form.items.each(function(o) {
                if (o.canFocus()) {
                    o.focus("", 10);
                    win.currentFocus = o;
                    return false;
                }
            });
        }
        if (!win.currentFocus) {
            if (win.buttons && win.buttons.length) {
                win.buttons[0].focus("", 10);
                win.currentFocus = win.buttons[0];
            }
        }
    },
    // private
    // 得到一个全局的操作窗口，参数width表示窗口宽度，height表示窗口高度，title表示窗口标题，workPanel表示窗口名称，save表示回调函数，作用域为this,pname表示属性名称，比如viewPanel
    createGlobalWin: function(winName, width, height, title, workPanel, save, pname, buttons, autoClose, resizable, maximizable) {
        var win = Disco.Ext[winName];
        if (!win) {
            this[pname ? pname : workPanel.id] = workPanel;
            var tools = [];
            tools.push({
                id: "help",
                handler: this.help
            });
            //bank_cms缓存功能
            if (this.storage) {
                this.storage.scope = this;
                buttons.push(this.storage);
            }
            Disco.Ext[winName] = new Disco.Ext.FormWindow({
                width: width,
                layout: 'fit',
                border: false,
                resizable: resizable,
                height: height,
                buttonAlign: "center",
                title: title,
                modal: true,
                defaultButton: 0,
                shadow: true,
                maximized: false,
                maximizable: maximizable,// this.enableMaxime,
                // constrain:true,
                tools: tools,
                closeAction: autoClose || this.singleWindowMode ? "close" : "hide",
                autoClose: autoClose || this.singleWindowMode,
                listeners: {
                    close: function(win) {
                        if (win.crudService.store && win.crudService.closeSaveWin === false && winName != "CrudSearchWindow") {
                            win.crudService.store.reload();
                        }
                        win.crudService.focusCrudGrid();
                        delete Disco.Ext[winName];
                    },
                    maximize: function(win) {
                        if (win.maximizable) {
                            win.doLayout();
                            win.maximized = true
                        }
                    },// 如果需要实现可最大化和最小化，就要重写maximize和restore事件
                    restore: function(win) {
                        if (win.maximizable) {
                            win.doLayout();
                            win.maximized = false
                        }
                    },
                    show: function(win) {
                        if (win.maximizable)
                            win.tools[win.maximized ? "maximize" : "restore"].setVisible(win.crudService.maximizable === true);
                        win.tools.help.setVisible(win.crudService.showHelp != undefined);
                        var fp = win.findByType(Ext.FormPanel);
                        win.crudService.focusFirstField(fp, win);
                    },
                    hide: function(win) {
                        if (win.crudService.store && win.crudService.closeSaveWin === false && winName != "CrudSearchWindow") {
                            win.crudService.store.reload();
                        }
                        win.crudService.focus();
                    }
                },
                items: [workPanel],
                buttons: buttons ? buttons : [{
                    itemId: "btnSave",
                    text: "确定(<u>K</u>)",
                    handler: function() {
                        var w = Disco.Ext[winName];
                        var h = true;
                        if (save)
                            h = save.call(w.crudService, autoClose);
                        if (h) {
                            if (autoClose)
                                w.close();
                            else
                                w.hide();
                        }
                    },
                    iconCls: 'save',
                    scope: this
                }, {
                    itemId: "btnClose",
                    text: "退出(<u>X</u>)",
                    iconCls: 'delete',
                    handler: function() {
                        if (autoClose || this.singleWindowMode)
                            Disco.Ext[winName].close();
                        else
                            Disco.Ext[winName].hide();
                    },
                    scope: this
                }],
                keys: [{
                    key: "k",
                    alt: true,
                    stopEvent: true,
                    fn: function() {
                        Disco.Ext.Util.executePanelButtons(win, "btnSave");
                    }
                }, {
                    key: "x",
                    stopEvent: true,
                    alt: true,
                    fn: function() {
                        Disco.Ext.Util.executePanelButtons(win, "btnClose");
                    }
                }, {
                    key: "r",
                    stopEvent: true,
                    alt: true,
                    fn: function() {
                        Disco.Ext.Util.executePanelButtons(win, "btnReset");
                    }
                }]
            });
            win = Disco.Ext[winName];
        } else if (workPanel) {// 更改窗口中的内容，包括全局的事件响应函数等
            if (win.crudService != this) {
                // win.resizable = resizable;
                // win.maximizable=maximizable;
                // if(win.maximizable&&win.tools["restore"])win.tools["restore"].setVisible(false);
            }

            win.setTitle(title);
            win.setWidth(width);
            win.setHeight(height);
            if (win.getComponent(0) != workPanel) {
                var p = win.remove(0);
                delete win.crudService[pname ? pname : p.id];// 删除上一个
                win.add(workPanel);
                this[pname ? pname : workPanel.id] = workPanel;
                win.doLayout();
            }
        }
        win.crudService = this; // crudService用来定义全局的添删改查服务
        this[winName] = win;
        win.show((typeof main != "undefined") && Disco_RIA.getCfg('enableAnimate') ? Ext.getBody() : false, function() {
            win.center();
        }, this);
        if (height == 'auto') {
            win.autoHeight = true;
            win.syncSize.defer(1000, win);
        } else {
            win.autoHeight = false;
        }
        return win;
    },
    /**
     * @cfg {Function} createViewPanel
     *      在执行查看业务的时候，如果配置了createViewPanel方法，则会调用该方法返回的面板作为弹出的查看窗口中的查看面板。<br/>
     *      如果没有配置createViewPanel方法，则使用编辑窗口来作为查看窗口面板。
     * 
     * @return {Ext.Panel} fp 返回创建的查看面板。
     */
    /**
     * @cfg {Function} createForm
     *      在执行添加和编辑业务的时候，必须要配置createForm方法，CrudFunction服务会调用该方法返回的面板作为弹出的添加和编辑窗口中的表单。<br/>
     * 
     * @return {Ext.Panel} fp 返回创建的添加/编辑表单面板。
     */
    // private
    showWin: function() {
        if (!this.fp) {
            if (!this.createViewPanel && this.viewPanel) {
                this.fp = this.viewPanel;
            } else {
                this.fp = this.createForm();
            }
        }
        this.win = this.createWin();
        this.win.on("close", function() {
            delete this.win;
            delete this.fp;
        }, this);
        this.win.show((typeof main != "undefined") && window.Global.Config.enableAnimate ? Ext.getBody() : false, function() {
            this.win.center();
        }, this);
    },
    /**
     * @cfg {Function} onCreate 该方法在添加窗口及表单创建完成后调用的钩子方法。<br/>
     *      一般子类可以覆盖该方法并在里面执行添加业务对象的初始化设置工作。<br/> 比如常见的设置默认值，加载单据编号等。
     */
    onCreate: function() {
    },
    // private
    create: function() {
        this.showWin();
        this.fp.form.clearData();
        this.reset();
        this.fp.form.isValid();
        this.onCreate();
        this.beingCreate = true;
        this.formFocus.defer(500, this);
    },
    // private
    createObject: function(callback, winReadyAction) {
        this.fp = this.createForm();
        this.win = this.createWin(callback, true);
        this.win.on("close", function() {
            delete this.win;
            delete this.fp;
        }, this);
        this.win.show((typeof main != "undefined") && Disco_RIA.getCfg('enableAnimate') ? Ext.getBody() : false, function() {
            this.win.center();
        }, this);
        this.reset();
        if (winReadyAction) {
            winReadyAction(this.win, this);
        }
        this.fp.form.isValid();
        this.onCreate();
        this.beingCreate = true;
        this.formFocus.defer(500, this);
    },
    /**
     * @cfg {Function} onEdit 该方法在编辑窗口及编辑表单创建，并设置完初始值完成后调用的钩子方法。<br/>
     *      一般子类可以覆盖该方法并在里面执行自定的编辑对象参数设置工作。<br/>
     *      比如常见的复杂组件值设置，不可编辑组件disabled等动作。
     * 
     * @param {Boolean}
     *            ret 如果当前选中的业务对象record是否有id值。
     * @param {Object}
     *            data 当前选中的业务对象record的data值。
     */
    onEdit: function(ret, data) {

    },
    // private
    editObject: function(id, callback, winReadyAction) {
        this.fp = this.createForm();
        this.beingCreate = true;
        this.win = this.createWin(callback, true);
        this.win.on("close", function() {
            delete this.win;
            delete this.fp;
        }, this);
        this.win.show((typeof main != "undefined") && Disco_RIA.getCfg('enableAnimate') ? Ext.getBody() : false, function() {
            this.win.center();
        }, this);
        var viewCmd = this.viewCmd || "view";
        Ext.Ajax.request({
            url: this.baseUrl + "?cmd=" + viewCmd,
            params: {
                id: id
            },
            waitMsg: "正在加载数据,请稍侯...",
            callback: function(options, success, response) {
                var r = Ext.decode(response.responseText);
                this.fp.form.setValues(r);
                if (winReadyAction) {
                    winReadyAction(this.win, r, this);
                }
                this.onEdit(true, r);
                this.fp.form.isValid();
                this.formFocus.defer(500, this);
                this.fp.form.clearDirty();
            },
            scope: this
        });
    },
    /**
     * 编辑业务对象方法。<br/>
     * <ul>
     * 该方法的执行流程为：
     * <li>1，如果设置showEdit为false或者编辑按钮当前状态为disable，直接调用查看逻辑。</li>
     * <li>2，得到当前业务对象列表中选定的record。</li>
     * <li>3，调用showWin方法，创建编辑窗口及表单</li>
     * <li>4，把record的值设置到编辑表单中</li>
     * <li>5，调用onEdit方法。</li>
     * <li>6，调用formFocus方法。</li>
     * </ul>
     */
    edit: function(e) {
        if (e && e.getTarget && e.getTarget('div.x-grid3-row-checker')) {
            return false;
        };

        if (this.btn_edit && this.btn_edit.disabled) {
            this.view();
            return false;
        }
        var record = this.grid.getSelectionModel().getSelected();
        if (!record) {
            this.alert("请先选择要操作的数据！", "提示信息");
            return false;
        }
        var id = record.get("id");
        this.beingCreate = false;
        this.showWin();
        this.fp.form.reset();
        this.fp.form.loadRecord(record);
        this.onEdit(id, record.data);
        this.fp.form.clearDirty();
        this.formFocus.defer(500, this);
        return true;
    },
    // private
    removeObject: function(id, callback) {
        this.confirm("删除确认", "确定要删除吗？", function(ret) {
            Ext.Ajax.request({
                url: this.baseUrl + '?cmd=remove',
                params: {
                    'id': id
                },
                method: 'POST',
                success: function(response) {
                    var r = Ext.decode(response.responseText);
                    if (!r.success) {
                        Disco_RIA.ajaxErrorHandler(r);
                    } else {
                        this.alert("删除成功", "提示信息");
                        if (callback)
                            callback();
                    }
                },
                scope: this
            });
        }.createDelegate(this), this);
    },
    /**
     * 在添加或者编辑窗口中，让表单的第一个组件获得焦点。<br/> 默认情况下从第一个元素开始得到焦点。<br/>
     * 子类可以覆盖该方法让焦点定在某一个指定组件上。<br/>
     * 比如在添加/修改单据的表单中，第一个组件往往是系统自动生成的编号组件，一般不允许修改，就需要覆盖该方法，让焦点定在第二个组件上。
     */
    formFocus: function() {
        var field = this.fp.form.items.get(1);
        if (!field || field.disabled)
            this.items.each(function(f) {
                if (!f.disabled) {
                    field = f;
                    return false;
                }
            });
        if (field && !field.disabled)
            field.focus("", 100);
    },
    // private
    validateForm: function(form) {
        if (!form.isValid()) {
            this.alert("表单数据不合法,请注意必填项及录入的数据格式!", "提示", function() {
                this.formFocus();
            }, this);
            return false;
        }
        return true;
    },
    /**
     * @cfg {function} beforeSave 在点击添加/修改表单保存按钮后，到提交表单数据之前调用的钩子方法。<br/>
     *      一般在该方法中完成获得复杂组件的值，或者拼凑指定提交值的动作。也常用于检查表单中复杂数据合法性或逻辑性的功能。<br/>
     *      比如在主从表单中，需要覆盖该方法，取得列表中的值，并组装成后台可识别的数据。<br/>
     *      在主从表单中，如果从列表中没有添加任何数据，则单据可以不用保存，类似的业务逻辑一般也在该方法中实现。<br/>
     * 
     * @return {Boolean} ret 如果ret为true，则执行表单提交，为false，则放弃表单提交。
     */
    beforeSave: function() {
        return true;
    },
    /**
     * @cfg {function} afterSave 在提交完成添加/修改表单数据后，在得到成功的返回之后调用的钩子方法。
     * 
     * @param {BasicForm}
     *            提交的表单
     * @param {Ext.form.Action}
     */
    afterSave: function(form, action) {

    },
    /**
     * 添加/编辑 表单的保存动作。<br/> private方法，一般子类不需要覆盖该方法。<br/> 在特殊情况下子类可能会定义自己的保存流程。<br/>
     * 具体流程请参考实现代码。
     * 
     * @param {function}
     *            callback 在完成表单提交成功后调用的回调方法。
     * @param {Boolean}
     *            autoClose 在完成表单提交成功后是否自动关闭添加/编辑窗口。
     * @param {Boolean}
     *            ignoreBeforeSave
     *            是否在表单提交流程中，忽略beforeSave方法。true为不执行beforeSave方法。默认执行该方法。<br/>
     *            子类有时会在一定的情况下忽略beforeSave方法中的某些业务逻辑检查，就可以设置为false。
     */
    save: function(callback, autoClose, ignoreBeforeSave) {
        if (!this.validateForm(this.fp.form))
            return false;
        if (ignoreBeforeSave !== true) {
            if (this.beforeSave && this.beforeSave() === false)
                return false;
        }
        var id = this.fp.form.findField("id").getValue();
        var url = this.baseUrl;
        if (this.fp.form.fileUpload) {
            var cmd = this.fp.form.findField("cmd");
            if (cmd == null) {
                cmd = new Ext.form.Hidden({
                    name: "cmd"
                });
                this.fp.add(cmd);
                this.fp.doLayout();
            }
            cmd.setValue((this.beingCreate ? "save" : (id ? "update" : "save")));
        } else {
            url = this.formatUrl((this.beingCreate ? "save" : (id ? "update" : "save")));
        }
        Disco.Ext.Util.submitForm(this.fp.form, url, function(form, action) {
            this.fp.form.clearDirty();
            if (this.closeSaveWin !== false)
                this.closeWin(autoClose);
            if (this.store && this.closeSaveWin !== false) {
                this.store.reload();
            }
            if (callback)
                callback();
            this.fireEvent("saveobject", this, form, action);
            this.afterSave(form, action);
            /*
             * if(this.win && this.closeSaveWin===false){ this.formFocus(); }
             */
        }, this);
    },
    /**
     * 打印预览
     * 
     * @param {}
     *            callback
     * @param {}
     *            autoClose
     * @return {Boolean}
     */
    preview: function(callback, autoClose) {
        if (!this.validateForm(this.fp.form))
            return false;
        var id = this.fp.form.findField("id").getValue();
        var url = this.baseUrl;
        if (this.fp.form.fileUpload) {
            var cmd = this.fp.form.findField("cmd");
            if (cmd == null) {
                cmd = new Ext.form.Hidden({
                    name: "cmd"
                });
                this.fp.add(cmd);
                this.fp.doLayout();
            }
            cmd.setValue("preview");
        } else {
            url += "?cmd=preview";
        }
        var tempHiddens = [];
        var ps = this.fp.form.baseParams;
        if (ps && typeof ps == 'string')
            ps = Ext.urlDecode(bp);
        var form = this.fp.form.el.dom;
        function addHiddenKey(key, value) {
            var hd = document.createElement('input');
            hd.type = 'hidden';
            hd.name = key;
            hd.value = value;
            form.appendChild(hd);
            tempHiddens.push(hd);
        }
        for (var k in ps) {
            if (Ext.isArray(ps[k])) {
                for (var i = 0; i < ps[k].length; i++) {
                    addHiddenKey(k, ps[k][i] ? ps[k][i] : "");

                }
            } else if (ps.hasOwnProperty(k)) {
                addHiddenKey(k, ps[k] ? ps[k] : "");
            }
        }
        this.fp.form.el.dom.action = url;
        this.fp.form.el.dom.target = "_blank";
        this.fp.form.el.dom.submit();
        if (tempHiddens) { // 删除动态的参数
            for (var i = 0, len = tempHiddens.length; i < len; i++) {
                Ext.removeNode(tempHiddens[i]);
            }
        }
    },
    /**
     * 重置表单中的数据方法。默认情况下直接调用form.reset方法。<br/>
     * 子类可以覆盖该方法来完成自定义的表单重置功能。比如主从表单中从单据的内容清空等动作。
     * 
     */
    reset: function() {
        if (this.win && this.fp) {
            this.fp.form.reset();
        }
    },
    // private
    closeWin: function(autoClose) {
        if (this.beforeClose) {
            this.beforeClose(function() {
                if (this.win) {
                    if (autoClose)
                        this.win.close();
                    else
                        this.win.hide();
                }
                if (this.store && this.closeSaveWin === false) {
                    // this.store.reload();
                }
            });
        } else {
            if (this.win) {
                if (autoClose)
                    this.win.close();
                else
                    this.win.hide();
            }
            if (this.store && this.closeSaveWin === false) {
                // this.store.reload();
            }
        }
    },
    // private
    removeData: function() {
        if (this.btn_remove.disabled)
            return false;
        var record = this.grid.getSelectionModel().getSelected();
        if (!record) {
            this.alert("请先选择要操作的数据！", "提示信息");
            return false;
        }
        var mulitId = "";
        if (this.batchRemoveMode) {
            var rs = this.grid.getSelectionModel().getSelections();
            for (var i = 0; i < rs.length; i++)
                mulitId += rs[i].get("id") + ",";
        }
        var m = this.confirm("删除确认", "确定要删除吗？", function(ret) {
            Ext.Ajax.request({
                url: this.formatUrl('remove'),
                params: {
                    'id': record.get("id"),
                    'mulitId': mulitId
                },
                method: 'POST',
                success: function(response, options) {
                    var r = Ext.decode(response.responseText);
                    if (r && !r.success)
                        /*Disco.Ext.Msg.error("失败原因为：<br/>"
                        		+ (r.error || r.errors.msg || r.errors.error || "未知"));*/
                        Disco_RIA.ajaxErrorHandler(r);
                    else {
                        Ext.Msg.alert("提示", "删除成功", function() {
                            this.store.removeAll();
                            this.store.reload();
                            this.focus();
                        }, this);
                    }
                    this.fireEvent("removeobject", this, r, options);
                },
                scope: this
            });

        }.createDelegate(this), this);
        return true;
    },
    /**
     * 异步请求的方法包装。
     */
    executeUrl: Disco.Ext.Util.executeUrl,
    /**
     * 悄然的异步请求指定的URL 常用于用户名等AJAX校验
     * @type 
     */
    executeQuietlyUrl: Disco.Ext.Util.executeQuietlyUrl,
    /**
     * 对列表中的一行数据进行执行。<br/>
     * <ul>
     * 执行流程：
     * <li>1，选中列表中的一行。</li>
     * <li>2，执行url，this.formatUrl(cmd);提交的参数还包括选中的id:{id:id}</li>
     * </ul>
     * 
     * @param {String}
     *            cmd 请求后台的cmd名称
     * @param {Boolean}
     *            allowBlank 是否必须选中列表中的一行数据。true为可以不用选择数据。false为必须选中一行数据。
     * 
     */
    executeCmd: function(cmd, allowBlank) {
        return function(c) {
            var sel = this.grid.getSelectionModel();
            var record = sel.getSelectedCell ? (sel.getSelectedCell() ? this.grid.store.getAt(sel.getSelectedCell()[0]) : null) : sel.getSelected();
            if (!c.noneSelectRow) {
                if (!record && !allowBlank) {
                    this.alert("请先选择要操作的数据！");
                    return;
                }
            }
            var mulitId = '', id = record ? record.get("id") : "";
            //批量操作模式
            if (this.batchRemoveMode) {
                var rs = this.grid.getSelectionModel().getSelections();
                for (var i = 0; i < rs.length; i++) {
                    mulitId += rs[i].get("id") + ",";
                }
            }
            Ext.Ajax.request({
                waitMsg: "正在执行操作，请稍候...",
                url: this.formatUrl(cmd),
                params: {
                    'id': id,
                    'mulitId': mulitId
                },
                method: 'POST',
                success: function(response) {
                    var r = Ext.decode(response.responseText);
                    if (!r.success)
                        Disco_RIA.ajaxErrorHandler(r);
                    else {
                        Ext.Msg.alert("提示", r.data ? r.data : "操作成功", function() {
                            this.store.reload();
                            this.focus();
                        }, this);
                    }
                },
                scope: this
            });
        }
    },
    /**
     * 对多条选中的数据执行命令
     * <ul>
     * 执行流程：
     * <li>1，得到列表中所有选中行。</li>
     * <li>2，执行url，this.formatUrl(cmd);提交的参数还包括选中的mulitId:{mulitId:mulitId}</li>
     * </ul>
     * 
     * @param {String}
     *            cmd 请求后台的cmd名称
     */
    executeMulitCmd: function(cmd) {
        return function() {
            var record = this.grid.getSelectionModel().getSelections();
            if (!record || record.length < 1) {
                this.alert("请先选择要操作的数据！");
                return;
            }
            var mulitId = [];
            for (var i = 0; i < record.length; i++) {
                if (record[i].get("id"))
                    mulitId.push(record[i].get("id"))
            }
            Ext.Ajax.request({
                waitMsg: "正在执行操作，请稍候...",
                url: this.formatUrl(cmd),
                params: {
                    'mulitId': mulitId.join(',')
                },
                method: 'POST',
                success: function(response) {
                    var r = Ext.decode(response.responseText);
                    if (!r.success)
                        Disco_RIA.ajaxErrorHandler(r);
                    else {
                        Ext.Msg.alert("提示", r.data ? r.data : "操作成功", function() {
                            this.store.reload();
                            this.focus();
                        }, this);
                    }
                },
                scope: this
            });
        }
    },

    /**
     * @cfg {Function} onView 该方法在查看窗口表单创建，并设置完初始值完成后调用的钩子方法。<br/>
     *      一般子类可以覆盖该方法并在里面执行设置自定的查看对象属性的设置工作。<br/> 比如常见的复杂组件值查看。
     * 
     * @param {Window}
     *            win 查看窗口实例。
     * @param {Object}
     *            data 当前选中的业务对象record的data值。
     */
    onView: function() {
    },
    /**
     * @cfg {Function} readInfo 自定义的查看业务对象的方法。 可以自定义该方法来完全覆盖默认的view方法。
     */
    view: function() {// 通用的查看数据窗口
        if (this.readInfo)
            return this.readInfo();
        var record = this.grid.getSelectionModel().getSelected();
        if (!record) {
            this.alert("请先选择要操作的数据！");
            return false;
        }
        var id = record.get("id");
        var win = this.showViewWin();
        for (var n in record.data) {
            var c = win.getComponent(0).findSomeThing(n);
            // if(!c &&
            // win.getComponent(0).getXType()=="form")c=win.getComponent(0).form.findField(n);
            if (c) {
                var v = record.get(n);
                // alert(n+":"+v);
                if (c.isFormField) {
                    c.setValue(v);
                    c.clearDirty();
                } else {
                    if (c.renderer)
                        v = c.renderer(v);
                    if (c.setText)
                        c.setText(v);
                    else if (c.getXType && c.getXType() == "panel")
                        c.body.update(v);
                }
                // alert(c.ownerCt.el.dom.innerHTML);
            }
        }
        this.onView(win, record.data);
        // this.fp.form.loadRecord(record);
        return win;
    },
    viewObject: function(id, callback) {
        var win = this.showViewWin(true);
        var viewCmd = this.viewCmd || "view";
        Ext.Ajax.request({
            url: this.formatUrl(viewCmd),
            params: {
                id: id
            },
            waitMsg: "正在加载数据,请稍侯...",
            callback: function(options, success, response) {
                var r = Ext.decode(response.responseText);
                for (var n in r) {
                    var c = win.getComponent(0).findSomeThing(n);

                    if (c) {
                        var v = r[n];
                        if (c.isFormField) {
                            c.setValue(v);
                            c.clearDirty();
                        } else {
                            if (c.renderer)
                                v = c.renderer(v);
                            if (c.setText)
                                c.setText(v);
                            else if (c.getXType && c.getXType() == "panel")
                                c.body.update(v);
                        }
                    }
                }
                if (callback)
                    callback(win, r);
                this.onView(win, r);
            },
            scope: this
        });
    },
    // private
    showViewWin: function(autoClose) {
        if (!this.viewPanel) {
            if (this.createViewPanel) {
                this.viewPanel = this.createViewPanel();
            } else {
                if (this.fp) {
                    this.viewPanel = this.fp;
                } else {
                    this.viewPanel = this.createForm();
                }
            }
        }
        var win = this.getViewWin(autoClose);
        return win;
    },
    /**
     * 业务对象列表中的查询方法。
     */
    doSearch: function() {
        var win = Disco.Ext.CrudSearchWindow;
        var o = win.getComponent(0).form.getValues(false);
        var service = win.crudService;
        service.store.baseParams = Ext.apply(o, {
            searchType: 'advancedSearch',
            pageSize: service.store.baseParams.pageSize || service.pageSize
        });
        win.hide();
        if (service.searchField && service.cleanQuickSearch) {
            service.searchField.reset();
        }
        service.search();
    },
    /**
     * 如果开启了高级查询，点击高级查询按钮弹出高级查询窗口。
     */
    advancedSearch: function() {
        return this.superSearchWin(this.searchWin.width, this.searchWin.height, this.searchWin.title);
    },
    /**
     * @cfg {Ext.form.FormPane||Ext.Panel} searchFP
     *      如果指定了该属性对应的Panel或者FormPanel实例，则在点击高级查询按钮后，会使用该面板作为高级查询面板。<br/>
     *      同时，如果开启了自定义查询条件的功能，该面板也会作为自定义条件的保存表单。
     */
    /**
     * @cfg {Function} searchFormPanel 如果配置了该方法，则在点击高级查询按钮后，会使用该方法创建的面板作为高级查询面板。<br/>
     *      同时，如果开启了自定义查询条件的功能，该面板也会作为自定义条件的保存表单。
     */
    /**
     * @cfg {Function} searchFP 如果配置了该方法，则在点击高级查询按钮后，会使用该方法创建的面板作为高级查询面板。<br/>
     *      同时，如果开启了自定义查询条件的功能，该面板也会作为自定义条件的保存表单。
     */
    /**
     * 创建高级查询窗口
     */
    superSearchWin: function(width, height, title) {
        var isNew = !Disco.Ext.CrudSearchWindow;
        if (!this.searchPanel) {
            if (this.searchFP || this.searchFormPanel) {
                this.searchPanel = this.searchFP ? this.searchFP() : this.searchFormPanel();
            }
        }
        if (!this.searchPanel)
            return null;// 如果没有定义searchFP或searchFormPanel，则返回
        var win = this.createGlobalWin("CrudSearchWindow", width, height, title, this.searchPanel, null, "searchPanel", [{
            id: "tb_search",
            text: "查询",
            handler: this.doSearch,
            iconCls: 'search',
            scope: this
        }, {
            text: "重置",
            iconCls: 'clean',
            handler: function() {
                Disco.Ext.CrudSearchWindow.getComponent(0).form.reset();
            }
        }, {
            text: "关闭",
            iconCls: 'delete',
            handler: function() {
                Disco.Ext.CrudSearchWindow.hide()
            }
        }]);
        if (isNew) {
            /*
             * var map = new Ext.KeyMap(win.el,{ key: 13, fn: this.doSearch });
             */
        }
        return win;
    },
    /**
     * 改变grid视图的显示方式，实现预览效果和正常效果的切换
     */
    toggleDetails: function(obj) {
        var view = this.grid.getView();
        if (view.showPreview)
            view.showPreview = false;
        else
            view.showPreview = true;
        view.refresh();
    },
    /**
     * 提供的一个企业业务中常见的调整顺序的功能方法包装。<br/>
     * 可以使用该方法来讲当前业务对象列表中选中的对象和其上一个业务对象或者下一个业务对象的位置进行交换。<br/>
     * 该方法在交换前台显示顺序后，还会向后台发送一个Ajax请求，在后台也完成顺序交换逻辑。<br/>
     * 
     * 例如，如果当前选中对象的id为2，调用方法：<br/> swapSequence(true,true)<br/> 会向后台发送请求：<br/>
     * <ul>
     * <li>url: format('swapSequence')</li>
     * <li>params:{down: true,id:2}</li>
     * </ul>
     * 
     * @param {Boolean}
     *            down 是将选中的业务对象下移还是上移。true是下移，false是上移。
     * @param {Boolean}
     *            inform 移动成功后是否提示。true为提示，提示内容为：操作成功。false不提示。
     */
    swapSequence: function(down, inform) {
        return function() {
            var record = this.grid.getSelectionModel().getSelected();
            if (!record) {
                this.alert("请先选择要操作的数据！", "提示");
                return;
            }
            var id = record.get("id");
            Ext.Ajax.request({
                url: this.formatUrl("swapSequence"),
                params: {
                    'id': record.get("id"),
                    down: down ? down : "",
                    parentId: this.parentId,
                    sq: this.grid.store.find("id", id) + 1
                },
                method: 'POST',
                success: function(response) {
                    var r = Ext.decode(response.responseText);
                    if (!r.success)
                        Disco_RIA.ajaxErrorHandler(r);
                    else {
                        if (inform) {
                            Ext.Msg.alert("提示", "操作成功", function() {
                                this.store.reload();
                                this.focus();
                            }, this);
                        } else {
                            this.store.reload();
                        }
                    }
                },
                scope: this
            });
        }
    },
    /**
     * 向业务对象列表面板的工具栏中插入按钮。
     * 
     * @param {Array}
     *            args 要插入的toolbaritem。
     */
    insertGridButton: function() {
        this.gridButtons.splice(10, 0, arguments);
    },
    /**
     * @cfg {Ext.Menu} menu 在列表中显示的右键菜单。
     */
    // private
    showContextMenu: function(g, i, e) {
        if (this.menu) {
            var evn = e ? e : g;
            evn.preventDefault();
            if (i >= 0) {
                this.grid.getSelectionModel().selectRow(i, false);
            }
            this.menu.showAt(evn.getPoint());
        }
    },
    /**
     * 在列表中完成Disco.Ext.Util.operaterRender渲染的可操作column动作。
     */
    doOperate: function(grid, rowIndex, columnIndex, e) {
        var tag = e.getTarget("A", 3);
        if (tag) {
            var id = tag.getAttribute("theid");
            var cmd = tag.getAttribute("op");
            var cf = tag.getAttribute("cf");
            if (id && cmd)
                this.operate(cmd, id, cf, grid, rowIndex, columnIndex, e);
        }
    },
    // private
    operate: function(cmd, id, cf, grid, rowIndex, columnIndex, e) {
        if (cmd == "edit")
            this.edit();
        else if (cmd == "view")
            this.view();
        else if (cmd == "remove")
            this.removeData();
        else {
            if (!cf)
                this.executeUrl(this.baseUrl, {
                    cmd: cmd,
                    id: id
                }, this.refresh.createDelegate(this))();
            else
                Ext.Msg.confirm("提示", "确认要执行该操作吗?", function(btn) {
                    if (btn == "yes")
                        this.executeUrl(this.baseUrl, {
                            cmd: cmd,
                            id: id
                        }, this.refresh.createDelegate(this))();
                    else
                        this.focus();
                }, this);
        }
    },
    /**
     * 根据一个查询方法来找到在业务列表面板工具栏（toptoolbar)中的符合条件的工具栏组件。
     * 
     * @param {Function}
     *            callback 传入的查询方法。
     *            <ul>
     *            该方法传入的参数：
     *            <li>{Component} 业务列表面板工具栏（toptoolbar)中的每一个按钮组件</li>
     *            </ul>
     * 
     * @return {Array} finds 找到的符合条件的工具栏组件
     */
    findOperatorBy: function(callback) {
        var objs = [];
        this.operators.each(function(o) {
            if (typeof o != "string") {
                if (callback && callback(o))
                    objs.push(o);
            }
        });
        return objs;
    },
    /**
     * 根据工具栏组件的属性找到业务列表面板工具栏（toptoolbar)中的符合条件的工具栏组件。
     * 
     * @param {String}
     *            name 要匹配的属性名称。
     * @parma {Object} value 要匹配的属性的值。
     * 
     * @return {Array} finds 找到的符合条件的工具栏组件
     */
    findOperatorByProperty: function(name, value) {
        return this.findOperatorBy(function(o) {
            if (o[name] == value)
                return true;
        });
    },

    // private
    toggleSingleRowOperator: function(enable) {
        var ids = this.findOperatorByProperty("singleRow", true);
        var args = [];
        if (ids && ids.length) {
            for (var i = 0; i < ids.length; i++)
                args.push(ids[i].name || ids[i].id);
        }
        if (enable)
            this.enableOperaterItem(args);
        else
            this.disableOperaterItem(args);
    },
    /**
     * 改变业务列表面板工具栏（toptoolbar)所有工具栏组件的状态
     * 
     * @param {Boolean}
     *            enable 改变的状态。如果为true，启用所有组件。如果为false，禁用所有组件。
     */
    toggleAllOperator: function(enable) {
        var args = [];
        this.operators.each(function(o) {
            if (typeof o != "string") {
                args.push(o.name || o.id);
            }
        });
        if (enable) {
            this.enableOperaterItem(args);
        } else {
            this.disableOperaterItem(args);
        }
    },
    /**
     * 当业务对象列表中的一行或者某行选中时，控制业务列表面板工具栏（toptoolbar)按钮的统一方法。<br/>
     * 子类如果在工具栏中加入了自定义的方法，可以复写该方法来控制自定义按钮的可用性。<br/>
     * 
     * 自定义的工具栏按钮也可以通过加入batch或者singleRow属性来标示按钮是否需要受到统一控制。<br/>
     * 
     * @param {Ext.data.Record}
     *            record 选中的行对应的record
     * @param {Integer}
     *            index 选中的行数
     * @param {Ext.grid.SelectionModel}
     *            sel grid的selectionModel。
     */
    onRowSelection: function(record, index, sel) {
        var sel = this.grid.getSelections();
        var ids = this.findOperatorByProperty("batch", true);// 打开支持批量操作的按钮
        var selected = true;
        if (sel.length == 1) {
            this.toggleSingleRowOperator(true);
        } else if (sel.length > 1) {
            this.toggleSingleRowOperator(false);
        } else {
            selected = false;
            this.toggleSingleRowOperator(false);
        }
        var args = [];

        if (ids && ids.length) {
            Ext.each(ids, function(obj) {
                args.push(obj.name || obj.id)
            });
        }
        this[(selected ? 'enable' : 'disable') + 'OperaterItem'](args);
    },
    // private
    changeOperaterItem: function(args, callback) {
        if (args.length == 1 && Ext.isArray(args[0]))
            args = args[0];
        if (this.grid.getTopToolbar() && this.grid.getTopToolbar().items) {// 已经渲染
            for (var i = 0; i < args.length; i++) {
                if (this.menu && this.menu.items) {
                    var o = this.menu.items.find(function(c) {
                        var n1 = c.name || c.id;
                        if (n1 == args[i])
                            return true;
                    });
                    if (o)
                        callback(o);
                }
                o = this.grid.getTopToolbar().items.find(function(c) {
                    var n1 = c.name || c.id;
                    if (n1 == args[i])
                        return true;
                });
                if (o)
                    callback(o);
            }
        } else {
            this.grid.on("render", function() {
                for (var i = 0; i < args.length; i++) {
                    var o = this.grid.getTopToolbar().items.find(function(c) {
                        var n1 = c.name || c.id;
                        if (n1 == args[i])
                            return true;
                    });;
                    if (o)
                        callback(o);
                }
            }, this);
        }
    },
    /**
     * 让一系列的右键菜单项变成可用状态
     * 
     * @param {Array}
     *            args 要设置为可用状态的右键菜单项的名称或者id。
     */
    enableOperaterItem: function() {
        var args = arguments;
        this.changeOperaterItem(args, function(o) {
            if (o.enable)
                o.enable();
        });
    },
    /**
     * 让一系列的右键菜单项变成禁用状态
     * 
     * @param {Array}
     *            args 要设置为禁用状态的右键菜单项的名称或者id。
     */
    disableOperaterItem: function() {
        var args = arguments;
        this.changeOperaterItem(args, function(o) {
            if (o.disable)
                o.disable();
        });
    },
    /**
     * 让一系列的右键菜单项变成可见状态
     * 
     * @param {Array}
     *            args 要设置为可见状态的右键菜单项的名称或者id。
     */
    showOperaterItem: function() {
        var args = arguments;
        this.changeOperaterItem(args, function(o) {
            if (o.show)
                o.show();
        });
    },
    /**
     * 让一系列的右键菜单项变成隐藏状态
     * 
     * @param {Array}
     *            args 要设置为隐藏状态的右键菜单项的名称或者id。
     */
    hideOperaterItem: function() {
        var args = arguments;
        this.changeOperaterItem(args, function(o) {
            if (o.hide) {
                o.hide();
            }
        });
    },
    // private
    operatorConfig2Component: function(o, isMenu) {
        var co = Ext.apply({}, o);
        if (!co.handler) {
            if (co.method && this[co.method]) {
                co.handler = this[co.method];
            } else if (co.cmd)
                co.handler = co.batch ? this.executeMulitCmd(co.cmd) : this.executeCmd(co.cmd);
        }
        if (co.handler && !co.scope)
            co.scope = this;
        if (!isMenu) {// 对按钮的样式作处理
            if (this.operatorButtonStyle == 2) {
                if (co.icon) {
                    co.cls = "x-btn-icon";
                    co.text = "";
                }
            } else if (this.operatorButtonStyle == 3) {
                co.icon = "";
                co.cls = "";
            }
        }
        var key = co.name || co.id;
        if (key == "searchField")
            co.store = this.store;
        else if (key == "btn_advancedSearch") {
            co.hidden = !((this.searchFormPanel || this.searchFP) && this.allowSearch);
        } else if (key == "btn_remove") {
            o.batch = this.batchRemoveMode;
        }
        return co;
    },
    // private
    buildCrudOperator: function() {
        if (!this.operators)
            this.initOperator();
        var bs = [];
        this.operators.each(function(c) {
            if (typeof c == "string") {
                bs.push(c);
            } else {
                if (!c.showInMenuOnly) {
                    var co = this.operatorConfig2Component(c);
                    var key = co.name || co.id;
                    try {
                        if (Ext.isString(co.type)) {
                            this[key] = Ext.create(co, co.type);
                        } else {
                            this[key] = new Ext.Toolbar.Button(co);
                        }
                        bs.push(this[key]);
                    } catch (e) {
                        alert(key + ":" + e);
                    }
                }
            }
        }, this);

        // 创建菜单
        if (!this.menu) {
            var ms = [];
            this.operators.each(function(c) {
                if (typeof c == "string") {
                    if (c == "-")
                        ms.push(c);
                } else {
                    if (!c.showInToolbarOnly) {
                        var co = this.operatorConfig2Component(c, true);
                        if (!co.type) {
                            ms.push(co);
                        }
                    }
                }
            }, this);
            ms.push({
                name: "btn_help",
                text: "帮助",
                handler: this.help,
                scope: this
            });
            this.menu = new Ext.menu.Menu({
                items: ms
            });
        }
        return bs;
    },
    // private
    initOperator: function() {
        this.initDisableOperators();
        this.operators = new Ext.util.MixedCollection(false);
        this.operators.getKey = function(o) {
            return o.id || o.name;
        };
        for (var i = 0; i < this.crud_operators.length; i++) {
            var co = this.crud_operators[i];
            if (typeof co == "object") {
                co = Ext.apply({}, co);
                if (!co.batch && !co.noneSelectRow)
                    co.singleRow = true;// noneSelectRow表示不需要进行行选择
            }
            var key = co.name || co.id;
            if (key && this.disable_operators.indexOf(key) >= 0)
                continue;// 如果被禁用,则不用加入到operators中
            this.operators.add(co);
        }
        if (this.customizeQueryObject) {
            this.operators.add({
                showInToolbarOnly: true,
                name: "btn_customizeQuery",
                cls: "x-btn-icon",
                icon: "images/icon-png/srsearch.gif",
                tooltip: "自定义查询",
                text: "",
                menu: ["-", {
                    text: "保存当前查询条件",
                    handler: this.createQueryObject,
                    scope: this
                }, {
                    text: "管理自定义查询",
                    handler: this.manageQueryObject,
                    scope: this
                }]
            })
        }
        if (this.gridButtons) {
            var bi = (this.disable_operators.indexOf("searchField") >= 0 ? this.operators.getCount() - 1 : this.operators.getCount() - 2);
            this.insertOperator(bi, this.gridButtons);
        }
    },
    /**
     * 向业务对象列表工具栏(toptoolbar)指定位置插入一个或一组工具栏组件。<br/>
     * 
     * @param {Integer}
     *            index 要插入工具栏的位置
     * @param {Objec|Array}
     *            items 要插入的工具栏组件<br/> 要插入的工具栏组件式一个或者一组配置对象。有几种类型，
     *            <ul>
     *            <li>文字或者按钮，直接使用{text:'',scope:this,handler:function}样式即可。</li>
     *            <li>标准分隔符，支持'-','->'标准工具栏分隔符。</li>
     *            <li>其他自定义组件。如果是要插入文本框，下拉列表框，或其他自定义组件，只需要加上xtype即可。比如{xtype:'textfield',name:'',id:''}。</li>
     *            </ul>
     * 
     * 在CrudFunction中，还提供了很多可选的，系统支持的配置选项，用来直接定义工具栏组件。<br/>
     * <ul>
     * <li>{Boolean} batch:设置为batch属性的组件，在列表选中一个或者多个的时候自动启用，没有选中数据时禁用。</li>
     * <li>{Boolean} noneSelectRow:设置为noneSelectRow属性的组件，在列表非选中的状态下可用，选中数据禁用。</li>
     * <li>{Boolean}
     * singleRow:设置为singleRow属性的组件，在列表选中一个的时候自动启用，没有选中数据货选中多个数据时禁用。</li>
     * <li>{Boolean} showInMenuOnly:该组件只在右键菜单项中显示，不在列表的工具栏中显示。</li>
     * <li>{Boolean} showInToolbarOnly:该类组件只在列表的工具栏中显示，不在列表的右键菜单显示。</li>
     * <li>{String} method：如果菜单项是个按钮，并且设置了method属性，则直接匹配this.method作为其handler</li>
     * <li>{String}
     * cmd:如果菜单项是个按钮，并且设置了cmd属性，则直接匹配this.executeMulitCmd(cmd)或者this.executeCmd(cmd)作为其handler</li>
     * </ul>
     */
    insertOperator: function(index, items) {
        if (!this.operators) {
            this.initOperator();
        }

        if (!Ext.isArray(items))
            items = [items];
        if (this.operators.getCount() < index)
            index = this.operators.getCount();

        var haveRender = this.grid && this.grid.getTopToolbar && this.grid.getTopToolbar() && this.grid.getTopToolbar().items;

        for (var i = 0; i < items.length; i++) {
            var co = items[i];
            if (typeof co == "object") {
                co = Ext.apply({}, co);
                if (!co.batch && !co.noneSelectRow)
                    co.singleRow = true;// noneSelectRow表示不需要进行行选择
            }
            this.operators.insert(index + i, co);
            if (haveRender) {
                if (!co.showInMenuOnly) {
                    var bo = this.operatorConfig2Component(Ext.apply({}, co));
                    this.grid.getTopToolbar().insert(index + i, bo);
                }
                if (!co.showInToolbarOnly) {
                    var mo = this.operatorConfig2Component(co, true);
                    if (this.menu)
                        this.menu.insert(index + i, new Ext.menu.Item(mo));
                }
            }
        }
    },
    // private
    initDisableOperators: function() {
        if (!this.disable_operators)
            this.disable_operators = [];
        if (!this.exportData)
            this.disable_operators.push("btn_export");
        if (!this.importData)
            this.disable_operators.push("btn_import");
        if (!this.printData)
            this.disable_operators.push("btn_print");
        if (!this.clearData)
            this.disable_operators.push("btn_clearSearch");
        if (!this.allowSearch)
            this.disable_operators.push("btn_advancedSearch");
        if (!this.showAdd)
            this.disable_operators.push("btn_add");
        if (!this.showEdit)
            this.disable_operators.push("btn_edit");
        if (!this.showRemove)
            this.disable_operators.push("btn_remove");
        if (!this.showView)
            this.disable_operators.push("btn_view");
        if (!this.showRefresh)
            this.disable_operators.push("btn_refresh");
        if (!this.showSearchField)
            this.disable_operators.push("searchField");
    },
    // private
    manageQueryObject: function() {
        var win = new UserQueryObjectWin({
            crudService: this,
            objName: this.queryObjectName
        });
        win.show();
    },
    // private
    createQueryObject: function() {
        if (!this.searchQueryObjectPanel) {
            if (this.searchFP || this.searchFormPanel) {
                this.searchQueryObjectPanel = this.searchFP ? this.searchFP() : this.searchFormPanel();
                this.searchQueryObjectPanel.insert(0, {
                    fieldLabel: '查询器名称',
                    xtype: "textfield",
                    name: "queryObjectName",
                    anchor: "-20",
                    allowBlank: false
                });
                this.searchQueryObjectPanel.insert(1, {
                    fieldLabel: '关键字',
                    xtype: "textfield",
                    name: "searchKey",
                    anchor: "-20"
                });
            }
        }
        if (!this.searchQueryObjectPanel)
            return null;// 如果没有定义searchFP或searchFormPanel，则返回
        var win = this.createGlobalWin("CrudQueryObjectWindow", this.searchWin.width, this.searchWin.height + 60, "保存查询条件", this.searchQueryObjectPanel, null, "searchQueryObjectPanel", [{
            id: "tb_search",
            text: "保存",
            handler: this.saveQueryObject,
            iconCls: 'search'
        }, {
            text: "取消",
            iconCls: 'delete',
            handler: function() {
                Disco.Ext.CrudQueryObjectWindow.hide()
            }
        }]);
        win.getComponent(0).form.setValues(this.store.baseParams || {});
        win.getComponent(0).form.findField("queryObjectName").setValue("");
    },
    /**
     * 保存用户自定义查询条件方法。<br/>
     * 如果开启了业务支持用户自定义查询方法，该方法在用户自定义了查询条件后，会向后台发送一个保存查询条件的请求:<br/>
     * {url:serQueryObject.java?cmd=save}，同时提交的还有高级查询面板中定义的各个查询项所设置的值。
     * 
     */
    saveQueryObject: function() {
        var win = Disco.Ext.CrudQueryObjectWindow;
        if (!win.getComponent(0).form.isValid()) {
            Ext.Msg.alert("提示", "必填项必须填写!");
            return;
        }
        var o = win.getComponent(0).form.getValues(false);
        var title = o.queryObjectName;
        delete o.queryObjectName;
        var service = win.crudService;

        var params = {
            title: title,
            content: Ext.urlEncode(o),
            objName: service.queryObjectName
        };
        Ext.Ajax.request({
            url: "userQueryObject.java?cmd=save",
            params: params,
            success: function(response) {
                var ret = Ext.decode(response.responseText);
                if (ret.success) {
                    this.addQueryObjectOperator([params]);
                    Ext.Msg.alert("提示", "操作成功!", this.focus, this);
                    win.hide();
                } else {
                    Ext.Msg.alert("无法保存", ret.errors.msg, function() {
                        win.getComponent(0).form.findField("searchKey").focus();
                    });
                }
            },
            scope: service
        });
    },
    /**
     * 按照自定义查询条件查询业务对象列表。
     * 
     * @param {Component}
     *            c 创建的自定义查询条件所对应的查询条件。
     *            也可以构造一个对象，该对象包含了一个content{String}属性，该属性中定义了要提交的请求参数。
     */
    searchByQueryObject: function(c) {
        if (c.content) {
            var params = Ext.urlDecode(c.content);
            if (this.searchField)
                this.searchField.setValue(params.searchField || "");
            this.store.baseParams = params;
            if (this.store.lastOptions && this.store.lastOptions.params)
                this.store.lastOptions.params.start = 0;
        }
        this.refresh();
    },
    // private
    addQueryObjectOperator: function(objs) {
        if (objs && objs.length) {
            var o = this.operators.find(function(c) {
                var n1 = c.name || c.id;
                if (n1 == "btn_customizeQuery")
                    return true;
            });
            if (!o)
                return;
            var haveRender = this.grid && this.grid.getTopToolbar && this.grid.getTopToolbar() && this.grid.getTopToolbar().items;
            var btn_customizeQuery = this["btn_customizeQuery"];
            for (var i = 0; i < objs.length; i++) {
                var co = objs[i];
                co.scope = this;
                co.text = co.title;
                co.name = "query_menu" + co.title;
                co.handler = this.searchByQueryObject;
                o.menu.splice(0, 0, co);
                if (btn_customizeQuery) {
                    btn_customizeQuery.menu.insert(0, new Ext.menu.Item(co));
                }
            }

        }
    },
    // private
    removeQueryObjectOperator: function(name) {
        var btn_customizeQuery = this["btn_customizeQuery"];
        if (btn_customizeQuery) {
            var item = btn_customizeQuery.menu.items.find(function(c) {
                var n1 = c.name || c.id;
                if (n1 == "query_menu" + name)
                    return true;
            });
            if (item)
                btn_customizeQuery.menu.remove(item);
        }
    },
    // private
    useOperatorsPermission: function(args) {
        var ret = args || this.permissions;
        var args = [];
        for (var i = 0; i < ret.length; i++) {
            args.push(ret[i]);
            var o = this.operators.find(function(c) {
                var n1 = c.name || c.id;
                if (n1 == args[i])
                    return true;
            });
            if (o)
                o.hidden = false;
        }
        this.showOperaterItem(args);
        this.fireEvent("usepermission", this);
    },
    // private
    loadOperatorsPermission: function() {
        var args = {}, names = [], actions = [], cmds = [];
        var baseUrl = this.baseUrl;
        this.operators.each(function(o) {
            if (typeof o != "string") {
                if (!o.clientOperator && (o.cmd || o.method)) {
                    actions.push(o.action || baseUrl);
                    cmds.push(o.cmd || o.method || "");
                    names.push(o.name || o.id || "");
                }
            }
        });
        if (!this.permissions) {
            var objs = {
                names: names,
                actions: actions,
                cmds: cmds
            };
            if (this.customizeQueryObject && this.queryObjectName) {
                objs.queryObjectName = this.queryObjectName;
            }
            if (Disco_RIA.permissionCheck) {
                Ext.Ajax.request({
                    url: (Disco_RIA.permissionCheckAction || "/manage.java?cmd=checkButtons"),
                    params: objs,
                    callback: function(options, success, response) {
                        var ret = Ext.decode(response.responseText);
                        if (ret && ret.permissions && ret.permissions.length) {// 处理权限
                            this.permissions = ret.permissions;
                            this.useOperatorsPermission();
                        }
                        if (ret && ret.queryObjects) {
                            this.addQueryObjectOperator(ret.queryObjects);
                        }
                    },
                    scope: this
                });
            } else {
                this.permissions = ["btn_add", "btn_edit", "btn_view", "btn_remove", "btn_refresh"];
                this.useOperatorsPermission();
            }
        } else {
            this.useOperatorsPermission();
        }
    },
    // private
    checkAdnLoadColumnField: function() {
        if (!this.storeMapping) {
            var url = this.baseUrl + "?cmd=loadColumnField";
            if (this.entityClzName) {
                url = "/extApp.java?cmd=loadColumnField&entityClzName=" + this.entityClzName;
            }
            var ajax = Ext.lib.Ajax.syncRequest("POST", url, "");
            var ret = Ext.decode(ajax.conn.responseText);
            if (ret && ret.fields) {
                this.storeMapping = ret.fields;
                if (ret.columnMap && !this.columns && !this.cm) {
                    this.columns = [];
                    for (var index in ret.columnMap) {
                        var c = ret.columnMap[index];
                        c.dataIndex = c.name;
                        if (!c.header)
                            c.header = c.name;
                        var d = [];
                        if (this.autoDisplayFields && this.autoDisplayFields.indexOf(c.name) < 0) {
                            c.hidden = true;
                        }
                        if (this.autoHideFields && this.autoHideFields.indexOf(c.name) >= 0) {
                            c.hidden = true;
                        }
                        if (this.disableHideableFields && this.disableHideableFields.indexOf(c.name) >= 0) {
                            c.hideable = false;
                        }
                        if (c.sortable === undefined) {
                            if (c.type != "object") {
                                c.sortable = true;
                            }
                        }
                        if (!c.renderer) {// 自动处理Renderer
                            if (c.type == "date") {
                                c.renderer = this.dateRender("Y-m-d");
                            } else if (c.type == "object" || c.type == "map") {
                                c.renderer = this.objectAutoRender;
                            }
                        } else {// 把renderer转换成javascript对象
                            try {
                                c.renderer = Ext.decode(c.renderer);
                            } catch (e) {
                            }
                        }
                        this.columns.push(c);
                    }
                }
            }
        }
    },
    // private
    haveOperatorRights: function(btn) {
        return this[btn] && (!(this[btn].disabled || this[btn].hidden));
    },
    // private
    handleCrudKey: function(e) {
        if (!(e.isSpecialKey() || e.altKey || e.getKey() == e.DELETE))
            return;
        if (e.getKey() == Ext.EventObject.ENTER && !e.ctrlKey) {
            e.stopEvent();
            this.edit();
        } else if (e.altKey && e.getKey() == 'c'.charCodeAt(0) && this.haveOperatorRights("btn_edit") && this.copy) {
            e.stopEvent();
            this.copy();
        } else if (e.altKey && e.getKey() == 'a'.charCodeAt(0) && this.haveOperatorRights("btn_add")) {
            e.stopEvent();
            this.create();
        } else if (e.altKey && e.getKey() == 'e'.charCodeAt(0) && this.haveOperatorRights("btn_edit")) {
            e.stopEvent();
            this.edit();
        } else if (e.altKey && e.getKey() == 'v'.charCodeAt(0) && this.haveOperatorRights("btn_view")) {
            e.stopEvent();
            this.view();
        } else if ((e.getKey() == e.DELETE || (e.altKey && e.getKey() == 'd'.charCodeAt(0))) && this.haveOperatorRights("btn_remove")) {
            e.stopEvent();
            this.removeData();
        } else if (e.altKey && e.getKey() == 's'.charCodeAt(0)) {
            e.stopEvent();
            this.advancedSearch();

        } else if ((e.getKey() == e.PRINT_SCREEN || (e.altKey && e.getKey() == 'p'.charCodeAt(0))) && this.haveOperatorRights("btn_print")) {
            e.stopEvent();
            this.printRecord();
        } else if (e.altKey && e.getKey() == 'o'.charCodeAt(0) && this.haveOperatorRights("btn_export")) {
            e.stopEvent();
            this.exportExcel();

        } else if (e.altKey && e.getKey() == 'i'.charCodeAt(0) && this.haveOperatorRights("btn_import")) {
            e.stopEvent();
            this.importExcel();

        }

    },
    /**
     * 定义的CrudFunction的响应事件。 系统级的事件定义：
     * <ul>
     * <li>celldblclick:this.edit 双击列表项进入编辑模式</li>
     * <li>cellclick:this.doOperate 点击列表项可能触发operaterRender的响应事件</li>
     * <li>keypress:this.handleCrudKey 系统的快捷键响应</li>
     * </ul>
     */
    initCrudEventHandler: function() { // 双击表格行进入编辑状态
        this.grid.on("celldblclick", this.edit, this);
        this.grid.on("cellclick", this.doOperate, this);
        this.grid.on("keypress", this.handleCrudKey, this);

        var rowSelFn = function(g, index, r) {
            this.onRowSelection(r, index, g);
        };

        this.grid.getSelectionModel().on({
            scope: this,
            rowselect: rowSelFn,
            rowdeselect: rowSelFn
        });
        if (this.showMenu) {
            this.grid.on("rowcontextmenu", this.showContextMenu, this);
        }
        Disco.Ext.Util.autoFocusFirstRow(this.grid);
    },
    focus: function() {
        this.focusCrudGrid();
    },
    /**
     * 让业务对象列表获得焦点<br/> 默认选中第一行。
     * 
     * @param {Ext.grid.GridPanel}
     *            grid 要获得焦点的列表（一般是this.grid）
     */
    focusCrudGrid: function(grid) {
        var g = grid || this.grid;
        if (g && g.rendered) {
            var sel = g.getSelectionModel();
            if (sel && sel.hasSelection()) {
                g.getView().focusRow(g.store.indexOf(g.getSelectionModel().getSelected()));
            } else if (g.store.getCount()) {
                g.getView().focusRow(0);
            } else {
                g.focus();
            }
        }
    },
    /**
     * 帮助。 子类覆盖该方法实现自定义的模块帮助。
     */
    help: function() {
        Ext.Msg.show({
            title: "系统帮助",
            buttons: Ext.Msg.OK,
            icon: Ext.Msg.INFO,
            msg: "欢迎使用本系统!"
        });
    }
}
/**
 * 增、删、改、查面板基类 该类是配合CrudFunction，用作页面的样式渲染。<br/>
 * 所有的拥有添删改查的业务对象管理页面都可以通过继承该类完成快速页面开发<br/>
 * 
 * @class Disco.Ext.CrudPanel
 * @extends Ext.Panel
 */
Disco.Ext.CrudPanel = Ext.extend(Ext.Panel, {
    // private
    border: false,
    // private
    layout: 'fit',
    // private
    closable: true,
    // private
    autoScroll: false,
    /**
     * @cfg {Boolean} gridForceFit 业务对象列表页面列表是否支持宽度自适应。
     */
    gridForceFit: true,
    /**
     * 导入说明
     * @type String
     */
    importExplain: "",
    /**
     * @cfg {Object} viewWin 如果开启了查看业务流程，则该对象定义了查看窗口的样式。
     *      <ul>
     *      <li>{Integer} width 窗口宽度</li>
     *      <li>{Integer} height 窗口高度</li>
     *      <li>{String} title 窗口标题</li>
     *      </ul>
     */
    viewWin: {
        width: 650,
        height: 410,
        title: "详情查看"
    },
    /**
     * @cfg {Object} searchWin 如果开启了高级查询业务流程，则该对象定义了高级查询窗口的样式。
     *      <ul>
     *      <li>{Integer} width 窗口宽度</li>
     *      <li>{Integer} height 窗口高度</li>
     *      <li>{String} title 窗口标题</li>
     *      </ul>
     */
    searchWin: {
        width: 630,
        height: 300,
        title: "高级查询"
    },
    /**
     * @cfg {Object} gridViewConfig 自定义的业务对象列表表格的视图样式配置。
     *      比如经常会自定义表格视图的getRowClass属性来在列表中控制不同状态的业务对象的显示方式。
     */
    gridViewConfig: {},
    /**
     * @cfg {Object} gridConfig 自定义的业务对象列表表格的配置。
     */
    gridConfig: {},
    /**
     * @cfg {Object} baseQueryParameter 定义的查询初始化参数
     *      该参数会一直绑定在业务对象列表的store上。在实际的开发中，一般用来区分类似于销售出库单，报损单等相同模型，近似逻辑的单据。
     */
    baseQueryParameter: {},
    /**
     * @cfg {String} localStoreVar 是否开启客户端cache。如果设置了该属性名称，则会用该名称来标示全局缓存store。
     *      一旦设置了该属性，列表会使用Disco.Ext.CachedRemoteStore作为业务对象列表的store。
     */
    localStoreVar: window.undefined,// 客户端cache的名称
    // private
    bulidGridStore: function() {
        var list = this.defaultsActions ? this.defaultsActions.list : 'list';
        var storeConfig = {
            id: this.storeId ? this.storeId : "id",
            //url: this.getCmdUrl('list'),
            url: this.defaultCmd ? (this.formatUrl(list)) : this.baseUrl,
            root: "result",
            autoDestroy: true,
            totalProperty: "rowCount",
            pageSize: this.pageSize,
            remoteSort: true,
            fields: this.storeMapping
        };
        if (Ext.isEmpty(this.localStoreVar, false)) {
            this.store = new Ext.data.JsonStore(storeConfig);
        } else {
            this.store = new Disco.Ext.CachedRemoteStore(Ext.apply({
                varName: this.localStoreVar,
                pageSize: Ext.num(this.pageSize, 20)
            }, storeConfig));
        }

        this.store.baseParams = Ext.apply({}, {
            limit: this.pageSize || ""
        }, this.initQueryParameter || {});
        if (Ext.objPcount(this.baseQueryParameter)) {
            this.store.on('beforeload', function(store, options) {
                Ext.apply(store.baseParams, this.baseQueryParameter);
            }, this);
        }
        this.store.paramNames.sort = "orderBy";
        this.store.paramNames.dir = "orderType";
        return this.store;
    },
    /**
     * @event saveobject 当保存或者修改业务对象成功后抛出的事件
     * 
     * @param {Disco.Ext.Util.CrudPanel}
     *            this CrudPanel自身
     * @param {Ext.form.BasicForm}
     *            form 提交的表单
     * @param {Ext.form.Action}
     *            action 提交表单绑定的acion对象。
     */
    /**
     * @event removeobject 当删除业务对象成功后抛出的事件
     * 
     * @param {Disco.Ext.Util.CrudPanel}
     *            this CrudPanel自身
     * @param {Ext.data.Record}
     *            r 删除的对象在列表中对应的record对象。
     * @param {Object}
     *            option 提交请求绑定的option对象。
     */
    // private
    initComponent: function() {
        this.checkAdnLoadColumnField();
        this.store = this.bulidGridStore();

        this.addEvents("saveobject", "removeobject");// 增加saveobject及removeobject事件
        Disco.Ext.CrudPanel.superclass.initComponent.call(this);

        var buttons = this.buildCrudOperator();

        var viewConfig = Ext.apply({
            forceFit: this.gridForceFit
        }, this.gridViewConfig);
        var gridConfig = Ext.apply(this.gridConfig, {
            store: this.store,
            stripeRows: true,
            trackMouseOver: false,
            loadMask: true,
            viewConfig: viewConfig,
            border: false,
            bbar: this.gridBbar || (this.pagingToolbar ? new Ext.ux.PagingComBo({
                rowComboSelect: true,
                pageSize: this.pageSize,
                store: this.store,
                displayInfo: true
            }) : null)
        });
        if (this.showTbar) {
            gridConfig.tbar = buttons
        }

        if (this.summaryGrid) {
            if (gridConfig.plugins) {
                if (typeof gridConfig.plugins == "object")
                    gridConfig.plugins = [gridConfig.plugins];
            } else
                gridConfig.plugins = [];
            gridConfig.plugins[gridConfig.plugins.length] = new Ext.ux.grid.GridSummary();
        }
        var columns = this.columns, cfg = {};
        columns = columns || this.cm.config;
        delete gridConfig.cm;

        columns = [].concat(columns);
        if (this.gridRowNumberer) {
            columns.unshift(new Ext.grid.RowNumberer({
                header: '序号',
                width: 36
            }));
        }

        if ((!gridConfig.sm && !gridConfig.selModel) && this.gridSelModel == 'checkbox') {
            cfg.sm = new Ext.grid.CheckboxSelectionModel();
            if (columns[0] instanceof Ext.grid.RowNumberer) {
                columns.splice(1, 0, cfg.sm);
            } else {
                columns.unshift(cfg.sm);
            }
        }
        cfg.columns = columns;

        gridConfig = Ext.applyIf(cfg, gridConfig);

        if (this.columnLock && Ext.grid.LockingGridPanel) {
            this.grid = new Ext.grid.LockingGridPanel(gridConfig);
        } else
            this.grid = new Ext.grid.GridPanel(gridConfig);

        // this.grid.colModel.defaultSortable = true;// 设置表格默认排序
        this.loadOperatorsPermission();
        // 双击表格行进入编辑状态
        this.initCrudEventHandler();
        this.add(this.grid);
        if (this.autoLoadGridData)
            this.store.load();
    }
});
Ext.applyIf(Disco.Ext.CrudPanel.prototype, Disco.Ext.CrudFunction);

/**
 * @class ExtAppBasePanel
 * @extend Ext.Panel
 * 
 * ExtApp的基类，用于实现通过iframe的方式把指定script文件中的指定appClass加载到iframe中显示
 */
ExtAppBasePanel = Ext.extend(Ext.Panel, {
    appClass: "",
    script: "",
    otherScripts: "",
    border: false,
    layout: "fit",
    closable: true,
    initComponent: function() {
        // this.html="<iframe frameborder='0' scrolling='auto'
        // src='extApp.java?appClass=BBSDirManagePanel&script=bbs.js'></iframe>";
        ExtAppBasePanel.superclass.initComponent.call(this);
        this.on("resize", function(c) {
            if (!c.framePanel) {
                var h =
                        "<iframe width='100%' height='" + c.body.getHeight(true) + "px' frameborder='0' scrolling='auto' src='/extApp.java?appClass=" + this.appClass + "&script=" + this.script
                                + "&otherScripts=" + this.otherScripts + "&params=" + this.params + "'></iframe>";
                c.body.update(h);
                c.framePanel = c.body.dom.firstChild;
            } else {
                c.framePanel.height = c.body.getHeight(true);
            }
        }, this)
    }
});

/**
 * @class Disco.Ext.TreeComboField
 * @extends Ext.form.TriggerField
 * 
 * 树状下拉框,基于TriggerFiel可以下拉出一个TreePanel供用户选择
 * 
 * <pre>
 * <code>
 *  //示例：
 *  //一般的应用来说，树状菜单的Loader使用Cache。
 *  //定制一个货品分类的树cache加载器。
 *  if (!Global.productDirLoader) {
 *  Global.productDirLoader = new Disco.Ext.MemoryTreeLoader({
 *  varName : &quot;Global.PRODUCT_DIR_NODES&quot;,
 *  url : &quot;productDir.java?cmd=getProductDirTree&amp;pageSize=-1&amp;treeData=true&amp;all=true&quot;,
 *  listeners : {
 *  'beforeload' : function(treeLoader, node) {
 *  treeLoader.baseParams.id = (node.id.indexOf('root') &lt; 0? node.id: &quot;&quot;);
 *  if (typeof node.attributes.checked !== &quot;undefined&quot;) {
 *  treeLoader.baseParams.checked = false;
 *  }
 *  }
 *  }
 *  });
 *  } 
 * 
 *  //创建一个树状下拉框
 *  this.productDir = new Disco.Ext.TreeComboField({
 *  xtype : &quot;treecombo&quot;,
 *  fieldLabel : &quot;所属分类&quot;,
 *  emptyText : &quot;分类&quot;,
 *  name : &quot;dirId&quot;,
 *  leafOnly : true,
 *  width:60,
 *  listWidth:120,
 *  hiddenName : &quot;dirId&quot;,
 *  displayField : &quot;title&quot;,
 *  allowBlank : false,
 *  tree : new Ext.tree.TreePanel({
 *  autoScroll:true,
 *  root : new Ext.tree.AsyncTreeNode(
 *  {
 *  id : &quot;root&quot;,
 *  text : &quot;产品分类&quot;,
 *  expanded : true,
 *  loader : Global.productDirLoader
 *  })
 *  }),
 *  listeners:{
 *  beforeSetValue:function(combo,val){
 *  if(val==0){
 *  combo.clearValue();
 *  return false;
 *  }
 *  },
 *  scope:this
 *  }		
 *  });
 * 
 *  //后台代码需要解析业务对象的树结构。
 *  public Page doGetProductDirTree(WebForm form) {
 *  String id = CommUtil.null2String(form.get(&quot;id&quot;));
 *  String prefix = CommUtil.null2String(form.get(&quot;prefix&quot;));
 *  if (!&quot;&quot;.equals(prefix)) {
 *  id = id.replaceAll(prefix, &quot;&quot;);
 *  }
 *  QueryObject query = new QueryObject();
 *  query.setPageSize(-1);
 *  if (!&quot;&quot;.equals(id)) {
 *  ProductDir parent = this.service.getProductDir(new Long(id));
 *  query.addQuery(&quot;obj.parent&quot;, parent, &quot;=&quot;);
 *  } else {
 *  query.addQuery(&quot;obj.parent is EMPTY&quot;, null);
 *  }
 *  query.setOrderBy(&quot;sequence&quot;);
 *  CompanyUtil.addAdvDistributorQueryStr(query);
 *  IPageList pageList = this.service.getProductDirs(query);
 *  String checked = CommUtil.null2String(form.get(&quot;checked&quot;));
 *  String all = CommUtil.null2String(form.get(&quot;all&quot;));
 *  List&lt;Map&gt; nodes = new java.util.ArrayList&lt;Map&gt;();
 *  if (pageList.getRowCount() &gt; 0) {
 *  for (int i = 0; i &lt; pageList.getResult().size(); i++) {
 *  ProductDir category = (ProductDir) pageList.getResult().get(i);
 *  Map map = obj2treemap(category, !&quot;&quot;.equals(checked), &quot;true&quot;.equals(all), prefix);
 *  nodes.add(map);
 *  }
 *  } else {
 *  Map map = new HashMap();
 *  map.put(&quot;text&quot;, &quot;无分类&quot;);
 *  map.put(&quot;id&quot;, &quot;0&quot;);
 *  map.put(&quot;leaf&quot;, true);
 *  map.put(&quot;icon&quot;, &quot;images/menuPanel/tag_blue3.gif&quot;);
 *  nodes.add(map);
 *  }
 *  form.jsonResult(nodes);
 *  return Page.JSONPage;
 *  }
 * </code>
 * </pre>
 * 
 * @xtype treecombo
 */
Disco.Ext.TreeComboField = Ext.extend(Ext.form.TriggerField, {
    /**
     * @cfg {String} valueField 取值绑定的字段名，默认为'id'
     */
    valueField: "id",
    /**
     * @cfg {String} displayField 下拉树种显示名称绑定的字段名，默认为'name'
     */
    displayField: "name",
    /**
     * @cfg {Integer} minListWidth 最小的列表显示宽度
     */
    minListWidth: 70,
    haveShow: false,
    /**
     * @cfg {Boolean} editable 是否默认可编辑 默认为false
     */
    editable: false,
    /**
     * @cfg {Boolean} returnObject 返回值是否作为对象返回
     */
    returnObject: false,
    /**
     * @cfg {Boolean} leafOnly 是否只能选择叶子节点，默认可以选择任何节点。
     */
    leafOnly: false,
    /**
     * @cfg {Integer} clicksFinishEdit 在节点上点击作为选择的次数
     */
    clicksFinishEdit: 1,
    /**
     * @cfg {Boolean} readOnly 是否只读
     */
    readOnly: false,
    hiddenNodes: [],
    // private
    initEvents: function() {
        Disco.Ext.TreeComboField.superclass.initEvents.call(this);
        this.keyNav = new Ext.KeyNav(this.el, {
            "up": function(e) {
                this.inKeyMode = true;
                this.selectPrevious();
            },

            "down": function(e) {
                if (!this.isExpanded()) {
                    this.onTriggerClick();
                } else {
                    this.inKeyMode = true;
                    this.selectNext();
                }
            },
            "enter": function(e) {
                var sm = this.tree.getSelectionModel();
                if (sm.getSelectedNode()) {
                    var node = sm.getSelectedNode();
                    this.choice(node);
                    sm.clearSelections();
                    return;
                }
            },
            "esc": function(e) {
                this.collapse();
            },
            scope: this
        });
        this.queryDelay = Math.max(this.queryDelay || 10, this.mode == 'local' ? 10 : 250);
        this.dqTask = new Ext.util.DelayedTask(this.initQuery, this);
        if (this.typeAhead) {
            this.taTask = new Ext.util.DelayedTask(this.onTypeAhead, this);
        }
        if (this.editable !== false) {
            this.el.on("keyup", this.onKeyUp, this);
        }
        if (this.forceSelection) {
            this.on('blur', this.doForce, this);
        }
    },
    // private
    selectPrevious: function() {
        var sm = this.tree.getSelectionModel();
        if (!sm.selectPrevious()) {
            var root = this.tree.getRootNode();
            sm.select(root);
            this.el.focus();
        } else {
            this.el.focus();
        }
    },
    // private
    selectNext: function() {
        var sm = this.tree.getSelectionModel();
        if (!sm.selectNext()) {
            var root = this.tree.getRootNode();
            sm.select(root);
            this.el.focus();
        } else {
            this.el.focus();
        }
    },
    // private
    onTriggerClick: function() {
        if (this.readOnly || this.disabled) {
            return false;
        } else if (!this.tree.rendered || !this.list) {
            this.treeId = Ext.id();
            this.list = new Ext.Layer({
                id: this.treeId,
                cls: "x-combo-list",
                constrain: false
            });
            if (!this.innerDom)
                this.innerDom = Ext.getBody().dom;
            if (this.tree.rendered) {
                this.list.appendChild(this.tree.el);
            } else {
                this.tree.render(this.treeId);
                var lw = this.listWidth || Math.max(this.wrap.getWidth(), this.minListWidth);
                this.tree.setWidth(lw);
                this.tree.on("expandnode", this.restrictHeight, this);
                this.tree.on("collapsenode", this.restrictHeight, this);
            }
        } else
            this.restrictHeight();
        this.expand();
    },
    // private
    restrictHeight: function() {
        // this.list.dom.style.height = '';
        if (!this.list)
            return;
        var inner = this.innerDom;
        var h = inner.clientHeight - this.wrap.getBottom();
        if (this.tree.el.dom.offsetHeight >= h) {
            this.tree.setHeight(h);
        } else {
            this.tree.setHeight("auto");
        }
        // this.list.alignTo(this.getEl(), "tl-bl?");
    },
    // private
    filterTree: function(e) {
        if (!this.isExpanded())
            this.expand();
        var text = e.target.value;
        Ext.each(this.hiddenNodes, function(n) {
            n.ui.show();
        });
        if (!text) {
            this.filter.clear();
            return;
        }
        this.tree.expandAll();
        this.restrictHeight();
        this.filter.filterBy(function(n) {
            return (!n.attributes.leaf || n.text.indexOf(text) >= 0);
        });

        // hide empty packages that weren't filtered
        this.hiddenNodes = [];
        this.tree.root.cascade(function(n) {
            if (!n.attributes.leaf && n.ui.ctNode.offsetHeight < 3) {
                n.ui.hide();
                this.hiddenNodes.push(n);
            }
        }, this);
    },
    // private
    expand: function() {
        if (this.list) {
            Ext.getDoc().on('mousedown', this.hideIf, this);
            /*
             * if(!this.tree.body.isScrollable()){ this.tree.setHeight('auto'); }
             */
            this.list.show();
            this.list.alignTo(this.getEl(), "tl-bl?");
        } else {
            this.onTriggerClick();
        }
    },
    // private
    collapse: function() {
        if (this.list) {
            this.list.hide();
            Ext.getDoc().un('mousedown', this.hideIf, this);
        }
    },
    // private
    onEnable: function() {
        Disco.Ext.TreeComboField.superclass.onEnable.apply(this, arguments);
        if (this.hiddenField) {
            this.hiddenField.disabled = false;
        }
    },
    // private
    onDisable: function() {
        Disco.Ext.TreeComboField.superclass.onDisable.apply(this, arguments);
        if (this.hiddenField) {
            this.hiddenField.disabled = true;
        }
        Ext.getDoc().un('mousedown', this.hideIf, this);
    },
    // private
    hideIf: function(e) {
        if (!e.within(this.wrap) && !e.within(this.list)) {
            this.collapse();
        }
    },
    // private
    initComponent: function() {
        Disco.Ext.TreeComboField.superclass.initComponent.call(this);
        this.addEvents('beforeSetValue');
        this.filter = new Ext.tree.TreeFilter(this.tree, {
            clearBlank: true,
            autoClear: true
        });
    },
    // private
    onRender: function(ct, position) {
        Disco.Ext.TreeComboField.superclass.onRender.call(this, ct, position);
        if (this.clicksFinishEdit > 1)
            this.tree.on("dblclick", this.choice, this);
        else
            this.tree.on("click", this.choice, this);
        if (this.hiddenName) {
            this.hiddenField = this.el.insertSibling({
                tag: 'input',
                type: 'hidden',
                name: this.hiddenName,
                id: (this.hiddenId || this.hiddenName)
            }, 'before', true);
            this.hiddenField.value = this.hiddenValue !== undefined ? this.hiddenValue : this.value !== undefined ? this.value : '';
            this.el.dom.removeAttribute('name');
        }
        if (!this.editable) {
            this.editable = true;
            this.setEditable(false);
        } else {
            this.el.on('keydown', this.filterTree, this, {
                buffer: 350
            });
        }
    },
    /**
     * 返回选中的树节点
     * 
     * @return {Object} ret 选中的节点值。
     */
    getValue: function(returnObject) {
        if ((returnObject === true) || this.returnObject)
            return typeof this.value != 'undefined' ? {
                value: this.value,
                text: this.text,
                toString: function() {
                    return this.text;
                }
            } : "";
        return typeof this.value != 'undefined' ? this.value : '';
    },
    /**
     * 清除选择的值。
     */
    clearValue: function() {
        if (this.hiddenField) {
            this.hiddenField.value = '';
        }
        this.setRawValue('');
        this.lastSelectionText = '';
        this.applyEmptyText();
        this.value = "";
    },
    /**
     * 验证选择的值
     * 
     * @return {Boolean} ret 如果选择的值合法，返回true。
     */
    validate: function() {
        if (this.disabled || this.validateValue(this.processValue(this.getValue()))) {
            this.clearInvalid();
            return true;
        }
        return false;
    },
    /**
     * Returns whether or not the field value is currently valid by validating
     * the processed value of the field. Note: disabled fields are ignored.
     * 
     * @param {Boolean}
     *            preventMark True to disable marking the field invalid
     * @return {Boolean} True if the value is valid, else false
     */
    isValid: function(preventMark) {
        if (this.disabled) {
            return true;
        }
        var restore = this.preventMark;
        this.preventMark = preventMark === true;
        var v = this.validateValue(this.processValue(this.getValue()));
        this.preventMark = restore;
        return v;
    },
    /**
     * Validates a value according to the field's validation rules and marks the
     * field as invalid if the validation fails
     * 
     * @param {Mixed}
     *            value The value to validate
     * @return {Boolean} True if the value is valid, else false
     */
    validateValue: function(value) {
        if (value.length < 1 || value === null) { // if it's
            // blank
            if (this.allowBlank) {
                this.clearInvalid();
                return true;
            } else {
                this.markInvalid(this.blankText);
                return false;
            }
        }
        if (value.length < this.minLength) {
            this.markInvalid(String.format(this.minLengthText, this.minLength));
            return false;
        }
        if (value.length > this.maxLength) {
            this.markInvalid(String.format(this.maxLengthText, this.maxLength));
            return false;
        }
        if (this.vtype) {
            var vt = Ext.form.VTypes;
            if (!vt[this.vtype](value, this)) {
                this.markInvalid(this.vtypeText || vt[this.vtype + 'Text']);
                return false;
            }
        }
        if (typeof this.validator == "function") {
            var msg = this.validator(value);
            if (msg !== true) {
                this.markInvalid(msg);
                return false;
            }
        }
        if (this.regex && !this.regex.test(value)) {
            this.markInvalid(this.regexText);
            return false;
        }
        return true;
    },
    readPropertyValue: function(obj, p) {
        var v = null;
        for (var o in obj) {
            if (o == p)
                return true;
            // v = obj[o];
        }
        return v;
    },
    /**
     * 给该属性设值。设定的值符合树形列表中的某一个值，就会自动选中那个值。如果没有找到，则通过配置的valueNotFoundText显示指定的值。
     * 
     * @param {Object}
     *            obj 设定的值
     */
    setValue: function(obj) {
        if (!obj) {
            this.clearValue();
            return;
        }
        if (this.fireEvent('beforeSetValue', this, obj) === false) {
            return;
        }
        var v = obj;
        var text = v;
        var value = this.valueField || this.displayField;
        if (typeof v == "object" && this.readPropertyValue(obj, value)) {
            text = obj[this.displayField || this.valueField];
            v = obj[value];
        }

        var node = this.tree.getNodeById(v);
        if (node) {
            text = node.text;
        } else if (this.valueNotFoundText !== undefined) {
            text = this.valueNotFoundText;
        }
        this.lastSelectionText = text;
        if (this.hiddenField) {
            this.hiddenField.value = v;
        }
        Disco.Ext.TreeComboField.superclass.setValue.call(this, text);
        this.value = v;
        this.text = text;
    },
    /**
     * 设置是否可编辑状态。
     * 
     * @param {Boolean}
     *            value 设置的可编辑状态。
     */
    setEditable: function(value) {
        if (value == this.editable) {
            return;
        }
        this.editable = value;
        if (!value) {
            this.el.dom.setAttribute('readOnly', true);
            this.el.on('mousedown', this.onTriggerClick, this);
            this.el.addClass('x-combo-noedit');
        } else {
            this.el.dom.setAttribute('readOnly', false);
            this.el.un('mousedown', this.onTriggerClick, this);
            this.el.removeClass('x-combo-noedit');
        }
    },
    // private
    choice: function(node, eventObject) {
        if ((!this.leafOnly || node.isLeaf())) {
            if (node.id != "root") {
                this.setValue(node.id);
            } else {
                this.clearValue();
                this.el.dom.value = node.text;
            }
            this.fireEvent('select', this, node);
            this.collapse();
            this.fireEvent('collapse', this);
        } else {
            if (node.id == "root") {
                this.clearValue();
                this.el.dom.value = node.text;
                this.fireEvent('select', this, node);
                this.collapse();
                this.fireEvent('collapse', this);
            }
        }
    },
    // private
    validateBlur: function() {
        return !this.list || !this.list.isVisible();
    },
    /**
     * 判断列表是否处于展开状态
     * 
     * @return {Boolean} 是否处于展开状态
     */
    isExpanded: function() {
        return this.list && this.list.isVisible();
    },
    canFocus: function() {
        return !this.disabled;
    },
    onDestroy: function() {
        if (this.tree.rendered && this.list) {
            this.list.hide();
            this.list.destroy();
            delete this.list;
        }
        Disco.Ext.TreeComboField.superclass.onDestroy.call(this);
    }
});
Ext.reg('treecombo', Disco.Ext.TreeComboField);

/**
 * @class Disco.Ext.CheckTreeComboField
 * @extends Disco.Ext.TreeComboField
 * 
 * 树状下拉框,可以下拉出一个TreePanel并且支持check选择<br/> 该组件的使用方式和TreeComboField基本相同。<br/>
 * 该组件的一些特性：1，在选中了子节点后，父节点也能跟着选定，但不放入选中列表。2，选中了父节点，该父节点下的子节点自动全部选中。
 * 
 * <pre>
 * <code>
 * new Disco.Ext.CheckTreeComboField({
 * 			fieldLabel : '部门',
 * 			name : 'dept',
 * 			tree : new Ext.tree.TreePanel({
 * 						border : false,
 * 						autoScroll : true,
 * 						rootVisible : false,
 * 						loader : new Ext.tree.TreeLoader({
 * 									url : 'data/tree.json',
 * 									baseAttrs : {
 * 										checked : false
 * 									}
 * 								}),
 * 						root : new Ext.tree.AsyncTreeNode({
 * 									id : 'id',
 * 									text : '部门'
 * 								})
 * 					})
 * 		})
 * </code>
 * </pre>
 * 
 * @xtype checktreecombo
 */
Disco.Ext.CheckTreeComboField = Ext.extend(Disco.Ext.TreeComboField, {
    leafOnly: false,// 只允许选择子节点
    editable: true,
    // private
    onTriggerClick: function() {
        if (this.disabled)
            return;
        if (!this.listPanel.rendered || !this.list) {
            this.treeId = Ext.id();
            this.list = new Ext.Layer({
                id: this.treeId,
                cls: "x-combo-list",
                constrain: false
            });
            if (!this.innerDom)
                this.innerDom = Ext.getBody().dom;
            if (this.listPanel.rendered) {
                this.list.appendChild(this.listPanel.el);
            } else {
                this.listPanel.render(this.treeId);
                var lw = this.listWidth || Math.max(this.wrap.getWidth(), this.minListWidth);
                this.listPanel.setWidth(lw);
                this.tree.on("expandnode", this.restrictHeight, this);
                this.tree.on("collapsenode", this.restrictHeight, this);
            }
            if (this.value)
                this.setCheckdNode(this.value);
        } else
            this.restrictHeight();
        this.expand();
    },
    // private
    restrictHeight: function() {
        // this.list.dom.style.height = '';
        if (!this.list)
            return;
        var inner = this.innerDom;
        var h = inner.clientHeight - this.wrap.getBottom();
        if (this.listPanel.el.dom.offsetHeight >= h) {
            this.listPanel.setHeight(h);
        } else {
            this.listPanel.setHeight("auto");
            this.tree.setHeight("auto");
        }
    },
    // private
    initComponent: function() {
        Disco.Ext.CheckTreeComboField.superclass.initComponent.call(this);
        this.listPanel = new Ext.Panel({
            border: false,
            bodyBorder: false,
            buttonAlign: "center",
            layout: "fit",
            items: this.tree,
            bbar: [{
                text: "确定选择",
                handler: this.choice,
                scope: this
            }, {
                text: "清空",
                handler: this.cleanChoice,
                scope: this
            }, {
                text: "取消",
                handler: this.cancelChoice,
                scope: this
            }]
        });

    },
    // private
    onRender: function(ct, position) {
        Disco.Ext.CheckTreeComboField.superclass.onRender.call(this, ct, position);
        this.tree.on("checkchange", this.checkNode, this);

    },
    /**
     * 设置节点的选中状态 将传入值字符串中所有匹配值的节点都设置为选中状态
     * 
     * @param {String}
     *            v 需要选中的节点的值组成的字符串。各值之间使用','连接
     */
    setCheckdNode: function(v) {
        this.cleanCheckNode(this.tree.root);
        var vs = v.split(",");
        for (var i = 0; i < vs.length; i++) {
            var node = null;
            var valueField = this.valueField;
            this.tree.root.cascade(function(n) {
                if (n.attributes[valueField] == vs[i]) {
                    node = n;
                    return false;
                }
            });
            if (node)
                this.checkNode(node, true);
        }
    },
    /**
     * 清除指定节点及其子节点的选中状态
     * 
     * @param {Ext.data.Node}
     *            要清除选中状态的节点。
     */
    cleanCheckNode: function(node) {
        var checked = false;
        node.cascade(function(n) {
            if (n.ui.checkbox) {
                n.attributes.checked = n.ui.checkbox.checked = checked;
                n.attributes.selectAll = checked;
                if (checked)
                    n.ui.addClass("x-tree-selected");
                else
                    n.ui.removeClass("x-tree-selected");
            }
        }, this);
    },
    /**
     * 改变指定节点的子节点和父节点的选中状态。<br/>
     * 
     * @param {Ext.data.Node}
     *            node 要改变状态的节点
     * @param {Boolean}
     *            checked 改变的状态。true为选中状态，false为取消选中状态。
     *            如果是将指定节点设置为选中状态，该方法会将该节点的直接/间接父节点设置为选中状态，该节点的直接/间接子节点设置为选中状态<br/>
     *            如果是将指定节点设置为取消选中状态，该方法会将该节点的直接/间接父节点按逻辑设置为取消选中状态，该节点的直接/间接子节点设置为取消选中状态<br/>
     * 
     */
    checkNode: function(node, checked) {
        node.cascade(function(n) {
            if (n.ui.checkbox) {
                n.attributes.checked = n.ui.checkbox.checked = checked;
                n.attributes.selectAll = checked;
                if (checked)
                    n.ui.addClass("x-tree-selected");
                else
                    n.ui.removeClass("x-tree-selected");
            }
        }, this);
        node.bubble(function(n) {
            if (n != node) {
                if (!checked) {
                    n.attributes.selectAll = checked;
                    if (n.attributes.checked) {
                        n.ui.removeClass("x-tree-selected");
                    }
                } else if (!n.attributes.checked) {
                    n.attributes.checked = n.ui.checkbox.checked = checked;
                }
            }
        });
    },
    /**
     * 得到组件的值
     * 
     * @return {String} v 选中额值字符串。如果是选择了多个值，则是由多个值+','连接的字符串。
     */
    getValue: function() {
        return typeof this.value != 'undefined' ? this.value : '';
    },
    // private
    clearValue: function() {
        if (this.hiddenField) {
            this.hiddenField.value = '';
        }
        this.setRawValue('');
        this.lastSelectionText = '';
        this.applyEmptyText();
        if (this.list)
            this.cleanCheckNode(this.tree.root);
    },
    // private
    getNodeValue: function(node) {
        if (node.attributes.selectAll && (!this.leafOnly || node.attributes.leaf)) {
            if (this.t != "")
                this.t += ",";
            if (this.v != "")
                this.v += ",";
            var text = node.attributes[this.displayField || this.valueField];
            if (text === undefined)
                text = node.text;
            var value = node.attributes[this.valueField];
            if (value === undefined)
                value = node.id;
            this.t += text;
            this.v += value;
        } else if (node.attributes.checked)
            node.eachChild(this.getNodeValue, this)
    },
    /**
     * 给该属性设值。设定的值符合树形列表中的某一个值，就会自动选中那个值。如果没有找到，则通过配置的valueNotFoundText显示指定的值。
     * 
     * @param {Object}
     *            obj 设定的值
     */
    setValue: function(obj) {
        if (!obj) {
            this.clearValue();
            return;
        }
        var v = obj;
        var text = v;
        var value = this.valueField || this.displayField;
        if (typeof v == "object" && this.readPropertyValue(obj, value)) {// 直接传入值

            text = obj[this.displayField || this.valueField];
            v = obj[value];
            if (this.list)
                this.setCheckdNode(v);

        } else {// 自动查找树中的选择节点，并设置值
            var root = this.tree.root;
            this.t = "";
            this.v = "";
            if (root.attributes.selectAll) {
                this.t = root.text;
            } else {
                root.eachChild(this.getNodeValue, this);
            }
            text = this.t;
            v = this.v;
        }
        this.lastSelectionText = text;
        if (this.hiddenField) {
            this.hiddenField.value = v;
        }
        Disco.Ext.TreeComboField.superclass.setValue.call(this, text);
        this.value = v;
        this.text = text;
    },
    /**
     * 当选中节点后的响应方法
     * 
     * @param {Boolean}
     *            notClean 是否清除之前已经选中项
     */
    choice: function(notClean) {
        if (notClean)
            this.setValue(true);
        else
            this.clearValue();
        this.list.hide();
    },
    /**
     * 清除当前已经选中项
     */
    cleanChoice: function() {
        this.clearValue();
        this.list.hide();
    },
    /**
     * 取消当前选项
     */
    cancelChoice: function() {
        this.list.hide();
    },
    onDestroy: function() {
        if (this.listPanel.rendered && this.list) {
            this.list.hide();
            this.list.remove();
        }
        Disco.Ext.CheckTreeComboField.superclass.onDestroy.call(this);
    }
});
Ext.reg('checktreecombo', Disco.Ext.CheckTreeComboField);

/**
 * @class Disco.Ext.SmartCombox
 * @extends Ext.form.ComboBox 对ComboBox进行扩展,更好的支持对象的选择及其显示,并且可以在下拉列表中直接支持新建数据
 * 
 * <pre>
 * <code>
 *  //示例,ria框架提供的创建指定数据字典下拉列表的封装方法。
 *  getDictionaryCombo : function(name, fieldLabel, sn, valueField,disableBlank, editable) {
 *  return {
 *  xtype : &quot;smartcombo&quot;,
 *  name : name,
 *  hiddenName : name,
 *  displayField : &quot;title&quot;,
 *  valueField : valueField ? valueField : &quot;id&quot;,
 *  lazyRender : true,
 *  triggerAction : &quot;all&quot;,
 *  typeAhead : true,
 *  editable : editable,
 *  allowBlank : !disableBlank,
 *  sn:sn,
 *  objectCreator:{appClass:&quot;SystemDictionaryDetailPanel&quot;,script:&quot;/systemManage/SystemDictionaryManagePanel.js&quot;},
 *  createWinReady:function(win){
 *  if(this.fieldLabel)win.setTitle(&quot;新建&quot;+this.fieldLabel);
 *  if(this.sn)win.findSomeThing(&quot;parentSn&quot;).setOriginalValue(this.sn);
 *  },
 *  store : new Ext.data.JsonStore({
 *  id : &quot;id&quot;,
 *  url : &quot;systemDictionary.java?cmd=getDictionaryBySn&amp;sn=&quot;
 *  + sn,
 *  root : &quot;result&quot;,
 *  totalProperty : &quot;rowCount&quot;,
 *  remoteSort : true,
 *  baseParams : {
 *  pageSize : &quot;-1&quot;
 *  },
 *  fields : [&quot;id&quot;, &quot;title&quot;, &quot;tvalue&quot;]
 *  }),
 *  fieldLabel : fieldLabel
 *  }
 *  },
 * </code>
 * </pre>
 * 
 * @xtype smartcombo
 */
Disco.Ext.SmartCombox = Ext.extend(Ext.form.ComboBox, {
    /**
     * @cfg {Boolean} disableCreateObject 是否禁用下拉列表下面的【新建】和【同步】按钮。
     */
    /**
     * 设置getValue获取的值，是基本类型，还是对象类型
     * 
     * @cfg {Boolean} returnObject
     */
    returnObject: false,

    /**
     * 用来定义新建对象的相关属性
     * 
     * @cfg {Object} objectCreator 该配置对象包括： <url>
     *      <li>appClass {String} : 调用的创建业务对象的管理模块类名</li>
     *      <li>script {String} : 调用的创建业务对象管理模块的script文件地址</li>
     *      </ul>
     */
    objectCreator: null,
    /**
     * 当点击新建按钮，打开创建业务对象的窗口后的钩子方法
     * 
     * @param {Ext.Window}
     *            win
     *            创建业务对象的窗口。如果是CrudPanel，则直接调用win.getComponent(0)即是fp。
     */
    createWinReady: function(win) {
        if (this.fieldLabel)
            win.setTitle("新建" + this.fieldLabel);
    },
    /**
     * 创建新对象，实质是调用Disco.Ext.Util.addObject方法。
     */
    newObject: function() {
        this.collapse();
        var title = this.fieldLabel;
        Disco.Ext.Util.addObject(this.objectCreator.appClass, this.reload.createDelegate(this), this.objectCreator.script, this.objectCreator.otherScripts, this.createWinReady.createDelegate(this));
    },
    /**
     * 同步下拉列表中的业务对象数据。
     */
    synchObject: function() {
        this.store.reload();
    },
    /**
     * @cfg {Array} operatorButtons
     *      显示在下拉列表底部的按钮数组。子类可以通过该属性覆盖并自定义自己的显示按钮。
     */
    initComponent: function() {
        if (this.objectCreator && !this.disableCreateObject) {
            this.operatorButtons = [{
                text: "新建",
                iconCls: "add",
                handler: this.newObject,
                scope: this
            }, {
                text: "同步",
                iconCls: "f5",
                handler: this.synchObject,
                scope: this
            }];
        }
        Disco.Ext.SmartCombox.superclass.initComponent.call(this);
    },
    initList: function() {
        if (!this.list) {
            Disco.Ext.SmartCombox.superclass.initList.call(this);
            // this.operatorButtons=[{text:"ddd"}];
            if (this.operatorButtons) {// 增加对操作按钮的支持
                if (this.pageTb) {
                    this.pageTb.insert(0, this.operatorButtons);
                } else {
                    this.bottomBar = this.list.createChild({
                        cls: 'x-combo-list-ft'
                    });
                    this.bottomToolbar = new Ext.Toolbar(this.operatorButtons);
                    this.bottomToolbar.render(this.bottomBar);
                    this.assetHeight += this.bottomBar.getHeight();
                }
            }
        }
    },
    /**
     * 获取SmartCombox符合指定条件的record值。<br/>
     * 实际的过程是首先得到combox的值value，然后在下拉列表绑定的store中查询field等于value的record。如果有，则返回该record。
     * 
     * @param {String}
     *            field 指定的valueField属性名称。
     * @return {Object} 符合条件的Record
     */
    getValueObject: function(field) {
        var val = "";
        if (this.returnObject) {
            val = this.getValue().value;
        } else {
            val = this.getValue();
        }
        if (val) {
            var index = this.store.find(field || "id", val);
            if (index >= 0) {
                var record = this.store.getAt(index);
                return record;
            }
            return null;
        }
        return null;
    },

    /**
     * 获取SmartCombox中的值
     * 
     * @return {Object}
     */
    getValue: function() {
        if (this.returnObject) {
            var value = this.value;
            if (this.el.dom.value == this.PleaseSelectedValue || this.el.dom.value == this.nullText)
                return null;
            if (this.selectedIndex >= 0) {
                var record = this.store.getAt(this.selectedIndex);
                if (record && record.data) {
                    var t = record.data[this.displayField || this.valueField];
                    if (t != this.el.dom.value)
                        value = null;
                }
            }
            return {
                value: value,
                text: this.el.dom.value,
                toString: function() {
                    return this.text;
                }
            };
        } else
            return Disco.Ext.SmartCombox.superclass.getValue.call(this);
    },
    /**
     * 设置SmartCombox的value
     * 
     * @param {String/Object/Number...}
     *            v 要设置的值
     */
    setValue: function(v) {
        if (v && typeof v == "object" && eval("v." + this.valueField)) {
            var value = eval("v." + this.valueField);
            var text = eval("v." + this.displayField) ? eval("v." + this.displayField) : this.valueNotFoundText;
            this.lastSelectionText = text;
            if (this.hiddenField) {
                this.hiddenField.value = value;
            }
            Ext.form.ComboBox.superclass.setValue.call(this, text);
            this.value = value;
            if (this.store.find(this.valueField, value) < 0) {
                var o = {};
                o[this.valueField] = value;
                o[this.displayField] = text;
                if (this.store && this.store.insert) {
                    this.store.insert(0, new Ext.data.Record(o));
                    // this.select(0);
                }
            }
        } else if (v === null) {
            Disco.Ext.SmartCombox.superclass.setValue.call(this, "");
        } else {
            Disco.Ext.SmartCombox.superclass.setValue.call(this, v);
        }
    },
    onSelect: function(record, index) {
        if (this.fireEvent('beforeselect', this, record, index) !== false) {
            this.setValue(record.data[this.valueField || this.displayField]);
            this.collapse();
            this.fireEvent('select', this, record, index);
        }
    }
});
Ext.reg('smartcombo', Disco.Ext.SmartCombox);

/**
 * 
 * @class Disco.Ext.PopupWindowField
 * @extends Ext.form.TriggerField
 *          弹出窗口选择组件（类似于下拉框列表Field，只是在点击旁边下拉按钮时是通过弹出一个列表窗口，在窗口列表中进行数据选择。）
 * 
 * <pre>
 * <code>
 *  //示例，选择部门使用PopupWindowField，在列表中显示更详细的内容。
 *  //首先创建一个Disco.Ext.GridSelectWin
 *  if(!Global.departmentSelectWin)
 *  Global.departmentSelectWin=new Disco.Ext.GridSelectWin({
 *  title : &quot;选择部门&quot;,
 *  width : 540,
 *  height : 400,
 *  layout : &quot;fit&quot;,
 *  buttonAlign : &quot;center&quot;,
 *  closeAction : &quot;hide&quot;,
 *  grid : new DepartmentGrid(),
 *  modal : true,
 *  });
 * 
 *  //在form里面
 *  {xtype:'popupwinfield',win:Global.departmentSelectWin,valueField:'id',displayField:'departmentName',returnObject:true},
 * </code>
 * </pre>
 * 
 */
Disco.Ext.PopupWindowField = Ext.extend(Ext.form.TriggerField, {
    /**
     * @cfg {Object} win 指定弹出的win。一般使用Disco.Ext.GridSelectWin<br/>
     */
    win: null,
    /**
     * @cfg {String} valueField 选中的列表Record中用来作为field值的属性名
     */
    valueField: "id",
    /**
     * @cfg {String} displayField 选中的列表Record中用来作为field显示的属性名
     */
    displayField: "name",
    haveShow: false,
    /**
     * @cfg {Booealn} editable 弹出窗口选择框是否能编辑。默认不能直接编辑
     */
    editable: false,
    callback: Ext.emptyFn,
    returnObject: false,
    /**
     * @cfg {Booealn} choiceOnly
     *      是否只是选择值。如果为true，只返回值，如果为false，才考虑returnObject条件。
     */
    choiceOnly: false,
    /**
     * 得到选择的值<br/> 如果是choiceOnly为true,则直接返回this.value<br/>
     * 如果是choiceOnly为false，并且returnObject为true，则会返回一个对象。<br/>
     * 
     * @return {Object} ret 得到选中的值
     */
    getValue: function() {
        if (this.choiceOnly)
            return this.value;
        if (this.returnObject)
            return typeof this.value != 'undefined' ? {
                value: this.value,
                text: this.text,
                toString: function() {
                    return this.text ? this.text : this.value;
                }
            } : "";
        return typeof this.value != 'undefined' ? this.value : '';
    },
    /**
     * 给组件设置值。<br/> 如果是choiceOnly为true,则直接设置this.value<br/>
     * 如果是choiceOnly为false，并且returnObject为true，则使用传入对象的o[displayField]属性值来作为显示的值，使用传入对象的o[valueField]作为值<br/>
     * 
     * @param {Object}
     *            v 传入的要设置的值
     */
    setValue: function(v) {
        if (this.choiceOnly)
            return this.value = v;
        if (v && typeof v == "object" && eval("v." + this.valueField)) {
            var value = eval("v." + this.valueField);
            var text = eval("v." + this.displayField) ? eval("v." + this.displayField) : this.valueNotFoundText;
            this.lastSelectionText = text;
            if (this.hiddenField) {
                this.hiddenField.value = value;
            }
            Disco.Ext.PopupWindowField.superclass.setValue.call(this, text);
            this.value = value;
            this.text = text;
        } else if (v === null)
            Disco.Ext.PopupWindowField.superclass.setValue.call(this, "");
        else
            Disco.Ext.PopupWindowField.superclass.setValue.call(this, v);
    },
    /**
     * 当点击后面的下拉框按钮，弹出选择窗口
     */
    onTriggerClick: function() {
        if (this.win) {
            this.win.show();
        }
    },
    onRender: function(ct, position) {
        Disco.Ext.PopupWindowField.superclass.onRender.call(this, ct, position);
        if (this.win) {
            this.win.on("select", this.choice, this);
        }
        if (this.hiddenName) {
            this.hiddenField = this.el.insertSibling({
                tag: 'input',
                type: 'hidden',
                name: this.hiddenName,
                id: (this.hiddenId || this.hiddenName)
            }, 'before', true);
            this.hiddenField.value = this.hiddenValue !== undefined ? this.hiddenValue : this.value !== undefined ? this.value : '';
            this.el.dom.removeAttribute('name');
        }
        if (!this.editable) {
            this.editable = true;
            this.setEditable(false);
        }
        if (this.choiceOnly)
            this.el.hide();
    },
    /**
     * 在弹出列表窗口中选择了值之后执行的方法<br/> 默认是直接设置该field的值
     * 
     * @param {Object}
     *            data 弹出窗口中列表选中Record对应的data值
     * @param {Object}
     *            win 弹出的列表窗口实例
     */
    choice: function(data, win) {
        this.setValue(data);
        this.fireEvent('select', data, win);
    },
    /**
     * @event select 当在弹出窗口列表中选定了某列数据，并执行完成choice方法后抛出<br/>
     * @param {Object}
     *            data 弹出窗口中列表选中Record对应的data值
     * @param {Object}
     *            win 弹出的列表窗口实例
     */
    initComponent: function() {
        Disco.Ext.PopupWindowField.superclass.initComponent.call(this);
        this.addEvents("select");

    },
    validateBlur: function() {
        return !this.win || !this.win.isVisible();
    },
    onDestroy: function() {
        if (this.win && this.win.isVisible()) {
            this.win.hide();
        }
        Disco.Ext.PopupWindowField.superclass.onDestroy.call(this);
    },
    /**
     * 设置为可以编辑
     * 
     * @param {Boolean}
     *            value
     */
    setEditable: function(value) {
        if (value == this.editable) {
            return;
        }
        this.editable = value;
        if (!value) {
            this.el.dom.setAttribute('readOnly', true);
            this.el.on("dblclick", this.onTriggerClick, this);
            this.el.on("click", this.onTriggerClick, this);
            this.el.addClass('x-combo-noedit');
        } else {
            this.el.dom.setAttribute('readOnly', false);
            this.el.un('mousedown', this.onTriggerClick, this);
            this.el.removeClass('x-combo-noedit');
        }
    }
});
Ext.reg('popupwinfield', Disco.Ext.PopupWindowField);

/**
 * @class Disco.Ext.GridSelectWin
 * @extends Ext.Window
 *          弹出一个Window供用户选择grid中的数据，一般和Disco.Ext.PopupWindowField配合使用，作为Disco.Ext.PopupWindowField中的win属性。
 *          示例参考Disco.Ext.PopupWindowField的示例
 */
Disco.Ext.GridSelectWin = Ext.extend(Ext.Window, {
    /**
     * @cfg {String} title 窗口的名称，默认为"选择数据"
     */
    title: "选择数据",
    /**
     * @cfg {Integer} width 窗口宽度，默认为540
     */
    width: 540,
    /**
     * @cfg {Integer} height 窗口高度，默认为400
     */
    height: 400,
    /**
     * @cfg {String} layout 窗口布局，默认为fit
     */
    layout: "fit",
    /**
     * @cfg {String} buttonAlign 窗口下方按钮的对其方式，默认为center
     */
    buttonAlign: "center",
    /**
     * @cfg {String} closeAction 窗口关闭方式，默认为hide
     */
    closeAction: "hide",
    /**
     * @cfg {Object} grid 窗口中显示的列表对象。必须是一个Ext.grid.GridPanel或者其继承类实例。<br/>
     *      必须要的参数
     */
    grid: null,// grid是必须传递的对象
    /**
     * @cfg {Booelan} modal 是否开启模式窗口
     */
    modal: true,
    callback: Ext.emptyFn,
    /**
     * @event select 当在列表中选择了一行或者多行后的抛出事件
     * @param {Array}
     *            datas 选中的record的data数组
     */
    /**
     * 当在列表中选择了一行或者多行后的处理事件。<br/>
     * 默认动作是将选中record的data放在一个数组中，关闭当前窗口，并将数组通过select事件抛出。<br/>
     */
    choice: function() {
        var grid = this.grid.grid || this.grid;
        var records = grid.getSelectionModel().getSelections();
        if (!records || records.length < 1) {
            Ext.Msg.alert("提示", "请选择一条数据记录");
            return false;
        }
        var datas = [];
        for (var i = 0; i < records.length; i++) {
            datas[i] = records[i].data;
        }
        this.hide();
        this.fireEvent('select', datas, this);
    },
    initComponent: function() {
        this.buttons = [{
            text: "确定",
            handler: this.choice,
            scope: this
        }, {
            text: "取消",
            handler: function() {
                this.hide();
            },
            scope: this
        }];
        Disco.Ext.GridSelectWin.superclass.initComponent.call(this);
        if (this.grid) {
            var grid = this.grid.grid || this.grid;// 兼容BaseGridList对象
            grid.on("rowdblclick", this.choice, this);
            this.add(this.grid);
        }
        this.addEvents("select");
    }
});
/**
 * 
 * @class Disco.Ext.TreeNodeUtil
 * @single
 */
Disco.Ext.TreeNodeUtil = {};
Ext.apply(Disco.Ext.TreeNodeUtil, {

    /**
     * 拷贝一颗树节点及所有子节点
     * 
     * <pre>
     * <code>
     * //例子，将当前A树中选中的节点拷贝到B树root节点下。
     * var cnode = this.atree.getSelectionModel().getSelectedNode();
     * this.btree.getRootNode().appendChild(Disco.Ext.TreeNodeUtil
     * 		.cloneTreeNode(cnode));
     * </code>
     * </pre>
     * 
     * @param {Array|Ext.data.Node}
     *            nodes 要拷贝的树节点或者节点的数组
     */
    cloneTreeNode: function(nodes) {
        var ns = [];
        for (var i = 0; i < nodes.length; i++) {
            var o = Ext.apply({}, nodes[i]);
            if (nodes[i].children && nodes[i].children.length) {
                o.children = this.cloneTreeNode(nodes[i].children);
            } else
                o.children = [];
            ns.push(o);
        }
        return ns;
    },

    /**
     * 从一个树节点级联向下完成某一个funcion
     * 
     * @param {Ext.data.Node}
     *            node 开始的节点
     * @param {Function}
     *            fn 对每一个节点要执行的方法
     * @param {Object}
     *            scope function执行的作用域
     * @param {Object|Array}
     *            args 执行function的参数
     */
    cascadeNode: function(node, fn, scope, args) {
        if (fn.apply(scope || this, args || [node]) !== false) {
            if (node.children && node.children.length) {
                for (var i = 0, cs = node.children; i < cs.length; i++) {
                    cs[i].parentNode = node;
                    this.cascadeNode(cs[i], fn, scope, args);
                }
            }
        }
    },
    /**
     * 从一个指定节点开始向下寻找匹配指定id的节点
     * 
     * @param {Object}
     *            id 要匹配的id值
     * @param {Ext.data.Node}
     *            node 开始寻找的节点
     * @return {Ext.data.Node} 找到的匹配节点
     */
    deepFindNode: function(id, node) {
        var ret = null;
        this.cascadeNode(node, function(n) {
            if (n.id == id) {
                ret = n;
                return false;
            }
        });
        return ret;
    },
    /**
     * 根据节点id查找指定节点
     * 
     * @param {}
     *            id
     * @param {}
     *            objs
     * @return {}
     */
    getNodeById: function(id, objs) {
        var ret = this.deepFindNode(id, {
            id: "root",
            children: objs
        });
        return ret || null;
    },
    /**
     * 从一组定节点中寻找匹配指定id的节点
     * 
     * @param {Object}
     *            id
     * @param {Array}
     *            objs 指定的节点。如果是数组，则将数组中的节点作为寻找的对象。
     * @return {Object} 找到的节点对象
     */
    findNode: function(id, objs) {
        if (id == "root")
            return objs;
        else {
            var ret = this.deepFindNode(id, {
                id: "root",
                children: objs
            });
            return ret && ret.children ? ret.children : null;
        }
    },
    /**
     * 从缓存（Disco.Cache）中删除缓存的节点信息。<br/> 该方法用于使用了本地缓存树加载器后，如果对应的对象执行了删除操作，<br/>
     * 则可以直接通过操作缓存中树节点的删除方法，让所有引用了该树加载器的列表或者下拉列表都及时更新。<br/> 而不用再重新去请求远端数据。
     * 
     * @param {String|Object}
     *            id 要删除的节点id
     * @param {Object|String}
     *            cache
     *            要清除的缓存。如果是Object，则默认为该Object即为缓存实例。如果是String，则默认为在Disco.Cache中保存的缓存名称
     */
    localRemove: function(id, cache) {
        var obj = Ext.type(cache) == "string" ? Disco.Cache.get(cache) : cache;
        if (obj) {
            var objs = obj.items;
            var node = this.getNodeById(id, objs);
            if (node) {
                if (node.parentNode && node.parentNode.children)
                    node.parentNode.children.remove(node);
                else
                    objs.remove(node);
                if (obj.fireEvent)
                    obj.fireEvent("removenode", node, obj);
            }
        }
    },
    /**
     * 添加节点到指定缓存中<br/> 该方法用于使用了本地缓存树加载器后，如果对应的对象执行了添加操作，<br/>
     * 则可以直接通过操作缓存中树节点的添加方法，将新添加的对象创建为节点信息，并直接添加到缓存中，让所有引用了该树加载器的列表或者下拉列表都及时更新。<br/>
     * 而不用再重新去请求远端数据。
     * 
     * @param {Ext.data.Node}
     *            node 要添加到缓存的节点对象
     * @param {String|Object}
     *            parentId 如果要将节点作为缓存中某个已经存在的节点的子节点，则必须指明该父节点的id值。
     * @param {Object|String}
     *            cache
     *            要添加的缓存。如果是Object，则默认为该Object即为缓存实例。如果是String，则默认为在Disco.Cache中保存的缓存名称
     */
    localAdd: function(node, parentId, cache) {
        var obj = Ext.type(cache) == "string" ? Disco.Cache.get(cache) : cache;
        if (obj) {
            var objs = obj.items;
            if (parentId) {
                var parentNode = this.getNodeById(parentId, objs);
                if (parentNode) {
                    parentNode.children = parentNode.children || [];
                    if (parentNode.children && parentNode.children.length == 0) {
                        parentNode.leaf = false;
                        parentNode.children = [node];
                    } else if (parentNode.children) {
                        parentNode.children.push(node);
                    }
                    if (obj.fireEvent)
                        obj.fireEvent("addnode", node, parentNode, obj);
                }
            } else {
                objs.push(node);
                if (obj.fireEvent)
                    obj.fireEvent("addnode", node, null, obj);
            }
        }
    },
    /**
     * 更新缓存中的节点<br/> 该方法用于使用了本地缓存树加载器后，如果对应的对象执行了修改操作，<br/>
     * 则可以直接通过操作缓存中树节点的修改方法，将修改后的对象创建为节点信息，并直接更新到缓存中，让所有引用了该树加载器的列表或者下拉列表都及时更新。<br/>
     * 而不用再重新去请求远端数据。
     * 
     * @param {Ext.data.Node}
     *            node 更新的节点，用该节点去更新拥有相同id的节点。
     * @param {Object|String}
     *            cache
     *            要更新的缓存。如果是Object，则默认为该Object即为缓存实例。如果是String，则默认为在Disco.Cache中保存的缓存名称
     */
    localChange: function(node, cache) {
        var obj = Ext.type(cache) == "string" ? Disco.Cache.get(cache) : cache;
        if (obj) {
            var objs = obj.items;
            var no = this.getNodeById(node.id, objs);
            if (no) {
                Ext.copyToIf(no, node, ["children", "cls", "loader", "parentNode"]);
                if (obj.fireEvent)
                    obj.fireEvent("updatenode", no, obj);
            }
        }
    },
    /**
     * 移动缓存中的节点<br/> 该方法用于使用了本地缓存树加载器后，如果对应的对象执行了移动操作，（即改变了某一个对象的位置）<br/>
     * 则可以直接通过操作缓存中树节点的移动方法，让所有引用了该树加载器的列表或者下拉列表都及时更新。<br/> 而不用再重新去请求远端数据。
     * 
     * @param {Ext.data.Node}
     *            node 要移动的节点
     * @param {String|Object}
     *            parentId 要移动到的目标父节点id。
     * @param {Object|String}
     *            cache
     *            要更新的缓存。如果是Object，则默认为该Object即为缓存实例。如果是String，则默认为在Disco.Cache中保存的缓存名称
     */
    localMove: function(node, parentId, cache) {
        this.localRemove(node.id, cache);
        this.localAdd(node, parentId, cache);
    }
});

/**
 * @class Disco.Ext.TreeCollection
 * @extends Ext.util.MixedCollection
 * 
 * 树状节点数据存储器，该缓存器配合Disco.Ext.TreeNodeUtil，提供了快速查询树节点及内在节点内容变更事件响应等相关方法<br/>
 * 如果在应用中如果需要缓存树状结构的数据，可以使用该缓存器。<br/>
 */
Disco.Ext.TreeCollection = Ext.extend(Ext.util.MixedCollection, {
    /**
     * 删除节点
     * 
     * @param {Object}
     *            id 要删除的节点的id值。
     */
    removeNode: function(id) {
        Disco.Ext.TreeNodeUtil.localRemove(id, this);
        this.unregisterNode(id);
    },
    /**
     * 添加节点
     * 
     * @param {Ext.data.Node}
     *            node 要添加的节点对象
     * @param {String}
     *            parentId 要添加到的父节点id。
     */
    addNode: function(node, parentId) {
        var n = null;
        if (n = this.getNodeById(node.id)) {
            Disco.Ext.TreeNodeUtil.localChange(node, this);
            return;
        }
        Disco.Ext.TreeNodeUtil.localAdd(node, parentId, this);
        this.registerNode(node);
    },
    /**
     * 更新节点
     * 
     * @param {Ext.data.Node}
     *            node 要更新的节点对象
     * @param {}
     *            cache
     */
    changeNode: function(node, cache) {
        Disco.Ext.TreeNodeUtil.localChange(node, this);
    },
    /**
     * 移动节点
     * 
     * @param {Ext.data.Node}
     *            node 需要移动的节点对象
     * @param {String|Object}
     *            parentId 要移动到的目标父节点id。
     * @param {}
     *            cache
     */
    moveNode: function(node, parentId, cache) {
        Disco.Ext.TreeNodeUtil.localMove(node, parentId, this);
    },
    /**
     * 注销nodeHash中的节点
     * 
     * @param {String}
     *            id 要注销（删除）的节点id
     */
    unregisterNode: function(id) {
        delete this.nodeHash[id];
    },
    /**
     * 往nodeHash中注册节点及所有子节点
     * 
     * @param {Ext.data.Node}
     *            node 要注册（添加）的节点
     */
    registerNode: function(node) {
        if (node.id)
            this.nodeHash[node.id] = node;
        var children = node.children || (node.attributes && node.attributes.children);
        if (children && children.length)
            Ext.each(children, this.registerNode, this);
    },
    /**
     * 获得所有节点
     * 
     * @return {Node} 根节点
     */
    getAllNode: function() {
        return this.nodeHash || {};
    },
    /**
     * 根据ID返回节点
     * 
     * @param {String|Object}
     *            id 要查询的节点id。
     * @return {Ext.data.Node} ret 找到的匹配的节点对象。
     */
    getNodeById: function(id) {
        return this.nodeHash[id];
    },
    /**
     * 重写MixCollection的addAll方法，并调用registerNode来注册节点
     * 
     * @param {Array}
     *            要添加的节点对象数组。
     */
    addAll: function(objs) {
        Disco.Ext.TreeCollection.superclass.addAll.call(this, objs);
        this.nodeHash = {};
        if (objs) {
            var obj = {
                id: 'root',
                children: objs
            };
            this.registerNode(obj)
        }
    },
    /**
     * 清空集合中的所有节点
     */
    clear: function() {
        this.nodeHash = {};
        Disco.Ext.TreeCollection.superclass.clear.call(this);
    }
});

Disco.Ext.CachedRemoteProxy = function(c, store) {
    this.enableCache = (c.enableCache == window.undefined ? true : c.enableCache);
    this.store = store;
    Disco.Ext.CachedRemoteProxy.superclass.constructor.call(this, {
        url: c.url
    });
    Ext.apply(this, c);
};
Disco.Ext.CachedRemoteProxy.DATAS = {};
Disco.Ext.CacheFilter = new Ext.util.MixedCollection();

Disco.Ext.CacheFilter.firstSearch = function(val, regexp) {
    if (typeof regexp != 'regexp')
        regexp = new RegExp("^" + regexp);
    return regexp.test(val);
}
/**
 * 
 * @class Disco.Ext.CachedRemoteProxy
 * @extends Ext.data.HttpProxy
 * 
 * 支持先从本地缓存中查找数据，如果没有数据，再从网络加载数据的Proxy对象。该类用作Disco.Ext.CachedRemoteStore的Proxy。
 */
Ext.extend(Disco.Ext.CachedRemoteProxy, Ext.data.HttpProxy, {
    /**
     * 根据查询条件过滤数据
     * 
     * @param {Object}
     *            params 要过滤的条件
     * @param {Array}
     *            datas 要过滤的数据
     * @return {Array} ret 过滤后的结果集
     */
    quickSearch: function(params, datas) {
        if (!datas.getCount())
            return datas;
        var objs = datas;
        var o = datas.items[0];
        if (!this.disableClientFilter) {
            for (var n in params) {
                if (n && o[n] !== undefined) {
                    objs = objs.filter(n, params[n]);
                }
            }
        }
        objs = this.cacheFilter(params, objs);
        return objs;
    },
    cacheFilter: function(params, objs) {
        var filter = Disco.Ext.CacheFilter.key(this.varName);
        if (filter && params) {
            if (typeof filter == 'function') {
                return filter.call(this, objs, params);
            }
        }
        return objs;
    },
    getData: function() {
        return Disco.Ext.CachedRemoteProxy.DATAS[this.varName];
    },
    /**
     * 从Cache中加载数据
     * 
     * @param {Object}
     *            params 要查询的条件
     * @param {Ext.data.DataReader}
     *            reader 数据读取器
     * @param {Function}
     *            callback 在加载完成后执行的回调方法
     * @param {Object}
     *            scope 回调方法执行的作用域
     * @param {Array}
     *            arg 回调方法执行的参数
     */
    loadFromCache: function(params, reader, callback, scope, arg) {
        var datas = this.quickSearch(params, this.getData());
        var o = {
            rowCount: datas.getCount(),
            result: []
        };
        var start = 0, limit = -1;

        if (params.start)
            start = Ext.num(parseInt(params.start), 0);

        if (params.limit)
            limit = Ext.num(parseInt(params.limit), -1);

        var max = datas.getCount();

        if (limit > 0)
            max = start + limit;
        if (max > datas.getCount())
            max = datas.getCount();
        if (max < 0)
            max = this.pageSize;
        o.result = datas.getRange(start, max - 1);
        var result;
        try {
            result = reader.readRecords(o);
        } catch (e) {
            this.fireEvent("loadexception", this, o, response, e);
            callback.call(scope, null, params, false);
            return;
        }
        this.fireEvent("load", this, o, arg);
        callback.call(scope, result, arg, true);
    },
    /*
     * loadFromCache:function(params, reader, callback, scope, arg){ var
     * datas=this.quickSearch(params,this.getData()); var
     * o={rowCount:datas.getCount(),result:[]}; var start=0,limit=-1;
     * if(params.start)start=Ext.num(parseInt(params.start),0);
     * if(params.limit)limit=Ext.num(parseInt(params.limit),-1); if(limit<0)limit=this.pageSize;
     * var max=datas.getCount(); if(limit>0)max=start+limit; if(max<0)max =
     * this.pageSize; if(max>datas.getCount())max=datas.getCount();
     * //console.dir(params); o.result=datas.getRange(start,max-1); var result;
     * try { result = reader.readRecords(o); }catch(e){
     * this.fireEvent("loadexception", this, o, response, e);
     * callback.call(scope, null, params, false); return; }
     * this.fireEvent("load", this, o, arg); callback.call(scope, result, arg,
     * true); },
     */
    /**
     * 发送请求，首先从缓存里面尝试加载数据，如果缓存中没有数据，再发送http请求从服务器端请求数据
     * 
     * @param {}
     *            action
     * @param {}
     *            rs
     * @param {}
     *            params
     * @param {}
     *            reader
     * @param {}
     *            callback
     * @param {}
     *            scope
     * @param {}
     *            options
     */
    request: function(action, rs, params, reader, callback, scope, options) {
        params = params || {};
        if (this.fireEvent("beforeload", this, params) !== false) {
            if (this.getData().getCount()) {
                this.loadFromCache(params, reader, callback, scope, options);
            } else {
                Disco.Ext.CachedRemoteProxy.superclass.request.apply(this, arguments);
            }
        }
    },
    // private 读取数据，完成本地分页。
    onRead: function(action, o, response) {
        /*
         * if (!success) { this.fireEvent("loadexception", this, o, response);
         * o.request.callback.call(o.request.scope, null, o.request.arg, false);
         * return; }
         */
        if (this.enableCache) {
            try {
                var json = response.responseText;
                var obj = eval("(" + json + ")");
                if (obj && (obj.enableCache === false || obj.enableCache === true)) {
                    // this.store.remoteSort=obj.enableCache;
                    this.enableCache = obj.enableCache;
                }
                if (this.enableCache) {
                    this.getData().clear();
                    this.getData().addAll(Ext.isArray(obj) ? obj : obj.result);
                    o.params.limit = this.pageSize ? this.pageSize : 10;
                    this.loadFromCache(o.params, o.reader, o.request.callback, o.request.scope, o.request.arg);
                } else {
                    var result;
                    try {
                        result = o.reader.read(response);
                    } catch (e) {
                        this.fireEvent("loadexception", this, o, response, e);
                        o.request.callback.call(o.request.scope, null, o.request.arg, false);
                        return;
                    }
                    this.fireEvent("load", this, o, o.request.arg);
                    o.request.callback.call(o.request.scope, result, o.request.arg, true);
                }
            } catch (e) {
                this.fireEvent("loadexception", this, o, response, e);
                o.request.callback.call(o.request.scope, null, o.request.arg, false);
                return;
            }
        } else {
            var result;
            try {
                result = o.reader.read(response);
            } catch (e) {
                this.fireEvent("loadexception", this, o, response, e);
                o.request.callback.call(o.request.scope, null, o.request.arg, false);
                return;
            }
            this.fireEvent("load", this, o, o.request.arg);
            o.request.callback.call(o.request.scope, result, o.request.arg, true);
        }
    },
    /**
     * 在本地缓存中直接添加数据。<br/>
     * 用于如果是在CrudPanel中，如果设置CrudPanel的storeType为CacheRemoteStore，<br/>
     * 那么在执行了添加业务对象操作后，要将新添加的业务对象直接通过该方法保存在本地缓存中。就不用去远端重新请求列表数据。<br/>
     * 
     * @param {Object}
     *            key 缓存对象的标识，一般用业务对象的id。
     * @param {Object}
     *            obj 缓存的对象。
     */
    add: function(key, obj) {// 增加
        this.getData().add(key, obj);
    },
    /**
     * 在本地缓存中直接删除数据。<br/>
     * 用于如果是在CrudPanel中，如果设置CrudPanel的storeType为CacheRemoteStore，<br/>
     * 那么在执行了删除业务对象操作后，要将删除的业务对象直接通过该方法在本地缓存中移除。就不用去远端重新请求列表数据。<br/>
     * 
     * @param {Object}
     *            obj 要删除的缓存对象。
     */
    remove: function(obj) {// 删除
        this.getData().remove(obj);
    },
    /**
     * 在本地缓存中直接删除数据。(使用key删除)<br/>
     * 用于如果是在CrudPanel中，如果设置CrudPanel的storeType为CacheRemoteStore，<br/>
     * 那么在执行了删除业务对象操作后，要将删除的业务对象直接通过该方法在本地缓存中移除。就不用去远端重新请求列表数据。<br/>
     * 
     * @param {Object}
     *            key 要删除的缓存对象的标识符。
     */
    removeKey: function(key) {// 删除
        this.getData().removeKey(key);
    },
    /**
     * 在本地缓存中直接添加或者修改数据。<br/>
     * 用于如果是在CrudPanel中，如果设置CrudPanel的storeType为CacheRemoteStore，<br/>
     * 那么在执行了添加/修改业务对象操作后，要将新添加/修改的业务对象直接通过该方法保存/更新在本地缓存中。就不用去远端重新请求列表数据。<br/>
     * 
     * @param {Object}
     *            key 缓存对象的标识，一般用业务对象的id。
     * @param {Object}
     *            obj 缓存的对象。
     */
    addOrUpdate: function(id, obj) {
        this.getData().replace(id, obj);
    },
    /**
     * 在本地缓存中直接修改数据。<br/>
     * 用于如果是在CrudPanel中，如果设置CrudPanel的storeType为CacheRemoteStore，<br/>
     * 那么在执行了修改业务对象操作后，要将修改的业务对象直接通过该方法更新在本地缓存中。就不用去远端重新请求列表数据。<br/>
     * 
     * @param {Object}
     *            key 缓存对象的标识，一般用业务对象的id。
     * @param {Object}
     *            obj 缓存的对象。
     */
    update: function(id, obj) {// 修改
        this.getData().replace(id, obj);
    }
});
Disco.Ext.CachedRemoteStore = function(c) {
    Disco.Ext.CachedRemoteStore.superclass.constructor.call(this, Ext.apply(c, {
        proxy: c.proxy || (!c.data ? new Disco.Ext.CachedRemoteProxy(c) : undefined),
        reader: new Ext.data.JsonReader(c, c.fields)
    }));
    if (!Disco.Ext.CachedRemoteProxy.DATAS[this.varName])
        Disco.Ext.CachedRemoteProxy.DATAS[this.varName] = new Ext.util.MixedCollection(true);
};
/**
 * 
 * @class Disco.Ext.CachedRemoteStore
 * @extends Ext.data.Store
 * 
 * 拥有本地缓存的store。<br/>
 * 该store能在第一次从服务器端获得数据后，将数据缓存在本地缓存中。之后所有的请求，如果在没有清空proxy中的数据情况下<br/>
 * 所有的查询都直接从本地缓存中查询。包括分页等操作。<br/>
 * 该store一般用于报表类查询。如果报表查询的条件没有改变的话，就可以多次的从本地缓存中列出相同的数据，减轻服务器端的报表查询压力。<br/>
 * 注意：该store后端对应的url，必须一次将所有数据都查询出来，分页的操作是在前端自动完成的。<br/>
 * 
 * <pre>
 * <code>
 *  //一个使用了CacheRemoteStore的报表的关键代码
 *  BaseAccountChartPanel=Ext.extend(BaseGridPanel,{//可以继承CrudListPanel，屏蔽所有的业务操作方法，也可以继承BaseGridPanel
 *  url:'chartHelper.java?cmd=stockNoOrders',//设置URL
 *  //在initComponent中，指定store为CachedRemoteStore。
 *  initComponent:function(){
 *  this.store = new Disco.Ext.CachedRemoteStore({
 *  id : &quot;id&quot;,
 *  url : this.url,
 *  varName:&quot;REPORT:stockNoOrder&quot;,
 *  disableClientFilter:true,
 *  pageSize:this.pageSize,
 *  root : &quot;result&quot;,
 *  remoteSort : false,
 *  totalProperty : &quot;rowCount&quot;,
 *  fields : this.storeMapping
 *  });
 *  this.store.paramNames.sort = &quot;orderBy&quot;;
 *  this.store.paramNames.dir = &quot;orderType&quot;;
 *  BaseAccountChartPanel.superclass.initComponent.call(this);
 *  }
 *  //查询方法，如果发现查询的条件没有改变，则直接从缓存中查询，如果查询条件已经修改
 *  //则调用this.store.prox.getData().clear()方法清空缓存。然后重新从服务器端请求数据。
 *  quickSearch:function(){
 *  var parsep = this.parseParams();
 *  var tag = parsep === undefined||parsep === true;
 *  this.store.proxy.getData().clear();
 *  this.store.searchKeys=Ext.urlEncode(this.store.baseParams);
 *  if(this.forceReload===true){
 *  this.store.baseParams.forceReload = true;
 *  this.forceReload = false;
 *  }
 *  if(!tag){
 *  return false;
 *  }
 *  this.refresh();
 *  },
 * </code>
 * </pre>
 */
Ext.extend(Disco.Ext.CachedRemoteStore, Ext.data.Store, {
    /**
     * 对外提供的重新刷新缓存的方法<br/> 该方法会清空当前store对应缓存中的数据，并重新从服务器端加载数据
     */
    refreshCache: function() {// 刷新缓存数据,重新从服务器端加载数据
        this.proxy.getData().clear();
        this.reload();
    },
    /**
     * 设置是否启用缓存<br/>
     * 如果设置为true，则默认开启缓存。也可以在运行状态中通过设置enableCache(false)来动态关闭store的缓存功能
     * 
     * @param {Boolean}
     *            b 是否开启缓存
     */
    enableCache: function(b) {
        this.proxy.enableCache = b === window.undefined ? true : !!!b;
    },
    /**
     * 判断当前store是否开启缓存功能
     * 
     * @return {Boolean} ret 返回当前store是否开启了缓存功能
     */
    isCache: function() {
        return this.proxy.enableCache;
    },
    // private
    sortFn: function(a, b) {
        if (!a || b || !a.toUpperCase || !b.toUpperCase) {
            var v1 = a, v2 = b;
            return v1 > v2 ? 1 : (v1 < v2 ? -1 : 0);
        }
        var i = a.toUpperCase() == b.toUpperCase();
        if (!i)
            return a.toUpperCase() < b.toUpperCase() ? -1 : 1;
        else {
            var ret = 0;
            for (var i = 0; i < a.length; i++) {
                if (a[i] !== b[i]) {
                    return a[i] < b[i] ? 1 : -1;
                }
            }
            return ret;
        }
    },
    // private
    sortData: function(f, direction) {
        direction = direction || 'ASC';
        var st = this.fields.get(f).sortType;
        var fn = this.sortFn;
        this.data.sort(direction, fn);
        if (this.snapshot && this.snapshot != this.data) {
            this.snapshot.sort(direction, fn);
        }
    },
    // private
    sort: function(fieldName, dir) {
        if (this.isCache === false) {
            Disco.Ext.CachedRemoteStore.superclass.sort.call(this, fieldName, dir);
            return;
        }
        var f = this.fields.get(fieldName);
        if (!f) {
            return false;
        }
        if (!dir) {
            if (this.sortInfo && this.sortInfo.field == f.name) { // toggle
                // sort
                // dir
                dir = (this.sortToggle[f.name] || "ASC").toggle("ASC", "DESC");
            } else {
                dir = f.sortDir;
            }
        }
        var st = (this.sortToggle) ? this.sortToggle[f.name] : null;
        var si = (this.sortInfo) ? this.sortInfo : null;

        this.sortToggle[f.name] = dir;
        this.sortInfo = {
            field: f.name,
            direction: dir
        };
        if (!this.remoteSort) {
            this.applySort();
            this.fireEvent("datachanged", this);
        } else {
            var direction = dir || 'ASC';
            var st2 = this.fields.get(f.name).sortType;
            var sortFn = this.sortFn;
            var fn = function(r1, r2) {
                var v1 = st2(r1[f.name]), v2 = st2(r2[f.name]);
                return sortFn(v1, v2);
            };
            this.proxy.getData().sort(direction, fn);
            if (!this.load(this.lastOptions)) {
                if (st) {
                    this.sortToggle[f.name] = st;
                }
                if (si) {
                    this.sortInfo = si;
                }
            }
        }
    }
});

/**
 * @class Disco.Ext.CachedRemoteObject
 * @extends Ext.util.Observable
 * 
 * 本地缓存对象<br/>
 */
Disco.Ext.CachedRemoteObject = function(varName, url, collectionType, shareCache) {
    /**
     * @cfg {Stirng} varName 缓存的名称
     */
    this.varName = varName;
    /**
     * @cfg {Stirng} url 获取数据的url
     */
    this.url = url;
    /**
     * @cfg {Stirng} collectionType 集合类型，默认为Ext.util.MixedCollection
     */
    this.collectionType = collectionType;
    /**
     * @cfg {Boolean} shareCache 是否共享缓存
     *      如果是共享缓存，数据缓存在Disco.Ext.CachedRemoteObject.shareCaches中，如果没有使用共享缓存，数据放在Disco.Ext.CachedRemoteObject.DATAS
     */

    /**
     * @event load 在缓存store加载数据的时候(不论是从缓存中加载还是真实加载)后抛出
     * @param {Object}
     *            加载的数据
     */
    this.addEvents("load");
    if (shareCache) {
        if (!Disco.Ext.CachedRemoteObject.shareCaches[varName])
            Disco.Ext.CachedRemoteObject.shareCaches[varName] = eval("new " + (collectionType || "Ext.util.MixedCollection") + "()");
    }
    Disco.Ext.CachedRemoteObject.superclass.constructor.call(this);
}
/**
 * 全局的缓存</br> ==Disco.Ext.CachedRemoteObject.DATAS
 */
Disco.Cache = Disco.Ext.CachedRemoteObject.DATAS = {};
/**
 * 全局的共享缓存<br/> ==Disco.Ext.CachedRemoteObject.shareCaches
 */
Disco.shareCaches = Disco.Ext.CachedRemoteObject.shareCaches = {};

/**
 * @class Disco.Ext.CachedRemoteObject
 * @extends Ext.util.Observable
 * 
 * 本地缓存对象<br/>
 */
Ext.extend(Disco.Ext.CachedRemoteObject, Ext.util.Observable, {
    varName: null,
    url: null,
    /**
     * 从缓存中得到数据
     * 
     * @return {Object} 得到的数据
     */
    getData: function() {
        var obj = Disco.Ext.CachedRemoteObject.DATAS[this.varName];
        return obj;
    },
    // private
    onload: function(callback) {
        this.fireEvent("load", this, Disco.Ext.CachedRemoteObject.DATAS[this.varName]);
        if (callback)
            callback();
    },
    /**
     * 刷新缓存数据,重新从服务器端加载数据
     */
    clearData: function() {// 
        var obj = Disco.Ext.CachedRemoteObject.DATAS[this.varName];
        if (obj.clear)
            obj.clear();
        else
            obj = null;
    },
    load: function(callback, params) {
        if (Disco.Ext.CachedRemoteObject.DATAS[this.varName]) {
            this.onload.call(this, callback, params);
            return;
        }
        if (this.varName && this.url) {
            Disco.Ext.Util.loadJSON2Collection("Disco.Ext.CachedRemoteObject.DATAS['" + this.varName + "']", this.url, this.onload.createDelegate(this, [callback]), Disco.shareCaches[this.varName], this.collectionType);
        }
    }
});
CachedRemoteObject = Disco.Ext.CachedRemoteObject;

/**
 * @class Disco.Ext.MemoryTreeLoader
 * @extends Ext.tree.TreeLoader
 * 
 * 支持本地缓存节点的树加载器<br/> 一般在系统中不经常变动的树状结构都使用该缓存树加载器。<br/>
 * 在不同的模块中可以使用同一个树加载器。一般设置为Global下的一个属性。
 * 
 * <pre>
 * <code>
 * if (!Global.platformMenuLoader) {
 * 	Global.platformMenuLoader = new Disco.Ext.MemoryTreeLoader({
 * 				iconCls : 'disco-tree-node-icon',
 * 				varName : &quot;Global.PLATFORM_MENU_NODES&quot;,
 * 				url : &quot;systemMenu.java?cmd=getTree&amp;pageSize=-1&amp;treeData=true&amp;all=true&quot;,
 * 				listeners : {
 * 					'beforeload' : function(treeLoader, node) {
 * 						treeLoader.baseParams.id = (node.id.indexOf('root') &lt; 0
 * 								? node.id
 * 								: &quot;&quot;);
 * 						if (typeof node.attributes.checked !== &quot;undefined&quot;) {
 * 							treeLoader.baseParams.checked = false;
 * 						}
 * 					}
 * 				}
 * 			});
 * }
 * </code>
 * </pre>
 * 
 */
Disco.Ext.MemoryTreeLoader = function(config) {
    Disco.Ext.MemoryTreeLoader.superclass.constructor.call(this, config);
    if (this.varName && (this.dataUrl || this.url)) {
        this.remoteObject = new Disco.Ext.CachedRemoteObject(this.varName, (this.url || this.dataUrl), "Disco.Ext.TreeCollection", true);
        if (!this.lazyLoad) {
            this.remoteObject.load();
        }
    }
    this.shareCache = Disco.Ext.CachedRemoteObject.shareCaches[this.varName];
}
/**
 * @class Disco.Ext.MemoryTreeLoader
 * @extends Ext.tree.TreeLoader
 * 
 * 
 */
Ext.extend(Disco.Ext.MemoryTreeLoader, Ext.tree.TreeLoader, {
    /**
     * @cfg (String} varName
     * 
     * 配置缓存的名称
     */
    varName: null,
    /**
     * @cfg {Object} remoteObject
     * 
     * 可以自己配置缓存树加载器数据缓存对象。默认使用Disco.Ext.CachedRemoteObject
     */
    remoteObject: null,
    dataProxy: false,
    autoLeaf: true,
    remoteDataProxy: null,
    /**
     * @cfg {Function|Object} transferNode 配置创建树节点的方法或属性。<br/>
     *      如果该属性是一个方法，则会传入从后台请求到的每一个节点对象作为该方法的参数。<br/>
     *      如果该属性是一个对象，则会自动讲该对象中的属性加入到节点对象中。<br/>
     */
    transferNode: window.undefined,
    /**
     * 从一个树节点级联向下完成某一个funcion
     * 
     * @param {Ext.data.Node}
     *            node 开始的节点
     * @param {Function}
     *            fn 对每一个节点要执行的方法
     * @param {Object}
     *            scope function执行的作用域
     * @param {Object|Array}
     *            args 执行function的参数
     */
    cascadeNode: function(node, fn, scope, args) {
        if (fn.apply(scope || this, args || [node]) !== false) {
            if (node.children && node.children.length) {
                for (var i = 0, cs = node.children; i < cs.length; i++)
                    this.cascadeNode(cs[i], fn, scope, args);
            }
        }
    },
    /**
     * 从一个指定节点开始向下寻找匹配指定id的节点
     * 
     * @param {Object}
     *            id 要匹配的id值
     * @param {Ext.data.Node}
     *            node 开始寻找的节点
     * @return {Ext.data.Node} 找到的匹配节点
     */
    deepFindNode: function(id, node) {
        var ret = null;
        this.cascadeNode(node, function(n) {
            if (n.id == id) {
                ret = n;
                return false;
            }
        });
        return ret;
    },
    /**
     * 从一组定节点中寻找匹配指定id的节点
     * 
     * @param {Object}
     *            id
     * @param {Object|Array}
     *            objs
     *            指定的节点。如果是数组，则将数组中的节点作为寻找的对象。如果是一个Object，则将对象下的item属性作为寻找的对象。
     * @return {Object} 找到的节点对象
     */
    findNode: function(id, objs) {
        if (id == "root")
            return Ext.isArray(objs) ? objs : objs.items;
        else {
            var ret = this.deepFindNode(id, {
                id: "root",
                children: (Ext.isArray(objs) ? objs : objs.items)
            });

            return ret && ret.children ? ret.children : null;
        }
    },
    /**
     * 加载节点下的数据
     * 
     * @param {Ext.data.Node}
     *            node 要加载下级数据的节点
     * @param {Function}
     *            callback 在加载完成后的回调方法
     */
    load: function(node, callback) {
        if (this.clearOnLoad) {
            while (node.firstChild) {
                node.removeChild(node.firstChild);
            }
        }
        var objs = this.remoteObject.getData();
        if (objs && objs.length)
            node.attributes.children = this.findNode(node.id, objs);
        if (this.doPreload(node)) {
            if (typeof callback == "function") {
                callback();
            }
        } else if (this.dataUrl || this.url) {
            this.requestData(node, callback);
        }
    },
    doPreload: function(node) {
        if (node.attributes.children && node.attributes.children.length) {
            if (node.childNodes.length < 1) { // preloaded?
                var cs = node.attributes.children;
                node.beginUpdate();
                for (var i = 0, len = cs.length; i < len; i++) {
                    var o = Ext.apply({}, cs[i]);
                    var cn = this.createNode(o);
                    if (cn === false)
                        continue;
                    var cn = node.appendChild(this.createNode(o));
                    if (this.preloadChildren) {
                        this.doPreload(cn);
                    }
                }
                node.endUpdate();
            }
            return true;
        } else {
            return false;
        }
    },
    /**
     * @cfg {Object} baseAttrs 为每一个节点对象配置基础的属性。这些属性会自动加到每一个节点对象上
     */
    // private
    createNode: function(attr) {
        attr = Ext.apply({}, attr);
        if (this.baseAttrs) {
            Ext.applyIf(attr, this.baseAttrs);
        }
        if (this.transferNode != window.undefined) {
            if (typeof this.transferNode == 'function') {
                attr = this.transferNode(attr);
            } else if (typeof this.transferNode == 'object') {
                for (var o in this.transferNode) {
                    if (this.transferNode.hasOwnProperty(o) && Ext.isEmpty(attr[o])) {
                        attr[o] = attr[this.transferNode[o]];
                    }
                }
            }
        }
        if (this.autoLeaf === true) {
            if (!attr.children || !attr.children.length) {
                attr.leaf = true;
            }
        }
        return Disco.Ext.MemoryTreeLoader.superclass.createNode.call(this, attr)
    },
    // private
    processResponse: function(response, parentNode, callback) {
        var json = response.responseText;
        try {
            var o = eval("(" + json + ")");

            var collection = Disco.Ext.CachedRemoteObject.shareCaches[this.varName];
            collection.clear();
            collection.addAll(o);
            Disco.Ext.CachedRemoteObject.DATAS[this.varName] = collection;
            o = collection.items;
            parentNode.beginUpdate();
            collection.each(function(node) {
                var n = this.createNode(node);
                if (n)
                    parentNode.appendChild(n);
            }, this);
            parentNode.endUpdate();
            if (typeof callback == "function") {
                callback(this, parentNode);
            }
        } catch (e) {
            this.handleFailure(response);
        }
    }
});
Ext.ns('Disco.Ext.Tree');

/**
 * 
 * @class BaseGridList
 * @extends Ext.Panel
 * 
 * 一个基础的包含了grid的panel组件。该组件一般用于报表等只需要grid的情况。
 * 
 * <pre>
 * <code>
 * //一个简单的包装基础报表的基础类
 * BaseOrderChartPanel = Ext.extend(BaseGridList, {
 * 	autoLoadGridData : false,
 * 	pagingToolbar : false,
 * 	gridBorder : false,
 * 	showMenu : false,
 * 	ableShowPic : false,
 * 	readOnlyNumRender : function(v) {
 * 		if (v == 0)
 * 			return &quot;&quot;;
 * 		return v;
 * 	},
 * 	createQuery : function() {
 * 	},
 * 	createButtonToolBar : function() {
 * 	},
 * 	createToolbar : function() {
 * 		this.createQuery();
 * 		var search_btn = this.createButtonToolBar();
 * 		var fixBtn = [&quot;-&quot;, {
 * 					text : &quot;查询&quot;,
 * 					iconCls : 'advance-search-icon',
 * 					handler : this.quickSearch,
 * 					scope : this
 * 				}, {
 * 					id : &quot;btn_refresh&quot;,
 * 					text : &quot;刷新&quot;,
 * 					iconCls : 'f5',
 * 					handler : this.resetSearch,
 * 					scope : this
 * 				}, {
 * 					text : &quot;打印&quot;,
 * 					iconCls : &quot;print-icon&quot;,
 * 					handler : this.printList,
 * 					scope : this
 * 				}];
 * 		Ext.each(fixBtn, function(o) {
 * 					search_btn.push(o);
 * 				}, this);
 * 		this.gridTbar = new Ext.Toolbar(search_btn);
 * 	},
 * 	resetSearch : function() {
 * 		this.gridTbar.items.each(function(o) {
 * 					if (o.isFormField) {
 * 						o.reset();
 * 					}
 * 				}, this);
 * 	},
 * 	quickSearch : function() {
 * 		var tag = this.parseParams() === undefined
 * 				|| this.parseParams() === true;
 * 		if (!tag) {
 * 			return false;
 * 		}
 * 		this.refresh();
 * 	},
 * 	printList : function() {
 * 		var tag = this.parseParams() === undefined
 * 				|| this.parseParams() === true;
 * 		if (!tag) {
 * 			return false;
 * 		}
 * 		this.parseParams();
 * 		var s = Ext.urlEncode(this.store.baseParams);
 * 		var win = new Ext.Window({
 * 			title : &quot;打印窗口&quot;,
 * 			html : &quot;&lt;iframe width='100%' frameborder='no' style='background:#FFF' border=0 height='100%' src ='&quot;
 * 					+ this.url + &quot;&amp;print=true&amp;&quot; + s + &quot;' &gt;&quot;
 * 		});
 * 		win.show();
 * 		win.fitContainer();
 * 		win.center();
 * 	},
 * 	showPic : function(grid, rowIndex, e) {
 * 		var record = grid.getStore().getAt(rowIndex); // Get the Record
 * 		if (!record.get(&quot;productId&quot;))
 * 			return false;
 * 		if (!this.chooser) {
 * 			this.chooser = new ImageChooser({
 * 						url : 'product.java?cmd=loadPic',
 * 						width : 500,
 * 						height : 400
 * 					});
 * 		}
 * 		this.chooser.setParams({
 * 					id : record.get(&quot;productId&quot;)
 * 				});
 * 		this.chooser.show(Ext.fly(e.getRelatedTarget()));
 * 	},
 * 	initComponent : function() {
 * 		this.cm = this.getColumnModel();
 * 		this.createToolbar();
 * 		this.gridConfig = {
 * 			enableHdMenu : false,
 * 			plugins : [new Ext.ux.grid.GridSummary()]
 * 		};
 * 		BaseOrderChartPanel.superclass.initComponent.call(this);
 * 		if (this.ableShowPic) {
 * 			this.grid.on(&quot;rowdblclick&quot;, this.showPic, this);
 * 		}
 * 	}
 * });
 * </code>
 * </pre>
 */
BaseGridList = Ext.extend(Ext.Panel, {
    /**
     * @cfg {String} layout 默认为fit
     */
    layout: "fit",
    /**
     * @cfg {Boolean} loadData 在页面加载完成后是否自动加载grid内容
     */
    loadData: false,
    /**
     * @cfg {Integer} pageSize 每页显示的条数
     */
    pageSize: 10,
    /**
     * @cfg {Boolean} closable 页面是否显示关闭按钮
     */
    closable: true,
    autoScroll: true,
    /**
     * @cfg {Boolean} pagingToolbar 是否显示分页组件 默认是
     */
    pagingToolbar: true,
    /**
     * @cfg {Boolean} gridForceFit 强制表格自动适应 默认是
     */
    gridForceFit: true,// 
    /**
     * @cfg {Object} gridViewConfig 自定义的表格视图配置对象。一般用于配置getRowClass等。
     */
    gridViewConfig: {},
    /**
     * @cfg {Object} gridConfig 自定义的表格配置对象。
     */
    gridConfig: {},
    /**
     * @cfg {Boolean} showMenu 在表格中是否显示右键菜单,默认显示
     */
    showMenu: true,
    /**
     * @cfg {Boolean} showMenu 在表格中是否显示右键菜单，默认不显示
     */
    menu_refresh: false,
    linkRenderer: Disco.Ext.Util.linkRenderer,
    linkRender: Disco.Ext.Util.linkRenderer,
    imgRender: Disco.Ext.Util.imgRender,
    booleanRender: Disco.Ext.Util.booleanRender,
    dateRender: Disco.Ext.Util.dateRender,
    userRender: Disco.Ext.Util.userRender,
    objectRender: Disco.Ext.Util.objectRender,
    typesRender: Disco.Ext.Util.typesRender,
    operaterRender: Disco.Ext.Util.operaterRender,
    /**
     * @cfg {Class} storeType
     *      配置列表的store的类型。默认是Ext.data.JsonStore，还可以配置成CacheStore等。
     */
    storeType: Ext.data.JsonStore,
    /**
     * @cfg {Object} storeConfig 自定义列表store的配置对象
     */
    storeConfig: {},
    emailRender: function(v) {
        return v ? v.email : "未知";
    },
    /**
     * 刷新当前列表store
     */
    refresh: function() {
        this.store.removeAll();
        this.store.reload({
            callback: function(rs) {
                if (rs && rs.length < 1) {
                    Ext.Msg.alert("提示", "没有符合条件的数据！");
                    Disco.Ext.Util.autoCloseMsg.defer(2000);
                }
            }
        });
    },
    /**
     * 显示列表右键菜单
     * 
     * @param {Ext.grid.GridPanel}
     *            g 要显示右键菜单的列表
     * @param {Integer}
     *            i 如果传入i，则首先选中列表第i列
     * @param {}
     *            e
     */
    showContextMenu: function(g, i, e) {
        var evn = e ? e : g;
        evn.preventDefault();
        if (i) {
            this.grid.getSelectionModel().selectRow(i, false);
        }
        this.menu.showAt(evn.getPoint());
    },
    /**
     * 在列表的点击事件中执行由operateRender创建的column
     * 
     * @param {}
     *            grid
     * @param {}
     *            rowIndex
     * @param {}
     *            columnIndex
     * @param {}
     *            e
     */
    doOperate: function(grid, rowIndex, columnIndex, e) {
        var tag = e.getTarget("A", 3);
        if (tag) {
            var id = tag.getAttribute("theid");
            var cmd = tag.getAttribute("op");
            var cf = tag.getAttribute("cf");
            if (id && cmd && this.operate)
                this.operate(cmd, id, cf, grid, rowIndex, columnIndex, e);
        }
    },
    /**
     * @cfg {Array} menus 右键菜单项数组
     */
    /**
     * @cfg {Ext.data.Store} store 可以直接配置列表的store
     */
    /**
     * @cfg {Ext.grid.ColumnModel} cm 列表的ColumnModel
     */
    /**
     * @cfg {Object|Array} gridTbar 配置的列表的tbar
     */
    /**
     * @cfg {Boolean} gridBorder 配置列表panel的边框
     */
    /**
     * @cfg {Boolean} columnLock 配置列表是否开启列锁定功能，默认不开启
     */
    initComponent: function() {
        BaseGridList.superclass.initComponent.call(this);
        if (!this.store) {
            this.store = new this.storeType(Ext.apply({
                id: "id",
                url: this.url,
                root: "result",
                totalProperty: "rowCount",
                remoteSort: true,
                fields: this.storeMapping
            }, this.storeConfig));
        }
        this.store.paramNames.sort = "orderBy";
        this.store.paramNames.dir = "orderType";
        this.cm.defaultSortable = true;
        var viewConfig = Ext.apply({
            forceFit: this.gridForceFit
        }, this.gridViewConfig);
        var gridConfig = Ext.apply({}, {
            store: this.store,
            cm: this.cm,
            trackMouseOver: false,
            loadMask: true,
            viewConfig: viewConfig,
            bbar: (Ext.isFunction(this.buildGridBbar) ? this.buildGridBbar() : false) || (this.pagingToolbar ? new Ext.ux.PagingComBo({
                pageSize: this.pageSize,
                store: this.store
            }) : null)
        });
        Ext.apply(gridConfig, this.gridConfig);
        this.menus = this.menus || [];
        if (this.menu_refresh)
            this.menus[this.menus.length] = {
                id: "menu_refresh",
                cls: "x-btn-text-icon",
                icon: "images/icons/arrow_refresh.png",
                text: "刷新",
                handler: this.refresh,
                scope: this
            };
        this.menu = new Ext.menu.Menu({
            items: this.menus
        });
        // if(this.gridConfig)gridConfig=Ext.apply(this.gridConfig,gridConfig);
        if (this.gridTbar)
            gridConfig.tbar = this.gridTbar;
        if (this.gridBorder === false || this.gridBorder)
            gridConfig.border = this.gridBorder;
        if (this.columnLock && Ext.grid.LockingGridPanel) {
            if (!gridConfig.columns && gridConfig.cm) {
                gridConfig.columns = gridConfig.cm.config;
                delete gridConfig.cm;
            }
            this.grid = new Ext.grid.LockingGridPanel(gridConfig);
        } else
            this.grid = new Ext.grid.GridPanel(gridConfig);
        this.grid.on("rowcontextmenu", function(g, n, e) {
            if (!this.showMenu)
                return false;
            if (g.getSelectionModel().selectRow)
                g.getSelectionModel().selectRow(n);
            this.menu.showAt(e.getPoint());
            e.preventDefault();
        }, this);
        this.add(this.grid);
        if (this.loadData)
            this.store.load();
    }
});
/*
 * UserSelectCombo = Ext.extend(Ext.form.ComboBox, { editable : false,
 * searchByUser : function(text, v) { this.store.baseParams.searchKey = v;
 * this.store.reload(); }, initList : function() {
 * UserSelectCombo.superclass.initList.call(this); if (this.pageTb) {
 * this.pageTb.add("用户名"); this.pageTb.add({ xtype : "textfield", id :
 * "searchKey", width : 50, listeners : { "change" : this.searchByUser, scope :
 * this } }); this.pageTb.addButton({ text : "查询" }); } } });
 * Ext.reg('userselectcombo', UserSelectCombo);
 */
/**
 * 
 * @class HTMLEditor
 * @extends Ext.form.HtmlEditor
 * 
 * 在Ext.form.HtmlEditor上面增加了一些实用方法。
 * 
 * @xtype myhtmleditor
 */
HTMLEditor = Ext.extend(Ext.form.HtmlEditor, {
    // enableFont:false,
    /**
     * @type String codeStyle 配置的格式化代码样式
     */
    codeStyle: '<br/><pre style="border-right: #999999 1px dotted; padding-right: 5px; border-top: #999999 1px dotted; padding-left: 5px; font-size: 12px; padding-bottom: 5px; margin-left: 10px; border-left: #999999 1px dotted; margin-right: 10px; padding-top: 5px; border-bottom: #999999 1px dotted; background-color: #eeeeee">{0}</pre><br/>',
    /**
     * @cfg {Array} keys 在HtmlEditor上面绑定的快捷键
     */
    onRender: function(ct, position) {
        HTMLEditor.superclass.onRender.call(this, ct, position);
        if (this.keys) {
            if (!this.keys.length) {
                this.keyMap = new Ext.KeyMap(this.getEditorBody(), this.keys);
            } else {
                this.keyMap = new Ext.KeyMap(this.getEditorBody(), this.keys[0]);
                for (var i = 1; i < this.keys.length; i++)
                    this.keyMap.addBinding(this.keys[i]);
            }
            this.keyMap.stopEvent = true;
        }
    },
    /**
     * 选择表情图标
     */
    showEmoteSelect: function() {
        emoteSelectWin.editor = this;
        emoteSelectWin.show();
    },
    /**
     * 添加图片方法。点击添加图片按钮，打开图片上传窗口。并能将上传的图片插入到当前编辑行。
     */
    addImage: function() {
        function insertImage() {
            var editor = this;
            win.upload(function(ret) {
                if (ret) {
                    var s = "<br/><img src=" + ret.path;
                    if (ret.width)
                        s += " width=" + ret.width;
                    if (ret.height)
                        s += " height=" + ret.height;
                    s += " /><br/>";
                    editor.insertAtCursor(s);
                    win.close();
                }
            });
        };
        var win = new UploadImageWindow({
            modal: true,
            iconCls: "icon-img",
            buttons: [{
                text: "确定",
                handler: insertImage,
                scope: this
            }, {
                text: "取消",
                handler: function() {
                    win.close();
                }
            }]
        });
        win.show();
    },
    /**
     * 添加代码方法。点击添加代码，打开添加代码窗口。并能将添加的代码插入到当前编辑行。
     */
    addCode: function() {
        function insertCode() {
            var value = win.getComponent("codes").getValue();
            this.insertAtCursor(String.format(this.codeStyle, value));
            win.close();
        };
        var win = new Ext.Window({
            title: "添加代码",
            width: 500,
            height: 300,
            modal: true,
            iconCls: "icon-code",
            layout: "fit",
            items: {
                xtype: "textarea",
                id: "codes"
            },
            buttons: [{
                text: "确定",
                handler: insertCode,
                scope: this
            }, {
                text: "取消",
                handler: function() {
                    win.close();
                }
            }]
        });
        win.show();
    },
    /**
     * 给HtmlEditor添加按钮。默认添加【插入图片】，【插入代码】和【添加表情】三个按钮，顺序为16,17,18
     * 
     * @param {}
     *            editor
     */
    createToolbar: function(editor) {
        HTMLEditor.superclass.createToolbar.call(this, editor);
        this.tb.insertButton(16, {
            cls: "x-btn-icon",
            icon: "images/qq/img.gif",
            handler: this.addImage,
            scope: this
        });
        this.tb.insertButton(17, {
            cls: "x-btn-icon",
            icon: "images/qq/code.gif",
            handler: this.addCode,
            scope: this
        });
        this.tb.insertButton(18, {
            cls: "x-btn-icon",
            icon: "images/emote/main.png",
            handler: this.showEmoteSelect,
            scope: this
        });

    },
    /**
     * @cfg {Integer} maxLength 在HtmlEditor中允许输入的最大字数
     */
    /**
     * 验证HtmlEditor的值。 如果配置了maxLength，则如果编辑器中的字数大于maxLength，则编辑器不可用。
     * 
     * @param {}
     *            value
     * @return {Boolean}
     */
    validateValue: function(value) {
        if (value.length > this.maxLength) {
            var s = String.format(this.maxLengthText, this.maxLength);
            this.markInvalid(s);
            return false;
        }
        return true;
    }
});
Ext.reg('myhtmleditor', HTMLEditor);

// 用来存放IFrames中的panel引用，以id为单位
IFrames = {};
Disco.Ext.CrudListPanel = function(config) {
    var c = config || {};
    Ext.apply(this, c);
    this.addEvents("saveobject", "removeobject");
    Disco.Ext.CrudListPanel.superclass.constructor.call(this);
};

/**
 * 
 * @class Disco.Ext.CrudListPanel
 * @extends Ext.util.Observable
 * 
 * 和Disco.Ext.CrudPanel功能一样，唯一的区别在于在创建了Disco.Ext.CrudPanel之后，<br/>
 * 执行了initComponent方法，不会立刻的创建列表等组件，而必须要等到调用了list方法后，才会去创建这些组件。<br/>
 * 相当于完成了一个延迟创建组件的功能。
 */
Ext.extend(Disco.Ext.CrudListPanel, Ext.util.Observable, {
    layout: "fit",
    border: false,
    closable: true,
    autoScroll: true,
    /**
     * 导入说明
     * @type String
     */
    importExplain: "",
    /**
     * @cfg {Boolean} gridForceFit 业务对象列表页面列表是否支持宽度自适应。
     */
    gridForceFit: true,
    /**
     * @cfg {Object} viewWin 如果开启了查看业务流程，则该对象定义了查看窗口的样式。
     *      <ul>
     *      <li>{Integer} width 窗口宽度</li>
     *      <li>{Integer} height 窗口高度</li>
     *      <li>{String} title 窗口标题</li>
     *      </ul>
     */
    viewWin: {
        width: 650,
        height: 410,
        title: "详情查看"
    },
    /**
     * @cfg {Object} searchWin 如果开启了高级查询业务流程，则该对象定义了高级查询窗口的样式。
     *      <ul>
     *      <li>{Integer} width 窗口宽度</li>
     *      <li>{Integer} height 窗口高度</li>
     *      <li>{String} title 窗口标题</li>
     *      </ul>
     */
    searchWin: {
        width: 630,
        height: 300,
        title: "高级查询"
    },// 查询窗口的高度、宽度及标题
    /**
     * @cfg {Object} gridViewConfig 自定义的业务对象列表表格的视图样式配置。
     *      比如经常会自定义表格视图的getRowClass属性来在列表中控制不同状态的业务对象的显示方式。
     */
    gridViewConfig: {},// 表格显示视图的自定义配置
    /**
     * @cfg {Object} gridConfig 自定义的业务对象列表表格的配置。
     */
    gridConfig: {},// 表格的自定义配置
    /**
     * @cfg {Object} baseQueryParameter 定义的查询初始化参数
     *      该参数会一直绑定在业务对象列表的store上。在实际的开发中，一般用来区分类似于销售出库单，报损单等相同模型，近似逻辑的单据。
     */
    baseQueryParameter: {}
    // 查询初始化参数
});
Ext.apply(Disco.Ext.CrudListPanel.prototype, Disco.Ext.CrudFunction, {
    /**
     * @cfg {Function} afterList 该方法在list方法执行最后执行。即在初始化完成了所有页面组件后执行。<br/>
     *      可以在该方法中完成添加按钮等功能。
     */
    afterList: Ext.emptyFn,
    /**
     * @event saveobject 当保存或者修改业务对象成功后抛出的事件
     * 
     * @param {Disco.Ext.Util.CrudPanel}
     *            this CrudPanel自身
     * @param {Ext.form.BasicForm}
     *            form 提交的表单
     * @param {Ext.form.Action}
     *            action 提交表单绑定的acion对象。
     */
    /**
     * @event removeobject 当删除业务对象成功后抛出的事件
     * 
     * @param {Disco.Ext.Util.CrudPanel}
     *            this CrudPanel自身
     * @param {Ext.data.Record}
     *            r 删除的对象在列表中对应的record对象。
     * @param {Object}
     *            option 提交请求绑定的option对象。
     */
    /**
     * 该方法真正的创建了CrudListPanel中的所有业务组件，包括列表，按钮，菜单，CRUD窗口等。
     * 
     * @return {Ext.Panel} panel 调用这个方法后，才会返回创建的CrudPanel。<br/>
     *         如果是需要在某些组件（比如窗口或tab）中显示一个CrudListPanel，必须要放入调用了list()方法后返回的Panel。<br/>
     *         例如，一个CrudListPanel ：MyCrudListPanel，要放在window中，<br/> items:new
     *         MyCrudListPanel()//是不对的，因为这样没有panel
     * 
     * var p=new MyCrudListPanel();<br/> items:p.list()//这样才是对的。
     */
    list: function() {
        this.initComponent();
        this.checkAdnLoadColumnField();
        this.store = new Ext.data.JsonStore({
            id: this.storeId ? this.storeId : "id",
            url: this.formatUrl('list'),
            root: "result",
            totalProperty: "rowCount",
            remoteSort: true,
            autoDestroy: true,
            fields: this.storeMapping
        });
        if (Ext.objPcount(this.baseQueryParameter)) {
            this.store.on('beforeload', function(store, options) {
                Ext.apply(store.baseParams, this.baseQueryParameter);
            }, this);
        }
        this.store.baseParams = Ext.apply({}, {
            limit: this.pageSize || ""
        }, this.initQueryParameter || {});
        this.store.paramNames.sort = "orderBy";
        this.store.paramNames.dir = "orderType";

        var buttons = this.buildCrudOperator();

        var viewConfig = Ext.apply({
            forceFit: this.gridForceFit
        }, this.gridViewConfig);

        var gridConfig = Ext.apply(this.gridConfig, {
            store: this.store,
            stripeRows: true,
            trackMouseOver: false,
            loadMask: true,
            viewConfig: viewConfig,
            tbar: buttons,
            border: false,
            bbar: this.pagingToolbar ? new Ext.ux.PagingComBo({
                rowComboSelect: true,
                pageSize: this.pageSize,
                store: this.store,
                displayInfo: true
            }) : null
        });

        if (this.summaryGrid) {
            if (gridConfig.plugins) {
                if (typeof gridConfig.plugins == "object")
                    gridConfig.plugins = [gridConfig.plugins];
            } else
                gridConfig.plugins = [];
            gridConfig.plugins[gridConfig.plugins.length] = new Ext.ux.grid.GridSummary();
        }

        var columns = this.columns, cfg = {};
        columns = columns || this.cm.config;
        delete gridConfig.cm;

        columns = columns.copy();
        if (this.gridRowNumberer) {
            columns.unshift(new Ext.grid.RowNumberer({
                header: '序号',
                width: 36
            }));
        }

        if ((!gridConfig.sm && !gridConfig.selModel) && this.gridSelModel == 'checkbox') {
            cfg.sm = new Ext.grid.CheckboxSelectionModel();
            if (columns[0] instanceof Ext.grid.RowNumberer) {
                columns.splice(1, 0, cfg.sm);
            } else {
                columns.unshift(cfg.sm);
            }
        }
        cfg.columns = columns;

        gridConfig = Ext.applyIf(cfg, gridConfig);

        if (this.columnLock && Ext.grid.LockingGridPanel) {
            this.grid = new Ext.grid.LockingGridPanel(gridConfig);
        } else {
            this.grid = new Ext.grid.GridPanel(gridConfig);
        }
        this.grid.colModel.defaultSortable = true;// 设置表格默认排序
        this.panel = new Ext.Panel({
            id: this.id,
            title: this.title,
            closable: this.closable,
            autoScroll: this.autoScroll,
            layout: this.layout,
            border: this.border,
            listeners: {
                close: function() {
                    delete this.panel;
                },
                scope: this
            }
        });
        this.panel.add(this.grid);
        this.loadOperatorsPermission();
        // 双击表格行进入编辑状态
        this.initCrudEventHandler();
        // this.disableOperaterItem("btn_edit","btn_remove","btn_view");
        this.afterList();
        if (this.autoLoadGridData)
            this.store.load();
        this.panel.service = this;
        return this.panel;
    }
});

Ext.namespace("Ext.ux.panel");
/**
 * @class Ext.ux.panel.DDTabPanel
 * @extends Ext.TabPanel
 * 
 * 可拖拽的TabPanel。该tabpanel可以拖拽任何一个tab到指定的位置。
 */
Ext.ux.panel.DDTabPanel = Ext.extend(Ext.TabPanel, {
    arrowOffsetX: -9,
    arrowOffsetY: -8,
    border: false,
    enableTabScroll: true,
    layoutOnTabChange: true,
    autoScroll: true,
    resizeTabs: true,
    tabWidth: 136,
    minTabWidth: 136,
    titleLength: 100,
    initComponent: function() {
        Ext.ux.panel.DDTabPanel.superclass.initComponent.call(this);
        if (!this.ddGroupId) {
            this.ddGroupId = "dd-tabpanel-group-" + Ext.ux.panel.DDTabPanel.superclass.getId.call(this)
        }
    },
    afterRender: function() {
        Ext.ux.panel.DDTabPanel.superclass.afterRender.call(this);
        this.arrow = Ext.DomHelper.append(Ext.getBody(), '<div class="dd-arrow-down"></div>', true);
        this.arrow.hide();
        var a = this.ddGroupId;
        this.dd = new Ext.ux.panel.DDTabPanel.DropTarget(this, {
            ddGroup: a
        });
    },
    initTab: function(b, a) {
        Ext.ux.panel.DDTabPanel.superclass.initTab.call(this, b, a);
        var c = this.id + "__" + b.id;
        var id = this.stripWrap.id;
        Ext.applyIf(b, {
            allowDrag: true
        });
        Ext.apply(b, {
            position: (a + 1) * 2,
            ds: new Ext.dd.DragSource(c, {
                ddGroup: this.ddGroupId,
                dropEl: b,
                dropElHeader: Ext.get(c, true),
                scroll: false,
                onStartDrag: function() {
                    this.constrainTo(id);
                    this.getProxy().ghost.dom.innerHTML = String.format("<div style='width:100px;overflow:hidden;'>{0}</div>", this.dropEl.title);
                    if (this.dropEl.iconCls) {
                        this.getProxy().getGhost().select(".x-tab-strip-text").applyStyles({
                            paddingLeft: "1px"
                        })
                    }
                },
                onMouseDown: function(d) {
                    if (!this.dropEl.isVisible()) {
                        this.dropEl.show();
                    }
                }
            }),
            enableDrag: function() {
                this.allowDrag = true;
                return this.ds.unlock()
            },
            disableDrag: function() {
                this.allowDrag = false;
                return this.ds.lock()
            }
        });
        if (b.allowDrag) {
            b.enableDrag()
        } else {
            b.disableDrag()
        }
    },
    /*
     * onRemove : function(a) { Ext.destroy(a.ds.proxy, a.ds);
     * Ext.ux.panel.DDTabPanel.superclass.onRemove.call(this, b, a) },
     */
    onDestroy: function() {
        Ext.destroy(this.dd, this.arrow);
        Ext.ux.panel.DDTabPanel.superclass.onDestroy.call(this)
    }
});
Ext.ux.panel.DDTabPanel.DropTarget = Ext.extend(Ext.dd.DropTarget, {
    constructor: function(b, a) {
        this.tabpanel = b;
        Ext.ux.panel.DDTabPanel.DropTarget.superclass.constructor.call(this, b.stripWrap, a)
    },
    notifyOver: function(q, l, j) {
        var m = this.tabpanel.items;
        var p = m.length;
        if (!l.within(this.getEl())) {
            return "x-dd-drop-nodrop"
        }
        var r = this.tabpanel.arrow;
        var o = this.el.getY();
        var f;
        var k = l.getPageX();
        for (var h = 0; h < p; h++) {
            var d = m.itemAt(h);
            var c = d.ds.dropElHeader;
            var n = c.getX();
            var a = n + c.dom.clientWidth / 2;
            if (k <= a) {
                f = n;
                break
            }
        }
        if (typeof f == "undefined") {
            var b = m.itemAt(p - 1);
            if (b) {
                var g = b.ds.dropElHeader.dom;
                f = (new Ext.Element(g).getX() + g.clientWidth) + 3
            }
        }
        r.setTop(o + this.tabpanel.arrowOffsetY).setLeft(f + this.tabpanel.arrowOffsetX).show();
        return this.dropAllowed;
    },
    notifyDrop: function(p, k, f) {
        this.tabpanel.arrow.hide();
        var m = this.tabpanel.items;
        var o = m.length;
        var h = k.getPageX();
        var l = o;
        p.dropEl.position = o * 2 + 1;
        var j = this.tabpanel.items.indexOf(p.dropEl);
        for (var d = 0; d < o; d++) {
            var c = m.itemAt(d);
            var b = c.ds.dropElHeader;
            var n = b.getX();
            var a = n + b.dom.clientWidth / 2;
            if (h <= a) {
                if (j < d) {
                    d -= 1
                }
                break
            }
        }
        p.proxy.hide();
        var g = p.dropEl.ownerCt.remove(p.dropEl, false);
        var c = this.tabpanel.getComponent(d);
        if (c && c.tabFixed) {
            d = this.getLastTabFixed()
        }
        if (this.tabpanel.items.getCount() < d) {
            d = this.tabpanel.items.getCount()
        }
        this.tabpanel.insert(d, g);
        this.tabpanel.activate(g);
        /*
         * var q =
         * this.tabpanel.getTabEl(g).childNodes[1].firstChild.firstChild;
         * if (q) { q.style.width =
         * Ext.value(this.tabpanel.minTabWidth,120) + "px" }
         */
        return true
    },
    getLastTabFixed: function() {
        var a = this.tabpanel.items.filter("tabFixed", true);
        return a.getCount()
    },
    notifyOut: function(a, c, b) {
        this.tabpanel.arrow.hide()
    }
});
Ext.reg("ddtabpanel", Ext.ux.panel.DDTabPanel);

ChartTools = {
    createFusionChart: function(url, swf, chartId, width, height, options) {
        options = options || {};
        var myChart = new FusionCharts(swf, chartId, width, height, "0", "0");
        url = url || options.url;
        if (url) {
            if (url.indexOf("?") < 1)
                url += "?";
            if (options.params)
                url += Ext.urlEncode(options.params);
            myChart.setDataURL(url);
        }
        if (options.renderTo && url) {
            myChart.render(options.renderTo);
        }
        return myChart;
    },
    // "FusionCharts/chart/Column3D.swf",
    createChart: function(url, swf, config) {
        config = config || {};
        var myChart = this.createFusionChart(url, swf, config.id, config.width, config.height, config);
        return myChart;
    },
    createColumn2D: function(url, config) {
        return this.createChart(url, "FusionCharts/chart/Column2D.swf", config);
    },
    createColumn3D: function(url, config) {
        return this.createChart(url, "FusionCharts/chart/Column3D.swf", config);
    },
    createGroupColumn2D: function(url, config) {
        return this.createChart(url, "FusionCharts/chart/MSColumn2D.swf", config);
    },
    createGroupColumn3D: function(url, config) {
        return this.createChart(url, "FusionCharts/chart/MSColumn3D.swf", config);
    },
    createPie2D: function(url, config) {
        return this.createChart(url, "FusionCharts/chart/Pie2D.swf", config);
    },
    createPie3D: function(url, config) {
        return this.createChart(url, "FusionCharts/chart/Pie3D.swf", config);
    },
    createBar2D: function(url, config) {
        return this.createChart(url, "FusionCharts/chart/Bar2D.swf", config);
    },
    createGroupBar2D: function(url, config) {
        return this.createChart(url, "FusionCharts/chart/MSBar2D.swf", config);
    },
    createGroupBar3D: function(url, config) {
        return this.createChart(url, "FusionCharts/chart/MSBar3D.swf", config);
    },
    createLine: function(url, config) {
        return this.createChart(url, "FusionCharts/chart/Line.swf", config);
    },
    createGroupLine: function(url, config) {
        return this.createChart(url, "FusionCharts/chart/MSLine.swf", config);
    }
};

ChartWindow = Ext.extend(Ext.Window, {
    max: 20,// 显示前几条
    store: null,// 数据项
    datas: null,// 直接把datas中的数据进行展示
    columnModel: null,// 表格模型
    url: "",// 远程数据加载url
    params: null,// 查询参数
    grid: null,// 直接拿一个表格来分析
    layout: "border",
    closeAction: "hide",
    buttonAlign: "center",
    tpl: new Ext.XTemplate('<chart caption="{title}"> ', '<tpl for="list">', '<set label="{name}" value="{value}" />', '</tpl></chart>'),
    initParams: function() {
        if (this.grid) {
            this.store = this.grid.store;
            if (!this.columnModel)
                this.columnModel = this.grid.getColumnModel();
        }
        if (this.columnModel) {
            var fields = [];
            if (this.columnModel.getColumnCount && this.columnModel.getDataIndex) {// 是表格的columnModel对象
                for (var i = 0; i < this.columnModel.getColumnCount(); i++) {
                    fields.push({
                        id: this.columnModel.getDataIndex(i),
                        title: this.columnModel.getColumnHeader(i)
                    });
                }
            } else {// 直接是描述可分析项目的数组
                fields = this.columnModel;
            }
            this.leftTree.root.attributes.children = [];
            for (var i = 0; i < fields.length; i++) {
                this.leftTree.root.attributes.children.push({
                    text: fields[i].title,
                    id: fields[i].id,
                    leaf: true
                });
            }
            this.leftTree.root.reload();
            this.btn_field.store.loadData(fields);
            this.btn_field.setValue(this.field || "");
        }
        this.btn_max.setValue(this.max);
        this.setTitle(this.title);
    },
    loadChart: function() {
        this.initParams();
        var objs = {
            title: this.title,
            list: [],
            order: false
        };
        var orderBy = "";
        if (this.btn_orderASC.getValue())
            orderBy = "ASC";
        else if (this.btn_orderDESC.getValue())
            orderBy = "DESC";
        if (this.datas && this.datas.length) {
            objs.list = this.datas;
        } else if (this.store) {
            if (orderBy)
                this.store.sort(this.field, orderBy);
            for (var i = 0; i < this.store.getCount() && i < this.max; i++) {
                var r = this.store.getAt(i).data;
                var obj = {
                    name: r[this.label || "sn"],
                    value: r[this.field]
                };
                objs.list.push(obj);
            }
            objs.order = true;
        }
        var xml = "";
        if (!objs.list.length && this.url) {
            var response = Ext.lib.Ajax.syncRequest("POST", this.url, Ext.apply(this.params, {
                sort: orderBy
            }));
            xml = response.conn.responseText;
        } else {
            if (!objs.order && orderBy) {// 如果是直接传的数据,则需要手动
                var f = "value";
                var dsc = orderBy.toUpperCase() == "DESC" ? -1 : 1;
                objs.list.sort(function(a, b) {
                    if (a[f] == b[f])
                        return 0;
                    else
                        return a[f] > b[f] ? 1 * dsc : -1 * dsc;
                });
            }
            xml = this.tpl.applyTemplate(objs);
        }
        var createChart = ChartTools.createColumn3D;
        if (this.btn_pie.getValue())
            createChart = ChartTools.createPie3D;
        else if (this.btn_pie2d.getValue())
            createChart = ChartTools.createPie2D;
        else if (this.btn_bar2d.getValue())
            createChart = ChartTools.createColumn2D;
        var chartPanel = this.chartPanel;
        var b = chartPanel.body.getBox();
        try {
            this.chart = createChart.call(ChartTools, null, {
                width: b.width - 20,
                height: b.height - 10
            });
        } catch (e) {
            alert(e);
        }
        this.chart.setDataXML(xml);
        this.chart.render(chartPanel.body.dom);
    },
    // 根据查询条件刷新报表
    refreshChart: function() {
        this.max = this.btn_max.getValue();
        // this.field=this.btn_field.getValue();
        this.loadChart();
    },
    clickDeptNode: function(node) {
        this.params = Ext.apply({}, {
            field: node.attributes.id
        }, this.params);
        this.field = node.attributes.id;
        this.title = node.attributes.text;
        if (this.store)
            this.datas = null;
        this.refreshChart();
    },
    initComponent: function() {
        this.btn_field = new Disco.Ext.SmartCombox({
            name: "colorInput",
            hiddenName: "colorInput",
            fieldLabel: "colorInput",
            displayField: "title",
            valueField: "id",
            allowBlank: false,
            width: 100,
            // selectedClass:'x-combo-color-selected', //
            // icon.css
            store: new Ext.data.JsonStore({
                fields: ["id", "title"]
            }),
            editable: false,
            mode: 'local',
            triggerAction: 'all',
            emptyText: '请选择...'
        });
        this.btn_max = new Ext.form.NumberField({
            name: "max",
            xtype: "numberfield",
            width: 30
        });
        this.btn_orderType = new Ext.form.ComboBox(Ext.apply({}, {
            width: 80
        }, Disco.Ext.Util.buildCombox("type", "是否", [["由低到高", "ASC"], ["由高到低", "DESC"]], "DESC", true)));
        this.btn_orderASC = new Ext.form.Radio({
            boxLabel: "由低到高",
            name: "orderTypes",
            handler: this.refreshChart,
            scope: this
        });
        this.btn_orderDESC = new Ext.form.Radio({
            boxLabel: "由高到低",
            name: "orderTypes",
            handler: this.refreshChart,
            scope: this
        });
        this.btn_bar = new Ext.form.Radio({
            boxLabel: "柱状图",
            cls: 'x-btn-text-icon',
            name: "types",
            handler: this.refreshChart,
            scope: this,
            checked: true
        });
        this.btn_pie = new Ext.form.Radio({
            boxLabel: "饼状图",
            cls: 'x-btn-text-icon',
            name: "types",
            handler: this.refreshChart,
            scope: this
        });
        this.btn_bar2d = new Ext.form.Radio({
            boxLabel: "柱状图2D",
            cls: 'x-btn-text-icon',
            name: "types",
            handler: this.refreshChart,
            scope: this
        });
        this.btn_pie2d = new Ext.form.Radio({
            boxLabel: "饼状图2D",
            cls: 'x-btn-text-icon',
            name: "types",
            handler: this.refreshChart,
            scope: this
        });
        this.btn_vdate1 = new Ext.form.DateField({
            format: "Y-m-d"
        });
        this.btn_vdate2 = new Ext.form.DateField({
            format: "Y-m-d"
        });
        Ext.apply(this, {
            width: Ext.getBody().getViewSize().width - 50,
            height: Ext.getBody().getViewSize().height - 20
        });

        ChartWindow.superclass.initComponent.call(this);
        this.leftTree = new Ext.tree.TreePanel({
            xtype: "treepanel",
            title: "分析项目",
            border: false,
            rootVisible: false,
            root: new Ext.tree.AsyncTreeNode({
                id: "root",
                text: "所有部门",
                expanded: true,
                children: [],
                loader: new Ext.tree.TreeLoader()
            }),
            listeners: {
                scope: this,
                click: this.clickDeptNode
            }
        });
        this.chartPanel = new Ext.Panel({
            html: "正在生成统计图..."
        });
        this.add({
            region: "west",
            width: 150,
            items: [this.leftTree]
        });
        this.add({
            region: "center",
            layout: "fit",
            items: this.chartPanel,
            tbar: ["最多分析数", this.btn_max, "排序方式:", this.btn_orderASC, this.btn_orderDESC, "-", "展示方式:", this.btn_bar, this.btn_pie, this.btn_bar2d, this.btn_pie2d, "    ", "-", {
                text: "开始分析",
                cls: 'x-btn-text-icon',
                handler: this.refreshChart,
                scope: this,
                icon: 'img/icons/application_side_expand.png'
            }]
        });
        this.on("show", this.loadChart, this);
    }
});
ChartWindow.showChart = function(config, callback) {
    if (!ChartWindow.win) {
        ChartWindow.win = new ChartWindow();
    }
    Ext.apply(ChartWindow.win, Ext.apply({
        store: null,// 数据项
        datas: null,// 直接把datas中的数据进行展示
        columnModel: null,// 表格模型
        url: "",// 远程数据加载url
        params: null,// 查询参数
        grid: null
        // 可以直接拿一个表格来进行分析
    }, config || {}));
    ChartWindow.win.show();
    if (callback)
        callback.call(ChartWindow.win);

};

/**
 * 显示查询结果预览窗口
 * 
 * @class SearchResultStatisticsWin
 * @extends Ext.Window
 */
SearchResultStatisticsWin = Ext.extend(Ext.Window, {
    title: "查询结果预览",
    width: 300,
    height: 300,
    layout: "fit",
    closeAction: "hide",
    initGrid: function() {
        var rs = [];
        if (this.data) {
            for (var name in this.data) {
                var o = {
                    name: name,
                    value: this.data[name]
                };
                rs.push(o);
            }
        }
        this.grid.store.removeAll();
        this.grid.store.loadData(rs);
        if (this.title)
            this.setTitle(this.title);
    },
    initComponent: function() {
        this.grid = new Ext.grid.GridPanel({
            viewConfig: {
                forceFit: true
            },
            store: new Ext.data.JsonStore({
                fields: ["name", "value"]
            }),
            cm: new Ext.grid.ColumnModel([{
                header: "项目名称",
                width: 100,
                sortable: false,
                dataIndex: 'name',
                id: 'name',
                menuDisabled: true
            }, {
                header: "值",
                width: 100,
                resizable: false,
                dataIndex: 'value',
                id: 'value',
                menuDisabled: true
            }])
        });
        SearchResultStatisticsWin.superclass.initComponent.call(this);
        this.on("show", this.initGrid, this);
        this.add(this.grid);
    }
});

/**
 * 
 */
SearchResultStatisticsWin.showWin = function(config) {
    if (!SearchResultStatisticsWin.win) {
        SearchResultStatisticsWin.win = new SearchResultStatisticsWin();
    }
    Ext.apply(SearchResultStatisticsWin.win, config);
    SearchResultStatisticsWin.win.show();
    return SearchResultStatisticsWin.win;
};

/**
 * @class Disco.Ext.MainAppService
 * 
 * 主应用框架平台的支持业务类。该类提供了一个应用平台中平台层面的基本操作。<br/> 比如打开应用模块，延迟加载应用模块js，IFrame模式打开应用等。<br/>
 * 该业务类作为MainTabPanel和MainSinglePanel的业务支持类。
 */
Disco.Ext.MainAppService = {
    /**
     * IFrame模式应用模块类缓存器。
     */
    IFrameClass: {},
    /**
     * 应用模块类缓存器。
     */
    NormalClass: {},
    /**
     * 得到应用平台是否是单Tab页模式。
     * 
     * @return {Boolean} ret是否是单Tab页模式。
     */
    getSingleTabMode: function() {
        return window.Global.Config.singleTabMode;
    },
    /**
     * 在Tab页中打开一个新的panel。如果该panel已经打开，则直接定位到该tab上。
     * 
     * @param {Object|String}
     *            如果是object，则得到panel.id，如果是string，直接作为查询panel的id。
     */
    openTab: function(panel, id) {
        var o = (typeof panel == "string" ? panel : id || panel.id);
        var tab = this.getComponent(o);
        if (tab) {
            this.setActiveTab(tab);
        } else if (typeof panel != "string") {
            if (!this.tabsCheck())
                return;
            panel.id = o;
            var p = this.add(panel);
            if (this.getSingleTabMode())
                this.closeAll(p);
            // if(this.items.getCount()>10)this.remove(1);
            this.setActiveTab(p);
        }
    },
    /**
     * 检查打开的panel是否达到了设置的最大tabpanel个数。
     * 
     * @return {Boolean} ret 是否达到最大tabpanel个数
     */
    tabsCheck: function() {
        if (this.items.getCount() > this.maxTabs) {
            Ext.Msg.alert("提示", "系统允许同时打开的面板数已经达到极限，请先关闭其它一打开的面板再重新进入");
            return false;
        }
        return true;
    },
    /**
     * 在弹出窗口中打开一个panel。默认的弹出窗口大小是全屏。如果要规定弹出窗口的大小，可以在传入的panel中通过配置inWinConfig对象来设置。<br/>
     * <ul>
     * inWinConfig:
     * <li>width:弹出窗口的宽度</li>
     * <li>height:弹出窗口的高度</li>
     * <li>modal:弹出窗口的模式</li>
     * <li>title:弹出窗口的名称</li>
     * </ul>
     * 
     * @param {Object}
     *            panel 要在窗口中打开的panel实例
     */
    openAppInWin: function(panel) {
        panel.elements = panel.elements.replace(",header", "");
        panel.inWinConfig = panel.inWinConfig || {};// window窗口附加配置 例如:
        // inWinConfig:{width:600,height:450}
        if (!this.appWin) {
            this.appWin = new Ext.Window(Ext.apply({
                width: Ext.getBody().dom.offsetWidth - 20,
                height: Ext.getBody().dom.offsetHeight - 20,
                closeAction: "hide",
                layout: "fit",
                modal: true,
                maximizable: true,
                title: panel.title,
                items: panel,
                listeners: {
                    maximize: function(win) {
                        win.doLayout();
                    },
                    restore: function(win) {
                        win.doLayout();
                    }
                }
            }, panel.inWinConfig));
            Ext.del(panel, 'inWinConfig');
        } else {
            this.appWin.remove(0);
            this.appWin.add(panel);
            this.appWin.setSize(panel.inWinConfig.width || (Ext.getBody().dom.offsetWidth - 20), panel.inWinConfig.height || (Ext.getBody().dom.offsetHeight - 20));
            this.appWin.doLayout();
            this.appWin.setTitle(panel.title);
        }
        this.appWin.show((typeof main != "undefined") && Disco_RIA.getCfg('enableAnimate') ? Ext.getBody() : false, function(win) {
            win.center();
        });
    },
    /**
     * 通过传入的菜单树节点，打开一个对应的模块页面。<br/> 其中包括模块js路径解析，模块js代码加载，解析，和根据应用设置打开模块界面的操作<br/>
     * 
     * @param {Object}
     *            node 要打开的模块的菜单树节点，允许有多个配置参数。即在应用中可以通过构建符合样式的对象来打开指定的模块。
     *            <ul>
     *            可以配置的参数(都在节点的attributes属性对象中)
     *            <li>package：模块js加载的包名</li>
     *            <li>script：模块js的名称</li>
     *            <li>appClass：模块js的类名</li>
     *            <li>title：模块js加载的tabpanel名称或者窗口名称</li>
     *            <li>node.text：模块js加载的tabpanel名称或者窗口名称</li>
     *            <li>otherScripts：模块js加载需要的其他script</li>
     *            <li>callback：模块js加载完成后调用的回调方法</li>
     *            <li>inWin：是否在弹出窗口中打开模块</li>
     *            <li>params：可以作为在加载js脚本时候的传入参数。可以用作国际化等动态脚本加载设置</li>
     *            </ul>
     */
    openExtAppNode: function(node, e) {
        // 使用树的package来代表所在的包,script代表公共脚本
        if ((!node.attributes.listeners || !node.attributes.listeners.click) && node.attributes.appClass) {
            var script = Disco.Ext.MainAppService.getClassScriptPath(node);
            /*
             * var params=node.attributes.params;
             * if(params)params=Ext.urlDecode(params);
             */
            // alert(script);
            // alert(appClass);
            var appClass = node.attributes.appClass || node.appClass;

            var title = node.attributes.title ? node.attributes.title : node.text;

            if (Global.Config.iframe && !node.attributes.frameDisable) {
                main.openExtApp(appClass, script, title, null, node.attributes.otherScripts, node.attributes.callback, node.attributes.inWin, node.attributes.params);
            } else {
                main.openClassApp(appClass, script, title, appClass, node.attributes.otherScripts, node.attributes.callback, node.attributes.inWin, node.attributes.params);
            }
        }
    },
    getClassScriptPath: function(node) {
        var pck = node.attributes['package'] ? node.attributes['package'] : this['package'];
        if (!pck)
            pck = "";
        else
            pck.replace(".", "/");
        var script = (node.attributes.script ? node.attributes.script : this.script);
        var appClass = node.attributes.appClass;
        if (!script)
            script = appClass + ".js";
        script = pck + "/" + script;
        return script;
    },

    /**
     * 加载，解析，和根据应用设置打开模块界面的操作。该方法是在系统启用IFrame模式下面打开模块应用。
     * 
     * @param {String}
     *            id 打开模块的id
     * @param {String}
     *            script 打开模块的script
     * @param {String}
     *            title 打开模块的弹出窗口或者tabpanel的名称
     * @param {String}
     *            appClass 打开模块的主类名称
     * @param {String}
     *            otherScripts 打开模块依赖的其他script文件路径，多个文件用逗号隔开
     * @param {Function}
     *            callback 打开模块之后的回调方法
     * @param {Boolean}
     *            inWin 是否在窗口中打开模块
     * @param {String}
     *            params 在加载js脚本时候的传入参数。可以用作国际化等动态脚本加载设置
     */
    openExtApp: function(id, script, title, appClass, otherScripts, callback, inWin, params) {
        appClass = appClass || id;
        title = title || id;
        otherScripts = otherScripts || "";

        var tab = this.getComponent(id);
        if (tab) {
            this.setActiveTab(tab);
        } else {
            if (!this.tabsCheck())
                return;
            var theParameter = params || {};
            if (params && (typeof params == "string"))
                theParameter = Ext.urlDecode(params);
            if (!this.IFrameClass[appClass]) {
                eval("this.IFrameClass." + appClass + "=Ext.extend(ExtAppBasePanel,{id:'" + id + "',title:'" + title + "',appClass:'" + appClass + "',script:'" + script + "',otherScripts:'"
                        + otherScripts + "',params:'" + (Ext.encode(theParameter)) + "'});");
            }
            if (inWin) {// 在新窗口中打开应用
                this.openAppInWin(new this.IFrameClass[appClass]());
            } else {
                eval("var p = this.add(new this.IFrameClass." + appClass + "());");
                p.on("destroy", new Function("delete IFrames." + appClass));
                if (this.getSingleTabMode())
                    this.closeAll(p);
                this.setActiveTab(p);
                if (p)
                    p.doLayout();
            }
        }
        if (callback)
            callback();
    },
    /**
     * 加载，解析，和根据应用设置打开模块界面的操作。该方法是在系统启用OPOA模式下面打开模块应用。
     * 
     * @param {String}
     *            id 打开模块的id
     * @param {String}
     *            script 打开模块的script
     * @param {String}
     *            title 打开模块的弹出窗口或者tabpanel的名称
     * @param {String}
     *            appClass 打开模块的主类名称
     * @param {String}
     *            otherScripts 打开模块依赖的其他script文件路径，多个文件用逗号隔开
     * @param {Function}
     *            callback 打开模块之后的回调方法
     * @param {Boolean}
     *            inWin 是否在窗口中打开模块
     * @param {String}
     *            params 在加载js脚本时候的传入参数。可以用作国际化等动态脚本加载设置
     */
    openClassApp: function(id, script, title, appClass, otherScripts, callback, inWin, params) {
        appClass = appClass || id;
        title = title || id;
        var tab = this.getComponent(id);
        if (tab) {
            this.setActiveTab(tab);
            if (callback)
                callback();
        } else {
            if (!this.tabsCheck())
                return;

            var theParameter = params || {};
            if (params && (typeof params == "string")) {
                theParameter = Ext.urlDecode(params);
            }
            if (Disco.Ext.MainAppService.NormalClass[appClass] && !Disco_RIA.devMode) {
                this.openAppHandler(appClass, id, theParameter, title, inWin);
            } else {
                function successLoad(req) {
                    eval(req.responseText);
                    if (eval(appClass)) {
                        this.openAppHandler(appClass, id, theParameter, title, inWin);
                    }
                }
                if (otherScripts) {
                    var s = otherScripts.split(";");
                    var total = s.length, ld = 0;
                    for (var i = 0; i < s.length; i++) {
                        Ext.Ajax.request({
                            url: s[i],
                            success: function(req) {
                                eval(req.responseText);
                                ld++;
                                if (ld >= total)
                                    Ext.Ajax.request({
                                        waitMsg: "正在加载程序，请稍候。。。",
                                        url: Disco_RIA.script + script,
                                        success: successLoad,
                                        scope: this
                                    });
                            },
                            scope: this
                        });
                    }
                } else {
                    Ext.Ajax.request({
                        waitMsg: "正在加载程序，请稍候。。。",
                        url: Disco_RIA.script + script,
                        success: successLoad,
                        scope: this
                    });
                }
            }
            if (callback)
                callback();
        }
    },
    //private
    openAppHandler: function(appClass, id, theParameter, title, inWin) {
        var wrapCls = this.getNormalClass(appClass, id, title);
        if (wrapCls.superclass.onWindowResize) {
            var win = Ext.getCmp(wrapCls.prototype.id);
            if (!win) {
                win = new wrapCls(theParameter);
            }
            win.show();
        } else {
            eval("var tempP = new " + appClass + "(theParameter);" + "if(tempP.list && (typeof tempP.list=='function'))tempP=tempP.list();");
            if (inWin) {// 在窗口中打开应用
                var inWinConfig = tempP.inWinConfig || {};
                Ext.del(tempP, 'inWinConfig');
                this.openAppInWin(new wrapCls({
                    items: tempP,
                    inWinConfig: inWinConfig
                }));
            } else {
                var p = this.add(new wrapCls({
                    items: tempP
                }));
                if (this.getSingleTabMode()) {
                    this.closeAll(p);
                }
                this.setActiveTab(p);
                if (p) {
                    p.doLayout();
                }
            }
        }
    },
    //private 
    getNormalClass: function(appClass, id, title) {
        try {
            if (typeof eval(appClass) == 'undefined') {
                return;
            }
        } catch (e) {
            return;
        }
        var directWin = Disco.Ext.MainAppService.NormalClass[appClass];
        if (directWin) {
            return directWin;
        }
        if (eval(appClass + ".superclass.onWindowResize")) {
            return eval("Disco.Ext.MainAppService.NormalClass." + appClass + "=" + appClass);
        }
        return eval("Disco.Ext.MainAppService.NormalClass." + appClass + "=Ext.extend(Ext.Panel,{id:'" + id + "',title:'" + title + "',border:false,layout:'fit',closable:true});");
    },
    /**
     * 关闭一个TabPanel
     * 
     * @param {Object}
     *            panel 要关闭的panel
     */
    closeTab: function(panel, id) {
        var o = (typeof panel == "string" ? panel : id || panel.id);
        var tab = this.getComponent(o);
        if (tab) {
            this.remove(tab);
        }
    },
    /**
     * 关闭所有可以关闭的tabpanel<br/> 该方法用于完成关闭除了首页的tabpanel或者点击【关闭其他】
     * 
     * @param {Object}
     *            excep 可以设置一个不关闭的panel。
     */
    closeAll: function(excep) {
        this.items.each(function(p) {
            if (p.closable && p != excep)
                this.closeTab(p);
        }, this);
    },
    /**
     * 保存用户个性化桌面protal设置<br/> portal中每一个portelet保存的格式为:<br/> id:每一个portal的id<br/>
     * col:该portal所处的列<br/> row:该portal所处的行<br/>
     * <ul>
     * 所有用户个性化设置参数如下：
     * <li>portalMode:portal的显示模式，调用Disco_RIA.getCfg('portalMode')得到</li>
     * <li>maxTabs:用户设置的最多开启tabpanel的个数，调用Disco_RIA.getCfg('maxTabs')得到</li>
     * <li>iframe:是否开启IFrame模式，调用Disco_RIA.getCfg('iframe')得到</li>
     * <li>singleTabMode:是否开启单Tab页显示模式，调用Disco_RIA.getCfg('singleTabMode')得到</li>
     * <li>enableAnimate:是否开启动画效果，调用Disco_RIA.getCfg('enableAnimate')得到</li>
     * <li>commonFunction:系统开启快捷菜单，调用Disco_RIA.getCfg('commonFunction')得到</li>
     * <li>style:系统皮肤，调用Disco_RIA.getCfg('style')得到</li>
     * <li>homePage:设置应用首页，调用Disco_RIA.getCfg('homePage')得到</li>
     * <li>lang:系统语言设置，调用Disco_RIA.getCfg('lang')得到</li>
     * <li>portals:portal设置，为一个字符串，每一个portal按照上面的格式拼成id:,col:,row:，如果有多个portal，则使用'@@'分隔</li>
     * </ul>
     * 该参数上传的地址为：Disco_RIA.formatUrl（Disco_RIA.PersonalityUrl,'savePersonality')
     */
    savePersonality: function(callback, hideMsg) {
        var result = [];
        var s = "", colCfg = [];
        var portal = this.getComponent("homePage");
        if (portal && portal.items && portal.getXType() == "ux.portal") {
            var items = portal.items;
            for (var i = 0; i < items.getCount(); i++) {
                var c = items.get(i);
                var ps = "";
                for (var j = 0; j < c.items.getCount(); j++) {
                    var it = c.items.get(j);
                    ps = "id:" + it.getId() + ",col:" + i + ",row:" + j;
                    colCfg.push({
                        id: it.getId(),
                        col: i,
                        row: j
                    });
                    if (it.customizeUrl)
                        ps += ",url:" + it.customizeUrl;
                    if (it.customizeHtml)
                        ps += ",html:" + it.customizeHtml;
                    if (it.customizeUrl || it.customizeHtml) {
                        ps += ",title:" + it.title;
                    }
                    s += ps + "@@";
                }
            }
        }
        var params = {
            portalMode: Disco_RIA.getCfg('portalMode'),
            maxTabs: Disco_RIA.getCfg('maxTabs'),
            iframe: Disco_RIA.getCfg('iframe'),
            singleTabMode: Disco_RIA.getCfg('singleTabMode'),
            enableAnimate: Disco_RIA.getCfg('enableAnimate'),
            commonFunction: Disco_RIA.getCfg('commonFunction'),
            style: Disco_RIA.getCfg('style'),
            homePage: Disco_RIA.getCfg('homePage'),
            lang: Disco_RIA.getCfg('lang'),
            viewMode: Disco_RIA.getCfg("viewMode"),
            portals: s
        };
        if (Disco_RIA.PersonalityUrl) {
            Ext.Ajax.request({
                url: Disco_RIA.formatUrl(Disco_RIA.PersonalityUrl, 'savePersonality'),
                params: params,
                waitMsg: hideMsg ? "" : "正在保存数据,请稍候...",
                success: function(res) {
                    Disco_RIA.setCfg('portalConfig', colCfg);
                    if (!hideMsg) {
                        Disco.Ext.Msg.alert('您的个性化配置信息已经成功保存！', '提示', function() {
                            if (callback)
                                callback();
                        }, this);
                    } else {
                        if (callback)
                            callback();
                    }
                }
            });
        }
        return result;
    },
    /**
     * 清除所有portal
     */
    cleanPortlet: function() {
        var portal = this.getComponent("homePage");
        if (portal && portal.items && portal.getXType() == "portal") {
            for (var i = 0; i < portal.items.getCount(); i++) {
                var ps = portal.items.get(i);
                ps.items.each(function(c) {
                    ps.remove(c);
                });
            }
        }
    },
    /**
     * 加载用户个性化桌面信息<br/>
     * 加载的用户自定义信息从Disco_RIA.formatUrl（Disco_RIA.PersonalityUrl,'loadPersonality')得到。
     */
    loadPersonality: function() {
        if (Disco_RIA.PersonalityUrl) {
            Ext.Ajax.request({
                url: Disco_RIA.formatUrl(Disco_RIA.PersonalityUrl, 'loadPersonality'),
                success: function(res) {
                    this.cleanPortlet();
                    var res = Ext.decode(res.responseText);
                    if (res) {
                        var config = {
                            maxTabs: res.maxTabs,// 默认的最大Tab数
                            singleTabMode: res.singleTabMode,// 单个Tab模式
                            enableAnimate: res.enableAnimate,
                            style: res.style,
                            homeMenu: res.homePage,
                            viewMode: res.viewMode,
                            commonFunction: res.commonFunctions
                        }
                        res.commonFunction = res.commonFunctions;
                        Ext.apply(this, config);

                        for (var p in res) {
                            if (Ext.isDefined(res[p])) {
                                Disco_RIA.setCfg(p, res[p]);
                            }
                        }

                        if (Ext.isString(this.commonFunction) && !Ext.isEmpty(this.commonFunction)) {
                            this.commonFunction = this.commonFunction.split(/[,;\s]/);
                            config.commonFunction = this.commonFunction;
                        }
                        this.fireEvent('loadSystemConfig', config);
                    };
                    var p = Ext.getCmp('homePage');

                    if (p && p.getXType() == 'ux.portal') {
                        p.updatePortalModeHandler();
                    }
                    /*
                     * var pcs = res.portalConfig; if (pcs && pcs.length >
                     * 0) { for (var i = 0;i < pcs.length; i++) { var
                     * info = pcs[i]; if (info.id) {
                     * this.addPortlet(info.col, info.row, info.id, { id :
                     * info.id, title : info.title, url : info.url, html :
                     * info.html }); } } }
                     */
                },
                scope: this
            });
        }
    },
    /**
     * 还原个人自定义到系统默认设置。<br/> 处理还原的方法地址为：manage.java?cmd=resetPersonality
     */
    resetPersonality: function() {
        Ext.Msg.confirm("提示", "是否真的要还原桌面设置?", function(btn) {
            if (btn == "yes") {
                Ext.Ajax.request({
                    url: "manage.java?cmd=resetPersonality",
                    success: function(res) {
                        Ext.Msg.alert("提示", "成功还原默认桌面设置!", function() {
                            this.loadPersonality();
                        }, this);
                    },
                    scope: this
                });
            }
        });
    },
    /**
     * 还原桌面设置 处理还原的方法地址为：manage.java?cmd=resetDesktop
     */
    resetDesktop: function() {
        Ext.Msg.confirm("提示", "是否真的要还原桌面设置?", function(btn) {
            if (btn == "yes") {
                Ext.Ajax.request({
                    url: "manage.java?cmd=resetDesktop",
                    success: function(res) {
                        Ext.Msg.alert("提示", "成功还原默认桌面设置!", function() {
                            this.loadPersonality();
                        }, this);
                    },
                    scope: this
                });
            }
        });
    },
    /*
     * // private 在桌面中创建新的模块 createPortlet : function() { if (!this.portletWin) {
     * this.portletWin = new PortletWin();
     * this.portletWin.setHandler(this.addPortlet);
     * this.portletWin.setScope(this); } this.portletWin.show(); },
     */
    // private 往桌面中加入一个portlet
    addPortlet: function(col, row, id, config) {
        var portal = this.getComponent("homePage"), portlet;
        if (portal && portal.getXType() == "ux.portal") {
            var pms = Ext.getObjVal(Global, 'Portal.PanelMC');
            if (pms && pms.getCount()) {
                portlet = pms.key(id);
            }
        }

        /*
         * if (portal && portal.getXType() == "portal") { var portlet;
         * if(window.portlets){ for (var n in portlets) { if (n == id) portlet =
         * portlets[n]; } } var tools = []; if (!portlet) {// 自定义portlet if
         * (config.url && config.url.indexOf("AppClass:") == 0) { var appClass =
         * config.url.substring("AppClass:".length); portlet = { id : id, tools :
         * tools, title : config.title, layout : "fit", items : eval("new " +
         * appClass), customizeUrl : config.url }; } else { portlet = { id : id,
         * tools : tools, title : config.title, autoLoad : config.url,
         * customizeUrl : config.url }; if (config.html) { portlet.html =
         * config.html; portlet.customizeHtml = config.html } } portlets[n] =
         * portlet; } portal.getComponent(col).insert(row, portlet);
         * portal.doLayout(); if (this.portletWin) this.portletWin.hide(); }
         */
    },
    /**
     * 切换系统皮肤
     * 
     * @param {String}
     *            value 皮肤名称
     */
    changeSkin: function(value) {
        Disco.Ext.Util.applySkin(value);
    }
};

Ext.override(Ext.data.JsonWriter, {
    render: function(params, baseParams, data) {
        if (this.encode === true) {
            Ext.apply(params, baseParams);
            params[this.meta.root] = Ext.encode(data);
        } else {
            var jdata = Ext.apply({}, baseParams);
            if (Ext.isArray(data))
                data = Ext.obj2Arr(data);
            if (!Ext.isObject(data)) {
                data = {
                    id: data
                };
            }
            Ext.apply(params, data, jdata);
        }
    }
});
/*
 * Ext.Ajax.request({ url : 'test.java', jsonData:"{name : 'sss'}" });
 */

Disco.Ext.CrudRowEditorPanel = Ext.extend(Ext.Panel, {
    // private
    layout: 'fit',
    /**
     * @cfg {String} defaultSortable grid 是否默认支持排序
     */
    defaultSortable: true,
    /**
     * @cfg {Array} storeMapping grid 的store对象的fields属性
     */
    storeMapping: ['id'],
    /**
     * grid的store配置,如果配置了gridStore直接用gridStore,如果没有配，组建通过配置来创建一个store cfg
     * {Ext.data.Store} gridStore
     */
    gridStore: null,
    gridStoreAutoLoad: true,
    gridStoreInitData: null,
    /**
     * grid配置属性
     * 
     * @cfg {Object} gridConfig
     */
    gridConfig: {},
    /**
     * gridView配置属性
     * 
     * @cfg {Object} gridConfig
     */
    gridViewConfig: {},
    /**
     * grid的每页条数
     * 
     * @cfg {Number} gridPageSize
     */
    gridPageSize: 20,
    /**
     * 配置grid对应的action，本组建会通过baseUrl属性生成访问数据，删除数据，修改数据,查询数据的Url cfg {String}
     * baseUrl
     */
    baseUrl: '',

    cmdParamName: 'cmd',
    // private
    defaultsActions: {
        create: 'save',
        list: 'list',
        view: 'view',
        update: 'update',
        remove: 'remove'
    },
    defaultShowButtons: ['btn_add', 'btn_edit', 'btn_view', 'btn_remove', 'btn_refresh', 'searchField'],
    /**
     * 额外显示的按钮
     */
    extraShowButtons: [],
    /**
     * 禁用按钮
     */
    disableShowButtons: ['btn_view'],
    // private
    getCmdName: function(cmd) {
        return this.defaultsActions[cmd];
    },
    /**
     * 获取cmd的url地址 ,如要获取删除数据的url, this.getCmdUrl('remove');//
     * this.baesUrl+'cmd=remove'; return {String}
     */
    getCmdUrl: function(cmdName) {
        var cmd = {}, url;
        cmd[this.cmdParamName] = this.getCmdName(cmdName);
        url = Ext.urlAppend(this.baseUrl, Ext.urlEncode(cmd));
        return url;
    },
    clearSearch: function() {
        this.gridStore.baseParams = {};
        this.gridStore.load({
            params: {
                start: 0,
                pageSize: this.gridPageSize
            }
        });
    },
    crud_operators: [{
        itemId: 'btn_add',
        name: "btn_add",
        text: "添加(<u>A</u>)",
        iconCls: 'add',
        method: "create",
        cmd: "save",
        noneSelectRow: true,
        hidden: true
    }, {
        itemId: 'btn_edit',
        name: "btn_edit",
        text: "编辑(<u>E</u>)",
        iconCls: "edit",
        disabled: true,
        method: "edit",
        cmd: "update",
        hidden: true,
        singleRow: true
    }, {
        itemId: 'btn_view',
        name: "btn_view",
        text: "查看(<u>V</u>)",
        iconCls: "view",
        method: "view",
        disabled: true,
        hidden: true,
        singleRow: true
    }, {
        itemId: 'btn_remove',
        name: "btn_remove",
        text: "删除(<u>D</u>)",
        iconCls: "delete",
        disabled: false,
        method: "remove",
        cmd: "remove",
        hidden: true
    }, {
        itemId: 'btn_refresh',
        name: "btn_refresh",
        text: "刷新",
        iconCls: "refresh",
        method: "refresh",
        noneSelectRow: true
    }, {
        itemId: 'btn_advancedSearch',
        name: "btn_advancedSearch",
        text: "高级查询(<u>S</u>)",
        iconCls: "srsearch",
        method: "advancedSearch",
        cmd: "list",
        hidden: true,
        noneSelectRow: true,
        clientOperator: true
    }, {
        itemId: 'btn_clearSearch',
        name: "btn_clearSearch",
        text: "显示全部",
        cls: "x-btn-text-icon",
        iconCls: "search",
        noneSelectRow: true,
        method: "clearSearch",
        hidden: true
    }, {
        itemId: 'btn_print',
        name: "btn_print",
        text: "打印(<u>P</u>)",
        iconCls: "print-icon",
        disabled: true,
        method: "printRecord",
        hidden: true
    }, {
        itemId: 'btn_export',
        name: "btn_export",
        text: "导出Excel(<u>O</u>)",
        iconCls: 'export-icon',
        method: "exportExcel",
        noneSelectRow: true,
        hidden: true
    }, {
        itemId: 'btn_import',
        name: "btn_import",
        text: "导入数据(<u>I</u>)",
        iconCls: 'import-icon',
        method: "importExcel",
        noneSelectRow: true,
        hidden: true
    }, '->', {
        itemId: 'searchField',
        name: "searchField",
        type: "searchfield",
        width: 100,
        noneSelectRow: true,
        paramName: 'searchKey',
        clientOperator: true
    }],
    // private
    _getGridButtons: function() {
        if (!this.grid_CrudButtons) {
            this.grid_CrudButtons = this.crud_operators.concat([]);
        }
        var buttons = this.defaultShowButtons.concat(this.extraShowButtons);
        var gbuttons = [];
        Ext.each(this.grid_CrudButtons, function(gbtn) {
            var n = gbtn.name;
            if ((buttons.contains(n) && !this.disableShowButtons.contains(n)) || Ext.isString(gbtn)) {
                gbuttons.push(gbtn);
            }
        }, this);
        return gbuttons;
    },
    /*
     * 获取权限验证信息
     */
    // private
    _getCheckPermissionsInfo: function() {
        var args = {}, names = [], actions = [], cmds = [];
        var btns = this._getGridButtons();
        var baseUrl = this.baseUrl;
        Ext.each(btns, function(o) {
            if (typeof o != "string") {
                if (!o.clientOperator && (o.cmd || o.method)) {
                    actions.push(o.action || baseUrl);
                    cmds.push(o.cmd || o.method || "");
                    names.push(o.name || o.id || "");
                }
            }
        });
        return {
            names: names,
            actions: actions,
            cmds: cmds
        };
    },
    useOperatorsPermission: function(args) {
        var ret = args || this.permissions;
        var args = [];
        for (var i = 0; i < ret.length; i++) {
            args.push(ret[i]);
            var o = this.operators.find(function(c) {
                var n1 = c.name || c.id;
                if (n1 == args[i])
                    return true;
            });
            if (o)
                o.hidden = false;
        }
        this.showOperaterItem(args);
        this.fireEvent("usepermission", this);
    },
    buildCrudOperator: function() {
        // if (!this.operators) this.initOperator();

    },
    buildButtons: function() {
        var btns = this._getGridButtons(), buttons = [];
        var showBtns = this.defaultShowButtons;
        var bs = [];
        Ext.each(btns, function(c) {
            if (typeof c == "string") {
                bs.push(c);
            } else {
                if (!c.showInMenuOnly) {
                    var co = this.operatorConfig2Component(c);
                    var key = co.name || co.id;
                    try {
                        if (Ext.isString(co.type)) {
                            this[key] = Ext.create(co, co.type);
                        } else {
                            this[key] = new Ext.Toolbar.Button(co);
                        }
                        bs.push(this[key]);
                    } catch (e) {
                        alert(key + ":" + e);
                    }
                }
            }
        }, this);
        return bs;
    },
    buildGridStore: function() {
        var store = this.gridStore;
        if (!store) {
            store = new Ext.data.JsonStore({
                root: 'result',
                totalProperty: 'rowCount',
                pageSize: this.gridPageSize,
                url: this.getCmdUrl('list'),
                fields: this.storeMapping,
                restful: false,
                autoLoad: true,
                writer: new Ext.data.JsonWriter({
                    encode: false
                }),
                proxy: new Ext.data.HttpProxy({
                    api: {
                        update: this.getCmdUrl('update'),
                        create: this.getCmdUrl('create'),
                        destroy: this.getCmdUrl('remove'),
                        read: this.getCmdUrl('list')
                    }
                })
            });
            if (this.gridStoreInitData) {
                store.loadData(this.gridStoreInitData);
            } else if (this.gridStoreAutoLoad) {
                store.load();
            }
        }
        return store;
    },
    buildGridCm: function() {
        var cm;
        if (Ext.isArray(this.columns)) {
            cm = new Ext.grid.ColumnModel({
                defaultSortable: this.defaultSortable,
                columns: this.columns
            });
        }
        return cm;
    },
    onBeforeRowEditor: function() {
        return this.permissions.contains('btn_edit');
    },
    buildGrid: function() {
        this.rowEditor = new Ext.ux.grid.RowEditor({
            saveText: 'Update',
            errorSummary: false,
            listeners: {
                scope: this,
                beforeedit: this.onBeforeRowEditor
            }
        });

        this.gridCm = this.buildGridCm();
        var viewConfig = {
            forceFit: true
        };

        var vcf = Ext.apply(viewConfig, this.gridViewConfig);

        var gcf = Ext.apply({
            store: this.gridStore,
            border: false,
            cm: this.gridCm,
            sm: this.gridSm,
            bbar: new Disco.Ext.PagingComBo({
                displayInfo: true,
                rowComboSelect: true,
                store: this.gridStore,
                pageSize: this.gridPageSize
            })
        }, this.gridConfig);

        if (!gcf.viewConfig) {
            gcf.viewConfig = {};
        }
        Ext.apply(gcf.viewConfig, vcf);
        gcf.plugins = gcf.plugins || [];
        gcf.plugins.push(this.rowEditor); // 添加grid插件
        var grid = new Ext.grid.GridPanel(gcf);
        return grid;
    },
    /**
     * 根据一个查询方法来找到在业务列表面板工具栏（toptoolbar)中的符合条件的工具栏组件。
     * 
     * @param {Function}
     *            callback 传入的查询方法。
     *            <ul>
     *            该方法传入的参数：
     *            <li>{Component} 业务列表面板工具栏（toptoolbar)中的每一个按钮组件</li>
     *            </ul>
     * 
     * @return {Array} finds 找到的符合条件的工具栏组件
     */
    findOperatorBy: function(callback) {
        var objs = [];
        this.operators.each(function(o) {
            if (typeof o != "string") {
                if (callback && callback(o))
                    objs.push(o);
            }
        });
        return objs;
    },
    /**
     * 根据工具栏组件的属性找到业务列表面板工具栏（toptoolbar)中的符合条件的工具栏组件。
     * 
     * @param {String}
     *            name 要匹配的属性名称。
     * @parma {Object} value 要匹配的属性的值。
     * @return {Array} finds 找到的符合条件的工具栏组件
     */
    findOperatorByProperty: function(name, value) {
        return this.findOperatorBy(function(o) {
            if (o[name] == value)
                return true;
        });
    },
    // private
    toggleSingleRowOperator: function(enable) {
        var ids = this.findOperatorByProperty("singleRow", true);
        var args = [];
        if (ids && ids.length) {
            for (var i = 0; i < ids.length; i++)
                args.push(ids[i].name || ids[i].id);
        }
        this[(enable ? 'enable' : 'disable') + 'OperaterItem'](args);
    },
    /**
     * 让一系列的右键菜单项变成可用状态
     * 
     * @param {Array}
     *            args 要设置为可用状态的右键菜单项的名称或者id。
     */
    enableOperaterItem: function() {
        var args = Ext.args();
        this.getTopToolbar().enables(args);
    },
    /**
     * 让一系列的右键菜单项变成禁用状态
     * 
     * @param {Array}
     *            args 要设置为禁用状态的右键菜单项的名称或者id。
     */
    disableOperaterItem: function() {
        var args = Ext.args();
        this.getTopToolbar().disables(args);
    },
    /**
     * 让一系列的右键菜单项变成可见状态
     * 
     * @param {Array}
     *            args 要设置为可见状态的右键菜单项的名称或者id。
     */
    showOperaterItem: function() {
        var args = Ext.args();
        this.getTopToolbar().shows(args);
    },
    /**
     * 让一系列的右键菜单项变成隐藏状态
     * 
     * @param {Array}
     *            args 要设置为隐藏状态的右键菜单项的名称或者id。
     */
    hideOperaterItem: function() {
        var args = Ext.args();
        this.getTopToolbar().hides(args);
    },
    // private
    /*
     * 处理grid行选择改变时候触发
     */
    onGridRowSelectionChang: function(g, index, r) {
        var sel = this.grid.getSelections();
        this.toggleSingleRowOperator(!(sel && sel.length > 1));
        var ids = this.findOperatorByProperty("batch", true);// 打开支持批量操作的按钮
        var args = [];
        if (ids && ids.length) {
            for (var i = 0; i < ids.length; i++)
                args.push(ids[i].name || ids[i].id);
        }
        this.enableOperaterItem(args);
    },
    // private
    operatorConfig2Component: function(o, isMenu) {
        var co = Ext.apply({}, o);
        if (!co.handler) {
            if (co.method && this[co.method]) {
                co.handler = this[co.method];
            }
        }
        if (co.handler && !co.scope)
            co.scope = this;
        if (!isMenu) {// 对按钮的样式作处理
            if (this.operatorButtonStyle == 2) {
                if (co.icon) {
                    co.cls = "x-btn-icon";
                    co.text = "";
                }
            } else if (this.operatorButtonStyle == 3) {
                co.icon = "";
                co.cls = "";
            }
        }
        var key = co.name || co.id;
        if (key == "searchField") {
            co.store = this.gridStore;
        } else if (key == "btn_advancedSearch") {
            co.hidden = !((this.searchFormPanel || this.searchFP) && this.allowSearch);
        }
        return co;
    },
    loadOperatorsPermission: function() {
        if (!this.permissions) {
            var objs = this._getCheckPermissionsInfo();
            if (Disco_RIA.permissionCheck) {
                Ext.Ajax.request({
                    url: (Disco_RIA.permissionCheckAction || "manage.java?cmd=checkButtons"),
                    params: objs,
                    callback: function(options, success, response) {
                        var ret = Ext.decode(response.responseText);
                        if (ret && ret.permissions && ret.permissions.length) {// 处理权限
                            this.permissions = ret.permissions;
                            this.useOperatorsPermission();
                        }
                        if (ret && ret.queryObjects) {
                            this.addQueryObjectOperator(ret.queryObjects);
                        }
                    },
                    scope: this
                });
            } else {
                this.permissions = ["btn_add", "btn_edit", "btn_view", "btn_remove", "btn_refresh"];
                this.useOperatorsPermission();
            }
        } else {
            this.useOperatorsPermission();
        }
    },
    initCrudRowEditorEvents: function() {
        var g = this.grid, sm = g.getSelectionModel();;
        sm.on('rowselect', this.onGridRowSelectionChang, this, {
            buffer: 200
        });
        /*
         * g.on({ scope : this ,
         * 
         * });
         */
    },
    // private
    create: function() {
        var data = new this.gridStore.recordType({});
        this.rowEditor.stopEditing();
        this.gridStore.insert(0, data);
        this.rowEditor.startEditing(0);
    },
    // private
    /*
     * 编辑
     */
    edit: function() {
        var rowIndex = this.getSingleRow();
        this.rowEditor.startEditing(rowIndex, false);
        this.rowEditor.doFocus.defer(this.rowEditor.focusDelay, this, []);
    },
    // private
    /*
     * 查看
     */
    view: function() {
        var rowIndex = this.getSingleRow();
    },
    // private 删除
    remove: function() {
        var rowIndex = this.getSingleRow();
        this.gridStore.remove(this.gridStore.getAt(rowIndex));
    },
    // private 刷新
    refresh: function() {
        this.grid.getStore().reload();
    },
    // private
    getSingleRow: function() {
        var g = this.grid, s = g.getStore();
        var records = g.getSelections();
        if (records && records.length) {
            var rowIndex = s.indexOf(records[0]);
        }
        return rowIndex;
    },
    beforeDestroy: function() {
        if (this.rowEditor) {
            Ext.destroy(this.rowEditor);
            delete this.rowEditor;
        }
    },
    initComponent: function() {
        this.gridStore = this.buildGridStore();
        var gridTbars = this.buildButtons();
        this.operators = new Ext.util.MixedCollection(false, function(o) {
            return o.id || o.name
        });
        this.operators.addAll(gridTbars);

        this.tbar = gridTbars;
        this.grid = this.buildGrid();
        this.items = [this.grid];
        this.initCrudRowEditorEvents();
        this.loadOperatorsPermission();
        Disco.Ext.CrudRowEditorPanel.superclass.initComponent.call(this);
    }
});
/**
 * @class Disco.Ext.MainTabPanel
 * @extend Ext.TabPanel
 * 
 * 基于TabPanel的应用程序主框架
 * 
 * <pre>
 * <code>
 * //使用示例
 * MainPanel = function() {
 * 	var homePage = Disco_RIA.getCfg('homePage');
 * 	var homeCfg = {
 * 		id : 'homePage',
 * 		title : '首 页',
 * 		xtype : 'panel',
 * 		border : false,
 * 		tabFixed : true,
 * 		closable : false,
 * 		allowDrag : false
 * 	}
 * 	if (homePage == 'menu') {
 * 		Ext.apply(homeCfg, {
 * 					xtype : 'ux.portal'
 * 				});
 * 	} else {
 * 		Ext.apply(homeCfg, {
 * 					html : {
 * 						tag : 'div',
 * 						style : 'text-align:center;',
 * 						cn : [{
 * 									tag : 'h1',
 * 									html : '我是自定义首页'
 * 								}]
 * 					}
 * 				});
 * 	}
 * 	MainPanel.superclass.constructor.call(this, {
 * 				id : 'main',
 * 				region : 'center',
 * 				margins : '0 2 0 0',
 * 				layoutOnTabChange : true,
 * 				plugins : [new Disco.Ext.TabPanelPlugin],
 * 				border : false,
 * 				items : homeCfg
 * 			});
 * 	this.on(&quot;render&quot;, this.loadPersonality, this);
 * };
 * Ext.extend(MainPanel, Disco.Ext[&quot;MainTabPanel&quot;]);
 * </code>
 * </pre>
 */
Disco.Ext.MainTabPanel = Ext.extend(Ext.TabPanel, {
    /**
     * @cfg {Boolean} singleTabMode 系统默认tab模式为多tab模式
     */
    singleTabMode: false,
    /**
     * @cfg {Boolean} iframe 系统默认IFrame模式为OPOA模式
     */
    iframe: false,
    /**
     * @cfg {Boolean} enableAnimate 系统默认关闭动画效果
     */
    enableAnimate: false,
    /**
     * @cfg {Boolean} resizeTabs 让tabpanel头的宽度自动适应模块title宽度
     */
    resizeTabs: true,
    /**
     * @cfg {Integer} minTabWidth 最小的tab头宽度：65
     */
    minTabWidth: 65,
    tabWidth: 120,
    enableTabScroll: true,
    activeTab: 0,
    /**
     * @cfg {Integer} maxTabs 最多打开tab个数：10
     */
    maxTabs: 10,// 默认的最大Tab数
    initComponent: function() {
        Disco.Ext.MainTabPanel.superclass.initComponent.call(this);
    }
});
Ext.apply(Disco.Ext.MainTabPanel.prototype, {}, Disco.Ext.MainAppService);

/**
 * 基于Panel的应用程序主框架
 * 
 * @class Disco.Ext.MainSinglePanel
 * @extends Ext.Panel
 */
Disco.Ext.MainSinglePanel = Ext.extend(Ext.Panel, {
    /**
     * @cfg {Boolean} iframe 系统默认IFrame模式为OPOA模式
     */
    iframe: false,
    /**
     * @cfg {Integer} maxTabs 最多打开tab个数：10
     */
    maxTabs: 10,// 默认的最大Tab数
    /**
     * @cfg {Boolean} singleTabMode 系统默认tab模式为单tab模式
     */
    singleTabMode: true,// 单个Tab模式
    /**
     * @cfg {Boolean} enableAnimate 系统默认开启动画效果
     */
    enableAnimate: true,
    layout: "fit",
    homeMenu: "menu",// 主页菜单
    theStyle: "ext-all",
    setActiveTab: function(p) {// 模拟TabPanel中的相应方法
        p.show();
        p.ownerCt.doLayout();
    },
    getActiveTab: function() {// 模拟TabPanel中的相应方法
        return this.getComponent(0);
    },
    getSingleTabMode: function() {
        return true;
    },
    closeAll: function(excep) {
        this.items.each(function(p) {
            if (p != excep)
                this.closeTab(p);
        }, this);
    },
    initComponent: function() {
        Disco.Ext.MainSinglePanel.superclass.initComponent.call(this);
    }
});
Ext.apply(Disco.Ext.MainSinglePanel.prototype, {}, Disco.Ext.MainAppService);

Ext.namespace("Ext.ux.plugins");
/**
 * 扩展列表分页组件，支持显示条数，分页条数，总页数，选择跳转页面。
 * 
 * @class Disco.Ext.PagingComBo
 * @extends Ext.PagingToolbar
 */
Disco.Ext.PagingComBo = Ext.extend(Ext.PagingToolbar, {
    displayMsg: "第{0}-{1}条&nbsp;&nbsp;共{2}条&nbsp;&nbsp;&nbsp;&nbsp;共{3}页",
    style: 'font-weight:900',
    rowComboSelect: true,
    displayInfo: true,
    doLoad: function(start) {
        var o = {}, pn = this.getParams() || {};
        o[pn.start] = start;
        o[pn.limit] = this.pageSize;
        if (this.store.baseParams && this.store.baseParams[pn.limit])
            this.store.baseParams[pn.limit] = this.pageSize;
        if (this.fireEvent('beforechange', this, o) !== false) {
            this.store.load({
                params: o
            });
        }
    },
    onPagingSelect: function(combo, record, index) {
        var d = this.getPageData(), pageNum;
        pageNum = this.readPage(d);
        if (pageNum !== false) {
            pageNum = Math.min(Math.max(1, record.data.pageIndex), d.pages) - 1;
            this.doLoad(pageNum * this.pageSize);
        }
    },
    readPage: Ext.emptyFn,
    onLoad: function(store, r, o) {
        var d = this.getPageData(), ap = d.activePage, ps = d.pages;
        this.combo.store.removeAll();
        if (ps == 0) {
            this.combo.store.add(new Ext.data.Record({
                pageIndex: 1
            }));
            this.combo.setValue(1);
        } else {
            for (var i = 0; i < ps; i++) {
                this.combo.store.add(new Ext.data.Record({
                    pageIndex: i + 1
                }));
            }
            this.combo.setValue(ap);
        }
        if (this.rowComboSelect)
            this.rowcombo.setValue(this.pageSize);
        Disco.Ext.PagingComBo.superclass.onLoad.apply(this, arguments);
    },
    updateInfo: function() {
        if (this.displayItem) {
            var count = this.store.getCount();
            var activePage = this.getPageData().activePage;
            var msg =
                    count == 0
                            ? this.emptyMsg
                            : String.format(this.displayMsg, this.cursor + 1, this.cursor + count, this.store.getTotalCount(), Math.ceil(this.store.getTotalCount() / this.pageSize), activePage);
            this.displayItem.setText(msg);
        }
    },
    // 选择每页多少条数据
    onComboPageSize: function(combo, record, index) {
        var pageSize = record.get('pageSize');
        this.store.pageSize = this.pageSize = pageSize;
        var d = this.getPageData(), pageNum;
        pageNum = this.readPage(d);
        if (pageNum !== false) {
            pageNum = Math.min(Math.max(1, record.data.pageIndex), d.pages) - 1;
            this.doLoad(0);
        }
    },
    initComponent: function() {
        if (Ext.getObjVal(this.store, 'pageSize')) {
            this.pageSize = Ext.getObjVal(this.store, 'pageSize');
        }
        this.combo = Ext.ComponentMgr.create(Ext.applyIf(this.combo || {}, {
            value: 1,
            width: 50,
            store: new Ext.data.SimpleStore({
                fields: ['pageIndex'],
                data: [[1]]
            }),
            mode: 'local',
            xtype: 'combo',
            minListWidth: 50,
            allowBlank: false,
            triggerAction: 'all',
            displayField: 'pageIndex',
            allowDecimals: false,
            allowNegative: false,
            enableKeyEvents: true,
            selectOnFocus: true,
            submitValue: false
        }));
        this.combo.on("select", this.onPagingSelect, this);
        this.combo.on('specialkey', function() {
            this.combo.setValue(this.combo.getValue());
        }, this);

        var T = Ext.Toolbar;

        var pagingItems = [];

        if (this.displayInfo) {
            pagingItems.push(this.displayItem = new T.TextItem({}));
        }

        if (this.rowComboSelect) {
            var data = this.rowComboData ? this.rowComboData : [[10], [20], [30], [50], [80], [100], [150], [200]];
            this.rowcombo = this.rowcombo || Ext.create({
                store: new Ext.data.SimpleStore({
                    fields: ['pageSize'],
                    data: data
                }),
                value: this.pageSize,
                width: 50,
                mode: 'local',
                xtype: 'combo',
                allowBlank: false,
                minListWidth: 50,
                displayField: 'pageSize',
                triggerAction: 'all'
            });
            pagingItems.push(this.rowcombo, "条/页&nbsp;&nbsp;");

            this.rowcombo.on("select", this.onComboPageSize, this);
            this.rowcombo.on('specialkey', function() {
                this.combo.setValue(this.combo.getValue());
            }, this);
        }

        // this.totalPage = new T.TextItem({})
        pagingItems.push('->', this.first = new T.Button({
            tooltip: this.firstText,
            overflowText: this.firstText,
            iconCls: 'x-tbar-page-first',
            disabled: true,
            handler: this.moveFirst,
            scope: this
        }), this.prev = new T.Button({
            tooltip: this.prevText,
            overflowText: this.prevText,
            iconCls: 'x-tbar-page-prev',
            disabled: true,
            handler: this.movePrevious,
            scope: this
        }), '-', this.beforePageText, this.inputItem = this.combo, this.afterTextItem = new T.TextItem({
            text: String.format(this.afterPageText, 1)
        }), '-', this.next = new T.Button({
            tooltip: this.nextText,
            overflowText: this.nextText,
            iconCls: 'x-tbar-page-next',
            disabled: true,
            handler: this.moveNext,
            scope: this
        }), this.last = new T.Button({
            tooltip: this.lastText,
            overflowText: this.lastText,
            iconCls: 'x-tbar-page-last',
            disabled: true,
            handler: this.moveLast,
            scope: this
        }), '-', this.refresh = new T.Button({
            tooltip: this.refreshText,
            overflowText: this.refreshText,
            iconCls: 'x-tbar-loading',
            handler: this.doRefresh,
            scope: this
        }));

        var userItems = this.items || this.buttons || [];
        if (this.prependButtons === true) {
            this.items = userItems.concat(pagingItems);
        } else if (Ext.isNumber(this.prependButtons)) {
            pagingItems.splice.apply(pagingItems, [this.prependButtons, 0].concat(userItems));
            this.items = pagingItems;
        } else {
            this.items = pagingItems.concat(userItems);
        }
        delete this.buttons;
        Ext.PagingToolbar.superclass.initComponent.call(this);
        this.addEvents('change', 'beforechange');
        this.on('afterlayout', this.onFirstLayout, this, {
            single: true
        });
        this.cursor = 0;
        this.bindStore(this.store, true);
    }
});
Ext.ux.PagingComBo = Disco.Ext.PagingComBo;
Ext.reg("pagingComBo", Ext.ux.PagingComBo);

Ext.grid.ObjectColumn = Ext.extend(Ext.grid.Column, {
    field: '',
    emptyText: '',
    constructor: function(cfg) {
        Ext.grid.ObjectColumn.superclass.constructor.call(this, cfg);
        var et = this.emptyText, f = this.field;
        this.renderer = function(v) {
            if (v) {
                if (!Ext.isArray(f)) {
                    return Ext.value(Ext.getObjVal(v, f), et);
                } else {
                    for (var i = 0; i < f.length; i++) {
                        if (v.hasOwnProperty(f[i])) {
                            return Ext.value(v[f[i]], et);
                        }
                    }
                }
            }
            return et;
        }
    }
});
Ext.grid.Column.types['objectcolumn'] = Ext.grid.ObjectColumn;

/**
 * 给所有的表单项默认绑定Disco.Ext.HelpIconPlugin
 */
Ext.apply(Ext.form.Field.prototype, {
    plugins: new Disco.Ext.HelpIconPlugin()
});
/**
 * 统一配置异步请求响应。
 */
Ext.Ajax.on("beforerequest", function(conn, options) {
    if (options.waitMsg) {
        Ext.Msg.wait(options.waitMsg, options.waitTitle || '请稍候...');
    }
})
/**
 * 如果配置了权限检查，这里统一处理权限当权限不够的情况下的响应方法。
 */
Ext.Ajax.on("requestcomplete", function(conn, response, options) {
    if (response && response.getResponseHeader && response.getResponseHeader("LoginRequired")) {
        Disco.Ext.Msg.alert("对不起，您还没有登录或者登录超时，请重新登录！", "提示", function() {
            if (!Disco.Ext.LoginWin) {
                var win = window.top ? window.top : window;
                if (win.relogin && !Disco.Ext.isLogin) {
                    Disco.Ext.isLogin = true;
                    win.relogin();
                } else if (!win.relogin) {
                    win.location.href = "";
                }
            } else if (!Disco.Ext.isLogin) {
                Disco.Ext.isLogin = true;
                var loginWin = new Disco.Ext.LoginWin();
                loginWin.show();
            }
        });
        return;
    }
    if (response && response.getResponseHeader && response.getResponseHeader("Unauthorized")) {
        Disco.Ext.Msg.alert("你没有该项操作的权限，请与管理员联系！", "警告");
        return;
    }
    if (options.waitMsg) {
        Ext.MessageBox.updateProgress(1);
        Ext.MessageBox.hide();
    }
});
/**
 * 注册全局的异步请求错误提示。 默认没有实现国际化等。可以通过覆盖该方法来自定义不同的错误提示和处理方法。<br/>
 * <ul>
 * <li>0|12002|12029|12030|12031|12152|13030:提示【您的网络连接发生中断】</li>
 * <li>403:提示【您没有操作的权限，请与管理员联系】</li>
 * <li>其他:提示【发生了其它通讯异常，异常状态编码为+code】</li>
 * </ul>
 */
Ext.Ajax.on("requestexception", function(conn, response, options) {

    var code = response.status || 0;
    var Msg = Disco.Ext.Msg;
    switch (code) {
        case 0:
        case 12002:
        case 12029:
        case 12030:
        case 12031:
        case 12152:
        case 13030:
            Msg.error("您的网络连接发生中断!", "通讯异常!", "警告");
            return false;
            break;
        case -1:
            // Ext.Msg.alert("通讯超时!","请求已经被自动取消!");
            return false;
            break;
        case 403:
            Disco.Ext.Msg.alert("您没有操作的权限，请与管理员联系!" + code, "警告!");
            return false;
            break;
        default:
            if (code < 200 || code >= 300) {
                var data = Ext.decode(response.responseText);
                var errMsg = "发生了其它通讯异常，异常状态编码为" + code + "警告!";

                /*if(Ext.getObjVal(data,'err')){
                	errMsg = Ext.getObjVal(data,'err');
                }
                Disco.Ext.Msg.error(errMsg);*/

                Disco_RIA.ajaxErrorHandler(data);

                return false;
            }
    }
    return true;
});

/**
 * 屏蔽退格键
 */
new Ext.KeyMap(document.documentElement, [{
    key: Ext.EventObject.BACKSPACE,
    fn: function(key, e) {
        var regExp = /(?:INPUT|TEXTAREA)/;
        if (!regExp.test(e.getTarget().nodeName)) {
            e.stopEvent();
        }
    }
}]);