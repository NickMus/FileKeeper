CREATE TABLE `filekeeper`.`user` IF NOT EXISTS
(
    `iduser` INT NOT NULL AUTO_INCREMENT,
    `user_name` VARCHAR(45) NOT NULL,
    `user_pass` VARCHAR(45) NOT NULL,
    PRIMARY KEY (`iduser`)
);