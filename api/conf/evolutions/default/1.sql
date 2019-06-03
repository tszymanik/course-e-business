-- !Ups

CREATE TABLE `Users`
(
    `UserId`     int(11) NOT NULL AUTO_INCREMENT,
    `Email`      varchar(45) DEFAULT NULL,
    `Password`   varchar(45) DEFAULT NULL,
    `Address`    varchar(45) DEFAULT NULL,
    `City`       varchar(45) DEFAULT NULL,
    `PostalCode` varchar(45) DEFAULT NULL,
    `Country`    varchar(45) DEFAULT NULL,
    `Phone`      varchar(45) DEFAULT NULL,
    PRIMARY KEY (`UserId`)
);

CREATE TABLE `Categories`
(
    `CategoryId`          int(11) NOT NULL AUTO_INCREMENT,
    `CategoryName`        varchar(45) DEFAULT NULL,
    `CategoryDescription` varchar(45) DEFAULT NULL,
    PRIMARY KEY (`CategoryId`)
);

CREATE TABLE `Orders`
(
    `OrderId`     int(11) NOT NULL AUTO_INCREMENT,
    `UserId`      int(11)  DEFAULT NULL,
    `OrderDate`   datetime DEFAULT NULL,
    `ShippedDate` datetime DEFAULT NULL,
    PRIMARY KEY (`OrderId`),
    KEY `UserId_idx` (`UserId`),
    CONSTRAINT `UserId` FOREIGN KEY (`UserId`) REFERENCES `Users` (`UserId`)
);

CREATE TABLE `Products`
(
    `ProductId`          int(11) NOT NULL AUTO_INCREMENT,
    `ProductName`        varchar(45)   DEFAULT NULL,
    `ProductDescription` varchar(45)   DEFAULT NULL,
    `ProductPhoto`       blob,
    `CategoryId`         int(11)       DEFAULT NULL,
    `QuantityPerUnit`    varchar(45)   DEFAULT NULL,
    `UnitPrice`          decimal(6, 4) DEFAULT NULL,
    `UnitsInStock`       int(11)       DEFAULT NULL,
    PRIMARY KEY (`ProductId`),
    KEY `CategoryId_idx` (`CategoryId`),
    CONSTRAINT `CategoryId` FOREIGN KEY (`CategoryId`) REFERENCES `Products` (`ProductId`)
);

CREATE TABLE `Order Details`
(
    `OrderDetailId` int(11) NOT NULL AUTO_INCREMENT,
    `OrderId`       int(11) NOT NULL,
    `ProductId`     int(11)       DEFAULT NULL,
    `UnitPrice`     decimal(6, 4) DEFAULT NULL,
    `Quantity`      int(11)       DEFAULT NULL,
    PRIMARY KEY (`OrderDetailId`),
    KEY `OrderId_idx` (`OrderId`),
    KEY `ProductId_idx` (`ProductId`),
    CONSTRAINT `OrderId` FOREIGN KEY (`OrderId`) REFERENCES `Orders` (`OrderId`),
    CONSTRAINT `ProductId` FOREIGN KEY (`ProductId`) REFERENCES `Products` (`ProductId`)
);


-- !Downs

DROP TABLE IF EXISTS `Categories`;
DROP TABLE IF EXISTS `Order Details`;
DROP TABLE IF EXISTS `Orders`;
DROP TABLE IF EXISTS `Products`;
DROP TABLE IF EXISTS `Users`;
