package huff.lib.various;

import huff.lib.manager.MessageManager;
import huff.lib.various.structures.MessagePair;

public class LibMessage
{
	private LibMessage() { }
	
	public static final MessagePair PREFIX_GENERAL = new MessagePair("lib.prefix.general", "§8☰ §aHufferlinge §8☷§7 ");
	public static final MessagePair PREFIX_CONSOLE = new MessagePair("lib.prefix.console", "§7Hufferlinge ☷ ");
	public static final MessagePair PREFIX_SUPPORT = new MessagePair("lib.prefix.support", "§8☰ §eSupport §8☷§7 ");
	public static final MessagePair PREFIX_DELAYMESSAGE = new MessagePair("lib.prefix.delaymessage", "§8☰ §bFrom Past §8☷§7 ");
	
	public static final MessagePair TITLE_CONSOLE = new MessagePair("lib.title.console", "§lTerminal§r");	
	public static final MessagePair TITLE_PLAYERCHOOSER = new MessagePair("lib.title.playerchooser", "§7» §9Personenauswahl");
	
	public static final MessagePair NOPERMISSION = new MessagePair("lib.general.no_permission", PREFIX_GENERAL.getKeyLink() + "Du hast keine Berechtigung für diesen Befehl.");
	public static final MessagePair NOTFOUND = new MessagePair("lib.general.not_found", PREFIX_GENERAL.getKeyLink() + "Der Spieler §9%user% §7konnte nicht zugeordnet werden.");
	public static final MessagePair WRONGINPUT = new MessagePair("lib.general.wrong_input", PREFIX_GENERAL.getKeyLink() + "Die Eingabe ist ungültig. %text%");
	public static final MessagePair NOTINCONSOLE = new MessagePair("lib.general.not_in_console", PREFIX_GENERAL.getKeyLink() + "Der Befehl kann nicht in der Konsole ausgeführt werden.");
	
	public static final MessagePair SUPPORT_USER_HELP = new MessagePair("lib.support.user.help", WRONGINPUT.getKeyLink() + "/support [leave] - §9\"/support\"§7 ohne Zusatz öffnet einen neuen Support-Kanal.");
	public static final MessagePair SUPPORT_USER_CREATE = new MessagePair("lib.support.user.create", PREFIX_SUPPORT.getKeyLink() + "Du hast einen Support-Kanal geöffnet. Alle Nachrichten werden, ohne erneute Befehlseingabe, hierher geschickt.\n" +  
	                                                                                                 PREFIX_SUPPORT.getKeyLink() + "Um den Support-Kanal zu verlassen nutze§9 \"/support leave\"§7.");
	public static final MessagePair SUPPORT_USER_ALREADYOPEN = new MessagePair("lib.support.user.already_open", PREFIX_SUPPORT.getKeyLink() + "Du hast schon einen Support-Kanal geöffnet. Um den Support-Kanal zu schließen nutze §9\"/support leave\"§7.");
	public static final MessagePair SUPPORT_USER_CANTLEAVE = new MessagePair("lib.support.user.cant_leave", PREFIX_SUPPORT.getKeyLink() + "Du bist in keinem Support-Kanal. Um dir einen zu Eröffnen nutze §9\"/support\"§7.");
	public static final MessagePair SUPPORT_USER_USERDELETE = new MessagePair("lib.support.user.user_delete", PREFIX_SUPPORT.getKeyLink() + "Du hast den Support-Kanal geschlossen.");
	public static final MessagePair SUPPORT_USER_TEAMDELETE = new MessagePair("lib.support.user.team_delete", PREFIX_SUPPORT.getKeyLink() + "Dein Support-Kanal wurde entfernt. Bei Rückfragen wende dich bitte per §9\"/kontakt\"§7 an ein Teammitglied.");
	
