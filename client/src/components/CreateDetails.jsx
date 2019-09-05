import React, { useState, useEffect } from 'react';
import { withApollo } from 'react-apollo';
import gql from 'graphql-tag'
import ScoreButton from './button/ScoreButton'

import { makeStyles } from '@material-ui/core/styles';

const useStyles = makeStyles({
    root: {
      flexGrow: 1,
      maxWidth: 500,
    },
});

const ADD_BALL_MUTATION = gql`
    mutation AddBallMutation($matchID: Int!, $ballDetails: String) {
        AddBall(ball: {
            matchId: $matchID
            ballDetails: $ballDetails
        }) {
            id
        }
    }
`

const ADD_LEGAL_BALL_MUTATION =gql`
  mutation AddLegalBallMutatuon(
    $innings: Int!, $matchId: Int!, 
    $teamId: Int!, $over: Int!,
    $ball: Int!, $batsman: Int!,
    $bowler: Int!, $nonStriker: Int!,
    $batsmanRuns: Int!
  ) {
    AddBall(ballInput: {
      innings: $innings
      matchId: $matchId
      teamId: $teamId
      over: $over
      ball: $ball
      batsman: $batsman
      bowler: $bowler
      nonStriker: $nonStriker
      batsmanRuns: $batsmanRuns
    }) {
      id
    }
  }
`

const CreateDetails = ({client, ...props}) => {

    const [value, setValue] = useState('');
    const [status, setStatus] = useState('');

    // some static values
    const innings = 1;
    const team = 1;
    


    const handleClick = () => {
        console.log(value);
        /*if(value !== '') {
            client.mutate({
                mutation: ADD_BALL_MUTATION,
                variables: { matchID: props.match.params.id, ballDetails: value },
            }).then(
                response => {
                    setStatus("Ball has been Added")
                }
            );
            setValue('');
        }*/
    }

    useEffect(() => {
        handleClick();
    })

    const classes = useStyles();

    return(
        <div>
            <div className="status">{status}</div>
            <ScoreButton />
            <br />
            <button className="square" onClick={() => {setValue('0')}}>
                DOT
            </button>{"  "}
            <button className="square" onClick={() => {setValue('1')}}>
                ONE
            </button>{"  "}
            <button className="square" onClick={() => {setValue('2')}}>
                TWO
            </button>{"  "}
            <button className="square" onClick={() => {setValue('3')}}>
                THREE
            </button>{"  "}
            <button className="square" onClick={() => {setValue('4B')}}>
                FOUR BOUNDARY
            </button>{"  "}
            <button className="square" onClick={() => {setValue('6B')}}>
                SIX BOUNDARY
            </button>{"  "}
            <button className="square" onClick={() => {setValue('WD')}}>
                WIDE
            </button>{"  "}
            <button className="square" onClick={() => {setValue('NB')}}>
                No Ball
            </button>{"  "}
        </div>
    );
}

export default withApollo(CreateDetails);
