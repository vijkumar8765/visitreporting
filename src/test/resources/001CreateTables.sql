
DROP DATABASE SCRATCH;
CREATE DATABASE SCRATCH;
USE SCRATCH;
DROP TABLE IF EXISTS `TESTVIJAY`;
DROP TABLE IF EXISTS `AGREEMENT`;
DROP TABLE IF EXISTS `SIGNATURE`;
DROP TABLE IF EXISTS `OBILL`;
DROP TABLE IF EXISTS `MARKET_LIST`;
DROP TABLE IF EXISTS `MARKET_LINK`;
DROP TABLE IF EXISTS `INVESTMENT_TERMS`;
DROP TABLE IF EXISTS `INVESTMENT_PROFILE`;
DROP TABLE IF EXISTS `MEMBER`;

CREATE  TABLE IF NOT EXISTS `TESTVIJAY` (
  `id` INT NOT NULL,
  `name` VARCHAR(20) NOT NULL ,
  `address` VARCHAR(10) NOT NULL)
ENGINE = InnoDB
;

CREATE  TABLE IF NOT EXISTS `MEMBER` (
  `id` INT NOT NULL auto_increment, 
  `parent_id` INT NULL ,
  `name` VARCHAR(45) NOT NULL ,
  `distributor_id` INT NULL ,
  `buyer_onboarded_flg` TINYINT(1) NULL DEFAULT false ,
  `supplier_onboarded_flg` TINYINT(1) NULL DEFAULT false ,
  `investor_flg` TINYINT(1) NULL DEFAULT false ,
  `distributor_flg` TINYINT(1) NULL DEFAULT false ,
  `company_reg_no` VARCHAR(45) NOT NULL ,
  PRIMARY KEY (`id`) ,
  UNIQUE INDEX `ID_UNIQUE` (`id`))
ENGINE = InnoDB
;

CREATE  TABLE IF NOT EXISTS `OBILL` (
  `drawee_id` INT  NOT NULL COMMENT 'not implemented in MVP' ,
  `id` INT NOT NULL auto_increment,
  `drawer_id` INT NOT NULL ,
  `payee_id` INT NOT NULL ,
  `drawn_dt` DATE NULL ,
  `agreed_dt` DATE NULL ,
  `amount` INT(11) NOT NULL DEFAULT 0 ,
  `currency` VARCHAR(5) NOT NULL DEFAULT 'GBP' COMMENT 'Not Implemented in MVP' ,
  `maturity_dt` DATE NOT NULL ,
  PRIMARY KEY (`id`))
ENGINE = InnoDB
;

CREATE  TABLE IF NOT EXISTS `AGREEMENT` (
  `id` INT  NOT NULL auto_increment COMMENT 'not implemented in MVP' ,
  `rendention_id` INT NOT NULL COMMENT 'rendered copy of the agreement' ,
  PRIMARY KEY (`id`) )
ENGINE = InnoDB
;

CREATE  TABLE IF NOT EXISTS `SIGNATURE` (
  `digital_signature` INT NOT NULL COMMENT 'actual signature' ,
  `party_id` INT NOT NULL COMMENT 'rendered copy of the agreement' ,
  `agreement_id` INT NOT NULL)
ENGINE = InnoDB
;

CREATE  TABLE IF NOT EXISTS `INVESTMENT_TERMS` (
  `id` INT NOT NULL auto_increment,
  `investor_id` INT NOT NULL ,
  `name` VARCHAR(20) NOT NULL ,
  `currency` VARCHAR(10) NULL DEFAULT 'GBP' ,
  `fixed_fee` INT NULL DEFAULT 0 ,
  `percentage_fee` INT NULL DEFAULT -1 ,
  PRIMARY KEY (`id`))
ENGINE = InnoDB
;

CREATE  TABLE IF NOT EXISTS `INVESTMENT_PROFILE` (
  `id` INT NOT NULL auto_increment,
  `description` VARCHAR(50) NOT NULL ,
  `name` VARCHAR(20) NOT NULL ,
  `employees_min` INT NULL ,
  `turnover_min` INT NULL ,
  `turnover_max` INT NULL ,
  `max_exposure` INT NOT NULL DEFAULT 0 ,
  `employees_max` INT NULL ,
  `investment_terms_id` INT NOT NULL ,
  `investor_id` INT NOT NULL ,
  PRIMARY KEY (`id`))
ENGINE = InnoDB
;

CREATE  TABLE IF NOT EXISTS `MARKET_LIST` (
  `id` INT NOT NULL auto_increment,
  `sector` VARCHAR(20) NOT NULL ,
  PRIMARY KEY (`id`) )
ENGINE = InnoDB;


CREATE  TABLE IF NOT EXISTS `MARKET_LINK` (
  `profile_id` INT NOT NULL ,
  `market_id` INT NULL ,
  PRIMARY KEY (`profile_id`,`market_id`) )
ENGINE = InnoDB
;

ALTER TABLE `MEMBER` ADD CONSTRAINT `parent_id_fk` foreign key(`parent_id`) references `member`(`id`);

ALTER TABLE `MEMBER` ADD CONSTRAINT `distributor_id_fk` foreign key(`distributor_id`) references `member`(`id`);

ALTER TABLE `OBILL` ADD CONSTRAINT `payee_id_fk` foreign key(`payee_id`) references `member`(`id`);

ALTER TABLE `OBILL` ADD CONSTRAINT `drawer_id_fk` foreign key(`drawer_id`) references `member`(`id`);

ALTER TABLE `OBILL` ADD CONSTRAINT `drawee_id_fk` foreign key(`drawee_id`) references `member`(`id`);

ALTER TABLE `SIGNATURE` ADD CONSTRAINT `party_id_fk` foreign key(`party_id`) references `member`(`id`);

ALTER TABLE `INVESTMENT_TERMS` ADD CONSTRAINT `investor_id_fk` foreign key(`investor_id`) references `member`(`id`);

ALTER TABLE `INVESTMENT_PROFILE` ADD CONSTRAINT `investment_terms_id_fk` foreign key(`investment_terms_id`) references `investment_terms`(`id`);

ALTER TABLE `INVESTMENT_PROFILE` ADD CONSTRAINT `investment_profile_investor_id_fk` foreign key(`investor_id`) references `member`(`id`);

ALTER TABLE `MARKET_LINK` ADD CONSTRAINT `profile_id_fk` foreign key(`profile_id`) references `investment_profile`(`id`);

ALTER TABLE `MARKET_LINK` ADD CONSTRAINT `market_id_fk` foreign key(`market_id`) references `market_list`(`id`);

COMMIT;