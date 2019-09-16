/*
 * Ext JS Library 3.2.1
 * http://www.disco.org.cn/
 * 对ExtJs扩展和增强
 */
/**
 * @class Ext
 * @singleton
 */
Ext.apply(Ext, {
	clone : function(obj){
		return Ext.apply({},obj);
	},
	/**
	 * 拷贝源对象中的属性到目标对象中,不会拷贝notFields中的属性
<pre>
<code>	 
//例子：
var target = {name:'张三',age:20};
var source = {name:'李四',age:22,sex:'男',remark:'我是李四里面remark属性的值'}};
Ext.copyToIf(target,source,"name,age");
console.dir(target); //out : {name:'张三',age:20,sex:'男',remark:'我是source里面remark属性的值'}}
</code>
</pre>
	 *
	 * @param {Object} target 拷贝的目标对象 
	 * @param {Object} source 拷贝的源对象
	 * @param {String/Array} notFields {String/Array} 不会拷贝的字段 
	 *  
	 */
	copyToIf : function(target, source, notFields) {
		notFields = notFields || [];
		for (var p in source) {
			if (notFields.indexOf(p) < 0) {
				target[p] = source[p];
			}
		}
		return target;
	},
	extendX : function(supr, fn , scope){
		return Ext.extend(supr,fn.call(scope||window,supr.prototype));
	},
	/**
	 * 删除对象中的某个属性
	 * <pre>
	 * <code>
	 * //例子： 
	 * var o = {name:'zhangsan',age:20,sex:'男'};
	 * Ext.del(o,'age','sex');
	 * 
	 * console.debug(o); // age和sex被删除了 
	 * //out : {name:'zhangsan'}
	 * </code>
	 * </pre>
	 *   	 
	 * @param {Object} o 目标对象
	 * @param {String/Array} args 需要删除的属性
	 * 
	 */
	del : function(o,args/*,arg,arg...*/) {
		var ps = Array.prototype.splice.call(arguments, 1);
		Ext.each(ps, function(p) {
					delete o[p];
				});
	},
	args:function(){
		var args = arguments.callee.caller.arguments;
    	if(Ext.isArray(args[0])){
    		return args[0];
    	}else{
    		return Array.prototype.slice.call(args,0)
    	}
    	return args;
	}
	,
	/**
	 * 获取Record数组中 每个Record某字段的值
	 * <pre>
	 * <code>
	 * //例子:
	 * var records=grid.getStore().getRanage();
	 * var props=Ext.pluckRecord(records,"id");
	 * console.debug(props);
	 * </pre>
	 * </code>
	 * 
	 * @param {Array} arr Ext.data.Record 数组
	 * @param {String} prop Record中的某字段
	 * 
	 * @return {Array}
	 */
	pluckRecord : function(arr, prop) {
		var ret = [], val;
		Ext.each(arr, function(v) {
			if (v.get) {
				ret.push(v.get(prop));
			} else {
				ret.push(window.undefined);
			}
		});
		return ret;
	},
	
	
	/**
	 * 获取对象中的某个属性值,  v 支持以“.”导航
	 * 
	 * <pre>
	 * <code>
	 * //例子: 
	 * var user = {name:'张三',dept:{name:'开发部'}}
	 * var name = Ext.getObjVal(user,'dept.name');
	 * alert(name); // '开发部'
	 * </code>
	 * </pre>
	 * 
	 * @param {Object} o 目标对象
	 * @param {String} v 要获取的属性名称，支持对象.导航
	 * @return {Mixed} 得到的属性值
	 * 
	 */
	getObjVal : function(o, v) {
		try {
			if(!o) return null;
			if(!v) return o;
			var ps = v.split('.'), val = o;
			for (var i = 0; i < ps.length; i++) {
				if (!val)
					return null;
				val = val[ps[i]];
			}
			return val;
		} catch (e) {
			return null
		}
	},
	
	
	/**
	 * 获取一个对象拥有属性的个数
	 * <pre>
	 * <code>
	 * //例子 : 
	 * var o = {name:'sss',age:29}
	 * alert(Ext.objPcount(o));//2
	 * </pre>
	 * </code>
	 * 
	 * @param {Object} o 待查找的对象
	 * @return {Number} 拥有属性的个数
	 * 
	 */
	objPcount : function(o) {
		var count = 0;
		if (typeof o == 'object') {
			for (var p in o) {
				if (o.hasOwnProperty(p)) {
					count++;
				}
			}
		}
		return count;
	},
	urlAppend : function(url, s){
		if(!Ext.isEmpty(s)){
			if(Ext.isObject(s))s = Ext.urlEncode(s);
            return url + (url.indexOf('?') === -1 ? '?' : '&') + s;
		}
        return url;
	},
	/**
	 * 格式化金额
	 * 
	 * @param {Number} value 要格式的数字
	 * @param {Number} num 保留几位小数
	 * @return {Number} 
	 */
	formatMoney : function(v, num) {
		if (typeof v !== 'number') {
			var v = parseFloat(v);
			if (isNaN(v))
				return 0;
		}
		num = num || 0
		return parseFloat(Math.round(v * Math.pow(10, num))
				/ Math.pow(10, num).toFixed(num));
	},
	arr2Map : function(key){
		var arr = Ext.combine.apply(Ext,Array.prototype.slice.call(arguments,1));
		var obj = {};
		if(arr.length){
			Ext.each(arr,function(o){
				if(Ext.isDefined(o[key])){
					obj[o[key]] = o ;
				}
			});	
		}
		return obj;
	},
	arr2obj : function(arr,valPrefix,fields,filterFn){
		var values = {};
		var getV = function(v){
            if(v&&v.value!==undefined)v=v.value;// 根据value属性来得到
            else if(v&& v.id!==undefined)v=v.id;// 根据id属性来得到
            if(v && typeof v=="object" && v.clearTime)v=v.format("Y-m-d");
            return v;       
        }
        
        if(Ext.isArray(valPrefix)){
        	valPrefix = '';
        	if(Ext.isFunction(fields)){
	        	filterFn = fields;
	        	fields = null;
	        }
	        fields = valPrefix;
        }
		valPrefix = valPrefix || '';
		if(Ext.isArray(arr)){
			Ext.each(arr,function(data,i){
				if(data instanceof Ext.data.Record){
					data = data.data;
				}
				if(fields && fields.length){
					if(!Ext.isFunction(filterFn) || filterFn(data)!==false){
						for (var j = 0; j < fields.length; j++) {
							var p = fields[j];
							var k = valPrefix+p;
							values[k] = values[k] || [];
							values[k][i]=getV(data[p]);
						}
					}
				}else{
					if(!Ext.isFunction(filterFn) || filterFn(data)!==false){
						for(var p in data){
								var k = valPrefix+p;
								values[k] = values[k] || [];
								values[k][i]=getV(data[p]);
							}
						}
					}
			});
		}
		return values;
	}
});

