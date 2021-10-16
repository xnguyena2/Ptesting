package com.example.heroku.intf.event;

import reactor.core.publisher.Flux;

public interface SeverEventAdapterServices {
    Flux<String> FolkEvent();
    void SendEvent(String data);
}
