package online.blueth.alerts.webhook;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

public class DiscordEmbed {

    private String title;
    private String description;
    private int color;
    private String authorName;
    private String authorIconUrl;
    private String thumbnail;
    private String footer;
    private Instant timestamp;
    private final List<Field> fields = new ArrayList<>();

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public void setAuthor(String name, String iconUrl) {
        this.authorName = name;
        this.authorIconUrl = iconUrl;
    }

    public void setThumbnail(String url) {
        this.thumbnail = url;
    }

    public void setFooter(String footer) {
        this.footer = footer;
    }

    public void setTimestamp(Instant timestamp) {
        this.timestamp = timestamp;
    }

    public void addField(String name, String value, boolean inline) {
        fields.add(new Field(name, value, inline));
    }

    public String toJson() {
        var sb = new StringBuilder();
        sb.append("{");

        sb.append("\"title\":").append(jsonStr(title));
        if (description != null) {
            sb.append(",\"description\":").append(jsonStr(description));
        }
        sb.append(",\"color\":").append(color);

        if (authorName != null) {
            sb.append(",\"author\":{\"name\":").append(jsonStr(authorName));
            if (authorIconUrl != null) {
                sb.append(",\"icon_url\":").append(jsonStr(authorIconUrl));
            }
            sb.append("}");
        }

        if (thumbnail != null) {
            sb.append(",\"thumbnail\":{\"url\":").append(jsonStr(thumbnail)).append("}");
        }

        if (footer != null) {
            sb.append(",\"footer\":{\"text\":").append(jsonStr(footer)).append("}");
        }

        if (timestamp != null) {
            sb.append(",\"timestamp\":").append(jsonStr(timestamp.toString()));
        }

        if (!fields.isEmpty()) {
            sb.append(",\"fields\":[");
            for (int i = 0; i < fields.size(); i++) {
                if (i > 0) sb.append(",");
                var f = fields.get(i);
                sb.append("{\"name\":").append(jsonStr(f.name))
                  .append(",\"value\":").append(jsonStr(f.value))
                  .append(",\"inline\":").append(f.inline)
                  .append("}");
            }
            sb.append("]");
        }

        sb.append("}");
        return sb.toString();
    }

    private String jsonStr(String value) {
        if (value == null) return "null";
        return "\"" + value.replace("\\", "\\\\")
                            .replace("\"", "\\\"")
                            .replace("\n", "\\n")
                            .replace("\r", "\\r")
                            .replace("\t", "\\t") + "\"";
    }

    private record Field(String name, String value, boolean inline) {}
}
