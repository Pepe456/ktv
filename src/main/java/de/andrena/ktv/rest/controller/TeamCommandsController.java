package de.andrena.ktv.rest.controller;

import static de.andrena.ktv.rest.constants.Constants.URL_PART_TEAMS;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.web.bind.annotation.RequestMethod.DELETE;
import static org.springframework.web.bind.annotation.RequestMethod.POST;
import static org.springframework.web.bind.annotation.RequestMethod.PUT;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.util.UriComponentsBuilder;

import de.andrena.ktv.core.domain.TeamDetails;
import de.andrena.ktv.core.events.teams.CreateTeamEvent;
import de.andrena.ktv.core.events.teams.CreatedTeamEvent;
import de.andrena.ktv.core.events.teams.DeleteTeamEvent;
import de.andrena.ktv.core.events.teams.DeletedTeamEvent;
import de.andrena.ktv.core.events.teams.UpdateTeamEvent;
import de.andrena.ktv.core.events.teams.UpdatedTeamEvent;
import de.andrena.ktv.core.services.TeamService;
import de.andrena.ktv.rest.domain.RestTeam;

@Controller
@RequestMapping(URL_PART_TEAMS)
public class TeamCommandsController {

	@Autowired
	TeamService teamService;

	@RequestMapping(method = POST)
	public ResponseEntity<RestTeam> createTeam(@RequestBody RestTeam team, UriComponentsBuilder builder) {
		CreatedTeamEvent teamCreatedEvent = this.teamService.createTeam(new CreateTeamEvent(team.toTeamDetails()));
		TeamDetails teamDetails = teamCreatedEvent.getTeamDetails();
		RestTeam newTeam = RestTeam.fromTeamDetails(teamDetails);
		HttpHeaders headers = new HttpHeaders();

		headers.setLocation(builder.path(URL_PART_TEAMS + "/{id}").buildAndExpand(teamCreatedEvent.getNewTeamKey().toString()).toUri());
		return new ResponseEntity<>(newTeam, headers, HttpStatus.CREATED);
	}

	@RequestMapping(method = DELETE, value = "/{id}")
	public ResponseEntity<RestTeam> deleteTeam(@PathVariable String id) {

		DeletedTeamEvent teamDeletedEvent = this.teamService.deleteTeam(new DeleteTeamEvent(UUID.fromString(id)));

		if (!teamDeletedEvent.isEntityFound()) {
			return new ResponseEntity<>(NOT_FOUND);
		}

		if (!teamDeletedEvent.isDeletionCompleted()) {
			return new ResponseEntity<>(FORBIDDEN);
		}

		return new ResponseEntity<>(OK);
	}

	@RequestMapping(method = PUT, value = "/{id}")
	public ResponseEntity<RestTeam> updateTeam(@PathVariable String id, @RequestBody RestTeam team) {

		UpdateTeamEvent event = new UpdateTeamEvent(UUID.fromString(id), team.toTeamDetails());
		UpdatedTeamEvent updatedTeamEvent = this.teamService.updateTeam(event);

		if (updatedTeamEvent.isEntityNotFound()) {
			return new ResponseEntity<>(NOT_FOUND);
		}

		RestTeam teamUpdated = RestTeam.fromTeamDetails(updatedTeamEvent.getTeamDetails());

		if (updatedTeamEvent.isUpdateNotCompleted()) {
			return new ResponseEntity<>(teamUpdated, FORBIDDEN);
		}

		return new ResponseEntity<>(teamUpdated, CREATED);
	}
}
