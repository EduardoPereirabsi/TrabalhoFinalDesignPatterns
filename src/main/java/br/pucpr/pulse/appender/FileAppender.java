package br.pucpr.pulse.appender;

import br.pucpr.pulse.LogEvent;
import br.pucpr.pulse.format.LogFormatter;
import br.pucpr.pulse.format.PlainTextFormatter;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

public final class FileAppender extends AbstractAppender {
  private final BufferedWriter writer;

  public FileAppender(Path path) {
    this(path, new PlainTextFormatter());
  }

  public FileAppender(Path path, LogFormatter formatter) {
    super(formatter);
    try {
      final var parent = path.getParent();
      if (parent != null) {
        Files.createDirectories(parent);
      }
      this.writer =
          Files.newBufferedWriter(path, StandardOpenOption.CREATE, StandardOpenOption.APPEND);
    } catch (IOException error) {
      throw new UncheckedIOException("Nao foi possivel abrir " + path, error);
    }
  }

  @Override
  protected synchronized void write(LogEvent event, String formatted) {
    try {
      writer.write(formatted);
      writer.newLine();
      writer.flush();
    } catch (IOException error) {
      throw new UncheckedIOException(error);
    }
  }

  @Override
  public synchronized void close() {
    try {
      writer.close();
    } catch (IOException error) {
      throw new UncheckedIOException(error);
    }
  }
}
