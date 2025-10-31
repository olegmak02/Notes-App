package com.notes.app.controller;

import com.notes.app.domain.MongoNote;
import com.notes.app.domain.User;
import com.notes.app.domain.enums.Tag;
import com.notes.app.dto.note.*;
import com.notes.app.mapper.NoteMapper;
import com.notes.app.repository.NoteRepository;
import org.assertj.core.api.Assertions;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MongoDBContainer;

import java.util.*;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class NoteControllerIT {

	@Autowired
	private NoteController noteController;

	@Autowired
	private NoteRepository noteRepository;

	private static MongoDBContainer mongoDBContainer = new MongoDBContainer("mongo:7.0.12");

	static {
		mongoDBContainer.start();
	}

	@DynamicPropertySource
	static void mongoProperties(DynamicPropertyRegistry registry) {
		registry.add("spring.data.mongodb.uri", mongoDBContainer::getReplicaSetUrl);
	}

	@Test
	void shouldSuccessfullyCreateNoteWithEmptyTag() {
		// GIVEN
		ObjectId userId = new ObjectId();
		populateSecurityContext(userId);

		NoteRequestDto note = NoteRequestDto.builder()
				.title("Title")
				.text("Text")
				.build();

		// WHEN
		noteController.create(note);

		// THEN
		List<NoteResponseDto> notes = noteController.findAll(0, 5, null);
		Assertions.assertThat(notes).hasSize(1);
	}

	@Test
	void shouldNotCreateNoteWithEmptyTitleAndText() {
		// GIVEN
		ObjectId userId = new ObjectId();
		populateSecurityContext(userId);

		// WHEN THEN
		assertThrows(NullPointerException.class, () -> NoteRequestDto.builder().build());
	}

	@Test
	void shouldSuccessfullyFindNoteById() {
		// GIVEN
		ObjectId userId = new ObjectId();
		populateSecurityContext(userId);

		MongoNote note = randomNote(userId.toString());

		MongoNote savedNote = noteRepository.save(note);

		// WHEN
		NoteResponseDto notes = noteController.getById(savedNote.getId().toString());

		// THEN
		Assertions.assertThat(notes.getTitle()).isEqualTo(savedNote.getTitle());
		Assertions.assertThat(notes.getCreatedDate()).isEqualTo(savedNote.getCreatedDate());
	}

	@Test
	void shouldSuccessfullyUpdateNote() {
		// GIVEN
		ObjectId userId = new ObjectId();
		populateSecurityContext(userId);

		NoteRequestDto noteDto = NoteRequestDto.builder()
				.title("Title")
				.text("Text")
				.tag(Tag.IMPORTANT)
				.build();

		NotePatchDto notePatch = NotePatchDto.builder()
				.title("New Title")
				.text("New Text")
				.tag(Tag.BUSINESS)
				.build();

		MongoNote savedNote = noteRepository.save(NoteMapper.toMongo(noteDto));

		// WHEN
		noteController.update(savedNote.getId().toString(), notePatch);

		// THEN
		MongoNote note = noteRepository.findById(savedNote.getId().toString(), userId.toString());

		Assertions.assertThat(note.getTitle()).isEqualTo(notePatch.getTitle());
		Assertions.assertThat(note.getText()).isEqualTo(notePatch.getText());
		Assertions.assertThat(note.getTag()).isEqualTo(notePatch.getTag());
	}

	@Test
	void shouldSuccessfullyDeleteNote() {
		// GIVEN
		ObjectId userId = new ObjectId();
		populateSecurityContext(userId);

		NoteRequestDto noteDto = NoteRequestDto.builder()
				.title("Title")
				.text("Text")
				.tag(Tag.IMPORTANT)
				.build();

		MongoNote savedNote = noteRepository.save(NoteMapper.toMongo(noteDto));

		MongoNote beforeDeletion = noteRepository.findById(savedNote.getId().toString(), userId.toString());

		// WHEN
		noteController.deleteById(savedNote.getId().toString());

		// THEN
		MongoNote afterDeletion = noteRepository.findById(savedNote.getId().toString(), userId.toString());

		Assertions.assertThat(beforeDeletion).isNotNull();
		Assertions.assertThat(afterDeletion).isNull();
	}

	@Test
	void shouldSuccessfullyReturnStatsOfNote() {
		// GIVEN
		ObjectId userId = new ObjectId();
		populateSecurityContext(userId);

		Map<String, Integer> expectedStats = new TreeMap<>();
		expectedStats.put("word", 1);
		expectedStats.put("text", 2);
		expectedStats.put("stats", 1);

		String noteText = "text stats word text";

		NoteRequestDto noteDto = NoteRequestDto.builder()
				.title("Title")
				.text(noteText)
				.build();

		MongoNote savedNote = noteRepository.save(NoteMapper.toMongo(noteDto));

		// WHEN
		NoteStatDto stats = noteController.getStats(savedNote.getId().toString());

		// THEN
		assertEquals(expectedStats, stats.getStats());
	}

	@Test
	void shouldSuccessfullyReturnTextNote() {
		// GIVEN
		ObjectId userId = new ObjectId();
		populateSecurityContext(userId);

		String text = "text stats word text";

		NoteRequestDto noteDto = NoteRequestDto.builder()
				.title("Title")
				.text(text)
				.build();

		MongoNote savedNote = noteRepository.save(NoteMapper.toMongo(noteDto));

		// WHEN
		NoteTextDto noteText = noteController.getNoteText(savedNote.getId().toString());

		// THEN
		assertEquals(text, noteText.getText());
	}

	@Test
	void shouldSuccessfullyReturnListOfNotesWithPagination() {
		// GIVEN
		ObjectId userId = new ObjectId();
		populateSecurityContext(userId);

		for (int i = 0; i < 10; i++) {
			noteRepository.save(randomNote(userId.toString()));
		}

		// WHEN
		List<NoteResponseDto> notes = noteController.findAll(1, 5, null);

		// THEN
		Assertions.assertThat(notes).hasSize(5);
	}

	@Test
	void shouldSuccessfullyReturnListOfNotesFilteredByTag() {
		// GIVEN
		ObjectId userId = new ObjectId();
		populateSecurityContext(userId);

		MongoNote note1 = randomNote(userId.toString());
		MongoNote note2 = randomNote(userId.toString());
		MongoNote note3 = randomNote(userId.toString());

		note1.setTag(Tag.IMPORTANT);
		note2.setTag(Tag.BUSINESS);
		note3.setTag(Tag.PERSONAL);

		noteRepository.save(note1);
		noteRepository.save(note2);
		noteRepository.save(note3);

		// WHEN
		List<NoteResponseDto> notes = noteController.findAll(0, 5, Tag.BUSINESS);

		// THEN
		Assertions.assertThat(notes).hasSize(1);
	}

	@Test
	void shouldSuccessfullyReturnListOfNotesSortedByCreatedDate() {
		// GIVEN
		ObjectId userId = new ObjectId();
		populateSecurityContext(userId);

		for (int i = 0; i < 10; i++) {
			noteRepository.save(randomNote(userId.toString()));
		}

		// WHEN
		List<NoteResponseDto> notes = noteController.findAll(1, 5, null);

		// THEN
		boolean isSorted =
				IntStream.range(1, notes.size())
						.noneMatch(i -> notes.get(i - 1).getCreatedDate().before(notes.get(i).getCreatedDate()));

        assertTrue(isSorted, "All notes should be sorted in descending order by creation date");
	}

	private MongoNote randomNote(String userId) {
		return MongoNote.builder()
				.authorId(userId)
				.title(randomAlphabetic(10))
				.text(randomAlphabetic(15))
				.createdDate(new Date())
				.build();
	}

	private String randomAlphabetic(int length) {
		Random random = new Random();
		return random.ints('a', 'z' + 1)
				.limit(length)
				.collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
				.toString();
	}

	private void populateSecurityContext(ObjectId userId) {
		UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
				User.builder().id(userId).build(),
				null,
				List.of()
		);

		SecurityContextHolder.getContext().setAuthentication(authToken);
	}
}
