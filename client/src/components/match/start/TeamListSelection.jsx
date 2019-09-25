import React, { useState } from "react";

import { makeStyles } from "@material-ui/core/styles";

import Paper from "@material-ui/core/Paper";
import Card from "@material-ui/core/Card";
import CardHeader from "@material-ui/core/CardHeader";
import Divider from "@material-ui/core/Divider";

import List from "@material-ui/core/List";
import ListItem from "@material-ui/core/ListItem";
import ListItemText from "@material-ui/core/ListItemText";
import ListItemIcon from "@material-ui/core/ListItemIcon";
import Checkbox from "@material-ui/core/Checkbox";

const useStyles = makeStyles(theme => ({
  root: {
    margin: "auto"
  },
  paper: {
    width: 250,
    height: 300,
    overflow: "auto"
  },
  button: {
    margin: theme.spacing(0.5, 0)
  }
}));

const TeamListSelection = ({ team, setTeamPlayers }) => {
  const classes = useStyles();

  const [checked, setChecked] = useState([]);

  const handleToggle = value => () => {
    if (checked.length !== 11 || checked.indexOf(value) !== -1) {
      const currentIndex = checked.indexOf(value);
      const newChecked = [...checked];
      if (currentIndex === -1) {
        newChecked.push(value);
      } else {
        newChecked.splice(currentIndex, 1);
      }

      setChecked(newChecked);
      setTeamPlayers(newChecked);
    } else {
      alert("Already 11 players selected.");
    }
  };

  return (
    <Card>
      <CardHeader
        title={team.teamName}
        subheader={`${checked.length} / 11 selected`}
      />
      <Divider />
      <Paper className={classes.paper}>
        <List dense component="div" role="list">
          {team.players
            .sort((a, b) =>
              a.firstName.toUpperCase() + a.lastName.toUpperCase() >
              b.firstName.toUpperCase() + b.lastName.toUpperCase()
                ? 1
                : -1
            )
            .map(player => {
              const labelId = `transfer-list-item-${player.id}-label`;
              return (
                <ListItem
                  key={player.id}
                  role="listitem"
                  button
                  onClick={handleToggle(player.id)}
                >
                  <ListItemIcon>
                    <Checkbox
                      checked={checked.indexOf(player.id) !== -1}
                      tabIndex={-1}
                      disableRipple
                      inputProps={{ "aria-labelledby": labelId }}
                    />
                  </ListItemIcon>
                  <ListItemText
                    id={player.id}
                    primary={`${player.firstName} ${player.lastName}`}
                  />
                </ListItem>
              );
            })}
        </List>
      </Paper>
    </Card>
  );
};

export default TeamListSelection;
