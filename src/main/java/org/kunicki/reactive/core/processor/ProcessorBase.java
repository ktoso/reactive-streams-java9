package org.kunicki.reactive.core.processor;

import org.kunicki.reactive.core.subscriber.SubscriberBase;

import java.util.concurrent.Flow;
import java.util.concurrent.SubmissionPublisher;

// should IS-A processor
public abstract class ProcessorBase<In, Out> extends SubscriberBase<In> implements Flow.Publisher<Out>, Flow.Processor<In, Out> {

    private final SubmissionPublisher<Out> submissionPublisher = new SubmissionPublisher<>();

    @Override
    public void subscribe(Flow.Subscriber<? super Out> subscriber) {
        submissionPublisher.subscribe(subscriber);
    }

    @Override
    public void onError(Throwable throwable) {
        submissionPublisher.closeExceptionally(throwable);
    }

    @Override
    public void onComplete() {
        submissionPublisher.close();
    }

    void submit(Out item) {
        submissionPublisher.submit(item); // technically, can throw -- should try/catch->onError?
    }
}
