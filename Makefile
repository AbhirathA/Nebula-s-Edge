# Detect JAVA_HOME (commented default setting for macOS, replace if needed)
# JAVA_HOME ?= $(shell /usr/libexec/java_home)

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

JAR_NAME = PhysicsEngine.jar

all: clean $(LIB_DIR)/$(LIB_NAME) $(LOCAL_LIB_DIR)/$(LIB_NAME) $(LIB_DIR)/$(JAR_NAME) $(LOCAL_LIB_DIR)/$(JAR_NAME)

jni: src/main/java/com/physics/Manager.java
	mkdir -p $(CLASS_BUILD_DIR)
	mkdir -p $(INCLUDE_DIR)
	javac -h $(INCLUDE_DIR) -d $(CLASS_BUILD_DIR) $^

$(NATIVE_BUILD_DIR)/%.o: src/main/native/%.cpp
	mkdir -p $(NATIVE_BUILD_DIR)
	$(CXX) $(CXXFLAGS) -c $< -o $@

# Compile and copy shared library to LIB_DIR and LOCAL_LIB_DIR
$(LIB_DIR)/$(LIB_NAME): jni $(NATIVE_OBJECTS)
	mkdir -p $(LIB_DIR)
	$(CXX) $(LDFLAGS) -o $(LIB_DIR)/$(LIB_NAME) $(NATIVE_OBJECTS)

$(LOCAL_LIB_DIR)/$(LIB_NAME): $(LIB_DIR)/$(LIB_NAME)
	mkdir -p $(LOCAL_LIB_DIR)
	cp $(LIB_DIR)/$(LIB_NAME) $(LOCAL_LIB_DIR)/$(LIB_NAME)

# Create JAR file for Manager.java and copy it to LIB_DIR and LOCAL_LIB_DIR
$(LIB_DIR)/$(JAR_NAME): jni
	mkdir -p $(LIB_DIR)
	jar cf $(LIB_DIR)/$(JAR_NAME) -C $(CLASS_BUILD_DIR) .

$(LOCAL_LIB_DIR)/$(JAR_NAME): $(LIB_DIR)/$(JAR_NAME)
	mkdir -p $(LOCAL_LIB_DIR)
	cp $(LIB_DIR)/$(JAR_NAME) $(LOCAL_LIB_DIR)/$(JAR_NAME)

clean:
	rm -rf $(BUILD_DIR) $(LIB_DIR) $(INCLUDE_DIR) $(LOCAL_LIB_DIR)

test: all
	cd test && javac -cp .:../build/classes TestPhysicsEngine.java
	cd test && java -Djava.library.path=../$(LIB_DIR) -cp .:../build/classes:. TestPhysicsEngine

.PHONY: all jni clean test
