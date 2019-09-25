import React, { useState } from "react";
import { withRouter } from "react-router-dom";
import { makeStyles } from "@material-ui/core/styles";

import gql from "graphql-tag";
import { useApolloClient } from "@apollo/react-hooks";

import MenuItem from "@material-ui/core/MenuItem";
import FormControl from "@material-ui/core/FormControl";
import Select from "@material-ui/core/Select";
import Radio from "@material-ui/core/Radio";
import RadioGroup from "@material-ui/core/RadioGroup";
import FormControlLabel from "@material-ui/core/FormControlLabel";
import Divider from "@material-ui/core/Divider";

import Grid from "@material-ui/core/Grid";
import TeamListSelection from "./TeamListSelection";

import Button from "@material-ui/core/Button";

const useStyles = makeStyles(theme => ({
  button: {
    margin: theme.spacing(1)
  },
  input: {
    display: "none"
  }
}));

const MATCH_QUERY = gql`
  query match_details($matchId: Long!) {
    GetMatchById(matchId: $matchId) {
      venue
      overs
      city
      currentInnings
      homeTeam {
        id
        teamName
        team3LetterName
        players {
          firstName
          lastName
          nickName
          id
        }
      }
      awayTeam {
        id
        teamName
        team3LetterName
        players {
          firstName
          lastName
          nickName
          id
        }
      }
      winningTeam {
        id
        teamName
        team3LetterName
      }
    }
  }
`;

const MATCH_START_MUTATION = gql`
  mutation startMatch(
    $matchId: Long!
    $tossWinnerId: Long!
    $tossDecision: TossDecision
    $innings1Team: Long!
    $innings2Team: Long!
    $innings1Playing11: [Long]
    $innings2Playing11: [Long]
  ) {
    StartMatch(
      matchId: $matchId
      startMatchInput: {
        tossWinnerId: $tossWinnerId
        tossDecision: $tossDecision
        innings1Team: $innings1Team
        innings2Team: $innings2Team
        innings1Playing11: $innings1Playing11
        innings2Playing11: $innings2Playing11
      }
    ) {
      id
    }
  }
`;

const BATTING = "BATTING";
const FIELDING = "FIELDING";

