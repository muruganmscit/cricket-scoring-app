import React, { Component } from 'react';
import { Table, Button } from "reactstrap";

import AddTeam from "./AddTeam";

import { Query } from 'react-apollo'
import { Mutation } from 'react-apollo'
import gql from 'graphql-tag'

const TEAMS_QUERY = gql`
{
    GetAllTeams {
      id
      teamName
      city
    }
}
`

const DELETE_TEAM_MUTATION = gql`
  mutation DeleteTeamMutation($id: Long!) {
    DeleteTeam(teamId: $id){
        id
    }
  }
`

const ADD_TEAM_SUBSCRIPTION = gql`
  subscription {
    teamAdded {
      id
      teamName
      city
    }
  }
`

const DELETE_TEAM_SUBSCRIPTION = gql`
  subscription {
    teamDeleted {
      id
    }
  }
`

class ListTeams extends Component {

    // to get the real time updates for adding a team
    _subscribeToNewTeams = subscribeToMore => {
        subscribeToMore({
            document: ADD_TEAM_SUBSCRIPTION,
            updateQuery: (prev, { subscriptionData }) => {
                if (!subscriptionData.data) return prev
                const newTeam = subscriptionData.data.teamAdded
                const exists = prev.GetAllTeams.find(({ id }) => id === newTeam.id);
                if (exists) return prev;
                
                return Object.assign({}, prev, {
                    GetAllTeams: [...prev.GetAllTeams, newTeam],
                    count: prev.GetAllTeams.length + 1,
                    __typename: prev.GetAllTeams.__typename
                })
            }
        })
    }

    // to get the real time updates for deleting a team
    _subscribeToDeleteTeam = subscribeToMore => {
        subscribeToMore({
          document: DELETE_TEAM_SUBSCRIPTION,
          updateQuery: (prev, { subscriptionData }) => {
            if (!subscriptionData.data) return prev
            const deletedTeam = subscriptionData.data.teamDeleted;
            const newTeams = prev.GetAllTeams.filter(({ id }) => id !== deletedTeam.id);
        
            return Object.assign({}, prev, {
                GetAllTeams: [...newTeams],
                count: prev.GetAllTeams.length - 1,
                __typename: prev.GetAllTeams.__typename
            })
          }
        })
      }

    render() {
        return(
            <div>
                <AddTeam />
                <Table>
                    <thead>
                    <tr>
                        <th>#</th>
                        <th>Team Name</th>
                        <th>City</th>
                        <th>Actions</th>
                    </tr>
                    </thead>
                    <Query query={TEAMS_QUERY}>
                    {
                        ({ loading, error, data, subscribeToMore }) => {
                            if (loading) return <tr><td>Fetching</td></tr>
                            if (error) return <tr><td>{error.message}</td></tr>

                            this._subscribeToNewTeams(subscribeToMore);
                            this._subscribeToDeleteTeam(subscribeToMore);

                            const teamsToRender = data.GetAllTeams;
                            return (
                                <tbody>
                                {
                                    teamsToRender.map(
                                        team => {
                                            const id = team.id;
                                            return(
                                            <tr key={team.id}>
                                                <td>{team.id}</td>
                                                <td>{team.teamName}</td>
                                                <td>{team.city}</td>
                                                <td>
                                                <Button color="success" size="sm" className="mr-2">Edit</Button>
                                                <Mutation 
                                                    mutation={DELETE_TEAM_MUTATION} 
                                                    variables={{id}}>
                                                {deleteTeamMutation => (
                                                    <Button color="danger" size="sm" 
                                                    onClick={deleteTeamMutation}>Delete</Button>
                                                )}
                                                </Mutation>
                                                </td>
                                            </tr>);
                                        }
                                    )
                                }
                                </tbody>
                            );
                        }
                    }
                    </Query>
                </Table>
            </div>
        );
    }
}

export default ListTeams;