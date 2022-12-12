package com.naskoni.orderbook.processor;

import static com.naskoni.orderbook.processor.Constants.PRICE;
import static com.naskoni.orderbook.processor.Constants.PRICE_BD;
import static com.naskoni.orderbook.processor.Constants.VALUE;
import static com.naskoni.orderbook.processor.Constants.VOLUME;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;
import org.junit.jupiter.api.Test;

class ValuesProcessorImplTest {
  
  private ValuesProcessor underTest = new ValuesProcessorImpl();

  @Test
  void process_whenAddingOneValue_containerShouldHaveSize1() {
    SortedMap<BigDecimal, String> container = new TreeMap<>();
    List<List<String>> values = new ArrayList<>();
    values.add(VALUE);

    underTest.process(container, values);

    assertEquals(1, container.size());
    assertTrue(container.containsKey(PRICE_BD));
    assertEquals(VOLUME, container.get(PRICE_BD));
  }

  @Test
  void process_whenAdding2EqualValues_containerShouldHaveSize1() {
    SortedMap<BigDecimal, String> container = new TreeMap<>();
    List<List<String>> values = new ArrayList<>();
    values.add(VALUE);
    values.add(VALUE);

    underTest.process(container, values);

    assertEquals(1, container.size());
    assertTrue(container.containsKey(PRICE_BD));
    assertEquals(VOLUME, container.get(PRICE_BD));
  }

  @Test
  void process_whenAdding2DiffValues_containerShouldHaveSize2() {
    SortedMap<BigDecimal, String> container = new TreeMap<>();
    List<List<String>> values = new ArrayList<>();
    values.add(VALUE);
    values.add(List.of("1266.89000", "11.21619186"));

    underTest.process(container, values);

    assertEquals(2, container.size());
    assertTrue(container.containsKey(PRICE_BD));
    assertEquals(VOLUME, container.get(PRICE_BD));
  }

  @Test
  void process_whenAddingEmptyValue_containerShouldHaveSize0() {
    SortedMap<BigDecimal, String> container = new TreeMap<>();
    List<List<String>> values = new ArrayList<>();
    values.add(new ArrayList<>());

    underTest.process(container, values);

    assertEquals(0, container.size());
  }

  @Test
  void update_whenAddingAskUpdate_asksMapShouldHaveSize1() {
    SortedMap<BigDecimal, String> asksMap = new TreeMap<>();
    SortedMap<BigDecimal, String> bidsMap = new TreeMap<>();
    Map<String, List<List<String>>> updateMessage = new HashMap<>();
    List<List<String>> values = new ArrayList<>();
    values.add(VALUE);
    updateMessage.put("a", values);

    underTest.update(asksMap, bidsMap, updateMessage);

    assertEquals(1, asksMap.size());
    assertTrue(asksMap.containsKey(PRICE_BD));
    assertEquals(VOLUME, asksMap.get(PRICE_BD));

    assertEquals(0, bidsMap.size());
  }

  @Test
  void update_whenAddingBidUpdate_bidsMapShouldHaveSize1() {
    SortedMap<BigDecimal, String> asksMap = new TreeMap<>();
    SortedMap<BigDecimal, String> bidsMap = new TreeMap<>();
    Map<String, List<List<String>>> updateMessage = new HashMap<>();
    List<List<String>> values = new ArrayList<>();
    values.add(VALUE);
    updateMessage.put("b", values);

    underTest.update(asksMap, bidsMap, updateMessage);

    assertEquals(1, bidsMap.size());
    assertTrue(bidsMap.containsKey(PRICE_BD));
    assertEquals(VOLUME, bidsMap.get(PRICE_BD));

    assertEquals(0, asksMap.size());
  }

  @Test
  void update_whenAddingAskAndBidUpdate_asksMapAndbidsMapShouldHaveSize1() {
    SortedMap<BigDecimal, String> asksMap = new TreeMap<>();
    SortedMap<BigDecimal, String> bidsMap = new TreeMap<>();
    Map<String, List<List<String>>> updateMessage = new HashMap<>();
    List<List<String>> values = new ArrayList<>();
    values.add(VALUE);
    updateMessage.put("a", values);
    updateMessage.put("b", values);

    underTest.update(asksMap, bidsMap, updateMessage);

    assertEquals(1, asksMap.size());
    assertTrue(asksMap.containsKey(PRICE_BD));
    assertEquals(VOLUME, asksMap.get(PRICE_BD));

    assertEquals(1, bidsMap.size());
    assertTrue(bidsMap.containsKey(PRICE_BD));
    assertEquals(VOLUME, bidsMap.get(PRICE_BD));
  }

  @Test
  void update_whenAddingZeroVolumeBidUpdate_bidsMapShouldHaveSize0() {
    SortedMap<BigDecimal, String> asksMap = new TreeMap<>();
    SortedMap<BigDecimal, String> bidsMap = new TreeMap<>();
    Map<String, List<List<String>>> updateMessage = new HashMap<>();
    List<List<String>> values = new ArrayList<>();
    values.add(VALUE);
    updateMessage.put("b", values);

    underTest.update(asksMap, bidsMap, updateMessage);

    assertEquals(1, bidsMap.size());
    assertEquals(0, asksMap.size());

    updateMessage = new HashMap<>();
    values = new ArrayList<>();
    values.add(List.of(PRICE, "0.0"));
    updateMessage.put("b", values);

    underTest.update(asksMap, bidsMap, updateMessage);

    assertEquals(0, bidsMap.size());
    assertEquals(0, asksMap.size());
  }

  @Test
  void update_whenAddingZeroVolumeAskUpdate_asksMapShouldHaveSize0() {
    SortedMap<BigDecimal, String> asksMap = new TreeMap<>();
    SortedMap<BigDecimal, String> bidsMap = new TreeMap<>();
    Map<String, List<List<String>>> updateMessage = new HashMap<>();
    List<List<String>> values = new ArrayList<>();
    values.add(VALUE);
    updateMessage.put("a", values);

    underTest.update(asksMap, bidsMap, updateMessage);

    assertEquals(1, asksMap.size());
    assertEquals(0, bidsMap.size());

    updateMessage = new HashMap<>();
    values = new ArrayList<>();
    values.add(List.of(PRICE, "0.0"));
    updateMessage.put("a", values);

    underTest.update(asksMap, bidsMap, updateMessage);

    assertEquals(0, asksMap.size());
    assertEquals(0, bidsMap.size());
  }

  @Test
  void update_whenAddingEmptyUpdate_containersShouldHaveSize0() {
    SortedMap<BigDecimal, String> asksMap = new TreeMap<>();
    SortedMap<BigDecimal, String> bidsMap = new TreeMap<>();
    Map<String, List<List<String>>> updateMessage = new HashMap<>();

    underTest.update(asksMap, bidsMap, updateMessage);

    assertEquals(0, asksMap.size());
    assertEquals(0, bidsMap.size());
  }
}