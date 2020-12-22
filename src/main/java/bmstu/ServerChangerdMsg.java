package bmstu;

public class ServerChangerdMsg {
    private String[] servers;

    public ServerChangerdMsg(String[] servers) {
        this.servers = servers;
    }

    public String[] getServers() {
        return servers;
    }

    public void setServers(String[] servers) {
        this.servers = servers;
    }
}