/**
 * @class Array
 */
Ext.applyIf(Array.prototype, {
	/**
	 * 拷贝数组
	 * @return {Array}
	 */
	copy : function(){
		return this.slice();
	},
	/**
	 * 判断数组是否为空
	 * 
	 * @return {Boolean}
	 */
    isEmpty : function() {
        if (this.length && this.length > 0) {
            return false;
        }
        return true;
    },
    
    /**
     * 根据数组索引删除数组中的元素
     * @param {Number} index
     */
    removeAt : function(index) {
        this.splice(index, 1);
    },
    
    /**
     * 根据数组中对象元素的某字段值删除数组中的元素
     * <pre>
     * <code>
     * //例子：
     * var arr = [{id:1,name:'zhangsan'},{id:2,name:'wangwu'},{id:3,name:'lisi'}];
     * arr.removeKey(3,'id');//现在要删除lishi 
     * </pre>
     * </code>
     * 
     * @param {String/Number} keyValue 删除对应属性的值
     * @param {String/Number} keyName 删除对应属性
     */
    removeKey : function(keyValue,keyName) {
    	keyName = keyName || 'id';
        Ext.each(this, function(o, i) {
            if (o[keyName] == keyValue) {
                this.removeAt(i);
                return;
            }
        }, this);
    },
    
    /**
     * 判断obj在数组中是否存在
     * 
     * @param {Mixed} obj 需要查找的值
     * @return {Boolean}
     */
	contains:function(obj){
	    return (this.indexOf(obj)>=0);
	},
	
	/**
	 * 过滤掉数组中重复的对象，返回过滤后的新数组
	 * <pre>
     * <code>
     * //例子：
     * var arr = ['abc','abc','123','234','',''];
	 * var newarr = arr.distinct(true);
	 * console.debug(newarr);//out:["abc", "123", "234"]
     * </pre>
     * </code>
     * 
	 * @param {Boolean} valid 是否过滤掉数组中的空对象
	 * @return {Array} 返回一个全新的数组
	 */
	distinct : function(valid) {
	    var ArrayObj = {};
	    var returnArray = [];
	    for (var i = 0; i < this.length; i++) {
	        if (ArrayObj[this[i]] || (valid && Ext.isEmpty(this[i])))
	            continue;
	        ArrayObj[this[i]] = this[i];
	        returnArray.push(this[i])
	    }
	    return returnArray
	}
});

/**
 * @class Ext.form.VTypes
 * Overridable validation definitions. The validations provided are basic and intended to be easily customizable and extended.
 * @singleton
 */
Ext.apply(Ext.form.VTypes, {
	/**
	 * 日期验证器 用于验证起始日期必须早于结束日期
	 * 
	 * <pre>
	 * <code>
	 * //例子:
items:[
	{xtype:"datefield",id:"startDate",fieldLabel:"开始时间",name:"start",vtype:"daterange",endDateField:"endDate"},
	{xtype:"datefield",id:"endDate",fieldLabel:"结束时间",name:"end",vtype:"daterange",startDateField:"startDate"},
]
    	</code>
    	</pre>
	 * @param {Date} val 日期
	 * @param {Date} field Ext.form.DateField
	 */
    daterange : function(val, field) {
        var date = field.parseDate(val);
        if (!date) {
            return;
        }
        if (field.startDateField
                && (!this.dateRangeMax || (date.getTime() != this.dateRangeMax
                        .getTime()))) {
            var start = Ext.getCmp(field.startDateField);
            start.setMaxValue(date);
            start.validate();
            this.dateRangeMax = date;
        } else if (field.endDateField
                && (!this.dateRangeMin || (date.getTime() != this.dateRangeMin
                        .getTime()))) {
            var end = Ext.getCmp(field.endDateField);
            end.setMinValue(date);
            end.validate();
            this.dateRangeMin = date;
        }
        return true;
    }
});


Ext.override(Ext.form.CheckboxGroup,{
    onRender : function(ct, position) {
        if (!this.el) {
            var panelCfg = {
                cls : this.groupCls,
                layout : 'column',
                border : false,
                renderTo : ct
            };
            var colCfg = {
                defaultType : this.defaultType,
                layout : 'form',
                border : false,
                defaults : {
                    hideLabel : true,
                    name:this.name,
                    anchor : '100%'
                }
            }
            if (this.items[0].items) {
                // The container has standard ColumnLayout configs, so pass them
                // in directly
                Ext.apply(panelCfg, {
                            layoutConfig : {
                                columns : this.items.length
                            },
                            defaults : this.defaults,
                            items : this.items
                        })
                for (var i = 0, len = this.items.length; i < len; i++) {
                    Ext.applyIf(this.items[i], colCfg);
                };

            } else {
                var numCols, cols = [];

                if (typeof this.columns == 'string') { // 'auto' so create a
                                                        // col per item
                    this.columns = this.items.length;
                }
                if (!Ext.isArray(this.columns)) {
                    var cs = [];
                    for (var i = 0; i < this.columns; i++) {
                        cs.push((100 / this.columns) * .01); // distribute by
                                                                // even %
                    }
                    this.columns = cs;
                }

                numCols = this.columns.length;

                // Generate the column configs with the correct width setting
                for (var i = 0; i < numCols; i++) {
                    var cc = Ext.apply({
                                items : []
                            }, colCfg);
                    cc[this.columns[i] <= 1 ? 'columnWidth' : 'width'] = this.columns[i];
                    if (this.defaults) {
                        cc.defaults = Ext.apply(cc.defaults || {},
                                this.defaults)
                    }
                    cols.push(cc);
                };

                // Distribute the original items into the columns
                if (this.vertical) {
                    var rows = Math.ceil(this.items.length / numCols), ri = 0;
                    for (var i = 0, len = this.items.length; i < len; i++) {
                        if (i > 0 && i % rows == 0) {
                            ri++;
                        }
                        if (this.items[i].fieldLabel) {
                            this.items[i].hideLabel = false;
                        }
                        cols[ri].items.push(this.items[i]);
                    };
                } else {
                    for (var i = 0, len = this.items.length; i < len; i++) {
                        var ci = i % numCols;
                        if (this.items[i].fieldLabel) {
                            this.items[i].hideLabel = false;
                        }
                        cols[ci].items.push(this.items[i]);
                    };
                }

                Ext.apply(panelCfg, {
                            layoutConfig : {
                                columns : numCols
                            },
                            items : cols
                        });
            }

            this.panel = new Ext.Panel(panelCfg);
            this.el = this.panel.getEl();

            if (this.forId && this.itemCls) {
                var l = this.el.up(this.itemCls).child('label', true);
                if (l) {
                    l.setAttribute('htmlFor', this.forId);
                }
            }

            var fields = this.panel.findBy(function(c) {
                        return c.isFormField;
                    }, this);

            this.items = new Ext.util.MixedCollection(false,function(o){
                return o.inputValue || o.value;
            });
            this.items.addAll(fields);
        }
        Ext.form.CheckboxGroup.superclass.onRender.call(this, ct, position);
    },
    getValue:function(){
        var val = [];
        this.items.filter('checked',true).each(function(item){
            var v = Ext.value(item.inputValue,item.value);
            if(!Ext.isEmpty(v))val.push(v);
        },this);
        return val;
    },
    getName:function(){
        return this.name || this.id;
    },
    setValue:function(v){
        //为了方便比较统一用字符串来比较
        if(Ext.isArray(v)){
            v = v.toString().split(',');
            
        }else if(v){
            v = v.split(/[,;\s]/);
        }
        var arr = [].concat(v||[]);
        this.items.eachKey(function(key,item,i,len){
            var checked = arr.indexOf(String(key))>=0;
            item.setValue(checked);
        },this);
    },
    initValue:function(){
        if (this.value !== undefined && this.value!==null) {
            this.setValue(this.value);
        }
    }
});

