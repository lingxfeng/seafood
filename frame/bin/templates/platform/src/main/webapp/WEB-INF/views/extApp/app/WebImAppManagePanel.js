MyFriendsPanel = Ext.extend(Ext.Panel, {
    title: '好友',
    initComponent: function() {
        MyFriendsPanel.superclass.initComponent.call(this);
    }
});
MyGroupsPanel = Ext.extend(Ext.Panel, {
    title: '讨论组',
    initComponent: function() {
        MyGroupsPanel.superclass.initComponent.call(this);
    }
});
RecentlyUserPanel = Ext.extend(Ext.Panel, {
    title: '最近联系人',
    initComponent: function() {
        RecentlyUserPanel.superclass.initComponent.call(this);
    }
});
/**
 * 联系人、群、最近联系人Tab面板
 * @class MemberTabPanel
 * @extends Ext.TabPanel
 */
MemberTabPanel = Ext.extend(Ext.TabPanel, {
    activeTab: 0,
    border: false,
    region: 'center',
    initComponent: function() {
        this.initBorderLayout();
        MemberTabPanel.superclass.initComponent.call(this);
    },
    initBorderLayout: function() {
        this.myFriends = new MyFriendsPanel();
        this.groups = new MyGroupsPanel();
        this.recentlyUser = new RecentlyUserPanel();
        this.items = [this.myFriends, this.groups, this.recentlyUser];
    }
});
/**
 * WebIm窗体中的顶部信息
 * @class WebImTopPanel
 * @extends Ext.Panel
 */
WebImTopPanel = Ext.extend(Ext.Panel, {
    region: 'north',
    height: 65,
    border: false,
    html: '个人头像、用户名及个性签名',
    initComponent: function() {
        WebImTopPanel.superclass.initComponent.call(this);
    }
});
/**
 * WebIm窗体中的中间区域
 * @class WebImCenterPanel
 * @extends Ext.Panel
 */
WebImCenterPanel = Ext.extend(Ext.Panel, {
    region: 'center',
    layout: 'border',
    border: false,
    initComponent: function() {
        this.initBorderLayout();
        WebImCenterPanel.superclass.initComponent.call(this);
    },
    initBorderLayout: function() {
        this.search = new Ext.form.TextField({
            anchor: '100%',
            region: 'north',
            margins: '0 -1 0 -1',
            emptyText: '输入关键字搜索联系人'
        });
        this.tab = new MemberTabPanel();
        this.items = [this.search, this.tab];
    }
});
/**
 * ebIm窗体中的底部区域
 * @class WebImBottomPanel
 * @extends Ext.Panel
 */
WebImBottomPanel = Ext.extend(Ext.Panel, {
    region: 'south',
    height: 65,
    border: false,
    html: '设置、添加好友及添加应用',
    initComponent: function() {
        WebImBottomPanel.superclass.initComponent.call(this);
    }
});
/**
 * WebIm APP应用对外调用主类
 * @class WebImAppManagePanel
 * @extends Ext.Window
 */
WebImAppManagePanel = Ext.extend(Ext.Window, {
    id: 'webImAppManagePanel',
    width: 203,
    height: Ext.getBody().dom.offsetHeight > 650 ? Ext.getBody().dom.offsetHeight - 100 : 550,
    shim: false,
    minWidth: 200,
    minHeight: 300,
    title: 'IM2012',
    maximizable: true,
    minimizable: true,
    layout: "border",
    show: function() {
        var width = Ext.getBody().getWidth();
        this.setPagePosition(Math.max(width - this.width - 20, 0), 30, true);
        if (!this.isVisible()) {
            WebImAppManagePanel.superclass.show.call(this);
            this.refreshUser();
        }
    },
    initComponent: function() {
        this.initBorderLayout();
        WebImAppManagePanel.superclass.initComponent.call(this);
    },
    initBorderLayout: function() {
        this.top = new WebImTopPanel();
        this.center = new WebImCenterPanel();
        this.bottom = new WebImBottomPanel();
        this.items = [this.top, this.center, this.bottom];
    },
    listeners: {
        'close': function(win) {
            console.log('断开与服务端的连接')
        }
    },
    /**
     * 每次显示的时候都刷新用户列表
     */
    refreshUser: function() {
        //TODO 刷新数据
    },
    minimize: function() {
        this.hide();
    }
});