/*
Navicat MySQL Data Transfer

Source Server         : localhost
Source Server Version : 50610
Source Host           : 127.0.0.1:4966
Source Database       : djsdb

Target Server Type    : MYSQL
Target Server Version : 50610
File Encoding         : 65001

Date: 2014-02-16 22:33:51
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for systemmenu
-- ----------------------------
DROP TABLE IF EXISTS `systemmenu`;
CREATE TABLE `systemmenu` (
  `id` bigint(20) NOT NULL,
  `appClass` varchar(255) DEFAULT NULL,
  `fee` double DEFAULT NULL,
  `icon` varchar(255) DEFAULT NULL,
  `otherScripts` varchar(255) DEFAULT NULL,
  `pack` varchar(255) DEFAULT NULL,
  `params` varchar(255) DEFAULT NULL,
  `sequence` int(11) DEFAULT NULL,
  `status` int(11) DEFAULT NULL,
  `title` varchar(100) DEFAULT NULL,
  `types` varchar(100) DEFAULT NULL,
  `url` varchar(250) DEFAULT NULL,
  `actionClass` varchar(255) DEFAULT NULL,
  `sn` varchar(32) DEFAULT NULL,
  `issystem` bit(1) DEFAULT NULL,
  `theRole` varchar(255) DEFAULT NULL,
  `vrtype` varchar(100) DEFAULT NULL,
  `parent_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_1d7qjwqkwtjb5o2b7e6f1txf7` (`sn`),
  KEY `FK_ecvep6po6367t0qrm5t2f7gi0` (`parent_id`),
  CONSTRAINT `FK_ecvep6po6367t0qrm5t2f7gi0` FOREIGN KEY (`parent_id`) REFERENCES `systemmenu` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of systemmenu
-- ----------------------------
INSERT INTO `systemmenu` VALUES ('32768', '', null, '', '', '', '', '20', '0', '系统管理', null, '', '', 'sys_manage', '\0', null, null, null);
INSERT INTO `systemmenu` VALUES ('32769', 'SystemMenuManagePanel', null, '', '', 'sys', '', '7', '0', '系统菜单', null, 'SystemMenuManagePanel.js', '', 'sys_menu', '\0', null, null, '32768');
INSERT INTO `systemmenu` VALUES ('32770', '', null, '', '', '', '', '16', '0', '业务模块', null, '', '', 'logic_module', '\0', null, null, null);
INSERT INTO `systemmenu` VALUES ('32771', null, null, null, null, null, null, '18', '0', '基础服务', null, null, '', 'base-service', '\0', null, null, null);
INSERT INTO `systemmenu` VALUES ('32772', null, null, null, null, null, null, '8', '0', '系统权限', null, null, '', 'sys_security', '\0', null, null, '32768');
INSERT INTO `systemmenu` VALUES ('32773', 'SystemDictionaryManagePanel', null, '', '', 'sys', '', '4', '0', '数据字典', null, 'SystemDictionaryManagePanel.js', '', 'sys_dictionary', '\0', null, null, '32768');
INSERT INTO `systemmenu` VALUES ('32774', 'OrganizationManagePanel', null, '', '', 'sys', '', '3', '0', '部门管理', null, 'OrganizationManagePanel.js', '', 'sys_org', '\0', null, null, '32768');
INSERT INTO `systemmenu` VALUES ('32775', 'EmployeeManagePanel', null, '', '', 'sys', '', '2', '0', '员工管理', null, 'EmployeeManagePanel.js', '', 'sys_emp', '\0', null, null, '32768');
INSERT INTO `systemmenu` VALUES ('32776', 'SystemLogPanel', null, null, null, 'sys', null, '1', '0', '日志记录', null, 'SystemLogPanel.js', '', 'sys_log', '\0', null, null, '32768');
INSERT INTO `systemmenu` VALUES ('32777', 'abcd', null, '', '', '', '', '9', '0', 'RIA API Docs', null, 'http://localhost:4944/plugins/ria/docs', '', 'ria_api_docs', '\0', null, null, '32768');
INSERT INTO `systemmenu` VALUES ('32778', '', null, '', '', '', '', '10', '0', 'RIA Demos', null, 'http://localhost:4944/plugins/ria/examples', '', 'ria_demos', '\0', null, null, '32768');
INSERT INTO `systemmenu` VALUES ('32779', null, null, null, null, null, null, '1', '0', 'JVM状态信息', null, null, '', 'vim_info', '\0', null, null, '32771');
INSERT INTO `systemmenu` VALUES ('32780', '', null, '', '', '', '', '2', '0', '在线用户列表', null, '', '', 'online_customers', '\0', null, null, '32771');
INSERT INTO `systemmenu` VALUES ('32781', null, null, null, null, null, null, '3', '0', '连接池监控', null, null, '', 'druid_info', '\0', null, null, '32771');
INSERT INTO `systemmenu` VALUES ('32782', null, null, null, null, null, null, '4', '0', 'Hibernate监控', null, null, '', 'Hibernate_info', '\0', null, null, '32771');
INSERT INTO `systemmenu` VALUES ('32783', null, null, null, null, null, null, '5', '0', 'Ehcache缓存状态', null, null, '', 'ehcache_info', '\0', null, null, '32771');
INSERT INTO `systemmenu` VALUES ('32784', null, null, null, null, null, null, '6', '0', '在线编辑', null, null, '', 'online_edit', '\0', null, null, '32771');
INSERT INTO `systemmenu` VALUES ('65536', 'RoleManagePanel', null, null, null, 'sys', null, '1', '0', '系统角色', null, 'RoleManagePanel.js', '', 'sys_roles', '\0', null, null, '32772');
INSERT INTO `systemmenu` VALUES ('65537', 'PermissionPanel', null, null, null, 'sys', null, '2', '0', '系统权限', null, 'PermissionPanel.js', '', 'sys_permission', '\0', null, null, '32772');
INSERT INTO `systemmenu` VALUES ('65538', 'SystemResourcePanel', null, null, null, 'sys', null, '3', '0', '系统资源', null, 'SystemResourcePanel.js', '', 'sys_resource', '\0', null, null, '32772');
INSERT INTO `systemmenu` VALUES ('98304', 'ClientPanel', null, null, null, 'crm', null, '2', '0', '客户管理', null, 'ClientPanel.js', '', 'client_manage', '\0', null, null, '32770');
INSERT INTO `systemmenu` VALUES ('98305', 'LinkManPanel', null, null, null, 'crm', null, '1', '0', '联系人', null, 'LinkManPanel.js', '', 'link_man', '\0', null, null, '32770');
INSERT INTO `systemmenu` VALUES ('98306', null, null, null, null, null, null, '3', '0', '货品管理', null, null, '', 'product_manage', '\0', null, null, '32770');
INSERT INTO `systemmenu` VALUES ('98307', 'ProductDirPanel', null, null, null, 'function', null, '1', '0', '商品分类', null, 'ProductDirPanel.js', '', 'product_dir', '\0', null, null, '98306');
INSERT INTO `systemmenu` VALUES ('98309', 'ProductManagePanel', null, null, null, 'function', null, '2', '0', '商品资料', null, 'ProductManagePanel', '', 'product_info', '\0', null, null, '98306');
INSERT INTO `systemmenu` VALUES ('98310', 'OrdersPanel', null, null, null, 'function', null, '3', '0', '订单管理', null, 'OrdersPanel.js', '', 'orders', '\0', null, null, '98306');
INSERT INTO `systemmenu` VALUES ('98311', null, null, null, null, null, null, '4', '0', '综合示例', null, null, '', 'mix_demos', '\0', null, null, '32770');
INSERT INTO `systemmenu` VALUES ('98312', 'BaseGridListReport', null, null, null, 'function', null, '1', '0', '基于BaseGridPanel的报表', null, 'BaseGridListReport.js', '', 'base_grid_panel', '\0', null, null, '98311');
INSERT INTO `systemmenu` VALUES ('98313', 'TabOrdersPanel', null, null, null, 'function', null, '2', '0', '订单管理(Tab)', null, 'TabOrdersPanel.js', '', 'tab_orders', '\0', null, null, '98311');
INSERT INTO `systemmenu` VALUES ('98314', 'TreeListDD', null, null, null, 'function', null, '3', '0', '树和列表交互', null, 'TreeListDD.js', '', 'tree_list_dd', '\0', null, null, '98311');
INSERT INTO `systemmenu` VALUES ('98315', 'SellingProfitPanel', null, null, null, 'function', null, '4', '0', '销售利润表', null, 'SellingProfitPanel.js', '', 'selling_profit', '\0', null, null, '98311');
INSERT INTO `systemmenu` VALUES ('98316', 'ProductCrudMultiEditorPanelxDemo', null, '', '', 'function', '', '5', '0', '商品录入CrudMultiEditor', null, 'ProductCrudMultiEditorPanelxDemo.js', '', 'crud_multi_editor', '\0', null, null, '98311');

-- ----------------------------
-- Table structure for systemmenutemplateitem
-- ----------------------------
DROP TABLE IF EXISTS `systemmenutemplateitem`;
CREATE TABLE `systemmenutemplateitem` (
  `id` bigint(20) NOT NULL,
  `appClass` varchar(255) DEFAULT NULL,
  `fee` double DEFAULT NULL,
  `icon` varchar(255) DEFAULT NULL,
  `otherScripts` varchar(255) DEFAULT NULL,
  `pack` varchar(255) DEFAULT NULL,
  `params` varchar(255) DEFAULT NULL,
  `sequence` int(11) DEFAULT NULL,
  `status` int(11) DEFAULT NULL,
  `title` varchar(100) DEFAULT NULL,
  `types` varchar(100) DEFAULT NULL,
  `url` varchar(250) DEFAULT NULL,
  `menu_id` bigint(20) DEFAULT NULL,
  `parent_id` bigint(20) DEFAULT NULL,
  `template_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_9p190d7m5u7ixycy11k9pn4wv` (`menu_id`),
  KEY `FK_fvd9p49jm0o8qh4iab4165g1r` (`parent_id`),
  KEY `FK_t7c2u2s4s5o9kftqojqj5483a` (`template_id`),
  CONSTRAINT `FK_9p190d7m5u7ixycy11k9pn4wv` FOREIGN KEY (`menu_id`) REFERENCES `systemmenu` (`id`),
  CONSTRAINT `FK_fvd9p49jm0o8qh4iab4165g1r` FOREIGN KEY (`parent_id`) REFERENCES `systemmenutemplateitem` (`id`),
  CONSTRAINT `FK_t7c2u2s4s5o9kftqojqj5483a` FOREIGN KEY (`template_id`) REFERENCES `systemmenutemplate` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of systemmenutemplateitem
-- ----------------------------
INSERT INTO `systemmenutemplateitem` VALUES ('32768', '', null, null, '', null, '', '1', '0', '系统管理', null, null, '32768', null, null);
INSERT INTO `systemmenutemplateitem` VALUES ('32769', 'SystemMenuManagePanel', null, null, '', 'sys', '', '1', '0', '系统菜单', null, null, '32769', '32768', null);
INSERT INTO `systemmenutemplateitem` VALUES ('32770', '', null, null, '', null, '', '21', '0', '业务模块', null, null, '32770', null, null);
INSERT INTO `systemmenutemplateitem` VALUES ('32771', null, null, null, null, null, null, '18', '0', '基础服务', null, null, '32771', null, null);
INSERT INTO `systemmenutemplateitem` VALUES ('32772', null, null, null, null, null, null, '8', '0', '系统权限', null, null, '32772', '32768', null);
INSERT INTO `systemmenutemplateitem` VALUES ('32773', 'SystemDictionaryManagePanel', null, null, '', 'sys', '', '9', '0', '数据字典', null, null, '32773', '32768', null);
INSERT INTO `systemmenutemplateitem` VALUES ('32774', 'OrganizationManagePanel', null, null, '', 'sys', '', '3', '0', '部门管理', null, null, '32774', '32768', null);
INSERT INTO `systemmenutemplateitem` VALUES ('32775', 'EmployeeManagePanel', null, null, '', 'sys', '', '9', '0', '员工管理', null, null, '32775', '32768', null);
INSERT INTO `systemmenutemplateitem` VALUES ('32776', 'SystemLogPanel', null, null, null, 'sys', null, '1', '0', '日志记录', null, null, '32776', '32768', null);
INSERT INTO `systemmenutemplateitem` VALUES ('32777', 'abcd', null, null, '', null, '', '9', '0', 'RIA API Docs', null, null, '32777', '32768', null);
INSERT INTO `systemmenutemplateitem` VALUES ('32778', '', null, null, '', null, '', '10', '0', 'RIA Demos', null, null, '32778', '32768', null);
INSERT INTO `systemmenutemplateitem` VALUES ('32779', null, null, null, null, null, null, '1', '0', 'JVM状态信息', null, null, '32779', '32771', null);
INSERT INTO `systemmenutemplateitem` VALUES ('32780', '', null, null, '', null, '', '2', '0', '在线用户数', null, null, '32780', '32771', null);
INSERT INTO `systemmenutemplateitem` VALUES ('32781', null, null, null, null, null, null, '3', '0', '连接池监控', null, null, '32781', '32771', null);
INSERT INTO `systemmenutemplateitem` VALUES ('32782', null, null, null, null, null, null, '4', '0', 'Hibernate监控', null, null, '32782', '32771', null);
INSERT INTO `systemmenutemplateitem` VALUES ('32783', null, null, null, null, null, null, '5', '0', 'Ehcache缓存状态', null, null, '32783', '32771', null);
INSERT INTO `systemmenutemplateitem` VALUES ('32784', null, null, null, null, null, null, '6', '0', '在线编辑', null, null, '32784', '32771', null);
INSERT INTO `systemmenutemplateitem` VALUES ('65536', 'RoleManagePanel', null, null, null, 'sys', null, '1', '0', '系统角色', null, null, '65536', '32772', null);
INSERT INTO `systemmenutemplateitem` VALUES ('65537', 'PermissionPanel', null, null, null, 'sys', null, '2', '0', '系统权限', null, null, '65537', '32772', null);
INSERT INTO `systemmenutemplateitem` VALUES ('65538', 'SystemResourcePanel', null, null, null, 'sys', null, '3', '0', '系统资源', null, null, '65538', '32772', null);
INSERT INTO `systemmenutemplateitem` VALUES ('98304', 'ClientPanel', null, null, null, 'crm', null, '1', '0', '客户管理', null, null, '98304', '32770', null);
INSERT INTO `systemmenutemplateitem` VALUES ('98305', 'LinkManPanel', null, null, null, 'crm', null, '2', '0', '联系人', null, null, '98305', '32770', null);
INSERT INTO `systemmenutemplateitem` VALUES ('98306', null, null, null, null, null, null, '3', '0', '货品管理', null, null, '98306', '32770', null);
INSERT INTO `systemmenutemplateitem` VALUES ('98307', 'ProductDirPanel', null, null, null, 'function', null, '1', '0', '商品分类', null, null, '98307', '98306', null);
INSERT INTO `systemmenutemplateitem` VALUES ('98308', 'ProductManagePanel', null, null, null, 'function', null, '2', '0', '商品资料', null, null, '98309', '98306', null);
INSERT INTO `systemmenutemplateitem` VALUES ('98309', 'OrdersPanel', null, null, null, 'function', null, '3', '0', '订单管理', null, null, '98310', '98306', null);
INSERT INTO `systemmenutemplateitem` VALUES ('98310', null, null, null, null, null, null, '4', '0', '综合示例', null, null, '98311', '32770', null);
INSERT INTO `systemmenutemplateitem` VALUES ('98311', 'BaseGridListReport', null, null, null, 'function', null, '1', '0', '基于BaseGridPanel的报表', null, null, '98312', '98310', null);
INSERT INTO `systemmenutemplateitem` VALUES ('98312', 'TabOrdersPanel', null, null, null, 'function', null, '2', '0', '订单管理(Tab)', null, null, '98313', '98310', null);
INSERT INTO `systemmenutemplateitem` VALUES ('98313', 'TreeListDD', null, null, null, 'function', null, '3', '0', '树和列表交互', null, null, '98314', '98310', null);
INSERT INTO `systemmenutemplateitem` VALUES ('98314', 'SellingProfitPanel', null, null, null, 'function', null, '4', '0', '销售利润表', null, null, '98315', '98310', null);
INSERT INTO `systemmenutemplateitem` VALUES ('98315', 'ProductCrudMultiEditorPanelxDemo', null, null, '', 'function', '', '5', '0', '商品录入CrudMultiEditor', null, null, '98316', '98310', null);
