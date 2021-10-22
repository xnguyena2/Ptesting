package com.example.heroku.intf.event;

import reactor.core.publisher.Flux;

public interface SeverEventAdapterServices {

    interface CloseStream {
        void Close();
    }

    Flux<Object> FolkEvent(CloseStream closeStream);
    void SendEvent(Object data);
}
