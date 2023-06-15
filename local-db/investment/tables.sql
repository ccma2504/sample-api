CREATE SCHEMA IF NOT EXISTS `investment` DEFAULT CHARACTER SET utf8;
USE `investment`;


CREATE TABLE `product` (
  `product_id` BIGINT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '상품 ID',
  `title` VARCHAR(100) NOT NULL COMMENT '상품 제목',
  `total_investing_amount` BIGINT UNSIGNED NOT NULL DEFAULT '0' COMMENT '총 모집금액',
  `current_investing_amount` BIGINT UNSIGNED NOT NULL DEFAULT '0' COMMENT '현재 모집금액',
  `current_investing_user_cnt` INT UNSIGNED NOT NULL DEFAULT '0' COMMENT '투자자 수',
  `started_at` DATETIME NOT NULL COMMENT '상품 모집 시작일시',
  `finished_at` DATETIME NOT NULL COMMENT '상품 모집 종료일시',
  `investing_status` CHAR(10) NOT NULL COMMENT '투자 모집 상태(모집중:PROCEEDING, 모집완료:DONE)',
  `reg_dt` DATETIME NOT NULL COMMENT '등록일시',
  `chg_dt` DATETIME NOT NULL COMMENT '수정일시',
  PRIMARY KEY (`product_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='상품';


CREATE TABLE `user` (
  `user_id` INT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '고객 ID',
  `reg_dt` DATETIME NOT NULL COMMENT '등록일시',
  PRIMARY KEY (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='고객';


CREATE TABLE `investment` (
  `product_id` BIGINT UNSIGNED NOT NULL COMMENT '상품 ID',
  `user_id` INT UNSIGNED NOT NULL COMMENT '고객 ID',
  `investing_amount` BIGINT UNSIGNED NOT NULL DEFAULT '0' COMMENT '투자금액',
  `investing_at` DATETIME NOT NULL COMMENT '투자일시',
  PRIMARY KEY (`product_id`, `user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='투자내역';