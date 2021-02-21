import java.time.Instant;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class CommandListener extends ListenerAdapter
{
	public void onGuildMessageReceived(GuildMessageReceivedEvent e)
	{
		if((e.getMessage().getMentionedRoles().contains(e.getGuild().getRoleById("565626094917648386")) //discord mods ping
				|| e.getMessage().getMentionedRoles().contains(e.getGuild().getRoleById("602889336748507164"))) //discord admins ping
				&& !isStaff(e.getMember())) //sent by a non-mod/admin
		{
			EmbedBuilder eb = new EmbedBuilder()
					.setTitle("Ping in #" + e.getChannel().getName())
					.addField("User: ", e.getAuthor().getAsTag() + " (" + e.getAuthor().getId() + ")", false)
					.addField("Go to: ", e.getMessage().getJumpUrl(), false)
					.setDescription(e.getMessage().getContentDisplay())
					.setColor(65280)
					.setTimestamp(Instant.now());
			e.getGuild().getTextChannelById(Main.ANNOUNCE_CHANNEL).sendMessage(eb.build()).queue(); //make an announcement
			return;
		}
		
		if(isStaff(e.getMember()))
		{
			String msg = e.getMessage().getContentRaw();
			String[] args = msg.split(" ");
			
			if(args[0].equalsIgnoreCase(Main.PREFIX + "disable") && args[1].equalsIgnoreCase("pingannouncements"))
			{
				e.getChannel().sendMessage("*Disabling ping announcements...*").complete();
				e.getJDA().shutdown();
				System.exit(0);
			}
		}
	}
	
	//return true if a member has discord mod, admin or is owner
	public static boolean isStaff(Member m)
	{
		try
		{
			//if owner
			if(m.isOwner())
				return true;
		}
		catch(NullPointerException e)
		{
			//no error message reee its pissing me off
		}
		
		//if admin
		if(m.hasPermission(Permission.ADMINISTRATOR))
			return true;
		
		//if discord mod
		switch(m.getGuild().getId())
		{
			case "565623426501443584" : //wilbur's discord
				for(Role r : m.getRoles())
				{
					if(r.getId().equals("565626094917648386")) //wilbur discord mod
						return true;
				}
				break;
				
			case "640254333807755304" : //charlie's server
				for(Role r : m.getRoles())
				{
					if(r.getId().equals("640255355401535499")) //charlie discord mod
						return true;
				}
				break;
		}
		
		return false;
	}
}
