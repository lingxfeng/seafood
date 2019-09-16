
ClientVisitPanel=Ext.extend(Disco.Ext.CrudPanel,{
    id:"clientVisitPanel",
    baseUrl:"clientVisit.java",
    createForm:function(){
    var formPanel=new Ext.form.FormPanel({
        frame:true,
        labelWidth:70,
        labelAlign:'right',
        defaultType:'textfield',
                        defaults:{width:200},
                        items:[{xtype:"hidden",name:"id"},
{fieldLabel:'vdate',name:'vdate'},
    {fieldLabel:'endTime',name:'endTime'},
    {fieldLabel:'remark',name:'remark'},
    {fieldLabel:'title',name:'title'},
    {fieldLabel:'content',name:'content'},
    {fieldLabel:'result',name:'result'}
    
                        ]
    });

        return formPanel;
    },
    createWin:function()
    {
        return this.initWin(438,300,"ClientVisit管理");
    },
    storeMapping:["id",
    "vdate","endTime","remark","title","content","result"   ],
    initComponent : function(){
    this.cm=new Ext.grid.ColumnModel([
{header: "vdate", sortable:true,width: 300, dataIndex:"vdate"},
{header: "endTime", sortable:true,width: 300, dataIndex:"endTime"},
{header: "remark", sortable:true,width: 300, dataIndex:"remark"},
{header: "title", sortable:true,width: 300, dataIndex:"title"},
{header: "content", sortable:true,width: 300, dataIndex:"content"},
{header: "result", sortable:true,width: 300, dataIndex:"result"}
        ]);
    ClientVisitPanel.superclass.initComponent.call(this);
}     
});