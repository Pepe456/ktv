package de.andrena.ktv.rest.controller;

import static de.andrena.ktv.core.fixtures.Fixtures.STANDARD_KEY;
import static de.andrena.ktv.core.fixtures.Fixtures.STANDARD_KEY_STRING;
import static de.andrena.ktv.core.fixtures.Fixtures.standardTeamDetailsWithoutKey;
import static de.andrena.ktv.rest.constants.Constants.URL_PART_TEAMS;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
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

import de.andrena.ktv.core.events.teams.DeleteTeamEvent;
import de.andrena.ktv.core.events.teams.DeletedTeamEvent;
import de.andrena.ktv.core.services.TeamService;

public class DeleteTeamIntegrationTest {
	MockMvc mockMvc;

	@InjectMocks
	TeamCommandsController controller;

	@Mock
	TeamService teamService;

	@Before
	public void setUp() {
		MockitoAnnotations.initMocks(this);
		this.mockMvc = MockMvcBuilders.standaloneSetup(this.controller).setMessageConverters(new MappingJackson2HttpMessageConverter()).build();
	}

	@Test
	public void thatDeleteTeamUsesHttpOkOnSuccess() throws Exception {

		when(this.teamService.deleteTeam(Mockito.any(DeleteTeamEvent.class)))//
				.thenReturn(new DeletedTeamEvent(STANDARD_KEY, standardTeamDetailsWithoutKey()));

		this.mockMvc.perform(delete(URL_PART_TEAMS + "/{id}", STANDARD_KEY_STRING).accept(APPLICATION_JSON))//
				.andExpect(status().isOk());
	}

	@Test
	public void thatDeleteTeamUsesHttpNotFoundOnEntityLookupFailure() throws Exception {
		when(this.teamService.deleteTeam(Mockito.any(DeleteTeamEvent.class)))//
				.thenReturn(DeletedTeamEvent.notFound(STANDARD_KEY));

		this.mockMvc.perform(delete(URL_PART_TEAMS + "/{id}", STANDARD_KEY_STRING).accept(APPLICATION_JSON))//
				.andExpect(status().isNotFound());
	}

	@Test
	public void thatDeleteTeamUsesHttpFordbiddenOnEntityDeletionFailure() throws Exception {
		when(this.teamService.deleteTeam(Mockito.any(DeleteTeamEvent.class)))//
				.thenReturn(DeletedTeamEvent.deletionForbidden(STANDARD_KEY, standardTeamDetailsWithoutKey()));

		this.mockMvc.perform(delete(URL_PART_TEAMS + "/{id}", STANDARD_KEY_STRING).accept(APPLICATION_JSON))//
				.andExpect(status().isForbidden());
	}

}
