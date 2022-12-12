package com.naskoni.orderbook.processor.crypto;

import static com.naskoni.orderbook.processor.Constants.VALUE;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.naskoni.orderbook.processor.ValuesProcessor;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
class AbstractProcessorTest {

  @Mock
  private ValuesProcessor valuesProcessor;

  @InjectMocks
  private AbstractProcessor underTest = new XbtUsdProcessor();

  @Test
  void process_withEmptyMessage_shouldNotCallProcessor() {
    Map<String, List<List<String>>> message = new HashMap<>();

    underTest.process(message);

    verify(valuesProcessor, times(0)).process(any(), any());
    verify(valuesProcessor, times(0)).update(any(), any(), any());
  }

  @Test
  void process_withNotExpectedMessage_shouldNotCallProcessor() {
    Map<String, List<List<String>>> message = new HashMap<>();
    message.put("someKey", new ArrayList<>());

    underTest.process(message);

    verify(valuesProcessor, times(0)).process(any(), any());
    verify(valuesProcessor, times(0)).update(any(), any(), any());
  }

  @Test
  void process_withValidUpdate_shouldCallProcessorUpdate() {
    Map<String, List<List<String>>> updateMessage = new HashMap<>();
    List<List<String>> values = new ArrayList<>();
    values.add(VALUE);
    updateMessage.put("a", values);

    underTest.process(updateMessage);

    verify(valuesProcessor, times(0)).process(any(), any());
    verify(valuesProcessor).update(any(), any(), any());
  }

  @Test
  void process_withValidMessage_shouldCallProcessor() {
    Map<String, List<List<String>>> message = new HashMap<>();
    List<List<String>> values = new ArrayList<>();
    values.add(VALUE);
    message.put("as", values);

    underTest.process(message);

    verify(valuesProcessor, times(0)).update(any(), any(), any());
    verify(valuesProcessor, times(2)).process(any(), any());
  }

  @Test
  void process_withNull_shouldFailFast() {
    Assertions.assertThatThrownBy(() -> underTest.process(null)).isInstanceOf(NullPointerException.class);
  }
}