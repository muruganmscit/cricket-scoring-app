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
  -- Need to Add 5 Runs
  SET vBatSixs = 0;
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
    WHEN NEW.batsman_runs=6 THEN SET vBatSixs = 1;
    WHEN (NEW.batsman_runs=0 && NEW.batsman_ball <> 0) THEN SET vBatDotBalls = 1;
    ELSE BEGIN END;
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

  SET vBatting = 1;
  IF ((NEW.batsman_runs + vRuns)%2 <> 0) THEN
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
    sixes = sixes + vBatSixs,
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
    wickets = wickets + vWicket,
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

END;