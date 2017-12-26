CREATE TABLE `T_Help_Center` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `question` varchar(250) DEFAULT NULL COMMENT '问题',
  `answer` varchar(250) DEFAULT NULL COMMENT '答案',
  `otherstatus` tinyint(4) DEFAULT NULL,
  `money` decimal(10,2) DEFAULT '0.00',
  `isdelete` tinyint(1) DEFAULT '0',
  `createtime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `modifytime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='帮助详情';