	public static final MessagePair SUPPORT_TEAM_HELP = new MessagePair("lib.support.team.help", WRONGINPUT.getKeyLink() + "/support [list|leave|enter <Name>|delete <Name>].");
	public static final MessagePair SUPPORT_TEAM_CREATE = new MessagePair("lib.support.team.create", PREFIX_SUPPORT.getKeyLink() + "Du kannst als Teammitglied keinen Support-Kanal öffnen. Nutze §9\"/tell <Name>\"§7 zur Teamkommunikation.");
	public static final MessagePair SUPPORT_TEAM_USERCREATE = new MessagePair("lib.support.team.user_create", PREFIX_SUPPORT.getKeyLink() + "§9%user%§7 hat einen Support-Kanal geöffnet.");
	public static final MessagePair SUPPORT_TEAM_ENTER = new MessagePair("lib.support.team.enter", PREFIX_SUPPORT.getKeyLink() + "Du hast den Support-Kanal von §9%user%§7 betreten.");
	public static final MessagePair SUPPORT_TEAM_ENTEROTHER = new MessagePair("lib.support.team.enter_other", PREFIX_SUPPORT.getKeyLink() + "Das Teammitglied §9%team%§7 hat den Support-Kanal von §9%user%§7 betreten.");
	public static final MessagePair SUPPORT_TEAM_NOTFOUND = new MessagePair("lib.support.team.not_found", PREFIX_SUPPORT.getKeyLink() + "Support-Kanal nicht gefunden. Um dir die Vorhandenen anzuschauen nutze §9\"/support list\"§7.");
	public static final MessagePair SUPPORT_TEAM_ALREADYENTERED = new MessagePair("lib.support.team.already_entered", PREFIX_SUPPORT.getKeyLink() + "Du bist noch im Support-Chat von §9%user%§7.");
	public static final MessagePair SUPPORT_TEAM_LEAVE = new MessagePair("lib.support.team.leave", PREFIX_SUPPORT.getKeyLink() + "Du hast den Support-Kanal von §9%user%§7 verlassen.");
	public static final MessagePair SUPPORT_TEAM_CANTLEAVE = new MessagePair("lib.support.team.cant_leave", PREFIX_SUPPORT.getKeyLink() + "Du bist in keinem Support-Kanal. Um dir die Vorhandenen anzuschauen nutze §9\"/support list\"§7.");
	public static final MessagePair SUPPORT_TEAM_TEAMDELETE = new MessagePair("lib.support.team.team_delete", PREFIX_SUPPORT.getKeyLink() + "Du hast den Support-Kanal von §9%user%§7 entfernt.");
	
	public static final MessagePair SUPPORT_CHANNEL_USERPREFIX = new MessagePair("lib.support.channel.user_prefix", PREFIX_SUPPORT.getKeyLink() + "§9%team%§8 » §7%text%");
	public static final MessagePair SUPPORT_CHANNEL_TEAMPREFIX = new MessagePair("lib.support.channel.team_prefix", PREFIX_SUPPORT.getKeyLink() + "§7%user%§8 » §7%text%");
	public static final MessagePair SUPPORT_CHANNEL_TEAMENTER = new MessagePair("lib.support.channel.team_enter", PREFIX_SUPPORT.getKeyLink() + "Das Teammitglied §9%team%§7 hat den Support-Kanal betreten.");
	public static final MessagePair SUPPORT_CHANNEL_TEAMLEAVE = new MessagePair("lib.support.channel.team_leave", PREFIX_SUPPORT.getKeyLink() + "Das Teammitglied §9%team%§7 hat den Support-Kanal verlassen.");
	public static final MessagePair SUPPORT_CHANNEL_TEAMDELETE = new MessagePair("lib.support.channel.team_delete", PREFIX_SUPPORT.getKeyLink() + "Das Teammitglied §9%team%§7 hat den Support-Kanal von §9%user%§7 entfernt.");
	public static final MessagePair SUPPORT_CHANNEL_USERDELETE = new MessagePair("lib.support.channel.user_delete", PREFIX_SUPPORT.getKeyLink() + "§9%user%§7 hat den Support-Kanal geschlossen.");
	public static final MessagePair SUPPORT_CHANNEL_USERDISCONNECT = new MessagePair("lib.support.channel.user_disconnect", PREFIX_SUPPORT.getKeyLink() + "§9%user%§7 hat den Server verlassen. Der Support-Kanal wurde geschlossen.");
	
