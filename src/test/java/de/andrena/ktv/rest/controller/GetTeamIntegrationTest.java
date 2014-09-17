package de.andrena.ktv.rest.controller;

import static de.andrena.ktv.core.fixtures.Fixtures.PLAYER2_OBELIX;
import static de.andrena.ktv.core.fixtures.Fixtures.PLAYER1_ASTERIX;
import static de.andrena.ktv.core.fixtures.Fixtures.STANDARD_KEY;
import static de.andrena.ktv.core.fixtures.Fixtures.STANDARD_KEY_STRING;
import static de.andrena.ktv.core.fixtures.Fixtures.TEAM_NAME_GAUL;
import static de.andrena.ktv.rest.constants.Constants.URL_PART_TEAMS;
import static de.andrena.ktv.rest.fixtures.RestEventFixtures.teamDetailsEvent;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.web.servlet.MockMvc;

import de.andrena.ktv.core.events.teams.ReadTeamDetailsEvent;
import de.andrena.ktv.core.events.teams.ReadedTeamDetailsEvent;
import de.andrena.ktv.core.services.TeamService;

public class GetTeamIntegrationTest {
	MockMvc mockMvc;

	@InjectMocks
	TeamQueriesController controller;

	@Mock
	TeamService teamService;

	@Before
	public void setUp() {
		MockitoAnnotations.initMocks(this);
		this.mockMvc = standaloneSetup(this.controller).setMessageConverters(new MappingJackson2HttpMessageConverter()).build();
	}

	@Test
	public void thatGetTeamUsesHttpNotFound() throws Exception {
		when(this.teamService.readTeamDetails(any(ReadTeamDetailsEvent.class))).thenReturn(ReadedTeamDetailsEvent.notFound(STANDARD_KEY));
		this.mockMvc.perform(get(URL_PART_TEAMS + "/{id}", STANDARD_KEY_STRING).accept(APPLICATION_JSON)).andExpect(status().isNotFound());
	}

	@Test
	public void thatGetTeamUsesHttpOK() throws Exception {

		when(this.teamService.readTeamDetails(any(ReadTeamDetailsEvent.class))).thenReturn(teamDetailsEvent(STANDARD_KEY));

		this.mockMvc.perform(get(URL_PART_TEAMS + "/{id}", STANDARD_KEY_STRING).accept(APPLICATION_JSON)).andExpect(status().isOk());
	}

	@Test
	public void thatGetTeamRendersCorrectly() throws Exception {

		ReadedTeamDetailsEvent teamDetailsEvent = teamDetailsEvent(STANDARD_KEY);
		when(this.teamService.readTeamDetails(any(ReadTeamDetailsEvent.class))).thenReturn(teamDetailsEvent);

		this.mockMvc.perform(get(URL_PART_TEAMS + "/{id}", STANDARD_KEY_STRING).accept(APPLICATION_JSON))//
				.andDo(print())//
				.andExpect(jsonPath("$.key").value(STANDARD_KEY_STRING))//
				.andExpect(jsonPath("$.name").value(TEAM_NAME_GAUL))//
				.andExpect(jsonPath("$.player1").value(PLAYER1_ASTERIX))//
				.andExpect(jsonPath("$.player2").value(PLAYER2_OBELIX))//
				.andReturn();
	}
}
