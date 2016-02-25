package pingy.service;

import org.junit.Before;
import org.junit.Test;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import pingy.TimeProvider;

import java.util.List;

import static java.util.Arrays.asList;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class PingServiceTest {

    private static final List<Long> timeSeq = asList(1456332663215l, 1456332685176l, 1456332697383l, 1456332697388l);

    private PingService pingService;

    @Before
    public void setUp() {
        TimeProvider timeProvider = mock(TimeProvider.class);
        when(timeProvider.getCurrentTime()).thenAnswer(new Answer<Long>() {

            private int index = 0;

            @Override
            public Long answer(InvocationOnMock invocation) throws Throwable {
                return index < timeSeq.size() ? timeSeq.get(index++) : null;
            }
        });
        pingService = new PingService(timeProvider);
    }

    @Test
    public void getAllPingsSinceReturnAllPingEventAfterATimePoint() {
        pingService.addPing();
        pingService.addPing();
        pingService.addPing();

        assertThat(pingService.getPings(12500L), is(asList(1456332685176l, 1456332697383l)));
    }

}