/**
 * Ext.grid.GridPanel 插件 功能 pageToolbar 支持快捷键上一页 下一页 全选 反全选
 * @class Disco.Ext.GridPanelPlugin
 * @extends Ext.util.Observable
 */
Ext.ns("Disco.Ext","Disco.Ext.Tree");
Disco.Ext.GridPanelPlugin=Ext.extend(Ext.util.Observable, {
    init : function(a) {
        this.grid = a;
        this.store = this.grid.store;
        this.grid.on("render", this.initEvents, this)
    },
    beforePage : function(c) {
        var b = this.pageToolbar.getPageData();
        var d = b.activePage;
        var a = b.pages;
        switch (c) {
            case "next" :
                if (d === a) {
                    return false
                }
                break;
            case "prev" :
                if (d == 1) {
                    return false
                }
                break
        }
    },
    initEvents : function() {
        this.keyMap = this.grid.getKeyMap();
        this.pageToolbar = this.grid.getBottomToolbar();
        if (this.pageToolbar) {
            this.keyMap.addBinding([{
                key : Ext.EventObject.RIGHT,
                fn : this.pageToolbar.onClick.createInterceptor(
                        this.beforePage, this).createDelegate(
                        this.pageToolbar, ["next"]),
                shift : true,
                scope : this
            }, {
                key : Ext.EventObject.LEFT,
                fn : this.pageToolbar.onClick.createInterceptor(
                        this.beforePage, this).createDelegate(
                        this.pageToolbar, ["prev"]),
                shift : true,
                scope : this
            }])
        };
        new Ext.KeyMap(this.grid.getGridEl(), [{
            key : Ext.EventObject.A,
            stopEvent : true,
            ctrl : true,
            fn : function(a, b) {
                this.grid.getSelectionModel().selectAll()
            },
            scope : this
        }, {
            key : Ext.EventObject.X,
            stopEvent : true,
            ctrl : true,
            fn : function() {
                this.grid.getSelectionModel().clearSelections()
            },
            scope : this
        }])
    }
});
/**
 * 表单元素帮助提示信息
 * @class Disco.Ext.HelpIconPlugin
 * @extends Ext.util.Observable
 */
Disco.Ext.HelpIconPlugin = Ext.extend(Ext.util.Observable, {
    init:function(field) {
    Ext.apply(field, {
      onRender:field.onRender.createSequence(function(ct, position) {
        // If field has the fieldLabel object, add the helpIcon
        if(this.fieldLabel && this.helpText) {
          var label = this.el.findParent('.x-form-element', 5, true) || this.el.findParent('.x-form-field-wrap', 5, true);
          
          this.helpIcon = label.createChild({
            cls:(this.helpIconCls || 'ux-helpicon-icon')
          ,    style:'width:16px; height:18px; position:absolute; left:0; top:0; display:block; background:transparent no-repeat scroll 0 2px;'
          });
          
          this.alignHelpIcon = function(){
            var el = this.wrap ? this.wrap : this.el; 
            this.helpIcon.alignTo(el, 'tl-tr', [2, 0]);
          }
          // Redefine alignErrorIcon to move the errorIcon (if it exist) to
            // the right of helpIcon
          if(this.alignErrorIcon) {
            this.alignErrorIcon = function() {
              this.errorIcon.alignTo(this.helpIcon, 'tl-tr', [2, 0]);
            }
          }
          
          this.on('resize', this.alignHelpIcon, this);
          // Register QuickTip for icon
          Ext.QuickTips.register({
            target:this.helpIcon
          , title:(this.helpTitle || '')
          , text:(this.helpText || 'No help defined yet!')
          , enabled:true
          });
        }
      }) 
    }); 
    } 
}); 

/**
 * tabpanel的按键操作 backspace ,tab
 * @class Disco.Ext.TabHistoryPlugin
 * @extends Object
 */
