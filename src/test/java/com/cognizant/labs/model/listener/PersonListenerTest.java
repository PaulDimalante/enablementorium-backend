package com.cognizant.labs.model.listener;

import com.cognizant.labs.model.Person;
import com.cognizant.labs.service.PublishingService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.support.membermodification.MemberModifier;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.modules.junit4.PowerMockRunner;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@PowerMockIgnore("javax.crypto.*")
@RunWith(PowerMockRunner.class)
public class PersonListenerTest {

    @Test
    public void testInvoked() throws Exception {
        //instantiate
        PersonListener listener = new PersonListener();
        Person person = new Person();
        //mock
        PublishingService service = mock(PublishingService.class);
        MemberModifier.field(PersonListener.class,"publishingService").set(listener,service);
        //execute
        listener.postUpdate(person);
        //verify invoked
        verify(service,atLeastOnce()).sendUpdate(any());
    }

}