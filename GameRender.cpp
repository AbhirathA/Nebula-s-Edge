#include "GameRender.h"

#define DISPLAY_SCALE 30

void GameRender::initVariables() {
    this->videoMode.height = 780;
    this->videoMode.width = 880;

    manager = new Manager(0, 0, 0, this->videoMode.width * DISPLAY_SCALE, 0, (-1) * (this->videoMode.height) * DISPLAY_SCALE, 1);
    manager->dropP(300, -300, 200, 5, 0, 2, 30, 30, 100, 0, 0, 40 * DISPLAY_SCALE, 45 * DISPLAY_SCALE, 100);
    //manager->drop1(100 * DISPLAY_SCALE, -10 * DISPLAY_SCALE, 20, 0, 0, 0, 0, 40 * DISPLAY_SCALE, 45 * DISPLAY_SCALE, 100);
    //manager->drop2(100 * DISPLAY_SCALE, -600 * DISPLAY_SCALE, -10, 30, 0, 0, 40 * DISPLAY_SCALE, 45 * DISPLAY_SCALE, 100);
    //manager->drop2(100 * DISPLAY_SCALE, -100 * DISPLAY_SCALE, 20, 90, 0, 0, 40 * DISPLAY_SCALE, 45 * DISPLAY_SCALE, 100);
}

void GameRender::initWindow() {
	this->window = new sf::RenderWindow(this->videoMode, "Dots moving", sf::Style::Close | sf::Style::Resize | sf::Style::Titlebar);
}

void GameRender::initObj(int id, double posX, double posY, double angle) {
    if (id == -1) {
        sf::RectangleShape* temp = new sf::RectangleShape();
        temp->setPosition(sf::Vector2f(posX / DISPLAY_SCALE, posY / DISPLAY_SCALE));
        temp->setSize(sf::Vector2f(50.f, 50.f));
        temp->setFillColor(sf::Color::Red);
        temp->setOutlineColor(sf::Color::Blue);
        temp->setOutlineThickness(2.f);
        temp->rotate(angle);
        this->shapeList[id] = temp;
    }
    sf::CircleShape* temp = nullptr;
    if ((this->shapeList).find(id) != (this->shapeList).end()) {
        (this->shapeList)[id]->setPosition(sf::Vector2f(posX/DISPLAY_SCALE, posY/DISPLAY_SCALE));
    }
    temp = new sf::CircleShape(40);
    temp->setPosition(sf::Vector2f(posX/DISPLAY_SCALE, posY/DISPLAY_SCALE));
    temp->setFillColor(sf::Color::Green);
    temp->setOutlineColor(sf::Color::Blue);
    temp->setOutlineThickness(1);
    this->shapeList[id] = temp;
}

void GameRender::initEnemy(int id, double posX, double posY){
    sf::RectangleShape* temp = nullptr;
    if ((this->shapeList).find(id) != (this->shapeList).end()) {
        (this->shapeList)[id]->setPosition(sf::Vector2f(posX/DISPLAY_SCALE, posY/ DISPLAY_SCALE));
    }
    temp = new sf::RectangleShape();
    temp->setPosition(sf::Vector2f(posX/DISPLAY_SCALE, posY/DISPLAY_SCALE));
    temp->setSize(sf::Vector2f(50.f, 50.f));
    temp->setFillColor(sf::Color::Red);
    temp->setOutlineColor(sf::Color::Blue);
    temp->setOutlineThickness(2.f);
    this->shapeList[id] = temp;
}

void GameRender::deleteShape(int id)
{
    delete (this->shapeList)[id];
    this->shapeList[id] = nullptr;
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
    this->manager->update();
}

void GameRender::render(){
    // Displays all objects
    this->window->clear(); //clear the frame
    std::map<int, std::pair<int, int>> temp = this->manager->display();
    //std::cout << "size: " << temp.size() << std::endl;
    for (auto i : temp) {
        //std::cout << i.first << " " << (i.second).first << " " << (-1) * (i.second).second << std::endl;
        this->initObj(i.first, (i.second).first, (-1)*(i.second).second, manager->angle());
    }
    for (auto i : this->shapeList) {
        this->window->draw(*(i.second));
    }
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
            if (ev.key.code == sf::Keyboard::RShift) { // 
                this->manager->thrust();
            }
            else if (ev.key.code == sf::Keyboard::W) { // 
                std::cout << "\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\n";
                this->manager->up();
            }
            else if (ev.key.code == sf::Keyboard::A) {
                this->manager->left();
            }
            else if(ev.key.code == sf::Keyboard::D){
                this->manager->right();
            }
            else if(ev.key.code == sf::Keyboard::S) {
                this->manager->stop();
            }
            break;
        }
    }
}
