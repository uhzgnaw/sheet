create database approve_test;
use approve_test;

CREATE TABLE `approvesheet` (

  `id` bigint(20) AUTO_INCREMENT NOT NULL COMMENT '主键，自增ID',
  
  `applicant` char(50)   NOT NULL COMMENT '申请人',
  
  `descript` longtext   NOT NULL  COMMENT '申请事宜',
  
  `createTime` datetime DEFAULT NULL COMMENT '创建时间',
  
  `endTime` datetime DEFAULT NULL COMMENT '结束时间',
  
  `version` int(11) DEFAULT NULL COMMENT '版本号',

  `status` int(1)   DEFAULT 0 COMMENT '审批单状态',
  
  `nextIndex` int(1)   DEFAULT 0 COMMENT '下一个审批节点',
  
  `approvers` longtext   NOT NULL  COMMENT '审批人列表',
  
  `currentApprover` char(50)   NOT NULL  COMMENT '当前审批人',

  `remark` longtext   COMMENT '备注',

  PRIMARY KEY (`id`),

  UNIQUE KEY `id_UNIQUE` (`id`)

) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='审批流程表';

CREATE TABLE `user` (

  `id` bigint(20) AUTO_INCREMENT NOT NULL COMMENT '主键，自增ID',
  
  `name` char(50)   NOT NULL COMMENT '用户名',
  
  `password` char(50)   NOT NULL COMMENT '密码',

  `remark` longtext   COMMENT '备注',

  PRIMARY KEY (`id`),

  UNIQUE KEY `id_UNIQUE` (`id`)

) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='用户表';

CREATE TABLE `approve_record` (

  `id` bigint(20) AUTO_INCREMENT NOT NULL COMMENT '主键，自增ID',
  
  `approve_sheet_id` bigint(20)  NOT NULL COMMENT '审批单ID',
  
  `approver` char(50)   NOT NULL COMMENT '审批人',
  
  `approval_opinion` longtext   COMMENT '审批意见',
  
  `status` int(1)   NOT NULL COMMENT '操作，拒绝/通过',

  `remark` longtext   COMMENT '备注',
  
  `approveTime` datetime DEFAULT NULL COMMENT '审批时间',

  PRIMARY KEY (`id`),

  UNIQUE KEY `id_UNIQUE` (`id`)

) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='审批记录表';
