Ext.override(Ext.form.RadioGroup,{
    setValue:function(v){
        this.items.each(function(item){
            if(item.inputValue==v)item.setValue(true);
        },this);
    }
});
/**
 * @class Ext.form.Field
 * 
 * 对Field及BasicForm进行了简单扩展,支持clearDirty
 */
Ext.apply(Ext.form.Field.prototype,{// msgTarget:"side",
    clearDirty:function(){
        this.originalValue=this.getValue();
    },
    clearData:function(){// 把字段设置为最原始的值,通过initialConfig的value属性得到
        var v=this.initialConfig.value;
        if(v === this.emptyText || v === undefined){
       		 v = '';
        }
        this.originalValue=v;
    },
    setOriginalValue:function(v){// 设置字段原始值
        this.setValue(v);
        this.clearDirty();
    },
    getCursorPosition:function(){
        var d = this.el.dom;
        if(d.setSelectionRange){
            return d.selectionStart||0;
        }else if(d.createTextRange){
             var   currentRange=document.selection.createRange();  
             var   workRange=currentRange.duplicate();  
                d.select();  
                var   allRange=document.selection.createRange();  
                var   len=0;
                while(workRange.compareEndPoints("StartToStart",allRange)>0){  
                  workRange.moveStart("character",-1);  
                  len++;  
                }
                currentRange.select();  
                return len;
        }
    },
    canFocus:function(){
        return !(/hidden|labelfield|checkbox|radio/.test(this.getXType())||this. disabled||this.hidden|| (this.el.dom.readOnly=== true));
    }
});
Ext.override(Ext.form.DateField,{
	clearData : function(){// 把字段设置为最原始的值,通过initialConfig的value属性得到
        var v=this.initialConfig.value;
        if(v === this.emptyText || v === undefined){
       		 v = '';
        }
        this.originalValue = this.parseDate(this.formatDate(this.parseDate(v)));
    }
});

/**
 * @class Ext.Container
 */
Ext.apply(Ext.Container.prototype,{
	/**
	 * 用来查询panel内组件id为指定id 或id/name/dataIndex为指定名称的的表单组件。
	 * 常用于查找form表单内的组件,替代formPanel.getForm().findField()方法。
	 * <pre>
	 * <code>
	 * //示例
	 * var formPanel = Ext.getCmp('LoginPanel');
	 * var name = formPanel.findSomeThing('name').getValue();
	 * </pre>
	 * </code>
	 * 
	 * @param {String} id 要查找的组件的id或者name名称
	 * @return {Component} 符合条件的第一个组件
	 */
	findSomeThing:function(id) {
	    var m, ct = this;
	    this.cascade(function(c) {
	        if (ct != c&& c.id === id || (c.isFormField && (c.dataIndex == id || c.id == id || c.getName() == id))) {
	            m = c;
	            return false;
	        }
	    });
	    return m || null;
	},
	/**
	 * getComponent 的简写
	 * @param {String/Number} id/index
	 */
	getItem : function(id){
		return this.getComponent(id);
	}
});

/**
 * @class Ext.DatePicker
 */
Ext.apply(Ext.DatePicker.prototype,{
	
	/**
	 * 修改日期选择控件，在选择了一个日期后，把当前的时间追加到选中的日期后。
	 * <pre>
	 * <code>
	 * //示例，在DatePicker上面选择了2010-8-10
{xtype:"datefield",fieldLabel:"开始时间",name:"start",listeners:{
	select:function(t,d){
		console.log(d.format('Y-m-d H:i:s')); //2010-8-10 10:34:29 而不是 2010-8-10 00:00:00
	}
}}
	 * </pre>
	 * </code>
	 * 
	 * @param {Ext.EventObject} e
	 * @param {Ext.DatePicker} t 
	 */
	handleDateClick: function(e, t) {
		e.stopEvent();
		if (t.dateValue && !Ext.fly(t.parentNode).hasClass("x-date-disabled")) {
		    var now = new Date();
		    var d = new Date(t.dateValue);
		    d.setHours(now.getHours());
		    d.setMinutes(now.getMinutes());
		    d.setSeconds(now.getSeconds());
		    this.setValue(d, true);
		    this.fireEvent("select", this, this.value);
		}
	},
	
	/**
	 * 修改日期选择控件，在选择今天后，把当前的时间追加到选中的今天的日期后。
	 * <pre>
	 * <code>
	 * //示例，在DatePicker上面选择了【今天】
{xtype:"datefield",fieldLabel:"业务时间",name:"billDate",listeners:{
	select:function(t,d){
		console.log(d.format('Y-m-d H:i:s')); //2010-8-10 10:34:29 而不是 2010-8-10 00:00:00
	}
}}
	 * </pre>
	 * </code>
	 * 
	 */
	selectToday:function() {
	    if (this.todayBtn && !this.todayBtn.disabled) {
	        this.setValue(new Date(), true);
	        this.fireEvent("select", this, this.value);
	    }
	},
	
	/**
	 * 修改日期选择控件，在setValue方法中，加入是否保持时间的选项
	 * 
	 * @param {Date} value 设置的日期值
	 * @param {Boolean} keepTime  是否在日期选择器上保留设置的时间
	 */
	setValue:function(value, keepTime) {
	    var old = this.value;
	    this.value = keepTime ? value : value.clearTime(true);
	    if (this.el) {
	        this.update(this.value);
	    }
	}
});

