/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mcmiddleearth.mcme.events.PVP;

import com.mcmiddleearth.mcme.events.PVP.Handlers.ChatHandler;
import java.util.ArrayList;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.Location;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;

/**
 *
 * @author Eric
 */
public class Team {
        
        @Getter
        private String name;
       
        @Getter @Setter
        private int score;
        
        @Getter
        private static ArrayList<Location> redCapturedPoints = new ArrayList<>();
        
        @Getter
        private static ArrayList<Location> blueCapturedPoints = new ArrayList<>();
        
        @Getter @Setter
        private Player bearer;
        
        @Getter @Setter
        private boolean canRespawn = true;
//        @Getters
//        private HashMap<String, Integer> Classes = new HashMap<>();
        
        @Getter
        private static ArrayList<Player> bluePlayers = new ArrayList<>();
        
        @Getter
        private static ArrayList<Player> redPlayers = new ArrayList<>();
        
        @Getter
        private static ArrayList<Player> spectators = new ArrayList<>();
        
        @Getter
        private static ArrayList<Player> infected = new ArrayList<>();
        
        @Getter
        private static ArrayList<Player> survivors = new ArrayList<>();
        
        @Getter
        private ArrayList<Player> Alive = new ArrayList<>();
        
        @Getter
        private GameMode gamemode;
        
        @Getter
        private Scoreboard board;
        
        public enum Teams {
            RED,BLUE,INFECTED,SURVIVORS,SPECTATORS;
        }
        
        public enum GameType {
            RINGBEARER,TEAM_CONQUEST,KING_OF_THE_HILL
        }
        
        public Team(GameType type){
            if(type == GameType.RINGBEARER){
                board = Bukkit.getScoreboardManager().getNewScoreboard();
                Objective objective = board.registerNewObjective("Bearer", "dummy");
                objective.setDisplaySlot(DisplaySlot.SIDEBAR);
                objective.setDisplayName("Bearer");
            }
        }
        
        public Team(){
            
        }
        
        public static void addToTeam(Player p,Teams team){
            Team.removeFromTeam(p);
            switch (team){
                case RED:
                    redPlayers.add(p);
                    ChatHandler.getPlayerPrefixes().put(p.getName(), ChatColor.RED + "Red");
                    ChatHandler.getPlayerColors().put(p.getName(), ChatColor.RED);
                    p.sendMessage(ChatColor.RED + "You are on the Red Team!");
                    
                    if(p.getName().length() < 14){
                        p.setPlayerListName(ChatColor.RED + p.getName());
                    }else{
                        String newName = p.getName().substring(0,13);
                        p.setPlayerListName(ChatColor.RED + newName);
                    }
                    
                    if(p.getGameMode() != GameMode.ADVENTURE){
                        p.setGameMode(GameMode.ADVENTURE);
                    }
                    p.setDisplayName(ChatColor.RED + p.getName());
                    break;
                case BLUE:
                    bluePlayers.add(p);
                    ChatHandler.getPlayerPrefixes().put(p.getName(), ChatColor.BLUE + "Blue");
                    ChatHandler.getPlayerColors().put(p.getName(), ChatColor.BLUE);
                    p.sendMessage(ChatColor.BLUE + "You are on the Blue Team!");
                    
                    if(p.getName().length() < 14){
                        p.setPlayerListName(ChatColor.BLUE + p.getName());
                    }else{
                        String newName = p.getName().substring(0,13);
                        p.setPlayerListName(ChatColor.BLUE + newName);
                    }
                    if(p.getGameMode() != GameMode.ADVENTURE){
                        p.setGameMode(GameMode.ADVENTURE);
                    }
                    p.setDisplayName(ChatColor.BLUE + p.getName());
                    break;
                case SPECTATORS:
                    spectators.add(p);
                    ChatHandler.getPlayerPrefixes().put(p.getName(), ChatColor.GRAY + "Spectator");
                    ChatHandler.getPlayerColors().put(p.getName(), ChatColor.GRAY);
                    p.sendMessage(ChatColor.GRAY + "You are Spectating!");
                    
                    if(p.getName().length() < 14){
                        p.setPlayerListName(ChatColor.GRAY + p.getName());
                    }else{
                        String newName = p.getName().substring(0,13);
                        p.setPlayerListName(ChatColor.GRAY + newName);
                    }
                    if(p.getGameMode() != GameMode.SPECTATOR){
                        p.setGameMode(GameMode.SPECTATOR);
                    }
                    p.setDisplayName(ChatColor.GRAY + p.getName());
                    break;
                case SURVIVORS:
                    survivors.add(p);
                    ChatHandler.getPlayerPrefixes().put(p.getName(), ChatColor.BLUE + "Survivor");
                    ChatHandler.getPlayerColors().put(p.getName(), ChatColor.BLUE);
                    p.sendMessage(ChatColor.BLUE + "You are a Survivor!");
                    
                    if(p.getName().length() < 14){
                        p.setPlayerListName(ChatColor.BLUE + p.getName());
                    }else{
                        String newName = p.getName().substring(0,13);
                        p.setPlayerListName(ChatColor.BLUE + newName);
                    }
                    if(p.getGameMode() != GameMode.ADVENTURE){
                        p.setGameMode(GameMode.ADVENTURE);
                    }
                    p.setDisplayName(ChatColor.BLUE + p.getName());
                    break;
                case INFECTED:
                    infected.add(p);
                    ChatHandler.getPlayerPrefixes().put(p.getName(), ChatColor.DARK_RED + "Infected");
                    ChatHandler.getPlayerColors().put(p.getName(), ChatColor.DARK_RED);
                    p.sendMessage(ChatColor.DARK_RED + "You are Infected!");
                    
                    p.addPotionEffect(new PotionEffect(PotionEffectType.SPEED,100000,2));
                    
                    if(p.getName().length() < 14){
                        p.setPlayerListName(ChatColor.DARK_RED + p.getName());
                    }else{
                        String newName = p.getName().substring(0,13);
                        p.setPlayerListName(ChatColor.DARK_RED + newName);
                    }
                    if(p.getGameMode() != GameMode.ADVENTURE){
                        p.setGameMode(GameMode.ADVENTURE);
                    }
                    p.setDisplayName(ChatColor.DARK_RED + p.getName());
                    break;
            }
        }
        
        public static void removeFromTeam(Player p){
            if(redPlayers.contains(p)){
                redPlayers.remove(p);
            }
            else if(bluePlayers.contains(p)){
                bluePlayers.remove(p);
            }
            else if(spectators.contains(p)){
                spectators.remove(p);
            }
            
            p.setHealth(20);
            p.setDisplayName(ChatColor.WHITE + p.getName());
            p.setPlayerListName(p.getName());
            ChatHandler.getPlayerPrefixes().remove(p);
        }
        
        //For use in RINGBEARER only
    
}
/*ChatHandler.getPlayerPrefixes().put(p.getName(), ChatColor.RED + "Red");
                    if(p.getName().length() < 14){
                        p.setPlayerListName(ChatColor.RED + p.getName());
                    }else{
                        String newName = p.getName().substring(0, 13);
                        p.setPlayerListName(ChatColor.RED + newName);
                    }*/