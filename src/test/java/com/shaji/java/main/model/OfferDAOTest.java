package com.shaji.java.main.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Matchers.refEq;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.test.util.ReflectionTestUtils;

public class OfferDAOTest {

	private OffersDAO offersDao;

	@Mock
	private NamedParameterJdbcTemplate jdbc;

	@Before
	public void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);
		offersDao = new OffersDAO();
		ReflectionTestUtils.setField(offersDao, "jdbc", jdbc);
	}

	@Test
	public void testGetOffers() {
		List<Offer> offers = new ArrayList<Offer>();
		offers.add(new Offer(100, "John", "john@test.com", "I can fix kitchen"));
		offers.add(new Offer(101, "Ted", "ted@test.com", "I can fix roof"));

		String sql = "select * from offers";
		when(jdbc.query(eq(sql), any(RowMapper.class))).thenReturn(offers);

		List<Offer> retOffers = offersDao.getOffers();
		assertEquals("Result -> count not matching", retOffers.size(), 2);
	}

	@Test
	public void testGetOfferById() {
		Offer offer = new Offer(100, "John", "john@test.com", "I can fix kitchen");

		MapSqlParameterSource paramMap = new MapSqlParameterSource("id", 100);
		String sql = "select * from offers where id = :id";

		when(jdbc.queryForObject(eq(sql), refEq(paramMap), any(RowMapper.class))).thenReturn(offer);

		Offer retOffer = offersDao.getOfferById(100);
		assertEquals("Result -> name not matching", retOffer.getName(), "John");
	}

	@Test
	public void testGetOfferByIdWithNonExistingId() {
		Offer offer = new Offer(100, "John", "john@test.com", "I can fix kitchen");

		MapSqlParameterSource paramMap = new MapSqlParameterSource("id", 100);
		String sql = "select * from offers where id = :id";

		when(jdbc.queryForObject(eq(sql), refEq(paramMap), any(RowMapper.class))).thenReturn(offer);

		Offer retOffer = offersDao.getOfferById(200);
		assertNull("Result -> not null when using non-existing id", retOffer);
	}

}