	public static final MessagePair DELAYMESSAGE_SAVED = new MessagePair("lib.delaymessage.saved", PREFIX_GENERAL.getKeyLink() + "Nachricht wurde zum Versand gespeichert.");
	public static final MessagePair DELAYMESSAGE_INVALIDTYPE = new MessagePair("lib.delaymessage.invalid_type", PREFIX_GENERAL.getKeyLink() + "Die Benachrichtigungs-Art ist ungültig. Mögliche Werte §9\"%types%\"§7.");
	
	public static final MessagePair TELL_TOPREFIX = new MessagePair("lib.tell.to_prefix", "§8☰ §dTell §8| §7An §9%user%§8 » §7%text%");
	public static final MessagePair TELL_FROMPREFIX = new MessagePair("lib.tell.from_prefix", "§8☰ §dTell §8| §7Von §9%user%§8 » §7%text%");
	
	public static final MessagePair VANISH_SELF_ON = new MessagePair("lib.vanish.self.on", PREFIX_GENERAL.getKeyLink() + "Du bist nun verschollen.");
	public static final MessagePair VANISH_SELF_ONREMINDER = new MessagePair("lib.vanish.self.on_reminder", PREFIX_GENERAL.getKeyLink() + "§cDu bist noch verschollen.");
	public static final MessagePair VANISH_SELF_OFF = new MessagePair("lib.vanish.self.off", PREFIX_GENERAL.getKeyLink() + "Du bist nun sichtbar.");
	public static final MessagePair VANISH_OTHER_ON = new MessagePair("lib.vanish.other.on", PREFIX_GENERAL.getKeyLink() + "Du hast den Spieler §9%user%7 verschollen.");
	public static final MessagePair VANISH_OTHER_OFF = new MessagePair("lib.vanish.other.off", PREFIX_GENERAL.getKeyLink() + "Du hast den Spieler §9%user%7 sichtbar gemacht.");
	
	public static final MessagePair CHAT_MESSAGE = new MessagePair("lib.chat.message", "§8☰§7 %world% %userprefix%%user%§8 » §7%text%");
	
	public static final MessagePair AREACHAT_GLOBALPREFIX = new MessagePair("lib.areachat.global_prefix", "§8☰ §3Botschaft §8×§7");
	public static final MessagePair AREACHAT_AREAPREFIX = new MessagePair("lib.areachat.area_prefix", "§8☰ §9Umgebung §8×§7");
	public static final MessagePair AREACHAT_MESSAGE = new MessagePair("lib.areachat.message", "%chatprefix% %userprefix%%user%§8 » §7%text%");
	public static final MessagePair AREACHAT_COOLDOWN = new MessagePair("lib.areachat.cooldown", "Du musst noch $9%time% Sekunde(n)§7 warten.");
	
