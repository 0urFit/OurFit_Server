package project1.OurFit.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import project1.OurFit.entity.Post;

public interface PostRepository extends JpaRepository<Post, Long> {

}
