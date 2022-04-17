package org.example.kafka.wikimedia;

import com.launchdarkly.eventsource.EventHandler;
import com.launchdarkly.eventsource.MessageEvent;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WikimediaChangeHandler implements EventHandler {

    KafkaProducer<String, String> kafkaProducer;
    String topic;
    private final Logger log = LoggerFactory.getLogger(WikimediaChangeHandler.class.getSimpleName());

    public WikimediaChangeHandler(KafkaProducer<String, String> kafkaProducer, String topic) {
        this.kafkaProducer = kafkaProducer;
        this.topic = topic;
    }
    @Override
    public void onOpen() throws Exception {
        // Nothing here
    }

    @Override
    public void onClosed() {
        kafkaProducer.close();
    }

    @Override
    public void onMessage(String event, MessageEvent messageEvent) {
        this.log.info(messageEvent.getData());
        // Asynchronous
        kafkaProducer.send(new ProducerRecord<String, String>(this.topic, messageEvent.getData()));
    }

    @Override
    public void onComment(String comment) {
        // Nothing here
    }

    @Override
    public void onError(Throwable t) {
        this.log.error("Error in stream reading", t);
    }
}
