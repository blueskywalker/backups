package org.blueskywalker.flume.interceptor;

import com.google.common.collect.Lists;
import org.apache.flume.Context;
import org.apache.flume.Event;
import org.apache.flume.FlumeException;
import org.apache.flume.interceptor.Interceptor;


import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by kkim on 3/21/16.
 */
public class HelloInterceptor implements Interceptor {
    private String hostValue;
    private String hostHeader;

    public HelloInterceptor(String hostHeader) {
        this.hostHeader = hostHeader;
    }

    public void initialize() {
        try {
            hostValue= InetAddress.getLocalHost().getHostName();
        } catch (UnknownHostException e) {
            throw new FlumeException("Canot get Hostname",e);
        }
    }

    public Event intercept(Event event) {
        Map<String,String> headers = event.getHeaders();
        headers.put(hostHeader,hostValue);
        return event;
    }

    public List<Event> intercept(List<Event> list) {
        List<Event> events = new ArrayList<Event>(list.size());
        for (Event event : events) {
            Event interceptedEvent = intercept(event);
            events.add(interceptedEvent);
        }
        return events;
    }

    public void close() {

    }

    public static class Builder implements Interceptor.Builder {

        private String header;

        public Interceptor build() {
            return new HelloInterceptor(header);
        }

        public void configure(Context context) {
            header = context.getString("hostHeader");
        }
    }
}
