import React, { Component } from 'react';
import { FormGroup, Label, Input, Modal, ModalHeader, ModalBody, ModalFooter, Button } from "reactstrap";

import { Mutation } from 'react-apollo'
import gql from 'graphql-tag'

const ADD_TEAM_MUTATION = gql`
  mutation AddTeamMutation($teamName: String!, $city: String!) {
    CreateTeam(team: {
        teamName: $teamName
        city: $city
      }) {
        id
      }
  }
`

class AddTeam extends Component {

    constructor(props) {
        super(props);
        this.state = {
            addTeamModal: false,
            teamName: '',
            city: '',
        };

        this.toggleNewTeam = this.toggleNewTeam.bind(this);
        this.addTeam = this.addTeam.bind(this);
    }

    toggleNewTeam() {
        this.setState(prevState => ({
            addTeamModal: !prevState.addTeamModal
        }));
    }

    addTeam() {
        this.setState(prevState => ({
            addTeamModal: !prevState.addTeamModal,
            teamName: '',
            city: '',
        }));
    }

    render() {
        const { teamName, city } = this.state
        return(
            <div className="container">
                <Button color="primary" onClick={this.toggleNewTeam}>Add Team</Button>
                <Modal isOpen={this.state.addTeamModal} toggle={this.toggleNewTeam}>
                    <ModalHeader toggle={this.toggle}>Add New Team</ModalHeader>
                    <ModalBody>
                        <FormGroup>
                            <Label for="teamName">Team Name</Label>
                            <Input id="teamName" 
                                value={teamName}
                                onChange={(e) => {
                                    let { teamName} = this.state;
                                    teamName = e.target.value
                                    this.setState({teamName})
                                }}
                            />
                        </FormGroup>
                        <FormGroup>
                            <Label for="city">City</Label>
                            <Input id="city" 
                                value={city}
                                onChange={(e) => {
                                    let { city} = this.state;
                                    city = e.target.value
                                    this.setState({city})
                                }}
                            />
                        </FormGroup>
                    </ModalBody>
                    <ModalFooter>
                        <Mutation 
                            mutation={ADD_TEAM_MUTATION} 
                            variables={{ teamName, city }} 
                            onCompleted={this.addTeam}>
                        {addTeamMutation => (
                            <Button 
                                color="primary" 
                                onClick={addTeamMutation}
                            >Add Team</Button>
                        )}
                        </Mutation>
                        <Button color="secondary" onClick={this.toggleNewTeam}>Cancel</Button>
                    </ModalFooter>
                </Modal>
            </div>
        );
    }
}

export default AddTeam;