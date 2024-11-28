# Detect JAVA_HOME (commented default setting for macOS, replace if needed)
# JAVA_HOME ?= $(shell /usr/libexec/java_home)

CXX = g++
CXXFLAGS = -g -std=c++17 -fPIC \
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

# Default target: compile native library and JAR
all: clean $(LIB_DIR)/$(LIB_NAME) $(LOCAL_LIB_DIR)/$(LIB_NAME) $(LIB_DIR)/$(JAR_NAME) $(LOCAL_LIB_DIR)/$(JAR_NAME)

# Generate JNI header files
jni: src/main/java/com/physics/Manager.java
	mkdir -p $(CLASS_BUILD_DIR)
	mkdir -p $(INCLUDE_DIR)
	javac -h $(INCLUDE_DIR) -d $(CLASS_BUILD_DIR) $^

# Compile native code to object files
$(NATIVE_BUILD_DIR)/%.o: src/main/native/%.cpp
	mkdir -p $(NATIVE_BUILD_DIR)
	$(CXX) $(CXXFLAGS) -c $< -o $@

# Link object files into a shared library
$(LIB_DIR)/$(LIB_NAME): jni $(NATIVE_OBJECTS)
	mkdir -p $(LIB_DIR)
	$(CXX) $(LDFLAGS) -o $(LIB_DIR)/$(LIB_NAME) $(NATIVE_OBJECTS)

# Copy shared library to the local lib directory
$(LOCAL_LIB_DIR)/$(LIB_NAME): $(LIB_DIR)/$(LIB_NAME)
	mkdir -p $(LOCAL_LIB_DIR)
	cp $(LIB_DIR)/$(LIB_NAME) $(LOCAL_LIB_DIR)/$(LIB_NAME)

# Create a JAR file from compiled Java classes
$(LIB_DIR)/$(JAR_NAME): jni
	mkdir -p $(LIB_DIR)
	jar cf $(LIB_DIR)/$(JAR_NAME) -C $(CLASS_BUILD_DIR) .

# Copy JAR file to the local lib directory
$(LOCAL_LIB_DIR)/$(JAR_NAME): $(LIB_DIR)/$(JAR_NAME)
	mkdir -p $(LOCAL_LIB_DIR)
	cp $(LIB_DIR)/$(JAR_NAME) $(LOCAL_LIB_DIR)/$(JAR_NAME)

# Clean up all build artifacts
clean:
	rm -rf $(BUILD_DIR) $(LIB_DIR) $(INCLUDE_DIR) $(LOCAL_LIB_DIR)

# Test the native library and JAR file
test: all
	cd test && javac -cp .:../build/classes TestPhysicsEngine.java
	cd test && java -Djava.library.path=../$(LIB_DIR) -cp .:../build/classes:. TestPhysicsEngine

.PHONY: all jni clean test

