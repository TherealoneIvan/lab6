package bmstu;

import java.util.ArrayList;

public class ServerChangerdMsg {
    private ArrayList<String> servers;

    public ServerChangerdMsg(ArrayList<String> servers) {
        this.servers = servers;
    }

    public ArrayList<String> getServers() {
        return servers;
    }

    public void setServers(ArrayList<String> servers) {
        this.servers = servers;
    }
}
