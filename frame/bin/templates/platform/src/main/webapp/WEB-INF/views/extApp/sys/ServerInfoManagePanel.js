ServerInfoManagePanel = Ext.extend(Ext.Panel, {
    border: false,
    layout: 'border',
    margins: '5 5 5 5',
    initComponent: function() {
        this.initBorderLayout();
        this.initAllEvent();
        ServerInfoManagePanel.superclass.initComponent.call(this);
    },
    initBorderLayout: function() {
        this.grid = new Ext.grid.PropertyGrid({
            title: '服务器基本配置',
            width: 320,
            split: true,
            collapseMode: 'mini',
            region: 'west',
            source: null
        });
        this.grid.getColumnModel().setColumnWidth(0, 35);

        this.jvm = new Ext.Panel({
            title: 'JVM内存监控视图',
            region: 'center',
            autoScroll: true
        });
        this.items = [this.grid, this.jvm]
    },
    initAllEvent: function() {
        this.grid.on('render', function(me) {
            var config = {
                url: 'serverInfo.java?cmd=config',
                success: function(response) {
                    var obj = Ext.decode(response.responseText);
                    me.setSource(obj);
                }
            };
            Ext.Ajax.request(config);
        }, this);
        //设置grid不可编辑
        this.grid.on('beforeedit', function(e) {
            //e.cancel = true;
            //return false;
        });
        this.jvm.on('render', function() {
            var target = this.jvm.body.dom;
            var jsPath = '/plugins/chart/Charts/FusionCharts.js';
            var width = this.jvm.getWidth();
            var height = this.jvm.getHeight();
            this.jvm.body.setStyle({
                'text-align': 'center',
                'padding-top': '50px'
            });
            Disco.loadJs(jsPath, false, function() {
                var url = "/charts.json";
                var config = {
                    width: 500,
                    height: 400,
                    renderTo: target
                };
                /**
                 * bgColor 				: 整体背景颜色
                 * canvasBorderColor	: 最外层方框边框线颜色
                 * canvasBgColor		: 坐标框的背景颜色
                 * divLineColor			: X坐标颜色
                 * vDivLineColor		: Y坐标颜色
                 * baseFontColor		: 所有文本的颜色
                 * toolTipBgColor		: 鼠标移到坐标节点上时提示框的背景色
                 * toolTipBorderColor	: 提示框的边框颜色
                 * legendBgColor		: 最下面的工具框背景颜色
                 * legendBorderColor	: 最下面的工具框边框颜色
                 * color				: 线条颜色
                 * */
                var dataString =
                        '<chart manageResize="1" caption="CPU、内存使用率实时监控(Html5 for WebSocket Demo)" bgColor="ffffff" bgAlpha="100"'
                                + ' canvasBorderThickness="1" canvasBorderColor="013475" canvasBgColor="f8f8f8" yAxisMaxValue="100"  decimals="0"'
                                + ' numdivlines="9" numVDivLines="28" numDisplaySets="30" divLineColor="013475" vDivLineColor="013475" '
                                + 'divLineAlpha="100" chartLeftMargin="10" baseFontColor="000000" showRealTimeValue="0" numberSuffix="%" '
                                + 'labelDisplay="rotate" slantLabels="1" toolTipBgColor="ea09f2" toolTipBorderColor="013475" baseFontSize="11" '
                                + 'showAlternateHGridColor="0" legendBgColor="ffffff" legendBorderColor="013475" legendPadding="35" showLabels="1">'
                                + '\n\<categories>\n\<category label="开始"/>\n\</categories>\n\<dataset color="e107fb" seriesName="CPU使用率" '
                                + 'showValues="0" alpha="100" anchorAlpha="0" lineThickness="1">\n\<set value="0" />\n\</dataset>\n\<dataset '
                                + 'color="4e4aff" seriesName="内存使用率" showValues="0" alpha="100" anchorAlpha="0" lineThickness="1">' + '\n\<set value="0" />\n\</dataset>\n\</chart>';

                var chart = new FusionCharts("/plugins/chart/Charts/RealTimeLine.swf", "serviceInfoManage", "650", "400", "0", "1");
                chart.setXMLData(dataString);
                chart.render(target);

                window.dataUpdateTimer = null;

                FusionCharts.addEventListener("Rendered", function(e, a) {
                    if (e.sender.id == "tmpChartId")
                        return;

                    window.dataUpdateTimer = window.setInterval(function() {
                        provideRealTimeDataThroughJSAPI(e.sender);
                    }, 2000);
                });

                function provideRealTimeDataThroughJSAPI(sender) {
                    var updater = sender.feedData ? sender : null;

                    var p1 = Math.round(Math.random() * 100);
                    var p2 = Math.round(Math.random() * 100);
                    var dateTimeLabel = new Date().format("H:i:s");
                    if (updater) {
                        Ext.Ajax.request({
                            async: false,
                            url: 'serverInfo.java?cmd=getCpuAndMem',
                            success: function(response) {
                                updater.feedData(response.responseText);
                            }
                        });
                    }
                }
            }, null, Disco.ajaxCache);
        }, this);
    }
});