package com.naskoni.orderbook.processor;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.naskoni.orderbook.processor.crypto.CryptoProcessor;
import com.naskoni.orderbook.processor.crypto.XbtUsdProcessor;
import java.util.Map;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
class OrderbookProcessorImplTest {

  @Mock
  private Map<String, CryptoProcessor> cryptoProcessorMap;

  @InjectMocks
  private OrderbookProcessorImpl underTest;

  @Test
  void processMessage_withExpectedMessageAndPresentProcessor_shouldDelegateToProcessor() {
    XbtUsdProcessor xbtUsdProcessor = mock(XbtUsdProcessor.class);
    when(cryptoProcessorMap.get(anyString())).thenReturn(xbtUsdProcessor);

    underTest.processMessage(getMessage());

    verify(cryptoProcessorMap).get(anyString());
    verify(xbtUsdProcessor).process(any());
  }

  @Test
  void processMessage_withExpectedMessageAndNotPresentProcessor_shouldNotDelegate() {
    XbtUsdProcessor xbtUsdProcessor = mock(XbtUsdProcessor.class);

    underTest.processMessage(getMessage());

    verify(cryptoProcessorMap).get(anyString());
    verify(xbtUsdProcessor, times(0)).process(any());
  }

  @Test
  void processMessage_withUnexpectedMessage_shouldNotGetProcessor() {
    underTest.processMessage("some random message");

    verify(cryptoProcessorMap, times(0)).get(anyString());
  }

  private String getMessage() {
    return """
        [
            336,
            {
                "as": [
                    [
                        "17166.00000",
                        "0.23368536",
                        "1670673760.678772"
                    ],
                    [
                        "17166.20000",
                        "0.50000000",
                        "1670673637.858095"
                    ],
                    [
                        "17167.00000",
                        "0.00646550",
                        "1670673717.081569"
                    ],
                    [
                        "17168.00000",
                        "0.01000000",
                        "1670673732.296118"
                    ],
                    [
                        "17168.70000",
                        "0.81687735",
                        "1670673757.762417"
                    ],
                    [
                        "17169.00000",
                        "0.01000000",
                        "1670673696.334452"
                    ],
                    [
                        "17169.30000",
                        "0.10196966",
                        "1670673764.020376"
                    ],
                    [
                        "17169.40000",
                        "0.05837677",
                        "1670673760.907286"
                    ],
                    [
                        "17169.50000",
                        "0.08833916",
                        "1670673759.425772"
                    ],
                    [
                        "17169.70000",
                        "0.50000000",
                        "1670673761.890153"
                    ]
                ],
                "bs": [
                    [
                        "17165.90000",
                        "1.84294467",
                        "1670673762.838517"
                    ],
                    [
                        "17165.70000",
                        "4.19334150",
                        "1670673756.113411"
                    ],
                    [
                        "17165.60000",
                        "0.07282090",
                        "1670673735.635466"
                    ],
                    [
                        "17165.30000",
                        "0.00107566",
                        "1670673711.174660"
                    ],
                    [
                        "17164.80000",
                        "0.09226056",
                        "1670673760.703780"
                    ],
                    [
                        "17164.70000",
                        "0.79255327",
                        "1670673748.658611"
                    ],
                    [
                        "17164.20000",
                        "0.48960000",
                        "1670673760.703987"
                    ],
                    [
                        "17164.10000",
                        "0.90304764",
                        "1670673732.796372"
                    ],
                    [
                        "17163.50000",
                        "2.47617551",
                        "1670673718.928039"
                    ],
                    [
                        "17162.30000",
                        "1.45231922",
                        "1670673679.854074"
                    ]
                ]
            },
            "book-10",
            "XBT/USD"
        ]
        """;
  }

}