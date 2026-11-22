package dating.agilis.ads;

import dating.agilis.ads.param.FileUpload;
import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.Map;
import java.util.UUID;

/** Encodes multipart/form-data bodies. Lists become repeated {@code name[]} parts. */
final class Multipart {

  record Encoded(byte[] body, String contentType) {}

  private Multipart() {}

  static Encoded encode(Map<String, Object> fields) {
    String boundary = "agilis-" + UUID.randomUUID().toString().replace("-", "");
    ByteArrayOutputStream out = new ByteArrayOutputStream();
    for (Map.Entry<String, Object> e : fields.entrySet()) {
      Object value = e.getValue();
      if (value == null) {
        continue;
      }
      if (value instanceof Collection<?>) {
        String name = e.getKey().endsWith("[]") ? e.getKey() : e.getKey() + "[]";
        for (Object item : (Collection<?>) value) {
          if (item != null) {
            part(out, boundary, name, item);
          }
        }
      } else {
        part(out, boundary, e.getKey(), value);
      }
    }
    write(out, "--" + boundary + "--\r\n");
    return new Encoded(out.toByteArray(), "multipart/form-data; boundary=" + boundary);
  }

  private static void part(ByteArrayOutputStream out, String boundary, String name, Object value) {
    write(out, "--" + boundary + "\r\n");
    if (value instanceof FileUpload) {
      FileUpload file = (FileUpload) value;
      write(
          out,
          "Content-Disposition: form-data; name=\""
              + quote(name)
              + "\"; filename=\""
              + quote(file.getFileName())
              + "\"\r\n");
      write(out, "Content-Type: " + file.getContentType() + "\r\n\r\n");
      out.writeBytes(file.getContent());
      write(out, "\r\n");
    } else {
      write(out, "Content-Disposition: form-data; name=\"" + quote(name) + "\"\r\n\r\n");
      write(out, text(value));
      write(out, "\r\n");
    }
  }

  static String text(Object value) {
    if (value instanceof BigDecimal) {
      return ((BigDecimal) value).toPlainString();
    }
    if (value instanceof Double || value instanceof Float) {
      return BigDecimal.valueOf(((Number) value).doubleValue()).toPlainString();
    }
    return String.valueOf(value);
  }

  private static String quote(String s) {
    return s.replace("\"", "%22").replace("\r", "").replace("\n", "");
  }

  private static void write(ByteArrayOutputStream out, String s) {
    out.writeBytes(s.getBytes(StandardCharsets.UTF_8));
  }
}