	public static void init()
	{
		final HuffConfiguration config = new HuffConfiguration();
		
		config.addEmptyLine("lib");
		config.addCommentLine("lib", "+--------------------------------------+ #", false);
		config.addCommentLine("lib", "          // L I B A R Y //              #", false);
		config.addCommentLine("lib", "+--------------------------------------+ #", false);
		config.addEmptyLine("lib");
		
		config.set(PREFIX_GENERAL);
		config.set(PREFIX_CONSOLE);
		config.set(PREFIX_SUPPORT);
		config.set(PREFIX_DELAYMESSAGE);
		
		config.addCommentLine("lib.title", "Titel von Inventaren, Items oder Chats", true);
		config.set(TITLE_CONSOLE);
		config.set(TITLE_PLAYERCHOOSER);
		
		config.set(NOPERMISSION);
		config.addContextLine(NOTFOUND.getKey(), "user");
		config.set(NOTFOUND);
		config.addContextLine(WRONGINPUT.getKey(), "text");
		config.set(WRONGINPUT);
		config.set(NOTINCONSOLE);
		
		config.set(SUPPORT_USER_HELP);
		config.set(SUPPORT_USER_CREATE);
		config.set(SUPPORT_USER_ALREADYOPEN);
		config.set(SUPPORT_USER_CANTLEAVE);
		config.set(SUPPORT_USER_USERDELETE);
		config.set(SUPPORT_USER_TEAMDELETE);
		
		config.set(SUPPORT_TEAM_HELP);
		config.set(SUPPORT_TEAM_CREATE);
		config.addContextLine(SUPPORT_TEAM_USERCREATE.getKey(), "user");
		config.set(SUPPORT_TEAM_USERCREATE);
		config.addContextLine(SUPPORT_TEAM_ENTER.getKey(), "user");
		config.set(SUPPORT_TEAM_ENTER);
		config.addContextLine(SUPPORT_TEAM_ENTEROTHER.getKey(), "team", "user");
		config.set(SUPPORT_TEAM_ENTEROTHER);
		config.set(SUPPORT_TEAM_NOTFOUND);
		config.addContextLine(SUPPORT_TEAM_ALREADYENTERED.getKey(), "user");
		config.set(SUPPORT_TEAM_ALREADYENTERED);
		config.addContextLine(SUPPORT_TEAM_LEAVE.getKey(), "user");
		config.set(SUPPORT_TEAM_LEAVE);
		config.set(SUPPORT_TEAM_CANTLEAVE);
		config.addContextLine(SUPPORT_TEAM_TEAMDELETE.getKey(), "user");
		config.set(SUPPORT_TEAM_TEAMDELETE);
		
		config.addContextLine(SUPPORT_CHANNEL_USERPREFIX.getKey(), "team", "text");
		config.set(SUPPORT_CHANNEL_USERPREFIX);
		config.addContextLine(SUPPORT_CHANNEL_TEAMPREFIX.getKey(), "user", "text");
		config.set(SUPPORT_CHANNEL_TEAMPREFIX);
		config.addContextLine(SUPPORT_CHANNEL_TEAMENTER.getKey(), "team");
		config.set(SUPPORT_CHANNEL_TEAMENTER);
		config.addContextLine(SUPPORT_CHANNEL_TEAMLEAVE.getKey(), "team");
		config.set(SUPPORT_CHANNEL_TEAMLEAVE);
		config.addContextLine(SUPPORT_CHANNEL_TEAMDELETE.getKey(), "team", "user");
		config.set(SUPPORT_CHANNEL_TEAMDELETE);
		config.addContextLine(SUPPORT_CHANNEL_USERDELETE.getKey(), "user");
		config.set(SUPPORT_CHANNEL_USERDELETE);
		config.addContextLine(SUPPORT_CHANNEL_USERDISCONNECT.getKey(), "user");
		config.set(SUPPORT_CHANNEL_USERDISCONNECT);
		
		config.set(DELAYMESSAGE_SAVED);
		config.addContextLine(DELAYMESSAGE_INVALIDTYPE.getKey(), "types");
		config.set(DELAYMESSAGE_INVALIDTYPE);
		
		config.addContextLine(TELL_TOPREFIX.getKey(), "user", "text");
		config.set(TELL_TOPREFIX);
		config.addContextLine(TELL_FROMPREFIX.getKey(), "user", "text");
		config.set(TELL_FROMPREFIX);
		
		config.set(VANISH_SELF_ON);
		config.set(VANISH_SELF_ONREMINDER);
		config.set(VANISH_SELF_OFF);
		config.addContextLine(VANISH_OTHER_ON.getKey(), "user");
		
		config.set(VANISH_OTHER_ON);
		config.addContextLine(VANISH_OTHER_OFF.getKey(), "user");
		config.set(VANISH_OTHER_OFF);
		
		config.addContextLine(CHAT_MESSAGE.getKey(), "world", "userprefix", "user", "text");
		config.set(CHAT_MESSAGE);
		
		config.set(AREACHAT_GLOBALPREFIX);
		config.set(AREACHAT_AREAPREFIX);
		config.addContextLine(AREACHAT_MESSAGE.getKey(), "chatprefix", "userprefix", "user", "text");
		config.set(AREACHAT_MESSAGE);	
		config.addContextLine(AREACHAT_COOLDOWN.getKey(), "time");
		config.set(AREACHAT_COOLDOWN);
		
		MessageManager.addDefaults(config);
	}
}
