#include "GameRender.h"

void GameRender::initVariables() {
	window = nullptr;
}

void GameRender::initWindow() {
	this->videoMode.height = 1080;
	this->videoMode.width = 1080;
	this->window = new sf::RenderWindow(sf::VideoMode(800, 600), "Dots moving", sf::Style::Close | sf::Style::Resize | sf::Style::Titlebar);
}

void GameRender::initObjects() {
    this->o.setPosition(100, 0);
    this->o.setSize(sf::Vector2f(100.f, 100.f));
    this->o.setFillColor(sf::Color::Green);
    this->o.setOutlineColor(sf::Color::Blue);
    this->o.setOutlineThickness(2.f);
}

void GameRender::initEnemies(){
    this->enemy.setPosition(0,0);
    this->enemy.setSize(sf::Vector2f(100.f, 100.f));
    this->enemy.setFillColor(sf::Color::Red);
    this->enemy.setOutlineColor(sf::Color::Blue);
    this->enemy.setOutlineThickness(2.f);
}

GameRender::GameRender(){
	this->initVariables();
	this->initWindow();
    this->initEnemies();
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
    this->window->draw(this->enemy);
    this->window->draw(this->o);
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
                this->initObjects();
            }
            else if (ev.key.code == sf::Keyboard::Escape) { // Close window if we hit X
                this->window->close();
            }
            break;
        }
    }
}
