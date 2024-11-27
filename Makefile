JAVA_HOME ?= $(shell /usr/libexec/java_home)

CXX = g++
CXXFLAGS = -std=c++17 -fPIC \
           -I$(JAVA_HOME)/include \
           -I$(JAVA_HOME)/include/darwin \
           -I$(JAVA_HOME)/include/linux \
           -Iinclude \
           -Isrc/main/native
LDFLAGS = -shared

LIB_DIR = ./server/src/main/java/org/spaceinvaders/gameEngine/libs
BUILD_DIR = build
NATIVE_BUILD_DIR = $(BUILD_DIR)/native
CLASS_BUILD_DIR = $(BUILD_DIR)/classes
INCLUDE_DIR = include
LOCAL_LIB_DIR = lib

ifeq ($(OS),Windows_NT)
    LIB_NAME = PhysicsEngine.dll
else
    UNAME_S := $(shell uname -s)
    ifeq ($(UNAME_S),Darwin)
        LIB_NAME = libPhysicsEngine.dylib
    else
        LIB_NAME = libPhysicsEngine.so
    endif
endif

NATIVE_SOURCES = $(wildcard src/main/native/*.cpp)
NATIVE_OBJECTS = $(patsubst src/main/native/%.cpp,$(NATIVE_BUILD_DIR)/%.o,$(NATIVE_SOURCES))

all: $(LIB_DIR)/$(LIB_NAME) $(LOCAL_LIB_DIR)/$(LIB_NAME)

jni: src/main/java/com/physics/Manager.java
	mkdir -p $(CLASS_BUILD_DIR)
	mkdir -p $(INCLUDE_DIR)
	javac -h $(INCLUDE_DIR) -d $(CLASS_BUILD_DIR) $^

$(NATIVE_BUILD_DIR)/%.o: src/main/native/%.cpp
	mkdir -p $(NATIVE_BUILD_DIR)
	$(CXX) $(CXXFLAGS) -c $< -o $@

$(LIB_DIR)/$(LIB_NAME): jni $(NATIVE_OBJECTS)
	mkdir -p $(LIB_DIR)
	mkdir -p $(LOCAL_LIB_DIR)
	$(CXX) $(LDFLAGS) -o $(LIB_DIR)/$(LIB_NAME) $(NATIVE_OBJECTS)
	cp $(LIB_DIR)/$(LIB_NAME) $(LOCAL_LIB_DIR)/$(LIB_NAME)

$(LOCAL_LIB_DIR)/$(LIB_NAME): $(LIB_DIR)/$(LIB_NAME)
	# This ensures the local lib directory also gets the library file

clean:
	rm -rf $(BUILD_DIR) $(LIB_DIR) $(INCLUDE_DIR) $(LOCAL_LIB_DIR)


test: all
	cd test && javac -cp .:../build/classes TestPhysicsEngine.java
	cd test && java -Djava.library.path=../$(LIB_DIR) -cp .:../build/classes:. TestPhysicsEngine

.PHONY: all jni clean test
