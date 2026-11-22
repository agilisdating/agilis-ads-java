package dating.agilis.ads.param;

import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Locale;
import java.util.Objects;

/**
 * A file sent in a multipart request, such as an Offer or Event image.
 *
 * <p>The content is read once, when the upload is created, so a retried request sends the same
 * bytes. The content type is detected from the file name unless one is given.
 */
public final class FileUpload {

  private final String fileName;
  private final String contentType;
  private final byte[] content;

  private FileUpload(String fileName, String contentType, byte[] content) {
    this.fileName = Objects.requireNonNull(fileName, "fileName");
    this.contentType = contentType == null ? contentTypeFor(fileName) : contentType;
    this.content = Objects.requireNonNull(content, "content");
  }

  /**
   * Reads a file from disk.
   *
   * @param path the file
   * @return the upload, named after the file
   * @throws UncheckedIOException when the file cannot be read
   */
  public static FileUpload of(Path path) {
    try {
      return new FileUpload(path.getFileName().toString(), null, Files.readAllBytes(path));
    } catch (IOException e) {
      throw new UncheckedIOException("Could not read " + path.getFileName(), e);
    }
  }

  /**
   * Uses bytes already in memory.
   *
   * @param content the file content
   * @param fileName the file name, for example {@code offer.jpg}
   * @return the upload
   */
  public static FileUpload of(byte[] content, String fileName) {
    return new FileUpload(fileName, null, content.clone());
  }

  /**
   * Uses bytes already in memory, with a given content type.
   *
   * @param content the file content
   * @param fileName the file name
   * @param contentType the content type, for example {@code image/png}
   * @return the upload
   */
  public static FileUpload of(byte[] content, String fileName, String contentType) {
    return new FileUpload(fileName, contentType, content.clone());
  }

  /**
   * Reads a stream to its end. The stream is not closed.
   *
   * @param stream the content
   * @param fileName the file name, for example {@code offer.jpg}
   * @return the upload
   * @throws UncheckedIOException when the stream cannot be read
   */
  public static FileUpload of(InputStream stream, String fileName) {
    try {
      return new FileUpload(fileName, null, stream.readAllBytes());
    } catch (IOException e) {
      throw new UncheckedIOException("Could not read " + fileName, e);
    }
  }

  /**
   * The file name sent with the content.
   *
   * @return the file name
   */
  public String getFileName() {
    return fileName;
  }

  /**
   * The content type sent with the content.
   *
   * @return the content type
   */
  public String getContentType() {
    return contentType;
  }

  /**
   * The content.
   *
   * @return a copy of the bytes
   */
  public byte[] getContent() {
    return content.clone();
  }

  /**
   * Returns the content type for a file name's extension.
   *
   * @param fileName the file name
   * @return the content type, {@code application/octet-stream} when the extension is unknown
   */
  public static String contentTypeFor(String fileName) {
    String lower = fileName.toLowerCase(Locale.ROOT);
    int dot = lower.lastIndexOf('.');
    String ext = dot < 0 ? "" : lower.substring(dot + 1);
    switch (ext) {
      case "jpg":
      case "jpeg":
        return "image/jpeg";
      case "png":
        return "image/png";
      case "gif":
        return "image/gif";
      case "webp":
        return "image/webp";
      case "heic":
        return "image/heic";
      default:
        return "application/octet-stream";
    }
  }

  @Override
  public String toString() {
    return "FileUpload[" + fileName + ", " + contentType + ", " + content.length + " bytes]";
  }
}
