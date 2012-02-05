/*
 * This program is free software: you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later
 * version.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU General Public License along with
 * this program. If not, see <http://www.gnu.org/licenses/>.
 */
package handlers.admincommandhandlers;

/**
 * 
 * @author SqueezeD
 *
 */

import com.l2jserver.gameserver.ThreadPoolManager;
import com.l2jserver.gameserver.handler.IAdminCommandHandler;
import com.l2jserver.gameserver.model.actor.instance.L2PcInstance;
import com.l2jserver.gameserver.model.entity.events.DM;
import com.l2jserver.gameserver.network.serverpackets.NpcHtmlMessage;

import com.l2jserver.util.StringUtil;

public class AdminDMEngine implements IAdminCommandHandler
{
	private static final String[]	ADMIN_COMMANDS	=
													{
			"admin_dmevent",
			"admin_dmevent_name",
			"admin_dmevent_desc",
			"admin_dmevent_join_loc",
			"admin_dmevent_minlvl",
			"admin_dmevent_maxlvl",
			"admin_dmevent_npc",
			"admin_dmevent_npc_pos",
			"admin_dmevent_reward",
			"admin_dmevent_reward_amount",
			"admin_dmevent_spawnpos",
			"admin_dmevent_color",
			"admin_dmevent_join",
			"admin_dmevent_teleport",
			"admin_dmevent_start",
			"admin_dmevent_abort",
			"admin_dmevent_finish",
			"admin_dmevent_sit",
			"admin_dmevent_dump",
			"admin_dmevent_save",
			"admin_dmevent_jointime",
			"admin_dmevent_eventtime",
			"admin_dmevent_minplayers",
			"admin_dmevent_maxplayers",
			"admin_dmevent_autoevent",
			"admin_dmevent_radius",
			"admin_dmevent_load"					};

	private static final int		REQUIRED_LEVEL	= 127;