Disco.Ext.TabHistoryPlugin = Ext.extend(Object,{
	/**
	 * tabpanel item项改变的时候 
	 * @param {Tabpanel} tp
	 * @param {Component} tab
	 */
	onTabChange:function(tp,tab){
		tp.focus();
		this.history.push(tab.id);
	},
	/**
	 * tabpanel销毁前触发
	 * @param {Tabpanel} t
	 */
	onTabBeforedestroy:function(t){
		Ext.del(this,'history','tabpanel');
		t.un('beforedestroy',this.onTabBeforedestroy,this);
	},
	/**
	 * tabpanel render的是触发
	 */
	onTabRender:function(){
		var tp = this.tabpanel;
		var km= tp.getKeyMap();
		tp.getEl().setStyle("outline","0 none");
		tp.getEl().dom.setAttribute('tabindex',0);
		
		km.addBinding([{
			scope : this ,
			key : Ext.EventObject.BACKSPACE,
			fn : function(k,e){
				e.stopPropagation();
				this.onBack();
			}
		},{
			scope : this ,
			key : Ext.EventObject.TAB,
			fn : function(k,e){
				var at = tp.getActiveTab();
				var items = tp.items,count = items.getCount(),index=0;
				if(at){
					index=items.indexOf(at);
					if(++index==count)index = 0;
				}
				if(count)tp.setActiveTab(index);
			}
		}]);
	},
	/**
	 * 上一个tab面板
	 */
	onBack:function(){
		 var tp =this.tabpanel; 
		 var history;
		 if(this.history.length){
		 	var at = tp.getActiveTab();
		 	if(this.history.length==1){
		 		return ;
		 	}
		 	this.history.pop();
		 	while(history = this.history.pop()){
		 		var p = tp.getItem(history);
		 		if(p){
		 			tp.setActiveTab(p);
		 			break;
		 		}
		 	}
		 }
	},
	init : function(t){
		this.tabpanel = t;
		this.history = [];
		t.on('tabchange',this.onTabChange,this);
		t.on('beforedestroy',this.onTabBeforedestroy,this);
		t.on('render',this.onTabRender,this);
	}
});
Ext.preg('tabhistoryplugin',Disco.Ext.TabHistoryPlugin);


