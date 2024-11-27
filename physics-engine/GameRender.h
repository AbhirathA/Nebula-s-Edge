#pragma once
#include <SFML/Graphics.hpp>
#include <SFML/Audio.hpp>
#include <SFML/Window.hpp>
#include <SFML/System.hpp>
#include <SFML/Network.hpp>
#include "Manager.h"
#include <map>
#include <vector>

// Rendering stuff

// Can be removed once Java GUI has come into play.


class GameRender{
	private:
		//Window
		sf::RenderWindow* window = nullptr;
		sf::Event ev;
		sf::VideoMode videoMode;

		//ObjectList
		std::map<int, sf::Shape*> shapeList = {};

		//Manager
		Manager* manager = nullptr;
		
		// Private functions
		void initVariables();
		void initWindow();
		void initObj(int id, double posX, double posY);
		void initEnemy(int id, double posX, double posY);
		void deleteShape(int id);

	public:

		GameRender();
		virtual ~GameRender();

		// Functions
		bool isRunning();
		void update();
		void render();
		void pollEvents();
};

