# JAVA_HOME ?= $(shell /usr/libexec/java_home)
CXX = g++
CXXFLAGS = -std=c++17 -fPIC \
           -I$(JAVA_HOME)/include \
           -I$(JAVA_HOME)/include/darwin \
           -I$(JAVA_HOME)/include/linux \
           -Iinclude \
           -Isrc/main/native
LDFLAGS = -shared
LIB_DIR = lib
BUILD_DIR = build
NATIVE_BUILD_DIR = $(BUILD_DIR)/native
CLASS_BUILD_DIR = $(BUILD_DIR)/classes
INCLUDE_DIR = include

ifeq ($(OS),Windows_NT)
    LIB_NAME = PhysicsEngine.dll
    SHARED_FLAG = -shared
else
    UNAME_S := $(shell uname -s)
    ifeq ($(UNAME_S),Darwin)
        LIB_NAME = libPhysicsEngine.dylib
    else
        LIB_NAME = libPhysicsEngine.so
    endif
    SHARED_FLAG = -shared
endif

NATIVE_SOURCES = $(wildcard src/main/native/*.cpp)
NATIVE_OBJECTS = $(patsubst src/main/native/%.cpp,$(NATIVE_BUILD_DIR)/%.o,$(NATIVE_SOURCES))

all: $(LIB_DIR)/$(LIB_NAME)

# javac -h $(INCLUDE_DIR) -d $(CLASS_BUILD_DIR) $^ put after mkdir -p $(INCLUDE_DIR) if you want to generate header files mkdir -p $(INCLUDE_DIR)

jni: src/main/java/com/physics/Manager.java
	mkdir -p $(CLASS_BUILD_DIR)
	mkdir -p $(INCLUDE_DIR)
	javac -h $(INCLUDE_DIR) -d $(CLASS_BUILD_DIR) $^


$(NATIVE_BUILD_DIR)/%.o: src/main/native/%.cpp
	mkdir -p $(NATIVE_BUILD_DIR)
	$(CXX) $(CXXFLAGS) -c $< -o $@

$(LIB_DIR)/$(LIB_NAME): jni $(NATIVE_OBJECTS)
	mkdir -p $(LIB_DIR)
	$(CXX) $(SHARED_FLAG) -o $@ $(NATIVE_OBJECTS)

clean:
	rm -rf $(BUILD_DIR) $(LIB_DIR) $(INCLUDE_DIR)

test: all
	cd test && javac -cp .:../build/classes TestPhysicsEngine.java
	cd test && java -Djava.library.path=../$(LIB_DIR) -cp .:../build/classes:. TestPhysicsEngine


.PHONY: all jni clean test