Ext.ns('Ext.ux.grid');
Ext.ux.grid.GridSummary = function(config) {
        Ext.apply(this, config);
};
Ext.extend(Ext.ux.grid.GridSummary, Ext.util.Observable, {
    init : function(grid) {
        this.grid = grid;
        this.cm = grid.getColumnModel();
        this.view = grid.getView();

        var v = this.view;

        // override GridView's onLayout() method
        v.onLayout = this.onLayout;

        v.afterMethod('render', this.refreshSummary, this);
        v.afterMethod('refresh', this.refreshSummary, this);
        v.afterMethod('syncScroll', this.syncSummaryScroll, this);
        v.afterMethod('onColumnWidthUpdated', this.doWidth, this);
        v.afterMethod('onAllColumnWidthsUpdated', this.doAllWidths, this);
        v.afterMethod('onColumnHiddenUpdated', this.doHidden, this);

        // update summary row on store's add/remove/clear/update events
        grid.store.on({
            add: this.refreshSummary,
            remove: this.refreshSummary,
            clear: this.refreshSummary,
            update: this.refreshSummary,
            scope: this
        });

        if (!this.rowTpl) {
            this.rowTpl = new Ext.Template(
                '<div class="x-grid3-summary-row x-grid3-gridsummary-row-offset">',
                    '<table class="x-grid3-summary-table" border="0" cellspacing="0" cellpadding="0" style="{tstyle}">',
                        '<tbody><tr>{cells}</tr><tr style="display:{totalDisplay}">{tcells}</tr></tbody>',
                    '</table>',
                '</div>'
            );
            this.rowTpl.disableFormats = true;
        }
        this.rowTpl.compile();

        if (!this.cellTpl) {
            this.cellTpl = new Ext.Template(
                '<td class="x-grid3-col x-grid3-cell x-grid3-td-{id} {css}" style="{style}">',
                    '<div class="x-grid3-cell-inner x-grid3-col-{id}" unselectable="on" {attr}>{value}</div>',
                "</td>"
            );
            this.cellTpl.disableFormats = true;
        }
        this.cellTpl.compile();
    },

    calculate : function(rs, cm,isTotal) {
        var data = {}, cfg = cm.config;
        for (var i = 0, len = cfg.length; i < len; i++) { // loop through all
                                                            // columns in
                                                            // ColumnModel
            var cf = cfg[i], // get column's configuration
                cname = cf.dataIndex; // get column dataIndex

            // initialise grid summary row data for
            // the current column being worked on
            data[cname+(isTotal?"_total":"")] = 0;

            if (cf.summaryType) {
                for (var j = 0, jlen = rs.length; j < jlen; j++) {
                    var r = rs[j]; // get a single Record
                    if(isTotal){
                        data[cname+"_total"] = Ext.ux.grid.GridSummary.Calculations[cf.summaryType](r[cname], r, cname+"_total", data, j);
                    }
                    else {
                        data[cname] = Ext.ux.grid.GridSummary.Calculations[cf.summaryType](r.get(cname), r, cname, data, j);
                    }
                }
            }
        }

        return data;
    },

    onLayout : function(vw, vh) {
        if (Ext.type(vh) != 'number') { // handles grid's height:'auto' config
            return;
        }
        // note: this method is scoped to the GridView
        if (!this.grid.getGridEl().hasClass('x-grid-hide-gridsummary')) {
            // readjust gridview's height only if grid summary row is visible
            this.scroller.setHeight(vh - this.summary.getHeight());
        }
    },

    syncSummaryScroll : function() {
        var mb = this.view.scroller.dom;

        this.view.summaryWrap.dom.scrollLeft = mb.scrollLeft;
        this.view.summaryWrap.dom.scrollLeft = mb.scrollLeft; // second time
                                                                // for IE (1/2
                                                                // time first
                                                                // fails, other
                                                                // browsers
                                                                // ignore)
    },

    doWidth : function(col, w, tw) {
        var s = this.view.summary.dom;

        s.firstChild.style.width = tw;
        s.firstChild.rows[0].childNodes[col].style.width = w;
    },

    doAllWidths : function(ws, tw) {
        var s = this.view.summary.dom, wlen = ws.length;

        s.firstChild.style.width = tw;

        var cells = s.firstChild.rows[0].childNodes;

        for (var j = 0; j < wlen; j++) {
            cells[j].style.width = ws[j];
        }
    },

    doHidden : function(col, hidden, tw) {
        var s = this.view.summary.dom,
            display = hidden ? 'none' : '';

        s.firstChild.style.width = tw;
        s.firstChild.rows[0].childNodes[col].style.display = display;
    },

    renderSummary : function(o, cs, cm) {
        cs = cs || this.view.getColumnData();
        var cfg = cm.config, buf = [],tbuf=[], last = cs.length - 1;
        for (var i = 0, len = cs.length; i < len; i++) {
            var c = cs[i], cf = cfg[i], p = {};

            p.id = c.id;
            p.style = c.style;
            p.css = i == 0 ? 'x-grid3-cell-first ' : (i == last ? 'x-grid3-cell-last ' : '');
            var tp=Ext.apply({},p);
          
            if (cf.summaryType || cf.summaryRenderer) {             
                p.value = (cf.summaryRenderer || c.renderer)(o.data[c.name], p, o);
                tp.value=(cf.summaryTotalRenderer||cf.summaryRenderer || c.renderer)(o.data[c.name+"_total"], p, o);
            } else {
                p.value = '';
                tp.value='';
            }
              
            if (p.value == undefined || p.value === "") p.value = "&#160;";
            if (tp.value == undefined || tp.value === "") tp.value = "&#160;";
            buf[buf.length] = this.cellTpl.apply(p);
            tbuf[tbuf.length]=this.cellTpl.apply(tp);
            
        }

        return this.rowTpl.apply({
            tstyle: 'width:' + this.view.getTotalWidth() + ';',
            cells: buf.join(''),
            tcells:tbuf.join(''),
            totalDisplay:this.grid.store.refreshCache?"display":"none"
        });
    },

    refreshSummary : function() {
        var g = this.grid, ds = g.store,
            cs = this.view.getColumnData(),
            cm = this.cm,
            rs = ds.getRange();
            var data = this.calculate(rs, cm);
            if(ds.refreshCache){
                var total=this.calculate(ds.proxy.getData().getRange(), cm,true);
                Ext.apply(data,total);
            }       
            var buf = this.renderSummary({data: data}, cs, cm);
            
        if (!this.view.summaryWrap) {        
            this.view.summaryWrap = Ext.DomHelper.insertAfter(this.view.scroller, {
                tag: 'div',
                cls: 'x-grid3-gridsummary-row-inner'
            }, true);           
        }
        this.view.summary = this.view.summaryWrap.update(buf).first();
        // alert(this.view.summary.dom.innerHTML);
       /*
         * Ext.DomHelper.insertAfter(this.view.summary.parent(),'<div
         * class="x-grid3-summary-row x-grid3-gridsummary-row-offset"><table
         * class="x-grid3-summary-table" border="0" cellspacing="0"
         * cellpadding="0" style="width:210px;"><tbody><tr><td class="x-grid3-col x-grid3-cell x-grid3-td-0 x-grid3-cell-first " style="width:98px;display:none;"><div
         * class="x-grid3-cell-inner x-grid3-col-0" unselectable="on" >&#160;</div></td><td class="x-grid3-col x-grid3-cell x-grid3-td-1 " style="width:18px;"><div
         * class="x-grid3-cell-inner x-grid3-col-1" unselectable="on" >&#160;</div></td><td class="x-grid3-col x-grid3-cell x-grid3-td-2 " style="width:18px;"><div
         * class="x-grid3-cell-inner x-grid3-col-2" unselectable="on" >&#160;</div></td><td class="x-grid3-col x-grid3-cell x-grid3-td-3 " style="width:18px;text-align:right;"><div
         * class="x-grid3-cell-inner x-grid3-col-3" unselectable="on" >0</div></td><td class="x-grid3-col x-grid3-cell x-grid3-td-4 " style="width:18px;text-align:right;"><div
         * class="x-grid3-cell-inner x-grid3-col-4" unselectable="on" >0</div></td><td class="x-grid3-col x-grid3-cell x-grid3-td-5 " style="width:18px;text-align:right;"><div
         * class="x-grid3-cell-inner x-grid3-col-5" unselectable="on" >0</div></td><td class="x-grid3-col x-grid3-cell x-grid3-td-6 " style="width:18px;text-align:right;"><div
         * class="x-grid3-cell-inner x-grid3-col-6" unselectable="on" >0</div></td><td class="x-grid3-col x-grid3-cell x-grid3-td-7 " style="width:18px;text-align:right;"><div
         * class="x-grid3-cell-inner x-grid3-col-7" unselectable="on" >0</div></td><td class="x-grid3-col x-grid3-cell x-grid3-td-8 " style="width:18px;text-align:right;"><div
         * class="x-grid3-cell-inner x-grid3-col-8" unselectable="on" >0</div></td><td class="x-grid3-col x-grid3-cell x-grid3-td-9 " style="width:13px;text-align:right;"><div
         * class="x-grid3-cell-inner x-grid3-col-9" unselectable="on" >&#160;</div></td><td class="x-grid3-col x-grid3-cell x-grid3-td-10 " style="width:13px;text-align:right;"><div
         * class="x-grid3-cell-inner x-grid3-col-10" unselectable="on" >&#160;</div></td><td class="x-grid3-col x-grid3-cell x-grid3-td-11 x-grid3-cell-last " style="width:18px;"><div
         * class="x-grid3-cell-inner x-grid3-col-11" unselectable="on" >&#160;</div></td></tr></tbody></table></div>');
         * alert(111);
         */
    },

    toggleSummary : function(visible) { // true to display summary row
        var el = this.grid.getGridEl();

        if (el) {
            if (visible === undefined) {
                visible = el.hasClass('x-grid-hide-gridsummary');
            }
            el[visible ? 'removeClass' : 'addClass']('x-grid-hide-gridsummary');

            this.view.layout(); // readjust gridview height
        }
    },

    getSummaryNode : function() {
        return this.view.summary
    }
});
Ext.reg('gridsummary', Ext.ux.grid.GridSummary);