const StartMatch = ({ ...props }) => {
  const classes = useStyles();

  // getting the match id from url
  const matchId = props.match.params.id;

  // state field for storing the match details
  const [match, setMatch] = useState();

  // state for holding the toss won team
  const [toss, setToss] = useState();

  // state for holding the toss decision
  const [tossDesision, setTossDesision] = useState(BATTING);

  // state for holding the playing 11
  const [homeTeamPlayers, setHomeTeamPlayers] = useState([]);
  const [awayTeamPlayers, setAwayTeamPlayers] = useState([]);

  // creating the apollo client
  const client = useApolloClient();

  // calling the graphql end point to get the match details
  const getMatchDetails = matchId => {
    // querying the api with match id
    client
      .query({
        query: MATCH_QUERY,
        variables: {
          matchId: matchId
        }
      })
      .then(({ loading, data, error }) => {
        // loading the match details into state
        data && setMatch(data.GetMatchById);
      });
  };

  // 1. Getting Match details with team members
  // if match is not set, then get the match details
  !match && getMatchDetails(matchId);
  //match && console.log(match); // temp printing the match details
  match && !toss && setToss(match.homeTeam.id); // init the toss with home team id

  //console.log(homeTeamPlayers);
  //console.log(awayTeamPlayers);
  // function to submit the data into server using graphql mutation
  const startMatch = () => {
    if (homeTeamPlayers.length === 11 && awayTeamPlayers.length === 11) {
      const tossLast =
        toss === match.homeTeam.id ? match.awayTeam.id : match.homeTeam.id;
      const innings1team = tossDesision === BATTING ? toss : tossLast;
      const innings2team = innings1team === toss ? tossLast : toss;
      const innings1Playing11 =
        innings1team === match.homeTeam.id ? homeTeamPlayers : awayTeamPlayers;
      const innings2Playing11 =
        innings2team === match.homeTeam.id ? homeTeamPlayers : awayTeamPlayers;

      console.log(matchId);

      client
        .mutate({
          mutation: MATCH_START_MUTATION,
          variables: {
            matchId: matchId,
            tossWinnerId: toss,
            tossDecision: tossDesision,
            innings1Team: innings1team,
            innings2Team: innings2team,
            innings1Playing11: innings1Playing11,
            innings2Playing11: innings2Playing11
          }
        })
        .then(console.log);
      props.history.push("/matches");
    } else {
      alert("Please select the playing 11.");
    }
  };

  return match ? (
    match.currentInnings === 4 ? (
      // 2. provide option to select the toss winner
      // 3. provide option to select the batting / fielding option
      // 4. list all team members to select the playing 11 for both team
      // 5. Start match button to update the status with all above details
      <div align="center">
        <Grid
          container
          spacing={2}
          justify="space-evenly"
          alignItems="center"
          border="1"
        >
          <Grid item xs={4}></Grid>
          <Grid item xs={2}>
            Home Team:{" "}
          </Grid>
          <Grid item xs={2}>
            {match.homeTeam.teamName} ({match.homeTeam.team3LetterName})
          </Grid>
          <Grid item xs={4}></Grid>
        </Grid>
        <Grid container spacing={2} justify="center" alignItems="center">
          <Grid item xs={4}></Grid>
          <Grid item xs={2}>
            Away Team:{" "}
          </Grid>
          <Grid item xs={2}>
            {match.awayTeam.teamName} ({match.awayTeam.team3LetterName})
          </Grid>
          <Grid item xs={4}></Grid>
        </Grid>
        <Grid container spacing={2} justify="center" alignItems="center">
          <Grid item xs={4}></Grid>
          <Grid item xs={2}>
            Address:{" "}
          </Grid>
          <Grid item xs={2}>
            {match.venue}, {match.city}
          </Grid>
          <Grid item xs={4}></Grid>
        </Grid>
        <br />
        <Grid container spacing={2} justify="center" alignItems="center">
          <Grid item>Team Won Toss:</Grid>
          <Grid item>
            <FormControl>
              <Select
                value={toss}
                onChange={e => setToss(e.target.value)}
                displayEmpty
                name="Toss"
              >
                <MenuItem value={match.homeTeam.id}>
                  {match.homeTeam.teamName}
                </MenuItem>
                <MenuItem value={match.awayTeam.id}>
                  {match.awayTeam.teamName}
                </MenuItem>
              </Select>
            </FormControl>
          </Grid>
        </Grid>
        <Grid container spacing={2} justify="center" alignItems="center">
          <Grid item>Team Decision:</Grid>
          <Grid item>
            <FormControl component="fieldset">
              <RadioGroup
                aria-label="tossDec"
                name="tossDec"
                value={tossDesision}
                onChange={e => setTossDesision(e.target.value)}
                row
              >
                <FormControlLabel
                  value={BATTING}
                  control={<Radio />}
                  label="BATTING"
                  labelPlacement="end"
                />
                <FormControlLabel
                  value={FIELDING}
                  control={<Radio />}
                  label="FIELDING"
                  labelPlacement="end"
                />
              </RadioGroup>
            </FormControl>
          </Grid>
        </Grid>

        <Grid container spacing={2} justify="center" alignItems="center">
          <Grid item>
            <TeamListSelection
              team={match.homeTeam}
              setTeamPlayers={setHomeTeamPlayers}
            />
          </Grid>
          <Grid item>
            <Divider orientation="vertical" />
          </Grid>
          <Grid item>
            <TeamListSelection
              team={match.awayTeam}
              setTeamPlayers={setAwayTeamPlayers}
            />
          </Grid>
        </Grid>

        <Grid container spacing={2} justify="center" alignItems="center">
          <Grid item>
            <Button
              variant="contained"
              color="primary"
              className={classes.button}
              onClick={() => startMatch()}
            >
              Start Match
            </Button>
          </Grid>
        </Grid>
      </div>
    ) : (
      <div>Match has been started already</div>
    )
  ) : (
    <div>Loading...</div>
  );
};

export default withRouter(StartMatch);
