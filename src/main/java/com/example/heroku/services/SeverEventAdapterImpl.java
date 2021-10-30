package com.example.heroku.services;

import com.example.heroku.intf.event.SeverEventAdapterServices;
import org.springframework.stereotype.Service;
import reactor.core.publisher.ConnectableFlux;
import reactor.core.publisher.Flux;

import java.util.List;

@Service
public class SeverEventAdapterImpl implements SeverEventAdapterServices {

    interface MyEventListener<T> {
        void onDataChunk(List<T> chunk);

        void onDataChunk(T data);

        void processComplete();
    }

    MyEventListener<Object> myEventListener;


    ConnectableFlux<Object> publish = Flux.create(sink ->
            this.register(
                    new MyEventListener<Object>() {

                        public void onDataChunk(List<Object> chunk) {
                            for (Object s : chunk) {
                                sink.next(s);
                            }
                        }

                        @Override
                        public void onDataChunk(Object data) {
                            sink.next(data);
                        }

                        public void processComplete() {
                            sink.complete();
                        }
                    })
    ).publish();

    @Override
    public Flux<Object> FolkEvent(CloseStream closeStream) {
        return publish.autoConnect()
                .doOnCancel(() -> {
                    closeStream.Close();
                    System.out.println("cancel event stream!");
                });
    }

    @Override
    public void SendEvent(Object data) {
        if (this.myEventListener == null)
            return;
        this.myEventListener.onDataChunk(data);
    }

    private void register(MyEventListener<Object> eventListener) {
        this.myEventListener = eventListener;
    }
}
