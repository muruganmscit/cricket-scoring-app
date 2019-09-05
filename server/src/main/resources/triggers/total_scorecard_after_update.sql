CREATE DEFINER = CURRENT_USER TRIGGER `cricket_1`.`total_scorecard_AFTER_UPDATE` AFTER UPDATE ON `total_scorecard` FOR EACH ROW
BEGIN

  -- ----------------------------------------------------------------------------------
  -- Updating the Wickets table 
  -- ----------------------------------------------------------------------------------
  IF ( NEW.wickets <> OLD.wickets) THEN
    INSERT INTO wicket_scorecard (match_id, team_id, wicket_no, runs) 
    values (NEW.match_id, NEW.team_id, NEW.wickets, NEW.total_runs);
  END IF;
  -- ----------------------------------------------------------------------------------
END;
