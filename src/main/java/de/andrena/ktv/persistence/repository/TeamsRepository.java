package de.andrena.ktv.persistence.repository;

import org.springframework.data.repository.CrudRepository;

import de.andrena.ktv.persistence.domain.RepositoryTeam;

public interface TeamsRepository extends CrudRepository<RepositoryTeam, String> {

}