	public boolean useAdminCommand(String command, L2PcInstance activeChar)
	{
		if (command.equals("admin_dmevent"))
			showMainPage(activeChar);
		else if (command.startsWith("admin_dmevent_name "))
		{
			DM._eventName = command.substring(19);
			showMainPage(activeChar);
		}
		else if (command.startsWith("admin_dmevent_desc "))
		{
			DM._eventDesc = command.substring(19);
			showMainPage(activeChar);
		}
		else if (command.startsWith("admin_dmevent_minlvl "))
		{
			if (!DM.checkMinLevel(Integer.valueOf(command.substring(21))))
				return false;
			DM._minlvl = Integer.valueOf(command.substring(21));
			showMainPage(activeChar);
		}
		else if (command.startsWith("admin_dmevent_maxlvl "))
		{
			if (!DM.checkMaxLevel(Integer.valueOf(command.substring(21))))
				return false;
			DM._maxlvl = Integer.valueOf(command.substring(21));
			showMainPage(activeChar);
		}
		else if (command.startsWith("admin_dmevent_join_loc "))
		{
			DM._joiningLocationName = command.substring(23);
			showMainPage(activeChar);
		}
		else if (command.startsWith("admin_dmevent_npc "))
		{
			DM._npcId = Integer.valueOf(command.substring(18));
			showMainPage(activeChar);
		}
		else if (command.equals("admin_dmevent_npc_pos"))
		{
			DM.setNpcPos(activeChar);
			showMainPage(activeChar);
		}
		else if (command.startsWith("admin_dmevent_reward "))
		{
			DM._rewardId = Integer.valueOf(command.substring(21));
			showMainPage(activeChar);
		}
		else if (command.startsWith("admin_dmevent_reward_amount "))
		{
			DM._rewardAmount = Integer.valueOf(command.substring(29));
			showMainPage(activeChar);
		}
		else if (command.equals("admin_dmevent_spawnpos"))
		{
			DM.setPlayersPos(activeChar);
			showMainPage(activeChar);
		}
		else if (command.startsWith("admin_dmevent_color "))
		{
			DM._playerColors = Integer.decode("0x" + (command.substring(20))); // name/title color in client is BGR, not RGB
			showMainPage(activeChar);
		}
		else if (command.equals("admin_dmevent_join"))
		{
			DM.startJoin(activeChar);
			showMainPage(activeChar);
		}
		else if (command.equals("admin_dmevent_teleport"))
		{
			DM.teleportStart();
			showMainPage(activeChar);
		}
		else if (command.equals("admin_dmevent_start"))
		{
			DM.startEvent(activeChar);
			showMainPage(activeChar);
		}
		else if (command.equals("admin_dmevent_abort"))
		{
			activeChar.sendMessage("Aborting event");
			DM.abortEvent();
			showMainPage(activeChar);
		}
		else if (command.equals("admin_dmevent_finish"))
		{
			DM.finishEvent();
			showMainPage(activeChar);
		}
		else if (command.equals("admin_dmevent_sit"))
		{
			DM.sit();
			showMainPage(activeChar);
		}
		else if (command.equals("admin_dmevent_load"))
		{
			DM.loadData();
			showMainPage(activeChar);
		}
		else if (command.equals("admin_dmevent_save"))
		{
			DM.saveData();
			showMainPage(activeChar);
		}
		else if (command.startsWith("admin_dmevent_jointime "))
		{
			DM._joinTime = Integer.valueOf(command.substring(23));
			showMainPage(activeChar);
		}
		else if (command.startsWith("admin_dmevent_eventtime "))
		{
			DM._eventTime = Integer.valueOf(command.substring(24));
			showMainPage(activeChar);
		}
		else if (command.startsWith("admin_dmevent_minplayers "))
		{
			DM._minPlayers = Integer.valueOf(command.substring(25));
			showMainPage(activeChar);
		}
		else if (command.startsWith("admin_dmevent_maxplayers "))
		{
			DM._maxPlayers = Integer.valueOf(command.substring(25));
			showMainPage(activeChar);
		}
		else if (command.startsWith("admin_dmevent_radius "))
		{
			DM._radius = Integer.valueOf(command.substring(21));
			showMainPage(activeChar);
		}
		else if (command.equals("admin_dmevent_autoevent"))
		{
			if (!DM._started)
			{
				if (DM._joinTime > 0 && DM._eventTime > 0)
				{
					ThreadPoolManager.getInstance().scheduleGeneral(new Runnable()
					{
						public void run()
						{
							DM.autoEvent();
						}
					}, 0);
				}
				else
					activeChar.sendMessage("Wrong usege: join time or event time invalid.");
			}
			else
				activeChar.sendMessage("A DM Event has already been started.");
			showMainPage(activeChar);
		}
		
		else if (command.equals("admin_dmevent_dump"))
			DM.dumpData();

		return true;
	}

	public String[] getAdminCommandList()
	{
		return ADMIN_COMMANDS;
	}

	public boolean checkLevel(int level)
	{
		return (level >= REQUIRED_LEVEL);
	}

