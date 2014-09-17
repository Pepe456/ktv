package de.andrena.ktv.rest.controller;

import static de.andrena.ktv.core.fixtures.Fixtures.PLAYER2_OBELIX;
import static de.andrena.ktv.core.fixtures.Fixtures.PLAYER1_ASTERIX;
import static de.andrena.ktv.core.fixtures.Fixtures.STANDARD_KEY;
import static de.andrena.ktv.core.fixtures.Fixtures.STANDARD_KEY_STRING;
import static de.andrena.ktv.core.fixtures.Fixtures.TEAM_NAME_GAUL;
import static de.andrena.ktv.rest.constants.Constants.URL_PART_TEAMS;
import static de.andrena.ktv.rest.fixtures.RestDataFixture.standardTeamJSON;
import static de.andrena.ktv.rest.fixtures.RestEventFixtures.teamCreatedEvent;
import static org.hamcrest.Matchers.endsWith;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.web.servlet.MockMvc;

import de.andrena.ktv.core.events.teams.CreateTeamEvent;
import de.andrena.ktv.core.services.TeamService;

public class CreateNewTeamIntegrationTest {
	MockMvc mockMvc;

	@Mock
	TeamService teamService;

	@InjectMocks
	TeamCommandsController controller;

	@Before
	public void setUp() {
		MockitoAnnotations.initMocks(this);

		this.mockMvc = standaloneSetup(this.controller).setMessageConverters(new MappingJackson2HttpMessageConverter()).build();
		when(this.teamService.createTeam(Mockito.any(CreateTeamEvent.class)))//
				.thenReturn(teamCreatedEvent(STANDARD_KEY));
	}

	@Test
	public void thatCreateTeamUsesHttpCreated() throws Exception {
		this.mockMvc.perform(post(URL_PART_TEAMS).content(standardTeamJSON())//
				.contentType(APPLICATION_JSON).accept(APPLICATION_JSON))//
				.andExpect(status().isCreated());
	}

	@Test
	public void thatCreateTeamRendersAsJSON() throws Exception {
		this.mockMvc.perform(post(URL_PART_TEAMS).content(standardTeamJSON())//
				.contentType(APPLICATION_JSON).accept(APPLICATION_JSON))//
				.andExpect(jsonPath("$.name").value(TEAM_NAME_GAUL))//
				.andExpect(jsonPath("$.player1").value(PLAYER1_ASTERIX))//
				.andExpect(jsonPath("$.player2").value(PLAYER2_OBELIX));
	}

	@Test
	public void thatCreateOrderPassesLocationHeader() throws Exception {
		this.mockMvc.perform(post(URL_PART_TEAMS).content(standardTeamJSON())//
				.contentType(APPLICATION_JSON).accept(APPLICATION_JSON))//
				.andDo(print())//
				.andExpect(header().string("Location", endsWith(URL_PART_TEAMS + "/" + STANDARD_KEY_STRING)));
	}

}
