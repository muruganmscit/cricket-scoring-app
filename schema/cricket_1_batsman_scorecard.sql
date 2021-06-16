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
-- Table structure for table `batsman_scorecard`
--

DROP TABLE IF EXISTS `batsman_scorecard`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `batsman_scorecard` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `balls` int(11) DEFAULT NULL,
  `batting` int(11) DEFAULT NULL,
  `batting_order` int(11) DEFAULT NULL,
  `dot_balls` int(11) DEFAULT NULL,
  `fours` int(11) DEFAULT NULL,
  `innings` int(11) NOT NULL,
  `match_id` bigint(20) DEFAULT NULL,
  `ones` int(11) DEFAULT NULL,
  `runs` int(11) DEFAULT NULL,
  `sixes` int(11) DEFAULT NULL,
  `threes` int(11) DEFAULT NULL,
  `twos` int(11) DEFAULT NULL,
  `wicket_type` int(11) DEFAULT NULL,
  `batsman_id` bigint(20) DEFAULT NULL,
  `bowler_id` bigint(20) DEFAULT NULL,
  `fielder1_id` bigint(20) DEFAULT NULL,
  `fielder2_id` bigint(20) DEFAULT NULL,
  `team_id` bigint(20) DEFAULT NULL,
  `fives` int(11) DEFAULT NULL,
  `sevens` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_BATSMAN_ID` (`batsman_id`),
  KEY `FK_WK_BOWLER_ID` (`bowler_id`),
  KEY `FK_F1_ID` (`fielder1_id`),
  KEY `FK_F2_ID` (`fielder2_id`),
  KEY `FK_BAT_SC_TEAM` (`team_id`),
  CONSTRAINT `FK_BATSMAN_ID` FOREIGN KEY (`batsman_id`) REFERENCES `players` (`id`),
  CONSTRAINT `FK_BAT_SC_TEAM` FOREIGN KEY (`team_id`) REFERENCES `teams` (`id`),
  CONSTRAINT `FK_F1_ID` FOREIGN KEY (`fielder1_id`) REFERENCES `players` (`id`),
  CONSTRAINT `FK_F2_ID` FOREIGN KEY (`fielder2_id`) REFERENCES `players` (`id`),
  CONSTRAINT `FK_WK_BOWLER_ID` FOREIGN KEY (`bowler_id`) REFERENCES `players` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=67 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2021-06-15 19:32:39