/**
 * @class Ext.form.ComboBox
 */
Ext.override(Ext.form.ComboBox.prototype,{
	/**
	 * 对ComboBox的onTriggerClick进行增强，支持父容器disabled值判断功能
	 */
	onTriggerClick: function() {
	    var disabled = false;
	    if (this.ownerCt)
	        this.ownerCt.bubble(function(c) {
	            if (c.disabled)
	                disabled = true;
	        });
	    if (this.disabled || disabled) {
	        return;
	    }
	    if (this.isExpanded()) {
	        this.collapse();
	        this.el.focus();
	    } else {
	        this.onFocus({});
	        if (this.triggerAction == 'all') {
	            this.doQuery(this.allQuery, true);
	        } else {
	            this.doQuery(this.getRawValue());
	        }
	        this.el.focus();
	    }
	},
	processValue:function(value) {
        if(this.comboBlank===false){
            return this.getValue();
        }else{
            return Ext.form.ComboBox.superclass.processValue.call(this,value);
        }           
    } 
});

Ext.override(Ext.form.ComboBox,{
	trigger2Class:'search',
	hideTrigger2:true,
    onRender : function(ct, position){
        if(this.hiddenName && !Ext.isDefined(this.submitValue)){
            this.submitValue = false;
        }
        this.triggerConfig = {
            tag:'span', cls:'x-form-twin-triggers', cn:[
            {tag: "img", src: Ext.BLANK_IMAGE_URL, cls: "x-form-trigger " + Ext.value(this.trigger1Class,'')},
            {tag: "img", src: Ext.BLANK_IMAGE_URL, cls: "x-form-trigger " + Ext.value(this.trigger2Class,'')}
        ]};
        Ext.form.ComboBox.superclass.onRender.call(this, ct, position);
        if(this.hiddenName){
            this.hiddenField = this.el.insertSibling({tag:'input', type:'hidden', name: this.hiddenName,
                    id: (this.hiddenId || Ext.id())}, 'before', true);

        }
        if(Ext.isGecko){
            this.el.dom.setAttribute('autocomplete', 'off');
        }

        if(!this.lazyInit){
            this.initList();
        }else{
            this.on('focus', this.initList, this, {single: true});
        }
    },
    onTrigger1Click:function(){
    	Ext.form.ComboBox.prototype.onTriggerClick.apply(this,arguments);
    },
    onTrigger2Click:function(){},
    getTriggerWidth: function(){
        var tw = 0;
        Ext.each(this.triggers, function(t, index){
            var triggerIndex = 'Trigger' + (index + 1),
                w = t.getWidth();
            if(w === 0 && !this['hidden' + triggerIndex]){
                tw += this.defaultTriggerWidth;
            }else{
                tw += w;
            }
        }, this);
        return tw;
    },
    onDestroy : function(){
        if (this.dqTask){
            this.dqTask.cancel();
            this.dqTask = null;
        }
        this.bindStore(null);
        Ext.destroy(
        	this.triggers,
            this.resizer,
            this.view,
            this.pageTb,
            this.list
        );
        Ext.destroyMembers(this, 'hiddenField');
        Ext.form.ComboBox.superclass.onDestroy.call(this);
    },
    initTrigger : function(){
        var ts = this.trigger.select('.x-form-trigger', true);
        var triggerField = this;
        ts.each(function(t, all, index){
            var triggerIndex = 'Trigger'+(index+1);
            t.hide = function(){
                var w = triggerField.wrap.getWidth();
                this.dom.style.display = 'none';
                triggerField.el.setWidth(w-triggerField.trigger.getWidth());
                this['hidden' + triggerIndex] = true;
            };
            t.show = function(){
                var w = triggerField.wrap.getWidth();
                this.dom.style.display = '';
                triggerField.el.setWidth(w-triggerField.trigger.getWidth());
                this['hidden' + triggerIndex] = false;
            };

            if(this['hide'+triggerIndex]){
                t.dom.style.display = 'none';
                this['hidden' + triggerIndex] = true;
            }
            this.mon(t, 'click', this['on'+triggerIndex+'Click'], this, {preventDefault:true});
            t.addClassOnOver('x-form-trigger-over');
            t.addClassOnClick('x-form-trigger-click');
        }, this);
        this.triggers = ts.elements;
    }
});
/**
 * @class Ext.form.ComboBox
 */
Ext.apply(Ext.form.ComboBox.prototype,{
    PleaseSelectedValue:"--PleaseSelectedValue--",
    PleaseSelectedText:"--请选择--",
    disableChoice:false,
    reload:function(){      
        this.store.reload();
    },
    bindStore:function(store, initial){
        if(this.store && !initial){
            this.store.un('beforeload', this.onBeforeLoad, this);
            this.store.un('load', this.onLoad, this);
            this.store.un('loadexception', this.collapse, this);
            if(!store){
                this.store = null;
                if(this.view){
                    this.view.setStore(null);
                }
            }
        }
        if(store){
	            this.store = Ext.StoreMgr.lookup(store);
	            if(this.view){
	                this.view.setStore(store);
	            }
	           if(this.store.data.getCount()>0 && (this.allowBlank||this.nullText) && !this.disableChoice){
	                var o={};               
	                o[this.valueField]=this.PleaseSelectedValue;
	                var nullText=this.nullText?this.nullText:this.PleaseSelectedText;
	                this.nullText=nullText;
	                o[this.displayField]=nullText;
	                this.store.insert(0,new Ext.data.Record(o));            
	           }
	           if(!this.lastOptions){
	                this.store.on('beforeload', this.onBeforeLoad, this);
	                this.store.on('loadexception', this.collapse, this);
	                if((this.allowBlank||this.nullText)&& !this.disableChoice){
	                this.store.on("load",function(A,B){
	                            var o={};
	                            o[this.valueField]=this.PleaseSelectedValue;
	                            o[this.displayField]=this.nullText?this.nullText:this.PleaseSelectedText;
	                            this.nullText=nullText;
	                            if(this.store&&this.store.insert&&this.store.find(this.valueField,o[this.valueField])<0)
	                            this.store.insert(0,new Ext.data.Record(o));
	                        },this);
	                }       
	                this.store.on('load', this.onLoad, this);
	           }
	   	}
	        
	},
	setValue : function(v){
        var text = v;
        if(this.valueField){
            var r = this.findRecord(this.valueField, v);
            if(r){
                text = r.data[this.displayField];
            }else if(this.valueNotFoundText !== undefined){
                text = this.valueNotFoundText;
            }
        }
        this.lastSelectionText = text;
        if(this.hiddenField){
            this.hiddenField.value =(v==this.PleaseSelectedValue?"":v);
        }
        Ext.form.ComboBox.superclass.setValue.call(this, text);
        this.value = v;
    },
	getValue : function(){
        if(this.value==this.PleaseSelectedValue||this.value==this.nullText){return "";}
        else if(this.valueField){
            return typeof this.value != 'undefined' ? this.value : '';
        }else{
            return Ext.form.ComboBox.superclass.getValue.call(this);
        }
    }  
});

