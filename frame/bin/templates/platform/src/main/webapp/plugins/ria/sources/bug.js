/**
 * ext 3.2.1 bug一新修复
 */
/**
 * 修复当表单中存在RadioGroup时，判断当前表单是否被修改时判断有误
 */
Ext.override(Ext.form.RadioGroup, {
    isDirty: function() {
        if (this.originalValue == undefined) {
            return false;
        }
        if (this.getValue() == null) {
            return false;
        }
        if (this.getValue() || this.originalValue.inputValue == this.getValue().inputValue) {
            return false;
        }
        return true;
    }
});