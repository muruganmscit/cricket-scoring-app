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
-- Table structure for table `innings`
--

DROP TABLE IF EXISTS `innings`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `innings` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `ball` int(11) NOT NULL,
  `batsman` int(11) NOT NULL,
  `batsman_ball` int(11) DEFAULT NULL,
  `batsman_runs` int(11) DEFAULT NULL,
  `bowler` int(11) NOT NULL,
  `bowler_ball` int(11) DEFAULT NULL,
  `bowler_runs` int(11) DEFAULT NULL,
  `extra_run` int(11) DEFAULT NULL,
  `extra_type` int(11) DEFAULT NULL,
  `fielder1` int(11) DEFAULT NULL,
  `fielder2` int(11) DEFAULT NULL,
  `innings` int(11) NOT NULL,
  `match_id` int(11) DEFAULT NULL,
  `non_boundary` bit(1) DEFAULT NULL,
  `non_striker` int(11) DEFAULT NULL,
  `overs` int(11) NOT NULL,
  `runs_total` int(11) DEFAULT NULL,
  `team_id` int(11) DEFAULT NULL,
  `wicket_player` int(11) DEFAULT NULL,
  `wicket_type` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=23 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_0900_ai_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
/*!50003 CREATE*/ /*!50017 DEFINER=`root`@`localhost`*/ /*!50003 TRIGGER `innings_AFTER_INSERT` AFTER INSERT ON `innings` FOR EACH ROW BEGIN

  -- variable for batting scorecard declarations
  DECLARE vBatOnes tinyint;
  DECLARE vBatTwos tinyint;
  DECLARE vBatThrees tinyint;
  DECLARE vBatFours tinyint;
  DECLARE vBatFives tinyint;
  DECLARE vBatSixs tinyint;
  DECLARE vBatSevens tinyint;
  DECLARE vBatDotBalls tinyint;
  DECLARE vBatting tinyint;

  -- variable for bowling scorecard 
  DECLARE vBowlDotBalls tinyint;
  DECLARE vBowlMaidens tinyint;
  DECLARE vBowlCurrentOverRuns tinyint;
  DECLARE vBowlNoBalls tinyint;
  DECLARE vBowlOver tinyint;
  DECLARE vBowlRuns tinyint;
  DECLARE vBowlWicket tinyint;
  DECLARE vBowlWides tinyint;

  -- variable for total scorecard
  DECLARE vTotByes tinyint;
  DECLARE vTotExtraRuns tinyint;
  DECLARE vTotLegByes tinyint;
  DECLARE vTotNoBalls tinyint;
  DECLARE vTotOvers tinyint;
  DECLARE vTotPenalty tinyint;
  DECLARE vTotRuns tinyint;
  DECLARE vTotWickets tinyint;
  DECLARE vTotWides tinyint;

  -- if wicket then checking who was out and updating the corresponding data
  DECLARE vBatWkType tinyint;
  DECLARE vBatBowlerId tinyint;
  DECLARE vBatFielder1 tinyint;
  DECLARE vBatFielder2 tinyint;

  DECLARE vRuns tinyint;
  DECLARE vWicket tinyint;
  DECLARE vTotalRuns integer;
  DECLARE vCurrentBall tinyint;
  DECLARE vOvers tinyint;
  DECLARE vBalls tinyint;
  DECLARE isBowling bit;

  -- ----------------------------------------------------------------------------------
  -- Processing Batting Scorecard
  -- ----------------------------------------------------------------------------------
  SET vBatOnes = 0;
  SET vBatTwos = 0;
  SET vBatThrees = 0;
  SET vBatFours = 0;
  SET vBatFives = 0;
  SET vBatSixs = 0;
  SET vBatSevens = 0;
  SET vBatDotBalls = 0;

  -- Setting total score variables
  SET vTotWides = 0;
  SET vTotNoBalls = 0;
  SET vTotLegByes = 0;
  SET vTotByes = 0;
  SET vTotPenalty = 0;

  -- Checking the runs scored to update corresponding type
  CASE
    WHEN NEW.batsman_runs=1 THEN SET vBatOnes = 1;
    WHEN NEW.batsman_runs=2 THEN SET vBatTwos = 1;
    WHEN NEW.batsman_runs=3 THEN SET vBatThrees = 1;
    WHEN NEW.batsman_runs=4 THEN SET vBatFours = 1;
    WHEN NEW.batsman_runs=5 THEN SET vBatFives = 1;
    WHEN NEW.batsman_runs=6 THEN SET vBatSixs = 1;
    WHEN NEW.batsman_runs=7 THEN SET vBatSevens = 1;
    WHEN (NEW.batsman_runs=0 && NEW.batsman_ball <> 0) THEN SET vBatDotBalls = 1;
    ELSE BEGIN END;
  END CASE;

  SET vWicket = 0;
  SET vBowlWicket = 0;
  SET vBatting = 1;
  IF (NEW.wicket_type IS NOT NULL) THEN
    SET vWicket = 1;

    IF(NEW.wicket_type <> 5) THEN
        SET vBowlWicket = 1;
    END IF;

    -- checking if the batsman is out. then batting flag is set to 0
    IF (NEW.batsman = NEW.wicket_player) THEN
      SET vBatting = 0;
    END IF;

    -- SET vBatting = 0;
    -- TODO: Check whether this can be single update with the below update
    UPDATE batsman_scorecard
    SET
      batting = 0,
      wicket_type = NEW.wicket_type,
      bowler_id = NEW.bowler,
      fielder1_id = NEW.fielder1,
      fielder2_id = NEW.fielder2
    WHERE
      match_id = NEW.match_id and team_id = NEW.team_id and batsman_id = NEW.wicket_player;
  END IF;

  -- logic to update the non-striker as striker if runs scored
  -- by batsnam / bye / leg byes
  -- updating the runner to batsman details
  CASE
    WHEN NEW.extra_type in (0, 1) THEN SET vRuns = NEW.extra_run;
    WHEN NEW.extra_type in (5, 6, 7) THEN SET vRuns = NEW.extra_run - 1;
    ELSE SET vRuns = 0;
  END CASE;

  -- NOTE: In-case of wickets we will no be switching the players
  -- we will initiate the request from UI.
  IF ((NEW.batsman_runs + vRuns)%2 <> 0 && NEW.wicket_type IS NULL) THEN
    IF (vBatting <> 0) THEN
        SET vBatting = 2;
    END IF;

    UPDATE batsman_scorecard
    SET
      batting = 1
    WHERE
      match_id = NEW.match_id and team_id = NEW.team_id and batting = 2;
  END IF;

  -- Update query
  UPDATE batsman_scorecard
  SET
    runs = runs + NEW.batsman_runs,
    balls = balls + NEW.batsman_ball,
    ones = ones + vBatOnes,
    twos = twos + vBatTwos,
    threes = threes + vBatThrees,
    fours = fours + vBatFours,
    fives = fives + vBatFives,
    sixes = sixes + vBatSixs,
    sevens = sevens + vBatSevens,
    dot_balls = dot_balls + vBatDotBalls,
    batting = vBatting
  WHERE
    match_id = NEW.match_id and team_id = NEW.team_id and batsman_id = NEW.batsman;
  -- ----------------------------------------------------------------------------------

  -- ----------------------------------------------------------------------------------
  -- Processing Batting Scorecard
  -- ----------------------------------------------------------------------------------
  -- calcaulating dot balls
  SET vBowlDotBalls = 0;
  IF (NEW.bowler_runs = 0) THEN
    SET vBowlDotBalls = 1;
  END IF;

  -- logic for adding wides & no-ball
  SET vBowlNoBalls = 0;
  SET vBowlWides = 0;
  CASE
    WHEN NEW.extra_type in (4, 5) THEN SET vBowlWides = NEW.extra_run;
    WHEN NEW.extra_type in (2, 6) THEN SET vBowlNoBalls = NEW.extra_run;
    WHEN NEW.extra_type in (7) THEN SET vBowlNoBalls = 1;
    ELSE SET vRuns = 0;
  END CASE;

  -- Logic for maidens over
  -- if balls = 6 and sum of bowler runs in that over is 0
  -- Check if Ball == 6
  -- Assumption ball increment is maintained in client-end too
  SET vBowlMaidens = 0;
  SET vOvers = 0;
  SET vBalls = NEW.ball;
  SET isBowling = b'1';
  IF (NEW.ball = 6 && NEW.bowler_ball <> 0) THEN
    -- incrementing the overs
    SET vBalls = 0;
    SET vOvers = 1;
    SET isBowling = b'0';

    -- Calculating the maiden login
    SET vBowlCurrentOverRuns = (SELECT SUM(bowler_runs) from innings where match_id = NEW.match_id and bowler = NEW.bowler and overs = NEW.overs);
    IF (vBowlCurrentOverRuns = 0) THEN
      SET vBowlMaidens = 1;
    END IF;

    -- PLAYER switching logic
    UPDATE batsman_scorecard
      SET batting = CASE WHEN batting = 2 THEN 1 ELSE ( IF(batting = 1, 2, batting) ) END
    WHERE
      match_id = NEW.match_id and team_id = NEW.team_id;

  ELSEIF (NEW.ball <= 6 && NEW.bowler_ball = 0) THEN
    SET vBalls = vBalls - 1;
  END IF;

  UPDATE bowler_scorecard
  SET
    overs = overs + vOvers,
    balls = vBalls,
    maidens = maidens + vBowlMaidens,
    runs = runs + NEW.bowler_runs,
    wickets = wickets + vBowlWicket,
    dot_balls = dot_balls + vBowlDotBalls,
    wides = wides + vBowlWides,
    no_balls = no_balls + vBowlNoBalls,
    bowling = isBowling
  WHERE
    match_id = NEW.match_id and bowler_id = NEW.bowler;
  -- ----------------------------------------------------------------------------------

  -- ----------------------------------------------------------------------------------
  -- Processing total Scorecard table
  -- ----------------------------------------------------------------------------------
  -- Computing the extras
  CASE NEW.extra_type
    WHEN 0 THEN SET vTotByes = NEW.extra_run;
    WHEN 1 THEN SET vTotLegByes = NEW.extra_run;
    WHEN 2 THEN SET vTotNoBalls = NEW.extra_run;
    WHEN 3 THEN SET vTotPenalty = NEW.extra_run;
    WHEN 4 THEN SET vTotWides = NEW.extra_run;
    WHEN 5 THEN SET vTotWides = NEW.extra_run;
    WHEN 6 THEN SET vTotNoBalls = NEW.extra_run;
    WHEN 7 THEN SET vTotNoBalls = 1; SET vTotLegByes = NEW.extra_run - 1;
    ELSE BEGIN END;
  END CASE;

  -- Updating total sc
  UPDATE total_scorecard
  SET
    overs = overs + vOvers,
    balls = vBalls,
    total_runs = total_runs + NEW.runs_total,
    wides = wides + vTotWides,
    no_balls = no_balls + vTotNoBalls,
    penalty = penalty + vTotPenalty,
    byes = byes + vTotByes,
    leg_byes = leg_byes + vTotLegByes,
    extra_runs = extra_runs + NEW.extra_run,
    wickets = wickets + vWicket
  WHERE
    match_id = NEW.match_id and team_id = NEW.team_id;
  -- ----------------------------------------------------------------------------------

END */;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2021-06-15 19:32:39
