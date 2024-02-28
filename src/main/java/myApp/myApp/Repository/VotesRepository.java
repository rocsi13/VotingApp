package myApp.myApp.Repository;

import myApp.myApp.Entity.Votes;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VotesRepository extends JpaRepository <Votes, Long> {
}
