package ru.d.sintez.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import ru.d.sintez.ShipsInSpace;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.width = 400;
		config.height = 600;
		config.resizable = false;
		new LwjglApplication(new ShipsInSpace(), config);
	}
}
