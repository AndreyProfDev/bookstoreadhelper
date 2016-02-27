package com.andreyprofdev.protractorendtoendtestsrunner.protractor;

import java.io.IOException;
import java.util.List;

public interface Process {
    int startAndWait(List<String> commands) throws InterruptedException, IOException;

    int startAndWaitForError(List<String> commands, String errorMessage) throws IOException, InterruptedException;

    void stop();
}
