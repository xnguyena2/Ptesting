package com.example.heroku.intf.event;

import reactor.core.publisher.Flux;

public interface SeverEventAdapterServices {
    Flux<Object> FolkEvent();
    void SendEvent(Object data);
}
