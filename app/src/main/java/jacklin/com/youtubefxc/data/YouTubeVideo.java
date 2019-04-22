package jacklin.com.youtubefxc.data;

public class YouTubeVideo {

    private String id;

    private String title;

    private String channel;

    private int number_views;

    private String time;

    public YouTubeVideo(String id, String title, String channel, int number_views, String time) {
        this.id = id;
        this.title = title;
        this.channel = channel;
        this.number_views = number_views;
        this.time = time;
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getChannel() {
        return channel;
    }

    public int getNumber_views() {
        return number_views;
    }

    public String getTime() {
        return time;
    }
}