/**
 * @class Ext.Toolbar
 */
Ext.apply(Ext.Toolbar.prototype,{
	/**
	 * 获得Toolbar中item为form元素的值,该form元素必须有itemId/name/id
	 * 
	 * <pre>
	 * <code>
	 * 
{
	xtype:"form",
	frame:true,
	width:400,
	height:300,
	region: 'center',
	id:"viewportform",
	tbar:['field1:',{xtype:'textfield',name:'field1'},'field2:',{xtype:'textfield',name:'field2'}],
	items:[
		{xtype:'button',text:'get toolbar values',scope:this,handler:function(){
			var o=Ext.getCmp('viewportform').getTopToolbar().getValues();
			console.debug(o);//在field1中填写123，在field2中添加345，得到的值：{{field1:'123'},{field2:'345'}}
		}}
	]
}
	 * </pre>
	 * </code>
	 * @return {Object} 选中的值
	 */
    getValues:function(){
        var c = this;
        var values={};
        if(!c.items || !c.items.getCount())return values;
        c.items.each(function(item){
            if(item.isFormField){
                var name = (item.itemId||item.name||item.id);
                var v = item.getValue();
                if(v instanceof Date) v = v.format('Y-m-d');
                values[name] = v;
            }
        },this);
        return values;
    },
    
    /**
     * insertButton方法的简写
     * 
     * @param {Number} index 要在工具栏中插入组件的序号
     * @param {Object/Ext.Toolbar.Item/Ext.Button/Array} item 工具栏按钮 或按钮的配置对象，或按钮/配置对象的数组
     * 
     * @return {Ext.Button/Item}
     */
    insert:Ext.Toolbar.prototype.insertButton
});

/**
 * @class Ext.Window
 * 
 * <p>
 * 增强Ext.Window的快捷键支持，包括：
 * <li>Alt+X：关闭窗口</li>
 * <li>Tab：在Window内的组件中依次获得焦点(包括Window中的按钮)</li>
 * </p>
 */
Ext.apply(Ext.Window.prototype,{
    onRender : function(ct, position){
        Ext.Window.superclass.onRender.call(this, ct, position);
        if(this.plain){
            this.el.addClass('x-window-plain');
        }
        // this element allows the Window to be focused for keyboard events
        this.focusEl = this.el.createChild({
                    tag: "a", href:"#", cls:"x-dlg-focus",
                    tabIndex:"-1", html: "&#160;"});
        this.focusEl.swallowEvent('click', true);

        this.proxy = this.el.createProxy("x-window-proxy");
        this.proxy.enableDisplayMode('block');

        if(this.modal){
            this.mask = this.container.createChild({cls:"ext-el-mask"}, this.el.dom);
            this.mask.enableDisplayMode("block");
            this.mask.hide();
        }
        this.getKeyMap().addBinding([
	        {
                key:"x",
                alt:true,
                fn: function(){this.hide();},
                scope: this
	        }
	        /*,{
	            key:Ext.EventObject.TAB,
	            fn:function(k,e){
	                if(this.buttons&&this.buttons.length){
	                    if(this.buttons[this.buttons.length-1].el.hasClass("x-btn-focus")){
		                    e.stopEvent();
		                    var firstField=[];
		                    // 此处有问题,影响效率
		                    this.cascade(function(c){
		                        if(c.isFormField && c.canFocus()){
		                            firstField.push(c);
		                            return false;
		                        }
		                    });
		                    if(firstField.length){
		                        firstField[0].focus();
		                    }
	                    }
	                }
	            },
	            scope:this
	        }*/
        ]);
    }
});

/**
 * @class Ext.form.BasicForm
 * 
 * <p>
 * 增加了在form表单中使用回车键在表单组件中按照指定的导航顺序导航的功能。
 * 并且可以使用方向键[UP][LEFT]反向导航。
 * </p>
 * 
 * <pre>
 * <code>
 * //示例:
{
	xtype:"form",
	frame:true,
	width:400,
	height:300,
	region: 'center',
	//自定义导航顺序
	navigationSequences:['name','email','password','bornDate'],
	//开启回车导航
	enterNavigationKey:true,
	items:[
		{xtype:'textfield',name:'name'},
		{xtype:'textfield',name:'password',inputType:'password'},
		{xtype:'datefield',name:'bornDate'},
		{xtype:'textfield',vtype:'email',name:'email'},
		{xtype:'textfield',name:'other'},
	]
}
 * </pre>
 * </code>
 * 
 * <p>
 * 回车导航在企业级应用中的表单中是很常用的功能。
 * </p>
 */
