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

    MyEventListener<String> myEventListener;


    ConnectableFlux<String> publish = Flux.<String>create(sink ->
        this.register(
                new MyEventListener<String>() {

                    public void onDataChunk(List<String> chunk) {
                        for(String s : chunk) {
                            sink.next(s);
                        }
                    }

                    @Override
                    public void onDataChunk(String data) {
                        sink.next(data);
                    }

                    public void processComplete() {
                        sink.complete();
                    }
                })
    ).publish();

    @Override
    public Flux<String> FolkEvent() {
        return publish.autoConnect();
    }

    @Override
    public void SendEvent(String data) {
        this.myEventListener.onDataChunk(data);
    }

    private void register(MyEventListener<String> eventListener){
        this.myEventListener = eventListener;
    }
}