/*
 * all Calculation methods are called on each Record in the Store with the
 * following 5 parameters:
 * 
 * v - cell value record - reference to the current Record colName - column name
 * (i.e. the ColumnModel's dataIndex) data - the cumulative data for the current
 * column + summaryType up to the current Record rowIdx - current row index
 */
Ext.ux.grid.GridSummary.Calculations = {
    sum : function(v, record, colName, data, rowIdx) {
        return data[colName] + Ext.num(v, 0);
    },

    count : function(v, record, colName, data, rowIdx) {
        return rowIdx + 1;
    },

    max : function(v, record, colName, data, rowIdx) {
        return Math.max(Ext.num(v, 0), data[colName]);
    },

    min : function(v, record, colName, data, rowIdx) {
        return Math.min(Ext.num(v, 0), data[colName]);
    },

    average : function(v, record, colName, data, rowIdx) {
        var t = data[colName] + Ext.num(v, 0), count = record.store.getCount();
        try{
        var result= rowIdx == count - 1 ? (t / count) : t;
         return result;
        }
        catch(e){
            alert(e);
        }
       
        
    },
    last:function(v, record, colName, data, rowIdx) {
        return v;
    }
}


/*
 * Ext JS Library 2.2 Copyright(c) 2006-2008, Ext JS, LLC. licensing@extjs.com
 * http://extjs.com/license
 */