	public void showMainPage(L2PcInstance activeChar)
	{
		NpcHtmlMessage adminReply = new NpcHtmlMessage(5);
		final StringBuilder replyMSG = StringUtil.startAppend(1000, "<html><body>");

		replyMSG.append("<center><font color=\"LEVEL\">[dm Engine - by SqueezeD]</font></center><br><br><br>");
		replyMSG.append("<table><tr><td><edit var=\"input1\" width=\"125\"></td><td><edit var=\"input2\" width=\"125\"></td></tr></table>");
		replyMSG.append("<table border=\"0\"><tr>");
		replyMSG
				.append("<td width=\"100\"><button value=\"Name\" action=\"bypass -h admin_dmevent_name $input1\" width=90 height=15 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\"></td>");
		replyMSG
				.append("<td width=\"100\"><button value=\"Description\" action=\"bypass -h admin_dmevent_desc $input1\" width=90 height=15 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\"></td>");
		replyMSG
				.append("<td width=\"100\"><button value=\"Join Location\" action=\"bypass -h admin_dmevent_join_loc $input1\" width=90 height=15 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\"></td>");
		replyMSG.append("</tr></table><br><table><tr>");
		replyMSG.append("</tr></table><br><table><tr>");
		replyMSG
				.append("<td width=\"100\"><button value=\"Max lvl\" action=\"bypass -h admin_dmevent_maxlvl $input1\" width=90 height=15 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\"></td>");
		replyMSG
				.append("<td width=\"100\"><button value=\"Min lvl\" action=\"bypass -h admin_dmevent_minlvl $input1\" width=90 height=15 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\"></td>");
		replyMSG.append("</tr></table><br><table><tr>");
		replyMSG.append("<td width=\"100\"><button value=\"Max players\" action=\"bypass -h admin_dmevent_maxplayers $input1\" width=90 height=15 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\"></td>");
		replyMSG.append("<td width=\"100\"><button value=\"Min players\" action=\"bypass -h admin_dmevent_minplayers $input1\" width=90 height=15 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\"></td>");
		replyMSG.append("</tr></table><br><table><tr>");
		replyMSG
				.append("<td width=\"100\"><button value=\"NPC\" action=\"bypass -h admin_dmevent_npc $input1\" width=90 height=15 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\"></td>");
		replyMSG
				.append("<td width=\"100\"><button value=\"NPC Pos\" action=\"bypass -h admin_dmevent_npc_pos\" width=90 height=15 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\"></td>");
		replyMSG.append("</tr></table><br><table><tr>");
		replyMSG.append("<td width=\"100\"><button value=\"Reward\" action=\"bypass -h admin_dmevent_reward $input1\" width=90 height=15 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\"></td>");
		replyMSG.append("<td width=\"100\"><button value=\"Reward Amount\" action=\"bypass -h admin_dmevent_reward_amount $input1\" width=90 height=15 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\"></td>");
		replyMSG.append("</tr></table><br><table><tr>");
		replyMSG.append("<td width=\"100\"><button value=\"Join Time\" action=\"bypass -h admin_dmevent_jointime $input1\" width=90 height=15 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\"></td>");
		replyMSG.append("<td width=\"100\"><button value=\"Event Time\" action=\"bypass -h admin_dmevent_eventtime $input1\" width=90 height=15 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\"></td>");
		replyMSG.append("</tr></table><br><table><tr>");
		replyMSG
				.append("<td width=\"100\"><button value=\"DM Color\" action=\"bypass -h admin_dmevent_color $input1\" width=90 height=15 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\"></td>");
		replyMSG.append("<td width=\"100\"><button value=\"Spawn Pos\" action=\"bypass -h admin_dmevent_spawnpos\" width=90 height=15 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\"></td>");
		replyMSG.append("<td width=\"100\"><button value=\"Radius\" action=\"bypass -h admin_dmevent_radius\" width=90 height=15 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\"></td>");
		replyMSG.append("</tr></table><table><br><br><tr>");
		replyMSG
				.append("<td width=\"100\"><button value=\"Join\" action=\"bypass -h admin_dmevent_join\" width=90 height=15 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\"></td>");
		replyMSG
				.append("<td width=\"100\"><button value=\"Teleport\" action=\"bypass -h admin_dmevent_teleport\" width=90 height=15 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\"></td>");
		replyMSG
				.append("<td width=\"100\"><button value=\"Start\" action=\"bypass -h admin_dmevent_start\" width=90 height=15 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\"></td>");
		replyMSG.append("</tr></table><table><tr>");
		replyMSG
				.append("<td width=\"100\"><button value=\"Abort\" action=\"bypass -h admin_dmevent_abort\" width=90 height=15 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\"></td>");
		replyMSG
				.append("<td width=\"100\"><button value=\"Finish\" action=\"bypass -h admin_dmevent_finish\" width=90 height=15 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\"></td>");
		replyMSG
				.append("<td width=\"100\"><button value=\"Auto Event\" action=\"bypass -h admin_dmevent_autoevent\" width=90 height=15 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\"></td>");
		replyMSG.append("</tr></table><br><table><tr>");
		replyMSG
				.append("<td width=\"100\"><button value=\"Sit Force\" action=\"bypass -h admin_dmevent_sit\" width=90 height=15 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\"></td>");
		replyMSG
				.append("<td width=\"100\"><button value=\"Dump\" action=\"bypass -h admin_dmevent_dump\" width=90 height=15 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\"></td>");
		replyMSG.append("</tr></table><br><br><table><tr>");
		replyMSG
				.append("<td width=\"100\"><button value=\"Save\" action=\"bypass -h admin_dmevent_save\" width=90 height=15 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\"></td>");
		replyMSG
				.append("<td width=\"100\"><button value=\"Load\" action=\"bypass -h admin_dmevent_load\" width=90 height=15 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\"></td>");
		replyMSG.append("</tr></table><br><br>");
		replyMSG.append("Death Match Settings:<br1>");
		replyMSG.append("Name:&nbsp;<font color=\"00FF00\">" + DM._eventName + "</font><br1>");
		replyMSG.append("Description:&nbsp;<font color=\"00FF00\">" + DM._eventDesc + "</font><br1>");
		replyMSG.append("Joining location name:&nbsp;<font color=\"00FF00\">" + DM._joiningLocationName + "</font><br1>");
		replyMSG.append("Joining NPC ID:&nbsp;<font color=\"00FF00\">" + DM._npcId + " on pos " + DM._npcX + "," + DM._npcY + "," + DM._npcZ
				+ "</font><br1>");
		replyMSG.append("Reward ID:&nbsp;<font color=\"00FF00\">" + DM._rewardId + "</font><br1>");
		replyMSG.append("Reward Amount:&nbsp;<font color=\"00FF00\">" + DM._rewardAmount + "</font><br><br>");
		replyMSG.append("Min lvl:&nbsp;<font color=\"00FF00\">" + DM._minlvl + "</font><br>");
		replyMSG.append("Max lvl:&nbsp;<font color=\"00FF00\">" + DM._maxlvl + "</font><br><br>");
		replyMSG.append("Min Players:&nbsp;<font color=\"00FF00\">" + DM._minPlayers + "</font><br>");
		replyMSG.append("Max Players:&nbsp;<font color=\"00FF00\">" + DM._maxPlayers + "</font><br><br>");
		replyMSG.append("Join Time:&nbsp;<font color=\"00FF00\">" + DM._joinTime + "</font><br>");
		replyMSG.append("Event Time:&nbsp;<font color=\"00FF00\">" + DM._eventTime + "</font><br><br>");
		replyMSG.append("Death Match Color:&nbsp;<font color=\"00FF00\">" + DM._playerColors + "</font><br>");
		replyMSG.append("DM Spawn Pos:&nbsp;<font color=\"00FF00\">" + DM._playerX + "," + DM._playerY + "," + DM._playerZ + "</font><br><br>");
		replyMSG.append("DM Radius:&nbsp;<font color=\"00FF00\">" + DM._radius + "</font><br><br>");
		replyMSG.append("Current players:<br1>");

		if (!DM._started)
		{
			replyMSG.append("<br1>");
			replyMSG.append(DM._players.size() + " players participating.");
			replyMSG.append("<br><br>");
		}
		else if (DM._started)
		{
			replyMSG.append("<br1>");
			replyMSG.append(DM._players.size() + " players are in fighting event.");
			replyMSG.append("<br><br>");
		}

		replyMSG.append("</body></html>");
		adminReply.setHtml(replyMSG.toString());
		activeChar.sendPacket(adminReply);
	}
}