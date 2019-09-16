var main, menu, header, bottom, onlineWindow, loginWin, messageWindow;
Ext.QuickTips.init();
if (typeof Global === "undefined") {
    Global = {};
}
Global = Ext.apply(Global || {}, {
    iframe: false,
    openExtAppNode: function(node, e) {
        //使用树的package来代表所在的包,script代表公共脚本
        main.openExtAppNode.call(this, node, e);
    }
});
/**
 * 配置菜单
 * @type 
 */
Global.SystemMenus = [{
    title: '系统基础配置',
    children: [{
        text: " 系统菜单配置",
        leaf: true,
        appClass: "TenantMenuManagePanel",
        script: "sys/TenantMenuManagePanel.js"
    }]
}];

function menuClick(node, e) {
    var script = node.attributes.script;
    if (script && script.substr(0,4) == 'http') {
        //打开一个外部指定http应用
        window.open(script).focus();
    } else if (node.attributes.appClass) {
        var script = node.attributes.script || (node.attributes.appClass + ".js");
        if (node.attributes.addObject) {
            Disco.Ext.Util.addObject(node.attributes.appClass, null, node.attributes.script, node.attributes.otherScripts);
        } else {
            node.attributes.script = script;
            Global.openExtAppNode(node, e);
        }
    }
};

MenuPanel = function() {
    /**
     * js内部类实现方式
     * disco增强
     */
    var SystemMenuTreePanel = Ext.extendX(Ext.tree.TreePanel, function() {
        var openNodeApp = this.openNodeApp;
        return {
            border: false,
            lines: false,
            children: [],
            rootVisible: false,
            style: "margin-bottom:5px;margin-top:5px",
            /**
             * 构建菜单根节点
             */
            buildTreeRoot: function() {
                var root = new Ext.tree.AsyncTreeNode({
                    loader: new Ext.tree.TreeLoader({
                        preloadChildren: true,
                        iconCls: 'disco-tree-node-icon'
                    }),
                    children: this.children || []
                });
                return root;
            },
            initComponent: function() {
                this.root = this.buildTreeRoot();
                this.on('click', openNodeApp, this);
                SystemMenuTreePanel.superclass.initComponent.call(this);
            }
        }
    }, this);

    if (true) {
        Ext.Ajax.request({
            async: false,
            //url: 'systemMenu.java?cmd=getUserMenuTree&all=true',
            url: 'manage.java?cmd=getUserMenuTree&all=true',
            callback: function(respnose) {
                var data = Ext.decode(respnose.responseText);
                //Global.SystemMenus = data;
                !data.length || (Global.SystemMenus = data);
            }
        });
    }
    var menus = [];
    var titleTpl = "<span class='titFor'>{0}</span>";
    Ext.each(Global.SystemMenus, function(item) {
        var parseChild = function(childs) {
            Ext.each(childs, function(child) {
                //控制菜单树节点的高度间距 这里应该抽取面一个递归 目前只处理了第一级节点
                child.cls = 'menu-node-cls';
                if (child.children) {
                    parseChild(child.children);
                }
            });
        }
        parseChild(item.children);
        var menu = {
            hideCollapseTool: true,//隐藏展开/折叠切换按钮
            title: String.format(titleTpl, item.text || item.title),
            //iconCls: "icon-join",//图标
            items: new SystemMenuTreePanel({
                children: item.children
            })
        }
        menus.push(menu);
    }, this);

    MenuPanel.superclass.constructor.call(this, {
        id: 'menu',
        region: 'west',
        //title: "<span class='titFor' style='margin-left:50px;font-size:14px'>业务模块</span>",
        split: true,
        width: 180,
        minSize: 180,
        maxSize: 250,
        collapseMode: 'mini',
        hideCollapseTool: true,
        layout: 'border',
        border: false,
        items: [{
            items: menus,
            border: false,
            region: 'center',
            defaults: {
                autoScroll: true
            },
            layout: "accordion"
        }]
    });
};
Ext.extend(MenuPanel, Ext.Panel, {
    openNodeApp: function(n, e) {
        if (n.isLeaf()) {
            menuClick(n, e);
        }
    }
});

MainPanel = function() {
    var homePage = Disco_RIA.getCfg('homePage');
    var homeCfg = {
        id: 'homePage',
        title: '系统首页',
        xtype: 'panel',
        border: false,
        tabFixed: true,
        closable: false,
        allowDrag: false,
        autoScroll: true
    };
    Ext.Ajax.request({
        url: 'common.java?cmd=homePage',
        success: function(response) {
            Ext.getCmp('homePage').update(response.responseText, true);
        }
    });
    MainPanel.superclass.constructor.call(this, {
        id: 'main',
        border: false,
        items: homeCfg,
        region: 'center',
        autoScroll: false,
        //margins: '0 2 0 0',
        layoutOnTabChange: true,
        plugins: [new Disco.Ext.TabPanelPlugin]
    });
        //this.on("render", this.loadPersonality, this);
    };
Ext.extend(MainPanel, Disco.Ext["MainTabPanel"]);

Ext.apply(MainPanel.prototype, {
    activeTab: 0
}, Ext.ux.panel.DDTabPanel.prototype);

Ext.onReady(function() {
    var config = {
        border: false,
        region: 'north',
        layout: 'anchor',
        height: 79,
        maxHeight: 79,
        minHeight: 79,
        split: true,
        collapseMode: 'mini',
        items: [{
            xtype: "box",
            border: true,
            el: "header",
            autoShow: true,
            anchor: '100% -23',
            listeners: {
                scope: this,
                afterrender: function(box) {
                    box.getEl().on('click', function(e) {
                        var t = e.getTarget('a', null, true);
                        var value = Ext.fly(t).getAttribute('skin', 'ext');
                        if (value) {
                            Disco_RIA.setCfg('style', value);
                            Disco.Ext.Util.applySkin(value);
                        }
                    }, this, {
                        delegate: 'a'
                    });
                }
            }
        }]
    };
    header = new Ext.Panel(config);
    menu = new MenuPanel();
    main = new MainPanel();
    bottom = {
        height: 38,
        xtype: "box",
        el: "bottom",
        region: "south"
    };
    var viewport = new Ext.Viewport({
        hideBorders: true,
        layout: 'card',
        activeItem: 0,
        id: 'viewport',
        items: {
            id: "desktop",
            layout: "border",
            items: [header, menu, main, bottom]
        }
    });
    setTimeout(function() {
        Ext.get('loading').remove();
        Ext.get('loading-mask').fadeOut({
            remove: true
        });
    }, 300);
    //当前登陆户如果是管理员则放在
    Ext.Ajax.request({
        method: 'POST',
        url: 'common.java?cmd=getCurrentUser',
        success: function(response) {
            var r = Ext.decode(response.responseText);
            if (r) {
                Global.getCurrentUser = r;
            }
        },
        scope: this
    });
});
