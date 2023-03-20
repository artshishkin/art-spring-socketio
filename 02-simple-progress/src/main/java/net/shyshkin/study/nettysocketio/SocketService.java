package net.shyshkin.study.nettysocketio;

import net.shyshkin.study.nettysocketio.model.Progress;

public interface SocketService {
    //analog io.emit(eventName,mess);
    void sendProgress(Progress progress);
}
