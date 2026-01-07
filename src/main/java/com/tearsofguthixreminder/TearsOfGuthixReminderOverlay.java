package com.tearsofguthixreminder;

import net.runelite.api.Client;
import net.runelite.client.ui.overlay.Overlay;
import net.runelite.client.ui.overlay.OverlayLayer;
import net.runelite.client.ui.overlay.OverlayPosition;
import net.runelite.client.ui.overlay.components.LineComponent;
import net.runelite.client.ui.overlay.components.PanelComponent;

import javax.inject.Inject;
import java.awt.*;

public class TearsOfGuthixReminderOverlay extends Overlay
{
	private final Client client;
	private final TearsOfGuthixReminderPlugin plugin;
	private final TearsOfGuthixReminderConfig config;
	private final PanelComponent panelComponent = new PanelComponent();

	@Inject
	private TearsOfGuthixReminderOverlay(Client client, TearsOfGuthixReminderPlugin plugin, TearsOfGuthixReminderConfig config)
	{
		this.client = client;
		this.plugin = plugin;
		this.config = config;
		setPosition(OverlayPosition.TOP_LEFT);
		setLayer(OverlayLayer.ABOVE_SCENE);
	}

	@Override
	public Dimension render(Graphics2D graphics)
	{
		if (!config.showOverlay())
		{
			return null;
		}

		panelComponent.getChildren().clear();

		boolean isAvailable = plugin.isTearsOfGuthixAvailable();
		String timeText = plugin.getTimeUntilReset();

		panelComponent.getChildren().add(LineComponent.builder()
			.left("Tears of Guthix:")
			.right(timeText)
			.rightColor(isAvailable ? Color.GREEN : Color.WHITE)
			.build());

		return panelComponent.render(graphics);
	}
}