Ext.apply(Ext.form.BasicForm.prototype,{
		/**
		 * 定义的顺序来导航
		 * @cfg {Array} navigationSequences
		 */
	
		/**
		 * 是否开启回车导航，只有开启了该属性，才能正确回车导航
		 * @cfg {Boolean} enterNavigationKey
		 */
	
		/**
		 * 清除所有表单中所有组件的修改状态
		 * 
		 * @return BasicForm
		 */
        clearDirty:function(){
            this.items.each(function(f){
               f.clearDirty();
            });
            return this;
        },
        
        /**
         * 把表单中所有字段设置为最原始的值，即通过initialConfig的value属性设置的值
         */
        clearData:function(){
            this.items.each(function(f){
               f.clearData();
            });
        },
        
        //private
        focusFirstButton:function(fp){
            if(fp){
                var buttons=fp.buttons;
                if(!buttons){
                    fp.bubble(function(c){if(c.buttons){buttons=c.buttons;return false;}});
                }
                for(var i=0;buttons&&i<buttons.length;i++){
                if(!buttons[i].disabled){
                    buttons[i].focus();
                    break;
                }}
            }
        },
        
        //private
        focusPreviousField:function(f,e){
            var n=f.name||f.id;
            // 通过navigationSequences定义的顺序来导航
            var sequence=-1,key=null;
            if(this.navigationSequences && ((sequence=this.navigationSequences.indexOf(n))>0)){
                key=this.navigationSequences[sequence-1];
            }
            else {
                 this.items.each(function(field){// 查找当前field所在的位置
                      sequence++;
                      if(f.id==field.id || f.name==field.name){
                        return false;
                      }
                    });
                while(sequence>0){
                        if(!this.items.get(sequence-1).canFocus()){
                        sequence--;
                        }
                        else {
                            break;
                        }
                }   
                if(sequence>0){
                    key=this.items.get(sequence-1).id;
                }               
            }
            if(key){// 如果找到下一个key
                var field=this.findField(key);
                    if(!field){// 通过表单来找
                        var fp=f.findParentByType("form");
                        if(fp)field=fp.findSomeThing(key);
                    }
                    if(field){
                            if(e)e.stopEvent();
                            field.focus();
                        }
            }
        },
        
        //private
        focusNextField:function(f,e){
            var n=f.name||f.id;
            // 通过navigationSequences定义的顺序来导航
            var sequence=-1,key=null;
            if(this.navigationSequences && ((sequence=this.navigationSequences.indexOf(n))>=0)){
                if(sequence>=this.navigationSequences.length-1){
                    var fp=f.findParentByType("form");
                    this.focusFirstButton(fp);// 导航到按钮
                }
                else {
                    key=this.navigationSequences[sequence+1];
                }
            }
            else {
                 this.items.each(function(field){// 查找当前field所在的位置
                      sequence++;
                      if(f.id==field.id || f.name==field.name){
                        return false;
                      }
                    });
                while(sequence<this.items.getCount()-1){
                        if(!this.items.get(sequence+1).canFocus()){
                        sequence++;
                        }
                        else {
                            break;
                        }
                }
                if(sequence>=this.items.getCount()-1){
                    var fp=f.findParentByType("form");
                    this.focusFirstButton(fp);// 导航到按钮
                }
                else {
                    key=this.items.get(sequence+1).id;
                }
            }
            if(key){// 如果找到下一个key
                var field=this.findField(key);
                    if(!field){// 通过表单来找
                        var fp=f.findParentByType("form");
                        if(fp)field=fp.findSomeThing(key);
                    }
                    if(field){
                        try{
                            field.focus();
                        }
                        catch(e){
                            if(e)e.keyCode=9;
                        }
                }
            }
        },
        //private
        handleNavigation:function(f,e){         
            var k = e.getKey();
            if(k==e.ENTER && this.enterNavigationKey){
                if(f.getValue()||f.allowBlank!==false){
                    this.focusNextField(f,e);
                    // alert("go next");
                }
                // else return false;
            }
            else if(k==e.UP||(k==e.LEFT && f.getCursorPosition()===0)){
                this.focusPreviousField(f,e);
            }           
        },
        add : function(){
            for(var i=0;i<arguments.length;i++){    
                var f=arguments[i];
                f.on("specialkey",this.handleNavigation,this);
            }
            this.items.addAll(Array.prototype.slice.call(arguments, 0));
            return this;
        }
    });
    
    
/**
 * @class  Ext.form.FormPanel
 * 对Ext.form.FormPanel增强
 */
Ext.apply(Ext.form.FormPanel.prototype,{
	/**
	 * 通过id或者name获取表单元素的快捷方式,等同form.getForm().findField(id);
	 * 
	 * @param {String/Object/...} id
	 * @return field
	 */
    findField:function(id){
        return this.getForm().findField(id);
    }
});

/**
 * @class Ext.grid.RowSelectionModel
 * 增强Ext.grid.RowSelectionModel
 */
Ext.override(Ext.grid.RowSelectionModel,{
	 /**
	  * 如果已选择到了最后一行，则自动跳到第一行
	  * 
	  * @param {Boolean} keepExisting
	  * 
	  * @return {Boolean}
	  */
     selectNext : function(keepExisting) {
        if (this.hasNext()) {
            this.selectRow(this.last + 1, keepExisting);
            this.grid.getView().focusRow(this.last);
            return true;
        }else{
            this.selectFirstRow(keepExisting);
            this.grid.getView().focusRow(0);
            return false;
        }
    },
    
    /**
	  * 如果已选择到了一行，则自动跳到最后一行
	  * @param {Boolean} keepExisting
	  * @return {Boolean}
	  */
    selectPrevious : function(keepExisting) {
        if (this.hasPrevious()) {
            this.selectRow(this.last - 1, keepExisting);
            this.grid.getView().focusRow(this.last);
            return true;
        }else{
            this.selectLastRow(keepExisting);
            this.grid.getView().focusRow(this.grid.store.getCount()-1);
            return false;
        }
    }
});

/**
 * @class Ext.grid.CellSelectionModel
 * 对Ext.grid.CellSelectionModel的增强，增加了在EditorGridPanel中回车按照指定顺序导航的功能。
 * <pre>
 * <code>
 * //示例:
 * </pre>
 * </code>
 */
