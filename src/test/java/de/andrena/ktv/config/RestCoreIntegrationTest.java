package de.andrena.ktv.config;

import static de.andrena.ktv.core.fixtures.Fixtures.PLAYER1_ASTERIX;
import static de.andrena.ktv.core.fixtures.Fixtures.PLAYER2_OBELIX;
import static de.andrena.ktv.core.fixtures.Fixtures.TEAM_NAME_GAUL;
import static de.andrena.ktv.core.fixtures.Fixtures.TEAM_NAME_ROMAN;
import static de.andrena.ktv.rest.constants.Constants.URL_PART_TEAMS;
import static de.andrena.ktv.rest.fixtures.RestDataFixture.standardTeamJSON;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.annotation.DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import de.andrena.ktv.rest.fixtures.RestTeamBuilder;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(classes = { CoreInMemoryConfig.class, MVCConfig.class })
@DirtiesContext(classMode = AFTER_EACH_TEST_METHOD)
public class RestCoreIntegrationTest {

	@Autowired
	WebApplicationContext webApplicationContext;

	private MockMvc mockMvc;

	@Before
	public void setUp() {
		this.mockMvc = MockMvcBuilders.webAppContextSetup(this.webApplicationContext).build();
	}

	@DirtiesContext
	@Test
	public void addNewTeamtoTheSystem() throws Exception {
		this.mockMvc.perform(post(URL_PART_TEAMS).content(standardTeamJSON())//
				.contentType(APPLICATION_JSON).accept(APPLICATION_JSON))//
				.andExpect(status().isCreated());

		this.mockMvc.perform(MockMvcRequestBuilders.get(URL_PART_TEAMS)//
				.accept(APPLICATION_JSON))//
				.andExpect(status().isOk())//
				.andExpect(jsonPath("$[0].name").value(TEAM_NAME_GAUL))//
				.andExpect(jsonPath("$[0].player1").value(PLAYER1_ASTERIX))//
				.andExpect(jsonPath("$[0].player2").value(PLAYER2_OBELIX));
	}

	@DirtiesContext
	@Test
	public void addNewTeamtoTheSystemAndReadIt() throws Exception {
		MvcResult mvcResult = this.mockMvc.perform(post(URL_PART_TEAMS).content(standardTeamJSON())//
				.contentType(APPLICATION_JSON).accept(APPLICATION_JSON))//
				.andExpect(status().isCreated()).andReturn();
		String keyNewTeam = mvcResult.getResponse().getHeader("location").substring(35);

		this.mockMvc.perform(get(URL_PART_TEAMS + "/" + keyNewTeam).accept(APPLICATION_JSON))//
				.andExpect(jsonPath("$name").value(TEAM_NAME_GAUL))//
				.andExpect(jsonPath("$player1").value(PLAYER1_ASTERIX))//
				.andExpect(jsonPath("$player2").value(PLAYER2_OBELIX));
	}

	@DirtiesContext
	@Test
	public void add10NewTeamstoTheSystemAndReadThem() throws Exception {
		for (int i = 0; i < 10; i++) {
			this.mockMvc.perform(post(URL_PART_TEAMS).content(standardTeamJSON())//
					.contentType(APPLICATION_JSON).accept(APPLICATION_JSON));
		}

		this.mockMvc.perform(MockMvcRequestBuilders.get(URL_PART_TEAMS)//
				.accept(APPLICATION_JSON))//
				.andExpect(status().isOk())//
				.andExpect(jsonPath("$", Matchers.hasSize(10)));
	}

	@DirtiesContext
	@Test
	public void addNewTeamtoTheSystemAndUpdateIt() throws Exception {
		MvcResult mvcResult = this.mockMvc.perform(post(URL_PART_TEAMS).content(standardTeamJSON())//
				.contentType(APPLICATION_JSON).accept(APPLICATION_JSON))//
				.andExpect(status().isCreated()).andReturn();
		String keyNewTeam = mvcResult.getResponse().getHeader("location").substring(35);

		this.mockMvc.perform(get(URL_PART_TEAMS + "/" + keyNewTeam).accept(APPLICATION_JSON))//
				.andExpect(jsonPath("$name").value(TEAM_NAME_GAUL));

		String updatedTeam = new RestTeamBuilder()//
				.withKey(keyNewTeam)//
				.withName(TEAM_NAME_ROMAN)//
				.withPlayer1(PLAYER1_ASTERIX)//
				.withPlayer2(PLAYER2_OBELIX)//
				.createAsJSON();

		this.mockMvc.perform(put(URL_PART_TEAMS + "/" + keyNewTeam).content(updatedTeam)//
				.contentType(APPLICATION_JSON).accept(APPLICATION_JSON))//
				.andExpect(status().isCreated());//

		this.mockMvc.perform(get(URL_PART_TEAMS + "/" + keyNewTeam).accept(APPLICATION_JSON))//
				.andExpect(jsonPath("$name").value(TEAM_NAME_ROMAN));
	}

	@DirtiesContext
	@Test
	public void addNewTeamtoTheSystemAndDeleteIt() throws Exception {
		MvcResult mvcResult = this.mockMvc.perform(post(URL_PART_TEAMS).content(standardTeamJSON())//
				.contentType(APPLICATION_JSON).accept(APPLICATION_JSON))//
				.andExpect(status().isCreated()).andReturn();
		String keyNewTeam = mvcResult.getResponse().getHeader("location").substring(35);

		this.mockMvc.perform(get(URL_PART_TEAMS + "/" + keyNewTeam).accept(APPLICATION_JSON))//
				.andExpect(status().isOk());

		this.mockMvc.perform(delete(URL_PART_TEAMS + "/" + keyNewTeam))//
				.andExpect(status().isOk());

		this.mockMvc.perform(get(URL_PART_TEAMS + "/" + keyNewTeam).accept(APPLICATION_JSON))//
				.andExpect(status().isNotFound());
	}
}
