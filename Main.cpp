#include "Obj.h"
#include "Manager.h"
#include <iostream>
#include "GameRender.h"

#define select 0
#if select
int main() {
	//Manager m;
	//m.drop(1,-1,0,0,0,0,0,1,1,1);
	std::cout << 0 << "  ----------------------------------------------------------" << std::endl;
	m.display();
	int i = 0;
	while (i++ < 30) {
		std::cout << i << "  ----------------------------------------------------------" << std::endl;
		m.update();
		m.display();
		std::cout << i << "  ----------------------------------------------------------" << std::endl;
	}
	return 0;
}
#endif
#if 1-select
int main()
{
    //Game Rendering
    GameRender game = GameRender();
    int l = 0;
    int t = 0;
    while (game.isRunning()) {

        game.update(); // generate the next frame in GUI form
        game.render(); // display it to user

    }
    return 0;
}
#endif