Ext.override(Ext.grid.CellSelectionModel,{
    tryEdit:function(row,col,required,step){
        this.tryDoEdit(null,row,col,step||1,this.acceptsNav,this,required!==false);
    },
    tryDoEdit:function(e,row,col,step,fn,scope,required){
        var g = this.grid;
        var newCol=step>0?col+1:col-1;      
        var cell=g.walkCells(row, newCol,step, fn, scope);
        if(cell){       
             g.startEditing(cell[0], cell[1],required);
             if(!g.editing){
                this.tryDoEdit(e,cell[0],cell[1],step,fn,scope,required);
             }
             else if(e) {
                e.stopEvent();
             }
        }else {
            g.focus();
        }
    },
    onEditorKey : function(field, e){
        var k = e.getKey(), newCell, g = this.grid,s=this.selection,sm=g.getColumnModel();
        var cell={
        	row:s.cell[0],
        	col:s.cell[1]
        };
        
        var ed = sm.getCellEditor(cell.col,cell.row);
        
        if(k == e.TAB ||(this.enterNavigation&&k==e.ENTER && (!ed.field.disableEnterNavigation||e.shiftKey))){           
            // ed.completeEdit();
            if(e.shiftKey){
                this.tryDoEdit(e,cell.row,cell.col,-1,this.acceptsNav, this,k==e.ENTER);
                // newCell = g.walkCells(ed.row, ed.col-1, -1, this.acceptsNav,
                // this);
            }else{
                this.tryDoEdit(e,cell.row,cell.col,1,this.acceptsNav, this,k==e.ENTER);
                // newCell = g.walkCells(ed.row, ed.col+1, 1, this.acceptsNav,
                // this);
            }
            // e.stopEvent();
        }
        else if(k == e.ENTER){
            e.stopEvent();
        }else if(k == e.ESC){
        	g.getView().focusCell(cell.row, cell.col);
            e.stopEvent();
        }
        else if(k==e.LEFT &&ed.field.getCursorPosition()==0){
            this.tryDoEdit(e,cell.row,cell.col,-1,this.acceptsNav, this,false);
        }
    }
});


Ext.override(Ext.grid.EditorGridPanel,{
	startEditing : function(row, col){
        this.stopEditing();
        if(this.colModel.isCellEditable(col, row)){
            this.view.ensureVisible(row, col, true);
            var r = this.store.getAt(row),
                field = this.colModel.getDataIndex(col),
                e = {
                    grid: this,
                    record: r,
                    field: field,
                    value: r.data[field],
                    row: row,
                    column: col,
                    cancel:false
                };
            if(this.fireEvent("beforeedit", e) !== false && !e.cancel){
                this.editing = true;
                var ed = this.colModel.getCellEditor(col, row);
                if(!ed){
                    return;
                }
                if(!ed.rendered){
                    ed.parentEl = this.view.getEditorParent(ed);
                    ed.on({
                        scope: this,
                        render: {
                            fn: function(c){
                                c.field.focus(false, true);
                            },
                            single: true,
                            scope: this
                        },
                        specialkey: function(field, e){
                            this.getSelectionModel().onEditorKey(field, e);
                        },
                        complete: this.onEditComplete,
                        canceledit: this.stopEditing.createDelegate(this, [true])
                    });
                }
                Ext.apply(ed, {
                    row     : row,
                    col     : col,
                    record  : r
                });
                this.lastEdit = {
                    row: row,
                    col: col
                };
                this.activeEditor = ed;
                // Set the selectSameEditor flag if we are reusing the same editor again and
                // need to prevent the editor from firing onBlur on itself.
                ed.selectSameEditor = (this.activeEditor == this.lastActiveEditor);
                var v = this.preEditValue(r, field);
                ed.startEdit(this.view.getCell(row, col).firstChild, Ext.isDefined(v) ? v : '');

                // Clear the selectSameEditor flag
                (function(){
                	if(ed.field.onTriggerClick){
	                    (function(){
	                        ed.field.onTriggerClick();
	                        if(ed.field.list&&ed.field.list.alignTo)
	                        ed.field.list.alignTo(this.view.getCell(ed.row,ed.col), 'tl-bl?');
	                    }).defer(100,this);
                    }else if(ed.field.isFormField){
                        ed.field.el.dom.select();
                    }
                    delete ed.selectSameEditor;
                }).defer(50,this);
            }
        }
    }
});

/**
 * @class Ext.grid.GridPanel
 * 
 * ExtJs3.0中Ext.grid.GridPanel没有getSelections,但是比较常用
 */
Ext.apply(Ext.grid.GridPanel.prototype,{
    getSelections:function(){
        return this.getSelectionModel().getSelections();
    }
});
/**
 * @class Ext.grid.EditorGridPanel
 * 
 * 增强Ext.grid.EditorGridPanel,让可以编辑表格可以获取修改后的数据
 */
Ext.override(Ext.grid.EditorGridPanel,{
	/**
	 * editor数据字段的前缀
	 * @cfg {String} valPrefix 
	 */
	valPrefix : 'item_',
	
	getFilterDataFn:null,
	
	/**
	 * 获取被修改后的Records数组
	 * return {Record[] recoreds} 
	 */
	getUpdateRecords:function(){
		return this.getStore().getModifiedRecords();
	},
	/**
	 * 获取editorGrid被修改过的数据
	 * return {Array}
	 */
	getUpdateRowsValues:function(){
		return Ext.arr2obj(this.getUpdateRecords(),this.valPrefix,this.getStore().fields.keys,this.getFilterDataFn);
	},
	/**
	 * 获取grid每行的数据
	 * return {Array}
	 */
	getRowsValues:function(){
		return Ext.arr2obj(this.getStore().getRange(),this.valPrefix,this.getStore().fields.keys,this.getFilterDataFn);
	},
	/**
	 * 修复如果 value 为 undefined编辑时出现 "&nbsp;"问题 
	 * @param {Ext.data.Record} r
	 * @param {Ext.form.Field} field
	 * @return {Mixed}
	 * 
	 */
	preEditValue : function(r, field){
        var value = r.data[field];
        if(!Ext.isDefined(value))value = '';
        return this.autoEncode && Ext.isString(value) ? Ext.util.Format.htmlDecode(value) : value;
    }
});



/**
 * @class Ext.grid.CheckboxSelectionModel
 * 
 * 增强Ext.grid.CheckboxSelectionModel,让选择模型智能选中或取消全选按钮的状态
 */
Ext.override(Ext.grid.CheckboxSelectionModel,{
    clearCheckerAll : function() {
        var a = this.grid.view;
        var b = Ext.fly(a.innerHd).child(".x-grid3-hd-checker");
        if (!b) {
            return
        }
        if (this.getCount() != 0 && this.getCount() == a.getRows().length) {
            b.addClass("x-grid3-hd-checker-on")
        } else {
            b.removeClass("x-grid3-hd-checker-on")
        }
    },/*,
    handleMouseDown : function(g, rowIndex, e){
        if(e.button !== 0 || this.isLocked()){
            return;
        }
        var view = this.grid.getView();
        if(e.shiftKey && !this.singleSelect && this.last !== false){
            var last = this.last;
            this.selectRange(last, rowIndex, e.ctrlKey);
            this.last = last; // reset the last
            view.focusRow(rowIndex);
        }else{
            var isSelected = this.isSelected(rowIndex);
            if(e.ctrlKey && isSelected){
                this.deselectRow(rowIndex);
            }else if(!isSelected || this.getCount() > 1){
              //  this.selectRow(rowIndex,( e.ctrlKey || e.shiftKey || this.keepExisting));
                view.focusRow(rowIndex);
            }
        }
    },*/
    init:function(grid){
        Ext.grid.CheckboxSelectionModel.superclass.init.call(this,grid);
        this.on("selectionchange", this.clearCheckerAll, this,{delay : 100});
    }
});