Ext.grid.GroupSummary = function(config){
    Ext.apply(this, config);
};

Ext.extend(Ext.grid.GroupSummary, Ext.util.Observable, {
    init : function(grid){
        this.grid = grid;
        this.cm = grid.getColumnModel();
        this.view = grid.getView();

        var v = this.view;
        v.doGroupEnd = this.doGroupEnd.createDelegate(this);

        v.afterMethod('onColumnWidthUpdated', this.doWidth, this);
        v.afterMethod('onAllColumnWidthsUpdated', this.doAllWidths, this);
        v.afterMethod('onColumnHiddenUpdated', this.doHidden, this);
        v.afterMethod('onUpdate', this.doUpdate, this);
        v.afterMethod('onRemove', this.doRemove, this);

        if(!this.rowTpl){
            this.rowTpl = new Ext.Template(
                '<div class="x-grid3-summary-row" style="{tstyle}">',
                '<table class="x-grid3-summary-table" border="0" cellspacing="0" cellpadding="0" style="{tstyle}">',
                    '<tbody><tr>{cells}</tr></tbody>',
                '</table></div>'
            );
            this.rowTpl.disableFormats = true;
        }
        this.rowTpl.compile();

        if(!this.cellTpl){
            this.cellTpl = new Ext.Template(
                '<td class="x-grid3-col x-grid3-cell x-grid3-td-{id} {css}" style="{style}">',
                '<div class="x-grid3-cell-inner x-grid3-col-{id}" unselectable="on">{value}</div>',
                "</td>"
            );
            this.cellTpl.disableFormats = true;
        }
        this.cellTpl.compile();
    },

    toggleSummaries : function(visible){
        var el = this.grid.getGridEl();
        if(el){
            if(visible === undefined){
                visible = el.hasClass('x-grid-hide-summary');
            }
            el[visible ? 'removeClass' : 'addClass']('x-grid-hide-summary');
        }
    },

    renderSummary : function(o, cs){
        cs = cs || this.view.getColumnData();
        var cfg = this.cm.config;

        var buf = [], c, p = {}, cf, last = cs.length-1;
        for(var i = 0, len = cs.length; i < len; i++){
            c = cs[i];
            cf = cfg[i];
            p.id = c.id;
            p.style = c.style;
            p.css = i == 0 ? 'x-grid3-cell-first ' : (i == last ? 'x-grid3-cell-last ' : '');
            if(cf.summaryType || cf.summaryRenderer){
                p.value = (cf.summaryRenderer || c.renderer)(o.data[c.name], p, o);
            }else{
                p.value = '';
            }
            if(p.value == undefined || p.value === "") p.value = "&#160;";
            buf[buf.length] = this.cellTpl.apply(p);
        }

        return this.rowTpl.apply({
            tstyle: 'width:'+this.view.getTotalWidth()+';',
            cells: buf.join('')
        });
    },

    calculate : function(rs, cs){
        var data = {}, r, c, cfg = this.cm.config, cf;
        for(var j = 0, jlen = rs.length; j < jlen; j++){
            r = rs[j];
            for(var i = 0, len = cs.length; i < len; i++){
                c = cs[i];
                cf = cfg[i];
                if(cf.summaryType){
                    data[c.name] = Ext.grid.GroupSummary.Calculations[cf.summaryType](data[c.name] || 0, r, c.name, data);
                }
            }
        }
        return data;
    },

    doGroupEnd : function(buf, g, cs, ds, colCount){
        var data = this.calculate(g.rs, cs);
        buf.push('</div>', this.renderSummary({data: data}, cs), '</div>');
    },

    doWidth : function(col, w, tw){
        var gs = this.view.getGroups(), s;
        for(var i = 0, len = gs.length; i < len; i++){
            s = gs[i].childNodes[2];
            s.style.width = tw;
            s.firstChild.style.width = tw;
            s.firstChild.rows[0].childNodes[col].style.width = w;
        }
    },

    doAllWidths : function(ws, tw){
        var gs = this.view.getGroups(), s, cells, wlen = ws.length;
        for(var i = 0, len = gs.length; i < len; i++){
            s = gs[i].childNodes[2];
            s.style.width = tw;
            s.firstChild.style.width = tw;
            cells = s.firstChild.rows[0].childNodes;
            for(var j = 0; j < wlen; j++){
                cells[j].style.width = ws[j];
            }
        }
    },

    doHidden : function(col, hidden, tw){
        var gs = this.view.getGroups(), s, display = hidden ? 'none' : '';
        for(var i = 0, len = gs.length; i < len; i++){
            s = gs[i].childNodes[2];
            s.style.width = tw;
            s.firstChild.style.width = tw;
            s.firstChild.rows[0].childNodes[col].style.display = display;
        }
    },

    // Note: requires that all (or the first) record in the
    // group share the same group value. Returns false if the group
    // could not be found.
    refreshSummary : function(groupValue){
        return this.refreshSummaryById(this.view.getGroupId(groupValue));
    },

    getSummaryNode : function(gid){
        var g = Ext.fly(gid, '_gsummary');
        if(g){
            return g.down('.x-grid3-summary-row', true);
        }
        return null;
    },

    refreshSummaryById : function(gid){
        var g = document.getElementById(gid);
        if(!g){
            return false;
        }
        var rs = [];
        this.grid.store.each(function(r){
            if(r._groupId == gid){
                rs[rs.length] = r;
            }
        });
        var cs = this.view.getColumnData();
        var data = this.calculate(rs, cs);
        var markup = this.renderSummary({data: data}, cs);

        var existing = this.getSummaryNode(gid);
        if(existing){
            g.removeChild(existing);
        }
        Ext.DomHelper.append(g, markup);
        return true;
    },

    doUpdate : function(ds, record){
        this.refreshSummaryById(record._groupId);
    },

    doRemove : function(ds, record, index, isUpdate){
        if(!isUpdate){
            this.refreshSummaryById(record._groupId);
        }
    },

    showSummaryMsg : function(groupValue, msg){
        var gid = this.view.getGroupId(groupValue);
        var node = this.getSummaryNode(gid);
        if(node){
            node.innerHTML = '<div class="x-grid3-summary-msg">' + msg + '</div>';
        }
    }
});

