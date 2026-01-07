package com.tearsofguthixreminder;

import com.google.inject.Provides;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Client;
import net.runelite.api.GameState;
import net.runelite.api.events.GameTick;
import net.runelite.client.Notifier;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.ui.overlay.OverlayManager;

import javax.inject.Inject;
import java.time.*;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAdjusters;

@Slf4j
@PluginDescriptor(
	name = "Tears of Guthix Reminder",
	description = "Reminds you to complete Tears of Guthix minigame",
	tags = {"tears", "guthix", "minigame", "reminder", "weekly"}
)
public class TearsOfGuthixReminderPlugin extends Plugin
{
	@Inject
	private Client client;

	@Inject
	private TearsOfGuthixReminderConfig config;

	@Inject
	private Notifier notifier;

	@Inject
	private OverlayManager overlayManager;

	@Inject
	private TearsOfGuthixReminderOverlay overlay;

	@Inject
	private ConfigManager configManager;

	private static final String CONFIG_GROUP = "tearsofguthixreminder";
	private static final String CONFIG_LAST_COMPLETED = "lastCompleted";
	private static final int CHECK_INTERVAL_TICKS = 50; // Check every 30 seconds (50 ticks)
	
	private int tickCounter = 0;
	private boolean hasNotifiedThisSession = false;

	@Override
	protected void startUp() throws Exception
	{
		log.info("Tears of Guthix Reminder started!");
		overlayManager.add(overlay);
		hasNotifiedThisSession = false;
	}

	@Override
	protected void shutDown() throws Exception
	{
		log.info("Tears of Guthix Reminder stopped!");
		overlayManager.remove(overlay);
	}

	@Subscribe
	public void onGameTick(GameTick tick)
	{
		if (client.getGameState() != GameState.LOGGED_IN)
		{
			return;
		}

		tickCounter++;
		if (tickCounter >= CHECK_INTERVAL_TICKS)
		{
			tickCounter = 0;
			checkAndNotify();
		}
	}

	private void checkAndNotify()
	{
		if (!config.enableNotifications() || hasNotifiedThisSession)
		{
			return;
		}

		if (isTearsOfGuthixAvailable())
		{
			notifier.notify("Tears of Guthix is available! Don't forget to complete it.");
			hasNotifiedThisSession = true;
		}
	}

	public boolean isTearsOfGuthixAvailable()
	{
		String lastCompletedStr = configManager.getConfiguration(CONFIG_GROUP, CONFIG_LAST_COMPLETED);
		
		if (lastCompletedStr == null || lastCompletedStr.isEmpty())
		{
			// Never completed before, so it's available
			return true;
		}

		try
		{
			Instant lastCompleted = Instant.parse(lastCompletedStr);
			Instant nextReset = getNextWednesdayReset(lastCompleted);
			Instant now = Instant.now();

			return now.isAfter(nextReset);
		}
		catch (Exception e)
		{
			log.error("Error parsing last completed date", e);
			return true;
		}
	}

	public void markAsCompleted()
	{
		Instant now = Instant.now();
		configManager.setConfiguration(CONFIG_GROUP, CONFIG_LAST_COMPLETED, now.toString());
		hasNotifiedThisSession = false;
		log.info("Tears of Guthix marked as completed at {}", now);
	}

	public void resetCompletion()
	{
		configManager.unsetConfiguration(CONFIG_GROUP, CONFIG_LAST_COMPLETED);
		hasNotifiedThisSession = false;
		log.info("Tears of Guthix completion reset");
	}

	public Instant getNextResetTime()
	{
		String lastCompletedStr = configManager.getConfiguration(CONFIG_GROUP, CONFIG_LAST_COMPLETED);
		
		if (lastCompletedStr == null || lastCompletedStr.isEmpty())
		{
			// If never completed, return the next Wednesday
			return getNextWednesdayReset(Instant.now());
		}

		try
		{
			Instant lastCompleted = Instant.parse(lastCompletedStr);
			return getNextWednesdayReset(lastCompleted);
		}
		catch (Exception e)
		{
			return getNextWednesdayReset(Instant.now());
		}
	}

	private Instant getNextWednesdayReset(Instant fromTime)
	{
		ZonedDateTime zdt = fromTime.atZone(ZoneOffset.UTC);
		
		// Get next Wednesday at 00:00 UTC
		ZonedDateTime nextWednesday = zdt.with(TemporalAdjusters.next(DayOfWeek.WEDNESDAY))
			.withHour(0)
			.withMinute(0)
			.withSecond(0)
			.withNano(0);

		// If we're currently on a Wednesday before midnight, next reset is next week
		if (zdt.getDayOfWeek() == DayOfWeek.WEDNESDAY)
		{
			nextWednesday = nextWednesday.plusWeeks(1);
		}

		return nextWednesday.toInstant();
	}

	public String getTimeUntilReset()
	{
		if (isTearsOfGuthixAvailable())
		{
			return "Available now!";
		}

		Instant nextReset = getNextResetTime();
		Instant now = Instant.now();
		
		long totalSeconds = ChronoUnit.SECONDS.between(now, nextReset);
		
		long days = totalSeconds / 86400;
		long hours = (totalSeconds % 86400) / 3600;
		long minutes = (totalSeconds % 3600) / 60;

		if (days > 0)
		{
			return String.format("%dd %dh %dm", days, hours, minutes);
		}
		else if (hours > 0)
		{
			return String.format("%dh %dm", hours, minutes);
		}
		else
		{
			return String.format("%dm", minutes);
		}
	}

	@Provides
	TearsOfGuthixReminderConfig provideConfig(ConfigManager configManager)
	{
		return configManager.getConfig(TearsOfGuthixReminderConfig.class);
	}
}
