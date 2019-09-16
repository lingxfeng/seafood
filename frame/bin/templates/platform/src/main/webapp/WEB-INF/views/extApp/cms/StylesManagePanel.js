// 首页样式风格管理
StylesManagePanel= Ext.extend(Disco.Ext.CrudPanel, {
    id: "stylesManagePanel",
   // title:"风格管理",
    baseUrl: "stylesManage.java",
    gridSelModel: 'checkbox',
    fileUpload:true,
    pageSize: 20,
    cssName:[["默认", 'style.css'], ["红色", 'style2.css'],["蓝色", 'style3.css']],
    cssNameFormat:function(v){
    	if(v=='style.css'){
    		return "默认";
    	}else if(v=='style2.css'){
    		return "红色";
    	}else if(v=='style3.css'){
    		return "蓝色";
    	}
    },
    isValid: [["启用", 1], ["禁用", 2] ],
    isValidFormat:function(v){
    	if(v==1){
    		return "启用";
    	}else if(v==2){
    		return "禁用";
    	}
    },
    bgPicRender: function(v) {
    	if (v!=null&&v!='') {
    		 return v+'<b><a style="color:green" href="'+v+'" target="_blank">&nbsp查看</a></b>'	
		}else
        return ''
    },
    createForm: function() {
    	
        var formPanel = new Ext.form.FormPanel({
            frame: true,
            labelWidth: 80,
            fileUpload: true,
            labelAlign: 'right',
            defaults: {
                width: 320,
                xtype: "textfield"
            },
            items: [{
                xtype: "hidden",
                name: "id"
            }, {
                fieldLabel: "名称",
                name: "name",
                emptyText: '名称不能为空',
				allowBlank: false,
				blankText: '名称不能为空'

            }, {
            	xtype: "combo",
                name: "cssName",
                hiddenName: "cssName",
                fieldLabel: "选择主题",
                displayField: "title",
                valueField: "value",
				allowBlank: false,
				blankText: '主题不能为空',
                store: new Ext.data.SimpleStore({
                    fields: ['title', 'value'],
                    data: this.cssName
                }),
                editable: false,
                mode: 'local',
                triggerAction: 'all',
                emptyText: '请选择...'
            }, {
                xtype: "combo",
                name: "isValid",
                hiddenName: "isValid",
                fieldLabel: "状态",
                displayField: "title",
                valueField: "value",
				allowBlank: false,
				blankText: '状态不能为空',
                store: new Ext.data.SimpleStore({
                    fields: ['title', 'value'],
                    data: this.isValid
                }),
                editable: false,
                mode: 'local',
                triggerAction: 'all',
                emptyText: '请选择...'
            }, 
              {
                xtype: 'fileuploadfield',
                emptyText: '选择上传文件',
                fieldLabel: '背景图片',
                name: "bgPicPath",
                buttonText: '选择',
                listeners: {
                    'fileselected': function(fb, v) { //undefined
                        var suffix = "";
                        var ext = "";
                        if (v != null) {
                            suffix = v.split(".")[1];
                            ext = suffix.toUpperCase();
                        }
                        if (suffix != '' && suffix != 'undefined') {
                            if (ext != "BMP" && ext != "PNG" && ext != "GIF" && ext != "JPG" && ext != "JPEG") {
                                Ext.Msg.alert("信息提示", "只能上传图片,图片限于bmp,png,gif,jpeg,jpg格式");
                                fb.setValue("");
                            }

                        }
                    }

                }// END listeners
            },
             {
               	 xtype: "textarea",
                 fieldLabel: "描述",
                 name: "info",
                 width: 330,
                 height:100
                	
            }
 
            ]
        });
        return formPanel;
    },
    userRender: function(v) {
        return v ? v.trueName : "$!{lang.get('Unknown')}";
    },
    createWin: function() {
        return this.initWin(450, 300, "首页样式风格管理");
    },
    storeMapping: ["id", "name", "cssName", "createTime", "isValid","bgPicPath"],
    initComponent: function() {
        this.cm = new Ext.grid.ColumnModel([{
            header: "名称",
            sortable: true,
            width: 100,
            dataIndex: "name"
        }, {
            header: "主题",
            sortable: true,
            width: 100,
            dataIndex: "cssName",
            renderer:this.cssNameFormat
        },
        {
            header: "背景",
            sortable: true,
            width: 120,
            dataIndex: "bgPicPath",
            renderer:this.bgPicRender
        },
          {
            header: "创建时间",
            sortable: true,
            width: 50,
            dataIndex: "createTime",
            renderer : this.dateRender()
            	
        }, {
            header: "状态",
            sortable: true,
            width: 50,
            dataIndex: "isValid",
            renderer:this.isValidFormat
        }])
        StylesManagePanel.superclass.initComponent.call(this);
    }
});
