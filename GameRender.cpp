#include "GameRender.h"

void GameRender::initVariables() {
	window = nullptr;
}

void GameRender::initWindow() {
	this->videoMode.height = 1080;
	this->videoMode.width = 1080;
	this->window = new sf::RenderWindow(sf::VideoMode(800, 600), "Dots moving", sf::Style::Close | sf::Style::Resize | sf::Style::Titlebar);
}

GameRender::GameRender(){
	this->initVariables();
	this->initWindow();
}

GameRender::~GameRender(){
	delete this->window;
}

bool GameRender::isRunning(){
	return this->window->isOpen();
}

void GameRender::update(){
    this->pollEvents();
}

void GameRender::render(){
    // Displays all objects
    this->window->clear(); //clear the frame
    this->window->display(); // redraw the frame
}

void GameRender::pollEvents(){
    //Event polling
    while (this->window->pollEvent(this->ev)) { // Keep checking for btn click
        switch ((this->ev).type) {
        case sf::Event::Closed:
            this->window->close(); // Close window if we hit X
            break;

        case sf::Event::KeyPressed:
            if (ev.key.code == sf::Keyboard::Space) { // Test space bar working
                std::cout << 10 << std::endl;
            }
            else if (ev.key.code == sf::Keyboard::Escape) { // Close window if we hit X
                this->window->close();
            }
            break;
        }
    }
}