Ext.grid.GroupSummary.Calculations = {
    'sum' : function(v, record, field){
        return v + (record.data[field]||0);
    },

    'count' : function(v, record, field, data){
        return data[field+'count'] ? ++data[field+'count'] : (data[field+'count'] = 1);
    },

    'max' : function(v, record, field, data){
        var v = record.data[field];
        var max = data[field+'max'] === undefined ? (data[field+'max'] = v) : data[field+'max'];
        return v > max ? (data[field+'max'] = v) : max;
    },

    'min' : function(v, record, field, data){
        var v = record.data[field];
        var min = data[field+'min'] === undefined ? (data[field+'min'] = v) : data[field+'min'];
        return v < min ? (data[field+'min'] = v) : min;
    },

    'average' : function(v, record, field, data){
        var c = data[field+'count'] ? ++data[field+'count'] : (data[field+'count'] = 1);
        var t = (data[field+'total'] = ((data[field+'total']||0) + (record.data[field]||0)));
        return t === 0 ? 0 : t / c;
    },
    'last':function(v, record, field, data) {
        return v;
    }
}

Ext.grid.HybridSummary = Ext.extend(Ext.grid.GroupSummary, {
    calculate : function(rs, cs){
        var gcol = this.view.getGroupField();
        var gvalue = rs[0].data[gcol];
        var gdata = this.getSummaryData(gvalue);
        return gdata || Ext.grid.HybridSummary.superclass.calculate.call(this, rs, cs);
    },

    updateSummaryData : function(groupValue, data, skipRefresh){
        var json = this.grid.store.reader.jsonData;
        if(!json.summaryData){
            json.summaryData = {};
        }
        json.summaryData[groupValue] = data;
        if(!skipRefresh){
            this.refreshSummary(groupValue);
        }
    },
    getSummaryData : function(groupValue){
        var json = this.grid.store.reader.jsonData;
        if(json && json.summaryData){
            return json.summaryData[groupValue];
        }
        return null;
    }
});

