package de.andrena.ktv.rest.controller;

import static de.andrena.ktv.core.fixtures.Fixtures.STANDARD_KEY;
import static de.andrena.ktv.core.fixtures.Fixtures.STANDARD_KEY_STRING;
import static de.andrena.ktv.core.fixtures.Fixtures.standardTeamDetailsWithoutKey;
import static de.andrena.ktv.rest.constants.Constants.URL_PART_TEAMS;
import static de.andrena.ktv.rest.fixtures.RestDataFixture.standardTeamJSONWithKey;
import static de.andrena.ktv.rest.fixtures.RestEventFixtures.teamUpdatedEvent;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import de.andrena.ktv.core.events.teams.UpdateTeamEvent;
import de.andrena.ktv.core.events.teams.UpdatedTeamEvent;
import de.andrena.ktv.core.services.TeamService;

public class UpdateTeamIntegrationTest {

	@Mock
	TeamService teamService;

	@InjectMocks
	TeamCommandsController controller;

	private MockMvc mockMvc;

	@Before
	public void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);
		this.mockMvc = MockMvcBuilders.standaloneSetup(this.controller)//
				.setMessageConverters(new MappingJackson2HttpMessageConverter()).build();
	}

	@Test
	public void thatUpdateTeamUsesHttpCreated() throws Exception {
		when(this.teamService.updateTeam(Mockito.any(UpdateTeamEvent.class)))//
				.thenReturn(teamUpdatedEvent(STANDARD_KEY));

		this.mockMvc.perform(put(URL_PART_TEAMS + "/" + STANDARD_KEY_STRING)//
				.content(standardTeamJSONWithKey())//
				.contentType(APPLICATION_JSON).accept(APPLICATION_JSON))//
				.andExpect(status().isCreated());
	}

	@Test
	public void thatUpdateTeamUsesHttpNotFound() throws Exception {
		when(this.teamService.updateTeam(Mockito.any(UpdateTeamEvent.class)))//
				.thenReturn(UpdatedTeamEvent.notFound(STANDARD_KEY, standardTeamDetailsWithoutKey()));
		this.mockMvc.perform(put(URL_PART_TEAMS + "/" + STANDARD_KEY_STRING)//
				.content(standardTeamJSONWithKey())//
				.contentType(APPLICATION_JSON).accept(APPLICATION_JSON))//
				.andExpect(status().isNotFound());
	}

	@Test
	public void thatUpdateTeamUsesHttpForbidden() throws Exception {
		when(this.teamService.updateTeam(Mockito.any(UpdateTeamEvent.class)))//
				.thenReturn(UpdatedTeamEvent.updateFordbidden(STANDARD_KEY, standardTeamDetailsWithoutKey()));
		this.mockMvc.perform(put(URL_PART_TEAMS + "/" + STANDARD_KEY_STRING)//
				.content(standardTeamJSONWithKey())//
				.contentType(APPLICATION_JSON).accept(APPLICATION_JSON))//
				.andExpect(status().isForbidden());
	}

}
