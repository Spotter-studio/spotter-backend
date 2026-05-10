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
import org.springframework.test.context.bean.override.mockito.MockitoBean;

@SpringBootTest(properties = {
	"spring.autoconfigure.exclude=org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration,"
		+ "org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration,"
		+ "org.springframework.boot.autoconfigure.flyway.FlywayAutoConfiguration,"
		+ "org.springframework.boot.autoconfigure.data.jpa.JpaRepositoriesAutoConfiguration"
})
class BackendApplicationTests {

	// @EnableJpaAuditing이 요구하는 JPA 메타모델을 mock으로 대체
	@MockitoBean JpaMetamodelMappingContext jpaMetamodelMappingContext;

	// JPA 비활성화 시 레포지토리 빈이 없으므로 전부 mock 처리
	@MockitoBean UserRepository userRepository;
	@MockitoBean FriendshipRepository friendshipRepository;
	@MockitoBean LocationRepository locationRepository;
	@MockitoBean CategoryRepository categoryRepository;
	@MockitoBean ScrapRepository scrapRepository;
	@MockitoBean SharedPostRepository sharedPostRepository;
	@MockitoBean MeetupsRepository meetupsRepository;
	@MockitoBean MeetupParticipantsRepository meetupParticipantsRepository;
	@MockitoBean MeetupInvitationsRepository meetupInvitationsRepository;

	@Test
	void contextLoads() {
	}
}
