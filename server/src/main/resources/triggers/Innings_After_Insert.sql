CREATE DEFINER=`root`@`localhost` TRIGGER `innings_AFTER_INSERT` AFTER INSERT ON `innings` FOR EACH ROW BEGIN

  -- variable for batting scorecard declarations
  DECLARE vBatOnes tinyint;
  DECLARE vBatTwos tinyint;
  DECLARE vBatThrees tinyint;
  DECLARE vBatFours tinyint;
  DECLARE vBatSixs tinyint;
  DECLARE vBatDotBalls tinyint;
  DECLARE vBatting tinyint;

  -- variable for bowling scorecard 
  DECLARE vBowlDotBalls tinyint;
  DECLARE vBowlMaidens tinyint;
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

  -- ----------------------------------------------------------------------------------
  -- Processing Batting Scorecard
  -- ----------------------------------------------------------------------------------
  SET vBatOnes = 0;
  SET vBatTwos = 0;
  SET vBatThrees = 0;
  SET vBatFours = 0;
  -- Need to Add 5 Runs
  SET vBatSixs = 0;
  SET vBatDotBalls = 0;

  -- Checking the runs scored to update corresponding type
  CASE NEW.batsman_runs
    WHEN 1 THEN SET vBatOnes = 1;
    WHEN 2 THEN SET vBatTwos = 1;
    WHEN 3 THEN SET vBatThrees = 1;
    WHEN 4 THEN SET vBatFours = 1;
    WHEN 6 THEN SET vBatSixs = 1;
    WHEN 0 THEN SET vBatDotBalls = 1;
  END CASE;

  SET vWicket = 0;
  IF (NEW.wicket_type IS NOT NULL) THEN
    SET vWicket = 1;
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

  IF ((NEW.batsman_runs + vRuns)%2 <> 0) THEN
    SET vBatting = 2;
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
    sixs = sixs + vBatSixs,
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

  -- TODO: Logic for maidens over
  -- if balls = 6 and sum of bowler runs in that over is 0 
  -- TODO: If ball is set to 6 have to update the over
  UPDATE bowlers_scorecard
  SET
    overs = CAST(FLOOR(overs) AS DECIMAL(4, 0)) + (NEW.bowler_ball/10.0),
    maidens = 0,
    runs = runs + NEW.bowler_runs,
    wickets = wickets + vWicket,
    dot_balls = dot_balls + vBowlDotBalls,
    wides = wides + vBowlWides,
    no_balls = no_balls + vBowlNoBalls
  WHERE 
    match_id = NEW.match_id and team_id = NEW.team_id and player_id = NEW.bowler;
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
  END CASE;

  -- Updating total sc
  -- TODO: If ball is set to 6 have to update the over
  UPDATE total_extras_scorecard
  SET
    overs = CAST(CONCAT(NEW.overs, ".", NEW.bowler_ball) AS DECIMAL(3,1)),
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
  
END