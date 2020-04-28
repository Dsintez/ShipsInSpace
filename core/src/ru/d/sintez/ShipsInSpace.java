package ru.d.sintez;

import com.badlogic.gdx.Game;
import ru.d.sintez.screen.MenuScreen;

public class ShipsInSpace extends Game {

	@Override
	public void create () {
		setScreen(new MenuScreen());
	}
}
