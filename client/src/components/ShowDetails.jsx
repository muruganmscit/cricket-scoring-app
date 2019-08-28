import React, { Component } from 'react';
import { Query } from 'react-apollo'
import gql from 'graphql-tag'

const GET_TOTAL = gql`
    {
        GetTotal {
        total
        extras
        wickets
    }
}
`

const GET_TOTAL_SUBS = gql`
    subscription BallAddedSub($matchID: Int!) {
        BallAdded(matchId: $matchID) {
            total
            wickets
            extras
        }
    }
`

export default class ShowDetails extends Component {

    _subscribeToTotal = subscribeToMore => {
        subscribeToMore({
            document: GET_TOTAL_SUBS,
            variables: { matchID: this.props.match.params.id },
            updateQuery: (prev, { subscriptionData }) => {
                console.log(subscriptionData);
                if (!subscriptionData.data) return prev;
                const newTotal = subscriptionData.data.BallAdded;
                return Object.assign(
                    {}, prev, {
                        GetTotal: newTotal
                    }
                );
            }
        })
    }

    render() {
        return(
            <Query query={GET_TOTAL}>
                {
                    ({loading, error, data, subscribeToMore }) => {
                        if (loading) return <div>Loading...</div>
                        if (error) return <div>{error.message}</div>

                        this._subscribeToTotal(subscribeToMore);

                        const totalToRender = data.GetTotal;

                        return(
                            <div>
                                {totalToRender.total} / {totalToRender.wickets} ---- Extras: {totalToRender.extras}
                            </div>
                        );
                    }
                }
            </Query>
        );
    }

}
