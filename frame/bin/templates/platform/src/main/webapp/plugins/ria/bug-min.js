Ext.override(Ext.form.RadioGroup,{isDirty:function(){if(this.originalValue==undefined){return false}if(this.getValue()==null){return false}if(this.getValue()||this.originalValue.inputValue==this.getValue().inputValue){return false}return true}});
