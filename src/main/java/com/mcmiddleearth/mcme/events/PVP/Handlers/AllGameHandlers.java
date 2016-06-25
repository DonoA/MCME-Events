/*
 * This file is part of MCME-Events.
 * 
 * MCME-Events is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * MCME-Events is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * m
 * You should have received a copy of the GNU General Public License
 * along with MCME-Events.  If not, see <http://www.gnu.org/licenses/>.
 * 
 * 
 */
package com.mcmiddleearth.mcme.events.PVP.Handlers;

import com.mcmiddleearth.mcme.events.Main;
import com.mcmiddleearth.mcme.events.PVP.Gamemode.BasePluginGamemode;
import com.mcmiddleearth.mcme.events.PVP.Gamemode.BasePluginGamemode.GameState;
import com.mcmiddleearth.mcme.events.PVP.maps.Map;
import com.mcmiddleearth.mcme.events.PVP.PVPCommandCore;
import com.mcmiddleearth.mcme.events.PVP.PVPCore;
import com.mcmiddleearth.mcme.events.PVP.Team;
import com.mcmiddleearth.mcme.events.Util.DBmanager;
import java.io.File;
import java.util.HashMap;
import lombok.Getter;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.event.world.WorldSaveEvent;
import org.bukkit.inventory.ItemStack;

/**
 *
 * @author Donovan <dallen@dallen.xyz>
 */
public class AllGameHandlers implements Listener{
    
    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent e){
        if(e.getEntity().getKiller() == null){
            return;
        }
        
        e.setDeathMessage(ChatHandler.getPlayerColors().get(e.getEntity().getName()) + e.getEntity().getName() + ChatColor.GRAY + " was killed by " + ChatHandler.getPlayerColors().get(e.getEntity().getKiller().getName()) + e.getEntity().getKiller().getName());
    }
    
    @EventHandler
    public void onPlayerRespawn(PlayerRespawnEvent e){
        if(PVPCommandCore.getRunningGame() != null){
            if(PVPCommandCore.getRunningGame().getGm().getPlayers().contains(e.getPlayer())){
                Map m = PVPCommandCore.getRunningGame();
                if(m != null){
                    if(m.getName().contains("HD")){
                        if(e.getPlayer().getInventory().contains(new ItemStack(Material.TNT))){
                            e.getPlayer().getInventory().remove(Material.TNT);
                            e.getPlayer().getLocation().getWorld().dropItem(e.getPlayer().getLocation(), new ItemStack(Material.TNT));
                        }
                    }
                }
            }
        }else{   
            e.setRespawnLocation(PVPCore.getSpawn());
        }
    }
    
    @EventHandler
    public void onWorldSave(WorldSaveEvent e){
        for(String mn : Map.maps.keySet()){
            Map m = Map.maps.get(mn);
            DBmanager.saveObj(m, new File(PVPCore.getSaveLoc() + Main.getFileSep() + "Maps"), mn);
        }
    }
    
    @EventHandler
    public void onPlayerMove(PlayerMoveEvent e){
        Location from = e.getFrom();
        Location to = e.getTo();
        
        if(PVPCommandCore.getRunningGame() != null){
            if(PVPCommandCore.getRunningGame().getGm().getState() == GameState.COUNTDOWN && !Team.getSpectators().contains(e.getPlayer())){
                if(from.getX() != to.getX() || from.getZ() != to.getZ()){
                    e.setTo(from);
                    return;
                }
            }
        }
    }
    
    @EventHandler
    public void onPlayerDamageByEntity(EntityDamageByEntityEvent e){
        Player damagee = null;
        Player damager = null;
        
        if(PVPCommandCore.getRunningGame() == null){
            e.setCancelled(true);
            return;
        }
        else{
            if(PVPCommandCore.getRunningGame().getGm().getState() != GameState.RUNNING){
                e.setCancelled(true);
                return;
            }
        }
        
        if(e.getEntity() instanceof Player){
            damagee = (Player) e.getEntity();
        }
        else{
            return;
        }
        
        if(e.getDamager() instanceof Player){
            damager = (Player) e.getDamager();
        }
        else if(e.getDamager() instanceof Arrow){
            if(((Arrow) e.getDamager()).getShooter() instanceof Player){
                damager =  (Player) ((Arrow) e.getDamager()).getShooter();
            }
        }
        else{
            return;
        }
        
        if(Team.areTeamMates(damagee, damager)){
            e.setCancelled(true);
        }
        
    }
    
    @EventHandler
    public void onPlayerDamage(EntityDamageEvent e){
        if(e.getEntity() instanceof Player){
            if(PVPCommandCore.getRunningGame() == null){
                e.setCancelled(true);
            }
            else if(PVPCommandCore.getRunningGame().getGm().getState() != GameState.RUNNING){
                e.setCancelled(true);
            }
        }
    }
}
