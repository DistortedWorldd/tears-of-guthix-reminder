package com.tearsofguthixreminder;

import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;

@ConfigGroup("tearsofguthixreminder")
public interface TearsOfGuthixReminderConfig extends Config
{
	@ConfigItem(
		keyName = "enableNotifications",
		name = "Enable Notifications",
		description = "Show a notification when Tears of Guthix is available"
	)
	default boolean enableNotifications()
	{
		return true;
	}

	@ConfigItem(
		keyName = "showOverlay",
		name = "Show Overlay",
		description = "Display an overlay showing time until next reset"
	)
	default boolean showOverlay()
	{
		return true;
	}

	@ConfigItem(
		keyName = "markCompleted",
		name = "Mark as Completed",
		description = "Click to mark Tears of Guthix as completed this week"
	)
	default boolean markCompleted()
	{
		return false;
	}

	@ConfigItem(
		keyName = "resetCompletion",
		name = "Reset Completion",
		description = "Click to reset completion status (marks as not completed)"
	)
	default boolean resetCompletion()
	{
		return false;
	}
}
