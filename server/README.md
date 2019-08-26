mutation {
  CreateMatch(match: {
    city: "Phoenix"
    homeTeam: 1
    awayTeam: 2
    dates: "08-21-2019"
    venue: "Road Runner Park"
    overs: 20
    tossWinner: 0
    winningTeam: 0
    difference: 0
  }) {
    id
  }
}

{
  GetAllMatchs {
    id
    homeTeam
    awayTeam
    city
    venue
  }
}

## Adding a Team
mutation {
  CreateTeam(team: {
    teamName: "NPCC Giants"
    city: "Phoenix"
  }) {
    id
    teamName
    city
  }
}

{
	GetAllTeams {
    id
    teamName
    city
  }
}

mutation {
  CreateMatch(createMatchInput: {
    homeTeamId: 1
    awayTeamId: 2
    overs: 20
    city: "Phoenix"
    matchDate: "08-22-2019"
    venue: "Road Runner Park"
  }) {
    id
  }
}