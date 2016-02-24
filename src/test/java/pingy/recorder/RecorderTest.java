package pingy.recorder;


import org.junit.Before;
import org.junit.Test;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import pingy.TimeProvider;

import java.util.List;

import static java.util.Arrays.asList;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class RecorderTest {

    private Recorder recorder;
    private static final List<Long> timeSeq = asList(1456332663215l, 1456332685176l, 1456332697383l);

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
        recorder = new Recorder(timeProvider);
    }

    @Test
    public void pingRecordsThePingEvent() {
        recorder.ping();
        recorder.ping();
        recorder.ping();

        assertThat(recorder.getAllPings(), is(timeSeq));
    }

    @Test
    public void getAllPingsSinceReturnAllPingEventAfterATimePoint() {
        recorder.ping();
        recorder.ping();
        recorder.ping();

        assertThat(recorder.getAllPingsSince(1456332663300l), is(asList(1456332685176l, 1456332697383l)));
    }

}