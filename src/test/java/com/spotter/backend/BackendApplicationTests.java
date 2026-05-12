package com.spotter.backend;

import com.spotter.backend.category.repository.CategoryRepository;
import com.spotter.backend.friendship.repository.FriendshipRepository;
import com.spotter.backend.location.repository.LocationRepository;
import com.spotter.backend.meetup.repository.MeetupInvitationsRepository;
import com.spotter.backend.meetup.repository.MeetupParticipantsRepository;
import com.spotter.backend.meetup.repository.MeetupsRepository;
import com.spotter.backend.scrap.repository.ScrapRepository;
import com.spotter.backend.sharedpost.repository.SharedPostRepository;
import com.spotter.backend.user.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

@SpringBootTest(properties = {
	"spring.autoconfigure.exclude=org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration,"
		+ "org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration,"
		+ "org.springframework.boot.autoconfigure.flyway.FlywayAutoConfiguration,"
		+ "org.springframework.boot.autoconfigure.data.jpa.JpaRepositoriesAutoConfiguration"
})
class BackendApplicationTests {

	@MockitoBean
	UserRepository userRepository;

	@MockitoBean
	CategoryRepository categoryRepository;

	@MockitoBean
	LocationRepository locationRepository;

	@MockitoBean
	FriendshipRepository friendshipRepository;

	@MockitoBean
	MeetupsRepository meetupsRepository;

	@MockitoBean
	MeetupParticipantsRepository meetupParticipantsRepository;

	@MockitoBean
	MeetupInvitationsRepository meetupInvitationsRepository;

	@MockitoBean
	ScrapRepository scrapRepository;

	@MockitoBean
	SharedPostRepository sharedPostRepository;

	@MockitoBean
	JdbcTemplate jdbcTemplate;

	@MockitoBean
	JpaMetamodelMappingContext jpaMetamodelMappingContext;

	@Test
	void contextLoads() {
	}
}
