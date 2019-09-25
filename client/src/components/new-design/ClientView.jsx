import React from "react";
import { Divider, Grid, Segment, Header, List, Image } from "semantic-ui-react";

const ClientView = () => {
  return (
    <Segment placeholder>
      <Grid columns={2} relaxed="very" stackable>
        <Grid.Column textAlign="center">
          <Image src="/images/as.jpg" size="small" circular />
          <Header as="h2" textAlign="center">
            <Header.Content>Team A</Header.Content>
            <List>
              <List.Item>
                <Image avatar src="/images/avatar/small/rachel.png" />
                <List.Content>
                  <List.Header as="a">Rachel</List.Header>
                </List.Content>
              </List.Item>
              <List.Item>
                <Image avatar src="/images/avatar/small/rachel.png" />
                <List.Content>
                  <List.Header as="a">Rachel</List.Header>
                </List.Content>
              </List.Item>
              <List.Item>
                <Image avatar src="/images/avatar/small/rachel.png" />
                <List.Content>
                  <List.Header as="a">Rachel</List.Header>
                </List.Content>
              </List.Item>
            </List>
          </Header>
        </Grid.Column>
        <Grid.Column>
          <Image src="/images/elites.jpg" size="small" circular />
          <Header as="h2" textAlign="center">
            <Header.Content>Team B</Header.Content>
            <List>
              <List.Item>
                <Image avatar src="/images/avatar/small/rachel.png" />
                <List.Content>
                  <List.Header as="a">Rachel</List.Header>
                </List.Content>
              </List.Item>
              <List.Item>
                <Image avatar src="/images/avatar/small/rachel.png" />
                <List.Content>
                  <List.Header as="a">Rachel</List.Header>
                </List.Content>
              </List.Item>
              <List.Item>
                <Image avatar src="/images/avatar/small/rachel.png" />
                <List.Content>
                  <List.Header as="a">Rachel</List.Header>
                </List.Content>
              </List.Item>
            </List>
          </Header>
        </Grid.Column>
      </Grid>
      <Divider vertical>Vs</Divider>
    </Segment>
  );
};

export default ClientView;
