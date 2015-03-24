-- adding weight column to Products 2/28/2015
ALTER TABLE `dmm`.`Products`
ADD COLUMN `weight` VARCHAR(45) NULL DEFAULT NULL COMMENT 'This is the shipping weight of this item.' AFTER `lastDODte`;


-- CREATE TABLE `dmm`.`ProductsLocationCount` (
--   `plcNum` INT,
--   `productNum` INT NOT NULL DEFAULT 0,
--   `OREM` INT,
--   `LEHI` INT,
--   `MURRAY` INT,
--   `LOCO1` INT,
--   `LOCO2` INT,
--   `DAYVIOLIN` INT,
--   PRIMARY KEY(`productNum`)
-- )
--
-- ALTER TABLE `dmm`.`Transactions` MODIFY COLUMN `transNum` BIGINT NOT NULL AUTO_INCREMENT;