/**
 * 邮件菜单树插件，支持数据同步更新
 * 
 * @param {} config
 */
Ext.ns("Disco.Ext.Tree");
Disco.Ext.Tree.LocalPlugin = function(config) {
	Ext.apply(this, config);
	Disco.Ext.Tree.LocalPlugin.superclass.constructor.call(this, config);
}
Ext.extend(Disco.Ext.Tree.LocalPlugin, Ext.util.Observable, {
	init : function(tree) {
		this.tree = tree;
		this.loader = this.tree.loader;
		this.tree.on('render', this.initEvents, this);
		this.tree.on('beforedestroy', this.destroyCmp, this);
	},
	destroyCmp : function() {
		delete this.tree;
		delete this.loader;
		var shareCache = this.loader.shareCache;
		shareCache.un("addnode", this.addUINode, this);// 数据变化,则创建新的节点
		shareCache.un("updatenode", this.updateUINode, this);// 更新节点内容
		shareCache.un("removenode", this.removeUINode, this);// 删除节点内容
	},
	initEvents : function() {
		var shareCache = this.loader.shareCache;
		shareCache.on("addnode", this.addUINode, this);// 数据变化,则创建新的节点
		shareCache.on("updatenode", this.updateUINode, this);// 更新节点内容
		shareCache.on("removenode", this.removeUINode, this);// 删除节点内容
	},
	addUINode : function(node, parentNode) {
		var parent = this.getNodeById(parentNode.id);
		if (parent) {
			parent.leaf = false;
			parent.appendChild(node);
		}
	},
	updateUINode : function(upateNode) {
		var node = this.getNodeById(upateNode.id);
		if (node) {
			if (Ext.type(this.loader.transferNode) == 'function') {
				upateNode = Ext.apply( {}, upateNode);
				this.loader.transferNode(upateNode);
			}
			node.setText(upateNode.text);
			Ext.copyToIf(node.attributes, upateNode, ["children", "cls",
					"loader", "parentNode"]);
			if (node.getUI() instanceof Ext.ux.tree.ColumnNodeUI) {
				node.getUI().updateNode(upateNode);
			}
			if (!Ext.isEmpty(upateNode.qtip)) {
				if (node.getUI().getTextEl().setAttributeNS) {
					node.getUI().getTextEl().setAttributeNS("ext", "qtip",
							upateNode.qtip);
				} else {
					node.getUI().getTextEl().setAttribute("ext:qtip",
							upateNode.qtip);
				}
			}
		}
	},
	removeUINode : function(node) {
		var parentNode = node.parentNode;
		if (parentNode && parentNode.children.length == 0) {
			var parentNode = this.getNodeById(parentNode.id);
			if (parentNode) {
				parentNode.leaf = true;
				parentNode.getUI().updateExpandIcon();
			}
		}
		var node = this.getNodeById(node.id);
		if (node) {
			node.remove();
		}
	},
	getNodeById : function(id) {
		return this.tree.getNodeById(id);
	}
});

