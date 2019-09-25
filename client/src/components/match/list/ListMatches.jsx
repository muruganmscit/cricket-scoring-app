import React, { useState } from "react";

import gql from "graphql-tag";
import { useApolloClient } from "@apollo/react-hooks";

import { makeStyles } from "@material-ui/core/styles";
import Table from "@material-ui/core/Table";
import TableBody from "@material-ui/core/TableBody";
import TableCell from "@material-ui/core/TableCell";
import TableHead from "@material-ui/core/TableHead";
import TableRow from "@material-ui/core/TableRow";
import Paper from "@material-ui/core/Paper";

import Button from "@material-ui/core/Button";

const GET_ALL_MATCH_QUERY = gql`
  query {
    GetAllMatches {
      id
      homeTeam {
        id
        teamName
      }
      awayTeam {
        id
        teamName
      }
      city
      venue
      currentInnings
      dates
      winningTeam {
        id
        teamName
      }
      outcomeBy
      difference
    }
  }
`;

const useStyles = makeStyles(theme => ({
  root: {
    width: "80%",
    marginTop: theme.spacing(3),
    overflowX: "auto"
  },
  table: {
    minWidth: 650
  },
  button: {
    margin: theme.spacing(0)
  }
}));

const ListMatches = ({ ...props }) => {
  const classes = useStyles();

  // creating the apollo client
  const client = useApolloClient();

  // state to hold the list of matches
  const [matches, setMatches] = useState();

  // calling the graphql end point to get the match details
  const getAllMatches = () => {
    // querying the api with match id
    client
      .query({
        query: GET_ALL_MATCH_QUERY
      })
      .then(({ loading, data, error }) => {
        // loading the match details into state
        data && setMatches(data.GetAllMatches);
      });
  };
  !matches && getAllMatches();

  const reDirect = path => {
    console.log(path);
    props.history.push(path);
  };

  return matches ? (
    <div align="center">
      <Paper className={classes.root}>
        <Table className={classes.table}>
          <TableHead>
            <TableRow>
              <TableCell>ID</TableCell>
              <TableCell align="left">Home Team</TableCell>
              <TableCell align="left">Away Team</TableCell>
              <TableCell align="center">Date</TableCell>
              <TableCell align="left">Venue</TableCell>
              <TableCell align="left">Action</TableCell>
            </TableRow>
          </TableHead>
          <TableBody>
            {matches.map(match => (
              <TableRow key={match.id}>
                <TableCell component="th" scope="row">
                  {match.id}
                </TableCell>
                <TableCell align="left">{match.homeTeam.teamName}</TableCell>
                <TableCell align="left">{match.awayTeam.teamName}</TableCell>
                <TableCell align="center">{match.dates}</TableCell>
                <TableCell align="left">
                  {match.venue}, {match.city}
                </TableCell>
                <TableCell align="left">
                  {match.currentInnings === 4 ? (
                    <Button
                      variant="contained"
                      color="primary"
                      className={classes.button}
                      size="small"
                      onClick={() => reDirect("/start_match/" + match.id)}
                    >
                      Start Match
                    </Button>
                  ) : (
                    <Button
                      variant="contained"
                      color="secondary"
                      className={classes.button}
                      size="small"
                      onClick={() => reDirect("/scoring/" + match.id)}
                    >
                      Continue Scoring
                    </Button>
                  )}
                </TableCell>
              </TableRow>
            ))}
          </TableBody>
        </Table>
      </Paper>
    </div>
  ) : (
    <div>Loading...</div>
  );
};

export default ListMatches;
