// 优惠管理
SystemLogManagePanel = Ext.extend(Disco.Ext.CrudPanel, {
    id: "systemLogManagePanel",
    baseUrl: "systemLog.java",
    gridSelModel: 'checkbox',
    batchRemoveMode: true,
    pageSize: 20,
    showView: false,
	showAdd: false,
    showEdit: true,
    baseQueryParameter : {
    	types : 1
	},
    showRemove:false,
    storeMapping: ["id", "user", "vdate","ip","action","cmdName","params"],
    initComponent: function() {
        this.cm = new Ext.grid.ColumnModel([{
            header: "访问者",
            sortable: true,
            width: 100,
            dataIndex: "user",renderer : this.objectRender("name")
        },{
            header: "访问时间",
            sortable: true,
            width: 100,
            dataIndex: "vdate",renderer:function(v){
            	var date = new Date(v);
            	return date.toLocaleString();
            }
        },{
            header: "访问者ip",
            sortable: true,
            width: 100,
            dataIndex: "ip"
        },{
            header: "访问的action",
            sortable: true,
            width: 100,
            dataIndex: "action"
        },{
            header: "访问的方法",
            sortable: true,
            width: 100,
            dataIndex: "cmdName",renderer:function(v){return v||"doInit"}
        },{
            header: "说明",
            sortable: true,
            width: 100,
            dataIndex: "params"
        }]);
        SystemLogManagePanel.superclass.initComponent.call(this);
    }
});
