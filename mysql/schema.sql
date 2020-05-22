-- MySQL Workbench Forward Engineering

SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='TRADITIONAL,ALLOW_INVALID_DATES';

-- -----------------------------------------------------
-- Schema touchmars_spring_template
-- -----------------------------------------------------
DROP SCHEMA IF EXISTS `touchmars_spring_template` ;

-- -----------------------------------------------------
-- Schema touchmars_spring_template
-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS `touchmars_spring_template` DEFAULT CHARACTER SET utf8 ;
USE `touchmars_spring_template` ;

-- -----------------------------------------------------
-- Table `Touch_User`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `Touch_User` ;

CREATE TABLE IF NOT EXISTS `Touch_User` (
  `id` BIGINT(20) NOT NULL AUTO_INCREMENT,
  `hash_id` VARCHAR(255) NULL DEFAULT NULL,
  `modified_dt` TIMESTAMP NULL DEFAULT CURRENT_TIMESTAMP,
  `modified_by` VARCHAR(255) NULL DEFAULT NULL,
  `created_dt` TIMESTAMP NULL DEFAULT CURRENT_TIMESTAMP,
  `created_by` VARCHAR(255) NULL DEFAULT NULL,
  PRIMARY KEY (`id`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `Account`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `Account` ;

CREATE TABLE IF NOT EXISTS `Account` (
  `id` BIGINT(100) NOT NULL AUTO_INCREMENT,
  `user_id` BIGINT(100) NOT NULL,
  `cell` VARCHAR(255) NULL DEFAULT NULL COMMENT 'register|login cell|email is different from contact methods!',
  `email` VARCHAR(255) NOT NULL,
  `password` VARCHAR(255) NOT NULL,
  `first_name` VARCHAR(255) NULL DEFAULT NULL,
  `last_name` VARCHAR(255) NULL DEFAULT NULL,
  `created_dt` TIMESTAMP NULL DEFAULT CURRENT_TIMESTAMP,
  `created_by` VARCHAR(255) NULL DEFAULT NULL,
  `modified_dt` TIMESTAMP NULL DEFAULT CURRENT_TIMESTAMP,
  `modified_by` VARCHAR(255) NULL DEFAULT NULL,
  PRIMARY KEY (`id`, `user_id`),
  CONSTRAINT `fk_Account_User`
    FOREIGN KEY (`user_id`)
    REFERENCES `Touch_User` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;

CREATE UNIQUE INDEX `uq_email` ON `Account` (`email` ASC);

CREATE UNIQUE INDEX `uq_Account_User` ON `Account` (`user_id` ASC);

CREATE INDEX `fk_Account_User1_idx` ON `Account` (`user_id` ASC);

-- -----------------------------------------------------
-- Table `Authority`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `Authority` ;

CREATE TABLE IF NOT EXISTS `Authority` (
  `id` INT(11) NOT NULL,
  `name` VARCHAR(50) NOT NULL COMMENT 'could be Driver, Driver Team, Rider, Inventory[gas station, convenient store, small warehouse, private garage], buyer, seller, packer, Sender[personal|business], Receiver, Deliver',
  `description` VARCHAR(255) NULL DEFAULT NULL,
  `code` VARCHAR(255) NULL DEFAULT NULL,
  PRIMARY KEY (`id`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `User_Authority`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `User_Authority` ;

CREATE TABLE IF NOT EXISTS `User_Authority` (
  `id` BIGINT(100) NOT NULL AUTO_INCREMENT,
  `user_id` BIGINT(100) NOT NULL,
  `authority_id` INT(11) NOT NULL,
  `created_dt` TIMESTAMP NULL DEFAULT CURRENT_TIMESTAMP,
  `created_by` VARCHAR(255) NULL DEFAULT NULL,
  `modified_dt` TIMESTAMP NULL DEFAULT CURRENT_TIMESTAMP,
  `modified_by` VARCHAR(255) NULL DEFAULT NULL,
  PRIMARY KEY (`id`),
  CONSTRAINT `role_id`
    FOREIGN KEY (`authority_id`)
    REFERENCES `Authority` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `user_id`
    FOREIGN KEY (`user_id`)
    REFERENCES `Touch_User` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;

CREATE INDEX `fk_User_Role_Role1_idx` ON `User_Authority` (`authority_id` ASC);

CREATE INDEX `user_id_idx` ON `User_Authority` (`user_id` ASC);


-- -----------------------------------------------------
-- Table `User_Connection`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `User_Connection` ;

CREATE TABLE IF NOT EXISTS `User_Connection` (
  `id` BIGINT(100) NOT NULL AUTO_INCREMENT,
  `user_id` BIGINT(100) NOT NULL,
  `provider_id` VARCHAR(255) NOT NULL,
  `provider_user_id` VARCHAR(255) NOT NULL,
  `rank` INT(11) NULL DEFAULT NULL,
  `display_name` VARCHAR(255) NULL DEFAULT NULL,
  `profile_url` VARCHAR(255) NULL DEFAULT NULL,
  `image_url` VARCHAR(255) NULL DEFAULT NULL,
  `access_token` VARCHAR(255) NULL DEFAULT NULL,
  `secret` VARCHAR(255) NULL DEFAULT NULL,
  `refresh_token` VARCHAR(255) NULL DEFAULT NULL,
  `expire_time` BIGINT(100) NULL DEFAULT NULL,
  `token_type` VARCHAR(255) NULL DEFAULT NULL,
  `last_name` VARCHAR(255) NULL DEFAULT NULL,
  `first_name` VARCHAR(255) NULL DEFAULT NULL,
  `locale` VARCHAR(255) NULL DEFAULT NULL,
  `email` VARCHAR(255) NULL DEFAULT NULL,
  `email_verified` TINYINT(1) NULL DEFAULT NULL,
  `created_dt` TIMESTAMP NULL DEFAULT CURRENT_TIMESTAMP,
  `created_by` VARCHAR(255) NULL DEFAULT NULL,
  `modified_dt` TIMESTAMP NULL DEFAULT CURRENT_TIMESTAMP,
  `modified_by` VARCHAR(255) NULL DEFAULT NULL,
  PRIMARY KEY (`id`),
  CONSTRAINT `fk_Connection_User`
    FOREIGN KEY (`user_id`)
    REFERENCES `Touch_User` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;

CREATE INDEX `fk_Connection_User_idx` ON `User_Connection` (`user_id` ASC);


-- -----------------------------------------------------
-- Table `User_Connection_Wechat`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `User_Connection_Wechat` ;

CREATE TABLE IF NOT EXISTS `User_Connection_Wechat` (
  `id` BIGINT(20) NOT NULL AUTO_INCREMENT,
  `user_id` BIGINT(20) NOT NULL,
  `app_id` VARCHAR(255) NULL DEFAULT NULL,
  `open_id` VARCHAR(255) NULL DEFAULT NULL,
  `union_id` VARCHAR(255) NULL DEFAULT NULL,
  `subscribe` TINYINT(1) NULL DEFAULT NULL,
  `session_key` VARCHAR(255) NULL DEFAULT NULL,
  `expires` BIGINT(20) NULL DEFAULT NULL,
  `nickname` VARCHAR(255) NULL DEFAULT NULL,
  `sex` INT(11) NULL DEFAULT NULL,
  `city` VARCHAR(255) NULL DEFAULT NULL,
  `province` VARCHAR(255) NULL DEFAULT NULL,
  `country` VARCHAR(255) NULL DEFAULT NULL,
  `language` VARCHAR(255) NULL DEFAULT NULL,
  `avatar_Url` VARCHAR(255) NULL DEFAULT NULL,
  `subscribe_time` BIGINT(20) NULL DEFAULT NULL,
  `remark` VARCHAR(255) NULL DEFAULT NULL,
  `created_dt` TIMESTAMP NULL DEFAULT CURRENT_TIMESTAMP,
  `created_by` VARCHAR(255) NULL DEFAULT NULL,
  `modified_dt` TIMESTAMP NULL DEFAULT CURRENT_TIMESTAMP,
  `modified_by` VARCHAR(255) NULL DEFAULT NULL,
  PRIMARY KEY (`id`),
  CONSTRAINT `fk_User_Connection_Wechat_Touch_User1`
    FOREIGN KEY (`user_id`)
    REFERENCES `Touch_User` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;

CREATE INDEX `fk_User_Connection_Wechat_Touch_User1_idx` ON `User_Connection_Wechat` (`user_id` ASC);


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
-- begin attached script 'script'
INSERT INTO Authority
(ID, name, description, code) VALUES
(1, 'ROLE_USER', 'ROLE_USER', 'ROLE_USER');
INSERT INTO Authority
(ID, name, description, code) VALUES
(2, 'ROLE_ADMIN','ROLE_ADMIN','ROLE_ADMIN');

-- end attached script 'script'
