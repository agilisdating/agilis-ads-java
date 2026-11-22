package dating.agilis.ads;

import dating.agilis.ads.http.TransportRequest;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/** Parses the multipart bodies the client sends, for assertions. */
final class MultipartParser {

  record Part(String name, String fileName, String contentType, byte[] content) {
    String text() {
      return new String(content, StandardCharsets.UTF_8);
    }
  }

  private static final Pattern NAME = Pattern.compile("name=\"([^\"]*)\"");
  private static final Pattern FILE = Pattern.compile("filename=\"([^\"]*)\"");

  private MultipartParser() {}

  static List<Part> parse(TransportRequest request) {
    String contentType = request.header("Content-Type");
    String boundary = contentType.substring(contentType.indexOf("boundary=") + 9);
    // ISO-8859-1 keeps one char per byte, so binary content survives the round trip.
    String body = new String(request.body(), StandardCharsets.ISO_8859_1);
    List<Part> parts = new ArrayList<>();
    String delimiter = "--" + boundary;
    if (!body.endsWith(delimiter + "--\r\n")) {
      throw new AssertionError("multipart body does not end with the closing boundary");
    }
    for (String chunk : body.split(Pattern.quote(delimiter))) {
      if (chunk.isEmpty() || chunk.startsWith("--")) {
        continue;
      }
      String section = chunk.substring(2, chunk.length() - 2); // strip leading and trailing CRLF
      int split = section.indexOf("\r\n\r\n");
      String head = section.substring(0, split);
      String content = section.substring(split + 4);
      Matcher name = NAME.matcher(head);
      if (!name.find()) {
        throw new AssertionError("part without a name");
      }
      Matcher file = FILE.matcher(head);
      String fileName = file.find() ? file.group(1) : null;
      String type = null;
      for (String line : head.split("\r\n")) {
        if (line.toLowerCase().startsWith("content-type:")) {
          type = line.substring("content-type:".length()).trim();
        }
      }
      parts.add(
          new Part(name.group(1), fileName, type, content.getBytes(StandardCharsets.ISO_8859_1)));
    }
    return parts;
  }

  static List<String> values(List<Part> parts, String name) {
    return parts.stream()
        .filter(p -> p.name().equals(name))
        .map(Part::text)
        .collect(Collectors.toList());
  }

  static String value(List<Part> parts, String name) {
    List<String> values = values(parts, name);
    if (values.size() != 1) {
      throw new AssertionError("expected one part " + name + " but found " + values.size());
    }
    return values.get(0);
  }

  static List<String> names(List<Part> parts) {
    return parts.stream().map(Part::name).collect(Collectors.toList());
  }
}
