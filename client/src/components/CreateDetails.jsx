import React, { useState, useEffect } from 'react';
import { withApollo } from 'react-apollo';
import gql from 'graphql-tag'

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

const CreateDetails = ({client, ...props}) => {

    const [value, setValue] = useState('');
    const [status, setStatus] = useState('');

    const handleClick = () => {
        if(value !== '') {
            client.mutate({
                mutation: ADD_BALL_MUTATION,
                variables: { matchID: props.match.params.id, ballDetails: value },
            }).then(
                response => {
                    setStatus("Ball has been Added")
                }
            );
            setValue('');
        }
    }

    useEffect(() => {
        handleClick();
    })

    return(
        <div>
            <div className="status">{status}</div>
            <br />
            <button className="square" onClick={() => {setValue('W')}}>W</button>{"  "}
            <button className="square" onClick={() => {setValue('N')}}>N</button>{"  "}
            <button className="square" onClick={() => {setValue('O')}}>O</button>{"  "}
            <button className="square" onClick={() => {setValue('T')}}>T</button>{"  "}
            <button className="square" onClick={() => {setValue('F')}}>F</button>{"  "}
        </div>
    );
}

export default withApollo(CreateDetails);
