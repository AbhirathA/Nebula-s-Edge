//
// Created by ibrahim on 21/11/24.
//

#include "Lifetime.h"

void Lifetime::incrementAge() {
    age++;
    if(age>=maxLife) {
        onExpire();
    }
}