Ext.ns("Disco.Ext");
Disco.Ext.TabPanelPlugin = function(b) {
	Ext.apply(this, b);
	Disco.Ext.TabPanelPlugin.superclass.constructor.call(this)
};
Ext.extend(Disco.Ext.TabPanelPlugin,Ext.util.Observable,{
	init : function(b) {
		this.tabPanel = b;
		this.initEvents()
	},
	initEvents : function() {
		this.tabPanel.on("contextmenu", this.showContextMenu,
				this);
		if (typeof(this.tabPanel.tabSize) == "number") {
			this.tabPanel
					.on("beforeadd", this.maxTabSize, this)
		}
	},
	maxTabSize : function(f, d, e) {
		if (f.items.getCount() > this.tabPanel.tabSize) {
			Ext.Msg
					.show( {
						title : "\u6e29\u99a8\u63d0\u793a",
						msg : "\u9762\u677f\u8d85\u8fc7\u6700\u5927\u6570\uff0c\u8bf7\u5148\u5173\u95ed\uff01",
						buttons : Ext.Msg.OK,
						icon : Ext.Msg.INFO
					});
			return false
		}
	},
	createTabMenu : function() {
		return new Ext.menu.Menu( {
			items : [ {
				text : "\u5173\u95ed\u5f53\u524d\u9762\u677f",
				id : "closaCurrentTab",
				handler : this.closeTab,
				scope : this
			}, {
				text : "\u5173\u95ed\u6240\u6709\u9762\u677f",
				handler : this.closeAllTab,
				id : "closaAllTab",
				scope : this
			}, {
				text : "关闭其它页签",
				id : "closeOtherTab",
				handler : this.closeElsetTab,
				scope : this
			}, {
				text : "\u5173\u95ed\u53f3\u8fb9\u9762\u677f",
				id : "closaRightTab",
				handler : this.closeRightTab,
				scope : this
			}, {
				text : "\u5173\u95ed\u5de6\u8fb9\u9762\u677f",
				id : "closaLeftTab",
				handler : this.closeLeftTab,
				scope : this
			}]
		})
	},
	showContextMenu : function(f, e, d) {
		if (!this.theMenu) {
			this.theMenu = this.createTabMenu()
		}
		this.doAction(f, e, d);
		this.theMenu.currentTab = e;
		this.theMenu.showAt(d.getXY())
	},
	doAction : function(tabPanel, tab, e) {
		!tab.closable
				? this.theMenu.items.get('closaCurrentTab')
						.disable()
				: this.theMenu.items.get('closaCurrentTab')
						.enable();
		var isDisableAll = true;
		var isDisableRight = true;
		var isDisableLeft = true;
		tabPanel.items.each(function(tab) {
			if (tab.closable)
				isDisableAll = false
		}, this);
		var closableTabs = tabPanel.items.filter('closable',
				true);
		if (isDisableAll) {
			this.theMenu.items.get('closeOtherTab')
					.setDisabled(true)
		} else {
			this.theMenu.items
					.get('closeOtherTab')
					.setDisabled((closableTabs.getCount() == 1 && closableTabs
							.itemAt(0) == tab))
		}
		isDisableAll ? this.theMenu.items.get('closaAllTab')
				.disable() : this.theMenu.items
				.get('closaAllTab').enable();
		!tab.closable
				? this.theMenu.items.get('closaCurrentTab')
						.disable()
				: this.theMenu.items.get('closaCurrentTab')
						.enable();
		var currentIndex = tabPanel.items.indexOf(tab);
		if (currentIndex == this.tabPanel.items.getCount() - 1) {
			this.theMenu.items.get('closaRightTab').disable()
		} else {
			this.theMenu.items.get('closaRightTab').enable()
		}
		for (var i = currentIndex;i < tabPanel.items.getCount()
				- 1; i++) {
			if (tabPanel.items.itemAt(i).closable) {
				isDisableRight = false
			}
		}
		isDisableRight
				? this.theMenu.items.get('closaRightTab')
						.disable()
				: this.theMenu.items.get('closaRightTab')
						.enable();
		for (var i = currentIndex - 1;i > 0; i--) {
			if (tabPanel.items.itemAt(i).closable) {
				isDisableLeft = false
			}
		}
		isDisableLeft ? this.theMenu.items.get('closaLeftTab')
				.disable() : this.theMenu.items
				.get('closaLeftTab').enable()
	},
	closeAllTab : function(d, f) {
		var e = d.parentMenu.currentTab;
		this.tabPanel.items.each(function(a) {
			if (a.closable && a != f) {
				this.tabPanel.remove(a)
			}
		}, this)
	},
	closeTab : function(c) {
		var d = c.parentMenu.currentTab;
		if (d.closable) {
			this.tabPanel.remove(d)
		}
	},
	closeElsetTab : function(c) {
		var d = c.parentMenu.currentTab;
		this.closeAllTab(c, d)
	},
	closeLeftTab : function(g) {
		var h = g.parentMenu.currentTab;
		var f = this.tabPanel.items.indexOf(h) - 1;
		if (f <= 0) {
			return
		}
		var e = this.tabPanel.items.getRange(1, f);
		Ext.each(e, function(a) {
			if (a && a.closable) {
				this.tabPanel.remove(a)
			}
		}, this)
	},
	closeRightTab : function(f) {
		var d = f.parentMenu.currentTab;
		var e = this.tabPanel.items.getRange(
				this.tabPanel.items.indexOf(d) + 1,
				this.tabPanel.items.getCount());
		Ext.each(e, function(a) {
			if (!a || a == d || !a.closable) {
				return
			}
			this.tabPanel.remove(a)
		}, this)
	}
});