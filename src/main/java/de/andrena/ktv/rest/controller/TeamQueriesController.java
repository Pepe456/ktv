package de.andrena.ktv.rest.controller;

import static de.andrena.ktv.rest.constants.Constants.URL_PART_TEAMS;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.web.bind.annotation.RequestMethod.GET;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import de.andrena.ktv.core.domain.TeamDetails;
import de.andrena.ktv.core.events.teams.ReadAllTeamDetailsEvent;
import de.andrena.ktv.core.events.teams.ReadTeamDetailsEvent;
import de.andrena.ktv.core.events.teams.ReadedTeamDetailsEvent;
import de.andrena.ktv.core.services.TeamService;
import de.andrena.ktv.rest.domain.RestTeam;

@Controller
@RequestMapping(URL_PART_TEAMS)
public class TeamQueriesController {

	@Autowired
	private TeamService teamService;

	@RequestMapping(method = GET)
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public List<RestTeam> getAllTeams() {
		List<RestTeam> teams = new ArrayList<>();
		for (TeamDetails teamDetails : this.teamService.readAllTeams(new ReadAllTeamDetailsEvent()).getTeamDetails()) {
			teams.add(RestTeam.fromTeamDetails(teamDetails));
		}
		return teams;
	}

	@RequestMapping(method = GET, value = "/{id}")
	public ResponseEntity<RestTeam> getTeam(@PathVariable String id) {
		ReadedTeamDetailsEvent detailsEvent = this.teamService.readTeamDetails(new ReadTeamDetailsEvent(UUID.fromString(id)));
		if (!detailsEvent.isEntityFound()) {
			return new ResponseEntity<RestTeam>(NOT_FOUND);
		}
		RestTeam team = RestTeam.fromTeamDetails(detailsEvent.getTeamDetails());
		return new ResponseEntity<RestTeam>(team, OK);
	}

}
