CREATE DATABASE  IF NOT EXISTS `cricket_1` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci */ /*!80016 DEFAULT ENCRYPTION='N' */;
USE `cricket_1`;
-- MySQL dump 10.13  Distrib 8.0.17, for macos10.14 (x86_64)
--
-- Host: localhost    Database: cricket_1
-- ------------------------------------------------------
-- Server version	8.0.16

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `matches`
--

DROP TABLE IF EXISTS `matches`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `matches` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `city` varchar(255) NOT NULL,
  `dates` varchar(255) NOT NULL,
  `difference` int(11) DEFAULT NULL,
  `outcome_by` varchar(255) DEFAULT NULL,
  `overs` int(11) NOT NULL,
  `toss_decision` int(11) DEFAULT NULL,
  `umpire1` varchar(255) DEFAULT NULL,
  `umpire2` varchar(255) DEFAULT NULL,
  `venue` varchar(255) NOT NULL,
  `away_team_id` bigint(20) DEFAULT NULL,
  `home_team_id` bigint(20) DEFAULT NULL,
  `toss_team_id` bigint(20) DEFAULT NULL,
  `winning_team_id` bigint(20) DEFAULT NULL,
  `running_innings` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_AWAY_TEAM` (`away_team_id`),
  KEY `FK_HOME_TEAM` (`home_team_id`),
  KEY `FK_TOSS_TEAM` (`toss_team_id`),
  KEY `FK_WINNING_TEAM` (`winning_team_id`),
  CONSTRAINT `FK_AWAY_TEAM` FOREIGN KEY (`away_team_id`) REFERENCES `teams` (`id`),
  CONSTRAINT `FK_HOME_TEAM` FOREIGN KEY (`home_team_id`) REFERENCES `teams` (`id`),
  CONSTRAINT `FK_TOSS_TEAM` FOREIGN KEY (`toss_team_id`) REFERENCES `teams` (`id`),
  CONSTRAINT `FK_WINNING_TEAM` FOREIGN KEY (`winning_team_id`) REFERENCES `teams` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2021-06-15 19:32:40
