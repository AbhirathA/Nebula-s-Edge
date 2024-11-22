#pragma once
#include <SFML/Graphics.hpp>
#include <SFML/Audio.hpp>
#include <SFML/Window.hpp>
#include <SFML/System.hpp>
#include <SFML/Network.hpp>
#include "Manager.h"

// Rendering stuff

// Can be removed once Java GUI has come into play.


class GameRender{
	private:
		//Window
		sf::RenderWindow* window;
		sf::Event ev;
		sf::VideoMode videoMode;

		// Private functions
		void initVariables();
		void initWindow();

	public:

		GameRender();
		virtual ~GameRender();

		// Functions
		bool isRunning();
		void update();
		void render();
		void pollEvents();
};

