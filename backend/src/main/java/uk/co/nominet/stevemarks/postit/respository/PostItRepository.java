package uk.co.nominet.stevemarks.postit.respository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uk.co.nominet.stevemarks.postit.domain.PostIt;

import java.util.List;

@Repository
public interface PostItRepository extends JpaRepository<PostIt, Long> {

    List<PostIt> findAllByEmail(String email);
}
