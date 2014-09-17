package de.andrena.ktv.system;

import static de.andrena.ktv.core.fixtures.Fixtures.PLAYER2_OBELIX;
import static de.andrena.ktv.core.fixtures.Fixtures.PLAYER1_ASTERIX;
import static de.andrena.ktv.core.fixtures.Fixtures.TEAM_NAME_ROMAN;
import static de.andrena.ktv.rest.fixtures.RestDataFixture.standardTeamJSON;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.hamcrest.Matchers.is;
import static org.springframework.http.HttpMethod.PUT;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.MediaType.APPLICATION_JSON;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import de.andrena.ktv.rest.domain.RestTeam;
import de.andrena.ktv.rest.fixtures.RestTeamBuilder;

public class TeamSystemTest {

	private static final String REST_URL = "http://localhost:8080/ktv/aggregators/teams/";
	private HttpHeaders headers;
	private RestTemplate restTemplate;

	@Rule
	public ExpectedException expectedException = ExpectedException.none();

	@Before
	public void setUp() {
		this.headers = new HttpHeaders();
		this.headers.setContentType(APPLICATION_JSON);
		this.headers.setAccept(Arrays.asList(APPLICATION_JSON));
		this.restTemplate = new RestTemplate();
	}

	@Test
	public void thatTeamsCanBeAdded() throws Exception {

		ResponseEntity<RestTeam> entity = this.addTeam();

		assertThat(entity.getStatusCode(), is((CREATED)));
		assertThat(this.getRespondedLocation(entity).startsWith(REST_URL.substring(21)), is(true));
	}

	@Test
	public void thatTeamsCanBeQueried() throws Exception {
		String newTeamLocation = REST_URL + this.addStandardTeamAndReturnKey();
		ResponseEntity<RestTeam> entity = this.restTemplate.getForEntity(newTeamLocation, RestTeam.class);

		assertThat(entity.getStatusCode(), is(equalTo(HttpStatus.OK)));
		assertThat(entity.getBody().getKey().toString(), is(equalTo(newTeamLocation.substring(44))));
	}

	@Test
	public void thatTeamsCanBeRetrievedAsList() throws Exception {
		List<ResponseEntity<RestTeam>> createdEntities = new ArrayList<>();
		for (int i = 0; i < 10; i++) {
			createdEntities.add(this.addTeam());
		}

		ResponseEntity<RestTeam[]> entity = this.restTemplate.getForEntity(REST_URL, RestTeam[].class);
		assertThat(entity.getStatusCode(), is(equalTo(HttpStatus.OK)));
		assertThat(entity.getBody().length, is(greaterThanOrEqualTo(10)));
	}

	@Test
	public void thatTeamsCanBeDeleted() throws Exception {
		String newTeamLocation = REST_URL + this.addStandardTeamAndReturnKey();

		assertThat(this.restTemplate.getForEntity(newTeamLocation, RestTeam.class).getStatusCode(), is(OK));
		this.restTemplate.delete(newTeamLocation);
		this.expectedException.expect(HttpClientErrorException.class);
		this.restTemplate.getForEntity(newTeamLocation, RestTeam.class);
	}

	@Test
	public void thatTeamsCanBeUpdated() throws Exception {
		String key = this.addStandardTeamAndReturnKey();
		String changedTeam = new RestTeamBuilder()//
				.withKey(key)//
				.withName(TEAM_NAME_ROMAN)//
				.withPlayer1(PLAYER2_OBELIX)//
				.withPlayer2(PLAYER1_ASTERIX)//
				.createAsJSON();

		HttpEntity<String> requestEntity = new HttpEntity<String>(changedTeam, this.headers);
		ResponseEntity<RestTeam> exchange = this.restTemplate.exchange(REST_URL + key, PUT, requestEntity, RestTeam.class);

		assertThat(exchange.getBody().getName(), is(equalTo(TEAM_NAME_ROMAN)));

		assertThat(exchange.getBody().getName(), is(equalTo(TEAM_NAME_ROMAN)));
		assertThat(this.restTemplate.getForObject(REST_URL + key, RestTeam.class).getName(), is(equalTo(TEAM_NAME_ROMAN)));

	}

	@After
	public void deleteAll() throws Exception {
		for (RestTeam team : this.restTemplate.getForObject(REST_URL, RestTeam[].class)) {
			this.delete(REST_URL + team.getKey());
		}
	}

	private String addStandardTeamAndReturnKey() {
		ResponseEntity<RestTeam> entity = this.addTeam();
		String key = this.getRespondedLocation(entity).substring(23);
		return key;
	}

	private ResponseEntity<RestTeam> addTeam() {
		HttpEntity<String> requestEntity = new HttpEntity<String>(standardTeamJSON(), this.headers);
		ResponseEntity<RestTeam> entity = this.restTemplate.postForEntity(REST_URL, requestEntity, RestTeam.class);
		return entity;
	}

	private String getRespondedLocation(ResponseEntity<RestTeam> entity) {
		return entity.getHeaders().getLocation().getPath();
	}

	private void delete(String url) {
		this.restTemplate.delete(url);
	}
}
