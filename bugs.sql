
DROP TABLE IF EXISTS `bug_threads`;
CREATE TABLE `bug_threads` (
  `bug_thread_id` int(11) NOT NULL auto_increment,
  `bug_id` int(11) default NULL,
  `bug_thread_reporter` varchar(45) default NULL,
  `bug_thread_desc` text,
  `bug_thread_date` date default NULL,
  PRIMARY KEY  (`bug_thread_id`),
  KEY `newfk` (`bug_id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

--
-- Dumping data for table `bug_threads`
--

LOCK TABLES `bug_threads` WRITE;
/*!40000 ALTER TABLE `bug_threads` DISABLE KEYS */;
/*!40000 ALTER TABLE `bug_threads` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `bugs`
--

DROP TABLE IF EXISTS `bugs`;
CREATE TABLE `bugs` (
  `bug_id` int(11) NOT NULL auto_increment,
  `bug_title` varchar(45) default NULL,
  `bug_reporter` varchar(100) default NULL,
  `bug_desc` text,
  `bug_status` varchar(10) default NULL,
  `bug_date` date default NULL,
  PRIMARY KEY  (`bug_id`)
) ENGINE=MyISAM AUTO_INCREMENT=4 DEFAULT CHARSET=utf8;

--
-- Dumping data for table `bugs`
--

LOCK TABLES `bugs` WRITE;
/*!40000 ALTER TABLE `bugs` DISABLE KEYS */;
INSERT INTO `bugs` VALUES (1,'bug1','scott','this is a bug description','','2008-09-15'),(2,'bug2','scott','this is a bug description','','2008-09-15'),(3,'bug3','scott','this is a test bug.','','2008-09-15');
/*!40000 ALTER TABLE `bugs` ENABLE KEYS */;
UNLOCK TABLES;
- Dump completed on 2008-09-16  3:46:05

-- new table 08.09.09 

CREATE TABLE `dmm`.`person` (
  `person_pid` INT NOT NULL DEFAULT NULL AUTO_INCREMENT,
  `firstName` VARCHAR(45) DEFAULT NULL,
  `lastName` varchar(45) DEFAULT NULL,
  `address1` varchar(45) DEFAULT NULL,
  `city` varchar(45) DEFAULT NULL,
  `county` varchar(45) DEFAULT NULL,
  `state` varchar(45),
  `phone` varchar(45),
  `cell` varchar(45) DEFAULT NULL,
  `email` varchar(45),
  `notes` LONGTEXT DEFAULT NULL,
  `person_type` varchar(45),
  PRIMARY KEY (`person_pid`)
)
CHARACTER SET utf8;

CREATE TABLE `dmm`.`instrument` (
  `pid` int  NOT NULL AUTO_INCREMENT,
  `name` varchar(45)  NOT NULL,
  `itemNumber` varchar(45)  NOT NULL,
  `supplier` varchar(45)  NOT NULL,
  `type` varchar(45)  NOT NULL,
  PRIMARY KEY (`pid`)
)
ENGINE = MyISAM;

---new table 09.10.09
CREATE TABLE `dmm`.`instrumentInventory` (
  `pid` int  NOT NULL AUTO_INCREMENT,
  `location` VARCHAR(45)  NOT NULL,
  `instrPid` varchar(45)  NOT NULL,
  `count` varchar(45)  NOT NULL,
  PRIMARY KEY (`pid`)
)
ENGINE = MyISAM;


