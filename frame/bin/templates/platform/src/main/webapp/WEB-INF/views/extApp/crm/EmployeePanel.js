EmployeePanel = Ext.extend(Disco.Ext.CrudPanel, {
    id: "employeePanel",
    baseUrl: "employee.java",
    createForm: function() {
        var formPanel = new Ext.form.FormPanel({
            frame: true,
            labelWidth: 70,
            labelAlign: 'right',
            defaultType: 'textfield',
            defaults: {
                width: 200
            },
            items: [{
                xtype: "hidden",
                name: "id"
            }, {
                fieldLabel: 'trueName',
                name: 'trueName'
            }, {
                fieldLabel: 'sex',
                name: 'sex'
            }, {
                fieldLabel: 'tel',
                name: 'tel'
            }, {
                fieldLabel: 'sn',
                name: 'sn'
            }, {
                fieldLabel: 'birthday',
                name: 'birthday'
            }, {
                fieldLabel: 'address',
                name: 'address'
            }]
        });

        return formPanel;
    },
    createWin: function() {
        return this.initWin(438, 300, "Employee管理");
    },
    storeMapping: ["id", "trueName", "sex", "tel", "sn", "birthday", "address"],
    initComponent: function() {
        this.cm = new Ext.grid.ColumnModel([{
            header: "trueName",
            sortable: true,
            width: 300,
            dataIndex: "trueName"
        }, {
            header: "sex",
            sortable: true,
            width: 300,
            dataIndex: "sex"
        }, {
            header: "tel",
            sortable: true,
            width: 300,
            dataIndex: "tel"
        }, {
            header: "sn",
            sortable: true,
            width: 300,
            dataIndex: "sn"
        }, {
            header: "birthday",
            sortable: true,
            width: 300,
            dataIndex: "birthday"
        }, {
            header: "address",
            sortable: true,
            width: 300,
            dataIndex: "address"
        }]);
        EmployeePanel.superclass.initComponent.call(this);
    }
});