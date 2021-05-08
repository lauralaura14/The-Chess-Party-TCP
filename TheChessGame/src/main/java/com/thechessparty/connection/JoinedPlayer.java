package com.thechessparty.connection;

import com.thechessparty.engine.Team;

import java.net.Socket;

public class JoinedPlayer {

    private Socket client;
    private String playerName;
    private Team team;
    private Team adversaryTeam;
    private String adversaryName;

    public JoinedPlayer(final Socket client,
                        final String playerName,
                        final Team team,
                        final String adversaryName,
                        final Team adversaryTeam) {
        this.client = client;
        this.playerName = playerName;
        this.team = team;
        this.adversaryName = adversaryName;
        this.adversaryTeam = adversaryTeam;
    }

    public String getName() {
        return this.playerName;
    }

    public Team getTeam() {
        return this.team;
    }

    public String getPlayerName() {
        return playerName;
    }

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    public void setTeam(Team team) {
        this.team = team;
    }

    public Team getAdversaryTeam() {
        return adversaryTeam;
    }

    public void setAdversaryTeam(Team adversaryTeam) {
        this.adversaryTeam = adversaryTeam;
    }

    public String getAdversaryName() {
        return adversaryName;
    }

    public void setAdversaryName(String adversaryName) {
        this.adversaryName = adversaryName;
    }
}