Ext.override(Ext.data.Store,{
	load : function(options) {
        options = Ext.apply({}, options);
        options.params = options.params || {};
        this.storeOptions(options);
        
        var pn = this.paramNames;
        if(this.sortInfo && this.remoteSort){
            options.params = Ext.apply({}, options.params);
            options.params[pn.sort] = this.sortInfo.field;
            options.params[pn.dir] = this.sortInfo.direction;
        }
        if((!options.params[pn.limit]) && this.pageSize){
        	options.params[pn.limit] =  this.pageSize;
        }
        try {
            return this.execute('read', null, options); // <-- null represents rs.  No rs for load actions.
        } catch(e) {
            this.handleException(e);
            return false;
        }
    }
}); 

/**
 * @class Ext.data.JsonReader
 * 修正在加载分页数据时,由于result为null引起的分页工具栏不正常工作的Bug
 */
Ext.apply(Ext.data.JsonReader.prototype,{
    readRecords : function(o){      
        /**
         * After any data loads, the raw JSON data is available for further
         * custom processing. If no data is loaded or there is a load exception
         * this property will be undefined.
         * 
         * @type Object
         */
        this.jsonData = o;
        if(o.metaData){
            delete this.ef;
            this.meta = o.metaData;
            this.recordType = Ext.data.Record.create(o.metaData.fields);
            this.onMetaChange(this.meta, this.recordType, o);
        }
        var s = this.meta, Record = this.recordType,
            f = Record.prototype.fields, fi = f.items, fl = f.length;
        if (!this.ef) {
            if(s.totalProperty) {
                this.getTotal = this.getJsonAccessor(s.totalProperty);
            }
            if(s.successProperty) {
                this.getSuccess = this.getJsonAccessor(s.successProperty);
            }
            this.getRoot = s.root ? this.getJsonAccessor(s.root) : function(p){return p;};
            if (s.id) {
                var g = this.getJsonAccessor(s.id);
                this.getId = function(rec) {
                    var r = g(rec);
                    return (r === undefined || r === "") ? null : r;
                };
            } else {
                this.getId = function(){return null;};
            }
            this.ef = [];
            for(var i = 0; i < fl; i++){
                f = fi[i];
                var map = (f.mapping !== undefined && f.mapping !== null) ? f.mapping : f.name;
                this.ef[i] = this.getJsonAccessor(map);
            }
        }
        var root = this.getRoot(o), c =root&&root.length?root.length:0, totalRecords = c, success = true;
        if(s.totalProperty){
            var v = parseInt(this.getTotal(o), 10);
            if(!isNaN(v)){
                totalRecords = v;
            }
        }
        if(s.successProperty){
            var v = this.getSuccess(o);
            if(v === false || v === 'false'){
                success = false;
            }
        }
        var records = [];
        for(var i = 0; i < c; i++){
            var n = root[i];
            var values = {};
            var id = this.getId(n);
            for(var j = 0; j < fl; j++){
                f = fi[j];
                var v = this.ef[j](n);
                values[f.name] = f.convert((v !== undefined) ? v : f.defaultValue, n);
            }
            var record = new Record(values, id);
            record.json = n;
            records[i] = record;
        }
        return {
            success : success,
            records : records,
            totalRecords : totalRecords
        };
    },
    getJsonAccessor: function(){
        var re = /[\[\.]/;
        return function(expr) {
            try {
                return(re.test(expr)) ? new Function("obj", "try{return obj." + expr+";}catch(e){ return ''}"):function(obj){return obj[expr];};
            } catch(e){}
            return Ext.emptyFn;
        };
    }()
});
Ext.override(Ext.tree.TreeLoader,{
	/**
	 * 配置TreeNode的图标样式
	 * @type {String}
	 * @property iconCls
	 */
	createNode : function(attr){
        if(this.baseAttrs){
            Ext.applyIf(attr, this.baseAttrs);
        }
        if(this.iconCls){
        	Ext.applyIf(attr,{iconCls:this.iconCls});	
        }
        
        if(this.applyLoader !== false && !attr.loader){
            attr.loader = this;
        }
        if(Ext.isString(attr.uiProvider)){
           attr.uiProvider = this.uiProviders[attr.uiProvider] || eval(attr.uiProvider);
        }
        if(attr.nodeType){
            return new Ext.tree.TreePanel.nodeTypes[attr.nodeType](attr);
        }else{
            return attr.leaf ?
                        new Ext.tree.TreeNode(attr) :
                        new Ext.tree.AsyncTreeNode(attr);
        }
    }
});

(function(){
	var actions = {
		_exeRegxp:/^(?:hide|show|disable|enable)$/,
	    execItemFn:function(exec,args){
	    	var arr = Array.prototype.slice.call(arguments,1);
			Ext.each(arr,function(key){
				if(Ext.isObject(key) && key[exec]){
					key[exec]();
				}else{
					var item = this.get(key);
					if(item) item[exec]();
				}
			},this);
	    },
	    getFnArguments:function(){
	    	var args = arguments.callee.caller.arguments;
	    	if(Ext.isArray(args[0])){
	    		return args[0];
	    	}else{
	    		return Array.prototype.slice.call(args,0)
	    	}
	    },
	    hides:function(args){
	    	var arr = this.getFnArguments();
	    	this.execItemFn.apply(this,['hide'].concat(arr));
	    	return this;
	    },
	    shows:function(){
	    	var arr = this.getFnArguments();
	    	this.execItemFn.apply(this,['show'].concat(arr));
	    	return this;
	    },
	    disables:function(){
	    	var arr = this.getFnArguments();
	    	this.execItemFn.apply(this,['disable'].concat(arr));
	    	return this;
	    },
	    enables:function(){
	    	var arr = this.getFnArguments();
	    	this.execItemFn.apply(this,['enable'].concat(arr));
	    	return this;
	    }
	};
	/**
	 * 对菜单进行扩展，添加insert方法，可以插入任意工具栏选项
	 */
	Ext.apply(Ext.menu.Menu.prototype,actions);
	/**
	 * 对工具栏进行扩展，添加insert方法，可以插入任意工具栏选项
	 */
	Ext.apply(Ext.Toolbar.prototype,actions);
})();
