#pragma once
#include "AngleObj.h"
#include "Lifetime.h"

class CtrledObj : public AngleObj {
	Lifetime* thruster =  nullptr;
	public:
		CtrledObj():AngleObj(){
		}
};

